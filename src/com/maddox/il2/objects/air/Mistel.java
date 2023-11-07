package com.maddox.il2.objects.air;

public interface Mistel {
    public abstract Aircraft getDrone();

    public abstract void setDrone(Aircraft drone);

    public abstract Aircraft getQueen();

    public abstract void setQueen(Aircraft queen);

    public abstract void mistelExplosion();
    
    public abstract boolean isQueen();

    public abstract boolean isDrone();
    
    public abstract void setIgnoreGroundTargetForMillis(long millis);
    
    public abstract void setBombChargeArmed(boolean arm);
    
    public abstract boolean isBombChargeArmed();
}
