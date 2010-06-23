<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@page import="org.apache.log4j.Logger" %>
<%
	String tipusCookies=request.getParameter("tipusCookie");
	String usuario=request.getRemoteUser();	
	String nomRepros = new String("(");
	String DatosRepros = new String("(");

	// contentType="binary/octet-stream"
	Logger log = Logger.getLogger(this.getClass());

	try {
		Vector vectorRepros = new Vector();
		
    ReproUsuarioFacade repro = ReproUsuarioFacadeUtil.getHome().create();
		
		vectorRepros=repro.recuperarRepros(usuario,tipusCookies);
		
	   if (vectorRepros.size() > 0) {
			log.debug("Hi ha repros a exportar.");
			//System.out.println("Hi ha repros a exportar.");
			response.setHeader("Content-Type", "binary/octet-stream"); 
			response.setHeader("Content-Disposition", "attachment; filename="+tipusCookies+"Repros.txt"); 
		    response.setHeader("Cache-Control", "store"); 
		    response.setHeader("Pragma", "cache");
		    
		    for (int i=0;i<vectorRepros.size();i=i+1){ 
		     	RegistroRepro regRepro = (RegistroRepro) vectorRepros.get(i);
				out.println( regRepro.getNomRepro()+"="+regRepro.getRepro());
		    }
	    } else {
			log.debug("No hi ha repros a exportar.");
%>
       
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
<% 		} 
	}catch(Exception e){
		System.err.println(" Error al exportar Repros. "+e.getMessage() );
	    e.printStackTrace();
	}%>