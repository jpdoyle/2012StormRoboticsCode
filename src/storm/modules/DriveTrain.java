/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.modules;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.util.AllocationException;
import storm.interfaces.IDriveTrain;
import storm.utility.Print;
import storm.utility.Queue;

/**
 *
 * @author Storm
 */
public class DriveTrain implements IDriveTrain {

    public static final int STOP_QUEUE = 0;

    SpeedController leftMotor, rightMotor;
    RobotDrive drive;
    Solenoid solenoidHigh;
    Solenoid solenoidLow;

    boolean highgear = true;
    boolean lowgear = false;

    Queue queue = new Queue();
    public static Encoder leftEncoder = new Encoder(1, 2);
    public static Encoder rightEncoder = new Encoder(3, 4);
    
    private final double deceleration = 0.025;
    private final double acceleration = 0.045;
    private double lastLeftSpeed = 0.0;
    private double lastRightSpeed = 0.0;
    double driveLeft = 0.0;
    double driveRight = 0.0;

    Print printer = Print.getInstance();
    
    public DriveTrain (int motorChannelL, int motorChannelR, int solChannelH, int solChannelL){
        leftMotor = new Victor(motorChannelL);
        rightMotor = new Victor(motorChannelR);
	drive = new RobotDrive(leftMotor, rightMotor);
	drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
	drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        solenoidHigh = new Solenoid(solChannelH);
        solenoidLow = new Solenoid(solChannelL);
    }
    
    public void drive(double leftSpeed, double rightSpeed) {
	
	// Left Deceleration
	if (leftSpeed == 0 && lastLeftSpeed > 0) {
	    if (lastLeftSpeed - leftSpeed < deceleration) driveLeft = leftSpeed;
	    else driveLeft = lastLeftSpeed - deceleration;
	} else if (leftSpeed == 0 && lastLeftSpeed < 0) {
	    if (lastLeftSpeed - leftSpeed > -deceleration) driveLeft = leftSpeed;
	    else driveLeft = lastLeftSpeed + deceleration;

        // Left Acceleration
	} else if (leftSpeed > lastLeftSpeed) {
            if (Math.abs(leftSpeed - lastLeftSpeed) < acceleration) driveLeft = leftSpeed;
            else driveLeft = lastLeftSpeed + acceleration;
        } else if (leftSpeed < lastLeftSpeed) {
            if (Math.abs(leftSpeed - lastLeftSpeed) < acceleration) driveLeft = leftSpeed;
            else driveLeft = lastLeftSpeed - acceleration;

        // Left Normal
        } else driveLeft = leftSpeed;
	
	lastLeftSpeed = driveLeft;
	
	// Right Deceleration
	if (rightSpeed == 0 && lastRightSpeed > 0) {
	    if (lastRightSpeed - rightSpeed < deceleration) driveRight = rightSpeed;
	    else driveRight = lastRightSpeed - deceleration;
	} else if (rightSpeed == 0 && lastRightSpeed < 0) {
	    if (lastRightSpeed - rightSpeed > -deceleration) driveRight = rightSpeed;
	    else driveRight = lastRightSpeed + deceleration;

        // Right Acceleration
	} else if (rightSpeed > lastRightSpeed) {
            if (Math.abs(rightSpeed - lastRightSpeed) < acceleration) driveRight = rightSpeed;
            else driveRight = lastRightSpeed + acceleration;
        } else if (rightSpeed < lastRightSpeed) {
            if (Math.abs(rightSpeed - lastRightSpeed) < acceleration) driveRight = rightSpeed;
            else driveRight = lastRightSpeed - acceleration;

        // Right Normal
        } else driveRight = rightSpeed;
	
	lastRightSpeed = driveRight;
	
        drive.tankDrive(driveLeft, driveRight);

    }

    public void addToQueue(int type, double speed, double distance) {
        queue.add(type, speed, distance);
    }

    public void resetQueue() {
        queue.clear();
    }

    public void executeQueue() {
        switch ((int)queue.getType()) {
            case 1:
                //Forward/Backward
                drive(queue.getSpeed(), queue.getSpeed());
                if (leftEncoder.getDistance() >= queue.getDistance()) queue.next();
                break;
            case 2:
                //Left/Right
                drive(queue.getSpeed(), -queue.getSpeed());
                if (queue.getSpeed() > 0) {
                    if (leftEncoder.getDistance() >= queue.getDistance()) queue.next();
                } else {
                    if (rightEncoder.getDistance() >= queue.getDistance()) queue.next();
                }
                break;
            case 3:
                //Hook Left
                drive(0, queue.getSpeed());
                if (rightEncoder.getDistance() >= queue.getDistance()) queue.next();
                break;
            case 4:
                //Hook Right
                drive(queue.getSpeed(), 0);
                if (leftEncoder.getDistance() >= queue.getDistance()) queue.next();
                break;
            default:
                System.out.println("Error: Invalid Type");
                drive(0,0);
        }
    }

    public boolean isQueueFinished() {
        return queue.isRunning();
    }

    public void resetDistance() {
       leftEncoder.reset();
    }

    public double getDistance() {
        return leftEncoder.getDistance();
    }

    public void switchGear() {
        if (highgear) {
            highgear = false;
            lowgear = true;
        }
        else {
            highgear = true;
            lowgear = false;
        }
        closeThePodBayDoors();
    }

    public void setHighGear() {
        highgear = true;
        lowgear = false;
        closeThePodBayDoors();
    }

    public void setLowGear() {
        highgear = false;
        lowgear = true;
        closeThePodBayDoors();
    }

    private void closeThePodBayDoors() {
        if (highgear && !lowgear) {
            solenoidHigh.set(true);
            solenoidLow.set(false);
        } else if (!highgear && lowgear) {
            solenoidHigh.set(false);
            solenoidLow.set(true);
        }
    }

    public boolean isHighGear() {
        return(highgear);
    }
    
}
