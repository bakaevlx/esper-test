package org.my.cep.espertest.eventsrc;

import java.util.List;

public interface EventSource<T> {
	
	void init() throws Exception;
	
	List<T> getEventsWithinTimeInterval(int tFrom, int tTo);

}
