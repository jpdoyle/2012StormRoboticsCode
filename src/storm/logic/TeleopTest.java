/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.modules.DriveTrain;
import storm.utility.Print;

/**
 *
 * @author Storm
 */
public class TeleopTest implements IRobotLogic {
    
    IDriveTrain driveTrain;
    
    Joystick driveJoystick;
    
    Print printer;

    public void doInit() {
        
        driveTrain = new DriveTrain(RobotState.PORT_MOTOR_DRIVE_LEFT, RobotState.PORT_MOTOR_DRIVE_RIGHT);
        
        driveJoystick = RobotState.joystickDrive;
        
        printer = new Print();
        
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
        driveTrain.drive(driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_LEFT),
			 driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_RIGHT));
    }
    
}
