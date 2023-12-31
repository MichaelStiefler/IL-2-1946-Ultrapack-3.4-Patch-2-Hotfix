package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitR_XIIID extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitR_XIIID.this.bNeedSetUp) {
                CockpitR_XIIID.this.reflectPlaneMats();
                CockpitR_XIIID.this.bNeedSetUp = false;
            }
            if (R_XIIIxyz.bChangedPit) {
                CockpitR_XIIID.this.reflectPlaneToModel();
                R_XIIIxyz.bChangedPit = false;
            }
            CockpitR_XIIID.this.setTmp = CockpitR_XIIID.this.setOld;
            CockpitR_XIIID.this.setOld = CockpitR_XIIID.this.setNew;
            CockpitR_XIIID.this.setNew = CockpitR_XIIID.this.setTmp;
            CockpitR_XIIID.this.setNew.altimeter = CockpitR_XIIID.this.fm.getAltitude();
            if (Math.abs(CockpitR_XIIID.this.fm.Or.getKren()) < 30F) {
                CockpitR_XIIID.this.setNew.azimuth.setDeg(CockpitR_XIIID.this.setOld.azimuth.getDeg(1.0F), CockpitR_XIIID.this.fm.Or.azimut());
            }
            CockpitR_XIIID.this.setNew.mix = ((10F * CockpitR_XIIID.this.setOld.mix) + CockpitR_XIIID.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitR_XIIID.this.setNew.throttle = ((10F * CockpitR_XIIID.this.setOld.throttle) + CockpitR_XIIID.this.fm.CT.PowerControl) / 11F;
            CockpitR_XIIID.this.w.set(CockpitR_XIIID.this.fm.getW());
            CockpitR_XIIID.this.fm.Or.transform(CockpitR_XIIID.this.w);
            CockpitR_XIIID.this.setNew.turn = ((33F * CockpitR_XIIID.this.setOld.turn) + CockpitR_XIIID.this.w.z) / 34F;
            CockpitR_XIIID.this.mesh.chunkSetAngles("Turret1AP", 0.0F, -CockpitR_XIIID.this.aircraft().FM.turret[0].tu[0], 0.0F);
            CockpitR_XIIID.this.mesh.chunkSetAngles("Turret1BP", 0.0F, CockpitR_XIIID.this.aircraft().FM.turret[0].tu[1], 0.0F);
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      mix;
        float      throttle;
        float      turn;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitR_XIIID() {
        super("3DO/Cockpit/R-XIIID/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(0.8980392F, 0.8117647F, 0.9235294F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "zegary1", "zegary2", "zegary3", "int5", "lamps" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 5000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 260F, 0.0F, 13F), CockpitR_XIIID.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zBoost", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 0.8F, 0.5F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, 90F - this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 180F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 328F), 0.0F);
        this.mesh.chunkSetAngles("zOilOut", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 338F), 0.0F);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2600F, 0.0F, 13F), CockpitR_XIIID.rpmScale), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.Or.getTangage(), -20F, 20F, -0.05F, 0.05F);
        this.mesh.chunkSetLocate("zPitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zSlide", 0.0F, this.cvt(this.getBall(3D), -2F, 2.0F, -4F, 4F), 0.0F);
        this.mesh.chunkSetAngles("zTurn", 0.0F, this.cvt(this.setNew.turn, -0.7F, 0.7F, -1.5F, 1.5F), 0.0F);
        this.mesh.chunkSetAngles("StickBase", 0.0F, 15F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Stick2", 0.0F, 0.0F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("SticksRod", 0.0F, this.pictElev * 12F, 0.0F);
        this.mesh.chunkSetAngles("StickElevRod", 0.0F, this.pictElev * 12F, 0.0F);
        this.mesh.chunkSetAngles("Rudder", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Rudder2", 11F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL2", -29F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR2", -29F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -25F + (50F * this.interp(this.setNew.throttle, this.setOld.throttle, f)));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -25F + (50F * this.interp(this.setNew.mix, this.setOld.mix, f)));
        this.mesh.chunkSetAngles("zMagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F, 0.0F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        this.mesh.chunkVisible("Tail1_CAP", hiermesh.isChunkVisible("Tail1_CAP"));
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.3F, 0.3F);
            this.light2.light.setEmit(0.3F, 0.3F);
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
    private LightPointActor    light1;
    private LightPointActor    light2;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 6F, 12F, 18F, 39F, 63F, 90F, 118F, 145F, 182F, 217F, 245F, 273F, 304F };
    private static final float rpmScale[]         = { 0.0F, 12.5F, 25F, 50F, 75F, 100F, 125F, 150F, 175F, 200F, 225F, 250F, 275F, 300F };

}
