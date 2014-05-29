package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;

public interface IHttpEntityHandler {
	HttpEntity getEntityForUpload(List<UsageDataEvent> events, String userId) throws StorageConverterException;
	Header[] getHeaders();
}
