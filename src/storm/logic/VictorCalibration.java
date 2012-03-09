
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
	double joystickValue = -RobotState.joystickDrive.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_BOTH);
	driveTrain.getLeftMotor().set(joystickValue);
	driveTrain.getRightMotor().set(joystickValue);
    }

    public void doEnd() {
    }
    
}
