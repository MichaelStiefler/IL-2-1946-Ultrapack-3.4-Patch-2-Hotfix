package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class JEEP extends CAR_NEW {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        JEEP.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(this.getClass(), this.thisWeaponsName);

        if ((weaponSlotsRegistered == null) || (weaponSlotsRegistered.length < 1) || weaponSlotsRegistered[0] == null) {
            this.FM.crew = 1;
            if (this == World.getPlayerAircraft() && Config.isUSE_RENDER()) {
                Cockpit acockpit[] = new Cockpit[1];
                System.arraycopy(Main3D.cur3D().cockpits, 0, acockpit, 0, 1);
                Main3D.cur3D().cockpits = acockpit;
                FM.turret[0].bIsAIControlled = false;
            }
        }
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);

        if ((weaponSlotsRegistered == null) || (weaponSlotsRegistered.length < 1)) {
            return;
        }
        
        hierMesh.chunkVisible("Turret1B_D0", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("ammo", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("ammo2", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("pipelong", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("Pilot2_D0", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("Head2_D0", weaponSlotsRegistered[0] != null);
        hierMesh.chunkVisible("Helm2_D0", weaponSlotsRegistered[0] != null);
    }

    public void update(float f) {
        if (this.FM.AS.bIsAboutToBailout) {
            this.hierMesh().chunkVisible("Helm_D0", false);
            this.hierMesh().chunkVisible("Helm2_D0", false);
        }
        this.FM.EI.engines[0].addVside *= 1E-7D;
        super.update(f);
    }
    
    public boolean turretAngles(int i, float af[]) {
        boolean retVal = super.turretAngles(i, af);
        if (i != 0) {
            return retVal;
        }
        if (af[1] > 50F) {
            af[1] = 50F;
            retVal = false;
        } else if (af[1] < 0F) {
            af[1] = 0F;
            retVal = false;
        }
        if (af[0] > 90F) {
            af[0] = 90F;
            retVal = false;
        } else if (af[0] < -90F) {
            af[1] = -90F;
            retVal = false;
        }
        return retVal;
    }

    static {
        Class class1 = JEEP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Jeep");
        Property.set(class1, "meshName", "3do/plane/Jeep/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Jeep.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJeep.class, CockpitJeep_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
