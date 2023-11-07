package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP149 extends CockpitPilot {
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
            if (CockpitP149.this.fm != null) {
                CockpitP149.this.setTmp = CockpitP149.this.setOld;
                CockpitP149.this.setOld = CockpitP149.this.setNew;
                CockpitP149.this.setNew = CockpitP149.this.setTmp;
                CockpitP149.this.setNew.throttle = ((10F * CockpitP149.this.setOld.throttle) + CockpitP149.this.fm.CT.PowerControl) / 11F;
                CockpitP149.this.setNew.prop = ((10F * CockpitP149.this.setOld.prop) + CockpitP149.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitP149.this.setNew.altimeter = CockpitP149.this.fm.getAltitude();
                if (Math.abs(CockpitP149.this.fm.Or.getKren()) < 30F) {
                    CockpitP149.this.setNew.azimuth.setDeg(CockpitP149.this.setOld.azimuth.getDeg(1.0F), CockpitP149.this.fm.Or.azimut());
                }
                CockpitP149.this.setNew.vspeed = ((199F * CockpitP149.this.setOld.vspeed) + CockpitP149.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitP149() {
        super("3DO/Cockpit/P149/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "Gauges_01", "Gauges_02", "Gauges_03", "Gauges_04", "Gauges_05" });
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
        this.resetYPRmodifier();
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * 0.6F;
        this.mesh.chunkSetLocate("zCanopy", Aircraft.xyz, Aircraft.ypr);
        this.mesh.chunkSetAngles("zRPM1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), CockpitP149.rpmScale), 0.0F);
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
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitP149.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x40) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
            this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
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
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 15.5F, 50F, 95.5F, 137F, 182.5F, 212F, 230F, 242F, 254.5F, 267.5F, 279F, 292F, 304F, 317F, 329.5F, 330F };
    private static final float rpmScale[]         = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };

}
