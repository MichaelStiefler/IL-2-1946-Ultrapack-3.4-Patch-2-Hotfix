package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_111H11 extends CockpitPilot {
    private class Variables {

        float      altimeter;
        float      throttlel;
        float      throttler;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      beaconDirection;
        float      beaconRange;
        float      turn;
        float      vspeed;
        float      cons;
        float      consL;
        float      consR;
        float      AFN101;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitHE_111H11.this.setTmp = CockpitHE_111H11.this.setOld;
            CockpitHE_111H11.this.setOld = CockpitHE_111H11.this.setNew;
            CockpitHE_111H11.this.setNew = CockpitHE_111H11.this.setTmp;
            CockpitHE_111H11.this.setNew.altimeter = CockpitHE_111H11.this.fm.getAltitude();
            CockpitHE_111H11.this.setNew.throttlel = ((10F * CockpitHE_111H11.this.setOld.throttlel) + CockpitHE_111H11.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitHE_111H11.this.setNew.throttler = ((10F * CockpitHE_111H11.this.setOld.throttler) + CockpitHE_111H11.this.fm.EI.engines[1].getControlThrottle()) / 11F;
            CockpitHE_111H11.this.w.set(CockpitHE_111H11.this.fm.getW());
            CockpitHE_111H11.this.fm.Or.transform(CockpitHE_111H11.this.w);
            CockpitHE_111H11.this.setNew.turn = ((12F * CockpitHE_111H11.this.setOld.turn) + CockpitHE_111H11.this.w.z) / 13F;
            CockpitHE_111H11.this.mesh.chunkSetAngles("zTurretA", 0.0F, CockpitHE_111H11.this.fm.turret[0].tu[0], 0.0F);
            CockpitHE_111H11.this.mesh.chunkSetAngles("zTurretB", 0.0F, CockpitHE_111H11.this.fm.turret[0].tu[1], 0.0F);
            float f = CockpitHE_111H11.this.waypointAzimuth();
            if (CockpitHE_111H11.this.useRealisticNavigationInstruments()) {
                CockpitHE_111H11.this.setNew.waypointAzimuth.setDeg(f - 90F);
                CockpitHE_111H11.this.setOld.waypointAzimuth.setDeg(f - 90F);
                CockpitHE_111H11.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_111H11.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitHE_111H11.this.radioCompassAzimuthInvertMinus() - CockpitHE_111H11.this.setOld.azimuth.getDeg(1.0F) - 90F);
            } else {
                CockpitHE_111H11.this.setNew.waypointAzimuth.setDeg(CockpitHE_111H11.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHE_111H11.this.setOld.azimuth.getDeg(1.0F));
            }
            if (Math.abs(CockpitHE_111H11.this.fm.Or.getKren()) < 30F) {
                CockpitHE_111H11.this.setNew.azimuth.setDeg(CockpitHE_111H11.this.setOld.azimuth.getDeg(1.0F), CockpitHE_111H11.this.fm.Or.azimut());
            }
            CockpitHE_111H11.this.setNew.vspeed = ((499F * CockpitHE_111H11.this.setOld.vspeed) + CockpitHE_111H11.this.fm.getVertSpeed()) / 500F;
            CockpitHE_111H11.this.setNew.beaconDirection = ((10F * CockpitHE_111H11.this.setOld.beaconDirection) + CockpitHE_111H11.this.getBeaconDirection()) / 11F;
            CockpitHE_111H11.this.setNew.beaconRange = ((10F * CockpitHE_111H11.this.setOld.beaconRange) + CockpitHE_111H11.this.getBeaconRange()) / 11F;
            float f1 = CockpitHE_111H11.this.prevFuel - CockpitHE_111H11.this.fm.M.fuel;
            CockpitHE_111H11.this.prevFuel = CockpitHE_111H11.this.fm.M.fuel;
            f1 /= 0.72F;
            f1 /= Time.tickLenFs();
            f1 *= 3600F;
            CockpitHE_111H11.this.setNew.cons = (0.9F * CockpitHE_111H11.this.setOld.cons) + (0.1F * f1);
            float f2 = CockpitHE_111H11.this.fm.EI.engines[0].getEngineForce().x;
            float f3 = CockpitHE_111H11.this.fm.EI.engines[1].getEngineForce().x;
            if ((f2 < 100F) || (CockpitHE_111H11.this.fm.EI.engines[0].getRPM() < 600F)) {
                f2 = 1.0F;
            }
            if ((f3 < 100F) || (CockpitHE_111H11.this.fm.EI.engines[1].getRPM() < 600F)) {
                f3 = 1.0F;
            }
            CockpitHE_111H11.this.setNew.consL = (0.9F * CockpitHE_111H11.this.setOld.consL) + ((0.1F * (CockpitHE_111H11.this.setNew.cons * f2)) / (f2 + f3));
            CockpitHE_111H11.this.setNew.consR = (0.9F * CockpitHE_111H11.this.setOld.consR) + ((0.1F * (CockpitHE_111H11.this.setNew.cons * f3)) / (f2 + f3));
            float f4 = CockpitHE_111H11.this.fm.Or.getKren();
            float f5 = CockpitHE_111H11.this.fm.Or.getTangage();
            if ((f4 > 55F) || (f4 < -55F) || (f5 < -55F) || (f5 > 55F)) {
                CockpitHE_111H11.this.Pn.z = 236D;
            } else {
                CockpitHE_111H11.this.Pn.set(CockpitHE_111H11.this.fm.Loc);
                CockpitHE_111H11.this.Pn.z = CockpitHE_111H11.this.fm.getAltitude() - Engine.cur.land.HQ(CockpitHE_111H11.this.Pn.x, CockpitHE_111H11.this.Pn.y);
                double d = CockpitHE_111H11.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f4));
                double d1 = CockpitHE_111H11.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f5));
                CockpitHE_111H11.this.Pn.z = (float) Math.sqrt((d * d) + (d1 * d1) + (CockpitHE_111H11.this.Pn.z * CockpitHE_111H11.this.Pn.z));
                if (CockpitHE_111H11.this.fm.CT.getGear() > 0.5F) {
                    CockpitHE_111H11.this.Pn.z = CockpitHE_111H11.this.cvt((float) CockpitHE_111H11.this.Pn.z, 0.0F, 150F, 0.0F, 236F);
                } else {
                    CockpitHE_111H11.this.Pn.z = CockpitHE_111H11.this.cvt((float) CockpitHE_111H11.this.Pn.z, 0.0F, 750F, 0.0F, 236F);
                }
            }
            CockpitHE_111H11.this.setNew.AFN101 = (0.9F * CockpitHE_111H11.this.setOld.AFN101) + (0.1F * (float) CockpitHE_111H11.this.Pn.z);
            return true;
        }

        Interpolater() {
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            if (this.fm.actor instanceof HE_111) {
                ((HE_111) this.fm.actor).bPitUnfocused = false;
            } else if (this.fm.actor instanceof HE_111xyz) {
                ((HE_111xyz) this.fm.actor).bPitUnfocused = false;
            }
            this.bTurrVisible = this.aircraft().hierMesh().isChunkVisible("Turret1C_D0");
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Head1_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Window_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.fm.actor instanceof HE_111) {
            ((HE_111) this.fm.actor).bPitUnfocused = true;
        } else if (this.fm.actor instanceof HE_111xyz) {
            ((HE_111xyz) this.fm.actor).bPitUnfocused = true;
        }
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.bTurrVisible);
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0") || this.aircraft().hierMesh().isChunkVisible("Nose_D1") || this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", this.aircraft().hierMesh().isChunkVisible("Turret1B_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Head1_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot1_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot2_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot2_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot2_D1"));
        this.aircraft().hierMesh().chunkVisible("Window_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitHE_111H11() {
        super("3DO/Cockpit/He-111P-4/hier-H11.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.w = new Vector3f();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(218F, 143F, 128F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        this.cockpitNightMats = new String[] { "clocks1C", "clocks2C", "clocks3B", "clocks4", "clocks5", "clocks6", "clocks7", "clocks8C" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
        this.prevFuel = 0.0F;
        this.Pn = new Point3d();
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
                this.mesh.chunkVisible("zFuelWarning1", this.fm.M.fuel < 600F);
                this.mesh.chunkVisible("zFuelWarning2", this.fm.M.fuel < 600F);
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
        this.mesh.chunkSetAngles("zOilFlap1", 0.0F, 0.0F, -50F * this.fm.EI.engines[0].getControlRadiator());
        this.mesh.chunkSetAngles("zOilFlap2", 0.0F, 0.0F, -50F * this.fm.EI.engines[1].getControlRadiator());
        this.mesh.chunkSetAngles("zMix1", 0.0F, 0.0F, -30F * this.fm.EI.engines[0].getControlMix());
        this.mesh.chunkSetAngles("zMix2", 0.0F, 0.0F, -30F * this.fm.EI.engines[1].getControlMix());
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 0.0F, -45F * this.fm.CT.FlapsControl);
        this.mesh.chunkSetAngles("zFlaps", 80F * this.fm.CT.getFlap(), 0.0F, 0.0F);
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
        this.mesh.chunkSetAngles("zHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, -6F, 714F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, -6F, 356F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSecond", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, -6F, 356F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), CockpitHE_111H11.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, -90F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVSI", this.cvt(this.setNew.vspeed, -15F, 15F, -135F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zTurnBank", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
        float f1 = this.getBall(4.5D);
        this.mesh.chunkSetAngles("zBall", this.cvt(f1, -4F, 4F, -6F, 6F), 0.0F, 0.0F);
        float f2 = this.fm.Or.getKren();
        this.mesh.chunkSetAngles("zAH1", 0.0F, -f2, 0.0F);
        this.resetYPRmodifier();
        float f3 = this.fm.Or.getTangage();
        Cockpit.xyz[1] = this.cvt(f3, -45F, 45F, -0.014F, 0.014F);
        this.mesh.chunkSetLocate("zHorizon", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zBall2", this.cvt(f1, -4F, 4F, -12.5F, 12.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBall3", this.cvt(f1, -4F, 4F, -12.5F, 12.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAFN101a", this.setNew.AFN101, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("AFN101b", this.cvt(this.fm.CT.getGear(), 0.5F, 0.6F, 0.0F, -45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("AFN-2A", 0.0F, this.cvt(this.setNew.beaconDirection, -45F, 45F, -14F, 14F), 0.0F);
        this.mesh.chunkSetAngles("AFN-2B", 0.0F, this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkVisible("AFN-2-RED", this.isOnBlindLandingMarker());
        this.mesh.chunkSetAngles("zRPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3600F, 0.0F, 6F), CockpitHE_111H11.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3600F, 0.0F, 6F), CockpitHE_111H11.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zBoost2", this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 328F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zCoolant2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, 70F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, -135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP1-2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, -135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOFP2-2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFC-1", this.cvt(this.setNew.consL, 0.0F, 500F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFC-2", this.cvt(this.setNew.consR, 0.0F, 500F, 0.0F, -270F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("zCompass", -this.setNew.azimuth.getDeg(f) + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zRepeater", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.waypointAzimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", this.setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zMagnetic", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("zRepeater", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zCompass", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zMagnetic", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("zFuel1", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 34.5F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 2000F, 34.5F, 140F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1b", this.cvt(this.fm.M.fuel / 0.72F, 2000F, 2835F, 49F, 132.6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zExtT", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 313.09F, 43.7F, 136.8F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("ZHolesL_D1", true);
            this.mesh.chunkVisible("PanelR_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("ZHolesL_D2", true);
            this.mesh.chunkVisible("InstrumPanelL_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("ZHolesR_D1", true);
            this.mesh.chunkVisible("InstrumPanelL_D2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("ZHolesR_D2", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("ZHolesF_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("PanelT_D1", true);
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
    private LightPointActor    light1;
    private float              pictAiler;
    private float              pictElev;
    public Vector3f            w;
    private float              prevFuel;
    private Point3d            Pn;
    private boolean            bTurrVisible;
    private static final float speedometerScale[] = { 0.0F, 6.01F, 18.5F, 43.65F, 75.81F, 113.39F, 152F, 192.26F, 232.24F, 270.67F, 308.39F, 343.38F };
    private static final float rpmScale[]         = { 0.0F, 14.7F, 76.15F, 143.86F, 215.97F, 282.68F, 346.18F };

    static {
        Property.set(CockpitHE_111H11.class, "normZNs", new float[] { 1.0F, 1.0F, 1.25F, 1.0F });
    }
}
