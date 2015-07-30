package org.my.cep.espertest.listener;

import org.my.cep.espertest.event.TemperatureEvent;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.EventBean;

@Component
public class TemperatureEventWarningListener implements EventListener {
	// If 2 consecutive temperature events are greater than this - issue a warning
    private static final String WARNING_EVENT_THRESHOLD = "400";

    /**
     * {@inheritDoc}
     */
	@Override
	public void update(EventBean[] newEvents, EventBean[] oldEvents) {
		
		EventBean event = newEvents[0];
		
        TemperatureEvent temp1 = (TemperatureEvent) event.get("temp1");
        TemperatureEvent temp2 = (TemperatureEvent) event.get("temp2");

        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------------");
        sb.append("\n- [WARNING] : TEMPERATURE SPIKE DETECTED = " + temp1 + "," + temp2);
        sb.append("\n--------------------------------------------------");

        System.out.println(sb.toString());
	}

	@Override
	public String getStatement() {
        String warningEventExpression = "select * from TemperatureEvent "
                + "match_recognize ( "
                + "       measures A as temp1, B as temp2 "
                + "       pattern (A B) " 
                + "       define " 
                + "               A as A.temperature > " + WARNING_EVENT_THRESHOLD + ", "
                + "               B as B.temperature > " + WARNING_EVENT_THRESHOLD + ")";
        
        return warningEventExpression;
	}

}
