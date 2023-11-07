package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitLAGG_3SERIES66 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitLAGG_3SERIES66.this.fm != null) {
                CockpitLAGG_3SERIES66.this.setTmp = CockpitLAGG_3SERIES66.this.setOld;
                CockpitLAGG_3SERIES66.this.setOld = CockpitLAGG_3SERIES66.this.setNew;
                CockpitLAGG_3SERIES66.this.setNew = CockpitLAGG_3SERIES66.this.setTmp;
                CockpitLAGG_3SERIES66.this.setNew.throttle = ((10F * CockpitLAGG_3SERIES66.this.setOld.throttle) + CockpitLAGG_3SERIES66.this.fm.CT.PowerControl) / 11F;
                CockpitLAGG_3SERIES66.this.setNew.prop = ((10F * CockpitLAGG_3SERIES66.this.setOld.prop) + CockpitLAGG_3SERIES66.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitLAGG_3SERIES66.this.setNew.altimeter = CockpitLAGG_3SERIES66.this.fm.getAltitude();
                if (CockpitLAGG_3SERIES66.this.useRealisticNavigationInstruments()) {
                    CockpitLAGG_3SERIES66.this.setNew.waypointAzimuth.setDeg(CockpitLAGG_3SERIES66.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitLAGG_3SERIES66.this.getBeaconDirection());
                } else {
                    CockpitLAGG_3SERIES66.this.setNew.waypointAzimuth.setDeg(CockpitLAGG_3SERIES66.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitLAGG_3SERIES66.this.waypointAzimuth() - CockpitLAGG_3SERIES66.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                }
                if (Math.abs(CockpitLAGG_3SERIES66.this.fm.Or.getKren()) < 30F) {
                    CockpitLAGG_3SERIES66.this.setNew.azimuth.setDeg(CockpitLAGG_3SERIES66.this.setOld.azimuth.getDeg(1.0F), CockpitLAGG_3SERIES66.this.fm.Or.azimut());
                }
                CockpitLAGG_3SERIES66.this.setNew.vspeed = ((199F * CockpitLAGG_3SERIES66.this.setOld.vspeed) + CockpitLAGG_3SERIES66.this.fm.getVertSpeed()) / 200F;
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

    public CockpitLAGG_3SERIES66() {
        super("3do/cockpit/LaGG-3series66/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "prib_one", "prib_two", "prib_three", "prib_four", "prib_five", "shkala", "prib_one_dd", "prib_two_dd", "prib_three_dd" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.materialReplace("prib_three", "EmptyGauge");
            this.mesh.materialReplace("prib_three_dd", "EmptyGaugeD");
            this.mesh.materialReplace("prib_three_night", "EmptyGauge_night");
            this.mesh.materialReplace("prib_three_dd_night", "EmptyGaugeD_night");
            this.mesh.chunkVisible("zRPK10", false);
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("richag", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("Tross_L", 0.0F, this.fm.CT.getRudder() * 15.65F, 0.0F);
        this.mesh.chunkSetAngles("Tross_R", 0.0F, this.fm.CT.getRudder() * 15.65F, 0.0F);
        this.mesh.chunkSetAngles("n_corr", 0.0F, 80F - (this.interp(this.setNew.throttle, this.setOld.throttle, f) * 95F), 0.0F);
        this.mesh.chunkSetAngles("VISH", 0.0F, (this.interp(this.setNew.prop, this.setOld.prop, f) * 90F) - 45F, 0.0F);
        this.mesh.chunkSetAngles("r_one", 0.0F, -20F * (this.fm.CT.WeaponControl[0] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("r_two", 0.0F, -20F * (this.fm.CT.WeaponControl[1] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("r_turn", 0.0F, 20F * this.fm.CT.BrakeControl, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.cvt(this.fm.M.fuel / 0.725F, 0.0F, 300F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitLAGG_3SERIES66.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
            this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 180F), 0.0F);
            this.mesh.chunkSetAngles("zGasPrs1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3050F, 0.0F, 4F), 0.0F, 8F, 0.0F, -180F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1b", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 120F, 0.0F, 86F), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zRPK10", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, -35F, 35F), 0.0F);
        this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Red3", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.M.fuel < 36F);
        this.mesh.chunkVisible("Z_Red2", (this.fm.EI.engines[0].tOilOut > 115F) || (this.fm.EI.engines[0].tOilOut < 40F));
        this.mesh.chunkVisible("Z_Red5", (this.fm.EI.engines[0].tWaterOut < 60F) || (this.fm.EI.engines[0].tWaterOut > 110F) || (this.fm.EI.engines[0].tOilIn < 40F));
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("prib_D1", false);
            this.mesh.chunkVisible("prib_DD1", true);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("radio", false);
            this.mesh.chunkVisible("radio_d1", true);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.chunkVisible("prib_D2", false);
            this.mesh.chunkVisible("prib_DD2", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zRPM1b", false);
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0)) {
            this.mesh.chunkVisible("pricel", false);
            this.mesh.chunkVisible("pricel_D1", true);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };

}
