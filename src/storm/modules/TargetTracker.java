package storm.modules;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.*;
import edu.wpi.first.wpilibj.image.*;
import java.util.TimerTask;

/**
 *
 * @author joe
 */
public class TargetTracker implements storm.interfaces.ITargetTracker {
	static final double PID_P = 0,PID_I = 0,PID_D = 0;
	static final double PID_INTERVAL = 0;

	static final int[][] THRESHOLD = {{0,150},
		{240,255},
		{200,255}};
	static final double FOV = 54;
	// z value required for vision target rectangle fill the view vertically
	static final double Z_BASE = 7.65;
	// offset of camera lens from center of robot
	static final double CAMERA_OFFSET = 1.65;

	AxisCamera camera_ = AxisCamera.getInstance();
	RobotDrive drive_;
	Gyro gyro_;

	PIDOutput turn_ = new PIDOutput() {
		public void pidWrite(double output) {
			/*
                                if(output < 0) {
                                    output = -output;
                                }
                                drive_.tankDrive(output, -output);
			 */
		}
	};
	PIDController pid_ = new PIDController(PID_P,PID_I,PID_D,new PIDSource() {
		public double pidGet() { return 0; }
	}, turn_);

	double angle_ = 0;
	int state_ = 0;
	ColorImage cameraImg_;
	BinaryImage image_;
	ParticleAnalysisReport topTarget_ = null;
	double[] angleRange_ = { Double.NaN,Double.NaN };
	double zLoc_ = Double.NaN;

	public TargetTracker() {}

	public TargetTracker(RobotDrive drive,int gyroPort) {
		drive_ = drive;
		gyro_ = new Gyro(gyroPort);
	}


	private void retrieveImage() {
		try {
			//angle_ = gyro_.getAngle();
			if(!camera_.freshImage())
				return;
			cameraImg_ = camera_.getImage();
			if(cameraImg_ != null) {
				++state_;
				DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, 1, "(" + cameraImg_.getWidth() + "," + cameraImg_.getHeight() + ")");
			}
		} catch (AxisCameraException ex) {
			ex.printStackTrace();
		} catch (NIVisionException ex) {
			ex.printStackTrace();
		}
	}
	private void matchThreshold() {
		try {
			image_ = null;
			image_ = cameraImg_.thresholdRGB(THRESHOLD[0][0], THRESHOLD[0][1], THRESHOLD[1][0], THRESHOLD[1][1], THRESHOLD[2][0], THRESHOLD[2][1]);
			++state_;
		} catch (NIVisionException ex) {
			ex.printStackTrace();
			if(image_ == null)
				state_ = 0;
		} finally {
			try {
				cameraImg_.free();
			} catch (NIVisionException ex) {
				ex.printStackTrace();
			}
		}
	}
	private void convexHull() {
		BinaryImage oldImage = image_;
		try {
			image_ = oldImage.convexHull(true);
			++state_;
		} catch (NIVisionException ex) {
			ex.printStackTrace();
			state_ = 0;
		} finally {
			try {
				oldImage.free();
			} catch (NIVisionException ex) {
				ex.printStackTrace();
			}
		}
	}
	private void findParticles() {
		//BinaryImage oldImage = image_;
		try {
			//image_ = image_.removeSmallObjects(true, 4);

			ParticleAnalysisReport[] particles = image_.getOrderedParticleAnalysisReports();
			if(particles == null || particles.length == 0)
				throw new NIVisionException("No particles found");
			topTarget_ = particles[0];
			for(int i=1;i<particles.length;++i)
				if(particles[i].center_mass_x < topTarget_.center_mass_x)
					topTarget_ = particles[i];
			++state_;
		} catch (NIVisionException ex) {
			ex.printStackTrace();
			state_ = 0;
		} finally {
			try {
				image_.free();
				//oldImage.free();
			} catch (NIVisionException ex1) {
				ex1.printStackTrace();
			}
		}
	}

	public synchronized boolean isAimed() {
		double angle = 0;//gyro_.getAngle();
		return angle >= angleRange_[0] && angle <= angleRange_[1];
	}
	private void findDetails() {
		zLoc_ = Z_BASE*topTarget_.imageWidth/topTarget_.boundingRectWidth;
		angleRange_[0] = angle_ + topTarget_.boundingRectLeft/(double)topTarget_.imageWidth*FOV - FOV/2;
		angleRange_[1] = angle_ + (topTarget_.boundingRectLeft+topTarget_.boundingRectWidth)/(double)topTarget_.imageWidth*FOV - FOV/2;
		pid_.setSetpoint((angleRange_[0]+angleRange_[1])/2);
		state_ = 0;
	}

	synchronized private void doAim() {
		System.gc();
		System.out.println("tracking target");
		String stateName = "";
		boolean once = false;
		state_ = 0;
		while(!once || state_ != 0) {
			once = true;
			switch(state_) {
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
				state_ = 0;
				return;
			}
			System.out.println("begin debug output");
			String line1 = "Setpoint: " + pid_.getSetpoint(),
			line2 = stateName,
			line3 = "Location: (?,?," + zLoc_ + ")",
			line4 = "";
			DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 1, line1);
			System.out.println(line1);
			DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, 1, line2);
			System.out.println(line2);
			DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, 1, line3);
			System.out.println(line3);

		}
	}

	public double getDistance() {
		return zLoc_;
	}

	java.util.Timer timer = new java.util.Timer();

	public void startLocking() {
		//pid_.enable();
		timer.schedule(new TimerTask() {

			public void run() {
				doAim();
			}

		}, 1/30,1/30);
	}
	public void stopLocking() {
		//pid_.disable();
		timer.cancel();
	}
}
