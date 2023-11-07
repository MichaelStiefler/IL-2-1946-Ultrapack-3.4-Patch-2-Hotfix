package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class G4M1_11 extends G4M implements TypeBomber {

    public G4M1_11() {
        this.ftpos = 0.0F;
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        boolean tipCone = thisWeaponsName.startsWith("early_");
        if (tipCone) thisWeaponsName = thisWeaponsName.substring(6);
        boolean fullFairing = thisWeaponsName.startsWith("default") || thisWeaponsName.startsWith("none");
        boolean slotFairing = thisWeaponsName.equals("1x250") || thisWeaponsName.equals("1x500");
        hierMesh.chunkVisible("Bay1_D0", fullFairing);
        hierMesh.chunkVisible("Bay2_D0", fullFairing);
        hierMesh.chunkVisible("Bay3_D0", slotFairing);
        hierMesh.chunkVisible("Bay4_D0", slotFairing);
        hierMesh.chunkVisible("Turret2E_D0", tipCone);
        hierMesh.chunkVisible("xTurret2E_D0", !tipCone);
    }
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        G4M1_11.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.turret[1].tu[0];
        float f2 = this.FM.turret[1].tu[1];
        f1 -= 360F;
        if (Math.abs(f1) > 2.0F || Math.abs(f2) > 2.0F) {
            float f3 = (float) Math.toDegrees(Math.atan2(f1, f2));
            this.ftpos = 0.8F * this.ftpos + 0.2F * f3;
            this.hierMesh().chunkSetAngles("Turret2E_D0", 0.0F, this.ftpos, 0.0F);
        }
    }

    private float ftpos;

    static {
        Class class1 = G4M1_11.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "G4M");
        Property.set(class1, "meshName", "3DO/Plane/G4M1-11(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ja", "3DO/Plane/G4M1-11(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/G4M1-11.fmd");
        Property.set(class1, "cockpitClass",
                new Class[] { CockpitG4M1_11.class, CockpitG4M1_11_Bombardier.class, CockpitG4M1_11_NGunner.class, CockpitG4M1_11_AGunner.class, CockpitG4M1_11_TGunner.class, CockpitG4M1_11_RGunner.class, CockpitG4M1_11_LGunner.class });
        Property.set(class1, "LOSElevation", 1.4078F);
        weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36", "_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_BombSpawn50", "_BombSpawn51", "_BombSpawn52", "_BombSpawn53", "_BombSpawn54", "_BombSpawn55", "_BombSpawn56", "_BombSpawn57", "_BombSpawn58", "_BombSpawn59", "_BombSpawn60", "_BombSpawn61", "_BombSpawn62", "_BombSpawn63", "_BombSpawn64", "_BombSpawn65", "_BombSpawn66", "_BombSpawn67", "_BombSpawn68", "_BombSpawn69", "_BombSpawn70", "_BombSpawn71", "_BombSpawn72", "_BombSpawn73", "_BombSpawn74", "_BombSpawn75", "_BombSpawn76", "_BombSpawn77", "_BombSpawn78", "_BombSpawn79", "_BombSpawn80", "_BombSpawn81", "_BombSpawn82", "_BombSpawn83", "_BombSpawn84", "_BombSpawn85", "_BombSpawn86", "_BombSpawn87", "_BombSpawn88" });
    }
}
