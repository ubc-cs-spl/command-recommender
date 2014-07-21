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

import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.gathering.monitors.BundleUsageMonitor;
import ca.ubc.cs.commandrecommender.usagedata.gathering.monitors.CommandUsageMonitor;

public class FilterUtils {
	
	/**
	 * This method returns a suggestion for a filter pattern based on the 
	 * bundle symbolic ids provided.
	 * 
	 * @param names an array of bundle symbolic ids.
	 * @return a suggestion for a a filter pattern.
	 */
	public static String getFilterSuggestionBasedOnBundleIds(String[] names) {
		int index;
		StringBuilder builder = new StringBuilder();
		index = 0;
		outer: while (true) {
			if (names[0].length() <= index) break outer;
			char next = names[0].charAt(index);
			for (int i=1;i<names.length;i++) {
				if (names[i].length() <= index) break outer;
				if (names[i].charAt(index) != next) break outer;
			}
			builder.append(next);
			index++;
		}
		if (builder.length() == 0) return getDefaultFilterSuggestion();
		builder.append("*"); //$NON-NLS-1$
		return builder.toString();
	}
	
	public static String getDefaultFilterSuggestion() {
		return "com.*"; //$NON-NLS-1$
	}
	
	public static boolean isValidBundleIdPattern(String pattern) {
		return pattern.matches("[a-zA-Z0-9\\*]+?(\\.[a-zA-Z0-9\\*]+?)*?"); //$NON-NLS-1$
	}
	
	public static EventFilter and(final EventFilter filter1, final EventFilter filter2) {
		return new EventFilter() {
			public boolean accepts(UsageDataEvent event) {
				return filter1.accepts(event) && filter2.accepts(event);
			}
		};
	}
	
	public static EventFilter or(final EventFilter filter1, final EventFilter filter2) {
		return new EventFilter() {
			public boolean accepts(UsageDataEvent event) {
				return filter1.accepts(event) || filter2.accepts(event);
			}
		};
	}
	
	public static EventFilter acceptAllEventFilter() {
		return new EventFilter() {
			public boolean accepts(UsageDataEvent event) {
				return true;
			}
		};
	}
	
	public static EventFilter acceptNoneEventFilter() {
		return new EventFilter() {
			public boolean accepts(UsageDataEvent event) {
				return false;
			}
		};
	}
	
	public static EventFilter acceptCommandEventFilter() {
		return new EventFilter() {
			public boolean accepts(UsageDataEvent event) {
				return isCommandEvent(event);
			}
		};
	}
	
	public static EventFilter defaultEventFilter() {
		return new EventFilter() {
			public boolean accepts(UsageDataEvent event) {
				return event.isValid() && (isCommandEvent(event) || isBundleStartedEvent(event));
			}
		};
	}
	
	private static boolean isCommandEvent(UsageDataEvent event) {
		return event.kind != null && event.kind.equals(CommandUsageMonitor.EVENT_KIND);
	}
	
	private static boolean isBundleStartedEvent(UsageDataEvent event) {
		return event.kind != null 
				&& event.kind.equals(BundleUsageMonitor.EVENT_KIND) 
				&& event.what.equals(BundleUsageMonitor.STARTED);
	}
	
	public static List<UsageDataEvent> filterEvents(List<UsageDataEvent> events, EventFilter filter) {
		List<UsageDataEvent> filteredEvents = new ArrayList<UsageDataEvent>();
		for (UsageDataEvent event : events) {
			if (filter.accepts(event))
				filteredEvents.add(event);
		}
		return filteredEvents;
	}
	
}
