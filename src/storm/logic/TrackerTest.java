/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.logic;

import edu.wpi.first.wpilibj.DriverStationLCD;
import storm.interfaces.IRobotLogic;
import storm.modules.TargetTracker;

/**
 *
 * @author joe
 */
public class TrackerTest implements IRobotLogic {
    TargetTracker tracker;
    int counter = 0;

    public void doInit() {
        tracker = new TargetTracker();
        counter = 0;
        tracker.startLocking();
    }

    public void doContinuous() {
        DriverStationLCD lcd = DriverStationLCD.getInstance();
        lcd.println(DriverStationLCD.Line.kMain6, 1, "                     ");
        lcd.println(DriverStationLCD.Line.kUser2, 1, "                     ");
        lcd.println(DriverStationLCD.Line.kUser3, 1, "                     ");
        lcd.println(DriverStationLCD.Line.kUser4, 1, "                     ");
        lcd.println(DriverStationLCD.Line.kUser5, 1, "                     ");
        //lcd.println(DriverStationLCD.Line.kUser6, 1, "                     ");
        System.out.println("beginning aim");
        ++counter;
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, Integer.toString(counter));
        tracker.isAimed();
        lcd.updateLCD();
    }

    public void doPeriodic() {
    }

    public void doEnd() {
        tracker.stopLocking();
    }
}
