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
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitU_2_Copilot extends CockpitPilot // CockpitCopilot
{
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitU_2_Copilot.this.setTmp = CockpitU_2_Copilot.this.setOld;
            CockpitU_2_Copilot.this.setOld = CockpitU_2_Copilot.this.setNew;
            CockpitU_2_Copilot.this.setNew = CockpitU_2_Copilot.this.setTmp;
            CockpitU_2_Copilot.this.setNew.pictAiler = (0.85F * CockpitU_2_Copilot.this.setOld.pictAiler) + (0.15F * CockpitU_2_Copilot.this.cvt(CockpitU_2_Copilot.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F));
            CockpitU_2_Copilot.this.setNew.pictElev = (0.85F * CockpitU_2_Copilot.this.setOld.pictElev) + (0.15F * CockpitU_2_Copilot.this.cvt(CockpitU_2_Copilot.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F));
            CockpitU_2_Copilot.this.setNew.pictRudd = (0.85F * CockpitU_2_Copilot.this.setOld.pictRudd) + (0.15F * CockpitU_2_Copilot.this.cvt(CockpitU_2_Copilot.this.fm.CT.getRudder(), -1F, 1.0F, -1F, 1.0F));
            CockpitU_2_Copilot.this.w.set(CockpitU_2_Copilot.this.fm.getW());
            CockpitU_2_Copilot.this.fm.Or.transform(CockpitU_2_Copilot.this.w);
            CockpitU_2_Copilot.this.setNew.turn = ((12F * CockpitU_2_Copilot.this.setOld.turn) + CockpitU_2_Copilot.this.w.z) / 13F;
            if (Math.abs(CockpitU_2_Copilot.this.fm.Or.getKren()) < 30F) {
                CockpitU_2_Copilot.this.setNew.azimuth.setDeg(CockpitU_2_Copilot.this.setOld.azimuth.getDeg(1.0F), CockpitU_2_Copilot.this.fm.Or.azimut());
            }
            CockpitU_2_Copilot.this.setNew.vspeed = ((199F * CockpitU_2_Copilot.this.setOld.vspeed) + CockpitU_2_Copilot.this.fm.getVertSpeed()) / 200F;
            CockpitU_2_Copilot.this.setNew.compasTangage = (0.95F * CockpitU_2_Copilot.this.setOld.compasTangage) + (0.05F * CockpitU_2_Copilot.this.cvt(CockpitU_2_Copilot.this.fm.Or.getTangage(), -20F, 20F, -20F, 20F));
            CockpitU_2_Copilot.this.setNew.compasKren = (0.95F * CockpitU_2_Copilot.this.setOld.compasKren) + (0.05F * CockpitU_2_Copilot.this.cvt(CockpitU_2_Copilot.this.fm.Or.getKren(), -20F, 20F, -20F, 20F));
            CockpitU_2_Copilot.this.setNew.throttle1 = (0.9F * CockpitU_2_Copilot.this.setOld.throttle1) + (0.1F * CockpitU_2_Copilot.this.fm.EI.engines[0].getControlThrottle());
            CockpitU_2_Copilot.this.setNew.mix1 = (0.8F * CockpitU_2_Copilot.this.setOld.mix1) + (0.2F * CockpitU_2_Copilot.this.fm.EI.engines[0].getControlMix());
            CockpitU_2_Copilot.this.setNew.altimeter = CockpitU_2_Copilot.this.fm.getAltitude();
            if (CockpitU_2_Copilot.this.bbombR1 && (CockpitU_2_Copilot.this.bombR1 > 0.0F)) {
                CockpitU_2_Copilot.this.bombR1 = CockpitU_2_Copilot.this.bombR1 - 0.05F;
            }
            if (CockpitU_2_Copilot.this.bbombL1 && (CockpitU_2_Copilot.this.bombL1 > 0.0F)) {
                CockpitU_2_Copilot.this.bombL1 = CockpitU_2_Copilot.this.bombL1 - 0.05F;
            }
            if ((CockpitU_2_Copilot.this.amountOfBombs == 2) && !CockpitU_2_Copilot.this.abpk && !CockpitU_2_Copilot.this.cargo) {
                if (!CockpitU_2_Copilot.this.bombs[0].haveBullets() && !CockpitU_2_Copilot.this.bbombR1) {
                    CockpitU_2_Copilot.this.bombR1 = 1.0F;
                    CockpitU_2_Copilot.this.bbombR1 = true;
                }
                if (!CockpitU_2_Copilot.this.bombs[1].haveBullets() && !CockpitU_2_Copilot.this.bbombL1) {
                    CockpitU_2_Copilot.this.bombL1 = 1.0F;
                    CockpitU_2_Copilot.this.bbombL1 = true;
                }
            } else if ((CockpitU_2_Copilot.this.amountOfBombs == 2) && CockpitU_2_Copilot.this.abpk && !CockpitU_2_Copilot.this.cargo) {
                if (!CockpitU_2_Copilot.this.bombs[2].haveBullets() && !CockpitU_2_Copilot.this.bbombR1) {
                    CockpitU_2_Copilot.this.bombR1 = 1.0F;
                    CockpitU_2_Copilot.this.bbombR1 = true;
                }
                if (!CockpitU_2_Copilot.this.bombs[3].haveBullets() && !CockpitU_2_Copilot.this.bbombL1) {
                    CockpitU_2_Copilot.this.bombL1 = 1.0F;
                    CockpitU_2_Copilot.this.bbombL1 = true;
                }
            } else if ((CockpitU_2_Copilot.this.amountOfBombs == 2) && !CockpitU_2_Copilot.this.abpk && CockpitU_2_Copilot.this.cargo) {
                if (!CockpitU_2_Copilot.this.bombs[4].haveBullets() && !CockpitU_2_Copilot.this.bbombR1) {
                    CockpitU_2_Copilot.this.bombR1 = 1.0F;
                    CockpitU_2_Copilot.this.bbombR1 = true;
                }
                if (!CockpitU_2_Copilot.this.bombs[5].haveBullets() && !CockpitU_2_Copilot.this.bbombL1) {
                    CockpitU_2_Copilot.this.bombL1 = 1.0F;
                    CockpitU_2_Copilot.this.bbombL1 = true;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      mix1;
        float      pictElev;
        float      pictAiler;
        float      pictRudd;
        float      compasTangage;
        float      compasKren;
        AnglesFork azimuth;
        float      altimeter;
        float      turn;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            for (int i = 0; i < 2; i++) {
                this.lights[i].light.setEmit(0.45F, 0.45F);
            }

            this.mesh.chunkSetAngles("zCabin_Lights", -25F, 0.0F, 0.0F);
        } else {
            for (int j = 0; j < 2; j++) {
                this.lights[j].light.setEmit(0.0F, 0.0F);
            }

            this.mesh.chunkSetAngles("zCabin_Lights", 0.0F, 0.0F, 0.0F);
        }
        this.setNightMats(false);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("WingLMid_D0_00", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1_00", hiermesh.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2_00", hiermesh.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingRMid_D0_00", hiermesh.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1_00", hiermesh.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2_00", hiermesh.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_CAP_00", hiermesh.isChunkVisible("WingRMid_CAP"));
        this.mesh.chunkVisible("WingLMid_CAP_00", hiermesh.isChunkVisible("WingLMid_CAP"));
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (U_2xyz.bChangedPit) {
            this.reflectPlaneToModel();
            U_2xyz.bChangedPit = false;
        }
        float f1 = 15F * this.setNew.pictAiler;
        float f2 = 0.0F;
        if (this.setNew.pictElev > 0.0F) {
            f2 = 15F * this.setNew.pictElev;
        } else {
            f2 = 13F * this.setNew.pictElev;
        }
        this.mesh.chunkSetAngles("Z_Stick", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick_e", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick_d1", 0.0F, 0.0F, -f2);
        this.mesh.chunkSetAngles("Z_Stick_d2", 0.0F, 0.0F, -f2);
        float f3 = 15F * this.setNew.pictRudd;
        this.mesh.chunkSetAngles("Z_PedalsC", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalsN", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zKI6a", 0.0F, this.setNew.compasTangage, -this.setNew.compasKren);
        this.mesh.chunkSetAngles("zAzimuth", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        float f4 = this.getBall(8D);
        this.mesh.chunkSetAngles("zArr_PioneerBal", this.cvt(-f4, -4F, 4F, -7.5F, 7.5F), 0.0F, 0.0F);
        f4 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        this.mesh.chunkSetAngles("zArr_Pioneer", -f4, 0.0F, 0.0F);
        float f5 = -50F * this.setNew.throttle1;
        this.mesh.chunkSetAngles("Z_Throtle", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, -f5);
        float f6 = 30F * this.setNew.mix1;
        this.mesh.chunkSetAngles("Z_Mixture", f6, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 0.0F, -f6);
        this.mesh.chunkSetAngles("zArr_RPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 9F), CockpitU_2_Copilot.RPM_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Speed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), CockpitU_2_Copilot.IAS_Scale), 0.0F, 0.0F);
        float f7 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 4.5F);
        this.mesh.chunkSetAngles("zArr_OilPres", this.cvt(f7, 0.0F, 10F, 0.0F, -265F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Temp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 125F, 0.0F, 17F), CockpitU_2_Copilot.Oil_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Alt_m", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 6000F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Variom", this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Clock_m", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Clock_h", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Clock_s", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 21600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_SW_Start", (this.fm.EI.engines[0].getStage() > 2) || (this.fm.EI.engines[0].getStage() <= 0) ? 0.0F : 25F, 0.0F, 0.0F);
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = 0.025F * this.bombR1;
        this.mesh.chunkSetLocate("Z_BdropR1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.025F * this.bombL1;
        this.mesh.chunkSetLocate("Z_BdropL1", Cockpit.xyz, Cockpit.ypr);
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        this.mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        this.mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Cockpit_D0", false);
            this.doHidePilot();
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Cockpit_D0", true);
            super.doFocusLeave();
            this.doShowPilot();
            return;
        } else {
            return;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("xHullDm1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Alt_m", false);
                this.mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            }
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Pioneer", false);
                this.mesh.chunkVisible("zArr_PioneerBal", false);
                this.mesh.materialReplace("Prib_Pioner", "Prib_Pioner_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("zArr_RPM", false);
            this.mesh.materialReplace("Prib_Prib22", "Prib_Prib22_dmg");
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Clock_h", false);
                this.mesh.chunkVisible("zArr_Clock_m", false);
                this.mesh.chunkVisible("zArr_Clock_s", false);
                this.mesh.materialReplace("Prib_Clock1", "Prib_Clock1_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_OilPres", false);
                this.mesh.materialReplace("Prib_M10", "Prib_M10_dmg");
            }
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Speed", false);
                this.mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlassDm1", true);
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_OilPres", false);
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
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) && ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("xHullDm3", true);
        }
    }

    public CockpitU_2_Copilot() {
        super("3DO/Cockpit/U_2-Bombardier/hier.him", "i16");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.abpk = false;
        this.cargo = false;
        this.amountOfBombs = 0;
        this.bombR1 = 0.0F;
        this.bombL1 = 0.0F;
        this.bbombR1 = false;
        this.bbombL1 = false;
        this.cockpitNightMats = new String[0];
        for (int i = 0; i < 2; i++) {
            HookNamed hooknamed = new HookNamed(this.mesh, "LAMP0" + (i + 3));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            this.lights[i] = new LightPointActor(new LightPoint(), loc.getPoint());
            this.lights[i].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            this.lights[i].light.setEmit(0.0F, 0.0F);
            this.pos.base().draw.lightMap().put("LAMP0" + (i + 3), this.lights[i]);
        }

        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.0F, -0.03F });
        try {
            this.bombs[0] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb01");
            this.amountOfBombs++;
        } catch (Exception exception) {
        }
        try {
            this.bombs[1] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb02");
            this.amountOfBombs++;
        } catch (Exception exception1) {
        }
        try {
            this.bombs[2] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
            this.amountOfBombs++;
            this.abpk = true;
        } catch (Exception exception2) {
        }
        try {
            this.bombs[3] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb06");
            this.amountOfBombs++;
            this.abpk = true;
        } catch (Exception exception3) {
        }
        try {
            this.bombs[4] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev01");
            this.amountOfBombs++;
            this.cargo = true;
        } catch (Exception exception4) {
        }
        try {
            this.bombs[5] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
            this.amountOfBombs++;
            this.cargo = true;
        } catch (Exception exception5) {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              bombR1;
    private float              bombL1;
    private boolean            bbombR1;
    private boolean            bbombL1;
    private int                amountOfBombs;
    private BombGun            bombs[]     = { null, null, null, null, null, null, null, null };
    private boolean            abpk;
    private boolean            cargo;
    private LightPointActor    lights[]    = { null, null };
    private static final float IAS_Scale[] = { 0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F };
    private static final float Oil_Scale[] = { 0.0F, 5.4F, 11.3F, 20.9F, 31.1F, 42.1F, 53.3F, 66.7F, 81.7F, 96.7F, 115.3F, 130.6F, 147.89F, 173.69F, 197.4F, 223.6F, 253.1F, 286F };
    private static final float RPM_Scale[] = { 0.0F, 30F, 68.8F, 114.8F, 160.9F, 200.4F, 236F, 263.9F, 288.1F, 307F };

    static {
        Property.set(CockpitU_2_Copilot.class, "aiTuretNum", -4);
        Property.set(CockpitU_2_Copilot.class, "astatePilotIndx", 1);
        Property.set(CockpitU_2_Copilot.class, "weaponControlNum", 3);
        Property.set(CockpitU_2_Copilot.class, "normZNs", new float[] { 0.65F, 0.55F, 0.65F, 0.55F });
        Property.set(CockpitU_2_Copilot.class, "gsZN", 0.6F);
    }
}
