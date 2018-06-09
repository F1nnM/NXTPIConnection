package datatype;

import nxt.Connection;
import nxt.NXT;

/**
 * A class for the LCD of the NXT. For further information on how the LCD works,
 * visit the leJos NXJ documentation.
 */
public class LCD {

	private Connection connection;

	/**
	 * the Constructor of LCD
	 * 
	 * @param nxt
	 *            the NXT class to get the connection from
	 */
	public LCD(NXT nxt) {
		this.connection = nxt.getConnection();
	}

	/**
	 * the method draws a String on the LCD of the NXT
	 * 
	 * @param toDraw
	 *            the String to draw
	 * @param x
	 *            the x-position on the Display
	 * @param y
	 *            the y-position on the Display
	 */
	public void drawString(String toDraw, int x, int y) {
		connection.enqueue(NXTMessage.drawLCD(toDraw, x, y));
	}

	/**
	 * this method clears the LCD of the NXT
	 */
	public void clear() {
		connection.enqueue(NXTMessage.clearLCD);
	}
}
