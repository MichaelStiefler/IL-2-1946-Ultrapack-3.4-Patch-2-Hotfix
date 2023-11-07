package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;

public abstract class HE_162 extends Scheme1_JetSurge implements TypeFighter {

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
                    new EjectionSeat(1, loc, vector3d, aircraft);
                    return;
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    if (this.hierMesh().isChunkVisible("Blister1_D0")) {
                        this.hierMesh().chunkVisible("Gore1_D0", true);
                    }
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if ((this.FM.AS.astateTankStates[0] > 4) && (World.Rnd().nextFloat() < 0.07F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[0] > 4) && (World.Rnd().nextFloat() < 0.14F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateTankStates[0] > 4) && (World.Rnd().nextFloat() < 0.14F)) {
                this.FM.AS.hitTank(this, 2, 1);
            }
            if ((this.FM.AS.astateTankStates[1] > 4) && (World.Rnd().nextFloat() < 0.07F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[1] > 4) && (World.Rnd().nextFloat() < 0.14F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
            if ((this.FM.AS.astateTankStates[1] > 4) && (World.Rnd().nextFloat() < 0.07F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[1] > 4) && (World.Rnd().nextFloat() < 0.14F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -110F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 110F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -110F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC25_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC27_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC28_D0", 0.0F, 0.0F, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, -0.0833F, 0.0F);
        hiermesh.chunkSetLocate("GearC64_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f) {
        HE_162.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 0.0632F);
        if (this.FM.CT.getGear() > 0.99F) {
            Aircraft.ypr[1] = 40F * this.FM.CT.getRudder();
        }
        this.hierMesh().chunkSetLocate("GearC25_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearC27_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, -15F), 0.0F);
        this.hierMesh().chunkSetAngles("GearC28_D0", 0.0F, Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.0632F, 0.0F, 30F), 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveFan(float f) {
    }

    public void update(float f) {
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
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
        super.update(f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Pilot")) {
            this.killPilot(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat() < 0.05F) {
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
            }
            if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) {
                this.FM.AS.hitEngine(shot.initiator, 0, 5);
            }
        }
        if (shot.chunkName.startsWith("CF")) {
            if (World.Rnd().nextFloat() < 0.1F) {
                this.FM.AS.hitTank(shot.initiator, 0, 1);
            }
            if (World.Rnd().nextFloat() < 0.1F) {
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                }
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                }
            }
        }
        super.msgShot(shot);
    }

    static {
        Class class1 = HE_162.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
