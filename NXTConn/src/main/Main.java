package main;

import java.util.Calendar;

import datatype.NXTMessage;
import listener.NewMessageListener;
import nxt.NXT;

public class Main {

	public static void main(String[] args) {
		Logger.debug();
		
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		today.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));
		today.set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND) + 2);
		today.set(Calendar.MILLISECOND, 0);
		
		
		
		NXT nxt = new NXT();
		nxt.connect(((args.length == 0 && args[0].isEmpty()) ? "NXTCore1" : args[0]), ((args.length == 0 && args[1].isEmpty()) ? 10 : Integer.parseInt(args[1])));
		nxt.addListener(new NewMessageListener() {

			@Override
			public void onNewMessageArrived(NXTMessage... nxtMessage) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
