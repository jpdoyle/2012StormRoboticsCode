package storm.modules;

/**
 * @author Gabe Casciato
 */

import edu.wpi.first.wpilibj.*;
import storm.interfaces.iBridgeManipulator;

public class BridgeManipulator implements iBridgeManipulator {

    SpeedController banebot;
    public static Encoder encoder = new Encoder(1, 2);

    public void raise() {

    }

    public void lower() {

    }

}
