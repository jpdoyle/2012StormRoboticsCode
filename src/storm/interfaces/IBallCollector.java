package storm.interfaces;

public interface IBallCollector {
    
    public static final int DIRECTION_UP   = -1;
    public static final int DIRECTION_DOWN = 1;
    
    public void start(int direction);
    public void stop();
    
    public boolean isRunning();
    
    public void run();
    
}
