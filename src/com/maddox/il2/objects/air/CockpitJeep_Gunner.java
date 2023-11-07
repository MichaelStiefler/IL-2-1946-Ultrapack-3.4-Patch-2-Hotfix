package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.Aircraft._WeaponSlot;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.Property;

public class CockpitJeep_Gunner extends CockpitGunner {

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", 0.0F, orient.getYaw(), 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (this.isRealMode()) {
            if (!this.aiTurret().bIsOperable) {
                orient.setYPR(0.0F, 0.0F, 0.0F);
            } else {
                float f = orient.getYaw();
                float f1 = orient.getTangage();
                if (f1 > 50F) {
                    f1 = 50F;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                }
                orient.setYPR(f, f1, 0.0F);
                orient.wrap();
            }
        }
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) {
                    this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
                }
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (this.isRealMode()) {
            if ((this.emitter != null) && this.emitter.haveBullets() && this.aiTurret().bIsOperable) {
                this.bGunFire = flag;
            } else {
                this.bGunFire = false;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public CockpitJeep_Gunner() {
        super("3DO/Cockpit/Jeep-Gun/hier.him", "he111_gunner");
        this.hook1 = null;
        this.bNeedSetUp = true;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("ammo", false);
            this.aircraft().hierMesh().chunkVisible("ammo2", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Helm_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Helm2_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        this.aircraft().hierMesh().chunkVisible("ammo", true);
        this.aircraft().hierMesh().chunkVisible("ammo2", true);
        if (!this.aircraft().hierMesh().isChunkVisible("Pilot1_D1")) {
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", true);
            this.aircraft().hierMesh().chunkVisible("Helm_D0", true);
        }
        if (!this.aircraft().hierMesh().isChunkVisible("Pilot2_D1")) {
            this.aircraft().hierMesh().chunkVisible("Pilot2_D0", true);
            this.aircraft().hierMesh().chunkVisible("Head2_D0", true);
            this.aircraft().hierMesh().chunkVisible("Helm2_D0", true);
        }
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public boolean isEnableFocusing() {
        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(this.aircraft().getClass(), this.aircraft().thisWeaponsName);

        if ((weaponSlotsRegistered != null) && (weaponSlotsRegistered.length > 0) && (weaponSlotsRegistered[0] != null)) {
            return super.isEnableFocusing();
        }
        HotKeyCmd.exec("aircraftView", "cockpitSwitch0");
        return false;
    }

    private Hook    hook1;
    private boolean bNeedSetUp;

    static {
        Property.set(CockpitKuebel_Gunner.class, "normZN", 0.1F);
        Property.set(CockpitKuebel_Gunner.class, "aiTuretNum", 0);
        Property.set(CockpitKuebel_Gunner.class, "weaponControlNum", 10);
        Property.set(CockpitKuebel_Gunner.class, "astatePilotIndx", 1);
    }
}
