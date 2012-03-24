package storm.interfaces;

public interface IBallCollector {
    
    public static final double DIRECTION_UP   = -1.0;
    public static final double DIRECTION_UP_HYBRID = -0.5;
    public static final double DIRECTION_DOWN = 1.0;
    
    public void start(double direction);
    public void stop();
    
    public boolean isRunning();
    
    public void run();
    
}
