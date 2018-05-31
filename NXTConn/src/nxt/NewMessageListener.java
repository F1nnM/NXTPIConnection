package nxt;

import Datatypes.NXTMessage;

public interface NewMessageListener {
	void onNewMessageArrived(NXTMessage nxtMessage);
}
