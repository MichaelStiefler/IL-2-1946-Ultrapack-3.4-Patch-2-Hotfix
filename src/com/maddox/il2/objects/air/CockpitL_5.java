package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitL_5 extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      stage;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitL_5.this.fm != null) {
                CockpitL_5.this.setTmp = CockpitL_5.this.setOld;
                CockpitL_5.this.setOld = CockpitL_5.this.setNew;
                CockpitL_5.this.setNew = CockpitL_5.this.setTmp;
                CockpitL_5.this.setNew.throttle = (0.85F * CockpitL_5.this.setOld.throttle) + (CockpitL_5.this.fm.CT.PowerControl * 0.15F);
                CockpitL_5.this.setNew.prop = (0.85F * CockpitL_5.this.setOld.prop) + (CockpitL_5.this.fm.CT.getStepControl() * 0.15F);
                CockpitL_5.this.setNew.stage = (0.85F * CockpitL_5.this.setOld.stage) + (CockpitL_5.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitL_5.this.setNew.mix = (0.85F * CockpitL_5.this.setOld.mix) + (CockpitL_5.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitL_5.this.setNew.altimeter = CockpitL_5.this.fm.getAltitude();
                CockpitL_5.this.setNew.waypointAzimuth.setDeg(CockpitL_5.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitL_5.this.waypointAzimuth() - CockpitL_5.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitL_5.this.fm.Or.getKren()) < 30F) {
                    CockpitL_5.this.setNew.azimuth.setDeg(CockpitL_5.this.setOld.azimuth.getDeg(1.0F), CockpitL_5.this.fm.Or.azimut());
                }
                CockpitL_5.this.setNew.vspeed = ((199F * CockpitL_5.this.setOld.vspeed) + CockpitL_5.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitL_5() {
        super("3DO/Cockpit/Sentinel/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
//        pictGear = 0.0F;
        this.pictFlap = 0.0F;
        this.cockpitNightMats = (new String[] { "Instrumentos001", "Instrumentos002", "Instrumentos003" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Trim_Ele", 0.0F, 0.0F, 722F * this.fm.CT.getTrimElevatorControl());
        this.mesh.chunkSetAngles("Z_Pedal_D", 0.0F, 0.0F, -15F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Pedal_I", 0.0F, 0.0F, 15F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Acelerador", 0.0F, 0.0F, -70F * this.setNew.throttle);
        this.mesh.chunkSetAngles("Z_Palanca", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("z_Flap", 0.0F, 0.0F, 18F * (this.pictFlap = (0.75F * this.pictFlap) + (0.95F * this.fm.CT.FlapsControl)));
        this.resetYPRmodifier();
        this.resetYPRmodifier();
        if ((this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 3)) {
            Cockpit.xyz[1] = 0.02825F;
        }
        this.mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Magneto", 0.0F, 60F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, 3F * this.fm.EI.engines[0].getControlRadiator());
        this.mesh.chunkSetAngles("Z_Hora", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minuto", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_ASI", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitL_5.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2500F, 0.0F, 504F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 250F, 0.0F, 97.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_OilTemp", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 78F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 256F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 180F, 0.0F, 275F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 180F, 0.0F, 275F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Palo", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Bola", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F);
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
//    private float pictGear;
    private float              pictFlap;
    private static final float speedometerScale[] = { 0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 669.75F, 719F };

}
