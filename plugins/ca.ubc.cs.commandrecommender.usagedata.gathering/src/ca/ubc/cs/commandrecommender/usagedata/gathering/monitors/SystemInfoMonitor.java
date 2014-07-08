/*******************************************************************************
 * Copyright (c) 2009 The Eclipse Foundation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package ca.ubc.cs.commandrecommender.usagedata.gathering.monitors;

import org.eclipse.core.runtime.Platform;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;
import ca.ubc.cs.commandrecommender.usagedata.gathering.services.UsageDataService;

/**
 * This monitor captures information about the System. Specifically,
 * we capture:
 * <ul>
 * <li>Operating System</li>
 * <li>System Architecture</li>
 * <li>Window System</li>
 * <li>Locale</li>
 * <li>Number of processors available</li>
 * <li>And a number of system properties</li>
 * 
 */
public class SystemInfoMonitor implements UsageMonitor {
	
	public static final String EVENT_KIND = "sysinfo"; //$NON-NLS-1$
	
	private static final String INFO_PROCESSORS = "processors"; //$NON-NLS-1$
	private static final String INFO_LOCALE = "locale"; //$NON-NLS-1$
	private static final String INFO_WS = "ws"; //$NON-NLS-1$
	private static final String INFO_ARCH = "arch"; //$NON-NLS-1$
	private static final String INFO_OS = "os"; //$NON-NLS-1$
	
	/**
	 * This property contains a list of system properties that
	 * we obtain the values for.
	 * <p>
	 * Many of the system properties contain information like paths
	 * which may provide us with too much information about a particular
	 * user. We avoid inadvertently including any of this information 
	 * by being particular about the actual properties we capture.
	 * AFAIK, none of these properties will likely contain any information
	 * of a personal nature.
	 */
	private static final String[] SYSTEM_PROPERTIES = {
		"java.runtime.name", //$NON-NLS-1$
		"java.runtime.version", //$NON-NLS-1$
		"java.specification.name", //$NON-NLS-1$
		"java.specification.vendor", //$NON-NLS-1$
		"java.specification.version", //$NON-NLS-1$
		"java.vendor", //$NON-NLS-1$
		"java.version", //$NON-NLS-1$
		"java.vm.info", //$NON-NLS-1$
		"java.vm.name", //$NON-NLS-1$
		"java.vm.specification.name", //$NON-NLS-1$
		"java.vm.specification.vendor", //$NON-NLS-1$
		"java.vm.specification.version", //$NON-NLS-1$
		"java.vm.vendor", //$NON-NLS-1$
		"java.vm.version" //$NON-NLS-1$
	};
	
	public void startMonitoring(UsageDataService usageDataService) {
		/*
		 * If you look deep enough into the call chain, there is some
		 * possibility that these Platform.xxx methods can cause a
		 * runtime exception. We'll catch and log that potential exception.
		 */
		try {
			recordEvent(usageDataService, INFO_OS, EVENT_KIND, Platform.getOS());
			recordEvent(usageDataService, INFO_ARCH, EVENT_KIND, Platform.getOSArch());
			recordEvent(usageDataService, INFO_WS, EVENT_KIND, Platform.getWS());
			recordEvent(usageDataService, INFO_LOCALE, EVENT_KIND, Platform.getNL());
		} catch (Exception e) {
			UsageDataCaptureActivator.getDefault().logException("Exception occurred while obtaining platform properties.", e); //$NON-NLS-1$
		}
		
		recordEvent(usageDataService, INFO_PROCESSORS, EVENT_KIND, String.valueOf(Runtime.getRuntime().availableProcessors()));
		
		for (String property : SYSTEM_PROPERTIES) {
			recordEvent(usageDataService, property, EVENT_KIND, System.getProperty(property));
		}
	}
	
	private void recordEvent(UsageDataService service, String what, String kind, String description) {
		service.recordEvent(what, kind, description, null, null, null, null);
	}

	public void stopMonitoring() {
	}

}
