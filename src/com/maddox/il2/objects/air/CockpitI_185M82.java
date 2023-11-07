package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Time;

public class CockpitI_185M82 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitI_185M82.this.fm != null) {
                if (CockpitI_185M82.this.bNeedSetUp) {
                    CockpitI_185M82.this.reflectPlaneMats();
                    CockpitI_185M82.this.bNeedSetUp = false;
                }
                CockpitI_185M82.this.setTmp = CockpitI_185M82.this.setOld;
                CockpitI_185M82.this.setOld = CockpitI_185M82.this.setNew;
                CockpitI_185M82.this.setNew = CockpitI_185M82.this.setTmp;
                CockpitI_185M82.this.setNew.throttle = ((10F * CockpitI_185M82.this.setOld.throttle) + CockpitI_185M82.this.fm.CT.PowerControl) / 11F;
                CockpitI_185M82.this.setNew.prop = (0.85F * CockpitI_185M82.this.setOld.prop) + (CockpitI_185M82.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitI_185M82.this.setNew.altimeter = CockpitI_185M82.this.fm.getAltitude();
                if (CockpitI_185M82.this.useRealisticNavigationInstruments()) {
                    CockpitI_185M82.this.setNew.waypointAzimuth.setDeg(CockpitI_185M82.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitI_185M82.this.getBeaconDirection());
                } else {
                    CockpitI_185M82.this.setNew.waypointAzimuth.setDeg(CockpitI_185M82.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitI_185M82.this.waypointAzimuth() - CockpitI_185M82.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                }
                if (Math.abs(CockpitI_185M82.this.fm.Or.getKren()) < 30F) {
                    CockpitI_185M82.this.setNew.azimuth.setDeg(CockpitI_185M82.this.setOld.azimuth.getDeg(1.0F), CockpitI_185M82.this.fm.Or.azimut());
                }
                CockpitI_185M82.this.setNew.vspeed = ((199F * CockpitI_185M82.this.setOld.vspeed) + CockpitI_185M82.this.fm.getVertSpeed()) / 200F;
                if (CockpitI_185M82.this.fm.getAltitude() > 3000F) {
                    float f = (float) Math.sin(1.0F * CockpitI_185M82.this.cvt(CockpitI_185M82.this.fm.getOverload(), 1.0F, 8F, 1.0F, 0.45F) * CockpitI_185M82.this.cvt(CockpitI_185M82.this.fm.AS.astatePilotStates[0], 0.0F, 100F, 1.0F, 0.1F) * (0.001F * Time.current()));
                    if (f > 0.0F) {
                        CockpitI_185M82.this.pictBlinker += 0.3F;
                        if (CockpitI_185M82.this.pictBlinker > 1.0F) {
                            CockpitI_185M82.this.pictBlinker = 1.0F;
                        }
                    } else {
                        CockpitI_185M82.this.pictBlinker -= 0.3F;
                        if (CockpitI_185M82.this.pictBlinker < 0.0F) {
                            CockpitI_185M82.this.pictBlinker = 0.0F;
                        }
                    }
                }
                CockpitI_185M82.this.pictStage = (0.8F * CockpitI_185M82.this.pictStage) + (0.1F * CockpitI_185M82.this.fm.EI.engines[0].getControlCompressor());
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
        float      xyz[] = { 0.0F, 0.0F, 0.0F };
        float      ypr[] = { 0.0F, 0.0F, 0.0F };

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitI_185M82() {
        super("3DO/Cockpit/I-185M-82/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.t1 = 0L;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictBlinker = 0.0F;
        this.pictStage = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "Prib_One-T", "Prib_Two", "Prib_Three", "Prib_Four", "Prib_Five" });
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
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkVisible("XGearDown_L", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XGearDown_C", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("XGearDown_R", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XGearUP_L", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XGearUP_R", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
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
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                this.mesh.chunkSetAngles("FlapHandle", 5F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("FlapHandle", -5F, 0.0F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, 0.0F);
        }
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            this.mesh.chunkSetAngles("GearHandle", -16F, 0.0F, 0.0F);
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            this.mesh.chunkSetAngles("GearHandle", 16F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("IgnitionSwitch", -23F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        if (this.fm.EI.engines[0].getStage() == 0) {
            this.mesh.chunkSetAngles("IgnitionSwitch", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("TQHandle", 16.3F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("MixtureHandle", -18.3F * this.fm.EI.engines[0].getControlMix(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PropPitchHandle", -21F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ChargerHandle", this.fm.EI.engines[0].getStage() >= 4 ? -22F * this.pictStage : 0.0F, 0.0F, 0.0F);
        if (this.fm.CT.saveWeaponControl[3]) {
            this.mesh.chunkSetAngles("BSLHandle", 22F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("BSLClamp", -11F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("CStick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", -this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", -this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PShad_L", -this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PShad_R", -this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Tross_L", -this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Tross_R", -this.fm.CT.getRudder() * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitI_185M82.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurn1a", this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", -this.cvt(this.getBall(8D), -8F, 8F, 25F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zManifold1a", this.floatindex(this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.399966F, 2.133152F, 3F, 16F), CockpitI_185M82.manifoldScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1b", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTOilOut1a", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F), 0.0F, 8F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGas1a", this.cvt(this.fm.M.fuel / 0.725F, 0.0F, 300F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPK10", this.cvt(this.setNew.waypointAzimuth.getDeg(f), -25F, 25F, 35F, -35F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zClock1a", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zClock1b", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 6F), CockpitI_185M82.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, 40F, -40F));
        this.mesh.chunkSetAngles("zAzimuth1b", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.resetYPRmodifier();
        this.setOld.xyz[1] = this.cvt(this.fm.Or.getTangage(), -40F, 40F, 0.03F, -0.03F);
        this.mesh.chunkSetLocate("zHorizon1a", this.setOld.xyz, this.setOld.ypr);
        this.mesh.chunkSetAngles("zHorizon1b", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCylHead", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 300F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPitchprop", this.cvt(this.fm.EI.engines[0].getPropAoA(), 0.0F, 12F, 0.0F, -225F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.Gears.rgear) {
            Cockpit.xyz[0] = 0.08F * this.fm.CT.getGear();
        }
        this.mesh.chunkSetLocate("zRGear_ind", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.Gears.lgear) {
            Cockpit.xyz[0] = 0.08F * this.fm.CT.getGear();
        }
        this.mesh.chunkSetLocate("zLGear_ind", Cockpit.xyz, Cockpit.ypr);
        float f1 = 25F;
        if (this.fm.AS.bLandingLightOn) {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("SW_LandLight", f1, 0.0F, 0.0F);
        if (this.cockpitLightControl) {
            f1 = 25F;
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("SW_UVLight", f1, 0.0F, 0.0F);
        if (this.fm.AS.bNavLightsOn) {
            f1 = 25F;
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("SW_NavLight", f1, 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };
    private static final float manifoldScale[]    = { 0.0F, 0.0F, 0.0F, 0.0F, 26F, 52F, 79F, 106F, 132F, 160F, 185F, 208F, 235F, 260F, 286F, 311F, 336F };
    private static final float variometerScale[]  = { -180F, -90F, -45F, 0.0F, 45F, 90F, 180F };

}
