package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitKI_61 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitKI_61.this.fm != null) {
                CockpitKI_61.this.setTmp = CockpitKI_61.this.setOld;
                CockpitKI_61.this.setOld = CockpitKI_61.this.setNew;
                CockpitKI_61.this.setNew = CockpitKI_61.this.setTmp;
                CockpitKI_61.this.setNew.throttle = (0.9F * CockpitKI_61.this.setOld.throttle) + (0.1F * CockpitKI_61.this.fm.CT.PowerControl);
                CockpitKI_61.this.setNew.manifold = (0.8F * CockpitKI_61.this.setOld.manifold) + (0.2F * CockpitKI_61.this.fm.EI.engines[0].getManifoldPressure());
                CockpitKI_61.this.setNew.pitch = (0.9F * CockpitKI_61.this.setOld.pitch) + (0.1F * CockpitKI_61.this.fm.EI.engines[0].getControlProp());
                CockpitKI_61.this.setNew.mix = (0.9F * CockpitKI_61.this.setOld.mix) + (0.1F * CockpitKI_61.this.fm.EI.engines[0].getControlMix());
                CockpitKI_61.this.setNew.altimeter = CockpitKI_61.this.fm.getAltitude();
                if (Math.abs(CockpitKI_61.this.fm.Or.getKren()) < 30F) {
                    CockpitKI_61.this.setNew.azimuth.setDeg(CockpitKI_61.this.setOld.azimuth.getDeg(1.0F), CockpitKI_61.this.fm.Or.azimut());
                }
                CockpitKI_61.this.setNew.vspeed = ((199F * CockpitKI_61.this.setOld.vspeed) + CockpitKI_61.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      pitch;
        float      mix;
        float      altimeter;
        float      manifold;
        AnglesFork azimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    public CockpitKI_61() {
        super("3DO/Cockpit/Ki-61/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.pictFlaps = 0.0F;
        this.ordnanceCounter = 0;
        this.mesh.chunkVisible("FlarePilonEn_L", false);
        this.mesh.chunkVisible("FlarePilonEn_R", false);
        if (this.fm.CT.Weapons[3] != null) {
            this.ordnanceCounter = this.fm.CT.Weapons[3].length;
            if (this.fm.CT.Weapons[9] != null) {
                this.ordnanceCounter = 3;
            }
        } else if (this.fm.CT.Weapons[9] != null) {
            this.ordnanceCounter = 3 + this.fm.CT.Weapons[9].length;
        }
        this.cockpitNightMats = (new String[] { "GP_I", "GP_II", "GP_III", "GP_IV", "GP_V", "GP_VI", "GP_II_D", "GP_III_D", "GP_IV_D", "GP_V_D", "GP_VI_D", "GP_VII", "GP_VIII" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.16F, 0.99F, 0.0F, -0.54F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_canopylock_R", 0.0F, this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.12F, 0.0F, -105F), 0.0F);
        this.mesh.chunkSetAngles("Z_canopylock_L", 0.0F, this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.12F, 0.0F, -105F), 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, -57F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_stickmount", 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 12F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 12F, 0.0F);
        this.mesh.chunkSetAngles("Z_elevshtck", 0.0F, this.pictElev * 12F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_L", 0.0F, -18F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R", 0.0F, 18F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_WlBreak_L", 0.0F, -30F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_WlBreak_R", 0.0F, -30F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_ElevTrimHandl", 0.0F, -45F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_FlapLever", 0.0F, 57F * (this.pictFlaps = (0.8F * this.pictFlaps) + (0.2F * this.fm.CT.FlapsControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_OilCoolerLvr", 0.0F, -57F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_TQHandle", 0.0F, 72F * this.setNew.throttle, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitchLvr", 0.0F, 90F * this.setNew.pitch, 0.0F);
        this.mesh.chunkSetAngles("Z_MixLvr", 0.0F, 30F * this.setNew.mix, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedElevTrim", 0.0F, -90F * this.fm.CT.trimElevator, 0.0F);
        this.mesh.chunkSetAngles("NeedCompass_B", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_Km", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_M", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedBank", this.cvt(this.getBall(8D), -8F, 8F, 10F, -10F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("NeedTurn", this.cvt(this.w.z, -0.23562F, 0.23562F, -25F, 25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedClimb", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.5F, 6.5F), CockpitKI_61.vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedFuel", this.cvt(this.fm.M.fuel, 0.0F, 525F, 0.0F, 328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.6F, 0.0F, 308F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedMin", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedManPress", this.cvt(this.setNew.manifold, 0.200068F, 1.799932F, -164F, 164F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOilPress", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOil_L", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOil_R", -this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 130F, 0.0F, 200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedCoolantT_L", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedCoolantT_R", -this.cvt(this.fm.EI.engines[0].tWaterOut - 20F, 0.0F, 130F, 0.0F, 200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedRPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitKI_61.revolutionsScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 20F), CockpitKI_61.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedExhTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 200F, 0.0F, 64F), 0.0F, 0.0F);
        float f1 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F;
        if (f1 > 0.0F) {
            this.mesh.chunkSetAngles("NeedFreeAirTemp", this.cvt(f1, 0.0F, 60F, 0.0F, 34.4F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedFreeAirTemp", this.cvt(f1, -40F, 0.0F, -34.4F, 0.0F), 0.0F, 0.0F);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 0.0567F);
        this.mesh.chunkSetLocate("NeedFlap", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -0.0567F);
        this.mesh.chunkSetLocate("NeedRadiator", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("FlareGearDn_A", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearDn_B", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearUp_A", (this.fm.CT.getGear() < 0.01F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearUp_B", (this.fm.CT.getGear() < 0.01F) && this.fm.Gears.rgear);
        if (this.fm.CT.bHasGearControl) {
            this.mesh.chunkVisible("FlareGearDn_C", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.cgear);
            this.mesh.chunkVisible("FlareGearUp_C", (this.fm.CT.getGear() < 0.01F) && this.fm.Gears.cgear);
        } else {
            this.mesh.chunkVisible("GIND2", false);
        }
        switch (this.ordnanceCounter) {
            default:
                break;

            case 1:
                if (this.fm.CT.Weapons[3][0].haveBullets()) {
                    this.mesh.chunkVisible("FlarePilonEn_R", true);
                } else {
                    this.mesh.chunkVisible("FlarePilonEn_R", false);
                }
                break;

            case 2:
                if (this.fm.CT.Weapons[3][0].haveBullets()) {
                    this.mesh.chunkVisible("FlarePilonEn_R", true);
                    this.mesh.chunkVisible("FlarePilonEn_L", true);
                } else {
                    this.mesh.chunkVisible("FlarePilonEn_R", false);
                    this.mesh.chunkVisible("FlarePilonEn_L", false);
                }
                break;

            case 3:
                if (this.fm.CT.Weapons[3][0].haveBullets()) {
                    this.mesh.chunkVisible("FlarePilonEn_R", true);
                } else {
                    this.mesh.chunkVisible("FlarePilonEn_R", false);
                }
                if (this.fm.CT.Weapons[9][0].haveBullets()) {
                    this.mesh.chunkVisible("FlarePilonEn_L", true);
                } else {
                    this.mesh.chunkVisible("FlarePilonEn_L", false);
                }
                break;

            case 4:
                if (this.fm.CT.Weapons[9][0].haveBullets()) {
                    this.mesh.chunkVisible("FlarePilonEn_R", true);
                } else {
                    this.mesh.chunkVisible("FlarePilonEn_R", false);
                }
                break;

            case 5:
                if (this.fm.CT.Weapons[9][0].haveBullets()) {
                    this.mesh.chunkVisible("FlarePilonEn_R", true);
                    this.mesh.chunkVisible("FlarePilonEn_L", true);
                } else {
                    this.mesh.chunkVisible("FlarePilonEn_R", false);
                    this.mesh.chunkVisible("FlarePilonEn_L", false);
                }
                break;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("OilSplats", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Z_Z_MASK_D1", true);
            this.mesh.chunkVisible("GS_Glass_D0", false);
            this.mesh.chunkVisible("GS_Glass_D1", true);
            this.mesh.chunkVisible("Z_wndshld_holes", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_canopy_holes", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gages", false);
            this.mesh.chunkVisible("Gages_D1", true);
            this.mesh.chunkVisible("NeedBank", false);
            this.mesh.chunkVisible("NeedSpeed", false);
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("NeedAlt_M", false);
            this.mesh.chunkVisible("NeedManPress", false);
            this.mesh.chunkVisible("NeedFreeAirTemp", false);
            this.mesh.chunkVisible("NeedExhTemp", false);
            this.mesh.chunkVisible("NeedRPM", false);
            this.mesh.chunkVisible("NeedOilPress", false);
            this.mesh.chunkVisible("NeedFuel", false);
            this.mesh.chunkVisible("NeedHPres_01", false);
            this.mesh.chunkVisible("NeedHPres_02", false);
        }
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictGear;
    private float              pictFlaps;
    private int                ordnanceCounter;
    private static final float vsiNeedleScale[]   = { -200F, -160F, -125F, -90F, 90F, 125F, 160F, 200F };
    private static final float speedometerScale[] = { 0.0F, 10F, 35F, 70F, 105F, 145F, 190F, 230F, 275F, 315F, 360F, 397.5F, 435F, 470F, 505F, 537.5F, 570F, 600F, 630F, 655F, 680F };
    private static final float revolutionsScale[] = { 0.0F, 20F, 75F, 125F, 180F, 220F, 285F, 335F };

}
