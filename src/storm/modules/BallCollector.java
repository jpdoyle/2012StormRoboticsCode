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
    
    SpeedController motor;
    DigitalInput in, out, ready;
    int ballCount;
    
 //need to ask about another IR sensor for whether the ball actually goes in   
    
    public BallCollector(int motorChannel, int IR1, int IR2, int IR3) {
        motor = new Victor(motorChannel);
        in = new DigitalInput(IR1);
        out = new DigitalInput(IR2);
        ready = new DigitalInput(IR3);
        
    }

    public void startCollecting() {
        motor.set(1.0);
    }

    public void stopCollecting() {
        motor.set(0);
    }

    public void addBall() {
        if (in.get() == true){
            ballCount ++ ;
        }
    }

    public void removeBall() {
        if (out.get() == true){
            ballCount -- ;
        }
    }

    public int getNumBalls() {
        return ballCount;
    }
    
}
