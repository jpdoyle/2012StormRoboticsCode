package storm.logic;

import edu.wpi.first.wpilibj.DigitalOutput;
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
    
    DigitalOutput led1 = new DigitalOutput(12);
    DigitalOutput led2 = new DigitalOutput(13);
    DigitalOutput led3 = new DigitalOutput(14);
    boolean led1b = false, led2b = false, led3b = false;
    
    double driveLeft;
    double driveRight;
    double driveModifier;
    
    boolean btnGearPressed;
    boolean btnToggleShootDistance;
    
    double[] distances;
    int distanceIndex;

    public void doInit() {
	
	driveTrain = RobotState.driveTrain;
	ballController = RobotState.ballController;
	threeBA = RobotState.threeBA;
	bridgeManipulator = RobotState.bridgeManipulator;
	targetTracker = RobotState.targetTracker;
	
	driveJoystick = RobotState.joystickDrive;
	shootJoystick = RobotState.joystickShoot;
	
	driveLeft = 0.0;
	driveRight = 0.0;
	driveModifier = 0.0;
	
	btnGearPressed = false;
	btnToggleShootDistance = false;
	
	distances = new double[] {
	    -1.0,
	    1.0,
	    2.0,
	    3.0,
	    4.0,
	    5.0
	};
	distanceIndex = 0;
	
	driveTrain.setLowGear();
	
	targetTracker.startTracking();
	
    }

    public void doContinuous() {
	ballController.runContinuous();
	
	driveTrain.drive(driveLeft * driveModifier, driveRight * driveModifier);
    }

    public void doPeriodic() {
	
	double jsLeft = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_LEFT);
	double jsRight = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_RIGHT);
	double jsBoth = driveJoystick.getRawAxis(RobotState.JOYSTICK_1_AXIS_DRIVE_BOTH);
	
	driveModifier = (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SPEED_MODIFIER)) ? 
										RobotState.DRIVE_SPEED_REDUCTION_VALUE : 
										RobotState.DRIVE_SPEED_NORMAL_VALUE;
	
	driveLeft = -Utility.checkDeadZone((Math.abs(jsBoth) > 0) ? jsBoth : jsLeft);
	driveRight = -Utility.checkDeadZone((Math.abs(jsBoth) > 0) ? jsBoth : jsRight);
	
	if (driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SWITCH_GEARS) && !btnGearPressed) {
	    btnGearPressed = true;
	    driveTrain.switchGear();
	} else if (!driveJoystick.getRawButton(RobotState.JOYSTICK_1_BUTTON_SWITCH_GEARS) && btnGearPressed) {
	    btnGearPressed = false;
	}
	
	String gear = (driveTrain.isHighGear()) ? "High Gear" : "Low Gear";
	RobotState.DASHBOARD_FEEDBACK.putString("gear", gear);
	
	if (shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_TOGGLE_DISTANCE) && !btnToggleShootDistance) {
	    btnToggleShootDistance = true;
	    distanceIndex++;
	    if (distanceIndex == distances.length) distanceIndex = 0;
	} else if (!shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_TOGGLE_DISTANCE) && btnToggleShootDistance) {
	    btnToggleShootDistance = false;
	}
	
	double distance = (distances[distanceIndex] == -1.0) ? targetTracker.getDistance() : distances[distanceIndex];
	String distanceString = (distances[distanceIndex] == -1.0) ? "Automatic" : distances[distanceIndex] + "m";
	RobotState.DASHBOARD_FEEDBACK.putString("distance.mode", distanceString);
	
	ballController.runPeriodic(
		-shootJoystick.getRawAxis(RobotState.JOYSTICK_2_AXIS_ELEVATOR),
		shootJoystick.getRawButton(RobotState.JOYSTICK_2_BUTTON_SHOOT),
		distance);
	
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
	
	led1.set(led1b);
	led2.set(led2b);
	led3.set(led3b);
	
	if (System.currentTimeMillis() - time1 >= 200) {
	    led1b = !led1b;
	    time1 = System.currentTimeMillis();
	}
	
	if (System.currentTimeMillis() - time2 >= 500) {
	    led2b = !led2b;
	    time2 = System.currentTimeMillis();
	}
	
	if (System.currentTimeMillis() - time3 >= 800) {
	    led3b = !led3b;
	    time3 = System.currentTimeMillis();
	}
	
    }
    
    double time1 = System.currentTimeMillis();
    double time2 = System.currentTimeMillis();
    double time3 = System.currentTimeMillis();

    public void doEnd() {
	if (targetTracker != null)
	    targetTracker.stopTracking();
    }
    
}
