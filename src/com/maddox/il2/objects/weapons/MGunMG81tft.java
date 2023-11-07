package com.maddox.il2.objects.weapons;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunMG81tft extends MGunMG81t
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0097F;
        gunproperties.bullet[0].kalibr = 2.900001E-005F;
        gunproperties.bullet[0].speed = 800F;
        gunproperties.bullet[0].power = 0.0002F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmPink/mono.sim";
        gunproperties.bullet[0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 1.0F;
        gunproperties.bullet[1].massa = 0.0097F;
        gunproperties.bullet[1].kalibr = 2.900001E-005F;
        gunproperties.bullet[1].speed = 800F;
        gunproperties.bullet[1].power = 0.0002F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmPink/mono.sim";
        gunproperties.bullet[1].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[1].traceColor = 0xd200ffff;
        gunproperties.bullet[1].timeLife = 1.0F;
        gunproperties.bullet[2].massa = 0.0097F;
        gunproperties.bullet[2].kalibr = 2.900001E-005F;
        gunproperties.bullet[2].speed = 800F;
        gunproperties.bullet[2].power = 0.0002F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.0F;
        gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmPink/mono.sim";
        gunproperties.bullet[2].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[2].traceColor = 0xd200ffff;
        gunproperties.bullet[2].timeLife = 1.0F;
        gunproperties.bullet[3].massa = 0.0097F;
        gunproperties.bullet[3].kalibr = 2.900001E-005F;
        gunproperties.bullet[3].speed = 800F;
        gunproperties.bullet[3].power = 0.0002F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.0F;
        gunproperties.bullet[3].traceMesh = "3DO/Effects/Tracers/20mmRed/mono.sim";
        gunproperties.bullet[3].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        gunproperties.bullet[3].traceColor = 0xd200ffff;
        gunproperties.bullet[3].timeLife = 1.0F;
        return gunproperties;
    }
}
