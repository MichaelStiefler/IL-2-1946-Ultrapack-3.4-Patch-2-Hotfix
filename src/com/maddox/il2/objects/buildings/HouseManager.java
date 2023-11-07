/*4.10.1 class*/
package com.maddox.il2.objects.buildings;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.ZutiSupportMethods_ResourcesManagement;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.util.NumberTokenizer;

// TODO: Edited by SAS~Storebror, Plate implementation backport!
public class HouseManager extends Actor {
    private int     houses         = 0;
    private House[] house          = null;
    private Plate[] plate          = null;
    private long[]  houseInitState = null;

    class HouseNet extends ActorNet {
        public void fullUpdateChannel(NetChannel netchannel) {
            int i = HouseManager.this.houses / 64 + 1;
            try {
                for (int i_0_ = 0; i_0_ < i; i_0_++) {
                    long l = 0L;
                    for (int i_1_ = 0; i_1_ < 64; i_1_++) {
                        int i_2_ = i_0_ * 64 + i_1_;
                        if (i_2_ >= HouseManager.this.houses) break;
                        if (Actor.isAlive(HouseManager.this.house[i_2_])) l |= 1L << i_1_;
                    }
                    if (l != HouseManager.this.houseInitState[i_0_]) {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                        netmsgguaranted.writeShort(i_0_);
                        netmsgguaranted.writeLong(l);
                        this.postTo(netchannel, netmsgguaranted);
                    }
                }
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.available() > 4 + NetMsgInput.netObjReferenceLen()) {
                int i = netmsginput.readUnsignedShort();
                long l = netmsginput.readLong();
                for (int i_3_ = 0; i_3_ < 64; i_3_++) {
                    int i_4_ = i * 64 + i_3_;
                    if (i_4_ >= HouseManager.this.houses) break;
                    House house = HouseManager.this.house[i_4_];
                    if (Actor.isValid(house)) {
                        boolean bool = (l & 1L << i_3_) != 0L;
                        house.setDiedFlag(!bool);
                    }
                }
            } else {
                int i = netmsginput.readInt();
                if (i >= HouseManager.this.houses) return true;
                House house = HouseManager.this.house[i];
                if (!Actor.isAlive(house)) return true;
                NetObj netobj = netmsginput.readNetObj();
                if (netobj == null) return true;
                Actor actor = (Actor) netobj.superObj();
                house.doDieShow();
                World.onActorDied(house, actor);
                this.postDie(i, actor, netmsginput.channel());
            }
            return true;
        }

        private void postDie(int i, Actor actor, NetChannel netchannel) {
            int i_5_ = this.countMirrors();
            if (this.isMirror()) i_5_++;
            if (netchannel != null) i_5_--;
            if (i_5_ > 0) try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeInt(i);
                netmsgguaranted.writeNetObj(actor.net);
                this.postExclude(netchannel, netmsgguaranted);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        public HouseNet(Actor actor) {
            super(actor);
        }

        public HouseNet(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
        }
    }

    public void destroy() {
        if (!this.isDestroyed()) {
            for (int i = 0; i < this.houses; i++) {
                if (Actor.isValid(this.house[i])) this.house[i].destroy();
                this.house[i] = null;
            }
            for(int j = 0; j < this.plate.length; j++)
            {
                if(Actor.isValid(this.plate[j]))
                    this.plate[j].destroy();
                this.plate[j] = null;
            }
            this.house = null;
            this.plate = null;
            this.houseInitState = null;
            super.destroy();
        }
    }

    public void fullUpdateChannel(NetChannel netchannel) {
        ((HouseNet) this.net).fullUpdateChannel(netchannel);
    }

    public void onHouseDie(House house, Actor actor) {
        for (int i = 0; i < this.houses; i++)
            if (this.house[i] == house) {
                ((HouseNet) this.net).postDie(i, actor, null);
                break;
            }

        // TODO: Comment by |ZUTI|; house = object that was destroyed, actor = destroyed
        // -----------------------------------------------------------------------------
        ZutiSupportMethods_ResourcesManagement.reduceResources(house, true);
        // -----------------------------------------------------------------------------
    }

    private void createNetObj(NetChannel netchannel, int i) {
        if (netchannel == null) this.net = new HouseNet(this);
        else this.net = new HouseNet(this, netchannel, i);
    }

    public HouseManager(SectFile sectfile, String string, NetChannel netchannel, int i) {
        int sectionIndex = sectfile.sectionIndex(string);
        if (sectionIndex < 0) return;
        int vars = sectfile.vars(sectionIndex);
//        this.houses = vars;
//        this.house = new House[vars];
        this.houseInitState = new long[vars / 64 + 1];
        Point3d point3d = new Point3d();
        Orient orient = new Orient();
        ActorSpawnArg actorspawnarg = new ActorSpawnArg();
//        int plates = 0;
//        for (int varIndex = 0; varIndex < vars; varIndex++) {
//            NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(sectionIndex, varIndex));
//            numbertokenizer.next("");
//            if (numbertokenizer.next("").startsWith("Plate")) plates++;
//        }
//        plate = new Plate[plates];
        int houseIndex = 0;
//        int plateIndex = 0;
//        
        ArrayList houseList = new ArrayList();
        ArrayList plateList = new ArrayList();
        for (int varIndex = 0; varIndex < vars; varIndex++) {
            try {
                NumberTokenizer numbertokenizer = new NumberTokenizer(sectfile.line(sectionIndex, varIndex));
                String buildingName = numbertokenizer.next("");
                String buildingClassName = numbertokenizer.next("");
                boolean isPlate = buildingClassName.toLowerCase().startsWith("plate");
                buildingClassName = "com.maddox.il2.objects.buildings." + buildingClassName;
                boolean isAlive = numbertokenizer.next(1) == 1;
                double coordX = numbertokenizer.next(0.0);
                double coordY = numbertokenizer.next(0.0);
                float orientation = numbertokenizer.next(0.0F);
                ActorSpawn actorspawn = (ActorSpawn) Spawn.get_WithSoftClass(buildingClassName, false);
                if (actorspawn == null) continue;
                point3d.set(coordX, coordY, 0.0);
                actorspawnarg.point = point3d;
                orient.set(orientation, 0.0F, 0.0F);
                actorspawnarg.orient = orient;
                if (isPlate) {
                    try {
                        Plate thePlate = (Plate) actorspawn.actorSpawn(actorspawnarg);
                        thePlate.setName(buildingName);
                        thePlate.setOwner(this);
//                        plate[plateIndex] = thePlate;
//                        plateIndex++;
                        plateList.add(thePlate);
                        continue;
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                } else {
                    try {
                        House theHouse = (House) actorspawn.actorSpawn(actorspawnarg);
                        // TODO: Added by |ZUTI|
                        theHouse.setName(buildingName);
                        if (!isAlive) theHouse.setDiedFlag(true);
                        else {
                            int houseInitStateIndex = houseIndex / 64;
                            int houseInitStateValue = houseIndex % 64;
                            this.houseInitState[houseInitStateIndex] |= 1L << houseInitStateValue;
                        }
//                        this.house[houseIndex] = theHouse;
                        theHouse.setOwner(this);
                        houseIndex++;
                        houseList.add(theHouse);
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error when reading .mis Buildings section line " + varIndex + " (" + ex.toString() + " )");
            }
        }
        this.houses = houseList.size();
        this.house = (House[]) houseList.toArray(new House[houseList.size()]);
        this.plate = (Plate[]) plateList.toArray(new Plate[plateList.size()]);
        this.createNetObj(netchannel, i);
        if (Actor.isValid(World.cur().houseManager)) World.cur().houseManager.destroy();
        World.cur().houseManager = this;
    }

    // TODO: Methods by |ZUTI|
    // --------------------------------------------
    public House[] zutiGetHouses() {
        return this.house;
    }
    // --------------------------------------------
}
