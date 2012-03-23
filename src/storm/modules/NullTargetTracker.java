package storm.modules;

import storm.interfaces.ITargetTracker;

public class NullTargetTracker implements ITargetTracker {

    public void startTracking() {
    }

    public void stopTracking() {
    }

    public boolean isAimed() {
	return false;
    }

    public double getDistance() {
	return -1.0;
    }
    
}
