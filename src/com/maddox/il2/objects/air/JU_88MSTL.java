package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.BombMistelJu88;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;

public class JU_88MSTL extends JU_88 implements TypeDockable, Mistel, MsgCollisionRequestListener {

    public JU_88MSTL() {
        this.bNeedSetup = true;
        this.dtime = -1L;
        this.queen_last = null;
        this.queen_time = 0L;
        this.target_ = null;
        this.queen_ = null;
        this.mistelQueen = null;
        this.patinAutopilotEngaged = false;
        this.pImpact = new Point3d();
        this.isExploded = false;
        this.wasAirborne = false;
        this.attachEnabled = true;
        this.targetActor = null;
        this.bombChargeArmed = false;
    }

//    public static void moveGear(HierMesh h, float f) {
////        Exception test = new Exception("JU_88MSTL static void moveGear(" + h.hashCode() + ", " + f + ")");
////        test.printStackTrace();
//        JU_88.moveGear(h, f);
//    }
//
//    protected void moveGear(float f) {
////        Exception test = new Exception("JU_88MSTL void moveGear(" + f + ")");
////        test.printStackTrace();
//        super.moveGear(f);
//    }
//
//    public void forceGear(float f) {
////        Exception test = new Exception("JU_88MSTL void forceGear(" + f + ")");
////        test.printStackTrace();
//        super.forceGear(f);
//    }
//
//    public static void forceGear(Class c, HierMesh h, float f) {
////        Exception test = new Exception("JU_88MSTL static void forceGear(" + c.getName() + ", " + h.hashCode() + ", " + f + ")");
////        test.printStackTrace();
//        JU_88.forceGear(c, h, f);
//    }
    
    public void moveElevator(float f) {
//        if (this.getQueen() instanceof BF_109) {
//            Exception test = new Exception("JU_88MSTL moveElevator(" + f + ")");
//            test.printStackTrace();
//        }
        super.moveElevator(f);
    }
    
    public Aircraft getDrone() {
        return this;
    }

    public void setDrone(Aircraft drone) {
    }
    
    public Aircraft getQueen() {
        if (!(this.mistelQueen instanceof Aircraft)) return null;
        return (Aircraft) this.mistelQueen;
    }
    
    public void setQueen(Aircraft queen) {
        this.mistelQueen = queen;
    }
    
    public void setIgnoreGroundTargetForMillis(long millis) {
    }

    public void setBombChargeArmed(boolean arm) {
//        Exception test = new Exception("JU_88MSTL setBombChargeArmed(" + arm + ")");
//        test.printStackTrace();
//        System.out.println("JU_88MSTL setBombChargeArmed(" + arm + ")");
        this.bombChargeArmed = arm;
    }
    
    public boolean isBombChargeArmed()  {
        return bombChargeArmed;
    }

    public void mistelExplosion() {
    }

    protected void finalize() {
//        System.out.println("JU_88MSTL finalize()");
        super.finalize();
        NetMistel.removeNetMistelFromList(this);
    }

    public void msgEndAction(Object obj, int i) {
//        System.out.println("JU_88MSTL msgEndAction(" + (obj==null?"null":""+obj.hashCode()) + "," + i + "), wasAirborne=" + this.wasAirborne + ", isExploded=" + this.isExploded + ", isBombChargeArmed()=" + this.isBombChargeArmed());
        super.msgEndAction(obj, i);
        if (!this.wasAirborne) return;
        if (!this.isBombChargeArmed()) return;
        switch (i) {
            case 2:
                if (!this.isExploded) {
                    this.isExploded = true;
//                    System.out.println("JU_88MSTL msgEndAction isNetMaster=" + this.isNetMaster());
                    if (this.isNetMaster()) {
                        boolean retVal = NetMistel.netSendExplosionToDroneMaster(this);
//                        System.out.println("JU_88MSTL msgEndAction netSendExplosionToDroneMaster=" + retVal);
                        if (retVal) break;
                    }
//                    System.out.println("JU_88MSTL msgEndAction mistelQueen=" + (this.mistelQueen==null?"null":""+this.mistelQueen.getClass().getName()));
                    if (this.mistelQueen != null && this.mistelQueen instanceof Mistel) {
                        ((Mistel) this.mistelQueen).mistelExplosion();
                        break;
                    }
//                    System.out.println("JU_88MSTL msgEndAction no remote detonator available, spawning BombFAB5000!");
                    BombMistelJu88 bomb = new BombMistelJu88();
                    bomb.pos.setUpdateEnable(true);
                    bomb.pos.setAbs(this.pos.getAbs());
                    bomb.pos.reset();
                    bomb.start();
                    bomb.setOwner(this);
                    bomb.setSpeed(new Vector3d());
                    bomb.armingTime = 0L;
                    Reflection.setBoolean(bomb, "isArmed", true);
                }
                break;
        }
    }

    protected void doExplosion() {
//        System.out.println("JU_88MSTL doExplosion(), wasAirborne=" + this.wasAirborne);
//        if (!this.wasAirborne) {
        if (!this.isBombChargeArmed()) {
            if (World.land().isWater(this.FM.Loc.x, this.FM.Loc.y))
            {
                Explosions.AirDrop_Water(this.FM.Loc);
                return;
            }
            else
            {
                Loc loc = new Loc(this.FM.Loc);
                loc.getPoint().z = World.land().HQ(this.FM.Loc.x, this.FM.Loc.y);
                Eff3DActor.New(loc, 1.0F, "EFFECTS/Smokes/SmokeBoiling.eff", 1200.0F);
                Eff3DActor.New(loc, 1.0F, "3DO/Effects/Aircraft/FireGND.eff", 1200.0F);
                Eff3DActor.New(loc, 1.0F, "3DO/Effects/Aircraft/BlackHeavyGND.eff", 1200.0F);
                return;
            }
        }
        super.doExplosion();
        if (this.FM.Loc.z - 300D < World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y)) if (Engine.land().isWater(this.FM.Loc.x, this.FM.Loc.y)) Explosions.bomb1000_water(this.FM.Loc, 1.0F, 1.0F);
        else Explosions.bomb1000_land(this.FM.Loc, 1.0F, 1.0F, true);
    }

    public void msgShot(Shot shot) {
//        System.out.println("JU_88MSTL msgShot(" + (shot==null?"null":""+shot.hashCode()) + ")");
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 3, 1);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 2, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 1, 1);
//        MistelTools.checkDetach(this, shot);
        super.msgShot(shot);
    }

    private void checkAsDrone() {
        if (this.target_ == null) {
            if (this.FM.AP.way.curr().getTarget() == null) {
                this.FM.AP.way.next();
            }
            this.target_ = this.FM.AP.way.curr().getTarget();
            if (Actor.isValid(this.target_) && (this.target_ instanceof Wing)) {
                Wing wing = (Wing) this.target_;
                int i = this.aircIndex();
                    if (Actor.isValid(wing.airc[i])) {
                        this.target_ = wing.airc[i];
                    } else {
                        this.target_ = null;
                    }
            }
        }
        if (Actor.isValid(this.target_) && (this.target_ instanceof BF_109F4MSTL || this.target_ instanceof FW_190A8MSTL)) {
            this.queen_last = this.target_;
            this.queen_time = Time.current();
            if (this.isNetMaster()) {
                ((TypeDockable) this.target_).typeDockableRequestAttach(this, 0, true);
            }
        }
        this.bNeedSetup = false;
        this.target_ = null;
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(this.queen_);
    }

    public Actor typeDockableGetQueen() {
        return this.queen_;
    }

    public void typeDockableAttemptAttach() {
    }

    public void typeDockableAttemptDetach() {
        if (this.FM.AS.isMaster() && this.typeDockableIsDocked() && Actor.isValid(this.queen_)) ((TypeDockable) this.queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor) {
//        System.out.println("JU_88MSTL typeDockableRequestAttach(" + actor.name() + ")");
        if (!(actor instanceof TypeDockable)) return;
        if (!attachEnabled) return;
        if (Actor.isValid(this.queen_)) return;
        if (actor.pos.getAbsPoint().distance(this.pos.getAbsPoint()) >= ATTACH_MAX_DISTANCE) return;
//        System.out.println("JU_88MSTL typeDockableRequestAttach(" + actor.name() + ") this.FM.AS.isMaster()=" + this.FM.AS.isMaster() + ", actor.FM.AS.isMaster()=" + ((Aircraft)actor).FM.AS.isMaster());
        if (((Aircraft)actor).FM.AS.isMaster()) {
            //this.typeDockableRequestAttach(actor, 0, true);
            ((TypeDockable)actor).typeDockableRequestAttach(this, 0, true);
           return;
        } else {
            ((Aircraft)actor).FM.AS.netToMaster(32, 0, 0, this);
            return;
        }
    }

    public void typeDockableRequestDetach(Actor actor) {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
    }

    public void typeDockableDoDetachFromDrone(int i) {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
//        System.out.println("JU_88MSTL typeDockableDoAttachToQueen(" + (actor==null?"null":actor.getClass().getName()) + ", " + i + "), queen_=" + (queen_==null?"null":queen_.getClass().getName()));
        if (i != 0) return;
        if (!Actor.isValid(this.queen_)) {
            HookNamed hooknamed = new HookNamed(this, "_Dockport0");
            Loc loc = new Loc();
            Loc loc1 = new Loc();
            this.pos.getAbs(loc1);
            hooknamed.computePos(this, loc1, loc);
            actor.pos.setAbs(loc);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
//            if (this.FM.EI.engines[0].getStage() == 0) this.FM.EI.engines[0].toggle();
//            if (this.FM.EI.engines[1].getStage() == 0) this.FM.EI.engines[1].toggle();
            if (this.FM.CT.GearControl < 0.5F) {
//                System.out.println("JU_88MSTL typeDockableDoAttachToQueen airborne!");
                if(this.FM.EI.engines[0].getStage() < Motor._E_STAGE_NOMINAL) this.FM.AS.setEngineRunning(0);
                if(this.FM.EI.engines[1].getStage() < Motor._E_STAGE_NOMINAL) this.FM.AS.setEngineRunning(1);
            }
            this.attachEnabled = false;
            this.queen_ = actor;
            this.mistelQueen = actor;
//            this.dockport_ = i;
            this.queen_last = this.queen_;
            this.queen_time = 0L;
            
            // TODO: Test by SAS~Storebror
//            for (int engineIndex=0; engineIndex<2; engineIndex++) {
//                this.FM.EI.engines[engineIndex].horsePowers = 3000F;
//                this.FM.EI.engines[engineIndex].propI = 0.1F;
//                this.FM.EI.engines[engineIndex].engineI = 0.1F;
//                
//                this.FM.EI.engines[engineIndex].engineMomentMax = (this.FM.EI.engines[engineIndex].horsePowers * 746.0F * 1.2F / this.FM.EI.engines[engineIndex].wMax);
//            }
//            if (this.FM.EI.getNum() == 2) {
//                this.FM.Scheme = 6;
//                Aircraft aircraft = (Aircraft) actor;
//                this.FM.EI.setNum(3);
//                Motor m0 = this.FM.EI.engines[0];
//                Motor m1 = this.FM.EI.engines[1];
//                this.FM.EI.engines = new Motor[3];
//                this.FM.EI.engines[0] = m0;
//                this.FM.EI.engines[1] = m1;
//                this.FM.EI.engines[2] = aircraft.FM.EI.engines[0];
//                this.FM.EI.bCurControl = new boolean[] { true, true, true };
//            }
//            this.FM.EI.engines[0].setEngineStarts(this);
//            this.FM.EI.engines[1].setEngineStarts(this);
//            this.FM.EI.setEngineRunning();
            if(aircIndex() == 0 && (FM instanceof Maneuver) && (((Aircraft)queen_).FM instanceof Maneuver))
            {
                Maneuver manQueen = (Maneuver)((Aircraft)queen_).FM;
                Maneuver manSelf = (Maneuver)FM;
                if(manQueen.Group != null && manQueen.Group.w != null && manSelf.Group != null && manSelf.Group.numInGroup(this) == manSelf.Group.nOfAirc - 1)
                {
                    AirGroup airgroup = new AirGroup(manSelf.Group);
                    manSelf.Group.delAircraft(this);
                    airgroup.addAircraft(this);
                    airgroup.attachGroup(manQueen.Group);
                    airgroup.rejoinGroup = null;
                    airgroup.leaderGroup = null;
                    airgroup.clientGroup = manQueen.Group;
                }
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i) {
        if (i != 0) return;
        if (!Actor.isValid(this.queen_)) return;
//        Exception test = new Exception("JU_88MSTL typeDockableDoDetachFromQueen(" + i +")");
//        test.printStackTrace();
        this.queen_ = null;
//        if (this.FM.EI.getNum() == 3) {
//            this.FM.Scheme = 2;
//            this.FM.EI.setNum(2);
//            Motor m0 = this.FM.EI.engines[0];
//            Motor m1 = this.FM.EI.engines[1];
//            this.FM.EI.engines = new Motor[2];
//            this.FM.EI.engines[0] = m0;
//            this.FM.EI.engines[1] = m1;
//            this.FM.EI.bCurControl = new boolean[] { true, true };
//        }
        if (!this.FM.Gears.onGround() && this.FM.getSpeedKMH() > 100F) this.engagePatinAutopilot();
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (this.typeDockableIsDocked()) {
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_IS_ATTACHED);
            ActorNet actornet = this.queen_.net;
            actornet = this.queen_.net;
//            System.out.println("JU_88MSTL typeDockableReplicateToNet docked, actornet=" + (actornet == null?"null":((Actor)actornet.superObj()).name() + ", countNoMirrors()=" + actornet.countNoMirrors() + ", countMirrors()=" + actornet.countMirrors()));
//            if (actornet.countNoMirrors() > 0) actornet = null;
//            if (actornet.countMirrors() < 1) actornet = null;
            netmsgguaranted.writeNetObj(actornet);
            netmsgguaranted.writeFloat(this.FM.CT.getGear());
            netmsgguaranted.writeBoolean(this.isBombChargeArmed());
        } else if (Actor.isValid(this.getQueen())) {
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_IS_DETACHED);
            ActorNet actornet = this.getQueen().net;
//            System.out.println("JU_88MSTL typeDockableReplicateToNet undocked, actornet=" + (actornet == null?"null":((Actor)actornet.superObj()).name() + ", countNoMirrors()=" + actornet.countNoMirrors() + ", countMirrors()=" + actornet.countMirrors()));
//            if (actornet.countNoMirrors() > 0) actornet = null;
//            if (actornet.countMirrors() < 1) actornet = null;
            netmsgguaranted.writeNetObj(actornet);
            netmsgguaranted.writeBoolean(this.isBombChargeArmed());
        } else {
//            System.out.println("JU_88MSTL typeDockableReplicateToNet no drone");
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_NO_QUEEN);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        byte dockStatus = netmsginput.readByte();
        NetObj netobj;
        switch (dockStatus) {
            case NetMistel.NETMSG_MISTEL_IS_ATTACHED:
                netobj = netmsginput.readNetObj();
//                this.setBombChargeArmed(netmsginput.readBoolean());
                System.out.println("JU_88MSTL typeDockableReplicateFromNet docked, netobj=" + (netobj == null?"null":((Actor)netobj.superObj()).name()));
                if (netobj != null) {
                    ((TypeDockable)netobj.superObj()).typeDockableDoAttachToDrone(this, 0);
                }
                float f = netmsginput.readFloat();
//              System.out.println("JU_88MSTL " + this.name() + " typeDockableReplicateFromNet forceGear(" + f + ")");
                this.forceGear(f);
                Reflection.setFloat(this, "Gear_", f);
                break;
            case NetMistel.NETMSG_MISTEL_IS_DETACHED:
                netobj = netmsginput.readNetObj();
                this.setBombChargeArmed(netmsginput.readBoolean());
//                System.out.println("JU_88MSTL typeDockableReplicateFromNet undocked, netobj=" + (netobj == null?"null":((Actor)netobj.superObj()).name()));
                if (netobj != null) {
                    this.queen_ = null;
                    this.mistelQueen = (Actor) netobj.superObj();
                    if (this.mistelQueen instanceof Mistel) {
                        Mistel mistel = (Mistel)this.mistelQueen;
                        mistel.setDrone(this);
                        mistel.setBombChargeArmed(this.isBombChargeArmed());
                    }
                }
                break;
            case NetMistel.NETMSG_MISTEL_NO_QUEEN:
            default:
//                System.out.println("JU_88MSTL typeDockableReplicateFromNet undocked");
                break;
        }
//        if (netmsginput.readByte() == 1) {
////            System.out.println("JU_88MSTL typeDockableReplicateFromNet docked 1");
////            this.dockport_ = netmsginput.readByte();
//            NetObj netobj = netmsginput.readNetObj();
//            if (netobj != null) {
////                System.out.println("JU_88MSTL typeDockableReplicateFromNet docked 2");
//                Actor actor = (Actor) netobj.superObj();
//                ((TypeDockable) actor).typeDockableDoAttachToDrone(this, 0);
//            }
//            float f = netmsginput.readFloat();
////            System.out.println("JU_88MSTL " + this.name() + " typeDockableReplicateFromNet forceGear(" + f + ")");
//            this.forceGear(f);
//            Reflection.setFloat(this, "Gear_", f);
//        } else {
////            System.out.println("JU_88MSTL typeDockableReplicateFromNet undocked");
//        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (this.FM.AS.isMaster()) {
            if (i == 2) this.typeDockableRequestDetach(this.queen_, 0, true);
            if (i == 13 && j == 0) {
                this.nextDMGLevels(4, 1, "CF_D0", this);
                return true;
            }
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (!this.wasAirborne) {
            if (this.FM.Loc.z - Engine.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 40.0) {
                this.wasAirborne = true;
                this.setBombChargeArmed(true);
            }
        }
        if (this.FM instanceof Pilot) ((Pilot) this.FM).setDumbTime(9999L);
//        if (this.FM instanceof Maneuver && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6) {
//            ((Maneuver) this.FM).set_maneuver(44);
//            ((Maneuver) this.FM).setSpeedMode(-1);
//        }
//        this.FM.CT.bHasGearControl = !this.FM.Gears.onGround();
//        if (this.FM.Gears.onGround() || this.FM.Gears.nearGround()) {
//        if (this.FM.Gears.onGround() || ((this.FM.Gears.nearGround() || (this.FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y) < 50F)) && this.FM.CT.getGear() > 0.5F)) {
        boolean queenIsPlayer = false;
        if (this.queen_ instanceof Aircraft) {
            Aircraft qa = (Aircraft)this.queen_;
            queenIsPlayer = qa.FM instanceof RealFlightModel && ((RealFlightModel)qa.FM).isRealMode();
        }

        this.FM.CT.bHasGearControl = !(this.FM.Gears.onGround() || (!queenIsPlayer && this.FM.Gears.nearGround() && this.FM.CT.getGear() > 0.5F));
        if (!this.FM.CT.bHasGearControl) {
            this.FM.CT.GearControl = 1F;
            this.FM.CT.CTL |= 0x01;
        }

        if (this.patinAutopilotEngaged && this.FM.AS.isMaster()) {
            if (Actor.isValid(this.targetActor)) {
                // As long as there's a valid target actor, keep following it's coords.
                float x = (float)this.targetActor.pos.getAbsPoint().x;
                float y = (float)this.targetActor.pos.getAbsPoint().y;
                float z = Landscape.HQ(x, y);
                this.pImpact.set(x, y, z);
            }
            Point3d p1 = new Point3d(this.pImpact);
            Point3d p2 = new Point3d();
            this.pos.getAbs(p2);
            Vector3d v1 = new Vector3d();
            v1.sub(p1, p2);
            v1.normalize();
            Orient o1 = new Orient();
            o1.setAT0(v1);
            Vector3d v2 = new Vector3d(this.FM.Vwld);
            v2.normalize();
            Orient o2 = new Orient();
            o2.setAT0(v2);
            o1.sub(o2);
            float krenTarget = this.FM.Or.getKren() + o1.getYaw() * 2;
            if (krenTarget > 60F) krenTarget = 60F;
            if (krenTarget < -60F) krenTarget = -60F;
            float aileronTarget = this.FM.Or.getKren() + krenTarget;
            float elevatorTarget = o1.getTangage();
            if (Math.abs(krenTarget) > 30F && Math.abs(this.FM.Or.getKren()) > 30F) elevatorTarget = (Math.abs(krenTarget) - 30F) * 0.2F;
            this.FM.CT.AileronControl = -0.2F * aileronTarget - 2.0F * (float) this.FM.getW().x;
            this.FM.CT.ElevatorControl = elevatorTarget * 0.2F;
            this.FM.CT.RudderControl = -o1.getYaw() * 0.2F;
            this.FM.EI.engines[0].setControlThrottle(1.1F);
            this.FM.EI.engines[1].setControlThrottle(1.1F);
        }

        if (this.isNetMaster() && Actor.isValid(this.queen_) && this.queen_ instanceof Aircraft) {
            this.FM.CT.setPowerControl(((Aircraft) this.queen_).FM.CT.getPowerControl());
            this.FM.CT.BrakeControl = ((Aircraft) this.queen_).FM.CT.BrakeControl;
            this.FM.CT.BrakeLeftControl = ((Aircraft) this.queen_).FM.CT.BrakeLeftControl;
            this.FM.CT.BrakeRightControl = ((Aircraft) this.queen_).FM.CT.BrakeRightControl;
            this.FM.CT.bHasLockGearControl = true;
            this.FM.Gears.bTailwheelLocked = ((Aircraft) this.queen_).FM.Gears.bTailwheelLocked;
            this.FM.brakeShoe = ((Aircraft) this.queen_).FM.brakeShoe;
            this.FM.AS.setNavLightsState(((Aircraft) this.queen_).FM.AS.bNavLightsOn);
            this.FM.AS.setAirShowState(((Aircraft) this.queen_).FM.AS.bShowSmokesOn);
        }

        if (this.typeDockableIsDocked()) {
            if (this.FM.EI.engines[0].getStage() == 0) this.FM.EI.engines[0].setEngineStarts(this);
            if (this.FM.EI.engines[1].getStage() == 0) this.FM.EI.engines[1].setEngineStarts(this);
        } else {
            if (this.FM.Gears.onGround() && this.FM.EI.engines[0].getStage() > 0) this.FM.EI.engines[0].setEngineStops(this);
            if (this.FM.Gears.onGround() && this.FM.EI.engines[1].getStage() > 0) this.FM.EI.engines[1].setEngineStops(this);
            if (!Actor.isAlive(this.queen_) && !Actor.isAlive(this.mistelQueen)) {
                this.attachEnabled = true;
            }
        }
        
        NetMistel.mistelToMirrors(this);
        super.update(f);
        if (this.bNeedSetup) this.checkAsDrone();
        if (!this.FM.AS.isMaster()) return;
        if (this.FM instanceof Maneuver) {
            if (this.typeDockableIsDocked()) {
                if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                    ((Maneuver) this.FM).set_maneuver(48);
                    ((FlightModelMain) this.FM).AP.way.setCur(((Aircraft) this.queen_).FM.AP.way.Cur());
                    ((Pilot) this.FM).setDumbTime(3000L);
                }
            } else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
                if (this.dtime > 0L) {
                    ((Maneuver) this.FM).set_maneuver(22);
                    ((Pilot) this.FM).setDumbTime(3000L);
                    if (Time.current() > this.dtime + 3000L) {
                        this.dtime = -1L;
                        ((Maneuver) this.FM).clear_stack();
                        ((Maneuver) this.FM).pop();
                        ((Pilot) this.FM).setDumbTime(0L);
                    }
                }
            }
        }
    }

    private void engagePatinAutopilot() {
//        System.out.println("JU_88MSTL engagePatinAutopilot()");
        if (!this.FM.AS.isMaster()) return;
        Aircraft queenAircraft;
        if (this.queen_last instanceof Aircraft) {
            queenAircraft = (Aircraft)queen_last;
            if (!(queenAircraft.FM instanceof RealFlightModel) || !((RealFlightModel) queenAircraft.FM).isRealMode()) {
                if (this.engagePatinAutopilotAI(queenAircraft)) return;
            }
        }
//        System.out.println("engagePatinAutopilot() Player controlled");
        Vector3d v1 = new Vector3d();
        v1.set(1.0D, 0.0D, 0.0D);
        this.FM.Or.transform(v1);
        v1.scale(10000D);
        Point3d p2 = new Point3d();
        p2.set(this.FM.Loc);
        p2.add(v1);
        Point3d p1 = new Point3d();
        p1.set(this.FM.Loc);
        if (Landscape.rayHitHQ(this.FM.Loc, p2, p1)) {
            this.pImpact.set(p1);
        } else {
            v1.set(1.0D, 0.0D, 0.0D);
            this.FM.Or.transform(v1);
            v1.scale(this.FM.getAltitude() - Landscape.HQ((float)this.FM.Loc.x, (float)this.FM.Loc.y));
            p1.set(this.FM.Loc);
            p1.add(v1);
            p1.z = Landscape.HQ((float)p1.x, (float)p1.y);
            this.pImpact.set(p1);
        }
        this.FM.CT.setPowerControl(1.1F);
        this.patinAutopilotEngaged = true;
    }
    
    
    /**
     * Generate an array of Target Waypoints for the current Mistel drop.
     * When Mistels get dropped (on last waypoint before a ground attack waypoint),
     * all subsequent consecutive ground attack waypoints on the carrying "queen" aircraft's
     * way are valid targets for the corresponding Mistel(s) of that queen's flight.
     * 
     * @param queen - The Aircraft carrying this Mistel
     * @return - An Array holding all waypoints that qualify for being a target for
     *           The Mistels of this flight
     */
    private WayPoint[] targetWaypoints(Aircraft queen) {
        ArrayList twp = new ArrayList();     // ArrayList of potential target waypoints
        int wpIndex = queen.FM.AP.way.Cur(); // The index of our current waypoint, zero-based
        
        // Initially we need to walk back through the queen's flight path in order to find
        // the first "ground attack" waypoint following the drop point.
        // This is required for the delayed "drop" pattern as the flight leader will proceed
        // to the first non-ground-attack waypoint after the drop, meaning that every Mistel
        // being dropped _after_ the leader's one will drop while their queen is already
        // _past_ the ground attack waypoints on their flight path.
        WayPoint wp = queen.FM.AP.way.get(wpIndex);
        if (wp != null && wp.Action != WayPoint.GATTACK) {
            boolean inAttackWp = false;
            while(true) {
                wp = queen.FM.AP.way.get(wpIndex);
                if (wp == null) break;
                if (wp.Action == WayPoint.GATTACK) {
                    inAttackWp = true;
                } else if (inAttackWp) {
                    wpIndex++;
                    break;
                }
                if (wpIndex < 1) break;
                wpIndex--;
            }
        }
        
        if (wpIndex > 0) {
            wp = queen.FM.AP.way.get(wpIndex - 1);
//            System.out.println("WP " + (wpIndex - 1) + " Distance= " + wp.getP().distance(this.pos.getAbsPoint())
//                             + ", cur Speed= " + this.FM.getSpeed() + ", flight time= " + (wp.getP().distance(this.pos.getAbsPoint()) / this.FM.getSpeed()));
            // Skip Target Waypoint if flight time exceeds 60 seconds
            if (wp.getP().distance(this.pos.getAbsPoint()) / this.FM.getSpeed() > 60D) return (WayPoint[])twp.toArray(new WayPoint[0]);
        }
        
//        System.out.println("1st GATTACK WP Index: " + wpIndex);

        // wpIndex _should_ now point to the first ground attack waypoint
        // following our drop point.
        // Time to build the target list.
        while(true) {
            wp = queen.FM.AP.way.get(wpIndex++);
            if (wp == null || wp.Action != WayPoint.GATTACK) break; // not a ground attack waypoint anymore, list is finished.
            twp.add(wp); // Add this waypoint to the list.
        }
        
//        System.out.println("Target WPs: " + twp.size());

        // Convert the ArrayList holding our target waypoints to a corresponding
        // Array of Waypoints.
        return (WayPoint[])twp.toArray(new WayPoint[0]);
    }
    
    /**
     * Shuffle the Waypoints created by the "targetWaypoints" method.
     * This is needed if there are more ground attack waypoints than Mistels to drop,
     * in which case we "randomly" select waypoints from the list as targets.
     * 
     * @param wp - Array of Waypoints to shuffle.
     */
    private void shuffleWaypoints(WayPoint[] wp) {
        // If our randomizer has not been seeded with mission startup random seed yet, it's time to do so now.
        if (randomMissionSeed == -1) randomMissionSeed = TrueRandom.nextInt();
        // Use the Wing's hash code to seed a random number generator.
        // This way we make sure that all aircraft in this wing use the same random numbering sequence.
        Random wingRandom = new Random((((long)this.getWing().hashCode()) << 32) | (randomMissionSeed & 0xffffffffL));
        List wpList = Arrays.asList(wp);
        // Shuffle the list of Target Waypoints according to the wing-seeded random number generator.
        // Result: Pseudo-Randomly shuffled Waypoint list, which is the same across all aircraft in wing.
        Collections.shuffle(wpList, wingRandom);
        // Copy the List elements back to our Waypoint Array.
        wpList.toArray(wp);
    }
    
    private boolean engagePatinAutopilotAI(Aircraft queen) {
        if (!(queen.FM instanceof Maneuver)) return false;
        WayPoint[] targetWaypoints = targetWaypoints(queen);
        if (targetWaypoints.length < 1) return false;
        AirGroup airgroup = ((Maneuver)queen.FM).Group;
        if (this.nOfAirc < targetWaypoints.length) {
            shuffleWaypoints(targetWaypoints);
        }
        int wayPointIndex = airgroup.numInGroup(queen) % targetWaypoints.length;
        float x=0,y=0;
        if (targetWaypoints[wayPointIndex].getTarget() != null) {
            // If the Target Waypoint has a Target Object set, try to attack that object
            this.targetActor = War.GetRandomFromChief(queen, targetWaypoints[wayPointIndex].getTarget());
            if (Actor.isValid(this.targetActor)) {
                // If the target actor is valid, use it's current coords as impact point and keep them updated
                x = (float)targetWaypoints[wayPointIndex].getTarget().pos.getAbsPoint().x;
                y = (float)targetWaypoints[wayPointIndex].getTarget().pos.getAbsPoint().y;
            }
        } 
        if (x==0 && y==0) {
            // If no target actor coords could be found, use the set target waypoint coords instead
            x = targetWaypoints[wayPointIndex].x();
            y = targetWaypoints[wayPointIndex].y();
            if (this.nOfAirc > targetWaypoints.length) {
                x += (float)TrueRandom.nextGaussian(50D);
                y += (float)TrueRandom.nextGaussian(50D);
            }
        }
        float z = Landscape.HQ(x, y);
        this.pImpact.set(x, y, z);
        this.FM.CT.setPowerControl(1.1F);
        this.patinAutopilotEngaged = true;
        // After detaching the Mistel from it's queen, make sure the queen doesn't start strafing the target in parallel
        ((Mistel)queen).setIgnoreGroundTargetForMillis(Time.current() + 10000L);
        ((Maneuver)queen.FM).target_ground = null;
//        System.out.println("engagePatinAutopilotAI pImpact=" + x + ":" + y + ":" + z);
        return true;
    }
    
    public boolean netGetGMsg(NetMsgInput netmsginput, boolean bool) throws IOException {
        if (!NetMistel.netGetGMsg(this, netmsginput, bool)) return super.netGetGMsg(netmsginput, bool);
        return true;
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        super.msgCollisionRequest(actor, aflag);
        if ((this.queen_last != null) && (this.queen_last == actor) && ((this.queen_time == 0L) || (Time.current() < (this.queen_time + 5000L)))) {
            aflag[0] = false;
        } else {
            aflag[0] = true;
        }
    }

    public void missionStarting() {
        randomMissionSeed = -1;
        this.checkAsDrone();
        if (!(this.FM instanceof Maneuver)) return;
        AirGroup airgroup = ((Maneuver)this.FM).Group;
        if (airgroup == null) return;
        this.nOfAirc = airgroup.nOfAirc;
    }

    public int typeDockableGetDockport() {
        if (this.typeDockableIsDocked()) return 0;
        else return -1;
    }

    public boolean isQueen() {
        return false;
    }

    public boolean isDrone() {
        return true;
    }
    
    private boolean        bNeedSetup;
    private long           dtime;
    private Actor          target_;
    private Actor          queen_;
//    private int            dockport_;
    private Actor          mistelQueen;
    private Actor          queen_last;
    private long           queen_time;
    private boolean        patinAutopilotEngaged;
    private boolean        isExploded;
    private Point3d        pImpact;
    private boolean        wasAirborne;
    private boolean        attachEnabled;
    private int            nOfAirc;
    private static final double ATTACH_MAX_DISTANCE = 50D;
    private Actor          targetActor;
    private static int     randomMissionSeed = -1;
    private boolean        bombChargeArmed;

    static {
        Class class1 = JU_88MSTL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88MSTL/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-4Mistel.fmd");
        Property.set(class1, "FormationScaleCoeff", 1.1F);
        Property.set(class1, "AutopilotElevatorAboveReferenceAltitudeFactor", 0.0F);
        weaponTriggersRegister(class1, new int[] { 9 });
        weaponHooksRegister(class1, new String[] { "_Dockport0" });
    }
}
