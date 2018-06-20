package main;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import datatype.SensorPort;
import datatype.UltrasonicSensor;
import listener.ButtonListener;
import nxt.NXT;

/**
 * the main class of the NXTConn jar For testing purposes
 */
public class Main {

	/**
	 * the main method
	 * 
	 * @param args
	 *            the args, the first arg specifies the name of the NXT Brick to
	 *            connect to, the second arg specifies the sleep time between
	 *            sending new messages
	 */
	public static void main(String[] args) {
		Logger.debug();

		NXT nxt = new NXT();

		nxt.connect(((args.length == 0 || args[0].isEmpty()) ? "NXTCore1" : args[0]),
				((args.length < 2 || args[1].isEmpty()) ? 10 : Integer.parseInt(args[1])));

		nxt.getLCD().clear();
		nxt.getLCD().drawString("HELO", 0, 0);

		nxt.getButtons().ENTER.addButtonListener(new ButtonListener() {

			@Override
			public void buttonPressed() {
				Logger.log("ENTER");
			}
		});

		nxt.getButtons().ESCAPE.addButtonListener(new ButtonListener() {
			
			@Override
			public void buttonPressed() {
				Logger.log("ESCAPE");
			}
		});

		nxt.getButtons().LEFT.addButtonListener(new ButtonListener() {

			@Override
			public void buttonPressed() {
				Logger.log("LEFT");
			}
		});

		nxt.getButtons().RIGHT.addButtonListener(new ButtonListener() {

			@Override
			public void buttonPressed() {
				Logger.log("RIGHT");
			}
		});
		
		UltrasonicSensor s1 = nxt.getUltrasonicSensors().getUltrasonicSensor(SensorPort.S1);
		s1.continuous();
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				Logger.log(s1.getDistance() + "");
			}
		}, 0, 10);

		Timer readCommandLine = new Timer();
		Scanner scanner = new Scanner(System.in);
		readCommandLine.schedule(new TimerTask() {

			@Override
			public void run() {
				if (scanner.hasNext()) {
					if (scanner.next().equals("exit")) {
						scanner.close();
						nxt.getConnection().disconnect();
						readCommandLine.cancel();
						System.exit(0);
					}
				}
			}
		}, 0, 1000);
	}
}
