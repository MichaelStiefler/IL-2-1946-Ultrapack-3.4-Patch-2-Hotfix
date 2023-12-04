package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Pylon_LAU130_TC_gn16 extends Pylon {
    public void matGreen() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("LAU10o", "LAU10greeno");
        this.mesh.materialReplace("LAU10p", "LAU10greenp");
        this.mesh.materialReplace("LAU10q", "LAU10greenq");
    }

    public void matBrown() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("LAU10o", "LAU10browno");
        this.mesh.materialReplace("LAU10p", "LAU10brownp");
        this.mesh.materialReplace("LAU10q", "LAU10brownq");
    }

    static {
        Class class1 = Pylon_LAU130_TC_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU130_gn16/monoTC.sim");
        Property.set(class1, "massa", 93F);
        Property.set(class1, "dragCx", 0.028F);
        Property.set(class1, "drag", 0.028F);
    }
}
