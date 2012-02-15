package storm.modules;

/**
 * @author Gabe Casciato
 */

import edu.wpi.first.wpilibj.*;
import storm.interfaces.IBridgeManipulator;

public class BridgeManipulator implements IBridgeManipulator {
    SpeedController bridgeManipulatorMotor;
    
    private final double MOTOR_SPEED = 1.0;
    
    public BridgeManipulator(int BridgeMotor) {
	bridgeManipulatorMotor = new Jaguar(BridgeMotor);
    }

    public void raise() {
	bridgeManipulatorMotor.set(MOTOR_SPEED);
    }

    public void lower() {
	bridgeManipulatorMotor.set(-MOTOR_SPEED);
    }
    
    public void stop() {
	bridgeManipulatorMotor.set(0.0);
    }

}
