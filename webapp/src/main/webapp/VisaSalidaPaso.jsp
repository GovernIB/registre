<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    String usuario=request.getRemoteUser();
%>

<html>
    <head>
        <title><fmt:message key='registre_de_sortides'/></title>
        
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript">
   function init() {
       // quit if this function has already been called
       if (arguments.callee.done) return;

       // flag this function so we don't do the same thing twice
       arguments.callee.done = true;

       // Redirigim 
       setTimeout("document.getElementById('pasoForm').submit()", 500);
   };

   /* for Mozilla */
   if (document.addEventListener) {
       document.addEventListener("DOMContentLoaded", init, false);
   }

   /* for Internet Explorer */
   /*@cc_on @*/
   /*@if (@_win32)
       document.write("<script defer src=ie_onload.js><"+"/script>");
   /*@end @*/

   /* for other browsers */
   window.onload = init;
  </script>
    </head>
    
   <%-- <body > --%>
     <body>
        
		<p class="pasoSalida"><strong><fmt:message key='processant_registre'/> . . . . . . .</strong></p>
        <form id="pasoForm" name="pasoForm" action="VisaSalidaPost.jsp" method="post">
<%
    Map mapaParametros=request.getParameterMap();
    Iterator clavesParametros = mapaParametros.keySet().iterator();
    String nombreParametro="";
    while (clavesParametros.hasNext()) {
        nombreParametro=(String)clavesParametros.next();
        %>
            <input type="hidden" name="<%=nombreParametro%>" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(request.getParameter(nombreParametro))%>">
        <%
    }
%>
        </form>
   		
          
   		
        </body>
</html>
