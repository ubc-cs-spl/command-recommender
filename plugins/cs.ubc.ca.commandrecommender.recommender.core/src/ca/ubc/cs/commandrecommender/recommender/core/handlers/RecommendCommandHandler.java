package ca.ubc.cs.commandrecommender.recommender.core.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

import ca.ubc.cs.commandrecommender.recommender.core.RecommendationRetrivalService;
import ca.ubc.cs.commandrecommender.recommender.core.model.RecommendationUtils;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RecommendCommandHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public RecommendCommandHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"RecommenderCore",
				"We are retrieving recommendations");
		
		//TODO: run in a separate thread. 
		// Please learn about and consider Job, UIJob, or just a simple Thread
		// Learn about eclipse threading rules.
		RecommendationRetrivalService.requestRecommendations();
		return null;
	}
	
	
}
