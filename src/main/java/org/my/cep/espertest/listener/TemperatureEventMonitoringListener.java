package org.my.cep.espertest.listener;

import org.springframework.stereotype.Component;

import com.espertech.esper.client.EventBean;

@Component
public class TemperatureEventMonitoringListener implements EventListener {

	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {

		EventBean event = newEvents[0];

		// average temp over 10 secs
		Double avg = (Double) event.get("avg_val");

		StringBuilder sb = new StringBuilder();
		sb.append("---------------------------------");
		sb.append("\n- [MONITOR] Average Temp = " + avg);
		sb.append("\n---------------------------------");

		System.out.println(sb.toString());

	}

	@Override
	public String getStatement() {
		return "select avg(temperature) as avg_val from TemperatureEvent.win:time_batch(5 sec)";
	}

}
