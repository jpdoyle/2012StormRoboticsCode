package storm.interfaces;

public interface ITargetTracker {
    
    public void startLocking();
    public void stopLocking();
    public boolean isAimed();
    public double getDistance();
    
}
