package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_TankSkyraider150gal_gn16 extends FuelTankGun {
    public void matSEAcamo() {
        ((FuelTank_TankSkyraider150gal_gn16) this.bomb).matSEAcamo();
    }

    static {
        Class class1 = FuelTankGun_TankSkyraider150gal_gn16.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_TankSkyraider150gal_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}
