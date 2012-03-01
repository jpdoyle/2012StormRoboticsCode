
package storm.logic;

import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;

public class VictorCalibration implements IRobotLogic {

    IDriveTrain driveTrain = RobotState.driveTrain;
    
    public void doInit() {
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
	driveTrain.driveDirect(
		-RobotState.joystickDrive.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_BOTH),
		-RobotState.joystickDrive.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_BOTH));
    }

    public void doEnd() {
    }
    
}
