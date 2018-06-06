package datatype;

import java.util.ArrayList;
import java.util.List;

import listener.RequestListener;
import nxt.Connection;

public class Motor {

	public static final Motor A = new Motor("A");
	public static final Motor B = new Motor("B");
	public static final Motor C = new Motor("C");

	private String number;

	private Motor(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}

	public void setSpeed(float speed) {
		Connection.enqueue(NXTMessage.setSpeed(speed, this));
		
	}

	public int getTachoCount() {
		final List<Integer> answer = new ArrayList<>();   
		Connection.addRequest(NXTMessage.getTachoCount(this), new RequestListener() {
			@Override
			public void AnswerArrived(String reply) {
				answer.add(Integer.parseInt(reply));
			}
		});
		return answer.get(0);
	}

	public void resetTachoCount() {
		Connection.enqueue(NXTMessage.resetTachoCount(this));
	}

	public void rotate(int angle) {
		Connection.enqueue(NXTMessage.rotate(angle, this));
	}

	public void flt() {
		Connection.enqueue(NXTMessage.flt);
	}

	public boolean isMoving() {
		final List<Boolean> answer = new ArrayList<>();
		Connection.addRequest(NXTMessage.isMoving, new RequestListener() {
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
