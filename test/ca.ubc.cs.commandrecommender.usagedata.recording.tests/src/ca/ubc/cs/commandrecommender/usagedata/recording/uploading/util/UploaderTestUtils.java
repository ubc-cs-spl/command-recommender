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
package ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.CsvStorageUtils;

public class UploaderTestUtils {

	private static final int NUMBER_OF_ENTRIES_PER_DAY = 500;

	public UploaderTestUtils() throws IOException {
	}
	
	public static File createBogusUploadDataFile(int days) throws Exception {
		File file = File.createTempFile("bogusUploadData", "csv");
		FileWriter writer = new FileWriter(file);
		CsvStorageUtils.writeHeader(writer);
		for(int index=0;index<days*NUMBER_OF_ENTRIES_PER_DAY;index++) {
			CsvStorageUtils.writeEvent(writer, new UsageDataEvent("bogusWhat", "bogusKind", "bogusBundleId", 
					"bogusBundleVersion","bogusDescription","bogusBindingUsed", System.currentTimeMillis(), "", "", ""));
		}

		writer.close();
		
		return file;
	}
	
	public static List<UsageDataEvent> getTestEvents(int numEvents){
		List<UsageDataEvent> events = new ArrayList<UsageDataEvent>();
		String bogusEvent = "bogus";
		for(int i=0; i < numEvents; i++){
			String bogusEventLoop = bogusEvent + i;
			events.add(new UsageDataEvent(bogusEventLoop, bogusEventLoop, bogusEventLoop, bogusEventLoop,bogusEventLoop,bogusEventLoop, System.currentTimeMillis(), "", "", ""));
		}
		return events;
	}
}
