package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class CockpitHalifaxBMkII extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHalifaxBMkII.this.fm != null) {
                CockpitHalifaxBMkII.this.setTmp = CockpitHalifaxBMkII.this.setOld;
                CockpitHalifaxBMkII.this.setOld = CockpitHalifaxBMkII.this.setNew;
                CockpitHalifaxBMkII.this.setNew = CockpitHalifaxBMkII.this.setTmp;
                CockpitHalifaxBMkII.this.setNew.throttle1 = (0.85F * CockpitHalifaxBMkII.this.setOld.throttle1) + (CockpitHalifaxBMkII.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.prop1 = (0.85F * CockpitHalifaxBMkII.this.setOld.prop1) + (CockpitHalifaxBMkII.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.throttle2 = (0.85F * CockpitHalifaxBMkII.this.setOld.throttle2) + (CockpitHalifaxBMkII.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.prop2 = (0.85F * CockpitHalifaxBMkII.this.setOld.prop2) + (CockpitHalifaxBMkII.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.throttle3 = (0.85F * CockpitHalifaxBMkII.this.setOld.throttle3) + (CockpitHalifaxBMkII.this.fm.EI.engines[2].getControlThrottle() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.prop3 = (0.85F * CockpitHalifaxBMkII.this.setOld.prop3) + (CockpitHalifaxBMkII.this.fm.EI.engines[3].getControlProp() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.throttle4 = (0.85F * CockpitHalifaxBMkII.this.setOld.throttle4) + (CockpitHalifaxBMkII.this.fm.EI.engines[3].getControlThrottle() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.prop4 = (0.85F * CockpitHalifaxBMkII.this.setOld.prop4) + (CockpitHalifaxBMkII.this.fm.EI.engines[3].getControlProp() * 0.15F);
                CockpitHalifaxBMkII.this.setNew.altimeter = CockpitHalifaxBMkII.this.fm.getAltitude();
                CockpitHalifaxBMkII.this.setNew.waypointAzimuth.setDeg(CockpitHalifaxBMkII.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitHalifaxBMkII.this.waypointAzimuth() - CockpitHalifaxBMkII.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitHalifaxBMkII.this.fm.Or.getKren()) < 30F) {
                    CockpitHalifaxBMkII.this.setNew.azimuth.setDeg(CockpitHalifaxBMkII.this.setOld.azimuth.getDeg(1.0F), CockpitHalifaxBMkII.this.fm.Or.azimut());
                }
                CockpitHalifaxBMkII.this.setNew.vspeed = (0.99F * CockpitHalifaxBMkII.this.setOld.vspeed) + (0.01F * CockpitHalifaxBMkII.this.fm.getVertSpeed());
                switch (CockpitHalifaxBMkII.this.iWiper) {
                    default:
                        break;

                    case 0:
                        if ((Mission.curCloudsType() > 4) && (CockpitHalifaxBMkII.this.fm.getSpeedKMH() < 220F) && (CockpitHalifaxBMkII.this.fm.getAltitude() < (Mission.curCloudsHeight() + 300F))) {
                            CockpitHalifaxBMkII.this.iWiper = 1;
                        }
                        break;

                    case 1:
                        CockpitHalifaxBMkII.this.setNew.wiper = CockpitHalifaxBMkII.this.setOld.wiper - 0.05F;
                        if (CockpitHalifaxBMkII.this.setNew.wiper < -1.03F) {
                            CockpitHalifaxBMkII.this.iWiper++;
                        }
                        if (CockpitHalifaxBMkII.this.wiState >= 2) {
                            break;
                        }
                        if (CockpitHalifaxBMkII.this.wiState == 0) {
                            if (CockpitHalifaxBMkII.this.fxw == null) {
                                CockpitHalifaxBMkII.this.fxw = CockpitHalifaxBMkII.this.aircraft().newSound("aircraft.wiper", false);
                                if (CockpitHalifaxBMkII.this.fxw != null) {
                                    CockpitHalifaxBMkII.this.fxw.setParent(CockpitHalifaxBMkII.this.aircraft().getRootFX());
                                    CockpitHalifaxBMkII.this.fxw.setPosition(CockpitHalifaxBMkII.this.sfxPos);
                                }
                            }
                            if (CockpitHalifaxBMkII.this.fxw != null) {
                                CockpitHalifaxBMkII.this.fxw.play(CockpitHalifaxBMkII.this.wiStart);
                            }
                        }
                        if (CockpitHalifaxBMkII.this.fxw != null) {
                            CockpitHalifaxBMkII.this.fxw.play(CockpitHalifaxBMkII.this.wiRun);
                            CockpitHalifaxBMkII.this.wiState = 2;
                        }
                        break;

                    case 2:
                        CockpitHalifaxBMkII.this.setNew.wiper = CockpitHalifaxBMkII.this.setOld.wiper + 0.05F;
                        if (CockpitHalifaxBMkII.this.setNew.wiper > 1.03F) {
                            CockpitHalifaxBMkII.this.iWiper++;
                        }
                        if (CockpitHalifaxBMkII.this.wiState > 1) {
                            CockpitHalifaxBMkII.this.wiState = 1;
                        }
                        break;

                    case 3:
                        CockpitHalifaxBMkII.this.setNew.wiper = CockpitHalifaxBMkII.this.setOld.wiper - 0.05F;
                        if (CockpitHalifaxBMkII.this.setNew.wiper >= 0.02F) {
                            break;
                        }
                        if ((CockpitHalifaxBMkII.this.fm.getSpeedKMH() > 250F) || (CockpitHalifaxBMkII.this.fm.getAltitude() > (Mission.curCloudsHeight() + 400F))) {
                            CockpitHalifaxBMkII.this.iWiper++;
                        } else {
                            CockpitHalifaxBMkII.this.iWiper = 1;
                        }
                        break;

                    case 4:
                        CockpitHalifaxBMkII.this.setNew.wiper = CockpitHalifaxBMkII.this.setOld.wiper;
                        CockpitHalifaxBMkII.this.iWiper = 0;
                        CockpitHalifaxBMkII.this.wiState = 0;
                        if (CockpitHalifaxBMkII.this.fxw != null) {
                            CockpitHalifaxBMkII.this.fxw.cancel();
                        }
                        break;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      throttle3;
        float      throttle4;
        float      prop3;
        float      prop4;
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      wiper;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitHalifaxBMkII() {
        super("3DO/Cockpit/HalifaxBMkII/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictBrake = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictBbay = 0.0F;
        this.setPictSupc(0.0F);
        this.setPictLlit(0.0F);
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.pictManf3 = 1.0F;
        this.pictManf4 = 1.0F;
        this.bNeedSetUp = true;
        this.iWiper = 0;
        this.fxw = null;
        this.wiStart = new Sample("wip_002_s.wav", 256, 65535);
        this.wiRun = new Sample("wip_002.wav", 256, 65535);
        this.wiState = 0;
        this.cockpitNightMats = new String[] { "01", "02", "03", "04", "05", "12", "20", "23", "24", "26", "27", "01_damage", "03_damage", "04_damage", "24_damage" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Wiper1", this.cvt(this.interp(this.setNew.wiper, this.setOld.wiper, f), -1F, 1.0F, -61F, 61F), 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("XLampFuel1", this.fm.EI.engines[0].getRPM() < 300F);
        this.mesh.chunkVisible("XLampFuel2", this.fm.EI.engines[1].getRPM() < 300F);
        this.mesh.chunkSetAngles("Z_Columnbase", 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 45F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ColumnSwitch", 20F * (this.pictBrake = (0.91F * this.pictBrake) + (0.09F * this.fm.CT.BrakeControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 50F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 50F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle3", 50F * this.interp(this.setNew.throttle3, this.setOld.throttle3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle4", 50F * this.interp(this.setNew.throttle4, this.setOld.throttle4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", -55F * (this.pictGear = (0.9F * this.pictGear) + (0.1F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        float f1;
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                f1 = -0.0299F;
            } else {
                f1 = 0.0F;
            }
        } else {
            f1 = -0.0144F;
        }
        this.pictFlap = (0.8F * this.pictFlap) + (0.2F * f1);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.pictFlap;
        this.mesh.chunkSetLocate("Z_Flaps1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", -1000F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 1000F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 90F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 90F * this.setNew.prop1, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, 90F * this.setNew.prop2, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop3", 0.0F, 90F * this.setNew.prop3, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop4", 0.0F, 90F * this.setNew.prop4, 0.0F);
        this.mesh.chunkSetAngles("Z_BombBay1", 70F * (this.pictBbay = (0.9F * this.pictBbay) + (0.1F * this.fm.CT.BayDoorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("COMPASS_M", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_LONG", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_SHORT", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F, 0.0F);
        this.pictManf1 = (0.91F * this.pictManf1) + (0.09F * this.fm.EI.engines[0].getManifoldPressure());
        f1 = this.pictManf1 - 1.0F;
        float f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST1", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitHalifaxBMkII.boostScale), 0.0F, 0.0F);
        this.pictManf2 = (0.91F * this.pictManf2) + (0.09F * this.fm.EI.engines[1].getManifoldPressure());
        f1 = this.pictManf2 - 1.0F;
        f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST2", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitHalifaxBMkII.boostScale), 0.0F, 0.0F);
        this.pictManf3 = (0.91F * this.pictManf3) + (0.09F * this.fm.EI.engines[2].getManifoldPressure());
        f1 = this.pictManf3 - 1.0F;
        f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST3", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitHalifaxBMkII.boostScale), 0.0F, 0.0F);
        this.pictManf4 = (0.91F * this.pictManf4) + (0.09F * this.fm.EI.engines[3].getManifoldPressure());
        f1 = this.pictManf4 - 1.0F;
        f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST4", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitHalifaxBMkII.boostScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL1", this.cvt(this.fm.M.fuel, 0.0F, 763F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL2", this.cvt(this.fm.M.fuel, 0.0F, 763F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL3", this.cvt((float) Math.sqrt(this.fm.M.fuel), 0.0F, (float) Math.sqrt(88.379997253417969D), 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL4", this.cvt(this.fm.M.fuel, 1022F, 1200F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL5", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL6", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL7", this.cvt(this.fm.M.fuel, 851F, 1123F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL8", this.cvt(this.fm.M.fuel, 851F, 1123F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT2", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG2", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT3", this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG3", this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT4", this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG4", this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL2", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL3", this.cvt(this.fm.EI.engines[2].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL4", this.cvt(this.fm.EI.engines[3].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD1", this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitHalifaxBMkII.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD2", this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitHalifaxBMkII.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD3", this.floatindex(this.cvt(this.fm.EI.engines[2].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitHalifaxBMkII.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD4", this.floatindex(this.cvt(this.fm.EI.engines[3].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitHalifaxBMkII.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STR_OIL_LB1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, -31F), 0.0F);
        this.mesh.chunkSetAngles("STR_OIL_LB2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 10F, 0.0F, -31F), 0.0F);
        this.mesh.chunkSetAngles("STR_OIL_LB3", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[2].tOilOut), 0.0F, 10F, 0.0F, -31F), 0.0F);
        this.mesh.chunkSetAngles("STR_OIL_LB4", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[3].tOilOut), 0.0F, 10F, 0.0F, -31F), 0.0F);
        this.mesh.chunkSetAngles("STRELK_TURN_UP", this.cvt(this.getBall(8D), -8F, 8F, 31F, -31F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("STREL_TURN_DOWN", this.cvt(this.w.z, -0.23562F, 0.23562F, -50F, 50F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_V_LONG", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 26.8224F, 214.5792F, 0.0F, 21F), CockpitHalifaxBMkII.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkVisible("STRELK_V_SHORT", false);
        this.mesh.chunkSetAngles("STRELKA_GOS", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02355F, -0.02355F);
        this.mesh.chunkSetLocate("STRELKA_GOR", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("STR_CLIMB", this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), CockpitHalifaxBMkII.variometerScale), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FlapPos", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 125F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1Pos", -104F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2Pos", 208F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
//        if ((this.fm.AS.astateCockpitState & 0x80) == 0);
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage4", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("HullDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("STRELKA_FUEL2", false);
            this.mesh.chunkVisible("STRELKA_FUEL3", false);
            this.mesh.chunkVisible("STRELKA_FUEL4", false);
            this.mesh.chunkVisible("STRELKA_FUEL5", false);
            this.mesh.chunkVisible("STRELKA_FUEL6", false);
            this.mesh.chunkVisible("STRELKA_FUEL7", false);
            this.mesh.chunkVisible("Z_RPM_SHORT1", false);
            this.mesh.chunkVisible("Z_RPM_LONG1", false);
            this.mesh.chunkVisible("Z_RPM_SHORT2", false);
            this.mesh.chunkVisible("Z_RPM_LONG2", false);
            this.mesh.chunkVisible("STRELKA_BOOST1", false);
            this.mesh.chunkVisible("Z_TEMP_OIL1", false);
            this.mesh.chunkVisible("Z_TEMP_OIL2", false);
            this.mesh.chunkVisible("Z_TEMP_RAD1", false);
            this.mesh.chunkVisible("STRELK_V_LONG", false);
            this.mesh.chunkVisible("STRELK_V_SHORT", false);
            this.mesh.chunkVisible("STRELKA_GOR", false);
            this.mesh.chunkVisible("STRELKA_GOS", false);
            this.mesh.chunkVisible("STREL_ALT_LONG", false);
            this.mesh.chunkVisible("STREL_ALT_SHORT", false);
            this.mesh.chunkVisible("STRELK_TURN_UP", false);
            this.mesh.chunkVisible("Z_FlapPos", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
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

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    public float getPictLlit() {
        return this.pictLlit;
    }

    public void setPictLlit(float pictLlit) {
        this.pictLlit = pictLlit;
    }

    public float getPictSupc() {
        return this.pictSupc;
    }

    public void setPictSupc(float pictSupc) {
        this.pictSupc = pictSupc;
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictBrake;
    private float              pictFlap;
    private float              pictGear;
    private float              pictBbay;
    private float              pictSupc;
    private float              pictLlit;
    private float              pictManf1;
    private float              pictManf2;
    private float              pictManf3;
    private float              pictManf4;
    private boolean            bNeedSetUp;
    private int                iWiper;
    private static final float speedometerScale[] = { 0.0F, 16.5F, 31F, 60.5F, 90F, 120.5F, 151.5F, 182F, 213.5F, 244F, 274F, 303F, 333.5F, 369.5F, 399F, 434.5F, 465.5F, 496.5F, 527.5F, 558.5F, 588.5F, 626.5F };
    private static final float radScale[]         = { 0.0F, 0.1F, 0.2F, 0.3F, 3.5F, 11F, 22F, 37.5F, 58.5F, 82.5F, 112.5F, 147F, 187F, 236F, 298.5F };
    private static final float boostScale[]       = { 0.0F, 21F, 39F, 56F, 90.5F, 109.5F, 129F, 146.5F, 163F, 179.5F, 196F, 212.5F, 231.5F, 250.5F };
    private static final float variometerScale[]  = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
    private SoundFX            fxw;
    private Sample             wiStart;
    private Sample             wiRun;
    private int                wiState;
}
