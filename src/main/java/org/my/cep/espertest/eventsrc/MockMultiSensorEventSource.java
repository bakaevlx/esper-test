package org.my.cep.espertest.eventsrc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.my.cep.espertest.event.MultiSensorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Generates speed + temperature events using initial and increment parameters
 * from the app config file.
 * 
 * @author Sasha
 *
 */
@Component
public class MockMultiSensorEventSource implements
		EventSource<MultiSensorEvent> {
	@Autowired
	private Environment env;
	private long time = 0L;
	// from the app config file
	private int tempInit;
	private int tempInc;
	private int speedInit;
	private int speedInc;

	@Override
	public void init() {
		tempInit = env.getProperty("sensor.mock.temp.init", Integer.class);
		tempInc = env.getProperty("sensor.mock.temp.inc", Integer.class);
		speedInit = env.getProperty("sensor.mock.speed.init", Integer.class);
		speedInc = env.getProperty("sensor.mock.speed.inc", Integer.class);
		System.out.println("tempInit=" + tempInit + " tempInc=" + tempInc
				+ " speedInit=" + speedInit + " speedInc=" + speedInc);
	}

	@Override
	public List<MultiSensorEvent> getEventsWithinTimeInterval(int tFrom, int tTo) {

		int temperature = (int) (tempInit + time * tempInc);
		int speed = (int) (speedInit + time * speedInc);
		time++;

		MultiSensorEvent event = new MultiSensorEvent(time, Calendar
				.getInstance().getTime(), temperature, speed);
		System.out.println(event);

		return (List<MultiSensorEvent>) Arrays
				.asList(new MultiSensorEvent[] { event });
	}

}
