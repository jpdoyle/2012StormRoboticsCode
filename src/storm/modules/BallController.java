package storm.modules;

import storm.RobotState;
import storm.interfaces.IBallCollector;
import storm.interfaces.IShooter;

public class BallController {
    
    IBallCollector ballCollector;
    IShooter shooter;
    
    boolean shootTrigger;
    
    public BallController(IBallCollector ballCollector, IShooter shooter) {
	this.ballCollector = ballCollector;
	this.shooter = shooter;
	
	shootTrigger = false;
    }
    
    public void runContinuous() {
	ballCollector.run();
	shooter.doShoot();
    }
    
    public void runPeriodic(double dPadValue, boolean shootButton, double distance) {
	manual(dPadValue);
	
	if (shootButton && !shootTrigger) {
	    shootTrigger = true;
	    shooter.startShoot(distance);
	} else if (!shootButton && shootTrigger) {
	    shootTrigger = false;
	}
	
    }
    
    private void manual(double dPad) {
	
	if (dPad > 0) {
	    ballCollector.start(IBallCollector.DIRECTION_UP);
	} else if (dPad < 0) {
	    ballCollector.start(IBallCollector.DIRECTION_DOWN);
	    shooter.endShoot();
	} else {
	    ballCollector.stop();
	}
	
    }
    
}
