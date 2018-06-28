package sql;

import java.util.ArrayList;

import datatype.NXTMotor;
import datatype.UltrasonicSensor;
import listener.OneCentimetreTravelledListener;
import listener.PositionListener;

/**
 * a Class for managing the Database
 */
public class DatabaseManager {

	private static UltrasonicSensor frontSensor;
	private static UltrasonicSensor rightSensor;
	private static UltrasonicSensor leftSensor;
	private static Boolean turning = false;
	private static double viewingDirection = 0.0;
	private static NXTMotor motor = null;
	private static double x = 0.0;
	private static double y = 0.0;
	private static ArrayList<PositionListener> listeners = new ArrayList<>();

	/**
	 * Initialize everything
	 * 
	 * @param motor
	 *            a main motor, used to accelerate the robot
	 */
	public static void init(NXTMotor motor) {
		SQL.connect("localhost", 3306, "data", "robot", "InsaneSecurePassword123");
		SQL.init();

		DatabaseManager.motor = motor;
	}

	/**
	 * add a positionListener to listen, if the robot has reached it's starting
	 * point
	 * 
	 * @param listener
	 */
	public static void addPositionListener(PositionListener listener) {
		listeners.add(listener);
	}

	/**
	 * set the the back right sensor
	 * 
	 * @param sensor
	 *            the sensor
	 */
	public static void setRightSensor(UltrasonicSensor sensor) {
		rightSensor = sensor;
	}

	/**
	 * set the back left sensor
	 * 
	 * @param sensor
	 *            the sensor
	 */
	public static void setLeftSensor(UltrasonicSensor sensor) {
		leftSensor = sensor;
	}

	/**
	 * set the front sensor
	 * 
	 * @param sensor
	 *            the sensor
	 */
	public static void setFrontSensor(UltrasonicSensor sensor) {
		frontSensor = sensor;
	}

	/**
	 * get the x-position of the robot
	 * 
	 * @return the x-position of the robot
	 */
	public static double getX() {
		return x;
	}

	/**
	 * get the y-position of the robot
	 * 
	 * @return the y-position of the robot
	 */
	public static double getY() {
		return y;
	}

	/**
	 * the main run method
	 */
	public static void run() {
		motor.addOneCentimetreTravelledListener(new OneCentimetreTravelledListener() {

			@Override
			public void travelledOneCentimetre() {
				if (!turning) {
					setValues();
				}

				if ((x < 1.0 || x > -1.0) && (y < 1.0 || y > -1.0)) {
					for (PositionListener listener : listeners) {
						listener.startReached();
					}
				}
			}
		});
	}

	/**
	 * save the values of the sensors to the Database
	 */
	private static void setValues() {
		x += Math.cos(viewingDirection);
		y += Math.sin(viewingDirection);

		if (frontSensor.getDistance() < 255) {
			SQL.save((int) ((frontSensor.getDistance() * Math.cos(viewingDirection)) + x),
					(int) ((frontSensor.getDistance() * Math.sin(viewingDirection)) + y));
		}

		if (rightSensor.getDistance() < 255) {
			SQL.save((int) ((rightSensor.getDistance() * Math.cos(viewingDirection + 90)) + x),
					(int) ((rightSensor.getDistance() * Math.sin(viewingDirection + 90)) + y));
		}

		if (leftSensor.getDistance() < 255) {
			SQL.save((int) ((leftSensor.getDistance() * Math.cos(viewingDirection - 90)) + x),
					(int) ((leftSensor.getDistance() * Math.sin(viewingDirection - 90)) + y));
		}
	}

	/**
	 * set the robot into turn mode. No data will be captured. After finishing
	 * turning call {@link #doneTurning(double) doneTurning} method and enter the
	 * turn angle, to start capturing
	 */
	public static void turning() {
		turning = true;
	}

	/**
	 * set the robot into normal mode
	 * 
	 * @param degrees
	 *            the turn angle
	 */
	public static void doneTurning(double degrees) {
		turning = false;
		viewingDirection += degrees;
		if ((viewingDirection % 360.0) > 1.0) {
			viewingDirection -= 360.0;
		}
	}

	/**
	 * get the current Viewing direction
	 * 
	 * @return the Viewing direction of the robot
	 */
	public static double getViewingDirection() {
		return viewingDirection;
	}

	/**
	 * disconnect from the Database
	 */
	public static void disconnect() {
		SQL.disconnect();
	}
}
