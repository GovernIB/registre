/**
 * Copyright 2002 Bob Lee (http://www.crazybob.org/)
 * This product is licensed under the GPL 
 * (http://www.gnu.org/licenses/gpl.txt).
 */

package es.caib.regweb.webapp.servlet;

import javax.servlet.http.*;
import java.io.*;

/**
 * LiveScript exception. Message is returned to client script.
 * @author Bob Lee (crazybob@crazybob.org)
 * @version 1.0
 */
public class LiveScriptException extends Exception {

  public LiveScriptException(String message) {
    super(message);
  }

}
