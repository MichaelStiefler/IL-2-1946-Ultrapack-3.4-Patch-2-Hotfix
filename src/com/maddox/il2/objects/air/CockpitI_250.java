package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitI_250 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitI_250.this.fm != null) {
                CockpitI_250.this.setTmp = CockpitI_250.this.setOld;
                CockpitI_250.this.setOld = CockpitI_250.this.setNew;
                CockpitI_250.this.setNew = CockpitI_250.this.setTmp;
                CockpitI_250.this.setNew.throttle1 = (0.85F * CockpitI_250.this.setOld.throttle1) + (CockpitI_250.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitI_250.this.setNew.throttle2 = (0.85F * CockpitI_250.this.setOld.throttle2) + (CockpitI_250.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitI_250.this.setNew.prop = (0.85F * CockpitI_250.this.setOld.prop) + (CockpitI_250.this.fm.CT.getStepControl() * 0.15F);
                CockpitI_250.this.setNew.mix = (0.85F * CockpitI_250.this.setOld.mix) + (CockpitI_250.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitI_250.this.setNew.altimeter = CockpitI_250.this.fm.getAltitude();
                float f = CockpitI_250.this.waypointAzimuth();
                CockpitI_250.this.setNew.waypointAzimuth.setDeg(CockpitI_250.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitI_250.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitI_250.this.fm.Or.getKren()) < 30F) {
                    CockpitI_250.this.setNew.azimuth.setDeg(CockpitI_250.this.setOld.azimuth.getDeg(1.0F), CockpitI_250.this.fm.Or.azimut());
                }
                CockpitI_250.this.setNew.vspeed = ((199F * CockpitI_250.this.setOld.vspeed) + CockpitI_250.this.fm.getVertSpeed()) / 200F;
                CockpitI_250.this.setNew.manifold1 = (0.8F * CockpitI_250.this.setOld.manifold1) + (0.2F * CockpitI_250.this.fm.EI.engines[0].getManifoldPressure());
                CockpitI_250.this.setNew.manifold2 = (0.8F * CockpitI_250.this.setOld.manifold2) + (0.2F * CockpitI_250.this.fm.EI.engines[1].getManifoldPressure());
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop;
        float      mix;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      manifold1;
        float      manifold2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    public CockpitI_250() {
        super("3DO/Cockpit/I-250/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.pictVRDK = 0.0F;
        this.pictTLck = 0.0F;
        this.pictMetl = 0.0F;
        this.pictTriE = 0.0F;
        this.pictTriR = 0.0F;
        this.cockpitNightMats = (new String[] { "gauges_01", "gauges_02", "gauges_03", "gauges_04", "gauges_05", "gauges_06", "DMG_gauges_01", "DMG_gauges_02", "DMG_gauges_03", "DMG_gauges_04", "DMG_gauges_05", "DMG_gauges_06" });
        this.setNightMats(false);
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LIGHTHOOK_L", this.light1);
        hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_R");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LIGHTHOOK_R", this.light2);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.6F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("PedalCrossBar", 0.0F, -12F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Pedal_L", 0.0F, 12F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Pedal_R", 0.0F, -12F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("IgnitionSwitch", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -129F), 0.0F);
        this.mesh.chunkSetAngles("GearHandle", 0.0F, 0.0F, 35F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)));
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                this.mesh.chunkSetAngles("FlapHandle", 0.0F, -20F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("FlapHandle", 0.0F, 20F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("TQHandle1", 0.0F, 49.9F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("PropPitchLvr", 0.0F, 49.9F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 1.0F, 0.1035F, 0.0F);
        this.mesh.chunkSetLocate("MixLvr", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Eng2Switch", 0.0F, 65F * (this.pictVRDK = (0.85F * this.pictVRDK) + (0.15F * (this.fm.EI.engines[1].getStage() != 6 ? 0.0F : 1.0F))), 0.0F);
        this.mesh.chunkSetAngles("TQHandle2", 0.0F, 100F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("TWLock", 0.0F, -31F * (this.pictTLck = (0.85F * this.pictTLck) + (0.15F * (this.fm.Gears.bTailwheelLocked ? 1.0F : 0.0F))), 0.0F);
        this.mesh.chunkSetAngles("ElevTrim", 0.0F, 0.0F, 42.2F * (this.pictTriE = (0.92F * this.pictTriE) + (0.08F * this.fm.CT.getTrimElevatorControl())));
        this.mesh.chunkSetAngles("RuddrTrim", 0.0F, 79.9F * (this.pictTriR = (0.92F * this.pictTriR) + (0.08F * this.fm.CT.getTrimRudderControl())), 0.0F);
        this.mesh.chunkSetAngles("FLCSA", 0.0F, -12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F);
        this.mesh.chunkSetAngles("FLCSB", 0.0F, 12F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.pictMetl = (0.96F * this.pictMetl) + (0.04F * (0.6F * this.fm.EI.engines[1].getThrustOutput() * this.fm.EI.engines[1].getControlThrottle() * (this.fm.EI.engines[1].getStage() != 6 ? 0.02F : 1.0F)));
        this.mesh.chunkSetAngles("NeedExhstPress2", 0.0F, this.cvt(this.pictMetl, 0.0F, 1.0F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress2", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.55F, 0.0F, 1.0F, 0.0F, 232F), 0.0F);
        this.mesh.chunkSetAngles("NeedExstT2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 300F, 900F, 0.0F, 239F), 0.0F);
        this.mesh.chunkSetAngles("NeedMfdP2", 0.0F, this.cvt(this.setNew.manifold2, 0.399966F, 2.133152F, 0.0F, 338F), 0.0F);
        this.mesh.chunkSetAngles("NeedOilT2", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 82F), 0.0F);
        this.mesh.chunkSetAngles("NeedWatT", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 82F), 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 60000F, 0.0F, 2160F), 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_M", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 60000F, 0.0F, 21600F), 0.0F);
        this.mesh.chunkSetAngles("NeedCompass", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("NeedMin", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("NeedSpeed", 0.0F, this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1200F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("NeedClimb", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("NeedRPMA", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("NeedRPMB", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("NeedMfdP1", 0.0F, this.cvt(this.setNew.manifold1, 0.399966F, 2.133152F, 0.0F, 335F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0204F, -0.0204F);
        this.mesh.chunkSetLocate("NeedAHCyl", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("NeedAHBar", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("NeedTurn", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -29F, 29F), 0.0F);
        this.mesh.chunkSetAngles("NeedOilT1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 1.0F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("NeedOilPress1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("NeedFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 432F, 0.0F, 180F), 0.0F);
        this.mesh.chunkVisible("FlareGearUp_R", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearUp_L", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareGearDn_R", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("FlareGearDn_L", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("FlareFuel", this.fm.M.fuel < 81F);
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Gages6_D0", false);
            this.mesh.chunkVisible("Gages6_D1", true);
            this.mesh.chunkVisible("NeedAHCyl", false);
            this.mesh.chunkVisible("NeedAHBar", false);
            this.mesh.chunkVisible("NeedMfdP1", false);
            this.mesh.chunkVisible("NeedOilT1", false);
            this.mesh.chunkVisible("NeedFuelPress1", false);
            this.mesh.chunkVisible("NeedOilPress1", false);
            this.mesh.chunkVisible("Z_Holes6_D1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("GunSight", false);
            this.mesh.chunkVisible("GSGlassMain", false);
            this.mesh.chunkVisible("DGunSight", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("OilSplats", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gages4_D0", false);
            this.mesh.chunkVisible("Gages4_D1", true);
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("NeedAlt_M", false);
            this.mesh.chunkVisible("NeedCompass", false);
            this.mesh.chunkVisible("NeedHour", false);
            this.mesh.chunkVisible("NeedMin", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Gages1_D0", false);
            this.mesh.chunkVisible("Gages1_D1", true);
            this.mesh.chunkVisible("NeedExstT2", false);
            this.mesh.chunkVisible("NeedExhstPress2", false);
            this.mesh.chunkVisible("NeedFuelPress2", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Gages2_D0", false);
            this.mesh.chunkVisible("Gages2_D1", true);
            this.mesh.chunkVisible("NeedOilT2", false);
            this.mesh.chunkVisible("NeedWatT", false);
            this.mesh.chunkVisible("NeedMfdP2", false);
            this.mesh.chunkVisible("DamageHull2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Gages5_D0", false);
            this.mesh.chunkVisible("Gages5_D1", true);
            this.mesh.chunkVisible("NeedSpeed", false);
            this.mesh.chunkVisible("NeedClimb", false);
            this.mesh.chunkVisible("NeedRPMA", false);
            this.mesh.chunkVisible("NeedRPMB", false);
            this.mesh.chunkVisible("DamageHull2", true);
            this.mesh.chunkVisible("DamageHull3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Gages7_D0", false);
            this.mesh.chunkVisible("Gages7_D1", true);
            this.mesh.chunkVisible("NeedFuel", false);
            this.mesh.chunkVisible("NeedAmmeter", false);
            this.mesh.chunkVisible("NeedVmeter", false);
            this.mesh.chunkVisible("DamageHull1", true);
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
            this.light1.light.setEmit(0.005F, 0.5F);
            this.light2.light.setEmit(0.005F, 0.5F);
        } else {
            this.setNightMats(false);
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
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

    public void doToggleDim() {
    }

    private boolean         bNeedSetUp;
    private Variables       setOld;
    private Variables       setNew;
    private Variables       setTmp;
    private LightPointActor light1;
    private LightPointActor light2;
    private float           pictAiler;
    private float           pictElev;
    private float           pictGear;
    private float           pictVRDK;
    private float           pictTLck;
    private float           pictMetl;
    private float           pictTriE;
    private float           pictTriR;

}
