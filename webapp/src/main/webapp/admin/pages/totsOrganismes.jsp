<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
  <title><fmt:message key='seleccioni_organisme'/></title>
	<script>
	function selecciona(cod) {
 	   top.opener.setOrganisme(cod);
 	   close();
	}
	</script>
</head>
<body>
<table width="100%" border="0">
<c:forEach var="item" begin='0' end='${organismesSize-1}' step='3'>
	<c:set var="codigo" value="${organismes[item]}" />
	<c:set var="texto" value="${organismes[item+1]}" />
	<c:set var="textoLargo" value="${organismes[item+2]}" />

  <c:set var="clase" value="impar" />
  <c:if test="${((item/3)%2)==0}"><c:set var="clase" value="par" /></c:if>

  <tr class="<c:out escapeXml='false' value='${clase}'/>">
  <td><a href='javascript:selecciona("<c:out escapeXml='false' value='${codigo}'/>")'><c:out escapeXml='false' value='${codigo}'/></a></td>
  <td><c:out escapeXml='false' value='${texto}'/></td>
  </tr>
</c:forEach></table>
</body>
</html>
