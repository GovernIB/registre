<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!--
  Registro General CAIB - Registro de Entradas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@page import="es.caib.regweb.webapp.servlet.EmailServlet"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%

RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();

session.setAttribute("oficinaSesion", request.getParameter("oficina"));
session.setAttribute("oficinaFisicaSesion", request.getParameter("oficinafisica"));

ValoresFacade valores = ValoresFacadeUtil.getHome().create();
String usuario=request.getRemoteUser();

Vector modelosRecibos = valores.buscarModelosRecibos("tots","totes");

Integer intSerie=(Integer)session.getAttribute("serie");
if (intSerie==null) {
    intSerie=new Integer(0);
    session.setAttribute("serie", intSerie);
}
int serie=intSerie.intValue();
int serieForm = Integer.parseInt(request.getParameter("serie"));

if (serie>serieForm) {
    session.setAttribute("errorAtras","1");
%>
    <jsp:forward page="pedirdatos.jsp" />
       <% }
    
    serie++;
    // intSerie++;
    intSerie=Integer.valueOf(String.valueOf(serie));
    session.setAttribute("serie", intSerie);
    session.removeAttribute("errorAtras");
%>

<%
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
registro.setEmailRemitent(request.getParameter("emailRemitente"));
//registro.setOrigenRegistro(request.getParameter("origenRegistro"));
registro.setLocalitzadorsDocs(request.getParameter("localitzadorsDocs"));
if(request.getParameter("mun_060")!=null){
	registro.setMunicipi060(request.getParameter("mun_060"));
	registro.setNumeroDocumentosRegistro060(request.getParameter("numreg_060"));
	}
registro.setcomentario(request.getParameter("comentario"));
%>
<%
     registro=regent.validar(registro);
     boolean ok=registro.getValidado();

if (!ok){
    request.setAttribute("registroEntrada",registro);
%>
        <jsp:forward page="pedirdatos.jsp" />
<% } else {
    
    registro=regent.grabar(registro);

    boolean grabado=registro.getGrabado();
    
    if (!grabado) {
        request.setAttribute("registroEntrada",registro);
%>
  <jsp:forward page="pedirdatos.jsp" />
<%            } else {
        registro = regent.leer(registro);
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
        function imprimeRecibo() {
        	var url = "imprimeixRebut?oficina="+encodeURIComponent("<%=registro.getOficina().toString()%>");
        	url += "&numeroentrada="+encodeURIComponent("<%=registro.getNumeroEntrada()%>")+"&anoentrada="+encodeURIComponent("<%=registro.getAnoEntrada()%>");
        	url += '&mode=N&modelo='+encodeURIComponent(document.getElementById('model').value);
        	window.open(url, "recibo");  		
        }
        </script>      
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
        <table class="recuadroEntradasRegistro" width="600" align="center">
            <tr>
                <td>
                    &nbsp;<br><center><font class="numeroRegistroEntrada"><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%></font>&nbsp;<font class="textoResaltadoRegistro"><fmt:message key='desat_correctament'/></font></center>
                </td>
            </tr>   
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                	<center>
                    <font class="textoResaltadoRegistro">
                    <fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%><br/>
                    <fmt:message key='oficina_fisica'/>:&nbsp;<%=registro.getOficinafisica()%>-<%=valores.recuperaDescripcionOficinaFisica(registro.getOficina().toString(),registro.getOficinafisica().toString())%>
                    </font>
                    </center>
                </td>
            </tr>
            <tr><td>&nbsp;</td></tr>
            <tr>
                <td>
                    <br/><center>                    
                    <% if (modelosRecibos.size()>0) { %>
                    <form name="rebut" id="rebut">
                    	<select name="model" id="model">
                    	<%     for (int i=0;i<modelosRecibos.size();i++) {
                            String codiModel=modelosRecibos.get(i).toString();%>
                            <option value="<%=codiModel %>"><%=codiModel %></option>
                        <% } %>

                    	</select>
                    	<a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="imprimeRecibo();">
                        &nbsp;<fmt:message key='imprimir_rebut'/>&nbsp;</a>                        
                    	</form> 
                		<% }  else { %>
                      <p align="center">
                		  <fmt:message key="no_hi_ha_models_rebut"/>
                	  </p>
                		<% } %>
                        <br/>
                    <p>                           
                    <a style="text-decoration: none;" type="button" class="botonFormulario" href="pedirdatos.jsp">
                        &nbsp;<fmt:message key='registro.nuevo_registro'/>&nbsp;
                    </a>
                    </p>
                    </center>
                </td>
            </tr>
            <!-- <tr><td>&nbsp;</td></tr> -->
        </table>
        <br/>  <br/>
        <!-- tabla envio emails-->
        <% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){
        	if(registro.tieneDocsElectronicos()){
        	 //Si el registro contiene documentos electronicos.
        %>
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
               <form name="emailUG" id="emailUG"">
                    <td><fmt:message key='registro.emailUnidadGestion'/></td>
                    <td><select name="UnitatGestio" id="UnitatGestio">
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
                    </td>
                    <td><a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="enviaEmail('<%=registro.getOficina()%>','<%=registro.getNumeroEntrada() %>','<%=registro.getAnoEntrada() %>','<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_INTERN %>','');">&nbsp;<fmt:message key='enviar_email'/>&nbsp;</a>  
                    </td>               
                </form> 
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
       <br/>
       <br/>
         <c:set var="data" scope="request"><%=registro.getDataEntrada()%></c:set>
         <c:set var="hora" scope="request"><%=registro.getHora()%></c:set>
         <c:set var="oficina" scope="request"><%=valores.recuperaDescripcionOficina(registro.getOficina())%></c:set>
         <c:set var="oficinaid" scope="request"><%=registro.getOficina()%></c:set>
         <c:set var="numero" scope="request"><%=registro.getNumeroEntrada()%></c:set>
         <c:set var="ano" scope="request"><%=registro.getAnoEntrada()%></c:set>
         <c:set var="ES" scope="request">E</c:set>
         <jsp:include page="sellos.jsp" flush="true" />	
        <!-- Fin de la nueva tabla -->
        <%
}
}
        %>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
    </body>
</html>