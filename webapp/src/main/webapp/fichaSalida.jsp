<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- Consulta de registros de salida. ficha --%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%
String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficina");
String numeroSalida=request.getParameter("numeroSalida");
String ano=request.getParameter("anoSalida");
String localitzadorsDocs[] ; 
%>
<!-- Exploter no renderitza bé.  -->
<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>    
    </head>
    <body bgcolor="#FFFFFF">
    <%
    RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
    ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
    ParametrosRegistroSalida reg = new ParametrosRegistroSalida();
    
    ParametrosListadoRegistrosSalida parametros;
    parametros = (ParametrosListadoRegistrosSalida)session.getAttribute("listadoSalida");
    
    pregsal.fijaUsuario(usuario);
    pregsal.setoficina(codOficina);
    pregsal.setNumeroSalida(numeroSalida);
    pregsal.setAnoSalida(ano);
    reg=regsal.leer(pregsal);
    localitzadorsDocs = reg.getArrayLocalitzadorsDocs();
    %>
    
          	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>		
		<li><a href="busquedaSalidasIndex.jsp"><fmt:message key='consulta_de_sortides'/></a></li>
                <% 
                if (parametros!=null) {
                   String oficinaDesde=parametros.getOficinaDesde();
                   String oficinaHasta=parametros.getOficinaHasta();
                   String fechaDesde=parametros.getFechaDesde();
                   String fechaHasta=parametros.getFechaHasta();
                   String extractoBusqueda=parametros.getExtracto();
                   String tipoBusqueda=parametros.getTipo();
                   String remitenteBusqueda=parametros.getRemitente();
                   String destinatarioBusqueda=parametros.getDestinatario();
                   String destinoBusqueda=parametros.getDestino();
                   String codiremitentBusqueda=parametros.getCodiRemitent();
                %>
                <li><a href="listadoSalida.jsp?oficinaDesde=<%=oficinaDesde%>&oficinaHasta=<%=oficinaHasta%>&fechaDesde=<%=fechaDesde%>&fechaHasta=<%=fechaHasta%>&extracto=<%=extractoBusqueda%>&tipo=<%=tipoBusqueda%>&remitente=<%=remitenteBusqueda%>&destinatario=<%=destinatarioBusqueda%>&destino=<%=destinoBusqueda%>&codiremitent=<%=codiremitentBusqueda%>"><fmt:message key='tornar_a_seleccionar_registre'/></a></li>
                <%    } else { %>
                <li><a href="busquedaSalidasXRegistro.jsp"><fmt:message key='consulta_sortides_per_numero_de_registre_i_any'/></a></li>
                <% } %>
		<li><fmt:message key='registre_de_sortida'/></li>
		</ul>
		<!-- Fi Molla pa-->
    <body>
        <p>&nbsp;</p>
        <!-- Cuerpo central -->
        <% if(!reg.getLeido()) {%>
        <center>
            <b><fmt:message key='no_sha_trobat_registre'/> <%=numeroSalida%>/<%=ano%></b>
        </center>
        &nbsp;<br><center>[&nbsp;<a href="busquedaSalidasIndex.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center>

        <% } else {
       String fechaSalida=reg.getDataSalida();
       String fechaVisado="";
       
       if (reg.getDataVisado()!=null && !reg.getDataVisado().trim().equals("0")) {
           fechaVisado=reg.getDataVisado();
       }
       
       String hora=reg.getHora();
       String hhmm="";
       if (hora!=null && !hora.equals("") && !hora.equals("0")) {
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
		       
       if (Helper.estaPdteVisado("S", reg.getOficina(), reg.getAnoSalida(), reg.getNumeroSalida())) {
           %>
           <SCRIPT>
                alert("<fmt:message key='avis_modificacio_pendent_visat'/>");
           </SCRIPT>
      <% } %>

        <center>
        <table width="591" class="recuadroSalidas">
            <tr>
                <td class="cellaSortides" width="581">
                    <table width="100%" border="0">
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='data_sortida'/> :<font class="ficha"><%=fechaSalida%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='registro.hora'/> <font class="ficha"><%=Helper.convierteHora_a_HHmmss(reg.getHora())%></font>
                            </td>
                            <td style="border:0" >
                            <%if(reg.getRegistroAnulado().trim().equals("")){ %>
                            	 &nbsp;
                            <%}else{ %>
                                <fmt:message key='sortida_anulada'/>:&nbsp;<%=(reg.getRegistroAnulado().trim().equals("")) ? "" : "S" %>
                            <%} %>               
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='oficina'/>: <font class="ficha"><%=reg.getOficina()%></font>-<font class="ficha"><%=reg.getDescripcionOficina()%></font>
                                &nbsp;/ <font class="ficha"><%=reg.getOficinafisica()%></font>-<font class="ficha"><%=reg.getDescripcionOficinaFisica()%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='num_registre'/> <font class="ficha"><%=reg.getNumeroSalida()%>/<%=reg.getAnoSalida()%></font>
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
            <!-- Fila de datos del documentos -->
            <tr>
                <td class="cellaSortides" width="581">&nbsp;<p><b>&nbsp;&nbsp;<fmt:message key='dades_del_document'/></b></p>
                    <!-- Fecha del documento -->
                    &nbsp;&nbsp;<fmt:message key='registro.fecha'/> <font class="ficha"><%=reg.getData()%></font>
                    &nbsp;&nbsp;&nbsp;
                    <!-- Despegable para Tipos de documentos -->
                    <fmt:message key='registro.tipo'/> <font class="ficha"><%=reg.getDescripcionDocumento()%></font>
                    &nbsp;
                    <!-- Despegable para Idiomas -->
                    <fmt:message key='registro.idioma'/> <font class="ficha"><%=reg.getDescripcionIdiomaDocumento()%></font>
                    <!-- Remitente --><c:set var="texto" scope="page"><%=reg.getDescripcionDestinatario()%></c:set>
                    <p>&nbsp;&nbsp;<fmt:message key='destinatari'/>..... <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font></p>
                    <!-- Procedencia geografica --><c:set var="texto" scope="page"><%=reg.getDestinoGeografico()%></c:set>
                    <p>&nbsp;&nbsp;<fmt:message key='registro.destino_geografico'/>... <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font></p>
                    <!-- Numero de salida -->
                    <% String entrada1=reg.getEntrada1().equals("0") ? "" :reg.getEntrada1();
                    String entrada2=reg.getEntrada2().equals("0") ? "" :reg.getEntrada2();
                    String barra=" / ";
                    if (entrada1.equals("") && entrada2.equals("")) {barra="";}
                    String entrada=entrada1+barra+entrada2;
                    %>
                    <div style="display:inline;width:580px;">
			        	<div style="display:inline;float:left;">
			        		&nbsp;&nbsp;<fmt:message key='registro.num_entrada'/><font class="ficha">&nbsp; </font>
			        	</div>
			        	<div style="display:inline;float:left;">
			        		<font class="ficha"><%=entrada%>&nbsp; </font>
                    	</div>
                    	<!-- Organismo destinatario  -->
                    	<div style="display:inline;float:left;">
                    	<c:set var="texto" scope="page"><%=reg.getDescripcionOrganismoRemitente()%></c:set>
                    	<fmt:message key='registro.organismo_emisor'/>:<font class="ficha">&nbsp; </font>
                    	</div>
                    	<div style="display:inline-block;float:left;width:200px;">
        	        		<font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
        	        	</div>
		            </div>
                    <div style="clear: both;">
		            </div>
                </td>
            </tr>
            <!-- Fila de datos del Extracto -->
            <tr>
                <td class="cellaSortides" width="581">&nbsp;
                	<p><b>&nbsp;&nbsp;<fmt:message key='dades_de_lextracte'/></b></p>
                    <!-- Idioma del Extracto -->
                    &nbsp;&nbsp;<fmt:message key='registro.idioma'/> <font class="ficha"><%=reg.getIdiomaExtracto()%></font>
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
					<p></p>
                </td>
            </tr> 
		<% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){
             if(localitzadorsDocs!=null){ %>
            <tr>
	            <td>
		             <b><fmt:message key='registro.datosDocumentosAnexados'/></b>
		             <p><fmt:message key='registro.emailRemitente'/>:&nbsp;<font class="ficha"><%=(reg.getEmailRemitent()==null) ? "" : reg.getEmailRemitent()%></font></p>
		             <fmt:message key='registro.textoEnlaces'/>:<br/>
		             <ul>
			            <%for(int i=0; i<localitzadorsDocs.length; i++){ %>
			            	<li><a href="<%= localitzadorsDocs[i]%>" target="_blank"><%= localitzadorsDocs[i]%></a></li>
			            <%} %>	            
				     </ul>
				</td>
            </tr>
            <%}
           }%>
        </table>
        </center>
        <p>
            <center>
                [&nbsp;<a href="busquedaSalidasIndex.jsp"><fmt:message key='tornar_a_seleccionar_criteris'/></a>&nbsp;]
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
                   String destinatarioBusqueda=parametros.getDestinatario();
                   String destinoBusqueda=parametros.getDestino();
                   String codiremitentBusqueda=parametros.getCodiRemitent();
                %>
                [&nbsp;<a href="listadoSalida.jsp?oficinaDesde=<%=oficinaDesde%>&oficinaHasta=<%=oficinaHasta%>&fechaDesde=<%=fechaDesde%>&fechaHasta=<%=fechaHasta%>&extracto=<%=extractoBusqueda%>&tipo=<%=tipoBusqueda%>&remitente=<%=remitenteBusqueda%>&destinatario=<%=destinatarioBusqueda%>&destino=<%=destinoBusqueda%>&codiremitent=<%=codiremitentBusqueda%>"><fmt:message key='tornar_a_seleccionar_registre'/></a>&nbsp;]
                <%    } else { %>
                [&nbsp;<a href="busquedaSalidasXRegistro.jsp"><fmt:message key='tornar_a_seleccionar_registre'/></a>&nbsp;]
                <% } %>
            </center>
        </p>
        <% } %>             
    </body>
</html>
