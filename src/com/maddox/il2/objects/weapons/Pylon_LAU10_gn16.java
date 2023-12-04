package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Pylon_LAU10_gn16 extends Pylon {
    public void matHighvis() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("LAU_10", "LAU_10HV");
        this.mesh.materialReplace("LAU_10p", "LAU_10HVp");
        this.mesh.materialReplace("LAU_10q", "LAU_10HVq");
    }

    static {
        Class class1 = Pylon_LAU10_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU10_gn16/mono.sim");
        Property.set(class1, "massa", 63.5F);
        Property.set(class1, "dragCx", 0.029F);
        Property.set(class1, "drag", 0.029F);
    }
}
