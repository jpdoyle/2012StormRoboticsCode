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
    DigitalInput limitSwitchBottom;
    DigitalInput limitSwitchTop;
    
    private final double MOTOR_SPEED_DOWN = 0.35;
    private final double MOTOR_SPEED_UP   = 0.45;
    
    public ThreeBA(int motorChannel, int limitSwitchTopChannel, int limitSwitchBottomChannel) {
	wormDrive = new Victor(motorChannel);
	limitSwitchBottom = new DigitalInput(limitSwitchBottomChannel);
	limitSwitchTop = new DigitalInput(limitSwitchTopChannel);
    }

    public void raise(double absValJoystick) {
//        if (limitSwitchTop.get() == false)
            wormDrive.set(absValJoystick * MOTOR_SPEED_UP);
//	else
//            stop();
    }

    public void stop() {
        wormDrive.set(0.0);
    }

    public void lower(double absValJoystick) {
//        if (limitSwitchBottom.get() == false)
	    wormDrive.set(-MOTOR_SPEED_DOWN);
//	else
//            stop();
    }

}
