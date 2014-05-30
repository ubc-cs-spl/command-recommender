package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.EventFilter;

public class H2StorageConverter implements IEventStorageConverter {
	
	Connection connection;

	public H2StorageConverter() {
		// TODO Auto-generated constructor stub
	}

	public void archive() {
		// TODO Auto-generated method stub

	}

	public void clearArchive() {
		// TODO Auto-generated method stub

	}

	public void writeEvents(List<UsageDataEvent> events)
			throws StorageConverterException {
		// TODO Auto-generated method stub

	}

	public List<UsageDataEvent> readEvents() throws StorageConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UsageDataEvent> readEvents(EventFilter filter)
			throws StorageConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

}
