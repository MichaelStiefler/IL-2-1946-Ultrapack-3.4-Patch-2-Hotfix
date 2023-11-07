package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombMistelJu88;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;

public class FW_190A8MSTL extends FW_190NEW implements TypeDockable, Mistel {

    public FW_190A8MSTL() {
        this.droneInitiator = null;
        this.ignoreGroundTargetTime = -1L;
    }
    
    public void onAircraftLoaded() {
//        System.out.println("FW_190A8MSTL onAircraftLoaded()");
        if (this.FM.CT.Weapons[3] == null) this.FM.CT.Weapons[3] = new BulletEmitter[1];
        this.FM.CT.Weapons[3][0] = new BombGunNull();
        this.FM.CT.Weapons[3][0].set(this, "_Clip00");
        this.FM.CT.Weapons[3][0].loadBullets(0);
    }

    public boolean dropExternalStores(boolean flag) {
        return true;
    }

    public Aircraft getDrone() {
        if (!(this.droneInitiator instanceof Aircraft)) return null;
        return (Aircraft) this.droneInitiator;
    }

    public void setDrone(Aircraft drone) {
        this.droneInitiator = drone;
    }
    
    public Aircraft getQueen() {
        return this;
    }

    public void setQueen(Aircraft queen) {
    }

    public void setIgnoreGroundTargetForMillis(long millis) {
        this.ignoreGroundTargetTime = Time.current() + millis;
    }
    
    public void setBombChargeArmed(boolean arm) {
//        System.out.println("FW_190A8MSTL setBombChargeArmed(" + arm + ")");
        this.bombChargeArmed = arm;
    }
    
    public boolean isBombChargeArmed()  {
        return bombChargeArmed;
    }

    public void mistelExplosion() {
//      System.out.println("FW_190A8MSTL mistelExplosion()");
      if (this.getDrone() == null) {
//          System.out.println("FW_190A8MSTL mistelExplosion() getDrone()=null");
          return;
      }
      if (this.getDrone().pos.getAbsPoint().z - Engine.cur.land.HQ(this.getDrone().pos.getAbsPoint().x, this.getDrone().pos.getAbsPoint().y) > 50D) {
//          System.out.println("FW_190A8MSTL mistelExplosion() Altitude above land > 50m");
          MsgExplosion.send(this.getDrone(), null, this.getDrone().pos.getAbsPoint(), this, 0.0F, 6550.0F, 0, 890.0F);
          return;
      }
//      System.out.println("FW_190A8MSTL mistelExplosion() spawning BombFAB5000, isBombChargeArmed()=" + this.isBombChargeArmed());
      if (!this.isBombChargeArmed()) return;
      BombMistelJu88 bomb = new BombMistelJu88();
      bomb.pos.setUpdateEnable(true);
      bomb.pos.setAbs(this.getDrone().pos.getAbs());
      bomb.pos.reset();
      bomb.start();
      if (Actor.isValid(this)) bomb.setOwner(this);
      bomb.setSpeed(new Vector3d());
      bomb.armingTime = 0L;
      Reflection.setBoolean(bomb, "isArmed", true);
    }

    protected void finalize() {
//        System.out.println("FW_190A8MSTL finalize()");
        super.finalize();
        NetMistel.removeNetMistelFromList(this);
    }

    protected void moveGear(float f) {
//        System.out.println("FW_190A8MSTL moveGear(" + f + ")");
        if (this.typeDockableIsDocked()) moveGear(this.hierMesh(), 0.0F);
        else moveGear(this.hierMesh(), f);
    }

    public void update(float f) {
        super.update(f);
        Maneuver maneuver = this.FM instanceof Maneuver ? (Maneuver)this.FM : null;
        if (this.typeDockableIsDocked()) {
            if (this.FM.EI.engines[0].getStage() == 0) this.FM.EI.engines[0].setEngineStarts(this);
            Aircraft aircraft = (Aircraft) this.typeDockableGetDrone();
            NetMistel.mistelToMirrors(this);
            if (!aircraft.isNetMirror()) {
                aircraft.FM.CT.AileronControl = this.FM.CT.AileronControl;
                aircraft.FM.CT.ElevatorControl = this.FM.CT.ElevatorControl;
                aircraft.FM.CT.RudderControl = this.FM.CT.RudderControl;
                aircraft.FM.CT.setTrimAileronControl(this.FM.CT.getTrimAileronControl());
                aircraft.FM.CT.setTrimElevatorControl(this.FM.CT.getTrimElevatorControl());
                aircraft.FM.CT.setTrimRudderControl(this.FM.CT.getTrimRudderControl());
            }
            aircraft.FM.CT.GearControl = this.FM.CT.GearControl;
            aircraft.FM.CT.FlapsControl = this.FM.CT.FlapsControl;
            aircraft.FM.CT.forceFlaps(this.FM.CT.getFlap());
            this.moveSteering(0.0F);
            this.FM.canChangeBrakeShoe = aircraft.FM.Gears.onGround() && this.FM.getSpeedKMH() < 10F;
//            if (aircraft.FM.Gears.onGround()) this.FM.Loc.z = Engine.cur.land.HQ(this.FM.Loc.x, this.FM.Loc.y) + 51D;
        }
        if(FM.isPlayers() && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode()) {
            this.FM.CT.bDropWithPlayer = true;
        }
        if (Actor.isValid(this.drone) && this.drone instanceof Mistel && ((Mistel)this.drone).isBombChargeArmed() != this.isBombChargeArmed()) {
            this.setBombChargeArmed(((Mistel)this.drone).isBombChargeArmed());
        }
        
        if (this.FM.CT.saveWeaponControl[3] || this.FM.CT.WeaponControl[3]) {
            boolean dumpBombsPassively = false;
            if (maneuver != null) {
                dumpBombsPassively = maneuver.dumpBombsPassively;
            }
            if (dumpBombsPassively) {
                if (Actor.isValid(this.drone) && this.drone instanceof Mistel) {
                    ((Mistel)this.drone).setBombChargeArmed(false);
                    this.setBombChargeArmed(false);
                }
            }
            this.typeDockableAttemptDetach();
            if(!dumpBombsPassively && FM.isPlayers() && (FM instanceof RealFlightModel) && (FM instanceof Maneuver) && ((RealFlightModel)FM).isRealMode()) {
                if (maneuver.Group != null && maneuver.Group.airc != null && this == maneuver.Group.airc[0]) {
                    // Make AI fellow planes drop Mistels with the human leader
//                    System.out.println("DROP GROUP!!!");
                    ((Maneuver)FM).Group.dropBombs();
                }
            }
        }
        
        if (this == World.getPlayerAircraft() && maneuver != null) {
            if (this.ignoreGroundTargetTime >= 0L) {
                // Make sure that this queen does not strafe it's drone targets
                if (Time.current() < this.ignoreGroundTargetTime) {
                    maneuver.target_ground = null;
                    Reflection.setValue(maneuver, "gTargActor", null);
                    maneuver.Group.setGTargMode(0);
                    maneuver.Group.setGroupTask(1);
                    maneuver.Group.setTaskAndManeuver(0);
                } else {
                    this.ignoreGroundTargetTime = -1L;
                }
            }
        }
        this.updateAI();
    }
    
    private void updateAI() {
        if(FM.isPlayers() && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
            return;
        if(!(FM instanceof Maneuver))
            return;
        Maneuver maneuver = (Maneuver)FM;
        maneuver.bKeepOrdnance = this.FM.isCapableOfBMP() && !this.FM.isTakenMortalDamage();
        if (!maneuver.bKeepOrdnance && this.typeDockableIsDocked()) {
//            System.out.println("FW_190A8MSTL updateAI() " + this.name() + " disarm Drone Charge since we're dumping the drone!");
            if (this.drone instanceof Mistel) ((Mistel)this.drone).setBombChargeArmed(false);
            this.setBombChargeArmed(false);
            maneuver.dumpBombsPassively = true;
            this.typeDockableAttemptDetach();
            if (maneuver.Group != null && maneuver.Group.airc != null && this == maneuver.Group.airc[0] && maneuver.Group.airc.length > 1) {
//                System.out.println("FW_190A8MSTL updateAI() " + this.name() + " change Mistel pos in Group since it's dumping the drone and it's the group leader!");
//                maneuver.Group.changeAircraft(this, maneuver.Group.airc[maneuver.Group.nOfAirc - 1]);
//                maneuver.Group.swapAircraft(0, maneuver.Group.nOfAirc - 1);
                maneuver.Group.delAircraft(this);
            }
        }
        if (this.undockTime > 0L && Time.current() > this.undockTime) {
            this.undockTime = -1L;
            this.typeDockableRequestDetach(this.drone);
//            System.out.println("updateAI() UNDOCK!!!");
            while (maneuver.AP.way.curr().Action == WayPoint.GATTACK) maneuver.AP.way.next();
            maneuver.setCheckStrike(false);
        }
//        if (maneuver.Group.numInGroup(this) == 0) {
//            HUD.training("M:" + maneuver.get_maneuver() + ", T:" + maneuver.get_task() + ", AP alt=" + Reflection.getFloat(this.FM.AP, "SA") + ", bWP=" + Reflection.getBoolean(this.FM.AP, "bWayPoint"));
//        }
//        if (maneuver.Group != null && maneuver.Group.numInGroup(this) == 1) {
//            System.out.println("BO:" + maneuver.dumpBombsPassively + ", F:" + this.FM.EI.producedF.x + ", M:" + this.FM.M.getFullMass() + ", Sq:" + this.FM.Sq.squareWing + ", D:" + this.FM.Sq.dragProducedCx);
//        }
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(this.drone);
    }

    public Actor typeDockableGetDrone() {
        return this.drone;
    }

    public void typeDockableAttemptAttach() {
//        System.out.println("FW_190A8MSTL typeDockableAttemptAttach()");
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
//            System.out.println("FW_190A8MSTL typeDockableAttemptAttach() aircraft=" + aircraft.name());
            if (aircraft instanceof JU_88MSTL) ((TypeDockable) aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach() {
        if (Actor.isValid(this.drone) && this.drone instanceof Aircraft && ((Aircraft)this.drone).FM.Gears.onGround()) return;
        if (this.FM.AS.isMaster())
            if (Actor.isValid(this.drone)) this.typeDockableRequestDetach(this.drone, 0, true);
    }

    public void typeDockableRequestAttach(Actor actor) {
    }

    public void typeDockableRequestDetach(Actor actor) {
        if (actor == null || actor != this.drone) return;
        Aircraft aircraft = (Aircraft) actor;
        if (Actor.isValid(aircraft) && aircraft.FM.Gears.onGround()) return;
        if (!aircraft.FM.AS.isMaster()) return;
        if (this.FM.AS.isMaster()) this.typeDockableRequestDetach(actor, 0, true);
        else this.FM.AS.netToMaster(33, 0, 1, actor);
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
        if (i != 0) return;
        if (flag) {
            if (this.FM.AS.isMaster()) {
                this.FM.AS.netToMirrors(34, 0, 1, actor);
                this.typeDockableDoAttachToDrone(actor, 0);
            } else this.FM.AS.netToMaster(34, 0, 1, actor);
        } else if (this.FM.AS.isMaster()) {
            if (!Actor.isValid(this.drone)) {
                this.FM.AS.netToMirrors(34, 0, 1, actor);
                this.typeDockableDoAttachToDrone(actor, 0);
            }
        } else this.FM.AS.netToMaster(34, 0, 0, actor);
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
        if (i != 0) return;
        if (Actor.isValid(this.drone) && this.drone instanceof Aircraft && ((Aircraft)this.drone).FM.Gears.onGround()) return;
        if (flag) {
            if (this.FM.AS.isMaster()) {
                this.FM.AS.netToMirrors(35, 0, 1, actor);
                this.typeDockableDoDetachFromDrone(0);
            } else this.FM.AS.netToMaster(35, 0, 1, actor);
        }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i) {
        if (i != 0) return;
        if (!Actor.isValid(this.drone)) {
            this.drone = actor;
            this.droneInitiator = actor;
            ((TypeDockable) this.drone).typeDockableDoAttachToQueen(this, 0);
//            if (this.FM.EI.getNum() == 1) {
//                this.FM.Scheme = 6;
//                Aircraft aircraft = (Aircraft) actor;
//                this.FM.EI.setNum(3);
//                Motor motor = this.FM.EI.engines[0];
//                this.FM.EI.engines = new Motor[3];
//                this.FM.EI.engines[0] = motor;
//                this.FM.EI.engines[1] = aircraft.FM.EI.engines[0];
//                this.FM.EI.engines[2] = aircraft.FM.EI.engines[1];
//                this.FM.EI.bCurControl = new boolean[] { true, true, true };
//            }
//            this.FM.EI.setEngineRunning();
            this.FM.CT.setGearAirborne();
            this.moveGear(0.0F);
            this.moveSteering(0.0F);
            this.FM.CT.GearControl = ((Aircraft) actor).FM.CT.GearControl;
            if (this.FM.CT.Weapons[3] == null) this.FM.CT.Weapons[3] = new BulletEmitter[1];
            this.FM.CT.Weapons[3][0] = new BombGunNull();
            this.FM.CT.Weapons[3][0].set(this, "_Clip00");
            this.FM.CT.Weapons[3][0].loadBullets(2);
        }
    }

    public void typeDockableDoDetachFromDrone(int i) {
        if (i != 0) return;
        if (!Actor.isValid(this.drone)) return;
        if (this.drone instanceof Aircraft && ((Aircraft)this.drone).FM.Gears.onGround()) return;
        this.pos.setBase(null, null, true);
        ((TypeDockable) this.drone).typeDockableDoDetachFromQueen(i);
        this.drone = null;
        this.FM.CT.setGearAirborne();
        //this.FM.Vwld.z += 25D;
        Orientation or = new Orientation();
        or.set(this.FM.Or);
        Vector3d dv = new Vector3d();
        dv.set(0D, 0D, 25D);
        or.transform(dv);
        this.FM.Vwld.add(dv);
//        if (this.FM.EI.getNum() == 3) {
//            this.FM.Scheme = 1;
//            this.FM.EI.setNum(1);
//            Motor motor = this.FM.EI.engines[0];
//            this.FM.EI.engines = new Motor[1];
//            this.FM.EI.engines[0] = motor;
//            this.FM.EI.bCurControl = new boolean[] { true };
//            for (int j = 1; j < 3; j++) {
//                if (this.FM.Gears.clpEngineEff[j][0] != null) {
//                    Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][0]);
//                    this.FM.Gears.clpEngineEff[j][0] = null;
//                }
//                if (this.FM.Gears.clpEngineEff[j][1] != null) {
//                    Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][1]);
//                    this.FM.Gears.clpEngineEff[j][1] = null;
//                }
//            }
//        }
        if (this.FM instanceof Maneuver) {
            // Invalidate any strafing target if exists
            Maneuver maneuver = (Maneuver)this.FM;
            maneuver.target_ground = null;
            
//            if (maneuver.Group != null && maneuver.Group.numInGroup(this) == 1) {
//                Exception test = new Exception("typeDockableDoDetachFromDrone");
//                test.printStackTrace();
//            }
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
    }

    public void typeDockableDoDetachFromQueen(int i) {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (Actor.isValid(this.drone)) {
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_IS_ATTACHED);
            ActorNet actornet = this.drone.net;
//            System.out.println("FW_190A8MSTL typeDockableReplicateToNet docked, actornet=" + (actornet == null?"null":((Actor)actornet.superObj()).name() + ", countNoMirrors()=" + actornet.countNoMirrors() + ", countMirrors()=" + actornet.countMirrors()));
//            if (actornet.countNoMirrors() > 0) actornet = null;
//            if (actornet.countMirrors() < 1) actornet = null;
            netmsgguaranted.writeNetObj(actornet);
            netmsgguaranted.writeBoolean(this.isBombChargeArmed());
        } else if (Actor.isValid(this.getDrone())) {
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_IS_DETACHED);
            ActorNet actornet = this.getDrone().net;
//            System.out.println("FW_190A8MSTL typeDockableReplicateToNet undocked, actornet=" + (actornet == null?"null":((Actor)actornet.superObj()).name() + ", countNoMirrors()=" + actornet.countNoMirrors() + ", countMirrors()=" + actornet.countMirrors()));
//            if (actornet.countNoMirrors() > 0) actornet = null;
//            if (actornet.countMirrors() < 1) actornet = null;
            netmsgguaranted.writeNetObj(actornet);
            netmsgguaranted.writeBoolean(this.isBombChargeArmed());
        } else {
//            System.out.println("FW_190A8MSTL typeDockableReplicateToNet no drone");
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_NO_DRONE);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        byte dockStatus = netmsginput.readByte();
        NetObj netobj;
        switch (dockStatus) {
            case NetMistel.NETMSG_MISTEL_IS_ATTACHED:
                netobj = netmsginput.readNetObj();
                this.setBombChargeArmed(netmsginput.readBoolean());
//                System.out.println("FW_190A8MSTL typeDockableReplicateFromNet docked, netobj=" + (netobj == null?"null":((Actor)netobj.superObj()).name()));
                if (netobj != null) {
                    this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), 0);
                }
                break;
            case NetMistel.NETMSG_MISTEL_IS_DETACHED:
                netobj = netmsginput.readNetObj();
                this.setBombChargeArmed(netmsginput.readBoolean());
//                System.out.println("FW_190A8MSTL typeDockableReplicateFromNet undocked, netobj=" + (netobj == null?"null":((Actor)netobj.superObj()).name()));
                if (netobj != null) {
                    this.drone = null;
                    this.droneInitiator = (Actor) netobj.superObj();
                    if (this.droneInitiator instanceof Mistel) {
                        Mistel mistel = (Mistel)this.droneInitiator;
                        mistel.setQueen(this);
                        mistel.setBombChargeArmed(this.isBombChargeArmed());
                    }
                }
                break;
            case NetMistel.NETMSG_MISTEL_NO_DRONE:
            default:
//                System.out.println("FW_190A8MSTL typeDockableReplicateFromNet undocked");
                break;
        }
//        if (netmsginput.readByte() != 1) {
////            System.out.println("FW_190A8MSTL typeDockableReplicateFromNet undocked");
//            return;
//        }
////        System.out.println("FW_190A8MSTL typeDockableReplicateFromNet docked 1");
//        NetObj netobj = netmsginput.readNetObj();
//        if (netobj != null) {
////            System.out.println("FW_190A8MSTL typeDockableReplicateFromNet docked 2");
//            this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), 0);
//        }
    }

    public boolean netGetGMsg(NetMsgInput netmsginput, boolean bool) throws IOException {
        if (!NetMistel.netGetGMsg(this, netmsginput, bool)) return super.netGetGMsg(netmsginput, bool);
        return true;
    }
    
    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (!flag) return;
        if (!this.FM.AS.isMaster()) return;
        if (!this.typeDockableIsDocked()) return;
        if (this.undockTime != -1L) return;
        if (!(this.FM instanceof Maneuver)) return;
        AirGroup airgroup = ((Maneuver)this.FM).Group;
        if (this.FM.AP.way.curr().Action != WayPoint.GATTACK) {
            if (airgroup == null || airgroup.airc == null || !Actor.isValid(airgroup.airc[0]) || !(airgroup.airc[0] instanceof FW_190A8MSTL)) return;
            if (airgroup != null && airgroup.airc != null && this == airgroup.airc[0]) return;
            if (Actor.isValid(((FW_190A8MSTL)airgroup.airc[0]).drone)) return;
            if (!((FW_190A8MSTL)airgroup.airc[0]).isBombChargeArmed()) return;
        }
        if (this.FM.AP.way.curr().Action != WayPoint.GATTACK) {
            if (this.FM.AP.way.next().Action != WayPoint.GATTACK) this.FM.AP.way.prev();
        }
        this.undockTime = Time.current() + (airgroup.numInGroup(this) * UNDOCK_GAP) + TrueRandom.nextLong(-UNDOCK_GAP/4L, UNDOCK_GAP/4L);
    }

    public boolean isQueen() {
        return true;
    }

    public boolean isDrone() {
        return false;
    }

//    public void msgShot(Shot shot) {
//        MistelTools.checkDetach(this, shot);
//        super.msgShot(shot);
//    }

    private Actor          drone       = null;
    private Actor          droneInitiator;
    private long undockTime = -1L;
    private long UNDOCK_GAP = 1000L;
    private long           ignoreGroundTargetTime;
    private boolean        bombChargeArmed;

    static {
        Class class1 = FW_190A8MSTL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190A-8(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190A-8-Mistel (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190F8MSTL.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Property.set(class1, "FormationScaleCoeff", 1.2F);
//        Property.set(class1, "AutopilotElevatorAboveReferenceAltitudeFactor", 0.006F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_CANNON07",
                "_CANNON08", "_ExternalDev05", "_ExternalDev06", "_CANNON09", "_CANNON10", "_ExternalDev07", "_ExternalDev08", "_CANNON11", "_CANNON12", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02" });
    }

}
