/**
 * 
 */
package org.eclipse.epp.usagedata.internal.recording.storage;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;
import org.eclipse.epp.usagedata.internal.recording.UsageDataRecorder;
import org.eclipse.epp.usagedata.internal.recording.UsageDataRecordingActivator;

/**
 * @author KeEr
 *
 */
public class CsvEventStorageConverter extends AbstractEventStorageConverter {

	public CsvEventStorageConverter() {
		super(".csv");
	}

	public void archive() {
		// TODO Auto-generated method stub
		
	}
	
	public void writeEvent(UsageDataEvent event) {
		// TODO Auto-generated method stub

	}

	public List<UsageDataEvent> readEvents() {
		// TODO Auto-generated method stub
		return null;
	}


}
