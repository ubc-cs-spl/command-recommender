package ca.ubc.cs.commandrecommender.recommender.core.model;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs.commandrecommender.recommender.core.Activator;

public class RecommendationStorageTests {
	
	private CommandRecommendation r1 = new CommandRecommendation("org.eclipse.ui.file.save");
	private CommandRecommendation r2 = new CommandRecommendation("org.eclipse.jdt.ui.edit.text.java.open.editor");
	private CommandRecommendation r3 = new CommandRecommendation("org.eclipse.jdt.ui.edit.text.java.show.outline");
	private CommandRecommendation r4 = new CommandRecommendation("org.eclipse.jdt.ui.edit.text.java.organize.imports");
	private CommandRecommendation r5 = new CommandRecommendation("org.eclipse.ui.file.properties");
	private CommandRecommendation r6 = new CommandRecommendation("org.eclipse.jdt.ui.edit.text.java.refactor.quickMenu");

	@Before
	public void initEnvironment() throws SQLException, ClassNotFoundException {
		RecommendationStorage.closeConnectionToDb();
		File workingDir = Activator.getDefault().getStateLocation().toFile();
		for (File file : workingDir.listFiles()) {
			file.delete();
		}
		RecommendationStorage.initConnectionToDb();
	}
	
	@Test
	public void retrieveAllWillRetrieveAllRecommendationStored() throws SQLException {
		RecommendationStorage.storeNewRecommendations(get2StubRecommendations());
		List<CommandRecommendation> recommendations1 = RecommendationStorage.retrieveAllRecommendations();
		assertEquals(2, recommendations1.size());
		assertTrue(recommendations1.contains(r5));
		assertTrue(recommendations1.contains(r6));
		RecommendationStorage.storeNewRecommendations(get4StubRecommendations());
		List<CommandRecommendation> recommendations2 = RecommendationStorage.retrieveAllRecommendations();
		assertEquals(6, recommendations2.size());
		assertTrue(recommendations2.contains(r1));
		assertTrue(recommendations2.contains(r2));
		assertTrue(recommendations2.contains(r3));
		assertTrue(recommendations2.contains(r4));
		assertTrue(recommendations2.contains(r5));
		assertTrue(recommendations2.contains(r6));
	}
	
	@Test
	public void retrieveNewWillOnlyRetrieveNewRecommendations() throws SQLException {
		RecommendationStorage.storeNewRecommendations(get2StubRecommendations());
		List<CommandRecommendation> recommendations1 = RecommendationStorage.retrieveNewRecommendations();
		assertEquals(2, recommendations1.size());
		assertTrue(recommendations1.contains(r5));
		assertTrue(recommendations1.contains(r6));
		RecommendationStorage.storeNewRecommendations(get4StubRecommendations());
		List<CommandRecommendation> recommendations2 = RecommendationStorage.retrieveNewRecommendations();
		assertEquals(4, recommendations2.size());
		assertTrue(recommendations2.contains(r1));
		assertTrue(recommendations2.contains(r2));
		assertTrue(recommendations2.contains(r3));
		assertTrue(recommendations2.contains(r4));
		assertFalse(recommendations2.contains(r5));
		assertFalse(recommendations2.contains(r6));
	}
	
	@Test
	public void testRetrieveAllCommandIds() throws SQLException {
		RecommendationStorage.storeNewRecommendations(get2StubRecommendations());
		RecommendationStorage.storeNewRecommendations(get4StubRecommendations());
		List<CommandRecommendation> recommendations = RecommendationStorage.retrieveAllRecommendations();
		List<String> commandIds = RecommendationStorage.retrieveAllRecommendedCommandIds();
		assertEquals(6, recommendations.size());
		assertEquals(6, commandIds.size());
		for (CommandRecommendation recommendation : recommendations) 
			assertTrue(commandIds.contains(recommendation.getCommandId()));
	}

	
	@Test
	public void newlyAddedRecommendationCanBeRetrieved() throws SQLException {
		RecommendationStorage.storeNewRecommendations(get2StubRecommendations());
		assertEquals(2, RecommendationStorage.retrieveAllRecommendations().size());
		RecommendationStorage.storeNewRecommendations(get4StubRecommendations());
		assertEquals(6, RecommendationStorage.retrieveAllRecommendedCommandIds().size());
		assertEquals(6, RecommendationStorage.retrieveAllRecommendations().size());
	}	
	
	@Test
	public void currentRecommendationsBecomesOldAfterNewOnesAdded() throws SQLException {
		RecommendationStorage.storeNewRecommendations(get2StubRecommendations());
		assertEquals(2, RecommendationStorage.retrieveNewRecommendations().size());
		RecommendationStorage.storeNewRecommendations(get4StubRecommendations());
		assertEquals(6, RecommendationStorage.retrieveAllRecommendedCommandIds().size());
		assertEquals(4, RecommendationStorage.retrieveNewRecommendations().size());
	}
	
	private List<CommandRecommendation> get4StubRecommendations() {
		List<CommandRecommendation> recommendations = new ArrayList<CommandRecommendation>();
		recommendations.add(r1);
		recommendations.add(r2);
		recommendations.add(r3);
		recommendations.add(r4);
		return recommendations;
	}
	
	private List<CommandRecommendation> get2StubRecommendations() {
		List<CommandRecommendation> recommendations = new ArrayList<CommandRecommendation>();
		recommendations.add(r5);
		recommendations.add(r6);
		return recommendations;
	}

}
