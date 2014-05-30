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
	
	public String getCommandId() {
		return commandId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((commandId == null) ? 0 : commandId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CommandRecommendation other = (CommandRecommendation) obj;
		if (commandId == null) {
			if (other.commandId != null)
				return false;
		} else if (!commandId.equals(other.commandId))
			return false;
		return true;
	}

}
