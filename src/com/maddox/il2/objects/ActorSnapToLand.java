package com.maddox.il2.objects;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

public class ActorSnapToLand extends ActorMesh {
    class Scaler extends Interpolate {

        public boolean tick() {
            if (Config.isUSE_RENDER()) {
                if (ActorSnapToLand.this.dScale != 0.0F) {
                    ActorSnapToLand.this.scale = ActorSnapToLand.this.scale + ActorSnapToLand.this.dScale;
                    ActorSnapToLand.this.mesh().setScale(ActorSnapToLand.this.scale);
                }
                if (ActorSnapToLand.this.adScale != 0.0F) {
                    ActorSnapToLand.this.ascale = ActorSnapToLand.this.ascale + ActorSnapToLand.this.adScale;
                    ActorSnapToLand.this.mat.set((byte) 10, ActorSnapToLand.this.ascale);
                }
            }
            return true;
        }

        Scaler() {
        }
    }

    public boolean isStaticPos() {
        return true;
    }

    public ActorSnapToLand(String s, boolean flag, Loc loc, float f, float f1, float f2, float f3, float f4) {
        this(s, flag, loc, 1.0F, f, f1, f2, f3, f4);
    }

    protected void createActorHashCode() {
        this.makeActorRealHashCode();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public ActorSnapToLand(String s, boolean flag, Loc loc, float f, float f1, float f2, float f3, float f4, float f5) {
        super(s, loc);
        this.scale = 1.0F;
        this.dScale = 0.0F;
        this.ascale = 1.0F;
        this.adScale = 0.0F;
        if (Config.isUSE_RENDER()) {
            this.mat = this.mesh().material(0);
            if (this.mat != null) {
                if (flag) {
                    Mat mat1 = (Mat) this.mat.Clone();
                    if (mat1 != null) {
                        this.mat = mat1;
                        this.mesh().materialReplace(0, this.mat);
                    }
                }
                this.mat.setLayer(0);
            }
        }
        if (f5 > 0.0F) {
            this.postDestroy(Time.current() + (long) (f5 * 1000F));
            Scaler scaler = null;
            if (f1 != f2) {
                int i = (int) (f5 / Time.tickLenFs());
                this.scale = f1;
                if (i > 0) {
                    this.dScale = (f2 - f1) / i;
                    scaler = new Scaler();
                    this.interpPut(scaler, "scaler", Time.current(), null);
                }
            }
            if (f3 != f4) {
                int j = (int) (f5 / Time.tickLenFs());
                this.ascale = f3;
                if (j > 0) {
                    this.adScale = (f4 - f3) / j;
                    if (scaler == null) {
                        Scaler scaler1 = new Scaler();
                        this.interpPut(scaler1, "scaler", Time.current(), null);
                    }
                }
            }
        } else {
            this.scale = f1;
            this.ascale = f3;
        }
        if (Config.isUSE_RENDER()) {
            if (this.scale != 1.0F) {
                this.mesh().setScale(this.scale);
            }
            if (this.ascale != 1.0F) {
                if (this.mat != null) this.mat.set((byte) 10, this.ascale);
            }
        }
        this.pos.getAbs(ActorSnapToLand.p, ActorSnapToLand.o);
        ActorSnapToLand.p.z = Engine.land().HQ(ActorSnapToLand.p.x, ActorSnapToLand.p.y) + f;
        Engine.land().N(ActorSnapToLand.p.x, ActorSnapToLand.p.y, ActorSnapToLand.normal);
        ActorSnapToLand.o.orient(ActorSnapToLand.normal);
        this.pos.setAbs(ActorSnapToLand.p, ActorSnapToLand.o);
        this.pos.reset();
        this.drawing(true);
    }

    private float           scale;
    private float           dScale;
    private float           ascale;
    private float           adScale;
    private Mat             mat;
    private static Vector3f normal = new Vector3f();
    private static Point3d  p      = new Point3d();
    private static Orient   o      = new Orient();
}
