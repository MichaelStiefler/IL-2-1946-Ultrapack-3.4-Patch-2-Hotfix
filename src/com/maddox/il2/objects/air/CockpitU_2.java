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
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitU_2 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitU_2.this.setTmp = CockpitU_2.this.setOld;
            CockpitU_2.this.setOld = CockpitU_2.this.setNew;
            CockpitU_2.this.setNew = CockpitU_2.this.setTmp;
            CockpitU_2.this.setNew.pictAiler = (0.85F * CockpitU_2.this.setOld.pictAiler) + (0.15F * CockpitU_2.this.cvt(CockpitU_2.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F));
            CockpitU_2.this.setNew.pictElev = (0.85F * CockpitU_2.this.setOld.pictElev) + (0.15F * CockpitU_2.this.cvt(CockpitU_2.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F));
            CockpitU_2.this.setNew.pictRudd = (0.85F * CockpitU_2.this.setOld.pictRudd) + (0.15F * CockpitU_2.this.cvt(CockpitU_2.this.fm.CT.getRudder(), -1F, 1.0F, -1F, 1.0F));
            CockpitU_2.this.w.set(CockpitU_2.this.fm.getW());
            CockpitU_2.this.fm.Or.transform(CockpitU_2.this.w);
            CockpitU_2.this.setNew.turn = ((12F * CockpitU_2.this.setOld.turn) + CockpitU_2.this.w.z) / 13F;
            if (Math.abs(CockpitU_2.this.fm.Or.getKren()) < 30F) {
                CockpitU_2.this.setNew.azimuth.setDeg(CockpitU_2.this.setOld.azimuth.getDeg(1.0F), CockpitU_2.this.fm.Or.azimut());
            }
            CockpitU_2.this.setNew.compasTangage = (0.95F * CockpitU_2.this.setOld.compasTangage) + (0.05F * CockpitU_2.this.cvt(CockpitU_2.this.fm.Or.getTangage(), -20F, 20F, -20F, 20F));
            CockpitU_2.this.setNew.compasKren = (0.95F * CockpitU_2.this.setOld.compasKren) + (0.05F * CockpitU_2.this.cvt(CockpitU_2.this.fm.Or.getKren(), -20F, 20F, -20F, 20F));
            CockpitU_2.this.setNew.throttle1 = (0.9F * CockpitU_2.this.setOld.throttle1) + (0.1F * CockpitU_2.this.fm.EI.engines[0].getControlThrottle());
            CockpitU_2.this.setNew.mix1 = (0.8F * CockpitU_2.this.setOld.mix1) + (0.2F * CockpitU_2.this.fm.EI.engines[0].getControlMix());
            CockpitU_2.this.setNew.altimeter = CockpitU_2.this.fm.getAltitude();
            CockpitU_2.this.setNew.vspeed = ((199F * CockpitU_2.this.setOld.vspeed) + CockpitU_2.this.fm.getVertSpeed()) / 200F;
            float f = 57.3F * CockpitU_2.this.fm.EI.engines[0].getw();
            f %= 2880F;
            f /= 2880F;
            if (f <= 0.5F) {
                f *= 2.0F;
            } else {
                f = (f * 2.0F) - 2.0F;
            }
            f *= 1200F;
            CockpitU_2.this.propPos = (CockpitU_2.this.propPos + (f * U_2xyz.updatef)) % 360F;
            if (CockpitU_2.this.fm.EI.engines[0].getRPM() > 0.0F) {
                if (CockpitU_2.this.fm.EI.engines[0].getRPM() < 60F) {
                    if ((CockpitU_2.this.propPos > 20F) && (CockpitU_2.this.propPos < 50F)) {
                        CockpitU_2.this.setNew.EngK2 = -15F;
                    }
                    CockpitU_2.this.setNew.EngK1 = 0.0F;
                    if ((CockpitU_2.this.propPos > 50F) && (CockpitU_2.this.propPos < 80F)) {
                        CockpitU_2.this.setNew.EngK2 = 0.0F;
                        CockpitU_2.this.setNew.EngK1 = -15F;
                    }
                    if (CockpitU_2.this.propPos > 80F) {
                        CockpitU_2.this.setNew.EngK2 = 0.0F;
                        CockpitU_2.this.setNew.EngK1 = 0.0F;
                    }
                } else {
                    CockpitU_2.this.setNew.EngK2 = -15F * World.Rnd().nextFloat();
                    CockpitU_2.this.setNew.EngK1 = -15F * World.Rnd().nextFloat();
                }
            } else {
                CockpitU_2.this.setNew.EngK2 = 0.0F;
                CockpitU_2.this.setNew.EngK1 = 0.0F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      throttle1;
        float      mix1;
        float      turn;
        float      vspeed;
        float      pictElev;
        float      pictAiler;
        float      pictRudd;
        float      compasTangage;
        float      compasKren;
        float      EngK1;
        float      EngK2;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.0D, 0.0D, 0.0D);
    }

    public CockpitU_2() {
        super("3DO/Cockpit/U_2/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
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
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.0F, -0.12F, 0.0F, -0.03F });
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
        this.mesh.chunkSetAngles("Z_Stick_e", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick_d", 0.0F, 0.0F, -f2);
        float f3 = 15F * this.setNew.pictRudd;
        this.mesh.chunkSetAngles("Z_Pedals", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedals1", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedals2", -f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalR", -f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalL", -f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalsN", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zKI6a", 0.0F, this.setNew.compasTangage, -this.setNew.compasKren);
        this.mesh.chunkSetAngles("zAzimuth", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        float f4 = this.getBall(8D);
        this.mesh.chunkSetAngles("zArr_PioneerBal", this.cvt(-f4, -4F, 4F, -7.5F, 7.5F), 0.0F, 0.0F);
        f4 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        this.mesh.chunkSetAngles("zArr_Pioneer", -f4, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Clock_m", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Clock_h", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Clock_s", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 21600F), 0.0F, 0.0F);
        float f5 = -50F * this.setNew.throttle1;
        this.mesh.chunkSetAngles("Z_Throtle", f5, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 0.0F, f5 / 10F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, 0.0F, -f5);
        float f6 = 30F * this.setNew.mix1;
        this.mesh.chunkSetAngles("Z_Mixture", f6, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", f6 / 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 0.0F, 0.0F, -f6);
        this.mesh.chunkSetAngles("zArr_RPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 400F, 2200F, 0.0F, 9F), CockpitU_2.RPM_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Speed", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), CockpitU_2.IAS_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Temp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 125F, 0.0F, 17F), CockpitU_2.Oil_Scale), 0.0F, 0.0F);
        float f7 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 4.5F);
        this.mesh.chunkSetAngles("zArr_OilPres", this.cvt(f7, 0.0F, 10F, 0.0F, -265F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Alt_m", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 6000F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Variom", this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        f1 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1700F, 0.0F, 10F);
        if (this.fm.AS.bNavLightsOn) {
            f1 -= 2.5F;
        }
        this.mesh.chunkSetAngles("zArr_Volt", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_SW_Magneto", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 85F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_SW_Start", (this.fm.EI.engines[0].getStage() > 2) || (this.fm.EI.engines[0].getStage() <= 0) ? 0.0F : 25F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("z_EngineK2", 0.0F, -this.setNew.EngK2, 0.0F);
        this.mesh.chunkSetAngles("z_EngineK1", 0.0F, this.setNew.EngK1, 0.0F);
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Pilot2_D0", hiermesh.isChunkVisible("Pilot2_D0"));
        this.mesh.chunkVisible("Pilot2_D1", hiermesh.isChunkVisible("Pilot2_D1"));
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
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Temp", false);
                this.mesh.materialReplace("Prib_OilTemp125", "Prib_OilTemp125_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("zArr_RPM", false);
            this.mesh.materialReplace("Prib_Prib22", "Prib_Prib22_dmg");
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Variom", false);
                this.mesh.materialReplace("Prib_Variom", "Prib_Variom_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Volt", false);
                this.mesh.materialReplace("Volt", "Volt_dmg");
            }
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Clock_h", false);
                this.mesh.chunkVisible("zArr_Clock_m", false);
                this.mesh.chunkVisible("zArr_Clock_s", false);
                this.mesh.materialReplace("Prib_Clock1", "Prib_Clock1_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlassDm2", true);
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
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Alt_m", false);
                this.mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            }
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) && ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("xHullDm3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("xOilSplats_D1", true);
        }
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

            this.mesh.chunkSetAngles("zCabin_Lights", 25F, 0.0F, 0.0F);
        }
        this.setNightMats(false);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Cockpit_D0", false);
            hiermesh.chunkVisible("Engine1p_D0", false);
            hiermesh.chunkVisible("Engine1p_D1", false);
            hiermesh.chunkVisible("Engine1p_D2", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        hiermesh.chunkVisible("Cockpit_D0", true);
        hiermesh.chunkVisible("Engine1p_D0", true);
        hiermesh.chunkVisible("Engine1p_D1", true);
        hiermesh.chunkVisible("Engine1p_D2", true);
        super.doFocusLeave();
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              propPos;
    private LightPointActor    lights[]    = { null, null };
    private static final float IAS_Scale[] = { 0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F };
    private static final float Oil_Scale[] = { 0.0F, 5.4F, 11.3F, 20.9F, 31.1F, 42.1F, 53.3F, 66.7F, 81.7F, 96.7F, 115.3F, 130.6F, 147.89F, 173.69F, 197.4F, 223.6F, 253.1F, 286F };
    private static final float RPM_Scale[] = { 0.0F, 30F, 68.8F, 114.8F, 160.9F, 200.4F, 236F, 263.9F, 288.1F, 307F };

    static {
        Property.set(CockpitU_2.class, "normZNs", new float[] { 0.7F, 0.6F, 0.6F, 0.6F });
        Property.set(CockpitU_2.class, "gsZN", 0.2F);
    }
}
