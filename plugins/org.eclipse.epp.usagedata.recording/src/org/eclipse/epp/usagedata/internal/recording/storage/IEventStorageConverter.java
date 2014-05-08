package org.eclipse.epp.usagedata.internal.recording.storage;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;

/**
 * This interface provides a common API to support different types of output
 * format for local storage of the collected user data.
 * 
 * @author KeEr
 *
 */
public interface IEventStorageConverter {
	
	public abstract void writeEvent(UsageDataEvent event);
	
	public abstract void readEvents();

}
