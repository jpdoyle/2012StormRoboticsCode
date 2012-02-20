package storm.modules;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import storm.RobotState;
import storm.interfaces.IShooter;
import storm.utility.Print;
/**
 *
 * @author Storm
 */

public class Shooter implements IShooter {
    SpeedController shooterMotor, transferMotor;
    DigitalInput ready, hallEffect;
    Counter counter;
    boolean shooting,
	    readyTripped;
    double motorSpeed,
	    calculatedMotorSpeed,
	    wantedRPM,
	    period,
	    RPMcurrent,
	    RPMdifference,
	    RPMthreshold;
    int state,
	    timeDifference,
	    currentTime,
	    goodRangeCount,
	    debugCounter,
	    modFactor;
       
    public Shooter(int shooterMotorChannel,int transferMotorChannel, int IRready, int hallEffectSensor) {
        
        shooterMotor = new Jaguar(shooterMotorChannel);
        transferMotor = new Victor(transferMotorChannel);
        ready = new DigitalInput(IRready);
        hallEffect = new DigitalInput(hallEffectSensor);
	readyTripped = false;
        counter = new Counter(EncodingType.k1X, hallEffect, hallEffect, false);
        counter.clearDownSource();
        counter.setUpSourceEdge(true, false);
    }
    
    public void startShoot(double distance) {
	motorSpeed = getMotorSpeed(distance);
        counter.start();
        state = 0;
	debugCounter = 0;
        shooting = true;
	goodRangeCount = 0;
	modFactor = 10;
	startTime = System.currentTimeMillis();

    }
    
    long startTime = -1;

    public void doShoot() {
        // set motor speed, check when ready, move ball into shooter, stop once IR sensor is clear
	if (!shooting) return;
	switch (state){
	    case 0:
		transferMotor.set(-1);
		shooterMotor.set(motorSpeed);
		startTime = System.currentTimeMillis();
		state ++;
		
		break;
	    case 1:
		if (!ready.get() == true){
		    transferMotor.set(0);
		    state ++;}
		break ;
	    case 2:
		if (checkRPM() == true){
		    state ++;
		}
		break;
	    case 3:
		transferMotor.set(-1);
		if (!ready.get() == false) {
		    startTime = System.currentTimeMillis();
		    state ++;
		}
		break;
	    case 4:
		if ((System.currentTimeMillis() - startTime) >= 2000){
		    state ++;
		}
		break;
	    case 5:
		endShoot();
		RobotState.BALL_CONTAINMENT_COUNT --;
		break;
	    default:
		break;
	}
    }

    private double getMotorSpeed(double distance) {
        //convert distance into rpm into motor speed value
	wantedRPM = 333.33*distance + 850.63 ;
	calculatedMotorSpeed = .0003*wantedRPM + 0.0457;
	return calculatedMotorSpeed;
    }
    
    private boolean checkRPM(){
	//check what the current RPM is
	
	period = counter.getPeriod();
	if ((System.currentTimeMillis() - startTime) >= 10000)
	{
	    return true;
	}
	debugCounter ++;
	if (debugCounter % modFactor != 0)
	{
	    return false;
	}
	
	if (Double.isInfinite(period) || period <= 0)
	{
	    return false;
	}
	
	RPMcurrent = 60/period;
	if (RPMcurrent > 3500) return false;
	if (RPMcurrent > 1200) modFactor = 5;
	else modFactor = 10;
	
	RPMthreshold = wantedRPM / 100;	
	RPMdifference = wantedRPM - RPMcurrent;
	
	motorSpeed += .00003*RPMdifference;
	
	if (motorSpeed <0) motorSpeed = 0;
	if (motorSpeed >1) motorSpeed = 1;
	
	shooterMotor.set(motorSpeed);
	    	
	if (Math.abs(RPMdifference) < RPMthreshold)
	{
	    goodRangeCount ++;
	}else goodRangeCount = 0;
	
	if(goodRangeCount > 10)
	{
	    return true;
	}else return false;
    }
    
    private void endShoot(){
	shooterMotor.set(0);
	transferMotor.set(0);
	state = 0;
	shooting = false;
	debugCounter = 0;
    }

    public boolean isShooting() {
        return shooting;
    }

}
