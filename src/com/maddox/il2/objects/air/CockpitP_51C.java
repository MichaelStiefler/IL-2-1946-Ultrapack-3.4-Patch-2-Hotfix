package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_51C extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_51C.this.fm != null) {
                CockpitP_51C.this.setTmp = CockpitP_51C.this.setOld;
                CockpitP_51C.this.setOld = CockpitP_51C.this.setNew;
                CockpitP_51C.this.setNew = CockpitP_51C.this.setTmp;
                CockpitP_51C.this.setNew.throttle = (0.85F * CockpitP_51C.this.setOld.throttle) + (CockpitP_51C.this.fm.CT.PowerControl * 0.15F);
                CockpitP_51C.this.setNew.prop = (0.85F * CockpitP_51C.this.setOld.prop) + (CockpitP_51C.this.fm.CT.getStepControl() * 0.15F);
                CockpitP_51C.this.setNew.stage = (0.85F * CockpitP_51C.this.setOld.stage) + (CockpitP_51C.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitP_51C.this.setNew.mix = (0.85F * CockpitP_51C.this.setOld.mix) + (CockpitP_51C.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitP_51C.this.setNew.altimeter = CockpitP_51C.this.fm.getAltitude();
                float f = CockpitP_51C.this.waypointAzimuth(10F);
                if (Math.abs(CockpitP_51C.this.fm.Or.getKren()) < 30F) {
                    CockpitP_51C.this.setNew.azimuth.setDeg(CockpitP_51C.this.setOld.azimuth.getDeg(1.0F), CockpitP_51C.this.fm.Or.azimut());
                }
                CockpitP_51C.this.setNew.waypointAzimuth.setDeg(CockpitP_51C.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                CockpitP_51C.this.setNew.vspeed = ((199F * CockpitP_51C.this.setOld.vspeed) + CockpitP_51C.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      stage;
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitP_51C() {
        super("3DO/Cockpit/P-51C/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "Fuel", "Textur1", "Textur2", "Textur3", "Textur4", "Textur5", "Textur6", "Textur8" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Trim1", 722F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 722F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", -722F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 21F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", -30F + (30F * this.fm.CT.GearControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 77F * this.setNew.throttle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 83.3F * this.setNew.prop, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 66F * this.setNew.mix, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPedalStep", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal2", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LPedalStep", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal2", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0362F, -0.0362F);
        this.mesh.chunkSetLocate("Z_TurnBank2a", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitP_51C.speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(7D), -7F, 7F, 14F, -14F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.getBall(7D), -7F, 7F, 14F, -14F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitP_51C.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Heading1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 316F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.35F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", this.cvt(this.fm.M.fuel, 0.0F, 245.2F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", this.cvt(this.fm.M.fuel, 245.2F, 490.4F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", this.cvt(this.fm.M.fuel, 245.2F, 490.4F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 14F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, 40F, 150F, 0.0F, 130F), 0.0F, 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction1", this.cvt(f1, 0.0F, 10F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(this.fm.EI.engines[0].getReadyness(), 0.0F, 2.0F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearGreen1", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearGreen2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_GearGreen3", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearRed2", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_GearRed3", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Oxywarning1", false);
        this.mesh.chunkVisible("Z_Supercharger1", this.fm.EI.engines[0].getControlCompressor() > 0);
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 5F, 47.5F, 92F, 134F, 180F, 227F, 241F, 255F, 272.5F, 287F, 299.5F, 312.5F, 325.5F, 338.5F };
    private static final float variometerScale[]  = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };

}
