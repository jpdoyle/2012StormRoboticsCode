package storm.modules;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import storm.utility.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import storm.RobotState;

/**
 *
 * @author joe
 */
public class TargetTracker implements storm.interfaces.ITargetTracker {
    // RGB threshold for the image
    static final int[][] THRESHOLD = {{0, 150},
                                      {200, 255},
                                      {190, 255}};
    static final NIVision.Range[] ranges = { new NIVision.Range(THRESHOLD[0][0], THRESHOLD[0][1]),
                                             new NIVision.Range(THRESHOLD[1][0], THRESHOLD[1][1]),
                                             new NIVision.Range(THRESHOLD[2][0], THRESHOLD[2][1]) };
    static final double FOV = 47;
    // z value (in meters) required for vision target rectangle fill the view vertically
    static final double Z_BASE = .77;
    // angle between horizontal and the camera's view
    static final double CAMERA_ANGLE = 23;
    static final double CAMERA_ANGLE_COS = Math.cos(CAMERA_ANGLE/180*Math.PI);

    private CriteriaCollection criteria = new CriteriaCollection();

    private AxisCamera camera_ = AxisCamera.getInstance();
    private Image cameraImg_;
    private BinaryImage binImage_;
    // the target we like
    private volatile ParticleAnalysisReport topTarget_ = null;
    private double[] angleRange_ = {Double.NaN,Double.NaN};
    private volatile double zLoc_ = Double.NaN;

    private final NetworkTable netTable_ = NetworkTable.getTable("Target Tracker");


    private volatile boolean tracking_ = false;
    private Thread thread_ = new Thread() {
            public void run() {
                // run forever
                for(;;) {
                    if(!tracking_) {
                        // if it isn't tracking, wait a bit before checking again
                        try {
                            Thread.sleep(1000 / 5);
                        } catch (InterruptedException ex) {}
                        continue;
                    }
                    doAim();
                }
            }
        };

    public TargetTracker() {
        // generally initialize stuff

        criteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 0, 40, true);
        criteria.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 0, 40, true);
        try {
            cameraImg_ = new RGBImage();
            binImage_ = new FancyBinaryImage();
        } catch (NIVisionException ex) {
            throw new NullPointerException("Cannot track targets without valid images");
        }
        thread_.start();
    }

    private void retrieveImage() throws NIVisionException, AxisCameraException {
        if (!camera_.freshImage()) {
            return;
        }
        camera_.getImage(cameraImg_);
    }

    private void matchThreshold() throws NIVisionException {
        NIVision.colorThreshold(cameraImg_.image, binImage_.image, NIVision.ColorMode.IMAQ_RGB, ranges[0].getPointer(), ranges[1].getPointer(), ranges[2].getPointer());
    }

    private void convexHull() throws NIVisionException {
        NIVision.convexHull(binImage_.image, binImage_.image, 1);
    }

    private void findParticles() throws NIVisionException {
        NIVision.particleFilter(binImage_.image, binImage_.image, criteria);
        NIVision.sizeFilter(binImage_.image, binImage_.image, true, 3, true);

        int numParticles = binImage_.getNumberParticles();
        if(numParticles <= 0) {
            reset();
            return;
        }
        topTarget_ = binImage_.getParticleAnalysisReport(0);
        for (int i = 1; i < numParticles; ++i) {
            ParticleAnalysisReport report = binImage_.getParticleAnalysisReport(i);
            if(report.center_mass_y < topTarget_.center_mass_y) {
                topTarget_ = report;
            }
        }
    }


    public synchronized boolean isAimed() {
        return 0 >= angleRange_[0] && 0 <= angleRange_[1];
    }

    private synchronized void findDetails() {
        // magnitude along an axis projecting directly from the center of the camera's lens
        double cameraZ = (Z_BASE * topTarget_.imageHeight) / topTarget_.boundingRectHeight;
        // magnitude along an axis in the direction of the camera, but parallel to the ground
        zLoc_ = cameraZ * CAMERA_ANGLE_COS;
        angleRange_[0] = topTarget_.boundingRectLeft / (double) topTarget_.imageWidth * FOV - FOV / 2;
        angleRange_[1] =  (topTarget_.boundingRectLeft + topTarget_.boundingRectWidth) / (double) topTarget_.imageWidth * FOV - FOV / 2;
    }
    private synchronized void reset() {
        topTarget_ = null;
        zLoc_ = angleRange_[0] = angleRange_[1] = Double.NaN;
    }

    private void sendData() {
        netTable_.beginTransaction();
            if(topTarget_ != null) {
                netTable_.putInt("X", topTarget_.boundingRectLeft);
                netTable_.putInt("Y", topTarget_.boundingRectTop);
                netTable_.putInt("Width", topTarget_.boundingRectWidth);
                netTable_.putInt("Height", topTarget_.boundingRectHeight);
                netTable_.putBoolean("Aimed", isAimed());
            } else {
                netTable_.putInt("X", 0);
                netTable_.putInt("Y", 0);
                netTable_.putInt("Width", 0);
                netTable_.putInt("Height", 0);
                netTable_.putBoolean("Aimed", false);
            }
        netTable_.endTransaction();
        RobotState.DASHBOARD_FEEDBACK.putDouble("target.distance", Math.floor(zLoc_*10+0.5)/10);
    }

    private void doAim() {
        try {
            retrieveImage();
            matchThreshold();
            convexHull();
            findParticles();
            findDetails();
        } catch (NIVisionException ex) {
            reset();
        } catch (AxisCameraException ex) {
            reset();
        }
        sendData();
    }

    public synchronized double getDistance() {
        return zLoc_;
    }

    public void startTracking() {
        tracking_ = true;
    }
    public void stopTracking() {
        tracking_ = false;
    }
}

