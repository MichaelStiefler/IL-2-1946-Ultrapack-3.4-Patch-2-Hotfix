package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Pylon_LAU131_Cap_gn16 extends Pylon {
    public void matGreen() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("Ordnance2", "Ordnance2green");
        this.mesh.materialReplace("Ordnance2p", "Ordnance2greenp");
        this.mesh.materialReplace("Ordnance2q", "Ordnance2greenq");
    }

    public void matBrown() {
        this.setMesh(Property.stringValue(this.getClass(), "mesh"));
        this.mesh.materialReplace("Ordnance2", "Ordnance2brown");
        this.mesh.materialReplace("Ordnance2p", "Ordnance2brownp");
        this.mesh.materialReplace("Ordnance2q", "Ordnance2brownq");
    }

    public void jettisonCap() {
        this.drawing(false);
        this.setMassa(1E-006F);
        this.setDragCx(1E-006F);
    }

    static {
        Class class1 = Pylon_LAU131_Cap_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU131_gn16/monocap.sim");
        Property.set(class1, "massa", 6F);
        Property.set(class1, "dragCx", 0.01F);
        Property.set(class1, "drag", 0.01F);
        Property.set(class1, "bMinusDrag", 1);
    }
}
