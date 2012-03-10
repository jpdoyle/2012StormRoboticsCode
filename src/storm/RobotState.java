package storm;


import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import storm.interfaces.*;
import storm.modules.*;

public abstract class RobotState {
    
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
    public static final int PORT_MOTOR_DRIVE_LEFT         = 8;
    public static final int PORT_MOTOR_DRIVE_RIGHT        = 3;
    public static final int PORT_MOTOR_SHOOTER_WHEEL      = 5;
    public static final int PORT_MOTOR_3BA                = 10;
    public static final int PORT_MOTOR_BRIDGE_MANIPULATOR = 1;
    public static final int PORT_MOTOR_KANAYERBELT_FEEDER = 4;
    public static final int PORT_MOTOR_KANAYERBELT_BOTTOM = 6;
    public static final int PORT_MOTOR_KANAYERBELT_TOP    = 9;
    
    // Joysticks
    public static final int PORT_JOYSTICK_DRIVE = 1;
    public static final int PORT_JOYSTICK_SHOOT = 2;
    
    // Joystick Axiseses
    public static final int JOYSTICK_1_AXIS_DRIVE_LEFT         = 2;
    public static final int JOYSTICK_1_AXIS_DRIVE_RIGHT        = 4;
    public static final int JOYSTICK_1_AXIS_DRIVE_BOTH         = 6;
    public static final int JOYSTICK_2_AXIS_3BA                = 2;
    public static final int JOYSTICK_2_AXIS_BRIDGE_MANIPULATOR = 4;
    public static final int JOYSTICK_2_AXIS_ELEVATOR           = 6;
    
    // Joystick Buttons
    public static final int JOYSTICK_1_BUTTON_SWITCH_GEARS          = 5;
    public static final int JOYSTICK_1_BUTTON_SPEED_MODIFIER        = 6;
    public static final int JOYSTICK_1_BUTTON_DIRECT_DRIVE          = 8;
    public static final int JOYSTICK_2_BUTTON_DECREASE_OFFSET       = 1;
    public static final int JOYSTICK_2_BUTTON_RESET_OFFSET          = 2;
    public static final int JOYSTICK_2_BUTTON_INCREASE_OFFSET       = 3;
    public static final int JOYSTICK_2_BUTTON_TOGGLE_DISTANCE       = 5;
    public static final int JOYSTICK_2_BUTTON_SHOOT                 = 6;
    public static final int JOYSTICK_2_BUTTON_3BA_SAFETY            = 7;
    public static final int JOYSTICK_2_BUTTON_PRESHOOT              = 8;
    public static final int JOYSTICK_2_BUTTON_TOGGLE_TARGET_TRACKER = 4;
    
    
    // Sensors
    public static final int PORT_IR_BALL_IN_1             = 6;
    public static final int PORT_IR_BALL_IN_2             = 7;
    public static final int PORT_IR_BALL_READY            = 9;
    public static final int PORT_LIMIT_SWITCH_3BA_TOP     = 10;
    public static final int PORT_LIMIT_SWITCH_3BA_BOTTOM  = 8;
    
    // Gyro
    public static final int PORT_GYRO_ROBOT_ROTATION = 1;
    
    // Encoders
    public static final int PORT_ENCODER_DRIVE_LEFT_A         = 1;
    public static final int PORT_ENCODER_DRIVE_LEFT_B         = 2;
    public static final int PORT_ENCODER_DRIVE_LEFT_A_BACKUP  = 1;
    public static final int PORT_ENCODER_DRIVE_LEFT_B_BACKUP  = 2;
    public static final int PORT_ENCODER_DRIVE_RIGHT_A        = 3;
    public static final int PORT_ENCODER_DRIVE_RIGHT_B        = 4;
    public static final int PORT_ENCODER_DRIVE_RIGHT_A_BACKUP = 3;
    public static final int PORT_ENCODER_DRIVE_RIGHT_B_BACKUP = 4;
    public static final int PORT_ENCODER_SHOOTER_SPEED        = 5;
    public static final int PORT_ENCODER_BRIDGE_MANIPULATOR   = 2; // <-- ANALOG

    // Solenoids
    public static final int PORT_SOLENOID_HIGH_GEAR = 1;
    public static final int PORT_SOLENOID_LOW_GEAR  = 2;
    
    // Info
    public static final double DRIVE_SPEED_REDUCTION_VALUE = 0.65;
    public static final double DRIVE_SPEED_NORMAL_VALUE    = 1.0;
    
    // Global State
    public static int BALL_CONTAINMENT_COUNT = 0;
    public static final NetworkTable DASHBOARD_FEEDBACK = NetworkTable.getTable("Feedback");
    public static boolean TARGET_TRACKER_IS_TRACKING = false;
    
    //Analog Channels
    public static final int PORT_SWITCH_HYBRID_TYPE = 3;
    
    //Compressor Info
    public static final int PORT_COMPRESSOR_RELAY           = 1;
    public static final int PORT_COMPRESSOR_PRESSURE_SWITCH = 14;
    
    //***** GLOBAL HARDWARE ****//
    
    // Joysticks
    public static final Joystick joystickDrive;
    public static final Joystick joystickShoot;
    
    // Modules
    public static final IDriveTrain driveTrain;
    public static final IBallCollector ballCollector;
    public static final IBridgeManipulator bridgeManipulator;
    public static final IShooter shooter;
    public static final I3BA threeBA;
    public static final ITargetTracker targetTracker;
    public static final BallController ballController;
    public static final Compressor compressor;
    
    static {
	joystickDrive = new Joystick(PORT_JOYSTICK_DRIVE);
	joystickShoot = new Joystick(PORT_JOYSTICK_SHOOT);
	
	driveTrain = new DriveTrain(PORT_MOTOR_DRIVE_LEFT, PORT_MOTOR_DRIVE_RIGHT, PORT_SOLENOID_HIGH_GEAR, PORT_SOLENOID_LOW_GEAR);
	ballCollector = new BallCollector(PORT_MOTOR_KANAYERBELT_FEEDER, PORT_MOTOR_KANAYERBELT_BOTTOM, PORT_IR_BALL_IN_1, PORT_IR_BALL_IN_2);
	bridgeManipulator = new BridgeManipulator(PORT_MOTOR_BRIDGE_MANIPULATOR, PORT_ENCODER_BRIDGE_MANIPULATOR);
	shooter = new Shooter(PORT_MOTOR_SHOOTER_WHEEL, PORT_MOTOR_KANAYERBELT_TOP, PORT_IR_BALL_READY, PORT_ENCODER_SHOOTER_SPEED);
	threeBA = new ThreeBA(PORT_MOTOR_3BA, PORT_LIMIT_SWITCH_3BA_TOP, PORT_LIMIT_SWITCH_3BA_BOTTOM);
	targetTracker = new TargetTracker(driveTrain,PORT_GYRO_ROBOT_ROTATION);
	ballController = new BallController(ballCollector, shooter);
	compressor = new Compressor(PORT_COMPRESSOR_PRESSURE_SWITCH, PORT_COMPRESSOR_RELAY);
    }
    
}
