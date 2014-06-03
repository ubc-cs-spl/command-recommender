package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.EventFilter;

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
	 * read events from the local storage that are accepted by the given filter
	 * @return the collected events that have not yet been uploaded and pass the filter
	 * @throws StorageConverterException 
	 */
	List<UsageDataEvent> readEvents(EventFilter filter) throws StorageConverterException;
	
	/**
	 * The format of local storage
	 * @return 
	 */
	String getFormat();
	
	/**
	 * Do the necessary procedures to handle a change in the storage location preference
	 */
	void handleStorageLocationChange();
	
	/**
	 * Do any clean up procedure as necessary
	 */
	void dispose();

}
