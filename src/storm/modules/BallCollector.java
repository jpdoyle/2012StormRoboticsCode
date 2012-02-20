package storm.modules;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.RobotState;
import storm.interfaces.IBallCollector;
import storm.utility.Print;

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
    DigitalInput in1, in2;
    boolean isRunning, tripped1, tripped2, readytripped;
    int loopCounterSensor1, loopCounterSensor2;
    
    private final double MOTOR_SPEED = 1.0;
    private final double BOTTOM_FEEDER_MODIFIER = 0.4;
    
    public BallCollector(int feederMotorChannel, int kanayerBeltMotorChannel, int bottomIR1Channel, int bottomIR2Channel) {
	feederMotor = new Victor(feederMotorChannel);
	kanayerBeltMotor = new Victor(kanayerBeltMotorChannel);
	in1 = new DigitalInput(bottomIR1Channel);
	in2 = new DigitalInput(bottomIR2Channel);
    }

    public void start(int direction) {
	feederMotor.set(direction * MOTOR_SPEED * BOTTOM_FEEDER_MODIFIER);
	kanayerBeltMotor.set(direction * MOTOR_SPEED);
	isRunning = true;
    }

    public void stop() {
	feederMotor.set(0.0);
	kanayerBeltMotor.set(0.0);
	isRunning = false;
    }

    public boolean isRunning() {
	return isRunning;
    }

    public void run() {
	
        if (!in1.get() == true) {
           if (loopCounterSensor1 < 0) {
	       RobotState.BALL_CONTAINMENT_COUNT ++;
	   }  
	   loopCounterSensor1 ++;
	} else {
	   loopCounterSensor1 = -1;
	}
	
        if (!in2.get() == true) {
           if (loopCounterSensor2 < 0){
	       RobotState.BALL_CONTAINMENT_COUNT ++;
	   }  
	   loopCounterSensor2 ++;
	} else {
	   loopCounterSensor2 = -1;
	}
	
	RobotState.DASHBOARD_FEEDBACK.putInt("ball.count", RobotState.BALL_CONTAINMENT_COUNT);
	
    }
}
