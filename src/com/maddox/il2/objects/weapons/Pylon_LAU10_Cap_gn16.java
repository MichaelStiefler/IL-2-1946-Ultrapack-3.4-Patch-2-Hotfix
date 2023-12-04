package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Pylon_LAU10_Cap_gn16 extends Pylon {
    public void matHighvis() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("LAU_10", "LAU_10HV");
        this.mesh.materialReplace("LAU_10p", "LAU_10HVp");
        this.mesh.materialReplace("LAU_10q", "LAU_10HVq");
    }

    public void jettisonCap() {
        this.drawing(false);
        this.setMassa(1E-006F);
        this.setDragCx(1E-006F);
    }

    static {
        Class class1 = Pylon_LAU10_Cap_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU10_gn16/monocap.sim");
        Property.set(class1, "massa", 8F);
        Property.set(class1, "dragCx", 0.011F);
        Property.set(class1, "drag", 0.011F);
        Property.set(class1, "bMinusDrag", 1);
    }
}
