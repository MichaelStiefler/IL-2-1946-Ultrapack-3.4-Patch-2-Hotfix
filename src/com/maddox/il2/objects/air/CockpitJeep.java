package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitJeep extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitJeep.this.fm != null) {
                CockpitJeep.this.setTmp = CockpitJeep.this.setOld;
                CockpitJeep.this.setOld = CockpitJeep.this.setNew;
                CockpitJeep.this.setNew = CockpitJeep.this.setTmp;
                if (CockpitJeep.this.cockpitDimControl) {
                    if (CockpitJeep.this.setNew.dimPosition > 0.0F) {
                        CockpitJeep.this.setNew.dimPosition = CockpitJeep.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitJeep.this.setNew.dimPosition < 1.0F) {
                    CockpitJeep.this.setNew.dimPosition = CockpitJeep.this.setOld.dimPosition + 0.05F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float dimPosition;
        private Variables() {
        }

    }

    public CockpitJeep() {
        super("3do/cockpit/Jeep/hier.him", "p39");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.setNew.dimPosition = 1.0F;
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(0.6D, 0.0D, 0.8D));
        this.light1.light.setColor(232F, 75F, 44F);
        this.light1.light.setEmit(0.0F, 0.0F);
        super.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        super.cockpitDimControl = !super.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        super.mesh.chunkSetAngles("AirSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) ((Tuple3d) (this.fm.Loc)).z, super.fm.getSpeedKMH() / 1.6F), 0.0F, 150F, 0.0F, 10F), CockpitJeep.speedometerScale), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Oil", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, 116.5F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("CoolantTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Fuel", this.cvt(this.fm.M.fuel, 0.0F, 155F, 0.0F, 120F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_AroneL", 0.0F, -180F * this.fm.CT.getRudder(), 0.0F);
        super.mesh.chunkSetAngles("Z_Wiper1", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 100F), 0.0F, 0.0F);
        super.mesh.chunkSetAngles("Z_Wiper3", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 100F), 0.0F, 0.0F);
    }

    public void toggleDim() {
        super.cockpitDimControl = !super.cockpitDimControl;
    }

    public void reflectCockpitState() {
    }

    public void toggleLight() {
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private static final float speedometerScale[] = { 0.0F, 17F, 80.5F, 143.5F, 205F, 227F, 249F, 271.5F, 294F, 317F, 339.5F, 341.5F };
}
