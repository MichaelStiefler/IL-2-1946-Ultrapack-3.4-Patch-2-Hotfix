/* 4.10.1 class */
package com.maddox.il2.ai;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.MistelTools;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class War {
    public static final int      TICK_DIV4  = 4;
    public static final int      TICK_DIV8  = 8;
    public static final int      TICK_DIV16 = 16;
    public static final int      TICK_DIV32 = 32;
    public static final int      ARMY_NUM   = 2;
    public static AirGroupList[] Groups     = new AirGroupList[2];
    private static int           curArmy    = 0;
    private static int           curGroup   = 0;
    private static Vector3d      tmpV       = new Vector3d();
    private static Vector3d      Ve         = new Vector3d();
    private static Vector3d      Vtarg      = new Vector3d();

    public static War cur() {
        return World.cur().war;
    }

    public War() {
        // TODO: Lutz mod
        ZutiSupportMethods_AI.bIniTakeOff = false;
    }

    public boolean isActive() {
        if (!Mission.isPlaying()) return false;
        if (NetMissionTrack.isPlaying()) return false;
        if (Mission.isSingle()) return true;

        // TODO: Edit by |ZUTI|
        // if (Mission.isServer() && Mission.isCoop())
        if (Mission.isServer() && (Mission.isCoop() || Mission.isDogfight())) return true;

        return false;
    }

    public void onActorDied(Actor actor, Actor actor_0_) {
        if (this.isActive()) if (actor instanceof Aircraft && ((Aircraft) actor).FM instanceof Maneuver) {
            Maneuver maneuver = (Maneuver) ((Aircraft) actor).FM;
            if (maneuver.Group != null) {
                maneuver.Group.delAircraft((Aircraft) actor);
                maneuver.Group = null;
            }
        }
    }

    public void missionLoaded() {
        /* empty */
    }

    public void resetGameCreate() {
        curArmy = 0;
        curGroup = 0;

        // TODO: Lutz Mod
        ZutiSupportMethods_AI.bIniTakeOff = false;
    }

    public void resetGameClear() {
        for (int i = 0; i < 2; i++)
            while (Groups[i] != null) {
                // TODO: Edit by |ZUTI|
                // Pinpointed to here. When errors are caught, all is fine!
                try {
                    Groups[i].G.release();
                } catch (Exception ex) {}
                try {
                    AirGroupList.delAirGroup(Groups, i, Groups[i].G);
                } catch (Exception ex) {}
            }

        // TODO: Lutz mod
        ZutiSupportMethods_AI.bIniTakeOff = false;
    }

    // TODO: +++ TD AI code backport from 4.13 +++
    public void interpolateTickOld() {
        if (this.isActive()) try {
            if (Time.tickCounter() % 4 == 0) {
                this.checkCollisionForAircraft();
                if (Time.tickCounter() % 32 == 0) {
                    this.checkGroupsContact();
                    if (Time.tickCounter() % 64 == 0) this.delEmptyGroups();
                }
                this.upgradeGroups();
            }
            // TODO: Lutz mod
            // ---------------------------------------------------
            else if (com.maddox.rts.Time.tickCounter() % 81 == 0) ZutiSupportMethods_AI.checkGroupTakeOff(Groups);
            // ---------------------------------------------------

            this.formationUpdate();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void interpolateTick() {
        if (!this.isActive()) return;
        try {
            // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
//          if(Time.tickCounter() % 128 == 0) // TODO: Changed by SAS~Storebror: All special trigger handling shifted to dedicated classes
              World.cur().triggersGuard.checkTask();
          // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
            if (Time.current() > 1000L) this.checkGroupsContact();
            this.checkCollisionForAircraft();
            if (Time.tickCounter() % 4 == 0) {
                if (Time.tickCounter() % 64 == 0) this.delEmptyGroups();
                this.upgradeGroups();
            }
            // TODO: Lutz mod
            // ---------------------------------------------------
            if (Time.tickCounter() % 81 == 0) ZutiSupportMethods_AI.checkGroupTakeOff(Groups);
            // ---------------------------------------------------
            this.formationUpdate();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
    // TODO: --- TD AI code backport from 4.13 ---

    private void upgradeGroups() {
        int i = AirGroupList.length(Groups[curArmy]);
        if (i > curGroup) AirGroupList.getGroup(Groups[curArmy], curGroup).update();
        else {
            curArmy++;
            if (curArmy > 1) curArmy = 0;
            curGroup = 0;
            return;
        }
        curGroup++;
    }

    private void formationUpdate() {
        for (int i = 0; i < 2; i++)
            if (Groups[i] != null) {
                int i_1_ = AirGroupList.length(Groups[i]);
                for (int i_2_ = 0; i_2_ < i_1_; i_2_++)
                    AirGroupList.getGroup(Groups[i], i_2_).formationUpdate();
            }
    }

    // TODO: +++ TD AI code backport from 4.13 +++
    public static boolean informOtherGroupsNearBy(AirGroup airgroup, AirGroupList airgrouplist, AirGroup airgroup1) {
        boolean flag = false;
        int i = AirGroupList.length(airgrouplist);
        for (int j = 0; j < i; j++) {
            AirGroup airgroup2 = AirGroupList.getGroup(airgrouplist, j);
            if (airgroup != airgroup2) {
                tmpV.sub(airgroup2.Pos, airgroup.Pos);
                double d = tmpV.length();
                if (airgroup2.clientGroup != null && airgroup == airgroup2.clientGroup) {
                    AirGroupList.addAirGroup(airgroup2.enemies, 0, airgroup1);
                    airgroup2.setEnemyFighters();
                    airgroup2.targetGroup = airgroup1;
                    airgroup2.bInitAttack = false;
                    flag = true;
                } else if (d < 12000D) {
                    AirGroupList.addAirGroup(airgroup2.enemies, 0, airgroup1);
                    airgroup2.setEnemyFighters();
                    flag = true;
                }
            }
        }

        return flag;
    }
    // TODO: --- TD AI code backport from 4.13 ---

    private void checkGroupsContact() {
        int i = AirGroupList.length(Groups[0]);
        int i_3_ = AirGroupList.length(Groups[1]);
        for (int i_4_ = 0; i_4_ < i; i_4_++) {
            AirGroup airgroup = AirGroupList.getGroup(Groups[0], i_4_);
            if (airgroup != null && airgroup.Pos != null) for (int i_5_ = 0; i_5_ < i_3_; i_5_++) {
                AirGroup airgroup1 = AirGroupList.getGroup(Groups[1], i_5_);
                if (airgroup1 != null && airgroup1.Pos != null) {
                    tmpV.sub(airgroup.Pos, airgroup1.Pos);
                    // TODO: +++ TD AI code backport from 4.13 +++
//						if (tmpV.lengthSquared() < 4.0E8 && airgroup.groupsInContact(airgroup1))
//						{
//							if (!AirGroupList.groupInList(airgroup.enemies[0], airgroup1))
//							{
//								AirGroupList.addAirGroup(airgroup.enemies, 0, airgroup1);
//								if (airgroup.airc[0] != null && airgroup1.airc[0] != null)
//									Voice.speakEnemyDetected(airgroup.airc[0], airgroup1.airc[0]);
//								airgroup.setEnemyFighters();
//							}
//							if (!AirGroupList.groupInList(airgroup1.enemies[0], airgroup))
//							{
//								AirGroupList.addAirGroup(airgroup1.enemies, 0, airgroup);
//								if (airgroup.airc[0] != null && airgroup1.airc[0] != null)
//									Voice.speakEnemyDetected(airgroup1.airc[0], airgroup.airc[0]);
//								airgroup1.setEnemyFighters();
//							}
//						}
//						else
//						{
//							if (AirGroupList.groupInList(airgroup.enemies[0], airgroup1))
//							{
//								AirGroupList.delAirGroup(airgroup.enemies, 0, airgroup1);
//								airgroup.setEnemyFighters();
//							}
//							if (AirGroupList.groupInList(airgroup1.enemies[0], airgroup))
//							{
//								AirGroupList.delAirGroup(airgroup1.enemies, 0, airgroup);
//								airgroup1.setEnemyFighters();
//							}
//						}

                    if (tmpV.lengthSquared() < 400000000D) {
                        if (!AirGroupList.groupInList(airgroup.enemies[0], airgroup1)) for (int i1 = 0; i1 < airgroup.nOfAirc; i1++) {
                            Aircraft aircraft = airgroup.airc[i1];
                            if (!aircraft.FM.isTick(32, 0)) continue;
                            int k1 = World.Rnd().nextInt(0, airgroup1.nOfAirc - 1);
                            VisCheck.checkGroup(aircraft, airgroup1.airc[k1], airgroup, airgroup1);
                            if (!airgroup.bSee) continue;
//                                    System.out.println("WAR setEnemyFighters 1");
                            informOtherGroupsNearBy(airgroup, Groups[0], airgroup1);
                            AirGroupList.addAirGroup(airgroup.enemies, 0, airgroup1);
                            if (airgroup.airc[0] != null && airgroup1.airc[k1] != null) Voice.speakEnemyDetected(airgroup.airc[0], airgroup1.airc[k1]);
                            airgroup.setEnemyFighters();
                            airgroup.bInitAttack = true;
                            airgroup.bSee = false;
                            break;
                        }
                        if (!AirGroupList.groupInList(airgroup1.enemies[0], airgroup)) for (int j1 = 0; j1 < airgroup1.nOfAirc; j1++) {
                            Aircraft aircraft1 = airgroup1.airc[j1];
                            if (!aircraft1.FM.isTick(32, 0)) continue;
                            int l1 = World.Rnd().nextInt(0, airgroup.nOfAirc - 1);
                            VisCheck.checkGroup(aircraft1, airgroup.airc[l1], airgroup1, airgroup);
                            if (!airgroup1.bSee) continue;
//                                    System.out.println("WAR setEnemyFighters 2");
                            informOtherGroupsNearBy(airgroup1, Groups[1], airgroup);
                            AirGroupList.addAirGroup(airgroup1.enemies, 0, airgroup);
                            if (airgroup.airc[0] != null && airgroup1.airc[l1] != null) Voice.speakEnemyDetected(airgroup1.airc[0], airgroup.airc[l1]);
                            airgroup1.setEnemyFighters();
                            airgroup1.bInitAttack = true;
                            airgroup1.bSee = false;
                            break;
                        }
                    } else {
                        if (AirGroupList.groupInList(airgroup.enemies[0], airgroup1)) {
                            AirGroupList.delAirGroup(airgroup.enemies, 0, airgroup1);
                            airgroup.setEnemyFighters();
                        }
                        if (AirGroupList.groupInList(airgroup1.enemies[0], airgroup)) {
                            AirGroupList.delAirGroup(airgroup1.enemies, 0, airgroup);
                            airgroup1.setEnemyFighters();
                        }
                    }

                }
                // TODO: --- TD AI code backport from 4.13 ---
            }
        }
    }

    private void delEmptyGroups() {
        int i = AirGroupList.length(Groups[0]);
        int i_7_ = AirGroupList.length(Groups[1]);
        for (int i_8_ = 0; i_8_ < i; i_8_++) {
            AirGroup airgroup = AirGroupList.getGroup(Groups[0], i_8_);
            if (airgroup != null && airgroup.nOfAirc == 0) {
                airgroup.release();
                AirGroupList.delAirGroup(Groups, 0, airgroup);
                // System.out.println("AirGroup released 0");
            }
        }
        for (int i_9_ = 0; i_9_ < i_7_; i_9_++) {
            AirGroup airgroup = AirGroupList.getGroup(Groups[1], i_9_);
            if (airgroup != null && airgroup.nOfAirc == 0) {
                airgroup.release();
                AirGroupList.delAirGroup(Groups, 1, airgroup);
                // System.out.println("AirGroup released 1");
            }
        }
    }

    private void checkCollisionForAircraft() {
        List targets = Engine.targets();
        int targetSize = targets.size();
        for (int firstIndex = 0; firstIndex < targetSize; firstIndex++) {
            Actor firstActor = (Actor) targets.get(firstIndex);
            if (!(firstActor instanceof Aircraft)) continue;
            FlightModel firstFM = ((Aircraft) firstActor).FM;
            if (!(firstFM instanceof Maneuver)) continue;
            Maneuver firstManeuver = (Maneuver)firstFM;
            for (int secondIndex = firstIndex + 1; secondIndex < targetSize; secondIndex++) {
                Actor secondActor = (Actor) targets.get(secondIndex);
                if (!(secondActor instanceof Aircraft)) continue;
                FlightModel secondFM = ((Aircraft) secondActor).FM;
                if (!(secondFM instanceof Maneuver)) continue;
                Maneuver secondManeuver = (Maneuver)secondFM;
                float distSquared = (float) firstFM.Loc.distanceSquared(secondFM.Loc);
//                if (firstActor.name().equalsIgnoreCase("usa01013") || secondActor.name().equalsIgnoreCase("usa01013")) System.out.println("War checkCollisionForAircraft checking " + firstActor.name() + " and " + secondActor.name() + " : distance=" + Math.sqrt(distSquared));
                if (distSquared > 10000000F) continue;
                if (firstFM.actor.getArmy() != secondFM.actor.getArmy()) {
                    if (firstFM instanceof RealFlightModel) testAsDanger(firstFM, secondFM);
                    if (secondFM instanceof RealFlightModel) testAsDanger(secondFM, firstFM);
                }
                Ve.sub(firstFM.Loc, secondFM.Loc);
                float distance = (float) Ve.length();
                Ve.normalize();
                if (firstFM.actor.getArmy() == secondFM.actor.getArmy()) {
                    tmpV.set(Ve);
                    secondFM.Or.transformInv(tmpV);
                    if (tmpV.x > 0.0 && tmpV.y > -0.1 && tmpV.y < 0.1 && tmpV.z > -0.1 && tmpV.z < 0.1) secondManeuver.setShotAtFriend(distance);
                    tmpV.set(Ve);
                    tmpV.scale(-1.0);
                    firstFM.Or.transformInv(tmpV);
                    if (tmpV.x > 0.0 && tmpV.y > -0.1 && tmpV.y < 0.1 && tmpV.z > -0.1 && tmpV.z < 0.1) firstManeuver.setShotAtFriend(distance);
                }
                if (distSquared > 20000F) continue;
                float collisionRadius = (firstFM.actor.collisionR() + secondFM.actor.collisionR()) * 1.5F;
                distance -= collisionRadius;
                Vtarg.sub(secondFM.Vwld, firstFM.Vwld);
                Vtarg.scale(1.5);
                float closingSpeed = (float) Vtarg.length();
                if (closingSpeed < distance) {
//                    if (firstActor.name().equalsIgnoreCase("usa01013") || secondActor.name().equalsIgnoreCase("usa01013")) System.out.println("skipped checking " + firstActor.name() + " and " + secondActor.name() + " : closingSpeed=" + closingSpeed + ", distance=" + distance);
                    continue;
                }
                Vtarg.normalize();
                Vtarg.scale(distance);
                Ve.scale(Vtarg.dot(Ve));
                Vtarg.sub(Ve);
                if (Vtarg.length() > collisionRadius && distance > 0F) {
//                    if (firstActor.name().equalsIgnoreCase("usa01013") || secondActor.name().equalsIgnoreCase("usa01013")) System.out.println("skipped checking " + firstActor.name() + " and " + secondActor.name() + " : Vtarg.length()=" + Vtarg.length() + ", collisionRadius=" + collisionRadius + ", distance=" + distance);
                    continue;
                }
//                if (firstActor.name().equalsIgnoreCase("usa01013") || secondActor.name().equalsIgnoreCase("usa01013")) System.out.println("Checking setStrikeEmer for " + firstActor.name() + " and " + secondActor.name());
                firstManeuver.setStrikeEmer(secondFM);
                firstManeuver.setCheckStrike(true);
                secondManeuver.setStrikeEmer(firstFM);
                secondManeuver.setCheckStrike(true);
//                boolean needsSetStrikeEmer = true;
//                if (firstManeuver.getCheckStrike()) {
//                    FlightModel strikeTarg = firstManeuver.getStrikeTarg();
//                    if (firstFM.Loc.distanceSquared(secondFM.Loc) > firstFM.Loc.distanceSquared(strikeTarg.Loc)) {
//                        if (firstActor.name().equalsIgnoreCase("usa01013")) System.out.println("Overriding strike target for " + firstActor.name() + ", existing strike target=" + strikeTarg.actor.name() + ", distance=" + firstFM.Loc.distance(strikeTarg.Loc) + " overrides new target=" + secondActor.name() + ", distance=" + firstFM.Loc.distance(secondFM.Loc));
//                        needsSetStrikeEmer = false;
//                    }
//                }
//                if (needsSetStrikeEmer) {
//                    firstManeuver.setStrikeEmer(secondFM);
//                    firstManeuver.setCheckStrike(true);
//                }
//                needsSetStrikeEmer = true;
//                if (secondManeuver.getCheckStrike()) {
//                    FlightModel strikeTarg = secondManeuver.getStrikeTarg();
//                    if (secondFM.Loc.distanceSquared(firstFM.Loc) > secondFM.Loc.distanceSquared(strikeTarg.Loc)) {
//                        if (secondActor.name().equalsIgnoreCase("usa01013")) System.out.println("Overriding strike target for " + secondActor.name() + ", existing strike target=" + strikeTarg.actor.name() + ", distance=" + secondFM.Loc.distance(strikeTarg.Loc) + " overrides new target=" + firstActor.name() + ", distance=" + secondFM.Loc.distance(firstFM.Loc));
//                        needsSetStrikeEmer = false;
//                    }
//                }
//                if (needsSetStrikeEmer) {
//                    secondManeuver.setStrikeEmer(firstFM);
//                    secondManeuver.setCheckStrike(true);
//                }
            }
        }
    }

    public static boolean isValidDanger(FlightModel fm) {
        if (fm.actor instanceof TypeTransport) {
            if (AircraftState.isCheaterFightAces(fm.actor)) return true;
            for (int trigger = 0; trigger < 2; trigger++)
                if (fm.CT.Weapons.length > trigger) {
                    BulletEmitter be[] = fm.CT.Weapons[trigger];
                    if (be != null) for (int gunindex = 0; gunindex < be.length; gunindex++)
                        if (be[gunindex] instanceof Gun) if (((Gun) be[gunindex]).haveBullets()) return true;
                }
            return false;
        }
        return true;
    }

    public static void testAsDanger(FlightModel fm1, FlightModel fm2) {
//        System.out.println("testAsDanger(" + fm1.actor.getClass().getName() + ", " + fm2.actor.getClass().getName() + ")");
//        if (fm1.actor instanceof TypeTransport) {
        if (isValidDanger(fm1)) {
            Ve.sub(fm2.Loc, fm1.Loc);
            fm1.Or.transformInv(Ve);
            if (Ve.x > 0.0) {
                float f = (float) Ve.length();
                Ve.normalize();
                ((Maneuver) fm2).incDangerAggressiveness(4, (float) Ve.x, f, fm1);
            }
        }
    }

    public static Aircraft getNearestFriend(Aircraft aircraft) {
        return getNearestFriend(aircraft, 10000.0F);
    }

    public static Aircraft getNearestFriend(Aircraft aircraft, float f) {
        if (aircraft == null || aircraft.pos == null) return null;
        Point3d point3d = aircraft.pos.getAbsPoint();
        double d = f * f;
        int i = aircraft.getArmy();
        Aircraft retVal = null;
        List list = Engine.targets();
        int i_19_ = list.size();
        for (int i_20_ = 0; i_20_ < i_19_; i_20_++) {
            Actor actor = (Actor) list.get(i_20_);
            if (actor instanceof Aircraft && actor != aircraft && actor.getArmy() == i) {
                Point3d point3d_21_ = actor.pos.getAbsPoint();
                double d_22_ = (point3d.x - point3d_21_.x) * (point3d.x - point3d_21_.x) + (point3d.y - point3d_21_.y) * (point3d.y - point3d_21_.y) + (point3d.z - point3d_21_.z) * (point3d.z - point3d_21_.z);
                if (d_22_ < d) {
                    retVal = (Aircraft) actor;
                    d = d_22_;
                }
            }
        }
        return retVal;
    }

    public static Aircraft getNearestFriendAtPoint(Point3d pos, Aircraft aircraft, float distance) {
        if (aircraft == null) return null;
        double squareDistance = distance * distance;
        int aircraftArmy = aircraft.getArmy();
        Aircraft nearestFriend = null;
        List list = Engine.targets();
        int targetSize = list.size();
//        if (aircraft.aircIndex() == 0) {
//            System.out.println("War.getNearestFriendAtPoint(pos, " + aircraft.getClass().getName() + ", " + distance + "), squareDistance=" + squareDistance + ", aircraftArmy=" + aircraftArmy + ", targetSize=" + targetSize);
//            System.out.println("         pos=" + pos.x + ":" + pos.y + ":" + pos.z);
//            System.out.println("aircraft pos=" + aircraft.FM.Loc.x + ":" + aircraft.FM.Loc.y + ":" + aircraft.FM.Loc.z);
//        }
        for (int targetIndex = 0; targetIndex < targetSize; targetIndex++) {
            Actor target = (Actor) list.get(targetIndex);
//            if (aircraft.aircIndex() == 0) System.out.println("target " + targetIndex + "=" + target.getClass().getName() + ", army=" + target.getArmy());
            if (target instanceof Aircraft && target.getArmy() == aircraftArmy) {
                Point3d targetPos = target.pos.getAbsPoint();
//                if (aircraft.aircIndex() == 0) System.out.println("target pos=" + targetPos.x + ":" + targetPos.y + ":" + targetPos.z);
                double squareDistanceToTarget = (pos.x - targetPos.x) * (pos.x - targetPos.x) + (pos.y - targetPos.y) * (pos.y - targetPos.y) + (pos.z - targetPos.z) * (pos.z - targetPos.z);
//                if (aircraft.aircIndex() == 0) System.out.println("squareDistanceToTarget=" + squareDistanceToTarget + ", squareDistance=" + squareDistance);
                if (squareDistanceToTarget < squareDistance) {
                    nearestFriend = (Aircraft) target;
                    squareDistance = squareDistanceToTarget;
                }
            }
        }
//        if (aircraft.aircIndex() == 0) System.out.println("nearestFriend=" + (nearestFriend==null?"null":nearestFriend.getClass().getName()));
        return nearestFriend;
    }

    public static Aircraft getNearestFriendlyFighter(Aircraft aircraft, float maxDistance) {
        if (aircraft == null || aircraft.pos == null) return null;
        double maxDistanceSquared = maxDistance * maxDistance;
        Point3d aircraftPos = aircraft.pos.getAbsPoint();
        int army = aircraft.getArmy();
        Aircraft nearestFriendlyFighter = null;
        List targets = Engine.targets();
        int targetsSize = targets.size();
        for (int targetsIndex = 0; targetsIndex < targetsSize; targetsIndex++) {
            Actor curTarget = (Actor) targets.get(targetsIndex);
            if (curTarget instanceof Aircraft) {
                Aircraft curAircraft = (Aircraft) curTarget;
                if (curAircraft != aircraft && curAircraft.getArmy() == army && curAircraft.getWing() != aircraft.getWing() && curAircraft instanceof TypeFighter && !MistelTools.isDockedQueen(curAircraft)) {
                    Point3d curAircraftPos = curAircraft.pos.getAbsPoint();
                    double curAircraftDistanceSquared = (aircraftPos.x - curAircraftPos.x) * (aircraftPos.x - curAircraftPos.x) + (aircraftPos.y - curAircraftPos.y) * (aircraftPos.y - curAircraftPos.y) + (aircraftPos.z - curAircraftPos.z) * (aircraftPos.z - curAircraftPos.z);
                    if (curAircraftDistanceSquared < maxDistanceSquared) {
                        nearestFriendlyFighter = curAircraft;
                        maxDistanceSquared = curAircraftDistanceSquared;
                    }
                }
            }
        }
        return nearestFriendlyFighter;
    }

    // TODO: +++ TD AI code backport from 4.13 +++
    public static Aircraft getNearestEnemyOld(Aircraft aircraft, float f) {
        if (aircraft == null || aircraft.pos == null) return null;
        double d = f * f;
        Point3d point3d = aircraft.pos.getAbsPoint();
        int i = aircraft.getArmy();
        Aircraft aircraft_34_ = null;
        List list = Engine.targets();
        int i_35_ = list.size();
        for (int i_36_ = 0; i_36_ < i_35_; i_36_++) {
            Actor actor = (Actor) list.get(i_36_);
            if (actor instanceof Aircraft && actor.getArmy() != i) {
                Point3d point3d_37_ = actor.pos.getAbsPoint();
                double d_38_ = (point3d.x - point3d_37_.x) * (point3d.x - point3d_37_.x) + (point3d.y - point3d_37_.y) * (point3d.y - point3d_37_.y) + (point3d.z - point3d_37_.z) * (point3d.z - point3d_37_.z);
                if (d_38_ < d) {
                    aircraft_34_ = (Aircraft) actor;
                    d = d_38_;
                }
            }
        }
        return aircraft_34_;
    }

    public static Aircraft getNearestEnemy(Aircraft aircraft, float f) {
        if (aircraft == null || aircraft.pos == null) return null;
        double d = f * f;
        tmpPoint.set(aircraft.pos.getAbsPoint());
        int i = aircraft.getArmy();
        Aircraft aircraft1 = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor = (Actor) list.get(k);
            if (actor instanceof Aircraft && actor.getArmy() != i) {
                Point3d point3d = actor.pos.getAbsPoint();
                double d1 = (tmpPoint.x - point3d.x) * (tmpPoint.x - point3d.x) + (tmpPoint.y - point3d.y) * (tmpPoint.y - point3d.y) + (tmpPoint.z - point3d.z) * (tmpPoint.z - point3d.z);
                if (d1 < d) {
                    tmpPoint.z += 5D;
                    if (VisCheck.visCheck(tmpPoint, point3d, null, (Aircraft) actor)) {
                        aircraft1 = (Aircraft) actor;
                        d = d1;
                    }
                }
            }
        }

        return aircraft1;
    }

    // TODO: +++ Added by SAS~Storebror
    public static Aircraft getNearestEnemyBomber(Aircraft referenceAircraft, float maxDistance, int minEngines) {
        if (referenceAircraft == null || referenceAircraft.pos == null) return null;
        double squaredDistance = maxDistance * maxDistance;
        tmpPoint.set(referenceAircraft.pos.getAbsPoint());
        int army = referenceAircraft.getArmy();
        Aircraft foundBomber = null;
        List targets = Engine.targets();
        int targetsSize = targets.size();
        for (int targetsIndex = 0; targetsIndex < targetsSize; targetsIndex++) {
            Actor curTarget = (Actor) targets.get(targetsIndex);
            if ((curTarget instanceof TypeBomber || MistelTools.isDockedQueen(curTarget)) && curTarget.getArmy() != army) {
                if (((Aircraft)curTarget).FM.EI.engines.length < minEngines) continue;
                Point3d targetPos = curTarget.pos.getAbsPoint();
                double targetDistanceSquared = (tmpPoint.x - targetPos.x) * (tmpPoint.x - targetPos.x) + (tmpPoint.y - targetPos.y) * (tmpPoint.y - targetPos.y) + (tmpPoint.z - targetPos.z) * (tmpPoint.z - targetPos.z);
                if (targetDistanceSquared < squaredDistance) {
                    tmpPoint.z += 5D;
                    if (VisCheck.visCheck(tmpPoint, targetPos, null, (Aircraft) curTarget)) {
                        foundBomber = (Aircraft) curTarget;
                        squaredDistance = targetDistanceSquared;
                    }
                }
            }
        }
        return foundBomber;
    }
    // ---

    private static Point3d tmpPoint = new Point3d();
    // TODO: --- TD AI code backport from 4.13 ---

    public static Actor GetNearestEnemy(Actor actor, int i, float f) {
        if (actor == null || actor.pos == null) return null;
        return NearestTargets.getEnemy(0, i, actor.pos.getAbsPoint(), f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, int i, float f, int i_39_) {
        if (actor == null || actor.pos == null) return null;
        return NearestTargets.getEnemy(i_39_, i, actor.pos.getAbsPoint(), f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, int i, float f, Point3d point3d) {
        if (actor == null) return null;
        return NearestTargets.getEnemy(0, i, point3d, f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, int i, Point3d point3d, float f) {
        if (actor == null) return null;
        return NearestTargets.getEnemy(i, 16, point3d, f, actor.getArmy());
    }

    public static Actor GetNearestEnemy(Actor actor, Point3d point3d, float f) {
        if (actor == null) return null;
        return NearestTargets.getEnemy(0, 16, point3d, f, actor.getArmy());
    }

    // TODO: +++ TD AI code backport from 4.13 +++
    public static AirGroupList getEnemyGroups(Actor actor) {
        if (actor == null) return null;
        return actor.getArmy() != 1 ? Groups[0] : Groups[1];
    }

    public static AirGroupList getFriendlyGroups(Actor actor) {
        if (actor == null) return null;
        if (Groups.length < actor.getArmy()) return Groups[actor.getArmy()];
        else return null;
    }
    // TODO: --- TD AI code backport from 4.13 ---

    public static Actor GetNearestFromChief(Actor actor, Actor actor_40_) {
        if (actor == null) return null;
        if (!Actor.isAlive(actor_40_)) return null;
        Actor actor_41_ = null;
        if (actor_40_ instanceof Chief || actor_40_ instanceof Bridge) {
            int i = actor_40_.getOwnerAttachedCount();
            if (i < 1) return null;
            actor_41_ = (Actor) actor_40_.getOwnerAttached(0);
            double d = actor.pos.getAbsPoint().distance(actor_41_.pos.getAbsPoint());
            for (int i_42_ = 1; i_42_ < i; i_42_++) {
                Actor actor_43_ = (Actor) actor_40_.getOwnerAttached(i_42_);
                double d_44_ = actor.pos.getAbsPoint().distance(actor_43_.pos.getAbsPoint());
                if (d_44_ < d) {
                    d_44_ = d;
                    actor_41_ = actor_43_;
                }
            }
        }
        return actor_41_;
    }

    public static Actor GetRandomFromChief(Actor actor, Actor actor_45_) {
        if (actor == null) return null;
        if (!Actor.isAlive(actor_45_)) return null;
        if (actor_45_ instanceof Chief || actor_45_ instanceof Bridge) {
            int i = actor_45_.getOwnerAttachedCount();
            if (i < 1) return null;
            for (int i_46_ = 0; i_46_ < i; i_46_++) {
                Actor actor_47_ = (Actor) actor_45_.getOwnerAttached(World.Rnd().nextInt(0, i - 1));
                if (Actor.isValid(actor_47_) && actor_47_.isAlive()) return actor_47_;
            }
            for (int i_48_ = 0; i_48_ < i; i_48_++) {
                Actor actor_49_ = (Actor) actor_45_.getOwnerAttached(i_48_);
                if (Actor.isValid(actor_49_) && actor_49_.isAlive()) return actor_49_;
            }
        }
        return actor_45_;
    }

    public static Aircraft GetNearestEnemyAircraft(Actor actor, float f, int i) {
        if (actor == null) return null;
        Actor actor_50_ = GetNearestEnemy(actor, -1, f, i);
        if (actor_50_ != null) return (Aircraft) actor_50_;
        actor_50_ = GetNearestEnemy(actor, -1, f, 9);
        return (Aircraft) actor_50_;
    }

    // TODO: +++ TD AI code backport from 4.13 +++
    public static Actor getNearestAnyFriendAtPoint(Point3d point3d, Aircraft aircraft, float f) {
        if (aircraft == null) return null;
        double d = f * f;
        int i = aircraft.getArmy();
        Actor actor = null;
        List list = Engine.targets();
        int j = list.size();
        for (int k = 0; k < j; k++) {
            Actor actor1 = (Actor) list.get(k);
            if (actor1.getArmy() == i) {
                Point3d point3d1 = actor1.pos.getAbsPoint();
                double d1 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
                if (d1 < d) {
                    actor = actor1;
                    d = d1;
                }
            }
        }

        return actor;
    }
    // TODO: --- TD AI code backport from 4.13 ---

}