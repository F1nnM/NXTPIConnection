package datatype;

/**
 * A class for messages
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NXTMessage {

    public static NXTMessage ok = new NXTMessage("ok");
    public static NXTMessage done = new NXTMessage("done");
    public static NXTMessage clearLCD = new NXTMessage("clear");
    public static NXTMessage flt = new NXTMessage("flt");
    public static NXTMessage isMoving = new NXTMessage("isMoving");
    
    private String message;
    private String[] values;

    /**
     * the Constructor of NXTMessage
     *
     * @param message the containing message
     */
    private NXTMessage(String message) {
        this.message = message;
        this.values = null;
    }

    /**
     * the Constructor of NXTMessage
     * @param message the containing message
     * @param value the value of the message
     */
    private NXTMessage(String message, String...values) {
        this.message = message;
        this.values = values;
    }
    
    

    /**
     * converts a String to a NXTMessage
     * @param in the String to convert
     * @return the NXTMessage
     */
    public static NXTMessage toNxtMessage(String in) {
        if (in.contains(",")) {
            String[] arr = in.split(",", 1);
            return new NXTMessage(arr[0], arr[1].split("."));
        } else {
            return new NXTMessage(in);
        }
    }
    
    public static NXTMessage buttonReleased(Button button) {
    	return new NXTMessage("buttonReleased", "Button." + button.getName());
    }
    
    public static NXTMessage buttonPressed(Button button) {
    	return new NXTMessage("buttonPressed", "Button." + button.getName());
    }
    
    public static NXTMessage getTachoCount(Motor motor) {
    	return new NXTMessage("getTachoCount", "Motor." + motor.getNumber());
    }
    
    public static NXTMessage resetTachoCount(Motor motor) {
    	return new NXTMessage("resetTachoCount", "Motor." + motor.getNumber());
    }
    
    public static NXTMessage buttonPressed(String buttonNumber) {
    	return new NXTMessage("buttonPressed", buttonNumber);
    }
    
    public static NXTMessage buttonReleased(String buttonNumber) {
    	return new NXTMessage("buttonReleased", buttonNumber);
    }
    
    public static NXTMessage drawLCD(String toDraw, int x, int y) {
    	return new NXTMessage("draw", toDraw + "." + x + "." + y);
    }
    
    public static NXTMessage setSpeed(float speed, Motor motor) {
        return new NXTMessage("setSpeed", String.valueOf(speed));
    }
	
	public static NXTMessage rotate(int angle, Motor motor){
        return new NXTMessage("rotate", angle + "");
    }

    /**
     * @return the message of the NXTMessage
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the value of the message; returns -1 if no value is given
     */
    public String[] getValues() {
        return values;
    }

    /**
     * converts the NXTMessage to a String
     * @return the resulting String
     */
    public String toString() {
    	if (values != null) {
    		StringBuilder builder = new StringBuilder();
    		for (String s:values) {
    			builder.append(s).append(".");
    		}
    		return message + "," + builder.toString();
    	}
        return message;
    }

    /**
     * checks if a NXTMessage equals another
     * @param nxtMessage the NXTMessage to compare to
     * @return a boolean
     */
    public boolean equals(NXTMessage nxtMessage) {
        return this.message.equals(nxtMessage.getMessage());
    }
}
