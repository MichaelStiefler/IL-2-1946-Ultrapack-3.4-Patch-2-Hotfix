package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPE_8 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitPE_8.this.fm != null) {
                if (CockpitPE_8.this.bNeedSetUp) {
                    CockpitPE_8.this.reflectPlaneMats();
                    CockpitPE_8.this.bNeedSetUp = false;
                }
                CockpitPE_8.this.setTmp = CockpitPE_8.this.setOld;
                CockpitPE_8.this.setOld = CockpitPE_8.this.setNew;
                CockpitPE_8.this.setNew = CockpitPE_8.this.setTmp;
                for (int i = 0; i < 4; i++) {
                    CockpitPE_8.this.setNew.throttle[i] = ((10F * CockpitPE_8.this.setOld.throttle[i]) + CockpitPE_8.this.fm.EI.engines[i].getControlThrottle()) / 11F;
                }

                CockpitPE_8.this.pictSupc1 = (0.8F * CockpitPE_8.this.pictSupc1) + (0.2F * CockpitPE_8.this.fm.EI.engines[0].getControlCompressor());
                CockpitPE_8.this.pictSupc2 = (0.8F * CockpitPE_8.this.pictSupc2) + (0.2F * CockpitPE_8.this.fm.EI.engines[1].getControlCompressor());
                CockpitPE_8.this.pictSupc3 = (0.8F * CockpitPE_8.this.pictSupc3) + (0.2F * CockpitPE_8.this.fm.EI.engines[2].getControlCompressor());
                CockpitPE_8.this.pictSupc4 = (0.8F * CockpitPE_8.this.pictSupc4) + (0.2F * CockpitPE_8.this.fm.EI.engines[3].getControlCompressor());
                float f = 20F;
                if ((CockpitPE_8.this.gearsLever != 0.0F) && (CockpitPE_8.this.gears == CockpitPE_8.this.fm.CT.getGear())) {
                    CockpitPE_8.this.gearsLever = CockpitPE_8.this.gearsLever * 0.8F;
                    if (Math.abs(CockpitPE_8.this.gearsLever) < 0.1F) {
                        CockpitPE_8.this.gearsLever = 0.0F;
                    }
                } else if (CockpitPE_8.this.gears < CockpitPE_8.this.fm.CT.getGear()) {
                    CockpitPE_8.this.gears = CockpitPE_8.this.fm.CT.getGear();
                    CockpitPE_8.this.gearsLever = CockpitPE_8.this.gearsLever + 2.0F;
                    if (CockpitPE_8.this.gearsLever > f) {
                        CockpitPE_8.this.gearsLever = f;
                    }
                } else if (CockpitPE_8.this.gears > CockpitPE_8.this.fm.CT.getGear()) {
                    CockpitPE_8.this.gears = CockpitPE_8.this.fm.CT.getGear();
                    CockpitPE_8.this.gearsLever = CockpitPE_8.this.gearsLever - 2.0F;
                    if (CockpitPE_8.this.gearsLever < -f) {
                        CockpitPE_8.this.gearsLever = -f;
                    }
                }
                float f1 = CockpitPE_8.this.fm.CT.FlapsControl;
                float f2 = 0.0F;
                if (f1 < 0.2F) {
                    f2 = 1.5F;
                } else if (f1 < 0.3333333F) {
                    f2 = 2.0F;
                } else {
                    f2 = 1.0F;
                }
                CockpitPE_8.this.setNew.flaps = (0.91F * CockpitPE_8.this.setOld.flaps) + (0.09F * f1 * f2);
                CockpitPE_8.this.w.set(CockpitPE_8.this.fm.getW());
                CockpitPE_8.this.fm.Or.transform(CockpitPE_8.this.w);
                CockpitPE_8.this.setNew.turn = ((12F * CockpitPE_8.this.setOld.turn) + CockpitPE_8.this.w.z) / 13F;
                CockpitPE_8.this.setNew.prop1 = ((10F * CockpitPE_8.this.setOld.prop1) + CockpitPE_8.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitPE_8.this.setNew.prop2 = ((10F * CockpitPE_8.this.setOld.prop2) + CockpitPE_8.this.fm.EI.engines[1].getControlProp()) / 11F;
                CockpitPE_8.this.setNew.prop3 = ((10F * CockpitPE_8.this.setOld.prop3) + CockpitPE_8.this.fm.EI.engines[2].getControlProp()) / 11F;
                CockpitPE_8.this.setNew.prop4 = ((10F * CockpitPE_8.this.setOld.prop4) + CockpitPE_8.this.fm.EI.engines[3].getControlProp()) / 11F;
                CockpitPE_8.this.setNew.mix1 = ((10F * CockpitPE_8.this.setOld.mix1) + CockpitPE_8.this.fm.EI.engines[0].getControlMix()) / 11F;
                CockpitPE_8.this.setNew.mix2 = ((10F * CockpitPE_8.this.setOld.mix2) + CockpitPE_8.this.fm.EI.engines[1].getControlMix()) / 11F;
                CockpitPE_8.this.setNew.mix3 = ((10F * CockpitPE_8.this.setOld.mix3) + CockpitPE_8.this.fm.EI.engines[2].getControlMix()) / 11F;
                CockpitPE_8.this.setNew.mix4 = ((10F * CockpitPE_8.this.setOld.mix4) + CockpitPE_8.this.fm.EI.engines[3].getControlMix()) / 11F;
                CockpitPE_8.this.setNew.altimeter = CockpitPE_8.this.fm.getAltitude();
                if (Math.abs(CockpitPE_8.this.fm.Or.getKren()) < 30F) {
                    CockpitPE_8.this.setNew.azimuth.setDeg(CockpitPE_8.this.setOld.azimuth.getDeg(1.0F), CockpitPE_8.this.fm.Or.azimut());
                }
                if (CockpitPE_8.this.useRealisticNavigationInstruments()) {
                    if (CockpitPE_8.this.fm.AS.listenLorenzBlindLanding && CockpitPE_8.this.fm.AS.isAAFIAS) {
                        CockpitPE_8.this.setNew.ilsLoc = ((10F * CockpitPE_8.this.setOld.ilsLoc) + CockpitPE_8.this.getBeaconDirection()) / 11F;
                        CockpitPE_8.this.setNew.ilsGS = ((10F * CockpitPE_8.this.setOld.ilsGS) + CockpitPE_8.this.getGlidePath()) / 11F;
                        CockpitPE_8.this.setNew.waypointAzimuth.setDeg(0.0F);
                    } else {
                        CockpitPE_8.this.setNew.waypointAzimuth.setDeg(CockpitPE_8.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitPE_8.this.getBeaconDirection());
                        CockpitPE_8.this.setNew.ilsLoc = 0.0F;
                        CockpitPE_8.this.setNew.ilsGS = 0.0F;
                    }
                } else {
                    CockpitPE_8.this.setNew.waypointAzimuth.setDeg(CockpitPE_8.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitPE_8.this.waypointAzimuth() - CockpitPE_8.this.fm.Or.azimut());
                }
                CockpitPE_8.this.setNew.vspeed = ((199F * CockpitPE_8.this.setOld.vspeed) + CockpitPE_8.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle[] = { 0.0F, 0.0F, 0.0F, 0.0F };
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        float      prop1;
        float      prop2;
        float      prop3;
        float      prop4;
        float      mix1;
        float      mix2;
        float      flaps;
        float      mix3;
        float      mix4;
        float      turn;
        AnglesFork waypointAzimuth;
        float      ilsLoc;
        float      ilsGS;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitPE_8() {
        super("3DO/Cockpit/Pe-8/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManifold1 = 0.0F;
        this.pictManifold2 = 0.0F;
        this.pictManifold3 = 0.0F;
        this.pictManifold4 = 0.0F;
        this.rpmGeneratedPressure1 = 0.0F;
        this.rpmGeneratedPressure2 = 0.0F;
        this.rpmGeneratedPressure3 = 0.0F;
        this.rpmGeneratedPressure4 = 0.0F;
        this.oilPressure1 = 0.0F;
        this.oilPressure2 = 0.0F;
        this.oilPressure3 = 0.0F;
        this.oilPressure4 = 0.0F;
        this.pictSupc1 = 0.0F;
        this.pictSupc2 = 0.0F;
        this.pictSupc3 = 0.0F;
        this.pictSupc4 = 0.0F;
        this.gearsLever = 0.0F;
        this.gears = 0.0F;
        this.enteringAim = false;
        this.bEntered = false;
        this.cockpitNightMats = (new String[] { "AP_Nid_DM", "AP_Nid", "APDials_DM", "APDials", "ArtHoriz", "ArtHoriz_DM", "GP_II", "GP_II_DM", "GP_III", "GP_III_DM", "GP_IV", "GP_IV_DM", "GP_V", "GP_V_DM", "GP_VI", "GP_VII", "GP_VII_DM", "TrimBase" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.04F });
    }

    public void reflectWorldToInstruments(float f) {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) {
                this.enteringAim = false;
            }
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_StickBase", 0.0F, 0.0F, (12.5F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl))) - 12.5F);
        this.mesh.chunkSetAngles("Z_Steer", -80F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        float f1 = 15F * this.fm.CT.getRudder();
        float f2 = 10F * this.fm.CT.getBrakeR();
        float f3 = 10F * this.fm.CT.getBrakeL();
        this.mesh.chunkSetAngles("PedLever", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("PedLever01", 0.0F, 0.0F, f1);
        this.mesh.chunkSetAngles("PedRod", 0.0F, 0.0F, f1);
        this.mesh.chunkSetAngles("PedRod01", 0.0F, 0.0F, -f1);
        this.mesh.chunkSetAngles("PedBase", 0.0F, 0.0F, f1 * 1.5F);
        this.mesh.chunkSetAngles("PedBase01", 0.0F, 0.0F, -f1 * 1.5F);
        this.mesh.chunkSetAngles("PedBrake", 0.0F, 0.0F, -f3);
        this.mesh.chunkSetAngles("PedBrake01", 0.0F, 0.0F, -f2);
        this.mesh.chunkSetAngles("PedBrakeRod", 0.0F, 0.0F, -f3 / 2.05F);
        this.mesh.chunkSetAngles("PedBrakeRod01", 0.0F, 0.0F, -f2 / 2.05F);
        this.mesh.chunkSetAngles("PedBrakeLever", 0.0F, 0.0F, f3);
        this.mesh.chunkSetAngles("PedBrakeLever01", 0.0F, 0.0F, f2);
        for (int i = 0; i < 4; i++) {
            this.mesh.chunkSetAngles("Z_throttle" + (i + 1), 0.0F, 0.0F, -70F * this.interp(this.setNew.throttle[i], this.setOld.throttle[i], f));
            this.mesh.chunkSetAngles("Z_aux_throttle" + (i + 1), 0.0F, 0.0F, -35F * this.interp(this.setNew.throttle[i], this.setOld.throttle[i], f));
        }

        float f4 = 45F * this.interp(this.setNew.prop1, this.setOld.prop1, f);
        this.mesh.chunkSetAngles("Z_pitch_1", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pitchrod_1", -f4, 0.0F, 0.0F);
        f4 = 45F * this.interp(this.setNew.prop2, this.setOld.prop2, f);
        this.mesh.chunkSetAngles("Z_pitch_2", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pitchrod_2", -f4, 0.0F, 0.0F);
        f4 = 45F * this.interp(this.setNew.prop3, this.setOld.prop3, f);
        this.mesh.chunkSetAngles("Z_pitch_3", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pitchrod_3", -f4, 0.0F, 0.0F);
        f4 = 45F * this.interp(this.setNew.prop4, this.setOld.prop4, f);
        this.mesh.chunkSetAngles("Z_pitch_4", f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pitchrod_4", -f4, 0.0F, 0.0F);
        f4 = 60F * this.interp(this.setNew.mix1, this.setOld.mix1, f);
        this.mesh.chunkSetAngles("Z_mixture_1", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Z_mixknob1", 0.0F, 0.0F, f4);
        f4 = 60F * this.interp(this.setNew.mix2, this.setOld.mix2, f);
        this.mesh.chunkSetAngles("Z_mixture_2", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Z_mixknob2", 0.0F, 0.0F, f4);
        f4 = 60F * this.interp(this.setNew.mix3, this.setOld.mix3, f);
        this.mesh.chunkSetAngles("Z_mixture_3", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Z_mixknob3", 0.0F, 0.0F, f4);
        f4 = 60F * this.interp(this.setNew.mix4, this.setOld.mix4, f);
        this.mesh.chunkSetAngles("Z_mixture_4", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Z_mixknob4", 0.0F, 0.0F, f4);
        this.mesh.chunkSetAngles("Z_superc_1", this.pictSupc1 * 39F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_superc_2", this.pictSupc2 * 39F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_superc_3", this.pictSupc3 * 39F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_superc_4", this.pictSupc4 * 39F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Compass", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AutoPilot01", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Alt_Short", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Alt_Big", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Inclinometer", this.cvt(this.setNew.turn, -0.2F, 0.2F, -25F, 25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_ball", this.cvt(this.getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_ball2", this.cvt(this.getBall(8D), -8F, 8F, 16F, -16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Airspeed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed2KMH()), 0.0F, 800F, 0.0F, 16F), CockpitPE_8.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMlong_L", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMShort_L", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMlong_L01", -this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMShort_L01", -this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMlong_L02", -this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMShort_L02", -this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMlong_L03", -this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPMShort_L03", -this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.pictManifold1 = (0.85F * this.pictManifold1) + (0.15F * this.fm.EI.engines[0].getManifoldPressure() * 76F);
        this.pictManifold2 = (0.85F * this.pictManifold2) + (0.15F * this.fm.EI.engines[1].getManifoldPressure() * 76F);
        this.pictManifold3 = (0.85F * this.pictManifold3) + (0.15F * this.fm.EI.engines[2].getManifoldPressure() * 76F);
        this.pictManifold4 = (0.85F * this.pictManifold4) + (0.15F * this.fm.EI.engines[3].getManifoldPressure() * 76F);
        this.mesh.chunkSetAngles("Z_Need_NadduvL", -this.cvt(this.pictManifold1, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NadduvL06", -this.cvt(this.pictManifold2, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NadduvL07", -this.cvt(this.pictManifold3, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_NadduvL08", -this.cvt(this.pictManifold4, 30F, 120F, 0.0F, 300F), 0.0F, 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure1 = this.rpmGeneratedPressure1 - 2.0F;
            this.rpmGeneratedPressure2 = this.rpmGeneratedPressure2 - 2.0F;
            this.rpmGeneratedPressure3 = this.rpmGeneratedPressure3 - 2.0F;
            this.rpmGeneratedPressure4 = this.rpmGeneratedPressure4 - 2.0F;
        } else {
            if (this.fm.EI.engines[0].getRPM() < this.rpmGeneratedPressure1) {
                this.rpmGeneratedPressure1 = this.rpmGeneratedPressure1 - ((this.rpmGeneratedPressure1 - this.fm.EI.engines[0].getRPM()) * 0.01F);
            } else {
                this.rpmGeneratedPressure1 = this.rpmGeneratedPressure1 + ((this.fm.EI.engines[0].getRPM() - this.rpmGeneratedPressure1) * 0.001F);
            }
            if (this.fm.EI.engines[1].getRPM() < this.rpmGeneratedPressure2) {
                this.rpmGeneratedPressure2 = this.rpmGeneratedPressure2 - ((this.rpmGeneratedPressure2 - this.fm.EI.engines[1].getRPM()) * 0.01F);
            } else {
                this.rpmGeneratedPressure2 = this.rpmGeneratedPressure2 + ((this.fm.EI.engines[1].getRPM() - this.rpmGeneratedPressure2) * 0.001F);
            }
            if (this.fm.EI.engines[2].getRPM() < this.rpmGeneratedPressure3) {
                this.rpmGeneratedPressure3 = this.rpmGeneratedPressure3 - ((this.rpmGeneratedPressure3 - this.fm.EI.engines[2].getRPM()) * 0.01F);
            } else {
                this.rpmGeneratedPressure3 = this.rpmGeneratedPressure3 + ((this.fm.EI.engines[2].getRPM() - this.rpmGeneratedPressure3) * 0.001F);
            }
            if (this.fm.EI.engines[3].getRPM() < this.rpmGeneratedPressure4) {
                this.rpmGeneratedPressure4 = this.rpmGeneratedPressure4 - ((this.rpmGeneratedPressure4 - this.fm.EI.engines[3].getRPM()) * 0.01F);
            } else {
                this.rpmGeneratedPressure4 = this.rpmGeneratedPressure4 + ((this.fm.EI.engines[3].getRPM() - this.rpmGeneratedPressure4) * 0.001F);
            }
        }
        if (this.rpmGeneratedPressure1 < 800F) {
            this.oilPressure1 = this.cvt(this.rpmGeneratedPressure1, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure1 < 1800F) {
            this.oilPressure1 = this.cvt(this.rpmGeneratedPressure1, 800F, 1800F, 4F, 8F);
        } else {
            this.oilPressure1 = this.cvt(this.rpmGeneratedPressure1, 1800F, 2750F, 8F, 10F);
        }
        if (this.rpmGeneratedPressure2 < 800F) {
            this.oilPressure2 = this.cvt(this.rpmGeneratedPressure2, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure2 < 1800F) {
            this.oilPressure2 = this.cvt(this.rpmGeneratedPressure2, 800F, 1800F, 4F, 8F);
        } else {
            this.oilPressure2 = this.cvt(this.rpmGeneratedPressure2, 1800F, 2750F, 8F, 10F);
        }
        if (this.rpmGeneratedPressure3 < 800F) {
            this.oilPressure3 = this.cvt(this.rpmGeneratedPressure3, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure3 < 1800F) {
            this.oilPressure3 = this.cvt(this.rpmGeneratedPressure3, 800F, 1800F, 4F, 8F);
        } else {
            this.oilPressure3 = this.cvt(this.rpmGeneratedPressure3, 1800F, 2750F, 8F, 10F);
        }
        if (this.rpmGeneratedPressure4 < 800F) {
            this.oilPressure4 = this.cvt(this.rpmGeneratedPressure4, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure4 < 1800F) {
            this.oilPressure4 = this.cvt(this.rpmGeneratedPressure4, 800F, 1800F, 4F, 8F);
        } else {
            this.oilPressure4 = this.cvt(this.rpmGeneratedPressure4, 1800F, 2750F, 8F, 10F);
        }
        float f6 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f6 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f6 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f6 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f7 = f6 * this.fm.EI.engines[0].getReadyness() * this.oilPressure1;
        this.mesh.chunkSetAngles("Z_Need_OilPressureL", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilPressureL15", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_GasPressureR", -this.cvt(this.rpmGeneratedPressure1, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        if (this.fm.EI.engines[1].tOilOut > 90F) {
            f6 = this.cvt(this.fm.EI.engines[1].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[1].tOilOut < 50F) {
            f6 = this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f6 = this.cvt(this.fm.EI.engines[1].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        f7 = f6 * this.fm.EI.engines[1].getReadyness() * this.oilPressure2;
        this.mesh.chunkSetAngles("Z_Need_OilPressureL09", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilPressureL14", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_GasPressureR01", -this.cvt(this.rpmGeneratedPressure2, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        if (this.fm.EI.engines[2].tOilOut > 90F) {
            f6 = this.cvt(this.fm.EI.engines[2].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[2].tOilOut < 50F) {
            f6 = this.cvt(this.fm.EI.engines[2].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f6 = this.cvt(this.fm.EI.engines[2].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        f7 = f6 * this.fm.EI.engines[2].getReadyness() * this.oilPressure3;
        this.mesh.chunkSetAngles("Z_Need_OilPressureL10", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilPressureL13", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_GasPressureR02", -this.cvt(this.rpmGeneratedPressure3, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        if (this.fm.EI.engines[3].tOilOut > 90F) {
            f6 = this.cvt(this.fm.EI.engines[3].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[3].tOilOut < 50F) {
            f6 = this.cvt(this.fm.EI.engines[3].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f6 = this.cvt(this.fm.EI.engines[3].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        f7 = f6 * this.fm.EI.engines[3].getReadyness() * this.oilPressure4;
        this.mesh.chunkSetAngles("Z_Need_OilPressureL11", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilPressureL12", -this.cvt(f7, 0.0F, 15F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_GasPressureR03", -this.cvt(this.rpmGeneratedPressure4, 0.0F, 2000F, 0.0F, 200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Lever01", 0.0F, -this.gearsLever, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Fuel", -this.cvt(this.fm.M.fuel, 0.0F, 1500F, 0.0F, 196F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("G_Hor_Sph02", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("G_Hor_Sph01", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -35F, 35F, 0.028F, -0.028F);
        this.mesh.chunkSetLocate("Z_Hor1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_Hor2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Need_ClimbRate", this.cvt(this.setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilTemp_L01", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilTemp_L02", -this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilTemp_L04", -this.cvt(this.fm.EI.engines[2].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_OilTemp_L03", -this.cvt(this.fm.EI.engines[3].tOilOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_WatT_L01", -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_WatT_L02", -this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_WatT_L04", -this.cvt(this.fm.EI.engines[2].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_WatT_L03", -this.cvt(this.fm.EI.engines[3].tWaterOut, 0.0F, 160F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RDF", this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_clock_minute", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_clock_hour", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_clock_second", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Trim2", 0.0F, 360F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("TrimElevator", 0.0F, 0.0F, 360F * this.fm.CT.getTrimElevatorControl());
        this.mesh.chunkSetAngles("Trimmer3", 0.0F, 0.0F, -360F * this.fm.CT.getTrimRudderControl());
        this.mesh.chunkVisible("L_gearUP_L", (this.fm.CT.getGearL() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("L_gearUP_R", (this.fm.CT.getGearR() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("L_gearDOWN_L", (this.fm.CT.getGearL() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("L_gearDOWN_R", (this.fm.CT.getGearR() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Lever02", this.interp(this.setNew.flaps, this.setOld.flaps, f) * 20F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_blind_V", this.cvt(this.setNew.ilsLoc, -63F, 63F, -45F, 45F), 0.0F, 0.0F);
        if (this.setNew.ilsGS >= 0.0F) {
            this.mesh.chunkSetAngles("Z_need_blind_H", this.cvt(this.setNew.ilsGS, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_need_blind_H", this.cvt(this.setNew.ilsGS, -0.3F, 0.0F, -40F, 0.0F), 0.0F, 0.0F);
        }
        if (this.fm.CT.bHasBrakeControl) {
            float f5 = this.fm.CT.getBrake();
            this.mesh.chunkSetAngles("Z_Need_Air", -this.cvt(this.fm.CT.getBrakeR(), 0.0F, 1.5F, 0.0F, 140F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Need_Air2", -this.cvt(this.fm.CT.getBrakeL(), 0.0F, 1.5F, 0.0F, 140F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Need_Air_Pressure", -155F + (f5 * 40F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Need_Oil_Pressure", 180F, 0.0F, 0.0F);
        this.mesh.chunkVisible("L_RED", this.fm.CT.pdiLights == 1);
        this.mesh.chunkVisible("L_WHITE", this.fm.CT.pdiLights == 2);
        this.mesh.chunkVisible("L_GREEN", this.fm.CT.pdiLights == 3);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Glass_DM_05", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Glass_DM_06", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Glass_DM_02", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Glass_DM_01", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Glass_DM_03", true);
            this.mesh.chunkVisible("BodyDamage1", true);
            this.mesh.chunkVisible("gauges1_D0", false);
            this.mesh.chunkVisible("gauges1_D1", true);
            this.mesh.chunkVisible("Z_Hor2", false);
            this.mesh.chunkVisible("Z_Need_ball", false);
            this.mesh.chunkVisible("Z_Need_Inclinometer", false);
            this.mesh.chunkVisible("Z_clock_minute", false);
            this.mesh.chunkVisible("Z_clock_hour", false);
            this.mesh.chunkVisible("Z_clock_second", false);
            this.mesh.chunkVisible("Z_Need_Air", false);
            this.mesh.chunkVisible("Z_Need_Air2", false);
            this.mesh.chunkVisible("Z_Need_Airspeed01", false);
            this.mesh.chunkVisible("Z_Need_RDF", false);
            this.mesh.chunkVisible("G_Hor_Sph01", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("BodyDamage2", true);
            this.mesh.chunkVisible("Glass_DM_04", true);
            this.mesh.chunkVisible("gauges2_D0", false);
            this.mesh.chunkVisible("gauges2_D1", true);
            this.mesh.chunkVisible("Z_Need_Alt_Big", false);
            this.mesh.chunkVisible("Z_Need_Alt_Short", false);
            this.mesh.chunkVisible("Z_Need_Vacuum", false);
            this.mesh.chunkVisible("Z_need_blind_V", false);
            this.mesh.chunkVisible("Z_need_blind_H", false);
            this.mesh.chunkVisible("Z_Need_Oil_Pressure", false);
            this.mesh.chunkVisible("Z_Need_NadduvL", false);
            this.mesh.chunkVisible("Z_Hor1", false);
            this.mesh.chunkVisible("G_Hor_Sph02", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Glass_DM_07", true);
            this.mesh.chunkVisible("BodyDamage1", true);
            this.mesh.chunkVisible("gauges3_D0", false);
            this.mesh.chunkVisible("gauges3_D1", true);
            this.mesh.chunkVisible("Z_Need_ClimbRate", false);
            this.mesh.chunkVisible("Z_Need_Airspeed", false);
            this.mesh.chunkVisible("Z_Need_Air_Pressure", false);
            this.mesh.chunkVisible("Z_Need_RPMShort_L01", false);
            this.mesh.chunkVisible("Z_Need_RPMlong_L01", false);
            this.mesh.chunkVisible("Z_Need_Ball2", false);
            this.mesh.chunkVisible("Z_AutoPilot", false);
            this.mesh.chunkVisible("Z_AutoPilot01", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Glass_DM_08", true);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private void prepareToEnter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(32.4F, -40.4F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 50");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.setInstantOrient(32.4F, -40.4F, 0.0F);
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.setInstantOrient(32.4F, -40.4F, 0.0F);
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            return;
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Antenna_D0", false);
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.0D, -0.14D, -0.02D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            this.aircraft().hierMesh().chunkVisible("Antenna_D0", true);
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused() || (this.isToggleAim() == flag)) {
            return;
        }
        if (flag) {
            this.prepareToEnter();
        } else {
            this.leave();
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold1;
    private float              pictManifold2;
    private float              pictManifold3;
    private float              pictManifold4;
    private float              rpmGeneratedPressure1;
    private float              rpmGeneratedPressure2;
    private float              rpmGeneratedPressure3;
    private float              rpmGeneratedPressure4;
    private float              oilPressure1;
    private float              oilPressure2;
    private float              oilPressure3;
    private float              oilPressure4;
    private float              pictSupc1;
    private float              pictSupc2;
    private float              pictSupc3;
    private float              pictSupc4;
    private float              gearsLever;
    private float              gears;
    private boolean            enteringAim;
    private static final float speedometerScale[] = { 0.0F, 7F, 11.5F, 42F, 85F, 125.5F, 164.5F, 181F, 198F, 213.5F, 230F, 248F, 266F, 289F, 310F, 327F, 346F };
    private float              saveFov;
    private boolean            bEntered;

    static {
        Property.set(CockpitPE_8.class, "normZNs", new float[] { 1.82F, 0.6F, 1.15F, 1.6F });
    }
}
