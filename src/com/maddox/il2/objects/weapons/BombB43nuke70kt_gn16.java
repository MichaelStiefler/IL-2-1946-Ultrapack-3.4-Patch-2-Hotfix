package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombB43nuke70kt_gn16 extends Bomb
{
    static 
    {
        Class class1 = BombB43nuke70kt_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeB43_gn16/monoShort.sim");
        Property.set(class1, "radius", 11000F);
        Property.set(class1, "power", 7E+007F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.457F);
        Property.set(class1, "massa", 935F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_USnukeDelay.class, Fuze_USnukeLowAlt.class, Fuze_AN_M100.class, Fuze_USnukeAir.class
        })));
        Property.set(class1, "dragCoefficient", 0.32F);
    }
}
