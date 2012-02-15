package storm.logic;

import com.sun.squawk.Isolate;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.IBallCollector;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;
import storm.modules.BallCollector;
import storm.modules.BridgeManipulator;
import storm.modules.DriveTrain;
import storm.modules.Shooter;
import storm.utility.Print;

public class TeleopTest implements IRobotLogic {
    
    IBallCollector ballCollector = new BallCollector(
		RobotState.PORT_MOTOR_KANAYERBELT_FEEDER,
		RobotState.PORT_MOTOR_KANAYERBELT_BOTTOM,
		RobotState.PORT_IR_BALL_IN_1,
		RobotState.PORT_IR_BALL_IN_2,
		RobotState.PORT_IR_BALL_READY
	    );
    IShooter shooter = new Shooter(
		RobotState.PORT_MOTOR_SHOOTER_WHEEL,
		RobotState.PORT_MOTOR_KANAYERBELT_TOP,
		RobotState.PORT_IR_BALL_READY,
		RobotState.PORT_ENCODER_SHOOTER_SPEED
	    );
    
    Joystick driveJoystick;
    Joystick shootJoystick;
    
    Print printer;

    public void doInit() {
	
	RobotState.BALL_CONTAINMENT_COUNT = 0;
        
        driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
        
        printer = Print.getInstance();
        
    }

    public void doContinuous() {
//	ballCollector.run();
	shooter.doShoot();
    }
    
    boolean btn6 = false;
    
    public void doPeriodic() {
	
	if (shootJoystick.getRawButton(6) && !btn6) {
	    btn6 = true;
	    shooter.startShoot(1.0);
	} else if (!shootJoystick.getRawButton(6) && btn6) {
	    btn6 = false;
	}
	
    }
    
    private double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
