<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<% 
	String tipusCookies=request.getParameter("tipusCookie");
	String usuario=request.getRemoteUser();	
	String nomRepros = new String("(");
	String DatosRepros = new String("(");
	
	try {
		Vector vectorRepros = new Vector();

    ReproUsuarioFacade repro = ReproUsuarioFacadeUtil.getHome().create();
		
		vectorRepros=repro.recuperarRepros(usuario,tipusCookies);
		
		for (int i=0;i<vectorRepros.size();i=i+1){
			RegistroRepro regRepro = (RegistroRepro) vectorRepros.get(i);
			if(i>0){
				nomRepros = nomRepros +",";	
				DatosRepros = DatosRepros +",";
			}
			nomRepros =   nomRepros + '"' + regRepro.getNomRepro() + '"';
			DatosRepros = DatosRepros + '"' + regRepro.getRepro() + '"';
		}
		nomRepros = nomRepros + ")";
		DatosRepros = DatosRepros + ")";
%>
<html lang="es">
<head>
    <title>Llegir Repro</title>
    
    
    <link rel="shortcut icon" href="favicon.ico"/>
    <script language="javascript" src="jscripts/TAO.js"></script>
    <script language="javascript">
        var nomRepro = new Array<%=nomRepros%>;
        var dadesRepro =  new Array<%=DatosRepros%>;
        
        function confirmaProceso(valor) {
            valor1 = dadesRepro[valor]; 
            top.opener.leerCookie(valor1);
            close();
        }
    </script>
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
					<tr>
						<td height="20px">
							<a style="font-size: 12px;" href="javascript: confirmaProceso(<%=i%>)"><%=java.net.URLDecoder.decode(regRepro.getNomRepro(),"UTF-8") %></a>
						</td>
					</tr>
<%}//  for (...		
				}catch(Exception e){
					System.out.println(" Error al leer Repro "+e.getMessage() );
                    e.printStackTrace();
				}
%>
	</table>
	<p></p>
	<center><a href="javascript:window.close();"><fmt:message key='tancar'/></a></center>
</body>
</html>