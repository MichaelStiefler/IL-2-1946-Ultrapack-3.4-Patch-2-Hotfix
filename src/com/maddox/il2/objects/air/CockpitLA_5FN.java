package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Time;

public class CockpitLA_5FN extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitLA_5FN.this.fm != null) {
                CockpitLA_5FN.this.setTmp = CockpitLA_5FN.this.setOld;
                CockpitLA_5FN.this.setOld = CockpitLA_5FN.this.setNew;
                CockpitLA_5FN.this.setNew = CockpitLA_5FN.this.setTmp;
                CockpitLA_5FN.this.setNew.throttle = ((10F * CockpitLA_5FN.this.setOld.throttle) + CockpitLA_5FN.this.fm.CT.PowerControl) / 11F;
                CockpitLA_5FN.this.setNew.prop = ((8F * CockpitLA_5FN.this.setOld.prop) + CockpitLA_5FN.this.fm.CT.getStepControl()) / 9F;
                CockpitLA_5FN.this.setNew.altimeter = CockpitLA_5FN.this.fm.getAltitude();
                if (CockpitLA_5FN.this.useRealisticNavigationInstruments()) {
                    CockpitLA_5FN.this.setNew.waypointAzimuth.setDeg(CockpitLA_5FN.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitLA_5FN.this.getBeaconDirection());
                } else {
                    CockpitLA_5FN.this.setNew.waypointAzimuth.setDeg(CockpitLA_5FN.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitLA_5FN.this.waypointAzimuth() - CockpitLA_5FN.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                }
                if (Math.abs(CockpitLA_5FN.this.fm.Or.getKren()) < 30F) {
                    CockpitLA_5FN.this.setNew.azimuth.setDeg(CockpitLA_5FN.this.setOld.azimuth.getDeg(1.0F), CockpitLA_5FN.this.fm.Or.azimut());
                }
                CockpitLA_5FN.this.setNew.vspeed = ((199F * CockpitLA_5FN.this.setOld.vspeed) + CockpitLA_5FN.this.fm.getVertSpeed()) / 200F;
                if (CockpitLA_5FN.this.fm.getAltitude() > 3000F) {
                    float f = (float) Math.sin(1.0F * CockpitLA_5FN.this.cvt(CockpitLA_5FN.this.fm.getOverload(), 1.0F, 8F, 1.0F, 0.45F) * CockpitLA_5FN.this.cvt(CockpitLA_5FN.this.fm.AS.astatePilotStates[0], 0.0F, 100F, 1.0F, 0.1F) * (0.001F * Time.current()));
                    if (f > 0.0F) {
                        CockpitLA_5FN.this.pictBlinker += 0.3F;
                        if (CockpitLA_5FN.this.pictBlinker > 1.0F) {
                            CockpitLA_5FN.this.pictBlinker = 1.0F;
                        }
                    } else {
                        CockpitLA_5FN.this.pictBlinker -= 0.3F;
                        if (CockpitLA_5FN.this.pictBlinker < 0.0F) {
                            CockpitLA_5FN.this.pictBlinker = 0.0F;
                        }
                    }
                }
                CockpitLA_5FN.this.pictStage = (0.8F * CockpitLA_5FN.this.pictStage) + (0.1F * CockpitLA_5FN.this.fm.EI.engines[0].getControlCompressor());
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitLA_5FN() {
        super("3DO/Cockpit/La-5FN/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.t1 = 0L;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictBlinker = 0.0F;
        this.pictStage = 0.0F;
        this.cockpitNightMats = (new String[] { "Prib_One", "Prib_Two", "Prib_Three", "Prib_Four", "Prib_Five", "Shkala128" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.materialReplace("prib_three", "EmptyGauge");
            this.mesh.materialReplace("prib_three_night", "EmptyGauge_night");
            this.mesh.chunkVisible("zRPK10", false);
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm.CT.getStepControlAuto()) {
            this.mesh.chunkSetAngles("PropPitchHandle", -70F + (70F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("PropPitchHandle", -70F + (70F * this.interp(this.setNew.prop, this.setOld.prop, f)), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("TQHandle", -54.54546F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("MixtureHandle", 50F * this.fm.EI.engines[0].getControlMix(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ChargerHandle", this.fm.EI.engines[0].getStage() >= 4 ? 70F - (60F * this.pictStage) : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CStick5FN", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("PriTrigger", this.fm.CT.WeaponControl[1] ? -30F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SecTrigger", this.fm.CT.WeaponControl[0] ? -30F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("WheelBrake", 20F * this.fm.CT.BrakeControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ped_Base", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Tross_L", 0.0F, this.fm.CT.getRudder() * 15.65F, 0.0F);
        this.mesh.chunkSetAngles("Tross_R", 0.0F, this.fm.CT.getRudder() * 15.65F, 0.0F);
        this.mesh.chunkSetAngles("IgnitionSwitch", 0.0F, -40F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("SW_LandLight", this.fm.AS.bLandingLightOn ? 60F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_Radio", 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_NavLight", this.fm.AS.bNavLightsOn ? 60F : 0.0F, 0.0F, 0.0F);
        this.resetYPRmodifier();
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            this.mesh.chunkSetAngles("GearHandle", -45F, 0.0F, 0.0F);
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            this.mesh.chunkSetAngles("GearHandle", 45F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                this.mesh.chunkSetAngles("FlapHandle", 30F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("FlapHandle", -30F, 0.0F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zAlt1a", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, 40F, -40F));
        this.mesh.chunkSetAngles("zAzimuth1b", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zManifold1a", this.floatindex(this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.399966F, 2.133152F, 3F, 16F), CockpitLA_5FN.manifoldScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGas1a", this.cvt(this.fm.M.fuel / 0.725F, 0.0F, 300F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitLA_5FN.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCylHead", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, 70F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurn1a", this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", this.cvt(this.getBall(8D), -8F, 8F, -25F, 25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTOilOut1a", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F), 0.0F, 8F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 6F), CockpitLA_5FN.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1b", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zClock1a", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zClock1b", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zRPK10", this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, -35F, 35F), 0.0F, 0.0F);
        }
        this.mesh.chunkVisible("XGearUP_L", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XGearUP_R", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XGearDown_L", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XGearDown_R", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XGearDown_C", this.fm.CT.getGear() == 1.0F);
        if (this.t1 < Time.current()) {
            BulletEmitter bulletemitter = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            if (bulletemitter != GunEmpty.get()) {
                this.mesh.chunkVisible("XBombOnboard_L", bulletemitter.haveBullets());
            } else {
                this.mesh.chunkVisible("XBombOnboard_L", false);
            }
            bulletemitter = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb02");
            if (bulletemitter != GunEmpty.get()) {
                this.mesh.chunkVisible("XBombOnboard_R", bulletemitter.haveBullets());
            } else {
                this.mesh.chunkVisible("XBombOnboard_R", false);
            }
            this.t1 = Time.current() + 500L;
        }
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.materialReplace("Prib_One", "DPrib_One");
            this.mesh.materialReplace("Prib_One_night", "DPrib_One_night");
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zRPM1b", false);
            this.mesh.chunkVisible("zTOilOut1a", false);
            this.mesh.chunkVisible("zOilPrs1a", false);
            this.mesh.chunkVisible("zGasPrs1a", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.materialReplace("Prib_Two", "DPrib_Two");
            this.mesh.materialReplace("Prib_Two_night", "DPrib_Two_night");
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
            this.mesh.chunkVisible("zGas1a", false);
            this.mesh.chunkVisible("zTurn1a", false);
            this.mesh.chunkVisible("zSlide1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            if (this.useRealisticNavigationInstruments()) {
                this.mesh.materialReplace("Prib_Three", "EmptyGaugeD_night");
                this.mesh.materialReplace("Prib_Three_night", "EmptyGaugeD_night");
            } else {
                this.mesh.materialReplace("Prib_Three", "DPrib_Three");
                this.mesh.materialReplace("Prib_Three_night", "DPrib_Three_night");
            }
            this.mesh.chunkVisible("zHorizon1a", false);
            this.mesh.chunkVisible("zHorizon1b", false);
            this.mesh.materialReplace("Prib_Four", "DPrib_Four");
            this.mesh.materialReplace("Prib_Four_night", "DPrib_Four_night");
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0)) {
            this.mesh.chunkVisible("PBP-1", false);
            this.mesh.chunkVisible("PBP-1_D0", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Z_Holes1_D0", true);
            this.mesh.chunkVisible("Z_Holes2_D0", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D0", true);
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
    private long               t1;
    private float              pictAiler;
    private float              pictElev;
    private float              pictBlinker;
    private float              pictStage;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };
    private static final float manifoldScale[]    = { 0.0F, 0.0F, 0.0F, 0.0F, 26F, 52F, 79F, 106F, 132F, 160F, 185F, 208F, 235F, 260F, 286F, 311F, 336F };
    private static final float variometerScale[]  = { -180F, -90F, -45F, 0.0F, 45F, 90F, 180F };

}
