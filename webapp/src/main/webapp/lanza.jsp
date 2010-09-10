<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<html>
    <head>
        <title><fmt:message key='registre_entrades'/> / <fmt:message key='sortides'/></title>
        <LINK TYPE="text/CSS" rel="stylesheet" HREF="/css/estilos.css">
        
        <link rel="shortcut icon" href="favicon.ico"/>
        <script>
            function abre() {
            ventana=window.open("index.jsp", "registre", "status=yes, toolbar=no, resizable=yes, scrollbars=yes");
            ventana.moveTo(0,0);
            ventana.resizeTo(screen.width, screen.height-30);
            ventana.focus();
            }
        </script>
    </head>
    <body bgcolor="#FFFFFF" text="#000000">
    &nbsp;<p>&nbsp;<p>&nbsp;<p>
        <div style="text-align: center"><img border="0" src="imagenes/ebal_g.gif" width="122" height="130" alt="<fmt:message key='inici'/>"></div>
        <div style="font-weight: bold; font-size: 24px; text-align: center"><fmt:message key='govern_de_les_illes_balears'/></div>
        <div style="font-weight: bold; font-size: 24px; text-align: center"><fmt:message key='registre_general'/></div>
        <div style="text-align: center; font-size: 200%; margin-top: 2em;">
            <a href="javascript:abre()">[ <fmt:message key='registre_entrades'/> / <fmt:message key='sortides'/> ]</a>
        </div>
    </body>
</html>
