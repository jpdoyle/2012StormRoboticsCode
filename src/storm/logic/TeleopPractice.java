package storm.logic;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.I3BA;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;

public class TeleopPractice implements IRobotLogic {
    
    IDriveTrain driveTrain;
    I3BA threeBA;
    IBridgeManipulator bridgeManipulator;
    SpeedController feeder;
    SpeedController elevator;
    SpeedController topFeeder;
    SpeedController shooterWheel;
    
    Joystick shootJoystick;
    Joystick driveJoystick;
    
    public TeleopPractice() {
	feeder = new Victor(RobotState.PORT_MOTOR_KANAYERBELT_FEEDER);
	elevator = new Victor(RobotState.PORT_MOTOR_KANAYERBELT_BOTTOM);
	topFeeder = new Victor(RobotState.PORT_MOTOR_KANAYERBELT_TOP);
	shooterWheel = new Jaguar(RobotState.PORT_MOTOR_SHOOTER_WHEEL);
    }

    public void doInit() {
	driveTrain = RobotState.driveTrain;
	threeBA = RobotState.threeBA;
	bridgeManipulator = RobotState.bridgeManipulator;
	shootJoystick = RobotState.joystickShoot;
	driveJoystick = RobotState.joystickDrive;
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
	
	double leftDrive = -driveJoystick.getRawAxis(2);
	double rightDrive = -driveJoystick.getRawAxis(4);
	double modifier = (driveJoystick.getRawButton(6)) ? 0.65 : 1.0;
	
	driveTrain.drive(modifier*leftDrive, modifier*rightDrive);
	
	double elevatorSpeed = shootJoystick.getRawAxis(6);
	elevator.set(elevatorSpeed);
	feeder.set(elevatorSpeed * 0.4);
	
	if (shootJoystick.getRawButton(6)) {
	    topFeeder.set(-1.0);
	    shooterWheel.set(0.81);
	} else {
	    topFeeder.set(0.0);
	    shooterWheel.set(0.0);
	}
	
	double shootLeftStick = checkDeadZone(shootJoystick.getRawAxis(2));
	
	if (shootLeftStick < 0) {
	    threeBA.raise(0.0);
	} else if (shootLeftStick > 0) {
	    threeBA.lower(0.0);
	} else {
	    threeBA.stop();
	}
	
	double shootRightStick = checkDeadZone(shootJoystick.getRawAxis(4));
	
	if (shootRightStick < 0) {
	    bridgeManipulator.raise();
	} else if (shootRightStick > 0) {
	    bridgeManipulator.lower();
	} else {
	    bridgeManipulator.stop();
	}
    }

    public void doEnd() {
    }
    
    private double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
