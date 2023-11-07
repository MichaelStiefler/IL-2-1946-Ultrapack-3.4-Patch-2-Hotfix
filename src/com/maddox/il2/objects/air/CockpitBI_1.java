package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitBI_1 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitBI_1.this.fm != null) {
                CockpitBI_1.this.setTmp = CockpitBI_1.this.setOld;
                CockpitBI_1.this.setOld = CockpitBI_1.this.setNew;
                CockpitBI_1.this.setNew = CockpitBI_1.this.setTmp;
                CockpitBI_1.this.setNew.throttle = ((10F * CockpitBI_1.this.setOld.throttle) + CockpitBI_1.this.fm.CT.PowerControl) / 11F;
                CockpitBI_1.this.setNew.altimeter = CockpitBI_1.this.fm.getAltitude();
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle;
        float altimeter;
        private Variables() {
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitBI_1() {
        super("3DO/Cockpit/BI-1/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "ONE", "TWO", "THREE" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.ypr[1] = -80.08F * this.interp(this.setNew.throttle, this.setOld.throttle, f);
        Cockpit.xyz[1] = Cockpit.ypr[1] >= -33F ? 0.0F : -0.0065F;
        this.mesh.chunkSetLocate("Throttle", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            this.mesh.chunkSetAngles("Lever_Gear", -17F, 0.0F, 0.0F);
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            this.mesh.chunkSetAngles("Lever_Gear", 15F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Lever_Gear", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                this.mesh.chunkSetAngles("Lever_Flaps", 15F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("Lever_Flaps", -20F, 0.0F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("Lever_Flaps", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1200F, 0.0F, 360F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.47124F, 0.47124F, 40F, -40F), 0.0F);
        this.mesh.chunkSetAngles("zSlide1a", this.cvt(this.getBall(8D), -8F, 8F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 300F, 0.0F, 180F), 0.0F);
        this.mesh.chunkVisible("Z_Red8", (this.fm.CT.getGear() > 0.05F) && (this.fm.CT.getGear() < 0.95F));
        this.mesh.chunkVisible("Z_Red5", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red7", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red4", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Red6", this.fm.CT.getGear() == 1.0F);
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 0x40) != 0) || ((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0) || ((this.fm.AS.astateCockpitState & 2) != 0)) {
            this.mesh.materialReplace("ONE", "ONE_D1");
            this.mesh.materialReplace("ONE_night", "ONE_D1_night");
            this.mesh.materialReplace("Dash", "Dash_D1");
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zSpeed1a", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0) || ((this.fm.AS.astateCockpitState & 0x80) != 0)) {
            this.mesh.materialReplace("THREE", "THREE_D1");
            this.mesh.materialReplace("THREE_night", "THREE_D1_night");
            this.mesh.chunkVisible("zSlide1a", false);
        }
        this.retoggleLight();
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
            this.mesh.chunkVisible("Z_Red11", true);
            this.mesh.chunkVisible("Z_Red14", true);
        } else {
            this.setNightMats(false);
            this.mesh.chunkVisible("Z_Red11", false);
            this.mesh.chunkVisible("Z_Red14", false);
        }
    }

    private void retoggleLight() {
        if (this.cockpitLightControl) {
            this.setNightMats(false);
            this.setNightMats(true);
        } else {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f   w;
    private float     pictAiler;
    private float     pictElev;

}
