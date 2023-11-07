package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitLI2_TGunner extends CockpitGunner {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", this.aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
        super.doFocusLeave();
    }

    protected void reflectPlaneMats() {
        this.mesh.materialReplace("Gloss1D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D0o")));
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        while (f < -180F) {
            f += 360F;
        }
        while (f > 180F) {
            f -= 360F;
        }
        while (this.prevA0 < -180F) {
            this.prevA0 += 360F;
        }
        while (this.prevA0 > 180F) {
            this.prevA0 -= 360F;
        }
        if (!this.isRealMode()) {
            this.prevA0 = f;
        } else {
            if (this.bNeedSetUp2) {
                this.prevTime = Time.current() - 1L;
                this.bNeedSetUp2 = false;
            }
            if ((f < -120F) && (this.prevA0 > 120F)) {
                f += 360F;
            } else if ((f > 120F) && (this.prevA0 < -120F)) {
                this.prevA0 += 360F;
            }
            float f3 = f - this.prevA0;
            float f4 = 0.001F * (Time.current() - this.prevTime);
            float f5 = Math.abs(f3 / f4);
            if (f5 > 120F) {
                if (f > this.prevA0) {
                    f = this.prevA0 + (120F * f4);
                } else if (f < this.prevA0) {
                    f = this.prevA0 - (120F * f4);
                }
            }
            this.prevTime = Time.current();
            if (f1 > 73F) {
                f1 = 73F;
            }
            if (f1 < 0.0F) {
                f1 = 0.0F;
            }
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            this.prevA0 = f;
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
            if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
                this.bGunFire = false;
            } else {
                this.bGunFire = flag;
            }
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
    }

    public CockpitLI2_TGunner() {
        super("3DO/Cockpit/LI2-TGun/TGunnerLI2.him", "he111_gunner");
        this.bNeedSetUp = true;
        this.bNeedSetUp2 = true;
        this.prevTime = -1L;
        this.prevA0 = 0.0F;
        this.hook1 = null;
    }

    private boolean bNeedSetUp;
    private boolean bNeedSetUp2;
    private long    prevTime;
    private float   prevA0;
    private Hook    hook1;

    static {
        Property.set(CockpitLI2_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitLI2_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitLI2_TGunner.class, "astatePilotIndx", 1);
    }
}
