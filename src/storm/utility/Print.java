package storm.utility;

import edu.wpi.first.wpilibj.DriverStationLCD;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Print {
    private static Print instance = null;

    public static Print getInstance() {
        if(instance == null)
            instance = new Print();
        return instance;
    }

    private static final int
            NUMLINES = 6,
            NUMCOLUMNS = 21;
    private static final String CLEARLINE = "                     ";

	private DriverStationLCD lcd_ = DriverStationLCD.getInstance();
	private String[] lines_ = new String[NUMLINES];

    private Print() {
        clearScreen();
    }

    public final void clearLine(int which) {
        lines_[which] = CLEARLINE;
        update();
    }

    public final void clearScreen() {
        for(int i=0;i<lines_.length;++i)
            lines_[i] = CLEARLINE;
        update();
    }

    private static String padString(String s) {
        if(s.length() >= NUMCOLUMNS)
            return s.substring(0,NUMCOLUMNS);
        return s + CLEARLINE.substring(s.length());
    }

    public final void setLine(int which,String s) {
        lines_[which] = padString(s);
        update();
    }

    private static boolean isEmpty(String s) {
        return s.equals(CLEARLINE);
    }

    private void rotateUp(int start,int end,String lastLine) {
        for(int i=start;i<end-1;++i) {
            lines_[i] = lines_[i+1];
        }
        lines_[end-1] = lastLine;
    }

    private void println(int start,int end,String s) {
        if(end > lines_.length)
            end = lines_.length;
        if(start < 0)
            start = 0;
        if(start > end)
            return;
        s = padString(s);
        int i;
        for(i=end-1;i>=start && isEmpty(lines_[i]);--i) {}
        ++i;
        if(i == end)
            rotateUp(start,end,s);
        else
            lines_[i] = s;
        update();
    }

    public final void println(String s) {
        println(0,lines_.length,s);
    }

    private class ostream_ extends OutputStream {
        String buffer_ = "";
        int start_,end_;

        public ostream_(int start,int end) {
            start_ = start;
            end_ = end;
        }

        public void write(int b) throws IOException {
            if(b == '\n') {
                println(start_,end_,buffer_);
                buffer_ = "";
                return;
            }
            buffer_ += (char)b;
        }

    }

    public final PrintStream getPrintStream(int start,int end) {
        return new PrintStream(new ostream_(start,end));
    }

    public final PrintStream getPrintStream() {
        return getPrintStream(0,lines_.length);
    }

    private void update() {
        lcd_.println(DriverStationLCD.Line.kMain6, 1, lines_[0]);
        lcd_.println(DriverStationLCD.Line.kUser2, 1, lines_[1]);
        lcd_.println(DriverStationLCD.Line.kUser3, 1, lines_[2]);
        lcd_.println(DriverStationLCD.Line.kUser4, 1, lines_[3]);
        lcd_.println(DriverStationLCD.Line.kUser5, 1, lines_[4]);
        lcd_.println(DriverStationLCD.Line.kUser6, 1, lines_[5]);
        lcd_.updateLCD();
    }
}
