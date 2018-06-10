package datatype;

import nxt.NXT;

public class Buttons {

	// Definition of the Buttons
	public final NXTButton ENTER;
	public final NXTButton LEFT;
	public final NXTButton RIGHT;
	public final NXTButton ESCAPE;

	/**
	 * the Constructor of Buttons, which creates all Buttons
	 */
	public Buttons(NXT nxt) {
		ENTER = new NXTButton("ENTER", nxt);
		LEFT = new NXTButton("LEFT", nxt);
		RIGHT = new NXTButton("RIGHT", nxt);
		ESCAPE = new NXTButton("ESCAPE", nxt);
	}

	/**
	 * this method converts a name of a button to a button
	 * 
	 * @param name
	 *            the name
	 * @return the Button
	 */
	public NXTButton getButton(String name) {
		switch (name) {
		case "ENTER":
			return ENTER;
		case "LEFT":
			return LEFT;
		case "RIGHT":
			return RIGHT;
		case "ESCAPE":
			return ESCAPE;
		default:
			return null;
		}
	}
}
