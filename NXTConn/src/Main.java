import Datatypes.NXTMessage;
import nxt.NXT;
import nxt.NewMessageListener;

public class Main {

	public static void main(String[] args) {
		NXT nxt1 = new NXT();
		nxt1.connect(10, "nxt1");
		nxt1.sendData(NXTMessage.forward(10));
		nxt1.addListener(new NewMessageListener() {
			
			@Override
			public void onNewMessageArrived(NXTMessage nxtMessage) {
				System.out.println("Message Arrived: " + nxtMessage.toString());
			}
		});
	}
}
