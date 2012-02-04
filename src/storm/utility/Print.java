package storm.utility;

import edu.wpi.first.wpilibj.DriverStationLCD;

public class Print {

	private static DriverStationLCD driver = DriverStationLCD.getInstance();
	private static String[] line;

	public Print(){
		line = new String[6];
		for(int i = 0; i < line.length; i++) {
                    line[i] = "";
		}
	}

	public void setLine(int lineNumber, String inputString)
	{
		//sets user's input to a desired line number
		line[lineNumber] = inputString;
		update();
	}

	public void printLine(String inputString)
	{
		//Prints Text from top to bottom
		printText(inputString);
		update();
	}

	private void printText(String str) {
		//Alternate Code: bottom to top; needs tweaking
		//for(int i = 0; i < line.length; i++)
		for(int i = line.length-1; i > 0; i--)
		{
			line[i] = line[i-1];
		}
		line[0] = str;
	}

	public void clearScreen()
	{
		for(int i = 0; i < line.length; i++) {
                    line[i] = "                                    ";
		}
	}

	private void update() {
		for(int i = 0; i < line.length; i++)
		{
                    DriverStationLCD.Line driverLine = null;
                    switch (i) {
                        case 0:
                            driverLine = DriverStationLCD.Line.kMain6;
                            break;
                        case 1:
                            driverLine = DriverStationLCD.Line.kUser2;
                            break;
                        case 2:
                            driverLine = DriverStationLCD.Line.kUser3;
                            break;
                        case 3:
                            driverLine = DriverStationLCD.Line.kUser4;
                            break;
                        case 4:
                            driverLine = DriverStationLCD.Line.kUser5;
                            break;
                        case 5:
                            driverLine = DriverStationLCD.Line.kUser6;
                            break;
                        default:
                            driverLine = DriverStationLCD.Line.kMain6;
                            break;
                    }
                    driver.println(driverLine, 1, line[i]);
		}
		driver.updateLCD();
	}
}