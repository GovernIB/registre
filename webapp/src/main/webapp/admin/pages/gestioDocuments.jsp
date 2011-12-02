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
      <li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
      <li><fmt:message key="admin.gestio_documents"/></li>
    </ul>
    <!-- Fi Molla pa--> 

    <br/>
  
    <c:choose>
    <c:when test="${empty missatge}">
  		<p>&nbsp;</p>
  		<div style="width: 250px;padding-left:10px;" id="menuDocAdm">
        <p>&nbsp;</p>
  	    <p><strong><fmt:message key="admin.gestio_documents"/></strong></p>
  			<ul style="padding-top:0px;">
  		 		<li><a href="<c:url value='/admin/controller.do?accion=modelsOficis'/>"><fmt:message key="admin.models_oficis"/></a></li>
  		 		<li><a href="<c:url value='/admin/controller.do?accion=modelsRebuts'/>"><fmt:message key="models_rebuts"/></a></li>
  		 		<% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){%>
  					<li><a href="<c:url value='/admin/controller.do?accion=modelsEmail'/>"><fmt:message key="admin.models_email"/></a></li> 				
  				<%} %>
  		 	</ul>
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
