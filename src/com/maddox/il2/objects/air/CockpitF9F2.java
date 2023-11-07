package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitF9F2 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF9F2.this.fm != null) {
                CockpitF9F2.this.setTmp = CockpitF9F2.this.setOld;
                CockpitF9F2.this.setOld = CockpitF9F2.this.setNew;
                CockpitF9F2.this.setNew = CockpitF9F2.this.setTmp;
                CockpitF9F2.this.setNew.throttle = (0.9F * CockpitF9F2.this.setOld.throttle) + (CockpitF9F2.this.fm.CT.PowerControl * 0.1F);
                CockpitF9F2.this.setNew.altimeter = CockpitF9F2.this.fm.getAltitude();
                CockpitF9F2.this.setNew.waypointAzimuth.setDeg(CockpitF9F2.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitF9F2.this.waypointAzimuth() - CockpitF9F2.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitF9F2.this.fm.Or.getKren()) < 30F) {
                    CockpitF9F2.this.setNew.azimuth.setDeg(CockpitF9F2.this.setOld.azimuth.getDeg(1.0F), CockpitF9F2.this.fm.Or.azimut());
                }
                CockpitF9F2.this.setNew.vspeed = ((199F * CockpitF9F2.this.setOld.vspeed) + CockpitF9F2.this.fm.getVertSpeed()) / 200F;
                if (CockpitF9F2.this.fm.CT.arrestorControl <= 0.5F) {
                    if (CockpitF9F2.this.setNew.stbyPosition > 0.0F) {
                        CockpitF9F2.this.setNew.stbyPosition = CockpitF9F2.this.setOld.stbyPosition - 0.05F;
                    }
                } else if (CockpitF9F2.this.setNew.stbyPosition < 1.0F) {
                    CockpitF9F2.this.setNew.stbyPosition = CockpitF9F2.this.setOld.stbyPosition + 0.05F;
                }
                if ((CockpitF9F2.this.fm.EI.engines[0].getStage() < 6) && !CockpitF9F2.this.fm.CT.bHasAileronControl && !CockpitF9F2.this.fm.CT.bHasElevatorControl && !CockpitF9F2.this.fm.CT.bHasGearControl && !CockpitF9F2.this.fm.CT.bHasAirBrakeControl && !CockpitF9F2.this.fm.CT.bHasFlapsControl) {
                    if (CockpitF9F2.this.setNew.stbyPosition3 > 0.0F) {
                        CockpitF9F2.this.setNew.stbyPosition3 = CockpitF9F2.this.setOld.stbyPosition3 - 0.005F;
                    }
                } else if (CockpitF9F2.this.setNew.stbyPosition3 < 1.0F) {
                    CockpitF9F2.this.setNew.stbyPosition3 = CockpitF9F2.this.setOld.stbyPosition3 + 0.005F;
                }
                float f = ((F9F) CockpitF9F2.this.aircraft()).k14Distance;
                CockpitF9F2.this.setNew.k14w = (5F * CockpitF9F2.k14TargetWingspanScale[((F9F) CockpitF9F2.this.aircraft()).k14WingspanType]) / f;
                CockpitF9F2.this.setNew.k14w = (0.9F * CockpitF9F2.this.setOld.k14w) + (0.1F * CockpitF9F2.this.setNew.k14w);
                CockpitF9F2.this.setNew.k14wingspan = (0.9F * CockpitF9F2.this.setOld.k14wingspan) + (0.1F * CockpitF9F2.k14TargetMarkScale[((F9F) CockpitF9F2.this.aircraft()).k14WingspanType]);
                CockpitF9F2.this.setNew.k14mode = (0.8F * CockpitF9F2.this.setOld.k14mode) + (0.2F * ((F9F) CockpitF9F2.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitF9F2.this.aircraft().FM.getW();
                double d = 0.00125D * f;
                float f1 = (float) Math.toDegrees(d * vector3d.z);
                float f2 = -(float) Math.toDegrees(d * vector3d.y);
                float f3 = CockpitF9F2.this.floatindex((f - 200F) * 0.04F, CockpitF9F2.k14BulletDrop) - CockpitF9F2.k14BulletDrop[0];
                f2 += (float) Math.toDegrees(Math.atan(f3 / f));
                CockpitF9F2.this.setNew.k14x = (0.92F * CockpitF9F2.this.setOld.k14x) + (0.08F * f1);
                CockpitF9F2.this.setNew.k14y = (0.92F * CockpitF9F2.this.setOld.k14y) + (0.08F * f2);
                if (CockpitF9F2.this.setNew.k14x > 7F) {
                    CockpitF9F2.this.setNew.k14x = 7F;
                }
                if (CockpitF9F2.this.setNew.k14x < -7F) {
                    CockpitF9F2.this.setNew.k14x = -7F;
                }
                if (CockpitF9F2.this.setNew.k14y > 7F) {
                    CockpitF9F2.this.setNew.k14y = 7F;
                }
                if (CockpitF9F2.this.setNew.k14y < -7F) {
                    CockpitF9F2.this.setNew.k14y = -7F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      vspeed;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;
        float      stbyPosition;
        float      stbyPosition3;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    public CockpitF9F2() {
        super("3DO/Cockpit/F9/F9.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.pictMet1 = 0.0F;
        this.cockpitNightMats = (new String[] { "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05", "Gauges_06", "Gauges_08", "Compass1", "Needles" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
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
            super.doFocusLeave();
            return;
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public void reflectWorldToInstruments(float f) {
        if (F9F.bChangedPit) {
            F9F.bChangedPit = false;
        }
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            int i = ((F9F) this.aircraft()).k14Mode;
            boolean flag = i < 2;
            this.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.setNew.k14w;
            for (int k = 1; k < 7; k++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + k, flag);
                this.mesh.chunkSetLocate("Z_Z_AIMMARK" + k, Cockpit.xyz, Cockpit.ypr);
            }

        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, -0.52F, 0.0F);
        this.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (!this.fm.brakeShoe && (this.fm.Gears.nOfGearsOnGr > 0)) {
            Cockpit.xyz[1] = -0.02F;
            this.mesh.chunkSetLocate("Body", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Boites", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever01", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever02", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever03", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever04", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Collimateur", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("EjectSeat", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("EjectSeat2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("EjectSeat3", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearDn_C", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearDn_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearDn_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearUp_C", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearUp_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearUp_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Instruments", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("InstrumentsD", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Interior", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Manette", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("NewVis", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Panel", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("PareBrise2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("PareBrise", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Placards", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Stick01", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Stick02", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Vis2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Vis", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("VisFace", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage3", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Canopy1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Canopy2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlapsLever", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlareFire1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlareFire2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlareFuel", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_GearHandle", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Hook2a", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Hook2b", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Hook", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Ignition1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Ignition2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Master1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Master2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Slide1a", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Target1", Cockpit.xyz, Cockpit.ypr);
            if (this.fm.CT.getCockpitDoor() == 1.0F) {
                this.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
                this.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
                this.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
            }
        }
        this.resetYPRmodifier();
        if (this.fm.Gears.nOfGearsOnGr == 0) {
            Cockpit.xyz[1] = 0.0F;
            this.mesh.chunkSetLocate("Body", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Boites", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever01", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever02", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever03", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("CanLever04", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Collimateur", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("EjectSeat", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("EjectSeat2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("EjectSeat3", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearDn_C", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearDn_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearDn_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearUp_C", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearUp_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("FlareGearUp_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Instruments", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("InstrumentsD", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Interior", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Manette", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("NewVis", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Panel", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("PareBrise2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("PareBrise", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Placards", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Stick01", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Stick02", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Vis2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Vis", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("VisFace", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage3", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("XGlassDamage4", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Canopy1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Canopy2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlapsLever", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlareFire1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlareFire2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_FlareFuel", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_GearHandle", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Hook2a", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Hook2b", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Hook", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Ignition1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Ignition2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Master1", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Master2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Slide1a", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetLocate("Z_Target1", Cockpit.xyz, Cockpit.ypr);
            if (this.fm.CT.getCockpitDoor() == 1.0F) {
                this.mesh.chunkSetLocate("CanopyOpen01", Cockpit.xyz, Cockpit.ypr);
                this.mesh.chunkSetLocate("CanopyOpen02", Cockpit.xyz, Cockpit.ypr);
                this.mesh.chunkSetLocate("CanopyOpen03", Cockpit.xyz, Cockpit.ypr);
            }
        }
        this.mesh.chunkSetAngles("Z_GearHandle", 0.0F, 0.0F, 50F * (this.pictGear = (0.82F * this.pictGear) + (0.18F * this.fm.CT.GearControl)));
        this.mesh.chunkSetAngles("Z_FlapsLever", -35F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hook", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition, this.setOld.stbyPosition, f), 0.0F, 1.0F, 0.0F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_Target1", this.setNew.k14wingspan, 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_G-Factor", this.cvt(this.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel", this.cvt(this.fm.M.fuel, 0.0F, 1358F, -150F, 150F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Turn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Slide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -24F, 24F), 0.0F);
        this.mesh.chunkSetAngles("Z_horizont1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Suction", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 100F, 395F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.035F, 0.035F);
        this.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0328F, -0.0328F);
        this.mesh.chunkSetLocate("Z_horizont1b", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Stick01", (this.pictElev = (0.7F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F);
        this.mesh.chunkSetAngles("Stick02", (this.pictElev = (0.7F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F);
        this.pictMet1 = (0.96F * this.pictMet1) + (0.04F * (0.6F * this.fm.EI.engines[0].getThrustOutput() * this.fm.EI.engines[0].getControlThrottle() * (this.fm.EI.engines[0].getStage() != 6 ? 0.02F : 1.0F)));
        this.mesh.chunkSetAngles("Z_Compass-Emerg1", this.cvt(this.fm.Or.getTangage(), -30F, 30F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass-Emerg3", this.cvt(this.fm.Or.getKren(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass-Emerg2", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM-1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2550F, -180F, 50F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM-2", this.cvt(this.fm.EI.engines[0].getRPM(), 2550F, 3500F, -10F, 335F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Exhaust_Temp", this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 95F, -160F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter_1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter_2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter_3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2_1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2_2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2_3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 1219.2F, -150F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitF9F2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour2", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute2", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second2", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkVisible("FlareGearUp_R", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearUp_L", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearUp_C", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("FlareGearDn_R", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearDn_L", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearDn_C", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("Z_FlareFuel", this.fm.M.fuel < 150F);
        this.mesh.chunkVisible("Z_FlareFire1", this.fm.AS.astateEngineStates[0] > 2);
        this.mesh.chunkVisible("Z_FlareFire2", this.fm.AS.astateEngineStates[0] > 2);
        this.resetYPRmodifier();
        Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f) * 45F;
        Cockpit.xyz[2] = this.cvt(Cockpit.ypr[0], 7.5F, 9.5F, 0.0F, 0.0F);
        this.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateEngineStates[0] > 2) && (this.fm.actor instanceof CockpitF9F2)) {
            Loc loc = new Loc();
            this.fm.actor.pos.getCurrent(loc);
            Eff3DActor.New(this.fm.actor, this.fm.actor.findHook("_Smoke"), null, 1.0F, "EFFECTS/Smokes/SmokeBoiling.eff", 1200F);
        }
        if (this.fm.AS.bIsAboutToBailout && !this.fm.brakeShoe && (this.fm.getSpeedKMH() >= 100F) && (this.fm.Gears.nOfGearsOnGr == 0)) {
            this.mesh.chunkVisible("Z_Canopy1", false);
            this.mesh.chunkVisible("Z_Canopy2", true);
        } else {
            this.mesh.chunkVisible("Z_Canopy1", true);
            this.mesh.chunkVisible("Z_Canopy2", false);
        }
        if (this.fm.AS.bIsAboutToBailout) {
            this.mesh.chunkVisible("CanopyOpen01", false);
            this.mesh.chunkVisible("CanopyOpen02", false);
            this.mesh.chunkVisible("CanopyOpen03", false);
            this.mesh.chunkVisible("XGlassDamage4", false);
        }
        if ((this.fm.CT.cockpitDoorControl == 1.0F) && !this.fm.AS.bIsAboutToBailout) {
            if ((this.fm.CT.getCockpitDoor() > 0.0F) && (this.fm.CT.getCockpitDoor() < 0.2F)) {
                this.mesh.chunkVisible("CanLever01", false);
                this.mesh.chunkVisible("CanLever02", true);
                this.mesh.chunkVisible("CanLever04", false);
            } else {
                this.mesh.chunkVisible("CanLever01", true);
                this.mesh.chunkVisible("CanLever02", false);
                this.mesh.chunkVisible("CanLever04", false);
            }
        } else if ((this.fm.CT.cockpitDoorControl == 0.0F) && !this.fm.AS.bIsAboutToBailout) {
            if ((this.fm.CT.getCockpitDoor() < 1.0F) && (this.fm.CT.getCockpitDoor() > 0.8F)) {
                this.mesh.chunkVisible("CanLever01", false);
                this.mesh.chunkVisible("CanLever03", true);
                this.mesh.chunkVisible("CanLever04", false);
            } else {
                this.mesh.chunkVisible("CanLever01", true);
                this.mesh.chunkVisible("CanLever03", false);
                this.mesh.chunkVisible("CanLever04", false);
            }
        } else if (this.fm.AS.bIsAboutToBailout && (this.fm.getSpeedKMH() < 100F) && (this.fm.Gears.nOfGearsOnGr > 0)) {
            this.mesh.chunkVisible("CanLever01", false);
            this.mesh.chunkVisible("CanLever04", true);
        }
        if (this.fm.CT.BayDoorControl == 1.0F) {
            this.mesh.chunkVisible("Z_Master1", false);
            this.mesh.chunkVisible("Z_Master2", true);
        }
        if (this.fm.CT.BayDoorControl == 0.0F) {
            this.mesh.chunkVisible("Z_Master1", true);
            this.mesh.chunkVisible("Z_Master2", false);
        }
        if (this.fm.CT.arrestorControl <= 0.5F) {
            this.mesh.chunkVisible("Z_Hook2b", false);
            this.mesh.chunkVisible("Z_Hook2a", true);
        }
        if (this.fm.CT.arrestorControl > 0.5F) {
            this.mesh.chunkVisible("Z_Hook2a", false);
            this.mesh.chunkVisible("Z_Hook2b", true);
        }
        if ((this.fm.EI.engines[0].getStage() < 6) && (this.fm.Gears.nOfGearsOnGr == 0)) {
            this.mesh.chunkVisible("Z_Ignition1", false);
            this.mesh.chunkVisible("Z_Ignition2", true);
        } else {
            this.mesh.chunkVisible("Z_Ignition1", true);
            this.mesh.chunkVisible("Z_Ignition2", false);
        }
        if (((F9F) this.aircraft()).k14Mode == 2) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
        } else {
            this.mesh.chunkVisible("Z_Z_RETICLE", true);
        }
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

    public void doToggleDim() {
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictGear;
    private float              pictMet1;
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 11F, 11.3F, 11.8F, 15F, 20F, 25F, 30F, 35F, 40F, 43.05F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    private static final float speedometerScale[]       = { 0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 249.4F, 271.7F, 294F, 316.5F, 339.5F };

}
