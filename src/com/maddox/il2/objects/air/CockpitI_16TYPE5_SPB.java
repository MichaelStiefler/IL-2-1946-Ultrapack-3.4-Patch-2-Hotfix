package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitI_16TYPE5_SPB extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;
        float      gCrankAngle;
        float      dimPos;

        private Variables() {
            this.gCrankAngle = 0.0F;
            this.dimPos = 1.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitI_16TYPE5_SPB.this.fm != null) {
                if (CockpitI_16TYPE5_SPB.this.bNeedSetUp) {
                    CockpitI_16TYPE5_SPB.this.reflectPlaneMats();
                    CockpitI_16TYPE5_SPB.this.bNeedSetUp = false;
                }
                CockpitI_16TYPE5_SPB.this.setTmp = CockpitI_16TYPE5_SPB.this.setOld;
                CockpitI_16TYPE5_SPB.this.setOld = CockpitI_16TYPE5_SPB.this.setNew;
                CockpitI_16TYPE5_SPB.this.setNew = CockpitI_16TYPE5_SPB.this.setTmp;
                CockpitI_16TYPE5_SPB.this.setNew.throttle = ((10F * CockpitI_16TYPE5_SPB.this.setOld.throttle) + CockpitI_16TYPE5_SPB.this.fm.CT.PowerControl) / 11F;
                CockpitI_16TYPE5_SPB.this.setNew.prop = ((10F * CockpitI_16TYPE5_SPB.this.setOld.prop) + CockpitI_16TYPE5_SPB.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitI_16TYPE5_SPB.this.setNew.altimeter = CockpitI_16TYPE5_SPB.this.fm.getAltitude();
                CockpitI_16TYPE5_SPB.this.setNew.waypointAzimuth.setDeg(CockpitI_16TYPE5_SPB.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitI_16TYPE5_SPB.this.waypointAzimuth() - CockpitI_16TYPE5_SPB.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitI_16TYPE5_SPB.this.fm.Or.getKren()) < 30F) {
                    CockpitI_16TYPE5_SPB.this.setNew.azimuth.setDeg(CockpitI_16TYPE5_SPB.this.setOld.azimuth.getDeg(1.0F), CockpitI_16TYPE5_SPB.this.fm.Or.azimut());
                }
                CockpitI_16TYPE5_SPB.this.setNew.vspeed = ((199F * CockpitI_16TYPE5_SPB.this.setOld.vspeed) + CockpitI_16TYPE5_SPB.this.fm.getVertSpeed()) / 200F;
                boolean flag = false;
                if (CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle < (CockpitI_16TYPE5_SPB.this.fm.CT.getGear() - 0.005F)) {
                    if (Math.abs(CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle - CockpitI_16TYPE5_SPB.this.fm.CT.getGear()) < 0.33F) {
                        CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle += 0.0025F;
                        flag = true;
                    } else {
                        CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle = CockpitI_16TYPE5_SPB.this.fm.CT.getGear();
                        CockpitI_16TYPE5_SPB.this.setOld.gCrankAngle = CockpitI_16TYPE5_SPB.this.fm.CT.getGear();
                    }
                }
                if (CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle > (CockpitI_16TYPE5_SPB.this.fm.CT.getGear() + 0.005F)) {
                    if (Math.abs(CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle - CockpitI_16TYPE5_SPB.this.fm.CT.getGear()) < 0.33F) {
                        CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle -= 0.0025F;
                        flag = true;
                    } else {
                        CockpitI_16TYPE5_SPB.this.setNew.gCrankAngle = CockpitI_16TYPE5_SPB.this.fm.CT.getGear();
                        CockpitI_16TYPE5_SPB.this.setOld.gCrankAngle = CockpitI_16TYPE5_SPB.this.fm.CT.getGear();
                    }
                }
                if (CockpitI_16TYPE5_SPB.this.cockpitDimControl) {
                    if (CockpitI_16TYPE5_SPB.this.setNew.dimPos < 1.0F) {
                        CockpitI_16TYPE5_SPB.this.setNew.dimPos = CockpitI_16TYPE5_SPB.this.setOld.dimPos + 0.03F;
                    }
                } else if (CockpitI_16TYPE5_SPB.this.setNew.dimPos > 0.0F) {
                    CockpitI_16TYPE5_SPB.this.setNew.dimPos = CockpitI_16TYPE5_SPB.this.setOld.dimPos - 0.03F;
                }
                if (flag != this.sfxPlaying) {
                    if (flag) {
                        CockpitI_16TYPE5_SPB.this.sfxStart(16);
                    } else {
                        CockpitI_16TYPE5_SPB.this.sfxStop(16);
                    }
                    this.sfxPlaying = flag;
                }
            }
            return true;
        }

        boolean sfxPlaying;

        Interpolater() {
            this.sfxPlaying = false;
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitI_16TYPE5_SPB() {
        super("3DO/Cockpit/I-16/hier_type5.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.bEntered = false;
        this.bDocked = false;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "prib_one", "prib_one_dd", "prib_two", "prib_two_dd", "prib_three", "prib_three_dd", "prib_four", "prib_four_dd", "shkala", "oxigen" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (((TypeDockable) this.aircraft()).typeDockableIsDocked() != this.bDocked) {
            this.bDocked = !this.bDocked;
            if (this.bDocked) {
                HierMesh hiermesh = ((ActorHMesh) ((I_16TYPE5_SPB) this.aircraft()).typeDockableGetQueen()).hierMesh();
                Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
                this.mesh.materialReplace("Gloss1D0o", mat);
                mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
                this.mesh.materialReplace("Matt1D0o", mat);
                mat = hiermesh.material(hiermesh.materialFind("Overlay7"));
                this.mesh.materialReplace("Overlay7", mat);
                if (((I_16TYPE5_SPB) this.aircraft()).typeDockableGetDockport() == 0) {
                    this.mesh.chunkVisible("WingLIn_D0_00", true);
                } else {
                    this.mesh.chunkVisible("WingRIn_D0_00", true);
                }
            } else {
                this.mesh.chunkVisible("WingLIn_D0_00", false);
                this.mesh.chunkVisible("WingRIn_D0_00", false);
            }
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("Fire1", 0.0F, -20F * (this.fm.CT.WeaponControl[0] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("Thtl", 30F - (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Thtl_Rod", -30F + (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Gear_Crank", (15840F * this.interp(this.setNew.gCrankAngle, this.setOld.gCrankAngle, f)) % 360F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -90F - this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 190F, 0.0F, 14F), CockpitI_16TYPE5_SPB.fuelQuantityScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), CockpitI_16TYPE5_SPB.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 4) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zPressAir1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 10F, 0.0F, 275F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            if (this.fm.EI.engines[0].getRPM() > 400F) {
                this.mesh.chunkSetAngles("zRPS1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 18F), CockpitI_16TYPE5_SPB.engineRPMScale), 0.0F);
            } else {
                this.mesh.chunkSetAngles("zRPS1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 400F, 0.0F, 18F), 0.0F);
            }
            this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
            this.mesh.chunkSetAngles("zTCil1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -95F), 0.0F);
        }
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zPressOil1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red2", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
        float f1 = this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F);
        this.mesh.chunkSetAngles("Z_sight_cap", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_sight_cap_big", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_sight_cap_inside", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_sight_cap_inside", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("SightCapSwitch", f1 * 0.75F, 0.0F, 0.0F);
        if (f1 <= -88F) {
            this.mesh.chunkVisible("Z_sight_cap_inside", false);
        } else {
            this.mesh.chunkVisible("Z_sight_cap_inside", true);
        }
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * -0.548F;
        this.mesh.chunkSetLocate("Canopy", Aircraft.xyz, Aircraft.ypr);
        this.mesh.chunkSetLocate("Glass", Aircraft.xyz, Aircraft.ypr);
        this.mesh.chunkSetLocate("Z_OilSplats_D1", Aircraft.xyz, Aircraft.ypr);
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_dd", true);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zPressOil1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_dd", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zGas1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
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
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void doToggleAim(boolean flag) {
        if (this.isFocused() && (this.isToggleAim() != flag)) {
            if (flag) {
                this.enter();
            } else {
                this.leave();
            }
        }
    }

    private void leave() {
        if (this.bEntered) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            this.bEntered = false;
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.mesh.chunkVisible("superretic", false);
            this.mesh.chunkVisible("Z_sight_cap_big", false);
        }
    }

    private void enter() {
        HookPilot hookpilot = HookPilot.current;
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
        this.mesh.chunkVisible("superretic", true);
        this.mesh.chunkVisible("Z_sight_cap_big", true);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            this.leave();
            super.doFocusLeave();
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bNeedSetUp;
    private float              saveFov;
    private boolean            bEntered;
    private boolean            bDocked;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[]  = { 0.0F, 0.0F, 18F, 44F, 74.5F, 106F, 136.3F, 169.5F, 207.5F, 245F, 287.5F, 330F };
    private static final float fuelQuantityScale[] = { 0.0F, 38.5F, 74.5F, 98.5F, 122F, 143F, 163F, 182.5F, 203F, 221F, 239.5F, 256F, 274F, 295F, 295F, 295F };
    private static final float engineRPMScale[]    = { 18F, 38F, 61F, 81F, 102F, 120F, 137F, 152F, 167F, 183F, 198F, 213F, 227F, 241F, 254F, 267F, 280F, 292F, 306F };

}
