/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.modules;
import storm.interfaces.IMotorEncoder;
//import storm.interfaces.SpeedController;

/**
 *
 * @author Developer
 */
public class MotorEncoder implements IMotorEncoder {

    public void resetDistance() {
        DriveTrain.leftEncoder.reset();
        DriveTrain.rightEncoder.reset();
    }

    public double getDistance(int oneIsLeftTwoIsRight) {
        if (oneIsLeftTwoIsRight == 1) return DriveTrain.leftEncoder.getDistance();
        else if(oneIsLeftTwoIsRight == 2) return DriveTrain.rightEncoder.getDistance();
        else return 0;
    }

}
