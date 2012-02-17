package storm.modules;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.util.TimerTask;
import storm.interfaces.IDriveTrain;
import storm.utility.Print;

import storm.utility.RobotTurner;

/**
 *
 * @author joe
 */
public class TargetTracker implements storm.interfaces.ITargetTracker {

    static final int[][] THRESHOLD = {{0, 150},
                                      {240, 255},
                                      {200, 255}};
    static final double FOV = 47;
    // z value required for vision target rectangle fill the view vertically
    static final double Z_BASE = .77;
    // offset of camera lens from center of robot
    //static final double CAMERA_OFFSET = .165;
    // angle between horizontal and the camera's view
    static final double CAMERA_ANGLE = 23;
    static final double CAMERA_ANGLE_COS = storm.utility.TrigLUT.cos(CAMERA_ANGLE/180*Math.PI);
    // frequency to update camera calculations (in iterations per second)
    // note that this is the upper bound for the frequency, not necessarily the actual frequency
    static final double CAMERA_FREQUENCY = 10;

    private AxisCamera camera_ = AxisCamera.getInstance();
    private RobotTurner turner_;
    private double angle_ = 0;
    private int state_ = 0;
    private ColorImage cameraImg_;
    private BinaryImage image_;
    private ParticleAnalysisReport topTarget_ = null;
    private double[] angleRange_ = {Double.NaN, Double.NaN};
    private double zLoc_ = Double.NaN;

    private final NetworkTable netTable_ = NetworkTable.getTable("Target Tracker");

    public TargetTracker(IDriveTrain drive,int gyroChannel) {
        turner_ = new RobotTurner(drive,gyroChannel);
        netTable_.beginTransaction();
            netTable_.putInt("X", camera_.getResolution().width/2);
            netTable_.putInt("Y",camera_.getResolution().height/2);
            netTable_.putInt("Width", 0);
            netTable_.putInt("Height", 0);
            netTable_.putBoolean("Aimed", false);
            netTable_.putDouble("Z", 0);
        netTable_.endTransaction();
    }

    private void retrieveImage() {
        try {
            angle_ = turner_.getGyroAngle();
            if (!camera_.freshImage()) {
                return;
            }
            cameraImg_ = camera_.getImage();
            if (cameraImg_ != null) {
                ++state_;
//                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "                     ");
//                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "(" + cameraImg_.getWidth() + "," + cameraImg_.getHeight() + ")");
            }
        } catch (AxisCameraException ex) {
            //ex.printStackTrace();
        } catch (NIVisionException ex) {
            //ex.printStackTrace();
        }
    }

    private void matchThreshold() {
        try {
            image_ = null;
            image_ = cameraImg_.thresholdRGB(THRESHOLD[0][0], THRESHOLD[0][1], THRESHOLD[1][0], THRESHOLD[1][1], THRESHOLD[2][0], THRESHOLD[2][1]);
            ++state_;
        } catch (NIVisionException ex) {
//            ex.printStackTrace();
            if (image_ == null) {
                state_ = 0;
            }
        } finally {
            try {
                cameraImg_.free();
            } catch (NIVisionException ex) {
                //ex.printStackTrace();
            }
        }
    }

    private void convexHull() {
        BinaryImage oldImage = image_;
        try {
            image_ = oldImage.convexHull(true);
            ++state_;
        } catch (NIVisionException ex) {
//            ex.printStackTrace();
            state_ = 0;
        } finally {
            try {
                oldImage.free();
            } catch (NIVisionException ex) {
               // ex.printStackTrace();
            }
        }
    }

    private void findParticles() {
        BinaryImage oldImage = image_;
        try {
            image_ = image_.removeSmallObjects(true, 4);

            ParticleAnalysisReport[] particles = image_.getOrderedParticleAnalysisReports();
            if (particles == null || particles.length == 0) {
                throw new NIVisionException("No particles found");
            }
            topTarget_ = particles[0];
            for (int i = 1; i < particles.length; ++i) {
                if (particles[i].center_mass_x < topTarget_.center_mass_x) {
                    topTarget_ = particles[i];
                }
            }
            ++state_;
        } catch (NIVisionException ex) {
//            ex.printStackTrace();
            state_ = 0;
        } finally {
            try {
                image_.free();
                oldImage.free();
            } catch (NIVisionException ex1) {
                //ex1.printStackTrace();
            }
        }
    }

    public synchronized boolean isAimed() {
        if(Double.isNaN(zLoc_))
            return false;
        double angle = turner_.getGyroAngle();
        return angle >= angleRange_[0] && angle <= angleRange_[1];
    }

    private synchronized void findDetails() {
        // magnitude along an axis projecting directly from the center of the camera's lens
        double cameraZ = Z_BASE * topTarget_.imageWidth / topTarget_.boundingRectWidth;
        // magnitude along an axis in the direction of the camera, but parallel to the ground
        zLoc_ = cameraZ * CAMERA_ANGLE_COS;
        angleRange_[0] = angle_ + topTarget_.boundingRectLeft / (double) topTarget_.imageWidth * FOV - FOV / 2;
        angleRange_[1] = angle_ + (topTarget_.boundingRectLeft + topTarget_.boundingRectWidth) / (double) topTarget_.imageWidth * FOV - FOV / 2;
        turner_.setAngle((angleRange_[0] + angleRange_[1]) / 2);
        state_ = 0;
        netTable_.beginTransaction();
            netTable_.putInt("X", topTarget_.boundingRectLeft);
            netTable_.putInt("Y", topTarget_.boundingRectTop);
            netTable_.putInt("Width", topTarget_.boundingRectWidth);
            netTable_.putInt("Height", topTarget_.boundingRectHeight);
            netTable_.putBoolean("Aimed", isAimed());
            netTable_.putDouble("Z", zLoc_);
        netTable_.endTransaction();
    }

    private void doAim() {
 //       System.out.println("tracking target");
        String stateName = "";
        boolean once = false;
        state_ = 0;
        while (!once || state_ != 0) {
            once = true;
            switch (state_) {
                case 0:
                    stateName = "Retrieving image";
                    retrieveImage();
                    break;
                case 1:
                    stateName = "Matching threshold";
                    matchThreshold();
                    break;
                case 2:
                    stateName = "Performing convex hull";
                    convexHull();
                    break;
                case 3:
                    stateName = "Finding particles";
                    findParticles();
                    break;
                case 4:
                    stateName = "Finding angle & location";
                    findDetails();
                    break;
                default:
                    return;
            }
	    Print.getInstance().setLine(4, stateName);
	    Print.getInstance().setLine(5, "Aimed: " + isAimed());
//            Thread.yield();
//            System.out.println("begin debug output");
//            String line1 = "Angle: " + turner_.getGyroAngle(),
//                   line2 = stateName,
//                   line3 = "Location: (?,?," + zLoc_ + ")",
//                   line4 = "";
//            DriverStationLCD lcd = DriverStationLCD.getInstance();
//
//            lcd.println(DriverStationLCD.Line.kMain6, 1, "                     ");
//            lcd.println(DriverStationLCD.Line.kMain6, 1, line1);
//            System.out.println(line1);
//
//            lcd.println(DriverStationLCD.Line.kUser2, 1, "                     ");
//            lcd.println(DriverStationLCD.Line.kUser2, 1, line2);
//            System.out.println(line2);
//
//            lcd.println(DriverStationLCD.Line.kUser3, 1, "                     ");
//            lcd.println(DriverStationLCD.Line.kUser3, 1, line3);
//            System.out.println(line3);
//
//            lcd.updateLCD();
        }
    }

    public synchronized double getDistance() {
        return zLoc_;
    }
    long period = (long)(1000.0/CAMERA_FREQUENCY);
    volatile boolean tracking = false;
    boolean locking = false;
    Thread thread = new Thread() {
            public void run() {
                long prevTime = System.currentTimeMillis();
                while(tracking) {
                    doAim();
                    long currTime = System.currentTimeMillis();
                    Print.getInstance().setLine(3, (currTime-prevTime)/1000.0 + " seconds");
                    prevTime = currTime;
                }
            }
        };

    public void startTracking() {
        if(tracking)
            return;
        tracking = true;
        thread.start();
    }
    public void stopTracking() {
        tracking = false;
    }

    public void startLocking() {
        if (locking)
            return;
        locking = true;
        turner_.enable();
    }

    public void stopLocking() {
        if (!locking)
            return;
        locking = false;
        turner_.disable();
    }
}

