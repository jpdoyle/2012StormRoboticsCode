package storm.modules;

/**
 * @author Gabe Casciato
 */

import edu.wpi.first.wpilibj.*;
import storm.interfaces.IBridgeManipulator;

public class BridgeManipulator implements IBridgeManipulator {

    private static final int BRIDGEMANIPULATORMOTORCHANNEL = 0;
    SpeedController banebot;
    public static Encoder encoder = new Encoder(1, 2);

    public void raise() {
      banebot = new Victor(BRIDGEMANIPULATORMOTORCHANNEL);

    }

    public void lower() {
      banebot = new Victor(BRIDGEMANIPULATORMOTORCHANNEL);

    }

}
