package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk83_gn16 extends Bomb
{
    static 
    {
        Class class1 = BombMk83_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk83_gn16/mono.sim");
        Property.set(class1, "radius", 150F);
        Property.set(class1, "power", 275F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 454F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M100.class, Fuze_M115.class, Fuze_M112.class
        })));
        Property.set(class1, "dragCoefficient", 0.26F);
    }
}
