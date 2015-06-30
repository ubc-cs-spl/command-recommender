package ca.ubc.cs.commandrecommender.usagedata.gathering.handlers;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

import ca.ubc.cs.commandrecommender.usagedata.gathering.UsageDataCaptureActivator;
import ca.ubc.cs.commandrecommender.usagedata.gathering.settings.UsageDataCaptureSettings;

public class ContextWriterToggleHandler extends AbstractHandler implements
		IElementUpdater {

	public ContextWriterToggleHandler() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("the context writer toggle was pressed");
		UsageDataCaptureSettings settings = UsageDataCaptureActivator
				.getDefault().getSettings();
		// reverse boolean for this setting
		settings.setScreencapEnabled(!settings.isScreencapEnabled());
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		// TODO auto-generated method implementation
	}
}
