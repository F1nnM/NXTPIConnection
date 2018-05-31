package nxt;

public class NXTMessage {

	private static String sforward = "forward";
	private static String sfollowWall = "followWall";
	private static String ssearchWall = "searchWall";

	public static NXTMessage forward = new NXTMessage(sforward);
	public static NXTMessage followWall = new NXTMessage(sfollowWall);
	public static NXTMessage searchWall = new NXTMessage(ssearchWall);

	private String message;
	private int value;

	private NXTMessage(String message) {
		this.message = message;
		this.value = -1;
	}

	private NXTMessage(String message, int value) {
		this.message = message;
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return message + ((value != -1) ? ":" + value : "");
	}

	public static NXTMessage toNxtMessage(String in) {
		if (in.contains(":")) {
			String[] arr = in.split(":");
			return new NXTMessage(arr[0], Integer.parseInt(arr[1]));
		} else {
			return new NXTMessage(in);
		}
	}

	public boolean equals(NXTMessage nxtMessage) {
		return this.message.equals(nxtMessage.getMessage());
	}

	public static NXTMessage forward(int value) {
		return new NXTMessage(sforward, value);
	}
}
