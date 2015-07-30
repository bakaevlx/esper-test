package org.my.cep.espertest.listener;

import org.my.cep.espertest.event.MultiSensorEvent;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.EventBean;

@Component
public class MultiSensorControllerFailureEventListener extends BaseEventlistener {

		/**
		 * {@inheritDoc}
		 */
		public String getStatement() {
			String crtiticalEventExpression = "select * from MultiSensorEvent "
					+ "match_recognize ( "
					+ "       measures A as multSens1, B as multSens2, C as multSens3 "
					+ "       pattern (A B C) "
					+ "       define "
					+ "               A as (A.temperature > " + getCritTempThreshold() + "), "
					+ "               B as (A.temperature < B.temperature and B.speed > A.speed), "
					+ "               C as (B.temperature < C.temperature and C.temperature > (A.temperature + 10) and C.speed > A.speed))";

			return crtiticalEventExpression;
		}

		@Override
		public void update(EventBean[] newEvents, EventBean[] oldEvents) {

			// An EventBean is a map containing fields in the EPL select.
			// oldEvents is typically null
			EventBean event = newEvents[0];

			MultiSensorEvent multSens1 = (MultiSensorEvent) event.get("multSens1");
			MultiSensorEvent multSens2 = (MultiSensorEvent) event.get("multSens2");
			MultiSensorEvent multSens3 = (MultiSensorEvent) event.get("multSens3");

			StringBuilder sb = new StringBuilder();
			sb.append("***************************************");
			sb.append("\n* [ALERT] : CRITICAL EVENT DETECTED MultiSensor ControllerFailure (CRITICAL_TEMP_THRESHOLD=" + 
					getCritTempThreshold() + ")! ");
			sb.append("\n* Event details: " + multSens1 + " > " + multSens2 + " > " + multSens3);
			sb.append("\n***************************************");

			System.out.println(sb.toString());

		}

}
