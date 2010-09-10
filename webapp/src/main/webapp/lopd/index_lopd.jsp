<%@page import="java.util.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
    <head>
        <title><fmt:message key='registre_e_s'/></title>
    </head>
    <body>
    <!-- capçal --> 

	<!-- Molla pa --> 
	<ul id="mollaPa">
		<li><a href="<c:url value='/index.jsp'/>"><fmt:message key='inici'/></a></li>	
		<li><fmt:message key='registre_accessos_lopd'/></li>
	</ul>
	<!-- Fi Molla pa-->    
        <br/>
		<p>&nbsp;</p>
        <br/>
            <div id="menuDocAdm" style="width:450px" >
	            <ul>
					<li><a href="busquedaRegAccEntradasXFechas.jsp"><fmt:message key='consulta_log_registre_entrades'/></a></li>
					<li><a href="busquedaRegAccSortidesXFechas.jsp"><fmt:message key='consulta_log_registre_sortides'/></a></li>
					<li><a href="busquedaRegAccVisatsEntXFechas.jsp"><fmt:message key='consulta_log_modif_entrades_visat'/></a></li>
					<li><a href="busquedaRegAccVisatsSorXFechas.jsp"><fmt:message key='consulta_log_modif_sortides_visat'/></a></li>
				</ul>
			</div>
		<br/>
		<br/>
        <br/>
        <br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
        <!-- PEU -->
        <!-- Fi PEU -->
    </body>
</html>
