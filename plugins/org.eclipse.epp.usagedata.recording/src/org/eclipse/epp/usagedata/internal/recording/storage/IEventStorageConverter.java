package org.eclipse.epp.usagedata.internal.recording.storage;

import java.util.List;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;

public interface IEventStorageConverter {

	/**
	 * Make back-ups of the data as necessary
	 */
	void archive();

	/**
	 * Write given events into local storage
	 * @param events
	 * @throws StorageConverterException 
	 */
	void writeEvents(List<UsageDataEvent> events) throws StorageConverterException;

	/**
	 * read events from the local storage
	 * @return the collected events that have not yet been uploaded
	 * @throws StorageConverterException 
	 */
	List<UsageDataEvent> readEvents() throws StorageConverterException;

}
