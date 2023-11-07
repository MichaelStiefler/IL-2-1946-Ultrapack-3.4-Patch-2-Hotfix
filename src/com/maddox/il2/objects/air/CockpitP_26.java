package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
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

public class CockpitP_26 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_26.this.fm != null) {
                CockpitP_26.this.setTmp = CockpitP_26.this.setOld;
                CockpitP_26.this.setOld = CockpitP_26.this.setNew;
                CockpitP_26.this.setNew = CockpitP_26.this.setTmp;
                if (CockpitP_26.this.cockpitDimControl) {
                    if (CockpitP_26.this.setNew.dimPos < 1.0F) {
                        CockpitP_26.this.setNew.dimPos = CockpitP_26.this.setOld.dimPos + 0.03F;
                    }
                } else if (CockpitP_26.this.setNew.dimPos > 0.0F) {
                    CockpitP_26.this.setNew.dimPos = CockpitP_26.this.setOld.dimPos - 0.03F;
                }
                CockpitP_26.this.setNew.manifold = (0.8F * CockpitP_26.this.setOld.manifold) + (0.2F * CockpitP_26.this.fm.EI.engines[0].getManifoldPressure());
                CockpitP_26.this.setNew.throttle = (0.8F * CockpitP_26.this.setOld.throttle) + (0.2F * CockpitP_26.this.fm.CT.PowerControl);
                CockpitP_26.this.setNew.prop = (0.8F * CockpitP_26.this.setOld.prop) + (0.2F * CockpitP_26.this.fm.EI.engines[0].getControlProp());
                CockpitP_26.this.setNew.mix = (0.8F * CockpitP_26.this.setOld.mix) + (0.2F * CockpitP_26.this.fm.EI.engines[0].getControlMix());
                CockpitP_26.this.setNew.man = (0.92F * CockpitP_26.this.setOld.man) + (0.08F * CockpitP_26.this.fm.EI.engines[0].getManifoldPressure());
                CockpitP_26.this.setNew.altimeter = CockpitP_26.this.fm.getAltitude();
                float f = CockpitP_26.this.waypointAzimuth();
                if (Math.abs(CockpitP_26.this.fm.Or.getKren()) < 30F) {
                    CockpitP_26.this.setNew.azimuth.setDeg(CockpitP_26.this.setOld.azimuth.getDeg(1.0F), CockpitP_26.this.fm.Or.azimut());
                }
                CockpitP_26.this.setNew.waypointDeviation.setDeg(CockpitP_26.this.setOld.waypointDeviation.getDeg(0.1F), (f - CockpitP_26.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-5F, 5F));
                CockpitP_26.this.setNew.waypointAzimuth.setDeg(CockpitP_26.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                CockpitP_26.this.setNew.vspeed = (0.5F * CockpitP_26.this.setOld.vspeed) + (0.5F * CockpitP_26.this.fm.getVertSpeed());
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      dimPos;
        float      throttle;
        float      prop;
        float      mix;
        float      altimeter;
        float      man;
        float      vspeed;
        float      manifold;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDeviation;

        private Variables() {
            this.dimPos = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDeviation = new AnglesFork();
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            this.leave();
            this.aircraft().hierMesh().chunkVisible("Head1_D0", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
            super.doFocusLeave();
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.mesh.chunkVisible("SuperReticle", true);
        this.mesh.chunkVisible("Z_BoxTinter", true);
        this.mesh.chunkVisible("EDET", false);
    }

    private void leave() {
        if (this.bEntered) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            this.mesh.chunkVisible("SuperReticle", false);
            this.mesh.chunkVisible("Z_BoxTinter", false);
            this.mesh.chunkVisible("EDET", true);
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
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

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitP_26() {
        super("3DO/Cockpit/P-26/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictRad = 0.0F;
        this.pictGun = 0.0F;
        this.pictFlap = 0.0F;
        this.bNeedSetUp = true;
        this.oldTime = -1L;
        this.bEntered = false;
        this.cockpitNightMats = (new String[] { "gauge1", "gauge2", "gauge3", "gauge4", "gauge1_d", "gauge2_d", "gauge3_d", "gauge4_d", "Arrows", "Digits" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if ((P_26x) this.aircraft() != null) {

        }
        if (P_26x.bChangedPit) {
            this.reflectPlaneToModel();
            if ((P_26x) this.aircraft() != null) {

            }
            P_26x.bChangedPit = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.05F, 0.95F, 0.0F, 0.55F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_ReViTinter", 0.0F, this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_BoxTinter", 0.0F, this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_ColumnBase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, 0.0F);
        this.mesh.chunkSetAngles("Z_ColumnWire", 0.0F, this.pictElev * 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalBase", 0.0F, -30F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 20F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 20F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightWire", -30F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftWire", -30F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiat", 0.0F, -450F * (this.pictRad = (0.9F * this.pictRad) + (0.1F * this.fm.EI.engines[0].getControlRadiator())), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, this.cvt(this.setNew.throttle, 0.0F, 1.1F, -38F, 38F), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, 30F * (this.pictGun = (0.8F * this.pictGun) + (0.2F * (this.fm.CT.saveWeaponControl[0] ? 1.0F : 0.0F))), 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 0.0F, this.cvt(this.setNew.prop, 0.0F, 1.0F, -38F, 38F), 0.0F);
        this.mesh.chunkSetAngles("zTrim1", 0.0F, this.cvt(this.fm.CT.trimElevator, -0.5F, 0.5F, 35F, -35F), 0.0F);
        this.mesh.chunkSetAngles("zTrim2", 0.0F, this.cvt(this.fm.CT.trimElevator, -0.5F, 0.5F, -35F, 35F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.setNew.mix, 0.0F, 1.2F, 0.03675F, 0.0F);
        long l = Time.current();
        long l1 = l - this.oldTime;
        this.oldTime = l;
        float f1 = l1 * 0.00016F;
        if (this.pictFlap < this.fm.CT.FlapsControl) {
            if ((this.pictFlap + f1) >= this.fm.CT.FlapsControl) {
                this.pictFlap = this.fm.CT.FlapsControl;
            } else {
                this.pictFlap += f1;
            }
        } else if ((this.pictFlap - f1) <= this.fm.CT.FlapsControl) {
            this.pictFlap = this.fm.CT.FlapsControl;
        } else {
            this.pictFlap -= f1;
        }
        this.mesh.chunkSetAngles("Z_Flaps", 0.0F, -3450F * this.pictFlap, 0.0F);
        this.mesh.chunkSetAngles("Z_Mag1", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 76.5F, -28.5F), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), CockpitP_26.speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(6D), -6F, 6F, 6F, -6F), 0.0F, 0.0F);
        float f2 = this.setNew.vspeed;
        if (Math.abs(f2) < 5F) {
            this.mesh.chunkSetAngles("Z_Climb1", this.cvt(f2, -5F, 5F, 90F, -90F), 0.0F, 0.0F);
        } else if (f2 > 0.0F) {
            this.mesh.chunkSetAngles("Z_Climb1", this.cvt(f2, 5F, 30F, -90F, -180F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Climb1", this.cvt(f2, -30F, -5F, 180F, 90F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Compass1", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Clock_H", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Clock_Min", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        f2 = this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 8.1F, 0.0F, 20F);
        for (int i = 1; i < 20; i++) {
            this.mesh.chunkVisible("Z_OilP" + (i < 10 ? "0" + i : "" + i), f2 > (20 - i));
        }

        this.mesh.chunkSetAngles("Z_Manipres", this.cvt(this.setNew.manifold, 0.33339F, 1.66661F, 150F, -150F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 200F, 3000F, -8.5F, -323F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuelpres", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 2.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oiltemp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 5.5F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Tempcyl", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, -90.6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel", this.cvt(this.fm.M.fuel, 0.0F, 108F, -41F, -320F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("XGlassDamage7", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_Manipres", false);
            this.mesh.chunkVisible("Z_Fuel", false);
            this.mesh.chunkVisible("Z_Fuelpres", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Oiltemp1", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_Clock_H", false);
            this.mesh.chunkVisible("Z_Clock_Min", false);
            this.mesh.chunkVisible("Z_Tempcyl", false);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage6", true);
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictRad;
    private float              pictGun;
    private float              pictFlap;
    private boolean            bNeedSetUp;
    private long               oldTime;
    private static final float speedometerScale[] = { 0.0F, 6.5F, 16.5F, 49F, 91.5F, 143.5F, 199F, 260F, 318F, 376.5F, 433F, 484F, 534F, 576F, 620F, 660F };
    private float              saveFov;
    private boolean            bEntered;

}
