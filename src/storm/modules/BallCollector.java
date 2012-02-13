package storm.modules;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.IBallCollector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Driver
 */
public class BallCollector implements IBallCollector {
    
    SpeedController feederMotor, kanayerBeltMotor;
    DigitalInput in1,in2, ready;
    boolean manual;
    boolean tripped1, tripped2, readytripped;
    
    public final double SPEED_FORWARD = 0.5,
	    SPEED_BACKWARD = -0.5,
	    SPEED_OFF = 0.0;
          
    public BallCollector(int Feeder,int KanayerBelt, int IRin1, int IRin2, int IRready) {
        feederMotor = new Victor(Feeder);
        kanayerBeltMotor = new Victor(KanayerBelt);      
        in1 = new DigitalInput(IRin1);
        in2 = new DigitalInput(IRin2);
        ready = new DigitalInput(IRready);
        tripped1 = false;
        tripped2 = false;
        readytripped = false;
        
    }
 
    public void startCollecting(double speed) {
        startCollecting(speed, true);
    }
    
    private void startCollecting(double speed, boolean isManual) {
	feederMotor.set(speed);
        kanayerBeltMotor.set(speed);
        manual = isManual;
    }

    public void stopCollecting() {
	stopCollecting(true);
    }
    
    private void stopCollecting(boolean isManual) {
        feederMotor.set(SPEED_BACKWARD);
        kanayerBeltMotor.set(SPEED_OFF);
        manual = isManual;
    }

    public int getNumBalls() {
        return RobotState.BALL_CONTAINMENT_COUNT;
    }

    public void run() {
	
        if (!in1.get() == true && !tripped1) {
            RobotState.BALL_CONTAINMENT_COUNT ++;
            tripped1 = true;
        }else if(!in1.get() == false && tripped1){
            tripped1 = false;
        }
        if (!in2.get() == true && !tripped2){
            RobotState.BALL_CONTAINMENT_COUNT ++;
            tripped2 = true;
        }else if(!in2.get() == false && tripped2){
            tripped2 = false;
        }
        if (manual) return;
        if (RobotState.BALL_CONTAINMENT_COUNT == 3){
            stopCollecting(false);
        }
        if (RobotState.BALL_CONTAINMENT_COUNT >= 3 && ((!in1.get() == true || !in2.get() == true))){
            feederMotor.set(SPEED_BACKWARD);
            kanayerBeltMotor.set(SPEED_BACKWARD);   
        } 
	startCollecting(SPEED_FORWARD, false);
    }

    public void returnControl() {
        manual = false;
    }   
}
