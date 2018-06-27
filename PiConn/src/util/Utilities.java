package util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import datatypes.Buttons;
import datatypes.Motors;
import datatypes.NXTMessage;
import datatypes.UltrasonicSensors;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;

/**
 * a Utility class
 */
public class Utilities {

	private static ArrayList<NXTMessage> toRun = new ArrayList<>();
	private static Boolean wait = false;

	/**
	 * this method splits a String by a delimiter it works pretty much like the
	 * String.split() in native Java.
	 * 
	 * @param toSplit
	 *            the String
	 * @param delimiter
	 *            the delimiter
	 * @return an Array, containing the split String
	 */
	public static String[] split(String toSplit, String delimiter) {
		return split(toSplit, delimiter, -1);
	}

	/**
	 * this method splits a String by a delimiter it works pretty much like the
	 * String.split() in native Java.
	 * 
	 * @param toSplit
	 *            the String to split
	 * @param delimiter
	 *            the delimiter
	 * @param maxSplits
	 *            the max number of splits to be made
	 * @return an Array, containing the split String
	 */
	public static String[] split(String toSplit, String delimiter, int maxSplits) {
		ArrayList<String> elements = new ArrayList<>();
		StringTokenizer stringTokenizer = new StringTokenizer(toSplit, delimiter);
		int splits = 0;
		StringBuilder sb = new StringBuilder();
		while (stringTokenizer.hasMoreElements()) {
			if (splits < maxSplits || maxSplits == -1) {
				elements.add(stringTokenizer.nextToken());
			} else {
				sb.append(stringTokenizer.nextToken());
			}
			splits++;
		}

		if (sb.length() != 0)
			elements.add(sb.toString());

		ArrayList<String> toRemove = new ArrayList<>();
		for (String s : elements) {
			if (s.trim().isEmpty()) {
				toRemove.add(s);
			}
		}

		elements.removeAll(toRemove);

		return elements.toArray(new String[elements.size()]);
	}

	/**
	 * this method checks if a String contains another String
	 * 
	 * @param toCheck
	 *            the String to check for
	 * @param toSearchFor
	 *            the String to search for
	 * @return the Boolean if the String contains the String toSearchFor
	 */
	public static Boolean contains(String toCheck, String toSearchFor) {
		for (int i = 0; i < toCheck.length(); i++) {
			if (toCheck.substring(i).startsWith(toSearchFor)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this method handles the input from the pi
	 * 
	 * @param syncTask
	 *            a Boolean, if this is a synchronised task, used in
	 *            {@link #runSyncTask(long, NXTMessage...) runSyncTask} method
	 * @param messages
	 *            the new Messages to handle
	 */
	public static void handleInput(Boolean syncTask, NXTMessage... messages) {
		if (!wait) {
			for (NXTMessage m : messages) {
				if (m.equals(NXTMessage.clearLCD)) {
					LCD.clear();
				} else if (m.equals(NXTMessage.flt)) {
					Motors.flt(m.getValues()[0]);
				} else if (m.equals(NXTMessage.isMoving)) {
					Motors.isMoving(m.getValues()[0]);
				} else if (m.equals(NXTMessage.buttonPressed)) {
					Buttons.buttonPressed(m.getValues()[0]);
				} else if (m.equals(NXTMessage.getTachoCount)) {
					Motors.getTachoCount(m.getValues()[0]);
				} else if (m.equals(NXTMessage.resetTachoCount)) {
					Motors.resetTachoCount(m.getValues()[0]);
				} else if (m.equals(NXTMessage.drawLCD)) {
					LCD.drawString(m.getValues()[0], Integer.parseInt(m.getValues()[1]),
							Integer.parseInt(m.getValues()[2]));
				} else if (m.equals(NXTMessage.setSpeed)) {
					Motors.setSpeed(Float.parseFloat(m.getValues()[0]), m.getValues()[1]);
				} else if (m.equals(NXTMessage.rotate)) {
					Motors.rotate(Integer.parseInt(m.getValues()[0]), m.getValues()[1]);
				} else if (m.equals(NXTMessage.waitTillFinished)) {
					waitTillFinished();
				} else if (m.equals(NXTMessage.runSyncTask) & !syncTask) {
					runSyncTask(Long.parseLong(m.getValues()[0]), messages);
					wait = true;
					break;
				} else if (m.equals(NXTMessage.stop)) {
					Motors.stop(m.getValues()[0]);
				} else if (m.equals(NXTMessage.continuous)) {
					UltrasonicSensors.continuous(m.getValues()[0]);
				} else if (m.equals(NXTMessage.oneCentimetreTravelled)) {
					Motors.oneCentimentreTravelled(m.getValues()[0]);
				} else if (m.equals(NXTMessage.rotateTo)) {
					Motors.rotateTo(Integer.parseInt(m.getValues()[0]), m.getValues()[1]);
				} else if (m.equals(NXTMessage.forward)) {
					Motors.forward(m.getValues()[0]);
				}
			}
		} else {
			for (NXTMessage m : messages) {
				toRun.add(m);
			}
		}
	}

	/**
	 * this method runs a task at a specific time
	 * 
	 * @param runTime
	 *            the time to run the task at
	 * @param messages
	 *            the messages containing the commands
	 */
	private static void runSyncTask(long runTime, NXTMessage... messages) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					handleInput(true, toRun.toArray(new NXTMessage[toRun.size()]));
					toRun.clear();
				} catch (Exception e) {
					e.printStackTrace(RConsole.getPrintStream());
				}
			}
		}, runTime - System.currentTimeMillis());
	}

	/**
	 * this method waits until the NXT brick is finished
	 */
	private static void waitTillFinished() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while ((Motor.A.isMoving() & !Motor.A.isStalled()) || (Motor.B.isMoving() & !Motor.B.isStalled())
						|| (Motor.C.isMoving() & !Motor.C.isStalled())) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Connection.send(NXTMessage.done);
			}
		}).start();
	}
}
