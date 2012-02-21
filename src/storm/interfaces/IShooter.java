package storm.interfaces;

public interface IShooter {
    
    public void startShoot(double distance);
    public void doShoot();
    public boolean isShooting();
    public double getRPM();
    
}
