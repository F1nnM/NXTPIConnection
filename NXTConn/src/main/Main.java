package main;

import datatype.NXTMessage;
import listener.NewMessageListener;
import nxt.NXT;

/**
 * the main class of the NXTConn jar For testing purposes
 */
public class Main {

	/**
	 * the main method
	 * 
	 * @param args
	 *            the args, the first arg specifies the name of the NXT Brick to
	 *            connect to, the second arg specifies the sleep time between
	 *            sending new messages
	 */
	public static void main(String[] args) {
		Logger.debug();

		NXT nxt = new NXT();

		nxt.connect(((args.length == 0 && args[0].isEmpty()) ? "NXTCore1" : args[0]),
				((args.length == 0 && args[1].isEmpty()) ? 10 : Integer.parseInt(args[1])));
		nxt.addListener(new NewMessageListener() {

			@Override
			public void onNewMessageArrived(NXTMessage... nxtMessage) {
				Logger.log("New messages arrived!");
			}
		});
	}
}
