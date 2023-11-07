package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunBrowning50tft extends MGunBrowning50t
{

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bullet = (new BulletProperties[] {
                new BulletProperties(), new BulletProperties(), new BulletProperties()
            });
            gunproperties.bullet[0].massa = 0.046F;
            gunproperties.bullet[0].kalibr = 0.0001181215F;
            gunproperties.bullet[0].speed = 870F;
            gunproperties.bullet[0].power = 0.0F;
            gunproperties.bullet[0].powerType = 0;
            gunproperties.bullet[0].powerRadius = 0.0F;
            gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
            gunproperties.bullet[0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
            gunproperties.bullet[0].traceColor = 0xf90000ff;
            gunproperties.bullet[0].timeLife = 6.5F;
            gunproperties.bullet[1].massa = 0.046F;
            gunproperties.bullet[1].kalibr = 0.0001181215F;
            gunproperties.bullet[1].speed = 870F;
            gunproperties.bullet[1].power = 0.0F;
            gunproperties.bullet[1].powerType = 0;
            gunproperties.bullet[1].powerRadius = 0.0F;
            gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
            gunproperties.bullet[1].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
            gunproperties.bullet[1].traceColor = 0xf90000ff;
            gunproperties.bullet[1].timeLife = 6.5F;
            gunproperties.bullet[2].massa = 0.042F;
            gunproperties.bullet[2].kalibr = 0.0001182215F;
            gunproperties.bullet[2].speed = 820F;
            gunproperties.bullet[2].power = 0.001F;
            gunproperties.bullet[2].powerType = 0;
            gunproperties.bullet[2].powerRadius = 0.0F;
            gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
            gunproperties.bullet[2].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
            gunproperties.bullet[2].traceColor = 0xf90000ff;
            gunproperties.bullet[2].timeLife = 6.5F;
            return gunproperties;
    }
}
