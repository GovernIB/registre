<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
  <title><fmt:message key='seleccioni_municipi_012'/></title>
	<script>
	function selecciona(cod) {
 	   top.opener.setMunicipis060(cod);
 	   close();
	}
	</script>
</head>
<body>
<table width="100%" border="0">
<c:forEach var="item" begin='0' end='${municipis060Size-1}' step='2'>
	<c:set var="codigo" value="${municipis060[item]}" />
	<c:set var="nombre" value="${municipis060[item+1]}" />

  <c:set var="clase" value="impar" />
  <c:if test="${((item/2)%2)==0}"><c:set var="clase" value="par" /></c:if>

  <tr class="<c:out escapeXml='false' value='${clase}'/>">
  <td><a href="javascript:selecciona('<c:out escapeXml="false" value="${codigo}"/>')"><c:out escapeXml='false' value='${codigo}'/></a></td>
  <td><c:out escapeXml='false' value='${nombre}'/></td>
  </tr>
</c:forEach>
</table>
</body>
</html>
