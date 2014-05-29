package ca.ubc.cs.commandrecommender.recommender.core.model;

public class CommandRecommendation {
	
	private final String commandId;
	
	//private List<String> peopleWhoUseThisCommand;  //we might have something like this later
	
	//TODO: do we want to store these or do we want to get them when we ask for them?
	//private String keybinding;
	//private String commandName;
	//private String description;

	public CommandRecommendation(String commandId) {
		this.commandId = commandId;
	}
	
	public String getKeybinding() {
		return RecommendationUtils.getKeyBindingFor(commandId);
	}
	
	public String getName() {
		return RecommendationUtils.getCommandName(commandId);
	}
	
	public String getDescription() {
		return RecommendationUtils.getCommandDescription(commandId);
	}
	
	public String getToolTip() {
		return ""; //TODO
	}
	
	public String getUsageSuggestion() {
		return ""; //TODO
	}
	
	public String getReasonForSuggestion() {
		return ""; //TODO
	}

}
