package storm.interfaces;

public interface IBallCollector {
    
    public void startCollecting();
    public void stopCollecting();
    
    public int getNumBalls();
    
    public void run();
    
}
