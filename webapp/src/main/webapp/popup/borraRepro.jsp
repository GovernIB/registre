<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<% request.setCharacterEncoding("UTF-8"); %>

<% 
	String tipusCookies=request.getParameter("tipusCookie");
    String nomRepro=request.getParameter("nomRepro");
	String usuario=request.getRemoteUser();	
	
	try {
		Vector vectorRepros = new Vector();
    	ReproUsuarioFacade repro = ReproUsuarioFacadeUtil.getHome().create();
		vectorRepros=repro.recuperarRepros(usuario,tipusCookies);
%>
<html lang="es" >
<head>
    <title>Llegir Repro</title>
    
    
    <link rel="shortcut icon" href="favicon.ico"/>
    <script language="javascript" src="jscripts/TAO.js"></script>
    <script language="javascript">
              
        function borrarRepro(p_nomRepro) {
	         if(p_nomRepro=='') {
		       alert("Heu d'introduir un nom per la repro");
		       return;
	         }
	      document.borrarForm.nomRepro.value = p_nomRepro;
	      document.borrarForm.submit() 
        }
    </script>
</head> 

<body bgcolor="#ECE9D8">
<%if(nomRepro == null){ %>

	<p></p>
	<div align="center"><b>Repros desades actualment (<%=vectorRepros.size()%>)</b></div>
	<p></p> 
	<form name="borrarForm" method="post"  action="borraRepro.jsp">
	    <input type="hidden" name="tipusCookie" value="<%=tipusCookies%>">
	    <input type="hidden" name="nomRepro" value="buit"> 
	</form>
    <table class="repro" width="90%">
<% 
				for (int i=0;i<vectorRepros.size();i=i+1){
					RegistroRepro regRepro = (RegistroRepro) vectorRepros.get(i);
%>
					<tr>
						<td height="20px">
							<a href="#">
								<div onclick='borrarRepro("<%=java.net.URLEncoder.encode(regRepro.getNomRepro(),"UTF-8")%>")'><%=java.net.URLDecoder.decode(regRepro.getNomRepro(),"UTF-8")%></div>
							</a>
						</td>
					</tr>
<%}//  for (...				
%>
	</table>
<%}else{ 	
	//Decodificamos el nombre de la repro
	nomRepro = java.net.URLDecoder.decode(nomRepro,"UTF-8");
	try{ // try (2)
		repro.eliminar(usuario,nomRepro);
%>
<p></p><p></p><p></p><p></p>
<div align="center"><b>Repro "<%=java.net.URLDecoder.decode(nomRepro,"UTF-8")%>" esborrada correctament.</b></div>
<%
			}catch(Exception ex){
				System.out.println("Error al esborrar la Repro." );
		        ex.printStackTrace();
%>
<p></p><p></p><p></p><p></p>
<div align="center"><b>Error al esborrar la Repro "<%=java.net.URLDecoder.decode(nomRepro,"UTF-8")%>".</b></div>
<%
			} // fin try (2)
   } // Fin if(nomRepro == null){ 
}catch(Exception e){
					System.err.println("Error dins borrarRepro.jsp.");
                    e.printStackTrace();
				}%>
	<p></p><p></p>
	<center><a href="javascript:window.close();"><fmt:message key='tancar'/></a></center>
</body>
</html>