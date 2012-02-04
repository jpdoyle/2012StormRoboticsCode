/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.modules;
import edu.wpi.first.wpilibj.*;
import storm.interfaces.IDriveTrain;

/**
 *
 * @author Storm
 */
public class DriveTrain implements IDriveTrain {

    SpeedController leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    
    public DriveTrain (int motorChannelL1,int motorChannelL2,int motorChannelR1,int motorChannelR2 ){
        leftMotor1 = new Victor(motorChannelL1);
        leftMotor2 = new Victor(motorChannelL2);
        rightMotor1 = new Victor(motorChannelR1);
        rightMotor2 = new Victor(motorChannelR2);
    }
    
    public void drive(double leftSpeed, double rightSpeed) {
        leftMotor1.set(leftSpeed);
        leftMotor2.set(leftSpeed);
        rightMotor1.set(rightSpeed);
        rightMotor2.set(rightSpeed);
    }

    public void addToQueue(int type, double leftSpeed, double rightSpeed, double distance) {
    }

    public void resetQueue() {
    }

    public void executeQueue() {
    }

    public boolean isQueueFinished() {
        return false;
    }

    public void resetDistance() {
    }

    public double getDistance() {
        return -1;
    }

    public void switchGear() {
    }

    public void setHighGear() {
    }

    public void setLowGear() {
    }
    
}
