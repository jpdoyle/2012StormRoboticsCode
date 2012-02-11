/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.utility;

import edu.wpi.first.wpilibj.*;

/**
 *
 * @author Awesome
 */

public class RobotTurner {

    static final double PID_P = 0,PID_I = 0,PID_D = 0;

    PIDOutput turn_ = new PIDOutput() {
	public void pidWrite(double output) {

	}
    };
    PIDController pid_ = new PIDController(PID_P,PID_I,PID_D,new PIDSource() {
	public double pidGet() { return 0; }
    }, turn_);

    //Gyro gyro;

    public void enable() {
        pid_.enable();
    }

    public void disable() {
        pid_.disable();
    }

    public void setAngle(double angle) {
        pid_.setSetpoint(angle);
    }

}
