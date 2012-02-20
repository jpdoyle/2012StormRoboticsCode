package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.I3BA;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.interfaces.ITargetTracker;
import storm.modules.BallController;
import storm.utility.Utility;

public class Teleop implements IRobotLogic {
    
    IDriveTrain driveTrain;
    BallController ballController;
    I3BA threeBA;
    IBridgeManipulator bridgeManipulator;
    ITargetTracker targetTracker;
    
    Joystick driveJoystick;
    Joystick shootJoystick;
    
    boolean btnGearPressed;

    public void doInit() {
	
	driveTrain = RobotState.driveTrain;
	ballController = RobotState.ballController;
	threeBA = RobotState.threeBA;
	bridgeManipulator = RobotState.bridgeManipulator;
	targetTracker = RobotState.targetTracker;
	
	driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
	
	btnGearPressed = false;
	
	driveTrain.setLowGear();
	
	targetTracker.startTracking();
	
    }

    public void doContinuous() {
	ballController.runContinuous();
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
	
	if (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SWITCH_GEARS) && !btnGearPressed) {
	    btnGearPressed = true;
	    driveTrain.switchGear();
	} else if (!driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SWITCH_GEARS) && btnGearPressed) {
	    btnGearPressed = false;
	}
	
	String gear = (driveTrain.isHighGear()) ? "High Gear" : "Low Gear";
	RobotState.DASHBOARD_FEEDBACK.putString("gear", gear);
	
	ballController.runPeriodic(
		-shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_ELEVATOR),
		shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_SHOOT),
		targetTracker.getDistance());
	
	if (Utility.checkDeadZone(shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_3BA)) < 0.0) {
	    threeBA.extend();
	} else if (Utility.checkDeadZone(shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_3BA)) > 0.0) {
	    threeBA.retract();
	} else {
	    threeBA.stop();
	}
	
	if (Utility.checkDeadZone(shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_BRIDGE_MANIPULATOR)) < 0.0) {
	    bridgeManipulator.raise();
	} else if (Utility.checkDeadZone(shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_BRIDGE_MANIPULATOR)) > 0.0) {
	    bridgeManipulator.lower();
	} else {
	    bridgeManipulator.stop();
	}
	
    }

    public void doEnd() {
	if (targetTracker != null)
	    targetTracker.stopTracking();
    }
    
}
