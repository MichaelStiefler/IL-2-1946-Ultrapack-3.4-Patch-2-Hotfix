package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitYAK_3R extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitYAK_3R.this.fm != null) {
                CockpitYAK_3R.this.setTmp = CockpitYAK_3R.this.setOld;
                CockpitYAK_3R.this.setOld = CockpitYAK_3R.this.setNew;
                CockpitYAK_3R.this.setNew = CockpitYAK_3R.this.setTmp;
                CockpitYAK_3R.this.setNew.throttle = ((10F * CockpitYAK_3R.this.setOld.throttle) + CockpitYAK_3R.this.fm.CT.PowerControl) / 11F;
                CockpitYAK_3R.this.setNew.prop = ((10F * CockpitYAK_3R.this.setOld.prop) + CockpitYAK_3R.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitYAK_3R.this.setNew.altimeter = CockpitYAK_3R.this.fm.getAltitude();
                if (Math.abs(CockpitYAK_3R.this.fm.Or.getKren()) < 30F) {
                    CockpitYAK_3R.this.setNew.azimuth.setDeg(CockpitYAK_3R.this.setOld.azimuth.getDeg(1.0F), CockpitYAK_3R.this.fm.Or.azimut());
                }
                CockpitYAK_3R.this.setNew.vspeed = ((199F * CockpitYAK_3R.this.setOld.vspeed) + CockpitYAK_3R.this.fm.getVertSpeed()) / 200F;
                CockpitYAK_3R.this.setNew.preved = (0.99F * CockpitYAK_3R.this.setOld.preved) + (0.01F * (CockpitYAK_3R.this.fm.EI.engines[1].getStage() != 6 ? 55F : 800F));
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        float      preved;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitYAK_3R() {
        super("3DO/Cockpit/Yak-3R/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(-0.3075D, 0.3392D, 0.2956D));
        this.light2 = new LightPointActor(new LightPoint(), new Point3d(-0.2095D, 0.2842D, 0.2655D));
        this.light3 = new LightPointActor(new LightPoint(), new Point3d(-0.037D, 0.201D, 0.2045D));
        this.light4 = new LightPointActor(new LightPoint(), new Point3d(-0.3075D, -0.3392D, 0.2956D));
        this.light5 = new LightPointActor(new LightPoint(), new Point3d(-0.2095D, -0.2842D, 0.2655D));
        this.light6 = new LightPointActor(new LightPoint(), new Point3d(-0.037D, -0.201D, 0.2045D));
        this.light1.light.setColor(245F, 221F, 189F);
        this.light2.light.setColor(245F, 221F, 189F);
        this.light3.light.setColor(245F, 221F, 189F);
        this.light4.light.setColor(245F, 221F, 189F);
        this.light5.light.setColor(245F, 221F, 189F);
        this.light6.light.setColor(245F, 221F, 189F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.light3.light.setEmit(0.0F, 0.0F);
        this.light4.light.setEmit(0.0F, 0.0F);
        this.light5.light.setEmit(0.0F, 0.0F);
        this.light6.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.pos.base().draw.lightMap().put("LAMPHOOK3", this.light3);
        this.pos.base().draw.lightMap().put("LAMPHOOK4", this.light4);
        this.pos.base().draw.lightMap().put("LAMPHOOK5", this.light5);
        this.pos.base().draw.lightMap().put("LAMPHOOK6", this.light6);
        this.cockpitNightMats = (new String[] { "prib_one", "prib_two", "prib_four", "prib_five" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("zKerosin", 0.0F, this.cvt(this.fm.M.nitro, 0.0F, 250F, 0.0F, -270F), 0.0F);
        this.mesh.chunkSetAngles("zAzotKislota", 0.0F, this.cvt(this.fm.M.nitro, 0.0F, 250F, 0.0F, -270F), 0.0F);
        this.mesh.chunkSetAngles("zPVRD_Temp", 0.0F, this.cvt(this.setNew.preved, 0.0F, 900F, 0.0F, -118F), 0.0F);
        this.mesh.chunkSetAngles("SW_PVRD", 0.0F, this.fm.EI.engines[1].getStage() <= 0 ? 0.0F : -45F, 0.0F);
        this.mesh.chunkSetAngles("richag", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("norm_gaz", 0.0F, -13F + (this.interp(this.setNew.throttle, this.setOld.throttle, f) * 33.8F), 0.0F);
        this.mesh.chunkSetAngles("shag_vinta", 0.0F, (this.interp(this.setNew.prop, this.setOld.prop, f) * 33.8F) - 13F, 0.0F);
        this.mesh.chunkSetAngles("nadduv", 0.0F, -19F + (39F * this.fm.EI.engines[0].getControlCompressor()), 0.0F);
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            this.mesh.chunkSetAngles("shassy", 0.0F, 24F, 0.0F);
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            this.mesh.chunkSetAngles("shassy", 0.0F, -24F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("shassy", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                this.mesh.chunkSetAngles("shitki", 0.0F, -24F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("shitki", 0.0F, 24F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("shitki", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitYAK_3R.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x40) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
            this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        if (((this.fm.AS.astateCockpitState & 4) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("zGas1a", 0.0F, this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 300F, 0.0F, 180F), 0.0F);
        }
        this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1b", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 8F, 0.0F, -180F), 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.M.fuel < 36F);
        this.mesh.chunkVisible("Z_Red2", this.fm.EI.engines[0].getRPM() < 650F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0) || ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zRPM1b", false);
            this.mesh.chunkVisible("zSlide1a", false);
            this.mesh.chunkVisible("zTOilOut1a", false);
            this.mesh.chunkVisible("zOilPrs1a", false);
            this.mesh.chunkVisible("zGasPrs1a", false);
            this.mesh.chunkVisible("panel", false);
            this.mesh.chunkVisible("panel_d1", true);
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0)) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.09F, 0.1F);
            this.light2.light.setEmit(0.02F, 0.2F);
            this.light3.light.setEmit(0.02F, 0.5F);
            this.light4.light.setEmit(0.09F, 0.1F);
            this.light5.light.setEmit(0.02F, 0.2F);
            this.light6.light.setEmit(0.02F, 0.5F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.light3.light.setEmit(0.0F, 0.0F);
            this.light4.light.setEmit(0.0F, 0.0F);
            this.light5.light.setEmit(0.0F, 0.0F);
            this.light6.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private LightPointActor    light3;
    private LightPointActor    light4;
    private LightPointActor    light5;
    private LightPointActor    light6;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };

}
