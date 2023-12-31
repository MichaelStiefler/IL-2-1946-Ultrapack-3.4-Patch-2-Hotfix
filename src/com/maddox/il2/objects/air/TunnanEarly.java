package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TunnanEarly extends Scheme1 implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker {

    public TunnanEarly() {
        this.overrideBailout = false;
        this.ejectComplete = false;
        this.lTimeNextEject = 0L;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);

        if ((weaponSlotsRegistered == null) || (weaponSlotsRegistered.length < 16)) {
            return;
        }
        
        hierMesh.chunkVisible("PilonL4_D0", weaponSlotsRegistered[8] != null);
        hierMesh.chunkVisible("PilonR4_D0", weaponSlotsRegistered[9] != null);
        hierMesh.chunkVisible("PilonL3_D0", weaponSlotsRegistered[10] != null);
        hierMesh.chunkVisible("PilonR3_D0", weaponSlotsRegistered[11] != null);
        hierMesh.chunkVisible("PilonL2_D0", weaponSlotsRegistered[12] != null);
        hierMesh.chunkVisible("PilonR2_D0", weaponSlotsRegistered[13] != null);
        hierMesh.chunkVisible("PilonL1_D0", weaponSlotsRegistered[14] != null);
        hierMesh.chunkVisible("PilonR1_D0", weaponSlotsRegistered[15] != null);
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

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -65F), 0.0F);
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, 0.65F);
        hiermesh.chunkSetLocate("GearL5_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -65F), 0.0F);
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, 0.65F);
        hiermesh.chunkSetLocate("GearR5_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -60F), 0.0F);
    }

    protected void moveGear(float f) {
        TunnanEarly.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() > 0.8F) {
            this.hierMesh().chunkSetAngles("GearC6_D0", 0.0F, f, 0.0F);
        }
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.28F, 0.0F, 0.295F);
        this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.28F, 0.0F, -48F), 0.0F);
        this.hierMesh().chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.28F, 0.0F, -96F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.28F, 0.0F, 0.295F);
        this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.28F, 0.0F, -48F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.28F, 0.0F, -96F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.1795F, 0.0F, 0.1795F);
        this.hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 35F * f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.endsWith("p1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(12F, 19F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("p2")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(12.7F, 12.7F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                } else if (s.endsWith("g1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 60F) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    if (shot.power <= 0.0F) {
                        this.doRicochetBack(shot);
                    }
                }
            } else if (s.startsWith("xxcontrols")) {
                this.debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch (i) {
                    case 1:
                    case 2:
                        if ((this.getEnergyPastArmor(0.99F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Ailerones Controls: Out..");
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                        }
                        break;

                    case 3:
                    case 4:
                        if ((this.getEnergyPastArmor(1.22F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                        }
                        if ((this.getEnergyPastArmor(1.22F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                            this.debuggunnery("Controls: Elevator Controls: Disabled..");
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
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
                if (s.endsWith("exht")) {
                    ;
                }
            } else if (s.startsWith("xxhyd")) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            } else if (s.startsWith("xxmgun0")) {
                int j = s.charAt(7) - 49;
                if (this.getEnergyPastArmor(0.5F, shot) > 0.0F) {
                    this.debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    this.FM.AS.setJamBullets(0, j);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else if (s.startsWith("xxpnm")) {
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            } else if (s.startsWith("xxspar")) {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
                }
                if (s.startsWith("xxsparri") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                }
                if (s.startsWith("xxsparro") && (World.Rnd().nextFloat(0.0F, 0.115F) < shot.mass) && (this.getEnergyPastArmor(6.8F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spar Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                }
                if (s.startsWith("xxspark") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor((6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            } else if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.1F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    if (this.FM.AS.astateTankStates[k] == 0) {
                        this.debuggunnery("Fuel Tank (" + k + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, k, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.1F)) {
                        this.FM.AS.hitTank(shot.initiator, k, 2);
                        this.debuggunnery("Fuel Tank (" + k + "): Hit..");
                    }
                }
            }
        } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
            this.hitChunk("CF", shot);
            if (s.startsWith("xcockpit")) {
                if (point3d.x > 2D) {
                    if (World.Rnd().nextFloat() < 0.2F) {
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                    }
                } else {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
            } else if (point3d.x > 2.5D) {
                if (World.Rnd().nextFloat() < 0.2F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
            } else if (point3d.y > 0.0D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) {
                this.hitChunk("Nose", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl")) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr")) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
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
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 1)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 1)) {
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
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else {
                l = s.charAt(5) - 49;
            }
            this.hitFlesh(l, shot, byte0);
        }
    }

    public void update(float f) {
        if (Config.isUSE_RENDER() && this.FM.AS.isMaster()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) {
                    this.FM.AS.setSootState(this, 0, 3);
                } else {
                    this.FM.AS.setSootState(this, 0, 2);
                }
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
        this.setExhaustFlame((int) Aircraft.cvt(this.FM.EI.engines[0].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F), 0);
        if ((this.FM.AS.bIsAboutToBailout || this.overrideBailout) && !this.ejectComplete && (this.FM.getSpeedKMH() > 15F)) {
            this.overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if (Time.current() > this.lTimeNextEject) {
                this.bailout();
            }
        }
        super.update(f);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.FM.EI.engines[0].setEngineDies(actor);
                return super.cutFM(i, j, actor);

            case 11:
                this.cut("StabL");
                this.cut("StabR");
                this.FM.cut(17, j, actor);
                this.FM.cut(18, j, actor);
                break;

            case 13:
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 500F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        TunnanEarly.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public void setExhaustFlame(int i, int j) {
        if (j == 0) {
            switch (i) {
                case 0:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 1:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 2:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 3:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    // fall through

                case 4:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 5:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 6:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 7:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", true);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 8:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", true);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 9:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", true);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;

                case 10:
                    this.hierMesh().chunkVisible("Exhaust1", true);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", true);
                    break;

                case 11:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", true);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", true);
                    break;

                case 12:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", true);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", true);
                    break;

                default:
                    this.hierMesh().chunkVisible("Exhaust1", false);
                    this.hierMesh().chunkVisible("Exhaust2", false);
                    this.hierMesh().chunkVisible("Exhaust3", false);
                    this.hierMesh().chunkVisible("Exhaust4", false);
                    this.hierMesh().chunkVisible("Exhaust5", false);
                    break;
            }
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 1.0F, 0.0F, 0.53F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
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
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + this.k14WingspanType);
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

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (!Actor.isValid(aircraft)) {
                    return;
                } else {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(12, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
    }

    private void bailout() {
        if (this.overrideBailout) {
            if ((this.FM.AS.astateBailoutStep >= 0) && (this.FM.AS.astateBailoutStep < 2)) {
                if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.CT.getCockpitDoor() > 0.5F)) {
                    this.FM.AS.astateBailoutStep = 11;
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
                        this.lTimeNextEject = Time.current() + 1000L;
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
                        this.lTimeNextEject = Time.current() + 1000L;
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
                        return;
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

    private long    lTimeNextEject;
    private boolean ejectComplete;
    private boolean overrideBailout;
    public int      k14Mode;
    public int      k14WingspanType;
    public float    k14Distance;

    static {
        Class class1 = TunnanEarly.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Tunnan");
        Property.set(class1, "meshName", "3DO/Plane/TunnanEarly(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1948F);
        Property.set(class1, "yearExpired", 1971.5F);
        Property.set(class1, "FlightModel", "FlightModels/Tunnan.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTunnan.class });
        Property.set(class1, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14" });
    }
}
