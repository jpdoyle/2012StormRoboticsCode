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

    Joystick shootJoystick;
    SpeedController shooterMotor, transferMotor;
    DigitalInput ready, hallEffect;
    Counter counter;
    boolean shooting, readyTripped, closeEnough;
    boolean btn7;
    double motorSpeed, wantedRPM, period, RPM, RPMdifference, RPMthreshold;
    double [][] RPMtoMotorSpeed;
    int state, timeDifference, currentTime;
       
    public Shooter(int shooterMotorChannel,int transferMotorChannel, int IRready, int hallEffectSensor) {
        
        shooterMotor = new Victor(shooterMotorChannel);
        transferMotor = new Victor(transferMotorChannel);
        ready = new DigitalInput(IRready);
        hallEffect = new DigitalInput(hallEffectSensor);
	readyTripped = false;
        counter = new Counter(EncodingType.k1X, hallEffect, hallEffect, false);
        counter.clearDownSource();
        counter.setUpSourceEdge(true, false);
	/*RPMtoMotorSpeed = new double[9][1];
	RPMtoMotorSpeed[0][0] = 360;
	RPMtoMotorSpeed[0][1] = .1;
	RPMtoMotorSpeed[1][0] = 720;
	RPMtoMotorSpeed[1][1] = .2;
	RPMtoMotorSpeed[2][0] = 1080;
	RPMtoMotorSpeed[2][1] = .3;
	RPMtoMotorSpeed[3][0] = 1440;
	RPMtoMotorSpeed[3][1] = .4;
	RPMtoMotorSpeed[4][0] = 1800;
	RPMtoMotorSpeed[4][1] = .5;
	RPMtoMotorSpeed[5][0] = 2160;
	RPMtoMotorSpeed[5][1] = .6;
	RPMtoMotorSpeed[6][0] = 2520;
	RPMtoMotorSpeed[6][1] = .7;
	RPMtoMotorSpeed[7][0] = 2880;
	RPMtoMotorSpeed[7][1] = .8;
	RPMtoMotorSpeed[8][0] = 3240;
	RPMtoMotorSpeed[8][1] = .9;
	RPMtoMotorSpeed[9][0] = 3600;
	RPMtoMotorSpeed[9][1] = 1;*/
    }
    
    public void startShoot(double velocity) {
		shootJoystick = RobotState.joystickShoot;

	motorSpeed = getMotorSpeed(velocity);
        //find out speed motor needs, move ball until ready to shoot,and start shooting process
        counter.start();
        state = 0;
        shooting = true;
    }
    
    long startTime = -1;

    public void doShoot() {
	checkRPM();
	shooterMotor.set(motorSpeed);
	if (shootJoystick.getRawButton(7) && !btn7) {
	    btn7 = true;
	    motorSpeed = motorSpeed + .1;
	} else if (!shootJoystick.getRawButton(7) && btn7) {
	    btn7 = false;
	}
	Print.getInstance().setLine(2, "Motor Speed: " + motorSpeed);

        // set motor speed, check when ready, move ball into shooter, stop once IR sensor is clear
	/*if (!shooting) return;
	switch (state){
	    case 0:
		transferMotor.set(-1);
		if (!ready.get() == true){
		    transferMotor.set(0);
		    shooterMotor.set(motorSpeed);
		    startTime = System.currentTimeMillis();
		    state ++;
		}
		break;
	    case 1:
		if (checkRPM() == true){
		    state ++;
		}
		break;
	    case 2:
		transferMotor.set(-1);
		if (!ready.get() == false) {
		    startTime = System.currentTimeMillis();
		    state ++;
		}
		break;
	    case 3:
		if ((System.currentTimeMillis() - startTime) >= 2000){
		    state ++;
		}
		break;
	    case 4:
		transferMotor.set(0);
		shooterMotor.set(0);
		shooting = false;
		state = 0;
		RobotState.BALL_CONTAINMENT_COUNT --;
		break;
	    default:
		break;
	}*/
    }

    private double getMotorSpeed(double velocity) {
        //convert velocity from m/s into rpm into motor speed value
	/*RPMtoMotorSpeed[1][0] = 720;
	RPMtoMotorSpeed[1][1] = .2;
	RPMtoMotorSpeed[2][0] = 1080;
	RPMtoMotorSpeed[2][1] = .3;*/	
        velocity = 0; //<-motorSpeed value right now *NOT ACTUAL VELOCITY*
	//wantedRPM = velocity * 94.13; //needs actual velocity
        wantedRPM = 2000;
	return velocity;
    }
    
    private boolean checkRPM(){
	//check what the current RPM is
	period = counter.getPeriod();
        RPM = 60/period;
	RPMdifference = RPM - wantedRPM;
	RPMthreshold = wantedRPM / 10;	
	Print.getInstance().setLine(1, "RPM: " + RPM);
	return false;
	/*
	if ((System.currentTimeMillis() - startTime) >= 3000)
	{
	    return true;
	}else return false;*/
	
	/*if (RPMdifference >= -RPMthreshold && RPMdifference <= RPMthreshold)
	{
	    closeEnough = true;
	}else closeEnough = false;
	return closeEnough;*/
    }

    public boolean isShooting() {
        return shooting;
    }

}
