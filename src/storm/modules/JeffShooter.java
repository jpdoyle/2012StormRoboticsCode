/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.modules;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import storm.RobotState;
import storm.interfaces.IShooter;

/**
 *
 * @author Awesome
 */
public class JeffShooter implements IShooter {
    
    SpeedController shooterMotor,
		    transferMotor;
    
    DigitalInput ready,
		 hallEffect;
    
    Counter counter;
    
    boolean shooting,
	    isReady;
	
    double RPM,
	   targetDistance;
    
    
    
    public JeffShooter() {
	
	RPM = 0;
	shooting = false;
	isReady = false;
	
	counter = new Counter(EncodingType.k1X, hallEffect, hallEffect, false);
        counter.clearDownSource();
        counter.setUpSourceEdge(true, false);
	
    }

    public void preShoot() {
    }

    public void startShoot(double distance) {
	
	shooting = true;
	targetDistance = distance;
	
    }

    public void doShoot() {
	
	setRPM(targetDistance);
	
    }

    public boolean isShooting() {
	return shooting;
    }
    
    void setRPM(double trueDistance) {
	
	double distance = Math.floor(trueDistance * 100 + 5) / 100;
	
    }

    public double getRPM() {
	return RPM;
    }

    public void endShoot() {
	shooterMotor.set(0.0);
    }

    public void setContinuousShoot(boolean continuousShoot) {
    }
    
}
