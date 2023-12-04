package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Spawn;

public class MissileAIM9B_gn16 extends Missile {
    static class SPAWN extends Missile.SPAWN {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
            new MissileAIM9B_gn16(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN() {
        }
    }

    public MissileAIM9B_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    static {
        GuidedMissileUtils.parseMissilePropertiesFile(MissileAIM9B_gn16.class);
        Spawn.add(MissileAIM9B_gn16.class, new SPAWN());
    }
}
