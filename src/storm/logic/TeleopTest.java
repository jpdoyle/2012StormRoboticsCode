package storm.logic;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.IBallCollector;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.modules.BallCollector;
import storm.modules.BridgeManipulator;
import storm.modules.DriveTrain;
import storm.utility.Print;

public class TeleopTest implements IRobotLogic {
    
    IDriveTrain driveTrain = new DriveTrain(RobotState.PORT_MOTOR_DRIVE_LEFT, RobotState.PORT_MOTOR_DRIVE_RIGHT);
    IBallCollector ballCollector = new BallCollector(
		RobotState.PORT_MOTOR_KANAYERBELT_FEEDER,
		RobotState.PORT_MOTOR_KANAYERBELT_BOTTOM,
		RobotState.PORT_IR_BALL_IN_1,
		RobotState.PORT_IR_BALL_IN_2,
		RobotState.PORT_IR_BALL_READY
	    );
    IBridgeManipulator bridgeManipulator = new BridgeManipulator(RobotState.PORT_MOTOR_BRIDGE_MANIPULATOR);
    
    Joystick driveJoystick;
    Joystick shootJoystick;
    
    Print printer;

    public void doInit() {
	
	RobotState.BALL_CONTAINMENT_COUNT = 0;
        
        driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
        
        printer = new Print();
        
    }

    public void doContinuous() {
//	ballCollector.run();
    }
    
    public void doPeriodic() {
	
	if (shootJoystick.getRawButton(2)) {
	    bridgeManipulator.lower();
	} else if (shootJoystick.getRawButton(4)) {
	    bridgeManipulator.raise();
	} else {
	    bridgeManipulator.stop();
	}
	
	printer.clearScreen();
//	printer.setLine(0, "Number of Balls: " + ballCollector.getNumBalls());
		
        driveTrain.drive(-checkDeadZone(driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_LEFT)),
			 -checkDeadZone(driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_RIGHT)));
//	shooter.set(-shootJoystick.getRawAxis(2));
    }
    
    private double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
