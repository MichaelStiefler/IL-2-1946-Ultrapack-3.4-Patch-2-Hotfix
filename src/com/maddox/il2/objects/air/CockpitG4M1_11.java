package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitG4M1_11 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitG4M1_11.this.fm != null) {
                CockpitG4M1_11.this.setTmp = CockpitG4M1_11.this.setOld;
                CockpitG4M1_11.this.setOld = CockpitG4M1_11.this.setNew;
                CockpitG4M1_11.this.setNew = CockpitG4M1_11.this.setTmp;
                CockpitG4M1_11.this.setNew.throttle1 = (0.9F * CockpitG4M1_11.this.setOld.throttle1) + (0.1F * CockpitG4M1_11.this.fm.EI.engines[0].getControlThrottle());
                CockpitG4M1_11.this.setNew.prop1 = (0.9F * CockpitG4M1_11.this.setOld.prop1) + (0.1F * CockpitG4M1_11.this.fm.EI.engines[0].getControlProp());
                CockpitG4M1_11.this.setNew.mix1 = (0.8F * CockpitG4M1_11.this.setOld.mix1) + (0.2F * CockpitG4M1_11.this.fm.EI.engines[0].getControlMix());
                CockpitG4M1_11.this.setNew.throttle2 = (0.9F * CockpitG4M1_11.this.setOld.throttle2) + (0.1F * CockpitG4M1_11.this.fm.EI.engines[1].getControlThrottle());
                CockpitG4M1_11.this.setNew.prop2 = (0.9F * CockpitG4M1_11.this.setOld.prop2) + (0.1F * CockpitG4M1_11.this.fm.EI.engines[1].getControlProp());
                CockpitG4M1_11.this.setNew.mix2 = (0.8F * CockpitG4M1_11.this.setOld.mix2) + (0.2F * CockpitG4M1_11.this.fm.EI.engines[1].getControlMix());
                CockpitG4M1_11.this.setNew.man1 = (0.92F * CockpitG4M1_11.this.setOld.man1) + (0.08F * CockpitG4M1_11.this.fm.EI.engines[0].getManifoldPressure());
                CockpitG4M1_11.this.setNew.man2 = (0.92F * CockpitG4M1_11.this.setOld.man2) + (0.08F * CockpitG4M1_11.this.fm.EI.engines[1].getManifoldPressure());
                CockpitG4M1_11.this.setNew.altimeter = CockpitG4M1_11.this.fm.getAltitude();
                if (Math.abs(CockpitG4M1_11.this.fm.Or.getKren()) < 30F) {
                    CockpitG4M1_11.this.setNew.azimuth.setDeg(CockpitG4M1_11.this.setOld.azimuth.getDeg(1.0F), CockpitG4M1_11.this.fm.Or.azimut());
                }
                CockpitG4M1_11.this.setNew.vspeed = ((199F * CockpitG4M1_11.this.setOld.vspeed) + CockpitG4M1_11.this.fm.getVertSpeed()) / 200F;
                if (CockpitG4M1_11.this.useRealisticNavigationInstruments()) {
                    CockpitG4M1_11.this.setNew.waypointAzimuth.setDeg(CockpitG4M1_11.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitG4M1_11.this.waypointAzimuth());
                } else {
                    CockpitG4M1_11.this.setNew.waypointAzimuth.setDeg(CockpitG4M1_11.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitG4M1_11.this.fm.Or.azimut() + 90F);
                }
                CockpitG4M1_11.this.setNew.beaconDirection = ((10F * CockpitG4M1_11.this.setOld.beaconDirection) + CockpitG4M1_11.this.getBeaconDirection()) / 11F;
                CockpitG4M1_11.this.setNew.beaconRange = ((10F * CockpitG4M1_11.this.setOld.beaconRange) + CockpitG4M1_11.this.getBeaconRange()) / 11F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      mix1;
        float      mix2;
        float      man1;
        float      man2;
        float      altimeter;
        AnglesFork azimuth;
        float      beaconDirection;
        float      beaconRange;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("CF_D0") || this.aircraft().hierMesh().isChunkVisible("CF_D1") || this.aircraft().hierMesh().isChunkVisible("CF_D2") || this.aircraft().hierMesh().isChunkVisible("CF_D3"));
        super.doFocusLeave();
    }

    public CockpitG4M1_11() {
        super("3DO/Cockpit/G4M1-11/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "gauges5", "GP1_d1", "GP1", "GP2_d1", "GP2", "GP3", "GP4_d1", "GP4", "GP5_d1", "GP5", "GP6_d1", "GP6", "GP7", "GP9", "throttle", "Volt_Tacho" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Z_ColumnL", 0.0F, 0.0F, -60.6F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)));
        this.mesh.chunkSetAngles("Z_ColumnR", 0.0F, 0.0F, -60.6F * this.pictAiler);
        this.mesh.chunkSetAngles("Z_Throtle1", 40F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 40F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 75.5F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 75.5F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 62.9F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 62.9F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPedal", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RPedalStep1", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RPedalStep2", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedal", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep1", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep2", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass5", -this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_AH3", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        this.mesh.chunkSetLocate("Z_AH4", Cockpit.xyz, Cockpit.ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        float f2 = this.getBall(7D);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(f2, -5F, 5F, 8.5F, -8.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(f2, -7F, 7F, 16F, -16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank5", this.cvt(f2, -7F, 7F, 16F, -16F), 0.0F, 0.0F);
        float f3 = this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), CockpitG4M1_11.speedometerScale);
        this.mesh.chunkSetAngles("Z_Speedometer1", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp3", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp4", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        float f1 = (Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F) + (44.4F * this.fm.EI.engines[0].getPowerOutput());
        if (f1 < 0.0F) {
            this.mesh.chunkSetAngles("Z_Temp5", this.cvt(f1, -40F, 0.0F, 0.0F, 45F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Temp5", this.cvt(f1, 0.0F, 60F, 45F, 90F), 0.0F, 0.0F);
        }
        f1 = (Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F) + (44.4F * this.fm.EI.engines[1].getPowerOutput());
        if (f1 < 0.0F) {
            this.mesh.chunkSetAngles("Z_Temp6", this.cvt(f1, -40F, 0.0F, 0.0F, 45F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Temp6", this.cvt(f1, 0.0F, 60F, 45F, 90F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Temp7", this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitG4M1_11.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp8", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitG4M1_11.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap1", this.cvt(this.fm.CT.getFlap(), 0.0F, 0.75F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap2", 57F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt((float) Math.sqrt(this.fm.M.fuel), 0.0F, 34.641F, 0.0F, 225F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", this.cvt((float) Math.sqrt(this.fm.M.fuel), 0.0F, 34.641F, 0.0F, 225F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", this.cvt((float) Math.sqrt(this.fm.M.fuel), 0.0F, 38.729F, 0.0F, 225F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", this.cvt((float) Math.sqrt(this.fm.M.fuel), 26.925F, 38.729F, 0.0F, 225F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel5", this.cvt((float) Math.sqrt(this.fm.M.fuel), 26.925F, 38.729F, 0.0F, 225F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.setNew.vspeed, -15F, 15F, -0.055F, 0.055F);
        this.mesh.chunkSetLocate("Z_Climb1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb2", this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.setNew.man1, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold2", this.cvt(this.setNew.man2, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitG4M1_11.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitG4M1_11.rpmScale), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkVisible("Z_GearGreen1", this.fm.CT.getGear() > 0.99F);
            this.mesh.chunkVisible("Z_GearGreen2", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearGreen3", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_GearRed1", this.fm.CT.getGear() < 0.01F);
            this.mesh.chunkVisible("Z_GearRed2", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearRed3", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        }
        this.mesh.chunkSetAngles("Z_AFN12", 0.0F, this.cvt(this.setNew.beaconDirection, -45F, 45F, -13F, 13F), 0.0F);
        this.mesh.chunkSetAngles("Z_AFN11", 0.0F, this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, 28F, -28F), 0.0F);
        this.mesh.chunkVisible("AFN1_RED", this.isOnBlindLandingMarker());
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Fuel1", false);
            this.mesh.chunkVisible("Z_Fuel2", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_TurnBank3", false);
            this.mesh.chunkVisible("Z_TurnBank4", false);
            this.mesh.chunkVisible("Z_TurnBank5", false);
            this.mesh.chunkVisible("Z_Flap2", false);
            this.mesh.chunkVisible("Z_GearGreen1", false);
            this.mesh.chunkVisible("Z_GearGreen2", false);
            this.mesh.chunkVisible("Z_GearGreen3", false);
            this.mesh.chunkVisible("Z_GearRed1", false);
            this.mesh.chunkVisible("Z_GearRed2", false);
            this.mesh.chunkVisible("Z_GearRed3", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_Oil1", false);
            this.mesh.chunkVisible("Z_Oil2", false);
            this.mesh.chunkVisible("Z_fuelpress1", false);
            this.mesh.chunkVisible("Z_fuelpress2", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Temp2", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage3", true);
            this.mesh.chunkVisible("XHullDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("XHullDamage1", true);
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
    private static final float speedometerScale[] = { 0.0F, 6.5F, 30F, 66F, 105F, 158.5F, 212F, 272.5F, 333F, 384F, 432.5F, 479.5F, 526.5F, 573.5F, 624.5F, 674F };
    private static final float oilTempScale[]     = { 0.0F, 4F, 17.5F, 38F, 63F, 90.5F, 115F, 141.3F, 180F, 221.7F, 269.5F, 311F, 357F };
    private static final float rpmScale[]         = { 0.0F, 10F, 75F, 126.5F, 179.5F, 232F, 284.5F, 336F };

}
