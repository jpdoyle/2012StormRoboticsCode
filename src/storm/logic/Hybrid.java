/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storm.logic;

import edu.wpi.first.wpilibj.AnalogChannel;
import storm.RobotState;
import storm.interfaces.IDriveTrain;
import storm.interfaces.IRobotLogic;
import storm.interfaces.IShooter;
import storm.interfaces.ITargetTracker;
import storm.utility.Queue;

/**
 *
 * @author Developer
 */
public class Hybrid implements IRobotLogic {

    IDriveTrain driveTrain;
    IShooter shooter;
    ITargetTracker targetTracker;

    AnalogChannel autoType;

    Queue Q = new Queue();

    public Hybrid(int port) {

        driveTrain = RobotState.driveTrain;
        shooter = RobotState.shooter;
        targetTracker = RobotState.targetTracker;

        autoType = new AnalogChannel(port);

    }

    public void doInit() {

        //1 - Straight    \\
        //2 - Left        \\
        //3 - Load        \\
        //4 - Shoot       \\
        //5 - Manipulator \\

        switch (autoType.getValue()) {
            case 1: //Super Auto Mode

                //Aim
                //Shoot
                Q.add(1, 60, -.5);
                Q.add(5, 25, 1);
                //Pick up
                //Possibly move
                //Aim
                //Shoot

                break;
            case 2: //Quick ninja

                Q.add(1, 60, -.5);
                Q.add(5, 25, 1);
                //Possibly do a shoot sequence

                break;
            case 3: //Hit and run

                //Aim
                //Shoot

                break;
            case 4: //Flee in terror to the left

                Q.add(2, 50, .8);
                Q.add(1, 50, .5);

                break;
            case 5: //Flee in terror to the right

                Q.add(2, 50, -.8);
                Q.add(1, 50, .5);

                break;
            case 6: //Sitting duck
                break;
            default:
                break;
        }

        Q.start();

    }

    public void doContinuous() {
        //Eat a muffin.  please :)
    }

    public void doPeriodic() {

        if (Q.isRunning()) runQueue();
        if (Q.getType() == 1 || Q.getType() == 2) {
            if (driveTrain.getDistance() >= Q.getDistance()) Q.next();
        } else if (Q.getType() == 4) {
            if (!shooter.isShooting()) Q.next();
        } else if (Q.getType() == 5) {
            
        }

    }

    public void doEnd() {
    }

    public void runQueue() {

        switch ((int)Q.getType()) {
            case 1:
                driveTrain.drive(Q.getSpeed(), Q.getSpeed());
                break;
            case 2:
                driveTrain.drive(Q.getSpeed(), -Q.getSpeed());
                break;
            case 3:
                break;
            case 4:
                if (!shooter.isShooting()) shooter.startShoot(targetTracker.getDistance());
            case 5:
                //Bridge Manipulator
                break;
            default:
                break;
        }

    }
    
}
