package datatype;

/**
 * A class for sensor ports
 */
public class SensorPort {

	public static final SensorPort S1 = new SensorPort("S1");
	public static final SensorPort S2 = new SensorPort("S2");
	public static final SensorPort S3 = new SensorPort("S3");
	public static final SensorPort S4 = new SensorPort("S4");

	private String name;

	/**
	 * the constructor of SensorPort
	 * 
	 * @param name
	 *            the name of the port
	 */
	private SensorPort(String name) {
		this.name = name;
	}

	/**
	 * returns the name of the port
	 * 
	 * @return the name of the port
	 */
	public String getName() {
		return name;
	}
}
