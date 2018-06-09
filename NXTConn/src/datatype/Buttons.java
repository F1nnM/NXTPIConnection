package datatype;

public class Buttons {

	// Definition of the Buttons
	public final NXTButton ENTER;
	public final NXTButton LEFT;
	public final NXTButton RIGHT;
	public final NXTButton ESCAPE;

	/**
	 * the Constructor of Buttons, which creates all Buttons
	 */
	public Buttons() {
		ENTER = new NXTButton("ENTER");
		LEFT = new NXTButton("LEFT");
		RIGHT = new NXTButton("RIGHT");
		ESCAPE = new NXTButton("ESCAPE");
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
