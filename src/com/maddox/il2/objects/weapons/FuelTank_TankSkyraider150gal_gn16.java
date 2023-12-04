package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankSkyraider150gal_gn16 extends FuelTank {
    public void matSEAcamo() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("Tank_GlossHV", "Tank_GlossSEA");
        this.mesh.materialReplace("Tank_GlossHVP", "Tank_GlossSEAP");
        this.mesh.materialReplace("Tank_GlossHVQ", "Tank_GlossSEAQ");
    }

    static {
        Class class1 = FuelTank_TankSkyraider150gal_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankSkyraider150gal_gn16/mono.sim");
        Property.set(class1, "kalibr", 0.56F);
        Property.set(class1, "massa", 570F);
        Property.set(class1, "dragCoefficient", 0.28F);
    }
}
