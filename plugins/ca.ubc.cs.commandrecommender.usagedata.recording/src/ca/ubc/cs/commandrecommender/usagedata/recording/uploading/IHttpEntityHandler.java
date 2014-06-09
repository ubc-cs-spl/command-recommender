package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;

public interface IHttpEntityHandler {
	HttpEntity getEntityForUpload(List<UsageDataEvent> events, String userId);
	Header[] getHeaders();
}
