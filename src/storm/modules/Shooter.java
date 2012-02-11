package storm.modules;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.interfaces.IShooter;
/**
 *
 * @author Storm
 */

public class Shooter implements IShooter {

    SpeedController shooterMotor, transferMotor;
    DigitalInput ready;
    boolean shooting;
    double motorSpeed;
    int state;
       
    public Shooter(int shooterMotorChannel,int transferMotorChannel, int IRready) {
        
        shooterMotor = new Victor(shooterMotorChannel);
        transferMotor = new Victor(transferMotorChannel);
        ready = new DigitalInput(IRready);
    }
    
    public void startShoot(double distance) {
        //find out speed motor needs, move ball until ready to shoot,and start shooting process
        motorSpeed = getMotorSpeed(distance);
        if (!ready.get() == false){
            transferMotor.set(1);
        }
        state = 0;
        shooting = true;
    }

    public void doShoot() {
        if (!shooting) return;
        
        // set motor speed, check when ready, move ball into shooter, stop once IR sensor is clear
        shooterMotor.set(motorSpeed);
        if (shooterMotor.get() == motorSpeed){
          state ++;  
        }
        if (state == 1){
            transferMotor.set(1);        
        }
        if (!ready.get() == false){
            transferMotor.set(0);
            shooterMotor.set(0);
            shooting = false;
            state = 0;
        }
    }
    
    private double getMotorSpeed(double velocity) { 
        //convert velocity from m/s into rpm into motor speed value     
        velocity = velocity * 4;
        return velocity;
    }    
}
