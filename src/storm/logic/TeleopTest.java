package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;

public class TeleopTest implements IRobotLogic {
    
    IShooter shooter = RobotState.shooter;
    
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
	    shooter.startShoot(false, 1.0);
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
