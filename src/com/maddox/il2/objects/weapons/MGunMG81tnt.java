package com.maddox.il2.objects.weapons;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunMG81tnt extends MGunMG81t
{
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.0115F;
        gunproperties.bullet[0].kalibr = 2.900001E-005F;
        gunproperties.bullet[0].speed = 765F;
        gunproperties.bullet[0].power = 0.0F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.0F;
        gunproperties.bullet[0].traceMesh = null;
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0;
        gunproperties.bullet[0].timeLife = 1.5F;
        gunproperties.bullet[1].massa = 0.0115F;
        gunproperties.bullet[1].kalibr = 2.900001E-005F;
        gunproperties.bullet[1].speed = 765F;
        gunproperties.bullet[1].power = 0.0F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 1.5F;
        gunproperties.bullet[2].massa = 0.0101F;
        gunproperties.bullet[2].kalibr = 2.900001E-005F;
        gunproperties.bullet[2].speed = 795F;
        gunproperties.bullet[2].power = 0.0005F;
        gunproperties.bullet[2].powerType = 0;
        gunproperties.bullet[2].powerRadius = 0.0F;
        gunproperties.bullet[2].traceMesh = null;
        gunproperties.bullet[2].traceTrail = null;
        gunproperties.bullet[2].traceColor = 0;
        gunproperties.bullet[2].timeLife = 1.3F;
        gunproperties.bullet[3].massa = 0.0101F;
        gunproperties.bullet[3].kalibr = 2.900001E-005F;
        gunproperties.bullet[3].speed = 795F;
        gunproperties.bullet[3].power = 0.0005F;
        gunproperties.bullet[3].powerType = 0;
        gunproperties.bullet[3].powerRadius = 0.0F;
        gunproperties.bullet[3].traceMesh = null;
        gunproperties.bullet[3].traceTrail = null;
        gunproperties.bullet[3].traceColor = 0;
        gunproperties.bullet[3].timeLife = 1.3F;
        gunproperties.bullet[4].massa = 0.0115F;
        gunproperties.bullet[4].kalibr = 2.900001E-005F;
        gunproperties.bullet[4].speed = 765F;
        gunproperties.bullet[4].power = 0.0F;
        gunproperties.bullet[4].powerType = 0;
        gunproperties.bullet[4].powerRadius = 0.0F;
        gunproperties.bullet[4].traceMesh = null;
        gunproperties.bullet[4].traceTrail = null;
        gunproperties.bullet[4].traceColor = 0;
        gunproperties.bullet[4].timeLife = 1.5F;
        gunproperties.bullet[5].massa = 0.0115F;
        gunproperties.bullet[5].kalibr = 2.900001E-005F;
        gunproperties.bullet[5].speed = 765F;
        gunproperties.bullet[5].power = 0.0F;
        gunproperties.bullet[5].powerType = 0;
        gunproperties.bullet[5].powerRadius = 0.0F;
        gunproperties.bullet[5].traceMesh = null;
        gunproperties.bullet[5].traceTrail = null;
        gunproperties.bullet[5].traceColor = 0;
        gunproperties.bullet[5].timeLife = 1.5F;
        gunproperties.bullet[6].massa = 0.0101F;
        gunproperties.bullet[6].kalibr = 2.900001E-005F;
        gunproperties.bullet[6].speed = 795F;
        gunproperties.bullet[6].power = 0.0005F;
        gunproperties.bullet[6].powerType = 0;
        gunproperties.bullet[6].powerRadius = 0.0F;
        gunproperties.bullet[6].traceMesh = null;
        gunproperties.bullet[6].traceTrail = null;
        gunproperties.bullet[6].traceColor = 0;
        gunproperties.bullet[6].timeLife = 1.3F;
        gunproperties.bullet[7].massa = 0.0108F;
        gunproperties.bullet[7].kalibr = 2.900001E-005F;
        gunproperties.bullet[7].speed = 855F;
        gunproperties.bullet[7].power = 0.000375F;
        gunproperties.bullet[7].powerType = 0;
        gunproperties.bullet[7].powerRadius = 0.01F;
        gunproperties.bullet[7].traceMesh = null;
        gunproperties.bullet[7].traceTrail = null;
        gunproperties.bullet[7].traceColor = 0;
        gunproperties.bullet[7].timeLife = 1.3F;
        return gunproperties;
    }
}
