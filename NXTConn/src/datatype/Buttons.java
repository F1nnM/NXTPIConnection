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
}
