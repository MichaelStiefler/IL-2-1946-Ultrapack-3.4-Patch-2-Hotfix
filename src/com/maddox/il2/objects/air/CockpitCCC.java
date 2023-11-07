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
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitCCC extends CockpitPilot {
    private class Variables {

        float      throttle1;
        float      mix1;
        float      altimeter;
        float      turn;
        float      compasTangage;
        float      compasKren;
        AnglesFork azimuth;
        float      pictElev;
        float      pictAiler;
        float      pictRudd;
        float      Asbr2;
        float      dimPos;
        float      Airstartr;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCCC.this.fm != null) {
                CockpitCCC.this.setTmp = CockpitCCC.this.setOld;
                CockpitCCC.this.setOld = CockpitCCC.this.setNew;
                CockpitCCC.this.setNew = CockpitCCC.this.setTmp;
                CockpitCCC.this.setNew.pictElev = (0.8F * CockpitCCC.this.setOld.pictElev) + (0.2F * CockpitCCC.this.fm.CT.ElevatorControl);
                CockpitCCC.this.setNew.pictAiler = (0.8F * CockpitCCC.this.setOld.pictAiler) + (2.0F * CockpitCCC.this.fm.CT.AileronControl);
                CockpitCCC.this.setNew.pictRudd = (0.8F * CockpitCCC.this.setOld.pictRudd) + (4F * CockpitCCC.this.fm.CT.getRudder());
                CockpitCCC.this.setNew.throttle1 = (0.9F * CockpitCCC.this.setOld.throttle1) + (0.1F * CockpitCCC.this.fm.EI.engines[0].getControlThrottle());
                CockpitCCC.this.setNew.mix1 = (0.8F * CockpitCCC.this.setOld.mix1) + (0.2F * CockpitCCC.this.fm.EI.engines[0].getControlMix());
                CockpitCCC.this.setNew.altimeter = CockpitCCC.this.fm.getAltitude();
                CockpitCCC.this.w.set(CockpitCCC.this.fm.getW());
                CockpitCCC.this.fm.Or.transform(CockpitCCC.this.w);
                CockpitCCC.this.setNew.turn = ((12F * CockpitCCC.this.setOld.turn) + CockpitCCC.this.w.z) / 13F;
                if (Math.abs(CockpitCCC.this.fm.Or.getKren()) < 30F) {
                    CockpitCCC.this.setNew.azimuth.setDeg(CockpitCCC.this.setOld.azimuth.getDeg(1.0F), CockpitCCC.this.fm.Or.azimut());
                }
                CockpitCCC.this.setNew.compasTangage = (0.95F * CockpitCCC.this.setOld.compasTangage) + (0.05F * CockpitCCC.this.cvt(CockpitCCC.this.fm.Or.getTangage(), -20F, 20F, -20F, 20F));
                CockpitCCC.this.setNew.compasKren = (0.95F * CockpitCCC.this.setOld.compasKren) + (0.05F * CockpitCCC.this.cvt(CockpitCCC.this.fm.Or.getKren(), -20F, 20F, -20F, 20F));
                if (CockpitCCC.this.cockpitDimControl) {
                    if (CockpitCCC.this.setNew.dimPos < 1.0F) {
                        CockpitCCC.this.setNew.dimPos = CockpitCCC.this.setOld.dimPos + 0.05F;
                    }
                } else if (CockpitCCC.this.setNew.dimPos > 0.0F) {
                    CockpitCCC.this.setNew.dimPos = CockpitCCC.this.setOld.dimPos - 0.05F;
                }
            }
            if ((CockpitCCC.this.fm.EI.engines[0].getStage() == 1) || (CockpitCCC.this.fm.EI.engines[0].getStage() == 2)) {
                if (CockpitCCC.this.setNew.Airstartr < 1.0F) {
                    CockpitCCC.this.setNew.Airstartr = CockpitCCC.this.setOld.Airstartr + 0.1F;
                }
                if (CockpitCCC.this.setNew.Airstartr > 1.0F) {
                    CockpitCCC.this.setNew.Airstartr = 1.0F;
                }
            } else {
                if (CockpitCCC.this.setNew.Airstartr > 0.0F) {
                    CockpitCCC.this.setNew.Airstartr = CockpitCCC.this.setOld.Airstartr - 0.1F;
                }
                if (CockpitCCC.this.setNew.Airstartr < 0.0F) {
                    CockpitCCC.this.setNew.Airstartr = 0.0F;
                }
            }
            R_5xyz r_5xyz = (R_5xyz) CockpitCCC.this.aircraft();
            if (r_5xyz.getGunnerAnimation() < 1.0D) {
                CockpitCCC.this.moveGunner();
            } else {
                CockpitCCC.this.mesh.chunkSetAngles("zTurret1A", CockpitCCC.this.aircraft().FM.turret[0].tu[0], 0.0F, 0.0F);
                CockpitCCC.this.mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -CockpitCCC.this.aircraft().FM.turret[0].tu[1]);
            }
            if ((CockpitCCC.this.aircraft().FM.CT.getBombDropMode() == Controls.defaultFire) && CockpitCCC.this.fm.CT.saveWeaponControl[3]) {
                CockpitCCC.this.setNew.Asbr2 = CockpitCCC.this.setOld.Asbr2 = 1.0F;
            } else if (CockpitCCC.this.setNew.Asbr2 > 0.0F) {
                CockpitCCC.this.setNew.Asbr2 = CockpitCCC.this.setOld.Asbr2 - 0.1F;
            } else {
                CockpitCCC.this.setNew.Asbr2 = CockpitCCC.this.setOld.Asbr2 = 0.0F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private void moveGunner() {
        R_5xyz r_5xyz = (R_5xyz) this.aircraft();
        if (r_5xyz.gunnerDead || r_5xyz.gunnerEjected) {
            return;
        }
        if (r_5xyz.getGunnerAnimation() > 0.75D) {
            this.mesh.chunkVisible("Pilot3_D0", false);
            if (r_5xyz.getGunnerAnimation() < 1.0D) {
                Cockpit.xyz[0] = 0.0F;
                Cockpit.xyz[1] = 0.0F;
                Cockpit.xyz[2] = -0.03F;
                Cockpit.ypr[0] = 0.0F;
                Cockpit.ypr[1] = 0.0F;
                Cockpit.ypr[2] = 0.0F;
                this.mesh.chunkSetLocate("Pilot2_D0", Cockpit.xyz, Cockpit.ypr);
            }
            this.mesh.chunkSetAngles("zTurret1A", 0.0F, 0.0F, 0.0F);
            float f = this.cvt(r_5xyz.getGunnerAnimation(), 0.75F, 1.0F, -70F, 0.0F);
            this.mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, f);
        } else if (r_5xyz.getGunnerAnimation() > 0.25D) {
            this.mesh.chunkVisible("Pilot2_D0", true);
            this.mesh.chunkVisible("Pilot3_D0", false);
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = this.cvt(r_5xyz.getGunnerAnimation(), 0.25F, 0.75F, -0.2F, -0.03F);
            Cockpit.ypr[0] = this.cvt(r_5xyz.getGunnerAnimation(), 0.25F, 0.75F, -180F, 0.0F);
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            this.mesh.chunkSetLocate("Pilot2_D0", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkSetAngles("zTurret1A", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -70F);
        } else {
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = this.cvt(r_5xyz.getGunnerAnimation(), 0.0F, 0.25F, 0.0F, 0.11F);
            Cockpit.ypr[0] = 0.0F;
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            this.mesh.chunkSetLocate("Pilot3_D0", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("Pilot3_D0", true);
            this.mesh.chunkVisible("Pilot2_D0", false);
            this.mesh.chunkSetAngles("zTurret1A", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -70F);
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.0D, 0.0D, 0.0D);
    }

    public CockpitCCC() {
        super("3DO/Cockpit/R-5/hier-CCC.him", "u2");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        Aircraft aircraft = this.aircraft();
        try {
            if (!(aircraft.getGunByHookName("_MGUNCCC05") instanceof GunEmpty)) {
                this.Gun5 = aircraft.getGunByHookName("_MGUNCCC05");
            }
        } catch (Exception exception) {
        }
        this.cockpitNightMats = new String[0];
        for (int i = 0; i < 2; i++) {
            HookNamed hooknamed = new HookNamed(this.mesh, "LAMP0" + (i + 1));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            this.lights[i] = new LightPointActor(new LightPoint(), loc.getPoint());
            this.lights[i].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            this.lights[i].light.setEmit(0.0F, 0.0F);
            this.pos.base().draw.lightMap().put("LAMP0" + (i + 1), this.lights[i]);
        }

        this.setNightMats(false);
        this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
        this.tros1 = 0.5F;
        this.tros1Mat = null;
        int j = -1;
        j = this.mesh.materialFind("chain");
        if (j != -1) {
            this.tros1Mat = this.mesh.material(j);
            this.tros1Mat.setLayer(0);
        }
        this.Patrons = 0.5F;
        this.PatronsMat = null;
        j = -1;
        j = this.mesh.materialFind("patron");
        if (j != -1) {
            this.PatronsMat = this.mesh.material(j);
            this.PatronsMat.setLayer(0);
        }
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.03F });
        this.hidePilot = true;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            this.bNeedSetUp = true;
            this.moveGunner();
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Interior_D0", true);
        HierMesh hiermesh = this.aircraft().hierMesh();
        R_5xyz r_5xyz = (R_5xyz) this.aircraft();
        hiermesh.chunkVisible("Turret1A_D0", true);
        if (r_5xyz.getGunnerAnimation() < 1.0D) {
            hiermesh.chunkVisible("Turret1C_D0", false);
            hiermesh.chunkVisible("Turret1D_D0", true);
        } else {
            hiermesh.chunkVisible("Turret1C_D0", true);
            hiermesh.chunkVisible("Turret1D_D0", false);
        }
        super.doFocusLeave();
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        this.mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
        this.mesh.materialReplace("Matt1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_CAP", hiermesh.isChunkVisible("WingRMid_CAP"));
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_CAP", hiermesh.isChunkVisible("WingLMid_CAP"));
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (R_5xyz.bChangedPit) {
            this.reflectPlaneToModel();
            R_5xyz.bChangedPit = false;
        }
        float f1 = this.setNew.pictRudd;
        this.mesh.chunkSetAngles("zPed1", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPed2", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPedalL", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPedalR", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPedTrosL", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPedTrosR", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("StckAiler", this.setNew.pictAiler, 0.0F, 0.0F);
        float f2 = 0.0F;
        if (this.setNew.pictElev > 0.0F) {
            f2 = 21F * this.setNew.pictElev;
        } else {
            f2 = 13F * this.setNew.pictElev;
        }
        this.mesh.chunkSetAngles("Stick", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("StckConnctr", 0.0F, 0.0F, -f2);
        this.mesh.chunkSetAngles("zFireL", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[0] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("zFireR", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[1] ? -15F : 0.0F);
        this.mesh.chunkSetAngles("zFireC", 0.0F, 0.0F, !this.fm.CT.saveWeaponControl[0] || !this.fm.CT.saveWeaponControl[1] ? 0.0F : -15F);
        float f3 = -50F * this.setNew.throttle1;
        this.mesh.chunkSetAngles("zThrotle", 0.0F, 0.0F, f3);
        this.mesh.chunkSetAngles("Cable_throtle01", 0.0F, 0.0F, -f3);
        this.mesh.chunkSetAngles("Cable_throtle02", 0.0F, 0.0F, -f3);
        float f4 = -37.5F * this.setNew.mix1;
        this.mesh.chunkSetAngles("zMixture", 0.0F, 0.0F, f4);
        this.mesh.chunkSetAngles("Cable_mixture01", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Cable_mixture02", 0.0F, 0.0F, -f4);
        float f5 = this.cvt(this.fm.EI.engines[0].getRPM(), 900F, 1200F, 0.0F, -50F);
        this.mesh.chunkSetAngles("zIgnition", 0.0F, 0.0F, f5);
        this.mesh.chunkSetAngles("Cable_Ignition", 0.0F, 0.0F, -f5);
        this.mesh.chunkSetAngles("zSW_Magneto", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -90F), 0.0F);
        this.mesh.chunkSetAngles("zArr_KI6a", 0.0F, -this.setNew.compasKren, -this.setNew.compasTangage);
        this.mesh.chunkSetAngles("zArr_KI6b", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AL2a", 0.0F, -this.setNew.compasKren, -this.setNew.compasTangage);
        this.mesh.chunkSetAngles("Z_AL2b", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        R_5xyz r_5xyz2 = (R_5xyz) this.aircraft();
        this.mesh.chunkSetAngles("Z_AL2c", r_5xyz2.CompassDelta, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Speed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), CockpitCCC.IAS_Scale), 0.0F);
        float f6 = this.getBall(8D);
        this.mesh.chunkSetAngles("zArr_PioneerBall", 0.0F, this.cvt(f6, -4F, 4F, -11F, 11F), 0.0F);
        f6 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        this.mesh.chunkSetAngles("zArr_Pioneer", 0.0F, f6, 0.0F);
        this.mesh.chunkSetAngles("zArr_ClockS", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zArr_ClockM", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zArr_ClockH", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zArr_RPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 9F), CockpitCCC.RPM_Scale), 0.0F);
        this.mesh.chunkSetAngles("zArr_Alt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 12000F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zArr_Fuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 200F, 0.0F, 303.3F), 0.0F);
        float f7 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 800F, 0.0F, 5F) - this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 0.5F);
        this.mesh.chunkSetAngles("zArr_OilPress", 0.0F, this.cvt(f7 * this.fm.EI.engines[0].getReadyness(), 0.0F, 10F, 0.0F, 265F), 0.0F);
        float f8 = 0.0F;
        if (this.fm.M.fuel > 382.5F) {
            f8 = 0.9F;
        } else if (this.fm.M.fuel > 1.0F) {
            f8 = 0.25F;
        }
        this.mesh.chunkSetAngles("zArr_FuelPress", 0.0F, this.cvt(f8, 0.0F, 1.0F, 0.0F, 266.7F), 0.0F);
        this.mesh.chunkSetAngles("zArr_WaterTemp", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 125F, 0.0F, 285.5F), 0.0F);
        this.mesh.chunkSetAngles("zArr_BrakePress", 0.0F, 122F * this.fm.CT.getBrake(), 0.0F);
        float f9 = 2.0F * this.fm.CT.getTrimElevatorControl();
        if (f9 > 0.0F) {
            this.mesh.chunkSetAngles("Z_Trim", 1324F * f9, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zArr_Stab", 0.0F, 110F * f9, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Trim", 1080F * f9, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zArr_Stab", 0.0F, 90F * f9, 0.0F);
        }
        this.mesh.chunkSetAngles("zArr_OilTemp", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 125F, 0.0F, 17F), CockpitCCC.Oil_Scale), 0.0F);
        float f10 = this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, 700F);
        this.mesh.chunkSetAngles("Z_Radiator", f10, 0.0F, 0.0F);
        if (this.tros1Mat != null) {
            this.tros1 = 6F * this.fm.EI.engines[0].getControlRadiator();
            this.tros1Mat.setLayer(0);
            this.tros1Mat.set((byte) 12, this.tros1);
        }
        this.mesh.chunkSetAngles("GS_Cap", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F));
        this.mesh.chunkSetAngles("GS_Tinter", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 0.69F, 0.0F, -90F));
        this.mesh.chunkSetAngles("GS_Spring", this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_AirPress250", 0.0F, 0.0F, 153F);
        this.mesh.chunkSetAngles("zAirstartr", 0.0F, 0.0F, -90F * this.setNew.Airstartr);
        if ((this.fm.EI.engines[0].getStage() == 1) || (this.fm.EI.engines[0].getStage() == 2)) {
            this.mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, (-245F * this.setNew.Airstartr) + (5F * World.Rnd().nextFloat()));
        } else {
            this.mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zFireCrane", 0.0F, this.fm.AS.astateEngineStates[0] <= 2 ? 90F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSW_ANO", 0.0F, 0.0F, this.fm.AS.bNavLightsOn ? -40F : 0.0F);
        this.mesh.chunkSetAngles("zSW_Fara", 0.0F, 0.0F, this.fm.AS.bLandingLightOn ? -40F : 0.0F);
        HierMesh hiermesh = this.aircraft().hierMesh();
        boolean flag = hiermesh.isChunkVisible("Pilot2_D0");
        this.mesh.chunkVisible("Pilot2_D0", flag);
        this.mesh.chunkVisible("Pilot3_D0", hiermesh.isChunkVisible("Pilot3_D0"));
        this.mesh.chunkVisible("Pilot3_D1", hiermesh.isChunkVisible("Pilot3_D1"));
        boolean flag1 = hiermesh.isChunkVisible("HMask2_D0");
        this.mesh.chunkVisible("HMask2_D0", flag1);
        this.mesh.chunkVisible("HMask3_D0", hiermesh.isChunkVisible("HMask3_D0"));
        hiermesh.chunkVisible("Turret1A_D0", false);
        hiermesh.chunkVisible("Turret1C_D0", false);
        hiermesh.chunkVisible("Turret1D_D0", false);
        if (this.Gun5 != null) {
            if (this.Gun5.isShots() && this.Gun5.haveBullets() && (this.PatronsMat != null)) {
                this.Patrons -= 0.35F * f;
                this.PatronsMat.setLayer(0);
                this.PatronsMat.set((byte) 11, this.Patrons);
            }
            if (World.cur().diffCur.Limited_Ammo && (this.Gun5.countBullets() < 50)) {
                this.mesh.chunkVisible("CCC_TUR8Patrons", false);
            }
        } else {
            this.mesh.chunkVisible("CCC_TUR8Patrons", false);
        }
        this.mesh.chunkSetAngles("CCC_zASBR2", -30F * this.setNew.Asbr2, 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("xHullDm1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Alt", false);
                this.mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            }
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Pioneer", false);
                this.mesh.chunkVisible("zArr_PioneerBall", false);
                this.mesh.materialReplace("Prib_Peoneer", "Prib_Peoneer_dmg");
            }
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_WaterTemp", false);
                this.mesh.materialReplace("Prib_Water125", "Prib_Water125_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_RPM", false);
                this.mesh.materialReplace("Prib_Prib22", "Prib_Prib22_dmg");
            }
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Stab", false);
                this.mesh.materialReplace("Prib_Stab6", "Prib_Stab6_dmg");
            }
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_FuelPress", false);
                this.mesh.materialReplace("Prib_Fuel1", "Prib_Fuel1_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("xGlassDm3", true);
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_ClockH", false);
                this.mesh.chunkVisible("zArr_ClockM", false);
                this.mesh.chunkVisible("zArr_ClockS", false);
                this.mesh.materialReplace("Prib_Clock1", "Prib_Clock1_dmg");
            }
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Fuel", false);
                this.mesh.materialReplace("Prib_G20", "Prib_G20_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlassDm2", true);
            this.mesh.materialReplace("Prib_Kpa3Alt", "Prib_Kpa3Alt_dmg");
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.materialReplace("Prib_Kpa3Pressure", "Prib_Kpa3Pressure_dmg");
            }
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_OilPress", false);
                this.mesh.materialReplace("Prib_M10", "Prib_M10_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("xGlassDm1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("xHullDm2", true);
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Speed", false);
                this.mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
            }
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_OilTemp", false);
                this.mesh.materialReplace("Prib_OilTemp125", "Prib_OilTemp125_dmg");
            }
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) && ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("xHullDm3", true);
            this.mesh.chunkVisible("zArr_AirPress25", false);
            this.mesh.materialReplace("Prib_Oxy25", "Prib_Oxy25_dmg");
            this.mesh.chunkVisible("zArr_AirPress250", false);
            this.mesh.materialReplace("Prib_Oxy250", "Prib_Oxy250_dmg");
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_BrakePress", false);
                this.mesh.materialReplace("Prib_M25", "Prib_M25_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("xOilSplats_D1", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            for (int i = 0; i < 2; i++) {
                this.lights[i].light.setEmit(0.5F, 0.7F);
            }

            this.mesh.materialReplace("AL2_add", "AL2_add_light");
            this.mesh.materialReplace("AL2_na", "AL2_na_light");
            this.mesh.chunkSetAngles("zSW_CockLight", 0.0F, 70F, 0.0F);
        } else {
            for (int j = 0; j < 2; j++) {
                this.lights[j].light.setEmit(0.0F, 0.0F);
            }

            this.mesh.materialReplace("AL2_add", "AL2_add");
            this.mesh.materialReplace("AL2_na", "AL2_na");
            this.mesh.chunkSetAngles("zSW_CockLight", 0.0F, 0.0F, 0.0F);
        }
        this.setNightMats(false);
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private LightPointActor    lights[]    = { null, null };
    private static final float IAS_Scale[] = { 0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F };
    private static final float RPM_Scale[] = { 0.0F, 30F, 68.8F, 114.8F, 160.9F, 200.4F, 236F, 263.9F, 288.1F, 307F };
    private static final float Oil_Scale[] = { 0.0F, 5.4F, 11.3F, 20.9F, 31.1F, 42.1F, 53.3F, 66.7F, 81.7F, 96.7F, 115.3F, 130.6F, 147.89F, 173.69F, 197.4F, 223.6F, 253.1F, 286F };
    private float              tros1;
    private Mat                tros1Mat;
    private float              Patrons;
    private Mat                PatronsMat;
    private Gun                Gun5;

    static {
        Property.set(CockpitCCC.class, "normZNs", new float[] { 1.9F, 0.7F, 0.7F, 0.7F });
    }
}