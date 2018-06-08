package nxt;

import java.util.ArrayList;

import datatype.*;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import listener.NewMessageListener;

public class NXT {

	private Motors motors;
	private Buttons buttons;

	private Connection connection;
	private LCD lcd;
	private NXTComm nxtComm;
	private ArrayList<NewMessageListener> listeners = new ArrayList<>();

	public NXT() {
		buttons = new Buttons();
		motors = new Motors(this);
		connection = new Connection();
		lcd = new LCD(this);

		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
	}
	
	public Motors getMotors() {
		return motors;
	}

	public Buttons getButtons() {
		return buttons;
	}

	public Connection getConnection() {
		return connection;
	}

	public LCD getLCD() {
		return lcd;
	}

	public void waitForFinish() {
		connection.enqueue(NXTMessage.waitForFinish);
		final Boolean[] done = { false };
		addListener(new NewMessageListener() {

			@Override
			public void onNewMessageArrived(NXTMessage... nxtMessage) {
				for (NXTMessage message : nxtMessage) {
					if (message.equals(NXTMessage.done)) {
						done[0] = true;
					}
				}
			}
		});

		while (!done[0]) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void connect(String name, int delay) {
		try {
			if (nxtComm.search(name).length == 0) {
				System.err.println("No NXT found using USB");
				System.exit(1);
			}
		} catch (NXTCommException e) {
			connection.disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		connection.conn(delay, nxtComm, this);
	}

	public void connect(NXTInfo nxtInfo, int delay) {
		try {
			if (!nxtComm.open(nxtInfo)) {
				System.err.println("No NXT found using USB");
				System.exit(1);
			}
		} catch (NXTCommException e) {
			connection.disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		connection.conn(delay, nxtComm, this);
	}

	public void addListener(NewMessageListener listener) {
		listeners.add(listener);
	}

	protected void newMessageArrived(NXTMessage... nxtMessage) {
		for (NewMessageListener listener : listeners) {
			listener.onNewMessageArrived(nxtMessage);
		}
	}
}