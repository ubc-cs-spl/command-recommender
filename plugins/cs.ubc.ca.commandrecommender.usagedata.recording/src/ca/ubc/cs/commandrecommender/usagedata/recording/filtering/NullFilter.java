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

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;

/**
 * The {@link NullFilter} accepts everything.
 * 
 * @author Wayne Beaton
 *
 */
public class NullFilter extends AbstractUsageDataEventFilter {

	public boolean accepts(UsageDataEvent event) {
		return true;
	}

}
