package ca.ubc.cs.commandrecommender.recommender.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.ubc.cs.commandrecommender.recommender.core.model.RecommendationStorageTests;
import ca.ubc.cs.commandrecommender.recommender.core.model.RecommendationUtilsTests;

@RunWith(Suite.class)
@SuiteClasses( { 
	RecommendationUtilsTests.class,
	RecommendationStorageTests.class
})
public class AllTests {
}
