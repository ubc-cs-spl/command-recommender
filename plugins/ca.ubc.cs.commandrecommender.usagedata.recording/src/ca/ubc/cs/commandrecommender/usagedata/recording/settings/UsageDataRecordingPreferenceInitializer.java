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
package ca.ubc.cs.commandrecommender.usagedata.recording.settings;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;

public class UsageDataRecordingPreferenceInitializer extends AbstractPreferenceInitializer {
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStore = UsageDataRecordingActivator.getDefault().getPreferenceStore();
		preferenceStore.setDefault(UsageDataRecordingSettings.UPLOAD_PERIOD_KEY, UsageDataRecordingSettings.UPLOAD_PERIOD_DEFAULT);
		preferenceStore.setDefault(UsageDataRecordingSettings.ASK_TO_UPLOAD_KEY, UsageDataRecordingSettings.ASK_TO_UPLOAD_DEFAULT);
		preferenceStore.setDefault(UsageDataRecordingSettings.LOCAL_STORAGE_FORMAT_KEY, UsageDataRecordingSettings.LOCAL_STORAGE_FORMAT_DEFAULT);
		preferenceStore.setDefault(UsageDataRecordingSettings.STORAGE_LOCATION_KEY, UsageDataRecordingSettings.STORAGE_LOCATION_DEFAULT);
		preferenceStore.setDefault(UsageDataRecordingSettings.FILTER_ECLIPSE_BUNDLES_ONLY_KEY, false);
		preferenceStore.setDefault(UsageDataRecordingSettings.UPLOAD_TYPE_KEY, UsageDataRecordingSettings.UPLOAD_TYPE_DEFAULT);
	}

}