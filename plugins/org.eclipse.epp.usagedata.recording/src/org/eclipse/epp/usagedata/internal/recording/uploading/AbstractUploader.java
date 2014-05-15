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
package org.eclipse.epp.usagedata.internal.recording.uploading;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.epp.usagedata.internal.recording.UsageDataRecordingActivator;
import org.eclipse.epp.usagedata.internal.recording.storage.IEventStorageConverter;

public abstract class AbstractUploader implements Uploader{

	private ListenerList uploadListeners = new ListenerList();
	private UploadParameters uploadParameters;
	public static String UPLOAD_TYPE_CSV = "csv";
	
	public static AbstractUploader createUploader(String type, UploadParameters uploadParameters){
		if(UPLOAD_TYPE_CSV.equals(type))
			return (AbstractUploader) new CsvUploader(uploadParameters);
		else
			return (AbstractUploader) new CsvUploader(uploadParameters); 
	}
	
	public void addUploadListener(UploadListener listener) {
		uploadListeners.add(listener);
	}

	public void removeUploadListener(UploadListener listener) {
		uploadListeners.remove(listener);
	}
	
	protected void fireUploadComplete(UploadResult result) {
		for (Object listener : uploadListeners.getListeners()) {
			((UploadListener)listener).uploadComplete(result);
		}
	}	
	
	public UploadParameters getUploadParameters() {
		return uploadParameters;
	}

	public void setUploadParameters(UploadParameters uploadParameters) {
		this.uploadParameters = uploadParameters;
	}
	
	protected void checkValues() {
		if (uploadParameters == null) throw new RuntimeException("The UploadParameters must be set."); //$NON-NLS-1$
	}
	
	protected IEventStorageConverter getEventStorage(){
		return UsageDataRecordingActivator.getDefault().getStorageConverter();
	}
	
	public abstract void startUpload();

	public abstract boolean isUploadInProgress();
}
