/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.logic;

import storm.RobotState;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IRobotLogic;
import storm.modules.BridgeManipulator;

/**
 *
 * @author Storm
 */
public class BridgeManip3BATest implements IRobotLogic {
    
    IBridgeManipulator bridgeManipulator = new BridgeManipulator(RobotState.PORT_MOTOR_BRIDGE_MANIPULATOR);

    public void doInit() {
    }

    public void doContinuous() {
    }

    public void doPeriodic() {
    }

    public void doEnd() {
    }
    
}
