package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GBevel;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GFont;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.opengl.util.ScrShot;
import com.maddox.rts.LDRres;
import com.maddox.rts.Mouse;
import com.maddox.rts.ObjIO;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.util.NumberTokenizer;

public class GUIObjectView extends GameState {
    public class DialogClient extends GUIDialogClient {

        public boolean notify(GWindow gwindow, int i, int j) {
            if (i != 2) {
                return super.notify(gwindow, i, j);
            }
            if (this.isMouseCaptured()) {
                return true;
            }
            if (gwindow == GUIObjectView.this.wCountry) {
                GUIObjectView.this.fillObjects();
                int k = GUIObjectView.this.wCountry.getSelected();
                if (k >= 0) {
                    Main3D.menuMusicPlay(k != 0 ? "de" : "ru");
                    GUIObjectInspector.s_country = GUIObjectView.this.wCountry.getSelected();
                    if (GUIObjectView.this.wTable.countRows() > 0) {
                        GUIObjectInspector.s_object = 0;
                        GUIObjectInspector.s_scroll = 0.0F;
                        GUIObjectView.this.wTable.setSelect(GUIObjectInspector.s_object, 0);
                        if (GUIObjectView.this.wTable.vSB.isVisible()) {
                            GUIObjectView.this.wTable.vSB.setPos(GUIObjectInspector.s_scroll, true);
                        }
                    }
                }
                return true;
            }
            if (gwindow == GUIObjectView.this.wText) {
                GUIObjectInspector.s_object = GUIObjectView.this.wTable.selectRow;
                GUIObjectInspector.s_scroll = GUIObjectView.this.wTable.vSB.pos();
                Main.stateStack().change(22);
                return true;
            }
            if (gwindow == GUIObjectView.this.wPrev) {
                GUIObjectInspector.s_object = GUIObjectView.this.wTable.selectRow;
                GUIObjectInspector.s_scroll = GUIObjectView.this.wTable.vSB.pos();
                Main.stateStack().change(22);
                Main.stateStack().pop();
                return true;
            } else {
                return super.notify(gwindow, i, j);
            }
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(40F), this.y1024(620F), this.x1024(250F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(320F), this.y1024(32F), 2.0F, this.y1024(650F));
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(40F), this.y1024(40F), this.x1024(240F), this.y1024(32F), 0, GUIObjectView.this.i18n("obj.SelectCountry"));
            this.draw(this.x1024(104F), this.y1024(652F), this.x1024(192F), this.y1024(48F), 0, GUIObjectView.this.i18n("obj.Back"));
            this.draw(this.x1024(730F), this.y1024(652F), this.x1024(192F), this.y1024(48F), 2, GUIObjectView.this.i18n("obj.Text"));
            this.root.C.font = GUIObjectView.this.helpFont;
            this.draw(this.x1024(360F), this.y1024(606F), this.x1024(560F), this.y1024(16F), 0, GUIObjectView.this.i18n("obj.Help0"));
            this.draw(this.x1024(360F), this.y1024(622F), this.x1024(470F), this.y1024(16F), 0, GUIObjectView.this.i18n("obj.Help1"));
            this.setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel) this.lookAndFeel()).bevelComboDown;
            this.lookAndFeel().drawBevel(this, this.x1024(360F) - gbevel.L.dx, this.y1024(50F) - gbevel.T.dy, this.x1024(625F) + (gbevel.R.dx * 2.0F), this.y1024(540F) + (gbevel.B.dy * 2.0F), gbevel, ((GUILookAndFeel) this.lookAndFeel()).basicelements, false);
        }

        public void setPosSize() {
            this.set1024PosSize(0.0F, 32F, 1024F, 736F);
            GUIObjectView.this.wPrev.setPosC(this.x1024(64F), this.y1024(676F));
            GUIObjectView.this.wText.setPosC(this.x1024(960F), this.y1024(676F));
            GUIObjectView.this.wCountry.setPosSize(this.x1024(40F), this.y1024(82F), this.x1024(246F), this.M(2.0F));
            GUIObjectView.this.wTable.setPosSize(this.x1024(40F), this.y1024(194F), this.x1024(246F), this.y1024(400F));
            GUIObjectView.this.wRenders.setPosSize(this.x1024(360F), this.y1024(50F), this.x1024(625F), this.y1024(540F));
        }

        public DialogClient() {
        }
    }

    public class Table extends GWindowTable {

        public int countRows() {
            return this.objects == null ? 0 : this.objects.size();
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1) {
            this.setCanvasFont(0);
            String s = ((ObjectInfo) this.objects.get(i)).name;
            if (flag) {
                this.setCanvasColorBLACK();
                this.draw(0.0F, 0.0F, f, f1, this.lookAndFeel().regionWhite);
                this.setCanvasColorWHITE();
                this.draw(0.0F, 0.0F, f, f1, 0, s);
            } else {
                this.setCanvasColorBLACK();
                this.draw(0.0F, 0.0F, f, f1, 0, s);
            }
        }

        public void setSelect(int i, int j) {
            super.setSelect(i, j);
        }

        public boolean notify(GWindow gwindow, int i, int j) {
            if (super.notify(gwindow, i, j)) {
                return true;
            } else {
                this.notify(i, j);
                return false;
            }
        }

        public void afterCreated() {
            super.afterCreated();
            this.bColumnsSizable = false;
            this.addColumn(I18N.gui("obj.ObjectTypesList"), null);
            this.vSB.scroll = this.rowHeight(0);
            this.bNotify = true;
            this.wClient.bNotify = true;
            this.resized();
        }

        public void resolutionChanged() {
            this.vSB.scroll = this.rowHeight(0);
            super.resolutionChanged();
        }

        public ArrayList objects;

        public Table(GWindow gwindow) {
            super(gwindow, 2.0F, 4F, 20F, 16F);
            this.objects = new ArrayList();
        }
    }

    public class WRenders extends GUIRenders {

        public void mouseMove(float f, float f1) {
            float f2 = this.root.mouseStep.dx;
            float f3 = this.root.mouseStep.dy;
            if (Mouse.adapter().isInvert()) {
                f3 = -f3;
            }
            if (this.isMouseCaptured() && this.MODE_ROTATE) {
                GUIObjectView.this.ROT_X += f2 / 2.0F;
                GUIObjectView.this.ROT_Y -= f3 / 2.0F;
                if (GUIObjectView.this.bGround) {
                    if (GUIObjectView.this.ROT_Y > 20) {
                        GUIObjectView.this.ROT_Y = 20;
                    }
                    if (GUIObjectView.this.ROT_Y < -50) {
                        GUIObjectView.this.ROT_Y = -50;
                    }
                }
            }
            if (this.isMouseCaptured() && this.MODE_SCALE) {
                GUIObjectView.SCALE_FACTOR -= f3 / 2.0F;
                if (GUIObjectView.SCALE_FACTOR > GUIObjectView.Z_DIST_FAR) {
                    GUIObjectView.SCALE_FACTOR = GUIObjectView.Z_DIST_FAR;
                }
                if (GUIObjectView.SCALE_FACTOR < GUIObjectView.Z_DIST_NEAR) {
                    GUIObjectView.SCALE_FACTOR = GUIObjectView.Z_DIST_NEAR;
                }
            }
        }

        public void mouseButton(int i, boolean flag, float f, float f1) {
            if ((i == 1) && flag) {
                this.mouseCursor = 0;
                this.mouseCapture(true);
                this.MODE_SCALE = true;
                this.MODE_ROTATE = false;
            }
            if ((i == 0) && flag) {
                this.mouseCursor = 0;
                this.mouseCapture(true);
                this.MODE_ROTATE = true;
                this.MODE_SCALE = false;
            }
            if (!this.isMouseCaptured()) {
                super.mouseButton(i, flag, f, f1);
                return;
            }
            if (!flag) {
                this.mouseCursor = 1;
                this.mouseCapture(false);
            }
        }

        public void created() {
            this.render3D = new _Render3D(this.renders, 1.0F);
            this.render3D.camera3D = new Camera3D();
            this.render3D.camera3D.set(50F, 1.0F, 20000F);
            this.render3D.setCamera(this.render3D.camera3D);
            LightEnvXY lightenvxy = new LightEnvXY();
            this.render3D.setLightEnv(lightenvxy);
            lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
            Vector3f vector3f = new Vector3f(-1F, 1.0F, -1F);
            vector3f.normalize();
            lightenvxy.sun().set(vector3f);
            this.bNotify = true;
        }

        public _Render3D render3D;
        public boolean   MODE_SCALE;
        public boolean   MODE_ROTATE;

        public WRenders() {
            this.MODE_SCALE = false;
            this.MODE_ROTATE = false;
        }
    }

    public class _Render3D extends Render {

        private int oldObjectIndex = -1;
        private boolean validForScreenshot = false;
        
        public void preRender() {
            this.checkMesh();
            if (Actor.isValid(this.actorMesh)) {
                if ((this.animateMeshA != 0.0F) || (this.animateMeshT != 0.0F)) {
                    this.actorMesh.pos.getAbs(GUIObjectView.this._orient);
                    GUIObjectView.this._orient.set(GUIObjectView.this._orient.azimut() + (this.animateMeshA * GUIObjectView.this.wRenders.root.deltaTimeSec), GUIObjectView.this._orient.tangage() + (this.animateMeshT * GUIObjectView.this.wRenders.root.deltaTimeSec), 0.0F);
                    this.actorMesh.pos.setAbs(GUIObjectView.this._orient);
                    this.actorMesh.pos.reset();
                }
                this.worldMesh.draw.preRender(this.worldMesh);
                this.isShadow = (this.actorMesh.draw.preRender(this.actorMesh) & 4) != 0;
            }
        }

        public void render() {
            if (Actor.isValid(this.actorMesh)) {
                Render.prepareStates();
                this.worldMesh.draw.render(this.worldMesh);
                if (this.isShadow && GUIObjectView.this.bGround) {
                    this.actorMesh.draw.renderShadowProjective(this.actorMesh);
                }
                this.actorMesh.draw.render(this.actorMesh);
                
                if(Config.cur.ini.get("Mods", "ObjectViewerScreenshotMode", false) && GUIObjectView.this.wTable.selectRow != oldObjectIndex) {
                    oldObjectIndex = GUIObjectView.this.wTable.selectRow;
                    validForScreenshot = true;
                }
            }
        }

        public void checkMesh() {
            int i = GUIObjectView.this.wTable.selectRow;
            if (i < 0) {
                if (Actor.isValid(this.actorMesh)) {
                    this.actorMesh.destroy();
                }
                this.actorMesh = null;
            }
            ObjectInfo objectinfo = (ObjectInfo) GUIObjectView.this.wTable.objects.get(i);
            if ((this.meshName == objectinfo.meshName) && Actor.isValid(this.actorMesh)) {
                double d = ((ActorMesh) this.actorMesh).mesh().visibilityR();
                if (GUIObjectView.this.bGround) {
                    d *= (Math.sqrt(2D) / 2D) / Math.sin((this.camera3D.FOV() * Math.PI) / 360D);
                    d -= GUIObjectView.Z_GAP;
                    if (d < GUIObjectView.Z_DIST_NEAR) {
                        GUIObjectView.Z_DIST_NEAR = d;
                    }
                    d = GUIObjectView.SCALE_FACTOR;
                    GUIObjectView.this._point.set(-d, 0.0D, 0.0D);
                    GUIObjectView.this._o.set(GUIObjectView.this.ROT_X, GUIObjectView.this.ROT_Y - 45, 0.0F);
                } else {
                    d *= ((Math.sqrt(6D) + Math.sqrt(2D)) / 4D) / Math.sin((this.camera3D.FOV() * Math.PI) / 360D);
                    d -= GUIObjectView.Z_GAP;
                    if (d < GUIObjectView.Z_DIST_NEAR) {
                        GUIObjectView.Z_DIST_NEAR = d;
                    }
                    d = GUIObjectView.SCALE_FACTOR;
                    GUIObjectView.this._point.set(-d, 0.0D, 0.0D);
                    GUIObjectView.this._o.set(GUIObjectView.this.ROT_X, GUIObjectView.this.ROT_Y, 0.0F);
                    for (int j = 1; j <= 6; j++) {
                        if (((ActorSimpleHMesh) this.actorMesh).hierMesh().chunkFindCheck("Prop" + j + "_D0") != -1) {
                            ((ActorSimpleHMesh) this.actorMesh).hierMesh().chunkSetAngles("Prop" + j + "_D0", 0.0F, -GUIObjectView.this.propRot + j * 50, 0.0F);
                        }
                    }

                    GUIObjectView.this.propRot = (GUIObjectView.this.propRot + GUIObjectView.this.propDelta) % 360F;
                }
                GUIObjectView.this._o.transform(GUIObjectView.this._point);
                this.camera3D.pos.setAbs(GUIObjectView.this._point, GUIObjectView.this._o);
                if (validForScreenshot) {
                    String objectName = objectinfo.name.replace('\\', '_').replace('/', '_').replace(':', '_').replace('*', '_').replace('?', '_').replace('\"', '_').replace('<', '_').replace('>', '_').replace('|', '_');
                    ScrShot scrShot = new ScrShot("grab");
                    Reflection.setInt(scrShot, "screenshotType", 1);
                    scrShot.grab(objectName);
                    validForScreenshot = false;
                    if (oldObjectIndex < GUIObjectView.this.wTable.countRows() - 1) GUIObjectView.this.wTable.setSelect(oldObjectIndex + 1, 0);
                }
                return;
            }
            if (Actor.isValid(this.actorMesh)) {
                this.actorMesh.destroy();
            }
            this.actorMesh = null;
            this.meshName = objectinfo.meshName;
            GUIObjectView.this.bGround = objectinfo._bGround;
            if (this.meshName == null) {
                return;
            }
            double d1 = 20D;
            GUIObjectView.SCALE_FACTOR = GUIObjectView.Z_DIST_BORN;
            GUIObjectView.this.ROT_Y = -20; // 20;
            GUIObjectView.this.ROT_X = 220;
            this.worldMesh = new ActorSimpleMesh("3do/GUI/ObjectInspector/" + GUIObjectInspector.type + "/mono.sim");
            if (this.meshName.toLowerCase().endsWith(".sim")) {
                try {
                    this.actorMesh = new ActorSimpleMesh(this.meshName);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    this.actorMesh = null;
                    return;
                }
                d1 = ((ActorMesh) this.actorMesh).mesh().visibilityR();
                int k = ((ActorSimpleMesh) this.actorMesh).mesh().hookFind("Ground_Level");
                if (k != -1) {
                    Matrix4d matrix4d = new Matrix4d();
                    ((ActorSimpleMesh) this.actorMesh).mesh().hookMatrix(k, matrix4d);
                    double d3 = -matrix4d.m23;
                    ((ActorSimpleMesh) this.actorMesh).pos.setAbs(new Point3d(0.0D, 0.0D, d3));
                }
            } else {
                try {
                    this.actorMesh = new ActorSimpleHMesh(this.meshName);
                } catch (Exception exception1) {
                    System.out.println(exception1.getMessage());
                    this.actorMesh = null;
                    return;
                }
                d1 = ((ActorHMesh) this.actorMesh).hierMesh().visibilityR();
                int l = ((ActorSimpleHMesh) this.actorMesh).mesh().hookFind("Ground_Level");
                if (l != -1) {
                    Matrix4d matrix4d1 = new Matrix4d();
                    ((ActorSimpleHMesh) this.actorMesh).mesh().hookMatrix(l, matrix4d1);
                    double d5 = -matrix4d1.m23;
                    ((ActorSimpleHMesh) this.actorMesh).pos.setAbs(new Point3d(0.0D, 0.0D, d5));
                }
                if (!GUIObjectView.this.bGround) {
                    if (objectinfo.camouflage != null) {
                        World.cur().setCamouflage(objectinfo.camouflage);
                    }
                    Aircraft.prepareMeshCamouflage(this.meshName, ((ActorHMesh) this.actorMesh).hierMesh());
                    if (objectinfo.reg != null) {
                        PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(objectinfo.airClass, objectinfo.reg.country());
                        if (paintscheme != null) {
                            int j1 = 0;
                            int k1 = 0;
                            int l1 = 0;
                            paintscheme.prepare(objectinfo.airClass, ((ActorHMesh) this.actorMesh).hierMesh(), objectinfo.reg, j1, k1, l1, true);
                        }
                    }
//                    System.out.print("Scale Factor before: " + GUIObjectView.SCALE_FACTOR);
                    GUIObjectView.SCALE_FACTOR *= ((ActorHMesh)actorMesh).hierMesh().collisionR() / 113F;
//                    System.out.println(", after: " + GUIObjectView.SCALE_FACTOR);
                    ((ActorHMesh)actorMesh).pos.getAbsPoint().add(0, ((ActorHMesh)actorMesh).hierMesh().collisionR() / 5D, 0);
                }
                for (int i1 = 1; i1 <= 12; i1++) {
                    if (((ActorSimpleHMesh) this.actorMesh).hierMesh().chunkFindCheck("Prop" + i1 + "_D0") != -1) {
                        ((ActorSimpleHMesh) this.actorMesh).hierMesh().chunkVisible("Prop" + i1 + "_D0", false);
                        ((ActorSimpleHMesh) this.actorMesh).hierMesh().chunkVisible("PropRot" + i1 + "_D0", true);
                    }
                }

            }
            GUIObjectView.this.getDistanceProperties();
            if (GUIObjectView.this.bGround) {
                d1 *= (Math.sqrt(2D) / 2D) / Math.sin((this.camera3D.FOV() * Math.PI) / 360D);
                d1 -= GUIObjectView.Z_GAP;
                if (d1 < GUIObjectView.Z_DIST_NEAR) {
                    GUIObjectView.Z_DIST_NEAR = d1;
                }
                d1 = GUIObjectView.SCALE_FACTOR;
                GUIObjectView.this._point.set(-d1, 0.0D, 0.0D);
                GUIObjectView.this._o.set(GUIObjectView.this.ROT_X, GUIObjectView.this.ROT_Y - 45, 0.0F);
            } else {
                d1 *= ((Math.sqrt(6D) + Math.sqrt(2D)) / 4D) / Math.sin((this.camera3D.FOV() * Math.PI) / 360D);
                d1 -= GUIObjectView.Z_GAP;
                if (d1 < GUIObjectView.Z_DIST_NEAR) {
                    GUIObjectView.Z_DIST_NEAR = d1;
                }
                d1 = GUIObjectView.SCALE_FACTOR;
                GUIObjectView.this._point.set(-d1, 0.0D, 0.0D);
                GUIObjectView.this._o.set(GUIObjectView.this.ROT_X, GUIObjectView.this.ROT_Y, 0.0F);
            }
            GUIObjectView.this._o.transform(GUIObjectView.this._point);
            this.camera3D.pos.setAbs(GUIObjectView.this._point, GUIObjectView.this._o);
            this.camera3D.pos.reset();
            if (GUIObjectView.this.bGround) {
                this.animateMeshT = 0.0F;
            }
        }

        public Camera3D camera3D;
        public String   meshName;
        public Actor    actorMesh;
        public Actor    worldMesh;
        public float    animateMeshA;
        public float    animateMeshT;
        public boolean  isShadow;

        public _Render3D(Renders renders, float f) {
            super(renders, f);
            this.meshName = null;
            this.actorMesh = null;
            this.worldMesh = null;
            this.animateMeshA = 0.0F;
            this.animateMeshT = 0.0F;
            this.isShadow = false;
            this.setClearColor(new Color4f(0.39F, 0.35F, 0.23F, 1.0F));
            this.useClearStencil(true);
        }
    }

    static class ObjectInfo {

        public String   key;
        public String   name;
        public String   meshName;
        public boolean  _bGround;
        public Class    airClass;
        public Regiment reg;
        public String   camouflage;

        public ObjectInfo(String s, String s1, String s2, boolean flag, Class class1, String s3, String s4) {
//            System.out.println("GUIObjectView.ObjectInfo(" + s + ", " + s1 + ", " + s2 + ", " + flag + ", " + class1.getName() + ", " + s3 + ", " + s4 + ")");
            this.key = s1;
            this.meshName = s2;
            this._bGround = flag;
            this.camouflage = (s4==null?"SUMMER":s4);
            if (!flag) {
                this.name = I18N.plane(s1);
                this.airClass = class1;
                if (s3 != null) {
                    this.reg = (Regiment) Actor.getByName(s3);
                    if (this.reg == null) this.reg = (Regiment)Regiment.getByName("NoNe");
//                    System.out.println("reg=" + (reg==null?"null":reg.country()));
                    this.meshName = Aircraft.getPropertyMesh(this.airClass, this.reg.country());
                }
            } else {
                try {
                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
                    this.name = resourcebundle.getString(s1 + "_NAME");
                } catch (Exception exception) {
                    this.name = s1;
                }
            }
        }
    }

    public void _enter() {
        this.wCountry.setSelected(GUIObjectInspector.s_country, true, false);
        Main3D.menuMusicPlay(GUIObjectInspector.s_country != 0 ? "de" : "ru");
        this.fillObjects();
        this.getDistanceProperties();
        this.client.activateWindow();
        this.wTable.resolutionChanged();
        if (this.wTable.countRows() > 0) {
            this.wTable.setSelect(GUIObjectInspector.s_object, 0);
            if (this.wTable.vSB.isVisible()) {
                this.wTable.vSB.setPos(GUIObjectInspector.s_scroll, true);
            }
        }
    }

    public void _leave() {
        this.client.hideWindow();
    }

    public void getDistanceProperties() {
        try {
            ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/Distance", RTSConf.cur.locale, LDRres.loader());
            String s1 = resourcebundle.getString(GUIObjectInspector.type);
            NumberTokenizer numbertokenizer = new NumberTokenizer(s1);
            GUIObjectView.Z_DIST_NEAR = numbertokenizer.next(1000F);
            GUIObjectView.Z_DIST_FAR = numbertokenizer.next(1000F);
            GUIObjectView.Z_DIST_BORN = numbertokenizer.next(1000F);
            GUIObjectView.Z_GAP = numbertokenizer.next(1000F);
        } catch (Exception exception) {
            GUIObjectView.Z_DIST_NEAR = 20D;
            GUIObjectView.Z_DIST_FAR = 100D;
            GUIObjectView.Z_DIST_BORN = 30D;
            GUIObjectView.Z_GAP = 6D;
            System.out.println(GUIObjectInspector.type + ": error occured");
        }
        if (this.bGround) {
            ;
        }
    }

    public void fillCountries() {
        this.wCountry.clear();
        for (int i = 0; i < GUIObjectView.cnt.length; i++) {
            String s1;
            try {
                ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
                s1 = resourcebundle.getString(GUIObjectView.cnt[i]);
            } catch (Exception exception) {
                s1 = GUIObjectView.cnt[i];
            }
            this.wCountry.add(s1);
        }

    }

    public void fillObjects() {
        this.wTable.objects.clear();
        int i = this.wCountry.getSelected() + 1;
        if ("air".equals(GUIObjectInspector.type)) {
            String s = "com/maddox/il2/objects/air.ini";
            SectFile sectfile = new SectFile(s, 0);
            int j = sectfile.sectionIndex("AIR");
            int k = sectfile.vars(j);
            for (int i1 = 0; i1 < k; i1++) {
                String s3 = sectfile.var(j, i1);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(j, i1));
                String s5 = numbertokenizer.next();
                int l1 = numbertokenizer.next(0);
                boolean flag = true;
                String s7 = null;
                String s8 = null;
                do {
                    if (!numbertokenizer.hasMoreTokens()) {
                        break;
                    }
                    String s9 = numbertokenizer.next();
//                    if ("NOINFO".equals(s9)) {
//                        flag = false;
//                        break;
//                    }
//                    if (!"NOQUICK".equals(s9)) {
                        if ("SUMMER".equals(s9)) {
                            s8 = s9;
                        } else if ("WINTER".equals(s9)) {
                            s8 = s9;
                        } else if ("DESERT".equals(s9)) {
                            s8 = s9;
                        } else {
                            s7 = s9;
                        }
//                    }
                } while (true);
                if (flag && (l1 == i)) {
                    try {
//                        System.out.println("Adding: " + s5);
                        Class class1 = ObjIO.classForName(s5);
                        String s10 = Aircraft.getPropertyMeshDemo(class1, GUIObjectView.cnt[this.wCountry.getSelected()]);
                        ObjectInfo objectinfo1 = new ObjectInfo(null, s3, s10, false, class1, s7, s8);
                        this.wTable.objects.add(objectinfo1);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

        } else {
            String s1 = "i18n/" + GUIObjectInspector.type + ".ini";
            SectFile sectfile1 = new SectFile(s1, 0);
            String s2 = "i18n/" + GUIObjectInspector.type;
            int l = sectfile1.sectionIndex("ALL");
            int j1 = sectfile1.vars(l);
            for (int k1 = 0; k1 < j1; k1++) {
                String s4 = sectfile1.var(l, k1);
                NumberTokenizer numbertokenizer1 = new NumberTokenizer(sectfile1.value(l, k1));
                String s6 = numbertokenizer1.next();
                int i2 = numbertokenizer1.next(0);
                if (i2 == i) {
                    ObjectInfo objectinfo = new ObjectInfo(s2, s4, s6, true, null, null, null);
                    this.wTable.objects.add(objectinfo);
                }
            }

        }
        this.wTable.resized();
    }

    public GUIObjectView(GWindowRoot gwindowroot) {
        super(23);
        this.propRot = 0.0F;
        this._o = new Orient(0.0F, 0.0F, 0.0F);
        this.ROT_X = 0;
        this.ROT_Y = 0;
        this.bGround = false;
        this._orient = new Orient();
        this._point = new Point3d();
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = this.i18n("obj.infoV");
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        this.wCountry = (GWindowComboControl) this.dialogClient.addControl(new GWindowComboControl(this.dialogClient, 2.0F, 2.0F, 20F + (gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric())));
        this.wCountry.setEditable(false);
        this.fillCountries();
        this.wCountry.setSelected(GUIObjectInspector.s_country, true, false);
        this.wTable = new Table(this.dialogClient);
        this.dialogClient.create(this.wRenders = new WRenders());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.wPrev = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        this.wText = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.helpFont = GFont.New("arial8");
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }

    public final float         propDelta    = 20F;
    public float               propRot;
    public Orient              _o;
    public int                 ROT_X;
    public int                 ROT_Y;
    public static double       Z_GAP        = 4D;
    public static double       Z_DIST_BORN  = 0.0D;
    public static double       Z_DIST_NEAR  = 0.0D;
    public static double       Z_DIST_FAR   = 100D;
    public static double       SCALE_FACTOR = 0.0D;
    public boolean             bGround;
    public GUIClient           client;
    public DialogClient        dialogClient;
    public GUIInfoMenu         infoMenu;
    public GUIInfoName         infoName;
    public GUIButton           wPrev;
    public GUIButton           wText;
    public GWindowComboControl wCountry;
    public Table               wTable;
    public WRenders            wRenders;
    public GFont               helpFont;
    public static String       cnt[]        = { "", "" };
    private Orient             _orient;
    private Point3d            _point;

    static {
        GUIObjectView.cnt[0] = "allies";
        GUIObjectView.cnt[1] = "axis";
    }

}
