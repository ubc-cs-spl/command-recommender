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
package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.CsvFileReader;

public class UsageDataFileReaderTests {
	private String header = "what,kind,bundleId,bundleVersion,description,bindingUsed,time,name,info,shortcut\n";
	private String valid =  "what,kind,bundleId,bundleVersion,description,0,123456,name,info,shortcut\n";
	private String invalidStart = "what,kind,bundleId,bundleVersion,description";
	
	@Test
	public void testIterate() throws Exception {
		String content = header + valid + valid + valid;
		
		final StringBuilder builder = new StringBuilder();

		CsvFileReader reader = new CsvFileReader(new StringReader(content));
		reader.iterate(new CsvFileReader.Iterator() {

			public void event(String line, UsageDataEvent event) {
				builder.append(line);
				builder.append("\n");
			}

			public void header(String header) {
				builder.append(header);
				builder.append("\n");
			}
		});
		assertEquals(content, builder.toString());
	}

	@Test
	public void testIterateSkipsRowsWithMissingFields() throws Exception {
	
		String invalid = invalidStart + "\n";
		final StringBuilder builder = new StringBuilder();
		
		CsvFileReader reader = new CsvFileReader(new StringReader(header+valid+invalid));
		reader.iterate(new CsvFileReader.Iterator() {

			public void event(String line, UsageDataEvent event) {
				builder.append(line);
				builder.append("\n");
			}

			public void header(String header) {
				builder.append(header);
				builder.append("\n");
			}
		});
		assertEquals(header+valid, builder.toString());
	}
	
	@Test
	public void testIterateSkipsRowsWithIncorrectlyFormattedNumberFields() throws Exception {
		String invalid = invalidStart + ",123bob\n";
		
		final StringBuilder builder = new StringBuilder();

		CsvFileReader reader = new CsvFileReader(new StringReader(header+valid+invalid));
		reader.iterate(new CsvFileReader.Iterator() {

			public void event(String line, UsageDataEvent event) {
				builder.append(line);
				builder.append("\n");
			}

			public void header(String header) {
				builder.append(header);
				builder.append("\n");
			}
		});
		assertEquals(header+valid, builder.toString());
	}
	
	
	/**
	 * This test scans through a file containing real usage data events.
	 * We're happy if this test just runs without causing any exceptions.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadSingleFile() throws Exception {
		CsvFileReader reader = new CsvFileReader(UsageDataFileReaderTests.class.getResourceAsStream("usagedata.csv"));
		reader.iterate(new CsvFileReader.Iterator() {
			public void event(String line, UsageDataEvent event) throws Exception {				
			}

			public void header(String header) throws Exception {				
			}
			
		});
	}
}
