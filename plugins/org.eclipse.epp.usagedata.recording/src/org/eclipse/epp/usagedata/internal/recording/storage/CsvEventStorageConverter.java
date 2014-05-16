/**
 * 
 */
package org.eclipse.epp.usagedata.internal.recording.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epp.usagedata.internal.gathering.events.UsageDataEvent;
import org.eclipse.epp.usagedata.internal.recording.CSVStorageUtils;

/**
 * @author KeEr
 *
 */
public class CsvEventStorageConverter extends AbstractFileEventStorageConverter {
	
	public CsvEventStorageConverter() {
		super(".csv");
	}
	
	public synchronized void writeEvents(List<UsageDataEvent> events) 
			throws StorageConverterException {
		Writer writer = null;
		try {
			writer = getWriter();
			if (writer == null) return;
			for (UsageDataEvent event : events) {
				CSVStorageUtils.writeEvent(writer, event);
			}
			events.clear();
		} catch (IOException e) {
			throw new StorageConverterException(e); //$NON-NLS-1$
		} finally { 
			close(writer);
		}
	}

	public synchronized List<UsageDataEvent> readEvents() throws StorageConverterException {
		final List<UsageDataEvent> events = new ArrayList<UsageDataEvent>();
		File file = getEventStorageFile();
		if(file == null)
			throw new StorageConverterException("File is null");
		CsvFileReader reader = null;
		try {
			reader = new CsvFileReader(file);
			reader.iterate(new CsvFileReader.Iterator() {
				public void header(String header) {
					// Ignore the header.
				}
				
				public void event(String line, UsageDataEvent event) {
					events.add(event);
				}	
			});
		} catch (Exception e) {
			throw new StorageConverterException(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	
		return events;
	}
	
	public String getFormat() {
		return "csv";
	}

	private Writer getWriter() throws IOException {
		File file = getEventStorageFile();

		if (file.exists())
			return new FileWriter(file, true);

		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		CSVStorageUtils.writeHeader(writer);
		return writer;
	}
	
	private void close(Writer writer) {
		if (writer == null) return;
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Handle exception
		}
	}
}
