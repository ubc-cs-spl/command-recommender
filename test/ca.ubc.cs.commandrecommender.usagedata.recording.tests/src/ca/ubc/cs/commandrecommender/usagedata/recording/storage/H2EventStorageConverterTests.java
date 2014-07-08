package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.FilterUtils;

public class H2EventStorageConverterTests {
	private static final String DATA_STORAGE_DIR = "storage-test-dir";
	private static H2EventStorageConverter converter;

	private static void initConverter() throws ClassNotFoundException, SQLException {
		converter = new TestH2EventStorageConverter();
	}
	
	private static class TestH2EventStorageConverter extends H2EventStorageConverter {

		public TestH2EventStorageConverter() throws ClassNotFoundException,
				SQLException {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override 
		protected String getDatabaseUrl() {
			String localDb = new File(getTempStorageDir(), DATABASE_NAME).getAbsolutePath();
			return "jdbc:h2:file:" + localDb;
		}
	}
	
	private static File getTempStorageDir() {
		return new File(System.getProperty("java.io.tmpdir"), DATA_STORAGE_DIR);
	}
	
	private static List<UsageDataEvent> generateEvents(int count) throws IOException  {
		List<UsageDataEvent> events = new ArrayList<UsageDataEvent>(count);
		for(int index = 0; index < count; index++) {
			events.add(new UsageDataEvent(String.valueOf(index), "a", "b", "c","d","e", System.currentTimeMillis(),  "", ""));
		}
		return events;
	}
	
	@Before
	public void initialize() throws ClassNotFoundException, SQLException {
		if (converter != null) 
			converter.closeConnectionToDb();
		File temp = getTempStorageDir();
		if (temp.exists()) {
			for (File file : temp.listFiles())
				file.delete();
		} else {
			temp.mkdir();
		}
		initConverter();
	}
	
	@Test
	public void testEventsWroteCanBeReadLater() throws IOException, StorageConverterException {
		List<UsageDataEvent> eventsWrote = generateEvents(5);
		converter.writeEvents(eventsWrote);
		List<UsageDataEvent> eventsRead = converter.readEvents();
		assertTrue(eventsWrote.containsAll(eventsRead));
		assertTrue(eventsRead.containsAll(eventsWrote));
	}
	
	@Test 
	public void testReadWithFilter() throws IOException, StorageConverterException {
		List<UsageDataEvent> eventsWrote = generateEvents(5);
		eventsWrote.add(new UsageDataEvent("", "command", "", "", "", "", 0, "", ""));
		converter.writeEvents(eventsWrote);
		assertEquals(0, converter.readEvents(FilterUtils.acceptNoneEventFilter()).size());
		assertEquals(6, converter.readEvents(FilterUtils.acceptAllEventFilter()).size());
		assertEquals(1, converter.readEvents(FilterUtils.acceptCommandEventFilter()).size());
	}
	
	@Test
	public void testArchive() throws StorageConverterException, IOException {
		List<UsageDataEvent> eventsWrote = generateEvents(5);
		converter.writeEvents(eventsWrote);
		assertEquals(5, converter.readEvents().size());
		converter.archive();
		assertEquals(0, converter.readEvents().size());
		assertEquals(5, converter.readArchievedEvents().size());
	}
	
	@Test
	public void testClearArchive() throws StorageConverterException, IOException {
		List<UsageDataEvent> eventsWrote = generateEvents(5);
		converter.writeEvents(eventsWrote);
		converter.archive();
		assertEquals(5, converter.readArchievedEvents().size());
		converter.clearArchive();
		assertEquals(0,	converter.readArchievedEvents().size());
		assertEquals(0, converter.readEvents().size());
	}
	
	@AfterClass
	public static void cleanUp() {
		//Note: this is not a recursive delete
		File temp = getTempStorageDir();
		if (temp.exists()) {
			for (File file : temp.listFiles())
				file.delete();
		}
		temp.delete();
	}
	
}