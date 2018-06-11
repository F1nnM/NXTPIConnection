package main;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import util.Connection;

/**
 * the main class
 */
public class Main {

	/**
	 * the main methods
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Button.ESCAPE.addButtonListener(new ButtonListener() {

					@Override
					public void buttonReleased(Button b) {

					}

					@Override
					public void buttonPressed(Button b) {
						int counter = 0;
						while (Button.ESCAPE.isDown()) {
							counter++;
							if (counter > 2000) {
								Connection.disconnect();
								System.exit(1);
							}
							try {
								Thread.sleep(1);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		}).start();

		Connection.connect();
	}
}
