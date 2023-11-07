package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitLa_15 extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      dimPosition;
        float      vspeed;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      beaconDirection;
        float      beaconRange;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;
        float      stbyPosition;
        float      stbyPosition2;
        float      stbyPosition3;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitLa_15.this.fm != null) {
                CockpitLa_15.this.setTmp = CockpitLa_15.this.setOld;
                CockpitLa_15.this.setOld = CockpitLa_15.this.setNew;
                CockpitLa_15.this.setNew = CockpitLa_15.this.setTmp;
                CockpitLa_15.this.setNew.throttle = (0.9F * CockpitLa_15.this.setOld.throttle) + (CockpitLa_15.this.fm.CT.PowerControl * 0.1F);
                CockpitLa_15.this.setNew.altimeter = CockpitLa_15.this.fm.getAltitude();
                if (CockpitLa_15.this.cockpitDimControl) {
                    if (CockpitLa_15.this.setNew.dimPosition > 0.0F) {
                        CockpitLa_15.this.setNew.dimPosition = CockpitLa_15.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitLa_15.this.setNew.dimPosition < 1.0F) {
                    CockpitLa_15.this.setNew.dimPosition = CockpitLa_15.this.setOld.dimPosition + 0.05F;
                }
                float f = CockpitLa_15.this.waypointAzimuth();
                if (CockpitLa_15.this.useRealisticNavigationInstruments()) {
                    CockpitLa_15.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitLa_15.this.setOld.waypointAzimuth.setDeg(f - 90F);
                } else {
                    CockpitLa_15.this.setNew.waypointAzimuth.setDeg(CockpitLa_15.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitLa_15.this.setOld.azimuth.getDeg(1.0F));
                }
                if (Math.abs(CockpitLa_15.this.fm.Or.getKren()) < 30F) {
                    CockpitLa_15.this.setNew.azimuth.setDeg(CockpitLa_15.this.setOld.azimuth.getDeg(1.0F), CockpitLa_15.this.fm.Or.azimut());
                }
                CockpitLa_15.this.setNew.beaconDirection = ((10F * CockpitLa_15.this.setOld.beaconDirection) + CockpitLa_15.this.getBeaconDirection()) / 11F;
                CockpitLa_15.this.setNew.beaconRange = ((10F * CockpitLa_15.this.setOld.beaconRange) + CockpitLa_15.this.getBeaconRange()) / 11F;
                CockpitLa_15.this.setNew.vspeed = ((199F * CockpitLa_15.this.setOld.vspeed) + CockpitLa_15.this.fm.getVertSpeed()) / 200F;
                if (CockpitLa_15.this.fm.EI.engines[0].getStage() < 6) {
                    if (CockpitLa_15.this.setNew.stbyPosition2 > 0.0F) {
                        CockpitLa_15.this.setNew.stbyPosition2 = CockpitLa_15.this.setOld.stbyPosition2 - 0.005F;
                    }
                    if (CockpitLa_15.this.setNew.stbyPosition > 0.0F) {
                        CockpitLa_15.this.setNew.stbyPosition = CockpitLa_15.this.setOld.stbyPosition - 0.025F;
                    }
                } else {
                    if (CockpitLa_15.this.setNew.stbyPosition2 < 1.0F) {
                        CockpitLa_15.this.setNew.stbyPosition2 = CockpitLa_15.this.setOld.stbyPosition2 + 0.005F;
                    }
                    if (CockpitLa_15.this.setNew.stbyPosition < 1.0F) {
                        CockpitLa_15.this.setNew.stbyPosition = CockpitLa_15.this.setOld.stbyPosition + 0.025F;
                    }
                }
                if ((CockpitLa_15.this.fm.EI.engines[0].getStage() < 6) && !CockpitLa_15.this.fm.CT.bHasAileronControl && !CockpitLa_15.this.fm.CT.bHasElevatorControl && !CockpitLa_15.this.fm.CT.bHasGearControl && !CockpitLa_15.this.fm.CT.bHasAirBrakeControl && !CockpitLa_15.this.fm.CT.bHasFlapsControl) {
                    if (CockpitLa_15.this.setNew.stbyPosition3 > 0.0F) {
                        CockpitLa_15.this.setNew.stbyPosition3 = CockpitLa_15.this.setOld.stbyPosition3 - 0.005F;
                    }
                } else if (CockpitLa_15.this.setNew.stbyPosition3 < 1.0F) {
                    CockpitLa_15.this.setNew.stbyPosition3 = CockpitLa_15.this.setOld.stbyPosition3 + 0.005F;
                }
                float f1 = ((La15) CockpitLa_15.this.aircraft()).k14Distance;
                CockpitLa_15.this.setNew.k14w = (5F * CockpitLa_15.k14TargetWingspanScale[((La15) CockpitLa_15.this.aircraft()).k14WingspanType]) / f1;
                CockpitLa_15.this.setNew.k14w = (0.9F * CockpitLa_15.this.setOld.k14w) + (0.1F * CockpitLa_15.this.setNew.k14w);
                CockpitLa_15.this.setNew.k14wingspan = (0.9F * CockpitLa_15.this.setOld.k14wingspan) + (0.1F * CockpitLa_15.k14TargetMarkScale[((La15) CockpitLa_15.this.aircraft()).k14WingspanType]);
                CockpitLa_15.this.setNew.k14mode = (0.8F * CockpitLa_15.this.setOld.k14mode) + (0.2F * ((La15) CockpitLa_15.this.aircraft()).k14Mode);
                com.maddox.JGP.Vector3d vector3d = CockpitLa_15.this.aircraft().FM.getW();
                double d = 0.00125D * f1;
                float f2 = (float) Math.toDegrees(d * vector3d.z);
                float f3 = -(float) Math.toDegrees(d * vector3d.y);
                float f4 = CockpitLa_15.this.floatindex((f1 - 200F) * 0.04F, CockpitLa_15.k14BulletDrop) - CockpitLa_15.k14BulletDrop[0];
                f3 += (float) Math.toDegrees(Math.atan(f4 / f1));
                CockpitLa_15.this.setNew.k14x = (0.92F * CockpitLa_15.this.setOld.k14x) + (0.08F * f2);
                CockpitLa_15.this.setNew.k14y = (0.92F * CockpitLa_15.this.setOld.k14y) + (0.08F * f3);
                if (CockpitLa_15.this.setNew.k14x > 7F) {
                    CockpitLa_15.this.setNew.k14x = 7F;
                }
                if (CockpitLa_15.this.setNew.k14x < -7F) {
                    CockpitLa_15.this.setNew.k14x = -7F;
                }
                if (CockpitLa_15.this.setNew.k14y > 7F) {
                    CockpitLa_15.this.setNew.k14y = 7F;
                }
                if (CockpitLa_15.this.setNew.k14y < -7F) {
                    CockpitLa_15.this.setNew.k14y = -7F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Seat_D0", false);
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Seat_D0", true);
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public CockpitLa_15() {
        super("3DO/Cockpit/LaG-15/LaG_15.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.pictMet1 = 0.0F;
        this.gun = new Gun[3];
        this.cockpitNightMats = (new String[] { "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_08", "MiG-15_Compass" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    private float machNumber() {
        return ((La15) this.aircraft()).calculateMach();
    }

    public void reflectWorldToInstruments(float f) {
        if (La15.bChangedPit) {
            La15.bChangedPit = false;
        }
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            int i = ((La15) this.aircraft()).k14Mode;
            boolean flag = i < 2;
            this.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.setNew.k14w;
            for (int j = 1; j < 7; j++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }

        }
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, -0.49F, 0.0F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, -0.065F, 0.0F);
        Cockpit.ypr[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.99F, 0.01F, -3F, 0.0F);
        this.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen04", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen05", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen06", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen07", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen08", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen09", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 50F * (this.pictGear = (0.82F * this.pictGear) + (0.18F * this.fm.CT.GearControl)));
        this.mesh.chunkSetAngles("Z_FlapsLever", -35F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gas1a", 0.0F, this.cvt(this.fm.M.fuel / 2.0F, 0.0F, 700F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Target1", 1.2F * this.setNew.k14wingspan, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gunsight_Button", -10F * this.setNew.k14wingspan, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gunsight_Mire", 0.0F, this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 47F), 0.0F);
        this.mesh.chunkSetAngles("Z_Amp", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition2, this.setOld.stbyPosition2, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        this.mesh.chunkSetAngles("Z_HydroPressure", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition3, this.setOld.stbyPosition3, f), 0.0F, 1.0F, 0.0F, 190F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Turn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Slide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -24F, 24F), 0.0F);
        this.mesh.chunkSetAngles("Z_Slide1a2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -20F, 20F), 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.azimuth.getDeg(f * 0.1F) + this.setNew.beaconDirection, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.azimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_horizont1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_GasPrs1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F) : 0.0F, 0.0F, 5F, -45F, 225F), 0.0F);
        this.mesh.chunkSetAngles("Z_GasPrs2a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F) : 0.0F, 0.0F, 5F, -180F, 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Z_TOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 110F, 5F, 300F), 0.0F);
        this.mesh.chunkSetAngles("Z_OilPrs1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, -155F, -360F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.035F, 0.035F);
        this.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        this.mesh.chunkSetLocate("Z_horizont1b", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Stick01", 0.0F, 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Stick02", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Stick03", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Stick04", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Stick05", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Stick06", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Stick07", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3480F, 90F, 625F), 0.0F, 0.0F);
        this.pictMet1 = (0.96F * this.pictMet1) + (0.04F * (0.6F * this.fm.EI.engines[0].getThrustOutput() * this.fm.EI.engines[0].getControlThrottle() * (this.fm.EI.engines[0].getStage() == 6 ? 1.0F : 0.02F)));
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel > 1.0F ? 0.55F : 0.0F, 0.0F, 1.0F, 0.0F, 284F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ExstT1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 1200F, 0.0F, 112F), 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", -this.setNew.azimuth.getDeg(f * 0.1F) + this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f * 0.1F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        if ((this.machNumber() < 0.95F) || (this.machNumber() > 1.0F)) {
            this.mesh.chunkSetAngles("Z_Alt_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 60000F, 0.0F, 2160F), 0.0F);
            this.mesh.chunkSetAngles("Z_Alt_M", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 60000F, 0.0F, 21600F), 0.0F);
            this.mesh.chunkSetAngles("Z_Alt2_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 12000F, 225F, 495F), 0.0F);
            this.mesh.chunkSetAngles("Z_Alt3_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 12000F, 295F, 395F), 0.0F);
            this.mesh.chunkSetAngles("Z_Speed", 0.0F, this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1200F, 0.0F, 360F), 0.0F);
            this.mesh.chunkSetAngles("Z_AirFlow", 0.0F, this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, -90F, 160F), 0.0F);
            this.mesh.chunkSetAngles("Z_Climb", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Vibrations", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 5.5F, 14F), CockpitLa_15.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3480F, -110F, 170F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkVisible("FlareGearUp_R", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearUp_L", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearUp_C", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("FlareGearDn_R", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearDn_L", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearDn_C", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("FlareFuel", this.fm.M.fuel < 296.1F);
        this.mesh.chunkVisible("FlareTankFuel", (this.fm.M.fuel < 1215F) && (this.fm.M.fuel > 1207F));
        this.mesh.chunkVisible("FlareFire", this.fm.AS.astateEngineStates[0] > 2);
        this.mesh.chunkVisible("FlareAirBrake", this.fm.CT.AirBrakeControl > 0.01F);
        this.mesh.chunkVisible("FlareAltitude", this.fm.getAltitude() < 500F);
        this.mesh.chunkVisible("FlareIgnition", (this.fm.Gears.nOfGearsOnGr == 0) && (this.fm.EI.engines[0].getStage() < 6));
        if (this.gun[0] != null) {
            this.mesh.chunkVisible("FlareN37", this.gun[0].haveBullets());
        }
        if (this.gun[1] != null) {
            this.mesh.chunkVisible("FlareNS23a", this.gun[1].haveBullets());
        }
        if (this.gun[2] != null) {
            this.mesh.chunkVisible("FlareNS23b", this.gun[2].haveBullets());
        }
        this.mesh.chunkVisible("FlareBattery", true);
        this.mesh.chunkVisible("FlareGenerator", true);
        this.resetYPRmodifier();
        Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f) * 65F;
        Cockpit.xyz[2] = this.cvt(Cockpit.ypr[0], 7.5F, 11.5F, 0.0F, 0.0F);
        this.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.CT.getCockpitDoor() == 1.0F) {
            this.mesh.chunkVisible("V_G", false);
            this.mesh.chunkVisible("V_D", false);
        } else {
            this.mesh.chunkVisible("V_G", true);
            this.mesh.chunkVisible("V_D", true);
        }
        if ((this.fm.CT.getCockpitDoor() == 1.0F) && !this.fm.AS.bIsAboutToBailout) {
            this.mesh.chunkVisible("CanopyOpen07", true);
        } else {
            this.mesh.chunkVisible("CanopyOpen07", false);
        }
        if ((this.fm.CT.getCockpitDoor() < 1.0F) && this.fm.CT.bHasCockpitDoorControl) {
            this.mesh.chunkVisible("CanopyOpen06", true);
        } else {
            this.mesh.chunkVisible("CanopyOpen06", false);
        }
        if (this.fm.AS.bIsAboutToBailout) {
            this.mesh.chunkVisible("CanopyOpen01", false);
            this.mesh.chunkVisible("CanopyOpen02", false);
            this.mesh.chunkVisible("CanopyOpen03", false);
            this.mesh.chunkVisible("CanopyOpen04", false);
            this.mesh.chunkVisible("CanopyOpen05", false);
            this.mesh.chunkVisible("CanopyOpen08", false);
            this.mesh.chunkVisible("CanopyOpen09", false);
            this.mesh.chunkVisible("XGlassDamage4", false);
        }
        if (this.fm.CT.BayDoorControl == 1.0F) {
            this.mesh.chunkVisible("Stick04", false);
            this.mesh.chunkVisible("Stick05", true);
        }
        if (this.fm.CT.BayDoorControl == 0.0F) {
            this.mesh.chunkVisible("Stick04", true);
            this.mesh.chunkVisible("Stick05", false);
        }
        this.mesh.chunkVisible("Z_Z_RETICLE", true);
        this.mesh.chunkVisible("Z_Gunsight_Button2", false);
        this.mesh.chunkVisible("Z_Gunsight_Button3", true);
        if (((La15) this.aircraft()).k14Mode == 2) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Gunsight_Button2", true);
            this.mesh.chunkVisible("Z_Gunsight_Button3", false);
        }
        if (this.fm.actor instanceof Mig_15) {
            this.mesh.chunkVisible("Left2", false);
        } else {
            this.mesh.chunkVisible("Left2", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Instruments", false);
            this.mesh.chunkVisible("InstrumentsD", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("Z_Speed", false);
            this.mesh.chunkVisible("Z_Compass1", false);
            this.mesh.chunkVisible("Z_Azimuth1", false);
            this.mesh.chunkVisible("Z_GasPrs1a", false);
            this.mesh.chunkVisible("Z_GasPrs2a", false);
            this.mesh.chunkVisible("Z_Alt_Km", false);
            this.mesh.chunkVisible("Z_Alt_M", false);
            this.mesh.chunkVisible("Z_Turn", false);
            this.mesh.chunkVisible("Z_Turn1a", false);
            this.mesh.chunkVisible("Z_Slide1a", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for (int i = 1; i < 7; i++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);
            }

        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((((this.fm.AS.astateCockpitState & 0x80) != 0) || ((this.fm.AS.astateCockpitState & 0x40) != 0)) && ((this.fm.AS.astateCockpitState & 4) != 0)) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) && ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.retoggleLight();
        }
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
    private float              pictMet1;
    private static final float rpmScale[]               = { 0.0F, 8F, 23.5F, 40F, 58.5F, 81F, 104.5F, 130.2F, 158.5F, 187F, 217.5F, 251.1F, 281.5F, 289.5F, 295.5F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    private Gun                gun[];

}
