package cs.ubc.ca.commandrecommender.recommender.core.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecommendationUtilsTests {
	
	private static final String COMMAND_ID = "cs.ubc.ca.commandrecommender.recommender.core.commands.recommendCommand";
	
	@Test
	public void testGetCommandName() {
		assertEquals(RecommendationUtils.getCommandName(COMMAND_ID), "Get Commend Recommendations");
	}
}
