package com.maddox.il2.engine.cmd;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.RendersMain;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.Message;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.RTSConf;
import com.maddox.sas1946.il2.util.CommonTools;
import com.maddox.sas1946.il2.util.Reflection;

public class CmdFov extends Cmd
{
    
    class CmdFovAnimated implements MsgTimeOutListener {
        private final int   steps;

        private final float targetFOV;
        private final float startFOV;
        private int         curStep;
        private MsgTimeOut  ticker;

        CmdFovAnimated(float startFOV, float targetFOV) {
            this.startFOV = startFOV;
            this.targetFOV = targetFOV;
            this.curStep = 0;
            this.steps = (int) Math.max(Math.min((Math.abs(targetFOV - startFOV) * 2F) / speedFactor, 60F / speedFactor), 1F);
//            System.out.println("startFOV=" + startFOV + ", targetFOV=" + targetFOV + ", speedFactor=" + speedFactor + ", Steps: " + this.steps);
            this.ticker = new MsgTimeOut();
            this.ticker.setTickPos(-1);
            this.ticker.setNotCleanAfterSend();
            this.ticker.setFlags(Message.REAL_TIME | Message.CLEAR_TIME | Message.NEXT_TICK);
            this.ticker.setListener(this);
            this.ticker.post();
        }

        public float getTargetFOV() {
            return this.targetFOV;
        }

        public void stopAnimation() {
            RTSConf.cur.queueRealTime.remove(this.ticker);
            RTSConf.cur.queueRealTimeNextTick.remove(this.ticker);
            this.ticker = null;
        }

        public void msgTimeOut(Object arg0) {
            this.curStep++;
//            final float newFOV = CmdFov.smoothCvt(this.curStep, 0F, this.steps, this.startFOV, this.targetFOV);
            float newFOV = CommonTools.smoothCvt(this.curStep, 0F, this.steps, this.startFOV, this.targetFOV, 2);
            if (Config.isUSE_RENDER() && Main3D.cur3D() != null && Main3D.cur3D().cockpitCur != null) {
                String cockpitName = Main3D.cur3D().cockpitCur.getClass().getName();
                cockpitName = cockpitName.toLowerCase().substring(cockpitName.lastIndexOf('.') + 1);
                if (Config.cur.ini.get("Mods", "FOVAnim", 1) == 0 || cockpitName.indexOf("bombardier") != -1 || cockpitName.indexOf("observer") != -1) newFOV = this.targetFOV;
            }
            if (Math.abs(newFOV - this.targetFOV) < 0.1F) {
                CmdFov.DoChangeFOV(this.targetFOV);
                curCmdFovAnimated = null;
                this.stopAnimation();
            } else {
                CmdFov.DoChangeFOV(newFOV);
                this.ticker.post();
            }
        }
    }

    public static int             hudLogFOVId          = -1;
    private static CmdFovAnimated curCmdFovAnimated    = null;
    private static float          speedFactor          = 1F;
    private static float          lastFOVX             = 70F;
    private static int            windowsWideScreenFoV = -1;

//    static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
//        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
//        return outMin + ((outMax - outMin) * ((-0.5F * (float) Math.cos(((inputValue - inMin) / (inMax - inMin)) * Math.PI)) + 0.5F));
//    }

    private static void DoChangeFOV(float fovValue) {
        final Camera3D camera3d1 = (Camera3D) Actor.getByName("camera");
        camera3d1.set(fovValue);
        final Render render = (Render) Actor.getByName("renderCockpit");
        if (render != null) {
            final Camera3D camera3d2 = (Camera3D) render.getCamera();
            camera3d2.set(fovValue);
        }
        Main3D.FOVX = fovValue;
    }

    private boolean isWindowsWideScreenFoV() {
        if (windowsWideScreenFoV == -1) {
            try {
                windowsWideScreenFoV = Reflection.getBoolean(Config.cur, "windowsWideScreenFoV") ? 1 : 0;
//                final Field windowsWideScreenFoVField = Config.cur.getClass().getDeclaredField("windowsWideScreenFoV");
//                windowsWideScreenFoVField.setAccessible(true);
//                windowsWideScreenFoV = windowsWideScreenFoVField.getBoolean(Config.cur) ? 1 : 0;
            } catch (final Exception e) {
                windowsWideScreenFoV = 0;
            }
        }
        return windowsWideScreenFoV > 0;
    }

    private int getFoVIntRev(float rawFOV, boolean increase) {
        final int renderWidth = RendersMain.width();
        final int renderHeight = RendersMain.height();
        if (!RendersMain.isSaveAspect() || !this.isWindowsWideScreenFoV() || (((renderWidth * 3) / 4) == renderHeight) || (renderWidth < renderHeight)) {
            return (int) rawFOV;
        }
        final float rawFOVWide = 0.75F - ((float) renderHeight / (float) renderWidth);
        final int aspect = Math.round(rawFOV / (1.0F + rawFOVWide));
        final int aspect10s = (aspect / 10) * 10;
        final int aspect1s = aspect % 10;
        if ((aspect1s == 0) || (aspect1s == 5)) {
            return aspect10s + aspect1s;
        }
        if (increase) {
            if (aspect1s >= 7) {
                return aspect10s + 10;
            }
            if (aspect1s >= 2) {
                return aspect10s + 5;
            } else {
                return aspect10s;
            }
        }
        if (aspect1s > 7) {
            return aspect10s + 10;
        }
        if (aspect1s > 3) {
            return aspect10s + 5;
        } else {
            return aspect10s;
        }
    }

    public Object exec(CmdEnv cmdenv, Map map) {
        if (map.containsKey("_$$")) {
            int fovMin = Config.cur.ini.get("Mods", "FOVMin", 5);
            int fovMax = Config.cur.ini.get("Mods", "FOVMax", 175);
            float targetFOV = Cmd.arg(map, "_$$", 0, 70F, fovMin, fovMax);

            speedFactor = Config.cur.ini.get("Mods", "FOVSpeed", 200) / 100F;

            final float curTargetFOV = curCmdFovAnimated == null ? Main3D.FOVX : curCmdFovAnimated.getTargetFOV();

            int iMaxFovOverride = 90;
            int iMinFovOverride = 30;
            int iStockFovDefault = 70;

            final int renderWidth = RendersMain.width();
            final int renderHeight = RendersMain.height();
            if (RendersMain.isSaveAspect() && this.isWindowsWideScreenFoV() && (((renderWidth * 3) / 4) != renderHeight) && (renderWidth >= renderHeight)) {
                final float fOverrideFactor = 1.75F - ((float) renderHeight / (float) renderWidth);
                iMaxFovOverride = (int) (iMaxFovOverride * fOverrideFactor);
                iMinFovOverride = (int) (iMinFovOverride * fOverrideFactor);
                iStockFovDefault = (int) (((0.75F - ((float) renderHeight / (float) renderWidth)) + 1F) * 70F);
            }

            if (targetFOV == 70F) {
                final StringWriter sw = new StringWriter();
                new Exception().printStackTrace(new PrintWriter(sw));
                final String exceptionAsString = sw.toString();
                if (exceptionAsString.indexOf("com.maddox.il2.game.Mission.doBegin") != -1) {
                    targetFOV = Config.cur.ini.get("Mods", "FOVDefault", iStockFovDefault);
                    lastFOVX = targetFOV;
                    DoChangeFOV(targetFOV);
                    return "OK";
                }
            }

            if ((targetFOV == iStockFovDefault) && (Math.abs(this.getFoVIntRev(lastFOVX, targetFOV > lastFOVX) - this.getFoVIntRev(targetFOV, targetFOV > lastFOVX)) > 5F)) {
                targetFOV = Config.cur.ini.get("Mods", "FOVDefault", iStockFovDefault);
            }

            if (curTargetFOV > (iMaxFovOverride - 2.5F)) {
                if ((int) targetFOV == (iMaxFovOverride - 5)) {
                    targetFOV = curTargetFOV - 5F;
                } else if ((int) targetFOV == iMaxFovOverride) {
                    targetFOV = curTargetFOV + 5F;
                }
            } else if (curTargetFOV < (iMinFovOverride + 2.5F)) {
                if ((int) targetFOV == iMinFovOverride) {
                    targetFOV = curTargetFOV - 5F;
                } else if ((int) targetFOV == (iMinFovOverride + 5)) {
                    targetFOV = curTargetFOV + 5F;
                }
            }
            if (targetFOV < fovMin) {
                targetFOV = fovMin;
            }
            if (targetFOV > fovMax) {
                targetFOV = fovMax;
            }

            lastFOVX = targetFOV;

            if (curCmdFovAnimated != null) {
                curCmdFovAnimated.stopAnimation();
            }
            curCmdFovAnimated = new CmdFovAnimated(Main3D.FOVX, targetFOV);
            if (Config.cur.ini.get("Mods", "FOVLog", 0) != 0) {
                if (hudLogFOVId == -1) {
                    hudLogFOVId = com.maddox.il2.game.HUD.makeIdLog();
                }
                HUD.log(hudLogFOVId, "FOV {0}°", new Object[] { new Integer((int) targetFOV) });
            }
        } else {
            final Camera3D camera3d = (Camera3D) Actor.getByName("camera");
            this.INFO_HARD("Camera fov: ".concat(String.valueOf(camera3d.FOV())));
        }
        return "OK";
    }

    public CmdFov() {
        super._properties.put("NAME", "fov");
        super._levelAccess = 1;
    }

//    public Object exec(CmdEnv cmdenv, Map map)
//    {
//        if(map.containsKey("_$$"))
//        {
//            float f = arg(map, "_$$", 0, 70F, 10F, 170F);
//            Camera3D camera3d1 = (Camera3D)Actor.getByName("camera");
//            camera3d1.set(f);
//            Render render = (Render)Actor.getByName("renderCockpit");
//            if(render != null)
//            {
//                Camera3D camera3d2 = (Camera3D)render.getCamera();
//                camera3d2.set(f);
//            }
//            Main3D.FOVX = f;
//        } else
//        {
//            Camera3D camera3d = (Camera3D)Actor.getByName("camera");
//            INFO_HARD("Camera fov: " + camera3d.FOV());
//        }
//        return CmdEnv.RETURN_OK;
//    }
//
//    public CmdFov()
//    {
//        _properties.put("NAME", "fov");
//        _levelAccess = 0;
//    }
}
