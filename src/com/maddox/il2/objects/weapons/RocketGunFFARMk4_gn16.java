package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunFFARMk4_gn16 extends RocketGun
{
    static 
    {
        Class class1 = RocketGunFFARMk4_gn16.class;
        Property.set(class1, "bulletClass", (Object)RocketFFARMk4_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.rocketgun_82");
    }
}
