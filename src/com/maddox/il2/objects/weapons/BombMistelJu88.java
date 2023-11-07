package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMistelJu88 extends Bomb
{
    static 
    {
        Class class1 = BombMistelJu88.class;
        Property.set(class1, "mesh", "3DO/Arms/2KgBomblet/mono.sim");
        Property.set(class1, "radius", 750F);
        Property.set(class1, "power", 3260F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 5000F);
        Property.set(class1, "newEffect", 0);
        Property.set(class1, "nuke", Bomb.NUKE_MISTEL);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
