package nxt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Datatypes.NXTMessage;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class NXT {

	private DataInputStream in;
	private DataOutputStream out;
	private NXTComm nxtComm;
	private Timer timer;
	private ArrayList<NewMessageListener> listeners = new ArrayList<>();

	public NXT() {
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
	}

	public void connect(int delay, String name) {
		try {
			if (!nxtComm.open(nxtComm.search(name)[0])) {
				System.err.println("No NXT found using USB");
				System.exit(1);
			}
		} catch (NXTCommException e) {
			disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		conn(delay);
	}

	public void connect(int delay, NXTInfo nxtInfo) {
		try {
			if (!nxtComm.open(nxtInfo)) {
				System.err.println("No NXT found using USB");
				System.exit(1);
			}
		} catch (NXTCommException e) {
			disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		conn(delay);
	}

	private void conn(int delay) {
		in = new DataInputStream(nxtComm.getInputStream());
		out = new DataOutputStream(nxtComm.getOutputStream());

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
			nxtComm.close();
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