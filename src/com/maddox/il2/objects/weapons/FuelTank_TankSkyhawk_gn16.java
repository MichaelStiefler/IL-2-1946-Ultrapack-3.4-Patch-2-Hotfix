package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankSkyhawk_gn16 extends FuelTank {
    public void matHighvis() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("Tank_Gloss", "Tank_GlossHV");
        this.mesh.materialReplace("Tank_GlossP", "Tank_GlossHVP");
        this.mesh.materialReplace("Tank_GlossQ", "Tank_GlossHVQ");
    }

    public void matSEAcamo() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("Tank_Gloss", "Tank_GlossSEA");
        this.mesh.materialReplace("Tank_GlossP", "Tank_GlossSEAP");
        this.mesh.materialReplace("Tank_GlossQ", "Tank_GlossSEAQ");
    }

    static {
        Class class1 = FuelTank_TankSkyhawk_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankSkyhawk_gn16/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 1150F);
        Property.set(class1, "dragCoefficient", 0.32F);
    }
}
