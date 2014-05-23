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

import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.UsageDataEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UploadSettings;

public class UploadParameters {

	private UploadSettings settings;
	
	public void setSettings(UploadSettings settings) {
		this.settings = settings;
	}

	public UploadSettings getSettings() {
		return settings;
	}

	public UsageDataEventFilter getFilter() {
		return settings.getFilter();
	}
}
