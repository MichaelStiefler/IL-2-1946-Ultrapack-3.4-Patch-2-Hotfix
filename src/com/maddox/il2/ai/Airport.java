package com.maddox.il2.ai;

import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Point_Runaway;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgDreamListener;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeAmphibiousPlane;
import com.maddox.il2.objects.air.TypeSailPlane;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

public abstract class Airport extends Actor
    implements MsgDreamListener
{
    class Interpolater extends Interpolate
    {

        public boolean tick()
        {
            update();
            return true;
        }

        Interpolater()
        {
        }
    }


    public static Airport nearest(Point3f point3f, int i, int j)
    {
        pd.set(point3f.x, point3f.y, point3f.z);
        return nearest(pd, i, j);
    }

    public static Airport nearest(Point3d point3d, int i, int j)
    {
        Airport airport = null;
        double d = 0.0D;
        pd.set(point3d.x, point3d.y, point3d.z);
        int k = World.cur().airports.size();
        for(int l = 0; l < k; l++)
        {
            Airport airport1 = (Airport)World.cur().airports.get(l);
            if(((j & TYPE_GROUND) == 0 || !(airport1 instanceof AirportGround)) && ((j & TYPE_MARITIME) == 0 || !(airport1 instanceof AirportMaritime)) && ((j & TYPE_CARRIER) == 0 || !(airport1 instanceof AirportCarrier)) || !Actor.isAlive(airport1))
                continue;
            if(i >= 0)
            {
                int i1 = airport1.getArmy();
                if(i1 != 0 && i1 != i)
                    continue;
            }
            pd.z = airport1.pos.getAbsPoint().z;
            double d1 = pd.distanceSquared(airport1.pos.getAbsPoint());
            if(airport == null || d1 < d)
            {
                airport = airport1;
                d = d1;
            }
        }

        if(d > 225000000D)
            airport = null;
        return airport;
    }

    public Airport()
    {
        takeoffRequest = 0;
        landingRequest = 0;
        flags |= 0x200;
        World.cur().airports.add(this);
    }

    public static double distToNearestAirport(Point3d point3d)
    {
        return distToNearestAirport(point3d, -1, TYPE_ANY);
    }

    public static double distToNearestAirport(Point3d point3d, int i, int j)
    {
        Airport airport = nearest(point3d, i, j);
        if(airport == null)
            return 225000000D;
        else
            return airport.pos.getAbsPoint().distance(point3d);
    }

    public static Airport makeLandWay(FlightModel flightmodel)
    {
        flightmodel.AP.way.curr().getP(PlLoc);
        int i = 0;
        Airport airport = null;
        int j = flightmodel.actor.getArmy();
        if(flightmodel.actor instanceof TypeSailPlane)
        {
            i = TYPE_MARITIME;
            airport = nearest(PlLoc, j, i);
        } else
        if(flightmodel.AP.way.isLandingOnShip())
        {
            i = TYPE_CARRIER;
            airport = nearest(PlLoc, j, i);
            if(!Actor.isAlive(airport))
            {
                i = TYPE_GROUND;
                airport = nearest(PlLoc, j, i);
            }
        } else
        {
            i = TYPE_GROUND | TYPE_MARITIME;
            if(!(flightmodel.actor instanceof TypeAmphibiousPlane))
                i &= -(TYPE_GROUND | TYPE_MARITIME);
            airport = nearest(PlLoc, j, i);
            if(!Actor.isAlive(airport))
            {
                i = TYPE_CARRIER;
                airport = nearest(PlLoc, j, i);
            }
        }
        Aircraft.debugprintln(flightmodel.actor, "Searching a place to land - Selecting RWY Type " + i);
        if(Actor.isAlive(airport))
        {
            if(airport.landWay(flightmodel))
            {
                flightmodel.AP.way.landingAirport = airport;
                return airport;
            } else
            {
                return null;
            }
        } else
        {
            return null;
        }
    }

    public boolean landWay(FlightModel flightmodel)
    {
        return false;
    }

    public void rebuildLandWay(FlightModel flightmodel)
    {
    }

    public void rebuildLastPoint(FlightModel flightmodel)
    {
    }

    public double ShiftFromLine(FlightModel flightmodel)
    {
        return 0.0D;
    }

    public int landingFeedback(Point3d point3d, Aircraft aircraft)
    {
        if(aircraft.FM.CT.GearControl > 0.0F)
            return 0;
        if(landingRequest > 0)
            return 1;
        double maxDistanceSquared = 640000D;
        List targets = Engine.targets();
        int targetSize = targets.size();
        for(int targetIndex = 0; targetIndex < targetSize; targetIndex++)
        {
            Actor curTarget = (Actor)targets.get(targetIndex);
            if(!(curTarget instanceof Aircraft) || curTarget == aircraft)
                continue;
            Aircraft curAircaft = (Aircraft)curTarget;
            Point3d curAircraftPos = curAircaft.pos.getAbsPoint();
            double curAircraftDistanceSquared = (point3d.x - curAircraftPos.x) * (point3d.x - curAircraftPos.x) + (point3d.y - curAircraftPos.y) * (point3d.y - curAircraftPos.y) + (point3d.z - curAircraftPos.z) * (point3d.z - curAircraftPos.z);
            if(curAircraftDistanceSquared >= maxDistanceSquared)
                continue;
            if(((Maneuver)curAircaft.FM).get_maneuver() == Maneuver.LANDING)
            {
                if(((Maneuver)curAircaft.FM).wayCurPos instanceof Point_Runaway)
                    return aircraft.FM.AP.way.isLanding2()?LANDING_PERMITTED:LANDING_WAVEOFF;
                if(curAircaft.FM.AP.way.isLanding() && curAircaft.FM.AP.way.Cur() > 5)
                    return aircraft.FM.AP.way.isLanding2()?LANDING_PERMITTED:LANDING_DENIED;
            }
            if(((Maneuver)curAircaft.FM).get_maneuver() == Maneuver.TAKEOFF || ((Maneuver)curAircaft.FM).get_maneuver() == Maneuver.PARKED_STARTUP)
                return LANDING_WAVEOFF;
        }

        landingRequest = aircraft.FM.AP.way.isLanding2()?0:2000;
        return LANDING_PERMITTED;
    }

    public abstract boolean nearestRunway(Point3d point3d, Loc loc);

    public abstract void setTakeoff(Point3d point3d, Aircraft aaircraft[]);

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    protected void createActorHashCode()
    {
        makeActorRealHashCode();
    }

    protected void update()
    {
        if(takeoffRequest > 0)
            takeoffRequest--;
        if(landingRequest > 0)
            landingRequest--;
    }

    public void msgDream(boolean flag)
    {
        if(flag)
        {
            if(interpGet("AirportTicker") == null)
                interpPut(new Interpolater(), "AirportTicker", Time.current(), null);
        } else
        {
            interpEnd("AirportTicker");
        }
    }

    public static final int LANDING_PERMITTED = 0;
    public static final int LANDING_DENIED = 1;
    public static final int LANDING_WAVEOFF = 2;
    public static final int TYPE_ANY = 7;
    public static final int TYPE_GROUND = 1;
    public static final int TYPE_MARITIME = 2;
    public static final int TYPE_CARRIER = 4;
    private static Point3f PlLoc = new Point3f();
    public int takeoffRequest;
    public int landingRequest;
    private static Point3d pd = new Point3d();

}
