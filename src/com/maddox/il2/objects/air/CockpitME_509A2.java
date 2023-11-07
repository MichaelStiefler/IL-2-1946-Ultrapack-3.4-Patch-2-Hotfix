package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitME_509A2 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitME_509A2.this.setTmp = CockpitME_509A2.this.setOld;
            CockpitME_509A2.this.setOld = CockpitME_509A2.this.setNew;
            CockpitME_509A2.this.setNew = CockpitME_509A2.this.setTmp;
            CockpitME_509A2.this.setNew.altimeter = CockpitME_509A2.this.fm.getAltitude();
            boolean flag = (CockpitME_509A2.this.fm.EI.engines[0].getStage() > 0) && (CockpitME_509A2.this.fm.EI.engines[0].getStage() < 7);
            boolean flag1 = (CockpitME_509A2.this.fm.EI.engines[0].getStage() == 1) || (CockpitME_509A2.this.fm.EI.engines[0].getStage() == 2);
            if (flag) {
                if (CockpitME_509A2.this.setNew.masterPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.masterPosition = CockpitME_509A2.this.setOld.masterPosition + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.reviPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.reviPosition = CockpitME_509A2.this.setOld.reviPosition - 0.5F;
                }
                if (CockpitME_509A2.this.setNew.fuelPump0Position < 1.0F) {
                    CockpitME_509A2.this.setNew.fuelPump0Position = CockpitME_509A2.this.setOld.fuelPump0Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.fuelSelectorPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.fuelSelectorPosition = CockpitME_509A2.this.setOld.fuelSelectorPosition - 0.03F;
                }
                if (CockpitME_509A2.this.setNew.masterSwitchPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.masterSwitchPosition = CockpitME_509A2.this.setOld.masterSwitchPosition + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.oxyPressurePosition < 1.0F) {
                    CockpitME_509A2.this.setNew.oxyPressurePosition = CockpitME_509A2.this.setOld.oxyPressurePosition + 0.01F;
                }
                if (CockpitME_509A2.this.setNew.circuitBreakers0Position < 1.0F) {
                    CockpitME_509A2.this.setNew.circuitBreakers0Position = CockpitME_509A2.this.setOld.circuitBreakers0Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.generator0Position < 1.0F) {
                    CockpitME_509A2.this.setNew.generator0Position = CockpitME_509A2.this.setOld.generator0Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.radioSwitch0Position < 1.0F) {
                    CockpitME_509A2.this.setNew.radioSwitch0Position = CockpitME_509A2.this.setOld.radioSwitch0Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.circuitBreakers2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.circuitBreakers2Position = CockpitME_509A2.this.setOld.circuitBreakers2Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.radioSwitch2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.radioSwitch2Position = CockpitME_509A2.this.setOld.radioSwitch2Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.generator2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.generator2Position = CockpitME_509A2.this.setOld.generator2Position + 0.1F;
                }
            } else {
                if (CockpitME_509A2.this.setNew.masterPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.masterPosition = CockpitME_509A2.this.setOld.masterPosition - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.reviPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.reviPosition = CockpitME_509A2.this.setOld.reviPosition + 0.5F;
                }
                if (CockpitME_509A2.this.setNew.fuelPump0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.fuelPump0Position = CockpitME_509A2.this.setOld.fuelPump0Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.fuelSelectorPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.fuelSelectorPosition = CockpitME_509A2.this.setOld.fuelSelectorPosition + 0.03F;
                }
                if (CockpitME_509A2.this.setNew.masterSwitchPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.masterSwitchPosition = CockpitME_509A2.this.setOld.masterSwitchPosition - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.oxyPressurePosition > 0.0F) {
                    CockpitME_509A2.this.setNew.oxyPressurePosition = CockpitME_509A2.this.setOld.oxyPressurePosition - 0.01F;
                }
                if (CockpitME_509A2.this.setNew.circuitBreakers0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.circuitBreakers0Position = CockpitME_509A2.this.setOld.circuitBreakers0Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.generator0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.generator0Position = CockpitME_509A2.this.setOld.generator0Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.radioSwitch0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.radioSwitch0Position = CockpitME_509A2.this.setOld.radioSwitch0Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.circuitBreakers2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.circuitBreakers2Position = CockpitME_509A2.this.setOld.circuitBreakers2Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.fuelPump2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.fuelPump2Position = CockpitME_509A2.this.setOld.fuelPump2Position + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.fuelPump2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.fuelPump2Position = CockpitME_509A2.this.setOld.fuelPump2Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.radioSwitch2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.radioSwitch2Position = CockpitME_509A2.this.setOld.radioSwitch2Position - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.generator2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.generator2Position = CockpitME_509A2.this.setOld.generator2Position - 0.1F;
                }
            }
            if (flag1) {
                if (CockpitME_509A2.this.setNew.starterPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.starterPosition = CockpitME_509A2.this.setOld.starterPosition + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.starter2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.starter2Position = CockpitME_509A2.this.setOld.starter2Position + 0.1F;
                }
            } else {
                if (CockpitME_509A2.this.setNew.starterPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.starterPosition = CockpitME_509A2.this.setOld.starterPosition - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.starter2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.starter2Position = CockpitME_509A2.this.setOld.starter2Position - 0.1F;
                }
            }
            if (CockpitME_509A2.this.setNew.masterArmPosition < 1.0F) {
                CockpitME_509A2.this.setNew.masterArmPosition = CockpitME_509A2.this.setOld.masterArmPosition + 0.5F;
            }
            if (flag) {
                for (int i = 0; i < CockpitME_509A2.NUM_COUNTERS; i++) {
                    if (!CockpitME_509A2.this.fm.CT.WeaponControl[CockpitME_509A2.this.triggerForBreechControl[i]]) {
                        if (CockpitME_509A2.this.setNew.shotPosition[i] < 1.0F) {
                            CockpitME_509A2.this.setNew.shotPosition[i] += 0.5F;
                        }
                    } else if ((CockpitME_509A2.this.gun[i] != null) && CockpitME_509A2.this.gun[i].haveBullets()) {
                        boolean flag3 = false;
                        long l = Time.current();
                        if (CockpitME_509A2.this.timePerShot[i] < (l - CockpitME_509A2.this.lastBreechTime[i])) {
                            flag3 = true;
                            CockpitME_509A2.this.lastBreechTime[i] = l;
                        }
                        if (flag3) {
                            CockpitME_509A2.this.setNew.shotPosition[i] = CockpitME_509A2.this.setNew.shotPosition[i] > 0.5F ? 0.0F : 1.0F;
                            CockpitME_509A2.this.setOld.shotPosition[i] = CockpitME_509A2.this.setNew.shotPosition[i];
                        }
                    } else if (CockpitME_509A2.this.setNew.shotPosition[i] > 0.0F) {
                        CockpitME_509A2.this.setNew.shotPosition[i] -= 0.5F;
                    }
                }

            } else {
                for (int j = 0; j < CockpitME_509A2.NUM_COUNTERS; j++) {
                    if (CockpitME_509A2.this.setNew.shotPosition[j] > 0.0F) {
                        CockpitME_509A2.this.setNew.shotPosition[j] = CockpitME_509A2.this.setOld.shotPosition[j] - 0.5F;
                    }
                }

            }
            if (CockpitME_509A2.this.fm.CT.GearControl == 1.0F) {
                if (CockpitME_509A2.this.setNew.gearLeverPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.gearLeverPosition = CockpitME_509A2.this.setOld.gearLeverPosition - 0.5F;
                }
            } else if (CockpitME_509A2.this.setNew.gearLeverPosition < 1.0F) {
                CockpitME_509A2.this.setNew.gearLeverPosition = CockpitME_509A2.this.setOld.gearLeverPosition + 0.5F;
            }
            if ((CockpitME_509A2.this.fm.CT.GearControl == 0.0F) && (CockpitME_509A2.this.fm.CT.getGear() > 0.5F)) {
                if (CockpitME_509A2.this.setNew.gearUpPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.gearUpPosition = CockpitME_509A2.this.setOld.gearUpPosition - 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.gearUpPosition < 1.0F) {
                CockpitME_509A2.this.setNew.gearUpPosition = CockpitME_509A2.this.setOld.gearUpPosition + 0.1F;
            }
            if ((CockpitME_509A2.this.fm.CT.GearControl == 1.0F) && (CockpitME_509A2.this.fm.CT.getGear() < 0.5F)) {
                if (CockpitME_509A2.this.setNew.gearDownPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.gearDownPosition = CockpitME_509A2.this.setOld.gearDownPosition - 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.gearDownPosition < 1.0F) {
                CockpitME_509A2.this.setNew.gearDownPosition = CockpitME_509A2.this.setOld.gearDownPosition + 0.1F;
            }
            if (CockpitME_509A2.this.fm.CT.BayDoorControl == 0.0F) {
                if (CockpitME_509A2.this.setNew.kg13TriggerPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.kg13TriggerPosition = CockpitME_509A2.this.setOld.kg13TriggerPosition - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.kg13TriggerPosition < 1.0F) {
                CockpitME_509A2.this.setNew.kg13TriggerPosition = CockpitME_509A2.this.setOld.kg13TriggerPosition + 0.05F;
            }
            if (!CockpitME_509A2.this.fm.CT.getRadiatorControlAuto()) {
                if (CockpitME_509A2.this.setNew.radiatorPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.radiatorPosition = CockpitME_509A2.this.setOld.radiatorPosition - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.radiatorPosition < 1.0F) {
                CockpitME_509A2.this.setNew.radiatorPosition = CockpitME_509A2.this.setOld.radiatorPosition + 0.05F;
            }
            if (CockpitME_509A2.this.setNew.quickBrakePosition1 < 1.0F) {
                CockpitME_509A2.this.setNew.quickBrakePosition1 = CockpitME_509A2.this.setOld.quickBrakePosition1 + 0.05F;
            }
            if (CockpitME_509A2.this.setNew.quickBrakePosition2 < 1.0F) {
                CockpitME_509A2.this.setNew.quickBrakePosition2 = CockpitME_509A2.this.setOld.quickBrakePosition2 + 0.05F;
            }
            if (!CockpitME_509A2.this.EjectCanopyState) {
                if (CockpitME_509A2.this.setNew.ejectSystemPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.ejectSystemPosition = CockpitME_509A2.this.setOld.ejectSystemPosition - 0.5F;
                }
            } else if (CockpitME_509A2.this.setNew.ejectSystemPosition < 1.0F) {
                CockpitME_509A2.this.setNew.ejectSystemPosition = CockpitME_509A2.this.setOld.ejectSystemPosition + 0.5F;
            }
            if (CockpitME_509A2.this.fm.AS.bIsAboutToBailout && (CockpitME_509A2.this.setNew.ejectCanopyPosition < 1.0F)) {
                CockpitME_509A2.this.setNew.ejectCanopyPosition = CockpitME_509A2.this.setOld.ejectCanopyPosition + 0.5F;
            }
            if (CockpitME_509A2.this.UnLockedTState) {
                if (CockpitME_509A2.this.setNew.lockTailWheel0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.lockTailWheel0Position = CockpitME_509A2.this.setOld.lockTailWheel0Position - 0.07F;
                }
            } else if (CockpitME_509A2.this.setNew.lockTailWheel0Position < 1.0F) {
                CockpitME_509A2.this.setNew.lockTailWheel0Position = CockpitME_509A2.this.setOld.lockTailWheel0Position + 0.07F;
            }
            if (!CockpitME_509A2.this.LockedTDelayState) {
                if (CockpitME_509A2.this.setNew.lockTailWheelPosition1 > 0.0F) {
                    CockpitME_509A2.this.setNew.lockTailWheelPosition1 = CockpitME_509A2.this.setOld.lockTailWheelPosition1 - 0.07F;
                }
            } else if (CockpitME_509A2.this.setNew.lockTailWheelPosition1 < 1.0F) {
                CockpitME_509A2.this.setNew.lockTailWheelPosition1 = CockpitME_509A2.this.setOld.lockTailWheelPosition1 + 0.07F;
            }
            if (CockpitME_509A2.this.fm.CT.cockpitDoorControl == 1.0F) {
                if (CockpitME_509A2.this.setNew.topCablePosition1 > 0.0F) {
                    CockpitME_509A2.this.setNew.topCablePosition1 = CockpitME_509A2.this.setOld.topCablePosition1 - 0.022F;
                }
            } else if (CockpitME_509A2.this.setNew.topCablePosition1 < 1.0F) {
                CockpitME_509A2.this.setNew.topCablePosition1 = CockpitME_509A2.this.setOld.topCablePosition1 + 0.022F;
            }
            if (CockpitME_509A2.this.fm.getAltitude() > 3000F) {
                if (CockpitME_509A2.this.setNew.oxyButtonPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.oxyButtonPosition = CockpitME_509A2.this.setOld.oxyButtonPosition + 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.oxyButtonPosition > 0.0F) {
                CockpitME_509A2.this.setNew.oxyButtonPosition = CockpitME_509A2.this.setOld.oxyButtonPosition - 0.05F;
            }
            if (CockpitME_509A2.this.fm.getAltitude() > 5000F) {
                if (CockpitME_509A2.this.setNew.glassCleanerPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.glassCleanerPosition = CockpitME_509A2.this.setOld.glassCleanerPosition + 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.glassCleanerPosition > 0.0F) {
                CockpitME_509A2.this.setNew.glassCleanerPosition = CockpitME_509A2.this.setOld.glassCleanerPosition - 0.05F;
            }
            if (!CockpitME_509A2.this.CanopyClosedState) {
                if (CockpitME_509A2.this.setNew.canopyHandlePosition > 0.0F) {
                    CockpitME_509A2.this.setNew.canopyHandlePosition = CockpitME_509A2.this.setOld.canopyHandlePosition - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.canopyHandlePosition < 1.0F) {
                CockpitME_509A2.this.setNew.canopyHandlePosition = CockpitME_509A2.this.setOld.canopyHandlePosition + 0.05F;
            }
            if (CockpitME_509A2.this.UnLockedTState) {
                if (CockpitME_509A2.this.setNew.lockTailWheel3Position > 0.0F) {
                    CockpitME_509A2.this.setNew.lockTailWheel3Position = CockpitME_509A2.this.setOld.lockTailWheel3Position - 0.07F;
                }
            } else if (CockpitME_509A2.this.setNew.lockTailWheel3Position < 1.0F) {
                CockpitME_509A2.this.setNew.lockTailWheel3Position = CockpitME_509A2.this.setOld.lockTailWheel3Position + 0.07F;
            }
            if (CockpitME_509A2.this.UnLockedTState) {
                if (CockpitME_509A2.this.setNew.lockTailWheelPosition2 > 0.0F) {
                    CockpitME_509A2.this.setNew.lockTailWheelPosition2 = CockpitME_509A2.this.setOld.lockTailWheelPosition2 - 0.07F;
                }
            } else if (CockpitME_509A2.this.setNew.lockTailWheelPosition2 < 1.0F) {
                CockpitME_509A2.this.setNew.lockTailWheelPosition2 = CockpitME_509A2.this.setOld.lockTailWheelPosition2 + 0.07F;
            }
            if (CockpitME_509A2.this.UnLockedTState) {
                if (CockpitME_509A2.this.setNew.lockTailWheel5Position > 0.0F) {
                    CockpitME_509A2.this.setNew.lockTailWheel5Position = CockpitME_509A2.this.setOld.lockTailWheel5Position - 0.07F;
                }
            } else if (CockpitME_509A2.this.setNew.lockTailWheel5Position < 1.0F) {
                CockpitME_509A2.this.setNew.lockTailWheel5Position = CockpitME_509A2.this.setOld.lockTailWheel5Position + 0.07F;
            }
            if (!CockpitME_509A2.this.cockpitLightControl) {
                if (CockpitME_509A2.this.setNew.cockpitLights0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.cockpitLights0Position = CockpitME_509A2.this.setOld.cockpitLights0Position - 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.cockpitLights0Position < 1.0F) {
                CockpitME_509A2.this.setNew.cockpitLights0Position = CockpitME_509A2.this.setOld.cockpitLights0Position + 0.1F;
            }
            if (!CockpitME_509A2.this.fm.AS.bNavLightsOn) {
                if (CockpitME_509A2.this.setNew.navLights0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.navLights0Position = CockpitME_509A2.this.setOld.navLights0Position - 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.navLights0Position < 1.0F) {
                CockpitME_509A2.this.setNew.navLights0Position = CockpitME_509A2.this.setOld.navLights0Position + 0.1F;
            }
            if (CockpitME_509A2.this.fm.EI.engines[0].isPropAngleDeviceOperational()) {
                if (CockpitME_509A2.this.setNew.propPitchPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.propPitchPosition = CockpitME_509A2.this.setOld.propPitchPosition + 0.1F;
                }
                if (CockpitME_509A2.this.setNew.propPitch4Position < 1.0F) {
                    CockpitME_509A2.this.setNew.propPitch4Position = CockpitME_509A2.this.setOld.propPitch4Position + 0.1F;
                }
            } else {
                if (CockpitME_509A2.this.setNew.propPitchPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.propPitchPosition = CockpitME_509A2.this.setOld.propPitchPosition - 0.1F;
                }
                if (CockpitME_509A2.this.setNew.propPitch4Position > 0.0F) {
                    CockpitME_509A2.this.setNew.propPitch4Position = CockpitME_509A2.this.setOld.propPitch4Position - 0.1F;
                }
            }
            if (!CockpitME_509A2.this.cockpitLightControl) {
                if (CockpitME_509A2.this.setNew.cockpitLights2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.cockpitLights2Position = CockpitME_509A2.this.setOld.cockpitLights2Position - 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.cockpitLights2Position < 1.0F) {
                CockpitME_509A2.this.setNew.cockpitLights2Position = CockpitME_509A2.this.setOld.cockpitLights2Position + 0.1F;
            }
            if (!CockpitME_509A2.this.fm.AS.bNavLightsOn) {
                if (CockpitME_509A2.this.setNew.navLights2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.navLights2Position = CockpitME_509A2.this.setOld.navLights2Position - 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.navLights2Position < 1.0F) {
                CockpitME_509A2.this.setNew.navLights2Position = CockpitME_509A2.this.setOld.navLights2Position + 0.1F;
            }
            if (!CockpitME_509A2.this.AccelerationState1) {
                if (CockpitME_509A2.this.setNew.cockpit0Position > 0.0F) {
                    CockpitME_509A2.this.setNew.cockpit0Position = CockpitME_509A2.this.setOld.cockpit0Position - 0.02F;
                }
            } else if (CockpitME_509A2.this.setNew.cockpit0Position < 1.0F) {
                CockpitME_509A2.this.setNew.cockpit0Position = CockpitME_509A2.this.setOld.cockpit0Position + 0.02F;
            }
            if (CockpitME_509A2.this.fm.AS.astateSootStates[0] > 0) {
                if (CockpitME_509A2.this.setNew.smoke1Position < 1.0F) {
                    CockpitME_509A2.this.setNew.smoke1Position = CockpitME_509A2.this.setOld.smoke1Position + 1E-005F;
                }
                if (CockpitME_509A2.this.setNew.smoke2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.smoke2Position = CockpitME_509A2.this.setOld.smoke2Position + 2E-005F;
                }
            } else {
                if (CockpitME_509A2.this.setNew.smoke1Position > 0.0F) {
                    CockpitME_509A2.this.setNew.smoke1Position = CockpitME_509A2.this.setOld.smoke1Position - 1E-005F;
                }
                if (CockpitME_509A2.this.setNew.smoke2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.smoke2Position = CockpitME_509A2.this.setOld.smoke2Position - 2E-005F;
                }
            }
            if (CockpitME_509A2.this.fm.EI.engines[0].getReadyness() < 0.8F) {
                if (CockpitME_509A2.this.setNew.smoke3Position < 1.0F) {
                    CockpitME_509A2.this.setNew.smoke3Position = CockpitME_509A2.this.setOld.smoke3Position + 1E-005F;
                }
                if (CockpitME_509A2.this.setNew.smoke4Position < 1.0F) {
                    CockpitME_509A2.this.setNew.smoke4Position = CockpitME_509A2.this.setOld.smoke4Position + 2E-005F;
                }
            } else {
                if (CockpitME_509A2.this.setNew.smoke3Position > 0.0F) {
                    CockpitME_509A2.this.setNew.smoke3Position = CockpitME_509A2.this.setOld.smoke3Position - 1E-005F;
                }
                if (CockpitME_509A2.this.setNew.smoke4Position > 0.0F) {
                    CockpitME_509A2.this.setNew.smoke4Position = CockpitME_509A2.this.setOld.smoke4Position - 2E-005F;
                }
            }
            if (CockpitME_509A2.this.fm.CT.GearControl == 1.0F) {
                if (CockpitME_509A2.this.setNew.gearIndicatorPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.gearIndicatorPosition = CockpitME_509A2.this.setOld.gearIndicatorPosition - 0.005F;
                }
            } else if (CockpitME_509A2.this.setNew.gearIndicatorPosition < 1.0F) {
                CockpitME_509A2.this.setNew.gearIndicatorPosition = CockpitME_509A2.this.setOld.gearIndicatorPosition + 0.005F;
            }
            boolean flag2 = false;
            if (((NetAircraft) ((Aircraft) CockpitME_509A2.this.fm.actor)).thisWeaponsName.toLowerCase().startsWith("u5") && CockpitME_509A2.this.fm.CT.Weapons[9][0].haveBullets()) {
                flag2 = true;
            }
            if (flag2) {
                if (CockpitME_509A2.this.setNew.dropTankPosition < 1.0F) {
                    CockpitME_509A2.this.setNew.dropTankPosition = CockpitME_509A2.this.setOld.dropTankPosition + 0.1F;
                }
            } else if (CockpitME_509A2.this.setNew.dropTankPosition > 0.0F) {
                CockpitME_509A2.this.setNew.dropTankPosition = CockpitME_509A2.this.setOld.dropTankPosition - 0.1F;
            }
            if (!CockpitME_509A2.this.GunsightMove) {
                if (CockpitME_509A2.this.setNew.revi16SupportPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.revi16SupportPosition = CockpitME_509A2.this.setOld.revi16SupportPosition - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.revi16SupportPosition < 1.0F) {
                CockpitME_509A2.this.setNew.revi16SupportPosition = CockpitME_509A2.this.setOld.revi16SupportPosition + 0.05F;
            }
            if (!CockpitME_509A2.this.RotGunsightDelay) {
                if (CockpitME_509A2.this.setNew.revi16Support2Position > 0.0F) {
                    CockpitME_509A2.this.setNew.revi16Support2Position = CockpitME_509A2.this.setOld.revi16Support2Position - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.revi16Support2Position < 1.0F) {
                CockpitME_509A2.this.setNew.revi16Support2Position = CockpitME_509A2.this.setOld.revi16Support2Position + 0.05F;
            }
            if (CockpitME_509A2.this.fm.EI.engines[0].getControlAfterburner()) {
                if (CockpitME_509A2.this.setNew.mw50Position < 1.0F) {
                    CockpitME_509A2.this.setNew.mw50Position = CockpitME_509A2.this.setOld.mw50Position + 0.5F;
                }
            } else if (CockpitME_509A2.this.setNew.mw50Position > 0.0F) {
                CockpitME_509A2.this.setNew.mw50Position = CockpitME_509A2.this.setOld.mw50Position - 0.5F;
            }
            if (!CockpitME_509A2.this.RotGunsightDelay) {
                if (CockpitME_509A2.this.setNew.revi16Support3Position > 0.0F) {
                    CockpitME_509A2.this.setNew.revi16Support3Position = CockpitME_509A2.this.setOld.revi16Support3Position - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.revi16Support3Position < 1.0F) {
                CockpitME_509A2.this.setNew.revi16Support3Position = CockpitME_509A2.this.setOld.revi16Support3Position + 0.05F;
            }
            if (CockpitME_509A2.this.fm.CT.cockpitDoorControl == 1.0F) {
                if (CockpitME_509A2.this.setNew.topCablePosition2 > 0.0F) {
                    CockpitME_509A2.this.setNew.topCablePosition2 = CockpitME_509A2.this.setOld.topCablePosition2 - 0.022F;
                }
            } else if (CockpitME_509A2.this.setNew.topCablePosition2 < 1.0F) {
                CockpitME_509A2.this.setNew.topCablePosition2 = CockpitME_509A2.this.setOld.topCablePosition2 + 0.022F;
            }
            if (CockpitME_509A2.this.fm.EI.engines[0].getControlAfterburner() && (CockpitME_509A2.this.setNew.mw50CountPosition < 1.0F)) {
                CockpitME_509A2.this.setNew.mw50CountPosition += 5E-005F;
            }
            boolean flag4 = false;
            if (((NetAircraft) ((Aircraft) CockpitME_509A2.this.fm.actor)).thisWeaponsName.toLowerCase().startsWith("r1") && CockpitME_509A2.this.fm.CT.Weapons[3][0].haveBullets()) {
                flag4 = true;
            }
            if (flag4) {
                if (CockpitME_509A2.this.setNew.bombsContact1Position < 1.0F) {
                    CockpitME_509A2.this.setNew.bombsContact1Position = CockpitME_509A2.this.setOld.bombsContact1Position + 0.5F;
                }
                if (CockpitME_509A2.this.setNew.bombsContact2Position < 1.0F) {
                    CockpitME_509A2.this.setNew.bombsContact2Position = CockpitME_509A2.this.setOld.bombsContact2Position + 0.5F;
                }
                if (CockpitME_509A2.this.setNew.bombsPanel1Position < 1.0F) {
                    CockpitME_509A2.this.setNew.bombsPanel1Position = CockpitME_509A2.this.setOld.bombsPanel1Position + 1.0F;
                }
            } else if (CockpitME_509A2.this.setNew.bombsPanel1Position > 0.0F) {
                CockpitME_509A2.this.setNew.bombsPanel1Position = CockpitME_509A2.this.setOld.bombsPanel1Position - 1.0F;
            }
            if (CockpitME_509A2.this.setNew.leftPedalBase1Position < 1.0F) {
                CockpitME_509A2.this.setNew.leftPedalBase1Position = CockpitME_509A2.this.setOld.leftPedalBase1Position + 1.0F;
            }
            if (CockpitME_509A2.this.setNew.rightPedalBase1Position < 1.0F) {
                CockpitME_509A2.this.setNew.rightPedalBase1Position = CockpitME_509A2.this.setOld.rightPedalBase1Position + 1.0F;
            }
            if (!CockpitME_509A2.this.cockpitDimControl) {
                if (CockpitME_509A2.this.setNew.dimPosition > 0.0F) {
                    CockpitME_509A2.this.setNew.dimPosition = CockpitME_509A2.this.setOld.dimPosition - 0.05F;
                }
            } else if (CockpitME_509A2.this.setNew.dimPosition < 1.0F) {
                CockpitME_509A2.this.setNew.dimPosition = CockpitME_509A2.this.setOld.dimPosition + 0.05F;
            }
            CockpitME_509A2.this.setNew.throttle = ((10F * CockpitME_509A2.this.setOld.throttle) + CockpitME_509A2.this.fm.CT.PowerControl) / 11F;
            float f = CockpitME_509A2.this.waypointAzimuth();
            if (CockpitME_509A2.this.useRealisticNavigationInstruments()) {
                CockpitME_509A2.this.setNew.waypointAzimuth.setDeg(f - 90.0F);
                CockpitME_509A2.this.setOld.waypointAzimuth.setDeg(f - 90.0F);
            } else {
                CockpitME_509A2.this.setNew.waypointAzimuth.setDeg(CockpitME_509A2.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitME_509A2.this.setOld.azimuth.getDeg(1.0F));
            }
            if (Math.abs(CockpitME_509A2.this.fm.Or.getKren()) < 30F) {
                CockpitME_509A2.this.setNew.azimuth.setDeg(CockpitME_509A2.this.setOld.azimuth.getDeg(1.0F), CockpitME_509A2.this.fm.Or.azimut());
            }
            CockpitME_509A2.this.buzzerFX((CockpitME_509A2.this.fm.CT.getGear() < 0.999999F) && (CockpitME_509A2.this.fm.CT.getFlap() > 0.1F));
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        float      masterPosition;
        float      starterPosition;
        float      starter2Position;
        float      reviPosition;
        float      masterArmPosition;
        float      shotPosition[];
        float      gearLeverPosition;
        float      gearUpPosition;
        float      gearDownPosition;
        float      kg13TriggerPosition;
        float      radiatorPosition;
        float      fuelPump0Position;
        float      quickBrakePosition1;
        float      quickBrakePosition2;
        float      fuelSelectorPosition;
        float      oilContactPosition;
        float      ejectSystemPosition;
        float      primer0Position;
        float      primer1Position;
        float      primer2Position;
        float      primer3Position;
        float      primer4Position;
        float      primer5Position;
        float      ejectCanopyPosition;
        float      lockTailWheel0Position;
        float      lockTailWheelPosition1;
        float      masterSwitchPosition;
        float      topCablePosition1;
        float      oxyButtonPosition;
        float      oxyPressurePosition;
        float      glassCleanerPosition;
        float      canopyHandlePosition;
        float      lockTailWheel3Position;
        float      lockTailWheelPosition2;
        float      lockTailWheel5Position;
        float      circuitBreakers0Position;
        float      cockpitLights0Position;
        float      navLights0Position;
        float      propPitchPosition;
        float      generator0Position;
        float      radioSwitch0Position;
        float      circuitBreakers2Position;
        float      cockpitLights2Position;
        float      navLights2Position;
        float      fuelPump2Position;
        float      propPitch4Position;
        float      radioSwitch2Position;
        float      generator2Position;
        float      cockpit0Position;
        float      smoke1Position;
        float      smoke2Position;
        float      smoke3Position;
        float      smoke4Position;
        float      gearIndicatorPosition;
        float      dropTankPosition;
        float      revi16SupportPosition;
        float      revi16Support2Position;
        float      mw50Position;
        float      revi16Support3Position;
        float      topCablePosition2;
        float      mw50CountPosition;
        float      bombsContact1Position;
        float      bombsPanel1Position;
        float      bombsContact2Position;
        float      leftPedalBase1Position;
        float      rightPedalBase1Position;
        float      leftPedalBase2Position;
        float      rightPedalBase2Position;

        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        public Variables() {
            this.shotPosition = new float[CockpitME_509A2.NUM_COUNTERS];
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

//    protected float waypointAzimuth()
//    {
//        WayPoint waypoint = this.fm.AP.way.curr();
//        if(waypoint == null)
//        {
//            return 0.0F;
//        } else
//        {
//            waypoint.getP(tmpP);
//            tmpV.sub(tmpP, this.fm.Loc);
//            return (float)(Math.toDegrees(Math.atan2(tmpV.y, tmpV.x)));
//        }
//    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitME_509A2() {
        super("3DO/Cockpit/Me-509A2-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.oldctl = -1F;
        this.curctl = -1F;
        this.AccelerationState1 = false;
        this.IceState1 = false;
        this.IceState2 = false;
        this.IceState3 = false;
        this.CleanIceState1 = false;
        this.CanopyClosedState = false;
        this.EjectCanopyState = false;
        this.timeCounterIce1 = 0.0F;
        this.timeIce1 = 60F;
        this.timeCounterIce2 = 0.0F;
        this.timeIce2 = 120F;
        this.timeCounterIce3 = 0.0F;
        this.timeIce3 = 180F;
        this.timeCounterCleanIce1 = 0.0F;
        this.timeCleanIce1 = 60F;
        this.LockedTDelayState = false;
        this.UnLockedTState = false;
        this.timeCounterLockT = 0.0F;
        this.timeLockT = 10F;
        this.timeCounterUnLockT = 0.0F;
        this.timeUnLockT = 10F;
        this.GunsightMove = false;
        this.RotGunsightDelay = false;
        this.timeCounterGunsightMove2 = 0.0F;
        this.timeGunsightMove2 = 20F;
        this.ReticleCut = false;
        this.timeCounterReticle = 0.0F;
        this.timeReticle = 34F;
        this.pictManifold = 0.0F;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
//        tmpP = new Point3d();
//        tmpV = new Vector3d();
        this.setNew.dimPosition = 1.0F;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "ZClocks1", "ZClocksDMG", "ZClocks2", "ZClocks3", "Needles", "ZClocks4" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
        this.bNeedSetUp = true;
        this.hasCanopy = true;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.loadBuzzerFX();
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        boolean flag = (this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 7);
        boolean flag1 = false;
        if (((NetAircraft) ((Aircraft) this.fm.actor)).thisWeaponsName.toLowerCase().startsWith("r1")) {
            flag1 = true;
        }
        boolean flag2 = false;
        if (((NetAircraft) ((Aircraft) this.fm.actor)).thisWeaponsName.toLowerCase().startsWith("r1") && this.fm.CT.Weapons[3][0].haveBullets()) {
            flag2 = true;
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Top", 0.0F, 80F * this.fm.CT.getCockpitDoor(), 0.0F);
        this.mesh.chunkSetAngles("Z_TrimIndicator", 330F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TrimWheel", 720F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal2", -25F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal2", -25F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FlapsWheel", -360F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Revi16Tinter", 0.0F, this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 45F), 0.0F);
        this.mesh.chunkSetAngles("Starter", 0.0F, this.cvt(this.interp(this.setNew.starterPosition, this.setOld.starterPosition, f), 0.0F, 1.0F, 0.0F, 45F), 0.0F);
        this.mesh.chunkSetAngles("Revi16Contact", 0.0F, this.cvt(this.interp(this.setNew.reviPosition, this.setOld.reviPosition, f), 0.0F, 1.0F, 0.0F, -46F), 0.0F);
        this.mesh.chunkSetAngles("Z_MasterArm", 0.0F, this.cvt(this.interp(this.setNew.masterArmPosition, this.setOld.masterArmPosition, f), 0.0F, 1.0F, 0.0F, -35F), 0.0F);
        for (int i = 0; i < CockpitME_509A2.NUM_COUNTERS; i++) {
            this.mesh.chunkSetAngles("Z_Shot" + (i + 1), 0.0F, this.cvt(this.interp(this.setNew.shotPosition[i], this.setOld.shotPosition[i], f), 0.0F, 1.0F, 0.0F, -60F), 0.0F);
        }

        this.mesh.chunkSetAngles("Z_GearLever", 0.0F, this.cvt(this.interp(this.setNew.gearLeverPosition, this.setOld.gearLeverPosition, f), 0.0F, 1.0F, 0.0F, -65F), 0.0F);
        this.mesh.chunkSetAngles("Z_KG13Trigger", 0.0F, this.cvt(this.interp(this.setNew.kg13TriggerPosition, this.setOld.kg13TriggerPosition, f), 0.0F, 1.0F, 0.0F, -270F), 0.0F);
        this.mesh.chunkSetAngles("Z_RadiatorSelector", 0.0F, this.cvt(this.interp(this.setNew.radiatorPosition, this.setOld.radiatorPosition, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F);
        this.mesh.chunkSetAngles("Z_RadiatorSelector2", this.cvt(this.fm.CT.getRadiatorControl(), 0.0F, 1.0F, -180F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_QuickBrake", 0.0F, this.cvt(this.interp(this.setNew.quickBrakePosition1, this.setOld.quickBrakePosition1, f), 0.0F, 1.0F, 0.0F, 38F), 0.0F);
        this.mesh.chunkSetAngles("Z_QuickBrake2", 0.0F, this.cvt(this.interp(this.setNew.quickBrakePosition2, this.setOld.quickBrakePosition2, f), 0.0F, 1.0F, 0.0F, -15F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelSelector", 0.0F, this.cvt(this.interp(this.setNew.fuelSelectorPosition, this.setOld.fuelSelectorPosition, f), 0.0F, 1.0F, 0.0F, -58F), 0.0F);
        this.mesh.chunkSetAngles("Z_OilContact", 0.0F, this.cvt(this.interp(this.setNew.oilContactPosition, this.setOld.oilContactPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F);
        this.mesh.chunkSetAngles("Z_EjectSystem", 0.0F, this.cvt(this.interp(this.setNew.ejectSystemPosition, this.setOld.ejectSystemPosition, f), 0.0F, 1.0F, 0.0F, -42F), 0.0F);
        this.mesh.chunkSetAngles("Z_LockTWheel2", 0.0F, this.cvt(this.interp(this.setNew.lockTailWheelPosition1, this.setOld.lockTailWheelPosition1, f), 0.0F, 1.0F, 0.0F, 135F), 0.0F);
        this.mesh.chunkSetAngles("Z_LockTWheel4", 0.0F, this.cvt(this.interp(this.setNew.lockTailWheelPosition2, this.setOld.lockTailWheelPosition2, f), 0.0F, 1.0F, 0.0F, -25F), 0.0F);
        this.mesh.chunkSetAngles("Z_TopCable", 0.0F, this.cvt(this.interp(this.setNew.topCablePosition1, this.setOld.topCablePosition1, f), 0.0F, 1.0F, 0.0F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_TopCable2", 0.0F, this.cvt(this.interp(this.setNew.topCablePosition2, this.setOld.topCablePosition2, f), 0.0F, 1.0F, 0.0F, -80F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyButton", 0.0F, this.cvt(this.interp(this.setNew.oxyButtonPosition, this.setOld.oxyButtonPosition, f), 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyPressure", 0.0F, this.cvt(this.interp(this.setNew.oxyPressurePosition, this.setOld.oxyPressurePosition, f), 0.0F, 1.0F, -258F, 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Z_GlassCleaner", 0.0F, this.cvt(this.interp(this.setNew.glassCleanerPosition, this.setOld.glassCleanerPosition, f), 0.0F, 1.0F, 0.0F, -88F), 0.0F);
        this.mesh.chunkSetAngles("Z_CanopyHandle", 0.0F, this.cvt(this.interp(this.setNew.canopyHandlePosition, this.setOld.canopyHandlePosition, f), 0.0F, 1.0F, 0.0F, 120F), 0.0F);
        this.mesh.chunkSetAngles("Smoke1", 0.0F, this.cvt(this.interp(this.setNew.smoke1Position, this.setOld.smoke1Position, f), 0.0F, 1.0F, 0.0F, 18000F), 0.0F);
        this.mesh.chunkSetAngles("Smoke2", 0.0F, this.cvt(this.interp(this.setNew.smoke2Position, this.setOld.smoke2Position, f), 0.0F, 1.0F, 0.0F, 18000F), 0.0F);
        this.mesh.chunkSetAngles("Smoke3", 0.0F, this.cvt(this.interp(this.setNew.smoke3Position, this.setOld.smoke3Position, f), 0.0F, 1.0F, 0.0F, 18000F), 0.0F);
        this.mesh.chunkSetAngles("Smoke4", 0.0F, this.cvt(this.interp(this.setNew.smoke4Position, this.setOld.smoke4Position, f), 0.0F, 1.0F, 0.0F, 18000F), 0.0F);
        this.mesh.chunkSetAngles("Revi16Support1", 0.0F, this.cvt(this.interp(this.setNew.revi16SupportPosition, this.setOld.revi16SupportPosition, f), 0.0F, 1.0F, 0.0F, 91F), 0.0F);
        this.mesh.chunkSetAngles("Z_MW50", 0.0F, this.cvt(this.interp(this.setNew.mw50Position, this.setOld.mw50Position, f), 0.0F, 1.0F, 0.0F, -40F), 0.0F);
        this.mesh.chunkSetAngles("Z_MW50Count", 0.0F, this.cvt(this.interp(this.setNew.mw50CountPosition, this.setOld.mw50CountPosition, f), 0.0F, 1.0F, 0.0F, 300F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ATA1", this.cvt(this.pictManifold = (0.75F * this.pictManifold) + (0.25F * this.fm.EI.engines[0].getManifoldPressure()), 0.6F, 1.8F, 0.0F, 329F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitME_509A2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitME_509A2.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity1", -44.5F + this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), CockpitME_509A2.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        if (this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        if (this.fm.EI.engines[0].getStage() == 6) {
            float f1 = this.cvt((this.setNew.azimuth.getDeg(f) - this.setOld.azimuth.getDeg(f)) / Time.tickLenFs(), -5F, 5F, 18F, -18F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
            this.mesh.chunkSetAngles("Z_TurnBank1", f1, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Horizon1", 0.0F, 0.0F, this.fm.Or.getKren());
            this.mesh.chunkSetAngles("Z_Horizon2", this.cvt(this.fm.Or.getTangage(), -45F, 45F, -13F, 13F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_TurnBank2", -this.cvt(this.getBall(6D), -6F, 6F, -4.5F, 4.5F), 0.0F, 0.0F);
        if (flag) {
            this.mesh.chunkSetAngles("Z_PropPitch1", 270F - ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_PropPitch2", 105F - ((float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F), 0.0F, 0.0F);
        }
        this.mesh.chunkVisible("BombsPanel1", flag1);
        this.mesh.chunkVisible("BombsPanel2", flag1);
        this.mesh.chunkVisible("BombsPanel3", flag1);
        this.mesh.chunkVisible("Z_BombsContact1", flag1);
        this.mesh.chunkVisible("Z_BombsContact2", flag1);
        this.mesh.chunkVisible("Z_BombsSelector", flag1);
        this.mesh.chunkVisible("RocketsPanel", false);
        this.mesh.chunkVisible("Z_RFire1", false);
        this.mesh.chunkVisible("Z_RFire2", false);
        if (this.GunsightMove) {
            this.cockpitDimControl = true;
            this.mesh.chunkVisible("Revi16Cable2", false);
        } else if (!this.GunsightMove) {
            this.mesh.chunkVisible("Revi16Cable2", true);
        }
        if ((this.fm.getAltitude() > 5000F) && !this.CleanIceState1) {
            this.timeCounterIce1 += f;
            this.timeCounterIce2 += f;
            this.timeCounterIce3 += f;
            if (this.timeCounterIce1 >= this.timeIce1) {
                this.timeCounterIce1 = 0.0F;
                this.timeIce1 = 0.0F;
                this.IceState1 = true;
            }
            if (this.timeCounterIce2 >= this.timeIce2) {
                this.timeCounterIce2 = 0.0F;
                this.timeIce2 = 0.0F;
                this.IceState2 = true;
            }
            if (this.timeCounterIce3 >= this.timeIce3) {
                this.timeCounterIce3 = 0.0F;
                this.timeIce3 = 0.0F;
                this.IceState3 = true;
            }
        } else if ((this.fm.getAltitude() <= 300F) || this.CleanIceState1) {
            this.timeCounterIce1 = 0.0F;
            this.timeIce1 = 60F;
            this.timeCounterIce2 = 0.0F;
            this.timeIce2 = 120F;
            this.timeCounterIce3 = 0.0F;
            this.timeIce3 = 180F;
        }
        if (this.fm.AS.astateEngineStates[0] == 1) {
            this.mesh.chunkVisible("Smoke1", true);
        } else {
            this.mesh.chunkVisible("Smoke1", false);
        }
        if (this.fm.AS.astateEngineStates[0] == 2) {
            this.mesh.chunkVisible("Smoke2", true);
        } else {
            this.mesh.chunkVisible("Smoke2", false);
        }
        if (this.fm.AS.astateEngineStates[0] == 3) {
            this.mesh.chunkVisible("Smoke3", true);
        } else {
            this.mesh.chunkVisible("Smoke3", false);
        }
        if (this.fm.AS.astateEngineStates[0] > 3) {
            this.mesh.chunkVisible("Smoke4", true);
        } else {
            this.mesh.chunkVisible("Smoke4", false);
        }
        if (this.aircraft().chunkDamageVisible("CF") > 0) {
            this.mesh.chunkVisible("Z_HitArmor1", true);
        } else {
            this.mesh.chunkVisible("Z_HitArmor1", false);
        }
        if (this.aircraft().chunkDamageVisible("Cockpit") > 0) {
            this.mesh.chunkVisible("Z_HitArmor2", true);
        } else {
            this.mesh.chunkVisible("Z_HitArmor2", false);
        }
        this.mesh.chunkVisible("Z_Blood", false);
        if (this.IceState1) {
            this.mesh.chunkVisible("GlassIce1", true);
            this.mesh.chunkVisible("TopIce1", true);
        } else if (!this.IceState1) {
            this.mesh.chunkVisible("GlassIce1", false);
            this.mesh.chunkVisible("TopIce1", false);
        }
        if (this.IceState2) {
            this.mesh.chunkVisible("GlassIce2", true);
            this.mesh.chunkVisible("TopIce2", true);
        } else if (!this.IceState2) {
            this.mesh.chunkVisible("GlassIce2", false);
            this.mesh.chunkVisible("TopIce2", false);
        }
        if (this.IceState3) {
            this.mesh.chunkVisible("GlassIce3", true);
            this.mesh.chunkVisible("TopIce3", true);
        } else if (!this.IceState3) {
            this.mesh.chunkVisible("GlassIce3", false);
            this.mesh.chunkVisible("TopIce3", false);
        }
        if (this.fm.getAltitude() > 5000F) {
            this.timeCounterCleanIce1 += f;
            if (this.timeCounterCleanIce1 >= this.timeCleanIce1) {
                this.timeCounterCleanIce1 = 0.0F;
                this.timeCleanIce1 = 0.0F;
                this.CleanIceState1 = true;
            }
        } else {
            this.timeCounterCleanIce1 = 0.0F;
            this.timeCounterCleanIce1 = 60F;
        }
        if (this.CleanIceState1) {
            this.IceState1 = false;
            this.IceState2 = false;
            this.IceState3 = false;
        }
        this.mesh.chunkVisible("Z_FuelTube", false);
        if (this.fm.CT.getCockpitDoor() == 1.0F) {
            this.mesh.chunkVisible("Z_TopCable2", true);
        } else {
            this.mesh.chunkVisible("Z_TopCable2", false);
        }
        this.timeCounterReticle += f;
        this.RotGunsightDelay = false;
        this.timeCounterGunsightMove2 += f;
        if (this.timeCounterReticle > this.timeReticle) {
            this.timeCounterReticle = 0.0F;
            this.ReticleCut = false;
        }
        if (this.timeCounterGunsightMove2 > this.timeGunsightMove2) {
            this.timeCounterGunsightMove2 = 0.0F;
            this.GunsightMove = false;
        }
        if (this.fm.Gears.bTailwheelLocked) {
            this.timeCounterLockT += f;
            if (this.timeCounterLockT > this.timeLockT) {
                this.timeCounterLockT = 0.0F;
                this.LockedTDelayState = true;
            }
        } else {
            this.timeCounterLockT = 0.0F;
            this.LockedTDelayState = false;
        }
        if (!this.fm.Gears.bTailwheelLocked) {
            this.timeCounterUnLockT += f;
            if (this.timeCounterUnLockT > this.timeUnLockT) {
                this.timeCounterUnLockT = 0.0F;
                this.UnLockedTState = true;
            }
        } else {
            this.timeCounterUnLockT = 0.0F;
            this.UnLockedTState = false;
        }
        if (this.fm.CT.getCockpitDoor() == 0.0F) {
            this.CanopyClosedState = true;
        } else {
            this.CanopyClosedState = false;
        }
        if (!this.fm.CT.getRadiatorControlAuto()) {
            this.mesh.chunkVisible("Z_RadiatorSelector", false);
        } else {
            this.mesh.chunkVisible("Z_RadiatorSelector", true);
        }
        if (this.fm.CT.getRadiatorControlAuto()) {
            this.mesh.chunkVisible("Z_RadiatorSelector2", false);
        } else {
            this.mesh.chunkVisible("Z_RadiatorSelector2", true);
        }
        if (flag) {
            if (this.fm.M.fuel < 36F) {
                this.mesh.chunkVisible("Z_FuelWarning1", true);
            } else {
                this.mesh.chunkVisible("Z_FuelWarning1", false);
            }
            if (this.fm.CT.getGear() == 0.0F) {
                this.mesh.chunkVisible("Z_GearLRed1", true);
                this.mesh.chunkVisible("Z_GearRRed1", true);
                this.mesh.chunkVisible("Z_GearEin", false);
            } else {
                this.mesh.chunkVisible("Z_GearLRed1", false);
                this.mesh.chunkVisible("Z_GearRRed1", false);
                this.mesh.chunkVisible("Z_GearEin", true);
            }
            if (this.fm.CT.getGear() == 1.0F) {
                this.mesh.chunkVisible("Z_GearLGreen1", true);
                this.mesh.chunkVisible("Z_GearRGreen1", true);
                this.mesh.chunkVisible("Z_GearAus", false);
            } else {
                this.mesh.chunkVisible("Z_GearLGreen1", false);
                this.mesh.chunkVisible("Z_GearRGreen1", false);
                this.mesh.chunkVisible("Z_GearAus", true);
            }
            if (flag2) {
                this.mesh.chunkVisible("Z_BombsLight", true);
            }
        } else {
            this.mesh.chunkVisible("Z_FuelWarning1", false);
            this.mesh.chunkVisible("Z_GearLRed1", false);
            this.mesh.chunkVisible("Z_GearRRed1", false);
            this.mesh.chunkVisible("Z_GearLGreen1", false);
            this.mesh.chunkVisible("Z_GearRGreen1", false);
            this.mesh.chunkVisible("Z_GearEin", true);
            this.mesh.chunkVisible("Z_GearAus", true);
            this.mesh.chunkVisible("Z_BombsLight", false);
        }
        if (this.gun[0] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter1", this.cvt(this.gun[0].countBullets(), 0.0F, this.ammoCounterMax[0], 15F, 0.0F), 0.0F, 0.0F);
        }
        if (this.gun[1] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter2", this.cvt(this.gun[1].countBullets(), 0.0F, this.ammoCounterMax[1], 15F, 0.0F), 0.0F, 0.0F);
        }
        if (this.gun[2] != null) {
            this.mesh.chunkSetAngles("Z_AmmoCounter3", this.cvt(this.gun[2].countBullets(), 0.0F, this.ammoCounterMax[2], 15F, 0.0F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_KG13a", (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 9F);
        if ((this.fm.AS.astateCockpitState & 8) == 0) {
            this.mesh.chunkSetAngles("Z_Throttle", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 68.18182F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + (28.333F * this.fm.EI.engines[0].getControlMagnetos()), 0.0F, 0.0F);
        if (flag) {
            for (int j = 3; j < CockpitME_509A2.MAX_GUNS; j++) {
                String s = j == 3 ? "Z_WhiteLampL" : "Z_WhiteLampR";
                if (this.gun[j] instanceof GunEmpty) {
                    this.mesh.chunkVisible(s, false);
                } else if (this.fm.CT.WeaponControl[this.triggerForBreechControl[j]]) {
                    if ((this.gun[j] != null) && this.gun[j].haveBullets()) {
                        boolean flag3 = false;
                        long l = Time.current();
                        if (this.timePerShot[j] < (l - this.lastBreechTime[j])) {
                            flag3 = true;
                            this.lastBreechTime[j] = l;
                        }
                        if (flag3) {
                            this.mesh.chunkVisible(s, !this.mesh.isChunkVisible(s));
                        }
                    } else {
                        this.mesh.chunkVisible(s, false);
                    }
                } else {
                    this.mesh.chunkVisible(s, true);
                }
            }

        }
        if (this.fm.AS.astateBailoutStep > 3) {
            this.removeCanopy();
        }
        if (this.fm.AS.bIsAboutToBailout) {
            this.EjectCanopyState = true;
        }
        if (!flag) {
            this.setNightMats(false);
        }
        if (flag && !this.ReticleCut) {
            this.mesh.chunkVisible("Z_Z_RETICLE", true);
        } else {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
        }
        if (this.curctl == -1F) {
            this.curctl = this.oldctl = this.fm.EI.engines[0].getControlThrottle();
        } else {
            this.curctl = this.fm.EI.engines[0].getControlThrottle();
            if ((this.curctl > 0.5F) && (((this.curctl - this.oldctl) / f) > 0.0F) && (this.fm.EI.engines[0].getRPM() > 300F) && (this.fm.getSpeedKMH() > 0.0F) && (this.fm.getSpeedKMH() <= 90F) && (this.fm.EI.engines[0].getStage() == 6)) {
                this.AccelerationState1 = true;
            }
            this.oldctl = this.curctl;
        }
        if (this.fm.getSpeedKMH() > 100F) {
            this.AccelerationState1 = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.masterPosition, this.setOld.masterPosition, f), 0.0F, 1.0F, 0.0F, -0.005F);
        this.mesh.chunkSetLocate("Z_Master", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.starter2Position, this.setOld.starter2Position, f), 0.0F, 1.0F, 0.0F, 0.02F);
        this.mesh.chunkSetLocate("Starter2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.gearUpPosition, this.setOld.gearUpPosition, f), 0.0F, 1.0F, 0.0F, 0.013F);
        this.mesh.chunkSetLocate("Z_GearUp", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.gearDownPosition, this.setOld.gearDownPosition, f), 0.0F, 1.0F, 0.0F, 0.013F);
        this.mesh.chunkSetLocate("Z_GearDown", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_RightPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.045F, 0.045F);
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] *= -1F;
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.fuelPump0Position, this.setOld.fuelPump0Position, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_FuelPomp", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.primer0Position, this.setOld.primer0Position, f), 0.0F, 1.0F, 0.0F, 0.035F);
        this.mesh.chunkSetLocate("Z_Primer", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.primer1Position, this.setOld.primer1Position, f), 0.0F, 1.0F, 0.0F, -0.035F);
        this.mesh.chunkSetLocate("Z_Primer1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.primer2Position, this.setOld.primer2Position, f), 0.0F, 1.0F, 0.0F, 0.035F);
        this.mesh.chunkSetLocate("Z_Primer2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.primer3Position, this.setOld.primer3Position, f), 0.0F, 1.0F, 0.0F, -0.035F);
        this.mesh.chunkSetLocate("Z_Primer3", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.primer4Position, this.setOld.primer4Position, f), 0.0F, 1.0F, 0.0F, 0.035F);
        this.mesh.chunkSetLocate("Z_Primer4", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.primer5Position, this.setOld.primer5Position, f), 0.0F, 1.0F, 0.0F, -0.035F);
        this.mesh.chunkSetLocate("Z_Primer5", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.ejectCanopyPosition, this.setOld.ejectCanopyPosition, f), 0.0F, 1.0F, 0.0F, 0.015F);
        this.mesh.chunkSetLocate("Z_EjectCanop", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.lockTailWheel0Position, this.setOld.lockTailWheel0Position, f), 0.0F, 1.0F, 0.0F, -0.06F);
        this.mesh.chunkSetLocate("Z_LockTWheel", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.masterSwitchPosition, this.setOld.masterSwitchPosition, f), 0.0F, 1.0F, 0.0F, -0.01F);
        this.mesh.chunkSetLocate("Z_MasterSwitch", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.lockTailWheel3Position, this.setOld.lockTailWheel3Position, f), 0.0F, 1.0F, 0.0F, -0.015F);
        this.mesh.chunkSetLocate("Z_LockTWheel3", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.lockTailWheel5Position, this.setOld.lockTailWheel5Position, f), 0.0F, 1.0F, 0.0F, -0.01F);
        this.mesh.chunkSetLocate("Z_LockTWheel5", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.circuitBreakers0Position, this.setOld.circuitBreakers0Position, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_CircuitBreakers", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.cockpitLights0Position, this.setOld.cockpitLights0Position, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_CockpitLights", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.navLights0Position, this.setOld.navLights0Position, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_NavLights", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.propPitchPosition, this.setOld.propPitchPosition, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_PropPitch", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.generator0Position, this.setOld.generator0Position, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_Generator", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.radioSwitch0Position, this.setOld.radioSwitch0Position, f), 0.0F, 1.0F, 0.0F, -0.009F);
        this.mesh.chunkSetLocate("Z_RadioSwitch", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.circuitBreakers2Position, this.setOld.circuitBreakers2Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_CircuitBreakers2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.cockpitLights2Position, this.setOld.cockpitLights2Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_CockpitLights2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.navLights2Position, this.setOld.navLights2Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_NavLights2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.fuelPump2Position, this.setOld.fuelPump2Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_FuelPomp2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.propPitch4Position, this.setOld.propPitch4Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_PropPitch4", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.radioSwitch2Position, this.setOld.radioSwitch2Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_RadioSwitch2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.generator2Position, this.setOld.generator2Position, f), 0.0F, 1.0F, 0.0F, 0.004F);
        this.mesh.chunkSetLocate("Z_Generator2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.cockpit0Position, this.setOld.cockpit0Position, f), 0.0F, 1.0F, 0.0F, -0.03F);
        this.mesh.chunkSetLocate("Cockpit", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.gearIndicatorPosition, this.setOld.gearIndicatorPosition, f), 0.0F, 1.0F, 0.0F, -0.04F);
        this.mesh.chunkSetLocate("GearIndicator", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.dropTankPosition, this.setOld.dropTankPosition, f), 0.0F, 1.0F, 0.0F, 0.03F);
        this.mesh.chunkSetLocate("Z_DropTank", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.revi16Support2Position, this.setOld.revi16Support2Position, f), 0.0F, 1.0F, 0.0F, -0.0675F);
        this.mesh.chunkSetLocate("Revi16Support2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.revi16Support3Position, this.setOld.revi16Support3Position, f), 0.0F, 1.0F, 0.0F, 0.01F);
        this.mesh.chunkSetLocate("Revi16Support3", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.bombsContact1Position, this.setOld.bombsContact1Position, f), 0.0F, 1.0F, 0.0F, -0.003F);
        this.mesh.chunkSetLocate("Z_BombsContact1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.bombsPanel1Position, this.setOld.bombsPanel1Position, f), 0.0F, 1.0F, 0.0F, -0.016F);
        this.mesh.chunkSetLocate("BombsPanel1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.bombsContact2Position, this.setOld.bombsContact2Position, f), 0.0F, 1.0F, 0.0F, 0.003F);
        this.mesh.chunkSetLocate("Z_BombsContact2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.leftPedalBase1Position, this.setOld.leftPedalBase1Position, f), 0.0F, 1.0F, 0.0F, -0.02F);
        this.mesh.chunkSetLocate("Z_LeftPedalBase1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.rightPedalBase1Position, this.setOld.rightPedalBase1Position, f), 0.0F, 1.0F, 0.0F, -0.023F);
        this.mesh.chunkSetLocate("Z_RightPedalBase1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.leftPedalBase2Position, this.setOld.leftPedalBase2Position, f), 0.0F, 1.0F, 0.0F, -0.011F);
        this.mesh.chunkSetLocate("Z_LeftPedalBase2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.rightPedalBase2Position, this.setOld.rightPedalBase2Position, f), 0.0F, 1.0F, 0.0F, -0.013F);
        this.mesh.chunkSetLocate("Z_RightPedalBase2", Cockpit.xyz, Cockpit.ypr);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.004F, 0.4F);
            this.light2.light.setEmit(0.004F, 0.4F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1", true);
            this.mesh.chunkVisible("Z_Holes3", true);
            this.mesh.chunkVisible("Z_Holes4", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("DMGInstruments", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_ATA1", false);
            this.mesh.chunkVisible("Z_FuelQuantity1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes1", true);
            this.mesh.chunkVisible("Z_Holes2", true);
            this.mesh.chunkVisible("DMGInstruments", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_ATA1", false);
            this.mesh.chunkVisible("Z_FuelQuantity1", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Z_Holes1", true);
        }
    }

    protected boolean doFocusEnter() {
        if (!super.doFocusEnter()) {
            return false;
        }
        this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
        this.aircraft().hierMesh().chunkVisible("Wire_D0", false);
        if (this.fm.AS.bIsAboutToBailout) {
            this.hasCanopy = false;
        }
        return true;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        }
        if (this.hasCanopy) {
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        }
        this.aircraft().hierMesh().chunkVisible("Wire_D0", true);
        super.doFocusLeave();
    }

    public void removeCanopy() {
        this.hasCanopy = false;
        this.mesh.chunkVisible("Top", false);
        this.mesh.chunkVisible("Top2", false);
        this.mesh.chunkVisible("Top3", false);
        this.mesh.chunkVisible("Z_TopCable", false);
        this.mesh.chunkVisible("Z_TopCable2", false);
        this.mesh.chunkVisible("TopIce1", false);
        this.mesh.chunkVisible("TopIce2", false);
        this.mesh.chunkVisible("TopIce3", false);
        this.mesh.chunkVisible("Z_Holes2", false);
        this.mesh.chunkVisible("Z_Holes3", false);
    }

    protected void reflectPlaneMats() {
        this.loadoutType = this.getLoadoutType();
        switch (this.loadoutType) {
            case 5:
                this.triggerForBreechControl[0] = 1;
                this.triggerForBreechControl[1] = 1;
                this.triggerForBreechControl[2] = 1;
                this.triggerForBreechControl[3] = 9;
                this.triggerForBreechControl[4] = 9;
                this.ammoCounterMax[0] = 100;
                this.ammoCounterMax[1] = 100;
                this.ammoCounterMax[2] = 100;
                this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
                break;

            case 6:
                this.triggerForBreechControl[0] = 1;
                this.triggerForBreechControl[1] = 1;
                this.triggerForBreechControl[2] = 1;
                this.triggerForBreechControl[3] = 0;
                this.triggerForBreechControl[4] = 0;
                this.ammoCounterMax[0] = 100;
                this.ammoCounterMax[1] = 100;
                this.ammoCounterMax[2] = 100;
                this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
                break;

            case 7:
                this.triggerForBreechControl[0] = 1;
                this.triggerForBreechControl[1] = 1;
                this.triggerForBreechControl[2] = 1;
                this.triggerForBreechControl[3] = 1;
                this.triggerForBreechControl[4] = 1;
                this.ammoCounterMax[0] = 100;
                this.ammoCounterMax[1] = 100;
                this.ammoCounterMax[2] = 100;
                this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
                break;

            case 1:
                this.triggerForBreechControl[0] = 0;
                this.triggerForBreechControl[1] = 1;
                this.triggerForBreechControl[2] = 0;
                this.triggerForBreechControl[3] = 9;
                this.triggerForBreechControl[4] = 9;
                this.ammoCounterMax[0] = 500;
                this.ammoCounterMax[1] = 100;
                this.ammoCounterMax[2] = 500;
                this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
                break;

            case 2:
                this.triggerForBreechControl[0] = 0;
                this.triggerForBreechControl[1] = 1;
                this.triggerForBreechControl[2] = 0;
                this.triggerForBreechControl[3] = 0;
                this.triggerForBreechControl[4] = 0;
                this.ammoCounterMax[0] = 500;
                this.ammoCounterMax[1] = 100;
                this.ammoCounterMax[2] = 500;
                this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
                break;

            case 3:
            case 4:
            default:
                this.triggerForBreechControl[0] = 0;
                this.triggerForBreechControl[1] = 1;
                this.triggerForBreechControl[2] = 0;
                this.triggerForBreechControl[3] = 1;
                this.triggerForBreechControl[4] = 1;
                this.ammoCounterMax[0] = 500;
                this.ammoCounterMax[1] = 100;
                this.ammoCounterMax[2] = 500;
                this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
                this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
                break;
        }
    }

    private int getLoadoutType() {
        String s = ((NetAircraft) (this.aircraft())).thisWeaponsName.toUpperCase();
        if (s.startsWith("2xMK-108")) {
            return 1;
        } else {
            return s.startsWith("2xMK-108+2xdpt") ? 2 : 1;
        }
    }

    private boolean            bNeedSetUp;
    private boolean            hasCanopy;
    private int                loadoutType;
    private static final int   NUM_COUNTERS              = 3;
    private static final int   MAX_GUNS                  = 5;
    private int                triggerForBreechControl[] = { 0, 1, 0, 9, 9 };
    private long               lastBreechTime[]          = { 0L, 0L, 0L, 0L, 0L };
    private long               timePerShot[]             = { 0L, 0L, 0L, 0L, 0L };
    private int                ammoCounterMax[]          = { 500, 100, 500 };
    private Gun                gun[]                     = { null, null, null, null, null };
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              oldctl;
    private float              curctl;
    private boolean            AccelerationState1;
    private boolean            IceState1;
    private boolean            IceState2;
    private boolean            IceState3;
    private boolean            CleanIceState1;
    private boolean            CanopyClosedState;
    private boolean            EjectCanopyState;
    private float              timeCounterIce1;
    private float              timeIce1;
    private float              timeCounterIce2;
    private float              timeIce2;
    private float              timeCounterIce3;
    private float              timeIce3;
    private float              timeCounterCleanIce1;
    private float              timeCleanIce1;
    private boolean            UnLockedTState;
    private boolean            LockedTDelayState;
    private float              timeCounterLockT;
    private float              timeLockT;
    private float              timeCounterUnLockT;
    private float              timeUnLockT;
    private boolean            GunsightMove;
    private boolean            RotGunsightDelay;
    private float              timeGunsightMove2;
    private float              timeCounterGunsightMove2;
    private boolean            ReticleCut;
    private float              timeCounterReticle;
    private float              timeReticle;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManifold;
    private static final float speedometerScale[]        = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F, 186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
    private static final float rpmScale[]                = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]               = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };
//    private Point3d tmpP;
//    private Vector3d tmpV;
    public float               limits6DoF[];

    static {
//        Property.set(CockpitME_509A2.class, "normZNs", new float[] {
////            0.72F, 0.47F, 0.47F, 0.47F
//            0.47F, 0.47F, 0.47F, 0.47F
//        });
        Property.set(CockpitME_509A2.class, "normZN", 0.85F);
    }

}
