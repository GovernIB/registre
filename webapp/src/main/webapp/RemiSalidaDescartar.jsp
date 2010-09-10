<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<!--
  Registro General CAIB - Registro de Salidas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficina");
String numeroSalida=request.getParameter("numeroSalida");
String ano=request.getParameter("ano");
String numOficio=request.getParameter("oficio");
String motivo=request.getParameter("motius");
%>

<%
OficioRemisionFacade ofi = OficioRemisionFacadeUtil.getHome().create();
ParametrosOficioRemision param = new ParametrosOficioRemision();
ParametrosOficioRemision oficio = new ParametrosOficioRemision();

param.setAnoOficio(ano);
param.setOficinaOficio(codOficina);
param.setNumeroOficio(numOficio);
oficio = ofi.leer(param);
oficio.setDescartadoEntrada("S");
oficio.setMotivosDescarteEntrada(motivo);
oficio.setUsuarioEntrada(usuario);
oficio.setAnoEntrada(null);
oficio.setOficinaEntrada(null);
oficio.setNumeroEntrada(null);
oficio.setFechaEntrada(null);
oficio=ofi.actualizar(oficio);

%>
    <%
    RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
    ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
    ParametrosRegistroSalida reg = new ParametrosRegistroSalida();
    
    String listado=(String)session.getAttribute("listadoOficiosSalida");
    
    pregsal.fijaUsuario(usuario);
    pregsal.setoficina(codOficina);
    pregsal.setNumeroSalida(numeroSalida);
    pregsal.setAnoSalida(ano);
    reg=regsal.leer(pregsal);
    %>


<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        
        <link rel="shortcut icon" href="favicon.ico"/>
        
        <script src="jscripts/TAO.js"></script>
    </head>
    <body>
            
          	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>		
		<li><a href="RemiEntradaSel.jsp"><fmt:message key='consulta_oficis_remisio_pendents_arribar'/></a></li>
                <% 
                if (listado!=null) { 
                        String oficina=listado;
				%>
                <li><a href="RemiSalidaLis.jsp?oficina=<%=oficina%>"><fmt:message key='tornar_a_seleccionar_registre'/></a></li>
                <% } %>
		<li><fmt:message key='ofici_remisio_pendent_arribar'/></li>
		</ul>
		<!-- Fi Molla pa-->

        <p>&nbsp;</p>
        <!-- Cuerpo central -->
        <% if(!reg.getLeido()) {%>
        <center>
            <b><fmt:message key='no_sha_trobat_registre'/> <%=numeroSalida%>/<%=ano%></b>
        </center>
        &nbsp;<br><center>[&nbsp;<a href="RemiSalidaSel.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center>

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
                    <table width="100%" border=0>
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='data_sortida'/> :<font class="ficha"><%=fechaSalida%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='registro.hora'/> <font class="ficha"><%=hhmm%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='sortida_anulada'/>:&nbsp;<%=(reg.getRegistroAnulado().trim().equals("")) ? "" : "S" %>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0" >
                                &nbsp;Oficina: <font class="ficha"><%=reg.getOficina()%></font>-<font class="ficha"><%=reg.getDescripcionOficina()%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='num_registre'/> <font class="ficha"><%=reg.getNumeroSalida()%>/<%=reg.getAnoSalida()%></font>
                            </td>
                            <td style="border:0" >
                                <c:set var="texto" scope="page"><%=fechaVisado%></c:set>
                                <fmt:message key='data_registre'/> <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
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
                    <div style="display:inline;width:580px">
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
                    	<div style="display:inline-block;float:left;width:200px">
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
        </table>
        </center>
        
        <%-- formulario --%>
        <p>
            <center>
				<fmt:message key='ofici_descartat_exit'/>
            </center>
        </p>

        <p>
            <center>
                [&nbsp;<a href="RemiSalidaSel.jsp"><fmt:message key='tornar_a_seleccionar_criteris'/></a>&nbsp;]
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <% 
                if (listado!=null) { 
                        String oficina=listado;
                %>
                [&nbsp;<a href="RemiSalidaLis.jsp?oficina=<%=oficina%>"><fmt:message key='tornar_a_seleccionar_registre'/></a>&nbsp;]
                <% } %>
            </center>
        </p>
        <% } %>
   		
                 
   		
    </body>
</html>
