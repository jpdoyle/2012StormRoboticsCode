/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.logic;

import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.interfaces.ITargetTracker;
import storm.utility.Print;

/**
 *
 * @author joe
 */
public class TrackerTest implements IRobotLogic {
    Print print = Print.getInstance();
    IDriveTrain drive = RobotState.driveTrain;
    ITargetTracker tracker = RobotState.targetTracker;
    
    public void doInit() {
        tracker.startTracking();
    }

    public void doContinuous() {
        boolean aimed = tracker.isAimed();
        print.setLine(0,aimed ? "Aimed" : "Not Aimed");
        if(aimed)
            print.setLine(1,"Z: " + tracker.getDistance());
    }

    public void doPeriodic() {
    }

    public void doEnd() {
        tracker.stopTracking();
    }
}
