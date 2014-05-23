
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
package ca.ubc.cs.commandrecommender.usagedata.recording;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import ca.ubc.cs.commandrecommender.usagedata.gathering.services.UsageDataService;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UsageDataRecordingSettings;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.CsvEventStorageConverter;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.IEventStorageConverter;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UploadManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class UsageDataRecordingActivator extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "ca.ubc.cs.commandrecommender.usagedata.recording"; //$NON-NLS-1$

	// The shared instance
	private static UsageDataRecordingActivator plugin;

	private UploadManager uploadManager;

	private UsageDataRecordingSettings settings;

	private UsageDataRecorder usageDataRecorder;

	private ServiceTracker usageDataServiceTracker;

	private IEventStorageConverter converter;
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		IPreferenceStore prefs = getPreferenceStore();
		prefs.addPropertyChangeListener(new IPropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent event) {
				if (UsageDataRecordingSettings.LOCAL_STORAGE_FORMAT_KEY.equals(event.getProperty())) {
					UsageDataRecordingActivator.this.setConverter((String) event.getNewValue());
				}
				
			}
		});
		setConverter(prefs.getString(UsageDataRecordingSettings.LOCAL_STORAGE_FORMAT_KEY));
		
		
		uploadManager = new UploadManager();
		settings = new UsageDataRecordingSettings();
		
		usageDataRecorder = new UsageDataRecorder();
		usageDataRecorder.start();
		
		/*
		 * Our relationship with the service is wrong. We have a very hard dependency on the
		 * UsageDataService *class* so there really is no benefit to using an OSGi service here,
		 * other than as a convenient way to avoid having a singleton (which is honourable enough).
		 * Perhaps at some future point, we can leverage the EventAdmin service or something.
		 * More immediately since we have a Bundle-Require dependency on the 
		 * org.eclipse.epp.usagedata.capture bundle, we can be sure that the UsageDataService
		 * has been created before we attempt to acquire it from the tracker.
		 */
		usageDataServiceTracker = new ServiceTracker(context, UsageDataService.class.getName(), null);
		usageDataServiceTracker.open();
		
		getUsageDataService().addUsageDataEventListener(usageDataRecorder);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		usageDataRecorder.stop();
		getUsageDataService().removeUsageDataEventListener(usageDataRecorder);
		settings.dispose();
		
		plugin = null;
		super.stop(context);
	}
	
	public IEventStorageConverter getStorageConverter() {
		return converter;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	private IEventStorageConverter getConverter(String format) {
		IConfigurationElement[] elements = Platform.getExtensionRegistry()
				.getConfigurationElementsFor("ca.ubc.cs.commandrecommender.usagedata.recording.converter"); //$NON-NLS-1$
		for (IConfigurationElement element : elements) {
			if ("converter".equals(element.getName())) { //$NON-NLS-1$
				try {
					Object converter = element.createExecutableExtension("class"); //$NON-NLS-1$
					if ((converter instanceof IEventStorageConverter) 
							&& ((IEventStorageConverter) converter).getFormat().equals(format)) {
						return (IEventStorageConverter) converter;
					}
				} catch (CoreException e) {
					UsageDataRecordingActivator.getDefault().getLog().log(e.getStatus());
				}
			}
		}
		return null;
	}
	
	public void setConverter(String format) {
		IEventStorageConverter storageConverter = getConverter(format);
		if (storageConverter == null) {
			converter = new CsvEventStorageConverter();
		} else {
			converter = storageConverter;
		}
	}

	private UsageDataService getUsageDataService() {
		return (UsageDataService)usageDataServiceTracker.getService();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UsageDataRecordingActivator getDefault() {
		return plugin;
	}

	public UsageDataRecordingSettings getSettings() {
		return settings;
	}

	public void log(int status, String message, Object ... arguments) {
		log(status, (Exception)null, message, arguments);
	}
	
	public void log(int status, Exception exception, String message, Object ... arguments) {
		log(status, exception, String.format(message, arguments));
	}
	
	public void log(int status, Exception e, String message) {
		getLog().log(new Status(status, PLUGIN_ID, message, e));
	}
	

	public void log(Status status) {
		getLog().log(status);
	}

	public void earlyStartup() {
		// Don't actually need to do anything, but still need the method.		
	}

	public UploadManager getUploadManager() {
		return uploadManager;
	}
	
	public void prepareUsageDataRecorderForUpload(){
		usageDataRecorder.prepareForUpload();
	}


}
