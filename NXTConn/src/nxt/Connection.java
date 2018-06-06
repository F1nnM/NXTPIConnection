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
import lejos.pc.comm.NXTComm;
import listener.RequestListener;
import main.Logger;

public class Connection {
	
	private static DataInputStream in;
	private static DataOutputStream out;
	private static ScheduledExecutorService Timer;
	private static NXTComm nxtComm;
	private static NXT nxt;
	private static StringBuilder queue = new StringBuilder();
	private static HashMap<NXTMessage, RequestListener> requests = new HashMap<>();
	
	protected static void conn(int delay, NXTComm nxtComm, NXT nxt) {
		Connection.nxt = nxt;
		Connection.nxtComm = nxtComm;
		in = new DataInputStream(nxtComm.getInputStream());
		out = new DataOutputStream(nxtComm.getOutputStream());
		
		Timer = Executors.newScheduledThreadPool(10);
		Timer.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				send();
				receive();
			}
		}, getCalendar().getTime().getTime() - Calendar.getInstance().getTimeInMillis() - 2, 10, TimeUnit.MILLISECONDS);
	}
	
	public static void addRequest(NXTMessage nxtMessage, RequestListener listener) {
		requests.put(nxtMessage, listener);
	}
	
	public void replyReceived(NXTMessage nxtMessage, String reply) {
		for (NXTMessage mess:requests.keySet()) {
			if(mess.equals(nxtMessage)) {
				requests.get(mess).AnswerArrived(reply);
			}
		}
	}

	public static void enqueue(NXTMessage nxtMessage) {
		queue.append(nxtMessage.toString()).append(":");
	}
	
	private static void send() {
		if(queue.length() != 0) {
			try {
				out.writeUTF(queue.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			queue = new StringBuilder();
		}
	}
	
	private static void receive() {
		try {
			if(in.available() > 0) {
				ArrayList<NXTMessage> messages = new ArrayList<>();
				for (String s:in.readUTF().split(":")) {
					messages.add(NXTMessage.toNxtMessage(s));
				}
				nxt.newMessageArrived(messages.toArray(new NXTMessage[0]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 2);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}

	public static void disconnect() {
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
