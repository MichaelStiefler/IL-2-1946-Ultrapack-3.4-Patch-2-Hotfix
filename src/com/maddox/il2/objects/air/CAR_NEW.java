package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;

public abstract class CAR_NEW extends Scheme1
    implements TypeScout
{
    
    public void update(float f)
    {
        if(this == World.getPlayerAircraft())
            World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
        hierMesh().chunkSetAngles("Head2_D0", -this.FM.turret[0].tu[1], 0.0F, 0.0F);
//        float turretRotation = this.FM.turret[0].tu[0];
//        while (turretRotation < -180F) turretRotation += 360F;
//        while (turretRotation > 180F) turretRotation -= 360F;
//        hierMesh().chunkSetAngles("pipelong", 0.0F, CommonTools.cvt(Math.abs(turretRotation), 0F, 180F, 0F, 20F), 0.0F);
//        hierMesh().chunkSetAngles("TurretBase_D0", 0.0F, -CommonTools.cvt(Math.abs(turretRotation), 0F, 180F, 0F, 20F), 0.0F);
        Eff3DActor eff3DActor[] = (Eff3DActor[])Reflection.getValue(this.FM.Gears, "clpEff");
        for (int i=0; i<3; i++) if (eff3DActor[i] != null) Eff3DActor.finish(eff3DActor[i]);
//        System.out.println("SM=" + this.FM.AS.bShowSmokesOn + ", WTLE=" + this.FM.AS.bWingTipLExists + ", WTRE=" + this.FM.AS.bWingTipRExists);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Helm_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Helm2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
    }

    protected void moveRudder(float f)
    {
//        System.out.println("moveRudder(" + f + ")");
        hierMesh().chunkSetAngles("GearL2_D0", -40F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("GearR2_D0", -40F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xxengine") && getEnergyPastArmor(2.4F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * (this.FM.EI.engines[0].getType() == 0 ? 1.75F : 0.5F))
        {
            if(this.FM.EI.engines[0].getType() == 0)
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
            else
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19200F)));
            debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
            if(World.Rnd().nextFloat() < shot.power / 96000F && this.FM.EI.engines[0].getType() == 0)
            {
                this.FM.AS.hitEngine(shot.initiator, 0, 3);
                this.FM.AS.hitOil(shot.initiator, 0);
                debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
            }
            if(World.Rnd().nextFloat() < shot.power / 96000F && this.FM.EI.engines[0].getType() == 1)
            {
                this.FM.AS.hitEngine(shot.initiator, 0, 1);
                debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
            }
            if(World.Rnd().nextFloat() < 0.01F)
            {
                this.FM.AS.setEngineStuck(shot.initiator, 0);
                debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
            }
            getEnergyPastArmor(43.6F, shot);
        }
        if(s.startsWith("xxtank"))
        {
            int i = s.charAt(6) - 48;
            switch(i)
            {
            default:
                break;

            case 1:
                if(getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                {
                    if(this.FM.AS.astateTankStates[2] == 0)
                    {
                        debugprintln(this, "*** Fuel Tank: Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, 2, 1);
                    } else
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.9F || World.Rnd().nextFloat() < 0.03F)
                    {
                        this.FM.AS.hitTank(shot.initiator, 2, 2);
                        debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                    if(shot.power > 200000F)
                    {
                        this.FM.AS.hitTank(shot.initiator, 2, 99);
                        debugprintln(this, "*** Fuel Tank: Major Hit..");
                    }
                }
                break;

            case 2:
                if(getEnergyPastArmor(1.2F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.25F)
                    break;
                if(this.FM.AS.astateTankStates[1] == 0)
                {
                    debugprintln(this, "*** Fuel Tank: Pierced..");
                    this.FM.AS.hitTank(shot.initiator, 1, 1);
                    this.FM.AS.doSetTankState(shot.initiator, 1, 1);
                } else
                if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F || World.Rnd().nextFloat() < 0.03F)
                {
                    this.FM.AS.hitTank(shot.initiator, 1, 2);
                    debugprintln(this, "*** Fuel Tank: Hit..");
                }
                if(shot.power > 200000F)
                {
                    this.FM.AS.hitTank(shot.initiator, 1, 99);
                    debugprintln(this, "*** Fuel Tank: Major Hit..");
                }
                break;

            case 3:
                if(getEnergyPastArmor(1.2F, shot) <= 0.0F || World.Rnd().nextFloat() >= 0.25F)
                    break;
                if(this.FM.AS.astateTankStates[0] == 0)
                {
                    debugprintln(this, "*** Fuel Tank: Pierced..");
                    this.FM.AS.hitTank(shot.initiator, 0, 1);
                    this.FM.AS.doSetTankState(shot.initiator, 0, 1);
                } else
                if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F || World.Rnd().nextFloat() < 0.03F)
                {
                    this.FM.AS.hitTank(shot.initiator, 0, 2);
                    debugprintln(this, "*** Fuel Tank: Hit..");
                }
                if(shot.power > 200000F)
                {
                    this.FM.AS.hitTank(shot.initiator, 0, 99);
                    debugprintln(this, "*** Fuel Tank: Major Hit..");
                }
                break;
            }
            return;
        } else
        {
            return;
        }
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("CF"))
        {
            if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.3F)
                this.FM.AS.hitEngine(shot.initiator, 0, 1);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.03F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
        }
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.06F && Pd.z > 0.48D)
        {
            this.FM.AS.setJamBullets(0, 0);
            this.FM.AS.setJamBullets(0, 1);
        }
        if(shot.chunkName.startsWith("Oil") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F)
            this.FM.AS.hitOil(shot.initiator, 0);
        if(shot.chunkName.startsWith("Pilot"))
        {
            if((double)shot.power * Math.abs(v1.x) > 12070D)
                this.FM.AS.hitPilot(shot.initiator, 0, (int)(shot.mass * 1000F * 0.5F));
            if(Pd.z > 1.01175D)
            {
                killPilot(shot.initiator, 0);
                if(shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                    HUD.logCenter("H E A D S H O T");
            }
            return;
        } else
        {
            super.msgShot(shot);
            return;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
            return super.cutFM(34, j, actor);

        case 36:
            return super.cutFM(37, j, actor);
        }
        return super.cutFM(i, j, actor);
    }
    
    static 
    {
        Class class1 = CAR_NEW.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
