/*4.10.1 class*/
package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.AirGroupList;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.MsgOwnerListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.SectFile;

public class Wing extends Chief implements MsgOwnerListener {
    // TODO: +++ TD AI code backport from 4.13 +++
    public Aircraft[]       airc;
    public Way              way;
    // TODO: --- TD AI code backport from 4.13 ---
    private static Vector3d tmpSpeed  = new Vector3d();
    private static Point3d  tmpPoint0 = new Point3d();
    private static Point3d  tmpPoint1 = new Point3d();
    private static Vector3d zeroSpeed = new Vector3d();
    private static Point3d  pGround   = new Point3d();
    private static Orient   oGround   = new Orient();
    private static Vector3d vGround   = new Vector3d();
    // TODO: +++ TD AI code backport from 4.13 +++
    public boolean          bOnlyAI;
    public boolean          bCoopSpawnStationary;

    public Wing() {
        this.airc = new Aircraft[4];
        this.way = new Way();
        this.bOnlyAI = false;
        this.bCoopSpawnStationary = false;
    }
    // TODO: --- TD AI code backport from 4.13 ---

    public int aircReady() {
        int i = 0;
        for (int i_0_ = 0; i_0_ < this.airc.length; i_0_++)
            if (Actor.isValid(this.airc[i_0_])) i++;
        return i;
    }

    public int aircIndex(Aircraft aircraft) {
        String string = aircraft.name();
        char c = string.charAt(string.length() - 1);
        return c - '0';
    }

    public Regiment regiment() {
        return this.squadron().regiment();
    }

    public Squadron squadron() {
        return (Squadron) this.getOwner();
    }

    public int indexInSquadron() {
        char c = this.name().charAt(this.name().length() - 1);
        return c - '0';
    }

    public void msgOwnerDied(Actor actor) {
        if (!this.getDiedFlag()) {
            for (int i = 0; i < this.airc.length; i++)
                if (Actor.isValid(this.airc[i]) && this.airc[i].isAlive()) return;
            World.onActorDied(this, null);
        }
    }

    public void msgOwnerTaskComplete(Actor actor) {
        for (int i = 0; i < this.airc.length; i++)
            if (Actor.isValid(this.airc[i]) && !this.airc[i].isTaskComplete()) return;
        World.onTaskComplete(this);
    }

    public void msgOwnerAttach(Actor actor) {
    }

    public void msgOwnerDetach(Actor actor) {
    }

    public void msgOwnerChange(Actor actor, Actor actor_1_) {
    }

    public void destroy() {
        for (int i = 0; i < this.airc.length; i++)
            this.airc[i] = null;
        super.destroy();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    // TODO: Added by SAS~Storebror for Trigger online replication compatibility
    public void load(SectFile sectfile, String string, NetChannel netchannel) throws Exception {
        this.load(sectfile, string, netchannel, false);
    }

    public void load(SectFile missionFile, String wingName, NetChannel netchannel, boolean loadFromTrigger) throws Exception {
    // ---
        if (missionFile.sectionIndex(wingName) < 0) {
            this.destroy();
            throw new Exception("Mission: section '" + wingName + "' not found");
        }
        String aircraftClassName = missionFile.get(wingName, "Class", (String) null);
        if (aircraftClassName == null) {
            this.destroy();
            throw new Exception("Mission: in section '" + wingName + "' class aircraft not defined");
        }
        String wayName = wingName + "_Way";
        if (missionFile.sectionIndex(wayName) < 0) {
            this.destroy();
            throw new Exception("Mission: section '" + wayName + "' not found");
        }
        if (Actor.getByName(wingName) != null) {
            this.destroy();
            throw new Exception("Mission: duplicate wing '" + wingName + "'");
        }
        try {
            this.setName(wingName);
            Squadron squadron = null;

            // TODO: Added by |ZUTI|: loading squadron might fail because of a mess with custom country and regiments. If that happens, load NoNe as the default one!
            // -------------------------------------------------------------------------
            try {
                squadron = Squadron.New(wingName.substring(0, wingName.length() - 1));
            } catch (Exception ex) {
                System.out.println("Loading of squadron failed: " + ex.getMessage());
                System.out.println("Trying to load default one...");
                try {
                    squadron = Squadron.New("NoNe");
                } catch (Exception ex1) {
                    System.out.println("Failed to load default squadron: " + ex1.getMessage());
                    System.out.println("Aborting to load current wing!");
                    this.destroy();
                    return;
                }
            }
            // -------------------------------------------------------------------------

            this.setOwner(squadron);
            squadron.wing[this.indexInSquadron()] = this;
            this.setArmy(squadron.getArmy());
            // TODO: +++ TD AI code backport from 4.13 +++
            this.bOnlyAI = missionFile.get(wingName, "OnlyAI", 0, 0, 1) == 1;
            // TODO: --- TD AI code backport from 4.13 ---
            NetAircraft.loadingCountry = squadron.regiment().country();
            int numPlanes = missionFile.get(wingName, "Planes", 1, 1, 4);

            // TODO: Added by |ZUTI|: check if wing is AI only or not
            // --------------------------------------------------------
            boolean isMulticrew = false;
            if (missionFile.get(wingName, "OnlyAI", 0, 0, 1) == 0) isMulticrew = true;

            this.way.load(missionFile, wayName);
            for (int planeIndex = 0; planeIndex < numPlanes; planeIndex++) {
                // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
//                this.airc[i_4_] = Mission.cur().loadAir(sectfile, string_2_, string, string + i_4_, i_4_);
                this.airc[planeIndex] = Mission.cur().loadAir(missionFile, aircraftClassName, wingName, wingName + planeIndex, planeIndex, loadFromTrigger);
                // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
                this.airc[planeIndex].setArmy(this.getArmy());
                this.airc[planeIndex].checkTurretSkill();
                this.airc[planeIndex].setOwner(this);
                if (this.airc[planeIndex] == World.getPlayerAircraft()) World.setPlayerRegiment();

                // TODO: Added by |ZUTI|
                // ----------------------------------
                this.airc[planeIndex].FM.AS.zutiSetMultiCrew(isMulticrew);
                // ----------------------------------

                this.airc[planeIndex].preparePaintScheme();
                this.airc[planeIndex].prepareCamouflage();
                this.setPosAndSpeed(this.airc[planeIndex], this.way);
                // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
                if(World.cur().triggersGuard.getListTriggerAircraftActivate().contains(wingName) && (airc[planeIndex].FM instanceof Maneuver))
                    ((Maneuver)airc[planeIndex].FM).triggerTakeOff = false;
                // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---
            }
            this.setArmy(this.airc[0].getArmy());
//            Formation.generate(this.airc); // TODO: Changed by SAS~Storebror, Take formation type into account!
            Formation.generate(this.airc, (byte)this.way.first().formation);
            for (int i_5_ = 0; i_5_ < numPlanes; i_5_++)
                this.airc[i_5_].FM.AP.way = new Way(this.way);
            // TODO: Edited by |ZUTI|
            // if ((Mission.isSingle() || Mission.isCoop() && Mission.isServer()) && !NetMissionTrack.isPlaying())
            if ((Mission.isSingle() || Mission.isDogfight() || Mission.isCoop() && Mission.isServer()) && !NetMissionTrack.isPlaying()) {
                AirGroup airgroup = new AirGroup(this.airc[0].getSquadron(), this.way);
                for (int aircraftIndex = 0; aircraftIndex < numPlanes; aircraftIndex++)
                    airgroup.addAircraft(this.airc[aircraftIndex]);

                AirGroupList.addAirGroup(War.Groups, this.airc[0].getArmy() - 1 & 0x1, airgroup);
            }
            NetAircraft.loadingCountry = null;
        } catch (Exception exception) {
            NetAircraft.loadingCountry = null;
            this.destroy();
            throw exception;
        }
        // TODO: +++ TD AI code backport from 4.13 +++
        if (this.way.first().waypointType == 4 || this.way.first().waypointType == 5) this.bCoopSpawnStationary = true;
        if (this.bOnlyAI) this.bCoopSpawnStationary = true;
        // TODO: --- TD AI code backport from 4.13 ---
    }

    private void setPosAndSpeed(Aircraft aircraft, Way way) {
//        if (setSingleWaypointSpawnDirection(aircraft, way)) return;
//        System.out.println("setPosAndSpeed for " + aircraft.name() + ", way size=" + this.way.size());
        if (way.size() == 1) {
            WayPoint spawnWaypoint = way.get(0);
            spawnWaypoint.getP(tmpPoint0);
            aircraft.pos.setAbs(tmpPoint0);
            tmpSpeed.set(spawnWaypoint.Speed, 0.0, 0.0);
            aircraft.setSpeed(tmpSpeed);
        } else {
            WayPoint spawnWaypoint = way.get(0);
            WayPoint firstFlightWaypoint = way.get(1);
            spawnWaypoint.getP(tmpPoint0);
            firstFlightWaypoint.getP(tmpPoint1);
            tmpSpeed.sub(tmpPoint1, tmpPoint0);
            tmpSpeed.normalize();
            Actor._tmpOrient.setAT0(tmpSpeed);
            tmpSpeed.scale(spawnWaypoint.Speed);
            aircraft.pos.setAbs(tmpPoint0, Actor._tmpOrient);
            aircraft.setSpeed(tmpSpeed);
        }
        aircraft.pos.reset();
    }
    
    // TODO: By SAS~Storebror: +++ Set initial Spawn position, orientation and speed for Squads +++
    public void setInitialSpawnParamsForSquad() {
        for (int aircraftIndex=0; aircraftIndex < this.airc.length; aircraftIndex++) {
            if (!Actor.isValid(this.airc[aircraftIndex])) continue;
//            System.out.println("setInitialSpawnParamsForSquad for " + this.airc[aircraftIndex].name() + ", way size=" + this.way.size());
            if (this.setFirstWaypointSpawnParams(this.airc[aircraftIndex], this.way))
                Formation.generate(this.airc, (byte)this.way.first().formation);
        }
    }
    
    private boolean setFirstWaypointSpawnParams(Aircraft aircraft, Way way) {
        if (way.size() < 1) return false;
        WayPoint spawnWaypoint = way.get(0);
        if (!(spawnWaypoint.getTarget() instanceof Wing)) return false;
        Wing targetWing = (Wing)spawnWaypoint.getTarget();
        if (targetWing.way == null) return false;
        if (spawnWaypoint.targetWaypointIndex < 0) return false;
        if (spawnWaypoint.targetWaypointIndex >= targetWing.way.size() - 1) return false;
        if (targetWing.airc == null || targetWing.airc.length < 1) return false;
        if (aircraft instanceof TypeFighter) {
            if(!(targetWing.airc[0] instanceof TypeFighter)) return false;
            if (!(aircraft.FM instanceof Maneuver) || !(targetWing.airc[0].FM instanceof Maneuver)) return false;
            Maneuver maneuver1 = (Maneuver)aircraft.FM;
            Maneuver maneuver2 = (Maneuver)targetWing.airc[0].FM;
            if (!maneuver1.hasBombs() && !maneuver1.hasGroundAttackRockets() && (maneuver2.hasBombs() || maneuver2.hasGroundAttackRockets())) return false;
        }
        
//        System.out.println("setInitialSpawnParamsForSquad for " + aircraft.name() + ", way size=" + this.way.size());
        
        WayPoint targetWingJoinWaypoint = targetWing.way.get(spawnWaypoint.targetWaypointIndex);
        WayPoint targetWingNextWaypointAfterJoin = targetWing.way.get(spawnWaypoint.targetWaypointIndex + 1);
        targetWingJoinWaypoint.getP(tmpPoint0);
        targetWingNextWaypointAfterJoin.getP(tmpPoint1);
        tmpSpeed.sub(tmpPoint1, tmpPoint0);
        tmpSpeed.normalize();
        Actor._tmpOrient.setAT0(tmpSpeed);
        tmpSpeed.scale(targetWingJoinWaypoint.Speed);
        
        Formation.leaderOffset(aircraft.FM, (byte)targetWingJoinWaypoint.formation, aircraft.FM.Offset);
        aircraft.FM.Offset.z *= -1;
        Actor._tmpOrient.transformInv(aircraft.FM.Offset);
        
        targetWingJoinWaypoint.getP(tmpPoint0);
        tmpPoint0.add(aircraft.FM.Offset);

        aircraft.pos.setAbs(tmpPoint0, Actor._tmpOrient);
        aircraft.setSpeed(tmpSpeed);
        return true;
    }

    //                         --- Set initial Spawn position, orientation and speed for Squads ---

    public void setOnAirport() {
        WayPoint waypoint = this.way.get(0);
        waypoint.getP(tmpPoint0);
        if (waypoint.Action == 1) {
            boolean bool = false;
            if (waypoint.sTarget != null) {
                Actor actor = waypoint.getTarget();
                if (actor != null && actor instanceof BigshipGeneric) {
                    AirportCarrier airportcarrier = ((BigshipGeneric) actor).getAirport();
                    if (Actor.isValid(airportcarrier)) {
                        airportcarrier.setTakeoff(tmpPoint0, this.airc);
                        bool = true;
                    }
                }
            }
            if (!bool) {
                Airport airport = Airport.nearest(tmpPoint0, this.getArmy(), 3);
                if (airport != null) {
                    double d = airport.pos.getAbsPoint().distance(tmpPoint0);
                    if (d < 1250.0) airport.setTakeoff(tmpPoint0, this.airc);
                    else this.setonground();
                } else this.setonground();
            }
        } else for (int i = 0; i < 4; i++)
            if (Actor.isValid(this.airc[i])) {
                if (this.airc[i] == World.getPlayerAircraft()) {
                    this.airc[i].FM.EI.setCurControlAll(true);
                    this.airc[i].FM.EI.setEngineRunning();
                    this.airc[i].FM.CT.setPowerControl(0.75F);
                }
                this.airc[i].FM.setStationedOnGround(false);
                this.airc[i].FM.setWasAirborne(true);
            }
    }

    private void setonground() {
        for (int i = 0; i < this.airc.length; i++)
            if (this.airc[i] != null) {
                this.airc[i].pos.getAbs(pGround, oGround);
                pGround.z = World.land().HQ(pGround.x, pGround.y) + this.airc[i].FM.Gears.H;
                Engine.land().N(pGround.x, pGround.y, vGround);
                oGround.orient(vGround);
                oGround.increment(0.0F, this.airc[i].FM.Gears.Pitch, 0.0F);
                this.airc[i].setOnGround(pGround, oGround, zeroSpeed);
            }
    }
}