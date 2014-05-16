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
package ca.ubc.cs.commandrecommender.usagedata.ui;

import ca.ubc.cs.commandrecommender.usagedata.ui.preferences.UsageDataCapturePreferencesPageTests;
import ca.ubc.cs.commandrecommender.usagedata.ui.preview.UploadPreviewTests;
import ca.ubc.cs.commandrecommender.usagedata.ui.preview.UsageDataEventWrapperTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( { 
	UsageDataEventWrapperTests.class,
	UploadPreviewTests.class,
	UsageDataCapturePreferencesPageTests.class
})
public class AllTests {

}
