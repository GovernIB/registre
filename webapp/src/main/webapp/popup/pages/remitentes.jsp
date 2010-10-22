<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://commons.apache.org/lang/stringescapeutils/functions" prefix="seu" %>
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
            <tr>
                <td colspan="2">
                    <fmt:message key="cadena_recerca"/>
                </td>
            </tr>
            <tr>
                <form action="popup.do" method="POST">
                <td>
                    <input type="hidden" name="accion" value="remitentes" />
                    <input type="text" name="subcadenaCodigo" size="7" maxlength="7" value="<c:out value='${subcadenaCodigo}' />">
                </td>
                <td>
                    <input type="text" name="subcadenaTexto" size="25" value="<c:out value='${subcadenaTexto}' />">
                    &nbsp;&nbsp;
                    <input type="submit" value="<fmt:message key='cercar'/>">
                </td>
                </form>
            </tr>
          
            <c:forEach var="item" begin='0' end='${remitentesSize-1}' step='3'>
              <c:set var="entidad1" value="${remitentes[item]}" />
              <c:set var="entidad2" value="${remitentes[item+1]}" />
              <c:set var="texto" value="${remitentes[item+2]}" />
              <c:set var="entidades" value="${entidad1}-${entidad2}" />
              <c:set var="clase" value="impar" />
              <c:if test="${((item/3)%2)==0}"><c:set var="clase" value="par" /></c:if>

              <tr class="<c:out escapeXml='false' value='${clase}'/>">
                <td><c:if test="${entidad1 != '&nbsp;'}"><a href="javascript:selecciona('<c:out escapeXml="false" value="${entidad1}"/>', '<c:out escapeXml="false" value="${entidad2}"/>', '<c:out escapeXml="false" value="${seu:escapeJavaScript(texto)}"/>')"><c:out escapeXml='false' value='${entidades}'/></a></c:if></td>
                <td><c:out escapeXml='false' value='${texto}'/></td>
              </tr>
            </c:forEach>
        </table>
    </body>
</html>
