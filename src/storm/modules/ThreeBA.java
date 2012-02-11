package storm.modules;

/**
 * @author Gabe Casciato
 */

import storm.interfaces.I3BA;
import edu.wpi.first.wpilibj.*;

public class ThreeBA implements I3BA {

    private static final int THREEBAMOTORCHANNEL = 10;
    private static final int LIMITSWITCHFRONTCHANNEL = 11;
    private static final int LIMITSWITCHBACKCHANNEL = 12;
    Relay wormDrive;
    DigitalInput limitSwitchBack = new DigitalInput(LIMITSWITCHBACKCHANNEL);
    DigitalInput limitSwitchFront = new DigitalInput(LIMITSWITCHFRONTCHANNEL);

    public void start() {
        wormDrive = new Relay (THREEBAMOTORCHANNEL);
        wormDrive.set(Relay.Value.kForward);
        if (limitSwitchFront.get()==true)
            wormDrive.set(Relay.Value.kOff);
    }

    public void stop() {
        wormDrive = new Relay (THREEBAMOTORCHANNEL);
        wormDrive.set(Relay.Value.kOff);
    }

    public void retract() {
        wormDrive = new Relay (THREEBAMOTORCHANNEL);
        wormDrive = new Relay (THREEBAMOTORCHANNEL);
        wormDrive.set(Relay.Value.kReverse);
        if (limitSwitchFront.get()==true)
            wormDrive.set(Relay.Value.kOff);
    }

}
