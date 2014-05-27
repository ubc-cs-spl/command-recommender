package ca.ubc.cs.commandrecommender.usagedata.recording.uploading;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.IEventStorageConverter;
import ca.ubc.cs.commandrecommender.usagedata.recording.storage.StorageConverterException;

public abstract class AbstractEventUploader extends AbstractUploader {

	
	
	public static AbstractEventUploader createUploader(String type, UploadParameters uploadParameters){
		if(UPLOAD_TYPE_CSV.equals(type))
			return (AbstractEventUploader) new CsvUploader(uploadParameters);
		else
			return (AbstractEventUploader) new CsvUploader(uploadParameters); 
	}
	

	/**
	 * Uploads are done with a {@link Job} running in the background
	 * at a relatively low priority. The intent is to make the user
	 * as blissfully unaware that anything is happening as possible.
	 * <p>
	 * Once the job has been started, the values on the instance
	 * cannot be modified. The instance is <em>not</em> reusable.
	 * </p>
	 */
	public synchronized void startUpload() {
		checkValues();
		if (uploadInProgress) return;
		uploadInProgress = true;
		Job job = new Job("Uploading usage data...") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				UploadResult result = upload(monitor);
				uploadInProgress = false;
				fireUploadComplete(result);
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.schedule();
	}
	
	/**
	 * Do the upload. This is basically a wrapper method that invokes the real
	 * behaviour and then deals with the fallout.
	 * 
	 * @param monitor
	 *            an instance of something that implements
	 *            {@link IProgressMonitor}. Must not be <code>null</code>.
	 * @return
	 */
	protected UploadResult upload(IProgressMonitor monitor) {
		UploadResult result = null;
		
		try {
			long start = System.currentTimeMillis();
			result = doUpload(monitor);
			long duration = System.currentTimeMillis() - start;
			
			if (result.isSuccess()) {
				UsageDataRecordingActivator.getDefault().log(IStatus.INFO, "Usage data uploaded to %1$s in %2$s milliseconds.", getUploadUrl(), duration); //$NON-NLS-1$
			} else {
				UsageDataRecordingActivator.getDefault().log(IStatus.INFO, "Usage data upload to %1$s failed with error code %2$s.", getUploadUrl(), result.getReturnCode()); //$NON-NLS-1$
			}
			
		} catch (IllegalStateException e) {
			UsageDataRecordingActivator.getDefault().log(IStatus.WARNING, e, "The URL provided for usage data upload, %1$s, is invalid.", getUploadUrl()); //$NON-NLS-1$
		} catch (UnknownHostException e) {
			UsageDataRecordingActivator.getDefault().log(IStatus.WARNING, e, "The usage data upload server at %1$s could not be found.", getUploadUrl()); //$NON-NLS-1$
		} catch (ConnectException e) {
			UsageDataRecordingActivator.getDefault().log(IStatus.WARNING, e, "Could not connect to the usage data upload server at %1$s.", getUploadUrl()); //$NON-NLS-1$
		} catch (InterruptedIOException e) {
			UsageDataRecordingActivator.getDefault().log(IStatus.WARNING, e, "A socket timeout occurred while trying to upload usage data.");			 //$NON-NLS-1$
		} catch (Exception e) {
			UsageDataRecordingActivator.getDefault().log(IStatus.WARNING, e, "An exception occurred while trying to upload usage data."); //$NON-NLS-1$
		}
		
		return result;
	}
	
	/**
	 * This method does the heavy lifting when it comes to downloads.
	 * 
	 * I can envision a time when we may want to upload something other than files.
	 * We may, for example, want to upload an in-memory representation of the files.
	 * For now, in the spirit of having something that works is better than
	 * overengineering something you may not need, we're just dealing with files.
	 * 
	 * @param monitor
	 *            an instance of something that implements
	 *            {@link IProgressMonitor}. Must not be <code>null</code>.
	 * @throws Exception 
	 */
	protected UploadResult doUpload(IProgressMonitor monitor) throws Exception {
		monitor.beginTask("Upload", 0); //$NON-NLS-1$
		UsageDataRecordingActivator.getDefault().prepareUsageDataRecorderForUpload();
		/*
		 * The files that we have been provided with were determined while the recorder
		 * was suspended. We should be safe to work with these files without worrying
		 * that other threads are messing with them. We do need to consider that other
		 * processes running outside of our JVM may be messing with these files and
		 * anticipate errors accordingly.
		 */
		
		// TODO Does it make sense to create a custom exception for this?
		if (!hasUserAuthorizedUpload()) throw new Exception("User has not authorized upload."); //$NON-NLS-1$
	
		
		HttpClient client = new DefaultHttpClient(); 
		HttpPost httpPost = new HttpPost(getUploadSettings().getUploadUrl());
	
		HttpEntity entity = getEntityForUpload();
		httpPost.setEntity(entity);
		try{
			HttpResponse response = client.execute(httpPost);
			if(shouldProcessServerResponse())
				handleServerResponse(response);
			if(response.getStatusLine().getStatusCode() != 200){
				return new UploadResult(response.getStatusLine().getStatusCode());
			}
		}catch(IOException exp){
			return new UploadResult(1);
		}
		getEventStorage().archive();
		return new UploadResult(200);
	}
	

	protected IEventStorageConverter getEventStorage(){
		return UsageDataRecordingActivator.getDefault().getStorageConverter();
	}
	
	protected boolean shouldProcessServerResponse() {
		if (getUploadSettings().isLoggingServerActivity()) return true;
		if (!responseListeners.isEmpty()) return true;
		return false;
	}

	protected void handleServerResponse(HttpResponse httpResponse) {
		UploaderServerResponse response = new UploaderServerResponse(Integer.toString(httpResponse.getStatusLine().getStatusCode()), httpResponse.getStatusLine().getReasonPhrase());
		for(Object listener : responseListeners.getListeners()) {
			((UploaderResponseListener)listener).handleServerResponse(response);
		}
	}
	
	protected String getUploadUrl() {
		return getUploadSettings().getUploadUrl();
	}
	
	protected abstract HttpEntity getEntityForUpload()
			throws StorageConverterException;

}
