package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Pylon_FJ4a extends Pylon {
    static {
        Class class1 = Pylon_FJ4a.class;
        Property.set(Pylon_FJ4a.class, "mesh", "3DO/Arms/Pylon_FJ4a/mono.sim");
        Property.set(class1, "dragCx", 0.011F);
        Property.set(class1, "drag", 0.011F);
    }
}
