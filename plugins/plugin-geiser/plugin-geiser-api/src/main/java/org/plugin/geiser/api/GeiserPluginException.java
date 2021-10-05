/**
 * 
 */
package org.plugin.geiser.api;

/**
 * Excepció que es produeix al accedir a un sistema extern.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@SuppressWarnings("serial")
public class GeiserPluginException extends RuntimeException {

	public GeiserPluginException(
			String message) {
		super(message);
	}

	public GeiserPluginException(
			String message,
			Throwable cause) {
		super(message, cause);
	}

}
