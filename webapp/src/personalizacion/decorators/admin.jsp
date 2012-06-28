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
    <link rel="shortcut icon" href="<c:url value='/favicon.ico'/>"/>

    <decorator:head />
  </head>
  <body bgcolor="#FFFFFF" text="#000000">
    <%
      String usuario=request.getRemoteUser();	
      session.setAttribute("nombre_usuario",usuario);
      Versio versio = new Versio();
    %>
    <div id="capsal">
      <a href="http://intranet.caib.es"><fmt:message key="tornar_intranet"/></a>
      <img src="<c:url value='/imagenes/aplicacio/logo_caib.gif'/>" alt="Govern de les Illes Balears" />
      <!-- Si es vol canviar el logo de l'aplicació, anyadir el següent estil al h1: style="background-image:url(imagenes/aplicacio/marcsup_titol_ico.gif);" -->
      <h1><fmt:message key="registre_general"/></h1>
    </div>  
    <div id="capsalUsuari">
      <strong><%=session.getAttribute("nombre_usuario")%></strong>
    </div>

    <decorator:body />

    <div style="position: relative;bottom:-1em; 0;width: 100%"> 
    	<div id="peu">
        <%--<div style="float:right;">${initParam['registro.version']}</div>--%>
        <div style="float:right;"><%=versio.VERSIO%></div>
      	<a href="http://dgtic.caib.es/difusioad/00index.html" title="<fmt:message key='enllac_ad'/>"><img border="0" id="logoAD" src="<c:url value='/imagenes/peu/logo_ad.gif'/>" alt="<fmt:message key='logo_ad'/>" /></a>
        	<fmt:message key="copyright"/>
    	</div>
    </div>
  </body>
</html>
