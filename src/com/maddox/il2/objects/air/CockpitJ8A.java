package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitJ8A extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitJ8A.this.bNeedSetUp) {
                CockpitJ8A.this.reflectPlaneMats();
                CockpitJ8A.this.bNeedSetUp = false;
            }
            if (((GLADIATOR1J8A) CockpitJ8A.this.aircraft()).bChangedPit) {
                CockpitJ8A.this.reflectPlaneToModel();
                ((GLADIATOR1J8A) CockpitJ8A.this.aircraft()).bChangedPit = false;
            }
            CockpitJ8A.this.setTmp = CockpitJ8A.this.setOld;
            CockpitJ8A.this.setOld = CockpitJ8A.this.setNew;
            CockpitJ8A.this.setNew = CockpitJ8A.this.setTmp;
            if (((CockpitJ8A.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitJ8A.this.setNew.stbyPosition < 1.0F)) {
                CockpitJ8A.this.setNew.stbyPosition = CockpitJ8A.this.setOld.stbyPosition + 0.0125F;
                CockpitJ8A.this.setOld.stbyPosition = CockpitJ8A.this.setNew.stbyPosition;
            }
            CockpitJ8A.this.setNew.altimeter = CockpitJ8A.this.fm.getAltitude();
            if (Math.abs(CockpitJ8A.this.fm.Or.getKren()) < 30F) {
                CockpitJ8A.this.setNew.azimuth.setDeg(CockpitJ8A.this.setOld.azimuth.getDeg(1.0F), CockpitJ8A.this.fm.Or.azimut());
            }
            CockpitJ8A.this.setNew.throttle = ((10F * CockpitJ8A.this.setOld.throttle) + CockpitJ8A.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitJ8A.this.setNew.mix = ((10F * CockpitJ8A.this.setOld.mix) + CockpitJ8A.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitJ8A.this.setNew.prop = CockpitJ8A.this.setOld.prop;
            if (CockpitJ8A.this.setNew.prop < (CockpitJ8A.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitJ8A.this.setNew.prop += 0.0025F;
            }
            if (CockpitJ8A.this.setNew.prop > (CockpitJ8A.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitJ8A.this.setNew.prop -= 0.0025F;
            }
            CockpitJ8A.this.w.set(CockpitJ8A.this.fm.getW());
            CockpitJ8A.this.fm.Or.transform(CockpitJ8A.this.w);
            CockpitJ8A.this.setNew.turn = ((12F * CockpitJ8A.this.setOld.turn) + CockpitJ8A.this.w.z) / 13F;
            CockpitJ8A.this.setNew.vspeed = ((299F * CockpitJ8A.this.setOld.vspeed) + CockpitJ8A.this.fm.getVertSpeed()) / 300F;
            CockpitJ8A.this.pictSupc = (0.8F * CockpitJ8A.this.pictSupc) + (0.2F * CockpitJ8A.this.fm.EI.engines[0].getControlCompressor());
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      throttle;
        float      mix;
        float      prop;
        float      turn;
        float      vspeed;
        float      stbyPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitJ8A() {
        super("3DO/Cockpit/J8A/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.cockpitNightMats = (new String[] { "PRIB_01", "PRIB_02", "PRIB_03", "PRIB_04", "PRIB_05", "PRIB_06", "PRIB_07" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((GLADIATOR1J8A) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((GLADIATOR1J8A) this.aircraft()).bChangedPit = false;
        }
        this.mesh.chunkSetAngles("Z_ReVi", 0.0F, 90F * this.setNew.stbyPosition, 0.0F);
        this.mesh.chunkSetAngles("Z_ManPrs1", 0.0F, 0.0F, 4F - this.cvt(this.pictManifold = (0.85F * this.pictManifold) + (0.15F * this.fm.EI.engines[0].getManifoldPressure()), 0.600034F, 1.66661F, -124.8F, 208F));
        this.mesh.chunkSetAngles("Flap01_D0", 0.0F, -50F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("Flap04_D0", 0.0F, -50F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 14F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 14F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Z_PedalStrut", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", 0.0F, 0.0F, -51.8F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Z_Mix", 0.0F, 0.0F, -52.3F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 16000F, 0.0F, -720F));
        float f1 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, 0.0F, this.cvt(f1, 60F, 450F, 0.0F, -340.5F));
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 1200F, 3400F, 0.0F, -328F));
        this.mesh.chunkSetAngles("Z_OilPress1", 0.0F, 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 12F, 0.0F, -309F));
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F));
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F));
        this.mesh.chunkSetAngles("Z_Second1", 0.0F, 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F));
        this.mesh.chunkSetAngles("Z_FuelPress1", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 282F, 0.0F, 71F));
        this.mesh.chunkSetAngles("Z_OilTemp1", 0.0F, 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, -315.5F));
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.042F, -0.042F);
        this.mesh.chunkSetLocate("Z_AoA1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Turn1", 0.0F, 0.0F, this.cvt(this.setNew.turn, -0.2F, 0.2F, -22.5F, 22.5F));
        this.mesh.chunkSetAngles("Turn2", 0.0F, 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 16.9F, -16.9F));
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, 0.0F, this.cvt(this.setNew.vspeed, -15F, 15F, 180F, -180F));
        this.mesh.chunkSetAngles("Z_Oxypres1", 0.0F, 0.0F, -260F);
        this.mesh.chunkSetAngles("Z_Oxypres2", 0.0F, 0.0F, -320F);
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * -0.6F;
        this.mesh.chunkSetLocate("XGlassDamage3", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * 0.6F;
        this.mesh.chunkSetLocate("Canopy", Aircraft.xyz, Aircraft.ypr);
        this.mesh.chunkSetLocate("CanopyGlass", Aircraft.xyz, Aircraft.ypr);
        this.mesh.chunkSetLocate("XGlassDamage2", Aircraft.xyz, Aircraft.ypr);
        if (this.fm.CT.bHasBrakeControl) {
            float f2 = this.fm.CT.getBrake();
            this.mesh.chunkSetAngles("Z_brake_handle", f2 * 20F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_brake_pressR", this.cvt(f2 + (f2 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 148F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_brake_pressL", this.cvt(f2 - (f2 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 148F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_brake_press", -150F + (f2 * 40F), 0.0F, 0.0F);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("Revi_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 4) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0) {

        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
        this.mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
        this.mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
        this.mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
        this.mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
        this.mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
        this.mesh.chunkVisible("WingLOut_D3", hiermesh.isChunkVisible("WingLOut_D3"));
        this.mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
        this.mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
        this.mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
        this.mesh.chunkVisible("WingROut_D3", hiermesh.isChunkVisible("WingROut_D3"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
        this.mesh.chunkVisible("WireL_D0", hiermesh.isChunkVisible("WireL_D0"));
        this.mesh.chunkVisible("WireR_D0", hiermesh.isChunkVisible("WireR_D0"));
        this.mesh.chunkVisible("Flap01_D0", hiermesh.isChunkVisible("Flap01_D0"));
        this.mesh.chunkVisible("Flap04_D0", hiermesh.isChunkVisible("Flap04_D0"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private Vector3f  w;
    private boolean   bNeedSetUp;
    private float     pictAiler;
    private float     pictElev;
    private float     pictSupc;
    private float     pictManifold;

}
