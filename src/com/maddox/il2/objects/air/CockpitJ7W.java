package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitJ7W extends CockpitPilot {
    private class Variables {

        float      flap;
        float      throttle;
        float      pitch;
        float      mix;
        float      gear;
        float      tlock;
        float      altimeter;
        float      manifold;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;
        float      stbyPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitJ7W.this.fm != null) {
                if (CockpitJ7W.this.bNeedSetUp) {
                    CockpitJ7W.this.reflectPlaneMats();
                    CockpitJ7W.this.bNeedSetUp = false;
                }
                CockpitJ7W.this.setTmp = CockpitJ7W.this.setOld;
                CockpitJ7W.this.setOld = CockpitJ7W.this.setNew;
                CockpitJ7W.this.setNew = CockpitJ7W.this.setTmp;
                CockpitJ7W.this.setNew.flap = (0.88F * CockpitJ7W.this.setOld.flap) + (0.12F * CockpitJ7W.this.fm.CT.FlapsControl);
                CockpitJ7W.this.setNew.tlock = (0.7F * CockpitJ7W.this.setOld.tlock) + (0.3F * (CockpitJ7W.this.fm.Gears.bTailwheelLocked ? 1.0F : 0.0F));
                if (CockpitJ7W.this.cockpitDimControl) {
                    if (CockpitJ7W.this.setNew.dimPosition > 0.0F) {
                        CockpitJ7W.this.setNew.dimPosition = CockpitJ7W.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitJ7W.this.setNew.dimPosition < 1.0F) {
                    CockpitJ7W.this.setNew.dimPosition = CockpitJ7W.this.setOld.dimPosition + 0.05F;
                }
                if ((CockpitJ7W.this.fm.AS.astateCockpitState & 2) != 0) {
                    if (CockpitJ7W.this.setNew.stbyPosition > 0.0F) {
                        CockpitJ7W.this.setNew.stbyPosition = CockpitJ7W.this.setOld.stbyPosition - 0.025F;
                    }
                } else if (CockpitJ7W.this.setNew.stbyPosition < 1.0F) {
                    CockpitJ7W.this.setNew.stbyPosition = CockpitJ7W.this.setOld.stbyPosition + 0.025F;
                }
                if ((CockpitJ7W.this.fm.CT.GearControl < 0.5F) && (CockpitJ7W.this.setNew.gear < 1.0F)) {
                    CockpitJ7W.this.setNew.gear = CockpitJ7W.this.setOld.gear + 0.02F;
                }
                if ((CockpitJ7W.this.fm.CT.GearControl > 0.5F) && (CockpitJ7W.this.setNew.gear > 0.0F)) {
                    CockpitJ7W.this.setNew.gear = CockpitJ7W.this.setOld.gear - 0.02F;
                }
                CockpitJ7W.this.setNew.throttle = (0.9F * CockpitJ7W.this.setOld.throttle) + (0.1F * CockpitJ7W.this.fm.CT.PowerControl);
                CockpitJ7W.this.setNew.manifold = (0.8F * CockpitJ7W.this.setOld.manifold) + (0.2F * CockpitJ7W.this.fm.EI.engines[0].getManifoldPressure());
                CockpitJ7W.this.setNew.pitch = (0.9F * CockpitJ7W.this.setOld.pitch) + (0.1F * CockpitJ7W.this.fm.EI.engines[0].getControlProp());
                CockpitJ7W.this.setNew.mix = (0.9F * CockpitJ7W.this.setOld.mix) + (0.1F * CockpitJ7W.this.fm.EI.engines[0].getControlMix());
                CockpitJ7W.this.setNew.altimeter = CockpitJ7W.this.fm.getAltitude();
                if (Math.abs(CockpitJ7W.this.fm.Or.getKren()) < 30F) {
                    CockpitJ7W.this.setNew.azimuth.setDeg(CockpitJ7W.this.setOld.azimuth.getDeg(1.0F), CockpitJ7W.this.fm.Or.azimut());
                }
                float f = CockpitJ7W.this.waypointAzimuth();
                CockpitJ7W.this.setNew.waypointAzimuth.setDeg(CockpitJ7W.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitJ7W.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-3F, 3F));
                CockpitJ7W.this.setNew.vspeed = ((199F * CockpitJ7W.this.setOld.vspeed) + CockpitJ7W.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(3F);
    }

    public CockpitJ7W() {
        super("3DO/Cockpit/J7W/hier.him", "p39");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.pictTE = 0.0F;
        this.pictTR = 0.0F;
        this.pictTA = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_07", "DGauges_01", "DGauges_02", "DGauges_03", "DGauges_04", "DGauges_05", "DGauges_06" });
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.13F, -0.1F, 0.025F, -0.035F });
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("CanopyOpenhnd", -720F * this.fm.CT.getCockpitDoor(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.5F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.5F, 0.0F, 0.02F);
        Cockpit.ypr[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.0F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, 0.0132F);
        this.mesh.chunkSetLocate("CanopyOpenRodL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, -0.0128F);
        this.mesh.chunkSetLocate("CanopyOpenRodR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("GSDimmArm", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 64F, 0.0F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, -0.0594F, 0.0F);
        this.mesh.chunkSetLocate("GSDimmBase", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("IgnitionSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 113F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("GearHandle", this.cvt(this.pictGear = (0.9F * this.pictGear) + (0.1F * this.fm.CT.GearControl), 0.0F, 1.0F, 0.0F, -31F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("GearHandleMan", 0.0F, 0.0F, 0.0F);
        if (this.fm.CT.FlapsControl != 0.19F) {
            this.mesh.chunkSetAngles("FlapHandle", -48F * this.setNew.flap, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("TQHandle", 60F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("meto", 20F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TRigger", this.fm.CT.saveWeaponControl[1] ? -12F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PropPitchLvr", 60F * this.setNew.pitch, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("MixLvr", 45F * this.setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ChargerLvr", this.cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 2.0F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ChargerAutoLvr", this.cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 2.0F, 0.0F, 49F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CowlFlapLvr", 60F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("OilCoolerLvr", 60F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalCrossBar", 25F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Pedal_L", 35F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Pedal_R", 35F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("FLCS", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, 0.0F, 20F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("FLCSRod", 0.0F, 0.0F, -20F * this.pictElev);
        this.mesh.chunkSetAngles("ElevTrim", -450F * (this.pictTE = (0.9F * this.pictTE) + (0.1F * this.fm.CT.trimElevator)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("AronTrim", 450F * (this.pictTA = (0.9F * this.pictTA) + (0.1F * this.fm.CT.trimAileron)), 0.0F, 0.0F);
        if (this.cockpitLightControl) {
            this.mesh.chunkSetAngles("SW_UVLight", 0.0F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("SW_UVLight", 0.0F, 0.0F, -54F);
        }
        this.resetYPRmodifier();
        float f1 = this.pictTE;
        if (f1 < 0.0F) {
            Cockpit.xyz[1] = this.cvt(f1, -0.25F, 0.0F, -0.02305F, 0.0F);
        } else {
            Cockpit.xyz[1] = this.cvt(f1, 0.0F, 0.5F, 0.0F, 0.04985F);
        }
        this.mesh.chunkSetLocate("NeedElevTrim", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("NeedRuddrTrim", this.cvt(this.pictTR, -0.5F, 0.5F, 90F, -90F), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) || ((this.fm.AS.astateCockpitState & 0x10) == 0) || ((this.fm.AS.astateCockpitState & 4) == 0)) {
            this.mesh.chunkSetAngles("NeedAHCyl", 0.0F, -this.fm.Or.getKren(), 0.0F);
            this.mesh.chunkSetAngles("NeedAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 20F, -20F));
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) || ((this.fm.AS.astateCockpitState & 8) == 0) || ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.mesh.chunkSetAngles("NeedCompass_A", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -20F, 20F, 20F, -20F));
            this.mesh.chunkSetAngles("Compasst", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedAlt_Km", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_M", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("oxyalt", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 3000F, 11000F, 0.0F, 175F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("oxypress", this.cvt(this.fm.M.nitro, 0.0F, 3F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("slipb", this.cvt(this.getBall(8D), -8F, 8F, -10F, 10F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("NeedTurn", this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -12F, 15F, -0.104F, 0.0F);
        this.mesh.chunkSetLocate("Z_Climb1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -12F, 15F, 0.018F, 0.0F);
        this.mesh.chunkSetLocate("Z_Climb2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -12F, 15F, 0.017F, 0.0F);
        this.mesh.chunkSetLocate("Z_Climb3", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -12F, 15F, 0.018F, 0.0F);
        this.mesh.chunkSetLocate("Z_Climb4", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -12F, 15F, 0.017F, 0.0F);
        this.mesh.chunkSetLocate("Z_Climb5", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -12F, 15F, 0.018F, 0.0F);
        this.mesh.chunkSetLocate("Z_Climb6", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("NeedClimb", this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedCylTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedExhTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 90F), 0.0F, 0.0F);
        if (this.fm.M.fuel > 400F) {
            this.mesh.chunkSetAngles("NeedFuel", this.cvt(this.fm.M.fuel, 400F, 800F, 0.0F, 120F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedFuel", this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 270F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedFuelPress", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.6F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedMin", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedManPress", this.cvt(this.setNew.manifold, 0.33339F, 1.6579F, -125.5F, 125.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOilPress", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedVcm", this.cvt(3F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 175F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 110F, 0.0F, 8F), CockpitJ7W.oilTScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedRPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitJ7W.revolutionsScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 740.7998F, 0.0F, 20F), CockpitJ7W.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedVMPressA", this.cvt(this.fm.M.nitro, 0.0F, 3F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedVMPressB", this.cvt((float) Math.sqrt(this.fm.M.nitro), 0.0F, 8F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedExternalT", this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 233.09F, 333.09F, 0.0F, 5F), CockpitJ7W.frAirTempScale), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {
            this.mesh.chunkSetAngles("NeedDF", this.cvt(this.setNew.waypointAzimuth.getDeg(f), -90F, 90F, -33F, 33F), 0.0F, 0.0F);
        }
        this.mesh.chunkVisible("FlareGearDn_L", (this.fm.CT.getGearL() > 0.98F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearDn_R", (this.fm.CT.getGearR() > 0.98F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearDn_C", this.fm.CT.getGear() > 0.98F);
        this.mesh.chunkVisible("FlareGearUp_L", (this.fm.CT.getGearL() < 0.02F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearUp_R", (this.fm.CT.getGearR() < 0.02F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearUp_C", this.fm.CT.getGear() < 0.02F);
        this.mesh.chunkVisible("FlareBoostRed", this.fm.M.fuel > 52.5F);
        this.mesh.chunkVisible("FlareBoostGreen", this.fm.M.fuel < 52.5F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("DamageHull2", true);
            this.mesh.chunkVisible("Gages5_D0", false);
            this.mesh.chunkVisible("Gages5_D1", true);
            this.mesh.chunkVisible("NeedFuelA", false);
            this.mesh.chunkVisible("NeedFuelB", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("DamageHull3", true);
            this.mesh.chunkVisible("Gages2_D0", false);
            this.mesh.chunkVisible("Gages2_D1", true);
            this.mesh.chunkVisible("NeedCylTemp", false);
            this.mesh.chunkVisible("NeedFuelPress", false);
            this.mesh.chunkVisible("NeedOilPress", false);
            this.mesh.chunkVisible("NeedOilTemp", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("DamageHull2", true);
            this.mesh.chunkVisible("Gages4_D0", false);
            this.mesh.chunkVisible("Gages4_D1", true);
            this.mesh.chunkVisible("NeedSpeed", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("DamageHull3", true);
            this.mesh.chunkVisible("Gages3_D0", false);
            this.mesh.chunkVisible("Gages3_D1", true);
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("NeedAlt_M", false);
            this.mesh.chunkVisible("NeedClimb", false);
            this.mesh.chunkVisible("NeedVMPressA", false);
            this.mesh.chunkVisible("NeedVMPressB", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("OilSplats", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("GunSight_T3", false);
            this.mesh.chunkVisible("DGS_Lenz", true);
            this.mesh.chunkVisible("GSGlassMain", false);
            this.mesh.chunkVisible("GSDimmArm", false);
            this.mesh.chunkVisible("GSDimmBase", false);
            this.mesh.chunkVisible("GSGlassDimm", false);
            this.mesh.chunkVisible("DGunSight_T3", true);
            this.mesh.chunkVisible("DGS_Lenz", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("DamageGlass1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("DamageGlass2", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("DamageHull1", true);
            this.mesh.chunkVisible("Gages1_D0", false);
            this.mesh.chunkVisible("Gages1_D1", true);
            this.mesh.chunkVisible("NeedRPM", false);
            this.mesh.chunkVisible("NeedManPress", false);
            this.mesh.chunkVisible("NeedExhTemp", false);
            this.mesh.chunkVisible("NeedTurn", false);
            this.mesh.chunkVisible("NeedBank", false);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
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
    private float              pictTE;
    private float              pictTR;
    private float              pictTA;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 30F, 75F, 90F, 135F, 180F, 221F, 270F, 315F, 360F, 405F, 450F, 490F, 520F, 545F, 580F, 605.5F, 635F, 675F, 698F, 725F };
    private static final float revolutionsScale[] = { 0.0F, 20F, 75F, 125F, 180F, 220F, 280F, 330F };
    private static final float oilTScale[]        = { 0.0F, 20F, 70.5F, 122.5F, 175F, 237.5F, 290.5F, 338F };
    private static final float frAirTempScale[]   = { 0.0F, 20.5F, 37F, 48.5F, 60.5F, 75.5F };
    static {
        Property.set(CockpitJ7W.class, "normZNs", new float[] { 0.77F, 0.77F, 0.79F, 0.77F });
    }

}
