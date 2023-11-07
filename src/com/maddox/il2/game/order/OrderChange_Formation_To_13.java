package com.maddox.il2.game.order;

import com.maddox.il2.ai.Formation;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;

class OrderChange_Formation_To_13 extends Order {
    public OrderChange_Formation_To_13() {
        super("C_F_WALL");
    }

    public void run() {
        Voice.setSyncMode(1);
        for (int commandSetIndex = 0; commandSetIndex < this.CommandSet().length; commandSetIndex++) {
            Aircraft aircraft = this.CommandSet()[commandSetIndex];
            if ((!Actor.isAlive(aircraft)) || (!(aircraft.FM instanceof Pilot)) || (!Actor.isAlive(aircraft.FM.actor))) {
                continue;
            }
            Pilot pilot = (Pilot) aircraft.FM;
            if (pilot.Group != null) {
                if ((this.isEnableVoice()) && (this.CommandSet()[commandSetIndex] != this.Player()) && ((this.CommandSet()[commandSetIndex].getWing() == this.Player().getWing()) || (this.CommandSet()[commandSetIndex].aircIndex() == 0))) {
                    Voice.speakLineAstern(this.CommandSet()[commandSetIndex]);
                }
                pilot.Group.setFormationAndScale(Formation.F_WALL, pilot.formationScale, true);
            }
        }
        Voice.setSyncMode(0);
    }
}
