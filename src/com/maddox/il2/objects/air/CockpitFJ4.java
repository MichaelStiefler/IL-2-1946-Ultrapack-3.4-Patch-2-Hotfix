package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitFJ4 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFJ4.this.fm != null) {
                CockpitFJ4.this.setTmp = CockpitFJ4.this.setOld;
                CockpitFJ4.this.setOld = CockpitFJ4.this.setNew;
                CockpitFJ4.this.setNew = CockpitFJ4.this.setTmp;
                CockpitFJ4.this.setNew.throttle = (0.85F * CockpitFJ4.this.setOld.throttle) + (CockpitFJ4.this.fm.CT.PowerControl * 0.15F);
                CockpitFJ4.this.setNew.prop = (0.85F * CockpitFJ4.this.setOld.prop) + (CockpitFJ4.this.fm.CT.getStepControl() * 0.15F);
                CockpitFJ4.this.setNew.stage = (0.85F * CockpitFJ4.this.setOld.stage) + (CockpitFJ4.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitFJ4.this.setNew.mix = (0.85F * CockpitFJ4.this.setOld.mix) + (CockpitFJ4.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitFJ4.this.setNew.altimeter = CockpitFJ4.this.fm.getAltitude();
                float f = CockpitFJ4.this.waypointAzimuth();
                CockpitFJ4.this.setNew.waypointAzimuth.setDeg(CockpitFJ4.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitFJ4.this.setOld.azimuth.getDeg(1.0F));
                CockpitFJ4.this.setNew.azimuth.setDeg(CockpitFJ4.this.setOld.azimuth.getDeg(1.0F), CockpitFJ4.this.fm.Or.azimut());
                CockpitFJ4.this.setNew.vspeed = ((199F * CockpitFJ4.this.setOld.vspeed) + CockpitFJ4.this.fm.getVertSpeed()) / 200F;
                float f1 = ((FJ_4) CockpitFJ4.this.aircraft()).k14Distance;
                CockpitFJ4.this.setNew.k14w = (5F * CockpitFJ4.k14TargetWingspanScale[((FJ_4) CockpitFJ4.this.aircraft()).k14WingspanType]) / f1;
                CockpitFJ4.this.setNew.k14w = (0.9F * CockpitFJ4.this.setOld.k14w) + (0.1F * CockpitFJ4.this.setNew.k14w);
                CockpitFJ4.this.setNew.k14wingspan = (0.9F * CockpitFJ4.this.setOld.k14wingspan) + (0.1F * CockpitFJ4.k14TargetMarkScale[((FJ_4) CockpitFJ4.this.aircraft()).k14WingspanType]);
                CockpitFJ4.this.setNew.k14mode = (0.8F * CockpitFJ4.this.setOld.k14mode) + (0.2F * ((FJ_4) CockpitFJ4.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitFJ4.this.aircraft().FM.getW();
                double d = 0.00125D * f1;
                float f2 = (float) Math.toDegrees(d * vector3d.z);
                float f3 = -(float) Math.toDegrees(d * vector3d.y);
                float f4 = CockpitFJ4.this.floatindex((f1 - 200F) * 0.04F, CockpitFJ4.k14BulletDrop) - CockpitFJ4.k14BulletDrop[0];
                f3 += (float) Math.toDegrees(Math.atan(f4 / f1));
                CockpitFJ4.this.setNew.k14x = (0.92F * CockpitFJ4.this.setOld.k14x) + (0.08F * f2);
                CockpitFJ4.this.setNew.k14y = (0.92F * CockpitFJ4.this.setOld.k14y) + (0.08F * f3);
                if (CockpitFJ4.this.setNew.k14x > 7F) {
                    CockpitFJ4.this.setNew.k14x = 7F;
                }
                if (CockpitFJ4.this.setNew.k14x < -7F) {
                    CockpitFJ4.this.setNew.k14x = -7F;
                }
                if (CockpitFJ4.this.setNew.k14y > 7F) {
                    CockpitFJ4.this.setNew.k14y = 7F;
                }
                if (CockpitFJ4.this.setNew.k14y < -7F) {
                    CockpitFJ4.this.setNew.k14y = -7F;
                }
                CockpitFJ4.this.buzzerFX((CockpitFJ4.this.fm.getSpeed() < 63F) && (CockpitFJ4.this.fm.Gears.nOfGearsOnGr < 1));
                if (CockpitFJ4.this.fm.EI.engines[0].getStage() < 6) {
                    if (CockpitFJ4.this.setNew.stbyPosition > 0.0F) {
                        CockpitFJ4.this.setNew.stbyPosition = CockpitFJ4.this.setOld.stbyPosition - 0.002F;
                    }
                } else if (CockpitFJ4.this.setNew.stbyPosition < 1.0F) {
                    CockpitFJ4.this.setNew.stbyPosition = CockpitFJ4.this.setOld.stbyPosition + 0.002F;
                }
                if (CockpitFJ4.this.setNew.stbyPosition2 < 1.0F) {
                    CockpitFJ4.this.setNew.stbyPosition2 = CockpitFJ4.this.setOld.stbyPosition2 + 0.002F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      stage;
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;
        float      stbyPosition;
        float      stbyPosition2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    private boolean hasHydraulicPressure() {
        return ((FJ_4) this.aircraft()).hasHydraulicPressure;
    }

    public CockpitFJ4() {
        super("3DO/Cockpit/CockpitFJ4/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "Sabre_Compass1", "Needles86", "Gaug86_01", "Gaug86_02", "Gaug86_03", "Gaug86_04", "Gaug86_05", "Gaug86_06" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
    }

    private int iLockState() {
        if (!(this.aircraft() instanceof TypeGuidedMissileCarrier)) {
            return 0;
        } else {
            return ((FJ_4B) this.aircraft()).getMissileLockState();
        }
    }

    private float machNumber() {
        return ((FJ_4) this.aircraft()).calculateMach();
    }

    public void reflectWorldToInstruments(float f) {
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            if (FJ_4.hunted != null) {
                this.mesh.chunkVisible("Z_RadarTarget", true);
            } else {
                this.mesh.chunkVisible("Z_RadarTarget", false);
            }
            int i = ((FJ_4) this.aircraft()).k14Mode;
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            boolean flag = i < 2;
            this.mesh.chunkVisible("Z_CollimateurLamp", true);
            this.mesh.chunkVisible("Z_GunLamp", true);
            this.mesh.chunkVisible("Colli01", false);
            this.mesh.chunkVisible("Colli02", true);
            this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            this.mesh.chunkVisible("Gun01", false);
            this.mesh.chunkVisible("Gun02", true);
            this.mesh.chunkVisible("Contact02a", false);
            this.mesh.chunkVisible("Contact02b", true);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.setNew.k14w;
            for (int j = 1; j < 11; j++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }

            if (i == 1) {
                this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
                this.mesh.chunkVisible("Z_CageSelect1", false);
                this.mesh.chunkVisible("Z_CageSelect2", true);
            }
            if (i == 0) {
                this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, 0.0F, 0.0F);
                this.mesh.chunkVisible("Z_CageSelect1", true);
                this.mesh.chunkVisible("Z_CageSelect2", false);
            }
            if (!flag) {
                this.mesh.chunkVisible("Z_SightCaged", false);
                this.mesh.chunkVisible("Z_SightUncaged", false);
                this.mesh.chunkVisible("Z_CollimateurLamp", false);
                this.mesh.chunkVisible("Z_GunLamp", false);
                this.mesh.chunkVisible("Colli01", true);
                this.mesh.chunkVisible("Colli02", false);
                this.mesh.chunkVisible("Gun01", true);
                this.mesh.chunkVisible("Gun02", false);
                this.mesh.chunkVisible("Contact02a", true);
                this.mesh.chunkVisible("Contact02b", false);
            }
        }
        this.mesh.chunkSetAngles("Z_PedalRight", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalLeft", -10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle86", 40F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", -62F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirBrakes", 29F * this.fm.CT.AirBrakeControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HookControl", 29F * this.fm.CT.arrestorControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hydro-Pres86", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition, this.setOld.stbyPosition, f), 0.0F, 1.0F, -253F, 10F), 0.0F);
        this.mesh.chunkSetAngles("Z_Ammeter86", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition2, this.setOld.stbyPosition2, f), 0.0F, 1.0F, -55F, 30F), 0.0F);
        this.resetYPRmodifier();
        float f1 = this.fm.EI.engines[0].getStage();
        if ((f1 > 0.0F) && (f1 < 7F)) {
            f1 = 0.0345F;
        } else {
            f1 = -0.05475F;
        }
        Cockpit.xyz[2] = f1;
        this.mesh.chunkSetAngles("Z_Stick01", (this.pictElev = (0.7F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F);
        this.mesh.chunkSetAngles("Z_Stick02", (this.pictElev = (0.7F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = -0.0029F;
        }
        this.mesh.chunkSetAngles("Z_Target1", -1.2F * this.setNew.k14wingspan, 0.0F, 0.0F);
        if ((this.machNumber() < 0.95F) || (this.machNumber() > 1.0F)) {
            this.mesh.chunkSetAngles("Z_Speedometer86-1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()) * 1.131F, 74.07994F, 1222.319F, 0.0F, 14F), CockpitFJ4.speedometerScale), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Speedometer86-2", this.floatindex(this.cvt((this.machNumber() * 1.4F) - 0.4F, 0.3F, 1.5F, 0.0F, 14F), CockpitFJ4.speedometerScale), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Speedometer86-3", this.floatindex(this.cvt(this.fm.getSpeedKMH() * 1.131F, 74.07994F, 1222.319F, 0.0F, 14F), CockpitFJ4.speedometerScale), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Climb86", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitFJ4.variometerScale), 0.0F, 0.0F);
            Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0362F, -0.0362F);
            this.mesh.chunkSetAngles("Z_Altimeter86_1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter86_2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter86_3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Altimeter86_4", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, -180F, 145F), 0.0F, 0.0F);
            this.resetYPRmodifier();
        }
        this.mesh.chunkSetAngles("Z_Hour86", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute86", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second86", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getPowerOutput();
        this.mesh.chunkSetAngles("Z_Fuel-Pres86", this.cvt((float) Math.sqrt(f1), 0.0F, 1.0F, -155F, 155F), 0.0F, 0.0F);
        f1 = this.cvt(this.fm.M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if (f1 < 45F) {
            f1 = this.cvt(f1, 0.0F, 45F, -58F, 45F);
        }
        f1 += 58F;
        this.mesh.chunkSetAngles("Z_Oilpres86", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 18F, 115F, 410F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM86-1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2550F, -110F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM86-2", this.cvt(this.fm.EI.engines[0].getRPM(), 2550F, 3500F, -10F, 335F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Suction86", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 100F, 395F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Exhaust_Temp", this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass-Emerg1", this.cvt(this.fm.Or.getTangage(), -30F, 30F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass-Emerg3", this.cvt(this.fm.Or.getKren(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass-Emerg2", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank86-1", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Fuel86", this.cvt(this.fm.M.fuel, 0.0F, 1650F, -150F, 150F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank86-2", this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_G-Factor", this.cvt(this.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Gear86Green1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_Gear86Green2", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_Gear86Green3", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_Gear86Red1", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Gear86Red2", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Gear86Red3", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_BrakeOut", this.fm.CT.getAirBrake() > 0.01F);
        this.mesh.chunkVisible("Z_BrakeIn", this.fm.CT.getAirBrake() < 0.01F);
        this.mesh.chunkVisible("Z_Flap0", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("Z_Flap1", (this.fm.CT.getFlap() > 0.1F) & (this.fm.CT.getFlap() < 0.6F));
        this.mesh.chunkVisible("Z_Flap2", this.fm.CT.getFlap() > 0.6F);
        this.mesh.chunkVisible("Z_CanopyUnlocked", this.fm.CT.getCockpitDoor() > 0.001F);
        this.mesh.chunkVisible("Z_CanopyLocked", this.fm.CT.getCockpitDoor() < 0.001F);
        this.mesh.chunkVisible("Z_HookOut", this.fm.CT.getArrestor() > 0.01F);
        this.mesh.chunkVisible("Z_HookIn", this.fm.CT.getArrestor() < 0.01F);
        this.mesh.chunkVisible("Z_FuelLamp", (this.fm.M.fuel < 200F) & (this.fm.M.fuel > 100F));
        this.mesh.chunkVisible("Z_FuelLampb", this.fm.M.fuel < 100F);
        this.mesh.chunkVisible("Z_ExtLamp", this.fm.EI.engines[0].tOilOut > 70F);
        this.mesh.chunkVisible("Z_EngOut", this.fm.EI.engines[0].getRPM() < 300F);
        this.mesh.chunkVisible("Z_EngRun", this.fm.EI.engines[0].getRPM() > 300F);
        if (this.fm.EI.engines[0].getRPM() > 50F) {
            this.mesh.chunkVisible("Z_TrimLamp", Math.abs(this.fm.CT.getTrimElevatorControl()) < 0.05F);
        }
        if (this.fm.EI.engines[0].getRPM() < 50F) {
            this.mesh.chunkVisible("Z_TrimLamp", false);
        }
        if (this.fm.Gears.nOfGearsOnGr < 3) {
            this.mesh.chunkVisible("Z_TrimLamp", false);
        }
        this.mesh.chunkVisible("Z_FireLamp1", this.fm.AS.astateEngineStates[0] > 2);
        this.mesh.chunkVisible("Z_FireLamp2", this.fm.AS.astateEngineStates[0] > 2);
        this.mesh.chunkSetAngles("Z_horizont1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        this.mesh.chunkSetLocate("Z_horizont1b", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Compass86_1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth86_1", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(f1, 0.01F, 0.08F, 0.0F, 0.1F);
        this.mesh.chunkSetAngles("Z_GearHandle", 0.0F, 0.0F, 50F * (this.pictGear = (0.82F * this.pictGear) + (0.18F * this.fm.CT.GearControl)));
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(f1, 0.01F, 0.08F, 0.0F, 0.1F);
        this.mesh.chunkSetAngles("Z_GearHandle", 0.0F, 0.0F, 50F * (this.pictGear = (0.82F * this.pictGear) + (0.18F * this.fm.CT.GearControl)));
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, -0.625F, 0.0F);
        this.mesh.chunkSetLocate("Canopy-Open1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Canopy-Open2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Canopy-Open3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Canopy-Open4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Canopy-Open5", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.AS.bIsAboutToBailout) {
            this.mesh.chunkVisible("Canopy-Open1", false);
            this.mesh.chunkVisible("Canopy-Open2", false);
            this.mesh.chunkVisible("Canopy-Open3", false);
            this.mesh.chunkVisible("Canopy-Open4", false);
            this.mesh.chunkVisible("Canopy-Open5", false);
        }
        if ((this.fm.Gears.nOfGearsOnGr > 0) && !this.hasHydraulicPressure()) {
            this.mesh.chunkVisible("ContactHydro1", true);
            this.mesh.chunkVisible("ContactHydro2", false);
        } else if (this.fm.Gears.nOfGearsOnGr == 0) {
            if ((this.fm.EI.engines[0].getStage() < 6) && this.hasHydraulicPressure()) {
                this.mesh.chunkVisible("ContactHydro1", false);
                this.mesh.chunkVisible("ContactHydro2", true);
                this.mesh.chunkVisible("Z_HydroLamp", true);
            } else if (!this.hasHydraulicPressure()) {
                this.mesh.chunkVisible("ContactHydro1", true);
                this.mesh.chunkVisible("ContactHydro2", false);
                this.mesh.chunkVisible("Z_HydroLamp", false);
            }
        }
        if (this.fm.EI.isSelectionHasControlExtinguisher()) {
            this.mesh.chunkVisible("Extinguisher1", true);
            this.mesh.chunkVisible("Extinguisher2", false);
        }
        if (!this.fm.EI.isSelectionHasControlExtinguisher()) {
            this.mesh.chunkVisible("Extinguisher1", false);
            this.mesh.chunkVisible("Extinguisher2", true);
        }
        if (this.fm.CT.cockpitDoorControl == 0.0F) {
            this.mesh.chunkVisible("Canopy_Contact2", false);
            this.mesh.chunkVisible("Canopy_Contact1", true);
        }
        if (this.fm.CT.cockpitDoorControl == 1.0F) {
            this.mesh.chunkVisible("Canopy_Contact1", false);
            this.mesh.chunkVisible("Canopy_Contact2", true);
        }
        if (this.fm.actor instanceof TypeGuidedMissileCarrier) {
            this.mesh.chunkVisible("MissileControl", true);
        } else {
            this.mesh.chunkVisible("MissileControl", false);
        }
        if (this.fm.actor instanceof TypeGuidedMissileCarrier) {
            if (this.iLockState() == 2) {
                this.mesh.chunkVisible("Z_Locked", true);
            } else {
                this.mesh.chunkVisible("Z_Locked", false);
            }
            if ((this.fm.getOverload() > 2.0F) || (this.fm.getOverload() < 0.0F)) {
                this.mesh.chunkVisible("Z_OverG", true);
            } else {
                this.mesh.chunkVisible("Z_OverG", false);
            }
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("Glass_Dam", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for (int i = 1; i < 11; i++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);
            }

            this.mesh.chunkVisible("Gaug86_02", false);
            this.mesh.chunkVisible("Gaug86_02_Dam", true);
            this.mesh.chunkVisible("Gaug86_03", false);
            this.mesh.chunkVisible("Gaug86_03_Dam", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("Z_InstrLamp", true);
            this.mesh.chunkVisible("Z_Speedometer86-1", false);
            this.mesh.chunkVisible("Z_Speedometer86-3", false);
            this.mesh.chunkVisible("Z_Altimeter86_1", false);
            this.mesh.chunkVisible("Z_Altimeter86_2", false);
            this.mesh.chunkVisible("Z_Altimeter86_3", false);
            this.mesh.chunkVisible("Z_Altimeter86_4", false);
            this.mesh.chunkVisible("Z_Compass86_1", false);
            this.mesh.chunkVisible("Z_Azimuth86_1", false);
            this.mesh.chunkVisible("Z_Azimuth86_2", false);
            this.mesh.chunkVisible("Z_horizont1b", false);
            this.mesh.chunkVisible("Z_Compass-Emerg1", false);
            this.mesh.chunkVisible("Z_Compass-Emerg3", false);
            this.mesh.chunkVisible("Z_Compass-Emerg2", false);
            this.mesh.chunkVisible("Z_horizont1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gaug86_02", false);
            this.mesh.chunkVisible("Gaug86_02_Dam", true);
            this.mesh.chunkVisible("Gaug86_03", false);
            this.mesh.chunkVisible("Gaug86_03_Dam", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("Z_InstrLamp", true);
            this.mesh.chunkVisible("Z_Speedometer86-1", false);
            this.mesh.chunkVisible("Z_Speedometer86-3", false);
            this.mesh.chunkVisible("Z_Altimeter86_1", false);
            this.mesh.chunkVisible("Z_Altimeter86_2", false);
            this.mesh.chunkVisible("Z_Altimeter86_3", false);
            this.mesh.chunkVisible("Z_Altimeter86_4", false);
            this.mesh.chunkVisible("Z_Compass86_1", false);
            this.mesh.chunkVisible("Z_Azimuth86_1", false);
            this.mesh.chunkVisible("Z_Azimuth86_2", false);
            this.mesh.chunkVisible("Z_horizont1b", false);
            this.mesh.chunkVisible("Z_Compass-Emerg1", false);
            this.mesh.chunkVisible("Z_Compass-Emerg3", false);
            this.mesh.chunkVisible("Z_Compass-Emerg2", false);
            this.mesh.chunkVisible("Z_horizont1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlasslDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
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
    private float              pictGear;
    private static final float speedometerScale[]       = { 0.0F, 22.5F, 45F, 67.5F, 90F, 112.5F, 135F, 157.5F, 180F, 202.5F, 225F, 247.5F, 270F, 292.5F, 315F, 336.5F, 360F };
    private static final float variometerScale[]        = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
    private static final float k14TargetMarkScale[]     = { 0.0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };

}
