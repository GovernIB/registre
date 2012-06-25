<%@page import="java.util.*, java.text.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*, java.util.Vector" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
	HistoricoEmailsFacade historicoEmails = HistoricoEmailsFacadeUtil.getHome().create();
    
    String tipoRegistro	= request.getParameter("tipoRegistro");
	int codigoOficina 	= Integer.parseInt(request.getParameter("oficina"));
	int numeroRegistro 	= Integer.parseInt(request.getParameter("numero"));
	int ano 			= Integer.parseInt(request.getParameter("ano"));
	boolean error = false;
	Vector emailsCiudadano = null;
	Vector emailsInternos = null;
	
	try{
		emailsCiudadano = historicoEmails.leer(ano, numeroRegistro, codigoOficina, tipoRegistro, es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_CIUTADA) ;
		emailsInternos = historicoEmails.leer(ano, numeroRegistro, codigoOficina, tipoRegistro, es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_INTERN) ;
	}catch (Exception ex){
		error = true;
	}
%>

<html>
    <head><title><fmt:message key='historicEmails.titulo'/></title>
    <link rel="shortcut icon" href="favicon.ico"/>
        <script>
            function selecciona() {
            close();
            }
        </script>
    </head>
    <body>
    <p class="textoResaltadoRegistro"><fmt:message key='historicEmails.titulo'/>:</p>
    	<fmt:message key='historicEmails.texto1'/> <%=((tipoRegistro.equalsIgnoreCase("E"))?"entrada ":"sortida ")%><%=numeroRegistro%>/<%=ano%> <fmt:message key='historicEmails.texto2'/> <%=codigoOficina%></span>.
    	<br/>
    	<%if(emailsCiudadano.size()>0){ %>
    	<p>
    	<fmt:message key='historicEmails.texto3'/> <%=emailsCiudadano.size() %>.
    	</p>
    	<div class="bloqueCentrado">
    	<table>
	    	<tr>
	    	<th><fmt:message key='historicEmails.tcorreu'/></th>
	    	<th><fmt:message key='historicEmails.tdata'/></th>
	    	<th><fmt:message key='historicEmails.thora'/></th>
	    	<th><fmt:message key='historicEmails.tusuari'/></th>
	    	</tr>
	    	<%for(int i=0;i<emailsCiudadano.size();i++){
	    		ParametrosHistoricoEmails param = (ParametrosHistoricoEmails) emailsCiudadano.get(i);
	    	%>
	    	<tr class="<%=(((i % 2)==0)?"par":"impar" )%>">
	    	<td><%=param.getEmailDestinatario() %></td>
	    	<td><%=param.getFecha() %></td>
	    	<td><%=param.getHora() %></td>
	    	<td><%=param.getCodigoUsuario() %></td>
	    	</tr>
	    	<%}//Fin for %>
	    </table>
    	<%}else{ %>
    	<p>
    	<fmt:message key='historicEmails.error1'/>
    	</p>
    	<%} // Fin if(emailsCiudadano.size()>0) %>
        <br/>
        <%if(emailsInternos.size()>0){ %>
    	<p><fmt:message key='historicEmails.texto4'/><%=emailsInternos.size() %>.</p>
    	<div class="bloqueCentrado">
	    	<table>
		    	<tr>
		    	<th><fmt:message key='historicEmails.tcorreu'/></th>
		    	<th><fmt:message key='historicEmails.tdata'/></th>
		    	<th><fmt:message key='historicEmails.thora'/></th>
		    	<th><fmt:message key='historicEmails.tusuari'/></th>
		    	</tr>
		    	<%for(int i=0;i<emailsInternos.size();i++){
		    		ParametrosHistoricoEmails param = (ParametrosHistoricoEmails) emailsInternos.get(i);
		    	%>
		    	<tr class="<%=(((i % 2)==0)?"par":"impar") %>">
		    	<td><%=param.getEmailDestinatario() %></td>
		    	<td><%=param.getFecha() %></td>
		    	<td><%=param.getHora() %></td>
		    	<td><%=param.getCodigoUsuario() %></td>
		    	</tr>
		    	<%}//Fin for %>
		    </table>
	    </div>
    	<%}else{ %>
    	<p><fmt:message key='historicEmails.error2'/></p>
    	<%} // Fin if(emailsCiudadano.size()>0) %>
        <br/>
        <p>
        <center><a href="javascript:selecciona()" class="textoResaltadoRegistro"><fmt:message key='tancar'/></a></center>
        </p>
    </body>
</html>
