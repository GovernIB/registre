<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
</head>
<body>
     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key="inici"/></a></li>	
			<li><a href="<c:url value='/admin/controller.do?accion=index'/>"><fmt:message key="administracio"/></a></li>	
			<li><fmt:message key="gestio_models_rebut"/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
		        <table align="center" >
            <tr>
            <td>
            	 <div  id="menuDocAdm" style="width:250px">
		

    <div align="center">

      <c:if test="${borrado}"> 
      <br><br><b> <fmt:message key='model_rebut_esborrat'/></b>
      </c:if>
      <c:if test="${grabado}"> 
      <br><br><b> <fmt:message key='model_rebut_desat'/></b>
      </c:if>

  </div>
<br/><br/><div align="center"><a href="<c:url value='/admin/controller.do?accion=modelsRebuts'/>"><fmt:message key='tornar'/></a></div>

</div>
</td>
</tr>
</table>


	</body>
</html>