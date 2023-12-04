package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_TankSkyhawk_gn16 extends FuelTankGun {
    public void matHighvis() {
        ((FuelTank_TankSkyhawk_gn16) this.bomb).matHighvis();
    }

    public void matSEAcamo() {
        ((FuelTank_TankSkyhawk_gn16) this.bomb).matSEAcamo();
    }

    static {
        Class class1 = FuelTankGun_TankSkyhawk_gn16.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_TankSkyhawk_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}
