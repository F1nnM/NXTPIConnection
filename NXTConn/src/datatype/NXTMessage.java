package datatype;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A class for messages
 */
public class NXTMessage {

	// Definition of all NXTMessages
	public static final NXTMessage ok = new NXTMessage("ok");
	public static final NXTMessage done = new NXTMessage("done");
	public static final NXTMessage clearLCD = new NXTMessage("clear");
	public static final NXTMessage flt = new NXTMessage("flt");
	public static final NXTMessage isMoving = new NXTMessage("isMoving");
	public static final NXTMessage buttonPressed = new NXTMessage("buttonPressed");
	public static final NXTMessage getTachoCount = new NXTMessage("getTachoCount");
	public static final NXTMessage resetTachoCount = new NXTMessage("resetTachoCount");
	public static final NXTMessage drawLCD = new NXTMessage("drawLCD");
	public static final NXTMessage setSpeed = new NXTMessage("setSpeed");
	public static final NXTMessage rotate = new NXTMessage("rotate");
	public static final NXTMessage waitTillFinished = new NXTMessage("waitTillFinished");
	public static final NXTMessage runSyncTask = new NXTMessage("runSyncTask");
	public static final NXTMessage ultraSonicSensorData = new NXTMessage("ultraSonicSensorData");
	public static final NXTMessage oneCentimetreTravelled = new NXTMessage("oneCentimetreTravelled");
	public static final NXTMessage stop = new NXTMessage("stop");

	private String message;
	private String[] values;

	/**
	 * the Constructor of NXTMessage
	 *
	 * @param message
	 *            the containing message
	 */
	private NXTMessage(String message) {
		this.message = message;
		this.values = null;
	}

	/**
	 * the Constructor of NXTMessage
	 * 
	 * @param message
	 *            the containing message
	 * @param values
	 *            the value of the message
	 */
	private NXTMessage(String message, String... values) {
		this.message = message;
		this.values = values;
	}

	/**
	 * converts a String to a NXTMessage
	 * 
	 * @param in
	 *            the String to convert
	 * @return the NXTMessage
	 */
	public static NXTMessage toNxtMessage(String in) {
		if (in.contains(".")) {
			String[] arr = split(in, ".");
			return new NXTMessage(arr[0], arr[1].split(","));
		} else {
			return new NXTMessage(in);
		}
	}

	/**
	 * sends a NXTMessage to rotate a motor to a specific angle
	 * 
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the motor to rotate
	 * @return the NXTMessage
	 */
	public static NXTMessage rotateTo(int angle, NXTMotor motor) {
		return new NXTMessage("rotateTo", String.valueOf(angle), motor.getNumber());
	}
	
	public static NXTMessage forward(NXTMotor motor) {
		return new NXTMessage("forward", motor.getNumber());
	}

	/**
	 * requests to send a message every travelled centimetre
	 * 
	 * @param motor
	 *            the motor to listen
	 * @return the NXTMessage
	 */
	public static NXTMessage oneCentimetreTravelled(NXTMotor motor) {
		return new NXTMessage("oneCentimetreTravelled", motor.getNumber());
	}

	/**
	 * this method splits a String by a delimiter it works pretty much like the
	 * String.split() in native Java.
	 * 
	 * @param toSplit
	 *            the String
	 * @param delimiter
	 *            the delimiter
	 * @return an Array, containing the split String
	 */
	public static String[] split(String toSplit, String delimiter) {
		ArrayList<String> elements = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(toSplit, delimiter);
		while (stringTokenizer.hasMoreElements()) {
			elements.add(stringTokenizer.nextToken());
		}
		return elements.toArray(new String[elements.size()]);
	}

	/**
	 * this method returns a NXTMessage to run a synchronised Task
	 * 
	 * @param time
	 *            the time to run the Task at
	 * @return the NXTMessage
	 */
	public static NXTMessage runSyncTask(long time) {
		return new NXTMessage("runSyncTask", time + "");
	}

	/**
	 * set a Ultrasonic Sensor in continuous mode
	 * 
	 * @param sensor
	 *            the sensor
	 * @return the NXTMessage
	 */
	public static NXTMessage continuous(UltrasonicSensor sensor) {
		return new NXTMessage("continuous", sensor.getSensorPort().getName());
	}

	/**
	 * this method returns a NXTMessage to listen for Button press
	 * 
	 * @param button
	 *            the Button to listen for
	 * @return the NXTMessage
	 */
	public static NXTMessage buttonPressed(NXTButton button) {
		return new NXTMessage("buttonPressed", button.getName());
	}

	/**
	 * this method returns a NXTMessage to get the TachoCount of a motor
	 * 
	 * @param motor
	 *            the motor to get the TachoCount from
	 * @return the NXTMessage
	 */
	public static NXTMessage getTachoCount(NXTMotor motor) {
		return new NXTMessage("getTachoCount", motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to reset the TachoCount of a motor
	 * 
	 * @param motor
	 *            the motor to reset the TachoCount for
	 * @return the NXTMessage
	 */
	public static NXTMessage resetTachoCount(NXTMotor motor) {
		return new NXTMessage("resetTachoCount", motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to draw a String on the LCD of the NXT
	 * 
	 * @param toDraw
	 *            the String to draw
	 * @param x
	 *            the x-position of the String
	 * @param y
	 *            the y-position of the String
	 * @return the NXTMessage
	 */
	public static NXTMessage drawLCD(String toDraw, int x, int y) {
		return new NXTMessage("drawLCD", toDraw, x + "", y + "");
	}

	/**
	 * this method returns a NXTMessage to set the speed of a motor
	 * 
	 * @param speed
	 *            the speed to set
	 * @param motor
	 *            the motor to set the speed for
	 * @return the NXTMessage
	 */
	public static NXTMessage setSpeed(float speed, NXTMotor motor) {
		return new NXTMessage("setSpeed", String.valueOf(speed), motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to rotate a motor to a specific angle
	 * 
	 * @param angle
	 *            the angle to rotate to
	 * @param motor
	 *            the motor to rotate
	 * @return the NXTMessage
	 */
	public static NXTMessage rotate(int angle, NXTMotor motor) {
		return new NXTMessage("rotate", angle + "", motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to set a motor into float mode
	 * 
	 * @param motor
	 *            the motor to set into float mode
	 * @return the NXTMessage
	 */
	public static NXTMessage flt(NXTMotor motor) {
		return new NXTMessage("flt", motor.getNumber());
	}

	/**
	 * this method returns a NXTMessage to check, if a motor is Moving
	 * 
	 * @param motor
	 *            the motor to check
	 * @return the NXTMessage
	 */
	public static NXTMessage isMoving(NXTMotor motor) {
		return new NXTMessage("isMoving", motor.getNumber());
	}

	public static NXTMessage stop(NXTMotor motor) {
		return new NXTMessage("stop", motor.getNumber());
	}

	/**
	 * @return the message of the NXTMessage
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the values of the message; returns null if no value is given
	 */
	public String[] getValues() {
		return values;
	}

	/**
	 * converts the NXTMessage to a String
	 * 
	 * @return the resulting String
	 */
	public String toString() {
		if (values != null) {
			StringBuilder builder = new StringBuilder();
			for (String s : values) {
				builder.append(s).append(",");
			}
			return message + "." + builder.toString();
		}
		return message;
	}

	/**
	 * checks if a NXTMessage equals another
	 * 
	 * @param nxtMessage
	 *            the NXTMessage to compare to
	 * @return a boolean
	 */
	public boolean equals(NXTMessage nxtMessage) {
		return this.message.equals(nxtMessage.getMessage());
	}
}
