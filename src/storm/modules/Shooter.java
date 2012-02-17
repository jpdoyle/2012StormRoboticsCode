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
    Timer timer;
    boolean shooting, readyTripped, closeEnough;
    double motorSpeed, wantedRPM, period, RPM, RPMdifference;
    double [][] RPMtoMotorSpeed;
    int state;
       
    public Shooter(int shooterMotorChannel,int transferMotorChannel, int IRready, int hallEffectSensor) {
        
        shooterMotor = new Victor(shooterMotorChannel);
        transferMotor = new Victor(transferMotorChannel);
        ready = new DigitalInput(IRready);
        hallEffect = new DigitalInput(hallEffectSensor);
	timer = new Timer();
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
	motorSpeed = getMotorSpeed(velocity);
        //find out speed motor needs, move ball until ready to shoot,and start shooting process
        counter.start();
        state = 0;
        shooting = true;
    }

    public void doShoot() {
        // set motor speed, check when ready, move ball into shooter, stop once IR sensor is clear
	if (!shooting) return;
	switch (state){
	    case 0:
		transferMotor.set(-1);
		if (!ready.get() == true){
		    transferMotor.set(0);
		    shooterMotor.set(motorSpeed);
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
		if (!ready.get() == true && !readyTripped) {
		    RobotState.BALL_CONTAINMENT_COUNT --;
		    readyTripped = true;
		}else if(!ready.get() == false && readyTripped){
		    readyTripped = false;
	    try {
		timer.wait(1000);
		state ++;
	    } catch (InterruptedException ex) {
	    }	   
		}
		break;
	    case 3:
		transferMotor.set(0);
		shooterMotor.set(0);
		shooting = false;
		state = 0;
		break;
	}
    }

    private double getMotorSpeed(double velocity) {
        //convert velocity from m/s into rpm into motor speed value
	/*RPMtoMotorSpeed[1][0] = 720;
	RPMtoMotorSpeed[1][1] = .2;
	RPMtoMotorSpeed[2][0] = 1080;
	RPMtoMotorSpeed[2][1] = .3;*/	
        velocity = 1; //<-motorSpeed valuse right now *NOT ACTUAL VELOCITY*
	wantedRPM = velocity * 94.13; //needs actual velocity
        return velocity;
    }
    
    private boolean checkRPM(){
	//check what the current RPM is
	period = counter.getPeriod();
        RPM = 60/period;
	RPMdifference = RPM - wantedRPM;
	if (RPMdifference >= -10 && RPMdifference <= 10){
	    closeEnough = true;
	}else closeEnough = false;
	Print.getInstance().setLine(1, "RPM: " + RPM);	
	return closeEnough;	

    }

    public boolean isShooting() {
        return shooting;
    }

}
