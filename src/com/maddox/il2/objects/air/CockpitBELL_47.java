package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitBELL_47 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      mix;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitBELL_47.this.setTmp = CockpitBELL_47.this.setOld;
            CockpitBELL_47.this.setOld = CockpitBELL_47.this.setNew;
            CockpitBELL_47.this.setNew = CockpitBELL_47.this.setTmp;
            CockpitBELL_47.this.setNew.altimeter = CockpitBELL_47.this.fm.getAltitude();
            if (CockpitBELL_47.this.cockpitDimControl) {
                if (CockpitBELL_47.this.setNew.dimPosition > 0.0F) {
                    CockpitBELL_47.this.setNew.dimPosition = CockpitBELL_47.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitBELL_47.this.setNew.dimPosition < 1.0F) {
                CockpitBELL_47.this.setNew.dimPosition = CockpitBELL_47.this.setOld.dimPosition + 0.05F;
            }
            CockpitBELL_47.this.setNew.throttle = ((10F * CockpitBELL_47.this.setOld.throttle) + CockpitBELL_47.this.fm.CT.PowerControl) / 11F;
            CockpitBELL_47.this.setNew.mix = ((8F * CockpitBELL_47.this.setOld.mix) + CockpitBELL_47.this.fm.EI.engines[0].getControlMix()) / 9F;
            CockpitBELL_47.this.setNew.waypointAzimuth.setDeg(CockpitBELL_47.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitBELL_47.this.waypointAzimuth() - CockpitBELL_47.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
            if (Math.abs(CockpitBELL_47.this.fm.Or.getKren()) < 30F) {
                CockpitBELL_47.this.setNew.azimuth.setDeg(CockpitBELL_47.this.setOld.azimuth.getDeg(1.0F), CockpitBELL_47.this.fm.Or.azimut());
            }
            CockpitBELL_47.this.setNew.vspeed = ((199F * CockpitBELL_47.this.setOld.vspeed) + CockpitBELL_47.this.fm.getVertSpeed()) / 200F;
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitBELL_47() {
        super("3DO/Cockpit/Bell47/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.setNew.dimPosition = 1.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = (new String[] { "Textur9", "Textur7" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f) + this.fm.getOverload(), 0.0F, 9144F, 0.0F, -10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f) + (this.fm.getOverload() * 10F), 0.0F, 9144F, 0.0F, -1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt((Atmosphere.pressure((float) this.fm.Loc.z) - (this.fm.EI.engines[0].getRPM() * 50F)) + (this.fm.getOverload() * 2500F), 0.0F, 300000F, 0.0F, 343F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, 90F + this.setNew.azimuth.getDeg(f) + this.fm.getOverload(), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, 90F + this.setNew.waypointAzimuth.getDeg(f) + this.fm.getOverload(), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM() / 840F, 0.0F, 4F, 0.0F, -180F) + this.fm.getOverload(), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (this.fm.EI.engines[0].getRPM() / 1300F) + (this.fm.getOverload() / 10F), 0.0F, 15F, 0.0F, 180F), 0.0F);
        if (this.fm.EI.engines[0].getRPM() < 100F) {
            this.mesh.chunkSetAngles("Z_Oil1", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 180F) + (this.fm.getOverload() * 1.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM() * 1.3F, 0.0F, 3500F, 0.0F, 280F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 83.6859F, 0.0F, 13F), CockpitBELL_47.speedometerScale) + this.fm.getOverload(), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Z_Column1", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Z_PedalStrut", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut2", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut3", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut4", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Collective", 0.0F, 0.0F, this.interp(this.setNew.throttle, this.setOld.throttle, f) * 25F);
        this.mesh.chunkSetAngles("Z_CollectiveSecond", 0.0F, 0.0F, this.interp(this.setNew.throttle, this.setOld.throttle, f) * 25F);
        this.mesh.chunkSetAngles("Z_FuelGauge", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 280F, 0.0F, 310F), 0.0F);
        this.mesh.chunkSetAngles("Horizon1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0321F, -0.0321F);
        this.mesh.chunkSetLocate("Horizon", Cockpit.xyz, Cockpit.ypr);
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

    public void reflectCockpitState() {
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 17F, 56.5F, 107.5F, 157F, 204F, 220.5F, 238.5F, 256.5F, 274.5F, 293F, 311F, 330F, 342F };
}
