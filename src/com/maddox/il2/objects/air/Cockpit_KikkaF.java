package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class Cockpit_KikkaF extends CockpitPilot {
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

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            Cockpit_KikkaF.this.setTmp = Cockpit_KikkaF.this.setOld;
            Cockpit_KikkaF.this.setOld = Cockpit_KikkaF.this.setNew;
            Cockpit_KikkaF.this.setNew = Cockpit_KikkaF.this.setTmp;
            Cockpit_KikkaF.this.setNew.altimeter = Cockpit_KikkaF.this.fm.getAltitude();
            Cockpit_KikkaF.this.setNew.throttlel = ((10F * Cockpit_KikkaF.this.setOld.throttlel) + Cockpit_KikkaF.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            Cockpit_KikkaF.this.setNew.throttler = ((10F * Cockpit_KikkaF.this.setOld.throttler) + Cockpit_KikkaF.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            Cockpit_KikkaF.this.setNew.waypointAzimuth.setDeg(Cockpit_KikkaF.this.setOld.waypointAzimuth.getDeg(0.1F), (Cockpit_KikkaF.this.waypointAzimuth() - Cockpit_KikkaF.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
            if (Math.abs(Cockpit_KikkaF.this.fm.Or.getKren()) < 30F) {
                Cockpit_KikkaF.this.setNew.azimuth.setDeg(Cockpit_KikkaF.this.setOld.azimuth.getDeg(1.0F), Cockpit_KikkaF.this.fm.Or.azimut());
            }
            Cockpit_KikkaF.this.setNew.vspeed = ((299F * Cockpit_KikkaF.this.setOld.vspeed) + Cockpit_KikkaF.this.fm.getVertSpeed()) / 300F;
            if (Cockpit_KikkaF.this.cockpitDimControl) {
                if (Cockpit_KikkaF.this.setNew.dimPosition > 0.0F) {
                    Cockpit_KikkaF.this.setNew.dimPosition = Cockpit_KikkaF.this.setOld.dimPosition - 0.05F;
                }
            } else if (Cockpit_KikkaF.this.setNew.dimPosition < 1.0F) {
                Cockpit_KikkaF.this.setNew.dimPosition = Cockpit_KikkaF.this.setOld.dimPosition + 0.05F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    public Cockpit_KikkaF() {
        super("3DO/Cockpit/Kikka-TP/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bU4 = false;
        this.cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank" });
        this.setNightMats(false);
        this.setNew.dimPosition = 1.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
            this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
        }
        if (this.fm.isTick(44, 0)) {
            this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_GearRGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearCGreen1", this.fm.CT.getGear() == 1.0F);
            this.mesh.chunkVisible("Z_GearLRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
            this.mesh.chunkVisible("Z_GearRRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
            this.mesh.chunkVisible("Z_GearCRed1", this.fm.CT.getGear() == 0.0F);
            if (!this.bU4) {
                this.mesh.chunkVisible("Z_GunLamp01", !this.gun[0].haveBullets());
                this.mesh.chunkVisible("Z_GunLamp02", !this.gun[1].haveBullets());
                this.mesh.chunkVisible("Z_GunLamp03", !this.gun[2].haveBullets());
                this.mesh.chunkVisible("Z_GunLamp04", !this.gun[3].haveBullets());
            }
            this.mesh.chunkVisible("Z_MachLamp", (this.fm.getSpeed() / Atmosphere.sonicSpeed((float) this.fm.Loc.z)) > 0.8F);
            this.mesh.chunkVisible("Z_CabinLamp", this.fm.Loc.z > 12000D);
            this.mesh.chunkVisible("Z_FuelLampV", this.fm.M.fuel < 300F);
            this.mesh.chunkVisible("Z_FuelLampIn", this.fm.M.fuel < 300F);
        }
        this.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = (this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F) ? -0.0107F : 0.0F;
        this.mesh.chunkSetLocate("Z_GearEin", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = (this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F) ? -0.0107F : 0.0F;
        this.mesh.chunkSetLocate("Z_GearAus", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.fm.CT.FlapsControl < this.fm.CT.getFlap() ? -0.0107F : 0.0F;
        this.mesh.chunkSetLocate("Z_FlapEin", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.fm.CT.FlapsControl > this.fm.CT.getFlap() ? -0.0107F : 0.0F;
        this.mesh.chunkSetLocate("Z_FlapAus", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Column", 10F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = -0.0025F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[2] || this.fm.CT.saveWeaponControl[3]) {
            Cockpit.xyz[2] = -0.00325F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_PedalStrut", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ThrottleL", 0.0F, -75F * this.interp(this.setNew.throttlel, this.setOld.throttlel, f), 0.0F);
        this.mesh.chunkSetAngles("Z_ThrottleR", 0.0F, -75F * this.interp(this.setNew.throttler, this.setOld.throttler, f), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelLeverL", this.fm.EI.engines[0].getControlMagnetos() == 3 ? 6.5F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelLeverR", this.fm.EI.engines[1].getControlMagnetos() == 3 ? 6.5F : 0.0F, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.03675F * this.fm.CT.getTrimElevatorControl();
        this.mesh.chunkSetLocate("Z_TailTrim", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.CT.Weapons[3] != null) && !this.fm.CT.Weapons[3][0].haveBullets()) {
            this.mesh.chunkSetAngles("Z_Bombbutton", 0.0F, 53F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt(this.gun[1].countBullets(), 0.0F, 100F, 0.0F, -7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt(this.gun[2].countBullets(), 0.0F, 100F, 0.0F, -7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 100F, 400F, 2.0F, 8F), Cockpit_KikkaF.speedometerIndScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), Cockpit_KikkaF.speedometerTruScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 16000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.fm.Or.getTangage(), 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -this.cvt(this.getBall(6D), -6F, 6F, -7.5F, 7.5F));
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, -50F, 50F));
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -20F, 50F, 0.0F, 14F), Cockpit_KikkaF.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPML", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), Cockpit_KikkaF.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPMR", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), Cockpit_KikkaF.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasPressureL", this.cvt(this.fm.M.fuel > 1.0F ? 0.6F * this.fm.EI.engines[0].getPowerOutput() : 0.0F, 0.0F, 1.0F, 0.0F, 273.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasPressureR", this.cvt(this.fm.M.fuel > 1.0F ? 0.6F * this.fm.EI.engines[1].getPowerOutput() : 0.0F, 0.0F, 1.0F, 0.0F, 273.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasTempL", this.cvt(this.fm.EI.engines[0].tWaterOut, 300F, 1000F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasTempR", this.cvt(this.fm.EI.engines[1].tWaterOut, 300F, 1000F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPressureL", this.cvt(1.0F + (0.005F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPressureR", this.cvt(1.0F + (0.005F * this.fm.EI.engines[1].tOilOut), 0.0F, 10F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPressL", this.cvt(this.fm.M.fuel > 1.0F ? 80F * this.fm.EI.engines[0].getPowerOutput() * this.fm.EI.engines[0].getReadyness() : 0.0F, 0.0F, 160F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPressR", this.cvt(this.fm.M.fuel > 1.0F ? 80F * this.fm.EI.engines[1].getPowerOutput() * this.fm.EI.engines[1].getReadyness() : 0.0F, 0.0F, 160F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelRemainV", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), Cockpit_KikkaF.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelRemainIn", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), Cockpit_KikkaF.fuelScale), 0.0F, 0.0F);
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
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {

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
    private Gun                gun[]                 = { null, null, null, null };
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bU4;
    private static final float speedometerIndScale[] = { 0.0F, 0.0F, 0.0F, 17F, 35.5F, 57.5F, 76F, 95F, 112F };
    private static final float speedometerTruScale[] = { 0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 336F };
    private static final float variometerScale[]     = { 0.0F, 13.5F, 27F, 43.5F, 90F, 142.5F, 157F, 170.5F, 184F, 201.5F, 214.5F, 226F, 239.5F, 253F, 266F };
    private static final float rpmScale[]            = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };
    private static final float fuelScale[]           = { 0.0F, 11F, 31F, 57F, 84F, 103.5F };

}
