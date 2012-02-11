package storm.modules;

/**
 * @author Gabe Casciato
 */

import edu.wpi.first.wpilibj.*;
import storm.interfaces.IBridgeManipulator;

public class BridgeManipulator implements IBridgeManipulator {

    private static final int BRIDGEMANIPULATORMOTORCHANNEL = 1;
    SpeedController banebot;

    public void raise() {
      banebot = new Victor(BRIDGEMANIPULATORMOTORCHANNEL);
      banebot.set(-0.5);
    }

    public void lower() {
      banebot = new Victor(BRIDGEMANIPULATORMOTORCHANNEL);
      banebot.set(0.5);
    }

}
