package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitLAGG_3RD extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitLAGG_3RD.this.fm != null) {
                CockpitLAGG_3RD.this.setTmp = CockpitLAGG_3RD.this.setOld;
                CockpitLAGG_3RD.this.setOld = CockpitLAGG_3RD.this.setNew;
                CockpitLAGG_3RD.this.setNew = CockpitLAGG_3RD.this.setTmp;
                CockpitLAGG_3RD.this.setNew.throttle = ((10F * CockpitLAGG_3RD.this.setOld.throttle) + CockpitLAGG_3RD.this.fm.CT.PowerControl) / 11F;
                CockpitLAGG_3RD.this.setNew.prop = ((10F * CockpitLAGG_3RD.this.setOld.prop) + CockpitLAGG_3RD.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitLAGG_3RD.this.setNew.altimeter = CockpitLAGG_3RD.this.fm.getAltitude();
                if (CockpitLAGG_3RD.this.useRealisticNavigationInstruments()) {
                    CockpitLAGG_3RD.this.setNew.waypointAzimuth.setDeg(CockpitLAGG_3RD.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitLAGG_3RD.this.getBeaconDirection());
                } else {
                    CockpitLAGG_3RD.this.setNew.waypointAzimuth.setDeg(CockpitLAGG_3RD.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitLAGG_3RD.this.waypointAzimuth() - CockpitLAGG_3RD.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                }
                if (Math.abs(CockpitLAGG_3RD.this.fm.Or.getKren()) < 30F) {
                    CockpitLAGG_3RD.this.setNew.azimuth.setDeg(CockpitLAGG_3RD.this.setOld.azimuth.getDeg(1.0F), CockpitLAGG_3RD.this.fm.Or.azimut());
                }
                CockpitLAGG_3RD.this.setNew.vspeed = ((199F * CockpitLAGG_3RD.this.setOld.vspeed) + CockpitLAGG_3RD.this.fm.getVertSpeed()) / 200F;
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

    public CockpitLAGG_3RD() {
        super("3do/cockpit/LaGG-3RD/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "prib_one", "prib_two", "prib_three", "prib_four", "prib_five", "shkala", "prib_one_dd", "prib_two_dd", "prib_three_dd", "Gauges_03", "Gauges_04", "Gauges_04_D", "Gauges_05" });
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
        this.mesh.chunkSetAngles("zGas1T", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 1000F, 0.0F, 73F), 0.0F);
        this.mesh.chunkSetAngles("zGasPres1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.6F * this.fm.EI.engines[0].getPowerOutput(), 0.0F, 1.0F, 0.0F, 64F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.005F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 272.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 80F * this.fm.EI.engines[0].getPowerOutput() * this.fm.EI.engines[0].getReadyness(), 0.0F, 160F, 0.0F, 272.5F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), CockpitLAGG_3RD.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("richag", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("Tross_L", 0.0F, this.fm.CT.getRudder() * 15.65F, 0.0F);
        this.mesh.chunkSetAngles("Tross_R", 0.0F, this.fm.CT.getRudder() * 15.65F, 0.0F);
        this.mesh.chunkSetAngles("n_corr", 0.0F, 80F - (this.interp(this.setNew.throttle, this.setOld.throttle, f) * 95F), 0.0F);
        this.mesh.chunkSetAngles("r_one", 0.0F, -20F * (this.fm.CT.WeaponControl[0] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("r_two", 0.0F, -20F * (this.fm.CT.WeaponControl[1] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("r_turn", 0.0F, 20F * this.fm.CT.BrakeControl, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.cvt(this.fm.M.fuel / 0.725F, 0.0F, 300F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitLAGG_3RD.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
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
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0)) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

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
    private static final float rpmScale[]         = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };

}
