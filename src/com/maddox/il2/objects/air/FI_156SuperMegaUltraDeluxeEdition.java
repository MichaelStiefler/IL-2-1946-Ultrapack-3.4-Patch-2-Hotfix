package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class FI_156SuperMegaUltraDeluxeEdition extends Scheme1 implements TypeFighter, TypeTNBFighter {

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName)
    {
        hierMesh.chunkVisible("Booster_D0", thisWeaponsName.startsWith("CMBSTR"));
        hierMesh.chunkVisible("BoosterAdd_D0", thisWeaponsName.startsWith("CMBSTR"));
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 5F);
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, Aircraft.floatindex(f, FI_156SuperMegaUltraDeluxeEdition.gearL2), 0.0F);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, Aircraft.floatindex(f, FI_156SuperMegaUltraDeluxeEdition.gearL4), 0.0F);
        this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, Aircraft.floatindex(f, FI_156SuperMegaUltraDeluxeEdition.gearL5), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 0.5F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 5F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -Aircraft.floatindex(f, FI_156SuperMegaUltraDeluxeEdition.gearL2), 0.0F);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, -Aircraft.floatindex(f, FI_156SuperMegaUltraDeluxeEdition.gearL4), 0.0F);
        this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, -Aircraft.floatindex(f, FI_156SuperMegaUltraDeluxeEdition.gearL5), 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -60F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && (World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)) {
            this.FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        }
        if (shot.chunkName.startsWith("WingRMid") && (World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)) {
            this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        }
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) {
                this.FM.AS.hitEngine(shot.initiator, 0, 1);
            }
            if ((Aircraft.v1.z > 0.0D) && (World.Rnd().nextFloat() < 0.12F)) {
                this.FM.AS.setEngineDies(shot.initiator, 0);
                if (shot.mass > 0.1F) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 5);
                }
            }
            if ((Aircraft.v1.x < 0.1D) && (World.Rnd().nextFloat() < 0.57F)) {
                this.FM.AS.hitOil(shot.initiator, 0);
            }
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
            if ((Aircraft.Pd.z > 0.5D) && (shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                HUD.logCenter("H E A D S H O T");
            }
            return;
        }
        if (shot.chunkName.startsWith("Pilot2")) {
            this.killPilot(shot.initiator, 1);
            if ((Aircraft.Pd.z > 0.5D) && (shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                HUD.logCenter("H E A D S H O T");
            }
            return;
        }
        if (shot.chunkName.startsWith("Turret")) {
            this.FM.turret[0].bIsOperable = false;
        }
        if ((this.FM.AS.astateEngineStates[0] == 4) && (World.Rnd().nextInt(0, 99) < 33)) {
            this.FM.setCapableOfBMP(false, shot.initiator);
        }
        super.msgShot(shot);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);

            case 37:
                return super.cutFM(38, j, actor);

            case 35:
            case 36:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void doWoundPilot(int i, float f) {
        if (i == 1) {
            this.FM.turret[0].setHealth(f);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (!this.FM.AS.bIsAboutToBailout && World.cur().isHighGore()) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                if (!this.FM.AS.bIsAboutToBailout && World.cur().isHighGore()) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        if (f < -45F) {
            f = -45F;
            flag = false;
        }
        if (f > 45F) {
            f = 45F;
            flag = false;
        }
        if (f1 < -45F) {
            f1 = -45F;
            flag = false;
        }
        if (f1 > 20F) {
            f1 = 20F;
            flag = false;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals(PaintScheme.countryHungary)) {
            return PaintScheme.countryHungary + "_";
        }
        if (regiment.country().equals(PaintScheme.countryFinland)) {
            return PaintScheme.countryFinland + "_";
        }
        if (regiment.country().equals(PaintScheme.countryRomania)) {
            return PaintScheme.countryRomania + "_";
        } else {
            return "";
        }
    }

    private static final float gearL2[] = { 0.0F, 1.0F, 2.0F, 2.9F, 3.2F, 3.35F };
    private static final float gearL4[] = { 0.0F, 7.5F, 15F, 22F, 29F, 35.5F };
    private static final float gearL5[] = { 0.0F, 1.5F, 4F, 7.5F, 10F, 11.5F };

    static {
        Class class1 = FI_156SuperMegaUltraDeluxeEdition.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Fi-156 Super Mega Ultra Deluxe Edition");
        Property.set(class1, "meshName", "3do/plane/Fi156SuperMegaUltraDeluxeEdition/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/Fi-156SuperMegaUltraDeluxeEdition.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFI_156SuperMegaUltraDeluxeEdition.class, CockpitFI_156SuperMegaUltraDeluxeEdition_Gunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 9, 0, 9, 0, 10, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 1, 9, 9, 9, 9,
                9, 9, 9, 9, 1, 1, 1, 1, 1, 1,
                1, 1});
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalDev01", "_MGUN02", "_ExternalDev02", "_MGUN03", "_MGUN01", "_MGUN02", "_MGUN02", "_MGUN02", "_MGUN02", "_MGUN02",
                "_MGUN02", "_MGUN02", "_MGUN02", "_MGUN02", "_MGUN02", "_MGUN03", "_MGUN03", "_MGUN03", "_MGUN03", "_MGUN03",
                "_MGUN03", "_MGUN03", "_MGUN03", "_MGUN03", "_MGUN03", "_MGUN04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06",
                "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", 
                "_ExternalRock07", "_ExternalRock08" });
    }
}
