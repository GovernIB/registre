package es.caib.regweb.ws;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * CallbackHandler.
 * 
 */
public class UsernamePasswordCallbackHandler implements CallbackHandler {

	private String username;
	private String password;

	public UsernamePasswordCallbackHandler(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback ncb = (NameCallback)callbacks[i];
				ncb.setName(username);
			} else if (callbacks[i] instanceof PasswordCallback) {
				PasswordCallback pcb = (PasswordCallback)callbacks[i];
				pcb.setPassword(password.toCharArray());
			} else {
				throw new UnsupportedCallbackException(
						callbacks[i],
						"Unrecognized Callback");
			}
		}
	}
}
