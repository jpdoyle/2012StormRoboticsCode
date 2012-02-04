package storm.interfaces;

public interface ITargetTracker {
    
    public void startLocking();
    public void stopLocking();
    
    public boolean doAim();
    
    public double getDistance();
    
}
