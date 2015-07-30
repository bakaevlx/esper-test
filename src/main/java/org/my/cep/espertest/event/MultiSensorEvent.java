package org.my.cep.espertest.event;

import java.util.Date;

/**
 * The parameters could be averages over say 5 secs if for example temperature
 * and speed measurement intervals are different.
 * 
 * @author Sasha
 *
 */
public class MultiSensorEvent extends BaseEvent {
	private final int temperature;
	private final int speed;

	public MultiSensorEvent(long id, Date timeOfReading, int temperature, int speed) {
		super(id, timeOfReading);
		this.temperature = temperature;
		this.speed = speed;
	}


	public int getTemperature() {
		return temperature;
	}

	public int getSpeed() {
		return speed;
	}

	@Override
	public String toString() {
		return super.toString() + " temperature=" + temperature +" speed="
				+ speed + "]";
	}

}
