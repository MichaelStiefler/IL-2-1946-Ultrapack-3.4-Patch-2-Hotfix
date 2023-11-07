package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitIL_2_1942 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitIL_2_1942.this.fm != null) {
                CockpitIL_2_1942.this.setTmp = CockpitIL_2_1942.this.setOld;
                CockpitIL_2_1942.this.setOld = CockpitIL_2_1942.this.setNew;
                CockpitIL_2_1942.this.setNew = CockpitIL_2_1942.this.setTmp;
                CockpitIL_2_1942.this.setNew.throttle = ((10F * CockpitIL_2_1942.this.setOld.throttle) + CockpitIL_2_1942.this.fm.CT.PowerControl) / 11F;
                if (CockpitIL_2_1942.this.fm.Gears.isHydroOperable()) {
                    CockpitIL_2_1942.this.setNew.undercarriage = ((10F * CockpitIL_2_1942.this.setOld.undercarriage) + CockpitIL_2_1942.this.fm.CT.GearControl) / 11F;
                }
                CockpitIL_2_1942.this.setNew.flaps = ((10F * CockpitIL_2_1942.this.setOld.flaps) + (CockpitIL_2_1942.this.fm.CT.FlapsControl <= 0.0F ? 1.0F : 0.0F)) / 11F;
                CockpitIL_2_1942.this.setNew.altimeter = CockpitIL_2_1942.this.fm.getAltitude();
                if (Math.abs(CockpitIL_2_1942.this.fm.Or.getKren()) < 30F) {
                    CockpitIL_2_1942.this.setNew.azimuth.setDeg(CockpitIL_2_1942.this.setOld.azimuth.getDeg(1.0F), CockpitIL_2_1942.this.fm.Or.azimut());
                }
                CockpitIL_2_1942.this.setNew.vspeed = ((199F * CockpitIL_2_1942.this.setOld.vspeed) + CockpitIL_2_1942.this.fm.getVertSpeed()) / 200F;
                if (CockpitIL_2_1942.this.fm.CT.saveWeaponControl[2]) {
                    CockpitIL_2_1942.this.roControl = true;
                    CockpitIL_2_1942.this.roControlTime = Time.current();
                }
                if (CockpitIL_2_1942.this.roControl) {
                    CockpitIL_2_1942.this.setNew.roPos = ((2.0F * CockpitIL_2_1942.this.setOld.roPos) - 230F) / 3F;
                    if (CockpitIL_2_1942.this.roControlTime < (Time.current() - 2210L)) {
                        CockpitIL_2_1942.this.roControl = false;
                    }
                } else {
                    CockpitIL_2_1942.this.setNew.roPos = ((14F * CockpitIL_2_1942.this.setOld.roPos) - 0.0F) / 15F;
                }
                if (CockpitIL_2_1942.this.fm.CT.saveWeaponControl[3]) {
                    CockpitIL_2_1942.this.bombControl = true;
                    CockpitIL_2_1942.this.bombControlTime = Time.current();
                }
                if (CockpitIL_2_1942.this.bombControl) {
                    CockpitIL_2_1942.this.setNew.bombPos = ((2.0F * CockpitIL_2_1942.this.setOld.bombPos) - 220F) / 3F;
                    if (CockpitIL_2_1942.this.bombControlTime < (Time.current() - 2210L)) {
                        CockpitIL_2_1942.this.bombControl = false;
                    }
                } else {
                    CockpitIL_2_1942.this.setNew.bombPos = ((14F * CockpitIL_2_1942.this.setOld.bombPos) - 0.0F) / 15F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      undercarriage;
        float      flaps;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        float      roPos;
        float      bombPos;
        float      xyz[] = { 0.0F, 0.0F, 0.0F };
        float      ypr[] = { 0.0F, 0.0F, 0.0F };

        private Variables() {
            this.roPos = 0.0F;
            this.bombPos = 0.0F;
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitIL_2_1942() {
        super("3DO/Cockpit/Il-2-Late/hier.him", "il2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.roControlTime = 0L;
        this.bombControlTime = 0L;
        this.previous = 0L;
        HookNamed hooknamed = new HookNamed(this.mesh, "_LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(245F, 238F, 126F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("_LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "_LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(245F, 238F, 126F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("_LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "prib_one", "prib_two", "prib_three", "prib_four", "prib_six", "prib_six_na", "shkala", "prib_one_dd", "prib_two_dd", "prib_three_dd" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("richag", 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 15F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)));
        this.mesh.chunkSetAngles("os_V", 0.0F, 0.0F, 10F * this.fm.CT.getElevator());
        this.mesh.chunkSetAngles("tyga_V", 0.0F, -12F * this.fm.CT.getElevator(), 0.0F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, 15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("ruchkaGaza", 0.0F, -60F + (120F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F);
        this.mesh.chunkSetAngles("ruchkaShassi", 0.0F, 85F * this.interp(this.setNew.undercarriage, this.setOld.undercarriage, f), 0.0F);
        this.mesh.chunkSetAngles("ruchkaShitkov", 0.0F, 85F - (85F * this.interp(this.setNew.flaps, this.setOld.flaps, f)), 0.0F);
        this.mesh.chunkSetAngles("r_one", 0.0F, -20F * (this.fm.CT.saveWeaponControl[0] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("r_two", 0.0F, -20F * (this.fm.CT.saveWeaponControl[1] ? 1 : 0), 0.0F);
        this.mesh.chunkSetAngles("r_RO", 0.0F, this.setNew.roPos, 0.0F);
        this.mesh.chunkSetAngles("r_bomb", 0.0F, this.setNew.bombPos, 0.0F);
        this.mesh.chunkSetAngles("r_turn", 0.0F, 20F * this.fm.CT.BrakeControl, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -30F, 30F, -30F, 30F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.setOld.xyz[2] = this.cvt(this.fm.Or.getTangage(), -40F, 40F, 0.03F, -0.03F);
        this.mesh.chunkSetLocate("zHorizon1a", this.setOld.xyz, this.setOld.ypr);
        this.mesh.chunkSetAngles("zHorizon1b", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 300F, 0.0F, 180F), 0.0F);
        if (((this.fm.AS.astateCockpitState & 2) == 0) && ((this.fm.AS.astateCockpitState & 4) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitIL_2_1942.speedometerScale), 0.0F);
            this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
            this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 180F), 0.0F);
            this.mesh.chunkSetAngles("zGasPrs1a", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 8F, 0.0F, -180F), 0.0F);
        }
        this.w.set(this.fm.getW());
        this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zRPM1b", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zTWater1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, -0F, -86F), 0.0F);
        if (((this.fm.AS.astateCockpitState & 1) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 120F, -0F, -86F), 0.0F);
        }
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red2", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red2", this.fm.CT.getGear() == 0.0F);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.005F, 0.6F);
            this.light2.light.setEmit(0.005F, 0.6F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zHorizon1a", false);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
        }
        if (((this.fm.AS.astateCockpitState & 1) != 0) || ((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d2", true);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zRPM1b", false);
            this.mesh.chunkVisible("zVariometer1a", false);
            this.mesh.chunkVisible("zTurn1a", false);
            this.mesh.chunkVisible("zSlide1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    boolean                    roControl;
    long                       roControlTime;
    boolean                    bombControl;
    long                       bombControlTime;
    long                       previous;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };

}
