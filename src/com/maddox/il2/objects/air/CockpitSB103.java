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
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB103 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitSB103.this.setTmp = CockpitSB103.this.setOld;
            CockpitSB103.this.setOld = CockpitSB103.this.setNew;
            CockpitSB103.this.setNew = CockpitSB103.this.setTmp;
            if (CockpitSB103.this.RPK) {
                float f = CockpitSB103.this.fm.EI.engines[0].getRPM() > 0.0F ? 1.0F : 0.0F;
                float f2 = CockpitSB103.this.fm.EI.engines[1].getRPM() > 0.0F ? 1.0F : 0.0F;
                CockpitSB103.this.setNew.Stoichion1 = (0.95F * CockpitSB103.this.setOld.Stoichion1) - (0.05F * f * ((3F * CockpitSB103.this.setNew.mix1) + CockpitSB103.this.cvt(CockpitSB103.this.fm.EI.engines[0].getDistabilisationAmplitude(), 0.0F, 3F, 0.0F, 35F)));
                CockpitSB103.this.setNew.Stoichion2 = (0.95F * CockpitSB103.this.setOld.Stoichion2) - (0.05F * f2 * ((3F * CockpitSB103.this.setNew.mix2) + CockpitSB103.this.cvt(CockpitSB103.this.fm.EI.engines[1].getDistabilisationAmplitude(), 0.0F, 3F, 0.0F, 35F)));
            }
            CockpitSB103.this.setNew.altimeter = CockpitSB103.this.fm.getAltitude();
            if (CockpitSB103.this.cockpitDimControl) {
                if ((CockpitSB103.this.setNew.dimPosition > 0.0F) && (CockpitSB103.this.setNew.ironSight == 0.0F)) {
                    CockpitSB103.this.setNew.dimPosition = CockpitSB103.this.setNew.dimPosition - 0.05F;
                }
            } else if ((CockpitSB103.this.setNew.dimPosition < 1.0F) && (CockpitSB103.this.setNew.ironSight == 0.0F)) {
                CockpitSB103.this.setNew.dimPosition = CockpitSB103.this.setNew.dimPosition + 0.05F;
            }
            CockpitSB103.this.setNew.throttle1 = (0.91F * CockpitSB103.this.setOld.throttle1) + (0.09F * CockpitSB103.this.fm.EI.engines[0].getControlThrottle());
            CockpitSB103.this.setNew.throttle2 = (0.91F * CockpitSB103.this.setOld.throttle2) + (0.09F * CockpitSB103.this.fm.EI.engines[1].getControlThrottle());
            CockpitSB103.this.setNew.mix1 = (0.88F * CockpitSB103.this.setOld.mix1) + (0.12F * CockpitSB103.this.fm.EI.engines[0].getControlMix());
            CockpitSB103.this.setNew.mix2 = (0.88F * CockpitSB103.this.setOld.mix2) + (0.12F * CockpitSB103.this.fm.EI.engines[1].getControlMix());
            if (Math.abs(CockpitSB103.this.fm.Or.getKren()) < 30F) {
                CockpitSB103.this.setNew.azimuth.setDeg(CockpitSB103.this.setOld.azimuth.getDeg(1.0F), -(-CockpitSB103.this.fm.Or.azimut() - 90F));
            }
            if (CockpitSB103.this.useRealisticNavigationInstruments()) {
                CockpitSB103.this.setNew.waypointAzimuth.setDeg(CockpitSB103.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitSB103.this.fm.Or.azimut() + 90F);
                CockpitSB103.this.setNew.beaconDirection.setDeg(CockpitSB103.this.setOld.beaconDirection.getDeg(1.0F), CockpitSB103.this.getBeaconDirection());
            } else {
                CockpitSB103.this.setNew.waypointAzimuth.setDeg(CockpitSB103.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitSB103.this.waypointAzimuth(1.0F));
                CockpitSB103.this.setNew.beaconDirection.setDeg(CockpitSB103.this.setOld.beaconDirection.getDeg(1.0F), CockpitSB103.this.waypointAzimuth(1.0F) - CockpitSB103.this.fm.Or.azimut() - 90F);
            }
            CockpitSB103.this.w.set(CockpitSB103.this.fm.getW());
            CockpitSB103.this.fm.Or.transform(CockpitSB103.this.w);
            CockpitSB103.this.setNew.turn = ((12F * CockpitSB103.this.setOld.turn) + CockpitSB103.this.w.z) / 13F;
            CockpitSB103.this.setNew.vspeed = ((199F * CockpitSB103.this.setOld.vspeed) + CockpitSB103.this.fm.getVertSpeed()) / 200F;
            CockpitSB103.this.setNew.manifold1 = (0.8F * CockpitSB103.this.setOld.manifold1) + (0.2F * CockpitSB103.this.fm.EI.engines[0].getManifoldPressure() * 76F);
            CockpitSB103.this.setNew.manifold2 = (0.8F * CockpitSB103.this.setOld.manifold2) + (0.2F * CockpitSB103.this.fm.EI.engines[1].getManifoldPressure() * 76F);
            float f1 = 25F;
            if ((CockpitSB103.this.gearsLever != 0.0F) && (CockpitSB103.this.gears == CockpitSB103.this.fm.CT.getGear())) {
                CockpitSB103.this.gearsLever = CockpitSB103.this.gearsLever * 0.8F;
                if (Math.abs(CockpitSB103.this.gearsLever) < 0.1F) {
                    CockpitSB103.this.gearsLever = 0.0F;
                }
            } else if (CockpitSB103.this.gears < CockpitSB103.this.fm.CT.getGear()) {
                CockpitSB103.this.gears = CockpitSB103.this.fm.CT.getGear();
                CockpitSB103.this.gearsLever = CockpitSB103.this.gearsLever + 2.0F;
                if (CockpitSB103.this.gearsLever > f1) {
                    CockpitSB103.this.gearsLever = f1;
                }
            } else if (CockpitSB103.this.gears > CockpitSB103.this.fm.CT.getGear()) {
                CockpitSB103.this.gears = CockpitSB103.this.fm.CT.getGear();
                CockpitSB103.this.gearsLever = CockpitSB103.this.gearsLever - 2.0F;
                if (CockpitSB103.this.gearsLever < -f1) {
                    CockpitSB103.this.gearsLever = -f1;
                }
            }
            f1 = 20F;
            if ((CockpitSB103.this.flapsLever != 0.0F) && (CockpitSB103.this.flaps == CockpitSB103.this.fm.CT.getFlap())) {
                CockpitSB103.this.flapsLever = CockpitSB103.this.flapsLever * 0.8F;
                if (Math.abs(CockpitSB103.this.flapsLever) < 0.1F) {
                    CockpitSB103.this.flapsLever = 0.0F;
                }
            } else if (CockpitSB103.this.flaps < CockpitSB103.this.fm.CT.getFlap()) {
                CockpitSB103.this.flaps = CockpitSB103.this.fm.CT.getFlap();
                CockpitSB103.this.flapsLever = CockpitSB103.this.flapsLever + 2.0F;
                if (CockpitSB103.this.flapsLever > f1) {
                    CockpitSB103.this.flapsLever = f1;
                }
            } else if (CockpitSB103.this.flaps > CockpitSB103.this.fm.CT.getFlap()) {
                CockpitSB103.this.flaps = CockpitSB103.this.fm.CT.getFlap();
                CockpitSB103.this.flapsLever = CockpitSB103.this.flapsLever - 2.0F;
                if (CockpitSB103.this.flapsLever < -f1) {
                    CockpitSB103.this.flapsLever = -f1;
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
        AnglesFork    beaconDirection;
        float         turn;
        float         mix1;
        float         mix2;
        float         vspeed;
        private float manifold1;
        private float manifold2;
        float         Stoichion1;
        float         Stoichion2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.beaconDirection = new AnglesFork();
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

    public CockpitSB103() {
        super("3DO/Cockpit/SB_103/hier.him", "bf109");
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
        this.bNeedSetUp = true;
        this.RPK = false;
        this.RPK = false;
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.printCompassHeading = true;
        this.cockpitNightMats = (new String[] { "arrow", "DPrib_one", "DPrib_one_new", "DPrib_two", "DPrib_three", "DPrib_four", "DPrib_six", "Prib_one", "Prib_one_new", "Prib_two", "Prib_three", "Prib_four", "Prib_five", "Prib_six", "Shkala" });
        this.setNightMats(false);
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
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public CockpitSB103(String s) {
        super(s, "bf109");
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
        this.bNeedSetUp = true;
        this.RPK = false;
        this.RPK = true;
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.printCompassHeading = true;
        this.cockpitNightMats = (new String[] { "arrow", "DPrib_one", "DPrib_one_new", "DPrib_two", "DPrib_three", "DPrib_four", "DPrib_six", "Prib_one", "Prib_one_new", "Prib_two", "Prib_three", "Prib_four", "Prib_five", "Prib_six", "Shkala", "Alpha", "AVR", "arrow2", "Alpha_dmg", "AVR_dmg", "RPK", "RPK_dmg" });
        this.setNightMats(false);
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
        this.interpPut(new Interpolater(), null, Time.current(), null);
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
        this.mesh.chunkSetAngles("Z_flaps", 0.0F, 0.0F, -this.flapsLever);
        this.mesh.chunkSetAngles("Z_flaps_tros", this.flapsLever, 0.0F, 0.0F);
        float f1 = (this.interp(this.setNew.throttle1, this.setOld.throttle1, f) * 70F) - 35F;
        this.mesh.chunkSetAngles("Z_throtle_L", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 1.1F, 0.0F, 2.0F), CockpitSB103.trosThrottleScale);
        this.mesh.chunkSetAngles("Z_throtle_tros_L", f1, 0.0F, 0.0F);
        f1 = (this.interp(this.setNew.throttle2, this.setOld.throttle2, f) * 70F) - 35F;
        this.mesh.chunkSetAngles("Z_throtle_R", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 1.1F, 0.0F, 2.0F), CockpitSB103.trosThrottleScale);
        this.mesh.chunkSetAngles("Z_throtle_tros_R", f1, 0.0F, 0.0F);
        f1 = this.cvt(this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 1.2F, 25F, -25F);
        this.mesh.chunkSetAngles("Z_mix_L", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 1.2F, 0.0F, 2.0F), CockpitSB103.trosMixScale);
        this.mesh.chunkSetAngles("Z_mix_tros_L", f1, 0.0F, 0.0F);
        f1 = this.cvt(this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 1.2F, 25F, -25F);
        this.mesh.chunkSetAngles("Z_mix_R", f1, 0.0F, 0.0F);
        f1 = this.floatindex(this.cvt(this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 1.2F, 0.0F, 2.0F), CockpitSB103.trosMixScale);
        this.mesh.chunkSetAngles("Z_mix_tros_R", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pitch_L", 0.0F, 0.0F, this.fm.EI.engines[0].getControlProp() * 360F);
        this.mesh.chunkSetAngles("Z_pitch_R", 0.0F, 0.0F, this.fm.EI.engines[1].getControlProp() * 360F);
        f1 = (this.fm.EI.engines[0].getControlRadiator() * 60F) - 30F;
        this.mesh.chunkSetAngles("Z_radiator_L", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_radiator_tros_L", -f1, 0.0F, 0.0F);
        f1 = (this.fm.EI.engines[1].getControlRadiator() * 60F) - 30F;
        this.mesh.chunkSetAngles("Z_radiator_R", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_radiator_tros_R", -f1, 0.0F, 0.0F);
        f1 = this.cvt(this.fm.EI.engines[0].getEngineLoad(), 0.0F, 0.018F, -15F, 20F);
        this.mesh.chunkSetAngles("Z_fire_L", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fire_tros_L", 0.0F, 0.0F, -f1);
        f1 = this.cvt(this.fm.EI.engines[1].getEngineLoad(), 0.0F, 0.018F, -15F, 20F);
        this.mesh.chunkSetAngles("Z_fire_R", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fire_tros_R", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("Z_ND_variometr2", this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_rpm_L", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2800F, 0.0F, 249F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_rpm_R", -this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 2800F, 0.0F, 249F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_airspeed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed2KMH()), 0.0F, 400F, 0.0F, 9F), CockpitSB103.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_alt_m", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_alt_km", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_ND_ball", this.cvt(f1, -4F, 4F, 8F, -8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_boost_L", this.cvt(this.interp(this.setNew.manifold1, this.setOld.manifold1, f), 30F, 120F, 0.0F, -298F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_boost_R", this.cvt(this.interp(this.setNew.manifold2, this.setOld.manifold2, f), 30F, 120F, 0.0F, -298F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_Turn", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, -30F, 30F), 0.0F, 0.0F);
        float f2 = this.fm.M.fuel / this.fm.M.maxFuel;
        this.mesh.chunkSetAngles("Z_ND_fuel_1", this.cvt(f2, 0.0F, 0.5F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuel_2", this.cvt(f2, 0.5F, 1.0F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuel_3", this.cvt(f2, 0.0F, 0.5F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuel_4", this.cvt(f2, 0.5F, 1.0F, 0.0F, -71F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_L", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_brake_L", -15F * this.fm.CT.getBrakeL(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_brake_R", 15F * this.fm.CT.getBrakeR(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_nd_k5_arrow2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        SB sb = (SB) this.aircraft();
        this.mesh.chunkSetAngles("Z_nd_k5_arrow1", -sb.headingBug, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oil_tem_L", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oil_tem_R", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("hMagn_L", -this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 84F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("hMagn_R", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 84F), 0.0F, 0.0F);
        this.mesh.chunkVisible("L_RED", this.fm.CT.pdiLights == 1);
        this.mesh.chunkVisible("L_WHITE", this.fm.CT.pdiLights == 2);
        this.mesh.chunkVisible("L_GREEN", this.fm.CT.pdiLights == 3);
        this.mesh.chunkSetAngles("Z_ND_Air_pres", -122F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        float f3 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1700F, 0.0F, 28F);
        if (this.fm.AS.bLandingLightOn) {
            f3--;
        }
        if (this.fm.AS.bNavLightsOn) {
            f3 -= 2.5F;
        }
        this.mesh.chunkSetAngles("Z_ND_Voltmeter", -f3, 0.0F, 0.0F);
        float f4 = this.fm.CT.getFlap();
        if (f4 < 0.2F) {
            this.mesh.chunkSetAngles("Z_ND_Flaps_position", -this.cvt(f4, 0.0F, 0.2F, 0.0F, 31F), 0.0F, 0.0F);
        } else if (f4 < 0.33F) {
            this.mesh.chunkSetAngles("Z_ND_Flaps_position", -this.cvt(f4, 0.2F, 0.33F, 31F, 50F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_ND_Flaps_position", -this.cvt(f4, 0.33F, 1.0F, 50F, 71F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_ND_clock_hour", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_clock_min", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_clock_sec", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        if (f2 > 0.5D) {
            f2 = this.cvt(f2, 0.5F, 0.505F, -200F, -290F);
        } else if (f2 > 0.4995D) {
            f2 = this.cvt(f2, 0.4995F, 0.5F, -290F, -200F);
        } else {
            f2 = this.cvt(f2, 0.0F, 0.005F, 0.0F, -290F);
        }
        this.mesh.chunkSetAngles("Z_ND_fuelpress_L", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_fuelpress_R", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oilpress_L", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilIn), 0.0F, 8.5F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_oilpress_R", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilIn), 0.0F, 8.5F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_water_tem_L", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ND_water_tem_R", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, -86F), 0.0F, 0.0F);
        if (this.RPK) {
            this.mesh.chunkSetAngles("Z_ND_RPK", -this.cvt(this.setNew.beaconDirection.getDeg(1.0F), -30F, 30F, -45F, 45F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Alpha_L", -this.setNew.Stoichion1, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Alpha_R", -this.setNew.Stoichion2, 0.0F, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.034F, 0.034F);
            Cockpit.xyz[2] = 0.0F;
            this.mesh.chunkSetLocate("Z_ND_ag3", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetAngles("Z_ND_fon_AG3", this.fm.Or.getKren(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Dirrect", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch01", 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch02", 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch03", 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch04", 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch05", 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch06", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_ufo_sw", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_ANO_switch", this.fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_land_l_sw", this.fm.AS.bLandingLightOn ? 90F : 0.0F, 0.0F, 0.0F);
        } else {
            this.resetYPRmodifier();
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.0365F, 0.0365F);
            Cockpit.xyz[2] = 0.0F;
            this.mesh.chunkSetLocate("Z_ND_ag2", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetAngles("Z_ND_fon_AG2", this.fm.Or.getKren(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_ball2", this.cvt(f1, -4F, 4F, 5F, -5F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_ND_Dirrectional", 0.0F, this.setNew.waypointAzimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("Z_ND_switch01", 0.0F, 0.0F, -30F);
            this.mesh.chunkSetAngles("Z_ND_switch02", 0.0F, 0.0F, -30F);
            this.mesh.chunkSetAngles("Z_ND_switch03", 0.0F, 0.0F, -30F);
            this.mesh.chunkSetAngles("Z_ND_switch04", 0.0F, 0.0F, -30F);
            this.mesh.chunkSetAngles("Z_ND_switch05", 0.0F, 0.0F, -30F);
            this.mesh.chunkSetAngles("Z_ND_pit_light_switch", 0.0F, 0.0F, this.cockpitLightControl ? -30F : 30F);
            this.mesh.chunkSetAngles("Z_ND_landing_light_switch", 0.0F, 0.0F, this.fm.AS.bLandingLightOn ? -30F : 30F);
            this.mesh.chunkSetAngles("Z_ND_ANO_switch", 0.0F, 0.0F, this.fm.AS.bNavLightsOn ? -30F : 30F);
        }
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
    private boolean            bNeedSetUp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            RPK;
    private static final float speedometerScale[]  = { 0.0F, 30F, 47F, 5F, 85F, 133.5F, 190F, 248.5F, 305F, 360F };
    private static final float trosThrottleScale[] = { -31F, 3.5F, 40.5F };
    private static final float trosMixScale[]      = { -23.5F, 0.0F, 24.5F };

    static {
        Property.set(CockpitSB103.class, "normZN", 0.9F);
    }

}
