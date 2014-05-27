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
package ca.ubc.cs.commandrecommender.usagedata.ui.uploaders;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.UserDefinedEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UsageDataRecordingSettings;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.AbstractUploader;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UploadListener;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UploadResult;
import ca.ubc.cs.commandrecommender.usagedata.ui.wizards.AskUserUploaderWizard;

public class AskUserUploader extends AbstractUploader {
	public static final int UPLOAD_NOW = 0;
	public static final int UPLOAD_ALWAYS = 1;
	public static final int DONT_UPLOAD = 2;
	public static final int NEVER_UPLOAD = 3;
	
	private AbstractUploader uploader;
	private WizardDialog dialog;

	private int action = UPLOAD_NOW;
	private boolean userAcceptedTermsOfUse;

	public void startUpload() {
		checkValues();
		if (needToOpenWizard()) {
			openUploadWizard();
		} else {
			startBasicUpload();
		}
	}

	protected boolean needToOpenWizard() {
		if (getSettings().shouldAskBeforeUploading()) return true;
		if (!getSettings().hasUserAcceptedTermsOfUse()) return true;		
		return false;
	}

	private void openUploadWizard() {
		action = getDefaultAction();
		userAcceptedTermsOfUse = getSettings().hasUserAcceptedTermsOfUse();
		
		final AskUserUploaderWizard wizard = new AskUserUploaderWizard(this);
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				dialog = new WizardDialog(getShell(), wizard);
				dialog.setBlockOnOpen(false);
				dialog.open();
			}

			private Shell getShell() {
				return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			}
		});
	}

	private int getDefaultAction() {
		if (getSettings().isEnabled()) {
			if (needToOpenWizard()) {
				return UPLOAD_NOW;
			} else {
				return UPLOAD_ALWAYS;
			}
		} else {
			return NEVER_UPLOAD;
		}
	}

	private UsageDataRecordingSettings getSettings() {
		return UsageDataRecordingActivator.getDefault().getSettings();
	}

	public synchronized boolean isUploadInProgress() {
		if (isWizardOpen()) return true;
		if (uploader != null) {
			return uploader.isUploadInProgress();
		}
		return false;
	}

	private boolean isWizardOpen() {
		if (dialog == null) return false;
		return dialog.getShell().isVisible();
	}

	public synchronized void cancel() {
		dialog = null;
		fireUploadComplete(new UploadResult(UploadResult.CANCELLED));
	}

	public synchronized void execute() {
		dialog = null;
		
		getSettings().setAskBeforeUploading(action != UPLOAD_ALWAYS);
		getSettings().setEnabled(action != NEVER_UPLOAD);
		getSettings().setUserAcceptedTermsOfUse(userAcceptedTermsOfUse);
		
		if (action == UPLOAD_ALWAYS || action == UPLOAD_NOW) {
			startBasicUpload();
		} else {
			fireUploadComplete(new UploadResult(UploadResult.CANCELLED));
		}
	}
	
	private void startBasicUpload() {
		uploader = AbstractUploader.createUploader(getUploaderType(), getUploadParameters());
		uploader.addUploadListener(new UploadListener() {
			public void uploadComplete(UploadResult result) {
				fireUploadComplete(result);
				uploader = null;
			}
		});
		uploader.startUpload();	
	}

	private String getUploaderType() {
		return UsageDataRecordingActivator.getDefault().getPreferenceStore().getString(UsageDataRecordingSettings.UPLOAD_TYPE_KEY);
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getAction() {
		return action;
	}

	public boolean hasUserAcceptedTermsOfUse() {
		return userAcceptedTermsOfUse;
	}

	public void setUserAcceptedTermsOfUse(boolean value) {
		userAcceptedTermsOfUse = value;
	}

	public boolean hasUploadAction() {
		if (action == UPLOAD_ALWAYS) return true;
		if (action == UPLOAD_NOW) return true;
		return false;
	}

	public UserDefinedEventFilter getFilter() {
		return getUploadParameters().getUserDefinedFilter();
	}
}
