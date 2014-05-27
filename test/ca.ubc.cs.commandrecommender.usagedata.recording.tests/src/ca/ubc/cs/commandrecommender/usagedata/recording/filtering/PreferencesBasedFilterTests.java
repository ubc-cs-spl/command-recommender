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
package ca.ubc.cs.commandrecommender.usagedata.recording.filtering;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UsageDataRecordingSettings;

/**
 * This class tests the {@link PreferencesBasedFilter} class. This test must
 * be &quot;Run As... | JUnit Plug-in Test&quot;.
 * 
 * @author Wayne Beaton
 *
 */
public class PreferencesBasedFilterTests {
	PreferencesBasedFilter filter;
	private UsageDataEvent eclipseEvent1 = createUsageDataEvent("org.eclipse.core");
	private UsageDataEvent eclipseEvent2 = createUsageDataEvent("org.eclipse.ui");
	private UsageDataEvent nonEclipseEvent = createUsageDataEvent("com.something.core");
	
	@Before
	public void setup() {
		filter = new PreferencesBasedFilter();
		getPreferencesStore().setToDefault(UsageDataRecordingSettings.FILTER_ECLIPSE_BUNDLES_ONLY_KEY);
		getPreferencesStore().setToDefault(UsageDataRecordingSettings.FILTER_PATTERNS_KEY);
	}
	
	@After
	public void teardown() {
		filter.dispose();
	}
	
	@Test
	public void testIncludesWithDefaults() {
		assertTrue(filter.accepts(eclipseEvent1));
		assertTrue(filter.accepts(eclipseEvent2));
		assertTrue(filter.accepts(nonEclipseEvent));
	}

	@Test
	public void testIncludes1() {
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "com.*");
	
		assertTrue(filter.accepts(eclipseEvent1));
		assertTrue(filter.accepts(eclipseEvent2));
		assertFalse(filter.accepts(nonEclipseEvent));
	}
	
	@Test
	public void testIncludes2() {
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "*.core");
	
		assertFalse(filter.accepts(eclipseEvent1));
		assertTrue(filter.accepts(eclipseEvent2));
		assertFalse(filter.accepts(nonEclipseEvent));
	}
	
	@Test
	public void testGetFilterPatterns() {
		assertEquals(0, filter.getFilterPatterns().length);
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "pattern1\npattern2");
		String[] patterns = filter.getFilterPatterns();
		assertEquals(2, patterns.length);
		assertEquals("pattern1", patterns[0]);
		assertEquals("pattern2", patterns[1]);
	}

	@Test
	public void testAddPattern1() {
		assertEquals(0, filter.getFilterPatterns().length);
		filter.addPattern("pattern1");
		assertEquals("pattern1", getPreferencesStore().getString(UsageDataRecordingSettings.FILTER_PATTERNS_KEY));
	}

	@Test
	public void testAddPattern2() {
		assertEquals(0, filter.getFilterPatterns().length);
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "pattern1");		
		filter.addPattern("pattern2");
		assertEquals("pattern1\npattern2", getPreferencesStore().getString(UsageDataRecordingSettings.FILTER_PATTERNS_KEY));
	}

	@Test
	public void testIncludesPattern() {
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "pattern1\npattern2");
		assertTrue(filter.includesPattern("pattern1"));
		assertTrue(filter.includesPattern("pattern2"));
		assertFalse(filter.includesPattern("pattern3"));
	}

	@Test
	public void testRemoveFilterPatterns1() {
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "pattern1\npattern2\npattern3");
		filter.removeFilterPatterns(new Object[] {"pattern1", "pattern3"});
		assertEquals("pattern2", getPreferencesStore().getString(UsageDataRecordingSettings.FILTER_PATTERNS_KEY));		
	}

	@Test
	public void testRemoveFilterPatterns2() {
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_PATTERNS_KEY, "pattern1\npattern2\npattern3");
		filter.removeFilterPatterns(new Object[] {"pattern2"});
		assertEquals("pattern1\npattern3", getPreferencesStore().getString(UsageDataRecordingSettings.FILTER_PATTERNS_KEY));		
	}


	@Test
	public void testSetEclipseOnly() {
		assertFalse(getPreferencesStore().getBoolean(UsageDataRecordingSettings.FILTER_ECLIPSE_BUNDLES_ONLY_KEY));	
		filter.setEclipseOnly(true);
		assertTrue(getPreferencesStore().getBoolean(UsageDataRecordingSettings.FILTER_ECLIPSE_BUNDLES_ONLY_KEY));	
	}

	@Test
	public void testIsEclipseOnly() {
		assertFalse(filter.isEclipseOnly()); // Default
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_ECLIPSE_BUNDLES_ONLY_KEY, true);	
		assertTrue(filter.isEclipseOnly());
	}
	
	@Test
	public void testFilterChangeListenerFiredFromFilter1() {
		final List<Boolean> fired = new ArrayList<Boolean>();
		filter.addFilterChangeListener(new FilterChangeListener() {
			public void filterChanged() {
				fired.add(true);
			}
		});
		assertEquals(0, fired.size());
		filter.setEclipseOnly(true);
		assertEquals(1, fired.size());
	}

	@Test
	public void testFilterChangeListenerFiredFromFilter2() {
		final List<Boolean> fired = new ArrayList<Boolean>();
		filter.addFilterChangeListener(new FilterChangeListener() {
			public void filterChanged() {
				fired.add(true);
			}
		});
		assertEquals(0, fired.size());
		filter.addPattern("pattern1");
		assertEquals(1, fired.size());
	}
	
	@Test
	public void testFilterChangeListenerFiredFromPreferences() {
		final List<Boolean> fired = new ArrayList<Boolean>();
		filter.addFilterChangeListener(new FilterChangeListener() {
			public void filterChanged() {
				fired.add(true);
			}
		});
		assertEquals(0, fired.size());
		getPreferencesStore().setValue(UsageDataRecordingSettings.FILTER_ECLIPSE_BUNDLES_ONLY_KEY, true);	
		assertEquals(1, fired.size());
	}
	
	private IPreferenceStore getPreferencesStore() {
		return UsageDataRecordingActivator.getDefault().getPreferenceStore();
	}
	
	private UsageDataEvent createUsageDataEvent(String bundleId) {
		return new UsageDataEvent("what", "kind", "description", bundleId, "version", "0", System.currentTimeMillis());
	}
}