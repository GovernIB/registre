<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
  <title><fmt:message key='seleccioni_oficina'/></title>
	<script>
	function selecciona(cod,codfis) {
 	   top.opener.setOficina(cod,codfis);
 	   close();
	}
	</script>
</head>
<body>
<table width="100%" border="0">
<c:forEach var="item" begin='0' end='${oficinesSize-1}' step='4'>
  <c:set var="codigo" value="${oficines[item]}" />
  <c:set var="codigoFisica" value="${oficines[item+1]}" />
  <c:set var="descripcionFis" value="${oficines[item+2]}" />
  <c:set var="descripcion" value="${oficines[item+3]}" />

  <c:set var="clase" value="impar" />
  <c:if test="${((item/4)%2)==0}"><c:set var="clase" value="par" /></c:if>

  <tr class="<c:out escapeXml='false' value='${clase}'/>">
  <td><a href="javascript:selecciona('<c:out escapeXml="false" value="${codigo}"/>','<c:out escapeXml="false" value="${codigoFisica}"/>')"><c:out escapeXml='false' value='${codigo}'/></a></td>
  <td><a href="javascript:selecciona('<c:out escapeXml="false" value="${codigo}"/>','<c:out escapeXml="false" value="${codigoFisica}"/>')"><c:out escapeXml='false' value='${codigoFisica}'/></a></td>
  <td><c:out escapeXml='false' value='${descripcion}'/></td>
  <td><c:out escapeXml='false' value='${descripcionFis}'/></td>
  </tr>
</c:forEach>
</table>
</body>
</html>
