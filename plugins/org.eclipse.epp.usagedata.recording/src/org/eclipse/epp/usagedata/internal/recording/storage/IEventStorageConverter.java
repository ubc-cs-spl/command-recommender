package org.eclipse.epp.usagedata.internal.recording.storage;

import java.io.File;
import java.util.List;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;

/**
 * This interface provides a common API to support different types of output
 * format for local storage of the collected user data.
 * 
 * @author KeEr
 *
 */
public interface IEventStorageConverter {
	
	File[] getUsageDataUploadFiles();
	
	File getEventStorageFile();
	
	void writeEvent(UsageDataEvent event);
	
	void archive();
	
	List<UsageDataEvent> readEvents();

}
