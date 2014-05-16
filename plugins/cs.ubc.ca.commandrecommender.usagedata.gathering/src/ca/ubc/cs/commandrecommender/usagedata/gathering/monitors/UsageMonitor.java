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
package ca.ubc.cs.commandrecommender.usagedata.gathering.monitors;

import ca.ubc.cs.commandrecommender.usagedata.gathering.services.UsageDataService;

public interface UsageMonitor {

	public abstract void startMonitoring(UsageDataService usageDataService);

	public abstract void stopMonitoring();

}