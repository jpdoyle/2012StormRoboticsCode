package storm.modules;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.interfaces.IShooter;
/**
 *
 * @author Storm
 */


public class Shooter implements IShooter {

    SpeedController motor;
       
    
    public Shooter(int motorChannel) {
        
        motor = new Victor(motorChannel);
  
        
        
    }
    
    public void Shoot(double velocity) {
        
        motor.set(velocity);
               
    }
    
    
    
}
