package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitTA_152H_EZ42 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitTA_152H_EZ42.this.fm != null) {
                CockpitTA_152H_EZ42.this.setTmp = CockpitTA_152H_EZ42.this.setOld;
                CockpitTA_152H_EZ42.this.setOld = CockpitTA_152H_EZ42.this.setNew;
                CockpitTA_152H_EZ42.this.setNew = CockpitTA_152H_EZ42.this.setTmp;
                CockpitTA_152H_EZ42.this.setNew.altimeter = CockpitTA_152H_EZ42.this.fm.getAltitude();
                if (CockpitTA_152H_EZ42.this.cockpitDimControl) {
                    if (CockpitTA_152H_EZ42.this.setNew.dimPosition > 0.0F) {
                        CockpitTA_152H_EZ42.this.setNew.dimPosition = CockpitTA_152H_EZ42.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitTA_152H_EZ42.this.setNew.dimPosition < 1.0F) {
                    CockpitTA_152H_EZ42.this.setNew.dimPosition = CockpitTA_152H_EZ42.this.setOld.dimPosition + 0.05F;
                }
                CockpitTA_152H_EZ42.this.setNew.throttle = ((10F * CockpitTA_152H_EZ42.this.setOld.throttle) + CockpitTA_152H_EZ42.this.fm.CT.PowerControl) / 11F;
                CockpitTA_152H_EZ42.this.setNew.vspeed = ((499F * CockpitTA_152H_EZ42.this.setOld.vspeed) + CockpitTA_152H_EZ42.this.fm.getVertSpeed()) / 500F;
                float f = CockpitTA_152H_EZ42.this.waypointAzimuth();
                if (CockpitTA_152H_EZ42.this.useRealisticNavigationInstruments()) {
                    CockpitTA_152H_EZ42.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitTA_152H_EZ42.this.setOld.waypointAzimuth.setDeg(f - 90F);
                } else {
                    CockpitTA_152H_EZ42.this.setNew.waypointAzimuth.setDeg(CockpitTA_152H_EZ42.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitTA_152H_EZ42.this.setOld.azimuth.getDeg(1.0F));
                }
                if (Math.abs(CockpitTA_152H_EZ42.this.fm.Or.getKren()) < 30F) {
                    CockpitTA_152H_EZ42.this.setNew.azimuth.setDeg(CockpitTA_152H_EZ42.this.setOld.azimuth.getDeg(1.0F), CockpitTA_152H_EZ42.this.fm.Or.azimut());
                }
                CockpitTA_152H_EZ42.this.w.set(CockpitTA_152H_EZ42.this.fm.getW());
                CockpitTA_152H_EZ42.this.fm.Or.transform(CockpitTA_152H_EZ42.this.w);
                CockpitTA_152H_EZ42.this.setNew.turn = ((12F * CockpitTA_152H_EZ42.this.setOld.turn) + CockpitTA_152H_EZ42.this.w.z) / 13F;
                CockpitTA_152H_EZ42.this.setNew.beaconDirection = ((10F * CockpitTA_152H_EZ42.this.setOld.beaconDirection) + CockpitTA_152H_EZ42.this.getBeaconDirection()) / 11F;
                CockpitTA_152H_EZ42.this.setNew.beaconRange = ((10F * CockpitTA_152H_EZ42.this.setOld.beaconRange) + CockpitTA_152H_EZ42.this.getBeaconRange()) / 11F;
                float f1 = ((TA_152H1) CockpitTA_152H_EZ42.this.aircraft()).k14Distance;
                CockpitTA_152H_EZ42.this.setNew.k14w = (5F * CockpitTA_152H_EZ42.k14TargetWingspanScale[((TA_152H1) CockpitTA_152H_EZ42.this.aircraft()).k14WingspanType]) / f1;
                CockpitTA_152H_EZ42.this.setNew.k14w = (0.9F * CockpitTA_152H_EZ42.this.setOld.k14w) + (0.1F * CockpitTA_152H_EZ42.this.setNew.k14w);
                CockpitTA_152H_EZ42.this.setNew.k14wingspan = (0.9F * CockpitTA_152H_EZ42.this.setOld.k14wingspan) + (0.1F * CockpitTA_152H_EZ42.k14TargetMarkScale[((TA_152H1) CockpitTA_152H_EZ42.this.aircraft()).k14WingspanType]);
                CockpitTA_152H_EZ42.this.setNew.k14mode = (0.8F * CockpitTA_152H_EZ42.this.setOld.k14mode) + (0.2F * ((TA_152H1) CockpitTA_152H_EZ42.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitTA_152H_EZ42.this.aircraft().FM.getW();
                double d = 0.00125D * f1;
                float f2 = (float) Math.toDegrees(d * vector3d.z);
                float f3 = -(float) Math.toDegrees(d * vector3d.y);
                float f4 = CockpitTA_152H_EZ42.this.floatindex((f1 - 200F) * 0.04F, CockpitTA_152H_EZ42.k14BulletDrop) - CockpitTA_152H_EZ42.k14BulletDrop[0];
                f3 += (float) Math.toDegrees(Math.atan(f4 / f1));
                CockpitTA_152H_EZ42.this.setNew.k14x = (0.92F * CockpitTA_152H_EZ42.this.setOld.k14x) + (0.08F * f2);
                CockpitTA_152H_EZ42.this.setNew.k14y = (0.92F * CockpitTA_152H_EZ42.this.setOld.k14y) + (0.08F * f3);
                if (CockpitTA_152H_EZ42.this.setNew.k14x > 7F) {
                    CockpitTA_152H_EZ42.this.setNew.k14x = 7F;
                }
                if (CockpitTA_152H_EZ42.this.setNew.k14x < -7F) {
                    CockpitTA_152H_EZ42.this.setNew.k14x = -7F;
                }
                if (CockpitTA_152H_EZ42.this.setNew.k14y > 7F) {
                    CockpitTA_152H_EZ42.this.setNew.k14y = 7F;
                }
                if (CockpitTA_152H_EZ42.this.setNew.k14y < -7F) {
                    CockpitTA_152H_EZ42.this.setNew.k14y = -7F;
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
        float      turn;
        float      beaconDirection;
        float      beaconRange;
        float      vspeed;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitTA_152H_EZ42() {
        super("3DO/Cockpit/Ta-152H-1/hier_EZ42.him", "bf109");
        this.gun = new Gun[4];
        this.bomb = new BulletEmitter[4];
        AircraftLH.printCompassHeading = true;
        ((AircraftLH) this.aircraft()).bWantBeaconKeys = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.w = new Vector3f();
        this.setNew.dimPosition = 1.0F;
        this.bNeedSetUp = true;
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
        this.cockpitNightMats = (new String[] { "D9GP1", "D9GP2", "Ta152GP3", "A5GP3Km", "Ta152GP4_MW50", "Ta152GP5", "A4GP6", "Ta152Trans2", "D9GP2", "Trans4", "Ta152Panel3", "Ta152EQpt5" });
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        int i = ((TA_152H1) this.aircraft()).k14Mode;
        boolean flag = i < 2;
        this.mesh.chunkVisible("Z_Z_RETICLE", flag);
        flag = i > 0;
        this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
        this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.setNew.k14w;
        for (int j = 1; j < 7; j++) {
            this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
            this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
        }

        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.bomb[0] == null) {
            for (int k = 0; k < this.bomb.length; k++) {
                this.bomb[k] = GunEmpty.get();
            }

            if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05") != GunEmpty.get()) {
                this.bomb[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
                this.bomb[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
            } else if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02") != GunEmpty.get()) {
                this.bomb[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
                this.bomb[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
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
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.48F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.01F);
        Cockpit.ypr[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, -2F);
        this.mesh.chunkSetLocate("CanopyTop", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("EjectatorHandle", 0.0F, 0.0F, 2520F * this.fm.CT.getCockpitDoor());
        this.mesh.chunkSetAngles("CanopyFrameL", -this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CanopyFrameR", this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.GearControl == 0.0F) {
            Cockpit.xyz[2] = 0.02F;
        }
        this.mesh.chunkSetLocate("FahrHandle", Cockpit.xyz, Cockpit.ypr);
        if (((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x80) == 0)) {
            this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 13000F, 0.0F, 4680F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleALT1", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 13000F, 0.0F, 468F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleALTKm", 0.0F, 0.0F, this.cvt(Atmosphere.pressure((float) this.fm.Loc.z), 0.0F, 100000F, 0.0F, 180F));
        }
        this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), CockpitTA_152H_EZ42.speedometerScale), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitTA_152H_EZ42.rpmScale), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.92F, 0.0F, 400F, 0.0F, 4F), CockpitTA_152H_EZ42.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleWaterTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleOilTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleAirPress", this.cvt(this.fm.EI.engines[0].getRPM() / 1500F, 0.0F, 15F, 0.0F, 200F), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 2) == 0) && ((this.fm.AS.astateCockpitState & 1) == 0) && ((this.fm.AS.astateCockpitState & 4) == 0)) {
            this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt(this.fm.EI.engines[0].getRPM() / 1500F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
            if (this.fm.getOverload() < 0.0F) {
                this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (this.fm.EI.engines[0].getRPM() / 1300F) + (this.fm.getOverload() * 2.0F), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (this.fm.EI.engines[0].getRPM() / 1300F) + (this.fm.getOverload() / 2.0F), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
            }
            if (this.fm.EI.engines[0].getRPM() < 200F) {
                this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(0.0F, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
            }
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleAHTurn", 0.0F, this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
            this.mesh.chunkSetAngles("NeedleAHBank", 0.0F, -this.cvt(this.getBall(6D), -6F, 6F, 11F, -11F), 0.0F);
            this.mesh.chunkSetAngles("NeedleAHCyl", 0.0F, 0.0F, this.fm.Or.getKren());
            this.mesh.chunkSetAngles("NeedleAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 12F, -12F));
        }
        if (((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x80) == 0)) {
            this.mesh.chunkSetAngles("NeedleCD", this.setNew.vspeed >= 0.0F ? -this.floatindex(this.cvt(this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitTA_152H_EZ42.vsiNeedleScale) : this.floatindex(this.cvt(-this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitTA_152H_EZ42.vsiNeedleScale), 0.0F, 0.0F);
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            if (this.useRealisticNavigationInstruments()) {
                this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.azimuth.getDeg(f) + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            }
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedleAirTemp", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 313.09F, -8.5F, 87F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.gun[0] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG17_2", this.gun[0].haveBullets());
            if (this.gun[1] != null) {
                Cockpit.xyz[0] = this.cvt(this.gun[1].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
                this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
                this.mesh.chunkVisible("XLampMG17_1", this.gun[1].haveBullets());
            }
            if (this.gun[2] != null) {
                Cockpit.xyz[0] = this.cvt(this.gun[2].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
                this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
                this.mesh.chunkVisible("XLampMG151_1", this.gun[2].haveBullets());
            }
            this.mesh.chunkSetAngles("IgnitionSwitch", 24F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Revi16TinterEZ42", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("EZ42Filter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -85F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("EZ42FLever", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("EZ42Range", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("EZ42Size", this.cvt(this.interp(-this.setNew.k14wingspan / 106F, -this.setOld.k14wingspan / 106F, f), 0.0F, 1.0F, 36.5F, -57.2F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 20F);
            this.resetYPRmodifier();
            if (this.fm.CT.saveWeaponControl[1]) {
                Cockpit.xyz[2] = -0.004F;
            }
            this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
            float f2 = 0.0F;
            if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1] || this.fm.CT.saveWeaponControl[3]) {
                if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1]) {
                    f2 = 20F;
                }
            }
            this.mesh.chunkSetAngles("SecTrigger2", -(240F + f2) * (this.pictClap = (0.85F * this.pictClap) + (0.15F * i)), 0.0F, 0.0F);
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
            this.mesh.chunkVisible("XLampTankSwitch", this.fm.EI.engines[0].getStage() > 0.0F);
            this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 43.2F);
            this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05F);
            this.mesh.chunkVisible("XLampGearL_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05F);
            this.mesh.chunkVisible("XLampGearR_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("XLampGearC_1", this.fm.CT.getFlap() < 0.1F);
            this.mesh.chunkVisible("XLampGearC_3", (this.fm.CT.getFlap() > 0.1F) && (this.fm.CT.getFlap() < 0.5F));
            this.mesh.chunkVisible("XLampGearC_2", this.fm.CT.getFlap() > 0.9F);
            this.resetYPRmodifier();
            Cockpit.xyz[2] = this.fm.CT.GearControl < 0.5F ? 0.02F : 0.0F;
            this.mesh.chunkSetAngles("NeedleNahe1", this.cvt(this.setNew.beaconDirection, -45F, 45F, 20F, -20F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleNahe2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -20F, 20F), 0.0F, 0.0F);
            this.mesh.chunkVisible("AFN2_RED", this.isOnBlindLandingMarker());
            if (this.fm.EI.engines[0].getControlAfterburner() && (this.fm.getAltitude() < 9000F)) {
                this.mesh.chunkSetAngles("SwitchMW50", -23F, 0.0F, 0.0F);
                this.mesh.chunkVisible("XLampMW50", true);
            } else {
                this.mesh.chunkSetAngles("SwitchMW50", 23F, 0.0F, 0.0F);
                this.mesh.chunkVisible("XLampMW50", false);
            }
            if (this.fm.EI.engines[0].getControlAfterburner() && (this.fm.getAltitude() > 9000F)) {
                this.mesh.chunkSetAngles("SwitchGM1", -23F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("SwitchGM1", 23F, 0.0F, 0.0F);
            }
            if (this.fm.getAltitude() > 8990F) {
                this.mesh.chunkVisible("XLampAlt", true);
            } else {
                this.mesh.chunkVisible("XLampAlt", false);
            }
            float f3 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F;
            if (f3 < -16F) {
                this.mesh.chunkVisible("XLampPitot", true);
            } else if (f3 > -12F) {
                this.mesh.chunkVisible("XLampPitot", false);
            }
        }
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
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0) || ((this.fm.AS.astateCockpitState & 4) != 0)) {
            this.mesh.chunkVisible("EZ42", false);
            this.mesh.chunkVisible("Revi16TinterEZ42", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Z_Z_RETICLEEZ42", false);
            this.mesh.chunkVisible("DEZ42", true);
            this.mesh.materialReplace("D9GP1", "DD9GP1");
            this.mesh.materialReplace("D9GP1_night", "DD9GP1_night");
            this.mesh.chunkVisible("NeedleManPress", false);
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("RepeaterOuter", false);
            this.mesh.chunkVisible("RepeaterPlane", false);
        }
        if (((this.fm.AS.astateCockpitState & 0x40) != 0) || ((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.materialReplace("D9GP2", "DD9GP2");
            this.mesh.materialReplace("D9GP2_night", "DD9GP2_night");
            this.mesh.chunkVisible("NeedleAHCyl", false);
            this.mesh.chunkVisible("NeedleAHBank", false);
            this.mesh.chunkVisible("NeedleAHBar", false);
            this.mesh.chunkVisible("NeedleAHTurn", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.materialReplace("Ta152GP4_MW50", "DTa152GP4");
            this.mesh.materialReplace("Ta152GP4_MW50_night", "DTa152GP4_night");
            this.mesh.chunkVisible("NeedleFuel", false);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = -0.001F;
            Cockpit.xyz[1] = 0.008F;
            Cockpit.xyz[2] = 0.025F;
            Cockpit.ypr[0] = 3F;
            Cockpit.ypr[1] = -6F;
            Cockpit.ypr[2] = 1.5F;
            this.mesh.chunkSetLocate("IPCentral", Cockpit.xyz, Cockpit.ypr);
        }
        if (((this.fm.AS.astateCockpitState & 0x20) != 0) || ((this.fm.AS.astateCockpitState & 0x80) != 0)) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.materialReplace("Ta152GP3", "DTa152GP3");
            this.mesh.materialReplace("Ta152GP3_night", "DTa152GP3_night");
            this.mesh.chunkVisible("NeedleKMH", false);
            this.mesh.chunkVisible("NeedleCD", false);
            this.mesh.chunkVisible("NeedleAlt", false);
            this.mesh.chunkVisible("NeedleAltKM", false);
            this.mesh.materialReplace("Ta152Trans2", "DTa152Trans2");
            this.mesh.materialReplace("Ta152Trans2_night", "DTa152Trans2_night");
            this.mesh.chunkVisible("NeedleWaterTemp", false);
            this.mesh.chunkVisible("NeedleOilTemp", false);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private Gun                gun[];
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            bNeedSetUp;
    private BulletEmitter      bomb[];
    private float              pictAiler;
    private float              pictElev;
    public Vector3f            w;
    private static final float speedometerScale[]       = { 0.0F, 18.5F, 67F, 117F, 164F, 215F, 267F, 320F, 379F, 427F, 428F };
    private static final float rpmScale[]               = { 0.0F, 11.25F, 53F, 108F, 170F, 229F, 282F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]              = { 0.0F, 16F, 35F, 52.5F, 72F, 72F };
    private static final float vsiNeedleScale[]         = { 0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    private float              pictClap;

    static {
        Property.set(CockpitTA_152H_EZ42.class, "normZN", 0.74F);
        Property.set(CockpitTA_152H_EZ42.class, "gsZN", 0.66F);
    }

}
