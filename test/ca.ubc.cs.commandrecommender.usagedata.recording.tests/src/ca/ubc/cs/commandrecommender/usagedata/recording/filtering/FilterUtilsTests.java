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

import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;

public class FilterUtilsTests {
	
	private UsageDataEvent bundleStartedEvent = new UsageDataEvent("started","bundle","org.eclipse.osgi","3.4.0.v20071207","org.eclipse.osgi", "0", 1, "", "", null);
	private UsageDataEvent bundleStoppedEvent = new UsageDataEvent("stopped","bundle","org.eclipse.equinox.common","3.4.0.v20071207","org.eclipse.equinox.common", "0", 1, "", "", null);
	private UsageDataEvent commandEvent = new UsageDataEvent("started","command","org.eclipse.update.configurator","3.2.200.v20071113","org.eclipse.update.configurator", "0", 1, "", "", "");
	private UsageDataEvent randomEvent = new UsageDataEvent("a", "b", "c", "d", "e", "f", 0, "", "", null);
	
	@Test
	public void testGetFilterSuggestionBasedOnBundleIds1() {
		String suggestion = FilterUtils.getFilterSuggestionBasedOnBundleIds(new String[] {
			"org.eclipse.core.stuff", "org.eclipse.core.junk", "org.eclipse.junk"
		});
		
		assertEquals("org.eclipse.*", suggestion);
	}	

	@Test
	public void testGetFilterSuggestionBasedOnBundleIds() {
		String suggestion = FilterUtils.getFilterSuggestionBasedOnBundleIds(new String[] {
			"org.eclipse.core.stuff", "org.eclipse.core"
		});
		
		assertEquals("org.eclipse.core*", suggestion);
	}

	@Test
	public void testIsValidBundleIdPattern() {
		// What's good...
		assertTrue(FilterUtils.isValidBundleIdPattern("org.eclipse.*"));
		assertTrue(FilterUtils.isValidBundleIdPattern("org.*.core"));
		assertTrue(FilterUtils.isValidBundleIdPattern("*.core.*"));
		assertTrue(FilterUtils.isValidBundleIdPattern("*.*.*"));
		
		// Some counter examples...
		assertFalse(FilterUtils.isValidBundleIdPattern("$"));
		assertFalse(FilterUtils.isValidBundleIdPattern(".core"));
		assertFalse(FilterUtils.isValidBundleIdPattern("core."));
		assertFalse(FilterUtils.isValidBundleIdPattern(".core."));
		assertFalse(FilterUtils.isValidBundleIdPattern(""));
		assertFalse(FilterUtils.isValidBundleIdPattern("."));
	}
	
	@Test
	public void testAcceptNoneEventFilter() {
		EventFilter filter = FilterUtils.acceptNoneEventFilter();
		assertFalse(filter.accepts(bundleStartedEvent));
		assertFalse(filter.accepts(bundleStoppedEvent));
		assertFalse(filter.accepts(commandEvent));
		assertFalse(filter.accepts(randomEvent));
	}
	
	@Test
	public void testAcceptAllEventFilter() {
		EventFilter filter = FilterUtils.acceptAllEventFilter();
		assertTrue(filter.accepts(bundleStartedEvent));
		assertTrue(filter.accepts(bundleStoppedEvent));
		assertTrue(filter.accepts(commandEvent));
		assertTrue(filter.accepts(randomEvent));
	}
	
	@Test
	public void testDefaultEventFilter() {
		EventFilter filter = FilterUtils.defaultEventFilter();
		assertTrue(filter.accepts(bundleStartedEvent));
		assertFalse(filter.accepts(bundleStoppedEvent));
		assertTrue(filter.accepts(commandEvent));
		assertFalse(filter.accepts(randomEvent));
	}
	
	@Test
	public void testAcceptCommandEventFilter() {
		EventFilter filter = FilterUtils.acceptCommandEventFilter();
		assertFalse(filter.accepts(bundleStartedEvent));
		assertFalse(filter.accepts(bundleStoppedEvent));
		assertTrue(filter.accepts(commandEvent));
		assertFalse(filter.accepts(randomEvent));
	}
	
	@Test 
	public void testOrForFilter() {
		EventFilter filter = FilterUtils.or(FilterUtils.acceptCommandEventFilter(),
				FilterUtils.acceptNoneEventFilter());
		assertFalse(filter.accepts(bundleStartedEvent));
		assertFalse(filter.accepts(bundleStoppedEvent));
		assertTrue(filter.accepts(commandEvent));
		assertFalse(filter.accepts(randomEvent));
	}
	
	@Test
	public void testAndForFilter() {
		EventFilter filter = FilterUtils.and(FilterUtils.acceptCommandEventFilter(),
				FilterUtils.acceptNoneEventFilter());
		assertFalse(filter.accepts(bundleStartedEvent));
		assertFalse(filter.accepts(bundleStoppedEvent));
		assertFalse(filter.accepts(commandEvent));
		assertFalse(filter.accepts(randomEvent));
	}
	
	@Test 
	public void testFilterEventsIsFilteringEventsBasedOnGivenFilter() {
		List<UsageDataEvent> eventList = new ArrayList<UsageDataEvent>();
		eventList.add(bundleStartedEvent);
		eventList.add(bundleStoppedEvent);
		eventList.add(commandEvent);
		eventList.add(randomEvent);
		assertEquals(0, FilterUtils.filterEvents(eventList, FilterUtils.acceptNoneEventFilter()).size());
		assertEquals(1, FilterUtils.filterEvents(eventList, FilterUtils.acceptCommandEventFilter()).size());
		assertEquals(2, FilterUtils.filterEvents(eventList, FilterUtils.defaultEventFilter()).size());
		assertEquals(4, FilterUtils.filterEvents(eventList, FilterUtils.acceptAllEventFilter()).size());
	}
}
