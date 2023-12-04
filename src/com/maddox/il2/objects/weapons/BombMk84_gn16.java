package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk84_gn16 extends Bomb
{
    static 
    {
        Class class1 = BombMk84_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk84_gn16/mono.sim");
        Property.set(class1, "radius", 400.65F);
        Property.set(class1, "power", 428.644F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.457F);
        Property.set(class1, "massa", 893.57F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M100.class, Fuze_M115.class, Fuze_M112.class
        })));
        Property.set(class1, "dragCoefficient", 0.26F);
    }
}
