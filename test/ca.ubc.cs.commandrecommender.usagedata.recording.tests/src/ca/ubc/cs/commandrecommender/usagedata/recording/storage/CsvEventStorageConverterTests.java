package ca.ubc.cs.commandrecommender.usagedata.recording.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.FilterUtils;

public class CsvEventStorageConverterTests {
	private static final String DATA_STORAGE_DIR = "storage-test-dir";
	private static CsvEventStorageConverter converter = new TestCsvEventStorageConverter();

	private static class TestCsvEventStorageConverter extends CsvEventStorageConverter {
		@Override 
		protected File getStorageDirectory(){
			return getTempStorageDir();
		}
	}
	
	private static File getTempStorageDir() {
		return new File(System.getProperty("java.io.tmpdir"), DATA_STORAGE_DIR);
	}
	
	private static List<UsageDataEvent> generateEvents(int count) throws IOException  {
		List<UsageDataEvent> events = new ArrayList<UsageDataEvent>(count);
		for(int index = 0; index < count; index++) {
			events.add(new UsageDataEvent(String.valueOf(index), "a", "b", "c","d","e", System.currentTimeMillis(), "f", "g", "h"));
		}
		return events;
	}
	
	@Before
	public void initialize() {
		File temp = getTempStorageDir();
		if (temp.exists()) {
			for (File file : temp.listFiles())
				file.delete();
		} else {
			temp.mkdir();
		}
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
		eventsWrote.add(new UsageDataEvent("", "command", "", "", "", "", 0, "", "", ""));
		converter.writeEvents(eventsWrote);
		assertEquals(0, converter.readEvents(FilterUtils.acceptNoneEventFilter()).size());
		assertEquals(6, converter.readEvents(FilterUtils.acceptAllEventFilter()).size());
		assertEquals(1, converter.readEvents(FilterUtils.acceptCommandEventFilter()).size());
	}
	
	@Test
	public void testArchive() throws StorageConverterException, IOException {
		File rootDir = getTempStorageDir();
		List<UsageDataEvent> eventsWrote = generateEvents(5);
		converter.writeEvents(eventsWrote);
		converter.archive();
		assertTrue(usageDataFileIsDeleted());
		assertTrue(rootDir.listFiles().length == 1);
		converter.writeEvents(eventsWrote);
		converter.archive();
		assertTrue(usageDataFileIsDeleted());
		assertTrue(rootDir.listFiles().length == 2);
	}
	
	private boolean usageDataFileIsDeleted() {
		File rootDir = getTempStorageDir();
		for (File file : rootDir.listFiles()) {
			if (file.getName().startsWith(AbstractFileEventStorageConverter.STORAGE_FILE_NAME))
				return false;
		}
		return true;
	}
	
	@Test
	public void testClearArchive() throws StorageConverterException, IOException {
		File rootDir = getTempStorageDir();
		List<UsageDataEvent> eventsWrote = generateEvents(5);
		converter.writeEvents(eventsWrote);
		converter.archive();
		converter.writeEvents(eventsWrote);
		converter.archive();
		assertTrue(rootDir.listFiles().length == 2);
		converter.clearArchive();
		assertTrue(rootDir.listFiles().length == 0);
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
