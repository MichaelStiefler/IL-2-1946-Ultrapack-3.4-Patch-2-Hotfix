package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class P_55xyz extends Scheme1 implements TypeFighter, TypeStormovik {
    public float getEyeLevelCorrection() {
        return 0.025F;
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

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -25F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 25F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 100F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 100F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -Math.max(-f * 1500F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
    }

    protected void moveGear(float f) {
        P_55xyz.moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -31F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, 31F * f, 0.0F);
        if (this.FM.CT.getGear() == 1.0F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", -40F * f, 0.0F, 0.0F);
        }
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 30F * f, 0.0F);
        this.updateControlsVisuals();
    }

    private final void updateControlsVisuals() {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, (-30F * this.FM.CT.getAileron()) + (30F * this.FM.CT.getElevator()), 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, (-30F * this.FM.CT.getAileron()) - (30F * this.FM.CT.getElevator()), 0.0F);
        this.hierMesh().chunkSetAngles("AroneLo_D0", 0.0F, (-30F * this.FM.CT.getAileron()) + (30F * this.FM.CT.getElevator()), 0.0F);
        this.hierMesh().chunkSetAngles("AroneRo_D0", 0.0F, (-30F * this.FM.CT.getAileron()) - (30F * this.FM.CT.getElevator()), 0.0F);
    }

    protected void moveAileron(float f) {
        this.updateControlsVisuals();
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && (this.FM.AS.astateBailoutStep == 2)) {
            this.doRemoveProp();
        }
    }

    private final void doRemoveProp() {
        this.oldProp[1] = 99;
        if (this.hierMesh().isChunkVisible("PropRot1_D0")) {
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Prop1_D0"));
            Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE, 3F);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("Prop1_D1", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
            this.FM.EI.engines[0].setEngineDies(this);
        }
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)) {
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("WingRIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)) {
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("CF")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.3F) {
                this.FM.AS.hitEngine(shot.initiator, 0, 1);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if (World.Rnd().nextFloat() < 0.03F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
        }
        if (shot.chunkName.startsWith("Nose1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.06F) && (Aircraft.Pd.z > 0.48D)) {
            this.FM.AS.setJamBullets(0, 0);
            this.FM.AS.setJamBullets(0, 1);
        }
        if (shot.chunkName.startsWith("Oil") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F)) {
            this.FM.AS.hitOil(shot.initiator, 0);
        }
        if (shot.chunkName.startsWith("Pilot")) {
            if ((shot.power * Math.abs(Aircraft.v1.x)) > 12070D) {
                this.FM.AS.hitPilot(shot.initiator, 0, (int) (shot.mass * 1000F * 0.5F));
            }
            if (Aircraft.Pd.z > 1.0117499828338623D) {
                this.killPilot(shot.initiator, 0);
                if ((shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                    HUD.logCenter("H E A D S H O T");
                }
            }
            return;
        } else {
            super.msgShot(shot);
            return;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    static {
        Class class1 = P_55xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
