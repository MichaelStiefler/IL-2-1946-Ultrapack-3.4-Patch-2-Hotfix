package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class Cockpit_JET extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if ((Cockpit_JET.this.fm != null) && Cockpit_JET.this.cockpitDimControl && (Cockpit_JET.this.setNew.dimPosition > 0.0F)) {
                Cockpit_JET.this.setNew.dimPosition = Cockpit_JET.this.setOld.dimPosition - 0.05F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float dimPosition;
        private Variables() {
        }

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

    private void enter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aAim, this.tAim, this.kAim);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.bEntered) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean bool = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", bool);
            HotKeyEnv.enable("SnapView", bool);
            this.bEntered = false;
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean bool) {
        if (this.isFocused() && (this.isToggleAim() != bool)) {
            if (bool) {
                this.enter();
            } else {
                this.leave();
            }
        }
    }

    public Cockpit_JET() {
        super("3DO/Cockpit/JetmanPit/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(super.mesh, "CAMERAAIM");
            hooknamed.computePos(this, super.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
            this.kAim = loc.getOrient().getKren();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (super.acoustics != null) {
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void toggleDim() {
        super.cockpitDimControl = !super.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f1) {
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public void reflectCockpitState() {
        this.retoggleLight();
    }

    public void toggleLight() {
        super.cockpitLightControl = !super.cockpitLightControl;
        if (super.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private void retoggleLight() {
        if (super.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    public Vector3f   w;
    private float     aAim;
    private float     tAim;
    private float     kAim;
    private boolean   bEntered;

}
