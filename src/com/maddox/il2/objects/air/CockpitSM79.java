package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitSM79 extends CockpitPilot {
    private class Variables {

        float      dimPos;
        float      throttleC;
        float      throttleR;
        float      throttleL;
        float      propC;
        float      propL;
        float      propR;
        float      MixC;
        float      MixL;
        float      MixR;
        float      altimeter;
        float      elevTrim;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      beaconDirection;

        private Variables() {
            this.dimPos = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitSM79.this.fm != null) {
                CockpitSM79.this.setTmp = CockpitSM79.this.setOld;
                CockpitSM79.this.setOld = CockpitSM79.this.setNew;
                CockpitSM79.this.setNew = CockpitSM79.this.setTmp;
                if (CockpitSM79.this.cockpitDimControl) {
                    if (CockpitSM79.this.setNew.dimPos < 1.0F) {
                        CockpitSM79.this.setNew.dimPos = CockpitSM79.this.setOld.dimPos + 0.03F;
                    }
                } else if (CockpitSM79.this.setNew.dimPos > 0.0F) {
                    CockpitSM79.this.setNew.dimPos = CockpitSM79.this.setOld.dimPos - 0.03F;
                }
                CockpitSM79.this.setNew.throttleC = (0.85F * CockpitSM79.this.setOld.throttleC) + (CockpitSM79.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitSM79.this.setNew.throttleL = (0.85F * CockpitSM79.this.setOld.throttleL) + (CockpitSM79.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitSM79.this.setNew.throttleR = (0.85F * CockpitSM79.this.setOld.throttleR) + (CockpitSM79.this.fm.EI.engines[2].getControlThrottle() * 0.15F);
                CockpitSM79.this.setNew.propC = (0.85F * CockpitSM79.this.setOld.propC) + (CockpitSM79.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitSM79.this.setNew.propL = (0.85F * CockpitSM79.this.setOld.propL) + (CockpitSM79.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitSM79.this.setNew.propR = (0.85F * CockpitSM79.this.setOld.propR) + (CockpitSM79.this.fm.EI.engines[2].getControlProp() * 0.15F);
                CockpitSM79.this.setNew.MixC = (0.85F * CockpitSM79.this.setOld.MixC) + (CockpitSM79.this.fm.EI.engines[1].getControlMix() * 0.15F);
                CockpitSM79.this.setNew.MixL = (0.85F * CockpitSM79.this.setOld.MixL) + (CockpitSM79.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitSM79.this.setNew.MixR = (0.85F * CockpitSM79.this.setOld.MixR) + (CockpitSM79.this.fm.EI.engines[2].getControlMix() * 0.15F);
                CockpitSM79.this.setNew.elevTrim = (0.85F * CockpitSM79.this.setOld.elevTrim) + (CockpitSM79.this.fm.CT.getTrimElevatorControl() * 0.15F);
                CockpitSM79.this.setNew.altimeter = CockpitSM79.this.fm.getAltitude();
                if (Math.abs(CockpitSM79.this.fm.Or.getKren()) < 30F) {
                    CockpitSM79.this.setNew.azimuth.setDeg(CockpitSM79.this.setOld.azimuth.getDeg(1.0F), 90F + CockpitSM79.this.fm.Or.azimut());
                }
                float f = CockpitSM79.this.waypointAzimuth();
                CockpitSM79.this.setNew.vspeed = ((199F * CockpitSM79.this.setOld.vspeed) + CockpitSM79.this.fm.getVertSpeed()) / 200F;
                if (CockpitSM79.this.useRealisticNavigationInstruments()) {
                    CockpitSM79.this.setNew.beaconDirection = ((10F * CockpitSM79.this.setOld.beaconDirection) + CockpitSM79.this.getBeaconDirection()) / 11F;
                } else {
                    CockpitSM79.this.setNew.waypointAzimuth.setDeg(CockpitSM79.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitSM79.this.setOld.azimuth.getDeg(1.0F)) + 90F);
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            ((SM79) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            this.aircraft().hierMesh().chunkVisible("fakePilot3_D0", true);
            if (!((SM79) this.fm.actor).bPilot3Killed) {
                this.mesh.chunkVisible("TailGunner", true);
            }
            if (this.firstEnter) {
                if (this.aircraft().thisWeaponsName.startsWith("1x")) {
                    this.mesh.chunkVisible("Torpsight_Base", true);
                    this.mesh.chunkVisible("Torpsight_Knob", true);
                    this.mesh.chunkVisible("Torpsight_Rot", true);
                }
                this.firstEnter = false;
            }
            this.mesh.chunkVisible("Tur2_DoorR_Int", this.aircraft().hierMesh().isChunkVisible("Tur2_DoorR_D0"));
            this.mesh.chunkVisible("Tur2_DoorR_open_Int", !this.aircraft().hierMesh().isChunkVisible("Tur2_DoorR_D0"));
            this.mesh.chunkVisible("Tur2_DoorL_Int", this.aircraft().hierMesh().isChunkVisible("Tur2_DoorR_D0"));
            this.mesh.chunkVisible("Tur2_DoorL_open_Int", !this.aircraft().hierMesh().isChunkVisible("Tur2_DoorR_D0"));
            this.mesh.chunkVisible("Tur2_Door1_Int", true);
            this.mesh.chunkVisible("Tur2_Door2_Int", true);
            this.mesh.chunkVisible("Tur2_Door3_Int", true);
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorL_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorR_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorL_open_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorR_open_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tur2_Door1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tur2_Door2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tur2_Door3_D0", false);
            this.aircraft().hierMesh().chunkVisible("WingWireL_D0", false);
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((SM79) this.fm.actor).bPitUnfocused = true;
            boolean flag = this.aircraft().isChunkAnyDamageVisible("Tail1_D");
            this.mesh.chunkVisible("TailGunner", false);
            if (!this.fm.AS.isPilotParatrooper(2)) {
                this.aircraft().hierMesh().chunkVisible("Pilot3_D0", !((SM79) this.fm.actor).bPilot3Killed);
                this.aircraft().hierMesh().chunkVisible("Pilot3_D1", ((SM79) this.fm.actor).bPilot3Killed);
            }
            if (flag) {
                this.aircraft().hierMesh().chunkVisible("Tur1_DoorL_D0", !this.aircraft().hierMesh().isChunkVisible("Tur1_DoorR_open_D0"));
                this.aircraft().hierMesh().chunkVisible("Tur1_DoorR_D0", !this.aircraft().hierMesh().isChunkVisible("Tur1_DoorR_open_D0"));
            }
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", flag);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", flag);
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", flag);
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorL_D0", !this.mesh.isChunkVisible("Tur2_DoorR_open_Int"));
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorL_open_D0", this.mesh.isChunkVisible("Tur2_DoorR_open_Int"));
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorR_D0", !this.mesh.isChunkVisible("Tur2_DoorR_open_Int"));
            this.aircraft().hierMesh().chunkVisible("Tur2_DoorR_open_D0", this.mesh.isChunkVisible("Tur2_DoorR_open_Int"));
            this.aircraft().hierMesh().chunkVisible("Tur2_Door1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Tur2_Door2_D0", true);
            this.aircraft().hierMesh().chunkVisible("Tur2_Door3_D0", true);
            this.aircraft().hierMesh().chunkVisible("WingWireL_D0", true);
            this.mesh.chunkVisible("Tur2_Door1_Int", false);
            this.mesh.chunkVisible("Tur2_Door2_Int", false);
            this.mesh.chunkVisible("Tur2_Door3_Int", false);
            this.mesh.chunkVisible("Tur2_DoorL_open_Int", false);
            this.mesh.chunkVisible("Tur2_DoorR_open_Int", false);
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
            super.doFocusLeave();
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    protected void mydebugcockpit(String s) {
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    public CockpitSM79() {
        super("3DO/Cockpit/SM79Pilot/hier.him", "bf109");
        this.firstEnter = true;
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(109F, 99F, 90F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        HookNamed hooknamed1 = new HookNamed(this.mesh, "LAMPHOOK2");
        Loc loc1 = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed1.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc1);
        this.light2 = new LightPointActor(new LightPoint(), loc1.getPoint());
        this.light2.light.setColor(109F, 99F, 90F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "SM79_gauges_1", "SM79_gauges_2", "SM79_gauges_3", "SM79_gauges_1_dmg", "Ita_Needles" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.aircraft().thisWeaponsName.startsWith("1x")) {
            this.mesh.chunkSetAngles("Torpsight_Rot", 0.0F, ((SM79) this.aircraft()).fSightCurSideslip, 0.0F);
            this.mesh.chunkSetAngles("Torpsight_Knob", 0.0F, 2.0F * ((SM79) this.aircraft()).fSightCurSideslip, 0.0F);
        }
        this.aircraft().hierMesh().chunkVisible("Tur2_DoorL_D0", false);
        this.aircraft().hierMesh().chunkVisible("Tur2_DoorR_D0", false);
        this.aircraft().hierMesh().chunkVisible("Tur2_DoorL_open_D0", false);
        this.aircraft().hierMesh().chunkVisible("Tur2_DoorR_open_D0", false);
        float f1 = this.fm.CT.getCockpitDoor();
        if (f1 < 0.99F) {
            if (!this.mesh.isChunkVisible("Tur2_DoorR_Int")) {
                this.mesh.chunkVisible("Tur2_DoorR_Int", true);
                this.mesh.chunkVisible("Tur2_DoorR_open_Int", false);
                this.mesh.chunkVisible("Tur2_DoorL_Int", true);
                this.mesh.chunkVisible("Tur2_DoorL_open_Int", false);
            }
            float f2 = 13.8F * f1;
            this.mesh.chunkSetAngles("Tur2_Door1_Int", 0.0F, -f2, 0.0F);
            f2 = 8.8F * f1;
            this.mesh.chunkSetAngles("Tur2_Door2_Int", 0.0F, -f2, 0.0F);
            f2 = 3.1F * f1;
            this.mesh.chunkSetAngles("Tur2_Door3_Int", 0.0F, -f2, 0.0F);
            f2 = 14F * f1;
            this.mesh.chunkSetAngles("Tur2_DoorL_Int", 0.0F, -f2, 0.0F);
            this.mesh.chunkSetAngles("Tur2_DoorR_Int", 0.0F, f2, 0.0F);
        } else {
            this.mesh.chunkVisible("Tur2_DoorR_Int", false);
            this.mesh.chunkVisible("Tur2_DoorR_open_Int", true);
            this.mesh.chunkVisible("Tur2_DoorL_Int", false);
            this.mesh.chunkVisible("Tur2_DoorL_open_Int", true);
            this.mesh.chunkSetAngles("Tur2_Door1_Int", 0.0F, -13.8F, 0.0F);
            this.mesh.chunkSetAngles("Tur2_Door2_Int", 0.0F, -8.8F, 0.0F);
            this.mesh.chunkSetAngles("Tur2_Door3_Int", 0.0F, -3.1F, 0.0F);
        }
        this.mesh.chunkSetAngles("Mirino_02", 0.0F, this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 80F), 0.0F);
        this.mesh.chunkSetAngles("ZSpeedL", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), CockpitSM79.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("ZSpeedR", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 460F, 0.0F, 23F), CockpitSM79.speedometerScale), 0.0F);
        this.mesh.chunkVisible("ZlampRedL", (this.fm.CT.getPowerControl() < 0.15F) && (this.fm.CT.getGear() < 0.99F));
        this.mesh.chunkVisible("ZlampRedR", (this.fm.CT.getPowerControl() < 0.15F) && (this.fm.CT.getGear() < 0.99F));
        this.buzzerFX((this.fm.CT.getPowerControl() < 0.15F) && (this.fm.CT.getGear() < 0.99F));
        this.mesh.chunkVisible("ZlampGreenL", (this.fm.CT.getPowerControl() < 0.15F) && (this.fm.CT.getGear() > 0.99F));
        this.mesh.chunkVisible("ZlampGreenR", (this.fm.CT.getPowerControl() < 0.15F) && (this.fm.CT.getGear() > 0.99F));
        this.mesh.chunkSetAngles("Mplane_LandingGear_L", 0.0F, 0.0F, 10F * this.fm.CT.getGear());
        this.mesh.chunkSetAngles("Mplane_LandingGear_R", 0.0F, 0.0F, 10F * this.fm.CT.getGear());
        this.mesh.chunkSetAngles("Mplane_LandingGear_L1", 0.0F, 90F * this.fm.CT.getGear(), 0.0F);
        this.mesh.chunkSetAngles("Mplane_LandingGear_R1", 0.0F, -90F * this.fm.CT.getGear(), 0.0F);
        this.mesh.chunkSetAngles("Zgear", 30F * (this.pictGear = (0.89F * this.pictGear) + (0.11F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        float f3 = this.interp(this.setNew.altimeter, this.setOld.altimeter, f) * 0.036F;
        this.mesh.chunkSetAngles("Zalt1L", 0.0F, f3 * 10F, 0.0F);
        this.mesh.chunkSetAngles("Zalt1R", 0.0F, f3 * 10F, 0.0F);
        this.mesh.chunkSetAngles("Zalt2L", 0.0F, f3, 0.0F);
        this.mesh.chunkSetAngles("Zalt2R", 0.0F, f3, 0.0F);
        this.mesh.chunkSetAngles("ZclockHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("ZclockMin", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        float f4 = 0.104F;
        this.mesh.chunkSetAngles("ZRPML", 0.0F, this.fm.EI.engines[0].getRPM() * f4, 0.0F);
        this.mesh.chunkSetAngles("ZRPMC", 0.0F, this.fm.EI.engines[1].getRPM() * f4, 0.0F);
        this.mesh.chunkSetAngles("ZRPMR", 0.0F, this.fm.EI.engines[2].getRPM() * f4, 0.0F);
        this.mesh.chunkSetAngles("ZbarometrL", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrC", 0.0F, this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrR", 0.0F, this.cvt(this.fm.EI.engines[2].getManifoldPressure(), 0.0F, 2.0F, 0.0F, 312F), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrEL", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 40F, 160F, 0.0F, 6F), CockpitSM79.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUL", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 160F, 0.0F, 6F), CockpitSM79.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrEC", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].tOilIn, 40F, 160F, 0.0F, 6F), CockpitSM79.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUC", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 40F, 160F, 0.0F, 6F), CockpitSM79.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrER", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[2].tOilIn, 40F, 160F, 0.0F, 6F), CockpitSM79.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZbarometrUR", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[2].tOilOut, 40F, 160F, 0.0F, 6F), CockpitSM79.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("ZoilL", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZoilC", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZoilR", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[2].tOilOut * this.fm.EI.engines[2].getReadyness()), 0.0F, 10F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelL", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelC", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZfuelR", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        float f5 = this.setNew.vspeed * 18F;
        this.mesh.chunkSetAngles("ZclimbL", 0.0F, f5, 0.0F);
        this.mesh.chunkSetAngles("ZclimbR", 0.0F, f5, 0.0F);
        this.mesh.chunkSetAngles("ZThrottleL", 0.0F, -49F * this.interp(this.setNew.throttleL, this.setOld.throttleL, f), 0.0F);
        this.mesh.chunkSetAngles("ZThrottleC", 0.0F, -49F * this.interp(this.setNew.throttleC, this.setOld.throttleC, f), 0.0F);
        this.mesh.chunkSetAngles("ZThrottleR", 0.0F, -49F * this.interp(this.setNew.throttleR, this.setOld.throttleR, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreL", 0.0F, 35F * this.interp(this.setNew.MixL, this.setOld.MixL, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreC", 0.0F, 35F * this.interp(this.setNew.MixC, this.setOld.MixC, f), 0.0F);
        this.mesh.chunkSetAngles("MixturreR", 0.0F, 35F * this.interp(this.setNew.MixR, this.setOld.MixR, f), 0.0F);
        this.mesh.chunkSetAngles("PitchL", 0.0F, -40F * this.interp(this.setNew.propL, this.setOld.propL, f), 0.0F);
        this.mesh.chunkSetAngles("PitchC", 0.0F, -40F * this.interp(this.setNew.propC, this.setOld.propC, f), 0.0F);
        this.mesh.chunkSetAngles("PitchR", 0.0F, -40F * this.interp(this.setNew.propR, this.setOld.propR, f), 0.0F);
        this.mesh.chunkSetAngles("Wheel_PilotL", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_PilotR", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Wheel_Stick", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresL", 0.0F, 7F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresR", 0.0F, -7F * this.fm.CT.getBrake(), 0.0F);
        this.resetYPRmodifier();
        float f6 = 0.07F * this.fm.CT.getRudder();
        Cockpit.xyz[1] = f6;
        this.mesh.chunkSetLocate("Pedal_LeftL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Pedal_LeftR", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -f6;
        this.mesh.chunkSetLocate("Pedal_RightL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Pedal_RightR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("ZmagnetoL", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("ZmagnetoC", 0.0F, this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("ZmagnetoR", 0.0F, this.cvt(this.fm.EI.engines[2].getControlMagnetos(), 0.0F, 3F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroC", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroL", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zdirectional_GiroR", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Zbank2L", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zbank1L", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("Zbank2R", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zbank1R", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("ZAH1R", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.011F, -0.015F);
        this.mesh.chunkSetLocate("ZcompassR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zAH1L", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02F, -0.02F);
        this.mesh.chunkSetLocate("ZcompassL", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_plane_FuelGaugeL", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1720F, 0.0F, 245F), 0.0F);
        this.mesh.chunkSetAngles("Z_plane_FuelGaugeR", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1720F, 0.0F, 245F), 0.0F);
        this.mesh.chunkSetAngles("Ztrim1", 0.0F, this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, -750F, 750F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.cvt(this.interp(this.setNew.elevTrim, this.setOld.elevTrim, f), -0.5F, 0.5F, -0.057F, 0.058F);
        this.mesh.chunkSetLocate("Z_Trim_Indicator", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_OAT", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -40F, 40F, 0.0F, 93F), 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            float f7 = -this.cvt(this.setNew.beaconDirection, -55F, 55F, -55F, 55F);
            this.mesh.chunkSetAngles("Zcourse", 0.0F, f7, 0.0F);
        } else {
            float f8 = -this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.1F), -55F, 55F, -55F, 55F);
            this.mesh.chunkSetAngles("Zcourse", 0.0F, f8, 0.0F);
        }
    }

    public void reflectCockpitState() {
        if (!this.fm.AS.isPilotDead(2)) {
            this.mesh.chunkVisible("TailGunner", true);
        } else {
            this.mesh.chunkVisible("TailGunner", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Line01", false);
            this.mesh.chunkVisible("Panel_dmg", true);
            this.mesh.chunkVisible("PressL_dmg", true);
            this.mesh.chunkVisible("XGlassHoles1", true);
            this.mesh.chunkVisible("RPMC_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Line01", false);
            this.mesh.chunkVisible("Panel_dmg", true);
            this.mesh.chunkVisible("AltL_dmg", true);
            this.mesh.chunkVisible("RPMC_dmg", true);
            this.mesh.chunkVisible("OilTempC_dmg", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
            this.mesh.chunkVisible("XGlassHoles1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Line01", false);
            this.mesh.chunkVisible("Panel_dmg", true);
            this.mesh.chunkVisible("ClockL_dmg", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
            this.mesh.chunkVisible("XGlassHoles1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {

        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassHoles2", true);
            this.mesh.chunkVisible("PressL_dmg", true);
            this.mesh.chunkVisible("OilTempC_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Line01", false);
            this.mesh.chunkVisible("Panel_dmg", true);
            this.mesh.chunkVisible("ClockL_dmg", true);
            this.mesh.chunkVisible("AltL_dmg", true);
            this.mesh.chunkVisible("XGlassHoles2", true);
            this.mesh.chunkVisible("XGlassHoles1", true);
            this.mesh.chunkVisible("RPMC_dmg", true);
            this.mesh.chunkVisible("PressL_dmg", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0032F, 7.2F);
            this.light2.light.setEmit(0.003F, 7F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
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
    }

    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            firstEnter;
    private static final float speedometerScale[] = { 0.0F, 10F, 20F, 30F, 50F, 68F, 88F, 109F, 126F, 142F, 159F, 176F, 190F, 206F, 220F, 238F, 253F, 270F, 285F, 300F, 312F, 325F, 337F, 350F, 360F };
    private static final float oilTempScale[]     = { 0.0F, 26F, 54F, 95F, 154F, 244F, 359F };
    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictGear;

}
