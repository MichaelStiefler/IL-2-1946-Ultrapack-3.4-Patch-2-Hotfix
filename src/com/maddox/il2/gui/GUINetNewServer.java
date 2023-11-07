package com.maddox.il2.gui;

import java.io.File;
import java.util.List;

import com.maddox.gwindow.GCaption;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowButton;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowDialogClient;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowFramed;
import com.maddox.gwindow.GWindowLabel;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.Chat;
import com.maddox.il2.net.NetLocalControl;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.NetUSGSControl;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.USGS;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HomePath;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetSocket;

public class GUINetNewServer extends GameState {
    public class DlgPassword extends GWindowFramed {

        public boolean doOk() {
            String s = this.pw0.getValue();
            String s1 = this.pw1.getValue();
            if (s.equals(s1)) {
                if ("".equals(s)) {
                    GUINetNewServer.this.password = null;
                } else {
                    GUINetNewServer.this.password = s;
                }
                return true;
            } else {
                new GWindowMessageBox(this.root, 22F, true, GUINetNewServer.this.i18n("netns.Pwd"), GUINetNewServer.this.i18n("netns.PwdIncorrect"), 3, 0.0F);
                return false;
            }
        }

        public void doCancel() {
        }

        public void afterCreated() {
            this.clientWindow = this.create(new GWindowDialogClient() {

                public boolean notify(GWindow gwindow, int i, int j) {
                    if (i != 2) {
                        return super.notify(gwindow, i, j);
                    }
                    if (gwindow == DlgPassword.this.bOk) {
                        if (DlgPassword.this.doOk()) {
                            this.close(false);
                        }
                        return true;
                    }
                    if (gwindow == DlgPassword.this.bCancel) {
                        DlgPassword.this.doCancel();
                        this.close(false);
                        return true;
                    } else {
                        return super.notify(gwindow, i, j);
                    }
                }

            });
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient) this.clientWindow;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 10F, 1.5F, GUINetNewServer.this.i18n("netns.Password_") + " ", null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 10F, 1.5F, GUINetNewServer.this.i18n("netns.ConfirmPassword") + " ", null));
            gwindowdialogclient.addControl(this.pw0 = new GWindowEditControl(gwindowdialogclient, 12F, 1.0F, 8F, 1.5F, null));
            gwindowdialogclient.addControl(this.pw1 = new GWindowEditControl(gwindowdialogclient, 12F, 3F, 8F, 1.5F, null));
            this.pw0.bPassword = this.pw1.bPassword = true;
            gwindowdialogclient.addDefault(this.bOk = new GWindowButton(gwindowdialogclient, 4F, 6F, 6F, 2.0F, GUINetNewServer.this.i18n("netns.Ok"), null));
            gwindowdialogclient.addEscape(this.bCancel = new GWindowButton(gwindowdialogclient, 12F, 6F, 6F, 2.0F, GUINetNewServer.this.i18n("netns.Cancel"), null));
            if (GUINetNewServer.this.password != null) {
                this.pw0.setValue(GUINetNewServer.this.password, false);
                this.pw1.setValue(GUINetNewServer.this.password, false);
            }
            super.afterCreated();
            this.resized();
            this.showModal();
        }

        GWindowEditControl pw0;
        GWindowEditControl pw1;
        GWindowButton      bOk;
        GWindowButton      bCancel;

        public DlgPassword(GWindow gwindow) {
            this.bSizable = false;
            this.title = GUINetNewServer.this.i18n("netns.EnterPassword");
            float f = 22F;
            float f1 = 12F;
            float f2 = gwindow.win.dx / gwindow.lookAndFeel().metric();
            float f3 = gwindow.win.dy / gwindow.lookAndFeel().metric();
            float f4 = (f2 - f) / 2.0F;
            float f5 = (f3 - f1) / 2.0F;
            this.doNew(gwindow, f4, f5, f, f1, true);
        }
    }

    public class DialogClient extends GUIDialogClient {

        public boolean notify(GWindow gwindow, int i, int j) {
            if (i != 2) {
                return super.notify(gwindow, i, j);
            }
            if (gwindow == GUINetNewServer.this.bStart) {
                GUINetNewServer.this.doNewLocal();
                return true;
            }
            if (gwindow == GUINetNewServer.this.cGameType) {
                GUINetNewServer.this.fillPlayers();
                return true;
            }
            if (gwindow == GUINetNewServer.this.bPassword) {
                new DlgPassword(this.root);
                return true;
            }
            if (gwindow == GUINetNewServer.this.bExit) {
                GUINetNewServer.this.doExit();
                return true;
            } else {
                return super.notify(gwindow, i, j);
            }
        }

        public void render() {
            super.render();
            GUISeparate.draw(this, GColor.Gray, this.x1024(32F), this.y1024(88F), this.x1024(800F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, this.x1024(32F), this.y1024(248F), this.x1024(800F), 2.0F);
            this.setCanvasColor(GColor.Gray);
            this.setCanvasFont(0);
            this.draw(this.x1024(32F), this.y1024(32F), this.x1024(160F), this.y1024(32F), 2, GUINetNewServer.this.i18n("netns.Name"));
            this.draw(this.x1024(576F), this.y1024(32F), this.x1024(256F), this.y1024(32F), 0, GUINetNewServer.this.i18n("netns.Password"));
            this.draw(this.x1024(144F), this.y1024(120F), this.x1024(400F), this.y1024(32F), 2, GUINetNewServer.this.i18n("netns.GameType"));
            this.draw(this.x1024(144F), this.y1024(184F), this.x1024(400F), this.y1024(32F), 2, GUINetNewServer.this.i18n("netns.Max.Players"));
            if (Main.cur().netGameSpy != null) {
                this.draw(this.x1024(96F), this.y1024(280F), this.x1024(256F), this.y1024(48F), 0, GUINetNewServer.this.i18n("main.Quit"));
            } else {
                this.draw(this.x1024(96F), this.y1024(280F), this.x1024(256F), this.y1024(48F), 0, GUINetNewServer.this.i18n("netns.MainMenu"));
            }
            this.draw(this.x1024(464F), this.y1024(280F), this.x1024(304F), this.y1024(48F), 2, GUINetNewServer.this.i18n("netns.Create"));
        }

        public void setPosSize() {
            this.set1024PosSize(80F, 216F, 864F, 360F);
            GUINetNewServer.this.wName.setPosSize(this.x1024(208F), this.y1024(32F), this.x1024(288F), this.y1024(32F));
            GUINetNewServer.this.bPassword.setPosC(this.x1024(536F), this.y1024(48F));
            GUINetNewServer.this.cGameType.setPosSize(this.x1024(560F), this.y1024(120F), this.x1024(272F), this.y1024(32F));
            GUINetNewServer.this.cPlayers.setPosSize(this.x1024(560F), this.y1024(184F), this.x1024(272F), this.y1024(32F));
            GUINetNewServer.this.bExit.setPosC(this.x1024(56F), this.y1024(304F));
            GUINetNewServer.this.bStart.setPosC(this.x1024(808F), this.y1024(304F));
        }

        public DialogClient() {
        }
    }

    public void _enter() {
        if (USGS.isUsed()) {
            ((NetUser) NetEnv.host()).reset();
            CmdEnv.top().exec("socket LISTENER 1");
            int i = 31;
            NetEnv.cur();
            NetEnv.host().setShortName(USGS.name);
            i = USGS.maxclients - 1;
            String s = "socket udp CREATE LOCALPORT " + Config.cur.netLocalPort + " SPEED " + Config.cur.netSpeed + " CHANNELS " + i;
            if ((Config.cur.netLocalHost != null) && (Config.cur.netLocalHost.length() > 0)) {
                s = s + " LOCALHOST " + Config.cur.netLocalHost;
            }
            CmdEnv.top().exec(s);
            new NetServerParams();
            Main.cur().netServerParams.setType(48);
            Main.cur().netServerParams.setServerName(USGS.room);
            if ("".equals(Main.cur().netServerParams.serverName())) {
                Main.cur().netServerParams.setServerName(NetEnv.host().shortName());
            }
            Main.cur().netServerParams.serverDescription = Config.cur.netServerDescription;
            Main.cur().netServerParams.setPassword(null);
            new NetUSGSControl();
            int k = 0;
            if (!USGS.bGameDfight) {
                k = 1;
            }
            Main.cur().netServerParams.setMode(k);
            Main.cur().netServerParams.setDifficulty(World.cur().diffUser.get());
            Main.cur().netServerParams.setMaxUsers(i + 1);
            new Chat();
            USGS.serverReady(Config.cur.netLocalPort);
            Main.cur().netServerParams.bNGEN = false;
            Main.stateStack().change(38);
            GUI.chatDlg.showWindow();
            return;
        }
        if (Main.cur().netGameSpy != null) {
            Config.cur.netServerChannels = Main.cur().netGameSpy.maxClients - 1;
            if ("coop".equals(Main.cur().netGameSpy.gameType)) {
                this.cGameType.setSelected(1, true, false);
            } else {
                this.cGameType.setSelected(0, true, false);
            }
        }
        if (Config.cur.netServerChannels < 1) {
            Config.cur.netServerChannels = 1;
        }
        if (Config.cur.netServerChannels > 31) {
            Config.cur.netServerChannels = 31;
        }
        this.fillPlayers();
        int j = Config.cur.netServerChannels - 1;
        this.cPlayers.setSelected(j, true, false);
        if (Main.cur().netGameSpy != null) {
            this.wName.setValue(Main.cur().netGameSpy.roomName, false);
            this.wName.setEditable(false);
        } else if ("".equals(this.wName.getValue())) {
            if ("".equals(Config.cur.netServerName)) {
                this.wName.setValue(NetEnv.host().shortName(), false);
            } else {
                this.wName.setValue(Config.cur.netServerName, false);
            }
        }
        ((NetUser) NetEnv.host()).reset();
        this.client.activateWindow();
    }

    public void _leave() {
        if (USGS.isUsing()) {
            return;
        }
        if (Main.cur().netGameSpy == null) {
            Config.cur.netServerName = this.wName.getValue();
        }
        this.client.hideWindow();
    }

    private void fillPlayers() {
        int i = this.cPlayers.getSelected();
        if (i < 0) {
            i = 0;
        }
        this.cPlayers.clear(false);
        for (int j = 2; j <= 127; j++) {
            this.cPlayers.add("" + j);
        }

        this.cPlayers.setSelected(i, true, false);
    }

    private void doNewServer(int i) {
        Config.cur.netServerChannels = this.cPlayers.getSelected() + 1;
        CmdEnv.top().exec("socket LISTENER 1");
        NetEnv.cur();
        NetEnv.host().setShortName(World.cur().userCfg.callsign);
        String s = "socket udp CREATE LOCALPORT " + Config.cur.netLocalPort + " SPEED " + Config.cur.netSpeed + " CHANNELS " + Config.cur.netServerChannels;
        if ((Config.cur.netLocalHost != null) && (Config.cur.netLocalHost.length() > 0)) {
            s = s + " LOCALHOST " + Config.cur.netLocalHost;
        }
        CmdEnv.top().exec(s);
        new NetServerParams();
        if (Main.cur().netGameSpy != null) {
            List list = NetEnv.socketsBlock();
            if ((list != null) && (list.size() > 0)) {
                Main.cur().netGameSpy.set(Main.cur().netGameSpy.roomName, (NetSocket) list.get(0), Config.cur.netLocalPort);
            }
            Main.cur().netServerParams.setType(32);
            Main.cur().netServerParams.setServerName(Main.cur().netGameSpy.roomName);
            NetEnv.cur();
            NetEnv.host().setShortName(Main.cur().netGameSpy.userName);
        } else {
            Main.cur().netServerParams.setType(i);
            Main.cur().netServerParams.setServerName(this.wName.getValue());
            if ("".equals(Main.cur().netServerParams.serverName())) {
                Main.cur().netServerParams.setServerName(NetEnv.host().shortName());
            }
        }
        Main.cur().netServerParams.setPassword(this.password);
        new NetLocalControl();
        int j = 0;
        if ((this.cGameType.getSelected() == 1) || (this.cGameType.getSelected() == 2)) {
            j = 1;
        }
        Main.cur().netServerParams.setMode(j);
        Main.cur().netServerParams.setDifficulty(World.cur().diffUser.get());
        Main.cur().netServerParams.setMaxUsers(Config.cur.netServerChannels + 1);
        new Chat();
        if (this.cGameType.getSelected() == 2) {
            Main.cur().netServerParams.bNGEN = true;
            Main.stateStack().change(68);
        } else {
            Main.cur().netServerParams.bNGEN = false;
            Main.stateStack().change(38);
        }
        GUI.chatDlg.showWindow();
    }

    private void doNewLocal() {
        this.doNewServer(0);
    }

    private void doExit() {
        GUIMainMenu guimainmenu = (GUIMainMenu) GameState.get(2);
        guimainmenu.pPilotName.cap = new GCaption(World.cur().userCfg.name + " '" + World.cur().userCfg.callsign + "' " + World.cur().userCfg.surname);
        GUIInfoName.nickName = null;
        ((NetUser) NetEnv.host()).reset();
        Main.stateStack().pop();
    }

//    private Object THIS()
//    {
//        return this;
//    }

    private boolean isExistNGEN() {
        File f = new File(HomePath.get(0), "ngen");
        if (!f.isDirectory()) {
            return false;
        }
        try {
            f = new File(HomePath.get(0), "ngen.exe");
            return f.exists();
        } catch (Exception exception) {
            return false;
        }
    }

    public GUINetNewServer(GWindowRoot gwindowroot) {
        super(35);
        this.client = (GUIClient) gwindowroot.create(new GUIClient());
        this.dialogClient = (DialogClient) this.client.create(new DialogClient());
        this.infoMenu = (GUIInfoMenu) this.client.create(new GUIInfoMenu());
        this.infoMenu.info = this.i18n("netns.info");
        this.infoName = (GUIInfoName) this.client.create(new GUIInfoName());
        this.wName = (GWindowEditControl) this.dialogClient.addControl(new GWindowEditControl(this.dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        this.wName.maxLength = 64;
        this.cGameType = (GWindowComboControl) this.dialogClient.addControl(new GWindowComboControl(this.dialogClient, 0.0F, 0.0F, 1.0F));
        this.cGameType.setEditable(false);
        this.cGameType.add(this.i18n("netns.Dogfight"));
        this.cGameType.add(this.i18n("netns.Cooperative"));
        if (this.isExistNGEN()) {
            this.cGameType.add(this.i18n("netns.NGEN"));
        }
        this.cGameType.setSelected(0, true, false);
        this.cPlayers = (GWindowComboControl) this.dialogClient.addControl(new GWindowComboControl(this.dialogClient, 0.0F, 0.0F, 1.0F));
        this.cPlayers.setEditable(false);
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
        this.bPassword = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bStart = (GUIButton) this.dialogClient.addControl(new GUIButton(this.dialogClient, gtexture, 0.0F, 48F, 48F, 48F));
        this.bExit = (GUIButton) this.dialogClient.addEscape(new GUIButton(this.dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        this.dialogClient.activateWindow();
        this.client.hideWindow();
    }

    public GUIClient           client;
    public DialogClient        dialogClient;
    public GUIInfoMenu         infoMenu;
    public GUIInfoName         infoName;
    public GWindowEditControl  wName;
    public GUIButton           bPassword;
    public GWindowComboControl cGameType;
    public GWindowComboControl cPlayers;
    public GUIButton           bStart;
    public GUIButton           bExit;
    private String             password;

}
