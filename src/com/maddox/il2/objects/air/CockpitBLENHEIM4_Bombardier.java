package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitBLENHEIM4_Bombardier extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            float f = ((BLENHEIM4base) CockpitBLENHEIM4_Bombardier.this.aircraft()).fSightCurForwardAngle;
            float f1 = ((BLENHEIM4base) CockpitBLENHEIM4_Bombardier.this.aircraft()).fSightCurSideslip;
            CockpitBLENHEIM4_Bombardier.calibrAngle = 360F - CockpitBLENHEIM4_Bombardier.this.fm.Or.getPitch();
            CockpitBLENHEIM4_Bombardier.this.mesh.chunkSetAngles("BlackBox", -10F * f1, 0.0F, CockpitBLENHEIM4_Bombardier.calibrAngle + f);
            if (CockpitBLENHEIM4_Bombardier.this.bEntered) {
                HookPilot hookpilot = HookPilot.current;
                hookpilot.setInstantOrient(CockpitBLENHEIM4_Bombardier.this.aAim + (10F * f1), CockpitBLENHEIM4_Bombardier.this.tAim + CockpitBLENHEIM4_Bombardier.calibrAngle + f, 0.0F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.15D, 0.0D, -0.1D);
            hookpilot.setTubeSight(point3d);
            this.aircraft().hierMesh().chunkVisible("Nose_D" + this.aircraft().chunkDamageVisible("Nose"), false);
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            this.aircraft().hierMesh().chunkVisible("Nose_D" + this.aircraft().chunkDamageVisible("Nose"), true);
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Head2_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", true);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, -33F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 23.913");
        Main3D.cur3D().aircraftHotKeys.enableBombSightFov();
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setInstantOrient(this.aAim, this.tAim, 0.0F);
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(0.0F, -33F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(0.0F, -33F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            return;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused() || (this.isToggleAim() == flag)) {
            return;
        }
        if (flag) {
            this.prepareToEnter();
        } else {
            this.leave();
        }
    }

    public CockpitBLENHEIM4_Bombardier() {
        super("3DO/Cockpit/Blenheim-Bombardier/BombardierBLENHEIM4.him", "he111");
        this.w = new Vector3f();
        this.bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.cockpitNightMats = (new String[] { "4_gauges" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 804.6721F, 0.0F, 10F), CockpitBLENHEIM4_Bombardier.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, this.fm.Or.getAzimut(), 0.0F);
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) {
                this.enteringAim = false;
            }
        }
        if (this.bEntered) {
            this.mesh.chunkSetAngles("zAngleMark", -this.floatindex(this.cvt(((BLENHEIM4base) this.aircraft()).fSightCurForwardAngle, 7F, 140F, 0.7F, 14F), CockpitBLENHEIM4_Bombardier.angleScale), 0.0F, 0.0F);
            boolean flag = ((BLENHEIM4base) this.aircraft()).fSightCurReadyness > 0.93F;
            this.mesh.chunkVisible("BlackBox", true);
            this.mesh.chunkVisible("zReticle", flag);
            this.mesh.chunkVisible("zAngleMark", flag);
        } else {
            this.mesh.chunkVisible("BlackBox", false);
            this.mesh.chunkVisible("zReticle", false);
            this.mesh.chunkVisible("zAngleMark", false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
    }

    public Vector3f            w;
    private boolean            enteringAim;
    private static final float angleScale[]       = { -38.5F, 16.5F, 41.5F, 52.5F, 59.25F, 64F, 67F, 70F, 72F, 73.25F, 75F, 76.5F, 77F, 78F, 79F, 80F };
    private static final float speedometerScale[] = { 0.0F, 17.5F, 82F, 143.5F, 205F, 226.5F, 248.5F, 270F, 292F, 315F, 338.5F };
    private static float       calibrAngle        = 0.0F;
    private float              saveFov;
    private float              aAim;
    private float              tAim;
    private boolean            bEntered;

    static {
        Property.set(CockpitBLENHEIM4_Bombardier.class, "astatePilotIndx", 0);
    }

}
