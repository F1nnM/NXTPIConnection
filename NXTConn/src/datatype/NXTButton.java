package datatype;

import java.util.ArrayList;

import listener.ButtonListener;

/**
 * A class for the buttons of the NXT. For further information on how the
 * buttons works, visit the leJos NXJ documentation.
 */
public class NXTButton {

	private static ArrayList<ButtonListener> listeners = new ArrayList<>();

	private String name;

	/**
	 * the constructor of NXTButton
	 * 
	 * @param name
	 *            the name of the Button; e.g. "LEFT", "RIGHT", "ENTER" or "ESCAPE"
	 */
	public NXTButton(String name) {
		this.name = name;
	}

	/**
	 * @return the name of the Button
	 */
	public String getName() {
		return name;
	}

	/**
	 * add a Listener for this Button
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addButtonListener(ButtonListener listener) {
		listeners.add(listener);
	}

	/**
	 * notifies all listeners, that the button is pressed
	 */
	public void buttonPressed() {
		for (ButtonListener l : listeners) {
			l.buttonPressed();
		}
	}

	/**
	 * notifies all listeners, that the Button is released
	 */
	public void buttonReleased() {
		for (ButtonListener l : listeners) {
			l.buttonReleased();
		}
	}
}
