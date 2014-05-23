package ca.ubc.cs.commandrecommender.usagedata.gathering.monitors;


import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class KeyBindingMonitor {
	private boolean potentialBindingUsed = false;
	private Set<Integer> keysPressed;
	
	public KeyBindingMonitor(){
		keysPressed = new HashSet<Integer>();
	}
	
	public final class KeyPressedListener implements Listener{
		public void handleEvent(Event event) {
			handleKeyPressed(event);
		}
	}
	
	public final class KeyReleasedListener implements Listener{
		public void handleEvent(Event event) {
			handleKeyReleased(event);
		}
	}
	
	public void handleKeyPressed(Event event){
		if (isModifierKey(event)) {
			keysPressed.add(event.keyCode);
			potentialBindingUsed = true;
		} 
	}

	private boolean isModifierKey(Event event) {
		return (event.keyCode & SWT.MODIFIER_MASK) != 0;
	}
	
	public void handleKeyReleased(Event event){
		if (isModifierKey(event)) {
			keysPressed.remove(event.keyCode);
			if(keysPressed.size() == 0){
				potentialBindingUsed = false;
			}
		}
	}
	
	private final KeyPressedListener keyPressedlistener = new KeyPressedListener();
	private final KeyReleasedListener keyReleasedListener = new KeyReleasedListener(); 
	
	public KeyPressedListener getKeyPressedListener(){
		return keyPressedlistener;
	}
	
	public KeyReleasedListener getKeyReleasedListener(){
		return keyReleasedListener;
	}
	
	public String getBindingUsed(){
		if(potentialBindingUsed){
			return "1";
		}else{
			return "0";
		}
	}
	
	public void commandExectued(){
		keysPressed.clear();
		potentialBindingUsed = false;
	}
	
}