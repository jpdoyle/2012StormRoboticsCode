package storm.interfaces;

public interface IShooter {
    
    public void startShoot(double velocity);
    public void doShoot(int state);
    public boolean isShooting();
    
}
