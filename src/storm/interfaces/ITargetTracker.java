package storm.interfaces;

public interface ITargetTracker {
    
//    public void startLocking();
//    public void stopLocking();
//    public boolean isLocking();
    public void startTracking();
    public void stopTracking();
    public boolean isAimed();
    public double getDistance();
    
}
