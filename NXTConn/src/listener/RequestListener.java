package listener;

/**
 * a listener to listen for answered requests
 */
public interface RequestListener {
	void AnswerArrived(String reply);
}
