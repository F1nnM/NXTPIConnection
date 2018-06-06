package nxt;

import java.util.ArrayList;
import datatype.NXTMessage;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import listener.NewMessageListener;

public class NXT {

	private NXTComm nxtComm;
	private ArrayList<NewMessageListener> listeners = new ArrayList<>();

	public NXT() {
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
	}

	public void connect(String name, int delay) {
		try {
			if (nxtComm.search(name).length == 0) {
				System.err.println("No NXT found using USB");
				System.exit(1);
			}
		} catch (NXTCommException e) {
			Connection.disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		Connection.conn(delay, nxtComm, this);
	}

	public void connect(NXTInfo nxtInfo, int delay) {
		try {
			if (!nxtComm.open(nxtInfo)) {
				System.err.println("No NXT found using USB");
				System.exit(1);
			}
		} catch (NXTCommException e) {
			Connection.disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		Connection.conn(delay, nxtComm, this);
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