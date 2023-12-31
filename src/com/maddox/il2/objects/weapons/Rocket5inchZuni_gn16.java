package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class Rocket5inchZuni_gn16 extends Rocket
{
    static 
    {
        Class class1 = Rocket5inchZuni_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Zuni_5inch_gn16/Mk16Mk24close.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Zuni_5inch_gn16/Mk16Mk24fly.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "timeLife", 999.999F);
        Property.set(class1, "timeFire", 1.5F);
        Property.set(class1, "force", 1600F);
        Property.set(class1, "power", 19F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.128F);
        Property.set(class1, "massa", 48.5F);
        Property.set(class1, "massaEnd", 31.1F);
        Property.set(class1, "spinningStraightFactor", 1.1F);
        Property.set(class1, "friendlyName", "Zuni");
    }
}
