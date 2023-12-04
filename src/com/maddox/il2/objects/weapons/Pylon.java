package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MeshShared;
import com.maddox.rts.Message;
import com.maddox.rts.Property;

public class Pylon extends ActorMesh implements BulletEmitter {
    private String hookName;
    private int    chunkIndx;
    private float massa;
    private float dragCx;
    private boolean bMinusDrag;

    public BulletEmitter detach(HierMesh hierMesh, int chunkIndex) {
        if (this.isDestroyed()) {
            return GunEmpty.get();
        }
        if ((chunkIndex == -1) || (chunkIndex == this.chunkIndx)) {
            this.destroy();
            return GunEmpty.get();
        }
        return this;
    }

    public boolean isEnablePause() {
        return false;
    }

    public boolean isPause() {
        return false;
    }

    public void setPause(boolean pause) {
    }

    public float bulletMassa() {
        return 0.0F;
    }

    public int countBullets() {
        return 0;
    }

    public boolean haveBullets() {
        return false;
    }

    public void loadBullets() {
    }

    public void loadBullets(int numBullets) {
    }

    public void _loadBullets(int numBullets) {
    }

    public Class bulletClass() {
        return null;
    }

    public void setBulletClass(Class theClass) {
    }

    public boolean isShots() {
        return false;
    }

    public void shots(int i, float f) {
    }

    public void shots(int i) {
    }

    public String getHookName() {
        return this.hookName;
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public Pylon() {
        this.setMesh(MeshShared.get(Property.stringValue(this.getClass(), "mesh", null)));
        this.collide(false);
        this.drawing(true);
        this.massa = Property.floatValue(getClass(), "massa", -1.0F);
        this.dragCx = Property.floatValue(getClass(), "dragCx", -1.0F);
        if (this.dragCx == -1.0F) this.dragCx = Property.floatValue(getClass(), "drag", -1.0F);
        int i = Property.intValue(getClass(), "bMinusDrag", 0);
        this.bMinusDrag = (i == 1);
    }

    public void set(Actor actor, String s, Loc l) {
        this.set(actor, s);
    }

    public void set(Actor actor, String s, String s2) {
        this.set(actor, s);
    }

    public void set(Actor actor, String hookName) {
        this.hookName = hookName;
        this.setOwner(actor);
        if (hookName != null) {
            Hook hook = actor.findHook(hookName);
            this.pos.setBase(actor, hook, false);
            this.pos.changeHookToRel();
            this.chunkIndx = hook.chunkNum();
        } else {
            this.pos.setBase(actor, null, false);
            this.chunkIndx = -1;
        }
        this.visibilityAsBase(true);
        this.pos.setUpdateEnable(false);
        this.pos.reset();
    }
    
    public float getMassa()
    {
      return this.massa;
    }
    
    public float setMassa(float massa)
    {
      return this.massa = massa;
    }
    
    public float getDragCx()
    {
      return this.dragCx;
    }
    
    public float setDragCx(float dragCx)
    {
      return this.dragCx = dragCx;
    }
    
    public boolean isMinusDrag()
    {
      return this.bMinusDrag;
    }
    
    public boolean setMinusDrag(boolean isMinusDrag)
    {
      return this.bMinusDrag = isMinusDrag;
    }
}
