package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitFW_190D11Sea extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFW_190D11Sea.this.fm != null) {
                CockpitFW_190D11Sea.this.setTmp = CockpitFW_190D11Sea.this.setOld;
                CockpitFW_190D11Sea.this.setOld = CockpitFW_190D11Sea.this.setNew;
                CockpitFW_190D11Sea.this.setNew = CockpitFW_190D11Sea.this.setTmp;
                CockpitFW_190D11Sea.this.setNew.altimeter = CockpitFW_190D11Sea.this.fm.getAltitude();
                if (CockpitFW_190D11Sea.this.cockpitDimControl) {
                    if (CockpitFW_190D11Sea.this.setNew.dimPosition > 0.0F) {
                        CockpitFW_190D11Sea.this.setNew.dimPosition = CockpitFW_190D11Sea.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitFW_190D11Sea.this.setNew.dimPosition < 1.0F) {
                    CockpitFW_190D11Sea.this.setNew.dimPosition = CockpitFW_190D11Sea.this.setOld.dimPosition + 0.05F;
                }
                CockpitFW_190D11Sea.this.setNew.throttle = ((10F * CockpitFW_190D11Sea.this.setOld.throttle) + CockpitFW_190D11Sea.this.fm.CT.PowerControl) / 11F;
                CockpitFW_190D11Sea.this.setNew.vspeed = ((499F * CockpitFW_190D11Sea.this.setOld.vspeed) + CockpitFW_190D11Sea.this.fm.getVertSpeed()) / 500F;
                CockpitFW_190D11Sea.this.setNew.waypointAzimuth.setDeg(CockpitFW_190D11Sea.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitFW_190D11Sea.this.waypointAzimuth() - CockpitFW_190D11Sea.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                if (Math.abs(CockpitFW_190D11Sea.this.fm.Or.getKren()) < 30F) {
                    CockpitFW_190D11Sea.this.setNew.azimuth.setDeg(CockpitFW_190D11Sea.this.setOld.azimuth.getDeg(1.0F), CockpitFW_190D11Sea.this.fm.Or.azimut());
                }
            }
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
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitFW_190D11Sea() {
        super("3DO/Cockpit/FW-190D-11Sea/hier.him", "bf109");
        this.gun = new Gun[6];
        this.bomb = new BulletEmitter[4];
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_R");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "D9GP1", "A8GP2", "D9GP3", "A8GP4", "A8GP5", "A4GP6", "A5GP3Km", "DA8GP1", "DA8GP2", "DA8GP3", "DA8GP4" });
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
            this.gun[4] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[5] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
        }
        if (this.bomb[0] == null) {
            for (int i = 0; i < this.bomb.length; i++) {
                this.bomb[i] = GunEmpty.get();
            }

            if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01") != GunEmpty.get()) {
                this.bomb[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
                this.bomb[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            }
            if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock01") != GunEmpty.get()) {
                this.bomb[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock01");
                this.bomb[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock02");
            } else if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03") != GunEmpty.get()) {
                this.bomb[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
                this.bomb[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
            } else if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev05") != GunEmpty.get()) {
                this.bomb[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev05");
                this.bomb[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev06");
            }
            this.t1 = Time.current();
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.43F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("CanopyTop", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Glass", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, -0.33F);
        this.mesh.chunkSetLocate("RearArmorPlate", Cockpit.xyz, Cockpit.ypr);
        if (((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x80) == 0)) {
            this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedleALTKm", 0.0F, 0.0F, this.cvt(this.setNew.altimeter, 0.0F, 10000F, 0.0F, -180F));
        this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), CockpitFW_190D11Sea.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitFW_190D11Sea.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 4F), CockpitFW_190D11Sea.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleOilTemp", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 3F), CockpitFW_190D11Sea.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -6F, 6F, 20F, -20F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("NeedleAHTurn", 0.0F, f1, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("NeedleAHBank", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -11F, 11F), 0.0F);
        }
        this.mesh.chunkSetAngles("NeedleAHCyl", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("NeedleAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 12F, -12F));
        this.mesh.chunkSetAngles("NeedleCD", this.setNew.vspeed >= 0.0F ? -this.floatindex(this.cvt(this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitFW_190D11Sea.vsiNeedleScale) : this.floatindex(this.cvt(-this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitFW_190D11Sea.vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RepeaterOuter", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RepeaterPlane", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleHBSmall", -105F + ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleHBLarge", -270F + ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 2) == 0) && ((this.fm.AS.astateCockpitState & 1) == 0) && ((this.fm.AS.astateCockpitState & 4) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25F, 0.0F, 0.0F);
        }
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.mesh.chunkSetAngles("NeedleHClock", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleMClock", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleSClock", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        }
        this.resetYPRmodifier();
        if (this.gun[2] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[2].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[3] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[3].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[0] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[1] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[1].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[4] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[4].countBullets(), 0.0F, 55F, -0.018F, 0.0F);
            this.mesh.chunkSetLocate("RC_MGFF_WingL", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[5] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[5].countBullets(), 0.0F, 55F, -0.018F, 0.0F);
            this.mesh.chunkSetLocate("RC_MGFF_WingR", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.t1 < Time.current()) {
            this.t1 = Time.current() + 500L;
            this.mesh.chunkVisible("XLampBombCL", this.bomb[1].haveBullets());
            this.mesh.chunkVisible("XLampBombCR", this.bomb[2].haveBullets());
        }
        this.mesh.chunkSetAngles("IgnitionSwitch", 24F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Revi16Tinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 20F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.fm.CT.WeaponControl[1] ? -0.004F : 0.0F;
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f) * 34F * 0.91F;
        Cockpit.xyz[2] = Cockpit.ypr[0] > 7F ? -0.006F : 0.0F;
        this.mesh.chunkSetLocate("ThrottleQuad", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RPedalBase", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedalStrut", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedal", 0.0F, 0.0F, (-this.fm.CT.getRudder() * 15F) - (this.fm.CT.getBrake() * 15F));
        this.mesh.chunkSetAngles("LPedalBase", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedalStrut", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedal", 0.0F, 0.0F, (this.fm.CT.getRudder() * 15F) - (this.fm.CT.getBrake() * 15F));
        this.mesh.chunkVisible("XLampTankSwitch", this.fm.M.fuel > 144F);
        this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 43.2F);
        this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearL_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearR_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.rgear);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0012F, 0.75F);
            this.light2.light.setEmit(0.0012F, 0.75F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
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

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0) || ((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.materialReplace("D9GP1", "DA8GP1");
            this.mesh.materialReplace("D9GP1_night", "DA8GP1_night");
            this.mesh.materialReplace("A8GP4", "DA8GP4");
            this.mesh.chunkVisible("NeedleManPress", false);
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("RepeaterOuter", false);
            this.mesh.chunkVisible("RepeaterPlane", false);
            this.mesh.chunkVisible("NeedleHBLarge", false);
            this.mesh.chunkVisible("NeedleHBSmall", false);
            this.mesh.chunkVisible("NeedleFuel", false);
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.materialReplace("A8GP2", "DA8GP2");
            this.mesh.materialReplace("A8GP2_night", "DA8GP2_night");
            this.resetYPRmodifier();
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.003F;
            Cockpit.xyz[2] = 0.012F;
            Cockpit.ypr[0] = -3F;
            Cockpit.ypr[1] = -3F;
            Cockpit.ypr[2] = 9F;
            this.mesh.chunkSetLocate("IPCentral", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("NeedleAHCyl", false);
            this.mesh.chunkVisible("NeedleAHBar", false);
            this.mesh.chunkVisible("NeedleAHTurn", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.chunkVisible("NeedleOilTemp", false);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.materialReplace("D9GP3", "DA8GP3");
            this.mesh.materialReplace("D9GP3_night", "DA8GP3_night");
            this.mesh.chunkVisible("NeedleKMH", false);
            this.mesh.chunkVisible("NeedleCD", false);
            this.mesh.chunkVisible("NeedleAlt", false);
            this.mesh.chunkVisible("NeedleAltKM Kill", false);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        this.retoggleLight();
    }

    private Gun                gun[];
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private BulletEmitter      bomb[];
    private long               t1;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 18.5F, 67F, 117F, 164F, 215F, 267F, 320F, 379F, 427F, 428F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 53F, 108F, 170F, 229F, 282F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]        = { 0.0F, 16F, 35F, 52.5F, 72F, 72F };
    private static final float vsiNeedleScale[]   = { 0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F };
    private static final float oilTempScale[]     = { 0.0F, 23F, 52F, 81F, 81F };

    static {
        Property.set(CockpitFW_190D11Sea.class, "normZN", 0.72F);
        Property.set(CockpitFW_190D11Sea.class, "gsZN", 0.66F);
    }

}
