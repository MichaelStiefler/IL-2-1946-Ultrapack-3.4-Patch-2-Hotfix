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
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombMistelJu88;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;

public class BF_109F4MSTL extends BF_109Fx implements TypeDockable, Mistel {

    public BF_109F4MSTL() {
        this.droneInitiator = null;
        this.ignoreGroundTargetTime = -1L;
    }

    public void onAircraftLoaded() {
//        System.out.println("BF_109F4MSTL onAircraftLoaded()");
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
//      System.out.println("BF_109F4MSTL setBombChargeArmed(" + arm + ")");
      this.bombChargeArmed = arm;
//      if (Actor.isValid(this.drone) && this.drone instanceof Mistel) {
//          ((Mistel)this.drone).setBombChargeArmed(arm);
//      }
  }
  
  public boolean isBombChargeArmed()  {
      return bombChargeArmed;
  }

  public void mistelExplosion() {
//      System.out.println("BF_109F4MSTL mistelExplosion()");
      if (this.getDrone() == null) {
//          System.out.println("BF_109F4MSTL mistelExplosion() getDrone()=null");
          return;
      }
      if (this.getDrone().pos.getAbsPoint().z - Engine.cur.land.HQ(this.getDrone().pos.getAbsPoint().x, this.getDrone().pos.getAbsPoint().y) > 50D) {
//          System.out.println("BF_109F4MSTL mistelExplosion() Altitude above land > 50m");
          MsgExplosion.send(this.getDrone(), null, this.getDrone().pos.getAbsPoint(), this, 0.0F, 6550.0F, 0, 890.0F);
          return;
      }
//      System.out.println("BF_109F4MSTL mistelExplosion() spawning BombFAB5000, isBombChargeArmed()=" + this.isBombChargeArmed());
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
        super.finalize();
        NetMistel.removeNetMistelFromList(this);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 0.8F;
        float f2 = -0.5F * (float) Math.cos(f / f1 * Math.PI) + 0.5F;
        if (f <= f1 || f == 1.0F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = -0.5F * (float) Math.cos((f - (1.0F - f1)) / f1 * Math.PI) + 0.5F;
        if (f >= 1.0F - f1) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        if (f < 0.98F) hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 70F * f, 0.0F, 0.0F);
        if (f > 0.99F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
        if (f < 0.01F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGear(float f) {
        if (this.typeDockableIsDocked()) moveGear(this.hierMesh(), 0.0F);
        else moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
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
//            System.out.println("BF_109F4MSTL updateAI() " + this.name() + " disarm Drone Charge since we're dumping the drone!");
            this.setBombChargeArmed(false);
            maneuver.dumpBombsPassively = true;
            this.typeDockableAttemptDetach();
            if (maneuver.Group != null && maneuver.Group.airc != null && this == maneuver.Group.airc[0] && maneuver.Group.airc.length > 1) {
//                System.out.println("BF_109F4MSTL updateAI() " + this.name() + " change Mistel pos in Group since it's dumping the drone and it's the group leader!");
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
    }

    public boolean typeDockableIsDocked() {
        return Actor.isValid(this.drone);
    }

    public Actor typeDockableGetDrone() {
        return this.drone;
    }

    public void typeDockableAttemptAttach() {
//        System.out.println("BF_109F4MSTL typeDockableAttemptAttach()");
        if (this.FM.AS.isMaster() && !this.typeDockableIsDocked()) {
            Aircraft aircraft = War.getNearestFriend(this);
//            System.out.println("BF_109F4MSTL typeDockableAttemptAttach() aircraft=" + aircraft.name());
            if (aircraft instanceof JU_88MSTL) ((TypeDockable) aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach() {
        if (Actor.isValid(this.drone) && this.drone instanceof Aircraft && ((Aircraft)this.drone).FM.Gears.onGround()) return;
        if (this.FM.AS.isMaster())
            if (Actor.isValid(this.drone)) this.typeDockableRequestDetach(this.drone, 0, true);
    }

    public void typeDockableRequestAttach(Actor actor) {
//        System.out.println("BF_109F4MSTL typeDockableRequestAttach(" + actor.name() + ")");
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
//        System.out.println("BF_109F4MSTL typeDockableRequestAttach(" + actor.name() + ", " + i + ", " + flag + ")");
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
//        System.out.println("BF_109F4MSTL typeDockableDoAttachToDrone(" + actor.name() + ", " + i + ")");
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
//        this.FM.Vwld.z += 25D;
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
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i) {
//        System.out.println("BF_109F4MSTL typeDockableDoAttachToQueen(" + actor.name() + ", " + i + ")");
    }

    public void typeDockableDoDetachFromQueen(int i) {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        if (Actor.isValid(this.drone)) {
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_IS_ATTACHED);
            ActorNet actornet = this.drone.net;
//            System.out.println("BF_109F4MSTL typeDockableReplicateToNet docked, actornet=" + (actornet == null?"null":((Actor)actornet.superObj()).name() + ", countNoMirrors()=" + actornet.countNoMirrors() + ", countMirrors()=" + actornet.countMirrors()));
//            if (actornet.countNoMirrors() > 0) actornet = null;
//            if (actornet.countMirrors() < 1) actornet = null;
            netmsgguaranted.writeNetObj(actornet);
            netmsgguaranted.writeBoolean(this.isBombChargeArmed());
        } else if (Actor.isValid(this.getDrone())) {
            netmsgguaranted.writeByte(NetMistel.NETMSG_MISTEL_IS_DETACHED);
            ActorNet actornet = this.getDrone().net;
//            System.out.println("BF_109F4MSTL typeDockableReplicateToNet undocked, actornet=" + (actornet == null?"null":((Actor)actornet.superObj()).name() + ", countNoMirrors()=" + actornet.countNoMirrors() + ", countMirrors()=" + actornet.countMirrors()));
//            if (actornet.countNoMirrors() > 0) actornet = null;
//            if (actornet.countMirrors() < 1) actornet = null;
            netmsgguaranted.writeNetObj(actornet);
            netmsgguaranted.writeBoolean(this.isBombChargeArmed());
        } else {
//            System.out.println("BF_109F4MSTL typeDockableReplicateToNet no drone");
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
//                System.out.println("BF_109F4MSTL typeDockableReplicateFromNet docked, netobj=" + (netobj == null?"null":((Actor)netobj.superObj()).name()));
                if (netobj != null) {
                    this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), 0);
                }
                break;
            case NetMistel.NETMSG_MISTEL_IS_DETACHED:
                netobj = netmsginput.readNetObj();
                this.setBombChargeArmed(netmsginput.readBoolean());
//                System.out.println("BF_109F4MSTL typeDockableReplicateFromNet undocked, netobj=" + (netobj == null?"null":((Actor)netobj.superObj()).name()));
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
//                System.out.println("BF_109F4MSTL typeDockableReplicateFromNet undocked");
                break;
        }
//        if (netmsginput.readByte() != 1) {
////            System.out.println("BF_109F4MSTL typeDockableReplicateFromNet undocked");
//            return;
//        }
////        System.out.println("BF_109F4MSTL typeDockableReplicateFromNet docked 1");
//        NetObj netobj = netmsginput.readNetObj();
//        if (netobj != null) {
////            System.out.println("BF_109F4MSTL typeDockableReplicateFromNet docked 2");
//            this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), 0);
//        }
    }

    public void moveCockpitDoor(float f) {
         if (this.bHasBlister) {
            this.resetYPRmodifier();
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if (Config.isUSE_RENDER()) {
                if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
                this.setDoorSnd(f);
            }
        }
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
            if (airgroup == null || airgroup.airc == null || !Actor.isValid(airgroup.airc[0]) || !(airgroup.airc[0] instanceof BF_109F4MSTL)) return;
            if (airgroup != null && airgroup.airc != null && this == airgroup.airc[0]) return;
            if (Actor.isValid(((BF_109F4MSTL)airgroup.airc[0]).drone)) return;
            if (!((BF_109F4MSTL)airgroup.airc[0]).isBombChargeArmed()) return;
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
        Class class1 = BF_109F4MSTL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109F-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_sk", "3DO/Plane/Bf-109F-4(sk)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109F-4_Mod.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109F2.class });
        Property.set(class1, "LOSElevation", 0.74205F);
        Property.set(class1, "FormationScaleCoeff", 1.1F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }

}
