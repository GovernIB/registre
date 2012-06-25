<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
  <body>

  	<!-- Molla pa --> 
  	<ul id="mollaPa">
  		<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
  		<li><fmt:message key="administracio"/></li>		
  	</ul>
  	<!-- Fi Molla pa-->    

    <br/>
  
    <c:choose>
    <c:when test="${empty missatge}">
  		<p>&nbsp;</p>
  		<div style="width: 250px;padding-left:10px;" id="menuDocAdm">
        <p>&nbsp;</p>
  	    <p><strong><fmt:message key="actualitzacio_informacio_permanent"/></strong></p>
  			<ul style="padding-top:0px;">
  				<li><a href="<c:url value='/admin/controller.do?accion=autoritzUsuari'/>"><fmt:message key="autoritzacions"/></a></li>
  				<li><a href="<c:url value='/admin/controller.do?accion=autoritzOficina'/>"><fmt:message key="autorizacions_oficina"/></a></li>
				<li><a href="<c:url value='/admin/controller.do?accion=oficines'/>"><fmt:message key="oficines"/></a></li>
  				<li><a href="<c:url value='/admin/controller.do?accion=oficinesFisiques'/>"><fmt:message key="oficines_fisiques"/></a></li>
  				<li><a href="<c:url value='/admin/controller.do?accion=organismesOficina'/>"><fmt:message key="organismes_per_oficina"/></a></li>
  				<li><a href="<c:url value='/admin/controller.do?accion=organismes'/>"><fmt:message key="organismes"/></a></li>
  				<% if (es.caib.regweb.logic.helper.Conf.get("viewRegistre012","false").equalsIgnoreCase("true")){%>
  					<li><a href="<c:url value='/admin/controller.do?accion=municipis060'/>"><fmt:message key="municipis_012"/></a></li>
  				<%} %>
  				<li><a href="<c:url value='/admin/controller.do?accion=entitats'/>"><fmt:message key="entitats"/></a></li>
  				<li><a href="<c:url value='/admin/controller.do?accion=comptadors'/>"><fmt:message key="inicialitzacio_comptador"/></a></li>
				<li><a href="<c:url value='/admin/controller.do?accion=agrupacionsgeografiques'/>"><fmt:message key="agrupacions_geografiques"/></a></li>
  				<li><a href="<c:url value='/admin/controller.do?accion=tipusDocuments'/>"><fmt:message key="tipus_documents"/></a></li>
          		<% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){%>
  					<li><a href="<c:url value='/admin/controller.do?accion=unitatsDeGestio'/>"><fmt:message key="unitats_de_gestio"/></a></li> 				
  				<%} %>
  		 		<li><a href="<c:url value='/admin/controller.do?accion=gestioDocuments'/>"><fmt:message key="admin.gestio_documents"/></a></li>
  		 		
  		 	</ul>

            <c:if test="${initParam['admin.view.traspasos']}">
	            <p><strong><fmt:message key="procediments"/></strong></p>
	  			<ul style="padding-top:0px;">
	  				<li><a href="<c:url value='/admin/controller.do?accion=traspassos'/>"><fmt:message key="traspassos_fogaiba"/></a></li>
	  			</ul>
            </c:if>

  	  </div>

    </c:when>
    <c:otherwise>

  	  <div name="RecuadreCentrat" id="RecuadreCentrat" class="RecuadreCentrat">
  		  <a style="align:center;" href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="tornar_administracio"/></a>
  	  </div>

  	  <table class="recuadroErrors" width="591" align="center">
        <tr>
          <td>
            <p><b><fmt:message key="error_processant_peticio"/></b></p>
            <ul>
              <li><b><fmt:message key="descripcio_error"/></b> <c:out escapeXml='false' value="${descMissatge}"/></li>
  						<li><b><fmt:message key="informacio_addicional"/></b> <c:out escapeXml='false' value="${mesInfoMissatge}"/></li>
            </ul>
          </td>
        </tr>
      </table>
  	  <div name="RecuadreCentrat" id="RecuadreCentrat2" class="RecuadreCentrat">
  		  <a style="align:center;" href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="tornar_administracio"/></a>
  	  </div>
    </c:otherwise>
    </c:choose>
		
  </body>
</html>
