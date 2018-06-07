package datatype;

import java.util.ArrayList;
import java.util.List;

import listener.RequestListener;
import nxt.Connection;

public class NXTMotor {
	
	private String number;
	private Connection connection;
	
	protected NXTMotor(String number, Connection connection) {
		this.connection = connection;
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}

	public void setSpeed(float speed) {
		connection.enqueue(NXTMessage.setSpeed(speed, this));
		
	}

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

	public void resetTachoCount() {
		connection.enqueue(NXTMessage.resetTachoCount(this));
	}

	public void rotate(int angle) {
		connection.enqueue(NXTMessage.rotate(angle, this));
	}

	public void flt() {
		connection.enqueue(NXTMessage.flt(this));
	}

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
