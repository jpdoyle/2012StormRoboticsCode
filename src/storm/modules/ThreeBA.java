package storm.modules;

/**
 * @author Gabe Casciato
 */

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import storm.interfaces.I3BA;

public class ThreeBA implements I3BA {
    
    SpeedController wormDrive;
    DigitalInput limitSwitchBack;
    DigitalInput limitSwitchFront;
    
    private final double MOTOR_SPEED = 0.25;
    
    public ThreeBA(int motorChannel, int limitSwitchFrontChannel, int limitSwitchBackChannel) {
	wormDrive = new Victor(motorChannel);
	limitSwitchBack = new DigitalInput(limitSwitchBackChannel);
	limitSwitchFront = new DigitalInput(limitSwitchFrontChannel);
    }

    public void extend() {
        if (limitSwitchFront.get() == false)
            wormDrive.set(MOTOR_SPEED);
	else
            stop();
    }

    public void stop() {
        wormDrive.set(0.0);
    }

    public void retract() {
        if (limitSwitchBack.get() == false)
	    wormDrive.set(-MOTOR_SPEED);
	else
            stop();
    }

}
