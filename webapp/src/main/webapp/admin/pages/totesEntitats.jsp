<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
    <head>
        <title><fmt:message key='${seleccionar_remitents}'/></title>
        <script>
            function selecciona(codEntidad1, codEntidad2, desc) {
            top.opener.setEntidad(codEntidad1, codEntidad2, desc);
            close();
            }
        </script>
    </head>
    <body>
      <form action="<c:url value='/admin/popup.do?accion=totesEntitats'/>">
        <table width="100%" border="0">
            <!-- <tr>
                <td colspan="2">
                  <fmt:message key='cadena_recerca'/>
                </td>
            </tr>
            <tr>
                <td>
                    <input type="text" name="subcadenaCodigo" size="7" maxlength="7">
                </td>
                <td>
                    <input type="text" name="subcadenaTexto" size="25">
                    &nbsp;&nbsp;
                    <input type="submit" value="<fmt:message key='cercar'/>">
                </td>
            </tr> -->
          <c:if test="${remitentesSize>0}">
          <c:forEach var="item" begin='0' end='${remitentesSize-1}' step='3'>
          <c:set var="entidad1" value="${remitentes[item]}" />
          <c:set var="entidad2" value="${remitentes[item+1]}" />
          <c:set var="texto" value="${remitentes[item+2]}" />
          <c:set var="entidades" value="${entidad1} ${entidad2}" />

          <c:set var="clase" value="impar" />
          <c:if test="${((item/3)%2)==0}"><c:set var="clase" value="par" /></c:if>

            <tr class="<c:out escapeXml='false' value='${clase}'/>">
              <td><a href="javascript:selecciona('<c:out escapeXml="false" value="${entidad1}"/>', '<c:out escapeXml="false" value="${entidad2}"/>', '<c:out escapeXml="false" value="${texto}"/>')"><c:out escapeXml='false' value='${entidades}'/></a></td>
              <td><c:out escapeXml='false' value='${texto}'/></td>
            </tr>
          </c:forEach>
          </c:if>
        </table>
        </form>
    </body>
</html>
