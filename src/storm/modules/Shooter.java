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
    DigitalInput transfer, ready;
    boolean shooting;
    double motorSpeed;
    int state;
       
    public Shooter(int shooterMotorChannel,int transferMotorChannel, int IRtransfer, int IRready) {
        
        shooterMotor = new Victor(shooterMotorChannel);
        transferMotor = new Victor(transferMotorChannel);
        transfer = new DigitalInput(IRtransfer);
        ready = new DigitalInput(IRready);
        int state = 0;
    }
    
    public void startShoot(double velocity) {
        //find out speed motor needs, move ball until ready to shoot,and start shooting process
        motorSpeed = getMotorSpeed(velocity);
        if (!ready.get() == false){
            transferMotor.set(1);
        }
        shooting = true;
    }

    public void doShoot(int state) {
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

    public boolean isShooting() {
        return shooting;
    }
    
    private double getMotorSpeed(double velocity) { 
        //convert velocity from m/s into rpm into motor speed value     
        velocity = velocity * 4;
        return velocity;
    }    
}
