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
package org.eclipse.epp.usagedata.internal.gathering;

import org.eclipse.epp.usagedata.internal.gathering.services.UsageDataServiceLifecycleTests;
import org.eclipse.epp.usagedata.internal.gathering.services.UsageDataServiceTests;
import org.eclipse.epp.usagedata.internal.gathering.settings.UsageDataCaptureSettingsTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This test suite must be run in the workbench.
 * 
 * @author Wayne Beaton 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	UsageDataServiceLifecycleTests.class,
	UsageDataServiceTests.class,
	UsageDataCaptureSettingsTests.class
})
public class AllTests {

}
