package org.my.cep.espertest.event;

import java.util.Date;

public class TemperatureEvent extends BaseEvent {
	private final int temperature;

	public TemperatureEvent(long id, Date timeOfReading, int temperature) {
		super(id, timeOfReading);
		this.temperature = temperature;
	}

	public int getTemperature() {
		return temperature;
	}

	@Override
	public String toString() {
		return super.toString() + "temperature=" + temperature + "]";
	}

}
