package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.EventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.FilterUtils;

public class H2EventStorageConverter implements IEventStorageConverter {
	
	private Connection connection;
	protected final static String DATABASE_NAME = "usageDB";
	private final static String FORMAT = "H2";
	private final static String EVENT_TABLE_NAME = "USAGEEVENT";
	private final static String WHAT_COLUMN_NAME = "WHAT";
	private final static String KIND_COLUMN_NAME = "KIND";
	private final static String DESCRIPTION_COLUMN_NAME = "DESCRIPTION";
	private final static String BUNDLE_ID_COLUMN_NAME = "BUNDLEID";
	private final static String BUNDLE_VERSION_COLUMN_NAME = "BUNDLEVER";
	private final static String WHEN_COLUMN_NAME = "WHEN";
	private final static String BINDING_USED_COLUMN_NAME = "BINDINGUSED";
	private final static String NAME_COLUMN_NAME = "NAME";
	private final static String INFO_COLUMN_NAME = "INFO";
	private final static String SHORTCUT_COLUMN_NAME = "SHORTCUT";
	private final static String UPLOADED_COLUMN_NAME = "UPLOADED";
	private final static String INSERT_STATEMENT = "INSERT INTO " + EVENT_TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?,FALSE)";
	private final static String SELECT_ACCORDING_TO_UPLOADED_FIELD = "SELECT * FROM " + EVENT_TABLE_NAME + " WHERE " + UPLOADED_COLUMN_NAME + "=?";
	

	public H2EventStorageConverter() throws ClassNotFoundException, SQLException {
		initConnectionToDb();
	}

	public void archive() {
		String sql = "UPDATE " + EVENT_TABLE_NAME + " SET " + UPLOADED_COLUMN_NAME + "=TRUE";
		try {
			connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// TODO: what to do?
		}
	}

	public void clearArchive() {
		String sql = "DELETE FROM " + EVENT_TABLE_NAME 
				+ " WHERE " + UPLOADED_COLUMN_NAME + "=TRUE";
		try {
			connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			// TODO: what to do?
		}
	}

	public void writeEvents(List<UsageDataEvent> events)
			throws StorageConverterException {
		for (UsageDataEvent event : events) {
			writeEvent(event);
		}
	}
	
	private void writeEvent(UsageDataEvent event) throws StorageConverterException{
		try {
			PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
			statement.setString(1, event.what);
			statement.setString(2, event.kind);
			statement.setString(3, event.description);
			statement.setString(4, event.bundleId);
			statement.setString(5, event.bundleVersion);
			statement.setLong(6, event.when);
			statement.setString(7, event.bindingUsed);
			statement.setString(8, event.name);
			statement.setString(9, event.info);
			statement.setString(10, event.shortcut);
			statement.execute();
		} catch (SQLException e) {
			throw new StorageConverterException(e);
		}
	}

	public List<UsageDataEvent> readEvents() throws StorageConverterException {
		try {
			PreparedStatement statement = connection.prepareStatement(SELECT_ACCORDING_TO_UPLOADED_FIELD);
			statement.setBoolean(1, false);
			return readEvents(statement);
		} catch (SQLException e) {
			throw new StorageConverterException(e);
		}
	}
	
	public List<UsageDataEvent> readArchievedEvents() throws StorageConverterException {
		try {
			PreparedStatement statement = connection.prepareStatement(SELECT_ACCORDING_TO_UPLOADED_FIELD);
			statement.setBoolean(1, true);
			return readEvents(statement);
		} catch (SQLException e) {
			throw new StorageConverterException(e);
		}
	}

	private List<UsageDataEvent> readEvents(PreparedStatement statement) throws SQLException {
		ResultSet rs = statement.executeQuery();
		List<UsageDataEvent> events = new ArrayList<UsageDataEvent>();
		while (rs.next()) {
			events.add(convertToUsageDataEvent(rs));
		}
		return events;
	}
	
	private UsageDataEvent convertToUsageDataEvent(ResultSet rs) throws SQLException {
		String what = rs.getString(WHAT_COLUMN_NAME);
		String kind = rs.getString(KIND_COLUMN_NAME);
		String description = rs.getString(DESCRIPTION_COLUMN_NAME);
		String bundleId = rs.getString(BUNDLE_ID_COLUMN_NAME);
		String bundleVersion = rs.getString(BUNDLE_VERSION_COLUMN_NAME);
		long when = rs.getLong(WHEN_COLUMN_NAME);
		String bindingUsed = rs.getString(BINDING_USED_COLUMN_NAME);
		String name = rs.getString(NAME_COLUMN_NAME);
		String info = rs.getString(INFO_COLUMN_NAME);
		String shortcut = rs.getString(SHORTCUT_COLUMN_NAME);
		return new UsageDataEvent(what, kind, description, bundleId, 
				bundleVersion, bindingUsed, when, name, info, shortcut);
	}

	public List<UsageDataEvent> readEvents(EventFilter filter)
			throws StorageConverterException {
		return FilterUtils.filterEvents(readEvents(), filter);
	}

	public String getFormat() {
		return FORMAT;
	}

	public void handleStorageLocationChange() {
		closeConnectionToDb();
		try {
			initConnectionToDb();
		} catch (ClassNotFoundException e) {
			//TODO: what to do?
		} catch (SQLException e) {
			//TODO: what to do?
		}
	}
	
	public void dispose() {
		closeConnectionToDb();
	}
	
	protected String getDatabaseUrl() {
		File storageDir = UsageDataRecordingActivator.getDefault().getSettings().getStorageDirectory();
		String localDb = new File(storageDir, DATABASE_NAME).getAbsolutePath();
		return "jdbc:h2:file:" + localDb;
	}
	
	//Connection should be closed after using this method
	private void initConnectionToDb() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(getDatabaseUrl(),"sa","");
		connection = conn;
		setUpTable();
	}
	
	private void setUpTable() throws SQLException {
		connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " 
				+ EVENT_TABLE_NAME + "("
				+ WHAT_COLUMN_NAME + " VARCHAR(255), " 
				+ KIND_COLUMN_NAME + " VARCHAR(255), "
				+ DESCRIPTION_COLUMN_NAME + " VARCHAR(255), "
				+ BUNDLE_ID_COLUMN_NAME + " VARCHAR(255), "
				+ BUNDLE_VERSION_COLUMN_NAME + " VARCHAR(255), "
				+ WHEN_COLUMN_NAME + " BIGINT, "
				+ BINDING_USED_COLUMN_NAME + " VARCHAR(5), "
				+ NAME_COLUMN_NAME + " VARCHAR(255), "
				+ INFO_COLUMN_NAME + " VARCHAR(255), "
				+ SHORTCUT_COLUMN_NAME + " VARCHAR(255), "
				+ UPLOADED_COLUMN_NAME + " BOOLEAN)");
	}
	
	public void closeConnectionToDb() {
		try {
			connection.close();
		} catch (SQLException e) {
			// We tried
		}
	}
}
