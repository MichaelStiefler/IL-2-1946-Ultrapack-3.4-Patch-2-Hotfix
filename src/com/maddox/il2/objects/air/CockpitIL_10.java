package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitIL_10 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitIL_10.this.fm != null) {
                CockpitIL_10.this.setTmp = CockpitIL_10.this.setOld;
                CockpitIL_10.this.setOld = CockpitIL_10.this.setNew;
                CockpitIL_10.this.setNew = CockpitIL_10.this.setTmp;
                CockpitIL_10.this.setNew.throttle = (0.85F * CockpitIL_10.this.setOld.throttle) + (CockpitIL_10.this.fm.CT.PowerControl * 0.15F);
                CockpitIL_10.this.setNew.prop = (0.85F * CockpitIL_10.this.setOld.prop) + (CockpitIL_10.this.fm.CT.getStepControl() * 0.15F);
                CockpitIL_10.this.setNew.altimeter = CockpitIL_10.this.fm.getAltitude();
                if (CockpitIL_10.this.useRealisticNavigationInstruments()) {
                    CockpitIL_10.this.setNew.waypointAzimuth.setDeg(CockpitIL_10.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitIL_10.this.getBeaconDirection());
                    float f = CockpitIL_10.this.waypointAzimuth();
                    CockpitIL_10.this.setNew.compassRim.setDeg(f - 90F);
                    CockpitIL_10.this.setOld.compassRim.setDeg(f - 90F);
                } else {
                    CockpitIL_10.this.setNew.waypointAzimuth.setDeg(CockpitIL_10.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitIL_10.this.waypointAzimuth() - CockpitIL_10.this.setOld.azimuth.getDeg(1.0F));
                    CockpitIL_10.this.setNew.compassRim.setDeg(0.0F);
                    CockpitIL_10.this.setOld.compassRim.setDeg(0.0F);
                }
                if (Math.abs(CockpitIL_10.this.fm.Or.getKren()) < 30F) {
                    CockpitIL_10.this.setNew.azimuth.setDeg(CockpitIL_10.this.setOld.azimuth.getDeg(1.0F), CockpitIL_10.this.fm.Or.azimut());
                }
                CockpitIL_10.this.setNew.vspeed = ((199F * CockpitIL_10.this.setOld.vspeed) + CockpitIL_10.this.fm.getVertSpeed()) / 200F;
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
        AnglesFork waypointAzimuth;
        AnglesFork compassRim;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.compassRim = new AnglesFork();
        }

    }

    public CockpitIL_10() {
        super("3DO/Cockpit/Il-10/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictManf = 1.0F;
        this.pictClap = 0.0F;
        this.pictFuel = 0.0F;
        this.tClap = -1;
        this.cockpitNightMats = (new String[] { "DGM_gauges_03", "DMG_gauges_01", "DMG_gauges_02", "DMG_gauges_04", "DMG_gauges_05", "gauges_01", "gauges_02", "gauges_03", "gauges_04", "gauges_05", "gauges_06" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
            this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
        }
        if (this.bgun[0] == null) {
            this.bgun[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_BombSpawn01");
            this.bgun[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_BombSpawn02");
            this.bgun[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            this.bgun[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb02");
        }
        this.mesh.chunkSetAngles("Ped_Base", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalL_wire", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalR_wire", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ruchkaShassi", 0.0F, 0.0F, 42.5F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)));
        this.mesh.chunkSetAngles("ruchkaShitkov", 0.0F, 0.0F, 42.5F * (this.pictFlap = (0.75F * this.pictFlap) + (0.25F * this.fm.CT.FlapsControl)));
        this.mesh.chunkSetAngles("ruchkaGaza", 0.0F, 50.5F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("ruchkaGaza_wire", 0.0F, -55F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture", 0.0F, -55F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop", 0.0F, 45.8F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop_wire", 0.0F, -52F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("os_V", 0.0F, 0.0F, 8F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("richag", 0.0F, 0.0F, 12F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)));
        this.mesh.chunkSetAngles("tyga_V", 0.0F, -8F * this.pictElev, 0.0F);
        float f1 = 0.0F;
        if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1] || this.fm.CT.saveWeaponControl[2] || this.fm.CT.saveWeaponControl[3]) {
            this.tClap = Time.tickCounter() + World.Rnd().nextInt(190, 260);
        }
        if (Time.tickCounter() < this.tClap) {
            f1 = 1.0F;
        }
        this.mesh.chunkSetAngles("r_one", 0.0F, 0.0F, -145F * (this.pictClap = (0.85F * this.pictClap) + (0.15F * f1)));
        this.mesh.chunkSetAngles("r_two", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.pictGunB[0] = (0.8F * this.pictGunB[0]) + (0.2F * (100F * (int) (0.01F * this.gun[1].countBullets())));
        this.pictGunB[1] = (0.8F * this.pictGunB[1]) + (0.2F * (100F * (int) (0.01F * this.gun[0].countBullets())));
        this.pictGunB[2] = (0.8F * this.pictGunB[2]) + (0.2F * (100F * (int) (0.01F * this.gun[2].countBullets())));
        this.pictGunB[3] = (0.8F * this.pictGunB[3]) + (0.2F * (100F * (int) (0.01F * this.gun[3].countBullets())));
        this.mesh.chunkSetAngles("Z_AmmoCounter1", 0.0F, 0.18F * this.pictGunB[0], 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter3", 0.0F, 0.18F * this.pictGunB[1], 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter5", 0.0F, 0.18F * this.pictGunB[2], 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter7", 0.0F, 0.18F * this.pictGunB[3], 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter2", 0.0F, 3.6F * (this.gun[1].countBullets() - (100F * (int) (0.01F * this.gun[1].countBullets()))), 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter4", 0.0F, 3.6F * (this.gun[0].countBullets() - (100F * (int) (0.01F * this.gun[0].countBullets()))), 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter6", 0.0F, 3.6F * (this.gun[2].countBullets() - (100F * (int) (0.01F * this.gun[2].countBullets()))), 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCounter8", 0.0F, 3.6F * (this.gun[3].countBullets() - (100F * (int) (0.01F * this.gun[3].countBullets()))), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitIL_10.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1b", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 5F, 89F), 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 1.0F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.pictFuel = (0.99F * this.pictFuel) + (0.01F * (this.cvt(this.fm.M.fuel * 1.33F, 0.0F, 1152F, 0.0F, 245F) + this.cvt(this.fm.Or.getTangage(), -12F, 12F, 21.5F, -21.5F))), 0.0F);
        this.mesh.chunkSetAngles("zTWater1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 5F, 89F), 0.0F);
        this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0241F, -0.0241F);
        this.mesh.chunkSetLocate("zHorizon1b", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zHorizon1a", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -29F, 29F), 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, this.setNew.azimuth.getDeg(f) - this.setNew.compassRim.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, -this.setNew.compassRim.getDeg(f), 0.0F);
        } else {
            this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_ATA1", 0.0F, this.pictManf = (0.9F * this.pictManf) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.399966F, 2.133152F, 0.0F, 337F)), 0.0F);
        this.mesh.chunkSetAngles("Z_Course", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -90F, 90F, -20.5F, 20.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Water", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil", 0.0F, 68F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Air1", 0.0F, 42F, 0.0F);
        this.mesh.chunkSetAngles("Z_Air2", 0.0F, this.fm.CT.bHasFlapsControl ? 50.5F : 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Red1", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_Red2", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_Red3", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Red4", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("Z_Red5", this.gun[1].countBullets() < 75);
        this.mesh.chunkVisible("Z_Red6", this.gun[0].countBullets() < 30);
        this.mesh.chunkVisible("Z_Red7", this.gun[2].countBullets() < 75);
        this.mesh.chunkVisible("Z_Red8", this.gun[3].countBullets() < 30);
        this.mesh.chunkVisible("Z_Red9", (this.gun[0].countBullets() != 0) || (this.gun[1].countBullets() != 0) || (this.gun[2].countBullets() != 0) || (this.gun[3].countBullets() != 0));
        this.mesh.chunkVisible("Z_Red10", this.fm.CT.GearControl > 0.5F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getFlap() > 0.9F);
        this.mesh.chunkVisible("Z_White1", !this.bgun[0].haveBullets());
        this.mesh.chunkVisible("Z_White2", !this.bgun[1].haveBullets());
        this.mesh.chunkVisible("Z_White4", !this.bgun[2].haveBullets());
        this.mesh.chunkVisible("Z_White3", !this.bgun[3].haveBullets());
        this.mesh.chunkVisible("Z_White5", this.fm.CT.getBayDoor() > 0.5F);
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes6_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
            this.mesh.chunkVisible("Z_Holes6_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Z_Holes6_D1", true);
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zHorizon1a", false);
            this.mesh.chunkVisible("zHorizon1b", false);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes3_D1", true);
            this.mesh.chunkVisible("Z_Holes6_D1", true);
            this.mesh.chunkVisible("Z_Holes7_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Z_Holes4_D1", true);
            this.mesh.chunkVisible("Z_Holes6_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("Z_Holes5_D1", true);
            this.mesh.chunkVisible("Z_Holes6_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Z_Holes4_D1", true);
            this.mesh.chunkVisible("Z_Holes6_D1", true);
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d2", true);
            this.mesh.chunkVisible("zTOilOut1a", false);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zRPM1b", false);
            this.mesh.chunkVisible("zTWater1a", false);
            this.mesh.chunkVisible("Z_Water", false);
            this.mesh.chunkVisible("Z_ATA1", false);
            this.mesh.chunkVisible("Z_Air1", false);
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
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private float              pictManf;
    private float              pictClap;
    private float              pictFuel;
    private Gun                gun[]              = { null, null, null, null };
    private BulletEmitter      bgun[]             = { null, null, null, null };
    private float              pictGunB[]         = { 0.0F, 0.0F, 0.0F, 0.0F };
    private int                tClap;
    private static final float speedometerScale[] = { 0.0F, 4F, 14F, 51.5F, 90.5F, 129.5F, 167.2F, 196F, 222.5F, 251.5F, 279.5F, 308F, 338.5F, 370.5F, 400.5F, 421F, 443F };

}
