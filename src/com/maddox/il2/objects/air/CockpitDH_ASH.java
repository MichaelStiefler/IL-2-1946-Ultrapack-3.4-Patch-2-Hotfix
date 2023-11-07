package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitDH_ASH extends CockpitPilot {

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isPadlock()) {
                hookpilot.stopPadlock();
            }
            hookpilot.reset();
            this.enter();
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.doAim(true);
        if (((DH103) this.aircraft()).scopemode == 1) {
            HUD.logCenter("ASH: Airbourne IFF targets");
            this.mesh.chunkVisible("ModeASV", false);
            this.mesh.chunkVisible("Range2", false);
            this.mesh.chunkVisible("Range3", false);
            this.mesh.chunkVisible("ModeAI", true);
            this.mesh.chunkVisible("Range1", true);
        }
        if (((DH103) this.aircraft()).scopemode == 2) {
            HUD.logCenter("ASH: Surface IFF targets");
            this.mesh.chunkVisible("ModeAI", false);
            this.mesh.chunkVisible("ModeASV", true);
        }
        this.startFrame = Math.random() * 2.99D;
        hookpilot.setSimpleAimOrient(0.0F, -95F, 0.0F);
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 70");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.DelayView = Time.current();
    }

    private void leave() {
        if (this.bEntered) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            HUD.logCenter(" ");
            this.mesh.chunkVisible("ViewSwitch", true);
        }
    }

    public void destroy() {
        super.destroy();
        this.leave();
    }

    public void doToggleAim(boolean flag) {
        if (((DH103) this.aircraft()).scopemode == 1) {
            this.mesh.chunkVisible("ModeAI", false);
            this.mesh.chunkVisible("ModeASV", true);
            ((DH103) this.aircraft()).scopemode = 2;
            HUD.logCenter("ASH: Surface IFF targets");
            return;
        }
        if (((DH103) this.aircraft()).scopemode == 2) {
            this.mesh.chunkVisible("ModeASV", false);
            this.mesh.chunkVisible("Range2", false);
            this.mesh.chunkVisible("Range3", false);
            this.mesh.chunkVisible("ModeAI", true);
            this.mesh.chunkVisible("Range1", true);
            ((DH103) this.aircraft()).scopemode = 1;
            HUD.logCenter("ASH: Airbourne IFF targets");
            return;
        } else {
            return;
        }
    }

    public CockpitDH_ASH() {
        super("3do/Cockpit/DH-ASH/ASH.him", "he111");
        this.bEntered = false;
        this.SetDelay = 800L;
        this.FOV = 1.0D;
        this.ScX = 0.002575D;
        this.ScY = 0.001D;
        this.ScZ = 0.001D;
        this.FOrigX = 0.0F;
        this.FOrigY = 0.0F;
        this.nTgts = 4;
        this.RClose = 1.0F;
        this.radarFoe = new ArrayList();
        this.radarFriend = new ArrayList();
        this.Range = 32000F;
        this.ECMpod = false;
        this.ScanPos = 0.0F;
        this.ScanDir = true;
    }

    public void reflectWorldToInstruments(float f) {
        if (((DH103) this.aircraft()).scopemode == 0) {
            this.destroy();
        }
        if (this.fm.CT.getGear() > 0.99F) {
            this.mesh.chunkVisible("ViewSwitch", true);
            if (((DH103) this.aircraft()).scopemode == 1) {
                HUD.logCenter("ASH: AI On standby");
            }
            if (((DH103) this.aircraft()).scopemode == 2) {
                HUD.logCenter("ASH: ASV On standby");
            }
            this.SetDelay = 0x895440L;
        }
        this.drawScan();
        if (((DH103) this.aircraft()).scopemode == 1) {
            this.drawFoeAI();
            this.drawFriendAI();
            this.mesh.chunkSetAngles("AH", -this.fm.Or.getKren(), 0.0F, 0.0F);
            this.Range = 9260F;
        }
        if (((DH103) this.aircraft()).scopemode == 2) {
            if (((DH103) this.aircraft()).radarrange == 1) {
                this.Range = 9260F;
                this.mesh.chunkVisible("Range3", false);
                this.mesh.chunkVisible("Range2", false);
                this.mesh.chunkVisible("Range1", true);
            }
            if (((DH103) this.aircraft()).radarrange == 2) {
                this.Range = 27780F;
                this.mesh.chunkVisible("Range3", false);
                this.mesh.chunkVisible("Range1", false);
                this.mesh.chunkVisible("Range2", true);
            }
            if (((DH103) this.aircraft()).radarrange == 3) {
                this.Range = 55560F;
                this.mesh.chunkVisible("Range2", false);
                this.mesh.chunkVisible("Range1", false);
                this.mesh.chunkVisible("Range3", true);
            }
            this.drawFoeSV();
            this.drawFriendSV();
            this.mesh.chunkSetAngles("AH", -this.fm.Or.getKren(), 0.0F, 0.0F);
        }
        if (this.fm.CT.getGear() < 0.01F) {
            this.SetDelay = 800L;
        }
        if (this.bEntered && (Time.current() > (this.DelayView + this.SetDelay))) {
            this.mesh.chunkVisible("ViewSwitch", false);
        }
    }

    private void drawScan() {
        if (this.ScanDir && (this.ScanPos <= 85F)) {
            this.ScanPos = this.ScanPos + 0.5F;
        }
        if (this.ScanDir && (this.ScanPos >= 85F)) {
            this.ScanDir = false;
        }
        if (!this.ScanDir && (this.ScanPos >= -85F)) {
            this.ScanPos = this.ScanPos - 0.5F;
        }
        if (!this.ScanDir && (this.ScanPos <= -85F)) {
            this.ScanDir = true;
        }
        this.mesh.chunkSetAngles("ArcSweep", this.ScanPos, 0.0F, 0.0F);
    }

    private void drawFoeAI() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if (l > (this.checkFoe + 750L)) {
                this.radarFoe.clear();
                this.clearFoe();
            }
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft) && (l > (this.checkFoe + 2000L))) {
                this.checkFoe = l;
                Point3d point3d = aircraft.pos.getAbsPoint();
                Orient orient = aircraft.pos.getAbsOrient();
                List list = Engine.targets();
                int i1 = list.size();
                for (int j1 = 0; j1 < i1; j1++) {
                    Actor actor = (Actor) list.get(j1);
                    if ((actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft())) {
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        if ((point3d1.x > this.RClose) && (point3d1.x < this.Range)) {
                            this.radarFoe.add(point3d1);
                            this.mesh.chunkVisible("RedIFF", true);
                        } else {
                            this.mesh.chunkVisible("RedIFF", false);
                        }
                    }
                }

                if (this.radarFoe.size() == 0) {
                    this.mesh.chunkVisible("RedIFF", false);
                }
            }
            int i = this.radarFoe.size();
            int j = 0;
            for (int k = 0; k < i; k++) {
                double d1 = ((Point3d) this.radarFoe.get(k)).x;
                if ((d1 > this.RClose) && (j <= this.nTgts)) {
                    this.FOV = 26D / d1;
                    double d2 = -((Point3d) this.radarFoe.get(k)).y * this.FOV;
                    double d3 = ((Point3d) this.radarFoe.get(k)).x;
                    double d4 = ((Point3d) this.radarFoe.get(k)).z * this.FOV;
                    float f = this.FOrigX + (float) (d2 * this.ScX);
                    float f1 = -0.1372F + (float) (0.1885D * (d3 / this.Range));
                    float f2 = (float) (d4 * 0.0003D);
                    String s = "  ";
                    if (f2 > 0.0015D) {
                        s = "Plus";
                    }
                    if (f2 < -0.0015D) {
                        s = "Minus";
                    }
                    if ((f2 < 0.0015D) && (f2 > -0.0015D)) {
                        s = "Equal";
                    }
                    j++;
                    String s1 = "Foe" + s + j;
                    this.mesh.setCurChunk(s1);
                    this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                    this.resetYPRmodifier();
                    if (f <= -0.074F) {
                        f = -0.074F;
                    }
                    if (f >= 0.074F) {
                        f = 0.074F;
                    }
                    if (f1 >= 0.0517F) {
                        f1 = 0.0517F;
                    }
                    Cockpit.xyz[0] = f1;
                    Cockpit.xyz[1] = -f;
                    this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                    this.mesh.render();
                    if (!this.mesh.isChunkVisible(s1)) {
                        this.mesh.chunkVisible(s1, true);
                    }
                    String s2 = "FoeAngels" + ((int) this.startFrame + 1);
                    String s3 = "FoeAngels" + ((int) this.startFrame + 2);
                    String s4 = "FoeAngels" + ((int) this.startFrame + 3);
                    if ((this.fm.getAltitude() < 4000F) && (this.fm.getAltitude() >= 1500F) && (this.fm.Or.getTangage() < (2D - (this.fm.getAltitude() * 0.002D)))) {
                        this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s2, true);
                    }
                    if ((this.fm.getAltitude() < 1500F) && (this.fm.getAltitude() >= 1000F) && (this.fm.Or.getTangage() < 0.0F)) {
                        this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s2, true);
                        this.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s3, true);
                    }
                    if ((this.fm.getAltitude() < 1000F) && (this.fm.Or.getTangage() < 2.0F)) {
                        this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s2, true);
                        this.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s3, true);
                        this.mesh.chunkSetLocate(s4, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s4, true);
                    }
                    if (this.ECMpod) {
                        this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s2, true);
                        this.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s3, true);
                        this.mesh.chunkSetLocate(s4, Cockpit.xyz, Cockpit.ypr);
                        this.mesh.chunkVisible(s4, true);
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void clearFoe() {
        this.mesh.chunkVisible("FoeEqual1", false);
        this.mesh.chunkVisible("FoeEqual2", false);
        this.mesh.chunkVisible("FoeEqual3", false);
        this.mesh.chunkVisible("FoeEqual4", false);
        this.mesh.chunkVisible("FoeMinus1", false);
        this.mesh.chunkVisible("FoeMinus2", false);
        this.mesh.chunkVisible("FoeMinus3", false);
        this.mesh.chunkVisible("FoeMinus4", false);
        this.mesh.chunkVisible("FoePlus1", false);
        this.mesh.chunkVisible("FoePlus2", false);
        this.mesh.chunkVisible("FoePlus3", false);
        this.mesh.chunkVisible("FoePlus4", false);
        this.mesh.chunkVisible("FoeAngels1", false);
        this.mesh.chunkVisible("FoeAngels2", false);
        this.mesh.chunkVisible("FoeAngels3", false);
        this.mesh.chunkVisible("FoeAngels4", false);
        this.mesh.chunkVisible("FoeAngels5", false);
    }

    private void drawFriendAI() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if (l > (this.checkFriend + 1000L)) {
                this.radarFriend.clear();
                this.mesh.chunkVisible("FndAngels1", false);
                this.mesh.chunkVisible("FndAngels2", false);
                this.mesh.chunkVisible("FndAngels3", false);
                this.mesh.chunkVisible("FndAngels4", false);
                this.mesh.chunkVisible("FndAngels5", false);
            }
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft)) {
                if (Actor.isValid(aircraft) && Actor.isAlive(aircraft) && (l > (this.checkFriend + 2000L))) {
                    this.checkFriend = l;
                    Point3d point3d = aircraft.pos.getAbsPoint();
                    Orient orient = aircraft.pos.getAbsOrient();
                    this.radarFriend.clear();
                    List list = Engine.targets();
                    int j1 = list.size();
                    for (int k1 = 0; k1 < j1; k1++) {
                        Actor actor = (Actor) list.get(k1);
                        if ((actor instanceof Aircraft) && (actor.getArmy() == World.getPlayerArmy()) && (actor != World.getPlayerAircraft())) {
                            Point3d point3d1 = new Point3d();
                            point3d1.set(actor.pos.getAbsPoint());
                            point3d1.sub(point3d);
                            orient.transformInv(point3d1);
                            if ((point3d1.x > this.RClose) && (point3d1.x < this.Range)) {
                                this.radarFriend.add(point3d1);
                                this.mesh.chunkVisible("GreenIFF", true);
                            } else {
                                this.mesh.chunkVisible("GreenIFF", false);
                            }
                        }
                    }

                    if (this.radarFriend.size() == 0) {
                        this.mesh.chunkVisible("GreenIFF", false);
                    }
                }
                int i = this.radarFriend.size();
                int j = 0;
                for (int k = 0; k < i; k++) {
                    double d1 = ((Point3d) this.radarFriend.get(k)).x;
                    if ((d1 > this.RClose) && (j <= this.nTgts)) {
                        this.FOV = 26D / d1;
                        double d2 = -((Point3d) this.radarFriend.get(k)).y * this.FOV;
                        double d3 = ((Point3d) this.radarFriend.get(k)).x;
                        float f = this.FOrigX + (float) (d2 * this.ScX);
                        float f1 = -0.1372F + (float) (0.1885D * (d3 / this.Range));
                        j++;
                        String s1 = "Friend" + j;
                        this.mesh.setCurChunk(s1);
                        this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        this.resetYPRmodifier();
                        if (f <= -0.074F) {
                            f = -0.074F;
                        }
                        if (f >= 0.074F) {
                            f = 0.074F;
                        }
                        if (f1 >= 0.0517F) {
                            f1 = 0.0517F;
                        }
                        Cockpit.xyz[0] = f1;
                        Cockpit.xyz[1] = -f;
                        this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        this.mesh.render();
                        if (!this.mesh.isChunkVisible(s1)) {
                            this.mesh.chunkVisible(s1, true);
                        }
                        String s2 = "FndAngels" + ((int) this.startFrame + 1);
                        String s3 = "FndAngels" + ((int) this.startFrame + 2);
                        String s4 = "FndAngels" + ((int) this.startFrame + 3);
                        if ((this.fm.getAltitude() < 4000F) && (this.fm.getAltitude() >= 1500F) && (this.fm.Or.getTangage() < (2D - (this.fm.getAltitude() * 0.002D)))) {
                            this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                            this.mesh.chunkVisible(s2, true);
                        }
                        if ((this.fm.getAltitude() < 1500F) && (this.fm.getAltitude() >= 1000F) && (this.fm.Or.getTangage() < 0.0F)) {
                            this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                            this.mesh.chunkVisible(s2, true);
                            this.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
                            this.mesh.chunkVisible(s3, true);
                        }
                        if ((this.fm.getAltitude() < 1000F) && (this.fm.Or.getTangage() < 2.0F)) {
                            this.mesh.chunkSetLocate(s2, Cockpit.xyz, Cockpit.ypr);
                            this.mesh.chunkVisible(s2, true);
                            this.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
                            this.mesh.chunkVisible(s3, true);
                            this.mesh.chunkSetLocate(s4, Cockpit.xyz, Cockpit.ypr);
                            this.mesh.chunkVisible(s4, true);
                        }
                    }
                }

                for (int i1 = j + 1; i1 <= this.nTgts; i1++) {
                    String s = "Friend" + i1;
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void drawFoeSV() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if (l > (this.checkFoe + 750L)) {
                this.radarFoe.clear();
                this.clearFoe();
            }
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft) && (l > (this.checkFoe + 2000L))) {
                this.checkFoe = l;
                Point3d point3d = aircraft.pos.getAbsPoint();
                Orient orient = aircraft.pos.getAbsOrient();
                List list = Engine.targets();
                int i1 = list.size();
                for (int j1 = 0; j1 < i1; j1++) {
                    Actor actor = (Actor) list.get(j1);
                    if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != World.getPlayerArmy()) && actor.isAlive() && (actor != World.getPlayerAircraft())) {
                        Point3d point3d1 = new Point3d();
                        point3d1.set(actor.pos.getAbsPoint());
                        point3d1.sub(point3d);
                        orient.transformInv(point3d1);
                        if ((point3d1.x > this.RClose) && (point3d1.x < this.Range)) {
                            this.radarFoe.add(point3d1);
                            this.mesh.chunkVisible("RedIFF", true);
                        } else {
                            this.mesh.chunkVisible("RedIFF", false);
                        }
                    }
                }

                if (this.radarFoe.size() == 0) {
                    this.mesh.chunkVisible("RedIFF", false);
                }
            }
            int i = this.radarFoe.size();
            int j = 0;
            for (int k = 0; k < i; k++) {
                double d1 = ((Point3d) this.radarFoe.get(k)).x;
                if ((d1 > this.RClose) && (j <= this.nTgts)) {
                    this.FOV = 26D / d1;
                    double d2 = -((Point3d) this.radarFoe.get(k)).y * this.FOV;
                    double d3 = ((Point3d) this.radarFoe.get(k)).x;
                    float f = this.FOrigX + (float) (d2 * this.ScX);
                    float f1 = -0.1372F + (float) (0.1885D * (d3 / this.Range));
                    j++;
                    String s = "FoeEqual" + j;
                    this.mesh.setCurChunk(s);
                    this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                    this.resetYPRmodifier();
                    if (f <= -0.074F) {
                        f = -0.074F;
                    }
                    if (f >= 0.074F) {
                        f = 0.074F;
                    }
                    if (f1 >= 0.0517F) {
                        f1 = 0.0517F;
                    }
                    Cockpit.xyz[0] = f1;
                    Cockpit.xyz[1] = -f;
                    this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                    this.mesh.render();
                    if (!this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, true);
                        this.mesh.chunkVisible("RedIFF", true);
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void drawFriendSV() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            long l = Time.current() + World.Rnd().nextLong(-250L, 250L);
            if (l > (this.checkFriend + 1000L)) {
                this.radarFriend.clear();
            }
            if (Actor.isValid(aircraft) && Actor.isAlive(aircraft)) {
                if (Actor.isValid(aircraft) && Actor.isAlive(aircraft) && (l > (this.checkFriend + 2000L))) {
                    this.checkFriend = l;
                    Point3d point3d = aircraft.pos.getAbsPoint();
                    Orient orient = aircraft.pos.getAbsOrient();
                    this.radarFriend.clear();
                    List list = Engine.targets();
                    int j1 = list.size();
                    for (int k1 = 0; k1 < j1; k1++) {
                        Actor actor = (Actor) list.get(k1);
                        if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() == World.getPlayerArmy()) && actor.isAlive() && (actor != World.getPlayerAircraft())) {
                            Point3d point3d1 = new Point3d();
                            point3d1.set(actor.pos.getAbsPoint());
                            point3d1.sub(point3d);
                            orient.transformInv(point3d1);
                            if ((point3d1.x > this.RClose) && (point3d1.x < this.Range)) {
                                this.radarFriend.add(point3d1);
                                this.mesh.chunkVisible("GreenIFF", true);
                            } else {
                                this.mesh.chunkVisible("GreenIFF", false);
                            }
                        }
                    }

                    if (this.radarFriend.size() == 0) {
                        this.mesh.chunkVisible("GreenIFF", false);
                    }
                }
                int i = this.radarFriend.size();
                int j = 0;
                for (int k = 0; k < i; k++) {
                    double d1 = ((Point3d) this.radarFriend.get(k)).x;
                    if ((d1 > this.RClose) && (j <= this.nTgts)) {
                        this.FOV = 26D / d1;
                        double d2 = -((Point3d) this.radarFriend.get(k)).y * this.FOV;
                        double d3 = ((Point3d) this.radarFriend.get(k)).x;
                        float f = this.FOrigX + (float) (d2 * this.ScX);
                        float f1 = -0.1372F + (float) (0.1885D * (d3 / this.Range));
                        j++;
                        String s1 = "Friend" + j;
                        this.mesh.setCurChunk(s1);
                        this.mesh.setScaleXYZ(0.5F, 0.5F, 0.5F);
                        this.resetYPRmodifier();
                        if (f <= -0.074F) {
                            f = -0.074F;
                        }
                        if (f >= 0.074F) {
                            f = 0.074F;
                        }
                        if (f1 >= 0.0517F) {
                            f1 = 0.0517F;
                        }
                        Cockpit.xyz[0] = f1;
                        Cockpit.xyz[1] = -f;
                        this.mesh.chunkSetLocate(Cockpit.xyz, Cockpit.ypr);
                        this.mesh.render();
                        if (!this.mesh.isChunkVisible(s1)) {
                            this.mesh.chunkVisible(s1, true);
                            this.mesh.chunkVisible("GreenIFF", true);
                        }
                    }
                }

                for (int i1 = j + 1; i1 <= this.nTgts; i1++) {
                    String s = "Friend" + i1;
                    if (this.mesh.isChunkVisible(s)) {
                        this.mesh.chunkVisible(s, false);
                    }
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private float     saveFov;
    private boolean   bEntered;
    private long      SetDelay;
    private long      DelayView;
    double            FOV;
    double            ScX;
    double            ScY;
    double            ScZ;
    float             FOrigX;
    float             FOrigY;
    int               nTgts;
    float             RClose;
    float             Range;
    float             ScanPos;
    private double    startFrame;
    private ArrayList radarFoe;
    private long      checkFoe;
    private ArrayList radarFriend;
    private long      checkFriend;
    private boolean   ECMpod;
    private boolean   ScanDir;

    static {
        Property.set(CockpitDH_ASH.class, "astatePilotIndx", 0);
    }
}
