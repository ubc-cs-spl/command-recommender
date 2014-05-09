package org.eclipse.epp.usagedata.internal.recording.storage;

import java.io.File;
import java.util.List;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;
import org.eclipse.epp.usagedata.internal.recording.UsageDataRecorder;
import org.eclipse.epp.usagedata.internal.recording.UsageDataRecordingActivator;

public abstract class AbstractEventStorageConverter implements IEventStorageConverter {
	
	protected final String FORMAT_EXT;
	protected final String STORAGE_FILE_NAME;
	protected final String ARCHIVE_FILE_PREFIX;

	protected AbstractEventStorageConverter(String extName){
		FORMAT_EXT = extName;
		STORAGE_FILE_NAME = UsageDataRecorder.USAGE_DATA_FILE_NAME;
		ARCHIVE_FILE_PREFIX = "archive_usagedata_";
	}

	public File getEventStorageFile() {
		return new File(getWorkingDirectory(), 
				STORAGE_FILE_NAME + FORMAT_EXT);
	}
	
	
	
	private File getWorkingDirectory() {
		return UsageDataRecordingActivator.getDefault().getStateLocation().toFile();
	}

	public abstract void writeEvent(UsageDataEvent event);

	public void archive() {
		// TODO Auto-generated method stub

	}

	public abstract List<UsageDataEvent> readEvents();

	
	public File[] getUsageDataUploadFiles() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * When it's time to start uploading the usage data, the file that's used
	 * to persist the data is moved (renamed) and a new file is created. The
	 * moved file is then uploaded to the server. This method finds an appropriate
	 * destination for the moved file. The destination {@link File} will be in the
	 * bundle's state location, but will not actually exist in the file system.
	 * 
	 * @return a destination {@link File} for the move operation. 
	 */
	public File computeDestinationFile() {
		int index = 0;
		File parent = getWorkingDirectory();
		File file = null;
		// TODO Unlikely (impossible?), but what if this spins forever.
		while (true) {
			file = new File(parent, ARCHIVE_FILE_PREFIX + index++ + ".csv"); //$NON-NLS-1$
			if (!file.exists())
				return file;
		}
	}

}
