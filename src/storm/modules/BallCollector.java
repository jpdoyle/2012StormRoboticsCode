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
    DigitalInput in1,in2, ready;
    boolean manual, tripped1, tripped2, readytripped;
    int loopCounterSensor1, loopCounterSensor2;
    
    public final int SPEED_FORWARD = -1,
	    SPEED_BACKWARD = 1,
	    SPEED_OFF = 0;
    
    Print printer = Print.getInstance();
          
    public BallCollector(int Feeder,int KanayerBelt, int IRin1, int IRin2, int IRready)
    {
        feederMotor = new Victor(Feeder);
        kanayerBeltMotor = new Victor(KanayerBelt);      
        in1 = new DigitalInput(IRin1);
        in2 = new DigitalInput(IRin2);
        ready = new DigitalInput(IRready);
        tripped1 = false;
        tripped2 = false;
        readytripped = false;
        
    }
 
    public void startCollecting(int direction)
    {
        startCollecting(direction, true);
    }
    
    private void startCollecting(int direction, boolean isManual)
    {
	feederMotor.set(direction * 0.8);
        kanayerBeltMotor.set(direction * 1.0);
        manual = isManual;
    }

    public void stopCollecting() 
    {
	stopCollecting(true);
    }
    
    private void stopCollecting(boolean isManual) 
    {
        feederMotor.set(SPEED_OFF);
        kanayerBeltMotor.set(SPEED_OFF);
        manual = isManual;
    }

    public int getNumBalls() 
    {
        return RobotState.BALL_CONTAINMENT_COUNT;
    }

    public void run() 
    {
	
	printer.setLine(1, "IR 1 (R): " + !in1.get());
	printer.setLine(2, "IR 2 (L): " + !in2.get());
	
        if (!in1.get() == true)
	{
           if (loopCounterSensor1 <0){
	       RobotState.BALL_CONTAINMENT_COUNT ++;
	   }  
	    loopCounterSensor1 ++;
	}else
	{
	    loopCounterSensor1 = -1;
	}
        if (!in2.get() == true)
	{
           if (loopCounterSensor2 <0){
	       RobotState.BALL_CONTAINMENT_COUNT ++;
	   }  
	    loopCounterSensor2 ++;
	}else
	{
	    loopCounterSensor2 = -1;
	}
        if (manual) return;
        if (RobotState.BALL_CONTAINMENT_COUNT == 3 && (!in1.get() == false && !in2.get() == false))
	{
	    stopCollecting(false);
        }else
	{
	    startCollecting(SPEED_FORWARD, false);
	}
        if (RobotState.BALL_CONTAINMENT_COUNT > 3)
	{
            feederMotor.set(SPEED_BACKWARD);  
        }
	if (RobotState.joystickShoot.getRawButton(7)){
	    RobotState.BALL_CONTAINMENT_COUNT = 0;
	}
    }

    public void returnControl() 
    {
        manual = false;
    }   
}
