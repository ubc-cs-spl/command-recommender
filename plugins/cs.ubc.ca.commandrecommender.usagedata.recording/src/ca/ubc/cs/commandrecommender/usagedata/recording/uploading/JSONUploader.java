package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;

import com.google.gson.Gson;


public class JSONUploader extends AbstractEventUploader {
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
	
	public JSONUploader(UploadParameters uploadParamters) {
		super(uploadParamters);
		parser = new Gson();
	}

	protected HttpEntity getEntityForUpload() throws StorageConverterException {
		List<UsageDataEvent> events = getEvents();
		UsageData data = new UsageData(getUserId(), events);
		String jsonData = parser.toJson(data);
		HttpEntity entity;
		try {
			entity = new ByteArrayEntity(jsonData.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new StorageConverterException(e);
		}
		return entity;
	}

	
	protected void setHeaders(HttpPost httpPost) {
		httpPost.addHeader(CONTENTTYPE);
	}

}
