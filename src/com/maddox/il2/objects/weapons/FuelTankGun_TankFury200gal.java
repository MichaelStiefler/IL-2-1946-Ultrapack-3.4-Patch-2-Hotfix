package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_TankFury200gal extends FuelTankGun {
    public void matHighvis() {
        ((FuelTank_TankFury200gal) this.bomb).matHighvis();
    }

    public void matSEAcamo() {
        ((FuelTank_TankFury200gal) this.bomb).matSEAcamo();
    }

    static {
        Class class1 = FuelTankGun_TankFury200gal.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_TankFury200gal.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}
