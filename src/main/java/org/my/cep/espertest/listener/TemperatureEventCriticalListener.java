package org.my.cep.espertest.listener;

import org.my.cep.espertest.event.TemperatureEvent;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.EventBean;

@Component
public class TemperatureEventCriticalListener implements EventListener {
	private static final String CRITICAL_EVENT_THRESHOLD = "100";
	/**
	 * If the last event in a critical sequence is this much greater than the
	 * first one - issue a critical alert.
	 */
	private static final String CRITICAL_EVENT_MULTIPLIER = "1.5";

	/**
	 * {@inheritDoc}
	 */
	public String getStatement() {
		String crtiticalEventExpression = "select * from TemperatureEvent "
				+ "match_recognize ( "
				+ "       measures A as temp1, B as temp2, C as temp3, D as temp4 "
				+ "       pattern (A B C D) "
				+ "       define "
				+ "               A as A.temperature > "
				+ CRITICAL_EVENT_THRESHOLD
				+ ", "
				+ "               B as (A.temperature < B.temperature), "
				+ "               C as (B.temperature < C.temperature), "
				+ "               D as (C.temperature < D.temperature) and D.temperature > (A.temperature * "
				+ CRITICAL_EVENT_MULTIPLIER + ")" + ")";

		return crtiticalEventExpression;
	}

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		// An EventBean is a map containing fields in the EPL select.
		// oldEvents is typically null
		EventBean event = newEvents[0];
		
		TemperatureEvent temp1 = (TemperatureEvent) event.get("temp1");
		TemperatureEvent temp2 = (TemperatureEvent) event.get("temp2");
		TemperatureEvent temp3 = (TemperatureEvent) event.get("temp3");
		TemperatureEvent temp4 = (TemperatureEvent) event.get("temp4");

		StringBuilder sb = new StringBuilder();
		sb.append("***************************************");
		sb.append("\n* [ALERT] : CRITICAL TemperatureEvent DETECTED (CRITICAL_EVENT_THRESHOLD=" + CRITICAL_EVENT_THRESHOLD + ")");
		sb.append("\n* Event details: " + temp1 + " > " + temp2 + " > " + temp3 + " > "
				+ temp4);
		sb.append("\n***************************************");

		System.out.println(sb.toString());

	}

}
