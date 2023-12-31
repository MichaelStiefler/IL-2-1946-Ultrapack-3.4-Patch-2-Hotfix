package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitBLENHEIM_BGunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret2A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret2B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) {
            return;
        }
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -40F) {
            f = -40F;
        }
        if (f > 40F) {
            f = 40F;
        }
        if (f1 > 1.0F) {
            f1 = 1.0F;
        }
        if (f1 < -40F) {
            f1 = -40F;
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
                this.hook1 = new HookNamed(this.aircraft(), "_MGUN09");
            }
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN09");
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

    public CockpitBLENHEIM_BGunner() {
        super("3DO/Cockpit/PZL23B_BGun/hier_blenheim.him", "he111_gunner");
        this.hook1 = null;
        this.bNeedSetUp = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            this.aircraft().hierMesh().chunkVisible("TurretBase3A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret3A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Blister3A_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
        this.aircraft().hierMesh().chunkVisible("TurretBase3A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret3A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Blister3A_D0", true);
        super.doFocusLeave();
    }

    private Hook    hook1;
    private boolean bNeedSetUp;

    static {
        Class class1 = CockpitBLENHEIM_BGunner.class;
        Property.set(class1, "normZN", 0.4F);
        Property.set(class1, "aiTuretNum", 1);
        Property.set(class1, "weaponControlNum", 11);
        Property.set(class1, "astatePilotIndx", 1);
    }
}
