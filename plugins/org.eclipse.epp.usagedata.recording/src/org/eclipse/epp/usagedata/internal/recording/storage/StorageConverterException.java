/**
 * 
 */
package org.eclipse.epp.usagedata.internal.recording.storage;

/**
 * @author KeEr
 *
 */
public class StorageConverterException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param message
	 * @param cause
	 */
	public StorageConverterException(Throwable cause) {
		super(cause);
	}
	
	public StorageConverterException(String msg){
		super(msg);
	}

}
