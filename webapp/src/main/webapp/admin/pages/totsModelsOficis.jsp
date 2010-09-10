<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head><title><fmt:message key='seleccioni_model'/></title>
	<script>
	function selecciona(cod) {
 	   top.opener.setModel(cod);
 	   close();
	}
	</script>
</head>
<body>
<table width="100%" border="0">
<c:choose>
<c:when test="${modelsSize eq 0}">
  <tr>
    <td><fmt:message key="no_hi_ha_elements"/></td>
  </tr>
</c:when>
<c:otherwise>
<c:forEach var="item" begin='0' end='${modelsSize-1}' step='1'>
	<c:set var="codiModel" value="${models[item]}" />

  <c:set var="clase" value="impar" />
  <c:if test="${(item%2)==0}"><c:set var="clase" value="par" /></c:if>

  <tr class="<c:out escapeXml='false' value='${clase}'/>">
  <td><a href="javascript:selecciona('<c:out escapeXml='false' value='${codiModel}'/>')"><c:out escapeXml='false' value='${codiModel}'/></a></td>
  </tr>
</c:forEach>
</c:otherwise>
</c:choose>
</table>
</body>
</html>
