package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import datatypes.NXTMessage;
import lejos.nxt.LCD;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

/**
 * the Connection class
 */
public class Connection {

	private static NXTConnection usb;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static Boolean connected;

	/**
	 * this method waits for a USB connection
	 */
	public static void connect() {
		LCD.drawString("waiting for connection", 0, 0);
		while (usb == null) {
			usb = USB.waitForConnection();
		}
		out = usb.openDataOutputStream();
		in = usb.openDataInputStream();
		
		LCD.clear();
		LCD.drawString("Connected", 0, 0);

		connected = true;

		while (connected) {
			receive();
		}
	}

	/**
	 * this method handles new Messages
	 */
	private static void receive() {
		try {
			if (in.available() > 0) {
				String str = in.readUTF();
				ArrayList<NXTMessage> messages = new ArrayList<>();

				for (String s : Utilities.split(str, ":")) {
					if (s.equals("bye")) {
						disconnect();
					} else if (s.equals("ping")) {
						out.writeUTF("pong:");
						out.flush();
					} else if (s.equals("getSystemMillis")) {
						out.writeUTF(System.currentTimeMillis() + ":");
						out.flush();
					} else {
						messages.add(NXTMessage.toNxtMessage(s));
					}
				}

				if (!messages.isEmpty()) {
					Utilities.handleInput(false, messages.toArray(new NXTMessage[messages.size()]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method sends a NXTMessage to the pi
	 * 
	 * @param nxtMessage
	 *            the NXTMessage
	 */
	public static void send(NXTMessage nxtMessage) {
		try {
			out.writeUTF(nxtMessage.toString() + ":");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method disconnects from the pi
	 */
	public static synchronized void disconnect() {
		try {
			out.writeUTF("bye:");
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		connected = false;

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		usb.close();
	}
}
