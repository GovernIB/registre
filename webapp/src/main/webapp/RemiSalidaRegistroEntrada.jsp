<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!--
  Registro General CAIB 
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@page import="es.caib.regweb.webapp.servlet.EmailServlet"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficinaSalida");
String numeroSalida=request.getParameter("numeroSalida");
String numeroOficio=request.getParameter("oficio");
String ano=request.getParameter("ano");
boolean errorCrearOfici =  false;
%>
<%
RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada pregent = new ParametrosRegistroEntrada();
ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();

ValoresFacade valores = ValoresFacadeUtil.getHome().create();
OficioRemisionFacade ofi = OficioRemisionFacadeUtil.getHome().create();
ParametrosOficioRemision param = new ParametrosOficioRemision();
ParametrosOficioRemision oficio = new ParametrosOficioRemision();

Integer intSerie=(Integer)session.getAttribute("serie");
if (intSerie==null) {
    intSerie=new Integer(0);
    session.setAttribute("serie", intSerie);
}
int serie=intSerie.intValue();
int serieForm = Integer.parseInt(request.getParameter("serie"));

serie++;   
intSerie=Integer.valueOf(String.valueOf(serie));
session.setAttribute("serie", intSerie);
session.removeAttribute("errorAtras");

registro.fijaUsuario(usuario);
registro.setCorreo(request.getParameter("correo"));
registro.setdataentrada(request.getParameter("dataentrada"));
registro.sethora(request.getParameter("hora"));
registro.setoficina(request.getParameter("oficina"));
registro.setoficinafisica(request.getParameter("oficinafisica"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setentidad1(request.getParameter("entidad1"));
registro.setentidad2(request.getParameter("entidad2"));
registro.setaltres(request.getParameter("altres"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setsalida1(request.getParameter("salida1"));
registro.setsalida2(request.getParameter("salida2"));
registro.setdestinatari(request.getParameter("destinatari"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
registro.setLocalitzadorsDocs(request.getParameter("localitzadorsDocs"));
registro.setEmailRemitent(request.getParameter("emailRemitente"));
if(request.getParameter("mun_060")!=null)
	registro.setMunicipi060(request.getParameter("mun_060"));

registro.setcomentario(request.getParameter("comentario"));
%>

<% 

param.setAnoOficio(ano);
param.setOficinaOficio(codOficina);
param.setNumeroOficio(numeroOficio);
oficio = ofi.leer(param);

     registro=regent.validar(registro);
     boolean ok=registro.getValidado();

if (!ok || oficio==null){
	if (oficio==null) {
		registro.getErrores().put("","Error inesperat, no s'ha pogut obtenir les dades de l'ofici");
	}
    request.setAttribute("registroEntrada",registro);
    request.setAttribute( "oficina", codOficina);
    request.setAttribute( "numeroSalida", numeroSalida);
    request.setAttribute( "oficio", numeroOficio);
    request.setAttribute( "ano", ano);
%>
        <jsp:forward page="RemiSalidaPaso.jsp" />
<% } else { 
    
    registro=regent.grabar(registro);
    boolean grabado=registro.getGrabado();

    if (!grabado) {
        request.setAttribute("registroEntrada",registro);
        request.setAttribute( "oficina", codOficina);
    	request.setAttribute( "numeroSalida", numeroSalida);
        request.setAttribute( "oficio", numeroOficio);
   		request.setAttribute( "ano", ano);
%>
                <jsp:forward page="RemiSalidaPaso.jsp" />
<%            
	} else {
			oficio.setDescartadoEntrada("N");
			oficio.setMotivosDescarteEntrada("");
			oficio.setUsuarioEntrada(usuario);
			oficio.setAnoEntrada(registro.getAnoEntrada());
			oficio.setOficinaEntrada(registro.getOficina());
			oficio.setNumeroEntrada(registro.getNumeroEntrada());
			oficio.setFechaEntrada(registro.getDataEntrada());

		try{
			oficio = ofi.actualizar(oficio);
		}catch(Exception e){
			errorCrearOfici = true;
		}
	
		String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
        String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
        String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");
        
        if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
            valores.liberarDisquete(bloqueoOficina,bloqueoTipo,bloqueoAno,usuario);
            session.removeAttribute("bloqueoOficina");
            session.removeAttribute("bloqueoTipo");
            session.removeAttribute("bloqueoAno");
            session.removeAttribute("bloqueoUsuario");
            session.removeAttribute("bloqueoDisquete");
        }
%> 

<html>
    <head><title><fmt:message key='registre_entrades'/></title>  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Cache-Control" content="no-cache">
        <script src="jscripts/TAO.js"></script>
        <script>
        function enviaEmail(ofi,num,ano,tipus, msgNoIdioma) {
        	var llista = document.getElementById("UnitatGestio");
        	var email = null;
			
			var enviar = true;
        	var url = "enviaEmail?oficina="+ofi+"&numero="+num+"&ano="+ano+"&tipus="+tipus;

        	if(llista!=null){
           	 email = llista.options[llista.selectedIndex].value;
           	 url+="&email="+email;
        	}
        	if(tipus=="<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_CIUTADA%>"){
	        	if(document.getElementById("idiomaEmail")!=null){
	        		var idiomaEmail = document.getElementById("idiomaEmail").value;
	            	if(idiomaEmail=="<%=EmailServlet.IDIOMA_NO_DETERMINADO %>"){
	            		alert (msgNoIdioma);
	            		enviar= false;
	            		return;
	                	}
	            	url+="&idioma="+idiomaEmail;
	        	}
        	}
			
        	if(enviar){
	        	miVentana = window.open(url, "enviaEmail","scrollbars,resizable,width=300,height=200");
	        	miVentana.focus(); 	
        	}	
        }
        </script>
    </head>
    <body>        
     	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
			<li><a href="pedirdatos.jsp"><fmt:message key='registre_entrades'/></a></li>
			<li><fmt:message key='registre_entrada_creat'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
        <table class="recuadroEntradas" width="600" align="center">
            <tr>
                <td style="border:0" >
                <%if (errorCrearOfici==false){ %>
                    &nbsp;<br><center><b><fmt:message key='ofici'/> <%=oficio.getNumeroOficio()%>/<%=oficio.getAnoOficio()%></b></center><br/>
                <%}else{ %>
                    &nbsp;<br><center><b><fmt:message key='error_ofici.generacio'/></b></center><br/>
                <%} %>
                </td>
            </tr>   
            <tr>   
                <td style="border:0" >
                    &nbsp;<br/><center><b><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%> <fmt:message key='desat_correctament'/></b></center><br/>
                </td>
            </tr>   
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >                  
                    <center>
                    	<a style="text-decoration: none;" type="button" target="_blank" class="botonFormulario" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&hora=<%=registro.getHora()%>">
                        &nbsp;<fmt:message key='imprimir_segell'/>&nbsp;</a>
                    	<a style="text-decoration: none;" type="button" class="botonFormulario" href="RemiSalidaLis.jsp">
                        &nbsp;<fmt:message key='tornar'/>&nbsp;</a>
                    </center>
                    <br/>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
        </table>      
		<p>&nbsp;</p>  
		<!-- tabla envio emails-->
   <% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){                                                        
        if(registro.tieneDocsElectronicos()){
        	 //Si el registro contiene documentos electronicos.%>
        <table class="recuadroEntradasRegistro" id="tablaEmails" width="600" align="center">
	        <tr><td colspan="3"> <font class="textoResaltadoRegistro"><fmt:message key='registro.titol1'/></font></td></tr>
	        <tr>
	        	<%if(registro.getEmailRemitent()!=null){ 
                    //Pasamos los datos del registro a la pantalla de envio de correo                   	
                %>
	              <td><fmt:message key='registro.emailRemitente'/></td>
	              <td><input type="text" name="emailRemitente" size="30" maxlength="50" value="<%=registro.getEmailRemitent()%>" readonly="readonly"></td>
	              <td rowspan="2" ><a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="enviaEmail('<%=registro.getOficina()%>','<%=registro.getNumeroEntrada() %>','<%=registro.getAnoEntrada() %>','<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_CIUTADA %>','<fmt:message key='registro.msgNoIdioma'/>');">&nbsp;<fmt:message key='enviar_email'/>&nbsp;</a></td>                 
                    <%}else{ %>
                  <td colspan="3"><fmt:message key='registro.noEmailRemitente'/></td>
                    <%} %>
	        </tr>
	        <%if(registro.getEmailRemitent()!=null){ %>
	        <tr>
	        	<td ><fmt:message key='registro.idioma'/></td>
	        	<td ><select name="idiomaEmail" id="idiomaEmail">
                            <option selected="selected" value="<%=EmailServlet.IDIOMA_NO_DETERMINADO %>" ><fmt:message key='registro.idioma.noDeterminado'/></option>
                            <option value="<%=EmailServlet.IDIOMA_CATALAN %>" > <fmt:message key='registro.idioma.catala'/></option>
                            <option value="<%=EmailServlet.IDIOMA_CASTELLANO %>" > <fmt:message key='registro.idioma.castella'/></option>
                       </select>
                </td>
	        </tr>
	        <%} %>	        
	        <tr><td colspan="3">&nbsp;</td></tr>
	        <tr>
	            <% 	if(valores.permiteEnviarEmailAlOrganismo(Integer.parseInt(registro.getOficina()),Integer.parseInt(registro.getDestinatari()))){ 
                      	//Si la oficina de registro puede enviar correos al organismo destinatario	
                    		Vector unitatsGestio = valores.buscarUnitatsGestioEmail(registro.getOficina(),registro.getDestinatari());
                    %>
                            
                    <td><fmt:message key='registro.emailUnidadGestion'/></td>
                    <td>
                    <form name="emailUG" id="emailUG""> 
                    <select name="UnitatGestio" id="UnitatGestio">
                    	<%     
                    	for (int i=0;i<unitatsGestio.size();i=i+4) { 
                            String codiOficina=unitatsGestio.get(i).toString();
                            String codiUnitat=unitatsGestio.get(i+1).toString();
                            String nomUnitat=unitatsGestio.get(i+2).toString();
                            String email=unitatsGestio.get(i+3).toString();  
                        %>
                            <option value="<%=email%>"><%=nomUnitat+" - "+email %></option>
                        <% }// fin for %>
                    	</select>
                    	 </form>
                    </td>
                    <td><a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="enviaEmail('<%=registro.getOficina()%>','<%=registro.getNumeroEntrada() %>','<%=registro.getAnoEntrada() %>','<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_INTERN %>','');">&nbsp;<fmt:message key='enviar_email'/>&nbsp;</a>  
                    </td>                                               
                    <% 	}else{
                    	//Es un registro con documentos electrónicos pero sin permisos para enviar el email
                    	%>
					 <td colspan="3"><fmt:message key='registro.NoEmailUnitatGestio'/></td>            
                    <%} 
                     %>
	        </tr>
	        <tr><td colspan="3">&nbsp;</td></tr>
        </table>
        <%} //if(registro.tieneDocsElectronicos()) 
        }%>          
<%
		}
	}%> 
    </body>
</html>