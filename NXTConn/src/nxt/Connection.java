package nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import datatype.NXTMessage;
import exception.NotConnectedException;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import listener.RequestListener;
import main.Logger;

/**
 * a class for handling all the connection stuff between this PC and the NXT
 */
public class Connection {

	private DataInputStream in = null;
	private DataOutputStream out = null;
	private ScheduledExecutorService Timer;
	private NXTComm nxtComm;
	private NXT nxt;
	private StringBuilder queue = new StringBuilder();
	private HashMap<NXTMessage, RequestListener> requests = new HashMap<>();
	private Boolean connected;
	private long NXTMillis;
	private long SyncMillis;
	private Boolean send = true;

	/**
	 * this method connects to a specific NXT Brick
	 * 
	 * @param delay
	 *            the delay between sending different messages
	 * @param nxt
	 *            an Instance of the NXT Class
	 */
	protected void conn(int delay, NXT nxt, NXTInfo info) {
		this.nxt = nxt;
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
			nxtComm.open(info);
		} catch (NXTCommException e1) {
			e1.printStackTrace();
		}
		in = new DataInputStream(nxtComm.getInputStream());
		out = new DataOutputStream(nxtComm.getOutputStream());

		connected = true;

		ping();

		getSystemMillis();

		startTimer(delay);

		Logger.done();
	}

	/**
	 * this method starts the main timer
	 */
	private void startTimer(int delay) {
		Timer = Executors.newSingleThreadScheduledExecutor();
		Timer.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				send();
				receive();
			}
		}, getCalendar().getTime().getTime() - Calendar.getInstance().getTime().getTime() - 2, delay,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * checks if the PC is connected to the NXT
	 * 
	 * @return a Boolean
	 */
	protected Boolean isConnected() {
		return connected;
	}

	/**
	 * this method manages the ping request to the NXT
	 */
	private void ping() {
		double pingTime;
		while ((pingTime = getPingTime()) > 10.0) {
			try {
				Logger.log("Ping time was greater than 10ms, is was: " + pingTime + ",retrying...");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Logger.log("Average ping time is: " + Math.round(pingTime * 1000) / 1000.0 + "ms");
	}

	/**
	 * this method sends a ping request the NXT
	 * 
	 * @return the average ping time
	 */
	private double getPingTime() {
		double time = 0;
		try {
			for (int i = 0; i < 100; i++) {
				out.writeUTF("ping");
				out.flush();
				long startTime = System.nanoTime();
				in.readUTF();
				time = time + ((System.nanoTime() - startTime) / 1000000.0);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return time / 100.0;
	}

	/**
	 * this method gets the SystemMillis of the NXT
	 */
	private void getSystemMillis() {
		try {
			out.writeUTF("getSystemMillis:");
			out.flush();
			String str = in.readUTF();
			for (String s : str.split(":")) {
				if (isNumeric(s)) {
					long[] SystemMillis = { Long.parseLong(s), System.currentTimeMillis() };
					NXTMillis = SystemMillis[0];
					SyncMillis = SystemMillis[1];

					Logger.log("Got NXT's current time: " + NXTMillis);
					Logger.log("Local time: " + SyncMillis);
				}
			}
			receive(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks, if a String is Numeric. Source: https://stackoverflow.com/a/2563653
	 * 
	 * @param str
	 *            the String to check
	 * @return a Boolean, if the String is numeric
	 */
	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * this method sets a Boolean to not send the messages to the NXT
	 */
	protected void doNotSend() {
		send = false;
	}

	/**
	 * this method is for sending synchronised messages
	 * 
	 * @param runTime
	 *            the time, when the commands should be executed
	 */
	protected void sendSyncMessage(long runTime) {
		long time = (runTime - SyncMillis) + NXTMillis;
		queue.insert(0, NXTMessage.runSyncTask(time).toString());
	}

	/**
	 * this method requests some info from the NXT
	 * 
	 * @param nxtMessage
	 *            the request
	 * @param listener
	 *            the listener for the answer
	 */
	public void addRequest(NXTMessage nxtMessage, RequestListener listener) {
		if (connected) {
			requests.put(nxtMessage, listener);
		} else {
			try {
				throw new NotConnectedException();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * this method notifies all listeners, if there's a reply for their answer
	 * 
	 * @param nxtMessage
	 *            the reply
	 */
	public void replyReceived(NXTMessage nxtMessage) {
		for (NXTMessage mess : requests.keySet()) {
			if (mess.equals(nxtMessage)) {
				requests.get(mess).AnswerArrived(nxtMessage.getValues()[0]);
			}
		}
	}

	/**
	 * this method handles the Buttons
	 * 
	 * @param nxtMessage
	 *            a NXTMessage
	 */
	private void handleButtons(NXTMessage nxtMessage) {
		if (nxtMessage.equals(NXTMessage.buttonPressed)) {
			nxt.getButtons().getButton(nxtMessage.getValues()[0]).buttonPressed();
		}
	}

	/**
	 * handle Ultrasonic Sensor messages
	 * 
	 * @param nxtMessage
	 *            the message to handle
	 */
	private void handleUltrasonicSensors(NXTMessage nxtMessage) {
		if (nxtMessage.equals(NXTMessage.ultraSonicSensorData)) {
			nxt.getUltrasonicSensors().getUltrasonicSensor(nxtMessage.getValues()[1])
					.setDistance(Integer.parseInt(nxtMessage.getValues()[0]));
		}
	}

	/**
	 * handle motor messages
	 * 
	 * @param nxtMessage
	 *            the message to handle
	 */
	private void handleMotors(NXTMessage nxtMessage) {
		if (nxtMessage.equals(NXTMessage.oneCentimetreTravelled)) {
			nxt.getMotors().getMotor(nxtMessage.getValues()[0]).oneCentimetreTravelled();
		}
	}

	/**
	 * this method enqueues a command
	 * 
	 * @param nxtMessage
	 *            the command
	 */
	public void enqueue(NXTMessage nxtMessage) {
		if (connected) {
			if (send) {
				try {
					Logger.log("Sending message: " + nxtMessage.toString());
					out.writeUTF(nxtMessage.toString());
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				queue.append(nxtMessage.toString()).append(":");
			}
		} else {
			try {
				throw new NotConnectedException();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * this method sends all commands
	 */
	private void send() {
		if (queue.length() != 0 && send) {
			try {
				out.writeUTF(queue.toString());
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			queue = new StringBuilder();
		}
	}

	/**
	 * this method handles new Messages from the NXT
	 */
	private void receive() {
		try {
			receive(in.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method handles new messages with a given String
	 * 
	 * @param data
	 *            the String to handle
	 */
	private void receive(String data) {
		ArrayList<NXTMessage> messages = new ArrayList<>();

		for (String s : data.split(":")) {
			if (!s.isEmpty()) {
				if (s.equals("bye")) {
					disconnect();
				} else {
					messages.add(NXTMessage.toNxtMessage(s));
				}
			}
		}

		for (NXTMessage m : messages) {
			replyReceived(m);
			handleButtons(m);
			handleUltrasonicSensors(m);
			handleMotors(m);
		}

		if (!messages.isEmpty()) {
			nxt.newMessageArrived(messages.toArray(new NXTMessage[0]));
		}
	}

	/**
	 * this method returns a Calendar with the current time + 2 seconds and 0
	 * Milliseconds
	 * 
	 * @return the Calendar
	 */
	public Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 10);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	/**
	 * this method disconnects from the NXT
	 */
	public void disconnect() {
		connected = false;

		Timer.shutdownNow();

		try {
			out.writeUTF("bye");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			nxtComm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
