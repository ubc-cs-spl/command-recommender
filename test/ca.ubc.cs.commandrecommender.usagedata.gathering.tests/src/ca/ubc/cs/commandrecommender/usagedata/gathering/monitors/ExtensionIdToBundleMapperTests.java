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
package ca.ubc.cs.commandrecommender.usagedata.gathering.monitors;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExtensionIdToBundleMapperTests {
	@Test
	public void testname() throws Exception {
		ExtensionIdToBundleMapper mapper = new ExtensionIdToBundleMapper("org.eclipse.ui.perspectives");
		assertEquals("org.eclipse.ui.ide.application", mapper.getBundleId("org.eclipse.ui.resourcePerspective"));
		mapper.dispose();
	}
}
