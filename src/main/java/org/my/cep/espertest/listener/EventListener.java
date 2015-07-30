package org.my.cep.espertest.listener;

import com.espertech.esper.client.UpdateListener;

/**
 * Defines an Esper statement and associated listener.
 * 
 * <p/>
 * A statement is a continuous query registered with an Esper engine instance
 * that provides results to listeners as new data arrives. It can be in
 * real-time or by demand via the iterator (pull) API.
 * 
 * @see http://www.espertech.com/esper/quickstart.php
 *
 */
public interface EventListener extends UpdateListener {
	
	String getStatement();
	
}
