/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.utility;

import edu.wpi.first.wpilibj.*;
import storm.interfaces.IDriveTrain;

/**
 *
 * @author Awesome
 */

public class RobotTurner {

    static final double PID_P = 0,PID_I = 0,PID_D = 0;

    Gyro gyro_;
    IDriveTrain drive_;


    PIDOutput turn_ = new PIDOutput() {
	public void pidWrite(double output) {
            //drive_.drive(output, -output);
	}
    };
    PIDSource source_ = /*gyro_;*/new PIDSource() {
	public double pidGet() { return 0; }
    };
    PIDController pid_ = new PIDController(PID_P,PID_I,PID_D,source_, turn_);
    
    public RobotTurner(IDriveTrain train,int gyroChannel) {
        gyro_ = new Gyro(gyroChannel);
    }

    public void enable() {
        pid_.enable();
    }

    public void disable() {
        pid_.disable();
    }

    public void setAngle(double angle) {
        pid_.setSetpoint(angle);
    }
    
    public double getGyroAngle() {
        return 0;//gyro_.getAngle();
    }

}

