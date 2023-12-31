package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk7nuke22kt_gn16 extends BombGun
{
    public void extendTailfin(boolean flag)
    {
        if(bomb != null)
            ((BombMk7nuke22kt_gn16)bomb).extendTailfin(flag);
    }

    public boolean getTailfin()
    {
        if(bomb == null)
            return false;
        else
            return ((BombMk7nuke22kt_gn16)bomb).getTailfin();
    }

    static 
    {
        Class class1 = BombGunMk7nuke22kt_gn16.class;
        Property.set(class1, "bulletClass", (Object)BombMk7nuke22kt_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
