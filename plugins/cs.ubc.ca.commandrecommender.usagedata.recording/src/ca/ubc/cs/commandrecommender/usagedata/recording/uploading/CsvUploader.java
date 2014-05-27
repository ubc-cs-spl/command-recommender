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
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.eclipse.core.runtime.ListenerList;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UploadSettings;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;

/**
 * Instances of the {@link CsvUploader} class are responsible for
 * uploading a set of files to the server.
 * 
 * @author Wayne Beaton
 *
 */
public class CsvUploader extends AbstractEventUploader {

	/**
	 * The HTTP_USERID constant is the key for the HTTP header
	 * that is used to pass the user (i.e. workstation) identifier.
	 * This value identifies the user's workstation (which may
	 * include multiple Eclipse workspaces).
	 */
	private static final String HTTP_USERID = "USERID"; //$NON-NLS-1$
	
	/**
	 * The HTTP_WORKSPACE constant is the key for the HTTP header
	 * that is used to pass the workspace identifier. This value
	 * is used to identify a single workspace on the user's workstation.
	 * A user may have more than one workspace and each will have
	 * a different workspace id.
	 */
	private static final String HTTP_WORKSPACEID = "WORKSPACEID";	 //$NON-NLS-1$

	/**
	 * The HTTP_TIME constant is the key for the HTTP header
	 * that is used to pass the current time on the workstation to
	 * the server. This value is included in the request so that the
	 * server, if desired, can account for differences in the clock
	 * between the user's workstation and the server.
	 */
	private static final String HTTP_TIME = "TIME"; //$NON-NLS-1$

	private static final String USER_AGENT = "User-Agent"; //$NON-NLS-1$
	
	public CsvUploader(UploadParameters uploadParameters) {
		setUploadParameters(uploadParameters);
	}
	

	@Override
	protected HttpEntity getEntityForUpload()
			throws StorageConverterException {
		MultipartEntity entity = new MultipartEntity();
		
		ContentBody body = getContentBody();
		entity.addPart("csv", body);
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

	protected List<UsageDataEvent> getEvents() throws StorageConverterException {
		return getEventStorage().readEvents();
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

	public synchronized boolean isUploadInProgress() {
		return uploadInProgress;
	}
}
