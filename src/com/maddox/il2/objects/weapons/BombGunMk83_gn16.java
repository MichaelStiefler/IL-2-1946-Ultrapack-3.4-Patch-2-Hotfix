package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk83_gn16 extends BombGun
{
    static 
    {
        Class class1 = BombGunMk83_gn16.class;
        Property.set(class1, "bulletClass", (Object)BombMk83_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 6F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
