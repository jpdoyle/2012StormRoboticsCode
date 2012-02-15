package storm.modules;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.IShooter;
/**
 *
 * @author Storm
 */

public class Shooter implements IShooter {

    SpeedController shooterMotor, transferMotor;
    DigitalInput ready, hallEffect;
    Counter counter;
    boolean shooting, readyTripped;
    double motorSpeed, wantedRPM, period, RPM;
    double [][] RPMtoMotorSpeed;
    int state;
       
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
	motorSpeed = getMotorSpeed(velocity);
        //find out speed motor needs, move ball until ready to shoot,and start shooting process
        counter.start();
        state = 0;
        shooting = true;
    }

    public void doShoot() {
        // set motor speed, check when ready, move ball into shooter, stop once IR sensor is clear
	if (!shooting) return;
	if (state == 0){
	  transferMotor.set(-1);
	}
	if (state == 0 && !ready.get() == true){
	  transferMotor.set(0);
	  shooterMotor.set(motorSpeed);
	}      
        if (checkRPM() == wantedRPM && state == 1){
          state ++;  
        }
        if (state == 2){
            transferMotor.set(-1);        
        }
	if (!ready.get() == true && !readyTripped) {
            RobotState.BALL_CONTAINMENT_COUNT --;
            readyTripped = true;
        }else if(!ready.get() == false && readyTripped){
            readyTripped = false;
        }
        if (!ready.get() == false && state == 2){
            state ++;
        }if (state == 3){
	    transferMotor.set(0);
            shooterMotor.set(0);
            shooting = false;
            state = 0;
	}
    }

    private double getMotorSpeed(double velocity) {
        //convert velocity from m/s into rpm into motor speed value
	/*RPMtoMotorSpeed[1][0] = 720;
	RPMtoMotorSpeed[1][1] = .2;
	RPMtoMotorSpeed[2][0] = 1080;
	RPMtoMotorSpeed[2][1] = .3;*/
        velocity = 1;
	wantedRPM = 2000;
        return velocity;
    }
    
    private double checkRPM(){
	//check what the current RPM is
	period = counter.getPeriod();
        RPM = 60/period;
	return RPM;
    }

}
