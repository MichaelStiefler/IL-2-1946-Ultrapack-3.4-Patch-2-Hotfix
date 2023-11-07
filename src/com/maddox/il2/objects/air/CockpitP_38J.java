package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_38J extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_38J.this.bNeedSetUp) {
                CockpitP_38J.this.reflectPlaneMats();
                CockpitP_38J.this.bNeedSetUp = false;
            }
            if (((P_38) CockpitP_38J.this.aircraft()).bChangedPit) {
                CockpitP_38J.this.reflectPlaneToModel();
                ((P_38) CockpitP_38J.this.aircraft()).bChangedPit = false;
            }
            if (CockpitP_38J.this.fm != null) {
                CockpitP_38J.this.setTmp = CockpitP_38J.this.setOld;
                CockpitP_38J.this.setOld = CockpitP_38J.this.setNew;
                CockpitP_38J.this.setNew = CockpitP_38J.this.setTmp;
                CockpitP_38J.this.setNew.trim = (0.92F * CockpitP_38J.this.setOld.trim) + (0.08F * CockpitP_38J.this.fm.CT.getTrimElevatorControl());
                CockpitP_38J.this.setNew.throttle1 = (0.85F * CockpitP_38J.this.setOld.throttle1) + (CockpitP_38J.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitP_38J.this.setNew.throttle2 = (0.85F * CockpitP_38J.this.setOld.throttle2) + (CockpitP_38J.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitP_38J.this.setNew.prop1 = (0.85F * CockpitP_38J.this.setOld.prop1) + (CockpitP_38J.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitP_38J.this.setNew.prop2 = (0.85F * CockpitP_38J.this.setOld.prop2) + (CockpitP_38J.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitP_38J.this.setNew.mix1 = (0.85F * CockpitP_38J.this.setOld.mix1) + (CockpitP_38J.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitP_38J.this.setNew.mix2 = (0.85F * CockpitP_38J.this.setOld.mix2) + (CockpitP_38J.this.fm.EI.engines[1].getControlMix() * 0.15F);
                CockpitP_38J.this.setNew.altimeter = CockpitP_38J.this.fm.getAltitude();
                float f = CockpitP_38J.this.waypointAzimuth();
                if (Math.abs(CockpitP_38J.this.fm.Or.getKren()) < 30F) {
                    CockpitP_38J.this.setNew.azimuth.setDeg(CockpitP_38J.this.setOld.azimuth.getDeg(1.0F), CockpitP_38J.this.fm.Or.azimut());
                }
                CockpitP_38J.this.setNew.waypointAzimuth.setDeg(CockpitP_38J.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                if (CockpitP_38J.this.useRealisticNavigationInstruments()) {
                    CockpitP_38J.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitP_38J.this.setOld.waypointAzimuth.setDeg(f - 90F);
                } else {
                    CockpitP_38J.this.setNew.waypointAzimuth.setDeg(CockpitP_38J.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                }
                CockpitP_38J.this.setNew.vspeed = ((199F * CockpitP_38J.this.setOld.vspeed) + CockpitP_38J.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      mix1;
        float      mix2;
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      trim;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitP_38J() {
        super("3DO/Cockpit/P-38J/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "gauges1", "gauges1_dam", "gauges2", "gauges2_dam", "gauges3", "gauges3_dam", "gauges4", "swbox", "swbox2" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("BMR_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("BMR_D0", this.aircraft().hierMesh().isChunkVisible("Blister1_D0"));
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((P_38) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((P_38) this.aircraft()).bChangedPit = false;
        }
        this.mesh.chunkSetAngles("Z_Trim1", -1722F * this.setNew.trim, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 60F * (this.pictFlap = (0.85F * this.pictFlap) + (0.15F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 90F * (this.pictGear = (0.85F * this.pictGear) + (0.15F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 93.1F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 93.1F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 95F * this.setNew.prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 95F * this.setNew.prop2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 90F * this.setNew.mix1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 90F * this.setNew.mix2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, 16F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, 0.0F, 16F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -16F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 0.0F, -16F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 70F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = 0.01065F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = 0.01065F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitP_38J.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        float f1 = 0.0F;
        if (this.fm.AS.bLandingLightOn) {
            f1 -= 35F;
        }
        if (this.fm.AS.bNavLightsOn) {
            f1 -= 5F;
        }
        if (this.cockpitLightControl) {
            f1 -= 2.87F;
        }
        this.mesh.chunkSetAngles("Z_Amper1", this.cvt(f1 + this.cvt(this.fm.EI.engines[0].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -11.5F, 81.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Amper2", this.cvt(f1 + this.cvt(this.fm.EI.engines[1].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -11.5F, 81.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair1", this.cvt((Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F) + (25F * this.fm.EI.engines[0].getPowerOutput()), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair2", this.cvt((Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F) + (25F * this.fm.EI.engines[1].getPowerOutput()), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitP_38J.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant2", this.cvt(this.fm.EI.engines[1].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 131.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 131.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.031F, -0.031F);
        this.mesh.chunkSetLocate("Z_TurnBank2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 0.0F, 245.2F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", this.cvt(this.fm.M.fuel, 0.0F, 245.2F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", this.cvt(this.fm.M.fuel, 245.2F, 490.4F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", this.cvt(this.fm.M.fuel, 245.2F, 490.4F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(7D), -7F, 7F, -16F, 16F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.w.z, -0.23562F, 0.23562F, 29.5F, -29.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 73F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold2", this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 28F, 0.0F, 164.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 28F, 0.0F, 164.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, 164F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.4F, 0.0F, 164F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearGreen1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 1) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 8) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0) {

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

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 18.5F, 62F, 107F, 152.5F, 198.5F, 238.5F, 252F, 265F, 278.5F, 292F, 305.5F, 319F, 331.5F, 343F };
    private static final float variometerScale[]  = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };

}
