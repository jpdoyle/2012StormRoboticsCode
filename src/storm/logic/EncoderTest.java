
package storm.logic;

import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;

public class EncoderTest implements IRobotLogic {

    IDriveTrain driveTrain = RobotState.driveTrain;
    
    public void doInit() {
	driveTrain.resetDistance();
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
	if (driveTrain.getDistance() <= 24) {
	    driveTrain.driveDirect(0.4, 0.4);
	} else {
	    driveTrain.driveDirect(0.0, 0.0);
	}
    }

    public void doEnd() {
    }
    
}
