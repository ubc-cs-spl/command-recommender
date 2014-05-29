package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;

import com.google.gson.Gson;


public class JsonHttpEntityHandler implements IHttpEntityHandler {
	private static Header CONTENTTYPE = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");

	Gson parser;
	private class UsageData implements Serializable{
		private static final long serialVersionUID = 1L;
		public String user_id;
		public UsageDataEvent[] commands;
		
		public UsageData(String user_id, List<UsageDataEvent> events){
			this.user_id = user_id;
			this.commands = new UsageDataEvent[events.size()];
			this.commands = events.toArray(this.commands);
		}
	}
	
	public JsonHttpEntityHandler() {
		parser = new Gson();
	}

	public HttpEntity getEntityForUpload(List<UsageDataEvent> events, String userId) throws StorageConverterException {
		UsageData data = new UsageData(userId, events);
		String jsonData = parser.toJson(data);
		HttpEntity entity;
		try {
			entity = new ByteArrayEntity(jsonData.getBytes(HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new StorageConverterException(e);
		}
		return entity;
	}

	
	public Header[] getHeaders() {
		return new Header[]{CONTENTTYPE};
	}

}
