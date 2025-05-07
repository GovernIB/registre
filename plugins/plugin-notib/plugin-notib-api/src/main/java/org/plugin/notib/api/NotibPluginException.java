package org.plugin.notib.api;

/**
 * Excepció que es produeix al accedir a un sistema extern.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class NotibPluginException extends RuntimeException {

	public NotibPluginException(
			String message) {
		super(message);
	}

	public NotibPluginException(
			String message,
			Throwable cause) {
		super(message, cause);
	}

}
