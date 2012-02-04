package storm.interfaces;

public interface IMotorEncoder {
    
    public void resetDistance();
    public double getDistance(int oneIsLeftTwoIsRight);
    
}