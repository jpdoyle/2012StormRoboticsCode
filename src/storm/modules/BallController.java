package storm.modules;

import storm.RobotState;
import storm.interfaces.IBallCollector;
import storm.interfaces.IShooter;

public class BallController {
    
    IBallCollector ballCollector;
    IShooter shooter;
    
    boolean preShootTrigger;
    boolean shootTrigger;
    
    public BallController(IBallCollector ballCollector, IShooter shooter) {
	this.ballCollector = ballCollector;
	this.shooter = shooter;
	
	preShootTrigger = false;
	shootTrigger = false;
    }
    
    public void runContinuous() {
	ballCollector.run();
	shooter.doShoot();
    }
    
    public void runPeriodic(double dPadValue, boolean preShoot, boolean shootButton, double distance) {
	manual(dPadValue);
	
	if (preShoot && !preShootTrigger) {
	    preShootTrigger = true;
	    shooter.preShoot();
	} else if (!preShoot && preShootTrigger) {
	    preShootTrigger = false;
	}
	
	if (shootButton && !shootTrigger) {
	    shootTrigger = true;
	    shooter.startShoot(false, distance);
	} else if (!shootButton && shootTrigger) {
	    shootTrigger = false;
	}
	
    }
    
    public void warmupShooter() {
	shooter.warmUp();
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
