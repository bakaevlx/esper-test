package org.my.cep.espertest.eventsrc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.my.cep.espertest.conf.AppConfig;
import org.my.cep.espertest.event.MultiSensorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class)
public class SharedFileMultiSensorEventSourceTest {
	@Autowired
	private SharedFileMultiSensorEventSource sharedFileMultiSensorEventSource;
	
	/**
	 * Try opening omnet.datafile in MS Word and Notepad.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadSensorData() throws Exception {
		
//		sharedFileMultiSensorEventSource.init();
//		List<MultiSensorEvent> ev = sharedFileMultiSensorEventSource.getEventsWithinTimeInterval(-1, -1);
		
	}

}
