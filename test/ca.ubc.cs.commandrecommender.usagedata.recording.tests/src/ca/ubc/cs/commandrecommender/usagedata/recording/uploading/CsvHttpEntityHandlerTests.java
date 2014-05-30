package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util.UploaderTestUtils;

public class CsvHttpEntityHandlerTests {
	
	private List<UsageDataEvent> events;
	private String userId;
	private CsvHttpEntityHandler handler;
	
	@Before
	public void setUp() throws IOException{
		events = UploaderTestUtils.getTestEvents(100);
		userId = "abcdefgh";
		handler = new CsvHttpEntityHandler();
	}
	
	@Test
	public void testGetContentBodyWithEvents() throws IOException{
		HttpEntity entity = handler.getEntityForUpload(events, userId);
		ByteArrayOutputStream entityOutputStream = new ByteArrayOutputStream();
		entity.writeTo(entityOutputStream);
		String testContents = extractContentBody(entity, entityOutputStream);
		String[] contents = testContents.split("\n");
		testHeader(contents[0]);
		assertEquals(contents.length, events.size()+1);
		testBody(contents);
	}
	
	@Test
	public void testGetContentBodyWithNoEvents() throws IOException{
		events = UploaderTestUtils.getTestEvents(0);
		HttpEntity entity = handler.getEntityForUpload(events, userId);
		ByteArrayOutputStream entityOutputStream = new ByteArrayOutputStream();
		entity.writeTo(entityOutputStream);
		String testContents = extractContentBody(entity, entityOutputStream);
		String[] contents = testContents.split("\n");
		testHeader(contents[0]);
		assertEquals(contents.length, events.size()+1);
		testBody(contents);
	}

	private void testBody(String[] contents) {
		for(int i=1; i<contents.length; i++)
			compareEvent(contents[i], events.get(i-1));
		
	}

	private void compareEvent(String string, UsageDataEvent event) {
		assertTrue(string.contains(event.bundleId));
		assertTrue(string.contains(event.bundleVersion));
		assertTrue(string.contains(event.description));
		assertTrue(string.contains(event.kind));
		assertTrue(string.contains(event.what));
		assertTrue(string.contains(event.bindingUsed));
		assertTrue(string.contains(String.valueOf(event.when)));
	}

	private void testHeader(String header) {
		assertTrue(header.contains("what"));
		assertTrue(header.contains("kind"));
		assertTrue(header.contains("bundleId"));
		assertTrue(header.contains("bundleVersion"));
		assertTrue(header.contains("description"));
		assertTrue(header.contains("time"));
	}

	private String extractContentBody(HttpEntity entity, ByteArrayOutputStream entityOutputStream) {
		byte[] contentBytes = entityOutputStream.toByteArray();
		int startOfContent = findEndOfContent(contentBytes, 1);
		int endOfContent = findEndOfContent(contentBytes, startOfContent);
		
		String testContents = new String(contentBytes, startOfContent+1, endOfContent-startOfContent-3);
		return testContents;
	}

	protected int findEndOfContent(byte[] contentBytes, int i) {
		while(i < contentBytes.length){
			if(contentBytes[i-1] == '\n' && (contentBytes[i] == '\r' || contentBytes[i] == '\n')){
				if(contentBytes[i] == '\r')
					i++;
				break;
			}
			i++;
		}
		return i;
	}

}