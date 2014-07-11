package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.eclipse.swt.SWT;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;

import com.google.gson.Gson;


public class JsonHttpEntityHandler implements IHttpEntityHandler {
	private static Header CONTENTTYPE = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
	private static Header ACCEPT = new BasicHeader("Accept", "application/json");
	protected Gson parser;
	protected class UsageData implements Serializable{
		private static final long serialVersionUID = 1L;
		public String user_id;
		public String os;
		public UsageDataEvent[] commands;
		
		public UsageData(String user_id, List<UsageDataEvent> events){
			this.user_id = user_id;
			this.os = SWT.getPlatform();
			this.commands = new UsageDataEvent[events.size()];
			this.commands = events.toArray(this.commands);
		}
	}
	
	public JsonHttpEntityHandler() {
		parser = new Gson();
	}

	@SuppressWarnings("deprecation")
	public HttpEntity getEntityForUpload(List<UsageDataEvent> events, String userId){
		UsageData data = new UsageData(userId, events);
		String jsonData = parser.toJson(data);
		HttpEntity entity = null;
		
		try {
			entity = new ByteArrayEntity(jsonData.getBytes(HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return entity;
	}

	
	public Header[] getHeaders() {
		return new Header[]{CONTENTTYPE, ACCEPT};
	}

}
