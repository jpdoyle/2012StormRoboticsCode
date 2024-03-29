package storm.logic;

import edu.wpi.first.wpilibj.RobotBase;
import storm.RobotState;
import storm.interfaces.IRobotLogic;
import storm.modules.BallController;

public class BallControllerTest implements IRobotLogic {
    
    BallController ballController;

    public void doInit() {
	
	ballController = RobotState.ballController;
	
    }

    public void doContinuous() {
	ballController.runContinuous();
    }

    public void doPeriodic() {
	ballController.runPeriodic(
		RobotState.joystickShoot.getRawAxis(6),
		false,
		RobotState.joystickShoot.getRawButton(6), 0);
    }

    public void doEnd() {
    }
    
}
