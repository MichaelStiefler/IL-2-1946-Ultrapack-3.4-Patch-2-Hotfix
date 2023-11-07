package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.sas1946.il2.util.TrueRandom;

public abstract class Scheme1_JetSurge extends Scheme1 {
    protected Scheme1_JetSurge() {
        this.jS_throttlePrevTick = -1;
        this.jS_throttleCurrentTick = -1;
        this.jS_engineSurgeDamage = 0;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster()) {
            this.engineSurge(f);
        }
    }

    public void engineSurge(float f) {
        if (this.hasCustomSurge()) {
            return;
        }

        if (this.jS_throttleCurrentTick == -1F) {
            this.jS_throttleCurrentTick = this.jS_throttlePrevTick = this.FM.EI.engines[0].getControlThrottle();
            return;
        }
        if (this.FM.EI.engines[0].type != Motor._E_TYPE_JET) return; // Only Jets have surge effect
        
        this.jS_throttleCurrentTick = this.FM.EI.engines[0].getControlThrottle();
        if ((((this.jS_throttleCurrentTick - this.jS_throttlePrevTick) / f) > this.getSurgeThrottleLimit()) && (this.FM.EI.engines[0].getRPM() < 3200F) && (this.FM.EI.engines[0].getStage() == Motor._E_STAGE_NOMINAL) && (World.Rnd().nextFloat() < 0.5F)) {
            if (this.FM.actor == World.getPlayerAircraft()) {
                HUD.log("Compressor Stall!");
            }
            this.playSound("models.jetEngineSurge", true);
            this.jS_engineSurgeDamage += (0.01F * this.FM.EI.engines[0].getRPM()) / 1000F;
            this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.jS_engineSurgeDamage);
            if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                if (TrueRandom.nextFloat() < 0.2F) {
                    this.FM.AS.hitEngine(this, 0, 100);
                }
                if (TrueRandom.nextFloat() < 0.2F) {
                    this.FM.EI.engines[0].setEngineDies(this);
                }
            }
        }
        if ((((this.jS_throttleCurrentTick - this.jS_throttlePrevTick) / f) < (-1F * this.getSurgeThrottleLimit())) && (((this.jS_throttleCurrentTick - this.jS_throttlePrevTick) / f) > -100F) && (this.FM.EI.engines[0].getRPM() < 3200F) && (this.FM.EI.engines[0].getStage() == Motor._E_STAGE_NOMINAL)) {
            this.playSound("models.jetEngineSurge", true);
            this.jS_engineSurgeDamage += (0.001F * this.FM.EI.engines[0].getRPM()) / 1000F;
            this.FM.EI.engines[0].doSetReadyness(this.FM.EI.engines[0].getReadyness() - this.jS_engineSurgeDamage);
            if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                if (TrueRandom.nextFloat() < 0.5F) {
                    if (this.FM.actor == World.getPlayerAircraft()) {
                        HUD.log("Engine Flameout!");
                    }
                    this.FM.EI.engines[0].setEngineStops(this);
                } else {
                    if (this.FM.actor == World.getPlayerAircraft()) {
                        HUD.log("Compressor Stall!");
                    }
                    this.FM.EI.engines[0].setKillCompressor(this);
                }
            }
        }
        this.jS_throttlePrevTick = this.jS_throttleCurrentTick;
    }

    public boolean hasCustomSurge() {
        return false;
    }

    public int getSurgeThrottleLimit() {
        return 8;
    }

    private float jS_throttlePrevTick;
    private float jS_throttleCurrentTick;
    private float jS_engineSurgeDamage;
}
