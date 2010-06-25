<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
       
        <html lang="es">
            <head>
                <title><fmt:message key='importar_repro'/></title>
                
                
                <link rel="shortcut icon" href="favicon.ico"/>	
			</head>
			<body bgcolor="#ECE9D8">
				<!-- capçal --> 
			     
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<center><h2><fmt:message key='no_hi_ha_cap_repro_a_exportar'/></h2></center>
				<center><a href="<%=request.getHeader("REFERER")%>"><font class="menu"><fmt:message key='tornar'/></font></a></center>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				
       		 	         
       		 	
        	</body>
		</html>
