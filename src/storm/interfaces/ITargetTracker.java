package storm.interfaces;

public interface ITargetTracker {
    
    public void startAim();
    public boolean isAimed();
    public void endAim();
    public double getVelocity();
    
}
