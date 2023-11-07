package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitHE_LIIIB extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHE_LIIIB.this.fm != null) {
                CockpitHE_LIIIB.this.setTmp = CockpitHE_LIIIB.this.setOld;
                CockpitHE_LIIIB.this.setOld = CockpitHE_LIIIB.this.setNew;
                CockpitHE_LIIIB.this.setNew = CockpitHE_LIIIB.this.setTmp;
                CockpitHE_LIIIB.this.setNew.throttle1 = (0.85F * CockpitHE_LIIIB.this.setOld.throttle1) + (CockpitHE_LIIIB.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitHE_LIIIB.this.setNew.prop1 = (0.85F * CockpitHE_LIIIB.this.setOld.prop1) + (CockpitHE_LIIIB.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitHE_LIIIB.this.setNew.mix1 = (0.85F * CockpitHE_LIIIB.this.setOld.mix1) + (CockpitHE_LIIIB.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitHE_LIIIB.this.setNew.throttle2 = (0.85F * CockpitHE_LIIIB.this.setOld.throttle2) + (CockpitHE_LIIIB.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitHE_LIIIB.this.setNew.prop2 = (0.85F * CockpitHE_LIIIB.this.setOld.prop2) + (CockpitHE_LIIIB.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitHE_LIIIB.this.setNew.mix2 = (0.85F * CockpitHE_LIIIB.this.setOld.mix2) + (CockpitHE_LIIIB.this.fm.EI.engines[1].getControlMix() * 0.15F);
                CockpitHE_LIIIB.this.setNew.altimeter = CockpitHE_LIIIB.this.fm.getAltitude();
                float f = CockpitHE_LIIIB.this.waypointAzimuth();
                CockpitHE_LIIIB.this.setNew.waypointAzimuth.setDeg(CockpitHE_LIIIB.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitHE_LIIIB.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-2F, 2.0F));
                if (Math.abs(CockpitHE_LIIIB.this.fm.Or.getKren()) < 30F) {
                    CockpitHE_LIIIB.this.setNew.azimuth.setDeg(CockpitHE_LIIIB.this.setOld.azimuth.getDeg(1.0F), CockpitHE_LIIIB.this.fm.Or.azimut());
                }
                CockpitHE_LIIIB.this.setNew.vspeed = (0.8F * CockpitHE_LIIIB.this.setOld.vspeed) + (0.2F * CockpitHE_LIIIB.this.fm.getVertSpeed());
                if (CockpitHE_LIIIB.this.cockpitDimControl) {
                    if (CockpitHE_LIIIB.this.setNew.dimPosition > 0.0F) {
                        CockpitHE_LIIIB.this.setNew.dimPosition = CockpitHE_LIIIB.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitHE_LIIIB.this.setNew.dimPosition < 1.0F) {
                    CockpitHE_LIIIB.this.setNew.dimPosition = CockpitHE_LIIIB.this.setOld.dimPosition + 0.05F;
                }
                CockpitHE_LIIIB.this.setNew.radioalt = (0.5F * CockpitHE_LIIIB.this.setOld.radioalt) + (0.5F * (CockpitHE_LIIIB.this.fm.getAltitude() - Landscape.HQ_Air((float) CockpitHE_LIIIB.this.fm.Loc.x, (float) CockpitHE_LIIIB.this.fm.Loc.y)));
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      prop1;
        float      mix1;
        float      throttle2;
        float      prop2;
        float      mix2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;
        float      radioalt;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
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
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            super.doFocusLeave();
            return;
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
        if (!this.bEntered) {
            return;
        } else {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
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
            this.enter();
        } else {
            this.leave();
        }
    }

    public CockpitHE_LIIIB() {
        super("3DO/Cockpit/He-LercheIIIb/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.bEntered = false;
        try {
            Loc loc = new Loc();
            HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
            hooknamed.computePos(this, this.pos.getAbs(), loc);
            this.aAim = loc.getOrient().getAzimut();
            this.tAim = loc.getOrient().getTangage();
            this.kAim = loc.getOrient().getKren();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Stick", -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 10F, 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Z_THTL1", 25F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_THTL2", 25F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 57F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, 57F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpress1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 58F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpress2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 58F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitHE_LIIIB.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitHE_LIIIB.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ATA1", this.pictManf1 = (0.9F * this.pictManf1) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 329F)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ATA2", this.pictManf2 = (0.9F * this.pictManf2) + (0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 329F)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 0.0F, 864F, -8F, 80F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 33F, -33F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(8D), -8F, 8F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.024F, -0.024F);
        this.mesh.chunkSetLocate("Z_TurnBank2a", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Speed1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), CockpitHE_LIIIB.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt1", this.cvt(this.interp(this.setNew.radioalt, this.setOld.radioalt, f), 6.27F, 206.27F, 0.0F, -100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 14000F, 0.0F, 315F), 0.0F, 0.0F);
        float f1 = this.setNew.vspeed <= 0.0F ? -1F : 1.0F;
        this.mesh.chunkSetAngles("Z_Climb1", f1 * this.floatindex(this.cvt(Math.abs(this.setNew.vspeed), 0.0F, 30F, 0.0F, 6F), CockpitHE_LIIIB.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_EnRoute", false);
        this.mesh.chunkVisible("Z_TD1", this.setNew.radioalt < 6.5F);
        this.mesh.chunkVisible("Z_TD2", this.setNew.radioalt < 6.5F);
        this.mesh.chunkVisible("Z_TD3", this.setNew.radioalt < 6.5F);
        this.mesh.chunkVisible("Z_Stabilizer1", this.fm.CT.AirBrakeControl > 0.5F);
        this.mesh.chunkVisible("Z_Stabilizer2", (this.fm.CT.getAirBrake() > 0.99F) && (this.fm.Or.getTangage() > 30F));
        this.mesh.chunkVisible("Z_Power25", this.fm.EI.getPowerOutput() > 0.25F);
        this.mesh.chunkVisible("Z_LoFuel", this.fm.M.fuel < 65F);
        this.mesh.chunkVisible("Z_Fire1", (this.fm.AS.astateEngineStates[0] > 2) || (this.fm.AS.astateEngineStates[1] > 2));
        this.mesh.chunkVisible("Z_Fire2", (this.fm.AS.astateEngineStates[0] > 2) || (this.fm.AS.astateEngineStates[1] > 2));
        this.mesh.chunkVisible("Z_Fire3", (this.fm.AS.astateEngineStates[0] > 2) || (this.fm.AS.astateEngineStates[1] > 2));
        this.mesh.chunkVisible("Z_MPlus", (this.fm.EI.engines[0].getStage() > 0) || (this.fm.EI.engines[1].getStage() > 0));
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("Revisun", false);
            this.mesh.chunkVisible("Revi_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PanelL_D0", false);
            this.mesh.chunkVisible("PanelL_D1", true);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_TurnBank2a", false);
            this.mesh.chunkVisible("Z_TurnBank3", false);
            this.mesh.chunkVisible("Z_Alt2", false);
            this.mesh.chunkVisible("Z_Alt3", false);
            this.mesh.chunkVisible("Z_Speed1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("PanelR_D0", false);
            this.mesh.chunkVisible("PanelR_D1", true);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Temp2", false);
            this.mesh.chunkVisible("Z_Oilpress1", false);
            this.mesh.chunkVisible("Z_Oilpress2", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_ATA1", false);
            this.mesh.chunkVisible("Z_ATA2", false);
            this.mesh.chunkVisible("Z_Fuel1", false);
        }
        this.retoggleLight();
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManf1;
    private float              pictManf2;
    private static final float speedometerScale[] = { 0.0F, 21F, 69.5F, 116F, 163F, 215.5F, 266.5F, 318.5F, 378F, 430.5F };
    private static final float variometerScale[]  = { 0.0F, 47F, 82F, 97F, 112F, 111.7F, 132F };
    private static final float rpmScale[]         = { 0.0F, 2.5F, 19F, 50.5F, 102.5F, 173F, 227F, 266.5F, 297F };
    private float              aAim;
    private float              tAim;
    private float              kAim;
    private boolean            bEntered;

}
