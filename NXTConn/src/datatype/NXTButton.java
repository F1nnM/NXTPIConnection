package datatype;

import java.util.ArrayList;

import listener.ButtonListener;

public class NXTButton {

	private static ArrayList<ButtonListener> listeners = new ArrayList<>();
	
	private String name;
	
	public NXTButton(String name) {
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
