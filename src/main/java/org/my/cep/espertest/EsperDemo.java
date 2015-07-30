package org.my.cep.espertest;

import java.io.IOException;
import java.util.List;

import org.my.cep.espertest.conf.AppConfig;
import org.my.cep.espertest.event.BaseEvent;
import org.my.cep.espertest.eventsrc.MockMultiSensorEventSource;
import org.my.cep.espertest.eventsrc.MockTemperatureEventSource;
import org.my.cep.espertest.eventsrc.SharedFileMultiSensorEventSource;
import org.my.cep.espertest.service.DemoEsperService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EsperDemo {
	@Autowired
	private Environment env;
	@Autowired
	private DemoEsperService srv;
	@Autowired
//	private SharedFileMultiSensorEventSource eventSource;
	private MockMultiSensorEventSource eventSource;
//	private TemperatureEventSource eventSource;

	public void exec() throws IOException {

		int eventCount = 0;	
		int maxEventCount = env.getProperty("demo.maxEventCount", Integer.class);
		int delayMs = env.getProperty("demo.delayMs", Integer.class);	
		System.out.println("Start; maxEventCount=" + maxEventCount + " delayMs=" + delayMs);

		srv.init();
		eventSource.init();

		while (eventCount < maxEventCount) {
			
			List<? extends BaseEvent> eventList = eventSource
					.getEventsWithinTimeInterval(-1, -1);
			
			if(eventList != null) {
				for (BaseEvent event : eventList) {
					System.out.println("eventCount=" + eventCount);
					srv.processEvent(event);
				}
			}

			eventCount++;
			sleep(delayMs);
		}
		
		System.out.println("All done");

	}

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.err.println("Thread Interrupted");
			e.printStackTrace(System.err);
		}
	}

	public static void main(String[] args) throws Exception {

		// Load spring config
		@SuppressWarnings("resource")
		ApplicationContext ctx = new AnnotationConfigApplicationContext(
				AppConfig.class);
		// run demo
		ctx.getBean(EsperDemo.class).exec();

	}

}
