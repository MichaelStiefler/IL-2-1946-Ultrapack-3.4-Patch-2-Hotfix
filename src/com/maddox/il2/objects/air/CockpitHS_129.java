package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.il2.objects.weapons.MGunBK374Hs129;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitHS_129 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttle1;
        float      throttle2;
        float      flaps;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      turn;
        float      mix1;
        float      mix2;
        float      vspeed;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if ((CockpitHS_129.this.ac != null) && CockpitHS_129.this.ac.bChangedPit) {
                CockpitHS_129.this.reflectPlaneToModel();
                CockpitHS_129.this.ac.bChangedPit = false;
            }
            if (CockpitHS_129.this.bNeedSetUp) {
                CockpitHS_129.this.reflectPlaneMats();
                CockpitHS_129.this.bNeedSetUp = false;
            }
            CockpitHS_129.this.setTmp = CockpitHS_129.this.setOld;
            CockpitHS_129.this.setOld = CockpitHS_129.this.setNew;
            CockpitHS_129.this.setNew = CockpitHS_129.this.setTmp;
            CockpitHS_129.this.setNew.altimeter = CockpitHS_129.this.fm.getAltitude();
            if (!CockpitHS_129.this.cockpitDimControl) {
                if (CockpitHS_129.this.setNew.dimPosition > 0.0F) {
                    CockpitHS_129.this.setNew.dimPosition = CockpitHS_129.this.setNew.dimPosition - 0.05F;
                }
            } else if (CockpitHS_129.this.setNew.dimPosition < 1.0F) {
                CockpitHS_129.this.setNew.dimPosition = CockpitHS_129.this.setNew.dimPosition + 0.05F;
            }
            CockpitHS_129.this.setNew.throttle1 = (0.91F * CockpitHS_129.this.setOld.throttle1) + (0.09F * CockpitHS_129.this.fm.EI.engines[0].getControlThrottle());
            CockpitHS_129.this.setNew.throttle2 = (0.91F * CockpitHS_129.this.setOld.throttle2) + (0.09F * CockpitHS_129.this.fm.EI.engines[1].getControlThrottle());
            CockpitHS_129.this.setNew.mix1 = (0.88F * CockpitHS_129.this.setOld.mix1) + (0.12F * CockpitHS_129.this.fm.EI.engines[0].getControlMix());
            CockpitHS_129.this.setNew.mix2 = (0.88F * CockpitHS_129.this.setOld.mix2) + (0.12F * CockpitHS_129.this.fm.EI.engines[1].getControlMix());
            CockpitHS_129.this.pictManifold1 = (0.75F * CockpitHS_129.this.pictManifold1) + (0.25F * CockpitHS_129.this.fm.EI.engines[0].getManifoldPressure());
            CockpitHS_129.this.pictManifold2 = (0.75F * CockpitHS_129.this.pictManifold2) + (0.25F * CockpitHS_129.this.fm.EI.engines[1].getManifoldPressure());
            if ((CockpitHS_129.this.gearsLever != 0.0F) && (CockpitHS_129.this.gears == CockpitHS_129.this.fm.CT.getGear())) {
                CockpitHS_129.this.gearsLever = CockpitHS_129.this.gearsLever * 0.8F;
                if (Math.abs(CockpitHS_129.this.gearsLever) < 0.1F) {
                    CockpitHS_129.this.gearsLever = 0.0F;
                }
            } else if (CockpitHS_129.this.gears < CockpitHS_129.this.fm.CT.getGear()) {
                CockpitHS_129.this.gears = CockpitHS_129.this.fm.CT.getGear();
                CockpitHS_129.this.gearsLever = CockpitHS_129.this.gearsLever + 2.0F;
                if (CockpitHS_129.this.gearsLever > 14F) {
                    CockpitHS_129.this.gearsLever = 14F;
                }
            } else if (CockpitHS_129.this.gears > CockpitHS_129.this.fm.CT.getGear()) {
                CockpitHS_129.this.gears = CockpitHS_129.this.fm.CT.getGear();
                CockpitHS_129.this.gearsLever = CockpitHS_129.this.gearsLever - 2.0F;
                if (CockpitHS_129.this.gearsLever < -14F) {
                    CockpitHS_129.this.gearsLever = -14F;
                }
            }
            float f = CockpitHS_129.this.waypointAzimuth();
            if (CockpitHS_129.this.useRealisticNavigationInstruments()) {
                CockpitHS_129.this.setNew.waypointAzimuth.setDeg(f);
                CockpitHS_129.this.setOld.waypointAzimuth.setDeg(f);
            } else {
                CockpitHS_129.this.setNew.waypointAzimuth.setDeg(CockpitHS_129.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHS_129.this.setOld.azimuth.getDeg(1.0F));
            }
            if (Math.abs(CockpitHS_129.this.fm.Or.getKren()) < 30F) {
                CockpitHS_129.this.setNew.azimuth.setDeg(CockpitHS_129.this.setOld.azimuth.getDeg(1.0F), CockpitHS_129.this.fm.Or.azimut());
            }
            CockpitHS_129.this.w.set(CockpitHS_129.this.fm.getW());
            CockpitHS_129.this.fm.Or.transform(CockpitHS_129.this.w);
            CockpitHS_129.this.setNew.turn = ((12F * CockpitHS_129.this.setOld.turn) + CockpitHS_129.this.w.z) / 13F;
            CockpitHS_129.this.setNew.vspeed = ((199F * CockpitHS_129.this.setOld.vspeed) + CockpitHS_129.this.fm.getVertSpeed()) / 200F;
            CockpitHS_129.this.setNew.beaconDirection = ((10F * CockpitHS_129.this.setOld.beaconDirection) + CockpitHS_129.this.getBeaconDirection()) / 11F;
            CockpitHS_129.this.setNew.beaconRange = ((10F * CockpitHS_129.this.setOld.beaconRange) + CockpitHS_129.this.getBeaconRange()) / 11F;
            f = CockpitHS_129.this.fm.CT.FlapsControl;
            float f1 = 0.0F;
            if (f < 0.2F) {
                f1 = 1.5F;
            } else if (f < 0.3333333F) {
                f1 = 2.0F;
            } else {
                f1 = 1.0F;
            }
            CockpitHS_129.this.setNew.flaps = (0.91F * CockpitHS_129.this.setOld.flaps) + (0.09F * f * f1);
            if (CockpitHS_129.this.MG17s[0] != null) {
                if ((CockpitHS_129.this.MG17s[0].countBullets() == 0) || (CockpitHS_129.this.oldbullets1 != CockpitHS_129.this.MG17s[0].countBullets())) {
                    CockpitHS_129.this.oldbullets1 = CockpitHS_129.this.MG17s[0].countBullets();
                    CockpitHS_129.this.gunLight1 = true;
                } else {
                    CockpitHS_129.this.gunLight1 = false;
                }
                if ((CockpitHS_129.this.MG17s[1].countBullets() == 0) || (CockpitHS_129.this.oldbullets2 != CockpitHS_129.this.MG17s[1].countBullets())) {
                    CockpitHS_129.this.oldbullets2 = CockpitHS_129.this.MG17s[1].countBullets();
                    CockpitHS_129.this.gunLight2 = true;
                } else {
                    CockpitHS_129.this.gunLight2 = false;
                }
                if ((CockpitHS_129.this.MG17s[2].countBullets() == 0) || (CockpitHS_129.this.oldbullets3 != CockpitHS_129.this.MG17s[2].countBullets())) {
                    CockpitHS_129.this.oldbullets3 = CockpitHS_129.this.MG17s[2].countBullets();
                    CockpitHS_129.this.gunLight3 = true;
                } else {
                    CockpitHS_129.this.gunLight3 = false;
                }
                if ((CockpitHS_129.this.MG17s[3].countBullets() == 0) || (CockpitHS_129.this.oldbullets4 != CockpitHS_129.this.MG17s[3].countBullets())) {
                    CockpitHS_129.this.oldbullets4 = CockpitHS_129.this.MG17s[3].countBullets();
                    CockpitHS_129.this.gunLight4 = true;
                } else {
                    CockpitHS_129.this.gunLight4 = false;
                }
            }
            if (CockpitHS_129.this.bombs[0] != null) {
                CockpitHS_129.this.mesh.chunkVisible("ZFlare_Bombs_01", CockpitHS_129.this.bombs[0].haveBullets());
            }
            if (CockpitHS_129.this.bombs[1] != null) {
                CockpitHS_129.this.mesh.chunkVisible("ZFlare_Bombs_02", CockpitHS_129.this.bombs[1].haveBullets());
            }
            if (CockpitHS_129.this.bombs[2] != null) {
                CockpitHS_129.this.mesh.chunkVisible("ZFlare_Bombs_04", CockpitHS_129.this.bombs[2].haveBullets());
            }
            if (CockpitHS_129.this.bombs[3] != null) {
                CockpitHS_129.this.mesh.chunkVisible("ZFlare_Bombs_03", CockpitHS_129.this.bombs[3].haveBullets());
            }
            if (CockpitHS_129.this.bombs[4] != null) {
                CockpitHS_129.this.mesh.chunkVisible("ZFlare_Bombs_06", CockpitHS_129.this.bombs[4].haveBullets());
            }
            if (CockpitHS_129.this.bombs[5] != null) {
                CockpitHS_129.this.mesh.chunkVisible("ZFlare_Bombs_05", CockpitHS_129.this.bombs[5].haveBullets());
            }
            if (CockpitHS_129.this.fm.CT.getStepControlAuto(0)) {
                if (CockpitHS_129.this.engine1PitchMode < 1.0F) {
                    CockpitHS_129.this.engine1PitchMode = CockpitHS_129.this.engine1PitchMode + 0.5F;
                }
            } else if (CockpitHS_129.this.engine1PitchMode > -1F) {
                CockpitHS_129.this.engine1PitchMode = CockpitHS_129.this.engine1PitchMode - 0.5F;
            }
            if (CockpitHS_129.this.fm.CT.getStepControlAuto(1)) {
                if (CockpitHS_129.this.engine2PitchMode < 1.0F) {
                    CockpitHS_129.this.engine2PitchMode = CockpitHS_129.this.engine2PitchMode + 0.5F;
                }
            } else if (CockpitHS_129.this.engine2PitchMode > -1F) {
                CockpitHS_129.this.engine2PitchMode = CockpitHS_129.this.engine2PitchMode - 0.5F;
            }
            CockpitHS_129.this.magneto1 = (0.5F * CockpitHS_129.this.magneto1) + (0.5F * CockpitHS_129.this.fm.EI.engines[0].getControlMagnetos());
            CockpitHS_129.this.magneto2 = (0.5F * CockpitHS_129.this.magneto2) + (0.5F * CockpitHS_129.this.fm.EI.engines[1].getControlMagnetos());
            CockpitHS_129.this.engine1PropPitch = (0.5F * CockpitHS_129.this.engine1PropPitch) + (0.5F * CockpitHS_129.this.fm.EI.engines[0].getControlPropDelta());
            CockpitHS_129.this.engine2PropPitch = (0.5F * CockpitHS_129.this.engine2PropPitch) + (0.5F * CockpitHS_129.this.fm.EI.engines[1].getControlPropDelta());
            if (CockpitHS_129.this.fm.CT.trimElevator < CockpitHS_129.this.currentElevatorTrim) {
                CockpitHS_129.this.etDelta = -1;
            } else if (CockpitHS_129.this.fm.CT.trimElevator > CockpitHS_129.this.currentElevatorTrim) {
                CockpitHS_129.this.etDelta = 1;
            }
            if ((CockpitHS_129.this.elevatorTrim > 1.0F) || (CockpitHS_129.this.elevatorTrim < -1F)) {
                CockpitHS_129.this.etDelta = 0;
            }
            if (CockpitHS_129.this.etDelta != 0) {
                CockpitHS_129.this.elevatorTrim = CockpitHS_129.this.elevatorTrim + (CockpitHS_129.this.etDelta * 0.2F);
            } else if (CockpitHS_129.this.currentElevatorTrim == CockpitHS_129.this.fm.CT.trimElevator) {
                CockpitHS_129.this.elevatorTrim = CockpitHS_129.this.elevatorTrim * 0.5F;
            }
            CockpitHS_129.this.currentElevatorTrim = CockpitHS_129.this.fm.CT.trimElevator;
            if (CockpitHS_129.this.fm.CT.trimRudder < CockpitHS_129.this.currentRudderTrim) {
                CockpitHS_129.this.rtDelta = 1;
            } else if (CockpitHS_129.this.fm.CT.trimRudder > CockpitHS_129.this.currentRudderTrim) {
                CockpitHS_129.this.rtDelta = -1;
            }
            if ((CockpitHS_129.this.rudderTrim > 1.0F) || (CockpitHS_129.this.rudderTrim < -1F)) {
                CockpitHS_129.this.rtDelta = 0;
            }
            if (CockpitHS_129.this.rtDelta != 0) {
                CockpitHS_129.this.rudderTrim = CockpitHS_129.this.rudderTrim + (CockpitHS_129.this.rtDelta * 0.2F);
            } else if (CockpitHS_129.this.currentRudderTrim == CockpitHS_129.this.fm.CT.trimRudder) {
                CockpitHS_129.this.rudderTrim = CockpitHS_129.this.rudderTrim * 0.5F;
            }
            CockpitHS_129.this.currentRudderTrim = CockpitHS_129.this.fm.CT.trimRudder;
            CockpitHS_129.this.buzzerFX((CockpitHS_129.this.fm.CT.getGear() < 0.999999F) && (CockpitHS_129.this.fm.CT.getFlap() > 0.0F));
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
        if (hiermesh.isChunkVisible("engine1_d2") || hiermesh.isChunkVisible("WingLIn_D2") || hiermesh.isChunkVisible("WingLIn_D3")) {
            this.mesh.chunkVisible("gaugesEx_01", false);
            this.mesh.chunkVisible("D_gaugesEx_01", true);
            this.mesh.chunkVisible("Z_need_RPM_01", false);
            this.mesh.chunkVisible("ZFlare_Fuel_01", false);
            this.mesh.chunkVisible("Z_need_temp_01", false);
            this.mesh.chunkVisible("Z_fuel_01", false);
            this.mesh.chunkVisible("Z_need_oilpress_b_01", false);
            this.mesh.chunkVisible("Z_need_oilpress_a_01", false);
            this.mesh.chunkVisible("Z_need_oilsystem", false);
        }
        if (hiermesh.isChunkVisible("engine2_d2") || hiermesh.isChunkVisible("WingRIn_D2") || hiermesh.isChunkVisible("WingRIn_D3")) {
            this.mesh.chunkVisible("gaugesEx_02", false);
            this.mesh.chunkVisible("D_gaugesEx_02", true);
            this.mesh.chunkVisible("Z_need_RPM_02", false);
            this.mesh.chunkVisible("ZFlare_Fuel_02", false);
            this.mesh.chunkVisible("Z_need_temp_02", false);
            this.mesh.chunkVisible("Z_fuel_02", false);
            this.mesh.chunkVisible("Z_need_oilpress_b_02", false);
            this.mesh.chunkVisible("Z_need_oilpress_a_02", false);
        }
        if (!hiermesh.isChunkVisible("WingLIn_D0") && !hiermesh.isChunkVisible("WingLIn_D1") && !hiermesh.isChunkVisible("WingLIn_D2") && !hiermesh.isChunkVisible("WingLIn_D3")) {
            this.mesh.chunkVisible("gaugesEx_01", false);
            this.mesh.chunkVisible("D_gaugesEx_01", false);
            this.mesh.chunkVisible("Z_need_RPM_01", false);
            this.mesh.chunkVisible("ZFlare_Fuel_01", false);
            this.mesh.chunkVisible("Z_need_temp_01", false);
            this.mesh.chunkVisible("Z_fuel_01", false);
            this.mesh.chunkVisible("Z_need_oilpress_b_01", false);
            this.mesh.chunkVisible("Z_need_oilpress_a_01", false);
            this.mesh.chunkVisible("Z_need_oilsystem", false);
        }
        if (!hiermesh.isChunkVisible("WingRIn_D0") && !hiermesh.isChunkVisible("WingRIn_D1") && !hiermesh.isChunkVisible("WingRIn_D2") && !hiermesh.isChunkVisible("WingRIn_D3")) {
            this.mesh.chunkVisible("gaugesEx_02", false);
            this.mesh.chunkVisible("D_gaugesEx_02", false);
            this.mesh.chunkVisible("Z_need_RPM_02", false);
            this.mesh.chunkVisible("ZFlare_Fuel_02", false);
            this.mesh.chunkVisible("Z_need_temp_02", false);
            this.mesh.chunkVisible("Z_fuel_02", false);
            this.mesh.chunkVisible("Z_need_oilpress_b_02", false);
            this.mesh.chunkVisible("Z_need_oilpress_a_02", false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public CockpitHS_129(String s) {
        super(s, "bf109");
        this.ammoCountGun0 = 0.0F;
        this.ammoCountGun1 = 0.0F;
        this.ammoCountGun2 = 0.0F;
        this.ammoCountGun3 = 0.0F;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManifold1 = 0.0F;
        this.pictManifold2 = 0.0F;
        this.bNeedSetUp = true;
        this.oilPressure1 = 0.0F;
        this.rpmGeneratedPressure1 = 0.0F;
        this.oilPressure2 = 0.0F;
        this.rpmGeneratedPressure2 = 0.0F;
        this.ac = null;
        this.gearsLever = 0.0F;
        this.gears = 0.0F;
        this.oldbullets1 = -1;
        this.oldbullets2 = -1;
        this.oldbullets3 = -1;
        this.oldbullets4 = -1;
        this.gunLight1 = false;
        this.gunLight2 = false;
        this.gunLight3 = false;
        this.gunLight4 = false;
        this.cannonLight = false;
        this.shotTime = -1L;
        this.reloadTimeNeeded = 0L;
        this.engine1PropPitch = 0.0F;
        this.engine2PropPitch = 0.0F;
        this.engine1PitchMode = 0.0F;
        this.engine2PitchMode = 0.0F;
        this.magneto1 = 0.0F;
        this.magneto2 = 0.0F;
        this.elevatorTrim = 0.0F;
        this.currentElevatorTrim = 0.0F;
        this.etDelta = 0;
        this.rudderTrim = 0.0F;
        this.currentRudderTrim = 0.0F;
        this.rtDelta = 0;
        this.tClap = -1;
        this.pictClap = 0.0F;
        this.selectorAngle = 10F;
        this.isSlideRight = false;
        this.setNew.dimPosition = 0.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.cockpitNightMats = (new String[] { "gauges_1_TR", "gauges_2_TR", "gauges_3_TR", "gauges_4_TR", "gauges_6_TR", "gauges_7_TR", "D_gauges_1_TR", "D_gauges_2_TR", "D_gauges_3_TR", "D_gauges_4_TR", "D_gauges_5_TR", "D_gauges_7_TR", "equip01_TR", "equip03_TR" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.randomizeGlassDamage();
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        this.ac = (HS_129) this.aircraft();
        this.ac.registerPit(this);
        try {
            if (!(this.ac.getGunByHookName("_CANNON01") instanceof GunEmpty)) {
                this.mainGuns[0] = this.ac.getGunByHookName("_CANNON01");
                this.mainGuns[3] = this.ac.getGunByHookName("_CANNON02");
                this.ammoCountGun0 = 500F;
                this.ammoCountGun3 = 500F;
            }
        } catch (Exception exception) {
        }
        try {
            if (!(this.ac.getGunByHookName("_MGUN01") instanceof GunEmpty)) {
                if (this.ac instanceof HS_129B2) {
                    this.mainGuns[1] = this.ac.getGunByHookName("_MGUN01");
                    this.mainGuns[2] = this.ac.getGunByHookName("_MGUN02");
                    this.ammoCountGun1 = 1000F;
                    this.ammoCountGun2 = 1000F;
                } else {
                    this.mainGuns[0] = this.ac.getGunByHookName("_MGUN01");
                    this.mainGuns[2] = this.ac.getGunByHookName("_MGUN02");
                    this.ammoCountGun0 = 500F;
                    this.ammoCountGun2 = 500F;
                }
            }
        } catch (Exception exception1) {
        }
        try {
            if (!(this.ac.getGunByHookName("_MGUN03") instanceof GunEmpty)) {
                this.MG17s[0] = this.ac.getGunByHookName("_MGUN03");
                this.MG17s[1] = this.ac.getGunByHookName("_MGUN04");
                this.MG17s[2] = this.ac.getGunByHookName("_MGUN05");
                this.MG17s[3] = this.ac.getGunByHookName("_MGUN06");
                this.mesh.chunkVisible("X_4xMG17_gauge", true);
                this.mesh.chunkVisible("X_noExGuns", false);
            }
        } catch (Exception exception2) {
        }
        try {
            if (!(this.ac.getGunByHookName("_CANNON03") instanceof GunEmpty)) {
                this.cannon = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
                this.reloadTimeNeeded = 131L;
                this.oldbullets1 = this.cannon.countBullets();
                this.mesh.chunkVisible("X_biggun_light", true);
                this.mesh.chunkVisible("X_noExGuns", false);
            }
        } catch (Exception exception3) {
        }
        try {
            if (!(this.ac.getGunByHookName("_CANNON04") instanceof GunEmpty) && (this.cannon == null)) {
                this.cannon = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
                this.reloadTimeNeeded = 43L;
                this.oldbullets1 = this.cannon.countBullets();
                this.mesh.chunkVisible("X_biggun_light", true);
                this.mesh.chunkVisible("X_noExGuns", false);
            }
        } catch (Exception exception4) {
        }
        try {
            if (!(this.ac.getGunByHookName("_HEAVYCANNON01") instanceof GunEmpty) && (this.cannon == null)) {
                this.cannon = ((Aircraft) this.fm.actor).getGunByHookName("_HEAVYCANNON01");
                if (this.cannon instanceof MGunBK374Hs129) {
                    this.reloadTimeNeeded = 700L;
                } else {
                    this.mainGuns[1] = this.ac.getGunByHookName("_HEAVYCANNON01");
                    this.cannon = null;
                    this.ammoCountGun1 = 16F;
                }
                this.oldbullets1 = this.cannon.countBullets();
                this.mesh.chunkVisible("X_biggun_light", true);
                this.mesh.chunkVisible("X_noExGuns", false);
            }
        } catch (Exception exception5) {
        }
        try {
            this.bombs[0] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb07");
        } catch (Exception exception6) {
        }
        try {
            this.bombs[1] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb08");
        } catch (Exception exception7) {
        }
        try {
            this.bombs[2] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
        } catch (Exception exception8) {
        }
        try {
            this.bombs[3] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb06");
        } catch (Exception exception9) {
        }
        try {
            this.bombs[4] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
        } catch (Exception exception10) {
        }
        try {
            this.bombs[5] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
        } catch (Exception exception11) {
        }
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        float f1 = this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.09F * f1;
        this.mesh.chunkSetLocate("Z_revidim_01", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_revidim_02", 0.0F, f1 * -23F, 0.0F);
        this.mesh.chunkSetAngles("Z_revidim_03", 0.0F, f1 * 23F, 0.0F);
        this.mesh.chunkVisible("ZFlare_Gear_03", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("ZFlare_Gear_04", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("ZFlare_Gear_01", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("ZFlare_Gear_02", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("ZFlare_Fuel_01", this.fm.M.fuel < 36F);
        this.mesh.chunkVisible("ZFlare_Fuel_02", this.fm.M.fuel < 36F);
        this.mesh.chunkSetAngles("stick", -14F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.ElevatorControl)), 14F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[3]) {
            Cockpit.xyz[2] = -0.003F;
        }
        this.mesh.chunkSetLocate("Z_trigger_02", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        float f2 = 0.0F;
        int i = 0;
        if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1] || this.fm.CT.saveWeaponControl[3]) {
            if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1]) {
                f2 = 20F;
            }
            this.tClap = Time.tickCounter() + World.Rnd().nextInt(500, 1000);
            if (this.fm.CT.saveWeaponControl[0] && !this.fm.CT.saveWeaponControl[1]) {
                this.selectorAngle = 24F;
            } else if (!this.fm.CT.saveWeaponControl[0] && this.fm.CT.saveWeaponControl[1]) {
                this.selectorAngle = 43F;
            } else {
                this.selectorAngle = 10F;
            }
        }
        if (Time.tickCounter() < this.tClap) {
            i = 1;
        }
        this.mesh.chunkSetAngles("Z_trigger_01", -(240F + f2) * (this.pictClap = (0.85F * this.pictClap) + (0.15F * i)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_trigger_03", this.selectorAngle, 0.0F, 0.0F);
        Cockpit.xyz[2] = 0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_ruddersupp_L", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -0.05F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_ruddersupp_R", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_rudder_R", this.fm.CT.getBrake() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_L", this.fm.CT.getBrake() * 15F, 0.0F, 0.0F);
        Cockpit.xyz[1] = 0.05F * this.fm.CT.getFlap();
        this.mesh.chunkSetLocate("Z_indicator_flaps", Cockpit.xyz, Cockpit.ypr);
        float f3 = this.interp(this.setNew.throttle1, this.setOld.throttle1, f);
        this.mesh.chunkSetAngles("Z_throttle_01", this.cvt(f3, 0.0F, 1.0F, 0.0F, 42.5F), 0.0F, 0.0F);
        float f4 = this.interp(this.setNew.throttle2, this.setOld.throttle2, f);
        this.mesh.chunkSetAngles("Z_throttle_02", this.cvt(f4, 0.0F, 1.0F, 0.0F, 42.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_wep", this.cvt(Math.max(f3, f4), 1.0F, 1.1F, 0.0F, 18F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_mixture", this.interp(this.setNew.mix1, this.setOld.mix1, f) * 19F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear", -this.gearsLever, 0.0F, 0.0F);
        if (this.gearsLever < -10F) {
            this.mesh.chunkSetAngles("Z_gear_safety", this.cvt(this.gearsLever, -13F, -10F, 0.0F, 30F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_gear_safety", this.cvt(this.gearsLever, -10F, -2.5F, 30F, 0.0F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_flaps", this.interp(this.setNew.flaps, this.setOld.flaps, f) * -28.5F, 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_need_kompass_03", (-this.setNew.azimuth.getDeg(f) - 90F) + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_need_kompass_02", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_need_kompass_02", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_need_kompass_03", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_magn_01", -26F * this.magneto1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_magn_02", -26F * this.magneto2, 0.0F, 0.0F);
        float f10 = 0.046F;
        if (this.mainGuns[0] != null) {
            float f6 = this.cvt(this.mainGuns[0].countBullets(), 0.0F, this.ammoCountGun0, f10, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[1] = -f6;
            this.mesh.chunkSetLocate("Z_ammo_counter_01", Cockpit.xyz, Cockpit.ypr);
        } else {
            this.mesh.chunkVisible("Z_ammo_counter_01", false);
        }
        if (this.mainGuns[1] != null) {
            float f7 = this.cvt(this.mainGuns[1].countBullets(), 0.0F, this.ammoCountGun1, f10, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[1] = -f7;
            this.mesh.chunkSetLocate("Z_ammo_counter_02", Cockpit.xyz, Cockpit.ypr);
        } else {
            this.mesh.chunkVisible("Z_ammo_counter_02", false);
        }
        if (this.mainGuns[2] != null) {
            float f8 = this.cvt(this.mainGuns[2].countBullets(), 0.0F, this.ammoCountGun2, f10, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[1] = -f8;
            this.mesh.chunkSetLocate("Z_ammo_counter_03", Cockpit.xyz, Cockpit.ypr);
        } else {
            this.mesh.chunkVisible("Z_ammo_counter_03", false);
        }
        if (this.mainGuns[3] != null) {
            float f9 = this.cvt(this.mainGuns[3].countBullets(), 0.0F, this.ammoCountGun3, f10, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[1] = -f9;
            this.mesh.chunkSetLocate("Z_ammo_counter_04", Cockpit.xyz, Cockpit.ypr);
        } else if (this.ammoCountGun0 == 1000F) {
            this.mesh.chunkVisible("Z_ammo_counter_04", false);
        }
        if (this.cannon != null) {
            if (this.cannon.countBullets() == 0) {
                this.cannonLight = true;
            } else if (this.oldbullets1 != this.cannon.countBullets()) {
                if (this.shotTime == -1L) {
                    this.shotTime = Time.current();
                    this.cannonLight = true;
                } else if ((Time.current() - this.shotTime) >= this.reloadTimeNeeded) {
                    this.oldbullets1 = this.cannon.countBullets();
                    this.shotTime = -1L;
                    this.cannonLight = false;
                } else {
                    this.cannonLight = true;
                }
            } else {
                this.cannonLight = false;
            }
        }
        if (this.MG17s[0] != null) {
            this.mesh.chunkVisible("ZFlare_Ordn_03", !this.gunLight1);
            this.mesh.chunkVisible("ZFlare_Ordn_01", !this.gunLight2);
            this.mesh.chunkVisible("ZFlare_Ordn_02", !this.gunLight3);
            this.mesh.chunkVisible("ZFlare_Ordn_04", !this.gunLight4);
        }
        if (this.cannon != null) {
            this.mesh.chunkVisible("ZFlare_bigguns", !this.cannonLight);
        }
        this.mesh.chunkSetAngles("Z_need_clock_01", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_clock_02", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_clock_03", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_turnbank_02", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, -21F, 21F), 0.0F, 0.0F);
        float f11 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_turnbank_01", this.cvt(f11, -4F, 4F, 8F, -8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_climb", this.cvt(this.setNew.vspeed, -15F, 15F, 135F, -135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_speed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitHS_129.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_pressure_01", -this.cvt(this.pictManifold1, 0.6F, 1.8F, 15F, 345.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_pressure_02", -this.cvt(this.pictManifold2, 0.6F, 1.8F, 15F, 345.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_alt_01", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_alt_02", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 14000F, 0.0F, 313F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuel_01", -this.cvt(this.fm.M.fuel * 0.4001F, 0.0F, 205F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuel_02", -this.cvt(this.fm.M.fuel * 0.4001F, 0.0F, 205F, 0.0F, 96F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_temp_01", -this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_temp_02", -this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 160F, 0.0F, 332F), 0.0F, 0.0F);
        float f12 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_need_RPM_01", -this.floatindex(this.cvt(f12, 0.0F, 3500F, 0.0F, 10F), CockpitHS_129.rpmScale), 0.0F, 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure1 = this.rpmGeneratedPressure1 - 2.0F;
        } else if (f12 < this.rpmGeneratedPressure1) {
            this.rpmGeneratedPressure1 = this.rpmGeneratedPressure1 - ((this.rpmGeneratedPressure1 - f12) * 0.01F);
        } else {
            this.rpmGeneratedPressure1 = this.rpmGeneratedPressure1 + ((f12 - this.rpmGeneratedPressure1) * 0.001F);
        }
        if (this.rpmGeneratedPressure1 < 800F) {
            this.oilPressure1 = this.cvt(this.rpmGeneratedPressure1, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure1 < 1800F) {
            this.oilPressure1 = this.cvt(this.rpmGeneratedPressure1, 800F, 1800F, 4F, 5F);
        } else {
            this.oilPressure1 = this.cvt(this.rpmGeneratedPressure1, 1800F, 3500F, 5F, 6F);
        }
        float f13 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f13 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f13 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f13 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f14 = f13 * this.fm.EI.engines[0].getReadyness() * this.oilPressure1;
        this.mesh.chunkSetAngles("Z_need_oilpress_b_01", this.cvt(f14, 0.0F, 10F, 0.0F, 140F), 0.0F, 0.0F);
        f12 = this.fm.EI.engines[1].getRPM();
        this.mesh.chunkSetAngles("Z_need_RPM_02", -this.floatindex(this.cvt(f12, 0.0F, 3500F, 0.0F, 10F), CockpitHS_129.rpmScale), 0.0F, 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure2 = this.rpmGeneratedPressure2 - 2.0F;
        } else if (f12 < this.rpmGeneratedPressure2) {
            this.rpmGeneratedPressure2 = this.rpmGeneratedPressure2 - ((this.rpmGeneratedPressure2 - f12) * 0.01F);
        } else {
            this.rpmGeneratedPressure2 = this.rpmGeneratedPressure2 + ((f12 - this.rpmGeneratedPressure2) * 0.001F);
        }
        if (this.rpmGeneratedPressure2 < 800F) {
            this.oilPressure2 = this.cvt(this.rpmGeneratedPressure2, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure2 < 1800F) {
            this.oilPressure2 = this.cvt(this.rpmGeneratedPressure2, 800F, 1800F, 4F, 5F);
        } else {
            this.oilPressure2 = this.cvt(this.rpmGeneratedPressure2, 1800F, 3500F, 5F, 6F);
        }
        f13 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f13 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f13 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f13 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f15 = f13 * this.fm.EI.engines[0].getReadyness() * this.oilPressure2;
        this.mesh.chunkSetAngles("Z_need_oilpress_b_02", this.cvt(f15, 0.0F, 10F, 0.0F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_oilpress_a_01", -this.cvt(this.rpmGeneratedPressure1, 0.0F, 1800F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_oilpress_a_02", -this.cvt(this.rpmGeneratedPressure2, 0.0F, 1800F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_oilsystem", -this.cvt((this.rpmGeneratedPressure1 + this.rpmGeneratedPressure2) / 2.0F, 0.0F, 2000F, 48F, 250F), 0.0F, 0.0F);
        if (!this.ac.sideWindow) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * 1.0F;
            if (Aircraft.xyz[1] < 0.01D) {
                Aircraft.xyz[1] = 0.0F;
            }
            this.mesh.chunkSetLocate("Z_canopy", Aircraft.xyz, Aircraft.ypr);
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = this.fm.CT.getCockpitDoor() * 0.33F;
            if (Aircraft.xyz[1] < 0.01D) {
                Aircraft.xyz[1] = 0.0F;
            }
            if (this.isSlideRight) {
                this.mesh.chunkSetLocate("Sliding_glass_01", Aircraft.xyz, Aircraft.ypr);
                this.mesh.chunkSetAngles("Z_glass_opener01", this.cvt(Aircraft.xyz[1], 0.0F, 0.05F, 0.0F, -27F), 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetLocate("Sliding_glass", Aircraft.xyz, Aircraft.ypr);
                this.mesh.chunkSetAngles("Z_glass_opener", this.cvt(Aircraft.xyz[1], 0.0F, 0.05F, 0.0F, -27F), 0.0F, 0.0F);
            }
        }
        if (this.fm.EI.engines[0].getExtinguishers() == 0) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = 0.05F;
            this.mesh.chunkSetLocate("Z_extinguisher_01", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.fm.EI.engines[1].getExtinguishers() == 0) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[1] = 0.05F;
            this.mesh.chunkSetLocate("Z_extinguisher_02", Aircraft.xyz, Aircraft.ypr);
        }
        this.mesh.chunkSetAngles("Z_need_radio_01", this.cvt(this.setNew.beaconDirection, -45F, 45F, 20F, -20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_radio_02", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -20F, 20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("ZFlare_Radio_01", this.isOnBlindLandingMarker());
        this.mesh.chunkVisible("ZFlare_Pitch_02", (this.fm.EI.engines[0].getElPropPos() < 1.0F) && (this.fm.EI.engines[0].getControlPropDelta() == 1));
        this.mesh.chunkVisible("ZFlare_Pitch_01", (this.fm.EI.engines[0].getElPropPos() > 0.0F) && (this.fm.EI.engines[0].getControlPropDelta() == -1));
        this.mesh.chunkVisible("ZFlare_Pitch_04", (this.fm.EI.engines[1].getElPropPos() < 1.0F) && (this.fm.EI.engines[1].getControlPropDelta() == 1));
        this.mesh.chunkVisible("ZFlare_Pitch_03", (this.fm.EI.engines[1].getElPropPos() > 0.0F) && (this.fm.EI.engines[1].getControlPropDelta() == -1));
        this.mesh.chunkSetAngles("Z_knobPITCH_01", this.engine1PropPitch * -15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_knobPITCH_02", this.engine2PropPitch * -15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_knobPITCH_03", this.engine1PitchMode * 17F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_knobPITCH_04", this.engine2PitchMode * 17F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_elevator_trim", this.elevatorTrim * 24F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_trim", this.rudderTrim * 24F, 0.0F, 0.0F);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.003F, 1.0F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private void randomizeGlassDamage() {
        double d = Math.random();
        if (d < 0.3D) {
            this.mesh.materialReplace("XArmor_glass_DMG", "XArmor_glass_DMG2");
        } else if (d < 0.6D) {
            this.mesh.materialReplace("XArmor_glass_DMG", "XArmor_glass_DMG3");
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Xfront_glass_D0", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassHoles_03", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHoles_01", true);
            this.mesh.chunkVisible("gauges1_d0", false);
            this.mesh.chunkVisible("D_gauges1_d0", true);
            this.mesh.chunkVisible("Z_need_pressure_01", false);
            this.mesh.chunkVisible("Z_need_pressure_02", false);
            this.mesh.chunkVisible("Z_need_clock_03", false);
            this.mesh.chunkVisible("Z_need_clock_02", false);
            this.mesh.chunkVisible("Z_need_clock_01", false);
            this.mesh.chunkVisible("Z_need_speed", false);
            this.mesh.chunkVisible("Z_need_kompass_03", false);
            this.mesh.chunkVisible("Z_need_kompass_02", false);
            this.mesh.chunkVisible("XHoles_01", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassHoles_02", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassHoles_01", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("D_revi", true);
            this.mesh.chunkVisible("D_reviglass", true);
            this.mesh.chunkVisible("D_dimmerglas", true);
            this.mesh.chunkVisible("revi_mounting", false);
            this.mesh.chunkVisible("revi", false);
            this.mesh.chunkVisible("Z_revidim_01", false);
            this.mesh.chunkVisible("Z_revidim_02", false);
            this.mesh.chunkVisible("Z_revidim_03", false);
            this.mesh.chunkVisible("reviglass", false);
            this.mesh.chunkVisible("reticle", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHoles_02", true);
            this.mesh.chunkVisible("gauges2_d0", false);
            this.mesh.chunkVisible("D_gauges2_d0", true);
            this.mesh.chunkVisible("Z_need_climb", false);
            this.mesh.chunkVisible("Z_need_turnbank_02", false);
            this.mesh.chunkVisible("Z_turnbank_01", false);
            this.mesh.chunkVisible("Z_need_alt_01", false);
            this.mesh.chunkVisible("Z_need_alt_02", false);
            this.mesh.chunkVisible("Z_need_radio_02", false);
            this.mesh.chunkVisible("Z_need_radio_01", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            if (this.fm.AS.astateOilStates[0] == 0) {
                this.mesh.chunkVisible("ZOil_01", false);
                this.mesh.chunkVisible("ZOil_03", false);
            } else {
                this.mesh.chunkVisible("ZOil_01", true);
                this.mesh.chunkVisible("ZOil_03", true);
            }
            if (this.fm.AS.astateOilStates[1] == 0) {
                this.mesh.chunkVisible("ZOil_02", false);
                this.mesh.chunkVisible("ZOil_04", false);
            } else {
                this.mesh.chunkVisible("ZOil_02", true);
                this.mesh.chunkVisible("ZOil_04", true);
            }
        }
    }

    protected boolean doFocusEnter() {
        boolean flag = super.doFocusEnter();
        HierMesh hiermesh = this.aircraft().hierMesh();
        hiermesh.chunkVisible("Revi_D0", false);
        return flag;
    }

    protected void doFocusLeave() {
        super.doFocusLeave();
        HierMesh hiermesh = this.aircraft().hierMesh();
        hiermesh.chunkVisible("Revi_D0", true);
    }

    public boolean isViewRight() {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HookPilot.current.computePos(this, loc, loc1);
        float f = loc1.getOrient().getYaw();
        if (f < 0.0F) {
            this.isSlideRight = true;
        } else {
            this.isSlideRight = false;
        }
        return this.isSlideRight;
    }

    private Gun                mainGuns[]         = { null, null, null, null };
    private float              ammoCountGun0;
    private float              ammoCountGun1;
    private float              ammoCountGun2;
    private float              ammoCountGun3;
    private Gun                MG17s[]            = { null, null, null, null };
    private Gun                cannon;
    private BombGun            bombs[]            = { null, null, null, null, null, null };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold1;
    private float              pictManifold2;
    private boolean            bNeedSetUp;
    private LightPointActor    light1;
    private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]         = { 0.0F, 7F, 24.5F, 60F, 99F, 140F, 180.2F, 221.5F, 260F, 297.5F, 334.5F };
    float                      oilPressure1;
    float                      rpmGeneratedPressure1;
    float                      oilPressure2;
    float                      rpmGeneratedPressure2;
    HS_129                     ac;
    private float              gearsLever;
    private float              gears;
    private int                oldbullets1;
    private int                oldbullets2;
    private int                oldbullets3;
    private int                oldbullets4;
    private boolean            gunLight1;
    private boolean            gunLight2;
    private boolean            gunLight3;
    private boolean            gunLight4;
    private boolean            cannonLight;
    private long               shotTime;
    private long               reloadTimeNeeded;
    private float              engine1PropPitch;
    private float              engine2PropPitch;
    private float              engine1PitchMode;
    private float              engine2PitchMode;
    private float              magneto1;
    private float              magneto2;
    private float              elevatorTrim;
    private float              currentElevatorTrim;
    private int                etDelta;
    private float              rudderTrim;
    private float              currentRudderTrim;
    private int                rtDelta;
    private int                tClap;
    private float              pictClap;
    private float              selectorAngle;
    private boolean            isSlideRight;

}
