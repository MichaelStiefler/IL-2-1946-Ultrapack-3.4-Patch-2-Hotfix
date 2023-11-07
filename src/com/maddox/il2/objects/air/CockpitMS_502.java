package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitMS_502 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMS_502.this.bNeedSetUp) {
                CockpitMS_502.this.reflectPlaneMats();
                CockpitMS_502.this.bNeedSetUp = false;
            }
            CockpitMS_502.this.setTmp = CockpitMS_502.this.setOld;
            CockpitMS_502.this.setOld = CockpitMS_502.this.setNew;
            CockpitMS_502.this.setNew = CockpitMS_502.this.setTmp;
            CockpitMS_502.this.setNew.altimeter = CockpitMS_502.this.fm.getAltitude();
            if (CockpitMS_502.this.cockpitDimControl) {
                if (CockpitMS_502.this.setNew.dimPosition > 0.0F) {
                    CockpitMS_502.this.setNew.dimPosition = CockpitMS_502.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitMS_502.this.setNew.dimPosition < 1.0F) {
                CockpitMS_502.this.setNew.dimPosition = CockpitMS_502.this.setOld.dimPosition + 0.05F;
            }
            CockpitMS_502.this.setNew.throttle = ((10F * CockpitMS_502.this.setOld.throttle) + CockpitMS_502.this.fm.CT.PowerControl) / 11F;
            CockpitMS_502.this.setNew.mix = ((8F * CockpitMS_502.this.setOld.mix) + CockpitMS_502.this.fm.EI.engines[0].getControlMix()) / 9F;
            CockpitMS_502.this.setNew.waypointAzimuth.setDeg(CockpitMS_502.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitMS_502.this.waypointAzimuth() - CockpitMS_502.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
            if (Math.abs(CockpitMS_502.this.fm.Or.getKren()) < 30F) {
                CockpitMS_502.this.setNew.azimuth.setDeg(CockpitMS_502.this.setOld.azimuth.getDeg(1.0F), CockpitMS_502.this.fm.Or.azimut());
            }
            CockpitMS_502.this.buzzerFX((CockpitMS_502.this.fm.CT.getGear() < 0.999999F) && (CockpitMS_502.this.fm.CT.getFlap() > 0.0F));
            CockpitMS_502.this.setNew.vspeed = ((199F * CockpitMS_502.this.setOld.vspeed) + CockpitMS_502.this.fm.getVertSpeed()) / 200F;
            return true;
        }

        Interpolater() {
        }
    }

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

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitMS_502() {
        super("3DO/Cockpit/MS_502/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.setNew.dimPosition = 1.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = (new String[] { "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass", "oxigen" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitMS_502.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitMS_502.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), CockpitMS_502.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Iengtemprad1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -3F, 3F, 30F, -30F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("Z_TurnBank1", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(6D), -6F, 6F, -7F, 7F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", this.cvt(this.setNew.vspeed, -15F, 15F, -160F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Z_PedalStrut", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalStrut2", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle_D1", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 27F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle_tube", -this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix_tros", -this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + (28.333F * this.fm.EI.engines[0].getControlMagnetos()), 0.0F, 0.0F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
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
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Z_Throttle", false);
            this.mesh.chunkVisible("Z_Throttle_tube", false);
            this.mesh.chunkVisible("Z_Throttle_D1", true);
            this.mesh.chunkVisible("Z_Throttle_TD1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            if ((this.type == 0) || (this.type == 1)) {
                this.mesh.chunkVisible("Z_OilSplatE4_D1", true);
            } else {
                this.mesh.chunkVisible("Z_OilSplats_D1", true);
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_HullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Z_HullDamage2", true);
            if ((this.type == 0) || (this.type == 1)) {
                this.mesh.chunkVisible("Z_Holes2E4_D1", true);
            } else {
                this.mesh.chunkVisible("Z_Holes2_D1", true);
            }
        }
    }

    protected void reflectPlaneMats() {
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private int                type;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]        = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };

}
