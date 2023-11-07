package com.maddox.il2.builder;

import java.util.ArrayList;
import java.util.HashMap;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.gwindow.GWindowTabDialogClient.Tab;
import com.maddox.il2.ai.Army;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.objects.buildings.House;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.NumberTokenizer;

public class PlMisHouse extends Plugin {
    static class Type {

        public int        indx;
        public String     name;
        public ActorSpawn spawn;
        public String     fullClassName;
        public String     shortClassName;

        public Type(int i, String s, String s1) {
            this.indx = i;
            this.name = s;
            this.fullClassName = s1;
            this.shortClassName = this.fullClassName.substring("com.maddox.il2.objects.buildings.".length());
            this.spawn = (ActorSpawn) Spawn.get_WithSoftClass(this.fullClassName);
        }
    }

    class ViewType extends GWindowMenuItem {

        public void execute() {
            this.bChecked = !this.bChecked;
            PlMisHouse.this.viewTypeAll(this.bChecked);
        }

        public ViewType(GWindowMenu gwindowmenu, String s, String s1) {
            super(gwindowmenu, s, s1);
        }
    }

    public PlMisHouse() {
        this.allActors = new ArrayList();
        this.allTitles = new ArrayList();
        this.allTypes = new HashMap();
        this.houseIcon = null;
        this.p2d = new Point2d();
        this.p = new Point3d();
        this.o = new Orient();
        this.spawnArg = new ActorSpawnArg();
        this.bView = true;
        this.viewClasses = new HashMap();
        this._actorInfo = new String[1];
    }

    public void mapLoaded() {
        this.deleteAll();
    }

    public void deleteAll() {
        for (int i = 0; i < this.allActors.size(); i++) {
            Actor actor = (Actor) this.allActors.get(i);
            if (Actor.isValid(actor)) {
                actor.destroy();
            }
        }

        this.allActors.clear();
    }

    public void delete(Actor actor) {
        this.allActors.remove(actor);
        actor.destroy();
    }

    public void renderMap2D() {
        if (Plugin.builder.isFreeView() || !this.bView) {
            return;
        }
        Actor actor = Plugin.builder.selectedActor();
        Plugin plugin = (Plugin) Property.value(actor, "builderPlugin");
        if (plugin instanceof PlMisHouse) {
            long l = Time.currentReal();
            long l1 = 1000L;
            double d = (2D * (l % l1)) / l1;
            if (d >= 1.0D) {
                d = 2D - d;
            }
            int i1 = (int) (100D * d);
            Plugin.builder.wBrowse.render3D[16].setClearColor(new Color4f(0.51F, 0.87F, 0.92F, i1 / 100F));
        } else {
            Plugin.builder.wBrowse.render3D[16].setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
        }
        Render.prepareStates();
        for (int i = 0; i < this.allActors.size(); i++) {
            Actor actor1 = (Actor) this.allActors.get(i);
            if (Actor.isValid(actor1) && (actor1.icon != null) && Plugin.builder.project2d(actor1.pos.getAbsPoint(), this.p2d)) {
                int j = actor1.getArmy();
                boolean flag = Plugin.builder.conf.bShowArmy[j];
                if (flag) {
                    int k;
                    if (Plugin.builder.isMiltiSelected(actor1)) {
                        k = Builder.colorMultiSelected(Army.color(actor1.getArmy()));
                    } else if (actor1 == actor) {
                        k = Builder.colorSelected();
                    } else {
                        k = Army.color(actor1.getArmy());
                    }
                    IconDraw.setColor(k);
                    IconDraw.render(actor1, this.p2d.x, this.p2d.y);
                }
            }
        }

    }

    public void load(SectFile sectfile) {
        int i = sectfile.sectionIndex("Buildings");
        if (i >= 0) {
            int j = sectfile.vars(i);
            for (int k = 0; k < j; k++) {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(i, k));
                numbertokenizer.next("");
                this.insert("com.maddox.il2.objects.buildings." + numbertokenizer.next(""), numbertokenizer.next(1) == 1, numbertokenizer.next(0.0D), numbertokenizer.next(0.0D), numbertokenizer.next(0.0F), false);
            }

        }
    }

    public boolean save(SectFile sectfile) {
        Orient orient = new Orient();
        int i = sectfile.sectionAdd("Buildings");
        for (int j = 0; j < this.allActors.size(); j++) {
            Actor actor = (Actor) this.allActors.get(j);
            if (Actor.isValid(actor)) {
                Point3d point3d = actor.pos.getAbsPoint();
                Orient orient1 = actor.pos.getAbsOrient();
                orient.set(orient1);
                orient.wrap360();
                Type type1 = (Type) Property.value(actor, "builderType", null);
                sectfile.lineAdd(i, j + "_bld", type1.shortClassName + " " + (actor.isAlive() ? "1 " : "0 ") + this.formatPos(point3d.x, point3d.y, orient.azimut()));
            }
        }

        return true;
    }

    private String formatPos(double d, double d1, float f) {
        return this.formatValue(d) + " " + this.formatValue(d1) + " " + this.formatValue(f);
    }

    private String formatValue(double d) {
        boolean flag = d < 0.0D;
        if (flag) {
            d = -d;
        }
        double d1 = (d + 0.005D) - (int) d;
        if (d1 >= 0.1D) {
            return (flag ? "-" : "") + (int) d + "." + (int) (d1 * 100D);
        } else {
            return (flag ? "-" : "") + (int) d + ".0" + (int) (d1 * 100D);
        }
    }

    private Actor insert(String className, boolean isAlive, double x, double y, float direction, boolean isSelected) {
//        System.out.println("PlMisHouse insert(" + s + ", " + flag + ", " + d + ", " + d1 + ", " + f + ")");
        ActorSpawn actorspawn = (ActorSpawn) Spawn.get_WithSoftClass(className, false);
        if (actorspawn == null) {
            Plugin.builder.tipErr("PlMisHouse: ActorSpawn for '" + className + "' not found");
            return null;
        }
        this.spawnArg.clear();
        this.p.set(x, y, 0.0D);
        this.spawnArg.point = this.p;
        this.o.set(direction, 0.0F, 0.0F);
        this.spawnArg.orient = this.o;
        try {
            Actor actor = actorspawn.actorSpawn(this.spawnArg);
            if (!isAlive) {
                actor.setDiedFlag(true);
            }
            Property.set(actor, "builderSpawn", "");
            Property.set(actor, "builderPlugin", this);
            Type houseType = (Type) this.allTypes.get(className);
            Property.set(actor, "builderType", houseType);
            actor.icon = this.houseIcon;
            this.allActors.add(actor);
            Plugin.builder.align(actor);
            if (isSelected) {
                Plugin.builder.setSelected(actor);
            }
            PlMission.setChanged();
            return actor;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }

    private Actor insert(Type houseType, Loc mapCoords, boolean isSelected) {
        this.spawnArg.clear();
        this.spawnArg.point = mapCoords.getPoint();
        this.spawnArg.orient = mapCoords.getOrient();
        return this.insert(houseType.fullClassName, true, mapCoords.getPoint().x, mapCoords.getPoint().y, mapCoords.getOrient().getAzimut(), isSelected);
    }

    public void insert(Loc mapCoords, boolean isSelected) {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if (i != this.startComboBox1) {
            return;
        }
        if ((j < 0) || (j >= this.type.length)) {
            return;
        } else {
            this.insert(this.type[j], mapCoords, isSelected);
            return;
        }
    }

    public void changeType() {
//        int i = Plugin.builder.wSelect.comboBox1.getSelected() - startComboBox1;
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        Actor actor = Plugin.builder.selectedActor();
//        Loc loc = actor.pos.getAbs();
//        this.insert(this.type[j], loc, true);
        this.insert(this.type[j], actor.pos.getAbs(), true);
        if (Plugin.builder.selectedActor() != actor) {
            this.allActors.remove(actor);
            actor.destroy();
            PlMission.setChanged();
        }
    }

    public void changeType(boolean changeToPrevious, boolean moveToEndOfSelection) {
//        System.out.println("PlMisHouse (" + changeToPrevious + ", " + moveToEndOfSelection + ") 01");
        if (moveToEndOfSelection || (Plugin.builder.wSelect.comboBox1.getSelected() != this.startComboBox1)) {
//            System.out.println("PlMisHouse (" + changeToPrevious + ", " + moveToEndOfSelection + ") 02");
            return;
        }
        int i = Plugin.builder.wSelect.comboBox2.getSelected();
        if (changeToPrevious) {
            if (++i >= this.type.length) {
                i = 0;
            }
        } else if (--i < 0) {
            i = this.type.length - 1;
        }
        Actor actor = Plugin.builder.selectedActor();
//        Loc loc = actor.pos.getAbs();
//        Actor actor1 = insert(type[i], loc, true);
        this.insert(this.type[i], actor.pos.getAbs(), true);
        if (Plugin.builder.selectedActor() != actor) {
//            System.out.println("PlMisHouse (" + changeToPrevious + ", " + moveToEndOfSelection + ") 03");
            this.allActors.remove(actor);
            actor.destroy();
            PlMission.setChanged();
        }
        this.fillComboBox2(this.startComboBox1, i);
    }

    public void configure() {
        if (Plugin.getPlugin("Mission") == null) {
            throw new RuntimeException("PlMisHouse: plugin 'Mission' not found");
        }
        this.pluginMission = (PlMission) Plugin.getPlugin("Mission");
        if (this.sectFile == null) {
            throw new RuntimeException("PlMisHouse: field 'sectFile' not defined");
        }
        SectFile sectfile = new SectFile(this.sectFile, 0);
        int i = sectfile.sections();
        if (i <= 0) {
            throw new RuntimeException("PlMisHouse: file '" + this.sectFile + "' is empty");
        }
        Type atype[] = new Type[i];
        int j = 0;
        int k = 0;
        for (int l = 0; l < i; l++) {
            String s = sectfile.sectionName(l);
            if (s.equals("***")) {
                String s1 = sectfile.varExist(l, "Title") ? sectfile.value(l, sectfile.varIndex(l, "Title")) : "Unnamed Section";
                if ((this.allTitles.size() > 0) && (k == 0)) {
                    this.allTitles.remove(this.allTitles.size() - 1);
                }
                this.allTitles.add("" + j + " " + s1);
                k = 0;
            }
            if (((s.indexOf("House$") >= 0) || (s.indexOf("Plate$") >= 0)) && ((s.indexOf("Plate$") == -1) || (s.indexOf("Kronstadt") == -1))) {
                k++;
                String s2 = s;
                String s3 = "";
                int j1 = s.lastIndexOf('$');
                if (j1 >= 0) {
                    s2 = s.substring(0, j1);
                    s3 = s.substring(j1 + 1);
                }
                Class class1 = null;
                try {
                    class1 = ObjIO.classForName(s2);
                } catch (Exception exception) {
                    throw new RuntimeException("PlMisHouse: class '" + s2 + "' not found");
                }
                if (j1 >= 0) {
                    s = class1.getName() + "$" + s3;
                } else {
                    s = class1.getName();
                }
                String s4 = Plugin.i18n(s3);
                atype[j] = new Type(j, j + " " + s4, s);
                this.allTypes.put(s, atype[j]);
                j++;
            }
        }

        this.type = new Type[j];
        for (int i1 = 0; i1 < j; i1++) {
            this.type[i1] = atype[i1];
            atype[i1] = null;
        }

        atype = null;
        this.houseIcon = IconDraw.get("icons/objHouse.mat");
    }

    void viewUpdate() {
        for (int i = 0; i < this.allActors.size(); i++) {
            Actor actor = (Actor) this.allActors.get(i);
            if (Actor.isValid(actor)) {
                actor.drawing(this.bView);
            }
        }

        if (Actor.isValid(Plugin.builder.selectedActor()) && !Plugin.builder.selectedActor().isDrawing()) {
            Plugin.builder.setSelected(null);
        }
        if (!Plugin.builder.isFreeView()) {
            Plugin.builder.repaint();
        }
    }

    public void viewTypeAll(boolean flag) {
        this.bView = flag;
        this.viewCheckBox.bChecked = this.bView;
        this.viewUpdate();
    }

    private void fillComboBox1() {
        this.startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        Plugin.builder.wSelect.comboBox1.add(Plugin.i18n("buildings"));
        if (this.startComboBox1 == 0) {
            Plugin.builder.wSelect.comboBox1.setSelected(0, true, false);
        }
        Plugin.builder.wBrowse.allTitles = new int[this.allTitles.size()];
        for (int i = 0; i < this.allTitles.size(); i++) {
            String s = "" + this.allTitles.get(i);
            int k = s.indexOf(" ");
            int l = Integer.parseInt(s.substring(0, k));
            s = s.substring(k + 1);
            Plugin.builder.wBrowse.allTitles[i] = l;
            if (l < this.type.length) {
                Plugin.builder.wBrowse.comboBox1.add(s);
            }
        }

        if (Plugin.builder.wBrowse.comboBox1.size() == 0) {
            Plugin.builder.wBrowse.allTitles = new int[1];
            Plugin.builder.wBrowse.allTitles[0] = 0;
            Plugin.builder.wBrowse.comboBox1.add("No Sections");
        }
        Plugin.builder.wBrowse.comboBox1.setSelected(0, true, false);
        for (int j = 0; j < this.type.length; j++) {
            Plugin.builder.wBrowse.comboBox2.add(this.type[j].name);
        }

        Plugin.builder.wBrowse.comboBox2.setSelected(0, true, false);
        this.fillViewerComboBox2Render(0);
    }

    private void fillComboBox2(int i, int j) {
        if (i != this.startComboBox1) {
            return;
        }
        if (Plugin.builder.wSelect.curFilledType != i) {
            Plugin.builder.wSelect.curFilledType = i;
            Plugin.builder.wSelect.comboBox2.clear(false);
            for (int k = 0; k < this.type.length; k++) {
                Plugin.builder.wSelect.comboBox2.add(this.type[k].name);
            }

            Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        Plugin.builder.wSelect.comboBox2.setSelected(j, true, false);
        this.fillComboBox2Render(i, j);
        Plugin.builder.wBrowse.comboBox2.setSelected(j, true, false);
        this.fillViewerComboBox2Render(j);
    }

    private void fillComboBox2Render(int i, int j) {
        try {
            Type type1 = this.type[j];
            if (type1.shortClassName.startsWith("Plate$")) {
                Plate.SPAWN spawn = (Plate.SPAWN) type1.spawn;
                Plugin.builder.wSelect.setMesh(spawn.prop.MeshName, true);
            } else {
                House.SPAWN spawn1 = (House.SPAWN) type1.spawn;
                Plugin.builder.wSelect.setMesh(spawn1.prop.MESH0_NAME, true);
            }
        } catch (Exception exception) {
            Plugin.builder.wSelect.setMesh(null, true);
        }
    }

    private void fillViewerComboBox2Render(int i) {
        for (int j = -16; j < 28; j++) {
            try {
                Type type1 = this.type[i + j];
                if (type1.shortClassName.startsWith("Plate$")) {
                    Plate.SPAWN spawn = (Plate.SPAWN) type1.spawn;
                    Plugin.builder.wBrowse.setMesh(j + 16, spawn.prop.MeshName, true);
                } else {
                    House.SPAWN spawn1 = (House.SPAWN) type1.spawn;
                    Plugin.builder.wBrowse.setMesh(j + 16, spawn1.prop.MESH0_NAME, true);
                }
            } catch (Exception exception) {
                Plugin.builder.wBrowse.setMesh(j + 16, null, true);
            }
        }

    }

    public String[] actorInfo(Actor actor) {
        Type type1 = (Type) Property.value(actor, "builderType", null);
        if (type1 != null) {
            this._actorInfo[0] = type1.name;
            return this._actorInfo;
        } else {
            return null;
        }
    }

    public void syncSelector() {
        Actor actor = Plugin.builder.selectedActor();
        Type type1 = (Type) Property.value(actor, "builderType", null);
        if (type1 == null) {
            return;
        } else {
            this.fillComboBox2(this.startComboBox1, type1.indx);
            return;
        }
    }

    public void createGUI() {
        this.fillComboBox1();
        this.fillComboBox2(0, 0);
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l) {
//                System.out.println("PlMisHouse createGUI comboBox1 notify(" + gwindow.toString() + ", " + k + ", " + l + ")");
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if ((i1 >= 0) && (k == 2)) {
                    PlMisHouse.this.fillComboBox2(i1, 0);
                }
                return false;
            }

        });
        Plugin.builder.wSelect.comboBox2.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l) {
//                System.out.println("PlMisHouse createGUI comboBox2 notify1(" + gwindow.toString() + ", " + k + ", " + l + ")");
                if (k != 2) {
                    return false;
                }
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if (i1 != PlMisHouse.this.startComboBox1) {
                    return false;
                } else {
                    int j1 = Plugin.builder.wSelect.comboBox2.getSelected();
                    PlMisHouse.this.fillComboBox2Render(i1, j1);
                    return false;
                }
            }

        });
        Plugin.builder.wBrowse.comboBox2.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l) {
//                System.out.println("PlMisHouse createGUI comboBox2 notify2(" + gwindow.toString() + ", " + k + ", " + l + ")");
                if (k != 2) {
                    return false;
                } else {
                    PlMisHouse.this.fillViewerComboBox2Render(Plugin.builder.wBrowse.comboBox2.getSelected());
                    return false;
                }
            }

        });
        int i;
        for (i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; (i >= 0) && (this.pluginMission.viewBridge != Plugin.builder.mDisplayFilter.subMenu.getItem(i)); i--) {
            ;
        }
        if (--i >= 0) {
//            int j = i;
            if ("de".equals(RTSConf.cur.locale.getLanguage())) {
                this.viewCheckBox = (ViewType) Plugin.builder.mDisplayFilter.subMenu.addItem(i, new ViewType(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("buildings") + " " + Plugin.i18n("show"), null));
            } else {
                this.viewCheckBox = (ViewType) Plugin.builder.mDisplayFilter.subMenu.addItem(i, new ViewType(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("show") + " " + Plugin.i18n("buildings"), null));
            }
            this.viewTypeAll(true);
        }
    }

    public String mis_getProperties(Actor actor) {
        String s = "";
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if (i != this.startComboBox1) {
            return s;
        }
        if ((j < 0) || (j >= this.type.length)) {
            return s;
        } else {
            Orient orient = new Orient();
            Point3d point3d = actor.pos.getAbsPoint();
            Orient orient1 = actor.pos.getAbsOrient();
            orient.set(orient1);
            orient.wrap360();
            Type type1 = (Type) Property.value(actor, "builderType", null);
            String s1 = "1_bld " + type1.shortClassName + " " + (actor.isAlive() ? "1 " : "0 ") + this.formatPos(point3d.x, point3d.y, orient.azimut());
            return s1;
        }
    }

    public Actor mis_insert(Loc loc, String s) {
        int i = Plugin.builder.wSelect.comboBox1.getSelected();
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        if (i != this.startComboBox1) {
            return null;
        }
        if ((j < 0) || (j >= this.type.length)) {
            return null;
        } else {
            NumberTokenizer numbertokenizer = new NumberTokenizer(s);
            numbertokenizer.next("");
            String s1 = numbertokenizer.next("");
            int k = numbertokenizer.next(1);
//            double d = numbertokenizer.next(0.0D);
//            d = numbertokenizer.next(0.0D);
            numbertokenizer.next(0.0D);
            numbertokenizer.next(0.0D);
            Actor actor = this.insert("com.maddox.il2.objects.buildings." + s1, k == 1, loc.getPoint().x, loc.getPoint().y, numbertokenizer.next(0.0F), false);
            return actor;
        }
    }

    public boolean mis_validateSelected(int i, int j) {
        if (i != this.startComboBox1) {
            return false;
        } else {
            return (j >= 0) && (j < this.type.length);
        }
    }

    private ArrayList     allActors;
    private ArrayList     allTitles;
    private HashMap       allTypes;
    private Mat           houseIcon;
    Type                  type[];
    private Point2d       p2d;
    private Point3d       p;
    private Orient        o;
    private ActorSpawnArg spawnArg;
    private PlMission     pluginMission;
    private int           startComboBox1;
    private boolean       bView;
    private ViewType      viewCheckBox;
    HashMap               viewClasses;
    private String        _actorInfo[];
    Tab                   tabActor;
    GWindowLabel          wName;
    GWindowComboControl   wArmy;

    static {
        Property.set(PlMisHouse.class, "name", "MisHouse");
    }
}
