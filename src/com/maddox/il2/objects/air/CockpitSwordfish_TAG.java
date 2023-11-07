package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.rts.Property;

public class CockpitSwordfish_TAG extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            ((Swordfish) this.fm.actor).bPitUnfocused = false;
            this.aircraft().hierMesh().chunkVisible("Gunsight_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            ((Swordfish) this.fm.actor).bPitUnfocused = true;
            this.aircraft().hierMesh().chunkVisible("Gunsight_D0", true);
            super.doFocusLeave();
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.cockpitAirBrakePos = (0.9F * this.cockpitAirBrakePos) + (0.1F * ((Swordfish) this.fm.actor).airBrakePos);
        this.elevTurretA = 70F * (1.0F - this.cockpitAirBrakePos);
        this.mesh.chunkSetAngles("TurrBase", 0.0F, this.elevTurretA, 0.0F);
        this.mesh.chunkSetAngles("TurrBase1", this.elevTurretA, 0.0F, 0.0F);
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.yawTurretA = orient.getYaw();
        this.mesh.chunkSetAngles("Turret1A", 0.0F, -this.yawTurretA, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) {
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -70F) {
            f = -70F;
        }
        if (f > 70F) {
            f = 70F;
        }
        if (f1 > 70F) {
            f1 = 70F;
        }
        if (f1 < -45F) {
            f1 = -45F;
        }
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) {
            return;
        }
        if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
            this.bGunFire = false;
        }
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.hook1 == null) {
                this.hook1 = new HookNamed(this.aircraft(), "_turret1");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_turret1");
            if (this.iCocking > 0) {
                this.iCocking = 0;
            } else {
                this.iCocking = 1;
            }
        } else {
            this.iCocking = 0;
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) {
            return;
        }
        if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
            this.bGunFire = false;
        } else {
            this.bGunFire = flag;
        }
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        this.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D2o"));
        this.mesh.materialReplace("Matt1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt2D0o"));
        this.mesh.materialReplace("Matt2D0o", mat);
    }

    public CockpitSwordfish_TAG() {
        super("3DO/Cockpit/SwordfishTAG/hier.him", "he111_gunner");
        this.yawTurretA = 0.0F;
        this.cockpitAirBrakePos = 0.0F;
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.hook1 = null;
        this.iCocking = 0;
    }

    private float   elevTurretA;
    private float   yawTurretA;
    private float   cockpitAirBrakePos;
    public Vector3f w;
    private boolean bNeedSetUp;
    private Hook    hook1;
    private int     iCocking;

    static {
        Property.set(CockpitSwordfish_TAG.class, "aiTuretNum", 0);
        Property.set(CockpitSwordfish_TAG.class, "weaponControlNum", 10);
        Property.set(CockpitSwordfish_TAG.class, "astatePilotIndx", 2);
    }
}
