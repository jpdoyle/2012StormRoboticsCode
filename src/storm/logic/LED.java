/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package storm.logic;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 *
 * @author joe
 */
public class LED {
    private DigitalOutput out;
    private boolean state = false;

    public LED(int channel) {
        out = new DigitalOutput(channel);
        out.set(true);
    }

    public void set(boolean output) {
        state = output;
        out.set(!output);
    }

    public boolean get() {
        return state;
    }

}
