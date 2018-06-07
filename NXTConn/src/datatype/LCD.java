package datatype;

import nxt.Connection;
import nxt.NXT;

public class LCD {

	private Connection connection;
	
	public LCD(NXT nxt) {
		this.connection = nxt.getConnection();
	}
	
	public void drawString(String toDraw, int x, int y) {
		connection.enqueue(NXTMessage.drawLCD(toDraw, x, y));
	}
	
	public void clear() {
		connection.enqueue(NXTMessage.clearLCD);
	}
}
