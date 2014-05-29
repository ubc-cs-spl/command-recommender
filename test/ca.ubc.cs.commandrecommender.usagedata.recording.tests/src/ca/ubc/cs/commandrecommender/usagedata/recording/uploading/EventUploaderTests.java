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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.equinox.http.jetty.JettyConstants;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import ca.ubc.cs.commandrecommender.usagedata.gathering.events.UsageDataEvent;
import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.settings.UploadSettings;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.CsvEventStorageConverter;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.IEventStorageConverter;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util.MockUploadSettings;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util.UploadGoodServlet;
import ca.ubc.cs.commandrecommender.usagedata.recording.uploading.util.UploaderTestUtils;

public class EventUploaderTests {
	private static final String GOOD_SERVLET_NAME = "/upload_good";
	private static final String SERVER_NAME = "usagedata.upload.tests";
	private static final String TEST_FILE = "usagedata.csv";
	private static int port;
	
	@SuppressWarnings("rawtypes")
	private static ServiceTracker tracker;
	private TestEventUploader eventUploader;
	private UploadParameters uploadParameters;
	
	private class TestEventUploader extends EventUploader{
		private IEventStorageConverter storage = new TestCsvEventStorageConverter(new File(TEST_FILE));
		public TestEventUploader(IHttpEntityHandler entityHandler, UploadParameters uploadParameters) {
			super(entityHandler, uploadParameters);
		}
		
		@Override
		public UploadResult doUpload(IProgressMonitor monitor) throws Exception{
			return super.doUpload(monitor);
		}
		
		@Override
		protected List<UsageDataEvent> getEvents(){
			List<UsageDataEvent> events = new ArrayList<UsageDataEvent>();
			IEventStorageConverter converter = getEventStorage();
			try {
				events = converter.readEvents();
			} catch (StorageConverterException e) {
				e.printStackTrace();
			}
			return events;
		}
		
		@Override
		public IEventStorageConverter getEventStorage(){
			return storage;
		}		
	}
	
	private class TestCsvEventStorageConverter extends CsvEventStorageConverter{
		private File test_file;
		
		public TestCsvEventStorageConverter(File test_file){
			this.test_file = test_file;
		}
		
		@Override
		public File getEventStorageFile(){
			return test_file;
		}
		
		@Override
		protected File computeArchiveFile(){
			return new File(test_file.getParent(), "archive_usagedata_1.csv");
		}
	}
	
	@Before
	public void setUp() throws Exception{
		uploadParameters = new UploadParameters();
		eventUploader = new TestEventUploader(new CsvHttpEntityHandler(), uploadParameters);
		startServer();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@BeforeClass
	public static void startServer() throws Exception {
		Dictionary<String, Object> settings = new Hashtable<String, Object>();	
		settings.put(JettyConstants.OTHER_INFO, SERVER_NAME);
		settings.put("http.port", 0);
		JettyConfigurator.startServer(SERVER_NAME, settings);
		
		ServiceReference[] reference = UsageDataRecordingActivator.getDefault().getBundle().getBundleContext().getServiceReferences("org.osgi.service.http.HttpService", "(other.info=usagedata.upload.tests)"); 
		Object assignedPort = reference[0].getProperty("http.port"); 
		port = Integer.parseInt((String)assignedPort);
		
		tracker = new ServiceTracker(UsageDataRecordingActivator.getDefault().getBundle().getBundleContext(), reference[0], null);
		tracker.open();
		HttpService server = (HttpService)tracker.getService();
		server.registerServlet(GOOD_SERVLET_NAME, new UploadGoodServlet(), null, null);	
	
	}
	
	@AfterClass
	public static void stopServer() throws Exception {
		tracker.close();
		JettyConfigurator.stopServer(SERVER_NAME);
	}
	
	@Test
	public void testBigUpload() throws Exception {
		MockUploadSettings settings = new MockUploadSettings();
		settings.setUploadUrl("http://localhost:" + port + GOOD_SERVLET_NAME);
		uploadParameters.setSettings(settings);
		
		File fileToUpload = UploaderTestUtils.createBogusUploadDataFile(50);
		
		
		 UploadResult result = eventUploader.doUpload(new NullProgressMonitor());

		assertEquals(200, result.getReturnCode());
	}
	
	@Test
	public void testInvalidUrl() throws Exception {
		MockUploadSettings settings = new MockUploadSettings();
		settings.setUploadUrl("httpx://localhost:" + port + GOOD_SERVLET_NAME);
		uploadParameters.setSettings(settings);
		
		try {
			UploadResult result = eventUploader.doUpload(new NullProgressMonitor());
			assertFalse(result.getReturnCode() == -1);
		} catch (IllegalStateException e) {
			// Expected
		} 
	}

	@Test
	public void testUnknownHost() throws Exception {
		MockUploadSettings settings = new MockUploadSettings();
		settings.setUploadUrl("http://localhost:" + port + "/Non-existent-path");
		uploadParameters.setSettings(settings);
		
		File file = UploaderTestUtils.createBogusUploadDataFile(1);

	
		UploadResult result = eventUploader.doUpload(new NullProgressMonitor());
		
		assertEquals(404, result.getReturnCode());
	}
		
	@Test
	public void testTermsOfUseNotAccepted() {
		UploadSettings settings = new MockUploadSettings() {
			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean hasUserAcceptedTermsOfUse() {
				return false;
			}
		};
		uploadParameters.setSettings(settings);
		assertFalse(eventUploader.hasUserAuthorizedUpload());
	}

	@Test
	public void testNotEnabled() {
		UploadSettings settings = new MockUploadSettings() {
			@Override
			public boolean isEnabled() {
				return false;
			}
			
			@Override
			public boolean hasUserAcceptedTermsOfUse() {
				return true;
			}
		};
		uploadParameters.setSettings(settings);
		
		assertFalse(eventUploader.hasUserAuthorizedUpload());
	}
}