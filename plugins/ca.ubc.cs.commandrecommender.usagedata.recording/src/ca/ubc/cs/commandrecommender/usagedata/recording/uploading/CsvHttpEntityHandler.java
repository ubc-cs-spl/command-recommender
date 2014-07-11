package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;


public class CsvHttpEntityHandler implements IHttpEntityHandler {
	public  HttpEntity getEntityForUpload(List<UsageDataEvent> events, String userId) {
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("csv", getContentBody(events));
		return entity;
	}

	//TODO: This is super vulnerable right now. Needs to be checked over if we want to use this
	private FileBody getContentBody(List<UsageDataEvent> events) {
		File temp = null;
		BufferedWriter writer = null;
		try {
		temp = File.createTempFile("temp_upload", ".csv");
		String content = "what,kind,bundleId,bundleVersion,description,time,bindingUsed,name,info\n";
		for(UsageDataEvent event : events){
			content += event.what +","+ event.kind + "," + event.bundleId + "," + event.bundleVersion + ",\"" 
					+ event.description + "\"," + event.when + "," + event.bindingUsed + ",\"" + event.name + "\",\""
					+ event.info + "\",\"" + event.shortcut + "\"\n";
		}
		writer = new BufferedWriter(new FileWriter(temp));
		writer.write(content);
		writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FileBody(temp);
	}

	public Header[] getHeaders() {
		return new Header[]{};
	}	
}
