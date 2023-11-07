package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitFw189 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitFw189.this.setTmp = CockpitFw189.this.setOld;
            CockpitFw189.this.setOld = CockpitFw189.this.setNew;
            CockpitFw189.this.setNew = CockpitFw189.this.setTmp;
            CockpitFw189.this.setNew.altimeter = CockpitFw189.this.fm.getAltitude();
            CockpitFw189.this.setNew.throttlel = ((10F * CockpitFw189.this.setOld.throttlel) + CockpitFw189.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitFw189.this.setNew.throttler = ((10F * CockpitFw189.this.setOld.throttler) + CockpitFw189.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            CockpitFw189.this.setNew.waypointAzimuth.setDeg(CockpitFw189.this.setOld.waypointAzimuth.getDeg(0.1F), (CockpitFw189.this.waypointAzimuth() - CockpitFw189.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
            if (Math.abs(CockpitFw189.this.fm.Or.getKren()) < 30F) {
                CockpitFw189.this.setNew.azimuth.setDeg(CockpitFw189.this.setOld.azimuth.getDeg(1.0F), CockpitFw189.this.fm.Or.azimut());
            }
            CockpitFw189.this.setNew.vspeed = ((499F * CockpitFw189.this.setOld.vspeed) + CockpitFw189.this.fm.getVertSpeed()) / 500F;
            if (CockpitFw189.this.fm.isTick(12, 0)) {
                Airport airport = Airport.nearest(CockpitFw189.this.fm.Loc, -1, 1);
                if ((airport != null) && airport.nearestRunway(CockpitFw189.this.fm.Loc, CockpitFw189.this.tmpLoc)) {
                    CockpitFw189.this.tmpLoc.transformInv(CockpitFw189.this.fm.Loc, CockpitFw189.this.tmpP);
                    CockpitFw189.this.tmpV.x = CockpitFw189.this.tmpP.x;
                    CockpitFw189.this.tmpV.y = CockpitFw189.this.tmpP.y;
                    CockpitFw189.this.tmpV.z = CockpitFw189.this.tmpP.z;
                    CockpitFw189.this.tmpV.normalize();
                    CockpitFw189.this.tmpV.z -= 0.034899D;
                    if (CockpitFw189.this.tmpV.x > 0.96D) {
                        if (CockpitFw189.this.tmpV.y > 0.0D) {
                            CockpitFw189.this.setNew.blindDotX = 1.889F * (float) Math.sqrt(CockpitFw189.this.tmpV.y);
                        } else {
                            CockpitFw189.this.setNew.blindDotX = -1.889F * (float) Math.sqrt(-CockpitFw189.this.tmpV.y);
                        }
                        if (CockpitFw189.this.tmpV.z > 0.0D) {
                            CockpitFw189.this.setNew.blindDotY = 1.889F * (float) Math.sqrt(CockpitFw189.this.tmpV.z);
                        } else {
                            CockpitFw189.this.setNew.blindDotY = -1.889F * (float) Math.sqrt(-CockpitFw189.this.tmpV.z);
                        }
                    } else {
                        CockpitFw189.this.setNew.blindDotX = 0.0F;
                        CockpitFw189.this.setNew.blindDotY = 0.0F;
                    }
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      blindDotX;
        float      blindDotY;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitFw189() {
        super("3DO/Cockpit/He-111H-2/CockpitFw189.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.tmpLoc = new Loc();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(218F, 143F, 128F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = (new String[] { "clocks1", "clocks2", "clocks2DMG", "clocks3", "clocks3DMG", "clocks4", "clocks5", "clocks6" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.fm.isTick(44, 0)) {
            if ((this.fm.AS.astateCockpitState & 8) == 0) {
                this.mesh.chunkVisible("Z_GearLRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
                this.mesh.chunkVisible("Z_GearRRed1", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
                this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
                this.mesh.chunkVisible("Z_GearRGreen1", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
            } else {
                this.mesh.chunkVisible("Z_GearLRed1", false);
                this.mesh.chunkVisible("Z_GearRRed1", false);
                this.mesh.chunkVisible("Z_GearLGreen1", false);
                this.mesh.chunkVisible("Z_GearRGreen1", false);
            }
            if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
                this.mesh.chunkVisible("zFuelWarning1", this.fm.M.fuel < 50F);
                this.mesh.chunkVisible("zFuelWarning2", this.fm.M.fuel < 50F);
            }
        }
        this.mesh.chunkSetAngles("zColumn1", 0.0F, 0.0F, -10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("zColumn2", 0.0F, 0.0F, -40F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)));
        if (this.fm.CT.getRudder() > 0.0F) {
            this.mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
            this.mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -45F * this.fm.CT.getRudder());
        } else {
            this.mesh.chunkSetAngles("zPedalL", 0.0F, 0.0F, -45F * this.fm.CT.getRudder());
            this.mesh.chunkSetAngles("zPedalR", 0.0F, 0.0F, -10F * this.fm.CT.getRudder());
        }
        this.mesh.chunkSetAngles("zTurretA", 0.0F, this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurretB", 0.0F, this.fm.turret[0].tu[1], 0.0F);
        this.mesh.chunkSetAngles("zOilFlap1", 0.0F, 0.0F, -50F * this.fm.EI.engines[0].getControlRadiator());
        this.mesh.chunkSetAngles("zOilFlap2", 0.0F, 0.0F, -50F * this.fm.EI.engines[1].getControlRadiator());
        this.mesh.chunkSetAngles("zMix1", 0.0F, 0.0F, -30F * this.fm.EI.engines[0].getControlMix());
        this.mesh.chunkSetAngles("zMix2", 0.0F, 0.0F, -30F * this.fm.EI.engines[1].getControlMix());
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -45F * this.fm.CT.FlapsControl);
        if (this.fm.EI.engines[0].getControlProp() >= 0.0F) {
            this.mesh.chunkSetAngles("zPitch1", 0.0F, 0.0F, -65F * this.fm.EI.engines[0].getControlProp());
        }
        if (this.fm.EI.engines[1].getControlProp() >= 0.0F) {
            this.mesh.chunkSetAngles("zPitch2", 0.0F, 0.0F, -65F * this.fm.EI.engines[1].getControlProp());
        }
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, 0.0F, -33.6F * this.interp(this.setNew.throttlel, this.setOld.throttlel, f));
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, 0.0F, -33.6F * this.interp(this.setNew.throttler, this.setOld.throttler, f));
        this.mesh.chunkSetAngles("zCompressor1", 0.0F, 0.0F, -25F * this.fm.EI.engines[0].getControlCompressor());
        this.mesh.chunkSetAngles("zCompressor2", 0.0F, 0.0F, -25F * this.fm.EI.engines[1].getControlCompressor());
        this.mesh.chunkSetAngles("zHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSecond", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.mesh.chunkSetAngles("zAH2", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -30F, 30F, -6.5F, 6.5F));
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -3F, 3F, 30F, -30F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        this.mesh.chunkSetAngles("zTurnBank", f1, 0.0F, 0.0F);
        float f2 = this.getBall(4.5D);
        this.mesh.chunkSetAngles("zBall", this.cvt(f2, -4F, 4F, -8F, 8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBall2", this.cvt(f2, -4.5F, 4.5F, -9F, 9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", this.cvt(this.setNew.vspeed, -15F, 15F, -160F, 160F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), CockpitFw189.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRepeater", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCompass", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zNavP", this.waypointAzimuth() - this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBlip1", 0.0F, 3F * this.setNew.blindDotY, 3F * this.setNew.blindDotX);
        if ((Math.abs(this.fm.Or.getKren()) < 20F) && (Math.abs(this.fm.Or.getTangage()) < 20F)) {
            this.mesh.chunkSetAngles("zMagnetic", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zRPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitFw189.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitFw189.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost2", this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 332F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 100F, 0.0F, 130F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 100F, 0.0F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-3", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-4", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-3", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-4", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 330F, 0.0F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 330F, 0.0F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zExtT", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 223.09F, 323.09F, -145F, 145F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zPatin", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        float f3 = (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f3 = (int) (-f3 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("zProp1-1", f3 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zProp1-2", f3 * 5F, 0.0F, 0.0F);
        f3 = (float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
        f3 = (int) (-f3 / 0.2F) * 0.2F;
        this.mesh.chunkSetAngles("zProp2-1", f3 * 60F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zProp2-2", f3 * 5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlapsIL", 145F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFlapsIR", 145F * this.fm.CT.getFlap(), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("ZHolesL_D1", true);
            this.mesh.chunkVisible("PanelL_D1", true);
            this.mesh.chunkVisible("PanelL_D0", false);
            this.mesh.chunkVisible("zVSI", false);
            this.mesh.chunkVisible("zBlip1", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("ZHolesL_D2", true);
            this.mesh.chunkVisible("PanelFloat_D1", true);
            this.mesh.chunkVisible("PanelFloat_D0", false);
            this.mesh.chunkVisible("zProp1-1", false);
            this.mesh.chunkVisible("zProp1-2", false);
            this.mesh.chunkVisible("zProp2-1", false);
            this.mesh.chunkVisible("zProp2-2", false);
            this.mesh.chunkVisible("zFlapsIL", false);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("ZHolesR_D1", true);
            this.mesh.chunkVisible("PanelR_D1", true);
            this.mesh.chunkVisible("PanelR_D0", false);
            this.mesh.chunkVisible("zRPM1", false);
            this.mesh.chunkVisible("zBoost2", false);
            this.mesh.chunkVisible("zOilTemp2", false);
            this.mesh.chunkVisible("zCoolant1", false);
            this.mesh.chunkVisible("zOFP1-1", false);
            this.mesh.chunkVisible("zOFP1-2", false);
            this.mesh.chunkVisible("zFlapsIR", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("ZHolesR_D2", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("ZHolesF_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PanelT_D1", true);
            this.mesh.chunkVisible("PanelT_D0", false);
            this.mesh.chunkVisible("zFuel2", false);
            this.mesh.chunkVisible("zOFP1-3", false);
            this.mesh.chunkVisible("zOFP1-4", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("zOil_D1", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0032F, 7.2F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Point3d            tmpP;
    private Vector3d           tmpV;
    private LightPointActor    light1;
    private Loc                tmpLoc;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };

}
