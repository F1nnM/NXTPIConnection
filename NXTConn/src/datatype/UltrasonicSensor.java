package datatype;

import nxt.NXT;

/**
 * A class for a single Ultrasonic Sensor
 */
public class UltrasonicSensor {

	private SensorPort sensorPort;
	private NXT nxt;
	private int lastDistance = -1;

	/**
	 * the constructor of UltrasonicSensor
	 * 
	 * @param sensorPort
	 *            the Sensor Port of the Sensor
	 * @param nxt
	 *            an Instance of the NXT class
	 */
	protected UltrasonicSensor(SensorPort sensorPort, NXT nxt) {
		this.sensorPort = sensorPort;
		this.nxt = nxt;
	}

	/**
	 * this method returns the SensorPort of the Sensor
	 * 
	 * @return the SensorPort
	 */
	public SensorPort getSensorPort() {
		return sensorPort;
	}

	/**
	 * set the Sensor into continuous mode
	 */
	public void continuous() {
		nxt.getConnection().enqueue(NXTMessage.continuous(this));
	}

	/**
	 * get the Distance to an Object
	 * 
	 * @return the Distance
	 */
	public int getDistance() {
		return lastDistance;
	}

	/**
	 * set the Distance; <b>NOTE: this method gets called automatically, it should not
	 * be called.</b>
	 * 
	 * @param distance
	 */
	public void setDistance(int distance) {
		lastDistance = distance;
	}
}
