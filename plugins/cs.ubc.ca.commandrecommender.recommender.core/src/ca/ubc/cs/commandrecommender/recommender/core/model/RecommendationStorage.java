package ca.ubc.cs.commandrecommender.recommender.core.model;

import java.io.File;
import java.util.List;

import ca.ubc.cs.commandrecommender.usagedata.recording.UsageDataRecordingActivator;

public class RecommendationStorage { 

	public static void storeNewRecommendations(List<CommandRecommendation> recommendations) {
		
	}
	
	public static void refreshView() {
		
	}
	
	public static List<CommandRecommendation> retrieveAllRecommendations() {
		return null; //TODO: stub
		
	}
	
	public static List<CommandRecommendation> retrieveNewRecommendations() {
		return null; // TODO: stub
	}
	
	public static List<String> retrieveAllRecommendedCommandIds() {
		return null;
	}
	
	public static String getDatabaseUrl() {
		File storageDir = UsageDataRecordingActivator.getDefault().getSettings().getStorageDirectory();
		String localDb = new File(storageDir, "database").getAbsolutePath();
		return "jdbc:h2:file:" + localDb;
	}

}
