package datatype;

import java.util.ArrayList;

import listener.ButtonListener;

public class Button {

	public static final Button ENTER = new Button("ENTER");
	public static final Button LEFT = new Button("LEFT");
	public static final Button RIGHT = new Button("RIGHT");
	public static final Button ESCAPE = new Button("RIGHT");

	private static ArrayList<ButtonListener> listeners = new ArrayList<>();
	
	private String name;
	
	private Button(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public void addButtonListener(ButtonListener listener) {
		listeners.add(listener);
	}
	
	public void buttonPressed() {
		for(ButtonListener l:listeners) {
			l.buttonPressed();
		}
	}
	
	public void buttonReleased() {
		for(ButtonListener l:listeners) {
			l.buttonReleased();
		}
	}
}
