package storm.interfaces;

public interface IShooter {

    public void preShoot();
    public void startShoot(boolean useTable, double distance);
    public void doShoot();
    public boolean isShooting();
    public double getRPM();
    public void endShoot();
    public void setContinuousShoot(boolean continuousShoot);
    public void warmUp();
    
}
