package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;
import storm.modules.Shooter;

public class TeleopTest implements IRobotLogic {
    
    IShooter shooter = new Shooter(
		RobotState.PORT_MOTOR_SHOOTER_WHEEL,
		RobotState.PORT_MOTOR_KANAYERBELT_TOP,
		RobotState.PORT_IR_BALL_READY,
		RobotState.PORT_ENCODER_SHOOTER_SPEED
	    );
    
    Joystick driveJoystick;
    Joystick shootJoystick;

    public void doInit() {
	
	RobotState.BALL_CONTAINMENT_COUNT = 0;
        
        driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
        
    }

    public void doContinuous() {
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

    public void doEnd() {
    }
    
    private double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
