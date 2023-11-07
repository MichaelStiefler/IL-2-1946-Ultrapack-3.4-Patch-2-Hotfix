package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitFW_190V extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFW_190V.this.fm != null) {
                CockpitFW_190V.this.setTmp = CockpitFW_190V.this.setOld;
                CockpitFW_190V.this.setOld = CockpitFW_190V.this.setNew;
                CockpitFW_190V.this.setNew = CockpitFW_190V.this.setTmp;
                CockpitFW_190V.this.setNew.altimeter = CockpitFW_190V.this.fm.getAltitude();
                if (CockpitFW_190V.this.cockpitDimControl) {
                    if (CockpitFW_190V.this.setNew.dimPosition > 0.0F) {
                        CockpitFW_190V.this.setNew.dimPosition = CockpitFW_190V.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitFW_190V.this.setNew.dimPosition < 1.0F) {
                    CockpitFW_190V.this.setNew.dimPosition = CockpitFW_190V.this.setOld.dimPosition + 0.05F;
                }
                CockpitFW_190V.this.setNew.throttle = ((10F * CockpitFW_190V.this.setOld.throttle) + CockpitFW_190V.this.fm.CT.PowerControl) / 11F;
                CockpitFW_190V.this.setNew.vspeed = ((499F * CockpitFW_190V.this.setOld.vspeed) + CockpitFW_190V.this.fm.getVertSpeed()) / 500F;
                CockpitFW_190V.this.setNew.waypointAzimuth.setDeg(CockpitFW_190V.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitFW_190V.this.waypointAzimuth() - CockpitFW_190V.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitFW_190V.this.fm.Or.getKren()) < 30F) {
                    CockpitFW_190V.this.setNew.azimuth.setDeg(CockpitFW_190V.this.setOld.azimuth.getDeg(1.0F), CockpitFW_190V.this.fm.Or.azimut());
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitFW_190V(String hierfile, String cockpitName) {
        super(hierfile, cockpitName);
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_R");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.01F);
        Cockpit.ypr[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, -2F);
        this.mesh.chunkSetLocate("CanopyTop", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("EjectatorHandle", 2520F * this.fm.CT.getCockpitDoor(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CanopyFrameL", -this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CanopyFrameR", this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleALTKm", 0.0F, 0.0F, this.cvt(this.setNew.altimeter, 0.0F, 10000F, 0.0F, -180F));
        this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleManPress1", -this.cvt(this.fm.EI.engines[0].getManifoldPressure() * (this.fm.EI.engines[0].getRPM() / 2700F), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), CockpitFW_190V.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitFW_190V.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 4F), CockpitFW_190V.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleCoolantGauge", this.floatindex(this.cvt(this.fm.EI.engines[0].getReadyness() * 400F, 0.0F, 400F, 0.0F, 4F), CockpitFW_190V.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SwitchHandle", this.fm.CT.getRadiatorControl() * 5.5F * 36F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleAtmTemp", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 313.09F, -45F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleB", -this.cvt(this.fm.getOverload(), -4F, 8F, -110.76F, 221.53F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleBLimitMax", -this.cvt(6.3F, -4F, 8F, -110.76F, 221.53F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleBLimitMin", -this.cvt(-3.3F, -4F, 8F, -110.76F, 221.53F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleVerbrauch", -this.cvt(this.fm.EI.engines[0].getPowerOutput() * (this.fm.EI.engines[0].getRPM() / 5.4F), 0.0F, 900F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PowerOutput", this.floatindex(this.cvt(this.fm.EI.engines[0].getReadyness() * (this.fm.EI.engines[0].getRPM() / 2500F) * 110F, 0.0F, 120F, 0.0F, 3F), CockpitFW_190V.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleOilTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 98F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleWaterTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 98F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt(this.fm.EI.engines[0].getRPM() / 1500F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        if (this.fm.getOverload() < 0.0F) {
            this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (this.fm.EI.engines[0].getRPM() / 1300F) + (this.fm.getOverload() * 2.0F), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (this.fm.EI.engines[0].getRPM() / 1300F) + (this.fm.getOverload() / 2.0F), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        }
        if (this.fm.EI.engines[0].getRPM() < 200F) {
            this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(0.0F, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        }
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -6F, 6F, -50F, 50F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("NeedleAHTurn", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleAHBank", this.cvt(this.getBall(7D), -7F, 7F, 11F, -11F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleAHCyl", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("NeedleAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 12F, -12F));
        this.mesh.chunkSetAngles("RepeaterOuter", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RepeaterPlane", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleHBSmall", -105F + ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleHBLarge", -270F + ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleHClock", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleMClock", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleSClock", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("IgnitionSwitch", 24F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 20F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = -0.004F;
        }
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        float f2 = 0.0F;
        int i = 0;
        if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1] || this.fm.CT.saveWeaponControl[3]) {
            if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1]) {
                f2 = 20F;
            }
            this.tClap = Time.tickCounter() + World.Rnd().nextInt(500, 1000);
        }
        if (Time.tickCounter() < this.tClap) {
            i = 1;
        }
        this.mesh.chunkSetAngles("SecTrigger2", -(240F + f2) * (this.pictClap = (0.85F * this.pictClap) + (0.15F * i)), 0.0F, 0.0F);
        Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f) * 34F * 0.91F;
        Cockpit.xyz[2] = Cockpit.ypr[0] <= 7F ? 0.0F : -0.006F;
        this.mesh.chunkSetLocate("ThrottleQuad", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RPedalBase", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedalStrut", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedal", 0.0F, 0.0F, (-this.fm.CT.getRudder() * 15F) - (this.fm.CT.getBrake() * 15F));
        this.mesh.chunkSetAngles("LPedalBase", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedalStrut", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedal", 0.0F, 0.0F, (this.fm.CT.getRudder() * 15F) - (this.fm.CT.getBrake() * 15F));
        this.mesh.chunkVisible("XLampTankSwitch", this.fm.EI.engines[0].getStage() > 0.0F);
        this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 39.4F);
        this.mesh.chunkVisible("XLampFlapLPos_3", this.fm.CT.getFlap() > 0.9F);
        this.mesh.chunkVisible("XLampFlapLPos_2", (this.fm.CT.getFlap() > 0.1F) && (this.fm.CT.getFlap() < 0.5F));
        this.mesh.chunkVisible("XLampFlapLPos_1", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("XLampFlapRPos_3", this.fm.CT.getFlap() > 0.9F);
        this.mesh.chunkVisible("XLampFlapRPos_2", (this.fm.CT.getFlap() > 0.1F) && (this.fm.CT.getFlap() < 0.5F));
        this.mesh.chunkVisible("XLampFlapRPos_1", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearL_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearR_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearC_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearC_2", this.fm.CT.getGear() > 0.95F);
        float f3 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F;
        if (f3 < -16F) {
            this.mesh.chunkVisible("XLampPitot", true);
        } else if (f3 > -12F) {
            this.mesh.chunkVisible("XLampPitot", false);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0012F, 0.75F);
            this.light2.light.setEmit(0.0012F, 0.75F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0) || ((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0)) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.materialReplace("A4GP1", "DA4GP1");
            this.mesh.materialReplace("A4GP1_night", "DA4GP1_night");
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("NeedleManPress", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0) || ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.materialReplace("A4GP2", "DA4GP2");
            this.mesh.materialReplace("A4GP2_night", "DA4GP2_night");
            this.mesh.chunkVisible("NeedleAHBank", false);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = -0.01F;
            Cockpit.xyz[1] = -0.01F;
            Cockpit.ypr[0] = -5F;
            Cockpit.ypr[2] = -5F;
            this.mesh.chunkSetLocate("IPCentral", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("HullDamage2", true);
            this.mesh.materialReplace("A4GP3", "DA4GP3");
            this.mesh.materialReplace("A4GP3_night", "DA4GP3_night");
            this.mesh.chunkVisible("NeedleALT", false);
            this.mesh.chunkVisible("NeedleKMH", false);
        }
        this.retoggleLight();
    }

    Variables                  setOld;
    Variables                  setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    float                      pictAiler;
    float                      pictElev;
    static final float         speedometerScale[] = { 0.0F, 18.5F, 67F, 117F, 164F, 215F, 267F, 320F, 379F, 427F, 428F };
    static final float         rpmScale[]         = { 0.0F, 11.25F, 53F, 108F, 170F, 229F, 282F, 334F, 342.5F, 342.5F };
    static final float         fuelScale[]        = { 0.0F, 16F, 35F, 52.5F, 72F, 72F };
    static final float         vsiNeedleScale[]   = { 0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F };
    private static final float oilTempScale[]     = { 0.0F, 23F, 52F, 81F, 81F };
    int                        tClap;
    float                      pictClap;

}
