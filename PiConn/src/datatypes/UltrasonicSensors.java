package datatypes;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import util.Connection;

/**
 * A class for Ultrasonic Sensors
 */
public class UltrasonicSensors {

	private static UltrasonicSensor s1 = null;
	private static UltrasonicSensor s2 = null;
	private static UltrasonicSensor s3 = null;
	private static UltrasonicSensor s4 = null;

	/**
	 * set the sensor in continuous mode
	 * 
	 * @param sensorPort
	 *            the port of the Sensor
	 */
	public static void continuous(String sensorPort) {
		UltrasonicSensor s = getUltraSonicSensor(sensorPort, true);
		s.continuous();

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (Connection.connected) {
					Connection.send(NXTMessage.ultraSonicSensorData(s.getDistance(), sensorPort));
				}
			}
		});
		t.start();
	}

	/**
	 * Initialise the Sensor
	 * 
	 * @param sensorPort
	 *            the port of the Sensor
	 */
	private static void init(String sensorPort) {
		if (getUltraSonicSensor(sensorPort, false) == null) {
			switch (sensorPort) {
			case "S1":
				s1 = new UltrasonicSensor(SensorPort.S1);
			case "S2":
				s2 = new UltrasonicSensor(SensorPort.S2);
			case "S3":
				s3 = new UltrasonicSensor(SensorPort.S3);
			case "S4":
				s4 = new UltrasonicSensor(SensorPort.S4);
			}
		}
	}

	/**
	 * get the UltrasonicSensor by the name of the port
	 * 
	 * @param sensorPort
	 *            the port of the Sensor
	 * @param init
	 *            if the sensor should be initialised
	 * @return the UltrasonicSensor
	 */
	private static UltrasonicSensor getUltraSonicSensor(String sensorPort, Boolean init) {
		if (init) {
			init(sensorPort);
		}
		switch (sensorPort) {
		case "S1":
			return s1;
		case "S2":
			return s2;
		case "S3":
			return s3;
		case "S4":
			return s4;
		default:
			return null;
		}
	}
}
