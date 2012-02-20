package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.utility.Utility;

public class Teleop implements IRobotLogic {
    
    IDriveTrain driveTrain;
    
    Joystick driveJoystick;
    Joystick shootJoystick;

    public void doInit() {
	
	driveTrain = RobotState.driveTrain;
	
	driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
	
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
	
	double jsLeft = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_LEFT);
	double jsRight = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_RIGHT);
	double jsBoth = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_BOTH);
	
	double modifier = (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SPEED_MODIFIER)) ? 
										RobotState.DRIVE_SPEED_REDUCTION_VALUE : 
										RobotState.DRIVE_SPEED_NORMAL_VALUE;
	
	double driveLeft = -Utility.checkDeadZone((Math.abs(jsBoth) > 0) ? jsBoth : jsLeft);
	double driveRight = -Utility.checkDeadZone((Math.abs(jsBoth) > 0) ? jsBoth : jsRight);
	
	driveTrain.drive(driveLeft * modifier, driveRight * modifier);
	
    }

    public void doEnd() {
    }
    
}
