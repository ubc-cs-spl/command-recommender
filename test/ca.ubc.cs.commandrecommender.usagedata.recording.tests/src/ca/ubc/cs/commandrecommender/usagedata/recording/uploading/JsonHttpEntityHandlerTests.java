package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util.UploaderTestUtils;

public class JsonHttpEntityHandlerTests {
	private List<UsageDataEvent> events;
	private String userId;
	private JsonHttpEntityHandler handler;
	
	private class TestJsonEntityHandler extends JsonHttpEntityHandler{
		public String getUsageDataJson(String userId, List<UsageDataEvent> events){
			return parser.toJson(new UsageData(userId, events));
		}
	}
	
	@Before
	public void setUp(){
		events = UploaderTestUtils.getTestEvents(100);
		userId = "abcdefgh";
		handler = new JsonHttpEntityHandler();
	}
	
	@Test
	public void testContent() throws ParseException, IOException{
		HttpEntity entity = handler.getEntityForUpload(events, userId);
		checkEntityEquals(entity);
	}
	
	@Test
	public void testHeader(){
		Header[] headers = handler.getHeaders();
		assertEquals(headers.length, 1);
		assertEquals(headers[0].getName(), HTTP.CONTENT_TYPE);
		assertEquals(headers[0].getValue(), "application/json");
	}
	
	@Test
	public void testNoEvents() throws IOException{
		events = new ArrayList<UsageDataEvent>();
		HttpEntity entity = handler.getEntityForUpload(events, userId);
		checkEntityEquals(entity);
	}
	
	protected void checkEntityEquals(HttpEntity entity) throws IOException {
		String testContents = EntityUtils.toString(entity);
		TestJsonEntityHandler handler =  new TestJsonEntityHandler();
		String actualContents = handler.getUsageDataJson(userId, events);
		assertEquals(testContents, actualContents);
	}
}
