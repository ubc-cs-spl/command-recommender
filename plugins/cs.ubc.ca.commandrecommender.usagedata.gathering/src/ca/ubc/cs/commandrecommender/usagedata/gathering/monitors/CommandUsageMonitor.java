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

import java.util.List;

import javax.swing.KeyStroke;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListener;
import org.eclipse.core.commands.NotHandledException;

import ca.ubc.cs.commandrecommender.usagedata.gathering.services.UsageDataService;

import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;

/**
 * Instances of this class monitor invocations of commands in the workbench.
 * 
 * @author Wayne Beaton
 */
public class CommandUsageMonitor implements UsageMonitor {
	private static final String COMMANDS_EXTENSION_POINT = "org.eclipse.ui.commands"; //$NON-NLS-1$

	private static final String COMMAND = "command"; //$NON-NLS-1$
	private static final String EXECUTED = "executed"; //$NON-NLS-1$
	private static final String FAILED = "failed"; //$NON-NLS-1$
	private static final String NO_HANDLER = "no handler"; //$NON-NLS-1$

	/**
	 * The {@link #executionListener} is installed into the {@link ICommandService}
	 * so that it can be notified when a command is invoked.
	 */
	private IExecutionListener executionListener;
	
	private ExtensionIdToBundleMapper commandToBundleIdMapper;
	
	private KeyBindingMonitor keyMonitor;
	
	public void startMonitoring(final UsageDataService usageDataService) {		
		executionListener = new IExecutionListener() {
			public void notHandled(String commandId, NotHandledException exception) {
				recordEvent(NO_HANDLER, usageDataService, commandId, keyMonitor.getBindingUsed());				
			}

			public void postExecuteFailure(String commandId, ExecutionException exception) {
				recordEvent(FAILED, usageDataService, commandId, keyMonitor.getBindingUsed());				
			}

			public void postExecuteSuccess(String commandId, Object returnValue) {
				
				recordEvent(EXECUTED, usageDataService, commandId, keyMonitor.getBindingUsed());				
			}

			public void preExecute(String commandId, ExecutionEvent event) {
				
			}			
		};
		Display display = PlatformUI.getWorkbench().getDisplay();
		keyMonitor = new KeyBindingMonitor();
		display.addFilter(SWT.KeyDown, keyMonitor.getKeyPressedListener());
		display.addFilter(SWT.KeyUp, keyMonitor.getKeyReleasedListener());
		getCommandService().addExecutionListener(executionListener);
		commandToBundleIdMapper = new ExtensionIdToBundleMapper(COMMANDS_EXTENSION_POINT);
	}

	private ICommandService getCommandService() {
		return (ICommandService) PlatformUI.getWorkbench().getAdapter(ICommandService.class);
	}
	
	public void stopMonitoring() {
		ICommandService commandService = getCommandService();
		if (commandService != null) commandService.removeExecutionListener(executionListener);
		commandToBundleIdMapper.dispose();
		Display display = PlatformUI.getWorkbench().getDisplay();
		if(!display.isDisposed()){
			display.removeFilter(SWT.KeyDown, keyMonitor.getKeyPressedListener());
			display.removeFilter(SWT.KeyUp, keyMonitor.getKeyReleasedListener());
		}

	}

	private void recordEvent(String what,
			final UsageDataService usageDataService, String commandId, String bindingUsed) {
		usageDataService.recordEvent(what, COMMAND, commandId, getBundleId(commandId), bindingUsed);
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
