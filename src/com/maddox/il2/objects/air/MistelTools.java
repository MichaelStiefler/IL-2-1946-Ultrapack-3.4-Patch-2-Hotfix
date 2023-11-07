package com.maddox.il2.objects.air;

public abstract class MistelTools {
    public static boolean isDockedQueen(Object o) {
        if (!(o instanceof Mistel)) return false;
        if (!(o instanceof TypeDockable)) return false;
        TypeDockable d = (TypeDockable)o;
        return d.typeDockableIsDocked();
    }
    
//    public static void checkDetach(Aircraft aircraft, Shot shot) {
//        if (!(aircraft instanceof Mistel)) return;
//        TypeDockable dockable = (TypeDockable)aircraft;
//        if (!dockable.typeDockableIsDocked()) return;
//        Mistel mistel = (Mistel)aircraft;
//        if (mistel.getQueen() == World.getPlayerAircraft()) return;
//        if (!(shot.initiator instanceof Aircraft)) return;
//        if (shot.initiator.getArmy() == aircraft.getArmy()) return;
//        if (War.getNearestFriendlyFighter(aircraft, 3000F) != null) return;
//        mistel.setBombChargeArmed(false);
//        dockable.typeDockableAttemptDetach();
//        Aircraft queen = mistel.getQueen();
//        if (queen.FM instanceof Pilot) {
//            Pilot p = (Pilot)queen.FM;
//            p.danger = ((Aircraft)shot.initiator).FM;
//            p.set_task(Maneuver.DEFENCE);
//            p.setManeuverByTask();
//        }
//    }
}
