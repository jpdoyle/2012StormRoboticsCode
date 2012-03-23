package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;


public class ShooterTest implements IRobotLogic {
    
    IShooter shooter;
    Joystick shootJoystick;
    IBridgeManipulator bridgeManip;

    public void doInit() {
	shooter = RobotState.shooter;
	
	bridgeManip = RobotState.bridgeManipulator;
	
	RobotState.BALL_CONTAINMENT_COUNT = 0;
	
	shootJoystick = RobotState.joystickShoot;

	RobotState.driveTrain.setLowGear();
	
	RobotState.targetTracker.startTracking();
    }

    public void doContinuous() {
	shooter.doShoot();
    }

    boolean btn6 = false;
    boolean btn5 = false;
    
    public void doPeriodic() {
	
	double driveLeft = -RobotState.joystickDrive.getRawAxis(2);
	double driveRight = -RobotState.joystickDrive.getRawAxis(4);
	
	double modifier = (RobotState.joystickDrive.getRawButton(6)) ? 0.5 : 1.0;
	
	RobotState.driveTrain.drive(
		modifier*driveLeft, 
		modifier*driveRight);
	
	if (RobotState.joystickDrive.getRawButton(5) && !btn5) {
	    btn5 = true;
	    RobotState.driveTrain.switchGear();
	} else if (!RobotState.joystickDrive.getRawButton(5) && btn5) {
	    btn5 = false;
	}
	
	if (RobotState.driveTrain.isHighGear()) {
	    RobotState.DASHBOARD_FEEDBACK.putString("gear", "High Gear");
	} else {
	    RobotState.DASHBOARD_FEEDBACK.putString("gear", "Low Gear");
	}
	
//	Print.getInstance().setLine(0, "Number of balls: " + ballCollector.getNumBalls());
	if (shootJoystick.getRawButton(6) && !btn6) {
	    btn6 = true;
	    shooter.startShoot(false, 5.0);
	} else if (!shootJoystick.getRawButton(6) && btn6) {
	    btn6 = false;
	}
	
	if (shootJoystick.getRawButton(4)) {
	    bridgeManip.raise();
	} else if (shootJoystick.getRawButton(2)) {
	    bridgeManip.lower();
	} else {
	    bridgeManip.stop();
	}
	
    }

    public void doEnd() {
	RobotState.targetTracker.stopTracking();
    }
    
    private double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
