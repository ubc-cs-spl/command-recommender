/*******************************************************************************
 * Copyright (c) 2007 The Eclipse Foundation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;

/**
 * Instances of the {@link CsvUploader} class are responsible for
 * uploading a set of files to the server.
 * 
 * @author Wayne Beaton
 *
 */

public class CsvUploader extends AbstractEventUploader {
	
	protected CsvUploader(UploadParameters uploadParameters) {
		super(uploadParameters);
	}

	protected HttpEntity getEntityForUpload() throws StorageConverterException {
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("csv", getContentBody());
		return entity;
	}

	private FileBody getContentBody() throws StorageConverterException{
		File temp = null;
		BufferedWriter writer = null;
		try {
		temp = File.createTempFile("temp_upload", ".csv");
		String content = "what,kind,bundleId,bundleVersion,description,time\n";
		List<UsageDataEvent> events = getEvents();
		for(UsageDataEvent event : events){
			if(getUploadParameters().getUserDefinedFilter().accepts(event)){
				content += event.what +","+ event.kind + "," + event.bundleId + "," + event.bundleVersion + ",\"" 
						+ event.description + "\"," + event.when + "," + event.bindingUsed  + "\n";
			}
		}
		writer = new BufferedWriter(new FileWriter(temp));
		writer.write(content);
		writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new FileBody(temp);
	}

	/**
	 * This method returns a &quot;reasonable&quot; value for 
	 * socket timeout based on the number of files we're trying
	 * to upload. Assumes that &quot;about a minute&quot; per
	 * file should be plenty of time.
	 * 
	 * @return int value specifying a reasonable timeout.
	 */
	int getSocketTimeout() {
		return 5 * 60000;
	}

	protected void setHeaders(HttpPost httpPost) {
	}
}
