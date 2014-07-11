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
package ca.ubc.cs.commandrecommender.usagedata.ui.preview;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.MockUsageDataEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UploadParameters;
import ca.ubc.cs.commandrecommender.usagedata.ui.preview.util.MockUploadSettings;

/**
 * These tests can be run outside of the workbench.
 * 
 * @author Wayne Beaton
 */
public class UsageDataEventWrapperTests {

	private UploadParameters parameters;
	private UsageDataEventWrapper wrapper;
	private MockUsageDataEventFilter filter;
	private MockUploadSettings settings;

	@Before
	public void setup() {
		parameters = new UploadParameters();
		settings = new MockUploadSettings();
		filter = (MockUsageDataEventFilter)settings.getFilter();
		parameters.setSettings(settings);
		
		wrapper = new UsageDataEventWrapper(parameters, new UsageDataEvent("what", "command", "description", "bundleId", "bundleVersion", "0", 1000, "", "", ""));
	}
	
	@Test
	public void testIsIncludedByFilter1() {
		// Should be null (unset) before we start
		assertNull(wrapper.isIncludedByFilter);
		// Lazy initialization should set it.
		assertTrue(wrapper.isIncludedByFilter());
	}

	@Test
	public void testIsIncludedByFilter2() {
		filter.addPattern("*");
		// Should be null (unset) before we start
		assertNull(wrapper.isIncludedByFilter);
		// Lazy initialization should set it.
		assertFalse(wrapper.isIncludedByFilter());
	}
	
	@Test
	public void testResetCaches() {
		// Should be null (unset) before we start
		assertNull(wrapper.isIncludedByFilter);
		// Lazy initialization should set it.
		assertTrue(wrapper.isIncludedByFilter());		
		wrapper.resetCaches();
		// Should be null (unset) again
		assertNull(wrapper.isIncludedByFilter);
	}

}
