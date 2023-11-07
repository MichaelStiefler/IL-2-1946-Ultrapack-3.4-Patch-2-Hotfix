package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitRWD_10 extends CockpitPilot {
    private class Variables {

        float altimeter;
        float throttle;
        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitRWD_10.this.bNeedSetUp) {
                CockpitRWD_10.this.reflectPlaneMats();
                CockpitRWD_10.this.bNeedSetUp = false;
            }
            if (RWD_10.bChangedPit) {
                CockpitRWD_10.this.reflectPlaneToModel();
                RWD_10.bChangedPit = false;
            }
            CockpitRWD_10.this.setTmp = CockpitRWD_10.this.setOld;
            CockpitRWD_10.this.setOld = CockpitRWD_10.this.setNew;
            CockpitRWD_10.this.setNew = CockpitRWD_10.this.setTmp;
            CockpitRWD_10.this.setNew.altimeter = CockpitRWD_10.this.fm.getAltitude();
            if (Math.abs(CockpitRWD_10.this.fm.Or.getKren()) < 30F) {
                CockpitRWD_10.this.setNew.throttle = ((10F * CockpitRWD_10.this.setOld.throttle) + CockpitRWD_10.this.fm.CT.PowerControl) / 11F;
            }
            CockpitRWD_10.this.w.set(CockpitRWD_10.this.fm.getW());
            CockpitRWD_10.this.fm.Or.transform(CockpitRWD_10.this.w);
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitRWD_10() {
        super("3DO/Cockpit/RWD-10/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("zAlt", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 350F, 0.0F, 14F), CockpitRWD_10.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zRPM", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 5F), CockpitRWD_10.rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, 20F * (this.pictAiler = (0.75F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 20F * (this.pictElev = (0.75F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("StickBase", 0.0F, 26F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("StickRodL", 0.0F, -26F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("StickRodR", 0.0F, -26F * this.pictAiler, 0.0F);
        this.mesh.chunkSetAngles("StickElevRod", 0.0F, this.pictElev * 26F, 0.0F);
        this.mesh.chunkSetAngles("Rudder", 26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableL", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RCableR", -26F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Throttle", 30F - (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ThrottleWire", -30F + (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPrs", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 272F), 0.0F);
        this.mesh.chunkSetAngles("zFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 60F, 0.0F, 272F), 0.0F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Cart_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (Actor.isAlive(this.aircraft())) {
            this.aircraft().hierMesh().chunkVisible("Cart_D0", true);
        }
        super.doFocusLeave();
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", hiermesh.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", hiermesh.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", hiermesh.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", hiermesh.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingLIn_CAP", hiermesh.isChunkVisible("WingLIn_CAP"));
        this.mesh.chunkVisible("WingRIn_D0", hiermesh.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", hiermesh.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", hiermesh.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", hiermesh.isChunkVisible("WingRIn_D3"));
        this.mesh.chunkVisible("WingRIn_CAP", hiermesh.isChunkVisible("WingRIn_CAP"));
        this.mesh.chunkVisible("WingLOut_CAP", hiermesh.isChunkVisible("WingLOut_CAP"));
        this.mesh.chunkVisible("WingROut_CAP", hiermesh.isChunkVisible("WingROut_CAP"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", mat);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 18F, 45F, 72F, 99F, 126F, 153F, 180F, 207F, 234F, 261F, 288F, 315F, 342F };
    private static final float rpmScale[]         = { 0.0F, 18F, 99F, 180F, 261F, 342F };

}
