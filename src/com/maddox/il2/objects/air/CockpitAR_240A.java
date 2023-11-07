package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class CockpitAR_240A extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttle1;
        float      throttle2;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      mix1;
        float      mix2;
        float      vspeed;
        float      radioalt;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitAR_240A.this.setTmp = CockpitAR_240A.this.setOld;
            CockpitAR_240A.this.setOld = CockpitAR_240A.this.setNew;
            CockpitAR_240A.this.setNew = CockpitAR_240A.this.setTmp;
            CockpitAR_240A.this.setNew.altimeter = CockpitAR_240A.this.fm.getAltitude();
            if (CockpitAR_240A.this.cockpitDimControl) {
                if (CockpitAR_240A.this.setNew.dimPosition > 0.0F) {
                    CockpitAR_240A.this.setNew.dimPosition = CockpitAR_240A.this.setNew.dimPosition - 0.05F;
                }
            } else if (CockpitAR_240A.this.setNew.dimPosition < 1.0F) {
                CockpitAR_240A.this.setNew.dimPosition = CockpitAR_240A.this.setNew.dimPosition + 0.05F;
            }
            CockpitAR_240A.this.setNew.throttle1 = (0.91F * CockpitAR_240A.this.setOld.throttle1) + (0.09F * CockpitAR_240A.this.fm.EI.engines[0].getControlThrottle());
            CockpitAR_240A.this.setNew.throttle2 = (0.91F * CockpitAR_240A.this.setOld.throttle2) + (0.09F * CockpitAR_240A.this.fm.EI.engines[1].getControlThrottle());
            CockpitAR_240A.this.setNew.mix1 = (0.88F * CockpitAR_240A.this.setOld.mix1) + (0.12F * CockpitAR_240A.this.fm.EI.engines[0].getControlMix());
            CockpitAR_240A.this.setNew.mix2 = (0.88F * CockpitAR_240A.this.setOld.mix2) + (0.12F * CockpitAR_240A.this.fm.EI.engines[1].getControlMix());
            CockpitAR_240A.this.setNew.waypointAzimuth.setDeg(CockpitAR_240A.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitAR_240A.this.waypointAzimuth() - CockpitAR_240A.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
            if (Math.abs(CockpitAR_240A.this.fm.Or.getKren()) < 30F) {
                CockpitAR_240A.this.setNew.azimuth.setDeg(CockpitAR_240A.this.setOld.azimuth.getDeg(1.0F), CockpitAR_240A.this.fm.Or.azimut());
            }
            Variables variables = CockpitAR_240A.this.setNew;
            float f = 0.9F * CockpitAR_240A.this.setOld.radioalt;
            float f1 = 0.1F;
            float f2 = CockpitAR_240A.this.fm.getAltitude();
            World.cur();
            World.land();
            variables.radioalt = f + (f1 * (f2 - Landscape.HQ_Air((float) CockpitAR_240A.this.fm.Loc.x, (float) CockpitAR_240A.this.fm.Loc.y)));
            CockpitAR_240A.this.setNew.vspeed = ((199F * CockpitAR_240A.this.setOld.vspeed) + CockpitAR_240A.this.fm.getVertSpeed()) / 200F;
            CockpitAR_240A.this.middleAngle = (CockpitAR_240A.this.middleAngleTarget * 0.05F) + (0.95F * CockpitAR_240A.this.middleAngle);
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        boolean flag = hiermesh.isChunkVisible("Engine1_D0") || hiermesh.isChunkVisible("Engine1_D1") || hiermesh.isChunkVisible("Engine1_D2");
        this.mesh.chunkVisible("Z_Temp4", flag);
        this.mesh.chunkVisible("Z_Temp6", flag);
        this.mesh.chunkVisible("Z_Fuelpress1", flag);
        this.mesh.chunkVisible("Z_OilPress1", flag);
        this.mesh.chunkVisible("Z_Oiltemp1", flag);
        flag = hiermesh.isChunkVisible("Engine2_D0") || hiermesh.isChunkVisible("Engine2_D1") || hiermesh.isChunkVisible("Engine2_D2");
        this.mesh.chunkVisible("Z_Temp5", flag);
        this.mesh.chunkVisible("Z_Temp7", flag);
        this.mesh.chunkVisible("Z_Fuelpress2", flag);
        this.mesh.chunkVisible("Z_OilPress2", flag);
        this.mesh.chunkVisible("Z_Oiltemp2", flag);
    }

    public CockpitAR_240A() {
        super("3DO/Cockpit/Ar240A-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManifold1 = 0.0F;
        this.pictManifold2 = 0.0F;
        this.middleAngle = 0.0F;
        this.middleAngleTarget = 0.0F;
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = (new String[] { "bague1", "bague2", "boitier", "cadran1", "cadran2", "cadran3", "cadran4", "cadran5", "cadran6", "cadran7", "cadran8", "consoledr2", "enggauge", "fils", "gauche", "skala" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
        }
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.06815F * this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f);
        this.mesh.chunkVisible("Z_GearLGreen", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearRGreen", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearLRed", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_GearRRed", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red4", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("Z_Red6", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("Z_Red8", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getFlap() > 0.665F);
        this.mesh.chunkVisible("Z_Green3", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("Z_Green5", this.fm.CT.getFlap() > 0.665F);
        this.mesh.chunkVisible("Z_White1", this.fm.CT.getFlap() > 0.265F);
        this.mesh.chunkVisible("Z_White2", this.fm.CT.getFlap() > 0.265F);
        this.mesh.chunkVisible("Z_FuelL1", this.fm.M.fuel < 36F);
        this.mesh.chunkVisible("Z_FuelL2", this.fm.M.fuel < 102F);
        this.mesh.chunkVisible("Z_FuelR1", this.fm.M.fuel < 36F);
        this.mesh.chunkVisible("Z_FuelR2", this.fm.M.fuel < 102F);
        if (this.gun[0] != null) {
            this.mesh.chunkVisible("Z_AmmoL", this.gun[0].countBullets() == 0);
        }
        if (this.gun[1] != null) {
            this.mesh.chunkVisible("Z_AmmoR", this.gun[1].countBullets() == 0);
        }
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = 0.00545F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Throttle1", this.interp(this.setNew.throttle1, this.setOld.throttle1, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle2", this.interp(this.setNew.throttle2, this.setOld.throttle2, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", this.interp(this.setNew.mix1, this.setOld.mix2, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", this.interp(this.setNew.mix1, this.setOld.mix2, f) * 52.2F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pitch1", this.fm.EI.engines[0].getControlProp() * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pitch2", this.fm.EI.engines[1].getControlProp() * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiat1", 0.0F, 0.0F, this.fm.EI.engines[0].getControlRadiator() * 15F);
        this.mesh.chunkSetAngles("Z_Radiat2", 0.0F, 0.0F, this.fm.EI.engines[1].getControlRadiator() * 15F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        if (this.gun[0] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt(this.gun[0].countBullets(), 0.0F, 500F, 13F, 0.0F), 0.0F, 0.0F);
        }
        if (this.gun[2] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt(this.gun[2].countBullets(), 0.0F, 500F, 13F, 0.0F), 0.0F, 0.0F);
        }
        if (this.gun[1] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter3", this.cvt(this.gun[1].countBullets(), 0.0F, 500F, 13F, 0.0F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Panelmid", 0.0F, this.cvt(this.middleAngle, 0.0F, 1.0F, 0.0F, -70F), 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -3F, 3F, 21F, -21F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("Z_TurnBank1", f1, 0.0F, 0.0F);
        f1 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(f1, -4F, 4F, 10F, -10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(f1, -3.8F, 3.8F, 9F, -9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(f1, -3.3F, 3.3F, 7.5F, -7.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Horizon1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("Z_Horizon2", this.cvt(this.fm.Or.getTangage(), -45F, 45F, -23F, 23F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.0F, 12F), CockpitAR_240A.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitAR_240A.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitAR_240A.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitAR_240A.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA1", this.cvt(this.pictManifold1 = (0.75F * this.pictManifold1) + (0.25F * this.fm.EI.engines[0].getManifoldPressure()), 0.6F, 1.8F, 0.0F, 332.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA2", this.cvt(this.pictManifold2 = (0.75F * this.pictManifold2) + (0.25F * this.fm.EI.engines[1].getManifoldPressure()), 0.6F, 1.8F, 0.0F, 332.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.radioalt, this.setOld.radioalt, f), 0.0F, 750F, 0.0F, 228.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 14000F, 0.0F, 313F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 66.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 233.09F, 313.09F, -42.5F, 42.4F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 68F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp3", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 68F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirPressure1", 170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Autopilot1", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Autopilot2", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp4", -(float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp6", -(float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp5", -(float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin()) * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp7", -(float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin()) * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuelpress1", this.cvt(this.fm.M.fuel > 1.0F ? 0.77F : 0.0F, 0.0F, 2.0F, 0.0F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuelpress2", this.cvt(this.fm.M.fuel > 1.0F ? 0.77F : 0.0F, 0.0F, 2.0F, 0.0F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oiltemp1", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 120F, 0.0F, 8F), CockpitAR_240A.oilTScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oiltemp2", this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 40F, 120F, 0.0F, 8F), CockpitAR_240A.oilTScale), 0.0F, 0.0F);
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
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.chunkVisible("Revi_D0", false);
            this.mesh.chunkVisible("ReviSun", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Revi_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("HullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
    }

    public void doToggleDim() {
        if (this.middleAngleTarget < 0.5F) {
            this.middleAngleTarget = 1.0F;
        } else {
            this.middleAngleTarget = 0.0F;
        }
    }

    private Gun                gun[]              = { null, null, null };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              middleAngle;
    private float              middleAngleTarget;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold1;
    private float              pictManifold2;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 36.5F, 70F, 111F, 149.5F, 186.5F, 233.5F, 282.5F, 308F, 318.5F };
    private static final float oilTScale[]        = { 0.0F, 24.5F, 47.5F, 74F, 102.5F, 139F, 188F, 227.5F, 290.5F };
    private static final float variometerScale[]  = { -130.5F, -119.5F, -109.5F, -96F, -83F, -49.5F, 0.0F, 49.5F, 83F, 96F, 109.5F, 119.5F, 130.5F };

}
