/*******************************************************************************
 * Copyright (c) 2008 The Eclipse Foundation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.AbstractUsageDataEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.UsageDataEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UploadSettings;

public class UsageDataRecordingSettingsMock implements UploadSettings {
	private String uploadUrl;
	private UsageDataEventFilter filter = new AbstractUsageDataEventFilter() {
		public boolean includes(UsageDataEvent event) {
			return true;
		}		
	};

	public String getUserId() {
		return "bogus";
	}

	public String getWorkspaceId() {
		return "bogus";
	}

	public boolean isLoggingServerActivity() {
		return false;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;		
	}
	
	public String getUploadUrl() {
		return uploadUrl;
	}
	
	public boolean hasUserAcceptedTermsOfUse() {
		return true;
	}
	
	public boolean isEnabled() {
		return true;
	}

	public UsageDataEventFilter getFilter() {
		return filter;
	}

	public String getUserAgent() {
		return "MockUpload/1.0";
	}
}