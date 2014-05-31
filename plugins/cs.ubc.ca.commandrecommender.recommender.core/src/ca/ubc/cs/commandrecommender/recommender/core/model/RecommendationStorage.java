package ca.ubc.cs.commandrecommender.recommender.core.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.engine.Comment;

import ca.ubc.cs.commandrecommender.recommender.core.Activator;

public class RecommendationStorage { 
	
	public static Connection connection;
	private final static String DATABASE_NAME = "database";
	private final static String RECOMMENDATION_TABLE_NAME = "RECOMMENDATION";
	private final static String IS_NEW_COLUMN = "ISNEW";
	private final static String COMMAND_ID_COLUMN = "COMMANDID";
	private final static String INSERT_STATEMENT = "INSERT INTO " + RECOMMENDATION_TABLE_NAME + " VALUES (?, TRUE)";

	public static void storeNewRecommendations(List<CommandRecommendation> recommendations) 
			throws SQLException {
		markRecommendationsOld();
		for (CommandRecommendation recommendation : recommendations)
			insertNewRecommendation(recommendation);
	}
	
	private static void markRecommendationsOld() throws SQLException {
		String sql = "UPDATE " + RECOMMENDATION_TABLE_NAME + " SET " + IS_NEW_COLUMN + "=FALSE";
		connection.createStatement().executeUpdate(sql);
	}
	
	public static void refreshView() {
		//TODO
	}
	
	public static List<CommandRecommendation> retrieveAllRecommendations() 
			throws SQLException {
		String sql = "SELECT * FROM " + RECOMMENDATION_TABLE_NAME;
		ResultSet rs = connection.createStatement().executeQuery(sql);
		return convertToRecommendations(rs);
	}

	public static List<CommandRecommendation> retrieveNewRecommendations()
			throws SQLException {
		String sql = "SELECT * FROM " + RECOMMENDATION_TABLE_NAME 
				+ " WHERE " + IS_NEW_COLUMN + "=TRUE";
		ResultSet rs = connection.createStatement().executeQuery(sql);
		return convertToRecommendations(rs);
	}

	private static List<CommandRecommendation> convertToRecommendations(ResultSet rs)
			throws SQLException {
		List<CommandRecommendation> recommendations = new ArrayList<CommandRecommendation>();
		while (rs.next()) 
			recommendations.add(convertToRecommendation(rs));
		return recommendations;
	}
	
	private static CommandRecommendation convertToRecommendation(ResultSet rs)
			throws SQLException {
		String commandId = rs.getString(COMMAND_ID_COLUMN);
		return new CommandRecommendation(commandId);
	}
	
	public static List<String> retrieveAllRecommendedCommandIds() 
			throws SQLException {
		String sql = "SELECT * FROM " + RECOMMENDATION_TABLE_NAME;
		ResultSet rs = connection.createStatement().executeQuery(sql);
		List<String> commandIds = new ArrayList<String>();
		while (rs.next())
			commandIds.add(rs.getString(COMMAND_ID_COLUMN));
		return commandIds;
	}
	
	public static String getDatabaseUrl() {
		File storageDir = Activator.getDefault().getStateLocation().toFile();
		String localDb = new File(storageDir, DATABASE_NAME).getAbsolutePath();
		return "jdbc:h2:file:" + localDb;
	}
	
	//Connection should be closed after using this method
	public static void initConnectionToDb() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(getDatabaseUrl(),"sa","");
		connection = conn;
		setUpTable();
	}

	public static void setUpTable() throws SQLException {
		connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " 
				+ RECOMMENDATION_TABLE_NAME + "("
				+"COMMANDID VARCHAR(255), " + IS_NEW_COLUMN + " BOOLEAN)");
	}

	public static void closeConnectionToDb() throws SQLException {
		connection.close();
	}

	private static void insertNewRecommendation(CommandRecommendation recommendation) 
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
		statement.setString(1, recommendation.getCommandId());
		statement.execute();
	}
}
