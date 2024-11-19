package org.plugin.lema.api;

/**
 * Excepció que es produeix al accedir a un sistema extern.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class LemaPluginException extends RuntimeException {

	public LemaPluginException(
			String message) {
		super(message);
	}

	public LemaPluginException(
			String message,
			Throwable cause) {
		super(message, cause);
	}

}
