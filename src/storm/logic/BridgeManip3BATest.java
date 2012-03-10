/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.I3BA;
import storm.interfaces.IRobotLogic;

/**
 *
 * @author Storm
 */
public class BridgeManip3BATest implements IRobotLogic {
    
    I3BA threeBA = RobotState.threeBA;
    
    Joystick shootJoystick = RobotState.joystickShoot;

    public void doInit() {
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
	if (shootJoystick.getRawButton(4)) {
	    threeBA.raise(0.0);
	} else if (shootJoystick.getRawButton(2)) {
	    threeBA.lower(0.0);
	} else {
	    threeBA.stop();
	}
    }

    public void doEnd() {
    }
    
}
