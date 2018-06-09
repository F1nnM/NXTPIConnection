package datatype;

import nxt.Connection;
import nxt.NXT;

public class Motors {

	private Connection connection;

	public final NXTMotor A;
	public final NXTMotor B;
	public final NXTMotor C;

	/**
	 * the constructor of Motors, which creates all single Motors
	 * 
	 * @param nxt
	 *            an Instance of the NXT class, which should be connected to a NXT
	 *            Brick
	 */
	public Motors(NXT nxt) {
		connection = nxt.getConnection();
		A = new NXTMotor("A", connection);
		B = new NXTMotor("B", connection);
		C = new NXTMotor("C", connection);
	}

}
