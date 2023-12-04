package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM12B_gn16 extends RocketGun
{
    public void setRocketTimeLife(float f)
    {
        this.timeLife = 330F;
    }

    static 
    {
        Class class1 = RocketGunAGM12B_gn16.class;
        Property.set(class1, "bulletClass", (Object)RocketAGM12B_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.1F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
