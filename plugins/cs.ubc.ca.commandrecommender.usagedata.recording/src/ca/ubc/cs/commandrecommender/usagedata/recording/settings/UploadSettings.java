
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
package ca.ubc.cs.commandrecommender.usagedata.recording.settings;

import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.NullFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.UsageDataEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.IEventStorageConverter;

public interface UploadSettings {

	/**
	 * This method answers whether or not we want to ask the server to 
	 * provide a log of activity. 
	 * 
	 * @return true if we're logging, false otherwise.
	 */
	boolean isLoggingServerActivity();

	/**
	 * This method returns the target URL for uploads.
	 * 
	 * @return the target URL for uploads.
	 */
	String getUploadUrl();

	/**
	 * This method returns the receiver's filter. A filter
	 * is <strong>always</strong> returned. If no filter is required,
	 * consider returning an instance of {@link NullFilter}.
	 * 
	 * @return an instance of a class that implements {@link UsageDataEventFilter}
	 */
	UsageDataEventFilter getFilter();

	boolean hasUserAcceptedTermsOfUse();

	boolean isEnabled();

	String getUserId();

	String getWorkspaceId();

	String getUserAgent();

}