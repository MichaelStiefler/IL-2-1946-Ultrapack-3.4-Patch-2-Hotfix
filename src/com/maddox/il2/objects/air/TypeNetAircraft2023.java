package com.maddox.il2.objects.air;

// TODO: Storebror: +++ New Generic Net Replication for Aircraft Classes
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;

public interface TypeNetAircraft2023 {
    abstract void netUpdateAircraft(NetMsgFiltered out);
    abstract void netInputAircraft(NetMsgInput in);
}
// TODO: Storebror: --- New Generic Net Replication for Aircraft Classes
