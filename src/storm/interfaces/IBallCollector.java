package storm.interfaces;

public interface IBallCollector {
    
    public void startCollecting(int direction);
    public void stopCollecting();
    public void returnControl();
    
    public int getNumBalls();
    
    public void run();
    
}
