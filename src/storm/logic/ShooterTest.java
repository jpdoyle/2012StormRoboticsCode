package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;


public class ShooterTest implements IRobotLogic {
    
    IShooter shooter;
    Joystick shootJoystick;

    public void doInit() {
	shooter = RobotState.shooter;
	shootJoystick = RobotState.joystickShoot;
    }

    public void doContinuous() {
	shooter.doShoot();
    }

    boolean btn6 = false;
    
    public void doPeriodic() {
	if (shootJoystick.getRawButton(6) && !btn6) {
	    btn6 = true;
	    shooter.startShoot(0.0);
	} else if (!shootJoystick.getRawButton(6) && btn6) {
	    btn6 = false;
	}
    }

    public void doEnd() {
    }
    
}
