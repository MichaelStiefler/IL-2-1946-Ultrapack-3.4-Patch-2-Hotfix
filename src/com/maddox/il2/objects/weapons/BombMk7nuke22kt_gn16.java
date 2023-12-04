package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk7nuke22kt_gn16 extends Bomb
{

    public BombMk7nuke22kt_gn16()
    {
        bTailfinExtended = false;
    }

    public void start()
    {
        super.start();
        extendTailfin(true);
    }

    public void extendTailfin(boolean flag)
    {
        if(bTailfinExtended == flag)
            return;
        if(flag)
            setMesh("3DO/Arms/USnukeMk7_gn16/mono_open.sim");
        else
            setMesh("3DO/Arms/USnukeMk7_gn16/mono.sim");
        bTailfinExtended = flag;
    }

    public boolean getTailfin()
    {
        return bTailfinExtended;
    }

    private boolean bTailfinExtended;

    static 
    {
        Class class1 = BombMk7nuke22kt_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeMk7_gn16/mono.sim");
        Property.set(class1, "radius", 9400F);
        Property.set(class1, "power", 2.2E+007F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.76F);
        Property.set(class1, "massa", 762F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_USnukeDelay.class, Fuze_USnukeLowAlt.class, Fuze_AN_M100.class, Fuze_USnukeAir.class
        })));
        Property.set(class1, "dragCoefficient", 0.7F);
    }
}
