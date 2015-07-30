package org.my.cep.espertest.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


public abstract class BaseEventlistener implements EventListener {
	@Autowired
	private Environment env;
	private int critTempThreshold = -1;
	
	protected synchronized int getCritTempThreshold() {
		if(critTempThreshold == -1) {
			critTempThreshold = env.getProperty("demo.critTempThreshold", Integer.class);
			System.out.println("critTempThreshold=" + critTempThreshold);
		}
		return critTempThreshold;
	}
}
