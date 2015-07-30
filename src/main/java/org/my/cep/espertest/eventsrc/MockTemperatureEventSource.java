package org.my.cep.espertest.eventsrc;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.my.cep.espertest.event.TemperatureEvent;
import org.springframework.stereotype.Component;

/**
 * Random temperature readings.
 * 
 * @author Sasha
 *
 */
@Component
public class MockTemperatureEventSource implements EventSource<TemperatureEvent> {
	private final Random rng = new Random(System.currentTimeMillis());
	private long count = 0L;

	@Override
	public void init() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n************************************************************");
		sb.append("\n* PLEASE WAIT - TEMPERATURES ARE RANDOM SO IT MAY ");
		sb.append("\n* TAKE A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
		sb.append("\n************************************************************\n");
		System.out.println(sb.toString());
	}

	@Override
	public List<TemperatureEvent> getEventsWithinTimeInterval(int tFrom, int tTo) {
		TemperatureEvent event;

		Date rightNow = Calendar.getInstance().getTime();
		event = new TemperatureEvent(count++, rightNow, rng.nextInt(500));
		
		System.out.println(event);

		return (List<TemperatureEvent>) Arrays
				.asList(new TemperatureEvent[] { event });
	}

}
