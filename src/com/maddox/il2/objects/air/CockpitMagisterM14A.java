package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitMagisterM14A extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMagisterM14A.this.fm != null) {
                if (CockpitMagisterM14A.this.bNeedSetUp) {
                    CockpitMagisterM14A.this.reflectPlaneMats();
                    CockpitMagisterM14A.this.bNeedSetUp = false;
                }
                CockpitMagisterM14A.this.setTmp = CockpitMagisterM14A.this.setOld;
                CockpitMagisterM14A.this.setOld = CockpitMagisterM14A.this.setNew;
                CockpitMagisterM14A.this.setNew = CockpitMagisterM14A.this.setTmp;
                CockpitMagisterM14A.this.setNew.throttle = ((10F * CockpitMagisterM14A.this.setOld.throttle) + CockpitMagisterM14A.this.fm.CT.PowerControl) / 11F;
                CockpitMagisterM14A.this.setNew.prop = ((10F * CockpitMagisterM14A.this.setOld.prop) + CockpitMagisterM14A.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitMagisterM14A.this.setNew.altimeter = CockpitMagisterM14A.this.fm.getAltitude();
                CockpitMagisterM14A.this.setNew.waypointAzimuth.setDeg(CockpitMagisterM14A.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitMagisterM14A.this.waypointAzimuth() - CockpitMagisterM14A.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitMagisterM14A.this.fm.Or.getKren()) < 30F) {
                    CockpitMagisterM14A.this.setNew.azimuth.setDeg(CockpitMagisterM14A.this.setOld.azimuth.getDeg(1.0F), CockpitMagisterM14A.this.fm.Or.azimut());
                }
                CockpitMagisterM14A.this.setNew.vspeed = ((199F * CockpitMagisterM14A.this.setOld.vspeed) + CockpitMagisterM14A.this.fm.getVertSpeed()) / 200F;
                boolean flag = false;
                if (CockpitMagisterM14A.this.setNew.gCrankAngle < (CockpitMagisterM14A.this.fm.CT.getGear() - 0.005F)) {
                    if (Math.abs(CockpitMagisterM14A.this.setNew.gCrankAngle - CockpitMagisterM14A.this.fm.CT.getGear()) < 0.33F) {
                        CockpitMagisterM14A.this.setNew.gCrankAngle += 0.0025F;
                        flag = true;
                    } else {
                        CockpitMagisterM14A.this.setNew.gCrankAngle = CockpitMagisterM14A.this.fm.CT.getGear();
                        CockpitMagisterM14A.this.setOld.gCrankAngle = CockpitMagisterM14A.this.fm.CT.getGear();
                    }
                }
                if (CockpitMagisterM14A.this.setNew.gCrankAngle > (CockpitMagisterM14A.this.fm.CT.getGear() + 0.005F)) {
                    if (Math.abs(CockpitMagisterM14A.this.setNew.gCrankAngle - CockpitMagisterM14A.this.fm.CT.getGear()) < 0.33F) {
                        CockpitMagisterM14A.this.setNew.gCrankAngle -= 0.0025F;
                        flag = true;
                    } else {
                        CockpitMagisterM14A.this.setNew.gCrankAngle = CockpitMagisterM14A.this.fm.CT.getGear();
                        CockpitMagisterM14A.this.setOld.gCrankAngle = CockpitMagisterM14A.this.fm.CT.getGear();
                    }
                }
                if (flag != this.sfxPlaying) {
                    if (flag) {
                        CockpitMagisterM14A.this.sfxStart(16);
                    } else {
                        CockpitMagisterM14A.this.sfxStop(16);
                    }
                    this.sfxPlaying = flag;
                }
            }
            return true;
        }

        boolean sfxPlaying;

        Interpolater() {
            this.sfxPlaying = false;
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;
        float      gCrankAngle;

        private Variables() {
            this.gCrankAngle = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitMagisterM14A() {
        super("3DO/Cockpit/Magister/hier.him", "i16");
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "prib_one", "prib_one_dd", "prib_two", "prib_two_dd", "prib_three", "prib_three_dd", "prib_four", "prib_four_dd", "shkala", "oxigen" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("Fire1", 0.0F, -20F * (this.fm.CT.WeaponControl[0] ? 1.0F : 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Fire2", 0.0F, -20F * (this.fm.CT.WeaponControl[1] ? 1.0F : 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Thtl", 30F - (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Prop", (this.interp(this.setNew.prop, this.setOld.prop, f) * -57F) + 30F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Thtl_Rod", -30F + (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Prop_Rod", (this.interp(this.setNew.prop, this.setOld.prop, f) * 57F) - 30F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Gear_Crank", (15840F * this.interp(this.setNew.gCrankAngle, this.setOld.gCrankAngle, f)) % 360F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -90F - this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 190F, 0.0F, 14F), CockpitMagisterM14A.fuelQuantityScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), CockpitMagisterM14A.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 4) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zPressAir1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 10F, 0.0F, 275F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.mesh.chunkSetAngles("zRPS1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2400F, 0.0F, 13F), CockpitMagisterM14A.engineRPMScale), 0.0F);
            this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
            this.mesh.chunkSetAngles("zTCil1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -88F), 0.0F);
        }
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zPressOil1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red2", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_dd", true);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zPressOil1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_dd", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zGas1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
    }

    protected void reflectPlaneMats() {
        this.mesh.chunkVisible("ritedoor", false);
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
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[]  = { 0.0F, 0.0F, 18F, 44F, 74.5F, 106F, 136.3F, 169.5F, 207.5F, 245F, 287.5F, 330F };
    private static final float fuelQuantityScale[] = { 0.0F, 38.5F, 74.5F, 98.5F, 122F, 143F, 163F, 182.5F, 203F, 221F, 239.5F, 256F, 274F, 295F, 295F, 295F };
    private static final float engineRPMScale[]    = { 0.0F, 16F, 18F, 59.5F, 100.5F, 135.5F, 166.5F, 198.5F, 227F, 255F, 281.5F, 307F, 317F, 327F };

}
