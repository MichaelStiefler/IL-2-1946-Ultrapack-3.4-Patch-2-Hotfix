package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitHS_129B3 extends CockpitHS_129 {

    public CockpitHS_129B3() {
        super("3DO/Cockpit/Hs-129B-3/hier.him");
    }

    static {
        Property.set(CockpitHS_129B3.class, "normZN", 0.6F);
        Property.set(CockpitHS_129B3.class, "gsZN", 0.6F);
    }
}
