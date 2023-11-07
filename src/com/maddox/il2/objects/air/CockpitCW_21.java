package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitCW_21 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      throttle;
        float      mix;
        float      prop;
        float      turn;
        float      vspeed;
        float      stbyPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCW_21.this.bNeedSetUp) {
                CockpitCW_21.this.reflectPlaneMats();
                CockpitCW_21.this.bNeedSetUp = false;
            }
            if (CW_21.bChangedPit) {
                CockpitCW_21.this.reflectPlaneToModel();
                CW_21.bChangedPit = false;
            }
            CockpitCW_21.this.setTmp = CockpitCW_21.this.setOld;
            CockpitCW_21.this.setOld = CockpitCW_21.this.setNew;
            CockpitCW_21.this.setNew = CockpitCW_21.this.setTmp;
            if (((CockpitCW_21.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitCW_21.this.setNew.stbyPosition < 1.0F)) {
                CockpitCW_21.this.setNew.stbyPosition = CockpitCW_21.this.setOld.stbyPosition + 0.0125F;
                CockpitCW_21.this.setOld.stbyPosition = CockpitCW_21.this.setNew.stbyPosition;
            }
            CockpitCW_21.this.setNew.altimeter = CockpitCW_21.this.fm.getAltitude();
            if (Math.abs(CockpitCW_21.this.fm.Or.getKren()) < 30F) {
                CockpitCW_21.this.setNew.azimuth.setDeg(CockpitCW_21.this.setOld.azimuth.getDeg(1.0F), CockpitCW_21.this.fm.Or.azimut());
            }
            CockpitCW_21.this.setNew.throttle = ((10F * CockpitCW_21.this.setOld.throttle) + CockpitCW_21.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitCW_21.this.setNew.mix = ((10F * CockpitCW_21.this.setOld.mix) + CockpitCW_21.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitCW_21.this.setNew.prop = ((8F * CockpitCW_21.this.setOld.prop) + CockpitCW_21.this.fm.CT.getStepControl()) / 9F;
            CockpitCW_21.this.w.set(CockpitCW_21.this.fm.getW());
            CockpitCW_21.this.fm.Or.transform(CockpitCW_21.this.w);
            CockpitCW_21.this.setNew.turn = ((12F * CockpitCW_21.this.setOld.turn) + CockpitCW_21.this.w.z) / 13F;
            CockpitCW_21.this.setNew.vspeed = ((299F * CockpitCW_21.this.setOld.vspeed) + CockpitCW_21.this.fm.getVertSpeed()) / 300F;
            CockpitCW_21.this.pictSupc = (0.8F * CockpitCW_21.this.pictSupc) + (0.2F * CockpitCW_21.this.fm.EI.engines[0].getControlCompressor());
            float f = 12F;
            if ((CockpitCW_21.this.flapsLever != 0.0F) && (CockpitCW_21.this.flaps == CockpitCW_21.this.fm.CT.getFlap())) {
                CockpitCW_21.this.flapsLever = CockpitCW_21.this.flapsLever * 0.8F;
                if (Math.abs(CockpitCW_21.this.flapsLever) < 0.1F) {
                    CockpitCW_21.this.flapsLever = 0.0F;
                }
            } else if (CockpitCW_21.this.flaps < CockpitCW_21.this.fm.CT.getFlap()) {
                CockpitCW_21.this.flaps = CockpitCW_21.this.fm.CT.getFlap();
                CockpitCW_21.this.flapsLever = CockpitCW_21.this.flapsLever + 2.0F;
                if (CockpitCW_21.this.flapsLever > f) {
                    CockpitCW_21.this.flapsLever = f;
                }
            } else if (CockpitCW_21.this.flaps > CockpitCW_21.this.fm.CT.getFlap()) {
                CockpitCW_21.this.flaps = CockpitCW_21.this.fm.CT.getFlap();
                CockpitCW_21.this.flapsLever = CockpitCW_21.this.flapsLever - 2.0F;
                if (CockpitCW_21.this.flapsLever < -f) {
                    CockpitCW_21.this.flapsLever = -f;
                }
            }
            if ((CockpitCW_21.this.gearsLever != 0.0F) && (CockpitCW_21.this.gears == CockpitCW_21.this.fm.CT.getGear())) {
                CockpitCW_21.this.gearsLever = CockpitCW_21.this.gearsLever * 0.8F;
                if (Math.abs(CockpitCW_21.this.gearsLever) < 0.1F) {
                    CockpitCW_21.this.gearsLever = 0.0F;
                }
            } else if (CockpitCW_21.this.gears < CockpitCW_21.this.fm.CT.getGear()) {
                CockpitCW_21.this.gears = CockpitCW_21.this.fm.CT.getGear();
                CockpitCW_21.this.gearsLever = CockpitCW_21.this.gearsLever + 2.0F;
                if (CockpitCW_21.this.gearsLever > f) {
                    CockpitCW_21.this.gearsLever = f;
                }
            } else if (CockpitCW_21.this.gears > CockpitCW_21.this.fm.CT.getGear()) {
                CockpitCW_21.this.gears = CockpitCW_21.this.fm.CT.getGear();
                CockpitCW_21.this.gearsLever = CockpitCW_21.this.gearsLever - 2.0F;
                if (CockpitCW_21.this.gearsLever < -f) {
                    CockpitCW_21.this.gearsLever = -f;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitCW_21() {
        super("3DO/Cockpit/CW-21/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.rpmGeneratedPressure = 0.0F;
        this.oilPressure = 0.0F;
        this.flapsLever = 0.0F;
        this.flaps = 0.0F;
        this.gearsLever = 0.0F;
        this.gears = 0.0F;
        this.warningLightsOK = true;
        this.cockpitNightMats = (new String[] { "CLOCKS1", "CLOCK2", "CLOCK3", "CLOCK5", "Compass", "STUFF4", "STUFF5" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (CW_21.bChangedPit) {
            this.reflectPlaneToModel();
            CW_21.bChangedPit = false;
        }
        this.mesh.chunkSetAngles("Z_NeedManifoldPres", this.cvt(this.pictManifold = (0.85F * this.pictManifold) + (0.15F * this.fm.EI.engines[0].getManifoldPressure() * 76F), 30F, 120F, 15F, 285F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick", -14F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, -14F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)));
        this.mesh.chunkSetAngles("Z_PedalR1", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalL1", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        float f1 = this.fm.CT.getBrake() * 45F;
        this.mesh.chunkSetAngles("Z_PedalR2", (15F * this.fm.CT.getRudder()) + f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalL2", (-15F * this.fm.CT.getRudder()) + f1, 0.0F, 0.0F);
        f1 = 70F * this.interp(this.setNew.throttle, this.setOld.throttle, f);
        this.mesh.chunkSetAngles("Z_LevelThrottle", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RodThrottle", -f1, 0.0F, 0.0F);
        f1 = 70F * this.interp(this.setNew.mix, this.setOld.mix, f);
        this.mesh.chunkSetAngles("Z_LevelMixture", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RodMixture", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeverRPM", 70F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeverSuperc", this.pictSupc * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RodSuperc", -this.pictSupc * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedAlt", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 720F), 0.0F, 0.0F);
        float f2 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
        if (f2 < 150F) {
            this.mesh.chunkSetAngles("Z_NeedSpeed", this.cvt(f2, 0.0F, 150F, 0.0F, 90F), 0.0F, 0.0F);
        } else if (f2 < 300F) {
            this.mesh.chunkSetAngles("Z_NeedSpeed", this.cvt(f2, 150F, 300F, 90F, 198F), 0.0F, 0.0F);
        } else if (f2 < 400F) {
            this.mesh.chunkSetAngles("Z_NeedSpeed", this.cvt(f2, 300F, 400F, 198F, 270F), 0.0F, 0.0F);
        } else if (f2 < 550F) {
            this.mesh.chunkSetAngles("Z_NeedSpeed", this.cvt(f2, 400F, 550F, 270F, 378F), 0.0F, 0.0F);
        } else if (f2 < 700F) {
            this.mesh.chunkSetAngles("Z_NeedSpeed", this.cvt(f2, 550F, 700F, 378F, 489F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_NeedSpeed", this.cvt(f2, 700F, 900F, 489F, 630F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_NeedCompass", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_NeedRPM", this.cvt(f1, 500F, 3500F, 0.0F, 540F), 0.0F, 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - 0.5F;
        } else if (f1 < this.rpmGeneratedPressure) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - ((this.rpmGeneratedPressure - f1) * 0.01F);
        } else {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure + ((f1 - this.rpmGeneratedPressure) * 0.001F);
        }
        if (this.rpmGeneratedPressure < 800F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure < 1800F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 800F, 1800F, 4F, 5F);
        } else {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 1800F, 2750F, 5F, 5.8F);
        }
        float f3 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f3 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f3 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f3 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f4 = f3 * this.fm.EI.engines[0].getReadyness() * this.oilPressure;
        this.mesh.chunkSetAngles("Z_NeedOilPres", this.cvt(f4, 0.0F, 15F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedClockHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedClockMinute", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedFuelPres", -this.cvt(this.rpmGeneratedPressure, 0.0F, 2000F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedHydrPres", this.cvt(this.rpmGeneratedPressure, 0.0F, 4200F, 0.0F, 270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedOilTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedCylTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 110F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedTurn", -this.cvt(this.setNew.turn, -0.2F, 0.2F, -45F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NeedBall", -this.cvt(this.getBall(8D), -8F, 8F, 10F, -10F), 0.0F, 0.0F);
        if (Math.abs(this.setNew.vspeed) <= 5F) {
            this.mesh.chunkSetAngles("Z_NeedVSpeed", this.cvt(this.setNew.vspeed, -5F, 5F, -90F, 90F), 0.0F, 0.0F);
        } else if (this.setNew.vspeed > 5F) {
            this.mesh.chunkSetAngles("Z_NeedVSpeed", this.cvt(this.setNew.vspeed, 5F, 20F, 90F, 180F), 0.0F, 0.0F);
        } else if (this.setNew.vspeed < -5F) {
            this.mesh.chunkSetAngles("Z_NeedVSpeed", this.cvt(this.setNew.vspeed, -20F, -5F, -180F, -90F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_NeedFuel", this.cvt(this.fm.M.fuel, 0.0F, 280F, 0.0F, 54F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ignition", 20F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.xyz[0] = this.fm.CT.getCockpitDoor() * -0.49F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        this.mesh.chunkSetLocate("Canopy", Aircraft.xyz, Aircraft.ypr);
        if (this.fm.AS.bLandingLightOn) {
            this.mesh.chunkSetAngles("Z_Switch3", 45F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Switch3", 0.0F, 0.0F, 0.0F);
        }
        if (this.fm.AS.bNavLightsOn) {
            this.mesh.chunkSetAngles("Z_Switch2", 45F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Switch2", 0.0F, 0.0F, 0.0F);
        }
        if (this.cockpitLightControl) {
            this.mesh.chunkSetAngles("Z_Switch7", 45F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Switch7", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_LeverFlaps", -this.flapsLever, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeverGear", this.gearsLever, 0.0F, 0.0F);
        if (this.warningLightsOK) {
            this.mesh.chunkVisible("WarningLight1", this.rpmGeneratedPressure < 700F);
            this.mesh.chunkVisible("WarningLight2", (f4 > 11F) || (f4 < 3F));
            this.mesh.chunkVisible("WarningLight3", this.fm.M.fuel < 0.1F);
            this.mesh.chunkVisible("WarningLight4", this.setNew.mix < 0.1D);
            this.mesh.chunkVisible("WarningLight5", this.setNew.prop < 0.1F);
            this.mesh.chunkVisible("WarningLight6", this.pictSupc > 0.1F);
            this.mesh.chunkVisible("WarningLight7", this.fm.CT.getFlap() > 0.01F);
            this.mesh.chunkVisible("WarningLight8", this.fm.CT.getGear() == 0.0F);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("CanopyGlassDamage", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Glass_damage", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Gauges1_D0", false);
            this.mesh.chunkVisible("Gauges1_D1", true);
            this.mesh.chunkVisible("Z_NeedCylTemp", false);
            this.mesh.chunkVisible("Z_NeedSpeed", false);
            this.mesh.chunkVisible("Z_NeedBall", false);
            this.mesh.chunkVisible("Z_NeedTurn", false);
            if (World.Rnd().nextFloat() < 0.15D) {
                this.warningLightsOK = false;
                this.mesh.chunkVisible("WarningLights", false);
                this.mesh.chunkVisible("WarningLight1", false);
                this.mesh.chunkVisible("WarningLight2", false);
                this.mesh.chunkVisible("WarningLight3", false);
                this.mesh.chunkVisible("WarningLight4", false);
                this.mesh.chunkVisible("WarningLight5", false);
                this.mesh.chunkVisible("WarningLight6", false);
                this.mesh.chunkVisible("WarningLight7", false);
                this.mesh.chunkVisible("WarningLight8", false);
            }
        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("GlassOil", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Gauges2_D0", false);
            this.mesh.chunkVisible("Gauges2_D1", true);
            this.mesh.chunkVisible("Z_NeedVSpeed", false);
            this.mesh.chunkVisible("Z_NeedRPM", false);
            this.mesh.chunkVisible("Z_NeedAlt", false);
            if (World.Rnd().nextFloat() < 0.15D) {
                this.warningLightsOK = false;
                this.mesh.chunkVisible("WarningLights", false);
                this.mesh.chunkVisible("WarningLight1", false);
                this.mesh.chunkVisible("WarningLight2", false);
                this.mesh.chunkVisible("WarningLight3", false);
                this.mesh.chunkVisible("WarningLight4", false);
                this.mesh.chunkVisible("WarningLight5", false);
                this.mesh.chunkVisible("WarningLight6", false);
                this.mesh.chunkVisible("WarningLight7", false);
                this.mesh.chunkVisible("WarningLight8", false);
            }
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private Vector3f  w;
    private boolean   bNeedSetUp;
    private float     pictAiler;
    private float     pictElev;
    private float     pictSupc;
    private float     pictManifold;
    private float     rpmGeneratedPressure;
    private float     oilPressure;
    private float     flapsLever;
    private float     flaps;
    private float     gearsLever;
    private float     gears;
    private boolean   warningLightsOK;

    static {
        Property.set(CockpitCW_21.class, "normZN", 0.8F);
        Property.set(CockpitCW_21.class, "gsZN", 0.8F);
    }

}
