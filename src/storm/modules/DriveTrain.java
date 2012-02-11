/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.modules;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.util.AllocationException;
import storm.interfaces.IDriveTrain;
import storm.utility.Queue;

/**
 *
 * @author Storm
 */
public class DriveTrain implements IDriveTrain {

    public static final int STOP_QUEUE = 0;

    SpeedController leftMotor, rightMotor;
    RobotDrive drive;
    boolean highgear = true;
    Queue queue = new Queue();
    public static Encoder leftEncoder = new Encoder(1, 2);
    public static Encoder rightEncoder = new Encoder(3, 4);

    public DriveTrain (int motorChannelL, int motorChannelR){
        leftMotor = new Victor(motorChannelL);
        rightMotor = new Victor(motorChannelR);
	drive = new RobotDrive(leftMotor, rightMotor);

    }
    
    public void drive(double leftSpeed, double rightSpeed) {

        drive.tankDrive(leftSpeed, rightSpeed);

        /*
        DEPRECATED
        leftMotor1.set(leftSpeed);
        leftMotor2.set(leftSpeed);
        rightMotor1.set(rightSpeed);
        rightMotor2.set(rightSpeed);
        */
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
        if (highgear) highgear = false;
        else highgear = true;
    }

    public void setHighGear() {
        highgear = true;
    }

    public void setLowGear() {
        highgear = false;
    }
    
}
