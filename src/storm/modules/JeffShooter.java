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
	   RPM_SCALE,
	   distance,
	   targetRPM,
	   period,
	   shooterMaxAcel,
	   shooterSlowAcel,
	   Accuracy,
	   correctPower,
	   Timer,
	   state;
    
    double[] table;
    
    public JeffShooter(int shooterMotorChannel,int transferMotorChannel, int IRready, int hallEffectSensor) {
	
        shooterMotor = new Jaguar(shooterMotorChannel);
        transferMotor = new Victor(transferMotorChannel);
        ready = new DigitalInput(IRready);
        hallEffect = new DigitalInput(hallEffectSensor);
	
	table = new double[42];
	
	Accuracy = 50;
	
	shooterMaxAcel = .008;
	shooterSlowAcel = .001;
	
	state = 0;
	
	correctPower = 0;
	RPM = 0;
	shooting = false;
	isReady = false;
	
	counter = new Counter(EncodingType.k1X, hallEffect, hallEffect, false);
        counter.clearDownSource();
        counter.setUpSourceEdge(true, false);
	
    }

    public void preShoot() {
	if (ready.get()) transferMotor.set(-1);
	else transferMotor.set(0);
    }

    public void startShoot(boolean useTable, double inputDistance) {
	
	endShoot();
	
	if (useTable) {
	    roundDistance(inputDistance);
	} else {
	    calculateRPM(inputDistance);
	}
	
	state = 0;
	correctPower = 0;
	shooting = true;
	counter.start();
	
    }

    public void doShoot() {
	
	RPM = counter.getPeriod() / 60;
	
	setRPMandOtherStuff();
	
    }

    public boolean isShooting() {
	return shooting;
    }
    
    void setRPMandOtherStuff() {
	RPM = counter.getPeriod() / 60;
	
	if (state == 0) {
	    shooterMotor.set(shooterMotor.get()+shooterMaxAcel);
	    if (RPM >= targetRPM) state++;
	    if (ready.get()) transferMotor.set(-1);
	    else transferMotor.set(0);
	} else if (state == 1) {
	    if (ready.get()) transferMotor.set(-1);
	    else {
		transferMotor.set(0);
		state++;
	    }
	} else if (state == 2) {
	    if (RPM > targetRPM + Accuracy/2) shooterMotor.set(shooterMotor.get()-shooterSlowAcel);
	    else if (RPM < targetRPM - Accuracy/2) shooterMotor.set(shooterMotor.get()+shooterSlowAcel);
	    else {
		correctPower = shooterMotor.get();
		state = 3;
	    }
	} else if (state == 3) {
	    transferMotor.set(-1);
	    shooterMotor.set(correctPower);
	    if (ready.get()) {
		Timer = System.currentTimeMillis();
		state++;
	    }
	} else if (state == 4) {
	    if (Timer + 3000 <= System.currentTimeMillis()) endShoot();
	}
	
    }

    public double getRPM() {
	return RPM;
    }

    public void endShoot() {
	shooterMotor.set(0.0);
	counter.stop();
    }

    public void setContinuousShoot(boolean continuousShoot) {
    }
    
    public void roundDistance(double inputDistance) {
	
	distance = inputDistance;
	
	double Place2 = 0;
	double Place3 = 0;
	
	boolean upper1 = false;
	boolean upper2 = false;
	
	Place2 = Math.floor(distance * 100 + 0.5) / 100.0 - Math.floor(distance);
	Place3 = Math.floor(distance * 1000 + 0.5) / 1000.0 - Math.floor(distance);
	
	double difference = Math.abs(Place2 - Place3);
	
	if (Math.floor(Place3 * 10 + .5) >= Place3 * 10) upper1 = true;
	
	if (upper1) {
	    if (Math.floor(Place3 * 10 + .25) > Place3 * 10) upper2 = true;
	    else upper2 = false;
	} else {
	    if (Math.ceil(Place3 * 10 - .25) > Place3 * 10) upper2 = true;
	    else upper2 = false;
	}
	
	if (upper1 && upper2) distance = Math.floor(distance * 10) + 1;
	else if ((upper1 && !upper2) || (!upper1 && upper2)) distance = Math.floor(distance * 10) + .5;
	else if (!upper1 && !upper2) distance = Math.floor(distance * 10);
	
	distance /= 10;
	
    }
    
    public void calculateRPM(double inputDistance) {
	targetRPM = 333.33*inputDistance + 850.63;
    }

    public void warmUp() {
	
	shooterMotor.set(.3);
	
    }
    
}
