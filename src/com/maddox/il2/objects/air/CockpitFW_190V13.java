package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.Property;

public class CockpitFW_190V13 extends CockpitFW_190V {
    public CockpitFW_190V13() {
        super("3DO/Cockpit/FW-190V13/hier.him", "bf109");
        this.cockpitNightMats = (new String[] { "A4GP1", "A4GP2", "A4GP3", "A4GP4", "A4GP5", "A4GP6", "A5GP3Km", "V13GP1", "V13GP2", "V13GP4", "EQpt5" });
        this.setNightMats(false);
    }

    public void reflectWorldToInstruments(float f) {
        super.reflectWorldToInstruments(f);
        this.mesh.chunkSetAngles("NeedleCD", this.setNew.vspeed < 0.0F ? this.floatindex(this.cvt(-this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitFW_190V.vsiNeedleScale) : -this.floatindex(this.cvt(this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitFW_190V.vsiNeedleScale), 0.0F, 0.0F);
        if (HotKeyCmd.getByRecordedId(142).isActive() && Main3D.cur3D().aircraftHotKeys.isPropAuto()) {
            this.mesh.chunkSetAngles("SwitchProp", 0.0F, -30F, 0.0F);
        }
        if (HotKeyCmd.getByRecordedId(142).isActive() && !Main3D.cur3D().aircraftHotKeys.isPropAuto()) {
            this.mesh.chunkSetAngles("SwitchProp", 0.0F, 30F, 0.0F);
        }
    }

    static {
        Property.set(CockpitFW_190V13.class, "normZN", 0.72F);
        Property.set(CockpitFW_190V13.class, "gsZN", 0.66F);
    }

}
