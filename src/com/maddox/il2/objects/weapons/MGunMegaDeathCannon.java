package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunMegaDeathCannon extends MGunAircraftGeneric {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = true;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = null;
        gunproperties.fire = "Effects/BigShip/GunFire150mm/Fire.eff";
        gunproperties.sprite = null;
        gunproperties.smoke = "Effects/BigShip/GunFire150mm/Burst.eff";
        gunproperties.shells = null;
        gunproperties.sound = "weapon.mgundeathcannon";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.5F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 100F;
        gunproperties.aimMaxDist = 6000F;
        gunproperties.weaponType = 2;
        gunproperties.maxDeltaAngle = 0.0F;
        gunproperties.shotFreq = 0.25F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 300;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] { new BulletProperties() });
        gunproperties.bullet[0].massa = 5F;
        gunproperties.bullet[0].kalibr = 0.1F;
        gunproperties.bullet[0].speed = 400F;
        gunproperties.bullet[0].power = 2000F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 100F;
        gunproperties.bullet[0].traceMesh = "3DO/Arms/SC-1000/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 255;
        gunproperties.bullet[0].timeLife = 100F;
        return gunproperties;
    }
}
