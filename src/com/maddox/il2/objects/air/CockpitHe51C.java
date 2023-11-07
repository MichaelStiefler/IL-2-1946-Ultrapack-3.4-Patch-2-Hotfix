package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitHe51C extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHe51C.this.bNeedSetUp) {
                CockpitHe51C.this.reflectPlaneMats();
                CockpitHe51C.this.bNeedSetUp = false;
            }
            if (((He51C) CockpitHe51C.this.aircraft()).bChangedPit) {
                CockpitHe51C.this.reflectPlaneToModel();
                ((He51C) CockpitHe51C.this.aircraft()).bChangedPit = false;
            }
            CockpitHe51C.this.setTmp = CockpitHe51C.this.setOld;
            CockpitHe51C.this.setOld = CockpitHe51C.this.setNew;
            CockpitHe51C.this.setNew = CockpitHe51C.this.setTmp;
            CockpitHe51C.this.setNew.altimeter = CockpitHe51C.this.fm.getAltitude();
            if (Math.abs(CockpitHe51C.this.fm.Or.getKren()) < 30F) {
                CockpitHe51C.this.setNew.azimuth.setDeg(CockpitHe51C.this.setOld.azimuth.getDeg(1.0F), CockpitHe51C.this.fm.Or.azimut());
            }
            CockpitHe51C.this.setNew.throttle = ((10F * CockpitHe51C.this.setOld.throttle) + CockpitHe51C.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitHe51C.this.setNew.mix = ((10F * CockpitHe51C.this.setOld.mix) + CockpitHe51C.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitHe51C.this.setNew.prop = CockpitHe51C.this.setOld.prop;
            if (CockpitHe51C.this.setNew.prop < (CockpitHe51C.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitHe51C.this.setNew.prop += 0.0025F;
            }
            if (CockpitHe51C.this.setNew.prop > (CockpitHe51C.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitHe51C.this.setNew.prop -= 0.0025F;
            }
            CockpitHe51C.this.w.set(CockpitHe51C.this.fm.getW());
            CockpitHe51C.this.fm.Or.transform(CockpitHe51C.this.w);
            CockpitHe51C.this.setNew.turn = ((12F * CockpitHe51C.this.setOld.turn) + ((Tuple3f) (CockpitHe51C.this.w)).z) / 13F;
            CockpitHe51C.this.setNew.vspeed = ((299F * CockpitHe51C.this.setOld.vspeed) + CockpitHe51C.this.fm.getVertSpeed()) / 300F;
            CockpitHe51C.this.pictSupc = (0.8F * CockpitHe51C.this.pictSupc) + (0.2F * CockpitHe51C.this.fm.EI.engines[0].getControlCompressor());
            CockpitHe51C.this.updateCompass();
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      throttle;
        float      mix;
        float      prop;
        float      turn;
        float      vspeed;
        float      radiator;
        Point3d    planeLoc;
        Point3d    planeMove;
        Vector3d   compassPoint[];
        Vector3d   cP[];

        private Variables() {
            this.planeLoc = new Point3d();
            this.planeMove = new Point3d();
            this.compassPoint = new Vector3d[4];
            this.cP = new Vector3d[4];
            this.compassPoint[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), CockpitHe51C.compassZ);
            this.compassPoint[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), 0.0D, CockpitHe51C.compassZ);
            this.compassPoint[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), CockpitHe51C.compassZ);
            this.compassPoint[3] = new Vector3d(Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), 0.0D, CockpitHe51C.compassZ);
            this.cP[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), CockpitHe51C.compassZ);
            this.cP[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), 0.0D, CockpitHe51C.compassZ);
            this.cP[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), CockpitHe51C.compassZ);
            this.cP[3] = new Vector3d(Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ)), 0.0D, CockpitHe51C.compassZ);
            this.azimuth = new AnglesFork();
        }

    }

    public CockpitHe51C() {
        super("3DO/Cockpit/He51C/hier.him", "u2");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.rpmGeneratedPressure = 0.0F;
        this.oilPressure = 0.0F;
        this.compassFirst = 0;
        super.cockpitNightMats = (new String[] { "Compass", "gauge1", "gauge2", "gauge3", "gauge4", "gauge5", "DM_gauge1", "DM_gauge2" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (super.acoustics != null) {
            super.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    private void initCompass() {
        this.accel = new Vector3d();
        this.compassSpeed = new Vector3d[4];
        this.compassSpeed[0] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[1] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[2] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[3] = new Vector3d(0.0D, 0.0D, 0.0D);
        float af[] = { 87F, 77.5F, 65.3F, 41.5F, -0.3F, -43.5F, -62.9F, -64F, -66.3F, -75.8F };
        float af1[] = { 55.8F, 51.5F, 47F, 40.1F, 33.8F, 33.7F, 32.7F, 35.1F, 46.6F, 61F };
        float f = this.cvt(Engine.land().config.declin, -90F, 90F, 9F, 0.0F);
        float f1 = this.floatindex(f, af);
        this.compassNorth = new Vector3d(0.0D, Math.cos(0.0174527D * f1), -Math.sin(0.0174527D * f1));
        this.compassSouth = new Vector3d(0.0D, -Math.cos(0.0174527D * f1), Math.sin(0.0174527D * f1));
        float f2 = this.floatindex(f, af1);
        this.compassNorth.scale((f2 / 600F) * Time.tickLenFs());
        this.compassSouth.scale((f2 / 600F) * Time.tickLenFs());
        this.segLen1 = 2D * Math.sqrt(1.0D - (CockpitHe51C.compassZ * CockpitHe51C.compassZ));
        this.segLen2 = this.segLen1 / Math.sqrt(2D);
        this.compassLimit = -1D * Math.sin(0.01745328888888889D * CockpitHe51C.compassLimitAngle);
        this.compassLimit *= this.compassLimit;
        this.compassAcc = 4.666D * Time.tickLenFs();
        this.compassSc = 0.101936799D / Time.tickLenFs() / Time.tickLenFs();
    }

    private void updateCompass() {
        if (this.compassFirst == 0) {
            this.initCompass();
            super.fm.getLoc(this.setOld.planeLoc);
        }
        super.fm.getLoc(this.setNew.planeLoc);
        this.setNew.planeMove.set(this.setNew.planeLoc);
        this.setNew.planeMove.sub(this.setOld.planeLoc);
        this.accel.set(this.setNew.planeMove);
        this.accel.sub(this.setOld.planeMove);
        this.accel.scale(this.compassSc);
        this.accel.x = -this.accel.x;
        this.accel.y = -this.accel.y;
        this.accel.z = -this.accel.z - 1.0D;
        this.accel.scale(this.compassAcc);
        if (this.accel.length() > (-CockpitHe51C.compassZ * 0.7D)) {
            this.accel.scale((-CockpitHe51C.compassZ * 0.7D) / this.accel.length());
        }
        for (int i = 0; i < 4; i++) {
            this.compassSpeed[i].set(this.setOld.compassPoint[i]);
            this.compassSpeed[i].sub(this.setNew.compassPoint[i]);
        }

        for (int j = 0; j < 4; j++) {
            double d = this.compassSpeed[j].length();
            d = 0.985D / (1.0D + (d * d * 15D));
            this.compassSpeed[j].scale(d);
        }

        Vector3d vector3d = new Vector3d();
        vector3d.set(this.setOld.compassPoint[0]);
        vector3d.add(this.setOld.compassPoint[1]);
        vector3d.add(this.setOld.compassPoint[2]);
        vector3d.add(this.setOld.compassPoint[3]);
        vector3d.normalize();
        for (int k = 0; k < 4; k++) {
            Vector3d vector3d1 = new Vector3d();
            double d1 = vector3d.dot(this.compassSpeed[k]);
            vector3d1.set(vector3d);
            d1 *= 0.28D;
            vector3d1.scale(-d1);
            this.compassSpeed[k].add(vector3d1);
        }

        for (int l = 0; l < 4; l++) {
            this.compassSpeed[l].add(this.accel);
        }

        this.compassSpeed[0].add(this.compassNorth);
        this.compassSpeed[2].add(this.compassSouth);
        for (int i1 = 0; i1 < 4; i1++) {
            this.setNew.compassPoint[i1].set(this.setOld.compassPoint[i1]);
            this.setNew.compassPoint[i1].add(this.compassSpeed[i1]);
        }

        vector3d.set(this.setNew.compassPoint[0]);
        vector3d.add(this.setNew.compassPoint[1]);
        vector3d.add(this.setNew.compassPoint[2]);
        vector3d.add(this.setNew.compassPoint[3]);
        vector3d.scale(0.25D);
        Vector3d vector3d2 = new Vector3d(vector3d);
        vector3d2.normalize();
        vector3d2.scale(-CockpitHe51C.compassZ);
        vector3d2.sub(vector3d);
        for (int j1 = 0; j1 < 4; j1++) {
            this.setNew.compassPoint[j1].add(vector3d2);
        }

        for (int k1 = 0; k1 < 4; k1++) {
            this.setNew.compassPoint[k1].normalize();
        }

        for (int l1 = 0; l1 < 2; l1++) {
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[2], this.segLen1);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[3], this.segLen1);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[1], this.segLen2);
            this.compassDist(this.setNew.compassPoint[2], this.setNew.compassPoint[3], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[2], this.segLen2);
            this.compassDist(this.setNew.compassPoint[3], this.setNew.compassPoint[0], this.segLen2);
            for (int i2 = 0; i2 < 4; i2++) {
                this.setNew.compassPoint[i2].normalize();
            }

            this.compassDist(this.setNew.compassPoint[3], this.setNew.compassPoint[0], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[2], this.segLen2);
            this.compassDist(this.setNew.compassPoint[2], this.setNew.compassPoint[3], this.segLen2);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[1], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[3], this.segLen1);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[2], this.segLen1);
            for (int j2 = 0; j2 < 4; j2++) {
                this.setNew.compassPoint[j2].normalize();
            }

        }

        Orientation orientation = new Orientation();
        super.fm.getOrient(orientation);
        for (int k2 = 0; k2 < 4; k2++) {
            this.setNew.cP[k2].set(this.setNew.compassPoint[k2]);
            orientation.transformInv(this.setNew.cP[k2]);
        }

        Vector3d vector3d3 = new Vector3d();
        vector3d3.set(this.setNew.cP[0]);
        vector3d3.add(this.setNew.cP[1]);
        vector3d3.add(this.setNew.cP[2]);
        vector3d3.add(this.setNew.cP[3]);
        vector3d3.scale(0.25D);
        Vector3d vector3d4 = new Vector3d();
        vector3d4.set(vector3d3);
        vector3d4.normalize();
        float f = (float) ((vector3d4.x * vector3d4.x) + (vector3d4.y * vector3d4.y));
        if ((f > this.compassLimit) || (vector3d3.z > 0.0D)) {
            for (int l2 = 0; l2 < 4; l2++) {
                this.setNew.cP[l2].set(this.setOld.cP[l2]);
                this.setNew.compassPoint[l2].set(this.setOld.cP[l2]);
                orientation.transform(this.setNew.compassPoint[l2]);
            }

            vector3d3.set(this.setNew.cP[0]);
            vector3d3.add(this.setNew.cP[1]);
            vector3d3.add(this.setNew.cP[2]);
            vector3d3.add(this.setNew.cP[3]);
            vector3d3.scale(0.25D);
        }
        vector3d4.set(this.setNew.cP[0]);
        vector3d4.sub(vector3d3);
        double d2 = -Math.atan2(vector3d3.y, -vector3d3.z);
        this.vectorRot2(vector3d3, d2);
        this.vectorRot2(vector3d4, d2);
        double d3 = Math.atan2(vector3d3.x, -vector3d3.z);
        this.vectorRot1(vector3d4, -d3);
        double d4 = Math.atan2(vector3d4.y, vector3d4.x);
        this.mesh.chunkSetAngles("NeedCompass_A", -(float) ((d2 * 180D) / Math.PI), -(float) ((d3 * 180D) / Math.PI), 0.0F);
        this.mesh.chunkSetAngles("NeedCompass_B", 0.0F, (float) (90D - ((d4 * 180D) / Math.PI)), 0.0F);
        this.compassFirst++;
    }

    private void vectorRot1(Vector3d vector3d, double d) {
        double d1 = Math.sin(d);
        double d2 = Math.cos(d);
        double d3 = (vector3d.x * d2) - (vector3d.z * d1);
        vector3d.z = (vector3d.x * d1) + (vector3d.z * d2);
        vector3d.x = d3;
    }

    private void vectorRot2(Vector3d vector3d, double d) {
        double d1 = Math.sin(d);
        double d2 = Math.cos(d);
        double d3 = (vector3d.y * d2) - (vector3d.z * d1);
        vector3d.z = (vector3d.y * d1) + (vector3d.z * d2);
        vector3d.y = d3;
    }

    private void compassDist(Vector3d vector3d, Vector3d vector3d1, double d) {
        Vector3d vector3d2 = new Vector3d();
        vector3d2.set(vector3d);
        vector3d2.sub(vector3d1);
        double d1 = vector3d2.length();
        if (d1 < 0.000001D) {
            d1 = 0.000001D;
        }
        d1 = (d - d1) / d1 / 2D;
        vector3d2.scale(d1);
        vector3d.add(vector3d2);
        vector3d1.sub(vector3d2);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((CR_42X) ((He51C) this.aircraft())).bChangedPit) {
            this.reflectPlaneToModel();
            ((He51C) this.aircraft()).bChangedPit = false;
        }
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) ((Interpolate) (super.fm)).actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) ((Interpolate) (super.fm)).actor).getGunByHookName("_MGUN02");
        }
        this.mesh.chunkSetAngles("NeedManPress", 0.0F, this.pictManifold = (0.85F * this.pictManifold) + (0.15F * this.cvt(this.fm.EI.engines[0].getManifoldPressure() * 760F, 260F, 1200F, 33.3F, 360F)), 0.0F);
        float f2 = -15F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl));
        float f3 = 14F * (this.pictElev = (0.85F * this.pictElev) + (0.2F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("StickB", 0.0F, -f2, f3);
        f2 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Pedal_L", 0.0F, 18F * f2, 0.0F);
        this.mesh.chunkSetAngles("Pedal_R", 0.0F, 18F * f2, 0.0F);
        this.mesh.chunkSetAngles("TQHandle", 0.0F, 0.0F, 75F * this.interp(this.setNew.throttle, this.setOld.throttle, f));
        this.mesh.chunkSetAngles("MixLvr", 0.0F, 0.0F, 65F * this.interp(this.setNew.mix, this.setOld.mix, f));
        this.mesh.chunkSetAngles("CowlFlapLvr", 0.0F, -70F * this.interp(this.setNew.radiator, this.setOld.radiator, f), 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_Km", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 450F), 0.0F);
        float f4 = Pitot.Indicator((float) this.fm.Loc.z, super.fm.getSpeedKMH());
        if (f4 < 100F) {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f4, 0.0F, 100F, 0.0F, -28.4F), 0.0F);
        } else if (f4 < 200F) {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f4, 100F, 200F, -28.4F, -102F), 0.0F);
        } else if (f4 < 300F) {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f4, 200F, 300F, -102F, -191.5F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedSpeed", 0.0F, -this.cvt(f4, 300F, 450F, -191.5F, -326F), 0.0F);
        }
        this.mesh.chunkSetAngles("NeedFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 250F, 0.0F, 255.5F), 0.0F);
        f2 = this.fm.EI.engines[0].tOilOut;
        if (f2 < 20F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 0.0F, 20F, 0.0F, 15F), 0.0F);
        } else if (f2 < 40F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 20F, 40F, 15F, 50F), 0.0F);
        } else if (f2 < 60F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 40F, 60F, 50F, 102.5F), 0.0F);
        } else if (f2 < 80F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 60F, 80F, 102.5F, 186F), 0.0F);
        } else if (f2 < 100F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 80F, 100F, 186F, 283F), 0.0F);
        } else if (f2 < 120F) {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 100F, 120F, 283F, 314F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedOilTemp", 0.0F, this.cvt(f2, 120F, 140F, 314F, 345F), 0.0F);
        }
        f2 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("NeedRPM", 0.0F, this.cvt(f2, 0.0F, 3000F, 0.0F, 310F), 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - 0.5F;
        } else if (f2 < this.rpmGeneratedPressure) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - ((this.rpmGeneratedPressure - f2) * 0.01F);
        } else {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure + ((f2 - this.rpmGeneratedPressure) * 0.001F);
        }
        if (this.rpmGeneratedPressure < 600F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 0.0F, 600F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure < 900F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 600F, 900F, 4F, 7F);
        } else {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 900F, 1200F, 7F, 10F);
        }
        float f5 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f5 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 120F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f5 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 1.5F, 0.9F);
        } else {
            f5 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f6 = f5 * this.fm.EI.engines[0].getReadyness() * this.oilPressure;
        if (f6 < 12F) {
            this.mesh.chunkSetAngles("NeedOilPress", 0.0F, this.cvt(f6, 0.0F, 12F, 0.0F, 230F), 0.0F);
        } else if (f6 < 16F) {
            this.mesh.chunkSetAngles("NeedOilPress", 0.0F, this.cvt(f6, 12F, 16F, 230F, 285F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedOilPress", 0.0F, this.cvt(f6, 16F, 32F, 285F, 320F), 0.0F);
        }
        f2 = this.fm.EI.engines[0].tWaterOut;
        if (f2 < 20F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f2, 0.0F, 20F, 0.0F, 15F), 0.0F);
        } else if (f2 < 40F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f2, 20F, 40F, 15F, 50F), 0.0F);
        } else if (f2 < 60F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f2, 40F, 60F, 50F, 109F), 0.0F);
        } else if (f2 < 80F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f2, 60F, 80F, 109F, 192F), 0.0F);
        } else if (f2 < 100F) {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f2, 80F, 100F, 192F, 294F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedWatTemp", 0.0F, this.cvt(f2, 100F, 111.5F, 294F, 356.1F), 0.0F);
        }
        this.mesh.chunkSetAngles("NeedFuelPress", 0.0F, this.cvt(this.rpmGeneratedPressure, 0.0F, 1200F, 0.0F, 260F), 0.0F);
        this.mesh.chunkSetAngles("NeedBank", 0.0F, this.cvt(this.setNew.turn, -0.2F, 0.2F, 22.5F, -22.5F), 0.0F);
        this.mesh.chunkSetAngles("NeedTurn", 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 9.5F, -9.5F), 0.0F);
        this.mesh.chunkSetAngles("NeedClimb", 0.0F, -this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F);
        this.mesh.chunkSetAngles("TrimIndicator", 120F * -this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ElevTrim", 0.0F, 600F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("IgnitionSwitch", 0.0F, 90F * (1 + this.fm.EI.engines[0].getControlMagnetos()), 0.0F);
        this.mesh.chunkSetAngles("Trigger1", 10F * (this.fm.CT.saveWeaponControl[0] ? 1.0F : 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Trigger2", 10F * (this.fm.CT.saveWeaponControl[3] ? 1.0F : 0.0F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("DamageGlass1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Gages1_d0", false);
            this.mesh.chunkVisible("Gages1_d1", true);
            this.mesh.chunkVisible("NeedOilTemp", false);
            this.mesh.chunkVisible("NeedSpeed", false);
            this.mesh.chunkVisible("NeedWatTemp", false);
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gages2_d0", false);
            this.mesh.chunkVisible("Gages2_d1", true);
            this.mesh.chunkVisible("NeedClimb", false);
            this.mesh.chunkVisible("NeedBank", false);
            this.mesh.chunkVisible("NeedTurn", false);
            this.mesh.chunkVisible("DamageHull1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("DamageHull2", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("DamageHull3", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("DamageGlass2", true);
            this.mesh.chunkVisible("DamageHull4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("OilSplats", true);
        }
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
    }

    public void doToggleUp(boolean flag) {
        super.doToggleUp(flag);
    }

    public void destroy() {
        super.destroy();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D2o"));
        this.mesh.materialReplace("Matt2D2o", mat);
    }

    public void toggleLight() {
        super.cockpitLightControl = !super.cockpitLightControl;
        if (super.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HierMesh hiermesh = this.aircraft().hierMesh();
            hiermesh.chunkVisible("Head1_D0", false);
            hiermesh.chunkVisible("Blister_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        hiermesh.chunkVisible("Head1_D0", !this.aircraft().FM.AS.isPilotDead(0));
        hiermesh.chunkVisible("Blister_D0", true);
        super.doFocusLeave();
    }

    private Gun           gun[]             = { null, null };
    private Variables     setOld;
    private Variables     setNew;
    private Variables     setTmp;
    private Vector3f      w;
    private boolean       bNeedSetUp;
    private float         pictAiler;
    private float         pictElev;
    private float         pictSupc;
    private float         pictManifold;
    private float         rpmGeneratedPressure;
    private float         oilPressure;
    private static double compassZ          = -0.2D;
    private double        segLen1;
    private double        segLen2;
    private double        compassLimit;
    private static double compassLimitAngle = 25D;
    private Vector3d      compassSpeed[];
    int                   compassFirst;
    private Vector3d      accel;
    private Vector3d      compassNorth;
    private Vector3d      compassSouth;
    private double        compassAcc;
    private double        compassSc;

    static {
        Property.set(CockpitHe51C.class, "normZN", 0.4F);
        Property.set(CockpitHe51C.class, "gsZN", 0.3F);
    }
}
