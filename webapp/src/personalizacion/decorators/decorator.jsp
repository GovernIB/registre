<%@ page pageEncoding="UTF-8"%>
<%@ page import="java.util.*, es.caib.regweb.logic.helper.*" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="sitemesh-decorator" prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/1999/REC-html401-19991224">

<html>
  <head>
    <title><fmt:message key="registre_e_s"/></title>
    <link type="text/CSS" rel="stylesheet" href="<c:url value='/css/estilos.css'/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
   <%-- <link rel="shortcut icon" href="<c:url value='/favicon.ico'/>"/>--%>
    <jsp:include page="favicon.jsp"/>

    <decorator:head />
  </head>
  <body bgcolor="#FFFFFF" text="#000000">
    <%
      String usuario=request.getRemoteUser();	
      session.setAttribute("nombre_usuario",usuario);
    %>
    <%
      String logourl = System.getProperty("entitat.logourl");
      String appTitol= System.getProperty("entitat.aplicacio.titol");
      if(logourl == null){
          logourl = "/imagenes/aplicacio/logo_caib.gif";
      }
      if(appTitol == null){
          appTitol = "Govern de les illes Balears";
      }
    %>
    <div id="capsal">
     <img src="<c:url value='<%=logourl%>'/>" alt="<%=appTitol%>" />
      <!-- Si es vol canviar el logo de l'aplicació, anyadir el següent estil al h1: style="background-image:url(imagenes/aplicacio/marcsup_titol_ico.gif);" -->
      <h1><fmt:message key="registre_general"/></h1>
    </div>  
    <div id="capsalUsuari">
      <strong><%=session.getAttribute("nombre_usuario")%></strong>
    </div>

    <decorator:body />

    <div style="position: relative;bottom:-1em; 0; width: 100%">
    	<div id="peu">
         <div style="float:right;"><%=Versio.VERSIO%></div>
          <%  if (appTitol==null) {%>
            <fmt:message key="copyright"/>
            <% } else {%>
            <%=appTitol%>
            <% } %>

    	</div>
    </div>
  </body>
</html>
