package listener;

import datatype.NXTMessage;

public interface NewMessageListener {
	void onNewMessageArrived(NXTMessage... nxtMessage);
}
