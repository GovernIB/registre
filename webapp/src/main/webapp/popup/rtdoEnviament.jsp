<%@page import="java.util.*, java.text.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    
    String correcto=request.getParameter("correcto");
    String email=request.getParameter("email");
	String codOficina = request.getParameter("oficina");
	String numeroEntrada = request.getParameter("numero");
	String anyo = request.getParameter("ano");
	String tipus = request.getParameter("tipus");
	
	String sello = numeroEntrada+"/"+anyo;
%>


<%@page import="es.caib.regweb.webapp.servlet.EmailServlet"%>
<html>
    <head><title><fmt:message key='rtdoEnviament.titulo'/></title>
        <script>
            function selecciona() {
            close();
            }
        </script>
    </head>
    <body>
    <p class="textoResaltadoRegistro"><fmt:message key='rtdoEnviament.titulo'/>:</p>
    <%if(correcto.equals("true")){ %>
    <p>
    <fmt:message key='rtdoEnviament.texto1'/> <%=sello %> <fmt:message key='rtdoEnviament.texto2'/> <%=codOficina%>
    <%if(tipus.equals(EmailServlet.TIPUS_CIUTADA) ){%><fmt:message key='rtdoEnviament.texto3'/> <%}else{ %><fmt:message key='rtdoEnviament.texto4'/> <%} %>.
    </p>
    <fmt:message key='rtdoEnviament.texto5'/> <%=email %>.
    <%}else{ %>
    <p><fmt:message key='rtdoEnviament.textoError1'/> <%=sello %> <fmt:message key='rtdoEnviament.textoError2'/> <%=codOficina%>.</p>
	<%} %>
	<br/>
        <p><center><a href="javascript:selecciona()" class="textoResaltadoRegistro"><fmt:message key='tancar'/></a></center></p>
    </body>
</html>
