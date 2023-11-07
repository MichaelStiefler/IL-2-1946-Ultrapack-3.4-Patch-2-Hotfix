package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitHO4S extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      stage;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        AnglesFork waypointAzimuth;
        float      radioalt;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHO4S.this.fm != null) {
                CockpitHO4S.this.setTmp = CockpitHO4S.this.setOld;
                CockpitHO4S.this.setOld = CockpitHO4S.this.setNew;
                CockpitHO4S.this.setNew = CockpitHO4S.this.setTmp;
                CockpitHO4S.this.setNew.throttle = (0.85F * CockpitHO4S.this.setOld.throttle) + (CockpitHO4S.this.fm.CT.PowerControl * 0.15F);
                CockpitHO4S.this.setNew.prop = (0.85F * CockpitHO4S.this.setOld.prop) + (CockpitHO4S.this.fm.CT.getStepControl() * 0.15F);
                CockpitHO4S.this.setNew.stage = (0.85F * CockpitHO4S.this.setOld.stage) + (CockpitHO4S.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitHO4S.this.setNew.mix = (0.85F * CockpitHO4S.this.setOld.mix) + (CockpitHO4S.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitHO4S.this.setNew.altimeter = CockpitHO4S.this.fm.getAltitude();
                CockpitHO4S.this.setNew.waypointAzimuth.setDeg(CockpitHO4S.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitHO4S.this.waypointAzimuth() - CockpitHO4S.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitHO4S.this.fm.Or.getKren()) < 30F) {
                    CockpitHO4S.this.setNew.azimuth.setDeg(CockpitHO4S.this.setOld.azimuth.getDeg(1.0F), CockpitHO4S.this.fm.Or.azimut());
                }
                CockpitHO4S.this.setNew.vspeed = ((199F * CockpitHO4S.this.setOld.vspeed) + CockpitHO4S.this.fm.getVertSpeed()) / 200F;
                Variables variables = CockpitHO4S.this.setNew;
                float f = 0.5F * CockpitHO4S.this.setOld.radioalt;
                float f1 = 0.5F;
                float f2 = CockpitHO4S.this.fm.getAltitude();
                World.cur();
                World.land();
                variables.radioalt = f + (f1 * (f2 - Landscape.HQ_Air((float) CockpitHO4S.this.fm.Loc.x, (float) CockpitHO4S.this.fm.Loc.y)));
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitHO4S() {
        super("3DO/Cockpit/HO4S/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.rotorrpm = 0;
//        pictGear = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "Instrumentos001", "Instrumentos002", "Instrumentos003" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Colectivo", 0.0F, 0.0F, -30F * this.setNew.throttle);
        this.mesh.chunkSetAngles("Z_Gases", 0.0F, 200F * this.setNew.throttle, 0.0F);
        this.mesh.chunkSetAngles("Z_Gases2", 0.0F, 200F * this.setNew.throttle, 0.0F);
        this.mesh.chunkSetAngles("Z_Palanca", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * -10F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * -10F);
        this.mesh.chunkSetAngles("Z_Palanca2", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * -10F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * -10F);
        this.mesh.chunkSetAngles("Z_Pedal_D", 0.0F, 0.0F, -15F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Pedal_I", 0.0F, 0.0F, 15F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Radiador", 0.0F, 0.0F, -60F * this.fm.EI.engines[0].getControlRadiator());
        if (this.fm.CT.PowerControl <= 0.11D) {
            this.mesh.chunkSetAngles("Z_clutch", 0.0F, 0.0F, 700F * this.fm.CT.PowerControl);
        }
        this.mesh.chunkSetAngles("Z_Magneto", 0.0F, 20F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkVisible("A_Fuel1", this.fm.M.fuel < 52F);
        this.mesh.chunkVisible("A_Fuel2", this.fm.M.fuel < 26F);
        this.resetYPRmodifier();
        if ((this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 3)) {
            Cockpit.xyz[1] = 0.01F;
        }
        this.mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("Z_Starter2", (this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 3));
        this.mesh.chunkVisible("A_EngFire", this.fm.AS.astateEngineStates[0] > 2);
        if (this.fm.CT.bHasArrestorControl) {
            this.mesh.chunkVisible("Z_Flotadores_A", true);
            this.mesh.chunkVisible("Z_Flotadores_D", false);
            this.mesh.chunkVisible("A_Foats_A", true);
        }
        this.mesh.chunkVisible("A_Foats_R", this.fm.CT.getArrestor() > 0.05F);
        this.mesh.chunkSetAngles("Z_Altimetro2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro4", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimetro3", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_ASI1", 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitHO4S.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_ASI2", 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitHO4S.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Horizonte1", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizonte2", 0.0F, 0.0F, -this.fm.Or.getTangage());
        this.mesh.chunkSetAngles("Z_Horizonte3", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Horizonte4", 0.0F, 0.0F, -this.fm.Or.getTangage());
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -90F + -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, -90F + -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, -90F + -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", 0.0F, -90F + -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass5", 0.0F, -90F + -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass6", 0.0F, -90F + -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Variometro1", 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitHO4S.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Variometro2", 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitHO4S.variometerScale), 0.0F);
        this.rotorrpm = Math.abs((int) ((this.fm.EI.engines[0].getw() * 1.0F) + (this.fm.Vwld.length() * 4D)));
        if (this.rotorrpm >= 250) {
            this.rotorrpm = 250;
        }
        if (this.rotorrpm <= 40) {
            this.rotorrpm = 0;
        }
        this.mesh.chunkSetAngles("Z_RotorRPM", 0.0F, -this.rotorrpm, 0.0F);
        this.mesh.chunkSetAngles("Z_EngineRPM", 0.0F, -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 504F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 210F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 120F, 0.0F, 250F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 256F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, -this.cvt(this.fm.M.fuel <= 5F ? 0.0F : 0.78F, 0.0F, 1.0F, 0.0F, 255F), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hora", 0.0F, -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minuto", 0.0F, -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 180F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, -this.cvt(this.fm.M.fuel, 0.0F, 180F, 0.0F, 110F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Palo", 0.0F, -this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Bola", 0.0F, -this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F);
        this.mesh.chunkSetAngles("Z_RadioAlt", 0.0F, this.cvt(this.interp(this.setNew.radioalt, this.setOld.radioalt, f), 6.27F, 306.27F, 0.0F, -330F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private int                rotorrpm;
//    private float pictGear;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 15.5F, 77F, 175F, 275F, 360F, 412F, 471F, 539F, 610.5F, 669.75F, 719F };
    private static final float variometerScale[]  = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };

}
