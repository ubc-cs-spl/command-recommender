package ca.ubc.cs.commandrecommender.usagedata.recording.filtering;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;

public interface EventFilter {

	boolean accepts(UsageDataEvent event);

}