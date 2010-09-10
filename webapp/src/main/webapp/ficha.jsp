<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%-- Consulta de registros de entrada. ficha --%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>

<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficina");
String numeroEntrada=request.getParameter("numeroEntrada");
String ano=request.getParameter("anoEntrada");
%>
<%
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
%>
<!-- Exploter no renderitza bé.  -->
<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        
        
        
    
        <script>
        function imprimeRecibo() {
        	var url = "imprimeixRebut?oficina="+encodeURIComponent("<%=reg.getOficina().toString()%>");
        	url += "&numeroentrada="+encodeURIComponent("<%=reg.getNumeroEntrada()%>")+"&anoentrada="+encodeURIComponent("<%=reg.getAnoEntrada()%>");
        	url += '&mode=N&modelo='+encodeURIComponent(document.getElementById('model').value);
        	window.open(url, "recibo");  		
        }
        </script>
    
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
  


<!--         <CENTER><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center> -->
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
       /*
       Comentat per a solventar el problema que no es visualitzaven registres
       antics debut a que no tenien hora de entrada/sortida 
       String hora=reg.getHora();
       if (hora.length()<4) {hora="0"+hora;}
       String hh=hora.substring(0,2);
       String mm=hora.substring(2,4);
       String hhmm=hh+":"+mm;
       */
       if (Helper.estaPdteVisado("E", reg.getOficina(), reg.getAnoEntrada(), reg.getNumeroEntrada())) {
           %>
           <SCRIPT>
                alert("<fmt:message key='avis_modificacio_pendent_visat'/>");
           </SCRIPT>
      <% } %>
        
        <center>
        <table width="700" class="recuadroEntradas">
            <tr>
                <td class="cellaEntrades"width="581">
                    <table width="100%" border=0>
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
                                <c:set var="texto" scope="page"><%=fechaVisado%></c:set>
                                <fmt:message key='data_registre'/> <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- Fila de datos del documento -->
            <tr>
                <td class="cellaEntrades" width="581">&nbsp;<p><b>&nbsp;&nbsp;<fmt:message key='dades_del_document'/></b></p>
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
                <td class="cellaEntrades" width="581">&nbsp;<p>
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
                    <p>&nbsp;&nbsp;<fmt:message key='extracte_del_document'/>: <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font><p>
                </td>
            </tr> 
           <tr>
           		<td class="cellaEntrades" width="581">
          			<form name="imprimirRecibo" action="" method="post">
    					<input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(codOficina)%>">
       					<input type="hidden" name="numeroEntrada" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(numeroEntrada)%>">
        				<input type="hidden" name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>">

                <% if (modelosRecibos.size()>0) { %>
                        <!-- Boton de enviar -->          
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
        </table>
        </center>
        <p>
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
        </p>
        <% } %>
   		
                 
   		
    </body>
</html>
