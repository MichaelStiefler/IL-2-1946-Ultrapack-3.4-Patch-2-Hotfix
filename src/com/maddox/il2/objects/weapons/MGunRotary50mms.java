package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunRotary50mms extends MGunAircraftGeneric {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = true;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = null;
        gunproperties.fire = "3DO/Effects/GunFire/45mm/GunFire.eff";
        gunproperties.sprite = null;
        gunproperties.smoke = null;
        gunproperties.shells = null;
        gunproperties.sound = "weapon.air_cannon_45";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.5F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 100F;
        gunproperties.aimMaxDist = 6000F;
        gunproperties.weaponType = 2;
        gunproperties.maxDeltaAngle = 0.0F;
        gunproperties.shotFreq = 6F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 300;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] { new BulletProperties() });
        gunproperties.bullet[0].massa = 0.3F;
        gunproperties.bullet[0].kalibr = 0.0005F;
        gunproperties.bullet[0].speed = 750F;
        gunproperties.bullet[0].power = 0.35F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 45F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xff0000aa;
        gunproperties.bullet[0].timeLife = 60F;
        return gunproperties;
    }

    public void setConvDistance(float f, float f1) {
        super.setConvDistance(f, f1 - 0.0F);
    }
}
