package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.electronics.RadarLiSN2Equipment;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class CockpitME_262HG_VB_Rear extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitME_262HG_VB_Rear.this.setTmp = CockpitME_262HG_VB_Rear.this.setOld;
            CockpitME_262HG_VB_Rear.this.setOld = CockpitME_262HG_VB_Rear.this.setNew;
            CockpitME_262HG_VB_Rear.this.setNew = CockpitME_262HG_VB_Rear.this.setTmp;
            CockpitME_262HG_VB_Rear.this.setNew.altimeter = CockpitME_262HG_VB_Rear.this.fm.getAltitude();
            CockpitME_262HG_VB_Rear.this.setNew.throttlel = ((10F * CockpitME_262HG_VB_Rear.this.setOld.throttlel) + CockpitME_262HG_VB_Rear.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitME_262HG_VB_Rear.this.setNew.throttler = ((10F * CockpitME_262HG_VB_Rear.this.setOld.throttler) + CockpitME_262HG_VB_Rear.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            float f = CockpitME_262HG_VB_Rear.this.waypointAzimuth();
            if (BaseGameVersion.is410orLater() && CockpitME_262HG_VB_Rear.this.useRealisticNavigationInstruments()) {
                CockpitME_262HG_VB_Rear.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitME_262HG_VB_Rear.this.setOld.waypointAzimuth.setDeg(f - 90F);
            } else {
                CockpitME_262HG_VB_Rear.this.setNew.waypointAzimuth.setDeg(CockpitME_262HG_VB_Rear.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitME_262HG_VB_Rear.this.setOld.azimuth.getDeg(1.0F));
            }
            if (Math.abs(CockpitME_262HG_VB_Rear.this.fm.Or.getKren()) < 30F) {
                CockpitME_262HG_VB_Rear.this.setNew.azimuth.setDeg(CockpitME_262HG_VB_Rear.this.setOld.azimuth.getDeg(1.0F), CockpitME_262HG_VB_Rear.this.fm.Or.azimut());
            }
            CockpitME_262HG_VB_Rear.this.setNew.vspeed = ((299F * CockpitME_262HG_VB_Rear.this.setOld.vspeed) + CockpitME_262HG_VB_Rear.this.fm.getVertSpeed()) / 300F;
            if (CockpitME_262HG_VB_Rear.this.cockpitDimControl) {
                if (CockpitME_262HG_VB_Rear.this.setNew.dimPosition > 0.0F) {
                    CockpitME_262HG_VB_Rear.this.setNew.dimPosition = CockpitME_262HG_VB_Rear.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitME_262HG_VB_Rear.this.setNew.dimPosition < 1.0F) {
                CockpitME_262HG_VB_Rear.this.setNew.dimPosition = CockpitME_262HG_VB_Rear.this.setOld.dimPosition + 0.05F;
            }
            if (BaseGameVersion.is410orLater()) {
                CockpitME_262HG_VB_Rear.this.setNew.beaconDirection = ((10F * CockpitME_262HG_VB_Rear.this.setOld.beaconDirection) + CockpitME_262HG_VB_Rear.this.getBeaconDirection()) / 11F;
                CockpitME_262HG_VB_Rear.this.setNew.beaconRange = ((10F * CockpitME_262HG_VB_Rear.this.setOld.beaconRange) + CockpitME_262HG_VB_Rear.this.getBeaconRange()) / 11F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitME_262HG_VB_Rear() {
        super("3DO/Cockpit/Me-262HG-VB-Rear/hier.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pilotLoc = new Loc();
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "2petitsb_d1", "2petitsb", "aiguill1", "badinetm_d1", "badinetm", "baguecom", "brasdele", "comptemu_d1", "comptemu", "petitfla_d1", "petitfla", "turnbank" });
        this.setNightMats(false);
        this.setNew.dimPosition = 1.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (BaseGameVersion.is411orLater()) {
            this.printCompassHeading = true;
        } else if (BaseGameVersion.is410orLater()) {
            try {
                AircraftLH.class.getField("printCompassHeading").setBoolean(null, true);
            } catch (Exception e) {
            }
        }
    }

    protected boolean doFocusEnter() {
        ((ME_262HG_VB) this.aircraft()).setCurPilot(2);
        if (!super.doFocusEnter()) {
            return false;
        }
        this.aircraft().hierMesh().chunkVisible("CF_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot1_D1", false);
        this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
        this.aircraft().hierMesh().chunkVisible("Head2_D0", false);
        this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Blister2_D0", false);
        return true;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        if (!this.fm.AS.isPilotParatrooper(0)) {
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", !this.fm.AS.isPilotDead(0));
            this.aircraft().hierMesh().chunkVisible("Head1_D0", !this.fm.AS.isPilotDead(0));
            this.aircraft().hierMesh().chunkVisible("Pilot1_D1", this.fm.AS.isPilotDead(0));
        }
        if (!this.fm.AS.isPilotParatrooper(1)) {
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", !this.fm.AS.isPilotDead(1));
            this.aircraft().hierMesh().chunkVisible("Head2_D0", !this.fm.AS.isPilotDead(1));
            this.aircraft().hierMesh().chunkVisible("Pilot2_D1", this.fm.AS.isPilotDead(1));
        }
        this.aircraft().hierMesh().chunkVisible("Blister1_D0", !((ME_262HG_VB) this.aircraft()).blisterRemoved[0]);
        this.aircraft().hierMesh().chunkVisible("Blister2_D0", !((ME_262HG_VB) this.aircraft()).blisterRemoved[1]);
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
            this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
            this.gun[4] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON05");
            this.gun[5] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON06");
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Canopy", 0.0F, 0.0F, -100F * this.fm.CT.getCockpitDoor());
        this.mesh.chunkSetAngles("Z_Speedometer1R", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 100F, 400F, 2.0F, 8F), CockpitME_262HG_VB_Rear.speedometerIndScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2R", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 100F, 1000F, 1.0F, 10F), CockpitME_262HG_VB_Rear.speedometerTruScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1R", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 16000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2R", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1R", this.floatindex(this.cvt(this.setNew.vspeed, -20F, 50F, 0.0F, 14F), CockpitME_262HG_VB_Rear.variometerScale), 0.0F, 0.0F);
        if (BaseGameVersion.is410orLater() && this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Compass2R", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass1R", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass1R", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass2R", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        if (this.aircraft().hierMesh().isChunkVisible("Pilot1_D0")) {
            this.aircraft().hierMesh().setCurChunk("Head1_D0");
            this.aircraft().hierMesh().getChunkLocObj(this.pilotLoc);
            this.mesh.chunkSetAngles("Head1_D0", 0F, this.pilotLoc.getOrient().getYaw(), 0F);
        }

        // +++ RadarLiSN2 +++
        this.cockpitRadarLiSN2.updateRadar();
        // --- RadarLiSN2 ---
    }

    // +++ RadarLiSN2 +++
    private RadarLiSN2Equipment cockpitRadarLiSN2 = new RadarLiSN2Equipment(this, 41, 70F, 5F, 0.2F, 0.012F, 0.011637F, 0.073296F);
    // --- RadarLiSN2 ---

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("HullDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("HullDamage4", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.chunkVisible("HullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("HullDamage1", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    private Loc                pilotLoc;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private Gun                gun[]                 = { null, null, null, null, null, null };
    private boolean            bNeedSetUp;
    private static final float speedometerIndScale[] = { 0.0F, 0.0F, 0.0F, 17F, 35.5F, 57.5F, 76F, 95F, 112F };
    private static final float speedometerTruScale[] = { 0.0F, 32.75F, 65.5F, 98.25F, 131F, 164F, 200F, 237F, 270.5F, 304F, 336F };
    private static final float variometerScale[]     = { 0.0F, 13.5F, 27F, 43.5F, 90F, 142.5F, 157F, 170.5F, 184F, 201.5F, 214.5F, 226F, 239.5F, 253F, 266F };

    static {
        Property.set(CockpitME_262HG_VB_Rear.class, "astatePilotIndx", 1);
    }

}
