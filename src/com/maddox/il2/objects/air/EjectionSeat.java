package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Message;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Time;

public class EjectionSeat extends ActorHMesh {
    class Interpolater extends Interpolate {

        public boolean tick() {
            float f = Time.tickLenFs();
            EjectionSeat.this.v.z -= 9.81D * f * f;
            EjectionSeat.this.v.x *= 0.99D;
            EjectionSeat.this.v.y *= 0.99D;
            EjectionSeat.this.l.add(EjectionSeat.this.v);
            EjectionSeat.this.pos.setAbs(EjectionSeat.this.l);
            World.cur();
            double d = World.land().HQ_Air(EjectionSeat.this.l.getPoint().x, EjectionSeat.this.l.getPoint().y);
            if (EjectionSeat.this.l.getPoint().z < d) {
                MsgDestroy.Post(Time.current(), super.actor);
            }
            if (EjectionSeat.this.bPilotAttached && ((EjectionSeat.this.l.getPoint().z < d) || (Time.current() > (EjectionSeat.this.timeStart + 3000L)))) {
                if (!EjectionSeat.this.ownerAircraft.isNet() || EjectionSeat.this.ownerAircraft.isNetMaster()) {
                    Vector3d vector3d = new Vector3d(EjectionSeat.this.v);
                    vector3d.scale(1.0F / Time.tickLenFs());
                    if (Actor.isValid(EjectionSeat.this.ownerAircraft)) {
                        new Paratrooper(EjectionSeat.this.ownerAircraft, EjectionSeat.this.ownerAircraft.getArmy(), 0, EjectionSeat.this.l, vector3d);
                        EjectionSeat.this.doRemovePilot();
                        EjectionSeat.this.bPilotAttached = false;
                        if (!EjectionSeat.this.bExtension) {
                            EjectionSeat.this.ownerAircraft.FM.AS.astateBailoutStep = 12;
                            EventLog.onBailedOut(EjectionSeat.this.ownerAircraft, 0);
                            EjectionSeat.this.ownerAircraft.FM.AS.setPilotState(EjectionSeat.this.ownerAircraft, 0, 100, false);
                        }
                    }
                } else {
                    if (!EjectionSeat.this.bExtension) {
                        EjectionSeat.this.doRemovePilot();
                    }
                    EjectionSeat.this.bPilotAttached = false;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private void doRemovePilot() {
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        if (this.bIsSK1) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.ownerAircraft.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft) {
        this.bIsSK1 = false;
        this.bExtension = false;
        this.v = new Vector3d();
        this.l = new Loc();
        this.bPilotAttached = true;
        switch (i) {
            case ESEAT_HE162:
            default:
                this.setMesh("3DO/Plane/He-162-ESeat/hier.him");
                this.drawing(true);
                break;

            case ESEAT_DO335:
                this.setMesh("3DO/Plane/Do-335A-0-ESeat/hier.him");
                this.drawing(true);
                break;

            case ESEAT_AR234:
                this.setMesh("3DO/Plane/Ar-234-ESeat/hier.him");
                this.drawing(true);
                break;

            case ESEAT_MB:
                this.setMesh("3DO/Plane/MB-ESeat/hier.him");
                this.drawing(true);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case ESEAT_MB_LATE:
                this.setMesh("3DO/Plane/MB-ESeat/hier_late.him");
                this.drawing(true);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                break;

            case ESEAT_NA:
                this.setMesh("3DO/Plane/NA-ESeat/hier.him");
                this.drawing(true);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case ESEAT_KK1:
                this.setMesh("3DO/Plane/KK1-ESeat/hier.him");
                this.drawing(true);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case ESEAT_KK2:
                this.setMesh("3DO/Plane/KK2-ESeat/hier.him");
                this.drawing(true);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case ESEAT_MIG21:
                this.setMesh("3DO/Plane/MiG21-SK1-ESeat/hier.him");
                this.drawing(true);
                this.bIsSK1 = true;
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                break;

            case ESEAT_KM1:
                this.setMesh("3DO/Plane/KM1-ESeat/hier.him");
                this.drawing(true);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster1"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 2.0F);
                Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 1.0F);
                Eff3DActor.New(this, this.findHook("_Booster2"), null, 0.5F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 0.5F);
                break;

            case ESEAT_METEORF8:
                this.setMesh("3DO/Plane/MeteorF8-ESeat/hier.him");
                this.drawing(true);
                break;

            case ESEAT_TUNNAN:
                this.setMesh("3DO/Plane/Tunnan-ESeat/hier.him");
                this.drawing(true);
                break;
        }
        this.l.set(loc);
        this.v.set(vector3d);
        this.v.scale(Time.tickConstLenFs());
        super.pos.setAbs(this.l);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.ownerAircraft = aircraft;
        this.timeStart = Time.current();
    }

    public EjectionSeat(int i, Loc loc, Vector3d vector3d, Aircraft aircraft, boolean flag) {
        this(i, loc, vector3d, aircraft);
        this.bExtension = flag;
    }

    public EjectionSeat(String s, String as[], float af[], float af1[], Loc loc, Vector3d vector3d, Aircraft aircraft, boolean flag) {
        this.bIsSK1 = false;
        this.v = new Vector3d();
        this.l = new Loc();
        this.bPilotAttached = true;
        this.bExtension = flag;
        this.setMesh(s);
        this.drawing(true);
        if (as != null) {
            if (as.length != af.length) {
                throw new RuntimeException("ERROR: " + aircraft + " 's EjectionSeat is array size mismatching between effStrings[] and effSize[].");
            }
            if (as.length != af1.length) {
                throw new RuntimeException("ERROR: " + aircraft + " 's EjectionSeat is array size mismatching between effStrings[] and effProcessTime[].");
            }
            int i = 1;
            for (int j = 0; j < as.length; j++) {
                String s1 = "_Booster" + i;
                if (this.hierMesh().hookFind(s1) < 0) {
                    if (i == 1) {
                        throw new RuntimeException("ERROR: " + aircraft + " 's EjectionSeat doesn't have the Hook '_Booster1'.");
                    }
                    i = 1;
                } else {
                    Eff3DActor.New(this, this.findHook(s1), null, af[j], as[j], af1[j]);
                    i++;
                }
            }

        }
        this.l.set(loc);
        this.v.set(vector3d);
        this.v.scale(Time.tickConstLenFs());
        this.pos.setAbs(this.l);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.ownerAircraft = aircraft;
        this.timeStart = Time.current();
    }

    public static final int ESEAT_HE162    = 1;
    public static final int ESEAT_DO335    = 2;
    public static final int ESEAT_AR234    = 3;
    public static final int ESEAT_MB       = 4;
    public static final int ESEAT_MB_LATE  = 5;
    public static final int ESEAT_NA       = 6;
    public static final int ESEAT_KK1      = 7;
    public static final int ESEAT_KK2      = 8;
    public static final int ESEAT_MIG21    = 9;
    public static final int ESEAT_KM1      = 10;
    public static final int ESEAT_METEORF8 = 11;
    public static final int ESEAT_TUNNAN   = 12;

    private Vector3d        v;
    private Loc             l;
    private boolean         bPilotAttached;
    private Aircraft        ownerAircraft;
    private long            timeStart;
    private boolean         bIsSK1;
    private boolean         bExtension;

}
