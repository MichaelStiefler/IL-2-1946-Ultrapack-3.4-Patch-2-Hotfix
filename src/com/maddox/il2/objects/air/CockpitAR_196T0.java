package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitAR_196T0 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitAR_196T0.this.fm = World.getPlayerFM();
            if (CockpitAR_196T0.this.fm != null) {
                CockpitAR_196T0.this.setTmp = CockpitAR_196T0.this.setOld;
                CockpitAR_196T0.this.setOld = CockpitAR_196T0.this.setNew;
                CockpitAR_196T0.this.setNew = CockpitAR_196T0.this.setTmp;
                CockpitAR_196T0.this.setNew.altimeter = CockpitAR_196T0.this.fm.getAltitude();
                if (CockpitAR_196T0.this.cockpitDimControl) {
                    if (CockpitAR_196T0.this.setNew.dimPosition > 0.0F) {
                        CockpitAR_196T0.this.setNew.dimPosition = CockpitAR_196T0.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitAR_196T0.this.setNew.dimPosition < 1.0F) {
                    CockpitAR_196T0.this.setNew.dimPosition = CockpitAR_196T0.this.setOld.dimPosition + 0.05F;
                }
                CockpitAR_196T0.this.setNew.throttle = ((10F * CockpitAR_196T0.this.setOld.throttle) + CockpitAR_196T0.this.fm.CT.PowerControl) / 11F;
                CockpitAR_196T0.this.setNew.waypointAzimuth.setDeg(CockpitAR_196T0.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitAR_196T0.this.waypointAzimuth() - CockpitAR_196T0.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitAR_196T0.this.fm.Or.getKren()) < 30F) {
                    CockpitAR_196T0.this.setNew.azimuth.setDeg(CockpitAR_196T0.this.setOld.azimuth.getDeg(1.0F), CockpitAR_196T0.this.fm.Or.azimut());
                }
                CockpitAR_196T0.this.setNew.vspeed = ((499F * CockpitAR_196T0.this.setOld.vspeed) + CockpitAR_196T0.this.fm.getVertSpeed()) / 500F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitAR_196T0() {
        super("3DO/Cockpit/AR-196T/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "87DClocks1", "87DClocks2", "87DClocks3", "87DClocks4", "87DClocks5", "87DPlanks2" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 1.0F, 0.0F, 0.6F);
        this.mesh.chunkSetLocate("Blister1_D0", Cockpit.xyz, Cockpit.ypr);
        if (this.fm == null) {
            return;
        }
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAltCtr2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 6000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTint", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitAR_196T0.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitAR_196T0.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 250F, 0.0F, 5F), CockpitAR_196T0.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), CockpitAR_196T0.temperatureScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), CockpitAR_196T0.temperatureScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurnBank", this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -3F, 3F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBall", this.cvt(this.getBall(6D), -6F, 6F, -10F, 10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompass", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zRepeater", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil1", this.fm.Or.getTangage(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil3", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil2", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", this.cvt(this.setNew.vspeed, -15F, 15F, -135F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn1", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("zPedalL", -this.fm.CT.getRudder() * 10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPedalR", this.fm.CT.getRudder() * 10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zThrottle1", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 80F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPitch1", (this.fm.CT.getStepControl() >= 0.0F ? this.fm.CT.getStepControl() : this.interp(this.setNew.throttle, this.setOld.throttle, f)) * 45F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlaps1", 55F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPipka1", 60F * this.fm.CT.AirBrakeControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBrake1", 46.5F * this.fm.CT.AirBrakeControl, 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.EI.engines[0].getControlCompressor() > 0) {
            Cockpit.xyz[0] = 0.155F;
            Cockpit.ypr[2] = 22F;
        }
        this.mesh.chunkSetLocate("zBoostCrank1", Cockpit.xyz, Cockpit.ypr);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.005F, 0.5F);
            this.light2.light.setEmit(0.005F, 0.5F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 8) != 0)) {
            this.mesh.chunkVisible("Radio_D0", false);
            this.mesh.chunkVisible("Radio_D1", true);
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if (((this.fm.AS.astateCockpitState & 0x10) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.chunkVisible("Radio_D0", false);
            this.mesh.chunkVisible("Radio_D1", true);
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if (((this.fm.AS.astateCockpitState & 1) != 0) || ((this.fm.AS.astateCockpitState & 2) != 0)) {
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("Z_ReviTint", false);
            this.mesh.chunkVisible("Z_ReviTinter", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Revi_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PoppedPanel_D0", false);
            this.mesh.chunkVisible("Z_Repeater1", false);
            this.mesh.chunkVisible("Z_Azimuth1", false);
            this.mesh.chunkVisible("Z_Compass1", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("PoppedPanel_D1", true);
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
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]        = { 0.0F, 11.5F, 24.5F, 46.5F, 67F, 88F };
    private static final float temperatureScale[] = { 0.0F, 15.5F, 35F, 50F, 65F, 79F, 92F, 117.5F, 141.5F, 178.5F, 222.5F, 261.5F, 329F, 340F };

}
