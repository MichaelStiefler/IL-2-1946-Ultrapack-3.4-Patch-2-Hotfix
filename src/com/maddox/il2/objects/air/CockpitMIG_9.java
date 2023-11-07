package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitMIG_9 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMIG_9.this.fm != null) {
                CockpitMIG_9.this.setTmp = CockpitMIG_9.this.setOld;
                CockpitMIG_9.this.setOld = CockpitMIG_9.this.setNew;
                CockpitMIG_9.this.setNew = CockpitMIG_9.this.setTmp;
                CockpitMIG_9.this.setNew.throttle1 = (0.85F * CockpitMIG_9.this.setOld.throttle1) + (CockpitMIG_9.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitMIG_9.this.setNew.throttle2 = (0.85F * CockpitMIG_9.this.setOld.throttle2) + (CockpitMIG_9.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitMIG_9.this.setNew.starter1 = (0.94F * CockpitMIG_9.this.setOld.starter1) + (0.06F * ((CockpitMIG_9.this.fm.EI.engines[0].getStage() <= 0) || (CockpitMIG_9.this.fm.EI.engines[0].getStage() >= 6) ? 0.0F : 1.0F));
                CockpitMIG_9.this.setNew.starter2 = (0.94F * CockpitMIG_9.this.setOld.starter2) + (0.06F * ((CockpitMIG_9.this.fm.EI.engines[1].getStage() <= 0) || (CockpitMIG_9.this.fm.EI.engines[1].getStage() >= 6) ? 0.0F : 1.0F));
                CockpitMIG_9.this.setNew.altimeter = CockpitMIG_9.this.fm.getAltitude();
                if (CockpitMIG_9.this.useRealisticNavigationInstruments()) {
                    CockpitMIG_9.this.setNew.waypointAzimuth.setDeg(CockpitMIG_9.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitMIG_9.this.getBeaconDirection());
                    float f = CockpitMIG_9.this.waypointAzimuth();
                    CockpitMIG_9.this.setNew.compassRim.setDeg(f - 90F);
                    CockpitMIG_9.this.setOld.compassRim.setDeg(f - 90F);
                } else {
                    CockpitMIG_9.this.setNew.waypointAzimuth.setDeg(CockpitMIG_9.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitMIG_9.this.waypointAzimuth() - CockpitMIG_9.this.setOld.azimuth.getDeg(1.0F));
                    CockpitMIG_9.this.setNew.compassRim.setDeg(0.0F);
                    CockpitMIG_9.this.setOld.compassRim.setDeg(0.0F);
                }
                if (Math.abs(CockpitMIG_9.this.fm.Or.getKren()) < 30F) {
                    CockpitMIG_9.this.setNew.azimuth.setDeg(CockpitMIG_9.this.setOld.azimuth.getDeg(1.0F), CockpitMIG_9.this.fm.Or.azimut());
                }
                CockpitMIG_9.this.setNew.vspeed = ((199F * CockpitMIG_9.this.setOld.vspeed) + CockpitMIG_9.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      starter1;
        float      starter2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork compassRim;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.compassRim = new AnglesFork();
        }

    }

    public CockpitMIG_9() {
        super("3DO/Cockpit/MiG-9/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictETP = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictTLck = 0.0F;
        this.pictMet1 = 0.0F;
        this.pictMet2 = 0.0F;
        this.pictETrm = 0.0F;
        this.cockpitNightMats = (new String[] { "gauges_01", "gauges_02", "gauges_03", "gauges_04", "gauges_05", "Dgauges_01", "Dgauges_02", "Dgauges_03", "Dgauges_05" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (MIG_9.bChangedPit) {
            this.reflectPlaneToModel();
            MIG_9.bChangedPit = false;
        }
        this.resetYPRmodifier();
        float f1 = this.fm.CT.getCockpitDoor();
        if (f1 < 0.1F) {
            Cockpit.xyz[1] = this.cvt(f1, 0.01F, 0.08F, 0.0F, 0.1F);
        } else {
            Cockpit.xyz[1] = this.cvt(f1, 0.17F, 0.99F, 0.1F, 0.6F);
        }
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("CnOpenLvr", this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.08F, 0.0F, -94F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 50F * (this.pictGear = (0.82F * this.pictGear) + (0.18F * this.fm.CT.GearControl)));
        this.mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, 111F * (this.pictFlap = (0.88F * this.pictFlap) + (0.12F * this.fm.CT.FlapsControl)));
        this.mesh.chunkSetAngles("TQHandle1", 0.0F, 0.0F, -40.909F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f));
        this.mesh.chunkSetAngles("TQHandle2", 0.0F, 0.0F, -40.909F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f));
        this.mesh.chunkSetAngles("NossleLvr1", 0.0F, 0.0F, -40.909F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f));
        this.mesh.chunkSetAngles("NossleLvr2", 0.0F, 0.0F, -40.909F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f));
        this.mesh.chunkSetAngles("Lvr1", 0.0F, 0.0F, -25F * (this.pictTLck = (0.85F * this.pictTLck) + (0.15F * (this.fm.Gears.bTailwheelLocked ? 1.0F : 0.0F))));
        if (this.fm.CT.getTrimElevatorControl() != this.pictETP) {
            if ((this.fm.CT.getTrimElevatorControl() - this.pictETP) > 0.0F) {
                this.mesh.chunkSetAngles("ElevTrim", 0.0F, -30F, 0.0F);
                this.pictETrm = Time.current();
            } else {
                this.mesh.chunkSetAngles("ElevTrim", 0.0F, 30F, 0.0F);
                this.pictETrm = Time.current();
            }
            this.pictETP = this.fm.CT.getTrimElevatorControl();
        } else if (Time.current() > (this.pictETrm + 500F)) {
            this.mesh.chunkSetAngles("ElevTrim", 0.0F, 0.0F, 0.0F);
            this.pictETrm = Time.current() + 0x7a120L;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.035F, 0.035F);
        this.mesh.chunkSetLocate("Pedal_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Pedal_R", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("FLCSA", 0.0F, 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("FLCSB", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("NeedRPM1", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 14F), CockpitMIG_9.rpmScale), 0.0F);
        this.pictMet1 = (0.96F * this.pictMet1) + (0.04F * (0.6F * this.fm.EI.engines[0].getThrustOutput() * this.fm.EI.engines[0].getControlThrottle() * (this.fm.EI.engines[0].getStage() != 6 ? 0.02F : 1.0F)));
        this.mesh.chunkSetAngles("NeedExhstPress1", 0.0F, this.cvt(this.pictMet1, 0.0F, 1.0F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 284F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedExstT1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 1200F, 0.0F, 112F), 0.0F);
        this.mesh.chunkSetAngles("NeedOilP1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 6.46F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("NeedRPM2", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 14F), CockpitMIG_9.rpmScale), 0.0F);
        this.pictMet2 = (0.96F * this.pictMet2) + (0.04F * (0.6F * this.fm.EI.engines[1].getThrustOutput() * this.fm.EI.engines[1].getControlThrottle() * (this.fm.EI.engines[1].getStage() != 6 ? 0.02F : 1.0F)));
        this.mesh.chunkSetAngles("NeedExhstPress2", 0.0F, this.cvt(this.pictMet2, 0.0F, 1.0F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 284F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedExstT2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 1200F, 0.0F, 112F), 0.0F);
        this.mesh.chunkSetAngles("NeedOilP2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 6.46F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("NeedFuel1", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 864F, 0.0F, 4F), CockpitMIG_9.fuelScale), 0.0F);
        this.mesh.chunkSetAngles("NeedFuel2", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 864F, 1728F, 0.0F, 4F), CockpitMIG_9.fuelScale), 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 60000F, 0.0F, 2160F), 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_M", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 60000F, 0.0F, 21600F), 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("NeedCompassB", 0.0F, this.setNew.azimuth.getDeg(f) - this.setNew.compassRim.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("NeedCompassA", 0.0F, -this.setNew.compassRim.getDeg(f), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedCompassA", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("NeedCompassB", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedSpeed", 0.0F, this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1200F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("NeedClimb", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("NeedAHCyl", 0.0F, -this.fm.Or.getKren() + 180F, 0.0F);
        this.mesh.chunkSetAngles("NeedAHBar", 0.0F, 0.0F, -this.fm.Or.getTangage());
        this.mesh.chunkSetAngles("NeedTurn", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("NeedDF", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -90F, 90F, -16.5F, 16.5F), 0.0F);
        this.mesh.chunkSetAngles("NeedHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("NeedMin", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("NeedStarter1", this.cvt(this.setNew.starter1, 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedStarter2", this.cvt(this.setNew.starter2, 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedEmrgAirP", -63.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedAirSysP", this.fm.Gears.isHydroOperable() ? -133.5F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkVisible("FlareGearUp_R", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearUp_L", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearUp_C", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("FlareGearDn_R", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearDn_L", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearDn_C", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("FlareFuel", this.fm.M.fuel < 296.1F);
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("DamageGlass2", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gages1_D0", false);
            this.mesh.chunkVisible("Gages1_D1", true);
            this.mesh.chunkVisible("NeedSpeed", false);
            this.mesh.chunkVisible("NeedClimb", false);
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("NeedAlt_M", false);
            this.mesh.chunkVisible("NeedDF", false);
            this.mesh.chunkVisible("NeedCompassA", false);
            this.mesh.chunkVisible("NeedCompassB", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Gages3_D0", false);
            this.mesh.chunkVisible("Gages3_D1", true);
            this.mesh.chunkVisible("NeedHour", false);
            this.mesh.chunkVisible("NeedMin", false);
            this.mesh.chunkVisible("NeedRPM1", false);
            this.mesh.chunkVisible("NeedExhstPress1", false);
            this.mesh.chunkVisible("DamageHull3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Gages4_D0", false);
            this.mesh.chunkVisible("Gages4_D1", true);
            this.mesh.chunkVisible("NeedRPM2", false);
            this.mesh.chunkVisible("NeedOilP1", false);
            this.mesh.chunkVisible("NeedFuel1", false);
            this.mesh.chunkVisible("NeedExstT1", false);
            this.mesh.chunkVisible("DamageHull2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Gages5_D0", false);
            this.mesh.chunkVisible("Gages5_D1", true);
            this.mesh.chunkVisible("NeedOilP2", false);
            this.mesh.chunkVisible("NeedExhstPress2", false);
            this.mesh.chunkVisible("NeedExstT2", false);
            this.mesh.chunkVisible("NeedFuel2", false);
            this.mesh.chunkVisible("", false);
            this.mesh.chunkVisible("", false);
            this.mesh.chunkVisible("", false);
            this.mesh.chunkVisible("", false);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("Wire_D0", hiermesh.isChunkVisible("Wire_D0"));
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

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictETP;
    private float              pictFlap;
    private float              pictGear;
    private float              pictTLck;
    private float              pictMet1;
    private float              pictMet2;
    private float              pictETrm;
    private static final float rpmScale[]  = { 0.0F, 8F, 23.5F, 40F, 58.5F, 81F, 104.5F, 130.2F, 158.5F, 187F, 217.5F, 251.1F, 281.5F, 289.5F, 295.5F };
    private static final float fuelScale[] = { 0.0F, 18.5F, 49F, 80F, 87F };

}
