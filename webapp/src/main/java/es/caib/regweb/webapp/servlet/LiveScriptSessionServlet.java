/**
 * Copyright 2002 Bob Lee (http://www.crazybob.org/)
 * This product is licensed under the GPL 
 * (http://www.gnu.org/licenses/gpl.txt).
 */

package es.caib.regweb.webapp.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;

/**
 * Base class for LiveScript invocation Servlets. Enables client scripts 
 * (for example, Javascript running in a web browser) to invoke methods
 * on the server. 
 *
 * <p>Subclass and implement the {@link #invoke(java.lang.String[]) invoke()} 
 * method.
 *
 * @author Bob Lee (crazybob@crazybob.org)
 * @version 1.0
 */
public abstract class LiveScriptSessionServlet extends HttpServlet {

  /**
   * Handles invocation request from client script.
   * @param args Arguments passed to the script function.
   * @return Value that will be passed back to the remote handler.
   */
  protected abstract String invoke(String[] args, HttpServletRequest request) throws LiveScriptException;

  /** Blank image. */
  private static final byte[] BLANK_IMAGE = {
      (byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38,
      (byte)0x39, (byte)0x61, (byte)0x01, (byte)0x00,
      (byte)0x01, (byte)0x00, (byte)0x80, (byte)0x00,
      (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
      (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x21,
      (byte)0xF9, (byte)0x04, (byte)0x01, (byte)0x00,
      (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x2C,
      (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
      (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00,
      (byte)0x40, (byte)0x02, (byte)0x02, (byte)0x4C,
      (byte)0x01, (byte)0x00, (byte)0x3B
  };

  /** No arguments. */
  private String[] NO_ARGS = {};
  
	/** 
   * Handles client request, calls <code>invoke()</code> method, 
   * and returns result to client. 
   */
  public final void doGet(HttpServletRequest request, 
    HttpServletResponse response) throws ServletException, IOException 
  {
    // name to use for cookie.
    String name = request.getParameter("id"); 
    if (name == null) throw new NullPointerException(
      "ID can not be null.");

    // function arguments.  
    String[] args = request.getParameterValues("arg");
    if (args == null) args = NO_ARGS;
    
    // invoke method.
    String result;
    try {
      result = invoke(args, request);
    }
    catch (LiveScriptException e) {
      result = "LIVE_SCRIPT_EXCEPTION=" + e.getMessage();
    }
    
    // invoke method & store return value in cookie.
    Cookie resultCookie = new Cookie(name, result);
    resultCookie.setPath("/");
    response.addCookie(resultCookie);

    // output blank image.
    response.setContentType("image/gif");
    response.setContentLength(BLANK_IMAGE.length);
    OutputStream out = response.getOutputStream();
    out.write(BLANK_IMAGE);
    out.close();
	}

}
