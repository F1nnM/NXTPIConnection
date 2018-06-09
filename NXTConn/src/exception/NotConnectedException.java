package exception;

import java.io.IOException;

/**
 * An Exception to throw, of the NXT is not connected
 */
public class NotConnectedException extends IOException{
	private static final long serialVersionUID = -4801333148439711075L;

	/**
	 * the constructor of NotConnectedException
	 */
	public NotConnectedException() {
		super();
	}
}
