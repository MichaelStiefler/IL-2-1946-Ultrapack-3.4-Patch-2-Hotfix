package com.maddox.il2.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GNotifyListener;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowCheckBox;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMenu;
import com.maddox.gwindow.GWindowMenuItem;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BmpUtils;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.gui.GUIAirArming;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.Finger;
import com.maddox.rts.HomePath;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.LDRres;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapInt;
import com.maddox.util.NumberTokenizer;

public class PlMisAir extends Plugin {
    static class Country {

        public String name;
        public String i18nName;

        Country() {
        }
    }

    class _Render3D extends Render {

        public void preRender() {
            PlMisAir.this.checkMesh(this.planeIndx);
            if (Actor.isValid(PlMisAir.this.actorMesh[this.planeIndx])) {
                if ((PlMisAir.this.animateMeshA[this.planeIndx] != 0.0F) || (PlMisAir.this.animateMeshT[this.planeIndx] != 0.0F)) {
                    PlMisAir.this.actorMesh[this.planeIndx].pos.getAbs(PlMisAir.this._orient);
                    PlMisAir.this._orient.set(PlMisAir.this._orient.azimut() + (PlMisAir.this.animateMeshA[this.planeIndx] * Main3D.cur3D().guiManager.root.deltaTimeSec), PlMisAir.this._orient.tangage() + (PlMisAir.this.animateMeshT[this.planeIndx] * Main3D.cur3D().guiManager.root.deltaTimeSec), 0.0F);
                    PlMisAir.this._orient.wrap360();
                    PlMisAir.this.actorMesh[this.planeIndx].pos.setAbs(PlMisAir.this._orient);
                    PlMisAir.this.actorMesh[this.planeIndx].pos.reset();
                }
                PlMisAir.this.actorMesh[this.planeIndx].draw.preRender(PlMisAir.this.actorMesh[this.planeIndx]);
            }
        }

        public void render() {
            if (Actor.isValid(PlMisAir.this.actorMesh[this.planeIndx])) {
                Render.prepareStates();
                PlMisAir.this.actorMesh[this.planeIndx].draw.render(PlMisAir.this.actorMesh[this.planeIndx]);
            }
        }

        int planeIndx;

        public _Render3D(Renders renders1, float f) {
            super(renders1, f);
            this.planeIndx = PlMisAir.this._planeIndx;
//            setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
            this.setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
            this.useClearStencil(true);
        }
    }

    class ViewItem extends GWindowMenuItem {

        public void execute() {
            this.bChecked = !this.bChecked;
            PlMisAir.this.viewType(this.indx);
        }

        int indx;

        public ViewItem(int i, GWindowMenu gwindowmenu, String s, String s1) {
            super(gwindowmenu, s, s1);
            this.indx = i;
        }
    }

    public static class DefaultArmy {

        public void save(PathAir pathair) {
            this.iRegiment = pathair.iRegiment;
            this.iSquadron = pathair.iSquadron;
            this.iWing = pathair.iWing;
            this.sCountry = pathair.sCountry;
        }

        public void load(PathAir pathair) {
            pathair.iRegiment = this.iRegiment;
            pathair.iSquadron = this.iSquadron;
            pathair.iWing = this.iWing;
            pathair.sCountry = this.sCountry;
        }

        public int    iRegiment;
        public int    iSquadron;
        public int    iWing;
        public String sCountry;

        public DefaultArmy() {
        }
    }

    public static class Type {

        public String name;
        public Item   item[];

        public Type(String s, Item aitem[]) {
            this.name = s;
            this.item = aitem;
        }
    }

    public static class Item {

        public String  name;
        public Class   clazz;
        public int     army;
        public boolean bEnablePlayer;
        public double  speedMin;
        public double  speedMax;
        public double  speedRunway;

        public Item(String s, Class class1, int i) {
            this.speedMin = 200D;
            this.speedMax = 500D;
            this.speedRunway = 200D;
            this.name = s;
            this.clazz = class1;
            this.army = i;
            this.bEnablePlayer = Property.containsValue(class1, "cockpitClass");
            String s1 = Property.stringValue(class1, "FlightModel", null);
            if (s1 != null) {
                SectFile sectfile = FlightModelMain.sectFile(s1);
                this.speedMin = sectfile.get("Params", "Vmin", (float) this.speedMin);
                this.speedMax = sectfile.get("Params", "VmaxH", (float) this.speedMax);
                this.speedRunway = this.speedMin;
            }
        }
    }

    public PlMisAir() {
        this.defaultHeight = 500D;
        this.defaultSpeed = 300D;
        int i = Army.amountSingle();
        if (i < Army.amountNet()) {
            i = Army.amountNet();
        }
        this.defaultArmy = new DefaultArmy[i];
        for (int j = 0; j < i; j++) {
            this.defaultArmy[j] = new DefaultArmy();
        }

        this.iArmyRegimentList = 0;
        this.sCountry = "";
        this.regimentList = new ArrayList();
        this.viewMap = new HashMapInt();
        this._actorInfo = new String[2];
        this.tabSkin = new com.maddox.gwindow.GWindowTabDialogClient.Tab[4];
        this._squads = new Object[4];
        this._wings = new Object[4];
        this.wPlayer = new GWindowCheckBox[4];
        this.wSkills = new GWindowComboControl[4];
        this.wSkins = new GWindowComboControl[4];
        this.wNoseart = new GWindowComboControl[4];
        this.wPilots = new GWindowComboControl[4];
        this.wSpawnPointSet = new GWindowButton[4];
        this.wSpawnPointClear = new GWindowButton[4];
        this.wSpawnPointLabel = new GWindowLabel[4];
        this.renders = new GUIRenders[4];
        this.camera3D = new Camera3D[4];
        this.render3D = new _Render3D[4];
        this.meshName = null;
        this.actorMesh = new ActorSimpleHMesh[4];
        this._orient = new Orient();
        this.bSpawnFromStationary = false;
    }

    private boolean makeRegimentList(int i, String s) {
        if ((this.iArmyRegimentList == i) && this.sCountry.equals(s)) {
            return false;
        }
        this.initCountry();
        this.wCountry.clear(false);
        ArrayList arraylist = this.listCountry[i];
        for (int j = 0; j < arraylist.size(); j++) {
            Country country = (Country) arraylist.get(j);
            this.wCountry.add(country.i18nName);
        }

        if ((s != null) && !this.mapCountry[i].containsKey(s)) {
            s = null;
        }
        if (s == null) {
            switch (i) {
                case 0:
                    s = "nn";
                    break;

                case 1:
                    s = "ru";
                    break;

                case 2:
                    s = "de";
                    break;
            }
        }
        Integer integer = (Integer) this.mapCountry[i].get(s);
        this.wCountry.setSelected(integer.intValue(), true, false);
        this.sCountry = s;
        this.regimentList.clear();
        List list = Regiment.getAll();
        int k = list.size();
        for (int l = 0; l < k; l++) {
            Regiment regiment = (Regiment) list.get(l);
            if ((regiment.getArmy() == i) && regiment.branch().equals(s)) {
                this.regimentList.add(regiment);
            }
        }

        this.iArmyRegimentList = i;
        this.wRegiment.clear(false);
        this.wRegiment.setSelected(-1, false, false);
        k = this.regimentList.size();
        if ((this.wRegiment.posEnable == null) || (this.wRegiment.posEnable.length < k)) {
            this.wRegiment.posEnable = new boolean[k];
        }
        for (int i1 = 0; i1 < k; i1++) {
            Regiment regiment1 = (Regiment) this.regimentList.get(i1);
            String s1 = I18N.regimentShort(regiment1.shortInfo());
            if ((s1 != null) && (s1.length() > 0) && (s1.charAt(0) == '<')) {
                s1 = I18N.regimentInfo(regiment1.info());
            }
            this.wRegiment.add(s1);
            this.wRegiment.posEnable[i1] = true;
        }

        this.wRegiment.setSelected(0, true, false);
        return true;
    }

    public void load(SectFile sectfile) {
        int i = sectfile.sectionIndex("Wing");
        if (i < 0) {
            return;
        }
        int j = sectfile.vars(i);
        Point3d point3d = new Point3d();
        for (int k = 0; k < j; k++) {
            String s = sectfile.var(i, k);
            String s1 = s + "_Way";
            if (!sectfile.sectionExist(s)) {
                Plugin.builder.tipErr("MissionLoad: Section '" + s + "' not found");
                continue;
            }
            int l = sectfile.sectionIndex(s1);
            if (l < 0) {
                Plugin.builder.tipErr("MissionLoad: Section '" + s1 + "' not found");
                continue;
            }
            int i1 = sectfile.vars(l);
            if (i1 == 0) {
                Plugin.builder.tipErr("MissionLoad: Section '" + s1 + "' is empty");
                continue;
            }
            String s2 = sectfile.get(s, "Class", (String) null);
            if (s2 == null) {
                Plugin.builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' not present");
                continue;
            }
            Class class1 = null;
            try {
                class1 = ObjIO.classForName(s2);
            } catch (Exception exception) {
                Plugin.builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' contains unknown class");
                continue;
            }
            int j1 = 0;
            int k1 = 0;
            for (j1 = 0; j1 < this.type.length; j1++) {
                for (k1 = 0; k1 < this.type[j1].item.length; k1++) {
                    if (this.type[j1].item[k1].clazz == class1) {
                        break;
                    }
                }

                if (k1 < this.type[j1].item.length) {
                    break;
                }
            }

            if (j1 >= this.type.length) {
                Plugin.builder.tipErr("MissionLoad: In section '" + s + "' field 'Class' contains unknown class");
            } else {
                boolean flag = sectfile.get(s, "OnlyAI", 0, 0, 1) == 1;
                boolean flag1 = sectfile.get(s, "Parachute", 1, 0, 1) == 1;
                int l1 = sectfile.get(s, "Fuel", 100, 0, 100);
                int i2 = sectfile.get(s, "Planes", 1, 1, 4);
                int j2 = -1;
                if (sectfile.exist(s, "Skill")) {
                    j2 = sectfile.get(s, "Skill", 1, 0, 3);
                }
                int ai[] = new int[4];
                for (int k2 = 0; k2 < 4; k2++) {
                    if (sectfile.exist(s, "Skill" + k2)) {
                        ai[k2] = sectfile.get(s, "Skill" + k2, 1, 0, 3);
                    } else {
                        ai[k2] = j2 != -1 ? j2 : 1;
                    }
                }

                String as[] = new String[4];
                for (int l2 = 0; l2 < 4; l2++) {
                    as[l2] = sectfile.get(s, "skin" + l2, (String) null);
                }

                String as1[] = new String[4];
                for (int i3 = 0; i3 < 4; i3++) {
                    as1[i3] = sectfile.get(s, "noseart" + i3, (String) null);
                }

                String as2[] = new String[4];
                for (int j3 = 0; j3 < 4; j3++) {
                    as2[j3] = sectfile.get(s, "pilot" + j3, (String) null);
                }

                boolean aflag[] = new boolean[4];
                for (int k3 = 0; k3 < 4; k3++) {
                    aflag[k3] = sectfile.get(s, "numberOn" + k3, 1, 0, 1) == 1;
                }

                String s3 = s.substring(0, s.length() - 2);
                Regiment regiment = (Regiment) Actor.getByName(s3);
                int l3 = 0;
                int i4 = 0;
                int j4 = 0;
                if (regiment != null) {
                    this.makeRegimentList(regiment.getArmy(), regiment.branch());
                    l3 = this.regimentList.indexOf(regiment);
                    if (l3 >= 0) {
                        j4 = Character.getNumericValue(s.charAt(s.length() - 1)) - Character.getNumericValue('0');
                        i4 = Character.getNumericValue(s.charAt(s.length() - 2)) - Character.getNumericValue('0');
                        if (j4 < 0) {
                            j4 = 0;
                        }
                        if (j4 > 3) {
                            j4 = 3;
                        }
                        if (i4 < 0) {
                            i4 = 0;
                        }
                        if (i4 > 3) {
                            i4 = 3;
                        }
                    } else {
                        regiment = null;
                    }
                }
                if (regiment == null) {
                    int k4 = sectfile.get(s, "Army", 0);
                    if ((k4 < 1) || (k4 >= Builder.armyAmount())) {
                        k4 = 1;
                    }
                    this.makeRegimentList(k4, null);
                    regiment = (Regiment) this.regimentList.get(0);
                    s3 = regiment.name();
                }
                String s4 = sectfile.get(s, "weapons", (String) null);
                int l4 = sectfile.get(s, "StartTime", 0);
                if (l4 < 0) {
                    l4 = 0;
                }
                String as3[] = new String[4];
                for (int i5 = 0; i5 < 4; i5++) {
                    as3[i5] = sectfile.get(s, "spawn" + i5, (String) null);
                }

                PathAir pathair = new PathAir(Plugin.builder.pathes, j1, k1);
                pathair.setArmy(regiment.getArmy());
                pathair.sRegiment = s3;
                pathair.iRegiment = l3;
                pathair.sCountry = regiment.branch();
                pathair.iSquadron = i4;
                pathair.iWing = j4;
                pathair.fuel = l1;
                pathair.bOnlyAI = flag;
                pathair.bParachute = flag1;
                pathair.planes = i2;
                pathair.skill = j2;
                pathair.skills = ai;
                pathair.skins = as;
                pathair.noseart = as1;
                pathair.pilots = as2;
                pathair.bNumberOn = aflag;
                pathair.weapons = s4;
                pathair.setSpawnPointPlaneName(0, as3[0]);
                pathair.setSpawnPointPlaneName(1, as3[1]);
                pathair.setSpawnPointPlaneName(2, as3[2]);
                pathair.setSpawnPointPlaneName(3, as3[3]);
                if (!this.searchEnabledSlots(pathair)) {
                    pathair.destroy();
                    Plugin.builder.tipErr("MissionLoad: Section '" + s + "', regiment table very small");
                } else {
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    Property.set(pathair, "builderPlugin", this);
                    pathair.drawing(this.viewMap.containsKey(j1));
                    PAir pair2 = null;
                    for (int j5 = 0; j5 < i1; j5++) {
                        String s5 = sectfile.var(l, j5);
                        byte byte0 = -1;
                        int k5 = 0;
                        if (s5.startsWith("NORMFLY")) {
                            byte0 = 0;
                            if (s5.endsWith("_401")) {
                                k5 = WayPoint.WP_NORMFLY_CAP_TRIANGLE;
                            } else if (s5.endsWith("_402")) {
                                k5 = WayPoint.WP_NORMFLY_CAP_SQUARE;
                            } else if (s5.endsWith("_403")) {
                                k5 = WayPoint.WP_NORMFLY_CAP_PENTAGON;
                            } else if (s5.endsWith("_404")) {
                                k5 = WayPoint.WP_NORMFLY_CAP_HEXAGON;
                            } else if (s5.endsWith("_405")) {
                                k5 = WayPoint.WP_NORMFLY_CAP_RANDOM;
                            } else if (s5.endsWith("_407")) {
                                k5 = WayPoint.WP_NORMFLY_ART_SPOT;
                            }
                        } else if (s5.startsWith("TAKEOFF")) {
                            byte0 = 1;
                            if (s5.endsWith("_001")) {
                                k5 = WayPoint.WP_TAKEOFF_NORMAL_DELAY;
                            } else if (s5.endsWith("_002")) {
                                k5 = WayPoint.WP_TAKEOFF_PAIRS;
                            } else if (s5.endsWith("_003")) {
                                k5 = WayPoint.WP_TAKEOFF_LINE;
                            } else if (s5.endsWith("_004")) {
                                k5 = WayPoint.WP_TAKEOFF_TAXI;
                            } else if (s5.endsWith("_005")) {
                                k5 = WayPoint.WP_TAKEOFF_TAXI;
                            }
                        } else if (s5.startsWith("LANDING")) {
                            byte0 = 2;
                            if (s5.endsWith("_101")) {
                                k5 = WayPoint.WP_LANDING_RIGHT;
                            } else if (s5.endsWith("_102")) {
                                k5 = WayPoint.WP_LANDING_SHORTLEFT;
                            } else if (s5.endsWith("_103")) {
                                k5 = WayPoint.WP_LANDING_SHORTRIGHT;
                            } else if (s5.endsWith("_104")) {
                                k5 = WayPoint.WP_LANDING_STRAIGHTIN;
                            }
                        } else if (s5.startsWith("GATTACK")) {
                            byte0 = 3;
                        } else {
                            Plugin.builder.tipErr("MissionLoad: Section '" + s1 + "' contains unknown type waypoint");
                            pathair.destroy();
                            continue;
                        }
                        String s6 = sectfile.value(l, j5);
                        if ((s6 == null) || (s6.length() <= 0)) {
                            Plugin.builder.tipErr("MissionLoad: Section '" + s1 + "' type '" + s5 + "' is empty");
                            pathair.destroy();
                        } else {
                            NumberTokenizer numbertokenizer = new NumberTokenizer(s6);
                            point3d.x = numbertokenizer.next(-1E+030D, -1E+030D, 1E+030D);
                            point3d.y = numbertokenizer.next(-1E+030D, -1E+030D, 1E+030D);
                            double d = numbertokenizer.next(0.0D, 0.0D, 10000D);
                            double d1 = numbertokenizer.next(0.0D, 0.0D, 1000D);
                            String s7 = null;
                            int l5 = 0;
                            String s8 = null;
                            s7 = numbertokenizer.next(null);
                            if (s7 != null) {
                                if (s7.equals("&0")) {
                                    s8 = s7;
                                    s7 = null;
                                } else if (s7.equals("&1")) {
                                    s8 = s7;
                                    s7 = null;
                                } else {
                                    l5 = numbertokenizer.next(0);
                                    s8 = numbertokenizer.next(null);
                                }
                            }
                            String s9 = null;
                            s9 = numbertokenizer.next(null);
                            if ((s9 != null) && s9.startsWith("F")) {
                                s9 = s9.substring(1);
                            }
                            switch (byte0) {
                                default:
                                    break;

                                case 0:
                                    byte0 = 0;
                                    break;

                                case 3:
                                    if ((s7 != null) && s7.startsWith("Bridge")) {
                                        s7 = " " + s7;
                                    }
                                    byte0 = 3;
                                    break;

                                case 1:
                                    byte0 = 1;
                                    break;

                                case 2:
                                    byte0 = 2;
                                    break;
                            }
                            pair2 = new PAir(pathair, ((pair2)), point3d, byte0, d, d1, null);
                            pair2.waypointType = k5;
                            if ((k5 > 0) && (byte0 != 2)) {
                                String s10 = sectfile.value(l, j5 + 1);
                                NumberTokenizer numbertokenizer1 = new NumberTokenizer(s10);
                                switch (k5) {
                                    case 401:
                                    case 402:
                                    case 403:
                                    case 404:
                                    case 405:
                                        pair2.cycles = numbertokenizer1.next(0);
                                        pair2.delayTimer = numbertokenizer1.next(0);
                                        pair2.orient = numbertokenizer1.next(0);
                                        pair2.baseSize = numbertokenizer1.next(0);
                                        pair2.altDifference = numbertokenizer1.next(0);
                                        break;

                                    case 407:
                                        pair2.cycles = numbertokenizer1.next(0);
                                        pair2.delayTimer = numbertokenizer1.next(0);
                                        pair2.orient = numbertokenizer1.next(0);
                                        pair2.baseSize = numbertokenizer1.next(0);
                                        break;

                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                    case 5:
                                        pair2.targetTrigger = numbertokenizer1.next(0);
                                        pair2.delayTimer = numbertokenizer1.next(0);
                                        pair2.takeoffSpacing = numbertokenizer1.next(0);
                                        pair2.ignoreAlt = numbertokenizer1.next(0);
                                        break;
                                }
                                j5++;
                            }
                            if (s7 != null) {
                                pair2.iTarget = l5;
                                pair2.sTarget = s7;
                            }
                            if ((s8 != null) && s8.equals("&1")) {
                                pair2.bRadioSilence = true;
                            }
                            if (s9 != null) {
                                pair2.formation = Integer.parseInt(s9);
                            }
                        }
                    }

                    if (l4 > 0) {
                        PAir pair = (PAir) pathair.point(0);
                        pair.time = l4 * 60D;
                        pathair.computeTimes(false);
                    }
                }
            }
        }

    }

    public boolean save(SectFile sectfile) {
        if (Plugin.builder.pathes == null) {
            return true;
        }
        int i = sectfile.sectionIndex("Wing");
        Object aobj[] = Plugin.builder.pathes.getOwnerAttached();
        for (int j = 0; j < aobj.length; j++) {
            Actor actor = (Actor) aobj[j];
            if (actor == null) {
                break;
            }
            if (actor instanceof PathAir) {
                PathAir pathair = (PathAir) actor;
                int k = pathair.points();
                if (i <= -1) {
                    i = sectfile.sectionAdd("Wing");
                }
                String s = pathair.sRegiment + pathair.iSquadron + pathair.iWing;
                String s1 = s + "_Way";
                sectfile.lineAdd(i, s, "");
                int l = sectfile.sectionAdd(s);
                sectfile.lineAdd(l, "Planes", "" + pathair.planes);
                if (pathair.bOnlyAI) {
                    sectfile.lineAdd(l, "OnlyAI", "1");
                }
                if (!pathair.bParachute) {
                    sectfile.lineAdd(l, "Parachute", "0");
                }
                if (pathair.skill != -1) {
                    sectfile.lineAdd(l, "Skill", "" + pathair.skill);
                }
                for (int i1 = 0; i1 < 4; i1++) {
                    if (pathair.skill != pathair.skills[i1]) {
                        sectfile.lineAdd(l, "Skill" + i1, "" + pathair.skills[i1]);
                    }
                }

                for (int j1 = 0; j1 < 4; j1++) {
                    if (pathair.skins[j1] != null) {
                        sectfile.lineAdd(l, "skin" + j1, pathair.skins[j1]);
                    }
                }

                for (int k1 = 0; k1 < 4; k1++) {
                    if (pathair.noseart[k1] != null) {
                        sectfile.lineAdd(l, "noseart" + k1, pathair.noseart[k1]);
                    }
                }

                for (int l1 = 0; l1 < 4; l1++) {
                    if (pathair.pilots[l1] != null) {
                        sectfile.lineAdd(l, "pilot" + l1, pathair.pilots[l1]);
                    }
                }

                for (int i2 = 0; i2 < 4; i2++) {
                    if (!pathair.bNumberOn[i2]) {
                        sectfile.lineAdd(l, "numberOn" + i2, "0");
                    }
                }

                sectfile.lineAdd(l, "Class", ObjIO.classGetName(this.type[pathair._iType].item[pathair._iItem].clazz));
                sectfile.lineAdd(l, "Fuel", "" + pathair.fuel);
                if (pathair.weapons != null) {
                    sectfile.lineAdd(l, "weapons", "" + pathair.weapons);
                } else {
                    sectfile.lineAdd(l, "weapons", "none");
                }
                PAir pair = (PAir) pathair.point(0);
                if (pair.time > 0.0D) {
                    sectfile.lineAdd(l, "StartTime", "" + (int) Math.round(pair.time / 60D));
                }
                boolean flag = true;
                for (int j2 = 0; j2 < 4; j2++) {
                    if (pathair.getSpawnPoint(j2) != null) {
                        sectfile.lineAdd(l, "spawn" + j2, pathair.getSpawnPoint(j2).name());
                    } else if (j2 < pathair.planes) {
                        flag = false;
                    }
                }

                int k2 = sectfile.sectionAdd(s1);
                for (int l2 = 0; l2 < k; l2++) {
                    PAir pair1 = (PAir) pathair.point(l2);
                    Point3d point3d = pair1.pos.getAbsPoint();
                    switch (pair1.type()) {
                        default:
                            break;

                        case 0:
                            String s2 = "";
                            if (pair1.waypointType > 0) {
                                s2 = "_" + pair1.waypointType;
                            }
                            sectfile.lineAdd(k2, "NORMFLY" + s2, this.fmt(point3d.x) + " " + this.fmt(point3d.y) + " " + this.fmt(pair1.height) + " " + this.fmt(pair1.speed) + this.saveTarget(pair1) + this.saveRadioSilence(pair1) + this.saveFormation(pair1));
                            if (pair1.waypointType > 400) {
                                sectfile.lineAdd(k2, "TRIGGERS", pair1.cycles + " " + pair1.delayTimer + " " + (int) pair1.orient + " " + pair1.baseSize + " " + pair1.altDifference);
                            }
                            break;

                        case 3:
                            String s3 = "";
                            if (pair1.waypointType > 0) {
                                s3 = "_" + pair1.waypointType;
                            }
                            sectfile.lineAdd(k2, "GATTACK" + s3, this.fmt(point3d.x) + " " + this.fmt(point3d.y) + " " + this.fmt(pair1.height) + " " + this.fmt(pair1.speed) + this.saveTarget(pair1) + this.saveRadioSilence(pair1) + this.saveFormation(pair1));
                            if (pair1.waypointType > 200) {
                                sectfile.lineAdd(k2, "TRIGGERS", pair1.cycles + " " + pair1.delayTimer + " " + (int) pair1.orient + " " + pair1.baseSize + " " + pair1.targetTrigger + " " + pair1.altDifference);
                            }
                            break;

                        case 1:
                            String s4 = "";
                            if (pair1.waypointType > 0) {
                                int i3 = pair1.waypointType;
                                if ((i3 == 4) && flag) {
                                    i3 = 5;
                                }
                                s4 = "_00" + i3;
                            }
                            sectfile.lineAdd(k2, "TAKEOFF" + s4, this.fmt(point3d.x) + " " + this.fmt(point3d.y) + " 0 0" + this.saveTarget(pair1) + this.saveRadioSilence(pair1));
                            if (pair1.waypointType > 0) {
                                sectfile.lineAdd(k2, "TRIGGERS", pair1.targetTrigger + " " + pair1.delayTimer + " " + pair1.takeoffSpacing + " " + pair1.ignoreAlt);
                            }
                            break;

                        case 2:
                            String s5 = "";
                            if (pair1.waypointType > 0) {
                                s5 = "_" + pair1.waypointType;
                            }
                            sectfile.lineAdd(k2, "LANDING" + s5, this.fmt(point3d.x) + " " + this.fmt(point3d.y) + " 0 0" + this.saveTarget(pair1) + this.saveRadioSilence(pair1));
                            break;
                    }
                }

            }
        }

        return true;
    }

    private String fmt(double d) {
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

    private String saveTarget(PAir pair) {
        if (!Actor.isValid(pair.getTarget())) {
            return "";
        }
        if (pair.getTarget() instanceof PPoint) {
            PPoint ppoint = (PPoint) pair.getTarget();
            Path path = (Path) ppoint.getOwner();
            return " " + path.name() + " " + path.pointIndx(ppoint);
        } else {
            return " " + pair.getTarget().name() + " 0";
        }
    }

    private String saveRadioSilence(PAir pair) {
        return " " + (pair.bRadioSilence ? "&1" : "&0");
    }

    private String saveFormation(PAir pair) {
        if (pair.formation == 0) {
            return "";
        } else {
            return " F" + pair.formation;
        }
    }

    private void clampSpeed(PAir pair) {
        PathAir pathair = (PathAir) pair.getOwner();
        if ((pair.type() == 0) || (pair.type() == 3)) {
            if (pair.speed < this.type[pathair._iType].item[pathair._iItem].speedMin) {
                pair.speed = this.type[pathair._iType].item[pathair._iItem].speedMin;
            }
            if (pair.speed > this.type[pathair._iType].item[pathair._iItem].speedMax) {
                pair.speed = this.type[pathair._iType].item[pathair._iItem].speedMax;
            }
        } else {
            pair.speed = 0.0D;
        }
    }

    private void clampSpeed(PathAir pathair) {
        int i = pathair.points();
        for (int j = 0; j < i; j++) {
            this.clampSpeed((PAir) pathair.point(j));
        }

    }

    public void insert(Loc loc, boolean flag) {
        PathAir pathair = null;
        try {
            Point3d point3d = loc.getPoint();
            int i = Plugin.builder.wSelect.comboBox1.getSelected();
            int j = Plugin.builder.wSelect.comboBox2.getSelected();
            if (Plugin.builder.selectedPath() != null) {
                Path path = Plugin.builder.selectedPath();
                if (!(path instanceof PathAir)) {
                    return;
                }
                pathair = (PathAir) path;
                if (((i - this.startComboBox1) != pathair._iType) || (j != pathair._iItem)) {
                    Plugin.builder.setSelected(null);
                }
            }
            PAir pair;
            if (Plugin.builder.selectedPoint() != null) {
                PAir pair1 = (PAir) Plugin.builder.selectedPoint();
                if (pair1.type() == 2) {
                    return;
                }
                int k = 0;
                if ((pair1.type() == 1) && (pair1.waypointType == 4)) {
                    k = 1;
                }
                pair = new PAir(Plugin.builder.selectedPath(), Plugin.builder.selectedPoint(), point3d, k, this.defaultHeight, this.defaultSpeed, pair1);
            } else {
                if ((i < this.startComboBox1) || (i >= (this.startComboBox1 + this.type.length))) {
                    return;
                }
                i -= this.startComboBox1;
                if ((j < 0) || (j >= this.type[i].item.length)) {
                    return;
                }
                pathair = new PathAir(Plugin.builder.pathes, i, j);
                pathair.setArmy(this.type[i].item[j].army);
                this.defaultArmy[this.type[i].item[j].army].load(pathair);
                if (!this.searchEnabledSlots(pathair)) {
                    pathair.destroy();
                    return;
                }
                pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                Property.set(pathair, "builderPlugin", this);
                pathair.drawing(this.viewMap.containsKey(i));
                pair = new PAir(pathair, null, point3d, 0, this.defaultHeight, this.defaultSpeed, null);
            }
            this.clampSpeed(pair);
            Plugin.builder.setSelected(pair);
            PlMission.setChanged();
        } catch (Exception exception) {
            if ((pathair != null) && (pathair.points() == 0)) {
                pathair.destroy();
            }
            System.out.println(exception);
        }
    }

    public void changeType() {
        int i = Plugin.builder.wSelect.comboBox1.getSelected() - this.startComboBox1;
        int j = Plugin.builder.wSelect.comboBox2.getSelected();
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (i != pathair._iType) {
            return;
        }
        pathair.skins = new String[4];
        pathair.noseart = new String[4];
        pathair._iItem = j;
        Class class1 = this.type[i].item[j].clazz;
        String as[] = Aircraft.getWeaponsRegistered(class1);
        if ((as != null) && (as.length > 0)) {
            int k;
            for (k = 0; k < as.length; k++) {
                String s = as[k];
                if (Aircraft.isWeaponDateOk(class1, as[k]) && s.equalsIgnoreCase(pathair.weapons)) {
                    break;
                }
            }

            if (k == as.length) {
                pathair.weapons = as[0];
            }
        }
        this.clampSpeed(pathair);
        this.fillDialogWay();
        this.fillSkins();
        this.fillNoseart();
        this.syncSkins();
        this.syncNoseart();
        this.syncPilots();
        this.resetMesh();
        PlMission.setChanged();
    }

    public void configure() {
        if (Plugin.getPlugin("Mission") == null) {
            throw new RuntimeException("PlMisAir: plugin 'Mission' not found");
        }
        this.pluginMission = (PlMission) Plugin.getPlugin("Mission");
        if (this.sectFile == null) {
            throw new RuntimeException("PlMisAir: field 'sectFile' not defined");
        }
        SectFile sectfile = new SectFile(this.sectFile, 0);
        int i = sectfile.sections();
        if (i <= 0) {
            throw new RuntimeException("PlMisAir: file '" + this.sectFile + "' is empty");
        }
        this.type = new Type[i];
        for (int j = 0; j < i; j++) {
            String s = sectfile.sectionName(j);
            int k = sectfile.vars(j);
            Item aitem[] = new Item[k];
            for (int l = 0; l < k; l++) {
                String s1 = sectfile.var(j, l);
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.value(j, l));
                String s2 = numbertokenizer.next((String) null);
                int i1 = numbertokenizer.next(1, 1, Builder.armyAmount() - 1);
                Class class1 = null;
                try {
                    class1 = ObjIO.classForName(s2);
                } catch (Exception exception) {
                    throw new RuntimeException("PlMisAir: class '" + s2 + "' not found");
                }
                aitem[l] = new Item(s1, class1, i1);
            }

            this.type[j] = new Type(s, aitem);
        }

    }

    void viewUpdate() {
        if (Plugin.builder.pathes == null) {
            return;
        }
        Object aobj[] = Plugin.builder.pathes.getOwnerAttached();
        for (int i = 0; i < aobj.length; i++) {
            Actor actor = (Actor) aobj[i];
            if (actor == null) {
                break;
            }
            if (actor instanceof PathAir) {
                PathAir pathair = (PathAir) actor;
                pathair.drawing(this.viewMap.containsKey(pathair._iType));
            }
        }

        if (Plugin.builder.selectedPath() != null) {
            Path path = Plugin.builder.selectedPath();
            if (path instanceof PathAir) {
                PathAir pathair1 = (PathAir) path;
                if (!this.viewMap.containsKey(pathair1._iType)) {
                    Plugin.builder.setSelected(null);
                }
            }
        }
        if (!Plugin.builder.isFreeView()) {
            Plugin.builder.repaint();
        }
    }

    void viewType(int i, boolean flag) {
        if (flag) {
            this.viewMap.put(i, null);
        } else {
            this.viewMap.remove(i);
        }
        this.viewUpdate();
    }

    void viewType(int i) {
        this.viewType(i, this.viewType[i].bChecked);
    }

    public void viewTypeAll(boolean flag) {
        for (int i = 0; i < this.type.length; i++) {
            if (this.viewType[i].bChecked != flag) {
                this.viewType[i].bChecked = flag;
                this.viewType(i, flag);
            }
        }

    }

    private void fillComboBox1() {
        this.startComboBox1 = Plugin.builder.wSelect.comboBox1.size();
        for (int i = 0; i < this.type.length; i++) {
            Plugin.builder.wSelect.comboBox1.add(I18N.technic(this.type[i].name));
        }

        if (this.startComboBox1 == 0) {
            Plugin.builder.wSelect.comboBox1.setSelected(0, true, false);
        }
    }

    private void fillComboBox2(int i, int j) {
        if ((i < this.startComboBox1) || (i >= (this.startComboBox1 + this.type.length))) {
            return;
        }
        if (Plugin.builder.wSelect.curFilledType != i) {
            Plugin.builder.wSelect.curFilledType = i;
            Plugin.builder.wSelect.comboBox2.clear(false);
            for (int k = 0; k < this.type[i - this.startComboBox1].item.length; k++) {
                Plugin.builder.wSelect.comboBox2.add(I18N.plane(this.type[i - this.startComboBox1].item[k].name));
            }

            Plugin.builder.wSelect.comboBox1.setSelected(i, true, false);
        }
        Plugin.builder.wSelect.comboBox2.setSelected(j, true, false);
        Class class1 = this.type[i].item[j].clazz;
        Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, this.currentCountry()), false);
    }

    public String[] actorInfo(Actor actor) {
        PathAir pathair = (PathAir) actor.getOwner();
        this._actorInfo[0] = I18N.technic(this.type[pathair._iType].name) + "." + I18N.plane(this.type[pathair._iType].item[pathair._iItem].name) + ":" + pathair.typedName;
        PAir pair = (PAir) actor;
        int i = pathair.pointIndx(pair);
        this._actorInfo[1] = "(" + i + ") " + Plugin.timeSecToString(pair.time + (int) (World.getTimeofDay() * 60F * 60F));
        return this._actorInfo;
    }

    public void syncSelector() {
        Plugin.builder.wSelect.tabsClient.addTab(1, this.tabActor);
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        this.fillComboBox2(pathair._iType + this.startComboBox1, pathair._iItem);
        Plugin.builder.wSelect.tabsClient.addTab(2, this.tabWay);
        Plugin.builder.wSelect.tabsClient.addTab(3, this.tabWay2);
        this.fillEditActor();
        this.fillDialogWay();
        PathAir pathair1 = (PathAir) Plugin.builder.selectedPath();
        for (int i = 0; i < pathair1.planes; i++) {
            Plugin.builder.wSelect.tabsClient.addTab(i + 4, this.tabSkin[i]);
            this.fillEditSkin(i);
        }

        this.fillSkins();
        this.fillNoseart();
        this.fillPilots();
        this.syncSkins();
        this.syncNoseart();
        this.syncPilots();
        this.resetMesh();
    }

    public void updateSelector() {
        this.fillDialogWay();
    }

    public void updateSelectorMesh() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(this.type[pathair._iType].item[pathair._iItem].clazz, pathair.regiment().country());
        if (paintscheme != null) {
            HierMesh hiermesh = Plugin.builder.wSelect.getHierMesh();
            if (hiermesh != null) {
                paintscheme.prepare(this.type[pathair._iType].item[pathair._iItem].clazz, hiermesh, pathair.regiment(), pathair.iSquadron, pathair.iWing, 0);
            }
        }
    }

    public void createGUI() {
        this.fillComboBox1();
        this.fillComboBox2(0, 0);
        Plugin.builder.wSelect.comboBox1.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l) {
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if ((i1 >= 0) && (k == 2)) {
                    PlMisAir.this.fillComboBox2(i1, 0);
                }
                return false;
            }

        });
        Plugin.builder.wSelect.comboBox2.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int k, int l) {
                if (k != 2) {
                    return false;
                }
                int i1 = Plugin.builder.wSelect.comboBox1.getSelected();
                if ((i1 < PlMisAir.this.startComboBox1) || (i1 >= (PlMisAir.this.startComboBox1 + PlMisAir.this.type.length))) {
                    return false;
                }
                int j1 = Plugin.builder.wSelect.comboBox2.getSelected();
                Class class1 = PlMisAir.this.type[i1 - PlMisAir.this.startComboBox1].item[j1].clazz;
                Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, PlMisAir.this.currentCountry()), false);
                PlMisAir.this.resetMesh();
                PlMisAir.this.fillSkins();
                PlMisAir.this.fillNoseart();
                PlMisAir.this.fillPilots();
                PlMisAir.this.syncSkins();
                PlMisAir.this.syncNoseart();
                PlMisAir.this.syncPilots();
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                if (pathair != null) {
                    pathair.updateTypedName();
                }
                PlMisAir.this.fillEditActor();
                return false;
            }

        });
        int i;
        for (i = Plugin.builder.mDisplayFilter.subMenu.size() - 1; i >= 0; i--) {
            if (this.pluginMission.viewBridge == Plugin.builder.mDisplayFilter.subMenu.getItem(i)) {
                break;
            }
        }

        if (--i >= 0) {
            int j = i;
            i = this.type.length - 1;
            this.viewType = new ViewItem[this.type.length];
            for (; i >= 0; i--) {
                ViewItem viewitem = null;
                if ("de".equals(RTSConf.cur.locale.getLanguage())) {
                    viewitem = (ViewItem) Plugin.builder.mDisplayFilter.subMenu.addItem(j, new ViewItem(i, Plugin.builder.mDisplayFilter.subMenu, I18N.technic(this.type[i].name) + " " + Plugin.i18n("show"), null));
                } else {
                    viewitem = (ViewItem) Plugin.builder.mDisplayFilter.subMenu.addItem(j, new ViewItem(i, Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("show") + " " + I18N.technic(this.type[i].name), null));
                }
                viewitem.bChecked = true;
                this.viewType[i] = viewitem;
                this.viewType(i, true);
            }

        }
        this.initEditActor();
        this.initEditWay();
        this.initEditWay2();
        this.initEditSkin();
    }

    public void start() {
        HotKeyCmdEnv.setCurrentEnv(Builder.envName);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "beginSelectSpawnPoint") {

            public void end() {
                if (!Plugin.builder.isLoadedLandscape() || (Plugin.builder.mouseState != 0)) {
                    return;
                }
                if (Plugin.builder.isFreeView()) {
                    return;
                } else {
                    Plugin.builder.beginSelectSpawnPoint();
                    return;
                }
            }

        });
    }

    private boolean searchEnabledSlots(PathAir pathair) {
        this.makeRegimentList(pathair.getArmy(), pathair.sCountry);
        int i = this.regimentList.size();
        if (pathair.iRegiment < 0) {
            pathair.iRegiment = 0;
        }
        if (pathair.iRegiment >= i) {
            pathair.iRegiment = i - 1;
        }
        for (int j = 0; j < i; j++) {
            Regiment regiment = (Regiment) this.regimentList.get(pathair.iRegiment);
            pathair.sRegiment = regiment.name();
            if (this.isEnabledRegiment(regiment)) {
                for (int k = 0; k < 4; k++) {
                    ResSquadron ressquadron = (ResSquadron) Actor.getByName(regiment.name() + pathair.iSquadron);
                    if (ressquadron == null) {
                        this.defaultArmy[pathair.getArmy()].save(pathair);
                        return true;
                    }
                    if (this.isEnabledSquad(ressquadron)) {
                        for (int l = 0; l < 4; l++) {
                            if (Actor.getByName(regiment.name() + pathair.iSquadron + pathair.iWing) == null) {
                                this.defaultArmy[pathair.getArmy()].save(pathair);
                                return true;
                            }
                            pathair.iWing = (pathair.iWing + 1) % 4;
                        }

                    }
                    pathair.iWing = 0;
                    pathair.iSquadron = (pathair.iSquadron + 1) % 4;
                }

            } else {
                pathair.iWing = 0;
            }
            pathair.iSquadron = 0;
            pathair.iRegiment = (pathair.iRegiment + 1) % i;
            pathair.sCountry = regiment.branch();
        }

        return false;
    }

    private void fillEnabledRegiments(int i) {
        int j = this.regimentList.size();
        for (int k = 0; k < j; k++) {
            if (k == i) {
                this.wRegiment.posEnable[k] = true;
            } else {
                Regiment regiment = (Regiment) this.regimentList.get(k);
                this.wRegiment.posEnable[k] = this.isEnabledRegiment(regiment);
            }
        }

    }

    private String currentCountry() {
        if (this.wRegiment == null) {
            return "ru";
        }
        int i = this.wRegiment.getSelected();
        if (i < 0) {
            return "ru";
        } else {
            return ((Regiment) this.regimentList.get(i)).country();
        }
    }

    private boolean isEnabledRegiment(Regiment regiment) {
        if (regiment.getOwnerAttachedCount() < 4) {
            return true;
        }
        this._squads = regiment.getOwnerAttached(this._squads);
        boolean flag = false;
        for (int i = 0; i < 4; i++) {
            ResSquadron ressquadron = (ResSquadron) this._squads[i];
            this._squads[i] = null;
            if ((ressquadron == null) || this.isEnabledSquad(ressquadron)) {
                flag = true;
            }
        }

        return flag;
    }

    private void fillEnabledSquads(Regiment regiment, int i) {
        for (int j = 0; j < 4; j++) {
            this.wSquadron.posEnable[j] = true;
        }

        this._squads = regiment.getOwnerAttached(this._squads);
        for (int k = 0; k < 4; k++) {
            ResSquadron ressquadron = (ResSquadron) this._squads[k];
            if (ressquadron == null) {
                break;
            }
            this._squads[k] = null;
            if (ressquadron.index() != i) {
                this.wSquadron.posEnable[ressquadron.index()] = this.isEnabledSquad(ressquadron);
            }
        }

    }

    private boolean isEnabledSquad(ResSquadron ressquadron) {
        return ressquadron.getAttachedCount() < 4;
    }

    private void fillEnabledWings(ResSquadron ressquadron, int i) {
        for (int j = 0; j < 4; j++) {
            this.wWing.posEnable[j] = true;
        }

        this._wings = ressquadron.getAttached(this._wings);
        for (int k = 0; k < 4; k++) {
            PathAir pathair = (PathAir) this._wings[k];
            if (pathair == null) {
                break;
            }
            this._wings[k] = null;
            if (pathair.iWing != i) {
                this.wWing.posEnable[pathair.iWing] = false;
            }
        }

    }

    private void controlResized(GWindowDialogClient gwindowdialogclient, GWindow gwindow) {
        if (gwindow == null) {
            return;
        } else {
            gwindow.setSize(gwindowdialogclient.win.dx - gwindow.win.x - gwindowdialogclient.lAF().metric(1.0F), gwindow.win.dy);
            return;
        }
    }

    private void editResized(GWindowDialogClient gwindowdialogclient) {
        this.controlResized(gwindowdialogclient, this.wArmy);
        this.controlResized(gwindowdialogclient, this.wCountry);
        this.controlResized(gwindowdialogclient, this.wRegiment);
        this.controlResized(gwindowdialogclient, this.wSquadron);
        this.controlResized(gwindowdialogclient, this.wWing);
        this.controlResized(gwindowdialogclient, this.wWeapons);
        this.controlResized(gwindowdialogclient, this.wFuel);
        this.controlResized(gwindowdialogclient, this.wPlanes);
        this.controlResized(gwindowdialogclient, this.wSkill);
    }

    public void initEditActor() {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient() {

            public void resized() {
                super.resized();
                PlMisAir.this.editResized(this);
//                if(render3D != null)
//                {
//                    for(int i = 0; i < render3D.length; i++)
//                        render3D[i].setClearColor(GWindowLookAndFeel.getUIBackgroundColor());
//
//                }
            }

        });
        this.tabActor = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("AircraftActor"), gwindowdialogclient);
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, Plugin.i18n("Army"), null));
        gwindowdialogclient.addControl(this.wArmy = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                for (int i = 1; i < Builder.armyAmount(); i++) {
                    this.add(I18N.army(Army.name(i)));
                }

            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                int k = this.getSelected() + 1;
                int l = pathair.getArmy();
                int i1 = pathair.iRegiment;
                int j1 = pathair.iSquadron;
                int k1 = pathair.iWing;
                String s = pathair.sCountry;
                pathair.setArmy(k);
                PlMisAir.this.defaultArmy[k].load(pathair);
                if (!PlMisAir.this.searchEnabledSlots(pathair)) {
                    pathair.setArmy(l);
                    pathair.iRegiment = i1;
                    pathair.iSquadron = j1;
                    pathair.iWing = k1;
                    pathair.sCountry = s;
                    PlMisAir.this.searchEnabledSlots(pathair);
                }
                pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                PlMisAir.this.fillEditActor();
                PlMisAir.this.fillNoseart();
                PlMisAir.this.syncNoseart();
                Class class1 = PlMisAir.this.type[pathair._iType].item[pathair._iItem].clazz;
                Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, PlMisAir.this.currentCountry()), false);
                PlMisAir.this.resetMesh();
                if (Path.player == pathair) {
                    PlMission.cur.missionArmy = k;
                }
                PlMission.setChanged();
                PlMission.checkShowCurrentArmy();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("Country"), null));
        gwindowdialogclient.addControl(this.wCountry = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                int k = this.getSelected();
                Country country = (Country) PlMisAir.this.listCountry[PlMisAir.this.iArmyRegimentList].get(k);
                if (PlMisAir.this.makeRegimentList(PlMisAir.this.iArmyRegimentList, country.name)) {
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    pathair.sCountry = country.name;
                    pathair.iRegiment = 0;
                    PlMisAir.this.searchEnabledSlots(pathair);
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    PlMisAir.this.defaultArmy[pathair.getArmy()].save(pathair);
                    PlMisAir.this.fillEditActor();
                    PlMisAir.this.fillNoseart();
                    PlMisAir.this.syncNoseart();
                    Class class1 = PlMisAir.this.type[pathair._iType].item[pathair._iItem].clazz;
                    Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, PlMisAir.this.currentCountry()), false);
                    PlMisAir.this.resetMesh();
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("Regiment"), null));
        gwindowdialogclient.addControl(this.wRegiment = new GWindowComboControl(gwindowdialogclient, 9F, 5F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    pathair.iRegiment = this.getSelected();
                    PlMisAir.this.searchEnabledSlots(pathair);
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    PlMisAir.this.defaultArmy[pathair.getArmy()].save(pathair);
                    PlMisAir.this.fillEditActor();
                    PlMisAir.this.fillNoseart();
                    PlMisAir.this.syncNoseart();
                    Class class1 = PlMisAir.this.type[pathair._iType].item[pathair._iItem].clazz;
                    Plugin.builder.wSelect.setMesh(Aircraft.getPropertyMesh(class1, PlMisAir.this.currentCountry()), false);
                    PlMisAir.this.resetMesh();
                    PlMission.setChanged();
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, Plugin.i18n("Squadron"), null));
        gwindowdialogclient.addControl(this.wSquadron = new GWindowComboControl(gwindowdialogclient, 9F, 7F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add("1");
                this.add("2");
                this.add("3");
                this.add("4");
                this.posEnable = new boolean[4];
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    pathair.iSquadron = this.getSelected();
                    PlMisAir.this.searchEnabledSlots(pathair);
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    PlMisAir.this.defaultArmy[pathair.getArmy()].save(pathair);
                    PlMisAir.this.fillEditActor();
                    PlMisAir.this.resetMesh();
                    PlMission.setChanged();
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, Plugin.i18n("Wing"), null));
        gwindowdialogclient.addControl(this.wWing = new GWindowComboControl(gwindowdialogclient, 9F, 9F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add("1");
                this.add("2");
                this.add("3");
                this.add("4");
                this.posEnable = new boolean[4];
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    pathair.iWing = this.getSelected();
                    pathair.setName(pathair.sRegiment + pathair.iSquadron + pathair.iWing);
                    PlMisAir.this.defaultArmy[pathair.getArmy()].save(pathair);
                    PlMisAir.this.fillEditActor();
                    PlMisAir.this.resetMesh();
                    PlMission.setChanged();
                    return false;
                }
            }

        });
        this.lWeapons = new ArrayList();
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, Plugin.i18n("Weapons"), null));
        gwindowdialogclient.addControl(this.wWeapons = new GWindowComboControl(gwindowdialogclient, 9F, 11F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                int k = this.getSelected();
                if (k < 0) {
                    pathair.weapons = null;
                } else {
                    pathair.weapons = (String) PlMisAir.this.lWeapons.get(k);
                }
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, Plugin.i18n("Fuel"), null));
        gwindowdialogclient.addControl(this.wFuel = new GWindowEditControl(gwindowdialogclient, 9F, 13F, 7F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bDelayedNotify = true;
                this.bNumericOnly = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                String s = this.getValue();
                int k = 0;
                if (s != null) {
                    try {
                        k = Integer.parseInt(s);
                    } catch (Exception exception) {
                        this.setValue("" + ((PathAir) Plugin.builder.selectedPath()).fuel, false);
                        return false;
                    }
                }
                if (k < 0) {
                    k = 0;
                } else if (k > 100) {
                    k = 100;
                }
                pathair.fuel = k;
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 15F, 7F, 1.3F, Plugin.i18n("Planes"), null));
        gwindowdialogclient.addControl(this.wPlanes = new GWindowComboControl(gwindowdialogclient, 9F, 15F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add("1");
                this.add("2");
                this.add("3");
                this.add("4");
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    pathair.planes = this.getSelected() + 1;
                    PlMisAir.this.checkEditSkinTabs();
                    PlMisAir.this.checkEditSkinSkills();
                    PlMission.setChanged();
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 17F, 7F, 1.3F, Plugin.i18n("Skill"), null));
        gwindowdialogclient.addControl(this.wSkill = new GWindowComboControl(gwindowdialogclient, 9F, 17F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(Plugin.i18n("Rookie"));
                this.add(Plugin.i18n("Average"));
                this.add(Plugin.i18n("Veteran"));
                this.add(Plugin.i18n("Ace"));
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                pathair.skill = this.getSelected();
                for (int k = 0; k < 4; k++) {
                    pathair.skills[k] = pathair.skill;
                }

                PlMisAir.this.checkEditSkinSkills();
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 19F, 7F, 1.3F, Plugin.i18n("OnlyAI"), null));
        gwindowdialogclient.addControl(this.wOnlyAI = new GWindowCheckBox(gwindowdialogclient, 9F, 19F, null) {

            public void preRender() {
                super.preRender();
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                if (pathair == null) {
                    return;
                } else {
                    this.setChecked(pathair.bOnlyAI, false);
                    this.setEnable(PlMisAir.this.type[pathair._iType].item[pathair._iItem].bEnablePlayer);
                    return;
                }
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                pathair.bOnlyAI = this.isChecked();
                if (this.isChecked() && (Path.player == pathair)) {
                    Path.player = null;
                    Path.playerNum = 0;
                }
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 21F, 7F, 1.3F, Plugin.i18n("Parachute"), null));
        gwindowdialogclient.addControl(this.wParachute = new GWindowCheckBox(gwindowdialogclient, 9F, 21F, null) {

            public void preRender() {
                super.preRender();
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                if (pathair == null) {
                    return;
                } else {
                    this.setChecked(pathair.bParachute, false);
                    return;
                }
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    pathair.bParachute = this.isChecked();
                    PlMission.setChanged();
                    return false;
                }
            }

        });
    }

    public void initEditWay2() {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient() {

            public void resized() {
                super.resized();
                PlMisAir.this.editResized(this);
            }

        });
        this.tabWay2 = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("WaypointOptions"), gwindowdialogclient);
        gwindowdialogclient.addLabel(this.wDelayTimerLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("Delay"), null));
        gwindowdialogclient.addLabel(this.wDelayTimerLabel2 = new GWindowLabel(gwindowdialogclient, 15F, 3F, 4F, 1.3F, Plugin.i18n("min"), null));
        gwindowdialogclient.addControl(this.wDelayTimer = new GWindowEditControl(gwindowdialogclient, 9F, 3F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PAir pair = (PAir) Plugin.builder.selectedPoint();
                int k = Integer.parseInt(PlMisAir.this.wDelayTimer.getValue());
                if (k < 0) {
                    k = 0;
                }
                if (k > 1800) {
                    k = 1800;
                }
                pair.delayTimer = k;
                if ((k > 0) && (PlMisAir.this.wTakeOffType.getSelected() == 0)) {
                    pair.waypointType = 1;
                }
                this.setValue("" + k, false);
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wTakeOffTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, Plugin.i18n("TakeOffType"), null));
        gwindowdialogclient.addControl(this.wTakeOffType = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 9F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(Plugin.i18n("TakeoffTNormal"));
                this.add(Plugin.i18n("TakeoffTPairs"));
                this.add(Plugin.i18n("TakeoffTLine"));
                this.add(Plugin.i18n("TakeoffTTaxi"));
                this.posEnable = new boolean[5];
                for (int i = 0; i < 5; i++) {
                    this.posEnable[i] = true;
                }

            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    pair.waypointType = PlMisAir.this.wTakeOffType.getSelected();
                    if (pair.waypointType > 0) {
                        pair.waypointType++;
                    } else if (pair.delayTimer > 0) {
                        pair.waypointType++;
                    }
                    PlMisAir.this.fillDialogWay2();
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wTakeoffSpacingLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("TakeOffSpacing"), null));
        gwindowdialogclient.addLabel(this.wTakeoffSpacingLabel2 = new GWindowLabel(gwindowdialogclient, 15F, 5F, 4F, 1.3F, Plugin.i18n("meters"), null));
        gwindowdialogclient.addControl(this.wTakeoffSpacing = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = false;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PAir pair = (PAir) Plugin.builder.selectedPoint();
                int k = Integer.parseInt(PlMisAir.this.wTakeoffSpacing.getValue());
                if (k < -1000) {
                    k = -1000;
                }
                if (k > 1000) {
                    k = 1000;
                }
                pair.takeoffSpacing = k;
                this.setValue("" + k, false);
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wLandingTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, Plugin.i18n("LandingType"), null));
        gwindowdialogclient.addControl(this.wLandingType = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 9F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(Plugin.i18n("LandingTypeLP"));
                this.add(Plugin.i18n("LandingTypeRP"));
                this.add(Plugin.i18n("LandingTypeSLP"));
                this.add(Plugin.i18n("LandingTypeSRP"));
                this.add(Plugin.i18n("LandingTypeStraightIn"));
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    if (PlMisAir.this.wLandingType.getSelected() > 0) {
                        pair.waypointType = PlMisAir.this.wLandingType.getSelected() + 100;
                    } else {
                        pair.waypointType = 0;
                    }
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wNormFlyTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, Plugin.i18n("NormflyType"), null));
        gwindowdialogclient.addControl(this.wNormFlyType = new GWindowComboControl(gwindowdialogclient, 9F, 1.0F, 9F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(Plugin.i18n("NormflyTypeNormal"));
                this.add(Plugin.i18n("NormflyTypeCAP"));
                this.add(Plugin.i18n("ArtSpot"));
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    if (PlMisAir.this.wNormFlyType.getSelected() == 1) {
                        pair.waypointType = 401;
                    } else if (PlMisAir.this.wNormFlyType.getSelected() == 2) {
                        pair.waypointType = 407;
                    } else {
                        pair.waypointType = 0;
                    }
                    PlMisAir.this.fillDialogWay2();
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wCAPTypeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("CAPType"), null));
        gwindowdialogclient.addControl(this.wCAPType = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 9F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                this.add(Plugin.i18n("CAPTypeTriangle"));
                this.add(Plugin.i18n("CAPTypeSquare"));
                this.add(Plugin.i18n("CAPTypePentagon"));
                this.add(Plugin.i18n("CAPTypeHexagon"));
                this.add(Plugin.i18n("CAPTypeRandom"));
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    pair.waypointType = PlMisAir.this.wCAPType.getSelected() + 401;
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wCAPCyclesLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("CAPCycles"), null));
        gwindowdialogclient.addControl(this.wCAPCycles = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(PlMisAir.this.wCAPCycles.getValue());
                    if (k < 0) {
                        k = 0;
                    }
                    if (k > 1000) {
                        k = 1000;
                    }
                    pair.cycles = k;
                    this.setValue("" + k);
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wCAPTimerLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, Plugin.i18n("CAPTimer"), null));
        gwindowdialogclient.addLabel(this.wCAPTimerLabelMin = new GWindowLabel(gwindowdialogclient, 15F, 7F, 7F, 1.3F, Plugin.i18n("min"), null));
        gwindowdialogclient.addControl(this.wCAPTimer = new GWindowEditControl(gwindowdialogclient, 9F, 7F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(PlMisAir.this.wCAPTimer.getValue());
                    if (k < 0) {
                        k = 0;
                    }
                    if (k > 10000) {
                        k = 10000;
                    }
                    pair.delayTimer = k;
                    this.setValue("" + k);
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wCAPOrientLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, Plugin.i18n("CAPAngle"), null));
        gwindowdialogclient.addLabel(this.wCAPOrientLabelDeg = new GWindowLabel(gwindowdialogclient, 15F, 9F, 7F, 1.3F, Plugin.i18n("CAPdeg"), null));
        gwindowdialogclient.addControl(this.wCAPOrient = new GWindowEditControl(gwindowdialogclient, 9F, 9F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(PlMisAir.this.wCAPOrient.getValue());
                    if (k < 0) {
                        k = 0;
                    }
                    if (k > 360) {
                        k = 360;
                    }
                    pair.orient = k;
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wCAPBSizeLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, Plugin.i18n("CAPBaseSize"), null));
        gwindowdialogclient.addLabel(this.wCAPBSizeLabelKM = new GWindowLabel(gwindowdialogclient, 15F, 11F, 7F, 1.3F, Plugin.i18n("km"), null));
        gwindowdialogclient.addControl(this.wCAPBSize = new GWindowEditControl(gwindowdialogclient, 9F, 11F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    int k = Integer.parseInt(PlMisAir.this.wCAPBSize.getValue());
                    if (k < 1) {
                        k = 1;
                    }
                    if (k > 10000) {
                        k = 10000;
                    }
                    pair.baseSize = k;
                    this.setValue("" + k);
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wCAPAltDifferenceLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, Plugin.i18n("CAPAltDifference"), null));
        gwindowdialogclient.addLabel(this.wCAPAltDifferenceLabelM = new GWindowLabel(gwindowdialogclient, 15F, 13F, 7F, 1.3F, Plugin.i18n("meters"), null));
        gwindowdialogclient.addControl(this.wCAPAltDifference = new GWindowEditControl(gwindowdialogclient, 9F, 13F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    pair.altDifference = Integer.parseInt(PlMisAir.this.wCAPAltDifference.getValue());
                    PlMission.setChanged();
                }
                return false;
            }

        });
        gwindowdialogclient.addLabel(this.wGAttackNoOptionInfoLabel = new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 19F, 1.3F, Plugin.i18n("GAttackNoOptions"), null));
    }

    private void showHideWay2() {
        PAir pair = (PAir) Plugin.builder.selectedPoint();
        this.wTakeOffType.hideWindow();
        this.wTakeOffTypeLabel.hideWindow();
        this.wTakeoffSpacing.hideWindow();
        this.wTakeoffSpacingLabel.hideWindow();
        this.wTakeoffSpacingLabel2.hideWindow();
        this.wDelayTimer.hideWindow();
        this.wDelayTimerLabel.hideWindow();
        this.wDelayTimerLabel2.hideWindow();
        this.wLandingType.hideWindow();
        this.wLandingTypeLabel.hideWindow();
        this.wNormFlyType.hideWindow();
        this.wNormFlyTypeLabel.hideWindow();
        this.wCAPType.hideWindow();
        this.wCAPTypeLabel.hideWindow();
        this.wCAPCycles.hideWindow();
        this.wCAPCyclesLabel.hideWindow();
        this.wCAPTimer.hideWindow();
        this.wCAPTimerLabel.hideWindow();
        this.wCAPTimerLabelMin.hideWindow();
        this.wCAPOrient.hideWindow();
        this.wCAPOrientLabel.hideWindow();
        this.wCAPOrientLabelDeg.hideWindow();
        this.wCAPBSize.hideWindow();
        this.wCAPBSizeLabel.hideWindow();
        this.wCAPBSizeLabelKM.hideWindow();
        this.wCAPAltDifference.hideWindow();
        this.wCAPAltDifferenceLabel.hideWindow();
        this.wCAPAltDifferenceLabelM.hideWindow();
        this.wGAttackNoOptionInfoLabel.hideWindow();
        switch (pair.type()) {
            default:
                break;

            case 0:
                this.wNormFlyType.showWindow();
                this.wNormFlyTypeLabel.showWindow();
                if (this.wNormFlyType.getSelected() == 1) {
                    this.wCAPType.showWindow();
                    this.wCAPTypeLabel.showWindow();
                    this.wCAPCycles.showWindow();
                    this.wCAPCyclesLabel.showWindow();
                    this.wCAPTimer.showWindow();
                    this.wCAPTimerLabel.showWindow();
                    this.wCAPTimerLabelMin.showWindow();
                    this.wCAPOrient.showWindow();
                    this.wCAPOrientLabel.showWindow();
                    this.wCAPOrientLabelDeg.showWindow();
                    this.wCAPBSize.showWindow();
                    this.wCAPBSizeLabel.showWindow();
                    this.wCAPBSizeLabelKM.showWindow();
                    this.wCAPAltDifference.showWindow();
                    this.wCAPAltDifferenceLabel.showWindow();
                    this.wCAPAltDifferenceLabelM.showWindow();
                }
                if (this.wNormFlyType.getSelected() == 2) {
                    this.wCAPCycles.showWindow();
                    this.wCAPCyclesLabel.showWindow();
                    this.wCAPTimer.showWindow();
                    this.wCAPTimerLabel.showWindow();
                    this.wCAPTimerLabelMin.showWindow();
                    this.wCAPOrient.showWindow();
                    this.wCAPOrientLabel.showWindow();
                    this.wCAPOrientLabelDeg.showWindow();
                    this.wCAPBSize.showWindow();
                    this.wCAPBSizeLabel.showWindow();
                    this.wCAPBSizeLabelKM.showWindow();
                }
                break;

            case 1:
                this.wTakeOffType.showWindow();
                this.wTakeOffTypeLabel.showWindow();
                this.wDelayTimer.showWindow();
                this.wDelayTimerLabel.showWindow();
                this.wDelayTimerLabel2.showWindow();
                if (this.wTakeOffType.getSelected() > 0) {
                    this.wTakeoffSpacing.showWindow();
                    this.wTakeoffSpacingLabel.showWindow();
                    this.wTakeoffSpacingLabel2.showWindow();
                }
                break;

            case 2:
                this.wLandingType.showWindow();
                this.wLandingTypeLabel.showWindow();
                break;

            case 3:
                this.wGAttackNoOptionInfoLabel.showWindow();
                break;
        }
    }

    private void fillEditEnabled(PathAir pathair) {
        this.makeRegimentList(pathair.getArmy(), pathair.sCountry);
        this.fillEnabledRegiments(pathair.iRegiment);
        this.fillEnabledSquads(pathair.regiment(), pathair.iSquadron);
        this.fillEnabledWings(pathair.squadron(), pathair.iWing);
        this.defaultArmy[pathair.getArmy()].save(pathair);
    }

    public void fillEditActor() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        this.fillEditEnabled(pathair);
        this.wRegiment.setSelected(pathair.iRegiment, true, false);
        this.wSquadron.setSelected(pathair.iSquadron, true, false);
        this.wWing.setSelected(pathair.iWing, true, false);
        this.wArmy.setSelected(pathair.getArmy() - 1, true, false);
        this.wFuel.setValue("" + pathair.fuel, false);
        this.wPlanes.setSelected(pathair.planes - 1, true, false);
        if (pathair.skill != -1) {
            this.wSkill.setSelected(pathair.skill, true, false);
        } else {
            this.wSkill.setValue(Plugin.i18n("Custom"));
        }
        this.wWeapons.clear(false);
        this.lWeapons.clear();
        Class class1 = this.type[pathair._iType].item[pathair._iItem].clazz;
        String as[] = Aircraft.getWeaponsRegistered(class1);
        String s = this.type[pathair._iType].item[pathair._iItem].name;
        int i = Aircraft.getWeaponsRegistered(class1).length;
        this.wWeapons.posEnable = new boolean[i];
        if ((as != null) && (as.length > 0)) {
            int j = -1;
            for (int k = 0; k < as.length; k++) {
                String s1 = as[k];
                this.wWeapons.add(I18N.weapons(s, s1));
                this.lWeapons.add(s1);
                if (!Aircraft.isWeaponDateOk(class1, s1)) {
                    this.wWeapons.posEnable[k] = false;
                } else {
                    this.wWeapons.posEnable[k] = true;
                }
                if (s1.equalsIgnoreCase(pathair.weapons)) {
                    j = k;
                    if (!this.wWeapons.posEnable[k]) {
                        j = 0;
                    }
                }
            }

            if (j == -1) {
                j = 0;
            }
            this.wWeapons.setSelected(j, true, false);
        }
    }

    private void fillDialogWay() {
        PAir pair = (PAir) Plugin.builder.selectedPoint();
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        int i = pathair.pointIndx(pair);
        this.wPrev.setEnable(i > 0);
        this.wNext.setEnable(i < (pathair.points() - 1));
        this.wCur.cap = new GCaption("" + i + "(" + pathair.points() + ")");
        this.wHeight.setValue("" + (int) pair.height, false);
        this.wSpeed.setValue("" + (int) pair.speed, false);
        this.wType.setSelected(pair.type(), true, false);
        this.fillDialogWay2();
        for (int j = 0; j < PAir.types.length; j++) {
            this.wType.posEnable[j] = true;
        }

        int k = pair.formation;
        if (k <= 0) {
            k = 0;
        } else {
            k--;
        }
        this.wFormations.setSelected(k, true, false);
        if (i > 0) {
            PAir pair1 = (PAir) pathair.point(i - 1);
            if (pair1.type() != 1) {
                this.wType.posEnable[1] = false;
            }
        }
        if (i < (pathair.points() - 1)) {
            this.wType.posEnable[2] = false;
        }
        this._curPointType = pair.type();
        int l = (int) Math.round((pair.time / 60D) + (World.getTimeofDay() * 60F));
        this.wTimeH.setValue("" + ((l / 60) % 24), false);
        this.wTimeM.setValue("" + (l % 60), false);
        if (pair.getTarget() != null) {
            if (pair.getTarget() instanceof PPoint) {
                if (pair.getTarget() instanceof PAir) {
                    this.wTarget.cap.set(((PathAir) pair.getTarget().getOwner()).typedName);
                } else if (pair.getTarget() instanceof PNodes) {
                    this.wTarget.cap.set(Property.stringValue(pair.getTarget().getOwner(), "i18nName", ""));
                } else {
                    this.wTarget.cap.set(pair.getTarget().getOwner().name());
                }
            } else {
                this.wTarget.cap.set(Property.stringValue(pair.getTarget().getClass(), "i18nName", ""));
            }
        } else {
            this.wTarget.cap.set("");
        }
    }

    private void fillDialogWay2() {
        PAir pair = (PAir) Plugin.builder.selectedPoint();
        int i = pair.waypointType;
        switch (pair.type()) {
            default:
                break;

            case 0:
                if ((i > 400) && (i < 407)) {
                    this.wNormFlyType.setSelected(1, true, false);
                    this.wCAPType.setSelected(i - 401, true, false);
                    break;
                }
                if (i == 0) {
                    this.wNormFlyType.setSelected(i, true, false);
                    break;
                }
                if (i == 407) {
                    this.wNormFlyType.setSelected(2, true, false);
                }
                break;

            case 1:
                this.wTakeOffType.setSelected(i <= 0 ? 0 : i - 1, true, false);
                this.wTakeoffSpacing.setValue("" + pair.takeoffSpacing, false);
                break;

            case 2:
                this.wLandingType.setSelected(i + (i <= 100 ? 0 : -100), true, false);
                break;

            case 3:
                pair.waypointType = 0;
                break;
        }
        this.wDelayTimer.setValue("" + pair.delayTimer, false);
        this.wTakeoffSpacing.setValue("" + pair.takeoffSpacing, false);
        this.wCAPCycles.setValue("" + pair.cycles, false);
        this.wCAPTimer.setValue("" + pair.delayTimer, false);
        this.wCAPOrient.setValue("" + (int) pair.orient, false);
        this.wCAPBSize.setValue("" + pair.baseSize, false);
        this.wCAPAltDifference.setValue("" + pair.altDifference, false);
        this.showHideWay2();
    }

    public void initEditWay() {
        GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient());
        this.tabWay = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("Waypoint"), gwindowdialogclient);
        gwindowdialogclient.addControl(this.wPrev = new GWindowButton(gwindowdialogclient, 1.0F, 1.0F, 5F, 1.6F, Plugin.i18n("&Prev"), null) {

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    int k = pathair.pointIndx(pair);
                    if (k > 0) {
                        Plugin.builder.setSelected(pathair.point(k - 1));
                        PlMisAir.this.fillDialogWay();
                        Plugin.builder.repaint();
                    }
                    PlMisAir.this.fillDialogWay2();
                    return true;
                } else {
                    return false;
                }
            }

        });
        gwindowdialogclient.addControl(this.wNext = new GWindowButton(gwindowdialogclient, 9F, 1.0F, 5F, 1.6F, Plugin.i18n("&Next"), null) {

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    int k = pathair.pointIndx(pair);
                    if (k < (pathair.points() - 1)) {
                        Plugin.builder.setSelected(pathair.point(k + 1));
                        PlMisAir.this.fillDialogWay();
                        Plugin.builder.repaint();
                    }
                    PlMisAir.this.fillDialogWay2();
                    return true;
                } else {
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(this.wCur = new GWindowLabel(gwindowdialogclient, 15F, 1.0F, 4F, 1.6F, "1(1)", null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("Height"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15F, 3F, 4F, 1.3F, Plugin.i18n("[M]"), null));
        gwindowdialogclient.addControl(this.wHeight = new GWindowEditControl(gwindowdialogclient, 9F, 3F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PAir pair = (PAir) Plugin.builder.selectedPoint();
                String s = this.getValue();
                double d = 0.0D;
                try {
                    d = Double.parseDouble(s);
                } catch (Exception exception) {
                    this.setValue("" + ((PAir) Plugin.builder.selectedPoint()).height, false);
                    return false;
                }
                if (d < 0.0D) {
                    d = 0.0D;
                } else if (d > 12000D) {
                    d = 12000D;
                }
                pair.height = d;
                PlMisAir.this.defaultHeight = d;
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("Speed"), null));
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 15F, 5F, 4F, 1.3F, Plugin.i18n("[kM/H]"), null));
        gwindowdialogclient.addControl(this.wSpeed = new GWindowEditControl(gwindowdialogclient, 9F, 5F, 5F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                }
                PAir pair = (PAir) Plugin.builder.selectedPoint();
                String s = this.getValue();
                double d = 0.0D;
                try {
                    d = Double.parseDouble(s);
                } catch (Exception exception) {
                    this.setValue("" + ((PAir) Plugin.builder.selectedPoint()).speed, false);
                    return false;
                }
                PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                if ((pair.type() == 1) || (pair.type() == 2)) {
                    d = 0.0D;
                } else if (d < PlMisAir.this.type[pathair._iType].item[pathair._iItem].speedMin) {
                    d = PlMisAir.this.type[pathair._iType].item[pathair._iItem].speedMin;
                    PlMisAir.this.defaultSpeed = d;
                } else if (d > PlMisAir.this.type[pathair._iType].item[pathair._iItem].speedMax) {
                    d = PlMisAir.this.type[pathair._iType].item[pathair._iItem].speedMax;
                    PlMisAir.this.defaultSpeed = d;
                } else {
                    PlMisAir.this.defaultSpeed = d;
                }
                pair.speed = d;
                pathair.computeTimes();
                PlMission.setChanged();
                return false;
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, Plugin.i18n("Time"), null));
        gwindowdialogclient.addControl(this.wTimeH = new GWindowEditControl(gwindowdialogclient, 9F, 7F, 2.0F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PlMisAir.this.getTimeOut();
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 11.2F, 7F, 1.0F, 1.3F, ":", null));
        gwindowdialogclient.addControl(this.wTimeM = new GWindowEditControl(gwindowdialogclient, 11.5F, 7F, 2.0F, 1.3F, "") {

            public void afterCreated() {
                super.afterCreated();
                this.bNumericOnly = true;
                this.bDelayedNotify = true;
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PlMisAir.this.getTimeOut();
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, Plugin.i18n("lType"), null));
        gwindowdialogclient.addControl(this.wType = new GWindowComboControl(gwindowdialogclient, 9F, 9F, 7F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                for (int i = 0; i < PAir.types.length; i++) {
                    this.add(Plugin.i18n(PAir.types[i]));
                }

                boolean aflag[] = new boolean[PAir.types.length];
                for (int j = 0; j < PAir.types.length; j++) {
                    aflag[j] = true;
                }

                this.posEnable = aflag;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    if (PlMisAir.this._curPointType != this.iSelected) {
                        PAir pair = (PAir) Plugin.builder.selectedPoint();
                        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                        int k = pathair.pointIndx(pair);
                        boolean flag = true;
                        if ((PlMisAir.this._curPointType == 1) || (PlMisAir.this._curPointType == 2)) {
                            flag = false;
                        }
                        boolean flag1 = true;
                        if ((this.iSelected == 1) || (this.iSelected == 2)) {
                            flag1 = false;
                        }
                        if (flag != flag1) {
                            if (flag1) {
                                pair.height = PlMisAir.this.defaultHeight;
                                pair.speed = PlMisAir.this.defaultSpeed;
                                PlMisAir.this.clampSpeed(pair);
                                PlMisAir.this.wHeight.setValue("" + (int) PlMisAir.this.defaultHeight, false);
                                PlMisAir.this.wSpeed.setValue("" + (int) pair.speed, false);
                            } else {
                                PlMisAir.this.wHeight.setValue("0", false);
                                PlMisAir.this.wSpeed.setValue("0", false);
                                pair.height = 0.0D;
                                pair.speed = 0.0D;
                            }
                        }
                        PlMisAir.this._curPointType = this.iSelected;
                        pair.setType(this.iSelected);
                        PlMisAir.this.fillDialogWay2();
                        PlMission.setChanged();
                        if (((this.iSelected == 1) && (k == 0)) || (this.iSelected == 2)) {
                            Airport airport = Airport.nearest(pair.pos.getAbsPoint(), -1, 7);
                            if (airport != null) {
                                if (airport.nearestRunway(pair.pos.getAbsPoint(), PlMisAir.nearestRunway)) {
                                    pair.pos.setAbs(PlMisAir.nearestRunway.getPoint());
                                } else {
                                    pair.pos.setAbs(airport.pos.getAbsPoint());
                                }
                            }
                            pair.setTarget(null);
                            pair.sTarget = null;
                            PlMisAir.this.wTarget.cap.set("");
                        } else {
                            Actor actor = pair.getTarget();
                            if ((actor != null) && (((this.iSelected == 0) && !(actor instanceof PAir)) || ((this.iSelected == 3) && (actor instanceof PAir)))) {
                                pair.setTarget(null);
                                pair.sTarget = null;
                                PlMisAir.this.wTarget.cap.set("");
                            }
                        }
                        pathair = (PathAir) Plugin.builder.selectedPath();
                        pathair.computeTimes();
                    }
                    return true;
                } else {
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 19F, 9F, 7F, 1.3F, Plugin.i18n("Formation"), null));
        gwindowdialogclient.addControl(this.wFormations = new GWindowComboControl(gwindowdialogclient, 25F, 9F, 12F) {

            public void afterCreated() {
                super.afterCreated();
                this.setEditable(false);
                boolean aflag[] = new boolean[PAir.formations.length];
                for (int i = 0; i < PAir.formations.length; i++) {
                    this.add(Plugin.i18n(PAir.formations[i]));
                    aflag[i] = true;
                }

                this.posEnable = aflag;
            }

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    int k = PlMisAir.this.wFormations.getSelected();
                    if (k < 0) {
                        k = 0;
                    }
                    if (k > (PAir.formations.length - 1)) {
                        k = PAir.formations.length - 1;
                    }
                    if (k == 0) {
                        pair.formation = k;
                    } else {
                        pair.formation = k + 1;
                    }
                    PlMission.setChanged();
                    return true;
                } else {
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, Plugin.i18n("RadioSilence"), null));
        gwindowdialogclient.addControl(this.wRadioSilence = new GWindowCheckBox(gwindowdialogclient, 9F, 11F, null) {

            public void preRender() {
                super.preRender();
                PAir pair = (PAir) Plugin.builder.selectedPoint();
                if (pair == null) {
                    return;
                } else {
                    this.setChecked(pair.bRadioSilence, false);
                    return;
                }
            }

            public boolean notify(int i, int j) {
                if (i != 2) {
                    return false;
                } else {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    pair.bRadioSilence = this.isChecked();
                    PlMission.setChanged();
                    return false;
                }
            }

        });
        gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, Plugin.i18n("Target"), null));
        gwindowdialogclient.addLabel(this.wTarget = new GWindowLabel(gwindowdialogclient, 9F, 13F, 7F, 1.3F, "", null));
        gwindowdialogclient.addControl(this.wTargetSet = new GWindowButton(gwindowdialogclient, 1.0F, 15F, 5F, 1.6F, Plugin.i18n("&Set"), null) {

            public boolean notify(int i, int j) {
                if (i == 2) {
                    Plugin.builder.beginSelectTarget();
                }
                return false;
            }

        });
        gwindowdialogclient.addControl(this.wTargetClear = new GWindowButton(gwindowdialogclient, 9F, 15F, 5F, 1.6F, Plugin.i18n("&Clear"), null) {

            public boolean notify(int i, int j) {
                if (i == 2) {
                    PAir pair = (PAir) Plugin.builder.selectedPoint();
                    pair.setTarget(null);
                    pair.sTarget = null;
                    PlMisAir.this.wTarget.cap.set("");
                    PlMission.setChanged();
                }
                return false;
            }

        });
    }

    private void getTimeOut() {
        PAir pair = (PAir) Plugin.builder.selectedPoint();
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        String s = this.wTimeH.getValue();
        double d = 0.0D;
        try {
            d = Double.parseDouble(s);
        } catch (Exception exception) {
        }
        if (d < 0.0D) {
            d = 0.0D;
        }
        if (d > 23D) {
            d = 23D;
        }
        s = this.wTimeM.getValue();
        double d1 = 0.0D;
        try {
            d1 = Double.parseDouble(s);
        } catch (Exception exception1) {
        }
        if (d1 < 0.0D) {
            d1 = 0.0D;
        }
        if (d1 > 59D) {
            d1 = 59D;
        }
        double d2 = (((d * 60D) + d1) * 60D) - Math.round(World.getTimeofDay() * 60F * 60F);
        if (d2 < 0.0D) {
            d2 = 0.0D;
        }
        int i = pathair.pointIndx(pair);
        if (i == 0) {
            if (pathair == Path.player) {
                pair.time = 0.0D;
            } else {
                pair.time = d2;
            }
        } else if (pair.type() != 2) {
            PPoint ppoint = pathair.point(i - 1);
            double d3 = d2 - ppoint.time;
            double d4 = 0.0D;
            if (d3 <= 0.0D) {
                d4 = this.type[pathair._iType].item[pathair._iItem].speedMax;
            } else {
                double d5 = ppoint.pos.getAbsPoint().distance(pair.pos.getAbsPoint());
                d4 = ((d5 / d3) * 3600D) / 1000D;
                if (d4 > this.type[pathair._iType].item[pathair._iItem].speedMax) {
                    d4 = this.type[pathair._iType].item[pathair._iItem].speedMax;
                }
                if (d4 < this.type[pathair._iType].item[pathair._iItem].speedMin) {
                    d4 = this.type[pathair._iType].item[pathair._iItem].speedMin;
                }
            }
            pair.speed = d4;
        }
        pathair.computeTimes();
        PlMission.setChanged();
    }

    private void fillEditSkin(int i) {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        } else {
            this.wSkills[i].setSelected(pathair.skills[i], true, false);
            return;
        }
    }

    private void fillSkins() {
        for (int i = 0; i < 4; i++) {
            this.wSkins[i].clear(false);
            this.wSkins[i].add(Plugin.i18n("Default"));
        }

        try {
            String s = Main.cur().netFileServerSkin.primaryPath();
            int j = Plugin.builder.wSelect.comboBox1.getSelected();
            if ((j < this.startComboBox1) || (j >= (this.startComboBox1 + this.type.length))) {
                return;
            }
            j -= this.startComboBox1;
            int k = Plugin.builder.wSelect.comboBox2.getSelected();
            String s1 = GUIAirArming.validateFileName(this.type[j].item[k].name);
            File file = new File(HomePath.toFileSystemName(s + "/" + s1, 0));
            File afile[] = file.listFiles();
            if (afile != null) {
                for (int l = 0; l < afile.length; l++) {
                    File file1 = afile[l];
                    if (file1.isFile()) {
                        String s2 = file1.getName();
                        String s3 = s2.toLowerCase();
                        if (s3.endsWith(".bmp") && ((s3.length() + s1.length()) <= 122)) {
//                            int i1 = BmpUtils._squareSizeBMP8Pal_(s + "/" + s1 + "/" + s2);
                            int i1 = BmpUtils.squareSizeBMP8Pal(s + "/" + s1 + "/" + s2);
                            if ((i1 == 512) || (i1 == 1024)) {
                                for (int j1 = 0; j1 < 4; j1++) {
                                    this.wSkins[j1].add(s2);
                                }

                            } else {
                                System.out.println("Skin " + s + "/" + s1 + "/" + s2 + " NOT loaded");
                            }
                        }
                    }
                }

            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void syncSkins() {
        if (!(Plugin.builder.selectedPath() instanceof PathAir)) {
            return;
        }
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (!this.syncComboControl(this.wSkins[i], pathair.skins[i])) {
                pathair.skins[i] = null;
            }
        }

    }

    private void fillNoseart() {
        for (int i = 0; i < 4; i++) {
            this.wNoseart[i].clear(false);
            this.wNoseart[i].add(Plugin.i18n("None"));
        }

        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        Class class1 = this.type[pathair._iType].item[pathair._iItem].clazz;
        boolean flag = Property.intValue(class1, "noseart", 0) != 1;
        if (!flag) {
            int j = pathair.iRegiment;
            if ((j < 0) && (j >= this.regimentList.size())) {
                flag = true;
            } else {
                Regiment regiment = (Regiment) this.regimentList.get(j);
                flag = !"us".equals(regiment.country());
            }
        }
        if (flag) {
            for (int k = 0; k < 4; k++) {
                this.wNoseart[k].setEnable(false);
            }

            return;
        }
        for (int l = 0; l < 4; l++) {
            this.wNoseart[l].setEnable(true);
        }

        try {
            String s = Main.cur().netFileServerNoseart.primaryPath();
            File file = new File(HomePath.toFileSystemName(s, 0));
            File afile[] = file.listFiles();
            if (afile != null) {
                for (int i1 = 0; i1 < afile.length; i1++) {
                    File file1 = afile[i1];
                    if (file1.isFile()) {
                        String s1 = file1.getName();
                        String s2 = s1.toLowerCase();
                        if (s2.endsWith(".bmp") && (s2.length() <= 122)) {
                            if (BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 512)) {
                                for (int j1 = 0; j1 < 4; j1++) {
                                    this.wNoseart[j1].add(s1);
                                }

                            } else {
                                System.out.println("Noseart " + s + "/" + s1 + " NOT loaded");
                            }
                        }
                    }
                }

            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void syncNoseart() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (!this.syncComboControl(this.wNoseart[i], pathair.noseart[i])) {
                pathair.noseart[i] = null;
            }
        }

    }

    private void fillPilots() {
        for (int i = 0; i < 4; i++) {
            this.wPilots[i].clear(false);
            this.wPilots[i].add(Plugin.i18n("Default"));
        }

        try {
            String s = Main.cur().netFileServerPilot.primaryPath();
            File file = new File(HomePath.toFileSystemName(s, 0));
            File afile[] = file.listFiles();
            if (afile != null) {
                for (int j = 0; j < afile.length; j++) {
                    File file1 = afile[j];
                    if (file1.isFile()) {
                        String s1 = file1.getName();
                        String s2 = s1.toLowerCase();
                        if (s2.endsWith(".bmp") && (s2.length() <= 122)) {
                            if (BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 256)) {
                                for (int k = 0; k < 4; k++) {
                                    this.wPilots[k].add(s1);
                                }

                            } else {
                                System.out.println("Pilot " + s + "/" + s1 + " NOT loaded");
                            }
                        }
                    }
                }

            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void syncPilots() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (!this.syncComboControl(this.wPilots[i], pathair.pilots[i])) {
                pathair.pilots[i] = null;
            }
        }

    }

    private boolean syncComboControl(GWindowComboControl gwindowcombocontrol, String s) {
        if (s == null) {
            gwindowcombocontrol.setSelected(0, true, false);
            return true;
        }
        int i = gwindowcombocontrol.size();
        for (int j = 1; j < i; j++) {
            String s1 = gwindowcombocontrol.get(j);
            if (s.equals(s1)) {
                gwindowcombocontrol.setSelected(j, true, false);
                return true;
            }
        }

        gwindowcombocontrol.setSelected(0, true, false);
        return false;
    }

    private void checkEditSkinTabs() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        int i = Plugin.builder.wSelect.tabsClient.sizeTabs();
        if (pathair.planes == (i - 4)) {
            return;
        }
        if (pathair.planes > (i - 4)) {
            for (int j = i - 4; j < pathair.planes; j++) {
                Plugin.builder.wSelect.tabsClient.addTab(j + 4, this.tabSkin[j]);
                this.fillEditSkin(j);
            }

        } else {
            int k = Plugin.builder.wSelect.tabsClient.current;
            for (; (Plugin.builder.wSelect.tabsClient.sizeTabs() - 4) > pathair.planes; Plugin.builder.wSelect.tabsClient.removeTab(Plugin.builder.wSelect.tabsClient.sizeTabs() - 1)) {
                ;
            }
            Plugin.builder.wSelect.tabsClient.setCurrent(k, false);
            if ((pathair == Path.player) && (pathair.planes >= Path.playerNum)) {
                Path.playerNum = 0;
            }
        }
    }

    private void checkEditSkinSkills() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        int i = pathair.skills[0];
        this.wSkills[0].setSelected(pathair.skills[0], true, false);
        pathair.skill = -2;
        for (int j = 1; j < pathair.planes; j++) {
            if (pathair.skills[j] != i) {
                pathair.skill = -1;
            }
            this.wSkills[j].setSelected(pathair.skills[j], true, false);
        }

        if (pathair.skill == -1) {
            this.wSkill.setValue(Plugin.i18n("Custom"));
        } else {
            pathair.skill = i;
            this.wSkill.setSelected(pathair.skill, true, false);
        }
    }

    private void resetMesh() {
        for (int i = 0; i < 4; i++) {
            this.resetMesh(i);
        }

    }

    private void resetMesh(int i) {
        if (Actor.isValid(this.actorMesh[i])) {
            this.actorMesh[i].destroy();
            this.actorMesh[i] = null;
        }
    }

    private void checkMesh(int i) {
        if (Actor.isValid(this.actorMesh[i])) {
            return;
        }
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        } else {
            Class class1 = this.type[pathair._iType].item[pathair._iItem].clazz;
            this.meshName = Aircraft.getPropertyMesh(class1, pathair.regiment().country());
            this.createMesh(i);
            PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class1, pathair.regiment().country());
            paintscheme.prepare(class1, this.actorMesh[i].hierMesh(), pathair.regiment(), pathair.iSquadron, pathair.iWing, i, pathair.bNumberOn[i]);
            this.prepareSkin(i, class1, pathair.skins[i]);
            this.prepareNoseart(i, pathair.noseart[i]);
            this.preparePilot(i, pathair.pilots[i]);
            return;
        }
    }

    private void createMesh(int i) {
        if (Actor.isValid(this.actorMesh[i])) {
            return;
        }
        if (this.meshName == null) {
            return;
        } else {
            double d = 20D;
            this.actorMesh[i] = new ActorSimpleHMesh(this.meshName);
            d = this.actorMesh[i].hierMesh().visibilityR();
            this.actorMesh[i].pos.setAbs(new Orient(90F, 0.0F, 0.0F));
            d *= Math.cos(0.26179938779914941D) / Math.sin((this.camera3D[i].FOV() * 3.1415926535897931D) / 180D / 2D);
            this.camera3D[i].pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
            this.camera3D[i].pos.reset();
            return;
        }
    }

    private void prepareSkin(int i, Class class1, String s) {
        if (!Actor.isValid(this.actorMesh[i])) {
            return;
        }
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (s != null) {
            s = Main.cur().netFileServerSkin.primaryPath() + "/" + GUIAirArming.validateFileName(Property.stringValue(class1, "keyName", null)) + "/" + s;
            String s1 = "PaintSchemes/Cache/" + Finger.file(0L, s, -1);
            Aircraft.prepareMeshSkin(this.meshName, this.actorMesh[i].hierMesh(), s, s1, pathair.regiment());
        } else {
            Aircraft.prepareMeshCamouflage(this.meshName, this.actorMesh[i].hierMesh(), class1, pathair.regiment());
        }
    }

    private void prepareNoseart(int i, String s) {
        if (!Actor.isValid(this.actorMesh[i])) {
            return;
        }
        if (s != null) {
            String s1 = Main.cur().netFileServerNoseart.primaryPath() + "/" + s;
            String s2 = s.substring(0, s.length() - 4);
            String s3 = "PaintSchemes/Cache/Noseart0" + s2 + ".tga";
            String s4 = "PaintSchemes/Cache/Noseart0" + s2 + ".mat";
            String s5 = "PaintSchemes/Cache/Noseart1" + s2 + ".tga";
            String s6 = "PaintSchemes/Cache/Noseart1" + s2 + ".mat";
            if (BmpUtils.bmp8PalTo2TGA4(s1, s3, s5)) {
                Aircraft.prepareMeshNoseart(this.actorMesh[i].hierMesh(), s4, s6, s3, s5, null);
            }
        }
    }

    private void preparePilot(int i, String s) {
        if (!Actor.isValid(this.actorMesh[i])) {
            return;
        }
        if (s != null) {
            String s1 = Main.cur().netFileServerPilot.primaryPath() + "/" + s;
            String s2 = s.substring(0, s.length() - 4);
            String s3 = "PaintSchemes/Cache/Pilot" + s2 + ".tga";
            String s4 = "PaintSchemes/Cache/Pilot" + s2 + ".mat";
            if (BmpUtils.bmp8PalToTGA3(s1, s3)) {
                Aircraft.prepareMeshPilot(this.actorMesh[i].hierMesh(), 0, s4, s3, null);
            }
        }
    }

    public void dateChanged() {
        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
        if (pathair == null) {
            return;
        }
        for (int i = 0; i < 4; i++) {
            this.resetMesh(i);
        }

        this.fillEditActor();
        PlMission.setChanged();
    }

    private void initEditSkin() {
        for (this._planeIndx = 0; this._planeIndx < 4; this._planeIndx++) {
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) Plugin.builder.wSelect.tabsClient.create(new GWindowDialogClient() {

                public void resized() {
                    super.resized();
                    PlMisAir.this.wSkills[this.planeIndx].setSize(this.win.dx - PlMisAir.this.wSkills[this.planeIndx].win.x - this.lookAndFeel().metric(1.0F), PlMisAir.this.wSkills[this.planeIndx].win.dy);
                    PlMisAir.this.wSkins[this.planeIndx].setSize(this.win.dx - PlMisAir.this.wSkins[this.planeIndx].win.x - this.lookAndFeel().metric(1.0F), PlMisAir.this.wSkins[this.planeIndx].win.dy);
                    PlMisAir.this.wNoseart[this.planeIndx].setSize(this.win.dx - PlMisAir.this.wNoseart[this.planeIndx].win.x - this.lookAndFeel().metric(1.0F), PlMisAir.this.wNoseart[this.planeIndx].win.dy);
                    PlMisAir.this.wPilots[this.planeIndx].setSize(this.win.dx - PlMisAir.this.wPilots[this.planeIndx].win.x - this.lookAndFeel().metric(1.0F), PlMisAir.this.wPilots[this.planeIndx].win.dy);
                    PlMisAir.this.renders[this.planeIndx].setSize(this.win.dx - PlMisAir.this.renders[this.planeIndx].win.x - this.lookAndFeel().metric(1.0F), this.win.dy - PlMisAir.this.renders[this.planeIndx].win.y - this.lookAndFeel().metric(1.0F));
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            this.tabSkin[this._planeIndx] = Plugin.builder.wSelect.tabsClient.createTab(Plugin.i18n("Plane" + (1 + this._planeIndx)), gwindowdialogclient);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 7F, 1.3F, Plugin.i18n("Player"), null));
            gwindowdialogclient.addControl(this.wPlayer[this._planeIndx] = new GWindowCheckBox(gwindowdialogclient, 9F, 1.0F, null) {

                public void preRender() {
                    super.preRender();
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    if (pathair == null) {
                        return;
                    }
                    this.setChecked((pathair == Path.player) && (this.planeIndx == Path.playerNum), false);
                    this.setEnable(PlMisAir.this.type[pathair._iType].item[pathair._iItem].bEnablePlayer && !pathair.bOnlyAI);
                    if (!this.isEnable() && this.isChecked()) {
                        this.setChecked(false, false);
                        Path.player = null;
                        Path.playerNum = 0;
                        PlMission.setChanged();
                    }
                }

                public boolean notify(int i, int j) {
                    if (i != 2) {
                        return false;
                    }
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    if (pathair == null) {
                        return false;
                    }
                    if (this.isChecked()) {
                        Path.player = pathair;
                        Path.playerNum = this.planeIndx;
                        PAir pair = (PAir) pathair.point(0);
                        pair.time = 0.0D;
                        PlMission.cur.missionArmy = pathair.getArmy();
                        pathair.computeTimes();
                    } else {
                        Path.player = null;
                        Path.playerNum = 0;
                    }
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 7F, 1.3F, Plugin.i18n("Skill"), null));
            gwindowdialogclient.addControl(this.wSkills[this._planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 3F, 7F) {

                public void afterCreated() {
                    super.afterCreated();
                    this.setEditable(false);
                    this.add(Plugin.i18n("Rookie"));
                    this.add(Plugin.i18n("Average"));
                    this.add(Plugin.i18n("Veteran"));
                    this.add(Plugin.i18n("Ace"));
                }

                public boolean notify(int i, int j) {
                    if (i != 2) {
                        return false;
                    } else {
                        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                        pathair.skills[this.planeIndx] = this.getSelected();
                        PlMisAir.this.checkEditSkinSkills();
                        PlMission.setChanged();
                        return false;
                    }
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 7F, 1.3F, Plugin.i18n("Skin"), null));
            gwindowdialogclient.addControl(this.wSkins[this._planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 5F, 7F) {

                public void afterCreated() {
                    super.afterCreated();
                    this.setEditable(false);
                    this.add(Plugin.i18n("Default"));
                }

                public boolean notify(int i, int j) {
                    if (i != 2) {
                        return false;
                    }
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    if (this.getSelected() == 0) {
                        pathair.skins[this.planeIndx] = null;
                    } else {
                        pathair.skins[this.planeIndx] = this.get(this.getSelected());
                    }
                    PlMisAir.this.resetMesh(this.planeIndx);
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 7F, 1.3F, Plugin.i18n("Noseart"), null));
            gwindowdialogclient.addControl(this.wNoseart[this._planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 7F, 7F) {

                public void afterCreated() {
                    super.afterCreated();
                    this.setEditable(false);
                    this.add(Plugin.i18n("None"));
                }

                public boolean notify(int i, int j) {
                    if (i != 2) {
                        return false;
                    }
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    if (this.getSelected() == 0) {
                        pathair.noseart[this.planeIndx] = null;
                    } else {
                        pathair.noseart[this.planeIndx] = this.get(this.getSelected());
                    }
                    PlMisAir.this.resetMesh(this.planeIndx);
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 7F, 1.3F, Plugin.i18n("Pilot"), null));
            gwindowdialogclient.addControl(this.wPilots[this._planeIndx] = new GWindowComboControl(gwindowdialogclient, 9F, 9F, 7F) {

                public void afterCreated() {
                    super.afterCreated();
                    this.setEditable(false);
                    this.add(Plugin.i18n("Default"));
                }

                public boolean notify(int i, int j) {
                    if (i != 2) {
                        return false;
                    }
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    if (this.getSelected() == 0) {
                        pathair.pilots[this.planeIndx] = null;
                    } else {
                        pathair.pilots[this.planeIndx] = this.get(this.getSelected());
                    }
                    PlMisAir.this.resetMesh(this.planeIndx);
                    PlMission.setChanged();
                    return false;
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 7F, 1.3F, Plugin.i18n("NumberOn"), null));
            gwindowdialogclient.addControl(this.wPlayer[this._planeIndx] = new GWindowCheckBox(gwindowdialogclient, 9F, 11F, null) {

                public void preRender() {
                    super.preRender();
                    PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                    if (pathair == null) {
                        return;
                    } else {
                        this.setChecked(pathair.bNumberOn[this.planeIndx], false);
                        return;
                    }
                }

                public boolean notify(int i, int j) {
                    if (i != 2) {
                        return false;
                    } else {
                        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                        pathair.bNumberOn[this.planeIndx] = this.isChecked();
                        PlMisAir.this.resetMesh(this.planeIndx);
                        PlMission.setChanged();
                        return false;
                    }
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            });
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 7F, 1.3F, Plugin.i18n("SpawnPoint"), null));
            gwindowdialogclient.addControl(this.wSpawnPointSet[this._planeIndx] = new GWindowButton(gwindowdialogclient, 9F, 12.5F, 5F, 1.6F, Plugin.i18n("&Set"), null) {

                public boolean notify(int i, int j) {
                    if (i == 2) {
                        Plugin.builder.beginSelectSpawnPoint();
                        PlMisAir.this.bSpawnFromStationary = true;
                    }
                    return false;
                }

            });
            gwindowdialogclient.addControl(this.wSpawnPointClear[this._planeIndx] = new GWindowButton(gwindowdialogclient, 15F, 12.5F, 5F, 1.6F, Plugin.i18n("&Clear"), null) {

                public boolean notify(int i, int j) {
                    if (i == 2) {
                        PathAir pathair = (PathAir) Plugin.builder.selectedPath();
                        int k = PlMisAir.this.getSelectedPlaneIndex();
                        pathair.setSpawnPoint(k, null);
                        PlMisAir.this.bSpawnFromStationary = false;
                        for (int l = 0; l < 4; l++) {
                            if (pathair.getSpawnPoint(l) == null) {
                                continue;
                            }
                            PlMisAir.this.bSpawnFromStationary = true;
                            break;
                        }

                        PlMisAir.this.wSpawnPointLabel[k].cap.set(Plugin.i18n("NotSet"));
                        PlMission.setChanged();
                    }
                    return false;
                }

            });
            gwindowdialogclient.addLabel(this.wSpawnPointLabel[this._planeIndx] = new GWindowLabel(gwindowdialogclient, 21F, 13F, 15F, 1.3F, Plugin.i18n("NotSet"), null));
            this.renders[this._planeIndx] = new GUIRenders(gwindowdialogclient, 1.0F, 15F, 17F, 7F, true) {

                public void mouseButton(int i, boolean flag, float f, float f1) {
                    super.mouseButton(i, flag, f, f1);
                    if (!flag) {
                        return;
                    }
                    if (i == 1) {
                        PlMisAir.this.animateMeshA[this.planeIndx] = PlMisAir.this.animateMeshT[this.planeIndx] = 0.0F;
                        if (Actor.isValid(PlMisAir.this.actorMesh[this.planeIndx])) {
                            PlMisAir.this.actorMesh[this.planeIndx].pos.setAbs(new Orient(90F, 0.0F, 0.0F));
                        }
                    } else if (i == 0) {
                        f -= this.win.dx / 2.0F;
                        if (Math.abs(f) < (this.win.dx / 16F)) {
                            PlMisAir.this.animateMeshA[this.planeIndx] = 0.0F;
                        } else {
                            PlMisAir.this.animateMeshA[this.planeIndx] = (-128F * f) / this.win.dx;
                        }
                        f1 -= this.win.dy / 2.0F;
                        if (Math.abs(f1) < (this.win.dy / 16F)) {
                            PlMisAir.this.animateMeshT[this.planeIndx] = 0.0F;
                        } else {
                            PlMisAir.this.animateMeshT[this.planeIndx] = (-128F * f1) / this.win.dy;
                        }
                    }
                }

                int planeIndx;

                {
                    this.planeIndx = PlMisAir.this._planeIndx;
                }
            };
            this.camera3D[this._planeIndx] = new Camera3D();
            this.camera3D[this._planeIndx].set(50F, 1.0F, 800F);
            this.render3D[this._planeIndx] = new _Render3D(this.renders[this._planeIndx].renders, 1.0F);
            this.render3D[this._planeIndx].setCamera(this.camera3D[this._planeIndx]);
            LightEnvXY lightenvxy = new LightEnvXY();
            this.render3D[this._planeIndx].setLightEnv(lightenvxy);
            lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
            Vector3f vector3f = new Vector3f(-2F, 1.0F, -1F);
            vector3f.normalize();
            lightenvxy.sun().set(vector3f);
        }

    }

    public void freeResources() {
        this.resetMesh();
    }

    public int getSelectedPlaneIndex() {
        for (int i = 0; i < this.tabSkin.length; i++) {
            if ((this.tabSkin[i] != null) && this.tabSkin[i].isCurrent()) {
                return i;
            }
        }

        return -1;
    }

    private void initCountry() {
        if (this.listCountry != null) {
            return;
        }
        this.listCountry = new ArrayList[3];
        this.mapCountry = new HashMap[3];
        for (int i = 0; i < 3; i++) {
            this.listCountry[i] = new ArrayList();
            this.mapCountry[i] = new HashMap();
        }

        ResourceBundle resourcebundle;
        try {
            resourcebundle = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
        } catch (Exception exception) {
            resourcebundle = null;
        }
        HashMap hashmap = new HashMap();
        List list = Regiment.getAll();
        for (int j = 0; j < list.size(); j++) {
            Regiment regiment = (Regiment) list.get(j);
            if (!hashmap.containsKey(regiment.branch())) {
                int k = regiment.getArmy();
                if ((k >= 0) && (k <= 2)) {
                    hashmap.put(regiment.branch(), null);
                    Country country = new Country();
                    country.name = regiment.branch();
                    if (resourcebundle != null) {
                        country.i18nName = resourcebundle.getString(country.name);
                    } else {
                        country.i18nName = country.name;
                    }
                    this.listCountry[k].add(country);
                    this.mapCountry[k].put(country.name, new Integer(this.listCountry[k].size() - 1));
                }
            }
        }

    }

    static Class _mthclass$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    protected Type                                type[];
    double                                        defaultHeight;
    double                                        defaultSpeed;
    private DefaultArmy                           defaultArmy[];
    private int                                   iArmyRegimentList;
    private String                                sCountry;
    private ArrayList                             regimentList;
    private PlMission                             pluginMission;
    private int                                   startComboBox1;
    ViewItem                                      viewType[];
    HashMapInt                                    viewMap;
    private String                                _actorInfo[];
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabActor;
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabWay;
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabSkin[];
    GWindowComboControl                           wArmy;
    GWindowComboControl                           wCountry;
    GWindowComboControl                           wRegiment;
    GWindowComboControl                           wSquadron;
    GWindowComboControl                           wWing;
    GWindowComboControl                           wWeapons;
    ArrayList                                     lWeapons;
    GWindowEditControl                            wFuel;
    GWindowComboControl                           wPlanes;
    GWindowComboControl                           wSkill;
    GWindowCheckBox                               wOnlyAI;
    GWindowCheckBox                               wParachute;
    private Object                                _squads[];
    private Object                                _wings[];
    GWindowButton                                 wPrev;
    GWindowButton                                 wNext;
    GWindowLabel                                  wCur;
    GWindowEditControl                            wHeight;
    GWindowEditControl                            wSpeed;
    GWindowEditControl                            wTimeH;
    GWindowEditControl                            wTimeM;
    GWindowComboControl                           wType;
    GWindowCheckBox                               wRadioSilence;
    GWindowLabel                                  wTarget;
    GWindowButton                                 wTargetSet;
    GWindowButton                                 wTargetClear;
    GWindowComboControl                           wFormations;
    private int                                   _curPointType;
    private static Loc                            nearestRunway  = new Loc();
    GWindowCheckBox                               wPlayer[];
    GWindowComboControl                           wSkills[];
    GWindowComboControl                           wSkins[];
    GWindowComboControl                           wNoseart[];
    GWindowComboControl                           wPilots[];
    GWindowButton                                 wSpawnPointSet[];
    GWindowButton                                 wSpawnPointClear[];
    GWindowLabel                                  wSpawnPointLabel[];
    GUIRenders                                    renders[];
    Camera3D                                      camera3D[];
    _Render3D                                     render3D[];
    String                                        meshName;
    ActorSimpleHMesh                              actorMesh[];
    float                                         animateMeshA[] = { 0.0F, 0.0F, 0.0F, 0.0F };
    float                                         animateMeshT[] = { 0.0F, 0.0F, 0.0F, 0.0F };
    private Orient                                _orient;
    protected boolean                             bSpawnFromStationary;
    private int                                   _planeIndx;
    private ArrayList                             listCountry[];
    private HashMap                               mapCountry[];
    com.maddox.gwindow.GWindowTabDialogClient.Tab tabWay2;
    GWindowComboControl                           wLandingType;
    GWindowLabel                                  wLandingTypeLabel;
    GWindowComboControl                           wTakeOffType;
    GWindowLabel                                  wTakeOffTypeLabel;
    GWindowEditControl                            wDelayTimer;
    GWindowLabel                                  wDelayTimerLabel;
    GWindowLabel                                  wDelayTimerLabel2;
    GWindowEditControl                            wTakeoffSpacing;
    GWindowLabel                                  wTakeoffSpacingLabel;
    GWindowLabel                                  wTakeoffSpacingLabel2;
    GWindowComboControl                           wNormFlyType;
    GWindowLabel                                  wNormFlyTypeLabel;
    GWindowComboControl                           wCAPType;
    GWindowLabel                                  wCAPTypeLabel;
    GWindowEditControl                            wCAPCycles;
    GWindowLabel                                  wCAPCyclesLabel;
    GWindowEditControl                            wCAPTimer;
    GWindowLabel                                  wCAPTimerLabel;
    GWindowLabel                                  wCAPTimerLabelMin;
    GWindowEditControl                            wCAPOrient;
    GWindowLabel                                  wCAPOrientLabel;
    GWindowLabel                                  wCAPOrientLabelDeg;
    GWindowEditControl                            wCAPBSize;
    GWindowLabel                                  wCAPBSizeLabel;
    GWindowLabel                                  wCAPBSizeLabelKM;
    GWindowEditControl                            wCAPAltDifference;
    GWindowLabel                                  wCAPAltDifferenceLabel;
    GWindowLabel                                  wCAPAltDifferenceLabelM;
    GWindowLabel                                  wGAttackNoOptionInfoLabel;

    static {
        Property.set(com.maddox.il2.builder.PlMisAir.class, "name", "MisAir");
    }

}
