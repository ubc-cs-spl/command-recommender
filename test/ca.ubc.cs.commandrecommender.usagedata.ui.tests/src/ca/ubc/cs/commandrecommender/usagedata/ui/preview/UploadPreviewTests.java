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
package ca.ubc.cs.commandrecommender.usagedata.ui.preview;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.filtering.MockUsageDataEventFilter;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.IEventStorageConverter;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.UploadParameters;
import ca.ubc.cs.commandrecommender.usagedata.ui.preview.util.MockUploadSettings;

/**
 * The {@link UploadPreviewTests} class tests various aspects of the
 * {@link UploadPreview} class. This test must be run as a &quot;JUnit Plug-in Test&quot;.
 * 
 * @author Wayne Beaton
 */
@SuppressWarnings("restriction")
public class UploadPreviewTests {
	UploadParameters parameters;
	UploadPreview preview;
	private Display display;
	private Shell shell;

	@Before
	public void setup() throws Exception {

		IEventStorageConverter converter = UsageDataRecordingActivator.getDefault().getStorageConverter();
		converter.archive();
		converter.clearArchive();
		converter.writeEvents(generateEvents());
		parameters = new UploadParameters();
		parameters.setSettings(new MockUploadSettings());

		preview = new UploadPreview(parameters);
		
		while (!EclipseStarter.isRunning()) Thread.sleep(100);
		display = PlatformUI.getWorkbench().getDisplay();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		preview.createControl(shell);
		shell.open();
		
		preview.startContentJob();
		preview.contentJob.join();
	}
	
	@After
	public void shutdown() {
		shell.close();
		shell.dispose();
	}
	
	@Test
	public void tableFullyPopulated() {
		assertEquals(4, preview.events.size());
	}
	
	@Test
	public void testUpdateButtons() throws Exception {
		while (!EclipseStarter.isRunning()) Thread.sleep(100);
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				doTestUpdateButtons(display);
			}
		});
	}
	
	void doTestUpdateButtons(Display display) {

		assertFalse(preview.removeFilterButton.getEnabled());
		
		((MockUsageDataEventFilter)parameters.getUserDefinedFilter()).addPattern("org.eclipse.core.*");
		
		assertTrue(preview.removeFilterButton.getEnabled());		
	}
	
	@Test
	@Ignore //TODO: This test is not currently working but filtering on the preview panel is working as expected. 
	public void testRowChangesColorWhenFilterChanges() throws Exception {
		assertNull(preview.viewer.getTable().getItem(0).getImage(0));
		assertEquals(display.getSystemColor(SWT.COLOR_BLACK), preview.viewer.getTable().getItem(0).getForeground(1));
		
		((MockUsageDataEventFilter)parameters.getUserDefinedFilter()).addPattern("org.eclipse.osgi");
		
		assertNotNull(preview.viewer.getTable().getItem(0).getImage(0));
		assertEquals(display.getSystemColor(SWT.COLOR_GRAY), preview.viewer.getTable().getItem(0).getForeground(1));
		
		((MockUsageDataEventFilter)parameters.getUserDefinedFilter()).removeFilterPatterns(new String[] {"org.eclipse.osgi"});
		
		assertNull(preview.viewer.getTable().getItem(0).getImage(0));
		assertEquals(display.getSystemColor(SWT.COLOR_BLACK), preview.viewer.getTable().getItem(0).getForeground(1));
		
	}
	
	private List<UsageDataEvent> generateEvents() {
		List<UsageDataEvent> events = new ArrayList<UsageDataEvent>();
		events.add(new UsageDataEvent("started","bundle","org.eclipse.osgi","3.4.0.v20071207","org.eclipse.osgi", "0", 1, "", ""));
		events.add(new UsageDataEvent("started","bundle","org.eclipse.equinox.common","3.4.0.v20071207","org.eclipse.equinox.common", "0", 1, "", ""));
		events.add(new UsageDataEvent("started","bundle","org.eclipse.update.configurator","3.2.200.v20071113","org.eclipse.update.configurator", "0", 1, "", ""));
		events.add(new UsageDataEvent("started","bundle","org.eclipse.core.runtime","3.4.0.v20071207","org.eclipse.core.runtime", "0", 1, "", ""));
		return events;
	}

}
