package storm;


import edu.wpi.first.wpilibj.Joystick;

public class RobotState {
    
    /*
    private static RobotState instance;
    
    public static RobotState getInstance() {
	if (instance == null)
	    instance = new RobotState();
	return instance;
    }
    */
    
    private RobotState() {}
    
    //***** GLOBAL CHANNEL CONSTANTS *****//
    
    // Motors
    public static final int PORT_MOTOR_DRIVE_LEFT_LOW   = 1;
    public static final int PORT_MOTOR_DRIVE_LEFT_HIGH  = 2;
    public static final int PORT_MOTOR_DRIVE_RIGHT_LOW  = 3;
    public static final int PORT_MOTOR_DRIVE_RIGHT_HIGH = 4;
    public static final int PORT_MOTOR_SHOOTER_WHEEL    = 5;
    public static final int PORT_MOTOR_3BA              = 6;
    public static final int PORT_MOTOR_KANAYERBELT      = 7;
    
    
    // Joysticks
    public static final int PORT_JOYSTICK_DRIVE = 1;
    public static final int PORT_JOYSTICK_SHOOT = 2;
    
    // Joystick Axiseses
    public static final int JOYSTICK_AXIS_DRIVE_LEFT  = 1;
    public static final int JOYSTICK_AXIS_DRIVE_RIGHT = 2;
    
    // Joystick Buttons
    public static final int NUM_BUTTONS                              = 10;
    public static final int JOYSTICK_BUTTON_AUTO_AIM                 = 1;
    public static final int JOYSTICK_BUTTON_SHOOT                    = 2;
    public static final int JOYSTICK_BUTTON_COLLECTOR_START_IN       = 3;
    public static final int JOYSTICK_BUTTON_COLLECTOR_START_OUT      = 4;
    public static final int JOYSTICK_BUTTON_COLLECTOR_STOP           = 5;
    public static final int JOYSTICK_BUTTON_COLLECTOR_RETURN_CONTROL = 6;
    
    // Sensors
    public static final int PORT_IR_BALL_IN = 1;
    public static final int PORT_IR_BALL_OUT = 2;
    public static final int PORT_IR_BALL_READY = 3;
    
    
    // Encoders
    public static final int PORT_ENCODER_DRIVE_LEFT_A         = 4;
    public static final int PORT_ENCODER_DRIVE_LEFT_B         = 5;
    public static final int PORT_ENCODER_DRIVE_LEFT_A_BACKUP  = 4;
    public static final int PORT_ENCODER_DRIVE_LEFT_B_BACKUP  = 5;
    public static final int PORT_ENCODER_DRIVE_RIGHT_A        = 6;
    public static final int PORT_ENCODER_DRIVE_RIGHT_B        = 7;
    public static final int PORT_ENCODER_DRIVE_RIGHT_A_BACKUP = 6;
    public static final int PORT_ENCODER_DRIVE_RIGHT_B_BACKUP = 7;
    public static final int PORT_ENCODER_SHOOTER_SPEED        = 8;
    public static final int PORT_ENCODER_3BA                  = 1; // <-- ANALOG
    
    
    //***** GLOBAL HARDWARE ****//
    
    // Joysticks
    public static final Joystick joystickDrive;
    public static final Joystick joystickShoot;
    static {
	joystickDrive = new Joystick(PORT_JOYSTICK_DRIVE);
	joystickShoot = new Joystick(PORT_JOYSTICK_SHOOT);
    }
    
}
