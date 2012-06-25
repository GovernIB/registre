<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- Consulta de registros de entrada. ficha --%>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficina");
String numeroEntrada=request.getParameter("numeroEntrada");
String ano=request.getParameter("anoEntrada");
%>
<!-- Exploter no renderitza bé.  -->
<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        <script>
            function confirmaDescarta() {
                var cadena=prompt("<fmt:message key='esta_segur_anular_ofici_motius'/>","");
                if (cadena==null || cadena=="") {
                return false;
                }
                var form = document.getElementById("oficioDescartaForm");

            	form.motius.value=cadena;
                
                form.submit();
            }
		</script>
    </head>
    <body bgcolor="#FFFFFF">
    <%
    RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
    ParametrosRegistroEntrada param = new ParametrosRegistroEntrada();
    ParametrosRegistroEntrada reg = new ParametrosRegistroEntrada();
    
    String localitzadorsDocs[] ; 
    String [] listado=(String [])session.getAttribute("listadoOficiosEntrada");
    
    param.fijaUsuario(usuario);
    param.setoficina(codOficina);
    param.setNumeroEntrada(numeroEntrada);
    param.setAnoEntrada(ano);
    reg = regent.leer(param);
    try{
    	localitzadorsDocs = reg.getLocalitzadorsDocs().split(",");
    }catch( Exception ex){
    	localitzadorsDocs=null;
    }
    %>
    
      	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>		
		<li><a href="RemiEntradaSel.jsp"><fmt:message key='consulta_registres_pendents_remisio'/></a></li>
                <% 
                if (listado!=null) { 
                        String oficina=listado[0];
				%>
                <li><a href="RemiEntradaLis.jsp?oficina=<%=oficina%>"><fmt:message key='tornar_a_seleccionar_registre'/></a></li>
                <% } %>
		<li><fmt:message key='registre_entrada_pendent_remisio'/></li>
		</ul>
		<!-- Fi Molla pa-->
  


<!--         <CENTER><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center> -->
        <p></p>
        <!-- Cuerpo central -->
        <% if(!reg.getLeido()) {%>
        <center>
            <b><fmt:message key='no_sha_trobat_registre'/> <%=numeroEntrada%>/<%=ano%></B>
        </center>
        &nbsp;<br><center>[&nbsp;<a href="RemiEntradaSel.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center>

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
                <td class="cellaEntrades"width="581">
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
                                &nbsp;<fmt:message key='oficina'/>: <font class="ficha"><%=reg.getOficina()%></font>-<font class="ficha"><%=reg.getDescripcionOficina()%></font>
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
                    
                    <div style="display:inline;width:580px;">
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
                <td class="cellaEntrades" width="581">&nbsp;<br/>
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
        
        <%-- formulario --%>
    <div align="center">        
            <table border="0" width="50%">
                    <tr>
                        <td>
      				     <form name="oficioForm" action="RemiEntradaPaso.jsp" method="post">
				            <input type="hidden" name="registre" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(codOficina)%>-<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(numeroEntrada)%>-<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>-<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(reg.getOficinafisica())%>">
				            <input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(codOficina)%>">
   				     	    <input type="hidden" name="numeroEntrada" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(numeroEntrada)%>">
    				        <input type="hidden" name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>">
                            <input type="hidden" name="oficinaFisica" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(reg.getOficinafisica())%>">
            				<!-- Boton de enviar -->          
                            <p align="center">
                                <input type="submit" name="crearSortida" value="<fmt:message key='crear_registre_sortida'/>">
                            </P>
                            </form>
                        </td>
                        <td>
                           <form name="oficioDescartaForm" id="oficioDescartaForm" action="RemiEntradaDescartar.jsp" method="post" >
				            <input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(codOficina)%>">
        				    <input type="hidden" name="numeroEntrada" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(numeroEntrada)%>">
            				<input type="hidden" name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>">
            				<input type="hidden" name="oficinaFisica" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(reg.getOficinafisica())%>">
            				<input type="hidden" name="motius" value=""/>
                            <!-- Boton de enviar -->          
                            <p align="center">
                                <input type="button" name="descartar" value="<fmt:message key='descartar'/>" onclick="return confirmaDescarta()">
                            </P>
                            </form>
                        </td>
                    </tr>
            </table>
    </div>
        <p>
            <center>
                [&nbsp;<a href="RemiEntradaSel.jsp"><fmt:message key='tornar_a_seleccionar_criteris'/></a>&nbsp;]
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <% 
                if (listado!=null) { 
                    String oficina=listado[0];
                    String oficinaFisica=listado[1];
                    boolean docsElec = (listado[2].equalsIgnoreCase("true"));
                    
                %>
                [&nbsp;<a href="RemiEntradaLis.jsp?oficina=<%=oficina%>&oficinafisica=<%=oficinaFisica%><%=((docsElec)?"&mostrarRegistrosConDocElectronicos=true":"") %>"><fmt:message key='tornar_a_seleccionar_registre'/></a>&nbsp;]
                <% } %>
            </center>
        </p>
        <% } %>
   		
                 
   		
    </body>
</html>
