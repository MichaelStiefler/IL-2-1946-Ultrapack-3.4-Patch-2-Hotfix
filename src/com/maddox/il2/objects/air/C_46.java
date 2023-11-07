package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class C_46 extends Scheme2 implements TypeTransport, TypeBomber {
    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 250F, -75F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.05F, 1.0F, 0.0F, -77F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.95F, 0.0F, -0.645F);
        hiermesh.chunkSetLocate("GearL5_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.05F, 1.0F, 0.0F, -77F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.95F, 0.0F, 0.645F);
        hiermesh.chunkSetLocate("GearR5_D0", Aircraft.xyz, Aircraft.ypr);
        if (f1 > -2.5F) {
            f1 = 0.0F;
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -f1, 0.0F);
        float f2 = Math.max(-f * 550F, -95F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 95F * f, 0.0F);
        if (f2 > -2.5F) {
            f2 = 0.0F;
        }
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f2, 0.0F);
    }

    protected void moveGear(float f) {
        C_46.moveGear(this.hierMesh(), f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLOut") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(Aircraft.Pd.y) < 6D)) {
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("WingROut") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(Aircraft.Pd.y) < 6D)) {
            this.FM.AS.hitTank(shot.initiator, 3, 1);
        }
        if (shot.chunkName.startsWith("WingLIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(Aircraft.Pd.y) < 1.94D)) {
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("WingRIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) && (Math.abs(Aircraft.Pd.y) < 1.94D)) {
            this.FM.AS.hitTank(shot.initiator, 2, 1);
        }
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("Nose") && (Aircraft.Pd.x > 4.9D) && (Aircraft.Pd.z > -0.09D) && (World.Rnd().nextFloat() < 0.1F)) {
            if (Aircraft.Pd.y > 0.0D) {
                this.killPilot(shot.initiator, 0);
                this.FM.setCapableOfBMP(false, shot.initiator);
            } else {
                this.killPilot(shot.initiator, 1);
            }
        }
        if (shot.chunkName.startsWith("Tail") && (Aircraft.Pd.x < -5.8D) && (Aircraft.Pd.x > -6.79D) && (Aircraft.Pd.z > -0.449D) && (Aircraft.Pd.z < 0.124D)) {
            this.FM.AS.hitPilot(shot.initiator, World.Rnd().nextInt(3, 4), (int) (shot.mass * 1000F * World.Rnd().nextFloat(0.9F, 1.1F)));
        }
        if ((this.FM.AS.astateEngineStates[0] > 2) && (this.FM.AS.astateEngineStates[1] > 2) && (World.Rnd().nextInt(0, 99) < 33)) {
            this.FM.setCapableOfBMP(false, shot.initiator);
        }
        super.msgShot(shot);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -95F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Blister3", 0.0F, 90F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Blister2", 0.0F, 95F * f, 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        C_46.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }
    
    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        hierMesh.chunkVisible("Blister1_D0", !thisWeaponsName.startsWith("40x"));
        hierMesh.chunkVisible("Blister2", !thisWeaponsName.startsWith("40x"));
        hierMesh.chunkVisible("Luggage", !(thisWeaponsName.startsWith("40x") || thisWeaponsName.startsWith("no") || thisWeaponsName.startsWith("med") || thisWeaponsName.startsWith("tra")));
        hierMesh.chunkVisible("Pilot3_D0", thisWeaponsName.startsWith("40x"));
        hierMesh.chunkVisible("Mash", thisWeaponsName.startsWith("med"));
        hierMesh.chunkVisible("LuggageWillys", thisWeaponsName.startsWith("tra"));
    }
    

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 13:
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                break;

            case 35:
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
                }
                break;

            case 38:
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
                }
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void update(float f)
    {
        super.update(f);
        if (this.FM.AS.isMaster() && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && this.FM.Gears.onGround()) {
            if (this.FM.getSpeedKMH() > 20.0F) {
              this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
    }
    
    static {
        Class class1 = C_46.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "C-46");
        Property.set(class1, "meshName", "3DO/Plane/C-46(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 2999.9F);
        Property.set(class1, "FlightModel", "FlightModels/C46.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitC46.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_InternalDev01" });
    }
}
