<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@page contentType="text/html"%>
<html>
    <head>
        <title><fmt:message key='popup.seleccionar_remitents'/></title>
        <script>
            function selecciona(codEntidad1, codEntidad2, desc) {
            top.opener.setEntidad(codEntidad1, codEntidad2, desc);
            close();
            }
        </script>
        
        
        <link rel="shortcut icon" href="favicon.ico"/>
    </head>
    <body>
        <table width="100%" border="0">
            <c:forEach var="item" begin='0' end='${remitentesSize-1}' step='3'>
              <c:set var="entidad1" value="${remitentes[item]}" />
              <c:set var="entidad2" value="${remitentes[item+1]}" />
              <c:set var="texto" value="${remitentes[item+2]}" />
              <c:set var="entidades" value="${entidad1}-${entidad2}" />
              <c:if test="${((item/3)%2)==0}"><c:set var="clase" value="par" /></c:if>

              <tr class="<c:out escapeXml='false' value='${clase}'/>">
                <td><a href="javascript:selecciona('<c:out escapeXml="false" value="${entidad1}"/>', '<c:out escapeXml="false" value="${entidad2}"/>', '<c:out escapeXml="false" value="${texto}"/>')"><c:out escapeXml='false' value='${entidades}'/></a></td>
                <td><c:out escapeXml='false' value='${texto}'/></td>
              </tr>
            </c:forEach>
        </table>
    </body>
</html>
