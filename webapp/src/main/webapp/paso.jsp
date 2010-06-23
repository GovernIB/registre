<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%
    String usuario=request.getRemoteUser();
%>

<html>
    <head>
        <title><fmt:message key='registre_entrades'/></title>
        
        
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
<!-- Script antic, el deix per ara per si l'he de recuperar.
	Si s'ha de tornar a emprar, s'ha de posar "onload="redirige();"
	a l'etiqueta "body".
  <script type="text/javascript">
           function redirige() {
             setTimeout("document.getElementById('pasoForm').submit()", 500);
           }
        </script> -->
    </head>
    
   <%-- <body > --%>
     <body>
        
		<p class="pasoEntrada"><strong><fmt:message key='processant_registre'/> . . . . . . .</strong></p>    
    <%-- cambiar la direccion --%>
        <form id="pasoForm" name="pasoForm" action="registro.jsp" method="post">
<%
    Map mapaParametros=request.getParameterMap();
    Iterator clavesParametros = mapaParametros.keySet().iterator();
    String nombreParametro="";
    while (clavesParametros.hasNext()) {
        nombreParametro=(String)clavesParametros.next();
        %>
            <%  if (nombreParametro.equals("comentario")) { 
            %>
              <c:set var="texto" scope="page"><%=es.caib.regweb.webapp.servlet.HtmlGen.cambiarCadena(request.getParameter(nombreParametro))%></c:set>
              <div style="display:none;">
              <textarea type="hidden" cols="67" rows="3" name="<%=nombreParametro%>"><c:out escapeXml="true" value="${texto}"/></textarea>
              </div>
            <% } else { %>
            <c:set var="texto" scope="page"><%=es.caib.regweb.webapp.servlet.HtmlGen.cambiarCadena(request.getParameter(nombreParametro))%></c:set>
            <input type="hidden" name="<%=nombreParametro%>" value="<c:out escapeXml="true" value="${texto}"/>">
            <% } %>
        <%
    }
%>
       <%-- <input type=submit value="enviar"> --%>
        </form>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
 		
                 
    </body>
</html>
