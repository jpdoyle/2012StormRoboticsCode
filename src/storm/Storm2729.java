package storm;

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import storm.interfaces.IRobotLogic;
import storm.logic.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Storm2729 extends IterativeRobot {
    
    Watchdog watchdog;
    
    IRobotLogic hybrid;
    IRobotLogic teleop;
    
    //***** This is called when the robot is first turned on *****//
    
    public void robotInit() {
	
	watchdog = Watchdog.getInstance();
	
	RobotState.compressor.start();
	
	//autonomous = new THING
        hybrid = new Hybrid();
	teleop = new Teleop();
	
	sendDefaultDashboardInfo();
	

    }
    
    //***** AUTONOMOUS FUNCTIONS *****//
    
    // Called at the beginning of autonomous
    public void autonomousInit() {
	watchdog.feed();
	hybrid.doInit();
    }

    // Called continuously during autonomous
    public void autonomousContinuous() {
	watchdog.feed();
	hybrid.doContinuous();
    }
    
    // Called periodically during autonomous
    public void autonomousPeriodic() {
	watchdog.feed();
	hybrid.doPeriodic();
	sendDashboardInfo();
    }
    
    //***** TELEOP FUNCTIONS *****//

    // Called at the beginning of teleop
    public void teleopInit() {
	watchdog.feed();
	teleop.doInit();
    }

    // Called continuously during autonomous
    public void teleopContinuous() {
	watchdog.feed();
	teleop.doContinuous();
    }
    
    // Called periodically during teleop
    public void teleopPeriodic() {
	watchdog.feed();
        teleop.doPeriodic();
	sendDashboardInfo();
    }
    
    // End Functions

    public void disabledPeriodic() {
	hybrid.doEnd();
	teleop.doEnd();
	sendDashboardInfo();
    }

    public void disabledContinuous() {
	hybrid.doEnd();
	teleop.doEnd();
    }

    public void disabledInit() {
	hybrid.doEnd();
	teleop.doEnd();
    }
    
    private void sendDefaultDashboardInfo() {
	RobotState.DASHBOARD_FEEDBACK.putString("gear", "Unknown");
	RobotState.DASHBOARD_FEEDBACK.putString("distance.mode", "Unknown");
	RobotState.DASHBOARD_FEEDBACK.putDouble("distance.offset", Double.NaN);
	RobotState.DASHBOARD_FEEDBACK.putInt("shooter.rpm", -1);
	RobotState.DASHBOARD_FEEDBACK.putInt("ball.count", -1);
	RobotState.DASHBOARD_FEEDBACK.putDouble("target.distance", Double.NaN);
	RobotState.DASHBOARD_FEEDBACK.putString("tracker.state", "Unknown");
    }
    
    private void sendDashboardInfo() {
	String gear = (RobotState.driveTrain.isHighGear()) ? "High Gear" : "Low Gear";
	RobotState.DASHBOARD_FEEDBACK.putString("gear", gear);
	
	RobotState.DASHBOARD_FEEDBACK.putInt("shooter.rpm", ((int)(RobotState.shooter.getRPM())));
	
	RobotState.DASHBOARD_FEEDBACK.putInt("ball.count", RobotState.BALL_CONTAINMENT_COUNT);
	
	String trackerState = (RobotState.TARGET_TRACKER_IS_TRACKING) ? "On" : "Off";
	RobotState.DASHBOARD_FEEDBACK.putString("tracker.state", trackerState);
    }
    
}