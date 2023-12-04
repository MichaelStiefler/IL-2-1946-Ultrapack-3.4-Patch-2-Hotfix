package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class FJ_4 extends Scheme1 implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeStormovik, TypeGSuit {

    public float getDragForce(float f, float f1, float f2, float f3) {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5) {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2) {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1) {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1) {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public FJ_4() {
        this.lLightHook = new Hook[4];
        this.bSlatsOff = false;
        this.oldthrl = -1F;
        this.curthrl = -1F;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.AirBrakeControl = 0.0F;
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lightTime = 0.0F;
        this.ft = 0.0F;
        this.engineSurgeDamage = 0.0F;
        this.hasHydraulicPressure = true;
        this.APmode1 = false;
        this.APmode2 = false;
        this.transsonicEffects = new TransonicEffects(this, 0.0F, 13000.0F, 0.65F, 1.0F, 0.01F, 1.0F, 0.2F, 1.0F, 0.5F, 0.6F, 0.0F, 0.9F, 1.0F, 1.25F);
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(FJ_4.NEG_G_TOLERANCE_FACTOR, FJ_4.NEG_G_TIME_FACTOR, FJ_4.NEG_G_RECOVERY_FACTOR, FJ_4.POS_G_TOLERANCE_FACTOR, FJ_4.POS_G_TIME_FACTOR, FJ_4.POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.transsonicEffects.onAircraftLoaded();
    }

    public void updateLLights() {
        this.pos.getRender(Actor._tmpLoc);
        if (this.lLight == null) {
            if (Actor._tmpLoc.getX() >= 1.0D) {
                this.lLight = new LightPointWorld[4];
                for (int i = 0; i < 4; i++) {
                    this.lLight[i] = new LightPointWorld();
                    this.lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    this.lLight[i].setEmit(0.0F, 0.0F);
                    try {
                        this.lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    } catch (Exception exception) {
                    }
                }

            }
        } else {
            for (int j = 0; j < 4; j++) {
                if (this.FM.AS.astateLandingLightEffects[j] != null) {
                    FJ_4.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, FJ_4.lLightLoc1);
                    FJ_4.lLightLoc1.get(FJ_4.lLightP1);
                    FJ_4.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, FJ_4.lLightLoc1);
                    FJ_4.lLightLoc1.get(FJ_4.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(FJ_4.lLightP1, FJ_4.lLightP2, FJ_4.lLightPL)) {
                        FJ_4.lLightPL.z++;
                        FJ_4.lLightP2.interpolate(FJ_4.lLightP1, FJ_4.lLightPL, 0.95F);
                        this.lLight[j].setPos(FJ_4.lLightP2);
                        float f = (float) FJ_4.lLightP1.distance(FJ_4.lLightPL);
                        float f1 = (f * 0.5F) + 60F;
                        float f2 = 0.7F - ((0.8F * f * this.lightTime) / 2000F);
                        this.lLight[j].setEmit(f2, f1);
                    } else {
                        this.lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else if (this.lLight[j].getR() != 0.0F) {
                    this.lLight[j].setEmit(0.0F, 0.0F);
                }
            }

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FJ_4.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FJ_4.bChangedPit = true;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            if ((((Maneuver) this.FM).get_maneuver() == 25) && (this.FM.Gears.nearGround() || this.FM.Gears.onGround())) {
                if (this.FM.CT.AirBrakeControl != 0.0F) {
                    this.FM.CT.AirBrakeControl = 0.0F;
                }
            } else if (this.FM.AP.way.isLanding() && (this.FM.getSpeed() > this.FM.VmaxFLAPS) && (this.FM.getSpeed() > (this.FM.AP.way.curr().getV() * 1.4F))) {
                if (this.FM.CT.AirBrakeControl != 1.0F) {
                    this.FM.CT.AirBrakeControl = 1.0F;
                }
            } else if (((Maneuver) this.FM).get_maneuver() == 66) {
                if (this.FM.CT.AirBrakeControl != 0.0F) {
                    this.FM.CT.AirBrakeControl = 0.0F;
                }
            } else if (((Maneuver) this.FM).get_maneuver() == 7) {
                if (this.FM.CT.AirBrakeControl != 1.0F) {
                    this.FM.CT.AirBrakeControl = 1.0F;
                }
            } else if (this.hasHydraulicPressure && (this.FM.CT.AirBrakeControl != 0.0F)) {
                this.FM.CT.AirBrakeControl = 0.0F;
            }
        }
        this.ft = World.getTimeofDay() % 0.01F;
        if (this.ft == 0.0F) {
            this.UpdateLightIntensity();
        }
    }

    private final void UpdateLightIntensity() {
        if ((World.getTimeofDay() >= 6F) && (World.getTimeofDay() < 7F)) {
            this.lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        } else if ((World.getTimeofDay() >= 18F) && (World.getTimeofDay() < 19F)) {
            this.lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        } else if ((World.getTimeofDay() >= 7F) && (World.getTimeofDay() < 18F)) {
            this.lightTime = 0.1F;
        } else {
            this.lightTime = 1.0F;
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        if (this.k14Mode == 0) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
            }
        } else if (this.k14Mode == 1) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
            }
        } else if ((this.k14Mode == 2) && (this.FM.actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
        }
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10F;
        if (this.k14Distance > 800F) {
            this.k14Distance = 800F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10F;
        if (this.k14Distance < 200F) {
            this.k14Distance = 200F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        if (this.k14WingspanType == 0) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
            }
        } else if (this.k14WingspanType == 1) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
            }
        } else if (this.k14WingspanType == 2) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
            }
        } else if (this.k14WingspanType == 3) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
            }
        } else if (this.k14WingspanType == 4) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
            }
        } else if (this.k14WingspanType == 5) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
            }
        } else if (this.k14WingspanType == 6) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
            }
        } else if (this.k14WingspanType == 7) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
            }
        } else if (this.k14WingspanType == 8) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
            }
        } else if ((this.k14WingspanType == 9) && (this.FM.actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
        }
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        if (this.k14WingspanType == 0) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
            }
        } else if (this.k14WingspanType == 1) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
            }
        } else if (this.k14WingspanType == 2) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
            }
        } else if (this.k14WingspanType == 3) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
            }
        } else if (this.k14WingspanType == 4) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
            }
        } else if (this.k14WingspanType == 5) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
            }
        } else if (this.k14WingspanType == 6) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
            }
        } else if (this.k14WingspanType == 7) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
            }
        } else if (this.k14WingspanType == 8) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
            }
        } else if ((this.k14WingspanType == 9) && (this.FM.actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
        }
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat1_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.75F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
            Main3D.cur3D().cockpits[0].onDoorMoved(f);
        }
        this.setDoorSnd(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -40F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC44_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC55_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearL66_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearR66_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 40F * f, -90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -40F * f, -90F * f);
    }

    protected void moveGear(float f) {
        FJ_4.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.19075F, 0.0F, 0.0F, 1.0F);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        this.hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, 40F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveFlap(float f) {
        float f1 = 5F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("WingLIn_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("WingRIn_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("Back_D0", 0.0F, 0.0F, f1);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 50F * f);
    }

    protected void moveFan(float f) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        this.part(s);
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(13.35D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(8.770001F, shot);
                } else if (s.endsWith("g1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(40F, 60F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                }
            } else if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch (j) {
                    case 1:
                    case 2:
                        if ((World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(1.1F, shot) > 0.0F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                    case 4:
                        if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                        }
                        if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        break;
                }
            } else if (s.startsWith("xxeng1")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("bloc")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                if (s.endsWith("cams") && (this.getEnergyPastArmor(0.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 20F))) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    this.debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < (shot.power / 24000F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        this.debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75F)) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        this.debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if (s.endsWith("eqpt") && (World.Rnd().nextFloat() < (shot.power / 24000F))) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 3);
                    this.debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            } else if (s.startsWith("xxmgun0")) {
                int k = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    this.FM.AS.setJamBullets(0, k);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else if (s.startsWith("xxtank")) {
                int l = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    if (this.FM.AS.astateTankStates[l] == 0) {
                        this.debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, l, 1);
                        this.FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.075F)) {
                        this.FM.AS.hitTank(shot.initiator, l, 2);
                        this.debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            } else if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else if (s.startsWith("xxhyd")) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            } else if (s.startsWith("xxpnm")) {
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
        } else {
            if (s.startsWith("xcockpit")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                this.getEnergyPastArmor(0.05F, shot);
            }
            if (s.startsWith("xcf")) {
                this.hitChunk("CF", shot);
            } else if (s.startsWith("xnose")) {
                this.hitChunk("Nose", shot);
            } else if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else if (s.startsWith("xkeel")) {
                if (this.chunkDamageVisible("Keel1") < 2) {
                    this.hitChunk("Keel1", shot);
                }
            } else if (s.startsWith("xrudder")) {
                this.hitChunk("Rudder1", shot);
            } else if (s.startsWith("xstab")) {
                if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                    this.hitChunk("StabL", shot);
                }
                if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                    this.hitChunk("StabR", shot);
                }
            } else if (s.startsWith("xvator")) {
                if (s.startsWith("xvatorl")) {
                    this.hitChunk("VatorL", shot);
                }
                if (s.startsWith("xvatorr")) {
                    this.hitChunk("VatorR", shot);
                }
            } else if (s.startsWith("xwing")) {
                if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                    this.hitChunk("WingLIn", shot);
                }
                if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                    this.hitChunk("WingRIn", shot);
                }
                if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                    this.hitChunk("WingLMid", shot);
                }
                if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                    this.hitChunk("WingRMid", shot);
                }
                if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                    this.hitChunk("WingLOut", shot);
                }
                if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                    this.hitChunk("WingROut", shot);
                }
            } else if (s.startsWith("xarone")) {
                if (s.startsWith("xaronel")) {
                    this.hitChunk("AroneL", shot);
                }
                if (s.startsWith("xaroner")) {
                    this.hitChunk("AroneR", shot);
                }
            } else if (s.startsWith("xgear")) {
                if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                    this.debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                byte byte0 = 0;
                int i1;
                if (s.endsWith("a")) {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                } else if (s.endsWith("b")) {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                } else {
                    i1 = s.charAt(5) - 49;
                }
                this.hitFlesh(i1, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 13:
                this.FM.Gears.cgear = false;
                float f = World.Rnd().nextFloat(0.0F, 1.0F);
                if (f < 0.1F) {
                    this.FM.AS.hitEngine(this, 0, 100);
                    if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.49D) {
                        this.FM.EI.engines[0].setEngineDies(actor);
                    }
                } else if (f > 0.55D) {
                    this.FM.EI.engines[0].setEngineDies(actor);
                }
                return super.cutFM(i, j, actor);

            case 19:
                this.FM.EI.engines[0].setEngineDies(actor);
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void typeFighterAceMakerRangeFinder() {
        if (this.k14Mode == 2) {
            return;
        }
        FJ_4.hunted = Main3D.cur3D().getViewPadlockEnemy();
        if (FJ_4.hunted == null) {
            this.k14Distance = 200F;
            FJ_4.hunted = War.GetNearestEnemyAircraft(this.FM.actor, 2700F, 9);
        }
        if (FJ_4.hunted != null) {
            this.k14Distance = (float) this.FM.actor.pos.getAbsPoint().distance(FJ_4.hunted.pos.getAbsPoint());
            if (this.k14Distance > 800F) {
                this.k14Distance = 800F;
            } else if (this.k14Distance < 200F) {
                this.k14Distance = 200F;
            }
        }
    }


    public void engineSurge(float f)
    {
      if (this.FM.AS.isMaster()) {
        if (this.curthrl == -1.0F)
        {
          this.curthrl = (this.oldthrl = this.FM.EI.engines[0].getControlThrottle());
        }
        else
        {
          this.curthrl = this.FM.EI.engines[0].getControlThrottle();
          if (this.curthrl < 1.05F)
          {
            if (((this.curthrl - this.oldthrl) / f > 20.0F) && (this.FM.EI.engines[0].getRPM() < 3200.0F) && (this.FM.EI.engines[0].getStage() == 6) && (World.Rnd().nextFloat() < 0.4F))
            {
              if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
              }
              playSound("weapon.MGunMk108s", true);
              this.engineSurgeDamage = ((float)(this.engineSurgeDamage + 0.01D * (this.FM.EI.engines[0].getRPM() / 1000.0F)));
              this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
              if ((World.Rnd().nextFloat() < 0.05F) && ((this.FM instanceof RealFlightModel)) && (((RealFlightModel)this.FM).isRealMode())) {
                this.FM.AS.hitEngine(this, 0, 100);
              }
              if ((World.Rnd().nextFloat() < 0.05F) && ((this.FM instanceof RealFlightModel)) && (((RealFlightModel)this.FM).isRealMode())) {
                this.FM.EI.engines[0].setEngineDies(this);
              }
            }
            if (((this.curthrl - this.oldthrl) / f < -20.0F) && ((this.curthrl - this.oldthrl) / f > -100.0F) && (this.FM.EI.engines[0].getRPM() < 3200.0F) && (this.FM.EI.engines[0].getStage() == 6))
            {
              playSound("weapon.MGunMk108s", true);
              this.engineSurgeDamage = ((float)(this.engineSurgeDamage + 0.001D * (this.FM.EI.engines[0].getRPM() / 1000.0F)));
              this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.engineSurgeDamage);
              if ((World.Rnd().nextFloat() < 0.4F) && ((this.FM instanceof RealFlightModel)) && (((RealFlightModel)this.FM).isRealMode()))
              {
                if (this.FM.actor == World.getPlayerAircraft()) {
                  HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                }
                this.FM.EI.engines[0].setEngineStops(this);
              }
              else if (this.FM.actor == World.getPlayerAircraft())
              {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
              }
            }
          }
          this.oldthrl = this.curthrl;
        }
      }
    }

    public void update(float f) {
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            this.bailout();
        }
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.45F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.65F) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) {
                        this.FM.AS.setSootState(this, 0, 3);
                    } else {
                        this.FM.AS.setSootState(this, 0, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, 0, 1);
                }
            } else if ((this.FM.EI.engines[0].getPowerOutput() <= 0.45F) || (this.FM.EI.engines[0].getStage() < 6)) {
                this.FM.AS.setSootState(this, 0, 0);
            }
            this.setExhaustFlame(Math.round(Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), 0);
        }
        this.engineSurge(f);
        this.typeFighterAceMakerRangeFinder();
        this.soundbarier();
        super.update(f);
    }

    public void doSetSootState(int engineIndex, int sootState) {
        if (engineIndex != 0) return;
        for (int i = 0; i < 2; i++) {
            if (this.FM.AS.astateSootEffects[0][i] != null) {
                Eff3DActor.finish(this.FM.AS.astateSootEffects[0][i]);
            }
            this.FM.AS.astateSootEffects[0][i] = null;
        }
        switch (sootState) {
            case 1:
                this.FM.AS.astateSootEffects[0][0] = Eff3DActor.New(this, findHook("_Engine1ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0F);
                this.FM.AS.astateSootEffects[0][1] = Eff3DActor.New(this, findHook("_Engine1ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0F);
                break;
            case 3:
                this.FM.AS.astateSootEffects[0][1] = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0F);
            case 2:
                this.FM.AS.astateSootEffects[0][0] = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1.0F);
                break;
            case 5:
                this.FM.AS.astateSootEffects[0][0] = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1.0F);
            case 4:
                this.FM.AS.astateSootEffects[0][1] = Eff3DActor.New(this, findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0F);
        }
    }

    public void setExhaustFlame(int exhaustFlame, int engineIndex) {
        if (engineIndex != 0) return;
        switch (exhaustFlame) {
            case 0:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 1:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 2:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 3:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
            case 4:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 5:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 6:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 7:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 8:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 9:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", true);
                hierMesh().chunkVisible("Exhaust5", false);
                break;
            case 10:
                hierMesh().chunkVisible("Exhaust1", true);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;
            case 11:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", true);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;
            case 12:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", true);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", true);
                break;
            default:
                hierMesh().chunkVisible("Exhaust1", false);
                hierMesh().chunkVisible("Exhaust2", false);
                hierMesh().chunkVisible("Exhaust3", false);
                hierMesh().chunkVisible("Exhaust4", false);
                hierMesh().chunkVisible("Exhaust5", false);
        }
    }
   
    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F)) {
                    this.FM.AS.astateBailoutStep = 11;
                    this.doRemoveBlisters();
                } else {
                    this.FM.AS.astateBailoutStep = 2;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 2) && (this.FM.AS.astateBailoutStep <= 3)) {
                switch (this.FM.AS.astateBailoutStep) {
                    case 2:
                        if (this.FM.CT.cockpitDoorControl < 0.5F) {
                            this.doRemoveBlister1();
                        }
                        break;

                    case 3:
                        this.doRemoveBlisters();
                        break;
                }
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                AircraftState aircraftstate = this.FM.AS;
                aircraftstate.astateBailoutStep = (byte) (aircraftstate.astateBailoutStep + 1);
                if (this.FM.AS.astateBailoutStep == 4) {
                    this.FM.AS.astateBailoutStep = 11;
                }
            } else if ((this.FM.AS.astateBailoutStep >= 11) && (this.FM.AS.astateBailoutStep <= 19)) {
                byte byte0 = this.FM.AS.astateBailoutStep;
                if (this.FM.AS.isMaster()) {
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                }
                AircraftState aircraftstate1 = this.FM.AS;
                aircraftstate1.astateBailoutStep = (byte) (aircraftstate1.astateBailoutStep + 1);
                if (byte0 == 11) {
                    this.FM.setTakenMortalDamage(true, null);
                    if ((this.FM instanceof Maneuver) && (((Maneuver) this.FM).get_maneuver() != 44)) {
                        World.cur();
                        if (this.FM.AS.actor != World.getPlayerAircraft()) {
                            ((Maneuver) this.FM).set_maneuver(44);
                        }
                    }
                }
                if (this.FM.AS.astatePilotStates[byte0 - 11] < 99) {
                    this.doRemoveBodyFromPlane(byte0 - 10);
                    if (byte0 == 11) {
                        this.doEjectCatapult();
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        this.overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        this.ejectComplete = true;
                        if ((byte0 > 10) && (byte0 <= 19)) {
                            EventLog.onBailedOut(this, byte0 - 11);
                        }
                    }
                }
            }
        }
    }

    private final void doRemoveBlister1() {
        if ((this.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    private final void doRemoveBlisters() {
        for (int i = 2; i < 10; i++) {
            if ((this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1) && (this.FM.AS.getPilotHealth(i - 1) > 0.0F)) {
                this.hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
        }

    }

    public float getAirPressure(float paramFloat)
    {
      return this.transsonicEffects.getAirPressure(paramFloat);
    }
    
    public float getAirPressureFactor(float paramFloat)
    {
      return this.transsonicEffects.getAirPressureFactor(paramFloat);
    }
    
    public float getAirDensity(float paramFloat)
    {
      return this.transsonicEffects.getAirDensity(paramFloat);
    }
    
    public float getAirDensityFactor(float paramFloat)
    {
      return this.transsonicEffects.getAirDensityFactor(paramFloat);
    }
    
    public float getMachForAlt(float paramFloat)
    {
      return this.transsonicEffects.getMachForAlt(paramFloat);
    }
    
    public float calculateMach()
    {
      return this.FM.getSpeedKMH() / getMachForAlt(this.FM.getAltitude());
    }
    
    public void soundbarier()
    {
      this.transsonicEffects.soundbarrier();
    }
    

    protected boolean          bSlatsOff;
    private float              oldthrl;
    private float              curthrl;
    public int                 k14Mode;
    public int                 k14WingspanType;
    public float               k14Distance;
    public float               AirBrakeControl;
    private boolean            overrideBailout;
    private boolean            ejectComplete;
    private float              lightTime;
    private float              ft;
    private LightPointWorld    lLight[];
    private Hook               lLightHook[];
    private static Loc         lLightLoc1             = new Loc();
    private static Point3d     lLightP1               = new Point3d();
    private static Point3d     lLightP2               = new Point3d();
    private static Point3d     lLightPL               = new Point3d();
    public static boolean      bChangedPit            = false;
    public static int          LockState              = 0;
    static Actor               hunted                 = null;
    private float              engineSurgeDamage;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR      = 2F;
    private static final float POS_G_RECOVERY_FACTOR  = 2F;
    public boolean             hasHydraulicPressure;
    public boolean             APmode1;
    public boolean             APmode2;
    private final TransonicEffects transsonicEffects;

    static {
        Class class1 = FJ_4.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
