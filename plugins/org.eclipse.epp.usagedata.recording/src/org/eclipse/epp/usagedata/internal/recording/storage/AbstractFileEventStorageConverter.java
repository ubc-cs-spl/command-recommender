package org.eclipse.epp.usagedata.internal.recording.storage;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.epp.usagedata.internal.recording.UsageDataRecordingActivator;

public abstract class AbstractFileEventStorageConverter implements IEventStorageConverter {

	protected final String FORMAT_EXT;
	
	/**
	 * The file name for the current local data dump without extension
	 */
	protected final String STORAGE_FILE_NAME = "usagedata";
	protected final String ARCHIVE_FILE_PREFIX = "archive_usagedata_";
	

	protected AbstractFileEventStorageConverter(String extName){
		FORMAT_EXT = extName;
	}

	public File getEventStorageFile() {
		return new File(getStorageDirectory(), 
				STORAGE_FILE_NAME + FORMAT_EXT);
	}
	
	public void clearArchive() {
		File storageDir = getStorageDirectory();
		FilenameFilter archiveFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.startsWith(ARCHIVE_FILE_PREFIX);
			}
		};
		for (File file : storageDir.listFiles(archiveFilter)) {
			file.delete();
		}
	}
	
	public synchronized void archive() {
		//TODO: improve performance
		File file = getEventStorageFile();
		file.renameTo(computeArchiveFile());
		
	}
	
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
		File parent = getStorageDirectory();
		File file = null;
		// TODO: Unlikely (impossible?), but what if this spins forever.
		// TODO: set a limit for the number of backup files
		while (true) {
			file = new File(parent, ARCHIVE_FILE_PREFIX + index++ + ".csv"); //$NON-NLS-1$
			if (!file.exists())
				return file;
		}
	}
	
	protected File getStorageDirectory() {
		return UsageDataRecordingActivator.getDefault().getSettings().getStorageDirectory();
	}
	
}
