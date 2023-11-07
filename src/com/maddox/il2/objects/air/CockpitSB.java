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
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitSB.this.setTmp = CockpitSB.this.setOld;
            CockpitSB.this.setOld = CockpitSB.this.setNew;
            CockpitSB.this.setNew = CockpitSB.this.setTmp;
            CockpitSB.this.setNew.altimeter = CockpitSB.this.fm.getAltitude();
            if (CockpitSB.this.cockpitDimControl) {
                if ((CockpitSB.this.setNew.dimPosition > 0.0F) && (CockpitSB.this.setNew.ironSight == 0.0F)) {
                    CockpitSB.this.setNew.dimPosition = CockpitSB.this.setNew.dimPosition - 0.05F;
                }
            } else if ((CockpitSB.this.setNew.dimPosition < 1.0F) && (CockpitSB.this.setNew.ironSight == 0.0F)) {
                CockpitSB.this.setNew.dimPosition = CockpitSB.this.setNew.dimPosition + 0.05F;
            }
            CockpitSB.this.setNew.throttle1 = (0.91F * CockpitSB.this.setOld.throttle1) + (0.09F * CockpitSB.this.fm.EI.engines[0].getControlThrottle());
            CockpitSB.this.setNew.throttle2 = (0.91F * CockpitSB.this.setOld.throttle2) + (0.09F * CockpitSB.this.fm.EI.engines[1].getControlThrottle());
            CockpitSB.this.setNew.mix1 = (0.88F * CockpitSB.this.setOld.mix1) + (0.12F * CockpitSB.this.fm.EI.engines[0].getControlMix());
            CockpitSB.this.setNew.mix2 = (0.88F * CockpitSB.this.setOld.mix2) + (0.12F * CockpitSB.this.fm.EI.engines[1].getControlMix());
            if (Math.abs(CockpitSB.this.fm.Or.getKren()) < 30F) {
                CockpitSB.this.setNew.azimuth.setDeg(CockpitSB.this.setOld.azimuth.getDeg(1.0F), -CockpitSB.this.fm.Or.azimut() - 90F);
            }
            if (CockpitSB.this.useRealisticNavigationInstruments()) {
                CockpitSB.this.setNew.waypointAzimuth.setDeg(CockpitSB.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitSB.this.fm.Or.azimut());
            } else {
                CockpitSB.this.setNew.waypointAzimuth.setDeg(CockpitSB.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitSB.this.waypointAzimuth(10F) - 90F);
            }
            CockpitSB.this.w.set(CockpitSB.this.fm.getW());
            CockpitSB.this.fm.Or.transform(CockpitSB.this.w);
            CockpitSB.this.setNew.turn = ((12F * CockpitSB.this.setOld.turn) + CockpitSB.this.w.z) / 13F;
            CockpitSB.this.setNew.vspeed = ((199F * CockpitSB.this.setOld.vspeed) + CockpitSB.this.fm.getVertSpeed()) / 200F;
            CockpitSB.this.setNew.manifold1 = (0.8F * CockpitSB.this.setOld.manifold1) + (0.2F * CockpitSB.this.fm.EI.engines[0].getManifoldPressure() * 76F);
            CockpitSB.this.setNew.manifold2 = (0.8F * CockpitSB.this.setOld.manifold2) + (0.2F * CockpitSB.this.fm.EI.engines[1].getManifoldPressure() * 76F);
            float f = 25F;
            if ((CockpitSB.this.gearsLever != 0.0F) && (CockpitSB.this.gears == CockpitSB.this.fm.CT.getGear())) {
                CockpitSB.this.gearsLever = CockpitSB.this.gearsLever * 0.8F;
                if (Math.abs(CockpitSB.this.gearsLever) < 0.1F) {
                    CockpitSB.this.gearsLever = 0.0F;
                }
            } else if (CockpitSB.this.gears < CockpitSB.this.fm.CT.getGear()) {
                CockpitSB.this.gears = CockpitSB.this.fm.CT.getGear();
                CockpitSB.this.gearsLever = CockpitSB.this.gearsLever + 2.0F;
                if (CockpitSB.this.gearsLever > f) {
                    CockpitSB.this.gearsLever = f;
                }
            } else if (CockpitSB.this.gears > CockpitSB.this.fm.CT.getGear()) {
                CockpitSB.this.gears = CockpitSB.this.fm.CT.getGear();
                CockpitSB.this.gearsLever = CockpitSB.this.gearsLever - 2.0F;
                if (CockpitSB.this.gearsLever < -f) {
                    CockpitSB.this.gearsLever = -f;
                }
            }
            f = 20F;
            if ((CockpitSB.this.flapsLever != 0.0F) && (CockpitSB.this.flaps == CockpitSB.this.fm.CT.getFlap())) {
                CockpitSB.this.flapsLever = CockpitSB.this.flapsLever * 0.8F;
                if (Math.abs(CockpitSB.this.flapsLever) < 0.1F) {
                    CockpitSB.this.flapsLever = 0.0F;
                }
            } else if (CockpitSB.this.flaps < CockpitSB.this.fm.CT.getFlap()) {
                CockpitSB.this.flaps = CockpitSB.this.fm.CT.getFlap();
                CockpitSB.this.flapsLever = CockpitSB.this.flapsLever + 2.0F;
                if (CockpitSB.this.flapsLever > f) {
                    CockpitSB.this.flapsLever = f;
                }
            } else if (CockpitSB.this.flaps > CockpitSB.this.fm.CT.getFlap()) {
                CockpitSB.this.flaps = CockpitSB.this.fm.CT.getFlap();
                CockpitSB.this.flapsLever = CockpitSB.this.flapsLever - 2.0F;
                if (CockpitSB.this.flapsLever < -f) {
                    CockpitSB.this.flapsLever = -f;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float         altimeter;
        float         throttle1;
        float         throttle2;
        float         dimPosition;
        float         ironSight;
        AnglesFork    azimuth;
        AnglesFork    waypointAzimuth;
        float         turn;
        float         mix1;
        float         mix2;
        float         vspeed;
        private float manifold1;
        private float manifold2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.manifold1 = 0.0F;
            this.manifold2 = 0.0F;
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    protected void reflectPlaneToModel() {
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitSB() {
        super("3DO/Cockpit/SB/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.gears = 0.0F;
        this.gearsLever = 0.0F;
        this.flapsLever = 0.0F;
        this.flaps = 0.0F;
        this.w = new Vector3f();
        this.isSlideRight = false;
        this.oldStyleAlt = true;
        this.bNeedSetUp = true;
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.printCompassHeading = true;
        this.cockpitNightMats = (new String[] { "arrow", "DPrib_one", "DPrib_one_new", "DPrib_two", "DPrib_three", "DPrib_four", "DPrib_six", "Prib_one", "Prib_one_new", "Prib_two", "Prib_three", "Prib_four", "Prib_five", "Prib_six", "Shkala" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK01", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK02", this.light2);
        if (Mission.getMissionDate(false) > 0x127de95) {
            this.oldStyleAlt = false;
            this.mesh.materialReplace("Prib_one", "Prib_one_new");
            this.mesh.materialReplace("Prib_one_night", "Prib_one_new_night");
            this.mesh.materialReplace("DPrib_one", "DPrib_one_new");
            this.mesh.materialReplace("DPrib_one_night", "DPrib_one_new_night");
            this.mesh.chunkVisible("Z_ND_alt_km", true);
            this.setNightMats(false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.15F, 0.99F, 0.0F, 0.655F);
        this.mesh.chunkSetLocate("Z_cannopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_cannopyGlass", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_cannopy_lock", this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.15F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkVisible("ZFlareRedR", (this.fm.CT.getGearR() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("ZFlareRedL", (this.fm.CT.getGearL() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("ZFlareGreenR", (this.fm.CT.getGearR() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("ZFlareGreenL", (this.fm.CT.getGearL() > 0.99F) && this.fm.Gears.lgear);
        this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl);
        this.mesh.chunkSetAngles("Z_column", this.pictElev * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_column_tros", -this.pictElev * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_wheel", -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 45F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_gears", this.gearsLever, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Elevator_Trim", 600F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_trim_L", 600F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_trim_R", 600F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_flaps", -this.flapsLever, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.flapsLever * 0.0035F;
        this.mesh.chunkSetLocate("Z_flaps_tros", Cockpit.xyz, Cockpit.ypr);
        float f1 = (this.interp(this.setNew.throttle1, this.setOld.throttle1, f) * 70F) - 35F;
        this.mesh.chunkSetAngles("Z_throtle_L", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 1.1F, 0.0F, 2.0F), CockpitSB.trosThrottleScale);
        this.mesh.chunkSetAngles("Z_throtle_tros_L", f1, 0.0F, 0.0F);
        f1 = (this.interp(this.setNew.throttle2, this.setOld.throttle2, f) * 70F) - 35F;
        this.mesh.chunkSetAngles("Z_throtle_R", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 1.1F, 0.0F, 2.0F), CockpitSB.trosThrottleScale);
        this.mesh.chunkSetAngles("Z_throtle_tros_R", f1, 0.0F, 0.0F);
        f1 = this.cvt(this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 1.2F, 25F, -25F);
        this.mesh.chunkSetAngles("Z_mix_L", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 1.2F, 0.0F, 2.0F), CockpitSB.trosMixScale);
        this.mesh.chunkSetAngles("Z_mix_tros_L", f1, 0.0F, 0.0F);
        f1 = this.cvt(this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 1.2F, 25F, -25F);
        this.mesh.chunkSetAngles("Z_mix_R", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 1.2F, 0.0F, 2.0F), CockpitSB.trosMixScale);
        this.mesh.chunkSetAngles("Z_mix_tros_R", f1, 0.0F, 0.0F);
        f1 = this.cvt(this.fm.EI.engines[0].getControlProp(), 0.0F, 1.0F, 20F, -35F);
        this.mesh.chunkSetAngles("Z_pitch", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.fm.EI.engines[0].getControlProp(), 0.0F, 1.0F, 0.0F, 2.0F), CockpitSB.trosPropPitchScale);
        this.mesh.chunkSetAngles("Z_pitch_tros", f1, 0.0F, 0.0F);
        f1 = (this.fm.EI.engines[0].getControlRadiator() * 60F) - 30F;
        this.mesh.chunkSetAngles("Z_radiator_L", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_radiator_tros_L", f1, 0.0F, 0.0F);
        f1 = (this.fm.EI.engines[1].getControlRadiator() * 60F) - 30F;
        this.mesh.chunkSetAngles("Z_radiator_R", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_radiator_tros_R", f1, 0.0F, 0.0F);
        f1 = this.cvt(this.fm.EI.engines[0].getEngineLoad(), 0.0F, 0.018F, -15F, 20F);
        this.mesh.chunkSetAngles("Z_fire_L", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fire_tros_L", -f1, 0.0F, 0.0F);
        f1 = this.cvt(this.fm.EI.engines[1].getEngineLoad(), 0.0F, 0.018F, -15F, 20F);
        this.mesh.chunkSetAngles("Z_fire_R", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fire_tros_R", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_variometr2", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_ND_rpm_L", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 268F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_rpm_R", -this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3000F, 0.0F, 268F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ag1", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ag2", this.cvt(this.fm.Or.getTangage(), -45F, 45F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_airspeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed2KMH()), 0.0F, 600F, 0.0F, 15F), CockpitSB.speedometerScale), 0.0F, 0.0F);
        if (this.oldStyleAlt) {
            this.mesh.chunkSetAngles("Z_ND_alt_m", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 12000F, 0.0F, 360F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_ND_alt_m", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_alt_km", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        }
        f1 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_ND_ball", this.cvt(f1, -4F, 4F, 8F, -8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_ball2", this.cvt(f1, -4F, 4F, 5F, -5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_boost_L", this.cvt(this.interp(this.setNew.manifold1, this.setOld.manifold1, f), 30F, 120F, -25F, -328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_boost_R", this.cvt(this.interp(this.setNew.manifold2, this.setOld.manifold2, f), 30F, 120F, -25F, -328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Turn", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_clock_hour", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_clock_min", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        float f2 = this.fm.M.fuel / this.fm.M.maxFuel;
        this.mesh.chunkSetAngles("Z_ND_fuel_1", this.cvt(f2, 0.0F, 0.5F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuel_2", this.cvt(f2, 0.5F, 1.0F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuel_3", this.cvt(f2, 0.0F, 0.5F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuel_4", this.cvt(f2, 0.5F, 1.0F, 0.0F, -71F), 0.0F, 0.0F);
        if (f2 > 0.5D) {
            f2 = this.cvt(f2, 0.5F, 0.505F, -200F, -290F);
        } else if (f2 > 0.4995D) {
            f2 = this.cvt(f2, 0.4995F, 0.5F, -290F, -200F);
        } else {
            f2 = this.cvt(f2, 0.0F, 0.005F, 0.0F, -290F);
        }
        this.mesh.chunkSetAngles("Z_ND_fuelpress_L", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuelpress_R", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_L", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_brake_L", -15F * this.fm.CT.getBrakeL(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_brake_R", -15F * this.fm.CT.getBrakeR(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -25F, 20F, -0.041F, 0.031F);
        this.mesh.chunkSetLocate("Z_ND_inclinometer", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_nd_k5_arrow2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        SB sb = (SB) this.aircraft();
        this.mesh.chunkSetAngles("Z_nd_k5_arrow1", -sb.headingBug, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_direct", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oilpress_L", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilIn), 0.0F, 8.5F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oilpress_R", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilIn), 0.0F, 8.5F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oiltemp_L", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 110F, 0.0F, 7F), CockpitSB.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oiltemp_R", this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 40F, 110F, 0.0F, 7F), CockpitSB.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_watertemp_L", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, -107F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_watertemp_R", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, -107F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_toggle_1", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_toggle_2", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_toggle_3", this.cockpitLightControl ? 100F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_toggle_4", this.fm.AS.bNavLightsOn ? 100F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_toggle_5", this.fm.AS.bLandingLightOn ? 100F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_magneto_L", -this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 84F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_magneto_R", -this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 84F), 0.0F, 0.0F);
        this.mesh.chunkVisible("L_RED", this.fm.CT.pdiLights == 1);
        this.mesh.chunkVisible("L_WHITE", this.fm.CT.pdiLights == 2);
        this.mesh.chunkVisible("L_GREEN", this.fm.CT.pdiLights == 3);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.5F, 0.6F);
            this.light2.light.setEmit(0.5F, 0.6F);
            this.mesh.materialReplace("shturval2", "shturval2_light");
            this.mesh.materialReplace("clepki2", "clepki2_light");
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.mesh.materialReplace("shturval2", "shturval2");
            this.mesh.materialReplace("clepki2", "clepki2");
        }
        this.setNightMats(false);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("front_cannopy_DM2", true);
            this.mesh.chunkVisible("front_cannopy_DM1", true);
            this.mesh.chunkVisible("Main_DM1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("prib_02", false);
            this.mesh.chunkVisible("Dprib_02", true);
            this.mesh.chunkVisible("prib_05", false);
            this.mesh.chunkVisible("Dprib_05", true);
            this.mesh.chunkVisible("Panel_DM", true);
            this.mesh.chunkVisible("Z_ND_airspeed", false);
            this.mesh.chunkVisible("Z_ND_Turn", false);
            this.mesh.chunkVisible("Z_ND_ball", false);
            this.mesh.chunkVisible("Z_ND_variometr2", false);
            this.mesh.chunkVisible("Z_ND_alt_km", false);
            this.mesh.chunkVisible("Z_ND_alt_m", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_cannopy_DM2", true);
            this.mesh.chunkVisible("Z_cannopy_DM5", true);
            this.mesh.chunkVisible("prib_04", false);
            this.mesh.chunkVisible("Dprib_04", true);
            this.mesh.chunkVisible("Panel_DM", true);
            this.mesh.chunkVisible("Main_DM2", true);
            this.mesh.chunkVisible("Z_ND_oiltemp_R", false);
            this.mesh.chunkVisible("Z_ND_fuelpress_R", false);
            this.mesh.chunkVisible("Z_ND_oilpress_L", false);
            this.mesh.chunkVisible("Z_ND_fuel_1", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_cannopy_DM1", true);
            this.mesh.chunkVisible("Z_cannopy_DM4", true);
            this.mesh.chunkVisible("prib_01", false);
            this.mesh.chunkVisible("Dprib_01", true);
            this.mesh.chunkVisible("prib_06", false);
            this.mesh.chunkVisible("Dprib_06", true);
            this.mesh.chunkVisible("Panel_DM", true);
            this.mesh.chunkVisible("Main_DM3", true);
            this.mesh.chunkVisible("Z_ND_clock_hour", false);
            this.mesh.chunkVisible("Z_ND_clock_min", false);
            this.mesh.chunkVisible("Z_ND_ag2", false);
            this.mesh.chunkVisible("Z_ND_ag1", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Z_cannopy_DM3", false);
            this.mesh.chunkVisible("prib_03", false);
            this.mesh.chunkVisible("Dprib_03", true);
            this.mesh.chunkVisible("Panel_DM", true);
            this.mesh.chunkVisible("Z_ND_rpm_R", false);
            this.mesh.chunkVisible("Z_ND_boost_R", false);
            this.mesh.chunkVisible("Z_ND_watertemp_L", false);
        }
    }

    public boolean isViewRight() {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HookPilot.current.computePos(this, loc, loc1);
        float f = loc1.getOrient().getYaw();
        if (f < 0.0F) {
            this.isSlideRight = true;
        } else {
            this.isSlideRight = false;
        }
        return this.isSlideRight;
    }

    protected boolean doFocusEnter() {
        return super.doFocusEnter();
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            super.doFocusLeave();
            return;
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private float              gears;
    private float              gearsLever;
    private float              flapsLever;
    private float              flaps;
    public Vector3f            w;
    private boolean            isSlideRight;
    private boolean            oldStyleAlt;
    private boolean            bNeedSetUp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private static final float speedometerScale[]   = { 0.0F, -10F, -19.5F, -32F, -46F, -66.5F, -89F, -114F, -141F, -170.5F, -200.5F, -232.5F, -264F, -295.5F, -328F, -360F };
    private static final float oilTempScale[]       = { -28F, -52.5F, -84F, -118.5F, -157F, -203F, -255F, -329F };
    private static final float trosThrottleScale[]  = { -31F, 3.5F, 40.5F };
    private static final float trosMixScale[]       = { -23.5F, 0.0F, 24.5F };
    private static final float trosPropPitchScale[] = { 18.5F, -7.5F, -35F };

    static {
        Property.set(CockpitSB.class, "normZN", 0.9F);
    }
}
