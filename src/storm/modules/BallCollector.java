package storm.modules;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
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
    
    SpeedController bottomMotor1, bottomMotor2;
    DigitalInput in1,in2, ready;
    int ballCount;
    boolean manual;
    boolean tripped1, tripped2, readytripped;
          
    public BallCollector(int bottomMotorChannel1,int bottomMotorChannel2, int IRin1, int IRin2, int IRready) {
        bottomMotor1 = new Victor(bottomMotorChannel1);
        bottomMotor2 = new Victor(bottomMotorChannel2);      
        in1 = new DigitalInput(IRin1);
        in2 = new DigitalInput(IRin2);
        ready = new DigitalInput(IRready);
        tripped1 = false;
        tripped2 = false;
        readytripped = false;
        
    }
 
    public void startCollecting(double direction) {
        bottomMotor1.set(direction);
        bottomMotor2.set(direction);
        manual = true;
    }

    public void stopCollecting() {
        bottomMotor1.set(0);
        bottomMotor2.set(0);
        manual = true;
    }

    public int getNumBalls() {
        return ballCount;
    }

    public void run() {
        if (!in1.get() == true && !tripped1) {
            ballCount ++;
            tripped1 = true;
        }else if(!in1.get() == false && tripped1){
            tripped1 = false;
        }
        if (!in2.get() == true && !tripped2){
            ballCount ++;
            tripped2 = true;
        }else if(!in2.get() == false && tripped2){
            tripped2 = false;
        }
        if (!ready.get() == true && !readytripped) {
            ballCount --;
            readytripped = true;
        }else if(!ready.get() == false && readytripped){
            readytripped = false;
        }
        if (manual) return;
        if (ballCount == 3){
            stopCollecting();
        }
        if (ballCount >= 3 && ((!in1.get() == true || !in2.get() == true))){
            bottomMotor1.set(-1);
            bottomMotor2.set(-1);
        }
    }

    public void returnControl() {
        manual = false;
    }   
}
