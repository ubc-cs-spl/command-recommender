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
package ca.ubc.cs.commandrecommender.usagedata.recording;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.AbstractUsageDataEventFilterTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.FilterUtilsTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.PreferencesBasedFilterTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UsageDataRecordingSettingsTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.CsvHttpEntityHandlerTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.EventUploaderTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.JsonHttpEntityHandlerTests;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UsageDataFileReaderTests;


@RunWith(Suite.class)
@SuiteClasses( { 
	CsvStorageUtilsTests.class,
	UsageDataRecordingSettingsTests.class,
	AbstractUsageDataEventFilterTests.class,
	FilterUtilsTests.class,
	PreferencesBasedFilterTests.class,
	EventUploaderTests.class, 
	UsageDataFileReaderTests.class,
	CsvHttpEntityHandlerTests.class,
	JsonHttpEntityHandlerTests.class
})
public class AllTests {

}
