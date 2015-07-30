package org.my.cep.espertest.event;

import java.util.Date;

/**
 * Events are immutable.
 */
public class BaseEvent {
	protected final long id;
	protected final Date timeOfReading;

	public BaseEvent(long id, Date timeOfReading) {
		super();
		this.id = id;
		this.timeOfReading = timeOfReading;
	}

	public long getId() {
		return id;
	}

	public Date getTimeOfReading() {
		return timeOfReading;
	}

	@Override
	public String toString() {
		return getClass().getName() + " [id=" + id + " timeOfReading="
				+ timeOfReading;
	}
}
