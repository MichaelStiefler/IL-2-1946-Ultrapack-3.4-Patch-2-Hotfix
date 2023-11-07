package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitDH103 extends CockpitPilot {
    private class Variables {

        float      throttle0;
        float      throttle1;
        float      prop0;
        float      prop1;
        float      mix0;
        float      mix1;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        float      gearPhi;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitDH103.this.fm != null) {
                CockpitDH103.this.setTmp = CockpitDH103.this.setOld;
                CockpitDH103.this.setOld = CockpitDH103.this.setNew;
                CockpitDH103.this.setNew = CockpitDH103.this.setTmp;
                CockpitDH103.this.setNew.throttle0 = (0.92F * CockpitDH103.this.setOld.throttle0) + (0.08F * CockpitDH103.this.fm.EI.engines[0].getControlThrottle());
                CockpitDH103.this.setNew.prop0 = (0.92F * CockpitDH103.this.setOld.prop0) + (0.08F * CockpitDH103.this.fm.EI.engines[0].getControlProp());
                CockpitDH103.this.setNew.mix0 = (0.92F * CockpitDH103.this.setOld.mix0) + (0.08F * CockpitDH103.this.fm.EI.engines[0].getControlMix());
                CockpitDH103.this.setNew.throttle1 = (0.92F * CockpitDH103.this.setOld.throttle1) + (0.08F * CockpitDH103.this.fm.EI.engines[1].getControlThrottle());
                CockpitDH103.this.setNew.prop1 = (0.92F * CockpitDH103.this.setOld.prop1) + (0.08F * CockpitDH103.this.fm.EI.engines[1].getControlProp());
                CockpitDH103.this.setNew.mix1 = (0.92F * CockpitDH103.this.setOld.mix1) + (0.08F * CockpitDH103.this.fm.EI.engines[1].getControlMix());
                CockpitDH103.this.setNew.altimeter = CockpitDH103.this.fm.getAltitude();
                CockpitDH103.this.setNew.waypointAzimuth.setDeg(CockpitDH103.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitDH103.this.waypointAzimuth() - CockpitDH103.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitDH103.this.fm.Or.getKren()) < 30F) {
                    CockpitDH103.this.setNew.azimuth.setDeg(CockpitDH103.this.setOld.azimuth.getDeg(1.0F), CockpitDH103.this.fm.Or.azimut());
                }
                CockpitDH103.this.setNew.vspeed = (0.99F * CockpitDH103.this.setOld.vspeed) + (0.01F * CockpitDH103.this.fm.getVertSpeed());
                if (CockpitDH103.this.fm.CT.GearControl < 0.5F) {
                    if (CockpitDH103.this.setNew.gearPhi > 0.0F) {
                        CockpitDH103.this.setNew.gearPhi = CockpitDH103.this.setOld.gearPhi - 0.021F;
                    }
                } else if (CockpitDH103.this.setNew.gearPhi < 1.0F) {
                    CockpitDH103.this.setNew.gearPhi = CockpitDH103.this.setOld.gearPhi + 0.021F;
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

    public CockpitDH103() {
        super("3DO/Cockpit/DH-103/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictSupc0 = 0.0F;
        this.pictSupc1 = 0.0F;
        this.pictLlit = 0.0F;
        this.pictManf0 = 1.0F;
        this.pictManf1 = 1.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "TEMPPIT5-op", "TEMPPIT6-op", "TEMPPIT14-op", "TEMPPIT18-op", "TEMPPIT22-op", "TEMPPIT28-op", "TEMPPIT38-op", "TEMPPIT1-tr", "TEMPPIT2-tr", "TEMPPIT3-tr", "TEMPPIT4-tr", "TEMPPIT5-tr", "TEMPPIT6-tr", "TEMPPIT14-tr", "TEMPPIT18-tr", "TEMPPIT22-tr", "TEMPPIT28-tr", "TEMPPIT38-tr", "TEMPPIT1_damage", "TEMPPIT3_damage" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.62F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("XLampGearUpR", ((this.fm.CT.getGear() > 0.01F) && (this.fm.CT.getGear() < 0.99F)) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpL", ((this.fm.CT.getGear() > 0.01F) && (this.fm.CT.getGear() < 0.99F)) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpC", (this.fm.CT.getGear() > 0.01F) && (this.fm.CT.getGear() < 0.99F));
        this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownC", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkSetAngles("Z_Columnbase", 16F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 45F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Elev", -16F * this.pictElev, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.pictAiler, -1F, 1.0F, -0.027F, 0.027F);
        this.mesh.chunkSetLocate("Z_Shlang01", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -Cockpit.xyz[2];
        this.mesh.chunkSetLocate("Z_Shlang02", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Throtle1", 65.45F * this.interp(this.setNew.throttle0, this.setOld.throttle0, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 65.45F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BasePedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Gear1", this.cvt(this.setNew.gearPhi, 0.2F, 0.8F, 0.0F, 116F), 0.0F, 0.0F);
        if (this.setNew.gearPhi < 0.5F) {
            this.mesh.chunkSetAngles("Z_Gear2", this.cvt(this.setNew.gearPhi, 0.0F, 0.2F, 0.0F, -65F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Gear2", this.cvt(this.setNew.gearPhi, 0.8F, 1.0F, -65F, 0.0F), 0.0F, 0.0F);
        }
        float f1;
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                f1 = 24F;
            } else {
                f1 = -24F;
            }
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("Z_Flaps1", this.pictFlap = (0.8F * this.pictFlap) + (0.2F * f1), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.667F - (0.047F * this.fm.CT.getFlap());
        this.mesh.chunkSetLocate("FlapPos", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", 1000F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 72.5F * this.setNew.prop0, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 72.5F * this.setNew.prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Surch1", this.cvt(this.pictSupc0 = (0.8F * this.pictSupc0) + (0.1F * this.fm.EI.engines[0].getControlCompressor()), 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Surch2", this.cvt(this.pictSupc1 = (0.8F * this.pictSupc1) + (0.1F * this.fm.EI.engines[1].getControlCompressor()), 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rad1", this.fm.CT.getRadiatorControl() * 70F, 0.0F, 0.0F);
        this.mesh.chunkVisible("HookLight", this.fm.CT.getArrestor() > 0.99F);
        f1 = 0.0F;
        if (this.fm.AS.bLandingLightOn) {
            f1 = 66F;
        }
        this.mesh.chunkSetAngles("Z_Land1", this.pictLlit = (0.85F * this.pictLlit) + (0.15F * f1), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("COMPASS_M", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_V_LONG", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 10F), CockpitDH103.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_LONG", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_SHORT", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_VY", -this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), CockpitDH103.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL1", this.cvt(this.fm.M.fuel / 1600F, 0.0F, 1.0F, 154F, -154F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL2", this.cvt(this.fm.M.fuel / 900F, 0.0F, 1.0F, 154F, -154F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL3", this.cvt(this.fm.M.fuel / 850F, 0.0F, 1.0F, 154F, -154F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL4", this.cvt(this.fm.M.fuel / 800F, 0.0F, 1.0F, 154F, -154F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_RPM0", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 10F), CockpitDH103.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_BOOST0", this.cvt(this.pictManf0 = (0.91F * this.pictManf0) + (0.09F * this.fm.EI.engines[0].getManifoldPressure()), 0.7242097F, 2.103161F, 65F, -305F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_TEMP_OIL0", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, -306F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_TEMP_RAD0", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitDH103.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STR_OIL_LB0", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, -36F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_RPM1", -this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 5000F, 0.0F, 10F), CockpitDH103.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_BOOST1", this.cvt(this.pictManf1 = (0.91F * this.pictManf1) + (0.09F * this.fm.EI.engines[1].getManifoldPressure()), 0.7242097F, 2.103161F, 65F, -305F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_TEMP_OIL1", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, -306F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_TEMP_RAD1", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitDH103.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STR_OIL_LB1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 10F, 0.0F, -36F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_TURN_UP", -this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("STREL_TURN_DOWN", -this.cvt(this.w.z, -0.23562F, 0.23562F, -48F, 48F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_GOR", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.022F, -0.022F);
        this.mesh.chunkSetLocate("STRELKA_GOS", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("STRELKA_HOUR", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_MINUTE", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_SECUND", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("STRELK_V_LONG", false);
            this.mesh.chunkVisible("STREL_ALT_LONG", false);
            this.mesh.chunkVisible("STREL_ALT_SHORT", false);
            this.mesh.chunkVisible("STRELKA_VY", false);
            this.mesh.chunkVisible("STRELKA_RPM", false);
            this.mesh.chunkVisible("STRELK_TEMP_RAD", false);
            this.mesh.chunkVisible("STRELK_TEMP_OIL", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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
    private float              pictFlap;
    private float              pictSupc0;
    private float              pictSupc1;
    private float              pictLlit;
    private float              pictManf0;
    private float              pictManf1;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 15.5F, 76F, 153.5F, 234F, 304F, 372.5F, 440F, 504F, 566F, 630F };
    private static final float radScale[]         = { 0.0F, 3F, 7F, 13.5F, 30.5F, 40.5F, 51.5F, 68F, 89F, 114F, 145.5F, 181F, 222F, 270.5F, 331.5F };
    private static final float rpmScale[]         = { 0.0F, 15F, 32F, 69.5F, 106.5F, 143F, 180F, 217.5F, 253F, 290F, 327.5F };
    private static final float variometerScale[]  = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };

}