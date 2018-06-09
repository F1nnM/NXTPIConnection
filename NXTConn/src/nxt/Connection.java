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
import listener.RequestListener;

/**
 * a class for handling all the connection stuff between this PC and the NXT
 */
public class Connection {

	private DataInputStream in;
	private DataOutputStream out;
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
	 * @param nxtComm
	 *            the NXTComm with the NXTInfo
	 * @param nxt
	 *            an Instance of the NXT Class
	 */
	protected void conn(int delay, NXTComm nxtComm, NXT nxt) {
		this.nxt = nxt;
		this.nxtComm = nxtComm;
		in = new DataInputStream(nxtComm.getInputStream());
		out = new DataOutputStream(nxtComm.getOutputStream());

		connected = true;

		long[] SystemMillis = getSystemMillis();
		NXTMillis = SystemMillis[0];
		SyncMillis = SystemMillis[1];

		int counter = 0;
		while (ping() > 1 && (counter = counter + 1) < 100) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Timer = Executors.newScheduledThreadPool(10);
		Timer.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				send();
				receive();
			}
		}, getCalendar().getTime().getTime() - Calendar.getInstance().getTimeInMillis() - 2, delay,
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
	 * this method pings the NXT
	 * 
	 * @return the average ping time
	 */
	private double ping() {
		long time = 0;
		try {
			for (int i = 0; i < 10; i++) {
				out.writeUTF("ping");
				out.flush();
				long startTime = System.nanoTime();
				while (in.available() <= 0) {

				}
				time = time + (System.nanoTime() - startTime);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return time / 10.0;
	}

	/**
	 * this method gets the SystemMillis of the NXT
	 * 
	 * @return an long array, where the first value is the time of the NXT, the
	 *         second one the time of this PC, when the answer of the NXT was
	 *         received
	 */
	private long[] getSystemMillis() {
		try {
			out.writeUTF("getSystemMillis");
			out.flush();
			long[] ret = { in.readLong(), System.currentTimeMillis() };
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	 * this method enqueues a command
	 * 
	 * @param nxtMessage
	 *            the command
	 */
	public void enqueue(NXTMessage nxtMessage) {
		if (connected) {
			queue.append(nxtMessage.toString()).append(":");
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
			if (in.available() > 0) {
				String str = in.readUTF();
				if (str.equals("bye")) {
					disconnect();
				} else {
					ArrayList<NXTMessage> messages = new ArrayList<>();

					for (String s : str.split(":")) {
						if (!s.isEmpty()) {
							messages.add(NXTMessage.toNxtMessage(s));
						}
					}
					for (NXTMessage m : messages) {
						replyReceived(m);
					}
					nxt.newMessageArrived(messages.toArray(new NXTMessage[0]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
		calendar.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 2);
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
