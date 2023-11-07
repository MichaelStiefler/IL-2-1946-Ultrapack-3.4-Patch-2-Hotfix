package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitGO_229 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitGO_229.this.setTmp = CockpitGO_229.this.setOld;
            CockpitGO_229.this.setOld = CockpitGO_229.this.setNew;
            CockpitGO_229.this.setNew = CockpitGO_229.this.setTmp;
            CockpitGO_229.this.setNew.altimeter = CockpitGO_229.this.fm.getAltitude();
            CockpitGO_229.this.setNew.throttlel = ((10F * CockpitGO_229.this.setOld.throttlel) + CockpitGO_229.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitGO_229.this.setNew.throttler = ((10F * CockpitGO_229.this.setOld.throttler) + CockpitGO_229.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            float f = CockpitGO_229.this.waypointAzimuth();
            if (CockpitGO_229.this.useRealisticNavigationInstruments()) {
                CockpitGO_229.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitGO_229.this.setOld.waypointAzimuth.setDeg(f - 90F);
            } else {
                CockpitGO_229.this.setNew.waypointAzimuth.setDeg(CockpitGO_229.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitGO_229.this.setOld.azimuth.getDeg(1.0F));
            }
            if (Math.abs(CockpitGO_229.this.fm.Or.getKren()) < 30F) {
                CockpitGO_229.this.setNew.azimuth.setDeg(CockpitGO_229.this.setOld.azimuth.getDeg(1.0F), CockpitGO_229.this.fm.Or.azimut());
            }
            CockpitGO_229.this.setNew.vspeed = ((299F * CockpitGO_229.this.setOld.vspeed) + CockpitGO_229.this.fm.getVertSpeed()) / 300F;
            if (CockpitGO_229.this.cockpitDimControl) {
                if (CockpitGO_229.this.setNew.dimPosition > 0.0F) {
                    CockpitGO_229.this.setNew.dimPosition = CockpitGO_229.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitGO_229.this.setNew.dimPosition < 1.0F) {
                CockpitGO_229.this.setNew.dimPosition = CockpitGO_229.this.setOld.dimPosition + 0.05F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitGO_229() {
        super("3DO/Cockpit/Go-229/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank" });
        this.setNightMats(false);
        this.setNew.dimPosition = 1.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.fm.isTick(44, 0)) {
            this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_GearRGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearCGreen1", this.fm.CT.getGear() == 1.0F);
            this.mesh.chunkVisible("Z_GearLRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
            this.mesh.chunkVisible("Z_GearCRed1", this.fm.CT.getGear() == 0.0F);
            this.mesh.chunkVisible("Z_FuelLampL", this.fm.M.fuel < 300F);
            this.mesh.chunkVisible("Z_FuelLampR", this.fm.M.fuel < 300F);
            this.mesh.chunkVisible("Z_Fire", false);
        }
        this.mesh.chunkVisible("Z_FlapEin", this.fm.CT.getFlap() < 0.05F);
        this.mesh.chunkVisible("Z_FlapStart", (this.fm.CT.getFlap() > 0.28F) && (this.fm.CT.getFlap() < 0.38F));
        this.mesh.chunkVisible("Z_FlapAus", this.fm.CT.getFlap() > 0.95F);
        this.mesh.chunkSetAngles("zColumn1", 0.0F, 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("zColumn2", 0.0F, -10F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut", 10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -10F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -32F + (42.5F * this.fm.CT.FlapsControl));
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, 0.0F, 20.5F - (32F * this.interp(this.setNew.throttlel, this.setOld.throttlel, f)));
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, 0.0F, 20.5F - (32F * this.interp(this.setNew.throttler, this.setOld.throttler, f)));
        this.mesh.chunkSetAngles("zGear1", 0.0F, 0.0F, -35.5F + (35.5F * this.fm.CT.GearControl));
        this.mesh.chunkSetAngles("zAirBrake1", 0.0F, 0.0F, 32F * this.fm.CT.AirBrakeControl);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.fm.Or.getTangage(), 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -this.cvt(this.getBall(6D), -6F, 6F, -7.5F, 7.5F));
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, -50F, 50F));
        this.mesh.chunkSetAngles("zSpeed1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 100F, 400F, 2.0F, 8F), CockpitGO_229.speedometerIndScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), CockpitGO_229.speedometerTruScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 16000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("zRepeater", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zCompass", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("zCompass", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zRepeater", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zRPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), CockpitGO_229.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), CockpitGO_229.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), CockpitGO_229.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), CockpitGO_229.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zExtT", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 273.09F, 373.09F, -26F, 144.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasTempL", this.cvt(this.fm.EI.engines[0].tWaterOut, 300F, 1000F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasTempR", this.cvt(this.fm.EI.engines[1].tWaterOut, 300F, 1000F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPressureL", this.cvt(1.0F + (0.005F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPressureR", this.cvt(1.0F + (0.005F * this.fm.EI.engines[1].tOilOut), 0.0F, 10F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPressure", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 80F * this.fm.EI.engines[0].getPowerOutput() * this.fm.EI.engines[0].getReadyness(), 0.0F, 160F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -20F, 50F, 0.0F, 14F), CockpitGO_229.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("Speedometer1", false);
            this.mesh.chunkVisible("Speedometer1_D1", true);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Speedometer2", false);
            this.mesh.chunkVisible("RPML", false);
            this.mesh.chunkVisible("RPML_D1", true);
            this.mesh.chunkVisible("Z_RPML", false);
            this.mesh.chunkVisible("FuelRemainV", false);
            this.mesh.chunkVisible("FuelRemainV_D1", true);
            this.mesh.chunkVisible("Z_FuelRemainV", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage4", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("Altimeter1", false);
            this.mesh.chunkVisible("Altimeter1_D1", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("GasPressureL", false);
            this.mesh.chunkVisible("GasPressureL_D1", true);
            this.mesh.chunkVisible("Z_GasPressureL", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("RPMR", false);
            this.mesh.chunkVisible("RPMR_D1", true);
            this.mesh.chunkVisible("Z_RPMR", false);
            this.mesh.chunkVisible("FuelPressR", false);
            this.mesh.chunkVisible("FuelPressR_D1", true);
            this.mesh.chunkVisible("Z_FuelPressR", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("GasPressureR", false);
            this.mesh.chunkVisible("GasPressureR_D1", true);
            this.mesh.chunkVisible("Z_GasPressureR", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("Climb", false);
            this.mesh.chunkVisible("Climb_D1", true);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("FuelPressR", false);
            this.mesh.chunkVisible("FuelPressR_D1", true);
            this.mesh.chunkVisible("Z_FuelPressR", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("HullDamage2", true);
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("Z_ReViTint", false);
            this.mesh.chunkVisible("Revi_D1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("FuelPressL", false);
            this.mesh.chunkVisible("FuelPressL_D1", true);
            this.mesh.chunkVisible("Z_FuelPressL", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("Altimeter1", false);
            this.mesh.chunkVisible("Altimeter1_D1", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Climb", false);
            this.mesh.chunkVisible("Climb_D1", true);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("AFN", false);
            this.mesh.chunkVisible("AFN_D1", true);
            this.mesh.chunkVisible("Z_AFN1", false);
            this.mesh.chunkVisible("Z_AFN2", false);
            this.mesh.chunkVisible("FuelPressL", false);
            this.mesh.chunkVisible("FuelPressL_D1", true);
            this.mesh.chunkVisible("Z_FuelPressL", false);
            this.mesh.chunkVisible("FuelRemainIn", false);
            this.mesh.chunkVisible("FuelRemainIn_D1", true);
            this.mesh.chunkVisible("Z_FuelRemainIn", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerIndScale[] = { 0.0F, 0.0F, 0.0F, 17F, 35.5F, 57.5F, 76F, 95F, 112F };
    private static final float speedometerTruScale[] = { 0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 336F };
    private static final float variometerScale[]     = { 0.0F, 13.5F, 27F, 43.5F, 90F, 142.5F, 157F, 170.5F, 184F, 201.5F, 214.5F, 226F, 239.5F, 253F, 266F };
    private static final float rpmScale[]            = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };
    private static final float fuelScale[]           = { 0.0F, 11F, 31F, 57F, 84F, 103.5F };

}
