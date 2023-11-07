package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitSU26SAS extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitSU26SAS.this.fm != null) {
                CockpitSU26SAS.this.setTmp = CockpitSU26SAS.this.setOld;
                CockpitSU26SAS.this.setOld = CockpitSU26SAS.this.setNew;
                CockpitSU26SAS.this.setNew = CockpitSU26SAS.this.setTmp;
                CockpitSU26SAS.this.setNew.throttle = ((10F * CockpitSU26SAS.this.setOld.throttle) + CockpitSU26SAS.this.fm.CT.PowerControl) / 11F;
                CockpitSU26SAS.this.setNew.prop = ((10F * CockpitSU26SAS.this.setOld.prop) + CockpitSU26SAS.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitSU26SAS.this.setNew.altimeter = CockpitSU26SAS.this.fm.getAltitude();
                if (Math.abs(CockpitSU26SAS.this.fm.Or.getKren()) < 30F) {
                    CockpitSU26SAS.this.setNew.azimuth.setDeg(CockpitSU26SAS.this.setOld.azimuth.getDeg(1.0F), CockpitSU26SAS.this.fm.Or.azimut());
                }
                CockpitSU26SAS.this.setNew.vspeed = ((199F * CockpitSU26SAS.this.setOld.vspeed) + CockpitSU26SAS.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitSU26SAS() {
        super("3DO/Cockpit/CockpitSU26SAS/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_02_D", "Gauges_03_D", "Gauges_04_D", "Gauges_05", "Gauges_05_D" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm.Gears.isHydroOperable()) {
            this.mesh.chunkSetAngles("shassy_R", 0.0F, 50F, 0.0F);
            this.mesh.chunkSetAngles("zAirpressa", 0.0F, 150F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("zAirpressa", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("shassy_R", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), CockpitSU26SAS.rpmScale), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.M.fuel, 72F, 360F, 0.0F, 0.0312F);
        this.mesh.chunkSetLocate("zFuelL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.fm.M.fuel, 72F, 360F, 0.0F, 0.0312F);
        this.mesh.chunkSetLocate("zFuelR", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.fm.M.fuel, 0.0F, 72F, 0.0F, 0.0312F);
        this.mesh.chunkSetLocate("zFuelC", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zGas1T", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 1000F, 0.0F, 73F), 0.0F);
        this.mesh.chunkSetAngles("zGasPres1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.6F * this.fm.EI.engines[0].getPowerOutput() : 0.0F, 0.0F, 1.0F, 0.0F, 64F), 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", 0.0F, this.cvt(1.0F + (0.005F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 272.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 80F * this.fm.EI.engines[0].getPowerOutput() * this.fm.EI.engines[0].getReadyness() : 0.0F, 0.0F, 160F, 0.0F, 272.5F), 0.0F);
        this.mesh.chunkSetAngles("richaga", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, 0.0F);
        this.mesh.chunkSetAngles("richagb", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("norm_gaz", 0.0F, this.interp(this.setNew.throttle, this.setOld.throttle, f) * 29.5F, 0.0F);
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            this.mesh.chunkSetAngles("shassy", 0.0F, 24F, 0.0F);
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            this.mesh.chunkSetAngles("shassy", 0.0F, -24F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("shassy", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                this.mesh.chunkSetAngles("shitki", 0.0F, -20F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("shitki", 0.0F, 20F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("shitki", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, this.setNew.azimuth.getDeg(f) + 90F, 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitSU26SAS.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x40) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
            this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkVisible("Z_Red1", false);
        this.mesh.chunkVisible("Z_Red2", false);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("Z_Holes3_D1", true);
            this.mesh.chunkVisible("panel_d1", true);
            this.mesh.chunkVisible("pribors4", false);
            this.mesh.chunkVisible("pribors4_d1", true);
            this.mesh.chunkVisible("zRPM1a", false);
            this.mesh.chunkVisible("zFuelPrs1a", false);
            this.mesh.chunkVisible("zFuelL", false);
            this.mesh.chunkVisible("zFuelR", false);
            this.mesh.chunkVisible("zFuelC", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0) || ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
            this.mesh.chunkVisible("pribors3", false);
            this.mesh.chunkVisible("pribors3_d1", true);
            this.mesh.chunkVisible("zClock1a", false);
            this.mesh.chunkVisible("zClock1b", false);
            this.mesh.chunkVisible("zVariometer1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("pribors5", false);
            this.mesh.chunkVisible("pribors5_d1", true);
            this.mesh.chunkVisible("zGas1T", false);
            this.mesh.chunkVisible("zOilPrs1a", false);
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
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };
    private static final float rpmScale[]         = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };

}
