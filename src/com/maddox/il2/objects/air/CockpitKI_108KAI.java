package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitKI_108KAI extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitKI_108KAI.this.bNeedSetUp) {
                CockpitKI_108KAI.this.bNeedSetUp = false;
                CockpitKI_108KAI.this.reflectPlaneMats();
            }
            CockpitKI_108KAI.this.setTmp = CockpitKI_108KAI.this.setOld;
            CockpitKI_108KAI.this.setOld = CockpitKI_108KAI.this.setNew;
            CockpitKI_108KAI.this.setNew = CockpitKI_108KAI.this.setTmp;
            if (((CockpitKI_108KAI.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitKI_108KAI.this.setNew.ironSight < 1.0F)) {
                CockpitKI_108KAI.this.setNew.ironSight = CockpitKI_108KAI.this.setOld.ironSight + 0.0125F;
                CockpitKI_108KAI.this.setOld.ironSight = CockpitKI_108KAI.this.setNew.ironSight;
            }
            CockpitKI_108KAI.this.setNew.altimeter = CockpitKI_108KAI.this.fm.getAltitude();
            if (CockpitKI_108KAI.this.cockpitDimControl) {
                if ((CockpitKI_108KAI.this.setNew.dimPosition > 0.0F) && (CockpitKI_108KAI.this.setNew.ironSight == 0.0F)) {
                    CockpitKI_108KAI.this.setNew.dimPosition = CockpitKI_108KAI.this.setNew.dimPosition - 0.05F;
                }
            } else if ((CockpitKI_108KAI.this.setNew.dimPosition < 1.0F) && (CockpitKI_108KAI.this.setNew.ironSight == 0.0F)) {
                CockpitKI_108KAI.this.setNew.dimPosition = CockpitKI_108KAI.this.setNew.dimPosition + 0.05F;
            }
            CockpitKI_108KAI.this.setNew.throttle1 = (0.91F * CockpitKI_108KAI.this.setOld.throttle1) + (0.09F * CockpitKI_108KAI.this.fm.EI.engines[0].getControlThrottle());
            CockpitKI_108KAI.this.setNew.throttle2 = (0.91F * CockpitKI_108KAI.this.setOld.throttle2) + (0.09F * CockpitKI_108KAI.this.fm.EI.engines[1].getControlThrottle());
            CockpitKI_108KAI.this.setNew.mix1 = (0.88F * CockpitKI_108KAI.this.setOld.mix1) + (0.12F * CockpitKI_108KAI.this.fm.EI.engines[0].getControlMix());
            CockpitKI_108KAI.this.setNew.mix2 = (0.88F * CockpitKI_108KAI.this.setOld.mix2) + (0.12F * CockpitKI_108KAI.this.fm.EI.engines[1].getControlMix());
            if (Math.abs(CockpitKI_108KAI.this.fm.Or.getKren()) < 30F) {
                CockpitKI_108KAI.this.setNew.azimuth.setDeg(CockpitKI_108KAI.this.setOld.azimuth.getDeg(1.0F), -CockpitKI_108KAI.this.fm.Or.azimut() - 90F);
            }
            CockpitKI_108KAI.this.w.set(CockpitKI_108KAI.this.fm.getW());
            CockpitKI_108KAI.this.fm.Or.transform(CockpitKI_108KAI.this.w);
            CockpitKI_108KAI.this.setNew.turn = ((12F * CockpitKI_108KAI.this.setOld.turn) + CockpitKI_108KAI.this.w.z) / 13F;
            CockpitKI_108KAI.this.setNew.vspeed = ((199F * CockpitKI_108KAI.this.setOld.vspeed) + CockpitKI_108KAI.this.fm.getVertSpeed()) / 200F;
            CockpitKI_108KAI.this.setNew.manifold1 = (0.8F * CockpitKI_108KAI.this.setOld.manifold1) + (0.2F * CockpitKI_108KAI.this.fm.EI.engines[0].getManifoldPressure());
            CockpitKI_108KAI.this.setNew.manifold2 = (0.8F * CockpitKI_108KAI.this.setOld.manifold2) + (0.2F * CockpitKI_108KAI.this.fm.EI.engines[1].getManifoldPressure());
            float f = 30F;
            for (int i = 0; i < 2; i++) {
                if ((CockpitKI_108KAI.this.cowlFlapsLever[i] != 0.0F) && (CockpitKI_108KAI.this.cowlFlaps[i] == CockpitKI_108KAI.this.fm.EI.engines[i].getControlRadiator())) {
                    CockpitKI_108KAI.this.cowlFlapsLever[i] = CockpitKI_108KAI.this.cowlFlapsLever[i] * 0.8F;
                    if (Math.abs(CockpitKI_108KAI.this.cowlFlapsLever[i]) < 0.1F) {
                        CockpitKI_108KAI.this.cowlFlapsLever[i] = 0.0F;
                    }
                } else if (CockpitKI_108KAI.this.cowlFlaps[i] < CockpitKI_108KAI.this.fm.EI.engines[i].getControlRadiator()) {
                    CockpitKI_108KAI.this.cowlFlaps[i] = CockpitKI_108KAI.this.fm.EI.engines[i].getControlRadiator();
                    CockpitKI_108KAI.this.cowlFlapsLever[i] = CockpitKI_108KAI.this.cowlFlapsLever[i] + 2.0F;
                    if (CockpitKI_108KAI.this.cowlFlapsLever[i] > f) {
                        CockpitKI_108KAI.this.cowlFlapsLever[i] = f;
                    }
                } else if (CockpitKI_108KAI.this.cowlFlaps[i] > CockpitKI_108KAI.this.fm.EI.engines[i].getControlRadiator()) {
                    CockpitKI_108KAI.this.cowlFlaps[i] = CockpitKI_108KAI.this.fm.EI.engines[i].getControlRadiator();
                    CockpitKI_108KAI.this.cowlFlapsLever[i] = CockpitKI_108KAI.this.cowlFlapsLever[i] - 2.0F;
                    if (CockpitKI_108KAI.this.cowlFlapsLever[i] < -f) {
                        CockpitKI_108KAI.this.cowlFlapsLever[i] = -f;
                    }
                }
            }

            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float         altimeter;
        float         throttle1;
        float         throttle2;
        float         dimPosition;
        float         ironSight;
        AnglesFork    azimuth;
        float         turn;
        float         mix1;
        float         mix2;
        float         vspeed;
        private float manifold1;
        private float manifold2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.manifold1 = 0.0F;
            this.manifold2 = 0.0F;
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("RGlassBlister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("LGlassBlister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Nose_D0", false);
            this.aircraft().hierMesh().chunkVisible("Nose_D1", false);
            this.aircraft().hierMesh().chunkVisible("Nose_D2", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        this.aircraft().hierMesh().chunkVisible("RGlassBlister1_D0", true);
        this.aircraft().hierMesh().chunkVisible("LGlassBlister1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Nose_D0", true);
        super.doFocusLeave();
    }

    public CockpitKI_108KAI() {
        super("3DO/Cockpit/Ki-108KAI-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.pictFlaps = 0.0F;
        this.bNeedSetUp = true;
        this.w = new Vector3f();
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = (new String[] { "Pressure_Gauges", "Needs_Instr", "maininstrumentspanel4", "instructions", "instru_main_pan", "instru_main_pan2", "instru_main_pan3", "dmaininstrumentspanel4", "dinstru_main_pan", "dinstru_main_pan2", "dinstru_main_pan3" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.bNeedSetUp = false;
            this.reflectPlaneMats();
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_RCanopy", 0.0F, this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("Z_GS_Sun_Shader", 0.0F, (this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f) * 90F) - 90F, 0.0F);
        this.mesh.chunkSetAngles("Z_GS_IronSight", 0.0F, (90F * this.setNew.ironSight) - 90F, 0.0F);
        this.mesh.chunkVisible("X_LGearDown", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("X_RGearDown", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("X_LGearUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("X_RGearUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("X_TailGearDown", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("X_TailGearUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkSetAngles("Z_CColumn_Elev", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, 0.0F);
        this.mesh.chunkSetAngles("Z_CColumn", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[1] = -0.002545F;
        }
        this.mesh.chunkSetLocate("Z_Trig1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[1] = -0.002545F;
        }
        this.mesh.chunkSetLocate("Z_Trig2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[3]) {
            Cockpit.xyz[1] = -0.002545F;
        }
        this.mesh.chunkSetLocate("Z_Bmb", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_Rudder_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_Rudder_R", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Throttle_L", 0.0F, -(this.interp(this.setNew.throttle1, this.setOld.throttle1, f) * 60F) + 30F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle_R", 0.0F, -(this.interp(this.setNew.throttle2, this.setOld.throttle2, f) * 60F) + 30F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture_L", 0.0F, this.cvt(this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 1.2F, 48F, -42F), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture_R", 0.0F, this.cvt(this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 1.2F, 48F, -42F), 0.0F);
        this.mesh.chunkSetAngles("Z_Propitch_L", 0.0F, -(this.fm.EI.engines[0].getControlProp() * 78F) + 39F, 0.0F);
        this.mesh.chunkSetAngles("Z_Propitch_R", 0.0F, -(this.fm.EI.engines[1].getControlProp() * 78F) + 39F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilcooler_L", 0.0F, -(this.fm.EI.engines[0].getControlRadiator() * 58F) + 29F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilcooler_R", 0.0F, -(this.fm.EI.engines[1].getControlRadiator() * 58F) + 29F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_ClockHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_ClockMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Turn", 0.0F, this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 21F, -21F), 0.0F);
        float f1 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_Need_Bank", 0.0F, this.cvt(f1, -4F, 4F, -13F, 13F), 0.0F);
        this.mesh.chunkSetAngles("Z_NeedAHcyl", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_NeedAHBar", 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 30F, -30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Climb", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 12F), CockpitKI_108KAI.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_AirSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), CockpitKI_108KAI.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_LTachio", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 38.8F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RTachio", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3500F, 0.0F, 38.8F), 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps_L", 0.0F, this.cowlFlapsLever[0] + 24F, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps_R", 0.0F, this.cowlFlapsLever[1] + 24F, 0.0F);
        int i = 0;
        if (this.fm.EI.getCurControl(1) && !this.fm.EI.getCurControl(0)) {
            i = 1;
        }
        this.mesh.chunkSetAngles("Z_cyltempsel", 0.0F, i == 0 ? 0.0F : 54F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_cyltemp", 0.0F, this.cvt(this.fm.EI.engines[i].tWaterOut, 0.0F, 360F, 0.0F, 56F), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps", 0.0F, 41F * (this.pictFlaps = (0.8F * this.pictFlaps) + (0.2F * this.fm.CT.FlapsControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_Gears", 0.0F, 48F * (this.pictGear = (0.8F * this.pictGear) + (0.2F * this.fm.CT.GearControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_alt_km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Compass", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_FuelQuant", 0.0F, this.cvt(this.fm.M.fuel / this.fm.M.maxFuel, 0.0F, 1.0F, 5F, 335F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_L_oilpress", 0.0F, this.cvt(60F + (this.fm.EI.engines[0].tOilIn * 0.222F), 0.0F, 100F, 0.0F, 209F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_R_oilpress", 0.0F, this.cvt(60F + (this.fm.EI.engines[1].tOilIn * 0.222F), 0.0F, 100F, 0.0F, -212F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_L_oiltemp", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 130F, 0.0F, 57F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_R_oiltemp", 0.0F, this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 130F, 0.0F, 57F), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger_L", 0.0F, (-40F * this.fm.EI.engines[0].getControlCompressor()) + 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger_R", 0.0F, (-40F * this.fm.EI.engines[1].getControlCompressor()) + 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_L_Manifold", 0.0F, 180F + this.cvt(this.setNew.manifold1, 0.200068F, 1.799932F, -164F, 164F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_R_Manifold", 0.0F, 180F + this.cvt(this.setNew.manifold2, 0.200068F, 1.799932F, -164F, 164F), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim_Elev", 0.0F, 180F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim_Rudder", 0.0F, 180F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.resetYPRmodifier();
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.06F * this.fm.CT.getFlap();
        this.mesh.chunkSetLocate("Z_FlapsBuffer", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = 0.05F * this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 1.0F);
        this.mesh.chunkSetLocate("Z_Need_ExhGasAnalys1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = 0.05F * this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 324F, 0.0F, 1.0F);
        this.mesh.chunkSetLocate("Z_Need_ExhGasAnalys2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("X_LGearDown", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("X_RGearDown", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("X_LGearUp", (this.fm.CT.getGear() < 0.01F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("X_RGearUp", (this.fm.CT.getGear() < 0.01F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("X_TailGearDown", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("X_TailGearUp", (this.fm.CT.getGear() < 0.01F) && this.fm.Gears.cgear);
        this.mesh.chunkSetAngles("Z_Magn_L", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, -20F, 20F), 0.0F);
        this.mesh.chunkSetAngles("Z_Magn_R", 0.0F, this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, -20F, 20F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_L_fuelpress", 0.0F, this.fm.M.fuel > 1.0F ? 129F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_R_fuelpress", 0.0F, this.fm.M.fuel > 1.0F ? -129F : 0.0F, 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
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
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("D_GS_Glass", true);
            this.mesh.chunkVisible("GS_Glass", false);
            this.mesh.chunkVisible("reticle", false);
            this.mesh.chunkVisible("reticlemask", false);
            this.mesh.chunkVisible("X_GHole01", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("X_GHole01", true);
            this.mesh.chunkVisible("X_Bullethole03", true);
            this.mesh.chunkVisible("DInstr1", true);
            this.mesh.chunkVisible("Z_Need_AirSpeed", false);
            this.mesh.chunkVisible("Z_Need_Bank", false);
            this.mesh.chunkVisible("Z_Need_Turn", false);
            this.mesh.chunkVisible("Z_Need_Climb", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("X_GHole03", true);
            this.mesh.chunkVisible("X_Bullethole01", true);
            this.mesh.chunkVisible("DInstr2", true);
            this.mesh.chunkVisible("Instr2", false);
            this.mesh.chunkVisible("Z_Need_L_Manifold", false);
            this.mesh.chunkVisible("Z_Need_alt_km", false);
            this.mesh.chunkVisible("Z_Need_L_oiltemp", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("X_GHole05", true);
            this.mesh.chunkVisible("X_GHole04", true);
            this.mesh.chunkVisible("X_Bullethole04", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("X_GHole06", true);
            this.mesh.chunkVisible("X_GHole02", true);
            this.mesh.chunkVisible("X_Bullethole02", true);
            this.mesh.chunkVisible("DInstr4", true);
            this.mesh.chunkVisible("Instr4", false);
            this.mesh.chunkVisible("Z_Need_cyltemp", false);
            this.mesh.chunkVisible("Z_Need_L_fuelpress", false);
            this.mesh.chunkVisible("Z_Need_R_fuelpress", false);
            this.mesh.chunkVisible("Z_Need_ClockHour", false);
            this.mesh.chunkVisible("Z_Need_ClockMinute", false);
            this.mesh.chunkVisible("Z_Need_Engines_Sync", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("X_GHole02", true);
            this.mesh.chunkVisible("X_GHole04", true);
            this.mesh.chunkVisible("DInstr3", true);
            this.mesh.chunkVisible("Instr3", false);
            this.mesh.chunkVisible("Z_Need_ExhGasAnalys1", false);
            this.mesh.chunkVisible("Z_Need_ExhGasAnalys2", false);
            this.mesh.chunkVisible("Z_Need_RTachio", false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private float              pictGear;
    private float              pictFlaps;
    public Vector3f            w;
    private float              cowlFlapsLever[]   = { 0.0F, 0.0F };
    private float              cowlFlaps[]        = { 0.0F, 0.0F };
    private static final float speedometerScale[] = { 0.0F, 8.6F, 23.6F, 64.2F, 114.5F, 172.8F, 239.4F, 299F, 360F, 417F, 479F, 533F, 582F, 627F, 657F };
    private static final float variometerScale[]  = { -180F, -162F, -143F, -125F, -108F, -90F, 0.0F, 90F, 108F, 125F, 143F, 162F, 180F };

    static {
        Property.set(CockpitKI_108KAI.class, "normZNs", new float[] { 0.7F, 0.7F, 0.8F, 0.7F });
    }

}
