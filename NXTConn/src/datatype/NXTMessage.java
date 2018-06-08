package datatype;

/**
 * A class for messages
 */
public class NXTMessage {

    public static final NXTMessage ok = new NXTMessage("ok");
    public static final NXTMessage done = new NXTMessage("done");
    public static final NXTMessage clearLCD = new NXTMessage("clear");
    public static final NXTMessage flt = new NXTMessage("flt");
    public static final NXTMessage isMoving = new NXTMessage("isMoving");
    public static final NXTMessage buttonReleased = new NXTMessage("buttonReleased");
    public static final NXTMessage buttonPressed = new NXTMessage("buttonPressed");
    public static final NXTMessage getTachoCount = new NXTMessage("getTachoCount");
    public static final NXTMessage resetTachoCount= new NXTMessage("resetTachoCount");
    public static final NXTMessage drawLCD = new NXTMessage("drawLCD");
    public static final NXTMessage setSpeed = new NXTMessage("setSpeed");
    public static final NXTMessage rotate = new NXTMessage("rotate");
    public static final NXTMessage waitForFinish = new NXTMessage("waitForFinish");
    
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
        if (in.contains("\\.")) {
            String[] arr = in.split("\\.", 1);
            return new NXTMessage(arr[0], arr[1].split(","));
        } else {
            return new NXTMessage(in);
        }
    }
    
    public static NXTMessage buttonReleased(NXTButton button) {
    	return new NXTMessage("buttonReleased", button.getName());
    }
    
    public static NXTMessage buttonPressed(NXTButton button) {
    	return new NXTMessage("buttonPressed", button.getName());
    }
    
    public static NXTMessage getTachoCount(NXTMotor motor) {
    	return new NXTMessage("getTachoCount", motor.getNumber());
    }
    
    public static NXTMessage resetTachoCount(NXTMotor motor) {
    	return new NXTMessage("resetTachoCount", motor.getNumber());
    }
    
    public static NXTMessage drawLCD(String toDraw, int x, int y) {
    	return new NXTMessage("draw", toDraw + "." + x + "." + y);
    }
    
    public static NXTMessage setSpeed(float speed, NXTMotor motor) {
        return new NXTMessage("setSpeed", String.valueOf(speed), motor.getNumber());
    }
	
	public static NXTMessage rotate(int angle, NXTMotor motor){
        return new NXTMessage("rotate", angle + "", motor.getNumber());
    }
	
	public static NXTMessage flt(NXTMotor motor) {
		return new NXTMessage("flt", motor.getNumber());
	}
	
	public static NXTMessage isMoving(NXTMotor motor) {
		return new NXTMessage("isMoving", motor.getNumber());
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
    			builder.append(s).append(",");
    		}
    		return message + "\\." + builder.toString();
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
