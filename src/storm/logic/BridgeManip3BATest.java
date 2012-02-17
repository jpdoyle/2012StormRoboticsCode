/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IRobotLogic;
import storm.modules.BridgeManipulator;

/**
 *
 * @author Storm
 */
public class BridgeManip3BATest implements IRobotLogic {
    
    IBridgeManipulator bridgeManipulator = RobotState.bridgeManipulator;
    
    Joystick shootJoystick = RobotState.joystickShoot;

    public void doInit() {
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
	if (shootJoystick.getRawButton(2)) {
	    bridgeManipulator.lower();
	} else if (shootJoystick.getRawButton(4)) {
	    bridgeManipulator.raise();
	} else {
	    bridgeManipulator.stop();
	}
    }

    public void doEnd() {
    }
    
}
