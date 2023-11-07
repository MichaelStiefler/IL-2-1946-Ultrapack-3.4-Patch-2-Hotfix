package com.maddox.il2.objects.air;

import com.maddox.il2.engine.InterpolateRef;
import com.maddox.rts.Time;

public class CockpitWagen extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitWagen() {
        super("3do/cockpit/Wagen/hier.him", "p39");
//        this.light1.light.setColor(232F, 75F, 44F);
//        this.light1.light.setEmit(0.0F, 0.0F);
//        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
    }

    public void reflectCockpitState() {
    }

    public void toggleLight() {
    }

//    private LightPointActor light1;

}
