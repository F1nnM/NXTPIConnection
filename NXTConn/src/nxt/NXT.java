package nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lejos.pc.comm.NXTConnector;

public class NXT {

	private DataInputStream in;
	private DataOutputStream out;
	private NXTConnector conn;
	private Timer timer;
	private ArrayList<NewMessageListener> listeners = new ArrayList<>();

	public void connect(int delay) {
		conn = new NXTConnector();

		if (!conn.connectTo("usb://")) {
			System.err.println("No NXT found using USB");
			System.exit(1);
		}

		in = new DataInputStream(conn.getInputStream());
		out = new DataOutputStream(conn.getOutputStream());

		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					if (in.available() > 0) {
						newMessageArrived(NXTMessage.toNxtMessage(in.readUTF()));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, delay);
	}

	public void sendData(NXTMessage nxtMessage) {
		try {
			out.writeUTF(nxtMessage.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
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
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addListener(NewMessageListener listener) {
		listeners.add(listener);
	}

	private void newMessageArrived(NXTMessage nxtMessage) {
		for (NewMessageListener listener : listeners) {
			listener.onNewMessageArrived(nxtMessage);
		}
	}
}