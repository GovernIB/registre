<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.*" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="sitemesh-decorator" prefix="decorator"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/1999/REC-html401-19991224">

<html>
  <head>
    <title><fmt:message key="registre_e_s"/></title>
    <link type="text/CSS" rel="stylesheet" href="<c:url value='/css/estilos.css'/>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="shortcut icon" href="<c:url value='/favicon.ico'/>"/>

    <decorator:head />
  </head>
  <body>

    <decorator:body />

  </body>
</html>
