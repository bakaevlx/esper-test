package org.my.cep.espertest.service;

import org.my.cep.espertest.listener.MultiSensorControllerFailureEventListener;
import org.my.cep.espertest.listener.MultiSensorIntrusionEventListener;
import org.my.cep.espertest.listener.TemperatureEventCriticalListener;
import org.my.cep.espertest.listener.TemperatureEventMonitoringListener;
import org.my.cep.espertest.listener.TemperatureEventWarningListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

@Service
public class DemoEsperService {
	private EPServiceProvider epService;
	@Autowired
	private TemperatureEventCriticalListener criticalTemperatureEventListener;
	@Autowired
	private TemperatureEventWarningListener warningTemperatureEventListener;
	@Autowired
	private TemperatureEventMonitoringListener monitoringTemperatureEventListener;
	@Autowired
	private MultiSensorIntrusionEventListener multiSensorIntrusionEventListener;
	@Autowired
	private MultiSensorControllerFailureEventListener multiSensorControllerFailureEventListener;

	public void processEvent(org.my.cep.espertest.event.BaseEvent event) {
		epService.getEPRuntime().sendEvent(event);
	}

	public void init() {

		System.out.println("Initializing Demo Servcie");
		Configuration config = new Configuration();
		config.addEventTypeAutoName("org.my.cep.espertest.event");
		epService = EPServiceProviderManager.getDefaultProvider(config);

		// addTemperatureEventListeners();
		addMultiSensorEventListeners();

	}

	private void addMultiSensorEventListeners() {

		EPStatement st;

		System.out.println("Add MultiSensorIntrusionEventListener");
		st = epService.getEPAdministrator().createEPL(
				multiSensorIntrusionEventListener.getStatement());
		st.addListener(multiSensorIntrusionEventListener);
		System.out.println("Add MultiSensorControllerFailureEventListener");
		st = epService.getEPAdministrator().createEPL(
				multiSensorControllerFailureEventListener.getStatement());
		st.addListener(multiSensorControllerFailureEventListener);

	}

	private void addTemperatureEventListeners() {

		EPStatement st;

		System.out.println("Add critical TemperatureEvent listener");
		st = epService.getEPAdministrator().createEPL(
				criticalTemperatureEventListener.getStatement());
		st.addListener(criticalTemperatureEventListener);

		System.out.println("Add warning TemperatureEvent listener");
		st = epService.getEPAdministrator().createEPL(
				warningTemperatureEventListener.getStatement());
		st.addListener(warningTemperatureEventListener);

		System.out.println("Add monitoring TemperatureEvent listener");
		st = epService.getEPAdministrator().createEPL(
				monitoringTemperatureEventListener.getStatement());
		st.addListener(monitoringTemperatureEventListener);
	}
}
