package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.electronics.RadarLiSN2Equipment;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;

public class CockpitFW_187_Radar extends CockpitPilot {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((TypeRadarLiSN2Carrier) this.aircraft()).setCurPilot(2);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            this.enter();
            this.go_top();
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((TypeRadarLiSN2Carrier) this.aircraft()).setCurPilot(1);
            this.leave();
            super.doFocusLeave();
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
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
        }
    }

    private void go_top() {
        CmdEnv.top().exec("fov 30.0");
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(false);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(this.aDiv, this.tDiv, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public CockpitFW_187_Radar() {
        super("3DO/Cockpit/FW-187A0-Radar/hier.him", "he111");
        this.cockpitRadarLiSN2 = new RadarLiSN2Equipment(this, 28, 70F, 5F, 0.2F, 0.012F, 0.011637F, 0.073296F);
        this.bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        try {
            Loc loc1 = new Loc();
            HookNamed hooknamed1 = new HookNamed(this.mesh, "CAMERA");
            hooknamed1.computePos(this, this.pos.getAbs(), loc1);
            this.aDiv = loc1.getOrient().getAzimut();
            this.tDiv = loc1.getOrient().getTangage();
        } catch (Exception exception1) {
            System.out.println(exception1.getMessage());
            exception1.printStackTrace();
        }
    }

    public void reflectWorldToInstruments(float f) {
        this.cockpitRadarLiSN2.updateRadar();
    }

    private RadarLiSN2Equipment cockpitRadarLiSN2;
    private float               saveFov;
    private float               aDiv;
    private float               tDiv;
    private boolean             bEntered;

    static {
        Property.set(CockpitFW_187_Radar.class, "astatePilotIndx", 0);
    }
}
