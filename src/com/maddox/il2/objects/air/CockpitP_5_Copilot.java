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
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitP_5_Copilot extends CockpitPilot // CockpitCopilot
{
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
        float      dimPos;
        float      Airstartr;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_5_Copilot.this.fm != null) {
                CockpitP_5_Copilot.this.setTmp = CockpitP_5_Copilot.this.setOld;
                CockpitP_5_Copilot.this.setOld = CockpitP_5_Copilot.this.setNew;
                CockpitP_5_Copilot.this.setNew = CockpitP_5_Copilot.this.setTmp;
                CockpitP_5_Copilot.this.setNew.pictElev = (0.8F * CockpitP_5_Copilot.this.setOld.pictElev) + (0.2F * CockpitP_5_Copilot.this.fm.CT.ElevatorControl);
                CockpitP_5_Copilot.this.setNew.pictAiler = (0.8F * CockpitP_5_Copilot.this.setOld.pictAiler) + (2.0F * CockpitP_5_Copilot.this.fm.CT.AileronControl);
                CockpitP_5_Copilot.this.setNew.pictRudd = (0.8F * CockpitP_5_Copilot.this.setOld.pictRudd) + (0.2F * CockpitP_5_Copilot.this.fm.CT.getRudder());
                CockpitP_5_Copilot.this.setNew.throttle1 = (0.9F * CockpitP_5_Copilot.this.setOld.throttle1) + (0.1F * CockpitP_5_Copilot.this.fm.EI.engines[0].getControlThrottle());
                CockpitP_5_Copilot.this.setNew.mix1 = (0.8F * CockpitP_5_Copilot.this.setOld.mix1) + (0.2F * CockpitP_5_Copilot.this.fm.EI.engines[0].getControlMix());
                CockpitP_5_Copilot.this.setNew.altimeter = CockpitP_5_Copilot.this.fm.getAltitude();
                CockpitP_5_Copilot.this.w.set(CockpitP_5_Copilot.this.fm.getW());
                CockpitP_5_Copilot.this.fm.Or.transform(CockpitP_5_Copilot.this.w);
                CockpitP_5_Copilot.this.setNew.turn = ((12F * CockpitP_5_Copilot.this.setOld.turn) + CockpitP_5_Copilot.this.w.z) / 13F;
                if (Math.abs(CockpitP_5_Copilot.this.fm.Or.getKren()) < 30F) {
                    CockpitP_5_Copilot.this.setNew.azimuth.setDeg(CockpitP_5_Copilot.this.setOld.azimuth.getDeg(1.0F), CockpitP_5_Copilot.this.fm.Or.azimut());
                }
                CockpitP_5_Copilot.this.setNew.compasTangage = (0.95F * CockpitP_5_Copilot.this.setOld.compasTangage) + (0.05F * CockpitP_5_Copilot.this.cvt(CockpitP_5_Copilot.this.fm.Or.getTangage(), -20F, 20F, -20F, 20F));
                CockpitP_5_Copilot.this.setNew.compasKren = (0.95F * CockpitP_5_Copilot.this.setOld.compasKren) + (0.05F * CockpitP_5_Copilot.this.cvt(CockpitP_5_Copilot.this.fm.Or.getKren(), -20F, 20F, -20F, 20F));
                if (CockpitP_5_Copilot.this.cockpitDimControl) {
                    if (CockpitP_5_Copilot.this.setNew.dimPos < 1.0F) {
                        CockpitP_5_Copilot.this.setNew.dimPos = CockpitP_5_Copilot.this.setOld.dimPos + 0.05F;
                    }
                } else if (CockpitP_5_Copilot.this.setNew.dimPos > 0.0F) {
                    CockpitP_5_Copilot.this.setNew.dimPos = CockpitP_5_Copilot.this.setOld.dimPos - 0.05F;
                }
                if ((CockpitP_5_Copilot.this.fm.EI.engines[0].getStage() == 1) || (CockpitP_5_Copilot.this.fm.EI.engines[0].getStage() == 2)) {
                    if (CockpitP_5_Copilot.this.setNew.Airstartr < 1.0F) {
                        CockpitP_5_Copilot.this.setNew.Airstartr = CockpitP_5_Copilot.this.setOld.Airstartr + 0.1F;
                    }
                    if (CockpitP_5_Copilot.this.setNew.Airstartr > 1.0F) {
                        CockpitP_5_Copilot.this.setNew.Airstartr = 1.0F;
                    }
                } else {
                    if (CockpitP_5_Copilot.this.setNew.Airstartr > 0.0F) {
                        CockpitP_5_Copilot.this.setNew.Airstartr = CockpitP_5_Copilot.this.setOld.Airstartr - 0.1F;
                    }
                    if (CockpitP_5_Copilot.this.setNew.Airstartr < 0.0F) {
                        CockpitP_5_Copilot.this.setNew.Airstartr = 0.0F;
                    }
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.0D, 0.0D, 0.0D);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.lights[0].light.setEmit(0.5F, 0.7F);
            this.lights[1].light.setEmit(0.5F, 0.7F);
            this.mesh.materialReplace("AL2_add", "AL2_add_light");
            this.mesh.materialReplace("AL2_na", "AL2_na_light");
            this.mesh.materialReplace("equip_AN4b", "equip_AN4b_light");
            this.mesh.materialReplace("equip_AN4c", "equip_AN4c_light");
            this.mesh.materialReplace("equip_AN4_sh", "equip_AN4_sh_light");
        } else {
            this.lights[0].light.setEmit(0.0F, 0.0F);
            this.lights[1].light.setEmit(0.0F, 0.0F);
            this.mesh.materialReplace("AL2_add", "AL2_add");
            this.mesh.materialReplace("AL2_na", "AL2_na");
            this.mesh.materialReplace("equip_AN4b", "equip_AN4b");
            this.mesh.materialReplace("equip_AN4c", "equip_AN4c");
            this.mesh.materialReplace("equip_AN4_sh", "equip_AN4_sh");
        }
        this.setNightMats(false);
    }

//    private void retoggleLight()
//    {
//        if(cockpitLightControl)
//            setNightMats(false);
//        else
//            setNightMats(false);
//    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        this.mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        this.mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
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
        this.resetYPRmodifier();
        float f1 = this.setNew.pictRudd * 20F;
        this.mesh.chunkSetAngles("Pedals", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Pedal_tros_L1", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Pedal_tros_R1", -f1, 0.0F, 0.0F);
        float f2 = this.setNew.pictRudd;
        if (f2 > 0.0F) {
            this.mesh.chunkSetAngles("Pedal_tros_L2", -f2 * 22.2F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Pedal_tros_R2", -f2 * 19.1F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Pedal_tros_L2", -f2 * 19.1F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Pedal_tros_R2", -f2 * 22.2F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zRollerL", 200F * f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRollerR", 200F * f2, 0.0F, 0.0F);
        if (this.tros2Mat != null) {
            this.tros2 = 0.25F * f2;
            this.tros2Mat.setLayer(0);
            this.tros2Mat.set((byte) 11, this.tros2);
        }
        float f3 = 0.0F;
        if (this.setNew.pictElev > 0.0F) {
            f3 = 21F * this.setNew.pictElev;
        } else {
            f3 = 13F * this.setNew.pictElev;
        }
        this.mesh.chunkSetAngles("StickElev", 0.0F, 0.0F, f3);
        this.mesh.chunkSetAngles("Stick", 0.0F, this.setNew.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("StickKardan", 0.0F, 0.0F, -f3 * (float) Math.cos(this.setNew.pictAiler * 0.01745329F));
        this.mesh.chunkSetAngles("StickConnctr", -f3 * (float) Math.sin(this.setNew.pictAiler * 0.01745329F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("StickEl_trosL1", 0.0F, 0.0F, -f3);
        this.mesh.chunkSetAngles("StickEl_trosL2", 0.0F, 0.0F, -f3);
        this.mesh.chunkSetAngles("StickEl_trosR1", 0.0F, 0.0F, -f3);
        this.mesh.chunkSetAngles("StickEl_trosR2", 0.0F, 0.0F, -f3);
        float f4 = -50F * this.setNew.throttle1;
        this.mesh.chunkSetAngles("zThrotle", 0.0F, 0.0F, f4);
        this.mesh.chunkSetAngles("Cable_throtle01", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Cable_throtle02", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("zThrotleNav", 0.0F, 0.0F, -f4);
        this.mesh.chunkSetAngles("Cable_throtle03", 0.0F, 0.0F, f4);
        float f5 = -37.5F * this.setNew.mix1;
        this.mesh.chunkSetAngles("zMixture", 0.0F, 0.0F, f5);
        this.mesh.chunkSetAngles("Cable_mixture01", 0.0F, 0.0F, -f5);
        this.mesh.chunkSetAngles("Cable_mixture02", 0.0F, 0.0F, -f5);
        this.mesh.chunkSetAngles("zMixtureNav", 0.0F, 0.0F, -f5);
        this.mesh.chunkSetAngles("Cable_mixture03", 0.0F, 0.0F, f5);
        float f6 = this.cvt(this.fm.EI.engines[0].getRPM(), 900F, 1200F, 0.0F, -50F);
        this.mesh.chunkSetAngles("zIgnition", 0.0F, 0.0F, f6);
        this.mesh.chunkSetAngles("Cable_Ignition", 0.0F, 0.0F, -f6);
        R_5xyz r_5xyz2 = (R_5xyz) this.aircraft();
        this.mesh.chunkSetAngles("Z_AL2c", r_5xyz2.CompassDelta, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AN4a", 0.0F, -this.setNew.compasKren, -this.setNew.compasTangage);
        this.mesh.chunkSetAngles("Z_AN4b", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zArr_Speed2", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), CockpitP_5_Copilot.IAS_Scale), 0.0F);
        float f7 = this.getBall(8D);
        this.mesh.chunkSetAngles("zArr_PioneerBall2", 0.0F, this.cvt(f7, -4F, 4F, -11F, 11F), 0.0F);
        f7 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        this.mesh.chunkSetAngles("zArr_Pioneer2", 0.0F, f7, 0.0F);
        this.mesh.chunkSetAngles("zArr_ClockS2", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zArr_ClockM2", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zArr_ClockH2", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zArr_Alt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 12000F, 0.0F, 720F), 0.0F);
        float f8 = 0.0F;
        if (this.fm.AS.bLandingLightOn) {
            f8 = -2.5F;
        }
        if (this.fm.AS.bNavLightsOn) {
            f8--;
        }
        this.mesh.chunkSetAngles("zArr_Volt", 0.0F, f8, 0.0F);
        this.mesh.chunkSetAngles("zArr_AirPress250", 0.0F, 0.0F, 153F);
        this.mesh.chunkSetAngles("zAirstartr", 0.0F, 0.0F, -90F * this.setNew.Airstartr);
        if ((this.fm.EI.engines[0].getStage() == 1) || (this.fm.EI.engines[0].getStage() == 2)) {
            this.mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, (-245F * this.setNew.Airstartr) + (5F * World.Rnd().nextFloat()));
        } else {
            this.mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, 0.0F);
        }
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        this.mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        this.mesh.chunkVisible("HMask1_D0", hiermesh.isChunkVisible("HMask1_D0"));
        this.mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Interior_D0", false);
            this.bNeedSetUp = true;
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Interior_D0", true);
        super.doFocusLeave();
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("xHullDm1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("xHullDm4", true);
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_ClockH2", false);
                this.mesh.chunkVisible("zArr_ClockM2", false);
                this.mesh.chunkVisible("zArr_ClockS2", false);
                this.mesh.chunkVisible("zArr_ClockDop2", false);
                this.mesh.materialReplace("Prib_ClockACHO", "Prib_ClockACHO_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("xHullDm5", true);
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Alt2", false);
                this.mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("xGlassDm3", true);
            this.mesh.chunkVisible("xHullDm6", true);
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Pioneer2", false);
                this.mesh.chunkVisible("zArr_PioneerBall2", false);
                this.mesh.materialReplace("Prib_Peoneer2", "Prib_Peoneer2_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlassDm2", true);
            this.mesh.chunkVisible("xHullDm7", true);
            this.mesh.materialReplace("Prib_Kpa3Alt", "Prib_Kpa3Alt_dmg");
            if (World.Rnd().nextFloat() < 0.75F) {
                this.mesh.chunkVisible("zArr_Speed2", false);
                this.mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
            }
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("xGlassDm1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("xHullDm2", true);
            if (World.Rnd().nextFloat() < 0.65F) {
                this.mesh.chunkVisible("zArr_Volt", false);
                this.mesh.materialReplace("Prib_Volt", "Prib_Volt_dmg");
            }
        }
        if (((this.fm.AS.astateCockpitState & 2) != 0) && ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("xHullDm3", true);
            this.mesh.chunkVisible("zArr_AirPress25", false);
            this.mesh.materialReplace("Prib_Oxy25", "Prib_Oxy25_dmg");
            this.mesh.chunkVisible("zArr_AirPress250", false);
            this.mesh.materialReplace("Prib_Oxy250", "Prib_Oxy250_dmg");
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("xOilSplats_D1", true);
        }
    }

    protected void interpTick() {
    }

    public CockpitP_5_Copilot() {
        super("3DO/Cockpit/R-5-Bombardier/hier-P5-Co.him", "u2");
        this.bNeedSetUp = true;
//        bEntered = false;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.tros2 = 0.5F;
        this.tros2Mat = null;
        int i = -1;
        i = this.mesh.materialFind("tros2");
        if (i != -1) {
            this.tros2Mat = this.mesh.material(i);
            this.tros2Mat.setLayer(0);
        }
        this.cockpitNightMats = new String[0];
        for (int j = 0; j < 2; j++) {
            HookNamed hooknamed = new HookNamed(this.mesh, "LAMP0" + (j + 3));
            Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
            this.lights[j] = new LightPointActor(new LightPoint(), loc.getPoint());
            this.lights[j].light.setColor(0.8980392F, 0.8117647F, 0.6235294F);
            this.lights[j].light.setEmit(0.0F, 0.0F);
            this.pos.base().draw.lightMap().put("LAMP0" + (j + 3), this.lights[j]);
        }

        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.hidePilot = true;
        this.limits6DoF = (new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.15F, -0.11F, 0.03F, -0.03F });
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    private boolean            bNeedSetUp;
//    private boolean bEntered;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              tros2;
    private Mat                tros2Mat;
    private LightPointActor    lights[]    = { null, null };
    private static final float IAS_Scale[] = { 0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F };

    static {
        Property.set(CockpitP_5_Copilot.class, "normZNs", new float[] { 0.3F, 0.7F, 1.1F, 0.7F });
        Property.set(CockpitP_5_Copilot.class, "aiTuretNum", -4);
        Property.set(CockpitP_5_Copilot.class, "weaponControlNum", 3);
        Property.set(CockpitP_5_Copilot.class, "astatePilotIndx", 1);
    }
}
