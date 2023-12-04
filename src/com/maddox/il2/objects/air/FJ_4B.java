package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FJ_4B extends FJ_4 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane {

    public FJ_4B() {
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
    }
    
    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        prepareWeapons(aircraftClass, hierMesh, thisWeaponsName, true);
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName, boolean inPreview) {
        boolean isBuddy = thisWeaponsName.indexOf("_mbuddy_") > 0;
        hierMesh.chunkVisible("D704_RefuelStore", isBuddy);
        hierMesh.chunkVisible("D704_rat", isBuddy);
        hierMesh.chunkVisible("D704_Drogue1_Fold", isBuddy && !inPreview);
        hierMesh.chunkVisible("D704_iGreen", isBuddy);
        hierMesh.chunkVisible("D704_iYellow", isBuddy);
        hierMesh.chunkVisible("D704_FuelLine1", isBuddy && inPreview);
        hierMesh.chunkVisible("D704_Drogue1", isBuddy && inPreview);
    }
    
    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -96F);
        Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -105F);
        float f3 = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F) : Aircraft.cvt(f, 0.8F, 1.0F, -90F, 0.0F);
        float f4 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
        Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -58F);
        if (f <= 0.5F) {
            Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -55F);
        } else {
            Aircraft.cvt(f, 0.8F, 1.0F, -55F, 0.0F);
        }
        if (f <= 0.5F) {
            Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -115F);
        } else {
            Aircraft.cvt(f, 0.8F, 1.0F, -115F, 0.0F);
        }
        Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -65F);
        Aircraft.cvt(f, 0.525F, 0.8F, 0.0F, 38F);
        float f10 = Aircraft.cvt(f, 0.3F, 0.7F, 0.0F, -55F);
        float f11 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, 90F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC6a_D0", 0.0F, f11, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f11, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f11, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f10, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f10, 0.0F);
    }

    protected void moveGear(float f) {
        FJ_4B.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.19075F, 0.0F, 0.0F, 1.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        this.hierMesh().chunkSetLocate("GearC6b_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 35F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -65F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -65F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake03_D0", 0.6F, -40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake04_D0", 0.6F, -40F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        if (f > 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -9.5F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -9.5F * f, 0.0F);
        }
        if (f < 0.0F) {
            this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -9.5F * f, 0.0F);
            this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -9.5F * f, 0.0F);
        }
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, -15F);
        Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, -20F);
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -30F * f, 0.0F);
    }

    public void moveArrestorHook(float f) {
        float f1 = Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -60F);
        float f2 = Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -90F);
        float f3 = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F) : Aircraft.cvt(f, 0.8F, 1.0F, -90F, 0.0F);
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("HookL_D0", 0.0F, f2, 0.0F);
        this.hierMesh().chunkSetAngles("HookR_D0", 0.0F, f2, 0.0F);
        this.hierMesh().chunkSetAngles("HookL1_D0", 0.0F, f3, 0.0F);
        this.hierMesh().chunkSetAngles("HookR1_D0", 0.0F, f3, 0.0F);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 97F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -97F), 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(false);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(FJ_4B.NEG_G_TOLERANCE_FACTOR, FJ_4B.NEG_G_TIME_FACTOR, FJ_4B.NEG_G_RECOVERY_FACTOR, FJ_4B.POS_G_TOLERANCE_FACTOR, FJ_4B.POS_G_TIME_FACTOR, FJ_4B.POS_G_RECOVERY_FACTOR);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public Point3f getMissileTargetOffset() {
        return this.guidedMissileUtils.getSelectedActorOffset();
    }

    public int getMissileLockState() {
        return this.guidedMissileUtils.getMissileLockState();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        FJ_4B.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName, false);
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        super.update(f);
    }

    private GuidedMissileUtils guidedMissileUtils;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;

    static {
        Class class1 = FJ_4B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FJ_4B");
        Property.set(class1, "meshName", "3DO/Plane/FJ_4B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1956.9F);
        Property.set(class1, "yearExpired", 1994.3F);
        Property.set(class1, "FlightModel", "FlightModels/FJ4B.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFJ4.class });
//        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 9, 9, 4, 4, 4, 4, 4, 4, 4, 4, 7, 7 });
//        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19",
//                "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalDev19", "_ExternalDev20", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40" });

        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ER001", "_ER001", "_ER002", "_ER002", "_ER003", "_ER003", "_ER004", "_ER004", "_ER005", "_ER005", "_ER006", "_ER006", "_ER007", "_ER008", "_ER009", "_ER010", "_ER011", "_ER012", "_ER013", "_ER013", "_ER014", "_ER014", "_ER015", "_ER015", "_ER016", "_ER016", "_ER017", "_ER017", "_ER018", "_ER019", "_ER020", "_ER021", "_ER022", "_ER023", "_ER024", "_ER025", "_ER026", "_ER027", "_ER028", "_ER029", "_ER030", "_ER031", "_ER032", "_ER033", "_ER034", "_ER035", "_ER036",
                        "_ER037", "_ER038", "_ER039", "_ER040", "_ER041", "_ER042", "_ER043", "_ER044", "_ER045", "_ER046", "_ER047", "_ER048", "_ER049", "_ER050", "_ER051", "_ER052", "_ER053", "_ER054", "_ER055", "_ER056", "_ER057", "_ER058", "_ER059", "_ER060", "_ER061", "_ER062", "_ER063", "_ER064", "_ER065", "_ER066", "_ER067", "_ER068", "_ER069", "_ER070", "_ER071", "_ER072", "_ER073", "_ER074", "_ER075", "_ER076", "_ER077", "_ER078", "_ER079", "_ER080", "_ER081", "_ER082", "_ER083", "_ER084", "_ER085", "_ER086",
                        "_ER087", "_ER088", "_ER089", "_ER090", "_ER091", "_ER092", "_ER093", "_ER094", "_ER095", "_ER096", "_ER097", "_ER098", "_ER099", "_ER100", "_ER101", "_ER102", "_ER103", "_ER104", "_ER105", "_ER106", "_ER107", "_ER108", "_ER109", "_ER110", "_ER111", "_ER112", "_ER113", "_ER114", "_ER115", "_ER116", "_ER117", "_ER118", "_ER119", "_ER120", "_ER121", "_ER122", "_ER123", "_ER124", "_ER125", "_ER126", "_ER127", "_ER128", "_ER129", "_ER130", "_ER131", "_ER132", "_ER133", "_ER134", "_ER135", "_ER136",
                        "_ER137", "_ER138", "_ER139", "_ER140", "_ER141", "_ER142", "_ER143", "_ER144", "_ER145", "_ER146", "_ER147", "_ER148", "_ER149", "_ER150", "_ER151", "_ER152", "_ER153", "_ER154", "_ER155", "_ER156", "_ER157", "_ER158", "_ER159", "_ER160", "_ER161", "_ER162", "_ER163", "_ER164", "_ER165", "_ER166", "_ER167", "_ER168", "_ER169", "_ER170", "_ER171", "_ER172", "_ER173", "_ER174", "_ER175", "_ER176", "_ER177", "_ER178", "_ER179", "_ER180", "_ER181", "_ER182", "_ER183", "_ER184", "_ER185", "_ER186",
                        "_ER187", "_ER188", "_ER189", "_ER190", "_ER191", "_ER192", "_ER193", "_ER194", "_ER195", "_ER196", "_ER197", "_ER198", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ED01", "_ED02", "_ED03", "_ED04", "_ED05", "_ED06", "_ED07", "_ED07", "_ED08", "_ED08", "_ED09", "_ED09", "_ED10", "_ED10", "_ED11", "_ED11", "_ED12", "_ED12", "_ED13", "_ED13", "_ED14", "_ED14", "_ED15", "_ED15", "_ED16", "_ED16", "_ED17", "_ED17", "_ED18", "_ED18", "_ED19", "_ED19", "_ED20",
                        "_ED20", "_ED21", "_ED21", "_ED22", "_ED22", "_ED23", "_ED23", "_ED24", "_ED24", "_ED25", "_ED26", "_ED27", "_ED28", "_ED29", "_ED30" });
    }
}
