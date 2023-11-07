package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;

public class CockpitI_15Bis extends CockpitPilot {
    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      throttle;
        float      mix;
        float      prop;
        float      turn;
        float      vspeed;
        float      dimPos;
        float      radiator;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitI_15Bis.this.bNeedSetUp) {
                CockpitI_15Bis.this.reflectPlaneMats();
                CockpitI_15Bis.this.bNeedSetUp = false;
            }
            if ((I_15xyz) CockpitI_15Bis.this.aircraft() == null) {

            }
            if (I_15xyz.bChangedPit) {
                CockpitI_15Bis.this.reflectPlaneToModel();
                if ((I_15xyz) CockpitI_15Bis.this.aircraft() == null) {

                }
                I_15xyz.bChangedPit = false;
            }
            CockpitI_15Bis.this.setTmp = CockpitI_15Bis.this.setOld;
            CockpitI_15Bis.this.setOld = CockpitI_15Bis.this.setNew;
            CockpitI_15Bis.this.setNew = CockpitI_15Bis.this.setTmp;
            if (CockpitI_15Bis.this.cockpitDimControl) {
                if (CockpitI_15Bis.this.setNew.dimPos < 1.0F) {
                    CockpitI_15Bis.this.setNew.dimPos = CockpitI_15Bis.this.setOld.dimPos + 0.05F;
                }
            } else if (CockpitI_15Bis.this.setNew.dimPos > 0.0F) {
                CockpitI_15Bis.this.setNew.dimPos = CockpitI_15Bis.this.setOld.dimPos - 0.05F;
            }
            CockpitI_15Bis.this.setNew.altimeter = CockpitI_15Bis.this.fm.getAltitude();
            if (Math.abs(CockpitI_15Bis.this.fm.Or.getKren()) < 30F) {
                CockpitI_15Bis.this.setNew.azimuth.setDeg(CockpitI_15Bis.this.setOld.azimuth.getDeg(1.0F), CockpitI_15Bis.this.fm.Or.azimut());
            }
            CockpitI_15Bis.this.setNew.throttle = ((10F * CockpitI_15Bis.this.setOld.throttle) + CockpitI_15Bis.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitI_15Bis.this.setNew.mix = ((10F * CockpitI_15Bis.this.setOld.mix) + CockpitI_15Bis.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitI_15Bis.this.setNew.prop = CockpitI_15Bis.this.setOld.prop;
            if (CockpitI_15Bis.this.setNew.prop < (CockpitI_15Bis.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitI_15Bis.this.setNew.prop += 0.0025F;
            }
            if (CockpitI_15Bis.this.setNew.prop > (CockpitI_15Bis.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitI_15Bis.this.setNew.prop -= 0.0025F;
            }
            CockpitI_15Bis.this.w.set(CockpitI_15Bis.this.fm.getW());
            CockpitI_15Bis.this.fm.Or.transform(CockpitI_15Bis.this.w);
            CockpitI_15Bis.this.setNew.turn = ((12F * CockpitI_15Bis.this.setOld.turn) + CockpitI_15Bis.this.w.z) / 13F;
            CockpitI_15Bis.this.setNew.vspeed = ((299F * CockpitI_15Bis.this.setOld.vspeed) + CockpitI_15Bis.this.fm.getVertSpeed()) / 300F;
            CockpitI_15Bis.this.pictSupc = (0.8F * CockpitI_15Bis.this.pictSupc) + (0.2F * CockpitI_15Bis.this.fm.EI.engines[0].getControlCompressor());
            CockpitI_15Bis.this.setNew.radiator = ((10F * CockpitI_15Bis.this.setOld.radiator) + CockpitI_15Bis.this.fm.CT.getRadiatorControl()) / 11F;
            if (((I_15xyz) CockpitI_15Bis.this.aircraft()).blisterRemoved) {
                CockpitI_15Bis.this.mesh.chunkVisible("Z_BlisterR1", false);
                CockpitI_15Bis.this.mesh.chunkVisible("Z_BlisterR2", false);
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitI_15Bis() {
        super("3DO/Cockpit/I-15Bis/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.scopeZoomArea = 15F;
        this.isIron = false;
        this.bEntered = false;
        this.rpmGeneratedPressure = 0.0F;
        this.oilPressure = 0.0F;
        this.isSlideRight = false;
        this.cockpitNightMats = (new String[] { "prib_four_damage", "prib_four", "PRIB_ONE", "prib_one_damage", "PRIB_three", "prib_three_damage", "PRIB_TWO", "prib_two_damage", "Shkala128" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Mix1", 50F * this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix2", 0.0F, 0.0F, -50F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("Z_Throtle1", 60F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, 0.0F, -60F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Z_Supc1", -50F * this.pictSupc, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Supc2", 0.0F, 0.0F, 50F * this.pictSupc);
        this.mesh.chunkSetAngles("Z_Rad1", -60F * this.interp(this.setNew.radiator, this.setOld.radiator, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rad2", 0.0F, 0.0F, -60F * this.interp(this.setNew.radiator, this.setOld.radiator, f));
        this.mesh.chunkSetAngles("Z_Magto", 44F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick1", 0.0F, 16F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 10.2F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Z_Stick2", 0.0F, 0.0F, 10.2F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Z_Ped_Base", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_PedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Ped_trossL", -15.65F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ped_trossR", -15.65F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 12F), CockpitI_15Bis.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.setNew.turn, -0.2F, 0.2F, 26F, -26F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 26F, -26F), 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.floatindex(this.cvt(f1, 0.0F, 2400F, 0.0F, 11F), CockpitI_15Bis.rpmScale), 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - 2.0F;
        } else if (f1 < this.rpmGeneratedPressure) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - ((this.rpmGeneratedPressure - f1) * 0.01F);
        } else {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure + ((f1 - this.rpmGeneratedPressure) * 0.001F);
        }
        if (this.rpmGeneratedPressure < 800F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure < 1800F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 800F, 1800F, 4F, 5F);
        } else {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 1800F, 2750F, 5F, 5.8F);
        }
        float f2 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f2 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f2 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f2 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f3 = f2 * this.fm.EI.engines[0].getReadyness() * this.oilPressure;
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(f3, 0.0F, 7F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuelpres1", 0.0F, -this.cvt(this.rpmGeneratedPressure, 0.0F, 1800F, 0.0F, 120F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 234F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Clock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Clock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, -60F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.399966F, 2.133152F, 3F, 16F), CockpitI_15Bis.manifoldScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Fire1", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[0] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Z_Fire5", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[0] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Z_Fire3", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[2] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Z_Bomb", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[3] ? 15F : 0.0F);
        this.mesh.chunkSetAngles("Z_Fire4", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[1] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("Z_Fire2", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[1] ? -15F : 0.0F);
        f1 = this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F);
        this.mesh.chunkSetAngles("Z_Tinter", 0.0F, 0.0F, f1);
        this.mesh.chunkSetAngles("Z_Tinter_I", 0.0F, 0.0F, f1);
        this.mesh.chunkSetAngles("Z_BoxTinter", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_GS", f1 / 2.0F, 0.0F, 0.0F);
        f1 = this.fm.CT.getCockpitDoor();
        this.mesh.chunkSetAngles("Z_BlisterR1", 0.0F, 0.0F, f1 * 177.7F);
        this.mesh.chunkSetAngles("Z_BlisterR2", 0.0F, 0.0F, f1 * 15.6F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gauges1_D0", false);
            this.mesh.chunkVisible("Gauges1_D1", true);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Fuel", false);
            this.mesh.chunkVisible("Z_Clock1a", false);
            this.mesh.chunkVisible("Z_Clock1b", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Pres1", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_Fuelpres1", false);
            this.mesh.chunkVisible("Z_Oil1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Gauges2_D0", false);
            this.mesh.chunkVisible("Gauges2_D1", true);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {

        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("WingLMid_D0_00", !hiermesh.isChunkVisible("WingLMid_Cap"));
        this.mesh.chunkVisible("WingRMid_D0_00", !hiermesh.isChunkVisible("WingRMid_Cap"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        this.setNightMats(this.cockpitLightControl);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void doToggleAim(boolean flag) {
        if (this.isFocused()) {
            if (flag && !this.isIron) {
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                HookPilot hookpilot = HookPilot.current;
                hookpilot.computePos(this, loc, loc1);
                float f = loc1.getOrient().getYaw();
                if (hookpilot.isPadlock() || ((f > -this.scopeZoomArea) && (f < this.scopeZoomArea) && (this.isToggleAim() != flag))) {
                    this.enterScope();
                } else if (f < -this.scopeZoomArea) {
                    hookpilot.doAim(true);
                    hookpilot.setAim(new Point3d(-1.68D, -0.08D, 0.848D));
                    this.isIron = true;
                } else if (f > this.scopeZoomArea) {
                    hookpilot.doAim(true);
                    hookpilot.setAim(new Point3d(-1.68D, 0.08D, 0.848D));
                    this.isIron = true;
                }
            } else {
                this.isIron = false;
                this.leave();
            }
        }
    }

    private void enterScope() {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setAim(new Point3d(0.184D, 0.0D, 0.8481D));
        hookpilot.doAim(true);
        this.bEntered = true;
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.mesh.chunkVisible("SuperReticle", true);
        this.mesh.chunkVisible("Z_BoxTinter", true);
    }

    private void leave() {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.doAim(false);
        hookpilot.setAim(new Point3d(0.1983D, -0.0044D, 0.8481D));
        if (this.bEntered) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.mesh.chunkVisible("SuperReticle", false);
            this.mesh.chunkVisible("Z_BoxTinter", false);
        }
    }

    protected boolean doFocusEnter() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        hiermesh.chunkVisible("Wire_D0", false);
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        hiermesh.chunkVisible("Wire_D0", true);
        if (this.isFocused()) {
            this.leave();
            super.doFocusLeave();
        }
    }

    public boolean isViewRight() {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HookPilot.current.computePos(this, loc, loc1);
        float f = loc1.getOrient().getYaw();
        if (f < 0.0F) {
            this.isSlideRight = true;
        } else {
            this.isSlideRight = false;
        }
        return this.isSlideRight;
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictSupc;
    private float              saveFov;
    private float              scopeZoomArea;
    private boolean            isIron;
    private boolean            bEntered;
    private float              rpmGeneratedPressure;
    private float              oilPressure;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 18F, 45F, 75.5F, 107F, 137.5F, 170F, 206.5F, 243.75F, 286.5F, 329.5F, 374.5F };
    private static final float rpmScale[]         = { 0.0F, 5.5F, 18.5F, 59F, 99.5F, 134.5F, 165.75F, 198F, 228F, 255.5F, 308F, 345F };
    private static final float manifoldScale[]    = { 0.0F, 0.0F, 0.0F, 0.0F, 26F, 52F, 79F, 106F, 132F, 160F, 185F, 208F, 235F, 260F, 286F, 311F, 336F };
    private boolean            isSlideRight;

}
