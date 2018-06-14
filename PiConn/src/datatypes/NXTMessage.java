package datatypes;

import util.Utilities;

/**
 * A class for messages
 */
public class NXTMessage {

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
	public static final NXTMessage continuous = new NXTMessage("continuous");
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
	 * @param value
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
		if (Utilities.contains(in, ".")) {
			String[] arr = Utilities.split(in, ".", 1);
			return new NXTMessage(arr[0], Utilities.split(arr[1], ","));
		} else {
			return new NXTMessage(in);
		}
	}

	/**
	 * this method says, if a button is pressed
	 * 
	 * @param button
	 *            the name of the pressed button
	 * @return the NXTMessage
	 */
	public static NXTMessage buttonPressed(String button) {
		return new NXTMessage("buttonPressed", button);
	}

	/**
	 * this method sends a distance of a UltrasonicSensor
	 * 
	 * @param distance
	 *            the distance to an object
	 * @param sensorPort
	 *            the port of the sensor
	 * @return the NXTMessage
	 */
	public static NXTMessage ultraSonicSensorData(int distance, String sensorPort) {
		return new NXTMessage("ultraSonicSensorData", String.valueOf(distance), sensorPort);
	}

	/**
	 * this method returns a NXTMessage to send the TachoCount
	 * 
	 * @param motor
	 *            the motor
	 * @param TachoCount
	 *            the TachoCount of the motor
	 * @return a NXTMessage
	 */
	public static NXTMessage getTachoCount(String motor, int TachoCount) {
		return new NXTMessage("getTachoCount", motor, TachoCount + "");
	}

	/**
	 * this method returns a NXTMessage to send, if a motor is moving
	 * 
	 * @param motor
	 *            the motor
	 * @param isMoving
	 *            a Boolean, if the motor is moving
	 * @return a NXTMessage
	 */
	public static NXTMessage isMoving(String motor, Boolean isMoving) {
		return new NXTMessage("isMoving", motor, ((isMoving) ? "true" : "false"));
	}

	/**
	 * @return the message of the NXTMessage
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the value of the message; returns -1 if no value is given
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
