package storm.interfaces;

public interface IDriveTrain {
    
    public void addToQueue(int type, double speed, double distance);
    public void resetQueue();
    public void executeQueue();
    public boolean isQueueFinished();
    
    public void drive(double leftSpeed, double rightSpeed);
    public void driveDirect(double leftSpeed, double rightSpeed);
    public void resetDistance();
    public double getDistance();
    
    public void switchGear();
    public void setHighGear();
    public void setLowGear();
    public boolean isHighGear();

}
