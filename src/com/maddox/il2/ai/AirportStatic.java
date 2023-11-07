package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.rts.Time;

public abstract class AirportStatic extends Airport
{
    private static class Runway
    {

        public Loc loc;
        public float planeShift[];
        public Aircraft planes[];
        public int curPlaneShift;
        public int oldTickCounter;

        public Runway(Loc loc)
        {
            this.loc = new Loc();
            planeShift = new float[32];
            planes = new Aircraft[32];
            curPlaneShift = 0;
            oldTickCounter = 0;
            this.loc.set(loc.getX(), loc.getY(), World.land().HQ(loc.getX(), loc.getY()), loc.getAzimut(), 0.0F, 0.0F);
        }
    }


    public AirportStatic()
    {
        runway = new ArrayList();
    }

    public static void make(ArrayList arraylist, Point2f point2fs[][], Point2f point2fs_0_[][], Point2f point2fs_1_[][])
    {
        if(arraylist != null)
        {
            ArrayList arraylist_2_ = new ArrayList();
            while(arraylist.size() > 0) 
            {
                Loc loc = (Loc)arraylist.remove(0);
                boolean bool = false;
                AirportStatic airportstatic = null;
                for(int i = 0; i < arraylist_2_.size(); i++)
                {
                    airportstatic = (AirportStatic)arraylist_2_.get(i);
                    if(airportstatic.oppositeRunway(loc) == null)
                        continue;
                    bool = true;
                    break;
                }

                if(bool)
                {
                    airportstatic.runway.add(new Runway(loc));
                    int i = airportstatic.runway.size();
                    p3d.set(0.0D, 0.0D, 0.0D);
                    for(int i_3_ = 0; i_3_ < i; i_3_++)
                    {
                        loc = ((Runway)airportstatic.runway.get(i_3_)).loc;
                        p3d.x += loc.getPoint().x;
                        p3d.y += loc.getPoint().y;
                        p3d.z += loc.getPoint().z;
                    }

                    p3d.x /= i;
                    p3d.y /= i;
                    p3d.z /= i;
                    ((Actor) (airportstatic)).pos.setAbs(p3d);
                } else
                {
                    if(Engine.cur.land.isWater(loc.getPoint().x, loc.getPoint().y))
                        airportstatic = new AirportMaritime();
                    else
                        airportstatic = new AirportGround();
                    airportstatic.pos = new ActorPosStatic(airportstatic, loc);
                    airportstatic.runway.add(new Runway(loc));
                    arraylist_2_.add(airportstatic);
                }
            }
        }
    }

    public boolean landWay(FlightModel flightmodel)
    {
        flightmodel.AP.way.curr().getP(pWay);
        Runway runway = nearestRunway(pWay);
        if(runway == null)
            return false;
        Way way = new Way();
        float f = (float)Engine.land().HQ_Air(runway.loc.getX(), runway.loc.getY());
        float f_4_ = flightmodel.M.massEmpty / 3000F;
        if(f_4_ > 1.0F)
            f_4_ = 1.0F;
        if(flightmodel.EI.engines[0].getType() > 1)
            f_4_ = 1.0F;
        if(flightmodel.EI.engines[0].getType() == 3)
            f_4_ = 1.5F;
        float f_5_ = f_4_;
        if(f_5_ > 1.0F)
            f_5_ = 1.0F;
        if(flightmodel.actor instanceof TypeFastJet)
            f_4_ = 3F;
        for(int i = x.length - 1; i >= 0; i--)
        {
            WayPoint waypoint = new WayPoint();
            pd.set(x[i] * f_4_, y[i] * f_4_, z[i] * f_5_);
            waypoint.set(Math.min(v[i] * 0.278F, flightmodel.Vmax * 0.7F));
            waypoint.Action = 2;
            runway.loc.transform(pd);
            float f_6_ = (float)Engine.land().HQ_Air(pd.x, pd.y);
            pd.z -= f_6_ - f;
            pf.set(pd);
            waypoint.set(pf);
            way.add(waypoint);
        }

        way.setLanding(true);
        if(flightmodel.AP.way.curr().waypointType == WayPoint.WP_LANDING_STRAIGHTIN)
            way.setLanding2(true);
        flightmodel.AP.way = way;
        return true;
    }

    public void setTakeoff(Point3d point3d, Aircraft aircrafts[])
    {
        Runway runway = nearestRunway(point3d);
        if(runway != null)
        {
            Runway oppositeRunway = oppositeRunway(runway.loc);
            double distance = 1000D;
            if(oppositeRunway != null)
                distance = runway.loc.getPoint().distance(oppositeRunway.loc.getPoint());
            if(Time.tickCounter() != runway.oldTickCounter)
            {
                runway.oldTickCounter = Time.tickCounter();
                runway.curPlaneShift = 0;
            }
            for(int aircraftIndex = 0; aircraftIndex < aircrafts.length; aircraftIndex++)
                if(Actor.isValid(aircrafts[aircraftIndex]))
                {
                    float f = aircrafts[aircraftIndex].collisionR() * 2.0F + 20F;
                    for(int runwayPlaneShiftIndex = runway.curPlaneShift; runwayPlaneShiftIndex > 0; runwayPlaneShiftIndex--)
                    {
                        runway.planeShift[runwayPlaneShiftIndex] = runway.planeShift[runwayPlaneShiftIndex - 1] + f;
                        runway.planes[runwayPlaneShiftIndex] = runway.planes[runwayPlaneShiftIndex - 1];
                    }

                    runway.planeShift[0] = 0.0F;
                    runway.planes[0] = aircrafts[aircraftIndex];
                    runway.curPlaneShift++;
                    if(runway.curPlaneShift > 31)
                        throw new RuntimeException("Too many planes on airdrome");
                    for(int runwayPlaneIndex = 0; runwayPlaneIndex < runway.curPlaneShift; runwayPlaneIndex++)
                        if(Actor.isValid(runway.planes[runwayPlaneIndex]))
                        {
                            tmpLoc.set((double)runway.planeShift[runwayPlaneIndex] - distance, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                            tmpLoc.add(runway.loc);
                            Point3d runwayPoint = tmpLoc.getPoint();
                            Orient orient = tmpLoc.getOrient();
                            runwayPoint.z = World.land().HQ(runwayPoint.x, runwayPoint.y) + (double)runway.planes[runwayPlaneIndex].FM.Gears.H;
                            Engine.land().N(runwayPoint.x, runwayPoint.y, v1);
                            orient.orient(v1);
                            orient.increment(0.0F, runway.planes[runwayPlaneIndex].FM.Gears.Pitch, 0.0F);
                            runway.planes[runwayPlaneIndex].setOnGround(runwayPoint, orient, zeroSpeed);
                            if(runway.planes[runwayPlaneIndex].FM instanceof Maneuver)
                            {
                                ((Maneuver)(runway.planes[runwayPlaneIndex].FM)).direction = runway.planes[runwayPlaneIndex].pos.getAbsOrient().getAzimut();
                                ((Maneuver)(runway.planes[runwayPlaneIndex].FM)).rwLoc = runway.loc;
                            }
                            runway.planes[runwayPlaneIndex].FM.AP.way.takeoffAirport = this;
                        }

                }

            if(Actor.isValid(aircrafts[0]) && aircrafts[0].FM instanceof Maneuver)
            {
                Maneuver maneuver = (Maneuver)aircrafts[0].FM;
                if(maneuver.Group != null && maneuver.Group.w != null)
                    maneuver.Group.w.takeoffAirport = this;
            }
        }
    }

    public double ShiftFromLine(FlightModel flightmodel)
    {
        tmpLoc.set(flightmodel.Loc);
        if(flightmodel instanceof Maneuver)
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            if(maneuver.rwLoc != null)
            {
                tmpLoc.sub(maneuver.rwLoc);
                return tmpLoc.getY();
            }
        }
        return 0.0D;
    }

    public boolean nearestRunway(Point3d point3d, Loc loc)
    {
        Runway runway = nearestRunway(point3d);
        if(runway != null)
        {
            loc.set(runway.loc);
            return true;
        } else
        {
            return false;
        }
    }

    private Runway nearestRunway(Point3d pos)
    {
        Runway runway = null;
        double d = 0.0D;
        np.set(pos);
        int i = this.runway.size();
        for(int runwayIndex = 0; runwayIndex < i; runwayIndex++)
        {
            Runway curRunway = (Runway)this.runway.get(runwayIndex);
            np.z = curRunway.loc.getPoint().z;
            double distanceSquared = curRunway.loc.getPoint().distanceSquared(np);
            if(runway == null || distanceSquared < d)
            {
                runway = curRunway;
                d = distanceSquared;
            }
        }

        if(d > 225000000D)
            runway = null;
        return runway;
    }

    private Runway oppositeRunway(Loc loc)
    {
        int i = this.runway.size();
        for(int runwayIndex = 0; runwayIndex < i; runwayIndex++)
        {
            Runway runway = (Runway)this.runway.get(runwayIndex);
            pcur.set(runway.loc.getPoint());
            loc.transformInv(pcur);
            if(Math.abs(pcur.y) < 15D && pcur.x < -800D && pcur.x > -2500D)
            {
                p1.set(1.0D, 0.0D, 0.0D);
                p2.set(1.0D, 0.0D, 0.0D);
                runway.loc.getOrient().transform(p1);
                loc.getOrient().transform(p2);
                if(p1.dot(p2) < -0.9D)
                    return runway;
            }
        }

        return null;
    }

    private ArrayList runway;
    public static final int PT_RUNWAY = 1;
    public static final int PT_TAXI = 2;
    public static final int PT_STAY = 4;
    private static Point3d p3d = new Point3d();
    private static float x[] = {
        -500F, 0.0F, 220F, 2000F, 4000F, 5000F, 4000F, 0.0F, 0.0F
    };
    private static float y[] = {
        0.0F, 0.0F, 0.0F, 0.0F, -500F, -2000F, -4000F, -4000F, -4000F
    };
    private static float z[] = {
        0.0F, 6F, 20F, 160F, 500F, 600F, 700F, 700F, 700F
    };
    private static float v[] = {
        0.0F, 180F, 220F, 240F, 270F, 280F, 300F, 300F, 300F
    };
    private static Point3d pWay = new Point3d();
    private static Point3d pd = new Point3d();
    private static Point3f pf = new Point3f();
    private static Vector3d v1 = new Vector3d();
    private static Vector3d zeroSpeed = new Vector3d();
    private static Loc tmpLoc = new Loc();
    private static Point3d pcur = new Point3d();
    private static Point3d np = new Point3d();
    private static Vector3d p1 = new Vector3d();
    private static Vector3d p2 = new Vector3d();

}
