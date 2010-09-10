<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>



<html>
  <head>
    <script language="javascript" src="<c:url value='/livescript.js'/>"></script>
  </head>
  <body>

    <!-- Molla pa --> 
    <ul id="mollaPa">
      <li><fmt:message key='inici'/></li>
    </ul>
    <!-- Fi Molla pa-->    
    <br/>
    <br/>
    <table align="center">
    <%-- Fila para los literales --%>
      <tr>
        <td align="center">
          <font class="titulo">
            <b><fmt:message key='entrades'/></b>
          </font>
        </td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td align="center">
          <font class="titulo">
            <b><fmt:message key='sortides'/></b>
          </font>
        </td>
      </tr>
      <tr>
        <td>
          <div id="menuEntrada">
            <ul>
              <li><a href="pedirdatos.jsp"><font class="menu"><fmt:message key='registre_entrades'/></font></a></li>
              <c:if test="${initParam['registro.entrada.view.preregistre']}">
              <li><a href="<c:out escapeXml="true" value='${preregistre}'/>/zonaperback/"><font class="menu"><fmt:message key='carrega_preregistre'/></font></a></li>
              </c:if>
              <li><a href="busquedaEntradasIndex.jsp"><font class="menu"><fmt:message key='consulta_entrades'/></font></a></li>
              <li><a href="ModiEntradaClave.jsp"><font class="menu"><fmt:message key='modificacio_entrades'/></font></a></li>
              <c:if test='${autorizadoVisadoEntradas}'>
              <li><a href="VisaEntradaSel.jsp"><font class="menu"><fmt:message key='entrades_pendents_visar'/></font></a></li>
              </c:if> 
              <li><a href="RemiEntradaSel.jsp"><font class="menu"><fmt:message key='oficis_pendents_remisio'/></font></a></li>
              <li><a href="busquedaOficiosIndex.jsp"><font class="menu"><fmt:message key='consulta_oficis'/></font></a></li>
            </ul>
          </div>
        </td>
        <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
        <td>    
          <div id="menuSortida">
            <ul>
              <li><a href="pedirdatosSalida.jsp"><font class="menu"><fmt:message key='registre_sortides'/></font></a></li>
              <li style="list-style-type:none">&nbsp;</li>
              <li><a href="busquedaSalidasIndex.jsp"><font class="menu"><fmt:message key='consulta_sortides'/></font></a></li>
              <li><a href="ModiSalidaClave.jsp"><font class="menu"><fmt:message key='modificacio_sortides'/></font></a></li>
              <c:if test='${autorizadoVisadoSalidas}'>
              <li><a href="VisaSalidaSel.jsp"><font class="menu"><fmt:message key='sortides_pendents_visar'/></font></a></li>
              </c:if> 
              <li><a href="RemiSalidaSel.jsp"><font class="menu"><fmt:message key='oficis_pendents_arribada'/></font></a></li>
              <li style="list-style-type:none">&nbsp;</li>
            </ul>
          </div>
        </td>
      </tr>
    </table>
    <br/>
    <!--            <table align="center" class="recuadroDocumentacio"> -->
    <div id="menuDocAdm" style="width:240px">
      <ul>
        <li><a href="documentacio.jsp"><fmt:message key='documentacio_aplicacio'/></a></li>
      <% 
      if(request.isUserInRole("RWE_LOPD")){
      %>
        <li><a href="lopd/index_lopd.jsp"><fmt:message key='registre_accessos_lopd'/></a></li>
      <% } %>
      <% 
      if(request.isUserInRole("RWE_ADMIN")){
      %>
        <li><a href="admin/controller.do?accion=index"><fmt:message key='administracio_aplicacio'/></a></li>
      <% } %>

      </ul>
    </div>
    <!--		</table> -->
    <br/>
    <br/>
    <br/>
    <br/>

  </body>
</html>
