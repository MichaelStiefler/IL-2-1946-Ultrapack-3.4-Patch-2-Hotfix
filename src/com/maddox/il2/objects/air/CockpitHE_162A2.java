package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitHE_162A2 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHE_162A2.this.bNeedSetUp) {
                CockpitHE_162A2.this.reflectPlaneMats();
                CockpitHE_162A2.this.bNeedSetUp = false;
            }
            CockpitHE_162A2.this.setTmp = CockpitHE_162A2.this.setOld;
            CockpitHE_162A2.this.setOld = CockpitHE_162A2.this.setNew;
            CockpitHE_162A2.this.setNew = CockpitHE_162A2.this.setTmp;
            CockpitHE_162A2.this.setNew.vspeed = ((299F * CockpitHE_162A2.this.setOld.vspeed) + CockpitHE_162A2.this.fm.getVertSpeed()) / 300F;
            CockpitHE_162A2.this.setNew.altimeter = CockpitHE_162A2.this.fm.getAltitude();
            if (CockpitHE_162A2.this.cockpitDimControl) {
                if (CockpitHE_162A2.this.setNew.dimPosition > 0.0F) {
                    CockpitHE_162A2.this.setNew.dimPosition = CockpitHE_162A2.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitHE_162A2.this.setNew.dimPosition < 1.0F) {
                CockpitHE_162A2.this.setNew.dimPosition = CockpitHE_162A2.this.setOld.dimPosition + 0.05F;
            }
            CockpitHE_162A2.this.setNew.throttle = ((10F * CockpitHE_162A2.this.setOld.throttle) + CockpitHE_162A2.this.fm.CT.PowerControl) / 11F;
            CockpitHE_162A2.this.setNew.mix = ((8F * CockpitHE_162A2.this.setOld.mix) + CockpitHE_162A2.this.fm.EI.engines[0].getControlMix()) / 9F;
            if (Math.abs(CockpitHE_162A2.this.fm.Or.getKren()) < 30F) {
                CockpitHE_162A2.this.setNew.azimuth.setDeg(CockpitHE_162A2.this.setOld.azimuth.getDeg(1.0F), CockpitHE_162A2.this.fm.Or.azimut());
            }
            CockpitHE_162A2.this.setNew.beaconDirection = ((10F * CockpitHE_162A2.this.setOld.beaconDirection) + CockpitHE_162A2.this.getBeaconDirection()) / 11F;
            CockpitHE_162A2.this.setNew.beaconRange = ((10F * CockpitHE_162A2.this.setOld.beaconRange) + CockpitHE_162A2.this.getBeaconRange()) / 11F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        AnglesFork azimuth;
        float      beaconDirection;
        float      beaconRange;
        float      mix;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitHE_162A2() {
        super("3DO/Cockpit/He-162A-2/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.tmpL = new Loc();
        this.bNeedSetUp = true;
        this.setNew.dimPosition = 1.0F;
        this.cockpitNightMats = (new String[] { "gauges1", "gauges2", "gauges3", "gauges4", "gauges5", "gauges6", "gauges1_d1", "gauges2_d1", "gauges3_d1", "gauges4_d1", "gauges5_d1", "gauges6_d1", "turnbank", "aiguill1", "Oxi_Press" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -this.interp(this.setNew.throttle, this.setOld.throttle, f) * 58F);
        this.mesh.chunkSetAngles("sunOFF", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -44.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Fuel_Tank", 0.0F, 0.0F, this.fm.EI.engines[0].getStage() != 0 ? 0.0F : 38F);
        this.mesh.chunkSetAngles("Landing_Gear", 0.0F, 0.0F, -24F + (24F * this.fm.CT.GearControl));
        this.mesh.chunkSetAngles("ElevatorCrank", 3600F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("PedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("PedalR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 10F), CockpitHE_162A2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 0.0F, 1000F, 0.0F, 10F), CockpitHE_162A2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), CockpitHE_162A2.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelContent", this.cvt(0.6F * this.fm.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, 0.0F, 272F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 300F, 1000F, 0.0F, 83F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPressure", this.cvt(1.0F + (0.005F * this.fm.EI.engines[0].tOilOut), 0.0F, 3.2F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPressure", this.cvt(this.fm.M.fuel, 0.0F, 500F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.setNew.vspeed < 0.0F ? -this.floatindex(this.cvt(-this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitHE_162A2.vsiNeedleScale) : this.floatindex(this.cvt(this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitHE_162A2.vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_Slide1a", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -13.5F, 13.5F), 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -6F, 6F, 24F, -24F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("Z_Azimuth1", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil1", this.cvt(this.fm.Or.getTangage(), -30F, 30F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil3", this.cvt(this.fm.Or.getKren(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil2", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.aircraft().hierMesh().setCurChunk("GearC1_D0");
        this.aircraft().hierMesh().getChunkLocObj(this.tmpL);
        this.mesh.chunkSetAngles("GearC1_D0", 0.0F, this.tmpL.getOrient().getTangage(), 0.0F);
        this.mesh.chunkSetAngles("GearC2_D0", 0.0F, 120F * this.fm.CT.getGear(), 0.0F);
        f1 = Math.max(-this.fm.CT.getGear() * 1500F, -110F);
        this.mesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
        this.resetYPRmodifier();
        if (this.aircraft().FM.CT.getGear() > 0.99F) {
            Cockpit.xyz[1] = this.cvt(this.aircraft().FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 0.0632F);
            Cockpit.ypr[1] = 40F * this.aircraft().FM.CT.getRudder();
            this.mesh.chunkSetLocate("GearC25_D0", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetAngles("GearC27_D0", 0.0F, this.cvt(this.aircraft().FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, -15F), 0.0F);
            this.mesh.chunkSetAngles("GearC28_D0", 0.0F, this.cvt(this.aircraft().FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 30F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("GearC25_D0", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("GearC27_D0", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("GearC28_D0", 0.0F, 0.0F, 0.0F);
        }
        if (this.fm.CT.Weapons[0] != null) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.Weapons[0][0].countBullets(), 0.0F, 120F, 0.0F, 0.0415F);
            this.mesh.chunkSetLocate("Zammo_counter1", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[1] = this.cvt(this.fm.CT.Weapons[0][1].countBullets(), 0.0F, 120F, 0.0F, 0.0415F);
            this.mesh.chunkSetLocate("Zammo_counter2", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkSetAngles("Z_AFN1", this.cvt(this.setNew.beaconDirection, -45F, 45F, -20F, 20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AFN2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, 20F, -20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("AFN2_RED", this.isOnBlindLandingMarker());
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

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_GasTemp", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Second1", false);
            this.mesh.chunkVisible("Z_OilPressure", false);
            this.mesh.chunkVisible("Z_FuelContent", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("z_Slide1a", false);
            this.mesh.chunkVisible("Z_Azimuth1", false);
            this.mesh.chunkVisible("Z_FuelPressure", false);
            this.mesh.chunkVisible("Z_RPM", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0) {

        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private Loc                tmpL;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { -1F, 3.5F, 48.5F, 97.5F, 144F, 198F, 253F, 305F, 358F, 407F, 419.5F };
    private static final float vsiNeedleScale[]   = { 0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F };
    private static final float rpmScale[]         = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };

}
