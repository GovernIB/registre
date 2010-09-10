<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*, java.net.URLDecoder"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html lang="es">
    <head>
        <title>Desar Repro</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="shortcut icon" href="favicon.ico"/>
        <script language="javascript" src="jscripts/TAO.js"></script>
<% 
	String tipusCookies=request.getParameter("tipusCookie");
    String nomRepro = request.getParameter("nombre");
    String valorRepro = request.getParameter("valorCookieAgrabar");
	String usuario=request.getRemoteUser();	
	String nomRepros = new String("(");
	
	try {
		Vector vectorRepros = new Vector();
		javax.naming.InitialContext contexto = new javax.naming.InitialContext();
		
		//Cercam el EJB daccès al repositori de Repros
    ReproUsuarioFacade repro = ReproUsuarioFacadeUtil.getHome().create();
		
		//Cercam el número màxim de repros per usuari
		javax.naming.Context myenv = (javax.naming.Context) contexto.lookup("java:comp/env");
	    Integer maxRepros = (Integer)myenv.lookup("Repros.max");

		// Si no hay nombre para la nueva Repro aparece la página para solicitarla
		// Tambien comprobamos que el valor de la repro haya llegado correctamente
		if((nomRepro == null || nomRepro.equals(""))&& !(valorRepro == null || (valorRepro.equalsIgnoreCase("null")) ) ){
			vectorRepros=repro.recuperarRepros(usuario,tipusCookies);
			
			for (int i=0;i<vectorRepros.size();i=i+1){
				RegistroRepro regRepro = (RegistroRepro) vectorRepros.get(i);
				if(i>0){
					nomRepros = nomRepros +",";	
				}
				nomRepros =   nomRepros + "'" + regRepro.getNomRepro() + "'";
			}
			nomRepros = nomRepros + ")";
%>
        <SCRIPT>
            function confirmaProceso() {
	            var ok = true;
	            
	            // alert('Sin codificar:'+document.grabarForm.nombre.value);
	            var valor=trim(document.grabarForm.nombre.value);
	            valor = valor.replace("'", '"');
	            valor = URLEncode(valor);
	            //alert('Codificado:'+valor);
	           
	            var nomRepro = new Array<%=nomRepros%>;
	            
	            if (valor=='') {
		            alert("Heu d'introduir un nom per la repro.");
		            ok = false;
	            }
	            if (valor.length >= 50){
	                alert("El nom sel·leccionat per la nova repro és massa llarg.");
		            ok = false;
	            }
	 
	            for(i=0; i<nomRepro.length; i++){
	            	if(valor==nomRepro[i]){
	            	alert("El nom de la repro ja existeix.");
	            	ok = false;
	            	}
	            }
	           if(ok == false){
	           document.grabarForm.reset();
	           return false;
	           }
	           document.grabarForm.nombre.value = valor;
	           return true;
            }
        </SCRIPT> 
    </head>
    <body bgcolor="#ECE9D8">
    <p></p>
	<div align="center"><b>Repros desades actualment (<%=vectorRepros.size()%>)</b></div>
	<p></p>
	 <table class="repro" width="90%">
<% 
					for (int i=0;i<vectorRepros.size();i=i+1){
						RegistroRepro regRepro = (RegistroRepro) vectorRepros.get(i);
%>
		<tr><td height="20px"><font style="font-size: 12px;"><%=java.net.URLDecoder.decode(regRepro.getNomRepro(),"UTF-8")%></font></td>
		</tr>
<%}//  for (...		   
 %>
	            <tr><td>
	            <table border="0" width="90%">
	             <tr>
	               <td align="center">
	               <%if (vectorRepros.size()< maxRepros.intValue()){ %>
	                 <form name="grabarForm" method="post" onsubmit="return confirmaProceso();" action="grabaRepro.jsp">
		                 Nom: <input type="text" name="nombre">
		                 <input type="submit" value="Desar">
		                 <input type="hidden" name="tipusCookie" value="<%=tipusCookies%>">
		                 <input type="hidden" name="valorCookieAgrabar" value="<%=valorRepro%>"> 
	                 </form>
	                 <%}else{ %>
	                 <div align="justify"><b>S'ha superat el màxim número de repros. No es pot guardar la repro.</b></div>
	                 <%} %>
	               </td>
	              </tr>
	            </table>            
	            </td></tr>
		</table>
<%
		}else{ // Fin if(nomRepro == null){
			if((valorRepro == null || valorRepro.equalsIgnoreCase("null"))){
				out.println("<p></p><p></p><p></p>");
				out.println("<div align=\"center\"><b>La repro té massa dades. Hi ha que reduir la longitut a l'extracte del document.</b></div>");
			}else{
//				 Hay que grabar la nueva Repro
				try{

					if (repro.grabar(usuario, nomRepro, valorRepro, tipusCookies)){
							out.println("<p></p><p></p><p></p>");
							out.println("<div align=\"center\"><b>Repro desada correctament.</b></div>");
							}else{
								throw new Exception("Error al desar la Repro");
							}
					
				}catch(Exception ex){
					//System.out.println(ex.getMessage() );
			        ex.printStackTrace();
					out.println("<p></p><p></p><p></p>");
					out.println("<div align=\"center\"><b>Error al desar la Repro.</b></div>");
				} // Fin try
			}
		}
	}catch(Exception e){
		System.out.println(" Error al desar la Repro "+e.getMessage() );
        e.printStackTrace();
	} //Fin try    
%>	<p></p>
    <center><a href="javascript:window.close();"><fmt:message key='tancar'/></a></center>   
    </body>
</html>