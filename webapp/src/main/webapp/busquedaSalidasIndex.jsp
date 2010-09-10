<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%-- Busqueda Registros Salidas Index --%>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>

<%
    String usuario=request.getRemoteUser();
%>

<html>
    <head>
        <title><fmt:message key='registre_de_sortides'/></title>
        
        
        
    </head>
    <body bgcolor="#FFFFFF">
        
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><fmt:message key='consulta_de_sortides'/></li>
		</ul>
		<!-- Fi Molla pa-->
        <br/>
		<br/>
		<br/>
        <p>&nbsp;</p>
        <table align="center">
		<tr><td>
            <div id="menuSortida">
            <ul>
                        <li><a href="busquedaSalidaXFechas.jsp"><font class="menu"><fmt:message key='per_oficines_i_dates'/></font></a></li>
                        <li><a href="busquedaSalidasXRegistro.jsp"><font class="menu"><fmt:message key='registre.per_numero_i_any_sortida'/></font></a></li>
            </ul>
			</div>
			</td>
		</tr>
        </table>
        <br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<br/>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		
                 
		
    </body>
</html>
