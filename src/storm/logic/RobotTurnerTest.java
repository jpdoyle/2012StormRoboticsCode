/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.logic;

import storm.RobotState;
import storm.interfaces.IRobotLogic;

/**
 *
 * @author joe
 */
public class RobotTurnerTest implements IRobotLogic {

    public void doInit() {
        RobotState.targetTracker.startTracking();
        RobotState.targetTracker.startLocking();
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
    }

    public void doEnd() {
        RobotState.targetTracker.stopTracking();
        RobotState.targetTracker.startLocking();
    }

}
