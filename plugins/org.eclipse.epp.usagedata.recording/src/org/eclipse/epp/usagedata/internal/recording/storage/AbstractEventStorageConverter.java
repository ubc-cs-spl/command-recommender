package org.eclipse.epp.usagedata.internal.recording.storage;

import java.util.List;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;

public abstract class AbstractEventStorageConverter {

	/**
	 * Make back-ups of the data as necessary
	 */
	public abstract void archive();

	/**
	 * Write given events into local storage
	 * @param events
	 * @throws StorageConverterException 
	 */
	public abstract void writeEvents(List<UsageDataEvent> events) throws StorageConverterException;

	/**
	 * read events from the local storage
	 * @return the collected events that have not yet been uploaded
	 * @throws StorageConverterException 
	 */
	public abstract List<UsageDataEvent> readEvents() throws StorageConverterException;

}
