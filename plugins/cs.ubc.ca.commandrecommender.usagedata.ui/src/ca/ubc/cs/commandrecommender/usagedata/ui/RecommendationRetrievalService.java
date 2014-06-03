package ca.ubc.cs.commandrecommender.usagedata.ui;

import java.io.IOException;
import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressConstants;

import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;

import com.google.gson.Gson;

public class RecommendationRetrievalService {
	private String recommendationUrl;
	
	private class UsageData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String user_id;
		
		UsageData(String user_id){
			this.user_id = user_id;
		}
	}

	public String getUrlFromServer(){
		HttpClient client = new DefaultHttpClient(); 
		HttpPost httpPost = new HttpPost("http://localhost:3000/recommend");
		String user_id = UsageDataRecordingActivator.getDefault().getSettings().getUserId();
		HttpEntity entity = null;
		Gson parser = new Gson();

		UsageData data = this.new UsageData(user_id);
		String recommendations = null;
		String jsonData = parser.toJson(data);
		try {
			entity = new ByteArrayEntity(jsonData.getBytes(HTTP.UTF_8));
			httpPost.setEntity(entity);
			httpPost.setHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			HttpResponse response = client.execute(httpPost);	
			recommendations = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return recommendations;
	}
	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public void getRecommendation() {
		Job job = new Job("Retrieving Recommendations...") { //$NON-NLS-1$
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				recommendationUrl = getUrlFromServer();
				showResults();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.setPriority(Job.LONG);
		job.schedule();
	}
	
	public boolean isModal(Job job){
		Boolean isModal = (Boolean) job.getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
		if(isModal == null)
			return false;
		return isModal.booleanValue();
	}
	
	private void showResults() {
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				recommendationRecieved().run();
			}
		});
		
	}
	
	protected Action recommendationRecieved(){
		return new Action("Recommendaiton Received"){
			public void run(){
				MessageDialog.openInformation(getShell()
						, "Recommedation Recieved"
						, "Please visit: " + recommendationUrl + " \nto see your recommendations.");
			}
		};
	}
	
	private Shell getShell(){
		 IWorkbench wb = PlatformUI.getWorkbench();
		 IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		 IWorkbenchPage page = win.getActivePage();
		 return page.getWorkbenchWindow().getShell();
	}
}
