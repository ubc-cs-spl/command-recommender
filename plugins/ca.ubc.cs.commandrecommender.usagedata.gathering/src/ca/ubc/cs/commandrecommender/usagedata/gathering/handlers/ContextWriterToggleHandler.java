package ca.ubc.cs.commandrecommender.usagedata.gathering.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;
import ca.ubc.cs.commandrecommender.usagedata.gathering.settings.UsageDataCaptureSettings;

public class ContextWriterToggleHandler extends AbstractHandler {

	public ContextWriterToggleHandler() {}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("the context writer toggle was pressed");
		UsageDataCaptureSettings settings = UsageDataCaptureActivator
				.getDefault().getSettings();
		settings.setScreencapEnabled(!settings.isScreencapEnabled());
		return null;
	}
}
