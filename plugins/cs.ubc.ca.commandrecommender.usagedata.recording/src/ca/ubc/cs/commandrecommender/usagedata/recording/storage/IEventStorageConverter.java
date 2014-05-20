package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;

public interface IEventStorageConverter {

	/**
	 * Make back-ups of the data as necessary
	 */
	void archive();
	
	/**
	 * Clear the archives
	 */
	void clearArchive();

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
	
	/**
	 * The format of local storage
	 * @return 
	 */
	String getFormat();

}
