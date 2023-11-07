package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunRotary50mmt extends MGunRotary50mms {
    public GunProperties createProperties() {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bUseHookAsRel = false;
        return gunproperties;
    }
}
