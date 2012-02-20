package storm.utility;

public final class Utility {
    
    public static double checkDeadZone(double joystickValue) {
	return (Math.abs(joystickValue) < 0.2) ? 0.0 : joystickValue;
    }
    
}
