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
package ca.ubc.cs.commandrecommender.usagedata.ui.preferences;

import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UsageDataRecordingSettings;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UploadParameters;
import ca.ubc.cs.commandrecommender.usagedata.ui.preview.UploadPreview;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class UsageDataUploadingPreviewPage extends PreferencePage
	implements IWorkbenchPreferencePage {

	public UsageDataUploadingPreviewPage() {
		noDefaultAndApplyButton();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		UploadParameters parameters = new UploadParameters();
		UsageDataRecordingSettings settings = getSettings();
		parameters.setSettings(settings);
		new UploadPreview(parameters).createControl(composite);
		return composite;
	}

	protected UsageDataRecordingSettings getSettings() {
		return UsageDataRecordingActivator.getDefault().getSettings();
	}
}