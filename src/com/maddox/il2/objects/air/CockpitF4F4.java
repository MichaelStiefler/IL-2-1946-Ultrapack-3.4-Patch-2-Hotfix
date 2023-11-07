package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitF4F4 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF4F4.this.fm != null) {
                CockpitF4F4.this.setTmp = CockpitF4F4.this.setOld;
                CockpitF4F4.this.setOld = CockpitF4F4.this.setNew;
                CockpitF4F4.this.setNew = CockpitF4F4.this.setTmp;
                CockpitF4F4.this.setNew.throttle = (0.85F * CockpitF4F4.this.setOld.throttle) + (CockpitF4F4.this.fm.CT.PowerControl * 0.15F);
                CockpitF4F4.this.setNew.prop = (0.85F * CockpitF4F4.this.setOld.prop) + (CockpitF4F4.this.fm.CT.getStepControl() * 0.15F);
                CockpitF4F4.this.setNew.mix = (0.85F * CockpitF4F4.this.setOld.mix) + (CockpitF4F4.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitF4F4.this.setNew.altimeter = CockpitF4F4.this.fm.getAltitude();
                float f = CockpitF4F4.this.waypointAzimuth(10F);
                if (Math.abs(CockpitF4F4.this.fm.Or.getKren()) < 30F) {
                    CockpitF4F4.this.setNew.azimuth.setDeg(CockpitF4F4.this.setOld.azimuth.getDeg(1.0F), CockpitF4F4.this.fm.Or.azimut());
                }
                CockpitF4F4.this.setNew.waypointAzimuth.setDeg(CockpitF4F4.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                CockpitF4F4.this.setNew.vspeed = ((199F * CockpitF4F4.this.setOld.vspeed) + CockpitF4F4.this.fm.getVertSpeed()) / 200F;
            }
            boolean flag = false;
            if (CockpitF4F4.this.setNew.gCrankAngle < (CockpitF4F4.this.fm.CT.getGear() - 0.05F)) {
                if ((Math.abs(CockpitF4F4.this.setNew.gCrankAngle - CockpitF4F4.this.fm.CT.getGear()) > 0.05F) && (Math.abs(CockpitF4F4.this.setNew.gCrankAngle - CockpitF4F4.this.fm.CT.getGear()) < 0.9F)) {
                    CockpitF4F4.this.setNew.gCrankAngle += 0.0025F;
                    flag = true;
                } else {
                    CockpitF4F4.this.setNew.gCrankAngle = CockpitF4F4.this.fm.CT.getGear();
                    CockpitF4F4.this.setOld.gCrankAngle = CockpitF4F4.this.fm.CT.getGear();
                }
            }
            if (CockpitF4F4.this.setNew.gCrankAngle > (CockpitF4F4.this.fm.CT.getGear() + 0.05F)) {
                if ((Math.abs(CockpitF4F4.this.setNew.gCrankAngle - CockpitF4F4.this.fm.CT.getGear()) > 0.05F) && (Math.abs(CockpitF4F4.this.setNew.gCrankAngle - CockpitF4F4.this.fm.CT.getGear()) < 0.9F)) {
                    CockpitF4F4.this.setNew.gCrankAngle -= 0.0025F;
                    flag = true;
                } else {
                    CockpitF4F4.this.setNew.gCrankAngle = CockpitF4F4.this.fm.CT.getGear();
                    CockpitF4F4.this.setOld.gCrankAngle = CockpitF4F4.this.fm.CT.getGear();
                }
            }
            if (flag != this.sfxPlaying) {
                if (flag) {
                    CockpitF4F4.this.sfxStart(16);
                } else {
                    CockpitF4F4.this.sfxStop(16);
                }
                this.sfxPlaying = flag;
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
        float      mix;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      gCrankAngle;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.fm.AS.astateBailoutStep < 2) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        }
        super.doFocusLeave();
    }

    public CockpitF4F4() {
        super("3DO/Cockpit/F4F-4/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "DGP_02", "DGP_03", "DGP_04", "GP_01", "GP_02", "GP_03", "GP_04", "GP_05", "GP_06", "GP_07" });
        this.setNightMats(false);
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(-0.8D, 0.0D, 1.0D));
        this.light1.light.setColor(232F, 75F, 44F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (F4F.bChangedPit) {
            this.reflectPlaneToModel();
            F4F.bChangedPit = false;
        }
        float f1 = this.fm.CT.getWing();
        this.mesh.chunkSetAngles("WingLMid_D0", 0.0F, -110F * f1, 0.0F);
        this.mesh.chunkSetAngles("WingRMid_D0", 0.0F, -110F * f1, 0.0F);
        f1 = -50F * this.fm.CT.getFlap();
        this.mesh.chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.69F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("XGlassDamage2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 722F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 116F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, -722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -168F * this.fm.CT.FlapsControl, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, (15840F * this.interp(this.setNew.gCrankAngle, this.setOld.gCrankAngle, f)) % 360F, 0.0F);
        if (this.fm.CT.GearControl > this.prevGearC) {
            this.mesh.chunkSetAngles("Z_Gear2", 0.0F, 62F, 0.0F);
        } else if (this.fm.CT.GearControl < this.prevGearC) {
            this.mesh.chunkSetAngles("Z_Gear2", 0.0F, 0.0F, 0.0F);
        }
        this.prevGearC = this.fm.CT.GearControl;
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 43.4F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.065F * this.setNew.prop;
        this.mesh.chunkSetLocate("Z_Prop1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 41.25F * this.cvt(this.fm.EI.engines[0].getControlMix(), 1.0F, 1.2F, 0.55F, 1.15F), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, -34F * this.fm.EI.engines[0].getControlCompressor(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal1", 0.0F, -20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal2", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal1", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal2", 0.0F, -20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F);
        this.mesh.chunkSetAngles("Z_Hook1", 0.0F, -60F * this.fm.CT.arrestorControl, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, 90F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 796.3598F, 0.0F, 11F), CockpitF4F4.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -10F, 10F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.0285F, 0.0285F);
        this.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitF4F4.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -90F + this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 296F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 4F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, -180F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.693189F, 0.0F, 342F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 295F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirTemp1", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 50F, -45F, 45F), 0.0F);
        this.mesh.chunkSetAngles("Z_CylTemp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 20F, 120F, 0.0F, 78.5F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.0301F * this.fm.CT.getGear();
        this.mesh.chunkSetLocate("Z_GearInd1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.Gears.bTailwheelLocked) {
            Cockpit.xyz[1] = -0.05955F;
        }
        this.mesh.chunkSetLocate("Z_TWheel_Lock", Cockpit.xyz, Cockpit.ypr);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_CylTemp1", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_AirTemp1", false);
            this.mesh.chunkVisible("Z_Fuel1", false);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHullDamage3", true);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_D3", hiermesh.isChunkVisible("WingLMid_D3"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_D3", hiermesh.isChunkVisible("WingRMid_D3"));
        this.mesh.chunkVisible("Flap01_D0", hiermesh.isChunkVisible("Flap01_D0"));
        this.mesh.chunkVisible("Flap02_D0", hiermesh.isChunkVisible("Flap02_D0"));
        this.mesh.chunkVisible("Flap03_D0", hiermesh.isChunkVisible("Flap03_D0"));
        this.mesh.chunkVisible("Flap04_D0", hiermesh.isChunkVisible("Flap04_D0"));
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
            this.light1.light.setEmit(0.005F, 1.0F);
        } else {
            this.setNightMats(false);
            this.light1.light.setEmit(0.0F, 0.0F);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              prevGearC;
    private LightPointActor    light1;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 669.75F, 719F };
    private static final float variometerScale[]  = { -175.5F, -160.5F, -145.5F, -128F, -100F, -65.5F, 0.0F, 65.5F, 100F, 128F, 145.5F, 160.5F, 175.5F };

}
