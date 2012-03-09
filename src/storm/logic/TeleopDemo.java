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
import storm.modules.BridgeManipulator;
import storm.modules.DriveTrain;
import storm.modules.ThreeBA;

public class TeleopDemo implements IRobotLogic {
    
    IDriveTrain driveTrain;
    I3BA threeBA;
    IBridgeManipulator bridgeManipulator;
    SpeedController feeder;
    SpeedController elevator;
    SpeedController topFeeder;
    SpeedController shooterWheel;
    
    Joystick shootJoystick;
    Joystick driveJoystick;
    
    public TeleopDemo() {
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
	driveTrain.drive(-driveJoystick.getRawAxis(2), -driveJoystick.getRawAxis(4));
	
	double elevatorSpeed = shootJoystick.getRawAxis(6);
	elevator.set(elevatorSpeed);
	feeder.set(elevatorSpeed * 0.4);
	
	if (shootJoystick.getRawButton(6)) {
	    topFeeder.set(-1.0);
	    shooterWheel.set(1.0);
	} else {
	    topFeeder.set(0.0);
	    shooterWheel.set(0.0);
	}
	
	if (shootJoystick.getRawButton(4)) {
	    threeBA.raise();
	} else if (shootJoystick.getRawButton(1)) {
	    threeBA.lower();
	} else {
	    threeBA.stop();
	}
	
	if (shootJoystick.getRawButton(3)) {
	    bridgeManipulator.raise();
	} else if (shootJoystick.getRawButton(2)) {
	    bridgeManipulator.lower();
	} else {
	    bridgeManipulator.stop();
	}
    }

    public void doEnd() {
    }
    
}
