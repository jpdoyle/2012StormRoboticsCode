package storm.modules;

/**
 * @author Gabe Casciato
 */


import storm.interfaces.I3BA;
import edu.wpi.first.wpilibj.*;


public class ThreeBA implements I3BA {

    private static final int THREEBAMOTORCHANNEL = 0;
    private static final int LIMITSWITCHFRONTCHANNEL = 0;
    private static final int LIMITSWITCHBACKCHANNEL = 0;
    Relay wormDrive;
    DigitalInput limitSwitchBack = new DigitalInput(LIMITSWITCHBACKCHANNEL);
    DigitalInput limitSwitchFront = new DigitalInput(LIMITSWITCHFRONTCHANNEL);

    public void stop() {
        wormDrive = new Relay (THREEBAMOTORCHANNEL);

    }

    public void start() {
        wormDrive = new Relay (THREEBAMOTORCHANNEL);

    }

}
