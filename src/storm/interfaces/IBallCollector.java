package storm.interfaces;

public interface IBallCollector {
    
    public static final double MOTOR_SPEED_IN = 1.0d;
    public static final double MOTOR_SPEED_OUT = -1.0d;
    
    public void startCollecting(double direction);
    public void stopCollecting();
    public void returnControl();
    
    public int getNumBalls();
    
    public void run();
    
}
