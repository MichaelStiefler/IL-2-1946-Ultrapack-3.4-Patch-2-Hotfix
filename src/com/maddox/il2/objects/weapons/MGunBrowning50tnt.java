package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunBrowning50tnt extends MGunBrowning50t
{

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bullet = (new BulletProperties[] {
                new BulletProperties(), new BulletProperties()
            });
            gunproperties.bullet[0].massa = 0.041F;
            gunproperties.bullet[0].kalibr = 0.0001185215F;
            gunproperties.bullet[0].speed = 900F;
            gunproperties.bullet[0].power = 0.0022F;
            gunproperties.bullet[0].powerType = 0;
            gunproperties.bullet[0].powerRadius = 0.0F;
            gunproperties.bullet[0].traceMesh = null;
            gunproperties.bullet[0].traceTrail = null;
            gunproperties.bullet[0].traceColor = 0;
            gunproperties.bullet[0].timeLife = 6.3F;
            gunproperties.bullet[1].massa = 0.041F;
            gunproperties.bullet[1].kalibr = 0.0001185215F;
            gunproperties.bullet[1].speed = 900F;
            gunproperties.bullet[1].power = 0.0022F;
            gunproperties.bullet[1].powerType = 0;
            gunproperties.bullet[1].powerRadius = 0.0F;
            gunproperties.bullet[1].traceMesh = null;
            gunproperties.bullet[1].traceTrail = null;
            gunproperties.bullet[1].traceColor = 0;
            gunproperties.bullet[1].timeLife = 6.3F;
            return gunproperties;
    }
}
