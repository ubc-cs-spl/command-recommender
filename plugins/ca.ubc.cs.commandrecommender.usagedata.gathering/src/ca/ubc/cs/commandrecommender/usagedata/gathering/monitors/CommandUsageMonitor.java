/*******************************************************************************
 * Copyright (c) 2007 The Eclipse Foundation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    The Eclipse Foundation - initial API and implementation
 *******************************************************************************/
package ca.ubc.cs.commandrecommender.usagedata.gathering.monitors;


import java.util.HashSet;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;

import ca.ubc.cs.commandrecommender.usagedata.gathering.services.UsageDataService;

/**
 * Instances of this class monitor invocations of commands in the workbench.
 * 
 * @author Wayne Beaton
 */
public class CommandUsageMonitor implements UsageMonitor {

	public static final String EVENT_KIND = "command"; //$NON-NLS-1$

	private static final String COMMANDS_EXTENSION_POINT = "org.eclipse.ui.commands"; //$NON-NLS-1$
	private static final String EXECUTED = "executed"; //$NON-NLS-1$
	private static final String FAILED = "failed"; //$NON-NLS-1$
	private static final String NO_HANDLER = "no handler"; //$NON-NLS-1$

	private static Object String;

	/**
	 * The {@link #executionListener} is installed into the {@link ICommandService}
	 * so that it can be notified when a command is invoked.
	 */
	private IExecutionListener executionListener;
	
	private ExtensionIdToBundleMapper commandToBundleIdMapper;
	
	private class CommandUsageListener implements IExecutionListener {
		
		private final UsageDataService usageDataService;
		private HashSet<String> commandsTriggeredThroughHotkey;
		
		public CommandUsageListener(UsageDataService service) {
			usageDataService = service;
			commandsTriggeredThroughHotkey = new HashSet<String>();
		}

		public void notHandled(String commandId, NotHandledException exception) {
			recordEvent(NO_HANDLER, commandId);				
		}

		public void postExecuteFailure(String commandId, ExecutionException exception) {
			recordEvent(FAILED, commandId);				
		}

		public void postExecuteSuccess(String commandId, Object returnValue) {
			recordEvent(EXECUTED, commandId);				
		}

		public void preExecute(String commandId, ExecutionEvent event) {
			if (event.getTrigger() == null)
				commandsTriggeredThroughHotkey.add(commandId);
		}
		
		private void recordEvent(String what, String commandId) {
			usageDataService.recordEvent(what, EVENT_KIND, commandId, getBundleId(commandId), null,
					getHotkeyUsageMarkerForCommand(commandId), getCommandName(commandId),
					getCommandDescription(commandId), getDefaultKeyBindingFor(commandId));
		}
		
		//Note: this modifies commandsTriggeredThroughHotkey by removing the commandId 
		//      from the set
		private String getHotkeyUsageMarkerForCommand(String commandId) {
			if (commandsTriggeredThroughHotkey.contains(commandId)) {
				commandsTriggeredThroughHotkey.remove(commandId);
				return "1";
			} else {
				return "0";
			}
		}
	}
	
	public void startMonitoring(final UsageDataService usageDataService) {
		commandToBundleIdMapper = new ExtensionIdToBundleMapper(COMMANDS_EXTENSION_POINT);		
		executionListener = new CommandUsageListener(usageDataService);
		getCommandService().addExecutionListener(executionListener);
	}

	private static ICommandService getCommandService() {
		return (ICommandService) PlatformUI.getWorkbench().getAdapter(ICommandService.class);
	}
	
	private static String getCommandDescription(String commandId) {
		ICommandService commandService = getCommandService();
		try {
			return commandService.getCommand(commandId).getDescription();
		} catch (NotDefinedException e) {
			return null;
		}
	}
	
	public static String getCommandName(String commandId) {
		ICommandService commandService = getCommandService();
		try {
			return commandService.getCommand(commandId).getName();
		} catch (NotDefinedException e) {
			return commandService.getCommand(commandId).getId();
		}
	}
	
	private static String getDefaultKeyBindingFor(String commandId) {
		IBindingService bindingService = (IBindingService) 
				PlatformUI.getWorkbench().getAdapter(IBindingService.class);
		if (bindingService == null)
			return null;
		String defaultSchemeId = bindingService.getDefaultSchemeId();
		for (TriggerSequence sequence : bindingService.getActiveBindingsFor(commandId)) {
			if (sequence == null) 
				continue;
			Binding binding = bindingService.getPerfectMatch(sequence);
			if (binding == null)
				continue;
			if ((binding.getType() == Binding.SYSTEM) && (defaultSchemeId.equals(binding.getSchemeId())))
				return sequence.format();
		}
		return null;
	}
	
	public void stopMonitoring() {
		ICommandService commandService = getCommandService();
		if (commandService != null) commandService.removeExecutionListener(executionListener);
		commandToBundleIdMapper.dispose();
	}
	
	/**
	 * This method fetches the bundle id (symbolic name) of the bundle that defines
	 * the command, commandId. 
	 * 
	 * @param commandId
	 * @return
	 */
	protected synchronized String getBundleId(String commandId) {
		return commandToBundleIdMapper.getBundleId(commandId);
	}
	
}
