package storm.logic;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.modules.DriveTrain;
import storm.utility.Print;

public class TeleopTest implements IRobotLogic {
    
    IDriveTrain driveTrain = new DriveTrain(RobotState.PORT_MOTOR_DRIVE_LEFT, RobotState.PORT_MOTOR_DRIVE_RIGHT);
    SpeedController shooter = new Jaguar(5);
    
    Joystick driveJoystick;
    Joystick shootJoystick;
    
    Print printer;

    public void doInit() {
        
        driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
        
        printer = new Print();
        
    }

    public void doContinuous() {
    }
    
    public void doPeriodic() {
	
        driveTrain.drive(-checkDeadZone(driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_LEFT)),
			 -checkDeadZone(driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_RIGHT)));
	shooter.set(-shootJoystick.getRawAxis(2));
    }
    
    private double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
