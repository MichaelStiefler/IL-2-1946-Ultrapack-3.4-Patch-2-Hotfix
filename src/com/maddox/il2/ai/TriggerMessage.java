// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.maddox.JGP.Point2d;
import com.maddox.il2.builder.ActorTrigger;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv.ResultTrigger;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.sas1946.il2.util.TrueRandom;

class TriggerMessage extends Trigger {

    public TriggerMessage(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability, String linkActorName, String displayMessage, float displayTime) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTriggerClass(TYPE_MESSAGE);
        if (this.getDisplayMessage() == "" || this.getDisplayMessage() == null)
            this.destroy();
    }

    public TriggerMessage(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability, String linkActorName, String displayMessage, float displayTime, boolean triggerMultiple) {
        super(triggerName, triggeredByArmy, timeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
        this.setTriggerClass(TYPE_MESSAGE);
        this.setTriggerMultiple(triggerMultiple);
        if (this.getDisplayMessage() == "" || this.getDisplayMessage() == null)
            this.destroy();
//        System.out.println("TriggerMessage (" + triggerName + 
//                ", " + triggeredByArmy + 
//                ", " + timeout + 
//                ", " + posX + 
//                ", " + posY + 
//                ", " + radius + 
//                ", " + altitudeMin + 
//                ", " + altitudeMax + 
//                ", " + triggeredBy + 
//                ", " + triggerOnExit + 
//                ", " + noObjectsMin + 
//                ", " + probability + 
//                ", " + linkActorName + 
//                ", " + displayMessage + 
//                ", " + displayTime + 
//                ", " + triggerMultiple + ")");
    }

//    protected boolean checkPeriodic() {
//        this.checkMove();
//        if (this.getTriggeredBy() != ActorTrigger.TRIGGERED_BY_CURRENT_PLAYER_ONLY) return super.checkPeriodic();
//        if (this.getLinkActorName() != "" && this.getLinkActorName() != null && Actor.getByName(this.getLinkActorName()) == null) return false;
//        if (!Config.isUSE_RENDER()) return false;
//        if (!Actor.isValid(World.getPlayerAircraft())) return false;
//        if ((World.getPlayerAircraft().getArmy() & this.getTriggeredByArmy()) == 0) return false;
//        Point3d playerPos = World.getPlayerAircraft().pos.getAbsPoint();
//        double altMin = (this.getAltitudeMin() < 1000.0D) ? (playerPos.z - Engine.land().HQ(playerPos.x, playerPos.y)) : playerPos.z;
//        double altMax = (this.getAltitudeMax() < 1000.0D) ? (playerPos.z - Engine.land().HQ(playerPos.x, playerPos.y)) : playerPos.z;
//        boolean result = (this.getPosTrigger().distance(new Point2d(playerPos.x, playerPos.y)) <= this.getRadius() && altMin >= this.getAltitudeMin() && altMax <= this.getAltitudeMax());
//        this.setMessageAltitude((int) playerPos.z);
//        System.out.println("checkPeriodic() TRIGGERED_BY_CURRENT_PLAYER_ONLY result=" + result);
//        if (this.isTriggerOnExit()) {
//            if (result) {
//                this.curState = false;
//                this.setEntered(true);
//                return false;
//            }
//            if (this.curState || !this.isEntered()) return false;
//            this.curState = true;
//            this.setEntered(false);
//            return true;
//        } else {
//            if (result) {
//                if (curState) return false;
//                curState = true;
//                return true;
//            }
//            curState = false;
//            return false;
//        }
//    }

    protected boolean checkPeriodic() {
        this.checkMove();
        if (this.getTriggeredBy() != ActorTrigger.TRIGGERED_BY_CURRENT_PLAYER_ONLY)
            return super.checkPeriodic();
        if (this.getLinkActorName() != "" && this.getLinkActorName() != null && Actor.getByName(this.getLinkActorName()) == null)
            return false;
        ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(this.getPosTrigger(), this.getRadius(), this.getAltitudeMin(), this.getAltitudeMax(), this.getTriggeredByArmy(), this.getTriggeredBy(), this.getNoObjectsMin());
//        this.setMessageAltitude((int) result.altiSea);
//        if (this.isTriggerOnExit()) {
//            if (this.isEntered()) return !result.result;
//            if (result.result) this.setEntered(true);
//            return false;
//        } else return result.result;

        if (result.result && result.triggerActors != null) {
            Iterator it = result.triggerActors.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (!playerAircraftInTrigger.containsKey(o)) {
                    newPlayerAircraftInTrigger(o);
                } else {
                    byte b = ((Byte) (playerAircraftInTrigger.get(o))).byteValue();
                    if ((b & IS_IN_TRIGGER) == 0)
                        playerAircraftBackInTrigger(o);
                }
            }
            it = playerAircraftInTrigger.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                byte b = ((Byte) (pair.getValue())).byteValue();
                if (((b & IS_IN_TRIGGER) != 0) && !result.triggerActors.contains(pair.getKey())) {
                    playerAircraftLeftTrigger(pair.getKey());
//                    if (this.isTriggerMultiple()) it.remove();
                }
            }
        } else {
            Iterator it = playerAircraftInTrigger.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                byte b = ((Byte) (pair.getValue())).byteValue();
                if ((b & IS_IN_TRIGGER) != 0)
                    playerAircraftLeftTrigger(pair.getKey());
            }
        }

        return false;
    }

    private void newPlayerAircraftInTrigger(Object o) {
        if (!(o instanceof NetAircraft)) {
            System.out.println("newPlayerAircraftInTrigger Object is not instanceof NetAircraft!!!");
            return;
        }
        if (this.isTriggerOnExit()) {
            playerAircraftInTrigger.put(o, Byte.valueOf(Byte.toString(IS_IN_TRIGGER)));
            return;
        }
        NetAircraft n = (NetAircraft) o;
        playerAircraftInTrigger.put(n, Byte.valueOf(Byte.toString((byte) (IS_IN_TRIGGER | HAS_BEEN_TRIGGERED))));
        this.replicatePlayerTrigger(n.netUser(), n.pos.getAbsPoint().z);
    }

    private void playerAircraftBackInTrigger(Object o) {
        if (!(o instanceof NetAircraft)) {
            System.out.println("playerAircraftBackInTrigger Object is not instanceof NetAircraft!!!");
            return;
        }
        byte curVal = ((Byte) (playerAircraftInTrigger.get(o))).byteValue();
        curVal |= IS_IN_TRIGGER;
        if (this.isTriggerOnExit()) {
//            if (this.isTriggerMultiple()) curVal &= ~HAS_BEEN_TRIGGERED;
            playerAircraftInTrigger.put(o, Byte.valueOf(Byte.toString(curVal)));
            return;
        }
        NetAircraft n = (NetAircraft) o;
        boolean replicatePending = this.isTriggerMultiple() || ((curVal & HAS_BEEN_TRIGGERED) == 0);
        curVal |= HAS_BEEN_TRIGGERED;
        playerAircraftInTrigger.put(n, Byte.valueOf(Byte.toString(curVal)));
        if (replicatePending)
            this.replicatePlayerTrigger(n.netUser(), n.pos.getAbsPoint().z);
    }

    private void playerAircraftLeftTrigger(Object o) {
        if (!(o instanceof NetAircraft)) {
            System.out.println("playerAircraftLeftTrigger Object is not instanceof NetAircraft!!!");
            return;
        }
        byte curVal = ((Byte) (playerAircraftInTrigger.get(o))).byteValue();
        curVal &= ~IS_IN_TRIGGER;
        if (this.isTriggerOnExit()) {
            NetAircraft n = (NetAircraft) o;
            boolean replicatePending = this.isTriggerMultiple() || ((curVal & HAS_BEEN_TRIGGERED) == 0);
            curVal |= HAS_BEEN_TRIGGERED;
            playerAircraftInTrigger.put(o, Byte.valueOf(Byte.toString(curVal)));
            if (replicatePending)
                this.replicatePlayerTrigger(n.netUser(), n.pos.getAbsPoint().z);
            return;
        }
        playerAircraftInTrigger.put(o, Byte.valueOf(Byte.toString(curVal)));
    }

    protected void execute() {
        if (TrueRandom.nextFloat(100F) < this.getProbability())
            this.doExecute();
        if (this.getTriggeredBy() == ActorTrigger.TRIGGERED_BY_CURRENT_PLAYER_ONLY)
            return;
        if (!this.isTriggerMultiple())
            super.execute();
    }

    protected void doExecute() {
        if (!this.isTriggerMultiple() && this.isTriggered())
            return;
        this.setTriggered(true);
        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) {
            EventLog.onTriggerActivate(null, this);
            this.doSendMsg(false);
        } else {
            EventLog.onTriggerActivateLink(null, this);
            this.doSendMsg(true);
        }
        if (this.getTriggeredBy() == ActorTrigger.TRIGGERED_BY_CURRENT_PLAYER_ONLY)
            return;
        super.doExecute();
    }

    public void replicatePlayerTrigger(NetUser theNetUser, double altitude) {
//      Exception test = new Exception("Trigger.replicatePlayerTrigger(" + theNetUser.shortName() + ", " + (int)altitude + ")");
//      test.printStackTrace();
//      System.out.println("replicatePlayerTrigger(" + theNetUser.shortName() + ", " + (int)altitude + ")");
//      this.updateLocalParams();
        if (Main.cur().netServerParams == null || (Mission.isSingle() && !NetMissionTrack.isRecording()))
            return;
//      System.out.println("replicatePlayerTrigger 1");
        if (!(NetEnv.host() instanceof NetUser))
            return;
//      System.out.println("replicatePlayerTrigger 2");
        NetUser netUser = ((NetUser) NetEnv.host());
        if (!netUser.isMirrored())
            return;
//      System.out.println("replicatePlayerTrigger 3");
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(NetUser.MSG_TRIGGER);
            netmsgguaranted.writeByte(this.getTriggerClass());
            netmsgguaranted.write255(this.getTriggerName());
            netmsgguaranted.writeInt((int) altitude);
            netmsgguaranted.writeFloat(this.getDisplayTime());
            netmsgguaranted.writeNetObj(theNetUser);
            if (this.getLinkActorName() != null && this.getLinkActorName().length() > 0) {
//                      netmsgguaranted.write255(this.getLinkActorName());
                Point2d p2dTemp = this.getPosLink();
                if (p2dTemp == null)
                    p2dTemp = this.getPosTrigger();
                netmsgguaranted.writeDouble(p2dTemp.x);
                netmsgguaranted.writeDouble(p2dTemp.y);
            } else {
                netmsgguaranted.writeDouble(this.getPosTrigger().x);
                netmsgguaranted.writeDouble(this.getPosTrigger().y);
            }
//          netUser.post(netmsgguaranted);
            theNetUser.postTo(theNetUser.masterChannel(), netmsgguaranted);
//          theNetUser.post(netmsgguaranted);

//          theNetUser.postTo(netUser.masterChannel(), netmsgguaranted);

//          System.out.println("Trigger replicatePlayerTrigger() " + this.getTriggerName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private boolean curState = false;
    private HashMap           playerAircraftInTrigger = new HashMap();

    private static final byte IS_IN_TRIGGER           = 0x01;
    private static final byte HAS_BEEN_TRIGGERED      = 0x02;
}
