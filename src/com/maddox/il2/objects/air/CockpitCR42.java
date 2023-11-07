package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class CockpitCR42 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCR42.this.bNeedSetUp) {
                CockpitCR42.this.reflectPlaneMats();
                CockpitCR42.this.bNeedSetUp = false;
            }
            if (((CR_42) CockpitCR42.this.aircraft()).bChangedPit) {
                CockpitCR42.this.reflectPlaneToModel();
                ((CR_42) CockpitCR42.this.aircraft()).bChangedPit = false;
            }
            CockpitCR42.this.setTmp = CockpitCR42.this.setOld;
            CockpitCR42.this.setOld = CockpitCR42.this.setNew;
            CockpitCR42.this.setNew = CockpitCR42.this.setTmp;
            if (((CockpitCR42.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitCR42.this.setNew.stbyPosition < 1.0F)) {
                CockpitCR42.this.delay--;
                if (CockpitCR42.this.delay <= 0) {
                    CockpitCR42.this.setNew.stbyPosition = CockpitCR42.this.setOld.stbyPosition + 0.03F;
                    CockpitCR42.this.setOld.stbyPosition = CockpitCR42.this.setNew.stbyPosition;
                    CockpitCR42.this.sightDamaged = true;
                }
            }
            CockpitCR42.this.setNew.altimeter = CockpitCR42.this.fm.getAltitude();
            if (Math.abs(CockpitCR42.this.fm.Or.getKren()) < 30F) {
                CockpitCR42.this.setNew.azimuth.setDeg(CockpitCR42.this.setOld.azimuth.getDeg(1.0F), CockpitCR42.this.fm.Or.azimut());
            }
            CockpitCR42.this.setNew.throttle = ((10F * CockpitCR42.this.setOld.throttle) + CockpitCR42.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitCR42.this.setNew.mix = ((10F * CockpitCR42.this.setOld.mix) + CockpitCR42.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitCR42.this.setNew.prop = CockpitCR42.this.setOld.prop;
            if (CockpitCR42.this.setNew.prop < (CockpitCR42.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitCR42.this.setNew.prop += 0.0025F;
            }
            if (CockpitCR42.this.setNew.prop > (CockpitCR42.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitCR42.this.setNew.prop -= 0.0025F;
            }
            CockpitCR42.this.w.set(CockpitCR42.this.fm.getW());
            CockpitCR42.this.fm.Or.transform(CockpitCR42.this.w);
            CockpitCR42.this.setNew.turn = ((12F * CockpitCR42.this.setOld.turn) + CockpitCR42.this.w.z) / 13F;
            CockpitCR42.this.setNew.vspeed = ((299F * CockpitCR42.this.setOld.vspeed) + CockpitCR42.this.fm.getVertSpeed()) / 300F;
            CockpitCR42.this.pictSupc = (0.8F * CockpitCR42.this.pictSupc) + (0.2F * CockpitCR42.this.fm.EI.engines[0].getControlCompressor());
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

    public CockpitCR42() {
        super("3DO/Cockpit/CR42/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.pictFlap = 0.0F;
        this.delay = 80;
        this.sightDamaged = false;
        this.cockpitNightMats = (new String[] { "z_clocks", "z_clocks2", "z_clocks3", "z_clocks4", "z_clocks5" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((CR_42) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((CR_42) this.aircraft()).bChangedPit = false;
        }
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
        }
        this.mesh.chunkSetAngles("Z_Column", 4F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 2.0F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = -0.01115F;
        }
        this.mesh.chunkSetLocate("Z_Column_but", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Pedals", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ped_trossL", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ped_trossR", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", 100F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", 27F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", 100F * this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch1", 90F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap", -180F * (this.pictFlap = (0.85F * this.pictFlap) + (0.15F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim", 1444F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.015F * this.fm.CT.getTrimAileronControl();
        this.mesh.chunkSetLocate("Z_Trim1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_OilRad", 70F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OverPres", 70F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 10F), CockpitCR42.speedometerScale), 0.0F, 0.0F);
        float f1 = this.interp(this.setNew.altimeter, this.setOld.altimeter, f);
        if (f1 < 5000F) {
            this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(f1, 0.0F, 5000F, 0.0F, 360F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(f1, 5000F, 11000F, 360F, 720F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 150F, 0.0F, 305.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilTemp1", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 150F, 0.0F, 5F), CockpitCR42.oilScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 3F, 0.0F, 6F, 0.0F, 297F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity1", this.cvt(this.fm.M.fuel, 0.0F, 252F, 0.0F, -316.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        if (this.gun[0] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt(this.gun[0].countBullets(), 0.0F, 600F, 0.0F, 338F), 0.0F, 0.0F);
        }
        if (this.gun[1] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt(this.gun[1].countBullets(), 0.0F, 600F, 0.0F, 338F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Turn1", this.cvt(this.setNew.turn, -0.2F, 0.2F, 35F, -35F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turn2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -16.4F, 16.4F), 0.0F);
        this.mesh.chunkSetAngles("Z_ManfoldPress", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.533288F, 1.33322F, 0.0F, 330F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_gunsight_rim", 50F * this.setNew.stbyPosition, 0.0F, 0.0F);
    }

    public void doToggleAim(boolean flag) {
        super.doToggleAim(flag);
        if (flag && this.sightDamaged) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(true);
            hookpilot.setAim(new Point3d(-1.5D, 0.0D, 0.72970002889633179D));
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("glass", false);
            this.mesh.chunkVisible("glass_d1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("gunsight_lense", false);
            this.mesh.chunkVisible("D_gunsight_lense", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("panel", false);
            this.mesh.chunkVisible("panel_d1", true);
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Turn1", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHullDamage3", true);
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
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
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
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
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

    private Gun                gun[]              = { null, null };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictSupc;
    private float              pictFlap;
    private int                delay;
    private boolean            sightDamaged;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 38F, 76.5F, 116F, 156F, 195F, 234F, 271F, 308.5F, 326F };
    private static final float oilScale[]         = { 0.0F, 36.5F, 53.5F, 103F, 194.5F, 332F };

}
