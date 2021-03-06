/**
 * 
 */
package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.CsvStorageUtils;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.EventFilter;


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
				CsvStorageUtils.writeEvent(writer, event);
			}
		} catch (IOException e) {
			throw new StorageConverterException(e); //$NON-NLS-1$
		} finally { 
			close(writer);
		}
	}

	public synchronized List<UsageDataEvent> readEvents(final EventFilter filter) throws StorageConverterException {
		final List<UsageDataEvent> events = new ArrayList<UsageDataEvent>();
		File file = getEventStorageFile();
		if(file == null)
			throw new StorageConverterException("File is null");
		if(!file.exists())
			return events;
		CsvFileReader reader = null;
		try {
			reader = new CsvFileReader(file);
			reader.iterate(new CsvFileReader.Iterator() {
				public void header(String header) {
					// Ignore the header.
				}
				
				public void event(String line, UsageDataEvent event) {
					if (filter.accepts(event))
						events.add(event);
				}	
			});
		} catch (Exception e) {
			throw new StorageConverterException(e);
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
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
		CsvStorageUtils.writeHeader(writer);
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
