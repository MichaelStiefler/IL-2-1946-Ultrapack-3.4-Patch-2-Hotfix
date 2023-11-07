package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.sas1946.il2.util.TrueRandom;

public abstract class Scheme2_JetSurge extends Scheme1 {
    protected Scheme2_JetSurge() {
        this.jS_initDone = false;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster()) {
            this.engineSurge(f);
        }
    }

    private void checkInitEngineSurgeParams() {
        if (this.jS_initDone) {
            return;
        }
        int numEngines = this.FM.EI.engines.length;

        this.jS_throttlePrevTick = new float[numEngines];
        this.jS_throttleCurrentTick = new float[numEngines];
        this.jS_engineSurgeDamage = new float[numEngines];

        for (int engineIndex = 0; engineIndex < numEngines; engineIndex++) {
            this.jS_throttlePrevTick[engineIndex] = -1F;
            this.jS_throttleCurrentTick[engineIndex] = -1F;
            this.jS_engineSurgeDamage[engineIndex] = -1F;
        }
        this.jS_initDone = true;
    }

    public void engineSurge(float f) {
        this.checkInitEngineSurgeParams();
        if (this.hasCustomSurge()) {
            return;
        }

        for (int engineIndex = 0; engineIndex < this.FM.EI.engines.length; engineIndex++) {
            if (this.FM.EI.engines[engineIndex].type != Motor._E_TYPE_JET) continue; // Only Jets have surge effect
            if (this.jS_throttleCurrentTick[engineIndex] == -1F) {
                this.jS_throttleCurrentTick[engineIndex] = this.jS_throttlePrevTick[engineIndex] = this.FM.EI.engines[engineIndex].getControlThrottle();
            } else {
                this.jS_throttleCurrentTick[engineIndex] = this.FM.EI.engines[engineIndex].getControlThrottle();
                if ((((this.jS_throttleCurrentTick[engineIndex] - this.jS_throttlePrevTick[engineIndex]) / f) > this.getSurgeThrottleLimit()) && (this.FM.EI.engines[engineIndex].getRPM() < 3200F) && (this.FM.EI.engines[engineIndex].getStage() == Motor._E_STAGE_NOMINAL) && (World.Rnd().nextFloat() < 0.5F)) {
                    if (this.FM.actor == World.getPlayerAircraft()) {
                        HUD.log("Compressor Stall!");
                    }
                    this.playSound("models.jetEngineSurge", true);
                    this.jS_engineSurgeDamage[engineIndex] += (0.01F * this.FM.EI.engines[engineIndex].getRPM()) / 1000F;
                    this.FM.EI.engines[engineIndex].doSetReadyness(this.FM.EI.engines[engineIndex].getReadyness() - this.jS_engineSurgeDamage[engineIndex]);
                    if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                        if (TrueRandom.nextFloat() < 0.2F) {
                            this.FM.AS.hitEngine(this, engineIndex, 100);
                        }
                        if (TrueRandom.nextFloat() < 0.2F) {
                            this.FM.EI.engines[engineIndex].setEngineDies(this);
                        }
                    }
                }
                if ((((this.jS_throttleCurrentTick[engineIndex] - this.jS_throttlePrevTick[engineIndex]) / f) < (-1F * this.getSurgeThrottleLimit())) && (((this.jS_throttleCurrentTick[engineIndex] - this.jS_throttlePrevTick[engineIndex]) / f) > -100F) && (this.FM.EI.engines[engineIndex].getRPM() < 3200F) && (this.FM.EI.engines[engineIndex].getStage() == Motor._E_STAGE_NOMINAL)) {
                    this.playSound("models.jetEngineSurge", true);
                    this.jS_engineSurgeDamage[engineIndex] += (0.001F * this.FM.EI.engines[engineIndex].getRPM()) / 1000F;
                    this.FM.EI.engines[engineIndex].doSetReadyness(this.FM.EI.engines[engineIndex].getReadyness() - this.jS_engineSurgeDamage[engineIndex]);
                    if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                        if (TrueRandom.nextFloat() < 0.5F) {
                            if (this.FM.actor == World.getPlayerAircraft()) {
                                HUD.log("Engine Flameout!");
                            }
                            this.FM.EI.engines[engineIndex].setEngineStops(this);
                        } else {
                            if (this.FM.actor == World.getPlayerAircraft()) {
                                HUD.log("Compressor Stall!");
                            }
                            this.FM.EI.engines[engineIndex].setKillCompressor(this);
                        }
                    }
                }
                this.jS_throttlePrevTick[engineIndex] = this.jS_throttleCurrentTick[engineIndex];
            }
        }
    }

    public boolean hasCustomSurge() {
        return false;
    }

    public int getSurgeThrottleLimit() {
        return 8;
    }

    private float[] jS_throttlePrevTick;
    private float[] jS_throttleCurrentTick;
    private float[] jS_engineSurgeDamage;
    private boolean jS_initDone;
}
