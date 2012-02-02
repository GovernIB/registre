<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%-- Consulta de registros de entrada. ficha --%>
<%@page import="java.util.*,java.text.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>

<%
	String usuario=request.getRemoteUser();
	String codOficina=request.getParameter("oficina");
	String numeroEntrada=request.getParameter("numeroEntrada");
	String ano=request.getParameter("anoEntrada");
	String localitzadorsDocs[] ; 
	
    RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
    ParametrosRegistroEntrada param = new ParametrosRegistroEntrada();
    ParametrosRegistroEntrada reg = new ParametrosRegistroEntrada();
    ParametrosListadoRegistrosEntrada parametros;
    parametros=(ParametrosListadoRegistrosEntrada)session.getAttribute("listadoEntrada");

    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    Vector modelosRecibos = valores.buscarModelosRecibos("tots","totes");

    param.fijaUsuario(usuario);
    param.setoficina(codOficina);
    param.setNumeroEntrada(numeroEntrada);
    param.setAnoEntrada(ano);
    reg = regent.leer(param);
    localitzadorsDocs = reg.getArrayLocalitzadorsDocs();
%>
<html>
    <head><title><fmt:message key='registre_entrades'/></title>
 	<script>
        function imprimeRecibo() {
        	var url = "imprimeixRebut?oficina="+encodeURIComponent("<%=reg.getOficina().toString()%>");
        	url += "&numeroentrada="+encodeURIComponent("<%=reg.getNumeroEntrada()%>")+"&anoentrada="+encodeURIComponent("<%=reg.getAnoEntrada()%>");
        	url += '&mode=N&modelo='+encodeURIComponent(document.getElementById('model').value);
        	window.open(url, "recibo");  		
        };

      
        function enviaEmail(ofi,num,ano,tipus, msgNoIdioma) {
        	var llista = document.getElementById("UnitatGestio");
        	var email = null;
			
			var enviar = true;
        	var url = "enviaEmail?oficina="+ofi+"&numero="+num+"&ano="+ano+"&tipus="+tipus;

        	if(llista!=null){
           	 email = llista.options[llista.selectedIndex].value;
           	 url+="&email="+email;
        	}

        	if(tipus=="<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_CIUTADA %>"){
	        	if(document.getElementById("idiomaEmail")!=null){
	        		var idiomaEmail = document.getElementById("idiomaEmail").value;
	            	if(idiomaEmail=="<%=es.caib.regweb.webapp.servlet.EmailServlet.IDIOMA_NO_DETERMINADO %>"){
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
     <script src="jscripts/TAO.js"></script>
    </head>
    <body bgcolor="#FFFFFF">
   
      	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>		
		<li><a href="busquedaEntradasIndex.jsp"><fmt:message key='consulta_entrades'/></a></li>
        <% 
              if (parametros!=null) { 
                String oficinaDesde=parametros.getOficinaDesde();
                String oficinaHasta=parametros.getOficinaHasta();
                String fechaDesde=parametros.getFechaDesde();
                String fechaHasta=parametros.getFechaHasta();
                String extractoBusqueda=parametros.getExtracto();
                String tipoBusqueda=parametros.getTipo();
                String remitenteBusqueda=parametros.getRemitente();
                String procedenciaBusqueda=parametros.getProcedencia();
                String destinatarioBusqueda=parametros.getDestinatario();
				String codidestinatariBusqueda=parametros.getCodiDestinatari();
		%>
                <!-- <li><a href="busquedaEntradasXFechas.jsp"><fmt:message key='consulta_per_oficines_i_dates'/></a></li> -->
                <li><a href="listado.jsp?oficinaDesde=<%=oficinaDesde%>&oficinaHasta=<%=oficinaHasta%>&fechaDesde=<%=fechaDesde%>&fechaHasta=<%=fechaHasta%>&extracto=<%=extractoBusqueda%>&tipo=<%=tipoBusqueda%>&remitente=<%=remitenteBusqueda%>&procedencia=<%=procedenciaBusqueda%>&destinatario=<%=destinatarioBusqueda%>&codidestinatari=<%=codidestinatariBusqueda%>"><fmt:message key='tornar_a_seleccionar_registre'/></a></li>
                <%    } else { %>
                <li><a href="busquedaEntradasXRegistro.jsp"><fmt:message key='consulta_entrades_per_numero_de_registre_i_any'/></a></li>
                <% } %>
		<li><fmt:message key='registre_entrada'/></li>
		</ul>
		<!-- Fi Molla pa-->
        <p></p>
        <!-- Cuerpo central -->
        <% if(!reg.getLeido()) {%>
        <center>
            <b><fmt:message key='no_sha_trobat_registre'/> <%=numeroEntrada%>/<%=ano%></B>
        </center>
        &nbsp;<br><center>[&nbsp;<a href="busquedaEntradasIndex.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center>

        <% } else {
       
       String fechaEntrada=reg.getDataEntrada();
       String fechaVisado="";
       if (reg.getDataVisado()!=null && !reg.getDataVisado().trim().equals("0")) {
           fechaVisado=reg.getDataVisado();
       }

       String hora=reg.getHora();
       String hhmm="";
       if (hora!=null && !hora.equals("") && !hora.equals("0") ) {
           if (hora.length()<4) {hora="0"+hora;}
           String hh=hora.substring(0,2);
           String mm=hora.substring(2,4);
           hhmm=hh+":"+mm;
       } else {
          hhmm=hora;
       }
       if (Helper.estaPdteVisado("E", reg.getOficina(), reg.getAnoEntrada(), reg.getNumeroEntrada())) {
           %>
           <SCRIPT>
                alert("<fmt:message key='avis_modificacio_pendent_visat'/>");
           </SCRIPT>
      <% } %>
        
        <center>
        <table width="700" class="recuadroEntradas">
            <tr>
                <td class="cellaEntrades" width="581">
                    <table width="100%" border="0">
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='registro.fecha_entrada'/> <font class="ficha"><%=fechaEntrada%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='registro.hora'/> <font class="ficha"><%=hhmm%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='entrada_anulada'/>:&nbsp;<%=(reg.getRegistroAnulado().trim().equals("")) ? "" : "S" %>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0" >
                                &nbsp;Oficina: <font class="ficha"><%=reg.getOficina()%></font>-<font class="ficha"><%=reg.getDescripcionOficina()%></font>
                                &nbsp;/ <font class="ficha"><%=reg.getOficinafisica()%></font>-<font class="ficha"><%=reg.getDescripcionOficinaFisica()%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='num_registre'/> <font class="ficha"><%=reg.getNumeroEntrada()%>/<%=reg.getAnoEntrada()%></font>
                            </td>
                            <td style="border:0" >                                
                                <% if(fechaVisado!=null && !fechaVisado.equals("")){ %>
	                                <c:set var="texto" scope="page"><%=fechaVisado%></c:set>
	                                <fmt:message key='data_visado'/> <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
                                <%} %>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- Fila de datos del documento -->
            <tr>
                <td class="cellaEntrades" width="581">
                	<p><b>&nbsp;&nbsp;<fmt:message key='dades_del_document'/></b></p>
                    <!-- Fecha del documento --> <c:set var="texto" scope="page"><%=reg.getData()%></c:set>
                    &nbsp;&nbsp;<fmt:message key='registro.fecha'/> <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
                    &nbsp;&nbsp;&nbsp;
                    <!-- Despegable para Tipos de documentos --><c:set var="texto" scope="page"><%=reg.getDescripcionDocumento()%></c:set>
                    <fmt:message key='registro.tipo'/> <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
                    &nbsp;
                    <!-- Despegable para Idiomas -->
                    <c:set var="texto" scope="page"><%=reg.getDescripcionIdiomaDocumento()%></c:set>
                    <fmt:message key='registro.idioma'/> <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
                    <!-- Remitente -->
                    <c:set var="texto" scope="page"><%=reg.getDescripcionRemitente()%></c:set>
                    <p>&nbsp;&nbsp;<fmt:message key='remitent'/>..... <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font></p>
                    <!-- Procedencia geografica -->
                    <c:set var="texto" scope="page"><%=reg.getProcedenciaGeografica()%></c:set>
                    <p>&nbsp;&nbsp;<fmt:message key='procedencia_geografica'/>... <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font></p> 
                    <!-- Numero de salida -->
                    <% String salida1=reg.getSalida1().equals("0") ? "" :reg.getSalida1();
                    String salida2=reg.getSalida2().equals("0") ? "" :reg.getSalida2();
                    String barra=" / ";
                    if (salida1.equals("") && salida2.equals("")) {barra="";}
                    String salida=salida1+barra+salida2;
                    %>
                    
                    <div style="display:inline;width:580px">
			        	<div style="display:inline;float:left;">
			        		&nbsp;&nbsp;<fmt:message key='registro.num_sortida'/><font class="ficha">&nbsp; </font>
			        	</div>
			        	<div style="display:inline;float:left;">
			        		<font class="ficha"><%=salida%>&nbsp; </font>
                    	</div>
                    	<!-- Organismo destinatario  -->
                    	<div style="display:inline;float:left;">
                    	<c:set var="texto" scope="page"><%=reg.getDescripcionOrganismoDestinatario()%></c:set>
                    	<fmt:message key='registro.organismo_destinatario'/>:<font class="ficha">&nbsp; </font>
                    	</div>
        	        	<div style="display:inline-block;float:left;width:200px">
        	        		<font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
        	        	</div>
		            </div>
					<%
 						if(!reg.getDescripcionMunicipi060().equals("")){
					%>
					<div style="display:inline;width:580px">
			        	<div style="display:inline;float:left;">
			        		&nbsp;&nbsp;<fmt:message key='entitat_local_012'/>:<font class="ficha">&nbsp; </font>
			        	</div>
			        	<div style="display:inline;float:left;">
			        		<font class="ficha"><%=reg.getDescripcionMunicipi060()%>&nbsp; </font>
                    	</div>
					</div>
		            <div style="clear: both;">
		            </div>
<% } %>
		       </td>
            </tr>
            <!-- Fila de datos del Extracto -->
            <tr>
                <td class="cellaEntrades" width="581">
                    <b>&nbsp;&nbsp;<fmt:message key='dades_de_lextracte'/></b>
                    <!-- Idioma del Extracto -->
                    <p>&nbsp;&nbsp;<fmt:message key='registro.idioma'/> <font class="ficha"><%=reg.getIdiomaExtracto()%></font>
                    &nbsp;

                    <c:choose>
                    <c:when test="${initParam['registro.entrada.view.disquete_correo']}">
                    <!--Numero de disquete -->
                    <fmt:message key='registro.num_disquete'/> <font class="ficha"><%=reg.getDisquet()%></font>
                    &nbsp;
                    <!--Numero de disquete -->
                    <fmt:message key='registro.num_correo'/> <font class="ficha"><%=(reg.getCorreo()==null) ? "" : reg.getCorreo()%></font>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                    </c:choose>
                    </p>
                    
                    <!-- Extracto del documento -->
                    <c:set var="texto" scope="page"><%=reg.getComentario()%></c:set>
                    <p>&nbsp;&nbsp;<fmt:message key='extracte_del_document'/>: <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font></p>
                </td>
            </tr> 
		<% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){
			 if(localitzadorsDocs!=null){ %>
            <tr>
	            <td>
	            	 <br/>
		             <b>&nbsp;&nbsp;<fmt:message key='registro.datosDocumentosAnexados'/></b>
		             <p>&nbsp;&nbsp;<fmt:message key='registro.emailRemitente'/>:&nbsp;<font class="ficha"><%=(reg.getEmailRemitent()==null) ? "" : reg.getEmailRemitent()%></font></p>
		             &nbsp;&nbsp;<fmt:message key='registro.textoEnlaces'/>:
		             <ul>
			            <%for(int i=0; i<localitzadorsDocs.length; i++){ %>
			            	<li><a href="<%= localitzadorsDocs[i]%>" target="_blank"><%= localitzadorsDocs[i]%></a></li>
			            <%} %>	            
				     </ul>
				</td>
            </tr>
            <tr>
            	<td>
		            <table class="recuadroEntradasRegistro" id="tablaEmails" width="100%" align="center">
				        <tr><td colspan="3">&nbsp;&nbsp;<font class="textoResaltadoRegistro"><fmt:message key='registro.titol1'/></font></td></tr>
				        <tr>
				        	<%if(reg.getEmailRemitent()!=null){ 
			                    //Pasamos los datos del registro a la pantalla de envio de correo                   	
			                %>
				              <td>&nbsp;&nbsp;<fmt:message key='registro.emailRemitente'/></td>
				              <td><input type="text" name="emailRemitente" size="30" maxlength="50" value="<%=reg.getEmailRemitent()%>" readonly="readonly"></td>
				              <td rowspan="2" ><a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="enviaEmail('<%=reg.getOficina()%>','<%=reg.getNumeroEntrada() %>','<%=reg.getAnoEntrada() %>','<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_CIUTADA %>','<fmt:message key='registro.msgNoIdioma'/>');">&nbsp;<fmt:message key='enviar_email'/>&nbsp;</a></td>                 
			                    <%}else{ %>
			                  <td colspan="3">&nbsp;&nbsp;<fmt:message key='registro.noEmailRemitente'/></td>
			                    <%} %>
				        </tr>
				        <%if(reg.getEmailRemitent()!=null){ %>
				        <tr>
				        	<td >&nbsp;&nbsp;<fmt:message key='registro.idioma'/></td>
				        	<td ><select name="idiomaEmail" id="idiomaEmail">
			                            <option selected="selected" value="<%=es.caib.regweb.webapp.servlet.EmailServlet.IDIOMA_NO_DETERMINADO %>" ><fmt:message key='registro.idioma.noDeterminado'/></option>
			                            <option value="<%=es.caib.regweb.webapp.servlet.EmailServlet.IDIOMA_CATALAN %>" > <fmt:message key='registro.idioma.catala'/></option>
			                            <option value="<%=es.caib.regweb.webapp.servlet.EmailServlet.IDIOMA_CASTELLANO %>" > <fmt:message key='registro.idioma.castella'/></option>
			                       </select>
			                </td>
				        </tr>
				        <%} %>	 
				        <tr><td colspan="3">&nbsp;</td></tr>				        
				     <% 	if(valores.permiteEnviarEmailAlOrganismo(Integer.parseInt(reg.getOficina()),Integer.parseInt(reg.getDestinatari()))){ 
			                      	//Si la oficina de registro puede enviar correos al organismo destinatario	
			                    		Vector unitatsGestio = valores.buscarUnitatsGestioEmail(reg.getOficina(),reg.getDestinatari());
			                    %>	
			            <tr>	                    
		                    <td>&nbsp;&nbsp;<fmt:message key='registro.emailUnidadGestion'/></td>
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
		                    <td><a style="text-decoration: none;" type="button" class="botonFormulario" href="#" onclick="enviaEmail('<%=reg.getOficina()%>','<%=reg.getNumeroEntrada() %>','<%=reg.getAnoEntrada() %>','<%=es.caib.regweb.webapp.servlet.EmailServlet.TIPUS_INTERN %>','');">&nbsp;<fmt:message key='enviar_email'/>&nbsp;</a></td>  
			            </tr>                                 	
			         <% 	}else{
			                    	//Es un registro con documentos electrónicos pero sin permisos para enviar el email
			         %>
						  <tr><td colspan="3">&nbsp;&nbsp;<fmt:message key='registro.NoEmailUnitatGestio'/></td></tr>								             
			          <%} // Fin if(valores.permiteEnviarEmailAlOrganismo(%>
				        <tr>
				        	<td colspan="3">
					        	<div style='text-align:center;'>
						        	<p>[&nbsp;<a href="#" onclick="verHistoricoEmails('<%=reg.getOficina()%>','<%=reg.getNumeroEntrada() %>','<%=reg.getAnoEntrada() %>','E');"><fmt:message key='verHistorico'/></a>&nbsp;]</p>
					        	</div>
				        	</td>	
				        </tr>
		        </table>
	        </td>
        </tr>
            <%}
           } // Fin if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false")
           %>
        <tr>
           	<td class="cellaEntrades" width="581">
          			<form name="imprimirRecibo" action="" method="post">
    					<input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(codOficina)%>">
       					<input type="hidden" name="numeroEntrada" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(numeroEntrada)%>">
        				<input type="hidden" name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>">
                <% if (modelosRecibos.size()>0) { %>
                        <!-- Boton de enviar --> 
                        <br/>         
                        <p align="center">
                        	<select name="model" id="model">
                    		<%     for (int i=0;i<modelosRecibos.size();i++) {
                        	    String codiModel=modelosRecibos.get(i).toString();%>
                        	    <option value="<%=codiModel %>"><%=codiModel %></option>
                       		<% } %>
                    		</select>
                            <input type="button" name="crearRebut" onclick="imprimeRecibo()" value="<fmt:message key='imprimir_rebut'/>">
                        </p>
              		<% }  else { %>
                    <p align="center">
                		  <fmt:message key="no_hi_ha_models_rebut"/>
              		  </p>
                  <% } %>
                    </form>
                 </td>
			</tr>     
			
		<%
		// Si esta actida la opción de mostrar los datos del BOIB
		if (Conf.get("infoBOIB","false").equalsIgnoreCase("true")) {
			// Si la oficina es la del BOIB lanzamos datos para fichero de publicaciones
             if (reg.getOficina().equals(Conf.get("oficinaBOIB","32"))) { 
            	RegistroPublicadoEntradaFacade registroPublicado = RegistroPublicadoEntradaFacadeUtil.getHome().create();
            	ParametrosRegistroPublicadoEntrada registroPublicadoParametros = new ParametrosRegistroPublicadoEntrada();            	
            	registroPublicadoParametros.setOficina(Integer.parseInt(reg.getOficina()));
            	registroPublicadoParametros.setNumero(Integer.parseInt(reg.getNumeroEntrada()));
            	registroPublicadoParametros.setAnoEntrada(Integer.parseInt(reg.getAnoEntrada()));

                String dataPublicacion="";
                String numeroBOCAIB="";
                String pagina="";
                String lineas="";
                String textoPublic="";
                String observaciones="";

                registroPublicadoParametros = registroPublicado.leer(registroPublicadoParametros); 
                if (registroPublicadoParametros.getLeido()) {
                    dataPublicacion=(registroPublicadoParametros.getFecha()==0) ? "" : registroPublicadoParametros.getFechaTexto();           
                    numeroBOCAIB=(registroPublicadoParametros.getNumeroBOCAIB()==0) ? "" :registroPublicadoParametros.getNumeroBOCAIB()+"";
                    pagina=(registroPublicadoParametros.getPagina()==0) ? "" : registroPublicadoParametros.getPagina()+"";
                    lineas=(registroPublicadoParametros.getLineas()==0) ? "" : registroPublicadoParametros.getLineas()+"";
                    textoPublic=registroPublicadoParametros.getContenido().trim();
                    observaciones=registroPublicadoParametros.getObservaciones().trim();
                }
            %> 
            <tr>
                <td>
                    <br>
                    &nbsp;&nbsp;<b><fmt:message key='publicacio.text_dades_publicacio'/></b>
                    &nbsp;<br>&nbsp;<br>
                    &nbsp;<fmt:message key='publicacio.text_data_public'/>
                    <font class="ficha"><%=dataPublicacion%></font>
                    &nbsp;&nbsp;&nbsp;<fmt:message key='publicacio.text_numero_boib'/>           
                    <font class="ficha"><%=numeroBOCAIB%></font>&nbsp;
                    <fmt:message key='publicacio.text_pagines_boib'/>
                    <font class="ficha"><%=pagina%></font>&nbsp;
                    <fmt:message key='publicacio.text_linies'/>
                    <font class="ficha"><%=lineas%></font>&nbsp;&nbsp;<br>&nbsp;<br>&nbsp;
                    <fmt:message key='publicacio.text_texte'/>&nbsp;
                    <c:set var="texto" scope="page"><%=textoPublic%></c:set>
                    <font class="ficha"><c:out value="${texto}"/></font>
                    &nbsp;<br>&nbsp;<br>&nbsp;
                    <fmt:message key='publicacio.text_observacions'/>&nbsp;<c:set var="texto" scope="page"><%=observaciones%></c:set>
                    <font class="ficha"><c:out value="${texto}"/></font>
                </td>
            </tr>
            <%
            registroPublicado.remove();
            } 
		} // Fin if (Conf.get("infoBOIB","false").equalsIgnoreCase("true")) {
            %>
            
        </table>
        </center>
        <br/>
            <center>
                [&nbsp;<a href="busquedaEntradasIndex.jsp"><fmt:message key='tornar_a_seleccionar_criteris'/></a>&nbsp;]
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <% 
                if (parametros!=null) { 
                        String oficinaDesde=parametros.getOficinaDesde();
                        String oficinaHasta=parametros.getOficinaHasta();
                        String fechaDesde=parametros.getFechaDesde();
                        String fechaHasta=parametros.getFechaHasta();
                        String extractoBusqueda=parametros.getExtracto();
                        String tipoBusqueda=parametros.getTipo();
                        String remitenteBusqueda=parametros.getRemitente();
                        String procedenciaBusqueda=parametros.getProcedencia();
                        String destinatarioBusqueda=parametros.getDestinatario();
                        String codidestinatariBusqueda=parametros.getCodiDestinatari();
                %>
                [&nbsp;<a href="listado.jsp?oficinaDesde=<%=oficinaDesde%>&oficinaHasta=<%=oficinaHasta%>&fechaDesde=<%=fechaDesde%>&fechaHasta=<%=fechaHasta%>&extracto=<%=extractoBusqueda%>&tipo=<%=tipoBusqueda%>&remitente=<%=remitenteBusqueda%>&procedencia=<%=procedenciaBusqueda%>&destinatario=<%=destinatarioBusqueda%>&codidestinatari=<%=codidestinatariBusqueda%>"><fmt:message key='tornar_a_seleccionar_registre'/></a>&nbsp;]
                <%    } else { %>
                [&nbsp;<a href="busquedaEntradasXRegistro.jsp"><fmt:message key='tornar_a_seleccionar_registre'/></a>&nbsp;]
                <% } %>
            </center>
        <% } %>	
    </body>
</html>
