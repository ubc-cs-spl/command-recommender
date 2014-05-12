package org.eclipse.epp.usagedata.internal.recording.storage;

import java.io.File;

import org.eclipse.epp.usagedata.internal.recording.UsageDataRecordingActivator;

public abstract class AbstractFileEventStorageConverter implements IEventStorageConverter {

	protected final String FORMAT_EXT;
	
	/**
	 * The file name for the current local data dump without extension
	 */
	protected final String STORAGE_FILE_NAME = "usagedata";
	protected final String ARCHIVE_FILE_PREFIX = "archive_usagedata_";
	protected final String UPLOAD_FILE_NAME = "upload";
	

	protected AbstractFileEventStorageConverter(String extName){
		FORMAT_EXT = extName;
	}

	public void archive() {
		//TODO: improve performance
		for (File file: getEventUploadFiles()) {
			file.renameTo(computeArchiveFile());
		}
	}

	public File getEventStorageFile() {
		return new File(getWorkingDirectory(), 
				STORAGE_FILE_NAME + FORMAT_EXT);
	}
	
	public abstract File[] getEventUploadFiles();
	
	public String getFileExt() {
		return FORMAT_EXT;
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
	protected File computeArchiveFile() {
		int index = 0;
		File parent = getWorkingDirectory();
		File file = null;
		// TODO: Unlikely (impossible?), but what if this spins forever.
		// TODO: set a limit for the number of backup files
		while (true) {
			file = new File(parent, ARCHIVE_FILE_PREFIX + index++ + ".csv"); //$NON-NLS-1$
			if (!file.exists())
				return file;
		}
	}
	
	protected File getWorkingDirectory() {
		return UsageDataRecordingActivator.getDefault().getStateLocation().toFile();
	}
	
}
