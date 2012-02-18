package storm.modules;

/**
 * @author Gabe Casciato
 */

import edu.wpi.first.wpilibj.*;
import storm.interfaces.IBridgeManipulator;
import storm.utility.Print;

public class BridgeManipulator implements IBridgeManipulator {
    SpeedController bridgeManipulatorMotor;
    
    AnalogChannel rotarySensor;
    
    private final double MOTOR_SPEED = 1.0;
    private final double TOP_LIMIT = 1.523;
    private final double BOTTOM_LIMIT = 3.35;
    
    public BridgeManipulator(int bridgeMotorPort, int rotarySensorPort) {
	bridgeManipulatorMotor = new Jaguar(bridgeMotorPort);
	rotarySensor = new AnalogChannel(rotarySensorPort);
    }

    public void raise() {
//	double rotaryVoltage = getRoundedVoltage();
	if (rotarySensor.getVoltage() >= TOP_LIMIT)
	    bridgeManipulatorMotor.set(MOTOR_SPEED);
	else
	    stop();
    }

    public void lower() {
//	double rotaryVoltage = getRoundedVoltage();
	if (rotarySensor.getVoltage() <= BOTTOM_LIMIT)
	    bridgeManipulatorMotor.set(-MOTOR_SPEED);
	else
	    stop();
    }
    
    public void stop() {
	bridgeManipulatorMotor.set(0.0);
    }
    
    private double getRoundedVoltage() { // Rounds voltage to one point of precision
	return ((int) (rotarySensor.getVoltage() * 10.0)) / 10.0;
    }

}
