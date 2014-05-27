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

import org.eclipse.core.runtime.ListenerList;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UploadSettings;

public abstract class AbstractUploader {

	private ListenerList uploadListeners = new ListenerList();
	
	protected boolean uploadInProgress = false;
	public static String UPLOAD_TYPE_CSV = "csv";
	private UploadParameters uploadParameters;
	
	protected ListenerList responseListeners = new ListenerList();
	
	public void addUploadListener(UploadListener listener) {
		uploadListeners.add(listener);
	}

	public void removeUploadListener(UploadListener listener) {
		uploadListeners.remove(listener);
	}
	
	public void setUploadParameters(UploadParameters uploadParameters) {
		this.uploadParameters = uploadParameters;
	}

	public void addResponseListener(UploaderResponseListener listener) {
		responseListeners.add(listener);
	}

	public void removeResponseListener(UploaderResponseListener listener) {
		responseListeners.remove(listener);
	}
	
	public abstract boolean isUploadInProgress();

	public abstract void startUpload();
	
	/**
	 * This method sets up a bit of a roadblock to ensure that an upload does
	 * not occur if the user has not explicitly consented. The user must have
	 * both enabled the service and agreed to the terms of use.
	 * 
	 * @return <code>true</code> if the upload can occur, or
	 *         <code>false</code> otherwise.
	 */
	protected boolean hasUserAuthorizedUpload() {
		if (!getUploadSettings().isEnabled()) return false;
		if (!getUploadSettings().hasUserAcceptedTermsOfUse()) return false;
		return true;
	}
	
	protected UploadSettings getUploadSettings() {
		return getUploadParameters().getSettings();
	}
	
	public UploadParameters getUploadParameters() {
		return uploadParameters;
	}

	protected void fireUploadComplete(UploadResult result) {
		for (Object listener : uploadListeners.getListeners()) {
			((UploadListener)listener).uploadComplete(result);
		}
	}
	
	protected void checkValues() {
		if (uploadParameters == null) throw new RuntimeException("The UploadParameters must be set."); //$NON-NLS-1$
	}
}
