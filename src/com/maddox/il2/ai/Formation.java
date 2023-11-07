package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.A6M;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.B5N;
import com.maddox.il2.objects.air.BF_109;
import com.maddox.il2.objects.air.BF_110;
import com.maddox.il2.objects.air.B_17;
import com.maddox.il2.objects.air.B_24;
import com.maddox.il2.objects.air.B_25;
import com.maddox.il2.objects.air.B_29;
import com.maddox.il2.objects.air.D3A;
import com.maddox.il2.objects.air.FW_190;
import com.maddox.il2.objects.air.G4M;
import com.maddox.il2.objects.air.H8K;
import com.maddox.il2.objects.air.HE_111H2;
import com.maddox.il2.objects.air.Hurricane;
import com.maddox.il2.objects.air.IL_2;
import com.maddox.il2.objects.air.JU_88;
import com.maddox.il2.objects.air.JU_88MSTL;
import com.maddox.il2.objects.air.KI_43;
import com.maddox.il2.objects.air.KI_46;
import com.maddox.il2.objects.air.KI_61;
import com.maddox.il2.objects.air.KI_84;
import com.maddox.il2.objects.air.ME_323;
import com.maddox.il2.objects.air.N1K;
import com.maddox.il2.objects.air.PE_2;
import com.maddox.il2.objects.air.PE_8;
import com.maddox.il2.objects.air.PE_8xyz;
import com.maddox.il2.objects.air.P_38;
import com.maddox.il2.objects.air.P_47;
import com.maddox.il2.objects.air.P_51;
import com.maddox.il2.objects.air.SBD;
import com.maddox.il2.objects.air.SPITFIRE;
import com.maddox.il2.objects.air.TA_152H1;
import com.maddox.il2.objects.air.TBF;
import com.maddox.il2.objects.air.TB_3;
import com.maddox.il2.objects.air.TEMPEST;
import com.maddox.il2.objects.air.TU_2;
import com.maddox.il2.objects.air.TypeGlider;
import com.maddox.il2.objects.air.YAK;
import com.maddox.rts.Property;

public class Formation
{

    public static final void generate(Aircraft aaircraft[])
    {
        gen(aaircraft, WR);
    }

    public static final void generate(Aircraft aaircraft[], byte formationType)
    {
//        generate(aaircraft, formationType, scaleCoeff(aaircraft[0]));
//    }
//
//    public static final void generate(Aircraft aaircraft[], byte formationType, float formationScale)
//    {
//        int leaderIndex = 0;
        for (int aircraftPosInFormation = 1; aircraftPosInFormation < aaircraft.length; aircraftPosInFormation++) {
            if(!Actor.isValid(aaircraft[aircraftPosInFormation])) continue;
            if (aaircraft[aircraftPosInFormation] instanceof TypeGlider) {
                return;
            }
//            Formation.gather(aaircraft[aircraftPosInFormation].FM, formationType, dd, Mission.curYear(), aircraftPosInFormation, formationScale);
            Formation.gather(aaircraft[aircraftPosInFormation].FM, formationType, dd, Mission.curYear(), aircraftPosInFormation);
            aaircraft[aircraftPosInFormation].FM.Offset.set(dd);
            aaircraft[0].pos.getAbsOrient().transform(dd);
            aaircraft[aircraftPosInFormation].FM.Leader = aaircraft[aircraftPosInFormation - 1].FM;
            aaircraft[aircraftPosInFormation - 1].FM.Wingman = aaircraft[aircraftPosInFormation].FM;
            aaircraft[aircraftPosInFormation].pos.getAbs(Pd);
            aaircraft[aircraftPosInFormation - 1].pos.getAbs(Pd);
            Pd.sub(dd);
            aaircraft[aircraftPosInFormation].pos.setAbs(Pd);
//            leaderIndex = aircraftPosInFormation;
        }
    }

    private static final float scaleCoeff(Aircraft aircraft)
    {
        if (aircraft == null) return 1.2F;
        // TODO: Changed by western0221, "Straight In Landing" space wider, other Landing a bit wide.
        float fsi = 1.0F;
        aircraft.FM.AP.way.next();
        if(aircraft.FM.AP.way.curr().Action == 2)
        {
            if(aircraft.FM.AP.way.curr().waypointType == 104) fsi = 2.5F;  // Straight In
            else if(aircraft.FM.EI.getNum() > 4) fsi = 1.8F;
            else if(aircraft.FM.EI.engines[0].getType() == 2) fsi = 1.4F;  // Jet engine
            else fsi = 1.2F;
        }
        aircraft.FM.AP.way.prev();
        
        // TODO: By SAS~Storebror: New Formation Parameters +++
        float newScaleCoeff = aircraft.FM.getFormationScaleCoeff();
        if (Float.isNaN(newScaleCoeff)) newScaleCoeff = Property.floatValue(aircraft.getClass(), "FormationScaleCoeff", Float.NaN);
        if (!Float.isNaN(newScaleCoeff))
            return newScaleCoeff * fsi;
        // TODO: By SAS~Storebror: New Formation Parameters ---
        
        if(aircraft instanceof ME_323)
            return 5F * fsi; // TODO: Added "spacing" parameter by western0221
        if((aircraft instanceof PE_8xyz) || (aircraft instanceof PE_8) || (aircraft instanceof TB_3) || (aircraft instanceof B_17) || (aircraft instanceof B_29))
            return 3.5F * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft instanceof TA_152H1)
            return 2.0F * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft instanceof SBD)
            return 1.8F * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft instanceof TBF)
            return 1.9F * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft instanceof FW_190)
            return 1.4F * fsi; // TODO: Added "spacing" parameter by western0221
        
        // TODO: Changed by SAS~Storebror, increase distance in formation depending on number of engines
//        return (aircraft instanceof Scheme1) ? 1.2F : 2.2F;
        //return 1.2F * aircraft.FM.EI.getNum();
        if(aircraft.FM.EI.getNum() == 0)
            return 2.2F * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft.FM.EI.getNum() < 2)
            return 1.2F * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft.FM.EI.getNum() < 4)
            return 1.1F * (float) aircraft.FM.EI.getNum() * fsi; // TODO: Added "spacing" parameter by western0221
        if(aircraft.FM.EI.getNum() < 8)
            return 1.0F * (float) aircraft.FM.EI.getNum() * fsi; // TODO: Added "spacing" parameter by western0221
        return 0.9F * (float) aircraft.FM.EI.getNum() * fsi; // TODO: Added "spacing" parameter by western0221
    }

    public static final void gather(FlightModel flightmodel, byte formationType)
    {
        gather(flightmodel, formationType, flightmodel.Offset);
    }

    public static final void gather(FlightModel flightmodel, byte formationType, Vector3d vector3d)
    {
        gather(flightmodel, formationType, vector3d, Mission.curYear());
    }
    
    public static final void gather(FlightModel flightmodel, byte formationType, Vector3d vector3d, int year)
    {
//        gather(flightmodel, formationType, vector3d, year, ((Maneuver)flightmodel).Group.numInGroup((Aircraft)flightmodel.actor), flightmodel.formationScale);
        gather(flightmodel, formationType, vector3d, year, ((Aircraft)flightmodel.actor).aircIndex());
    }

//    public static final void gather(FlightModel flightmodel, byte formationType, Vector3d vector3d, int year, int numInGroup, float formationScale)
    public static final void gather(FlightModel flightmodel, byte formationType, Vector3d vector3d, int year, int numInGroup)
    {

        Aircraft aircraft = (Aircraft)flightmodel.actor;
        switch(formationType)
        {
        default:
            break;

        case F_DEFAULT:
            // TODO: By SAS~Storebror: New Formation Parameters +++
            byte newFormationType = aircraft.FM.getFormationDefault();
            if (newFormationType == F_DEFAULT) newFormationType = (byte)Property.intValue(aircraft.getClass(), "FormationDefault", F_DEFAULT);
            if (newFormationType != F_DEFAULT) {
                gather(flightmodel, newFormationType, vector3d, year, numInGroup); //, formationScale);
                return;
            }
            // TODO: By SAS~Storebror: New Formation Parameters ---
            
            if(        ((aircraft instanceof BF_109) || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("me_109")))
                    || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("me_155"))
                    || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("me_209"))
                    || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("me_309"))
                    || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("me_509"))
                    || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("me_262"))
                    || ((aircraft instanceof BF_110) || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("bf_110")))
                    || ((aircraft instanceof FW_190) || (aircraft != null && (shortClassName(aircraft.getClass()).toLowerCase().startsWith("fw_190") || shortClassName(aircraft.getClass()).toLowerCase().startsWith("ta_152"))))
                    || ((aircraft instanceof SPITFIRE) || (aircraft != null && (shortClassName(aircraft.getClass()).toLowerCase().indexOf("spitfire") != -1)) && year > 1941) 
                    || ((aircraft instanceof Hurricane) || (aircraft != null && (shortClassName(aircraft.getClass()).toLowerCase().indexOf("hurricane") != -1)) && year > 1941) 
                    || ((aircraft instanceof P_38) || (aircraft != null && shortClassName(aircraft.getClass()).toLowerCase().startsWith("p_38")))
                    || ((aircraft instanceof P_47) || (aircraft != null && (shortClassName(aircraft.getClass()).toLowerCase().startsWith("p_43") || shortClassName(aircraft.getClass()).toLowerCase().startsWith("p_47") || shortClassName(aircraft.getClass()).toLowerCase().startsWith("thunderbolt"))))
                    || ((aircraft instanceof P_51) || (aircraft != null && (shortClassName(aircraft.getClass()).toLowerCase().startsWith("p_51") || shortClassName(aircraft.getClass()).toLowerCase().startsWith("mustang"))))
                    || ((aircraft instanceof TEMPEST) || (aircraft != null && (shortClassName(aircraft.getClass()).toLowerCase().startsWith("tempest") || shortClassName(aircraft.getClass()).toLowerCase().startsWith("typhoon"))))
                    || (aircraft instanceof JU_88MSTL))
            {
                gather(flightmodel, F_FINGERFOUR, vector3d, year, numInGroup); //, formationScale);
                return;
            }
            if((aircraft instanceof IL_2) || (aircraft instanceof YAK) || (aircraft instanceof PE_2) || (aircraft instanceof TU_2))
            {
                gather(flightmodel, F_VIC, vector3d, year, numInGroup); //, formationScale);
                return;
            }
            if((aircraft instanceof A6M) || (aircraft instanceof B5N) || (aircraft instanceof D3A) || (aircraft instanceof G4M) || (aircraft instanceof KI_43) || (aircraft instanceof KI_46) || (aircraft instanceof KI_84) || (aircraft instanceof N1K) || (aircraft instanceof H8K) || (aircraft instanceof KI_61))
            {
                gather(flightmodel, F_VIC, vector3d, year, numInGroup); //, formationScale);
                return;
            }
            if((aircraft instanceof JU_88) || (aircraft instanceof HE_111H2))
            {
                gather(flightmodel, F_DIAMOND, vector3d, year, numInGroup); //, formationScale);
                return;
            }
            if(aircraft instanceof ME_323)
            {
                gather(flightmodel, F_DIAMOND, vector3d, year, numInGroup); //, formationScale);
                return;
            }
            if((aircraft instanceof PE_8xyz) || (aircraft instanceof PE_8) || (aircraft instanceof B_17) || (aircraft instanceof B_24) || (aircraft instanceof B_25) || (aircraft instanceof B_29))
            {
                gather(flightmodel, F_VIC, vector3d, year, numInGroup); //, formationScale);
                return;
            } else
            {
                gather(flightmodel, F_ECHELONRIGHT, vector3d, year, numInGroup); //, formationScale);
                return;
            }

        case F_PREVIOUS:
            gather(flightmodel, flightmodel.formationType, vector3d, year, numInGroup); //, formationScale);
            return;

        case F_ECHELONRIGHT:
            flightmodel.formationType = formationType;
            vector3d.set(25D, 25D, 0D);
//            vector3d.scale(formationScale);
            break;

        case F_ECHELONLEFT:
            flightmodel.formationType = formationType;
            vector3d.set(25D, -25D, 0D);
//            vector3d.scale(formationScale);
            break;

        case F_LINEABREAST:
            flightmodel.formationType = formationType;
            if(numInGroup % 4 == 0)
                vector3d.set(0D, 75D, 0D); // Leader Pos (Offset to WP pos), 25m behind, 75m to the left, same altitude
//                vector3d.set(25D, 75D, 0D); // Leader Pos (Offset to WP pos), 25m behind, 75m to the left, same altitude
            else
//              vector3d.set(1.0D, 33D, 0.0D); // TODO: By SAS~Storebror: Adjusted Formation Offsets
                vector3d.set(0D, 33D, 0D); // All further A/C in the formation are shoulder-to-shoulder, 33m to the right of the previous A/C, same altitude
//            vector3d.scale(formationScale);
            break;

        case F_LINEASTERN:
            flightmodel.formationType = formationType;
            if(numInGroup % 4 == 0)
                vector3d.set(120D, 0D, 0D);
//                vector3d.set(120D, 0D, 15D);
            else
//              vector3d.set(80D, 0D, 10D); // TODO: By SAS~Storebror: Adjusted Formation Offsets
                vector3d.set(80D, 0D, 0D); // All further A/C in the formation are 80m behind the previous A/C, in line, same altitude
//            vector3d.scale(formationScale);
            return;

        case F_VIC:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(55D, 55D, 0D); // Leader Pos (Offset to WP pos), 55m behind, 55m to the right, same altitude
                break;

            case 1:
                vector3d.set(25D, 25D, 0D); // Offset to previous A/C, 25m behind, 25m to the right, same altitude
                break;

            case 2:
                vector3d.set(0D, -50D, 0D); // Offset to previous A/C, shoulder-to-shoulder, 50m to the left, same altitude
                break;

            case 3:
                vector3d.set(25D, -25D, 0D); // Offset to previous A/C, 25m behind, 25m to the left, same altitude
                break;
            }
//            vector3d.scale(formationScale);
            break;

        case F_FINGERFOUR:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(25D, 25D, 0D); // Leader Pos (Offset to WP pos), 25m behind, 25m to the right, same altitude
                break;

            case 1:
                vector3d.set(15D, 30D, 0D); // Offset to previous A/C, 15m behind, 30m to the right, same altitude
                break;

            case 2:
//                vector3d.set(25D, -60D, 0D); // TODO: By SAS~Storebror: Adjusted Formation Offsets
                vector3d.set(0D, -60D, 0D); // Offset to previous A/C, shoulder-to-shoulder, 60m to the left, same altitude
                break;

            case 3:
//                vector3d.set(15D, -20D, 0.0D); // TODO: By SAS~Storebror: Adjusted Formation Offsets
                vector3d.set(15D, -30D, 0D); // Offset to previous A/C, 15m behind, 30m to the left, same altitude
                break;
            }
//            vector3d.scale(formationScale);
            break;

        case F_DIAMOND:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(75D, 30D, 0D); // Leader Pos (Offset to WP pos), 75m behind, 30m to the right, same altitude
                break;

            case 1:
                vector3d.set(25D, 25D, 0D); // Offset to previous A/C, 25m behind, 25m to the right, same altitude
                break;

            case 2:
                vector3d.set(0D, -50D, 0D); // Offset to previous A/C, shoulder-to-shoulder, 50m to the left, same altitude
                break;

            case 3:
                vector3d.set(25D, 25D, 0D); // Offset to previous A/C, 25m behind, 25m to the right, same altitude
                break;
            }
//            vector3d.scale(formationScale);
            break;
            
        case F_JAVELIN: // similar to Diamond, but stacked vertically
            switch (numInGroup % 4)
            {
            case 0: 
                vector3d.set(75D, 30D, 0D); // Leader Pos (Offset to WP pos), 75m behind, 30m to the right, same altitude
              break;
            case 1: 
                vector3d.set(25D, 25D, -10D); // Offset to previous A/C, 25m behind, 25m to the right, 10m above
              break;
            case 2: 
                vector3d.set(0D, -50D, 20D); // Offset to previous A/C, shoulder-to-shoulder, 50m to the left, 20m below
              break;
            case 3: 
                vector3d.set(25D, 25D, -10D); // Offset to previous A/C, 25m behind, 25m to the right, 10m above
            }
//            vector3d.scale(formationScale);
            break;
            
        case F_LINE:
            flightmodel.formationType = formationType;
            if(numInGroup % 4 == 0)
                vector3d.set(120D, 0D, 0D); // Leader Pos (Offset to WP pos), 120m behind, in line, same altitude
            else
//              vector3d.set(80D, 0D, 0D); // TODO: By SAS~Storebror: Adjusted Formation Offsets
                vector3d.set(80D, 0D, 10D); // All further A/C in the formation are 80m behind the previous A/C, in line, 10m below
//            vector3d.scale(formationScale);
            return;
            
        case F_LINEASTERNLONG:
            flightmodel.formationType = formationType;
            if(numInGroup % 4 == 0)
                vector3d.set(-360D, 0D, 0D); // Leader Pos (Offset to WP pos), 240m upfront, in line, same altitude
            else
                vector3d.set(240D, 0D, 20D); // All further A/C in the formation are 240m behind the previous A/C, in line, 20m below
            
         // TODO: By SAS~Storebror: Adjusted Formation Offsets
//            switch(numInGroup)
//            {
//            case 0:
//                vector3d.set(-360D, 0D, 0D);
//                break;
//
//            case 1:
//                vector3d.set(240D, 0D, 0D);
//                break;
//
//            case 2:
//                vector3d.set(120D, 0D, 0D);
//                break;
//
//            case 3:
//                vector3d.set(120D, 0D, 0D); // TODO: Changed for 0s all over
//                break;
//            }
//            vector3d.scale(formationScale);
            return;
            
        case F_LINEUPSTACKED:
            flightmodel.formationType = formationType;
            if(numInGroup % 4 == 0)
                vector3d.set(120D, 0D, -5D); // Leader Pos (Offset to WP pos), 120m behind, in line, 5m above
            else
                vector3d.set(280D, 0D, -20D); // All further A/C in the formation are 280m behind the previous A/C, in line, 20m above
//            vector3d.scale(formationScale);
            return;
            
        case F_FINGERTIP_RIGHT:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(25D, -25D, 0D); // Leader Pos (Offset to WP pos), 25m behind, 25m to the left, same altitude
                break;

            case 1:
                vector3d.set(15D, -30D, 0D); // Offset to previous A/C, 15m behind, 30m to the left, same altitude
                break;

            case 2:
                vector3d.set(0D, 60D, 0D); // Offset to previous A/C, shoulder-to-shoulder, 60m to the right, same altitude
                break;

            case 3:
                vector3d.set(15D, 30D, 0D); // Offset to previous A/C, 15m behind, 30m to the right, same altitude
                break;
            }
            break;

        case F_WALL:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(25D, -45D, -15D); // Leader Pos (Offset to WP pos), 25m behind, 45m to the left, 15m above
                break;

            case 1:
                vector3d.set(0D, 60D, 10D); // Offset to previous A/C, shoulder-to-shoulder, 60m to the right, 10m below
                break;

            case 2:
                vector3d.set(0D, -30D, 10D); // Offset to previous A/C, shoulder-to-shoulder, 30m to the left, 10m below
                break;

            case 3:
                vector3d.set(0D, 60D, 10D); // Offset to previous A/C, shoulder-to-shoulder, 60m to the right, 10m below
                break;
            }
            break;

        case F_FLUID_FOUR:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(25D, -20D, 5D); // Leader Pos (Offset to WP pos), 25m behind, 15m to the left, 5m below
                break;

            case 1:
                vector3d.set(30D, -30D, -10D); // Offset to previous A/C, 30m behind, 30m to the left, 10m above
                break;

            case 2:
                vector3d.set(-30D, 45D, 10D); // Offset to previous A/C, 30m upfront, 45m to the right, 10m below
                break;

            case 3:
                vector3d.set(30D, 30D, -10D); // Offset to previous A/C, 30m behind, 30m to the right, 10m above
                break;
            }
            break;

        case F_ROUTE:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(25D, -25D, -10D); // Leader Pos (Offset to WP pos), 25m behind, 25m to the left, 10m above
                break;

            case 1:
                vector3d.set(20D, -40D, 10D); // Offset to previous A/C, 15m behind, 30m to the left, 10m below
                break;

            case 2:
                vector3d.set(0D, 80D, 0D); // Offset to previous A/C, shoulder-to-shoulder, 60m to the right, same altitude
                break;

            case 3:
                vector3d.set(20D, 40D, 0D); // Offset to previous A/C, 15m behind, 30m to the right, 10m below
                break;
            }
            break;

        case F_BOX:
            flightmodel.formationType = formationType;
            switch(numInGroup % 4)
            {
            case 0:
                vector3d.set(25D, -30D, -25D); // Leader Pos (Offset to WP pos), 25m behind, 30m to the left, 25m above
                break;

            case 1:
                vector3d.set(0D, 50D, 5D); // Offset to previous A/C, shoulder-to-shoulder, 50m to the right, 5m below
                break;

            case 2:
                vector3d.set(50D, -40D, 45D); // Offset to previous A/C, 50m behind, 40m to the left, 40m below
                break;

            case 3:
                vector3d.set(0D, 50D, 5D); // Offset to previous A/C, shoulder-to-shoulder, 50m to the right, 5m below
                break;
            }
            break;

        }
//        vector3d.scale(formationScale * scaleCoeff(aircraft));
        vector3d.scale(scaleCoeff(aircraft) * flightmodel.formationScale);
     }

    public static final void leaderOffset(FlightModel flightmodel, byte formationType, Vector3d vector3d)
    {
        int wingSquadIndex = 0;
        Aircraft aircraft = null;
        if (flightmodel != null) {
            aircraft = (Aircraft)flightmodel.actor;
            if (aircraft != null) {
                Wing wing = (Wing)aircraft.getOwner();
                if(wing != null) {
                    wingSquadIndex = wing.indexInSquadron();
                }
            }
        }
//        System.out.println("Formation.leaderOffset(" + aircraft.name() + ", " + formationType + ", v3d), wingSquadIndex=" + wingSquadIndex);
        
        // TODO: By SAS~Storebror: New Formation Parameters +++
        boolean offsetDone = false;
        if (aircraft != null) {
            if (aircraft.FM != null && aircraft.FM.isLeaderOffsetValid()) {
    //            System.out.println("aircraft.FM.isLeaderOffsetValid()=true");
                vector3d.set(aircraft.FM.getLeaderOffset(wingSquadIndex));
                offsetDone = true;
            } else {
    //            System.out.println("aircraft.FM.isLeaderOffsetValid()=false");
                do {
                    try {
                        float leaderOffsetX = Property.floatValue(aircraft.getClass(), "FormationOffset" + (wingSquadIndex + 1) + "X", Float.NaN);
                        if (Float.isNaN(leaderOffsetX))
                            break;
                        float leaderOffsetY = Property.floatValue(aircraft.getClass(), "FormationOffset" + (wingSquadIndex + 1) + "Y", Float.NaN);
                        if (Float.isNaN(leaderOffsetY))
                            break;
                        float leaderOffsetZ = Property.floatValue(aircraft.getClass(), "FormationOffset" + (wingSquadIndex + 1) + "Z", Float.NaN);
                        if (Float.isNaN(leaderOffsetZ))
                            break;
                        vector3d.set(leaderOffsetX, leaderOffsetY, leaderOffsetZ);
                        offsetDone = true;
                    } catch (Exception e) {
                    }
                } while (false);
            }
        }
//        System.out.println("offsetDone=" + offsetDone);
        if (!offsetDone) {
        // TODO: By SAS~Storebror: New Formation Parameters ---
        
            if (aircraft != null && (((aircraft instanceof B_17) || (aircraft instanceof B_24) || (aircraft instanceof B_29))) && formationType != F_LINEASTERNLONG) { // Combat Box
                switch(wingSquadIndex)
                {
                case 0:
                    vector3d.set(300D, -150D, 0D); // Leader Pos (Offset to WP pos), 300m behind, 150m to the left, same altitude
                    break;
    
                case 1:
                    vector3d.set(100D, -80D, -30D); // Offset to Leader, 100m behind, 80m to the left, 30m above
                    break;
    
                case 2:
                    vector3d.set(100D, 80D, 25D); // Offset to Leader, 100m behind, 80m to the right, 25m below
                    break;
    
                case 3:
                    vector3d.set(200D, 0.0D, 50D); // Offset to Leader, 200, behind, in line, 50m below
                    break;
                }
            } else {
//                if (wingSquadIndex == 0) vector3d.set(300D, -150D, 0D); // Leader Pos (Offset to WP pos), 300m behind, 150m to the left, same altitude
                if (wingSquadIndex == 0) vector3d.set(0D, 0D, 0D); // Leader Pos (Offset to WP pos), 300m behind, 150m to the left, same altitude
                else switch (formationType) {
                    case F_ECHELONRIGHT:
                        vector3d.set(100D * wingSquadIndex, 100D * wingSquadIndex, 0D);
                        break;
                    case F_ECHELONLEFT:
                        vector3d.set(-100D * wingSquadIndex, -100D * wingSquadIndex, 0D);
                        break;
                    case F_LINEABREAST:
                        vector3d.set(0D, 187D * wingSquadIndex, 0D);
//                        vector3d.set(0D, 132D * wingSquadIndex, 0D);
                        break;
                    case F_LINEASTERN:
                        vector3d.set(200D * wingSquadIndex, 0D, 0D);
//                        vector3d.set(320D * wingSquadIndex, 0D, 0D);
                        break;
                    case F_LINEASTERNLONG:
                        vector3d.set(600D * wingSquadIndex, 0D, 50D * wingSquadIndex);
//                        vector3d.set(960D * wingSquadIndex, 0D, 80D);
                        break;
                    case F_LINEUPSTACKED:
                        vector3d.set(1120D * wingSquadIndex, 0D, -80D);
                        break;
                    case F_LINE:
                        vector3d.set(320D * wingSquadIndex, 0D, 40D);
                        break;
                    case F_WALL:
                        vector3d.set(0D, 125D * wingSquadIndex, 0D);
                        break;
                    default:
                        switch(wingSquadIndex)
                        {
                        case 1:
                            vector3d.set(100D, -80D, -30D); // Offset to Leader, 100m behind, 80m to the left, 30m above
                            break;
                        case 2:
                            vector3d.set(100D, 80D, 25D); // Offset to Leader, 100m behind, 80m to the right, 25m below
                            break;
                        case 3:
                            vector3d.set(200D, 0.0D, 50D); // Offset to Leader, 200, behind, in line, 50m below
                            break;
                        }
                }
                
                
                
//                switch(wingSquadIndex)
//                {
//                default:
//                    break;
//    
//                case 0:
//                    vector3d.set(300D, -150D, 0D); // Leader Pos (Offset to WP pos), 300m behind, 150m to the left, same altitude
//                    break;
//    
//                case 1:
//                    if(formationType != F_ECHELONRIGHT)
//                        vector3d.set(100D, 100D, 0.0D); // Offset to Leader, 100m behind, 100m to the right, same altitude
//                    else
//                        vector3d.set(200D, 200D, 0.0D); // Offset to Leader, 200m behind, 200m to the right, same altitude
//                    break;
//    
//                case 2:
//                    if(formationType != F_ECHELONLEFT && formationType != F_VIC)
//                        vector3d.set(150D, -150D, 0.0D); // Offset to Leader, 150m behind, 150m to the left, same altitude
//                    else
//                        vector3d.set(210D, -210D, 0.0D); // Offset to Leader, 210m behind, 210m to the left, same altitude
//                    break;
//    
//                case 3:
//                    if(formationType != F_LINEASTERN)
//                        vector3d.set(150D, 0.0D, 0.0D); // Offset to Leader, 150m behind, in line, same altitude
//                    else
//                        vector3d.set(300D, 0.0D, 0.0D); // Offset to Leader, 300m behind, in line, same altitude
//                    break;
//                }
            }
        }
        vector3d.scale(0.7D * scaleCoeff(aircraft));
    }
    
    private static final void gen(Aircraft aaircraft[], Vector3f vector3f)
    {
        dd.set(vector3f);
        aaircraft[0].pos.getAbsOrient().transform(dd);
        int j = 0;
        for(int i = 1; i < aaircraft.length; i++)
            if(Actor.isValid(aaircraft[i]))
            {
                aaircraft[i].FM.Offset.set(vector3f);
                aaircraft[i].FM.Leader = aaircraft[j].FM;
                aaircraft[j].FM.Wingman = aaircraft[i].FM;
                aaircraft[j].pos.getAbs(Pd);
                Pd.sub(dd);
                aaircraft[i].pos.setAbs(Pd);
                j = i;
            }

    }

    public static String shortClassName(Class theClass) {
        if (theClass.isArray()) {
            return shortClassName(theClass.getComponentType()) + "[]";
        }
        String str = theClass.getName();
        return str.substring(str.lastIndexOf(".") + 1);
    }
    
    public static final byte F_DEFAULT = 0;
    public static final byte F_PREVIOUS = 1;
    public static final byte F_ECHELONRIGHT = 2;
    public static final byte F_ECHELONLEFT = 3;
    public static final byte F_LINEABREAST = 4;
    public static final byte F_LINEASTERN = 5;
    public static final byte F_VIC = 6;
    public static final byte F_FINGERFOUR = 7;
    public static final byte F_FINGERTIP_LEFT = 7; // Alias for the Stock "Finger four" formation
    public static final byte F_DIAMOND = 8;
    // TODO: 4.14 backport , idiotically they've thrown in F_JAVELIN at index 9 !!??!? +++
    public static final byte F_JAVELIN = 9;
    // TODO: 4.14 backport ---
    public static final byte F_LINE = 10;
    public static final byte F_LINEASTERNLONG = 11;
    public static final byte F_LINEUPSTACKED = 12;
    public static final byte F_FINGERTIP_RIGHT = 13; // Same as stock "Finger four", but strong element to the right
    public static final byte F_TRAIL = 5; // Alias for F_LINEASTERN
    public static final byte F_WALL = 14; // Wall formation, see https://www.combataircraft.com/en/Formations/
    public static final byte F_FLUID_FOUR = 15; // Fluid four formation, see https://www.combataircraft.com/en/Formations/
    public static final byte F_ROUTE = 16; // Route formation, see https://www.combataircraft.com/en/Formations/ - essentially F_FINGERTIP_RIGHT stacked vertically
    public static final byte F_BOX = 17; // Box formation, see https://www.combataircraft.com/en/Formations/
    
    private static final Vector3f WR = new Vector3f(100F, 100F, 0.0F);
    private static final Vector3d dd = new Vector3d();
    private static final Point3d Pd = new Point3d();

}
