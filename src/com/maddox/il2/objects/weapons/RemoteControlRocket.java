
package com.maddox.il2.objects.weapons;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.NearestTargets;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeX4Carrier;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.NetUpdate;
import com.maddox.rts.Time;

public class RemoteControlRocket extends Rocket {
    class Master extends ActorNet implements NetUpdate {

        public void msgNetNewChannel(NetChannel netchannel) {
            if (!Actor.isValid(this.actor()) || netchannel.isMirrored(this)) {
                return;
            }
            try {
                if (netchannel.userState == 0) {
                    NetMsgSpawn netmsgspawn = this.actor().netReplicate(netchannel);
                    if (netmsgspawn != null) {
                        this.postTo(netchannel, netmsgspawn);
                        this.actor().netFirstUpdate(netchannel);
                    }
                }
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            return false;
        }

        public void netUpdate() {
            try {
                this.out.unLockAndClear();
                RemoteControlRocket.this.pos.getAbs(RemoteControlRocket.p, RemoteControlRocket.or);
                this.out.writeFloat((float) RemoteControlRocket.p.x);
                this.out.writeFloat((float) RemoteControlRocket.p.y);
                this.out.writeFloat((float) RemoteControlRocket.p.z);
                RemoteControlRocket.or.wrap();
                int i = (int) ((RemoteControlRocket.or.getYaw() * 32000F) / 180F);
                int j = (int) ((RemoteControlRocket.or.tangage() * 32000F) / 90F);
                this.out.writeShort(i);
                this.out.writeShort(j);
                this.post(Time.current(), this.out);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;

        public Master(Actor actor) {
            super(actor);
            this.out = new NetMsgFiltered();
        }
    }

    class Mirror extends ActorNet {

        public void msgNetNewChannel(NetChannel netchannel) {
            if (!Actor.isValid(this.actor()) || netchannel.isMirrored(this)) {
                return;
            }
            try {
                if (netchannel.userState == 0) {
                    NetMsgSpawn netmsgspawn = this.actor().netReplicate(netchannel);
                    if (netmsgspawn != null) {
                        this.postTo(netchannel, netmsgspawn);
                        this.actor().netFirstUpdate(netchannel);
                    }
                }
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                return false;
            }
            if (this.isMirrored()) {
                this.out.unLockAndSet(netmsginput, 0);
                this.postReal(Message.currentTime(true), this.out);
            }
            RemoteControlRocket.p.x = netmsginput.readFloat();
            RemoteControlRocket.p.y = netmsginput.readFloat();
            RemoteControlRocket.p.z = netmsginput.readFloat();
            int i = netmsginput.readShort();
            int j = netmsginput.readShort();
            float f = -((i * 180F) / 32000F);
            float f1 = (j * 90F) / 32000F;
            RemoteControlRocket.or.set(f, f1, 0.0F);
            RemoteControlRocket.this.pos.setAbs(RemoteControlRocket.p, RemoteControlRocket.or);
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    static class SPAWN implements NetSpawn {

        public void netSpawn(int i, NetMsgInput netmsginput) {
            NetObj netobj = netmsginput.readNetObj();
            if (netobj == null) {
                return;
            }
            try {
                Actor actor = (Actor) netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
                float f = netmsginput.readFloat();
                new RemoteControlRocket(actor, netmsginput.channel(), i, point3d, orient, f);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        SPAWN() {
        }
    }

    public boolean interpolateStep() {
        float tickLen = Time.tickLenFs();
        float speed = (float) this.getSpeed(null);
        speed += (320F - speed) * 0.1F * tickLen;
        this.pos.getAbs(RemoteControlRocket.p, RemoteControlRocket.or);
        RemoteControlRocket.v.set(1.0D, 0.0D, 0.0D);
        RemoteControlRocket.or.transform(RemoteControlRocket.v);
        RemoteControlRocket.v.scale(speed);
        this.setSpeed(RemoteControlRocket.v);
        RemoteControlRocket.p.x += RemoteControlRocket.v.x * tickLen;
        RemoteControlRocket.p.y += RemoteControlRocket.v.y * tickLen;
        RemoteControlRocket.p.z += RemoteControlRocket.v.z * tickLen;
        if (this.isNet() && this.isNetMirror()) {
            this.pos.setAbs(RemoteControlRocket.p, RemoteControlRocket.or);
            return false;
        }
        if (Actor.isValid(this.getOwner())) {
            TypeX4Carrier tx4Carrier = (TypeX4Carrier) this.fm.actor;
            if (((this.getOwner() != World.getPlayerAircraft()) || !((RealFlightModel) this.fm).isRealMode()) && (this.fm instanceof Pilot)) {
                Pilot pilot = (Pilot) this.fm;
                if (pilot.target != null) {
                    pilot.target.Loc.get(RemoteControlRocket.pT);
                    RemoteControlRocket.pT.sub(RemoteControlRocket.p);
                    RemoteControlRocket.or.transformInv(RemoteControlRocket.pT);
                    if (RemoteControlRocket.pT.x > -10D) {
                        double d = Aircraft.cvt(this.fm.Skill, 0.0F, 3F, 15F, 0.0F);
                        if (RemoteControlRocket.pT.y > d) {
                            tx4Carrier.typeX4CAdjSideMinus();
                        }
                        if (RemoteControlRocket.pT.y < -d) {
                            tx4Carrier.typeX4CAdjSidePlus();
                        }
                        if (RemoteControlRocket.pT.z < -d) {
                            tx4Carrier.typeX4CAdjAttitudeMinus();
                        }
                        if (RemoteControlRocket.pT.z > d) {
                            tx4Carrier.typeX4CAdjAttitudePlus();
                        }
                    }
                }
            }
            RemoteControlRocket.or.increment(50F * tickLen * ((TypeX4Carrier) this.fm.actor).typeX4CgetdeltaAzimuth(), 50F * tickLen * ((TypeX4Carrier) this.fm.actor).typeX4CgetdeltaTangage(), 0.0F);
            RemoteControlRocket.or.setYPR(RemoteControlRocket.or.getYaw(), RemoteControlRocket.or.getPitch(), 0.0F);
            tx4Carrier.typeX4CResetControls();
        }
        this.pos.setAbs(RemoteControlRocket.p, RemoteControlRocket.or);
        if (Time.current() > (this.tStart + 500L)) {
            RemoteControlRocket.hunted = NearestTargets.getEnemy(0, -1, RemoteControlRocket.p, 800D, 0);
            if (Actor.isValid(RemoteControlRocket.hunted)) {
                float f2 = (float) RemoteControlRocket.p.distance(RemoteControlRocket.hunted.pos.getAbsPoint());
                if ((RemoteControlRocket.hunted instanceof Aircraft) && ((f2 < 20F) || ((f2 < 40F) && (f2 > this.prevd) && (this.prevd != 1000F)))) {
                    this.doExplosionAir();
                    this.postDestroy();
                    this.collide(false);
                    this.drawing(false);
                }
                this.prevd = f2;
            } else {
                this.prevd = 1000F;
            }
        }
        if (!Actor.isValid(this.getOwner()) || !(this.getOwner() instanceof Aircraft)) {
            this.doExplosionAir();
            this.postDestroy();
            this.collide(false);
            this.drawing(false);
            return false;
        } else {
            return false;
        }
    }

    public RemoteControlRocket() {
        this.fm = null;
        this.tStart = 0L;
        this.prevd = 1000F;
    }

    public RemoteControlRocket(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
        this.fm = null;
        this.tStart = 0L;
        this.prevd = 1000F;
        this.net = new Mirror(this, netchannel, i);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.pos.setBase(actor, null, true);
        this.doStart(-1F);
        RemoteControlRocket.v.set(1.0D, 0.0D, 0.0D);
        orient.transform(RemoteControlRocket.v);
        RemoteControlRocket.v.scale(f);
        this.setSpeed(RemoteControlRocket.v);
        this.collide(false);
    }

    public void start(float f, int i) {
        Actor actor = this.pos.base();
        if (Actor.isValid(actor) && (actor instanceof Aircraft)) {
            if (actor.isNetMirror()) {
                this.destroy();
                return;
            }
            this.net = new Master(this);
        }
        this.doStart(f);
    }

    protected void doStart(float f) {
        this.start(-1F, 0);
        this.fm = ((Aircraft) this.getOwner()).FM;
        this.tStart = Time.current();
        this.pos.getAbs(RemoteControlRocket.p, RemoteControlRocket.or);
        RemoteControlRocket.or.setYPR(RemoteControlRocket.or.getYaw(), RemoteControlRocket.or.getPitch(), 0.0F);
        this.pos.setAbs(RemoteControlRocket.p, RemoteControlRocket.or);
    }

    public void destroy() {
        if (this.isNet() && this.isNetMirror()) {
            this.doExplosionAir();
        }
        super.destroy();
    }

    protected void doExplosion(Actor actor, String s) {
        this.pos.getTime(Time.current(), RemoteControlRocket.p);
        MsgExplosion.send(actor, s, RemoteControlRocket.p, this.getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir() {
        this.pos.getTime(Time.current(), RemoteControlRocket.p);
        MsgExplosion.send(null, null, RemoteControlRocket.p, this.getOwner(), 45F, 2.0F, 1, 550F);
        super.doExplosionAir();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(this.getOwner().net);
        Point3d point3d = this.pos.getAbsPoint();
        netmsgspawn.writeFloat((float) point3d.x);
        netmsgspawn.writeFloat((float) point3d.y);
        netmsgspawn.writeFloat((float) point3d.z);
        Orient orient = this.pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        float f = (float) this.getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    private FlightModel     fm;
    private static Orient   or     = new Orient();
    private static Point3d  p      = new Point3d();
    private static Point3d  pT     = new Point3d();
    private static Vector3d v      = new Vector3d();
    private static Actor    hunted = null;
    private long            tStart;
    private float           prevd;

}
