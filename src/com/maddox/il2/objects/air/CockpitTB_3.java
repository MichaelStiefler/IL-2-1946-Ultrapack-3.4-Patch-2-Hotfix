package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitTB_3 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitTB_3.this.fm != null) {
                if (CockpitTB_3.this.bNeedSetUp) {
                    CockpitTB_3.this.reflectPlaneMats();
                    CockpitTB_3.this.bNeedSetUp = false;
                }
                CockpitTB_3.this.setTmp = CockpitTB_3.this.setOld;
                CockpitTB_3.this.setOld = CockpitTB_3.this.setNew;
                CockpitTB_3.this.setNew = CockpitTB_3.this.setTmp;
                for (int i = 0; i < 4; i++) {
                    CockpitTB_3.this.setNew.throttle[i] = ((10F * CockpitTB_3.this.setOld.throttle[i]) + CockpitTB_3.this.fm.EI.engines[i].getControlThrottle()) / 11F;
                }

                CockpitTB_3.this.setNew.altimeter = CockpitTB_3.this.fm.getAltitude();
                if (CockpitTB_3.this.useRealisticNavigationInstruments()) {
                    CockpitTB_3.this.setNew.waypointAzimuth.setDeg(CockpitTB_3.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitTB_3.this.getBeaconDirection());
                } else {
                    CockpitTB_3.this.setNew.waypointAzimuth.setDeg(CockpitTB_3.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitTB_3.this.waypointAzimuth() - CockpitTB_3.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                }
                if (Math.abs(CockpitTB_3.this.fm.Or.getKren()) < 30F) {
                    CockpitTB_3.this.setNew.azimuth.setDeg(CockpitTB_3.this.setOld.azimuth.getDeg(1.0F), CockpitTB_3.this.fm.Or.azimut());
                }
                CockpitTB_3.this.setNew.vspeed = ((199F * CockpitTB_3.this.setOld.vspeed) + CockpitTB_3.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle[] = { 0.0F, 0.0F, 0.0F, 0.0F };
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.waypointAzimuth = new AnglesFork();
            this.azimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitTB_3() {
        super("3DO/Cockpit/TB-3/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(3.67495D, 0.72745D, 1.04095D));
        this.light2 = new LightPointActor(new LightPoint(), new Point3d(3.67495D, -0.77925D, 1.04095D));
        this.light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "Bombgauges", "Gauge02", "Gauge03", "Instr01", "Instr01_dd", "Instr02", "Instr02_dd", "oxigen" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_AroneL", 0.0F, -115F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_AroneR", 0.0F, -115F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -25F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 25F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -25F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 25F * this.fm.CT.getRudder(), 0.0F);
        for (int i = 0; i < 4; i++) {
            this.mesh.chunkSetAngles("Z_Throttle" + (i + 1), 0.0F, -90F * this.interp(this.setNew.throttle[i], this.setOld.throttle[i], f), 0.0F);
            this.mesh.chunkSetAngles("Z_Throtlev" + (i + 1), 0.0F, -90F * this.interp(this.setNew.throttle[i], this.setOld.throttle[i], f), 0.0F);
        }

        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter3", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter4", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 400F, 1.0F, 8F), CockpitTB_3.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 400F, 1.0F, 8F), CockpitTB_3.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizon1", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizon2", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -35F, 35F, 0.028F, -0.028F);
        this.mesh.chunkSetLocate("Z_Tangage1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_Tangage2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Variometr", 0.0F, this.cvt(this.setNew.vspeed, -40F, 40F, -180F, 180F), 0.0F);
        for (int j = 0; j < 4; j++) {
            this.mesh.chunkSetAngles("Z_RPM" + (j + 1), 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[j].getRPM(), 400F, 2400F, 2.0F, 13F), CockpitTB_3.engineRPMScale), 0.0F);
        }

        this.mesh.chunkSetAngles("Z_RPK1", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, -30F, 30F), 0.0F);
        this.mesh.chunkSetAngles("Z_RPK2", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, -30F, 30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("HullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.materialReplace("Instr01", "Instr01_dd");
            this.mesh.materialReplace("Instr01_night", "Instr01_dd_night");
            this.mesh.materialReplace("Instr02", "Instr02_dd");
            this.mesh.materialReplace("Instr02_night", "Instr02_dd_night");
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Speedometer2", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Variometr", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Altimeter3", false);
            this.mesh.chunkVisible("Z_Altimeter4", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((ActorHMesh) this.fm.actor).hierMesh().chunkVisible("Windscreen_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        ((ActorHMesh) this.fm.actor).hierMesh().chunkVisible("Windscreen_D0", true);
        super.doFocusLeave();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.98F, 0.45F);
            this.light2.light.setEmit(0.98F, 0.45F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bNeedSetUp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 52F, 91F, 139.5F, 190F, 249.5F, 308F, 360F };
    private static final float engineRPMScale[]   = { 0.0F, 0.0F, 0.0F, 40F, 80.5F, 115.3F, 145.5F, 177.6F, 206.5F, 234.5F, 261F, 287F, 320F, 358.5F };

}
