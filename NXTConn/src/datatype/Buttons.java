package datatype;

public class Buttons {

	public final NXTButton ENTER;
	public final NXTButton LEFT;
	public final NXTButton RIGHT;
	public final NXTButton ESCAPE;
	
	public Buttons() {
		ENTER = new NXTButton("ENTER");
		LEFT = new NXTButton("LEFT");
		RIGHT = new NXTButton("RIGHT");
		ESCAPE = new NXTButton("ESCAPE");
	}
}
