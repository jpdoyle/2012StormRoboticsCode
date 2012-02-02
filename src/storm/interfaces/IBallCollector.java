package storm.interfaces;

public interface IBallCollector {
    
    public void startCollecting();
    public void stopCollecting();
    
    public void addBall();
    public void removeBall();
    
    public int getNumBalls();
    
}
