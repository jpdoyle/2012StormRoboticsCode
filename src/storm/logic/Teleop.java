package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.*;
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
    boolean btnToggleShootDistance;
    boolean btnIncreaseOffset;
    boolean btnDecreaseOffset;
    boolean btnResetOffset;
    boolean btnToggleTracker;
    
    double[] distances;
    int distanceIndex;
    
    double distanceOffset;
    final double DISTANCE_OFFSET_INCREMENT = 0.1;

    public void doInit() {
	
	driveTrain = RobotState.driveTrain;
	ballController = RobotState.ballController;
	threeBA = RobotState.threeBA;
	bridgeManipulator = RobotState.bridgeManipulator;
	targetTracker = RobotState.targetTracker;
	
	driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
	
	btnGearPressed = false;
	btnToggleShootDistance = false;
	btnIncreaseOffset = false;
	btnDecreaseOffset = false;
	btnResetOffset = false;
	btnToggleTracker = false;
	
	distances = new double[] {
	    -1.0,
	    1.0,
	    2.0,
	    3.0,
	    4.0,
	    5.0
	};
	distanceIndex = 0;
	
	distanceOffset = 0.0;
	
	driveTrain.setLowGear();
	
	RobotState.TARGET_TRACKER_IS_TRACKING = false;
	targetTracker.stopTracking();
	
    }

    public void doContinuous() {
	ballController.runContinuous();
	
	double jsLeft = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_LEFT);
	double jsRight = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_RIGHT);
	double jsBoth = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_BOTH);
	
	double driveModifier = (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SPEED_MODIFIER)) ? 
										RobotState.DRIVE_SPEED_REDUCTION_VALUE : 
										RobotState.DRIVE_SPEED_NORMAL_VALUE;
	
	double driveLeft = -Utility.checkDeadZone((Math.abs(jsBoth) > 0) ? jsBoth : jsLeft);
	double driveRight = -Utility.checkDeadZone((Math.abs(jsBoth) > 0) ? jsBoth : jsRight);
	
	if (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_DIRECT_DRIVE)) {
	    driveTrain.driveDirect(driveLeft * driveModifier, driveRight * driveModifier);
	} else {
	    driveTrain.drive(driveLeft * driveModifier, driveRight * driveModifier);
	}
    }

    public void doPeriodic() {
	
	if (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SWITCH_GEARS) && !btnGearPressed) {
	    btnGearPressed = true;
	    driveTrain.switchGear();
	} else if (!driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SWITCH_GEARS) && btnGearPressed) {
	    btnGearPressed = false;
	}
	
	if (shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_TOGGLE_DISTANCE) && !btnToggleShootDistance) {
	    btnToggleShootDistance = true;
	    distanceIndex++;
	    if (distanceIndex == distances.length) distanceIndex = 0;
	} else if (!shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_TOGGLE_DISTANCE) && btnToggleShootDistance) {
	    btnToggleShootDistance = false;
	}
	
	double distance = (distances[distanceIndex] == -1.0) ? RobotState.DASHBOARD_FEEDBACK.getDouble("trackerExtension.z", 1.0) : distances[distanceIndex];
	String distanceString = (distances[distanceIndex] == -1.0) ? "Automatic" : distances[distanceIndex] + "m";
	RobotState.DASHBOARD_FEEDBACK.putString("distance.mode", distanceString);
	
	if (shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_INCREASE_OFFSET) && !btnIncreaseOffset) {
	    btnIncreaseOffset = true;
	    distanceOffset += DISTANCE_OFFSET_INCREMENT;
	} else if (!shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_INCREASE_OFFSET) && btnIncreaseOffset) {
	    btnIncreaseOffset = false;
	}
	
	if (shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_DECREASE_OFFSET) && !btnDecreaseOffset) {
	    btnDecreaseOffset = true;
	    distanceOffset -= DISTANCE_OFFSET_INCREMENT;
	} else if (!shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_DECREASE_OFFSET) && btnDecreaseOffset) {
	    btnDecreaseOffset = false;
	}
	
	if (shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_RESET_OFFSET) && !btnResetOffset) {
	    btnResetOffset = true;
	    distanceOffset = 0.0;
	} else if (!shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_RESET_OFFSET) && btnResetOffset) {
	    btnResetOffset = false;
	}
	
	RobotState.DASHBOARD_FEEDBACK.putDouble("distance.offset", ((int)(distanceOffset * 10.0))/10.0);
	
	ballController.runPeriodic(
		-shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_ELEVATOR),
		shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_PRESHOOT),
		shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_SHOOT),
		distance + distanceOffset);
	
	boolean threeBASafety = shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_3BA_SAFETY);
	
	double raw3BAJsValue = Utility.checkDeadZone(shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_3BA));
	if (raw3BAJsValue < 0.0 && threeBASafety) {
	    threeBA.raise(Math.abs(raw3BAJsValue));
	} else if (raw3BAJsValue > 0.0 && threeBASafety) {
	    threeBA.lower(Math.abs(raw3BAJsValue));
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
	
	if (shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_TOGGLE_TARGET_TRACKER) && !btnToggleTracker) {
	    btnToggleTracker = true;
	    if (RobotState.TARGET_TRACKER_IS_TRACKING) {
		targetTracker.stopTracking();
		RobotState.TARGET_TRACKER_IS_TRACKING = false;
	    } else if (!RobotState.TARGET_TRACKER_IS_TRACKING) {
		targetTracker.startTracking();
		RobotState.TARGET_TRACKER_IS_TRACKING = true;
	    }
	} else if (!shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_TOGGLE_TARGET_TRACKER) && btnToggleTracker) {
	    btnToggleTracker = false;
	}
    }

    public void doEnd() {
	if (targetTracker != null)
	    targetTracker.stopTracking();
    }
    
}
