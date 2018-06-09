package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import datatypes.NXTMessage;
import lejos.nxt.LCD;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

/**
 * the Connection class
 */
public class Connection {

	private static USBConnection usb;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static Boolean connected;

	/**
	 * this method waits for a USB connection
	 */
	public static void connect() {
		LCD.drawString("waiting for connection", 0, 0);
		usb = USB.waitForConnection();
		out = usb.openDataOutputStream();
		in = usb.openDataInputStream();

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
				if (str.equals("bye")) {
					disconnect();
				} else if (str.equals("ping")) {
					out.writeUTF("pong");
					out.flush();
				} else if (str.equals("getSystemMillis")) {
					out.writeLong(System.currentTimeMillis());
					out.flush();
				} else {
					ArrayList<NXTMessage> messages = new ArrayList<>();
					for (String s : Utilities.split(str, ":")) {
						if (!s.isEmpty()) {
							messages.add(NXTMessage.toNxtMessage(s));
						}
					}
					Utilities.handleInput(false, messages.toArray(new NXTMessage[0]));
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
			out.writeUTF(nxtMessage.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method disconnects from the pi
	 */
	public static void disconnect() {
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
