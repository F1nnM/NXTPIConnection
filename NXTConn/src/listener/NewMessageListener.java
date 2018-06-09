package listener;

import datatype.NXTMessage;

/**
 * a listener to listen for new Messages
 */
public interface NewMessageListener {
	void onNewMessageArrived(NXTMessage... nxtMessage);
}
