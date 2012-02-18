package storm.logic;

import edu.wpi.first.wpilibj.Joystick;
import storm.RobotState;
import storm.interfaces.*;

public class Teleop implements IRobotLogic {
    
    // Modules
    IDriveTrain driveTrain;
    ITargetTracker targetTracker;
    IShooter shooter;
    IBallCollector kanayerBelt;
    Joystick driveJoystick;
    Joystick shootJoystick;
    
    // Allowed booleans
    boolean driveAllowed;
    
    boolean isAimed;
    
    //Button booleans
    boolean[] buttonPressed;

    public void doInit() {
        driveJoystick = RobotState.joystickDrive;
        shootJoystick = RobotState.joystickShoot;
        
        driveAllowed = true;
        
        isAimed = false;
        
        buttonPressed = new boolean[RobotState.NUM_BUTTONS];
        for (int i = 0; i < buttonPressed.length; i++) {
            buttonPressed[i] = false;
        }
    }

    public void doContinuous() {
        
        isAimed = targetTracker.isAimed();
        
        shooter.doShoot();
        
        kanayerBelt.run();
        
    }

    public void doPeriodic() {
        
        // Aim robot
        if (buttonPressed(RobotState.JOYSTICK_BUTTON_AUTO_AIM)) {
            setButtonState(RobotState.JOYSTICK_BUTTON_AUTO_AIM, true);
            targetTracker.startLocking();
            driveAllowed = false;
        } else if (buttonReleased(RobotState.JOYSTICK_BUTTON_AUTO_AIM)) {
            setButtonState(RobotState.JOYSTICK_BUTTON_AUTO_AIM, false);
            targetTracker.stopLocking();
            driveAllowed = true;
        }
        
        // Shoot shooter
        if (buttonPressed(RobotState.JOYSTICK_BUTTON_SHOOT)) {
            setButtonState(RobotState.JOYSTICK_BUTTON_SHOOT, true);
            if (isAimed)
                shooter.startShoot(targetTracker.getDistance());
        } else if (buttonReleased(RobotState.JOYSTICK_BUTTON_SHOOT)) {
            setButtonState(RobotState.JOYSTICK_BUTTON_SHOOT, false);
        }
        
        // Drive if robot is not aiming
        if (driveAllowed) {
            double leftYValue = driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_LEFT);
            double rightYValue = driveJoystick.getRawAxis(RobotState.JOYSTICK_AXIS_DRIVE_RIGHT);
            driveTrain.drive(leftYValue, rightYValue);
        }
        
    }
    
    // Button pressed once
    private boolean buttonPressed(int button) {
        return shootJoystick.getRawButton(button) && !getButtonState(button);
    }
    
    // Button released
    private boolean buttonReleased(int button) {
        return !shootJoystick.getRawButton(button) && getButtonState(button);
    }
    
    // Get button state from array
    private boolean getButtonState(int button) {
        return buttonPressed[button-1];
    }
    
    // Set button state in array
    private void setButtonState(int button, boolean state) {
        buttonPressed[button-1] = state;
    }

    public void doEnd() {
    }
    
}
