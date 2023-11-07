package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitA6M7_62 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitA6M7_62.this.fm != null) {
                CockpitA6M7_62.this.setTmp = CockpitA6M7_62.this.setOld;
                CockpitA6M7_62.this.setOld = CockpitA6M7_62.this.setNew;
                CockpitA6M7_62.this.setNew = CockpitA6M7_62.this.setTmp;
                if (CockpitA6M7_62.this.cockpitDimControl) {
                    if (CockpitA6M7_62.this.setNew.dimPosition > 0.0F) {
                        CockpitA6M7_62.this.setNew.dimPosition = CockpitA6M7_62.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitA6M7_62.this.setNew.dimPosition < 1.0F) {
                    CockpitA6M7_62.this.setNew.dimPosition = CockpitA6M7_62.this.setOld.dimPosition + 0.05F;
                }
                if ((CockpitA6M7_62.this.fm.AS.astateCockpitState & 2) != 0) {
                    if (CockpitA6M7_62.this.setNew.stbyPosition > 0.0F) {
                        CockpitA6M7_62.this.setNew.stbyPosition = CockpitA6M7_62.this.setOld.stbyPosition - 0.025F;
                    }
                } else if (CockpitA6M7_62.this.setNew.stbyPosition < 1.0F) {
                    CockpitA6M7_62.this.setNew.stbyPosition = CockpitA6M7_62.this.setOld.stbyPosition + 0.025F;
                }
                CockpitA6M7_62.this.setNew.throttle = (0.9F * CockpitA6M7_62.this.setOld.throttle) + (0.1F * CockpitA6M7_62.this.fm.CT.PowerControl);
                CockpitA6M7_62.this.setNew.prop = (0.9F * CockpitA6M7_62.this.setOld.prop) + (0.1F * CockpitA6M7_62.this.fm.EI.engines[0].getControlProp());
                CockpitA6M7_62.this.setNew.mix = (0.8F * CockpitA6M7_62.this.setOld.mix) + (0.2F * CockpitA6M7_62.this.fm.EI.engines[0].getControlMix());
                CockpitA6M7_62.this.setNew.man = (0.92F * CockpitA6M7_62.this.setOld.man) + (0.08F * CockpitA6M7_62.this.fm.EI.engines[0].getManifoldPressure());
                CockpitA6M7_62.this.setNew.altimeter = CockpitA6M7_62.this.fm.getAltitude();
                if (Math.abs(CockpitA6M7_62.this.fm.Or.getKren()) < 30F) {
                    CockpitA6M7_62.this.setNew.azimuth.setDeg(CockpitA6M7_62.this.setOld.azimuth.getDeg(1.0F), CockpitA6M7_62.this.fm.Or.azimut());
                }
                CockpitA6M7_62.this.setNew.vspeed = ((199F * CockpitA6M7_62.this.setOld.vspeed) + CockpitA6M7_62.this.fm.getVertSpeed()) / 200F;
                float f = CockpitA6M7_62.this.waypointAzimuth();
                if (CockpitA6M7_62.this.useRealisticNavigationInstruments()) {
                    CockpitA6M7_62.this.setNew.waypointAzimuth.setDeg(CockpitA6M7_62.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitA6M7_62.this.getBeaconDirection());
                    CockpitA6M7_62.this.setNew.waypointDirection.setDeg(CockpitA6M7_62.this.setOld.waypointDirection.getDeg(1.0F), f);
                } else {
                    CockpitA6M7_62.this.setNew.waypointAzimuth.setDeg(CockpitA6M7_62.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitA6M7_62.this.setOld.azimuth.getDeg(1.0F));
                    CockpitA6M7_62.this.setNew.waypointDirection.setDeg(CockpitA6M7_62.this.setOld.waypointDirection.getDeg(0.1F), CockpitA6M7_62.this.fm.Or.azimut() + 90F);
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      man;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDirection;
        float      vspeed;
        float      dimPosition;
        float      stbyPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDirection = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(5F);
    }

    public CockpitA6M7_62() {
        super("3DO/Cockpit/A6M7_62/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.light1 = new LightPointActor(new LightPoint(), new Point3d(-0.1756D, 0.3924D, 0.5913D));
        this.light2 = new LightPointActor(new LightPoint(), new Point3d(-0.1479D, -0.3612D, 0.5913D));
        this.light1.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light2.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.setNew.dimPosition = 1.0F;
        this.cockpitNightMats = (new String[] { "gauges1", "gauges2", "gauges3", "gauges4", "gauges5", "gauges1_d1", "gauges2_d1", "gauges3_d1", "gauges4_d1", "turnbank", "pressmix" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.6F);
        this.mesh.chunkSetLocate("Blister_D0", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("sunOFF", 0.0F, this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -77F), 0.0F);
        this.mesh.chunkSetAngles("sight_rev", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition, this.setOld.stbyPosition, f), 0.0F, 1.0F, 0.0F, -115F), 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 15.5F);
        this.mesh.chunkSetAngles("Stick_tube", 0.0F, -15.5F * this.pictElev, 0.0F);
        if ((this.fm.CT.Weapons[3] != null) && !this.fm.CT.Weapons[3][0].haveBullets()) {
            this.mesh.chunkSetAngles("Turn1", 0.0F, 53F, 0.0F);
        }
        this.mesh.chunkSetAngles("Turn2", 0.0F, this.fm.Gears.bTailwheelLocked ? 53F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turn3", 0.0F, 68F * this.cvt(this.interp(this.setNew.mix, this.setOld.mix, f), 1.0F, 1.2F, 0.5F, 1.0F), 0.0F);
        this.mesh.chunkSetAngles("Turn3_rod", 0.0F, -68F * this.cvt(this.interp(this.setNew.mix, this.setOld.mix, f), 1.0F, 1.2F, 0.5F, 1.0F), 0.0F);
        this.mesh.chunkSetAngles("Turn5", 0.0F, 75F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F);
        this.mesh.chunkSetAngles("Turn6", 0.0F, 68F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("Turn6_rod", 0.0F, -68F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("Turn7", 0.0F, this.fm.CT.saveWeaponControl[1] ? 26F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turn8", 0.0F, 20F - (40F * this.fm.EI.engines[0].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Pedals", 11F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ped_trossL", -11F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Ped_trossR", -11F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil1", this.cvt(this.fm.Or.getTangage(), -10F, 10F, -10F, 10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil3", this.cvt(this.fm.Or.getKren(), -10F, 10F, -10F, 10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompassOil2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Azimuth1a", 0.0F, -this.setNew.waypointDirection.getDeg(f * 0.1F), 0.0F);
        this.mesh.chunkSetAngles("Z_Navigation", 0.0F, this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -25F, 25F, -45F, 45F), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMix(), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("Z_Clock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Clock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Magneto", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -104F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 740.7998F, 0.0F, 20F), CockpitA6M7_62.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Alt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 14400F), 0.0F);
        this.mesh.chunkSetAngles("Z_Alt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F);
        this.mesh.chunkSetAngles("Z_Horison1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_Horison1b", 0.0F, this.cvt(this.fm.Or.getTangage(), -33F, 33F, 33F, -33F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Horison1c", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Turn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
        this.mesh.chunkSetAngles("Z_Turn1b", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -14F, 14F), 0.0F);
        this.mesh.chunkSetAngles("Z_Vspeed", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_TempCilinder", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("Z_TempOil", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 7F), CockpitA6M7_62.oilScale), 0.0F);
        this.mesh.chunkSetAngles("Z_pressFuel", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 8F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_pressOil", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 5F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Revolution", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 4500F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold", 0.0F, this.cvt(this.setNew.man, 0.400051F, 1.333305F, -202.5F, 112.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelWing", 0.0F, this.cvt(this.fm.M.fuel * 1.388F, 0.0F, 250F, 0.0F, 235F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelFuse", 0.0F, this.cvt(this.fm.M.fuel * 1.388F, 0.0F, 80F, 0.0F, 264F), 0.0F);
        this.mesh.chunkSetAngles("Z_PressMixt1a", 0.0F, this.cvt((float) Math.sqrt(Math.sqrt(this.fm.M.nitro)), 0.0F, 2.783F, 0.0F, 330F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes4_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("Z_Clock1a", false);
            this.mesh.chunkVisible("Z_Clock1b", false);
            this.mesh.chunkVisible("Z_TempOil", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("Z_Horison1c", false);
            this.mesh.chunkVisible("Z_AirSpeed", false);
            this.mesh.chunkVisible("Z_pressFuel", false);
            this.mesh.chunkVisible("Z_pressOil", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes3_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Z_Holes3_D1", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.5F, 0.25F);
            this.light2.light.setEmit(1.0F, 0.25F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 45F, 48.5F, 51.5F, 80F, 129F, 180F, 227F, 270.5F, 315F, 360F, 401F, 440F, 476F, 512F, 546.4F, 579F, 608F, 637.5F, 666.5F, 696.5F, 726F };
    private static final float oilScale[]         = { 0.0F, -27.5F, 12F, 59.5F, 127F, 212.5F, 311.5F };

}
