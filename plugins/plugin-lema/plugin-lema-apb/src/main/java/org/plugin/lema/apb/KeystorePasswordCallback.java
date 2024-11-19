package org.plugin.lema.apb;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;

public class KeystorePasswordCallback extends AbstractPluginProperties implements CallbackHandler {

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (Callback callback : callbacks) {
			if (callback instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callback;

				pc.setPassword("apb1234");
				return;
			}
		}
	}

}