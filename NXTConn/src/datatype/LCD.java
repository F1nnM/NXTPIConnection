package datatype;

import nxt.Connection;

public class LCD {

	public static void drawString(String toDraw, int x, int y) {
		Connection.enqueue(NXTMessage.drawLCD(toDraw, x, y));
	}
	
	public static void clear() {
		Connection.enqueue(NXTMessage.clearLCD);
	}
}
