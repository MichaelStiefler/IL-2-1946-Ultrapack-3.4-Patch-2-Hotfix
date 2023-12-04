package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.rts.Property;
import com.maddox.rts.Spawn;

public class RocketAGM12B_gn16 extends RemoteControlRocket {
    public RocketAGM12B_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        super(actor, netchannel, i, point3d, orient, f);
    }

    protected void doStart(float f) {
        super.doStart(f);
        if (Config.isUSE_RENDER()) {
            this.fl1 = Eff3DActor.New(this, this.findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            this.fl2 = Eff3DActor.New(this, this.findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        }
    }

    public void destroy() {
        if (Config.isUSE_RENDER()) {
            Eff3DActor.finish(this.fl1);
            Eff3DActor.finish(this.fl2);
        }
        super.destroy();
    }

    private Eff3DActor fl1;
    private Eff3DActor fl2;

    static {
        Class class1 = RocketAGM12B_gn16.class;
        Property.set(class1, "mesh", "3do/arms/AGM12B_gn16/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "timeLife", 330F);
        Property.set(class1, "timeFire", 6F);
        Property.set(class1, "force", 13000F);
        Property.set(class1, "power", 120F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 259F);
        Property.set(class1, "massaEnd", 183F);
        Property.set(class1, "friendlyName", "AGM-12B");
        Spawn.add(class1, new RemoteControlRocket.SPAWN());
    }
}
