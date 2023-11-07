package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitFW200 extends CockpitPilot {
    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      vspeed;
        float      PDI;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFW200.this.fm != null) {
                CockpitFW200.this.setTmp = CockpitFW200.this.setOld;
                CockpitFW200.this.setOld = CockpitFW200.this.setNew;
                CockpitFW200.this.setNew = CockpitFW200.this.setTmp;
                CockpitFW200.this.setNew.throttle1 = (0.85F * CockpitFW200.this.setOld.throttle1) + (CockpitFW200.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitFW200.this.setNew.throttle2 = (0.85F * CockpitFW200.this.setOld.throttle2) + (CockpitFW200.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitFW200.this.setNew.prop1 = (0.85F * CockpitFW200.this.setOld.prop1) + (CockpitFW200.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitFW200.this.setNew.prop2 = (0.85F * CockpitFW200.this.setOld.prop2) + (CockpitFW200.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitFW200.this.setNew.altimeter = CockpitFW200.this.fm.getAltitude();
                float f = CockpitFW200.this.waypointAzimuth();
                if (Math.abs(CockpitFW200.this.fm.Or.getKren()) < 30F) {
                    CockpitFW200.this.setNew.azimuth.setDeg(CockpitFW200.this.setOld.azimuth.getDeg(1.0F), CockpitFW200.this.fm.Or.azimut());
                }
                if (CockpitFW200.this.useRealisticNavigationInstruments()) {
                    CockpitFW200.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitFW200.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitFW200.this.setNew.radioCompassAzimuth.setDeg(CockpitFW200.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitFW200.this.radioCompassAzimuthInvertMinus() - CockpitFW200.this.setOld.azimuth.getDeg(1.0F) - 90F);
                } else {
                    CockpitFW200.this.setNew.waypointAzimuth.setDeg(CockpitFW200.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                    CockpitFW200.this.setNew.radioCompassAzimuth.setDeg(CockpitFW200.this.setOld.radioCompassAzimuth.getDeg(0.1F), f - CockpitFW200.this.setOld.azimuth.getDeg(0.1F) - 90F);
                }
                if (CockpitFW200.this.fw200 != null) {
                    CockpitFW200.this.setNew.PDI = CockpitFW200.this.fw200.fSightCurSideslip;
                }
                CockpitFW200.this.setNew.vspeed = ((199F * CockpitFW200.this.setOld.vspeed) + CockpitFW200.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitFW200() {
        super("3DO/Cockpit/FW-200/CockpitFW200.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
//        pictGear = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.fw200 = null;
        this.cockpitNightMats = (new String[] { "texture01_dmg", "texture01", "texture02_dmg", "texture02", "texture03_dmg", "texture03", "texture04_dmg", "texture04", "texture05_dmg", "texture05", "texture06_dmg", "texture06", "texture21_dmg", "texture21", "texture25" });
        this.setNightMats(false);
        if (this.aircraft() instanceof FW_200C3U4) {
            this.fw200 = (FW_200C3U4) this.aircraft();
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneL", 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneR", 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_PedalWireR", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_PedalWireL", 0.0F, 0.0F, 10F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 38F * (this.pictFlap = (0.75F * this.pictFlap) + (0.25F * this.fm.CT.FlapsControl)), 0.0F);
        this.mesh.chunkSetAngles("zOilFlap1", 0.0F, -35F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("zOilFlap2", 0.0F, -35F * this.fm.EI.engines[1].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("zMix1", 0.0F, -45.8F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("zMix2", 0.0F, -45.8F * this.fm.EI.engines[1].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 0.0F, -54F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("zPitch2", 0.0F, -54F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, -49F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, -49F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("zCompressor1", 0.0F, this.cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("zCompressor2", 0.0F, this.cvt(this.fm.EI.engines[1].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        if (this.fm.Gears.cgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            this.mesh.chunkSetLocate("Z_Gearc1", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.fm.Gears.lgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            this.mesh.chunkSetLocate("Z_GearL1", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.fm.Gears.rgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            this.mesh.chunkSetLocate("Z_GearR1", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkSetAngles("zFlaps2", 0.0F, 90F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        this.mesh.chunkSetLocate("zAH2", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitFW200.variometerScale), 0.0F);
        }
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurnBank", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("zBall", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        this.mesh.chunkSetAngles("zPDI", 0.0F, this.cvt(this.setNew.PDI, -30F, 30F, -46.5F, 46.5F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitFW200.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zCompass3", 0.0F, this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zMagnetic", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("zNavP", 0.0F, this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zRPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("zRPM2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.pictManf1 = (0.9F * this.pictManf1) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
            this.mesh.chunkSetAngles("Z_Pres2", 0.0F, this.pictManf2 = (0.9F * this.pictManf2) + (0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair1", 0.0F, -this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair2", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 100F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 100F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Brkpres1", 0.0F, 118F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("zFuel1", 0.0F, -this.cvt(this.fm.M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        this.mesh.chunkSetAngles("zFuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        this.mesh.chunkSetAngles("zFuel3", 0.0F, -this.cvt(this.fm.M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuel4", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuel5", 0.0F, -this.cvt(this.fm.M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuel6", 0.0F, this.cvt(this.fm.M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zFreeAir", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -70F, 150F, -26.6F, 57F), 0.0F);
        }
        this.mesh.chunkSetAngles("zHydPres", 0.0F, this.fm.Gears.bIsHydroOperable ? 165.5F : 0.0F, 0.0F);
        float f1 = (0.5F * this.fm.EI.engines[0].getRPM()) + (0.5F * this.fm.EI.engines[1].getRPM());
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("zSuction", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 297F), 0.0F);
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() > 0.01F) && (this.fm.CT.getGear() < 0.99F));
        this.mesh.chunkVisible("Z_GearCGreen1", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearRGreen1", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage", true);
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("zCompass3", false);
            this.mesh.chunkVisible("Z_FuelPres1", false);
            this.mesh.chunkVisible("Z_FuelPres2", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_Oilpres2", false);
            this.mesh.chunkVisible("zOilTemp1", false);
            this.mesh.chunkVisible("zOilTemp2", false);
            this.mesh.chunkVisible("Z_Brkpres1", false);
            this.mesh.chunkVisible("zHydPres", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
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
    private float              pictFlap;
//    private float pictGear;
    private float              pictManf1;
    private float              pictManf2;
    private FW_200C3U4         fw200;
    private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
    private static final float variometerScale[]  = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };

}
