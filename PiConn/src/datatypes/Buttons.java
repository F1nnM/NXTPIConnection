package datatypes;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import util.Connection;

/**
 * a class for the Buttons of the NXT
 */
public class Buttons {

	/**
	 * this method adds a Listener for buttonPress
	 * 
	 * @param buttonName
	 *            the name of the Button to listen for
	 */
	public static void buttonPressed(String buttonName) {
		toButton(buttonName).addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
				// This is Empty
			}

			@Override
			public void buttonPressed(Button b) {
				Connection.send(NXTMessage.buttonPressed(buttonName));
			}
		});
	}

	/**
	 * this method converts a String to a Button
	 * 
	 * @param buttonName
	 *            the name of the Button
	 * @return the Button
	 */
	private static Button toButton(String buttonName) {
		switch (buttonName) {
		case "ENTER":
			return Button.ENTER;
		case "LEFT":
			return Button.LEFT;
		case "RIGHT":
			return Button.RIGHT;
		case "ESCAPE":
			return Button.ESCAPE;
		default:
			return null;
		}
	}
}
