<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- Busqueda Registros Entradas Index --%>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%
    String usuario=request.getRemoteUser();
%>

<html>
    <head>
        <title><fmt:message key='registre_entrades'/></title>
        
        
        
    </head>
    <body bgcolor="#FFFFFF">
        
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><fmt:message key='consulta_entrades'/></li>
		</ul>
		<!-- Fi Molla pa-->
 		<br/>
		<br/>
		<br/>
        <p>&nbsp;</p>
        <table align="center" >
            <tr>
            <td>
            	<div id="menuEntrada">
            	<ul style="margin-right: 5px">
					<li><a href="busquedaEntradasXFechas.jsp"><font class="menu"><fmt:message key='per_oficines_i_dates'/></font></a></li>
                	<li><a href="busquedaEntradasXRegistro.jsp"><font class="menu"><fmt:message key='registre.per_numero_i_any_entrada'/></font></a></li>
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