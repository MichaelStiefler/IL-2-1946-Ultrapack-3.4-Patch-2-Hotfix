package com.maddox.rts;

import com.maddox.il2.engine.Config;

public final class Time {
    public static boolean     bShowDiag               = true;
    private static final long MAX_NATIVE_DELTA        = 16777215L;
    private static final int  MAX_TICK_LEN            = 300;
    private int               _typedTickCounter       = 0;
    private int               _newTickLen;
    private boolean           bInLoop                 = false;

    private boolean           bChangeSpeed            = false;
    private double            newChangedSpeed         = 1.0D;

    private int               tickCounter             = 0;
    private long              tick                    = 0L;
    protected int             tickLen0                = 30;
    protected int             tickLen                 = this.tickLen0;
    protected int             tickConstLen            = this.tickLen0;
    private long              tickNext                = this.tick + this.tickLen;
    protected float           tickLenFms              = 30.0F;
    protected float           tickLenFs               = 0.03F;
    protected float           tickConstLenFms         = 30.0F;
    protected float           tickConstLenFs          = 0.03F;
    protected int             maxTimerTicksInRealTick = 5;
    private float             tickOffset              = 0.0F;
    private boolean           bEnableChangePause0     = true;
    private boolean           bEnableChangePause1     = true;
    private boolean           bEnableChangeTickLen    = true;
    private boolean           bEnableChangeSpeed      = false;
    private long              real_time;
    private long              time                    = 0L;
    private long              base_real_time;
    private long              base_time               = 0L;
    protected double          speed                   = 1.0D;
    private double            speedCur                = 1.0D;
    private long              beginRealTime           = 0L;
    private long              beginTime               = 0L;
    private long              endRealTime             = 0L;
    private long              endTime                 = 0L;
    private boolean           bPause                  = true;
    private boolean           bRealOnly               = false;
    private static long       lastLogTimeOverflow     = Long.MIN_VALUE;

    public static boolean isPaused() {
        return RTSConf.cur.time.bPause;
    }

    public static boolean isRealOnly() {
        return RTSConf.cur.time.bRealOnly;
    }

    public static int tickCounter() {
        return RTSConf.cur.time.tickCounter;
    }

    public static long tick() {
        return RTSConf.cur.time.tick;
    }

    public static long tickNext() {
        return RTSConf.cur.time.tickNext;
    }

    public static int tickLen() {
        return RTSConf.cur.time.tickLen;
    }

    public static float tickLenFms() {
        return RTSConf.cur.time.tickLenFms;
    }

    public static float tickLenFs() {
        return RTSConf.cur.time.tickLenFs;
    }

    public static float tickOffset() {
        return RTSConf.cur.time._tickOffset();
    }

    protected float _tickOffset() {
        return this.tickOffset;
    }

    public static float tickOffset(long tick) {
        return RTSConf.cur.time._tickOffset(tick);
    }

    protected float _tickOffset(long tick) {
        if (tick <= this.tick) {
            return 0.0F;
        }
        if (tick >= this.tickNext) {
            return 1.0F;
        }
        return (tick - this.tick) / this.tickLenFms;
    }

    public static long current() {
        if (RTSConf.cur == null) {
            return 0L;
        }
        return RTSConf.cur.time.time;
    }

    public static long currentReal() {
        return RTSConf.cur.time.real_time;
    }

    public static long end() {
        return RTSConf.cur.time._end();
    }

    protected long _end() {
        return this.bPause ? this.time : this.endTime;
    }

    public static long endReal() {
        return RTSConf.cur.time._endReal();
    }

    protected long _endReal() {
        return this.endRealTime;
    }

    public static int tickConstLen() {
        return RTSConf.cur.time.tickConstLen;
    }

    public static float tickConstLenFms() {
        return RTSConf.cur.time.tickConstLenFms;
    }

    public static float tickConstLenFs() {
        return RTSConf.cur.time.tickConstLenFs;
    }

    private static boolean logTimeOverflow() {
        if (Config.getLogTimeOverflow() == Config.LOG_TIME_OVERFLOW_NONE) return false;
        if (Config.getLogTimeOverflow() == Config.LOG_TIME_OVERFLOW_FULL) return true;
        if (lastLogTimeOverflow + 1000 > currentReal()) return false;
        lastLogTimeOverflow = currentReal();
        return true;
    }
    
    public void loopMessages() {
        if (this.bChangeSpeed) {
            this._resetSpeed(this.newChangedSpeed);
            this.bChangeSpeed = false;
        }
        this.endRealTime = Time.real();
        if (this.endRealTime == this.real_time) {
            return;
        }
        this.bInLoop = true;
        this.beginRealTime = this.real_time;
        this.beginTime = this.time;
        if ((this.endRealTime - this.base_real_time) > 16777215L) {
            this.base_time = this.beginTime;
            this.base_real_time = this.beginRealTime;
        }
        this.endTime = (long) (((this.endRealTime - this.base_real_time) * this.speed) + 0.5D + this.base_time);

        boolean canChangeSpeed = false;
        if (this.bEnableChangeTickLen) {
            if (((this.endTime - this.beginTime) / this.tickLen0) > this.maxTimerTicksInRealTick) {
                this._newTickLen = (int) (((this.endTime - this.beginTime) / this.maxTimerTicksInRealTick) + 1L);

                if (this._newTickLen > 300) {
                    this._newTickLen = 300;
                    canChangeSpeed = true;
                }
                if ((!this.bPause) && ((this._typedTickCounter + 20) < this.tickCounter)) {
                    if (Time.bShowDiag) {
                        if (logTimeOverflow()) System.err.println("Time overflow (" + this.tickCounter + "): tickLen " + this._newTickLen);
                    }
                    this._typedTickCounter = this.tickCounter;
                }
            } else {
                this._newTickLen = ((this.tickLen0 + (2 * this.tickLen)) / 3);
            }
        } else {
            this._newTickLen = this.tickLen0;
            canChangeSpeed = true;
        }

        if ((this.bEnableChangeSpeed) && canChangeSpeed) {
            if (((this.endTime - this.beginTime) / this.tickLen) > this.maxTimerTicksInRealTick) {
                long realTimeElapsed = this.endRealTime - this.beginRealTime;
                this.speedCur = ((this.tickLen * this.maxTimerTicksInRealTick) / (realTimeElapsed * this.speed));
                if (this.speedCur >= 1.0D) {
                    this.speedCur = 1.0D;
                } else {
                    if ((this.speed * this.speedCur) < 0.05D) {
                        this.speedCur = (0.05D / this.speed);
                    }
                    this.endTime = (long) ((realTimeElapsed * this.speed * this.speedCur) + 0.5D + this.beginTime);
                    this.base_time = this.beginTime;
                    this.base_real_time = this.beginRealTime;
                    if ((!this.bPause) && (Time.bShowDiag)) {
                        if (logTimeOverflow()) System.err.println("Time overflow (" + this.tickCounter + "): speed " + (float) this.speedCur);
                    }
                }
            } else {
                this.speedCur = 1.0D;
            }
        } else {
            this.speedCur = 1.0D;
        }

        RTSConf.cur.queueRealTimeNextTick.moveFromNextTick(RTSConf.cur.queueRealTime, true);
        while (true) {
            Message realTimeQueueMessage;
            if (this.bPause) {
                realTimeQueueMessage = RTSConf.cur.queueRealTime.get(this.endRealTime);
                if (realTimeQueueMessage == null) {
                    this.real_time = this.endRealTime;
                    Time.setCurrent(this.time, this.real_time);
                    break;
                }
                if (realTimeQueueMessage._time > this.real_time) {
                    this.real_time = realTimeQueueMessage._time;
                    Time.setCurrent(this.time, this.real_time);
                }
                realTimeQueueMessage.trySend();

                if (!this.bPause) {
                    this.endTime = (long) (((this.endRealTime - this.real_time) * this.speed * this.speedCur) + 0.5D + this.beginTime);
                    this.base_time = this.beginTime;
                    this.base_real_time = this.real_time;
                }

                continue;
            } else {
                realTimeQueueMessage = null;
                synchronized (RTSConf.cur.queueRealTime) {
                    synchronized (RTSConf.cur.queue) {
                        realTimeQueueMessage = RTSConf.cur.queue.peek(this.endTime);
                        Message endRealTimeQueueMessage = RTSConf.cur.queueRealTime.peek(this.endRealTime);

                        if ((realTimeQueueMessage == null) && (endRealTimeQueueMessage == null)) {
                            if (!this._setCurrent(this.endTime, this.endRealTime)) {
                                break;
                            }
                        } else {
                            long endRealTimeQueueMessageTime = 0L;
//                            int j;
                            boolean isReal = false;
                            if (endRealTimeQueueMessage != null) {
                                endRealTimeQueueMessageTime = this._fromReal(endRealTimeQueueMessage._time);
                                if (endRealTimeQueueMessageTime >= this.endTime) {
                                    endRealTimeQueueMessageTime = this.endTime - 1L;
                                }
                                if (realTimeQueueMessage != null) {
                                    if ((realTimeQueueMessage._time < endRealTimeQueueMessageTime) || ((realTimeQueueMessage._time == endRealTimeQueueMessageTime) && (realTimeQueueMessage._tickPos < endRealTimeQueueMessage._tickPos))) {
                                        isReal = false;
                                    } else {
                                        isReal = true;
                                    }
                                } else {
                                    isReal = true;
                                }
                            } else {
                                isReal = false;
                            }

                            if (isReal) {
                                if (endRealTimeQueueMessageTime > this.time) {
                                    if (!this._setCurrent(endRealTimeQueueMessageTime, endRealTimeQueueMessage._time)) {
                                        realTimeQueueMessage = RTSConf.cur.queueRealTime.get(this.endRealTime);
                                    } else {
                                        realTimeQueueMessage = null;
                                    }
                                } else {
                                    if (endRealTimeQueueMessage._time > this.real_time) {
                                        this.real_time = endRealTimeQueueMessage._time;
                                        Time.setCurrent(this.time, this.real_time);
                                    }
                                    realTimeQueueMessage = RTSConf.cur.queueRealTime.get(this.endRealTime);
                                }
                            } else {
                                long realTimeQueueMessageTime = this._toReal(realTimeQueueMessage._time);
                                if (realTimeQueueMessageTime >= this.endRealTime) {
                                    realTimeQueueMessageTime = this.endRealTime - 1L;
                                }
                                if (realTimeQueueMessage._time > this.time) {
                                    if (!this._setCurrent(realTimeQueueMessage._time, realTimeQueueMessageTime)) {
                                        realTimeQueueMessage = RTSConf.cur.queue.get(this.endTime);
                                    } else {
                                        realTimeQueueMessage = null;
                                    }
                                } else {
                                    if (realTimeQueueMessageTime > this.real_time) {
                                        this.real_time = endRealTimeQueueMessage._time;
                                        Time.setCurrent(this.time, this.real_time);
                                    }
                                    realTimeQueueMessage = RTSConf.cur.queue.get(this.endTime);
                                }
                            }
                        }
                    }
                }
                if (realTimeQueueMessage != null) {
                    realTimeQueueMessage.trySend();
                }
            }
        }
        this.bInLoop = false;
    }

    private boolean _setCurrent(long time, long endTime) {
        boolean retVal = false;
        this.time = time;
        if (this.time >= this.tickNext) {
            this.time = (this.tick = this.tickNext);
            if (this.tickLen != this._newTickLen) {
                this.tickLen = this._newTickLen;
                this.tickNext += this.tickLen;
                this.tickLenFms = this.tickLen;
                this.tickLenFs = (this.tickLenFms / 1000.0F);
            } else {
                this.tickNext += this.tickLen;
            }
            RTSConf.cur.queueNextTick.moveFromNextTick(RTSConf.cur.queue, false);
            this.tickCounter += 1;
            endTime = (long) (((this.time - this.base_time) / (this.speed * this.speedCur)) + 0.5D + this.base_real_time);
            if (endTime >= this.endRealTime) {
                endTime = this.endRealTime - 1L;
            }
            retVal = true;
        }
        if (endTime > this.real_time) {
            this.real_time = endTime;
        }
        Time.setCurrent(this.time, this.real_time);
        this.tickOffset = this._tickOffset(this.time);
        return retVal;
    }

    private void _resetSpeed(double speed) {
        this.speed = speed;
        this.base_time = this.time;
        this.base_real_time = this.real_time;
    }

    protected void resetGameClear() {
        this.tickCounter = 0;
        this.tick = 0L;
        this.tickLen = this.tickLen0;
        this.tickNext = this.tickLen;
        this.tickLenFms = this.tickLen;
        this.tickLenFs = (this.tickLenFms / 1000.0F);
        this.time = 0L;
        this.tickOffset = 0.0F;
        this.base_time = 0L;
        this.real_time = (this.base_real_time = Time.real());
        Time.setCurrent(this.time, this.real_time);
        this.bPause = true;
    }

    protected void resetGameCreate() {
    }

    public void setCurrent(long time) {
        if (!this.bPause) {
            return;
        }
        this.base_time = (this.time = time);
        this.tickCounter = (int) (this.time / this.tickLen0);
        this.tick = (this.tickCounter * this.tickLen0);
        this.tickNext = (this.tick + this.tickLen0);
        this.tickOffset = this._tickOffset(this.time);
        Time.setCurrent(this.time, this.real_time);
    }

    public static void setPause(boolean pause) {
        RTSConf.cur.time._setPause(pause);
    }

    protected void _setPause(boolean pause) {
        if (this.bPause == pause) {
            return;
        }
        this.bPause = pause;
        if ((!this.bInLoop) && (!this.bPause)) {
            this.base_time = this.time;
            this.base_real_time = this.real_time;
        }
    }

    public static void setRealOnly(boolean realOnly) {
        RTSConf.cur.time._setRealOnly(realOnly);
    }

    protected void _setRealOnly(boolean realOnly) {
        if (this.bRealOnly == realOnly) {
            return;
        }
        this.bRealOnly = realOnly;
        if ((this.bRealOnly) && (!this.bPause)) {
            this._setPause(true);
        }
    }

    public static void setSpeed(float speed) {
        RTSConf.cur.time._setSpeed(speed);
    }

    protected void _setSpeed(double speed) {
        if (this.bInLoop) {
            if (this.bPause) {
                this.speed = speed;
            } else {
                this.bChangeSpeed = true;
                this.newChangedSpeed = speed;
            }
        } else {
            this.bChangeSpeed = false;
            this._resetSpeed(speed);
        }
    }

    public static float speed() {
        return (float) RTSConf.cur.time.speed;
    }

    public static float nextSpeed() {
        return RTSConf.cur.time._nextSpeed();
    }

    protected float _nextSpeed() {
        if (this.bChangeSpeed) {
            return (float) this.newChangedSpeed;
        }
        return (float) this.speed;
    }

    public static long fromReal(long time) {
        return RTSConf.cur.time._fromReal(time);
    }

    protected long _fromReal(long time) {
        if (this.bPause) {
            return this.time;
        }
        long l = time - this.base_real_time;
        if ((this.speed == 1.0D) && (this.speedCur == 1.0D)) {
            return l + this.base_time;
        }
        return (long) ((l * this.speed * this.speedCur) + 0.5D + this.base_time);
    }

    public static long toReal(long time) {
        return RTSConf.cur.time._toReal(time);
    }

    protected long _toReal(long time) {
        if (this.bPause) {
            return this.real_time;
        }
        long l = time - this.base_time;
        if ((this.speed == 1.0D) && (this.speedCur == 1.0D)) {
            return l + this.base_real_time;
        }
        return (long) ((l / (this.speed * this.speedCur)) + 0.5D + this.base_real_time);
    }

    public static long fromRaw(int time) {
        return RTSConf.cur.time._fromReal(Time.realFromRaw(time));
    }

    public static long realFromRawClamp(int time) {
        long l = Time.realFromRaw(time);
        if (l < RTSConf.cur.time.real_time) {
            l = RTSConf.cur.time.real_time;
        }
        if (l > RTSConf.cur.time.endRealTime) {
            l = RTSConf.cur.time.endRealTime;
        }
        return l;
    }

    public static native long realFromRaw(int paramInt);

    public static native void setSpeedReal(float paramFloat);

    public static native float speedReal();

    public static native long real();

    public static native int raw();

    private static native void setCurrent(long paramLong1, long paramLong2);

    public static boolean isEnableChangePause() {
        Time localTime = RTSConf.cur.time;
        return (localTime.bEnableChangePause0) && (localTime.bEnableChangePause1);
    }

    public boolean isEnableChangePause0() {
        return this.bEnableChangePause0;
    }

    public boolean isEnableChangePause1() {
        return this.bEnableChangePause1;
    }

    public static boolean isEnableChangeTickLen() {
        return RTSConf.cur.time.bEnableChangeTickLen;
    }

    public static boolean isEnableChangeSpeed() {
        return RTSConf.cur.time.bEnableChangeSpeed;
    }

    public void setEnableChangePause0(boolean paramBoolean) {
        this.bEnableChangePause0 = paramBoolean;
    }

    public void setEnableChangePause1(boolean paramBoolean) {
        this.bEnableChangePause1 = paramBoolean;
    }

    public void setEnableChangeTickLen(boolean paramBoolean) {
        this.bEnableChangeTickLen = paramBoolean;
    }

    public void setEnableChangeSpeed(boolean paramBoolean) {
        this.bEnableChangeSpeed = paramBoolean;
    }

    protected Time(int tickLen, int maxTimerTicksInRealTick) {
        if (tickLen > 300) {
            tickLen = 300;
        }
        this.tickLen0 = tickLen;
        this.tickLen = tickLen;
        this.tickConstLen = tickLen;
        this.tickNext = (this.tick + tickLen);
        this.tickLenFms = tickLen;
        this.tickConstLenFms = tickLen;
        this.tickLenFs = (this.tickLenFms / 1000.0F);
        this.tickConstLenFs = (this.tickLenFms / 1000.0F);
        if (maxTimerTicksInRealTick <= 0) {
            maxTimerTicksInRealTick = 1;
        }
        this.maxTimerTicksInRealTick = maxTimerTicksInRealTick;
        this.real_time = Time.real();
        this.base_real_time = this.real_time;
        this.beginRealTime = this.real_time;
        this.endRealTime = this.real_time;
        Time.setCurrent(this.time, this.real_time);
    }

    protected void setMaxTimerTicksInRealTick(int maxTimerTicksInRealTick) {
        if (maxTimerTicksInRealTick <= 0) {
            maxTimerTicksInRealTick = 1;
        }
        this.maxTimerTicksInRealTick = maxTimerTicksInRealTick;
    }

    static {
        RTS.loadNative();
    }
}
