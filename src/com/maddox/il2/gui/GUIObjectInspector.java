package com.maddox.il2.gui;

import java.util.ArrayList;
import java.util.ResourceBundle;

import com.maddox.gwindow.GBevel;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowScrollingDialogClient;
import com.maddox.gwindow.GWindowTable;
import com.maddox.gwindow.GWindowVScrollBar;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.LDRres;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.util.NumberTokenizer;

public class GUIObjectInspector extends GameState {
    public class DialogClient extends GUIDialogClient {

        public boolean notify(GWindow gwindow, int i, int j) {
            if (i != 2) {
                return super.notify(gwindow, i, j);
            }
            if (gwindow == GUIObjectInspector.this.wCountry) {
                GUIObjectInspector.this.fillObjects();
                int k = GUIObjectInspector.this.wCountry.getSelected();
                if (k >= 0) {
                    Main3D.menuMusicPlay(k != 0 ? "de" : "ru");
                    GUIObjectInspector.s_country = GUIObjectInspector.this.wCountry.getSelected();
                    if (GUIObjectInspector.this.wTable.countRows() > 0) {
                        GUIObjectInspector.s_object = 0;
                        GUIObjectInspector.s_scroll = 0.0F;
                        GUIObjectInspector.this.wTable.setSelect(GUIObjectInspector.s_object, 0);
                        if (GUIObjectInspector.this.wTable.vSB.isVisible()) {
                            GUIObjectInspector.this.wTable.vSB.setPos(GUIObjectInspector.s_scroll, true);
                        }
                    }
                }
                return true;
            }
            if (gwindow == GUIObjectInspector.this.wPrev) {
                Main.stateStack().pop();
                return true;
            }
            if (gwindow == GUIObjectInspector.this.wView) {
                GUIObjectInspector.s_object = GUIObjectInspector.this.wTable.selectRow;
                GUIObjectInspector.s_scroll = GUIObjectInspector.this.wTable.vSB.pos();
                Main.stateStack().change(23);
                return true;
            } else {
                return super.notify(gwindow, i, j);
            }
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(40F), this.y1024(620F), this.x1024(250F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(320F), this.y1024(32F), 2.0F, this.y1024(655F));
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(40F), this.y1024(40F), this.x1024(240F), this.y1024(32F), 0, GUIObjectInspector.this.i18n("obj.SelectCountry"));
            this.draw(this.x1024(360F), this.y1024(40F), this.x1024(248F), this.y1024(32F), 0, GUIObjectInspector.this.i18n("obj.Description"));
            this.draw(this.x1024(104F), this.y1024(652F), this.x1024(192F), this.y1024(48F), 0, GUIObjectInspector.this.i18n("obj.Back"));
            this.draw(this.x1024(730F), this.y1024(652F), this.x1024(192F), this.y1024(48F), 2, GUIObjectInspector.this.i18n("obj.View"));
            this.setCanvasColorWHITE();
        }

        public void setPosSize() {
            this.set1024PosSize(0.0F, 32F, 1024F, 736F);
            GUIObjectInspector.this.wPrev.setPosC(this.x1024(64F), this.y1024(676F));
            GUIObjectInspector.this.wView.setPosC(this.x1024(960F), this.y1024(676F));
            GUIObjectInspector.this.wCountry.setPosSize(this.x1024(40F), this.y1024(82F), this.x1024(246F), this.M(2.0F));
            GUIObjectInspector.this.wTable.setPosSize(this.x1024(40F), this.y1024(194F), this.x1024(246F), this.y1024(400F));
            GUIObjectInspector.this.wScrollDescription.setPosSize(this.x1024(360F), this.y1024(80F), this.x1024(625F), this.y1024(514F));
        }

        public DialogClient() {
        }
    }

    public class Descript extends GWindowDialogClient {

        public void render() {
            String s = null;
            if (GUIObjectInspector.this.wTable.selectRow >= 0) {
                s = ((ObjectInfo) GUIObjectInspector.this.wTable.objects.get(GUIObjectInspector.this.wTable.selectRow)).info;
                if ((s != null) && (s.length() == 0)) {
                    s = null;
                }
            }
            if (s != null) {
                GBevel gbevel = ((GUILookAndFeel) this.lookAndFeel()).bevelComboDown;
                this.setCanvasFont(0);
                this.setCanvasColorBLACK();
                this.root.C.clip.y += gbevel.T.dy;
                this.root.C.clip.dy -= gbevel.T.dy + gbevel.B.dy;
                this.drawLines(gbevel.L.dx + 2.0F, gbevel.T.dy + 2.0F, s, 0, s.length(), this.win.dx - gbevel.L.dx - gbevel.R.dx - 4F, this.root.C.font.height);
            }
        }

        public void computeSize() {
            String s = null;
            if (GUIObjectInspector.this.wTable.selectRow >= 0) {
                s = ((ObjectInfo) GUIObjectInspector.this.wTable.objects.get(GUIObjectInspector.this.wTable.selectRow)).info;
                if ((s != null) && (s.length() == 0)) {
                    s = null;
                }
            }
            if (s != null) {
                this.win.dx = this.parentWindow.win.dx;
                GBevel gbevel = ((GUILookAndFeel) this.lookAndFeel()).bevelComboDown;
                this.setCanvasFont(0);
                int i = this.computeLines(s, 0, s.length(), this.win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                this.win.dy = (this.root.C.font.height * i) + gbevel.T.dy + gbevel.B.dy + 4F;
                if (this.win.dy > this.parentWindow.win.dy) {
                    this.win.dx = this.parentWindow.win.dx - this.lookAndFeel().getVScrollBarW();
                    int j = this.computeLines(s, 0, s.length(), this.win.dx - gbevel.L.dx - gbevel.R.dx - 4F);
                    this.win.dy = (this.root.C.font.height * j) + gbevel.T.dy + gbevel.B.dy + 4F;
                }
            } else {
                this.win.dx = this.parentWindow.win.dx;
                this.win.dy = this.parentWindow.win.dy;
            }
        }

        public Descript() {
        }
    }

    public class ScrollDescript extends GWindowScrollingDialogClient {

        public void created() {
            this.fixed = GUIObjectInspector.this.wDescript = (Descript) this.create(new Descript());
            this.fixed.bNotify = true;
            this.bNotify = true;
        }

        public boolean notify(GWindow gwindow, int i, int j) {
            if (super.notify(gwindow, i, j)) {
                return true;
            } else {
                this.notify(i, j);
                return false;
            }
        }

        public void resized() {
            if (GUIObjectInspector.this.wDescript != null) {
                GUIObjectInspector.this.wDescript.computeSize();
            }
            super.resized();
            if (this.vScroll.isVisible()) {
                GBevel gbevel = ((GUILookAndFeel) this.lookAndFeel()).bevelComboDown;
                this.vScroll.setPos(this.win.dx - this.lookAndFeel().getVScrollBarW() - gbevel.R.dx, gbevel.T.dy);
                this.vScroll.setSize(this.lookAndFeel().getVScrollBarW(), this.win.dy - gbevel.T.dy - gbevel.B.dy);
            }
        }

        public void render() {
            this.setCanvasColorWHITE();
            GBevel gbevel = ((GUILookAndFeel) this.lookAndFeel()).bevelComboDown;
            this.lookAndFeel().drawBevel(this, 0.0F, 0.0F, this.win.dx, this.win.dy, gbevel, ((GUILookAndFeel) this.lookAndFeel()).basicelements, true);
        }

        public ScrollDescript() {
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
            GUIObjectInspector.this.wScrollDescription.resized();
            if (GUIObjectInspector.this.wScrollDescription.vScroll.isVisible()) {
                GUIObjectInspector.this.wScrollDescription.vScroll.setPos(0.0F, true);
            }
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

    static class ObjectInfo {

        public String   key;
        public String   name;
        public String   info;
        public boolean  bGround;
        public Regiment reg;

        public ObjectInfo(String s, String s1, boolean flag, String s2) {
            this.key = s1;
            this.bGround = flag;
            if (!flag) {
                this.name = I18N.plane(s1);
                try {
                    ResourceBundle resourcebundle = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
                    this.info = resourcebundle.getString(s1);
                } catch (Exception exception) {
                    this.info = "";
                }
            } else {
                try {
                    ResourceBundle resourcebundle1 = ResourceBundle.getBundle(s, RTSConf.cur.locale, LDRres.loader());
                    this.name = resourcebundle1.getString(s1 + "_NAME");
                    this.info = resourcebundle1.getString(s1 + "_INFO");
                } catch (Exception exception1) {
                    this.name = s1;
                    this.info = "";
                }
            }
            if (s2 != null) {
                this.reg = (Regiment) Actor.getByName(s2);
            }
        }
    }

    public void enterPush(GameState gamestate) {
        GUIObjectInspector.s_object = 0;
        GUIObjectInspector.s_scroll = 0.0F;
        this._enter();
    }

    public void _enter() {
        World.cur().camouflage = 0;
        this.wCountry.setSelected(GUIObjectInspector.s_country, true, false);
        Main3D.menuMusicPlay(GUIObjectInspector.s_country != 0 ? "de" : "ru");
        this.fillObjects();
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

    public void fillCountries() {
        this.wCountry.clear();
        for (int i = 0; i < GUIObjectInspector.cnt.length; i++) {
            String s1;
            try {
                ResourceBundle resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
                s1 = resourcebundle.getString(GUIObjectInspector.cnt[i]);
            } catch (Exception exception) {
                s1 = GUIObjectInspector.cnt[i];
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
                String s5 = "i18n/air";
                numbertokenizer.next();
                int l1 = numbertokenizer.next(0);
                boolean flag = true;
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
                    if (!"NOQUICK".equals(s9)) {
                        s8 = s9;
                    }
                } while (true);
                if (flag && (l1 == i)) {
                    ObjectInfo objectinfo1 = new ObjectInfo(s5, s3, false, s8);
                    this.wTable.objects.add(objectinfo1);
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
                numbertokenizer1.next();
                int i2 = numbertokenizer1.next(0);
                if (i2 == i) {
                    ObjectInfo objectinfo = new ObjectInfo(s2, s4, true, null);
                    this.wTable.objects.add(objectinfo);
                }
            }

        }
        this.wTable.resized();
    }

    public GUIObjectInspector(GWindowRoot gwindowroot) {
        super(22);
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = this.i18n("obj.infoI");
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        this.wCountry = (GWindowComboControl) this.dialogClient.addControl(new GWindowComboControl(this.dialogClient, 2.0F, 2.0F, 20F + (gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric())));
        this.wCountry.setEditable(false);
        this.fillCountries();
        this.wCountry.setSelected(GUIObjectInspector.s_country, true, false);
        this.wTable = new Table(this.dialogClient);
        this.dialogClient.create(this.wScrollDescription = new ScrollDescript());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.wPrev = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        this.wView = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }

    public static String       type;
    public static int          s_country = 0;
    public static int          s_object  = 0;
    public static float        s_scroll  = 0.0F;
    public GUIClient           client;
    public DialogClient        dialogClient;
    public GUIInfoMenu         infoMenu;
    public GUIInfoName         infoName;
    public GUIButton           wPrev;
    public GUIButton           wView;
    public GWindowComboControl wCountry;
    public Table               wTable;
    public ScrollDescript      wScrollDescription;
    public Descript            wDescript;
    public GWindowVScrollBar   wDistance;
    public static String       cnt[]     = { "", "" };

    static {
        GUIObjectInspector.cnt[0] = "allies";
        GUIObjectInspector.cnt[1] = "axis";
    }
}
