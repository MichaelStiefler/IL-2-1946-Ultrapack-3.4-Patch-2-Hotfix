package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitMeteorBase extends CockpitPilot {
    class Interpolater extends InterpolateRef {
        public boolean tick() {
            CockpitMeteorBase.this.setTmp = CockpitMeteorBase.this.setOld;
            CockpitMeteorBase.this.setOld = CockpitMeteorBase.this.setNew;
            CockpitMeteorBase.this.setNew = CockpitMeteorBase.this.setTmp;
            CockpitMeteorBase.this.setNew.altimeter = CockpitMeteorBase.this.fm.getAltitude();
            CockpitMeteorBase.this.setNew.throttlel = ((10F * CockpitMeteorBase.this.setOld.throttlel) + CockpitMeteorBase.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitMeteorBase.this.setNew.throttler = ((10F * CockpitMeteorBase.this.setOld.throttler) + CockpitMeteorBase.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            float f = CockpitMeteorBase.this.waypointAzimuth();
            if (CockpitMeteorBase.this.useRealisticNavigationInstruments()) {
                CockpitMeteorBase.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitMeteorBase.this.setOld.waypointAzimuth.setDeg(f - 90F);
            } else {
                CockpitMeteorBase.this.setNew.waypointAzimuth.setDeg(CockpitMeteorBase.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitMeteorBase.this.setOld.azimuth.getDeg(1.0F));
            }
            if (Math.abs(CockpitMeteorBase.this.fm.Or.getKren()) < 30F) {
                CockpitMeteorBase.this.setNew.azimuth.setDeg(CockpitMeteorBase.this.setOld.azimuth.getDeg(1.0F), CockpitMeteorBase.this.fm.Or.azimut());
            }
            CockpitMeteorBase.this.setNew.vspeed = ((299F * CockpitMeteorBase.this.setOld.vspeed) + CockpitMeteorBase.this.fm.getVertSpeed()) / 300F;
            if (CockpitMeteorBase.this.cockpitDimControl) {
                if (CockpitMeteorBase.this.setNew.dimPosition > 0.0F) {
                    CockpitMeteorBase.this.setNew.dimPosition = CockpitMeteorBase.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitMeteorBase.this.setNew.dimPosition < 1.0F) {
                CockpitMeteorBase.this.setNew.dimPosition = CockpitMeteorBase.this.setOld.dimPosition + 0.05F;
            }
            CockpitMeteorBase.this.setNew.beaconDirection = ((10F * CockpitMeteorBase.this.setOld.beaconDirection) + CockpitMeteorBase.this.getBeaconDirection()) / 11F;
            CockpitMeteorBase.this.setNew.beaconRange = ((10F * CockpitMeteorBase.this.setOld.beaconRange) + CockpitMeteorBase.this.getBeaconRange()) / 11F;
            return true;
        }
    }

    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitMeteorBase(String hierFile, String shortName) {
        super(hierFile, shortName);
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
//        pictGear = 0.0F;
        this.cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank" });
        this.setNightMats(false);
        this.setNew.dimPosition = 1.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        this.mesh.chunkSetLocate("canopy", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.isTick(44, 0)) {
            this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_GearRGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearCGreen1", this.fm.CT.getGear() == 1.0F);
            this.mesh.chunkVisible("Z_GearLRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
            this.mesh.chunkVisible("Z_GearRRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
            this.mesh.chunkVisible("Z_GearCRed1", this.fm.CT.getGear() == 0.0F);
            this.mesh.chunkVisible("Z_MachLamp", (this.fm.getSpeed() / Atmosphere.sonicSpeed((float) ((Tuple3d) (this.fm.Loc)).z)) > 0.8F);
            this.mesh.chunkVisible("Z_EngineFireLampR", this.fm.AS.astateEngineStates[1] > 2);
            this.mesh.chunkVisible("Z_EngineFireLampL", this.fm.AS.astateEngineStates[0] > 2);
            this.mesh.chunkVisible("Z_FuelLampV", this.fm.M.fuel < 200F);
            this.mesh.chunkVisible("Z_FuelLampIn", this.fm.M.fuel < 200F);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.pictGear = (0.85F * this.pictGear) + (0.011F * this.fm.CT.GearControl);
        this.mesh.chunkSetLocate("Z_GearEin", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_FlapEin", 0.0F, 0.0F, -(this.fm.CT.FlapsControl - 0.08F) * 115F);
        this.mesh.chunkSetAngles("Z_Columnbase", 8F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 30F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = -0.0025F;
        }
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[2] || this.fm.CT.saveWeaponControl[3]) {
            Cockpit.xyz[2] = -0.00325F;
        }
        this.mesh.chunkSetAngles("Z_PedalStrut", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.EI.engines[0].getControlThrottle(), 0.01F, 0.99F, 0.0F, 0.55F) / 5F;
        this.mesh.chunkSetLocate("Z_ThrottleL", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.EI.engines[1].getControlThrottle(), 0.01F, 0.99F, 0.0F, 0.55F) / 5F;
        this.mesh.chunkSetLocate("Z_ThrottleR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 257.2222F, 0.0F, 10F), CockpitMeteorBase.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), CockpitMeteorBase.speedometerTruScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.fm.Or.getTangage(), 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F));
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, -48F, 48F));
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), CockpitMeteorBase.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPML", this.cvt(this.fm.EI.engines[0].getRPM(), 10000F, 20000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPML2", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPMR", this.cvt(this.fm.EI.engines[1].getRPM(), 10000F, 20000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPMR2", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass2", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_GasTempL", this.cvt(this.fm.EI.engines[1].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_GasTempR", this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 95F, -150F, -25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelRemainV", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), CockpitMeteorBase.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelRemainIn", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 1000F, 0.0F, 5F), CockpitMeteorBase.fuelScale), 0.0F, 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictGear;
    private static final float speedometerScale[]    = { 0.0F, 15.5F, 76F, 153.5F, 234F, 304F, 372.5F, 440F, 504F, 566F, 630F };
    private static final float speedometerTruScale[] = { 0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 336F };
    private static final float variometerScale[]     = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
    private static final float fuelScale[]           = { 0.0F, 11F, 31F, 57F, 84F, 103.5F };

}
