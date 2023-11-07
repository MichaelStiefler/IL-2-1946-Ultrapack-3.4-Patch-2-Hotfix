package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitJ8M3 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitJ8M3.this.bNeedSetUp) {
                CockpitJ8M3.this.reflectPlaneMats();
                CockpitJ8M3.this.bNeedSetUp = false;
            }
            if (CockpitJ8M3.this.fm != null) {
                CockpitJ8M3.this.setTmp = CockpitJ8M3.this.setOld;
                CockpitJ8M3.this.setOld = CockpitJ8M3.this.setNew;
                CockpitJ8M3.this.setNew = CockpitJ8M3.this.setTmp;
                if (Math.abs(CockpitJ8M3.this.fm.Or.getKren()) < 30F) {
                    CockpitJ8M3.this.setNew.azimuth.setDeg(CockpitJ8M3.this.setOld.azimuth.getDeg(1.0F), CockpitJ8M3.this.fm.Or.azimut());
                }
                CockpitJ8M3.this.setNew.vspeed = ((98F * CockpitJ8M3.this.setOld.vspeed) + CockpitJ8M3.this.fm.getVertSpeed()) / 99F;
                CockpitJ8M3.this.setNew.altimeter = CockpitJ8M3.this.fm.getAltitude();
                CockpitJ8M3.this.setNew.fuel = CockpitJ8M3.this.fm.M.fuel;
                if (CockpitJ8M3.this.cockpitDimControl) {
                    if (CockpitJ8M3.this.setNew.dimPosition > 0.0F) {
                        CockpitJ8M3.this.setNew.dimPosition = CockpitJ8M3.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitJ8M3.this.setNew.dimPosition < 1.0F) {
                    CockpitJ8M3.this.setNew.dimPosition = CockpitJ8M3.this.setOld.dimPosition + 0.05F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        float      fuel;
        float      dimPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitJ8M3() {
        super("3DO/Cockpit/J8M/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictThtl = 0.0F;
        this.pictFlap = 0.0F;
        this.pictTurbo = 0.0F;
        this.pictTout = 0.0F;
        this.pictCons = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank", "oxyregul", "oxygaug", "pompeco" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.setNew.dimPosition = 1.0F;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearLRed1", (this.fm.CT.getGear() < 0.05F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearCGreen1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearCRed1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("Z_GunLamp01", !this.aircraft().getGunByHookName("_CANNON01").haveBullets());
        this.mesh.chunkVisible("Z_GunLamp02", !this.aircraft().getGunByHookName("_CANNON02").haveBullets());
        this.mesh.chunkVisible("Z_FuelLampV", this.fm.M.fuel < (0.25F * this.fm.M.maxFuel));
        this.mesh.chunkSetAngles("zCompassOil1", this.cvt(this.fm.Or.getTangage(), -45F, 45F, -45F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil2", 0.0F, 0.0F, this.cvt(this.fm.Or.getKren(), -45F, 45F, 45F, -45F));
        this.mesh.chunkSetAngles("zCompassOil3", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        float f1;
        if ((this.fm.CT.getGear() - this.fm.CT.GearControl) > 0.02F) {
            f1 = 30F;
        } else if ((this.fm.CT.getGear() - this.fm.CT.GearControl) < -0.02F) {
            f1 = -30F;
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("Z_Gear", f1, 0.0F, 0.0F);
        if (!((J8M3) this.aircraft()).bCartAttached) {
            this.mesh.chunkSetAngles("Z_Gearskid", 0.0F, 90F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 18F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 18F);
        this.resetYPRmodifier();
        if (this.fm.CT.WeaponControl[1]) {
            Cockpit.xyz[2] = -0.0025F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_PedalStrutL", -25F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrutR", -25F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -25F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -25F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", -30F + (60F * (this.pictThtl = (0.72F * this.pictThtl) + (0.21F * this.fm.CT.PowerControl))), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle1", -30F + (60F * this.pictThtl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trimlever", -6200F * this.fm.CT.trimElevator, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.01825F * this.fm.CT.trimElevator;
        this.mesh.chunkSetLocate("Z_Trimposit", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Flaplever", 150F * (this.pictFlap = (0.6F * this.pictFlap) + (0.4F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneleverL", 30F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneleverR", -30F * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneL", (this.pictAiler >= 0.0F ? 35F : 25F) * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneR", (this.pictAiler >= 0.0F ? -25F : -35F) * this.pictAiler, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt(this.aircraft().getGunByHookName("_CANNON01").countBullets(), 0.0F, 100F, 0.0F, -7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt(this.aircraft().getGunByHookName("_CANNON02").countBullets(), 0.0F, 100F, 0.0F, -7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.fm.Or.getTangage(), 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -this.cvt(this.getBall(6D), -6F, 6F, -7.5F, 7.5F));
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, -50F, 50F));
        if (this.setNew.vspeed < 0.0F) {
            f1 = this.cvt(this.setNew.vspeed, -25F, 0.0F, -45F, 0.0F);
        } else if (this.setNew.vspeed < 100F) {
            f1 = this.cvt(this.setNew.vspeed, 0.0F, 100F, 0.0F, 180F);
        } else if (this.setNew.vspeed < 125F) {
            f1 = this.cvt(this.setNew.vspeed, 100F, 125F, 180F, 223F);
        } else {
            f1 = this.cvt(this.setNew.vspeed, 125F, 150F, 223F, 258F);
        }
        this.mesh.chunkSetAngles("Z_Climb1", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 100F, 400F, 2.0F, 8F), CockpitJ8M3.speedometerIndScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), CockpitJ8M3.speedometerTruScale), 0.0F, 0.0F);
        if (this.fm.EI.engines[0].getReadyness() > 0.0F) {
            f1 = 6022F;
            if (this.fm.EI.engines[0].getPowerOutput() > 0.0F) {
                f1 = 8200F;
            }
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("RPM", this.floatindex(this.cvt(this.pictTurbo = (0.92F * this.pictTurbo) + (0.08F * f1), 2000F, 14000F, 2.0F, 14F), CockpitJ8M3.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.pictTurbo, 0.0F, 10000F, 0.0F, 270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress2", this.cvt(this.pictTout = (0.92F * this.pictTout) + (0.08F * this.fm.EI.engines[0].getPowerOutput()), 0.0F, 1.1F, 0.0F, 270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 16000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAltCtr2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        f1 = ((this.setOld.fuel - this.setNew.fuel) / Time.tickLenFs()) * 60F;
        this.mesh.chunkSetAngles("Z_Fuelconsum1", this.cvt(this.pictCons = (0.75F * this.pictCons) + (0.25F * f1), 0.0F, 20F, 0.0F, 282F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 1000F, 0.0F, 85F), 0.0F, 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("HullDamage4", true);
        }
        this.retoggleLight();
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
    private float              pictAiler;
    private float              pictElev;
    private float              pictThtl;
    private float              pictFlap;
    private float              pictTurbo;
    private float              pictTout;
    private float              pictCons;
    private boolean            bNeedSetUp;
    private static final float speedometerIndScale[] = { 0.0F, 0.0F, 0.0F, 17F, 35.5F, 57.5F, 76F, 95F, 112F };
    private static final float speedometerTruScale[] = { 0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 336F };
    private static final float rpmScale[]            = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };

}
