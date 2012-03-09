/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.logic;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Preferences;
import storm.RobotState;
import storm.interfaces.IBallCollector;
import storm.interfaces.IBridgeManipulator;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;
import storm.interfaces.ITargetTracker;
import storm.utility.Print;
import storm.utility.Queue;

/**
 *
 * @author Awesome
 */
public class Hybrid implements IRobotLogic {

    IDriveTrain driveTrain;
    IShooter shooter;
    ITargetTracker targetTracker;
    IBallCollector ballCollector;
    IBridgeManipulator manipulator;

    AnalogChannel autoType;

    Queue Q = new Queue();

    boolean isLoading = false;
    boolean isManipulating = false;

    double endTime = 0;
    double startTime = 0;

    int autoNum = 0;

    public Hybrid() {

        driveTrain = RobotState.driveTrain;
        shooter = RobotState.shooter;
        targetTracker = RobotState.targetTracker;
        ballCollector = RobotState.ballCollector;
        manipulator = RobotState.bridgeManipulator;

        autoType = new AnalogChannel(RobotState.PORT_SWITCH_HYBRID_TYPE);

    }

    public void doInit() {

        Q.clear();
	driveTrain.resetDistance();
        endTime = 0;
        startTime = System.currentTimeMillis();
        autoNum = 0;
	driveTrain.setLowGear();
        isManipulating = false;
        isLoading = false;
        shooter.endShoot();
        ballCollector.stop();

        //1 - Straight     \\
        //2 - Left         \\
        //3 - Manipulate   \\
        //4 - Shoot        \\
        //5 - Start Load   \\
        //6 - Stop Load    \\
        //7 - Wait         \\

        //System.currentTimeMillis();

        autoNum = (int) Math.floor(autoType.getVoltage() + 0.5) + 1;

        switch (autoNum) {//autoType.getValue()) {
            case 1: //Super Auto Mode

                Q.add(4, 3.66, 0); //Shoot
                Q.add(7, 2.5, 0); //Wait
                Q.add(5, 0, 0); //Start Loading

                break;
            case 2: //Quick ninja

                Q.add(1, 57.45, -0.4); //Move
		Q.add(3, 0, 0); //Manipulate
                Q.add(4, 4.66/*shoot from bridge distance*/, 0); //Shoot
                Q.add(7, 2.5, 0); //Wait
                Q.add(5, 0, 0); //Start Loading

                break;
            case 3: //Nuttin'
		
		

                break;
            case 4: //Stand and shoot 1


                Q.add(4, 3.66, 0); //Shoot
                Q.add(7, 2.5, 0); //Wait
                Q.add(5, 0, 0); //Start Loading

                break;
            case 5: //Stand and shoot 2


                Q.add(4, 4.66, 0); //Shoot
                Q.add(7, 2.5, 0); //Wait
                Q.add(5, 0, 0); //Start Loading

                break;
            case 6: //Sitting duck
                break;
            default:
                break;
        }

        Q.start();
	
	driveTrain.resetDistance();

    }

    public void doContinuous() {
        Print.getInstance().setLine(0, "Queue Part: " + Q.getPart());
        Print.getInstance().setLine(2, "End Time: " + endTime);
        Print.getInstance().setLine(3, "Current Time: " + (System.currentTimeMillis()-startTime));
        shooter.doShoot();
        //Eat a muffin.  please :)
    }

    public void doPeriodic() {
	
	Print.getInstance().setLine(5, "D: " + driveTrain.getDistance());

        if (Q.isRunning()) runQueue();

        if (Q.getType() == 1 || Q.getType() == 2) {
            if (Math.abs(driveTrain.getDistance()) >= Q.getDistance() && Math.abs(driveTrain.getRDistance()) >= Q.getDistance()) {
                Q.next();
            }
        } else if (Q.getType() == 3) {
            //Manipulate
            Q.next();
        } else if (Q.getType() == 4) {
            //if (!shooter.isShooting()) Q.next();
            Q.next();
        } else if (Q.getType() == 5 || Q.getType() == 6) {
            Q.next();
        } else if (Q.getType() == 7) {
            if ((System.currentTimeMillis()-startTime) >= endTime) {
                endTime = 0;
                Q.next();
            }
        }

        //if (isLoading) ballCollector.start(IBallCollector.DIRECTION_UP);
        if (isManipulating) manipulator.lower();

    }

    public void doEnd() {
        shooter.endShoot();
        ballCollector.stop();
        Q.clear();
    }

    public void runQueue() {

        switch ((int)Q.getType()) {
            case 1: //Forward/Backward
                if (Math.abs(driveTrain.getDistance()) < Q.getDistance() && Math.abs(driveTrain.getRDistance()) < Q.getDistance())
                    driveTrain.drive(Q.getSpeed(), Q.getSpeed());
                if (Math.abs(driveTrain.getDistance()) >= Q.getDistance() && Math.abs(driveTrain.getRDistance()) < Q.getDistance())
                    driveTrain.drive(0, Q.getSpeed());
                if (Math.abs(driveTrain.getDistance()) < Q.getDistance() && Math.abs(driveTrain.getRDistance()) >= Q.getDistance())
                    driveTrain.drive(Q.getSpeed(), 0);
                break;
            case 2: //Turning
                driveTrain.drive(Q.getSpeed(), -Q.getSpeed());
                break;
            case 3: //Manipulate
                isManipulating = true;
                break;
            case 4: //Shoot
                shooter.setContinuousShoot(true);
                if (!shooter.isShooting()) shooter.startShoot(Q.getDistance());
                break;
            case 5:
                isLoading = true;
                ballCollector.start(IBallCollector.DIRECTION_UP);
                break;
            case 6:
                isLoading = false;
                ballCollector.stop();
                break;
            case 7:
                if (endTime == 0) {
                    endTime = System.currentTimeMillis() - startTime + Q.getDistance() * 1000;
                }
                break;
            default:
                break;
        }

    }

}