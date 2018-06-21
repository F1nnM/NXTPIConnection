package nxt;

import java.util.ArrayList;
import java.util.Calendar;

import datatype.*;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import listener.FinishedListener;
import listener.NewMessageListener;
import main.Logger;

/**
 * a class for the NXT
 */
public class NXT {

	private Motors motors;
	private Buttons buttons;
	private UltrasonicSensors ultrasonicSensors;
	private Connection connection;
	private LCD lcd;
	private NXTComm nxtComm;
	private ArrayList<NewMessageListener> listeners = new ArrayList<>();

	/**
	 * the constructor of NXT
	 */
	public NXT() {
		connection = new Connection();
		buttons = new Buttons(this);
		motors = new Motors(this);
		lcd = new LCD(this);
		ultrasonicSensors = new UltrasonicSensors(this);

		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
		} catch (NXTCommException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method forces the connection to net send any commands to the NXT; this
	 * method is meant to be used with the method sendSyncMessage()
	 */
	public void doNotSend() {
		connection.doNotSend();
	}

	/**
	 * this method sends a synchronized message
	 * 
	 * @return the time, when the commands in the messages should be executed
	 */
	public long sendSyncMessage() {
		Calendar c = connection.getCalendar();
		c.set(Calendar.SECOND, 1);
		sendSyncMessage(c.getTimeInMillis());

		return c.getTimeInMillis();
	}

	/**
	 * this method sends a synchronised message
	 * 
	 * @param runTime
	 *            the time, when the commands in the messages should be executed
	 */
	public void sendSyncMessage(long runTime) {
		connection.sendSyncMessage(runTime);
	}

	/**
	 * this method checks, if this PC is connected to a NXT
	 * 
	 * @return a Boolean, if a NXT is connected
	 */
	public Boolean isConnected() {
		return connection.isConnected();
	}

	/**
	 * this method returns the UltrasonicSensors
	 * 
	 * @return the UltrasonicSensors
	 */
	public UltrasonicSensors getUltrasonicSensors() {
		return ultrasonicSensors;
	}

	/**
	 * this method returns the motors of the NXT
	 * 
	 * @return the motors of the NXT
	 */
	public Motors getMotors() {
		return motors;
	}

	/**
	 * this method returns the buttons of the NXT
	 * 
	 * @return
	 */
	public Buttons getButtons() {
		return buttons;
	}

	/**
	 * this method returns the connection to the NXT; this method is not meant to be
	 * called by the user
	 * 
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * this method returns the LCD of the NXT
	 * 
	 * @return the LCD
	 */
	public LCD getLCD() {
		return lcd;
	}

	/**
	 * this method waits until the NXT has finished its actions
	 */
	public void waitTillFinished(FinishedListener listener) {
		connection.enqueue(NXTMessage.waitTillFinished);
		addListener(new NewMessageListener() {

			@Override
			public void onNewMessageArrived(NXTMessage... nxtMessage) {
				for (NXTMessage message : nxtMessage) {
					if (message.equals(NXTMessage.done)) {
						listener.onFinish();
					}
				}
			}
		});
	}

	/**
	 * this method connects to a NXT
	 * 
	 * @param name
	 *            the name of the NXt
	 * @param delay
	 *            the delay between sending arrays of commands
	 */
	public void connect(String name, int delay) {
		NXTInfo nxtInfo = null;
		Logger.log("Trying to connect to NXT with name: " + name);
		try {
			while (nxtComm.search(name).length == 0) {
				Logger.log("No NXT found using USB");
				Thread.sleep(1000);
			}
			nxtInfo = nxtComm.search(name)[0];
		} catch (NXTCommException | InterruptedException e) {
			connection.disconnect();
			e.printStackTrace();
			System.exit(1);
		}

		connection.conn(delay, this, nxtInfo);
	}

	/**
	 * this method adds a NewMessageListener
	 * 
	 * @param listener
	 *            the NewMessageListener
	 */
	public void addListener(NewMessageListener listener) {
		listeners.add(listener);
	}

	/**
	 * this method notifies all Listeners of a new Message
	 * 
	 * @param nxtMessages
	 *            the new messages
	 */
	protected void newMessageArrived(NXTMessage... nxtMessages) {
		for (NewMessageListener listener : listeners) {
			listener.onNewMessageArrived(nxtMessages);
		}
	}
}