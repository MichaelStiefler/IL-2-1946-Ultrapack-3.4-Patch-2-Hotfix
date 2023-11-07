package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitR_5_OP1 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitR_5_OP1.this.setTmp = CockpitR_5_OP1.this.setOld;
            CockpitR_5_OP1.this.setOld = CockpitR_5_OP1.this.setNew;
            CockpitR_5_OP1.this.setNew = CockpitR_5_OP1.this.setTmp;
            if (CockpitR_5_OP1.this.cockpitDimControl) {
                if (CockpitR_5_OP1.this.setNew.dimPos < 1.0F) {
                    CockpitR_5_OP1.this.setNew.dimPos = CockpitR_5_OP1.this.setOld.dimPos + 0.05F;
                }
            } else if (CockpitR_5_OP1.this.setNew.dimPos > 0.0F) {
                CockpitR_5_OP1.this.setNew.dimPos = CockpitR_5_OP1.this.setOld.dimPos - 0.05F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float dimPos;
        private Variables() {
            this.dimPos = 1.0F;
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.0D, 0.0D, 0.0D);
            hookpilot.setTubeSight(point3d);
            this.enter();
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
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31.0");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
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
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public CockpitR_5_OP1() {
        super("3DO/Cockpit/R-5_OP1/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.bEntered = false;
        this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
    }

    public void reflectWorldToInstruments(float f) {
        float f1 = this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F);
        this.mesh.chunkSetAngles("Z_BoxTinter", 0.0F, f1, 0.0F);
    }

    private float     saveFov;
    private boolean   bEntered;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

    static {
        Property.set(CockpitR_5_OP1.class, "astatePilotIndx", 0);
        Property.set(CockpitR_5_OP1.class, "gsZN", 0.2F);
    }
}
