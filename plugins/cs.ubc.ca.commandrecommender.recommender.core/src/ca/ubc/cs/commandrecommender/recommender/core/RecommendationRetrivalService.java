package ca.ubc.cs.commandrecommender.recommender.core;

import java.sql.SQLException;

import ca.ubc.cs.commandrecommender.recommender.core.model.RecommendationStorage;
import ca.ubc.cs.commandrecommender.recommender.core.model.RecommendationUtils;

public class RecommendationRetrivalService {

	public static void requestRecommendations() {
		try {
			RecommendationStorage.storeNewRecommendations(
					RecommendationUtils.getStubRecommendations());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //TODO: stub
	}
}
