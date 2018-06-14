package datatypes;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import util.Connection;

/**
 * a class for motors
 */
public class Motors {

	/**
	 * this method stops a motor
	 * 
	 * @param motor
	 *            the name of the motor to stop
	 */
	public static void stop(String motor) {
		getMotor(motor).stop();
	}

	/**
	 * this method returns the TachoCount of a motor
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void getTachoCount(String motor) {
		Connection.send(NXTMessage.getTachoCount(motor, getMotor(motor).getTachoCount()));
	}

	/**
	 * this method resets the TachoCount of a motor
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void resetTachoCount(String motor) {
		getMotor(motor).resetTachoCount();
	}

	/**
	 * this method sets the speed of a motor
	 * 
	 * @param speed
	 *            the speed
	 * @param motor
	 *            the name of the motor
	 */
	public static void setSpeed(float speed, String motor) {
		getMotor(motor).setSpeed(speed);
		getMotor(motor).forward();
	}

	/**
	 * this method rotates a motor
	 * 
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the name of the motor
	 */
	public static void rotate(int angle, String motor) {
		getMotor(motor).rotate(angle, true);
	}

	/**
	 * this method sets a motor into float mode
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void flt(String motor) {
		getMotor(motor).flt();
	}

	/**
	 * this method returns, if the motor is moving
	 * 
	 * @param motor
	 *            the name of the motor
	 */
	public static void isMoving(String motor) {
		Connection.send(NXTMessage.isMoving(motor, getMotor(motor).isMoving()));
	}

	/**
	 * this method converts a name of a motor to a motor
	 * 
	 * @param motorName
	 *            the name of the motor
	 * @return the motor
	 */
	private static NXTRegulatedMotor getMotor(String motorName) {
		switch (motorName) {
		case "A":
			return Motor.A;
		case "B":
			return Motor.B;
		case "C":
			return Motor.C;
		default:
			return null;
		}
	}
}
