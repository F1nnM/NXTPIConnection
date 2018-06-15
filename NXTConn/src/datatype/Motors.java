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

	/**
	 * get a NXTMotor from it's name
	 * 
	 * @param name
	 *            the name of the motor
	 * @return the NXTMotor
	 */
	public NXTMotor getMotor(String name) {
		switch (name) {
		case "A":
			return A;
		case "B":
			return B;
		case "C":
			return C;
		default:
			return null;
		}
	}

}
