package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class CockpitF84G2 extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      stage;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF84G2.this.fm != null) {
                CockpitF84G2.this.setTmp = CockpitF84G2.this.setOld;
                CockpitF84G2.this.setOld = CockpitF84G2.this.setNew;
                CockpitF84G2.this.setNew = CockpitF84G2.this.setTmp;
                CockpitF84G2.this.setNew.throttle = (0.85F * CockpitF84G2.this.setOld.throttle) + (CockpitF84G2.this.fm.CT.PowerControl * 0.15F);
                CockpitF84G2.this.setNew.prop = (0.85F * CockpitF84G2.this.setOld.prop) + (CockpitF84G2.this.fm.CT.getStepControl() * 0.15F);
                CockpitF84G2.this.setNew.stage = (0.85F * CockpitF84G2.this.setOld.stage) + (CockpitF84G2.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitF84G2.this.setNew.mix = (0.85F * CockpitF84G2.this.setOld.mix) + (CockpitF84G2.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitF84G2.this.setNew.altimeter = CockpitF84G2.this.fm.getAltitude();
                CockpitF84G2.this.setNew.waypointAzimuth.setDeg(CockpitF84G2.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitF84G2.this.waypointAzimuth() - CockpitF84G2.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitF84G2.this.fm.Or.getKren()) < 30F) {
                    CockpitF84G2.this.setNew.azimuth.setDeg(CockpitF84G2.this.setOld.azimuth.getDeg(1.0F), CockpitF84G2.this.fm.Or.azimut());
                }
                CockpitF84G2.this.setNew.vspeed = ((199F * CockpitF84G2.this.setOld.vspeed) + CockpitF84G2.this.fm.getVertSpeed()) / 200F;
                float f = ((F84G2) CockpitF84G2.this.aircraft()).k14Distance;
                CockpitF84G2.this.setNew.k14w = (5F * CockpitF84G2.k14TargetWingspanScale[((F84G2) CockpitF84G2.this.aircraft()).k14WingspanType]) / f;
                CockpitF84G2.this.setNew.k14w = (0.9F * CockpitF84G2.this.setOld.k14w) + (0.1F * CockpitF84G2.this.setNew.k14w);
                CockpitF84G2.this.setNew.k14wingspan = (0.9F * CockpitF84G2.this.setOld.k14wingspan) + (0.1F * CockpitF84G2.k14TargetMarkScale[((F84G2) CockpitF84G2.this.aircraft()).k14WingspanType]);
                CockpitF84G2.this.setNew.k14mode = (0.8F * CockpitF84G2.this.setOld.k14mode) + (0.2F * ((F84G2) CockpitF84G2.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitF84G2.this.aircraft().FM.getW();
                double d = 0.00125D * f;
                float f1 = (float) Math.toDegrees(d * vector3d.z);
                float f2 = -(float) Math.toDegrees(d * vector3d.y);
                float f3 = CockpitF84G2.this.floatindex((f - 200F) * 0.04F, CockpitF84G2.k14BulletDrop) - CockpitF84G2.k14BulletDrop[0];
                f2 += (float) Math.toDegrees(Math.atan(f3 / f));
                CockpitF84G2.this.setNew.k14x = (0.92F * CockpitF84G2.this.setOld.k14x) + (0.08F * f1);
                CockpitF84G2.this.setNew.k14y = (0.92F * CockpitF84G2.this.setOld.k14y) + (0.08F * f2);
                if (CockpitF84G2.this.setNew.k14x > 7F) {
                    CockpitF84G2.this.setNew.k14x = 7F;
                }
                if (CockpitF84G2.this.setNew.k14x < -7F) {
                    CockpitF84G2.this.setNew.k14x = -7F;
                }
                if (CockpitF84G2.this.setNew.k14y > 7F) {
                    CockpitF84G2.this.setNew.k14y = 7F;
                }
                if (CockpitF84G2.this.setNew.k14y < -7F) {
                    CockpitF84G2.this.setNew.k14y = -7F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitF84G2() {
        super("3DO/Cockpit/F84G/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
        this.pictGear = 0.0F;
        this.pictFlap = 0.0F;
        this.cockpitNightMats = (new String[] { "Instrumentos1", "Instrumentos2", "Instrumentos3", "Instrumentos4", "Instrumentos5", "Instrumentos6", "Instrumentos7", "GagePanel3", "GagePanel5", "needles" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (Math.abs(this.fm.CT.getTrimAileronControl() - this.fm.CT.trimAileron) > 1E-006F) {
            if ((this.fm.CT.getTrimAileronControl() - this.fm.CT.trimAileron) > 0.0F) {
                this.mesh.chunkSetAngles("A_Trim1", 0.0F, 10F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("A_Trim1", 0.0F, 10F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("A_Trim1", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.getTrimRudderControl() - this.fm.CT.trimRudder) > 1E-006F) {
            if ((this.fm.CT.getTrimRudderControl() - this.fm.CT.trimRudder) > 0.0F) {
                this.mesh.chunkSetAngles("A_Trim2", 0.0F, 10F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("A_Trim2", 0.0F, 10F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("A_Trim2", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.getTrimElevatorControl() - this.fm.CT.trimElevator) > 1E-006F) {
            if ((this.fm.CT.getTrimElevatorControl() - this.fm.CT.trimElevator) > 0.0F) {
                this.mesh.chunkSetAngles("A_Trim3", 0.0F, 10F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("A_Trim3", 0.0F, 10F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("A_Trim3", 0.0F, 0.0F, 0.0F);
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Trim_Rud", -60F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Trim_Ele", 0.0F, 0.0F, 722F * this.fm.CT.getTrimElevatorControl());
        this.mesh.chunkSetAngles("Trim_Ail", 0.0F, 722F * this.fm.CT.getTrimAileronControl(), 0.0F);
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            int i = ((F84G2) this.aircraft()).k14Mode;
            boolean flag = i < 2;
            this.mesh.chunkVisible("Z_Z_RETICLE", flag);
            flag = i > 0;
            this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
            this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = this.setNew.k14w;
            for (int j = 1; j < 7; j++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
                this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
            }

        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.2F, 0.0F, 0.625F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_RightPedal", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, -70F * this.setNew.throttle);
        this.resetYPRmodifier();
        float f1 = this.fm.EI.engines[0].getStage();
        if ((f1 > 0.0F) && (f1 < 7F)) {
            f1 = 0.0345F;
        } else {
            f1 = -0.05475F;
        }
        Cockpit.xyz[2] = f1;
        this.mesh.chunkSetLocate("Z_EngShutOff", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Column", (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F);
        this.mesh.chunkSetAngles("z_Flap", 0.0F, -38F * (this.pictFlap = (0.75F * this.pictFlap) + (0.95F * this.fm.CT.FlapsControl)), 0.0F);
        this.mesh.chunkSetAngles("Palanca_Tren", 0.0F, 0.0F, 46F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)));
        this.mesh.chunkSetAngles("Z_Target1", 0.0F, 0.0F, this.setNew.k14wingspan);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitF84G2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 0.0F, 1126.541F, 0.0F, 14F), CockpitF84G2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getPowerOutput();
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt((float) Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 123F), 0.0F, 0.0F);
        f1 = this.cvt(this.fm.M.fuel, 0.0F, 2760F, 0.0F, 270F);
        if (f1 < 45F) {
            f1 = this.cvt(f1, 0.0F, 45F, -58F, 45F);
        }
        f1 += 58F;
        this.mesh.chunkSetAngles("Z_Fuel2", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 28F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 322F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitF84G2.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 90F + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3a", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ny1", this.cvt(this.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        this.mesh.chunkVisible("A_Tren", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_Alt_Alarm", (this.fm.getAltitude() < 50F) && (this.fm.CT.getGear() < 0.05F));
        this.mesh.chunkVisible("Z_Hook", this.fm.CT.getArrestor() > 0.9F);
        this.mesh.chunkVisible("A_Brake", this.fm.CT.AirBrakeControl > 0.5F);
        this.mesh.chunkVisible("A_Eng_Fire", this.fm.AS.astateEngineStates[0] > 2);
        this.mesh.chunkVisible("A_Eng_OH", this.fm.EI.engines[0].tOilOut > 125D);
        this.mesh.chunkVisible("A_ATO", (this.fm.EI.getPowerOutput() > 0.8F) && this.fm.Gears.onGround());
        this.mesh.chunkVisible("A_Fuel1", this.fm.M.fuel < 455F);
        this.mesh.chunkVisible("A_Fuel2", this.fm.M.fuel < 226F);
        if (this.tgun[0] == null) {
            this.tgun[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev07");
        }
        if (this.tgun[0] != null) {
            this.mesh.chunkVisible("A_Fuel3", this.tgun[0].countBullets() > 0);
        }
        this.mesh.chunkVisible("TankMain", this.tgun[0].countBullets() == 0);
        this.mesh.chunkVisible("TankDrop", this.tgun[0].countBullets() > 0);
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN06");
        }
        if (this.bgun[0] == null) {
            this.bgun[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            this.bgun[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
        }
        if (this.rgun[0] == null) {
            this.rgun[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock02");
            this.rgun[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock22");
            this.rgun[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock09");
        }
        if (this.gun[0] != null) {
            this.mesh.chunkVisible("A_Guns_A", this.gun[0].countBullets() > 0);
        }
        this.mesh.chunkVisible("A_Guns_E", this.gun[0].countBullets() < 50);
        if (this.bgun[0] != null) {
            this.mesh.chunkVisible("A_Bomb_I", this.bgun[0].countBullets() > 0);
        }
        if (this.bgun[1] != null) {
            this.mesh.chunkVisible("A_Bomb_O", this.bgun[1].countBullets() > 0);
        }
        if (this.rgun[0] != null) {
            this.mesh.chunkVisible("A_Rock_I2", this.rgun[0].countBullets() > 0);
        }
        if (this.rgun[1] != null) {
            this.mesh.chunkVisible("A_Rock_I", this.rgun[1].countBullets() > 0);
        }
        if (this.rgun[2] != null) {
            this.mesh.chunkVisible("A_Rock_O", this.rgun[2].countBullets() > 0);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("Mira", false);
            this.mesh.chunkVisible("Mira_D", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for (int i = 1; i < 7; i++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);
            }

            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            if ((this.fm.AS.astateCockpitState & 0x80) != 0) {

            }
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
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
    protected boolean          bHasBoosters;
    protected long             boosterFireOutTime;
    private float              pictGear;
    private float              pictFlap;
    private Gun                gun[]                    = { null, null, null, null };
    private BulletEmitter      bgun[]                   = { null, null, null, null };
    private BulletEmitter      rgun[]                   = { null, null, null, null };
    private BulletEmitter      tgun[]                   = { null, null };
    private static final float speedometerScale[]       = { 0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 249.4F, 271.7F, 294F, 316.5F, 339.5F };
    private static final float variometerScale[]        = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };

}
