package datatype;

import java.util.ArrayList;
import java.util.List;

import listener.OneCentimetreTravelledListener;
import listener.RequestListener;
import nxt.Connection;

/**
 * A class for the motors of the NXT. For further information on how the motors
 * works, visit the leJos NXJ documentation.
 */
public class NXTMotor {

	private String number;
	private Connection connection;
	private ArrayList<OneCentimetreTravelledListener> listeners;

	/**
	 * the Constructor of NXTMotor
	 * 
	 * @param number
	 *            the number of the motor; e.g. "A", "B" or "C"
	 * @param connection
	 */
	protected NXTMotor(String number, Connection connection) {
		this.connection = connection;
		this.number = number;
		listeners = new ArrayList<>();
	}

	/**
	 * add a listener to listen for every travelled centimetre
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addOneCentimetreTravelledListener(OneCentimetreTravelledListener listener) {
		if (listeners.isEmpty()) {
			connection.enqueue(NXTMessage.oneCentimetreTravelled(this));
		}
		listeners.add(listener);
	}

	/**
	 * notify all listeners
	 */
	public void oneCentimetreTravelled() {
		for (OneCentimetreTravelledListener l : listeners) {
			l.travelledOneCentimetre();
		}
	}

	/**
	 * @return the number of the motor
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * this method sets the speed of the motor
	 * 
	 * @param speed
	 *            the speed of the motor
	 */
	public void setSpeed(float speed) {
		connection.enqueue(NXTMessage.setSpeed(speed, this));
	}

	/**
	 * this method stops the motor
	 */
	public void stop() {
		connection.enqueue(NXTMessage.stop(this));
	}

	/**
	 * this method returns the TachoCount of the motor
	 * 
	 * @return the TachoCount
	 */
	public int getTachoCount() {
		final List<Integer> answer = new ArrayList<>();
		connection.addRequest(NXTMessage.getTachoCount(this), new RequestListener() {
			@Override
			public void AnswerArrived(String reply) {
				answer.add(Integer.parseInt(reply));
			}
		});
		return answer.get(0);
	}

	/**
	 * this method resets the TachoCount of the motor
	 */
	public void resetTachoCount() {
		connection.enqueue(NXTMessage.resetTachoCount(this));
	}

	/**
	 * this method rotates the motor to a specific angle
	 * 
	 * @param angle
	 *            the angle to rotate to
	 */
	public void rotate(int angle) {
		connection.enqueue(NXTMessage.rotate(angle, this));
	}

	/**
	 * this method runs 'flt' on the NXT for the motor
	 */
	public void flt() {
		connection.enqueue(NXTMessage.flt(this));
	}

	/**
	 * this method checks, if the motor is moving
	 * 
	 * @return a boolean indicating if the motor is moving
	 */
	public boolean isMoving() {
		final List<Boolean> answer = new ArrayList<>();
		connection.addRequest(NXTMessage.isMoving(this), new RequestListener() {
			@Override
			public void AnswerArrived(String reply) {
				if (reply.equals("true")) {
					answer.add(true);
				} else if (reply.equals("false")) {
					answer.add(false);
				}
			}
		});
		return answer.get(0);
	}
}
