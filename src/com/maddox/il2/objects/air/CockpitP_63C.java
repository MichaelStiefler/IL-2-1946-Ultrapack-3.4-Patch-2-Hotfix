package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_63C extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_63C.this.fm != null) {
                CockpitP_63C.this.setTmp = CockpitP_63C.this.setOld;
                CockpitP_63C.this.setOld = CockpitP_63C.this.setNew;
                CockpitP_63C.this.setNew = CockpitP_63C.this.setTmp;
                CockpitP_63C.this.setNew.throttle = ((10F * CockpitP_63C.this.setOld.throttle) + CockpitP_63C.this.fm.CT.PowerControl) / 11F;
                CockpitP_63C.this.setNew.mix = ((10F * CockpitP_63C.this.setOld.mix) + CockpitP_63C.this.fm.EI.engines[0].getControlMix()) / 11F;
                CockpitP_63C.this.setNew.altimeter = CockpitP_63C.this.fm.getAltitude();
                if (Math.abs(CockpitP_63C.this.fm.Or.getKren()) < 45F) {
                    CockpitP_63C.this.setNew.azimuthMag = ((349F * CockpitP_63C.this.setOld.azimuthMag) + CockpitP_63C.this.fm.Or.azimut()) / 350F;
                }
                if ((CockpitP_63C.this.setOld.azimuthMag > 270F) && (CockpitP_63C.this.setNew.azimuthMag < 90F)) {
                    CockpitP_63C.this.setOld.azimuthMag -= 360F;
                }
                if ((CockpitP_63C.this.setOld.azimuthMag < 90F) && (CockpitP_63C.this.setNew.azimuthMag > 270F)) {
                    CockpitP_63C.this.setOld.azimuthMag += 360F;
                }
                CockpitP_63C.this.setNew.waypointAzimuth.setDeg(CockpitP_63C.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitP_63C.this.waypointAzimuth() - CockpitP_63C.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitP_63C.this.fm.Or.getKren()) < 30F) {
                    CockpitP_63C.this.setNew.azimuth.setDeg(CockpitP_63C.this.setOld.azimuth.getDeg(1.0F), CockpitP_63C.this.fm.Or.azimut());
                }
                int i = CockpitP_63C.this.fm.EI.engines[0].getControlMagnetos();
                if ((CockpitP_63C.this.oldMag == 0) && (i != 0)) {
                    CockpitP_63C.this.tOldMag = Time.current() + 10000L;
                }
                CockpitP_63C.this.oldMag = i;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      mix;
        float      altimeter;
        AnglesFork azimuth;
        float      azimuthMag;
        float      vspeed;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    public CockpitP_63C() {
        super("3DO/Cockpit/P-63C/hier.him", "p39");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.oldMag = 0;
        this.tOldMag = -1L;
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(0.955D, 0.0D, 0.598D));
        this.light1.light.setColor(232F, 75F, 44F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("PedalBaseLeft", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("PedalBaseRight", 0.0F, 0.0F, 20F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("PedalLeft", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalRight", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = -0.009F;
        }
        this.mesh.chunkSetLocate("PriTrigger", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Throttle", 0.0F, 0.0F, -45.5F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("Mixture", 0.0F, 0.0F, -50F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("IgnitionSW", -110F * this.oldMag * 0.333333F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SWCLight", 0.0F, 0.0F, this.cockpitLightControl ? 40F : 0.0F);
        this.mesh.chunkSetAngles("SWGear", 0.0F, 0.0F, this.fm.CT.GearControl <= 0.5F ? 0.0F : 40F);
        this.mesh.chunkSetAngles("SWLandLight", 0.0F, 0.0F, this.fm.AS.bLandingLightOn ? 40F : 0.0F);
        this.mesh.chunkSetAngles("SWNavTail", 0.0F, 0.0F, this.fm.AS.bNavLightsOn ? 20F : 0.0F);
        this.mesh.chunkSetAngles("SWNavWing", 0.0F, 0.0F, this.fm.AS.bNavLightsOn ? 20F : 0.0F);
        this.mesh.chunkSetAngles("SWStarter", 0.0F, 0.0F, 0.0F);
        if ((this.tOldMag > 0L) && (Time.current() < this.tOldMag)) {
            this.mesh.chunkSetAngles("SWStarter", 0.0F, 0.0F, 40F);
        }
        this.mesh.chunkSetAngles("Alt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Alt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurnInd", this.interp(this.setNew.azimuthMag, this.setOld.azimuthMag, f), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 2) == 0) {
            this.mesh.chunkSetAngles("FltInd", this.fm.Or.getKren(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("FltIndMesh", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        }
        this.mesh.chunkSetAngles("SettingStrelki", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CompassStrelka", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("AirSpeed", this.floatindex(this.cvt(0.6213711F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 500F, 0.0F, 10F), CockpitP_63C.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Climb", this.setNew.vspeed < 0.0F ? -this.floatindex(this.cvt(-this.setNew.vspeed / 5.08F, 0.0F, 6F, 0.0F, 12F), CockpitP_63C.variometerScale) : this.floatindex(this.cvt(this.setNew.vspeed / 5.08F, 0.0F, 6F, 0.0F, 12F), CockpitP_63C.variometerScale), 0.0F, 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -3F, 3F, -30F, 30F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("TurnStrelka", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Hodilka", this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TBReVi", this.cvt(this.getBall(4D), -4F, 4F, 13F, -13F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ManPress", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386F, 2.5398F, 0.0F, 345F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CoolantTemp", this.cvt(this.fm.EI.engines[0].tOilIn, 40F, 160F, 0.0F, 116.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Engine", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Oil", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 17.2369F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Fuel", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 1.72369F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Liquid1", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 454.2493F, 0.0F, 97F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Liquid2", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 454.2493F, 0.0F, 97F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RPMStrelka", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 280F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Hour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Min", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("OilPress", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 20.68428F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SuperOilPress", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (5F * this.fm.EI.engines[0].getManifoldPressure()), 0.0F, 40F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CarbAir", this.cvt((Atmosphere.temperature((float) this.fm.Loc.z - 278F) + this.fm.EI.engines[0].tOilIn) / 2.0F, -50F, 50F, -52F, 52F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SuctStrelka", this.cvt((this.fm.EI.engines[0].getw() / 57F) * Atmosphere.density((float) this.fm.Loc.z), 0.0F, 12F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGearUp", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("XLampGearDn", this.fm.CT.getGear() > 0.99F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Kollimator", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("DKollimator", true);
            this.mesh.chunkVisible("Z_Z_MASK_D", true);
            this.mesh.chunkVisible("Z_Z_RETICLE_D", true);
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.chunkVisible("XHullHoles4", true);
            this.mesh.materialReplace("APanelUp", "DPanelUp");
            this.mesh.chunkVisible("TBReVi", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("GunSightN9", false);
            this.mesh.chunkVisible("DGunSightN9", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlAssHoles3", true);
            this.mesh.chunkVisible("XGlAssHoles4", true);
            this.mesh.chunkVisible("XGlAssHoles5", true);
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.materialReplace("APanelUp", "DPanelUp");
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.chunkVisible("XHullHoles6", true);
            this.mesh.materialReplace("GagePanel1", "DGagePanel1");
            this.mesh.chunkVisible("Climb", false);
            this.mesh.chunkVisible("ManPress", false);
            this.mesh.chunkVisible("Engine", false);
            this.mesh.chunkVisible("Oil", false);
            this.mesh.chunkVisible("Fuel", false);
            this.mesh.chunkVisible("RPMStrelka", false);
            this.mesh.materialReplace("GagePanel2", "DGagePanel2");
            this.mesh.chunkVisible("AirSpeed", false);
            this.mesh.chunkVisible("TurnStrelka", false);
            this.mesh.chunkVisible("Hodilka", false);
            this.mesh.chunkVisible("OilPress", false);
            this.mesh.chunkVisible("SuperOilPress", false);
            this.mesh.chunkVisible("CarbAir", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlAssHoles1", true);
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.chunkVisible("XHullHoles2", true);
            this.mesh.chunkVisible("XHullHoles4", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlAssHoles3", true);
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.chunkVisible("XHullHoles3", true);
            this.mesh.materialReplace("GagePanel3", "DGagePanel3");
            this.mesh.chunkVisible("Alt1", false);
            this.mesh.chunkVisible("Alt2", false);
            this.mesh.chunkVisible("CoolantTemp", false);
            this.mesh.chunkVisible("Liquid1", false);
            this.mesh.chunkVisible("Liquid2", false);
            this.mesh.chunkVisible("SuctStrelka", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0) {

        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlAssHoles2", true);
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.chunkVisible("XHullHoles2", true);
            this.mesh.chunkVisible("XHullHoles6", true);
            this.mesh.chunkVisible("RDoorHandle", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlAssHoles3", true);
            this.mesh.chunkVisible("XHullHoles1", true);
            this.mesh.chunkVisible("XHullHoles4", true);
            this.mesh.chunkVisible("XHullHoles5", true);
            this.mesh.materialReplace("GagePanel4", "DGagePanel4");
            this.mesh.chunkVisible("TurnInd", false);
            this.mesh.chunkVisible("CompassStrelka", false);
            this.mesh.chunkVisible("SettingStrelki", false);
            this.mesh.chunkVisible("Hour", false);
            this.mesh.chunkVisible("Min", false);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.005F, 1.0F);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private float              pictAiler;
    private float              pictElev;
    private int                oldMag;
    private long               tOldMag;
    private static final float speedometerScale[] = { 0.0F, 17F, 80.5F, 143.5F, 205F, 227F, 249F, 271.5F, 294F, 317F, 339.5F, 341.5F };
    private static final float variometerScale[]  = { 0.0F, 25F, 49.5F, 64F, 78.5F, 89.5F, 101F, 112F, 123F, 134.5F, 145.5F, 157F, 168F, 168F };

}
