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
    
    public void runPeriodic(double dPadValue, boolean overrideButton, boolean shootButton) {
	if (overrideButton || shooter.isShooting()) {
	    manual(dPadValue);
	} else {
	    automatic();
	}
	
	if (shootButton && !shootTrigger) {
	    shootTrigger = true;
	    shooter.startShoot(0.0);
	} else if (!shootButton && shootTrigger) {
	    shootTrigger = false;
	}
	
    }
    
    private void manual(double dPad) {
	
	if (dPad > 0) {
	    ballCollector.start(IBallCollector.DIRECTION_UP);
	} else if (dPad < 0) {
	    ballCollector.start(IBallCollector.DIRECTION_DOWN);
	} else {
	    ballCollector.stop();
	}
	
    }
    
    private void automatic() {
	
	if (!ballCollector.isRunning() && RobotState.BALL_CONTAINMENT_COUNT < 3) {
	    ballCollector.start(IBallCollector.DIRECTION_UP);
	} else if (RobotState.BALL_CONTAINMENT_COUNT >= 3) {
	    ballCollector.stop();
	}
	
    }
    
}
