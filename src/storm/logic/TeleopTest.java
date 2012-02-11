package storm.logic;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.modules.DriveTrain;
import storm.utility.Print;

public class TeleopTest implements IRobotLogic {
    
    IDriveTrain driveTrain = new DriveTrain(RobotState.PORT_MOTOR_DRIVE_LEFT, RobotState.PORT_MOTOR_DRIVE_RIGHT);
    
    Joystick driveJoystick;
    
    Print printer;
    
    DigitalInput di = new DigitalInput(5);

    public void doInit() {
        
        driveJoystick = RobotState.joystickDrive;
        
        printer = new Print();
        
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
        driveTrain.drive(-driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_LEFT),
			 -driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_RIGHT));
	printer.setLine(3, "DigitalInput: " + !di.get());
    }
    
}
