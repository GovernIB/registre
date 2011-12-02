<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
%>

<%-- Consulta de registros de salida. ficha --%>
<%@page import="java.util.*,es.caib.regweb.logic.interfaces.*,es.caib.regweb.logic.util.*,es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%
	String usuario = request.getRemoteUser();
	String codOficina = request.getParameter("oficina");
	String ano = request.getParameter("any");
	String oficio = request.getParameter("numero");

	RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
	ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
	ParametrosRegistroSalida reg = new ParametrosRegistroSalida();
	ValoresFacade valores = ValoresFacadeUtil.getHome().create();
	OficioRemisionFacade of = OficioRemisionFacadeUtil.getHome().create();
	Vector modelosOficios = valores.buscarModelos("tots", "totes");

	ParametrosOficioRemision param = new ParametrosOficioRemision();

	param.setAnoOficio(ano);
	param.setNumeroOficio(oficio);
	param.setOficinaOficio(codOficina);
	ParametrosOficioRemision ofi = of.leer(param);
	
	if(ofi.isLeidos()){
		pregsal.fijaUsuario(usuario);
		pregsal.setoficina(codOficina);
		pregsal.setNumeroSalida(ofi.getNumeroSalida());
		pregsal.setAnoSalida(ano);
		reg = regsal.leer(pregsal);
	}else{
		reg=null;
	}
%>
<!-- Exploter no renderitza bé.  -->
<html>
    <head>
    	<title><fmt:message key='registre_de_sortides'/></title>    
        <script>
        function imprimeOficio() {
        	var url = "imprimeixOfici?";
            url += "oficina="+encodeURIComponent("<%=codOficina%>");
        	url += "&oficio="+encodeURIComponent("<%=oficio%>");
        	url += "&any="+encodeURIComponent("<%=ano%>");
			url += '&mode=S&modelo='+encodeURIComponent(document.getElementById('model').value);
        	window.open(url, "oficio");  		
        }
        </script>       
        <script>
        function confirmaDescarta() {
            var cadena=prompt("<fmt:message key='esta_segur_anular_ofici_motius'/>","");
            if (cadena==null || cadena=="") {
            return false;
            }
        	oficioDescartaForm.motius.value=cadena;
            
            oficioDescartaForm.submit();
        }      
		</script>
    </head>
    <body bgcolor="#FFFFFF">
		<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="index.jsp"><fmt:message key='inici'/></a></li>		
			<li><a href="busquedaOficiosIndex.jsp"><fmt:message key='consulta_oficis'/></a></li>
            <li><a href="busquedaOficiosXOficio.jsp"><fmt:message key='consulta_per_numero_dofici_i_any'/></a></li>
			<li><fmt:message key='oficis_de_remisio'/></li>
		</ul>
		<!-- Fi Molla pa-->
        <p>&nbsp;</p>
        <!-- Cuerpo central -->
        <%
        	if (reg == null) {
        %>
        <center>
            <b><fmt:message key='ofici_no_trobat'/> <%=oficio%>/<%=ano%></b>
        </center>
        &nbsp;<br><center>[&nbsp;<a href="busquedaOficiosXOficio.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center>
        <%
        	} else {
        		String fechaSalida = reg.getDataSalida();
        		String fechaVisado = "";

        		if (reg.getDataVisado() != null
        				&& !reg.getDataVisado().trim().equals("0")) {
        			fechaVisado = reg.getDataVisado();
        		}

        		String hora = reg.getHora();
        		String hhmm = "";
        		if (hora != null && !hora.equals("") && !hora.equals("0")) {
        			if (hora.length() < 4) {
        				hora = "0" + hora;
        			}
        			String hh = hora.substring(0, 2);
        			String mm = hora.substring(2, 4);
        			hhmm = hh + ":" + mm;
        		} else {
        			hhmm = hora;
        		}

        		if (Helper.estaPdteVisado("S", reg.getOficina(), reg.getAnoSalida(), reg.getNumeroSalida())) {
        %>
           <SCRIPT>
                alert("<fmt:message key='avis_modificacio_pendent_visat'/>");
           </SCRIPT>
      <%
      	}
      %>
        <center>
        <table width="591" class="recuadroEntradas">
            <tr>
                <td class="cellaSortides" width="581">
                    <table width="100%" border="0">
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='oficina'/>: <font class="ficha"><%=ofi.getOficinaOficio()%></font>-<font class="ficha"><%=reg.getDescripcionOficina()%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='num_ofici'/>: <font class="ficha"><%=ofi.getNumeroOficio()%>/<%=ofi.getAnoOficio()%></font>
                            </td>
                            <td style="border:0" >
                                <c:set var="texto" scope="page"><%=ofi.getFechaOficio()%></c:set>
                                <fmt:message key='data_ofici'/><font class="ficha"><c:out escapeXml="false" value="${texto}"/></font>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='data_sortida'/> :<font class="ficha"><%=fechaSalida%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='registro.hora'/> <font class="ficha"><%=hhmm%></font>
                            </td>
                            <td style="border:0" >
                                <fmt:message key='ofici_annullat'/>&nbsp;<%=(ofi.getNulo().trim().equals("") || ofi.getNulo().trim().equals("N")) ? "" : "S"%>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0" >
                                &nbsp;<fmt:message key='oficina'/>: <font class="ficha"><%=reg.getOficina()%></font>-<font class="ficha"><%=reg.getDescripcionOficina()%></font>
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
                    <%
                    	String entrada1 = reg.getEntrada1().equals("0") ? "" : reg.getEntrada1();
                    		String entrada2 = reg.getEntrada2().equals("0") ? "" : reg.getEntrada2();
                    		String barra = " / ";
                    		if (entrada1.equals("") && entrada2.equals("")) {
                    			barra = "";
                    		}
                    		String entrada = entrada1 + barra + entrada2;
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
                    <fmt:message key='registro.num_correo'/> <font class="ficha"><%=(reg.getCorreo() == null) ? "" : reg.getCorreo()%></font>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                    </c:choose>
                    </p>
                    
                    <!-- Extracto del documento -->
                    <c:set var="texto" scope="page"><%=reg.getComentario()%></c:set>                
                    <p>&nbsp;&nbsp;<fmt:message key='extracte_del_document'/>: <font class="ficha"><c:out escapeXml="false" value="${texto}"/></font></p>
					<p></p>
					<c:set var="texto" scope="page"><%=ofi.getDescripcion().replaceAll(";", "<br/>")%></c:set>                
                    <p><c:out value="${texto}" escapeXml="false" /></p>
					<p></p>
					<% if (es.caib.regweb.logic.helper.Conf.get("integracionIBKEYActiva","false").equalsIgnoreCase("true")){
						//Mostramos aviso si el registro reitido tiene documenos electrónicos anexados.
						 if(reg.tieneDocsElectronicos()) {%>
						 	<p>
	          				&nbsp;&nbsp;<img src="imagenes/icono_document.gif" border="0"  title="<fmt:message key='document_electronic'/>">
	          				<fmt:message key='oficis.tieneDocsElectronicos'/>
	        				</p>
	        				<br/>
	        			<%}%>
        			<%} %> 
                </td>
            </tr> 
           <tr>
           		<td class="cellaSortides" width="581">
          			<form name="imprimirOficio" action="RemiSalidaPaso.jsp" method="post">
    					<input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen
								.toHtml(codOficina)%>">
       					<input type="hidden" name="numeroSalida" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(reg
								.getNumeroSalida())%>">
        				<input type="hidden" name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>">
        				<input type="hidden" name="oficio" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(oficio)%>">
                        <!-- Boton de enviar --> 
                        <br/>         
                        <p align="center">
                          <%
                          	if (modelosOficios.size() > 0) {
                          %>
                        	<select name="model" id="model">
                    		<%
                    			for (int i = 0; i < modelosOficios.size(); i++) {
                    						String codiModel = modelosOficios.get(i).toString();
                    		%>
                        	    <option value="<%=codiModel%>"><%=codiModel%></option>
                       		<%
                       			}
                       		%>
                    		</select>
                            <input type="button" name="crearEntrada" onclick="imprimeOficio()" value="<fmt:message key='tornar_imprimir_ofici'/>">
                        <%
                        	} else {
                        %>
                          <fmt:message key="no_hi_ha_models_ofici"/>
                        <%
                        	}
                        %>
                        </p>
                    </form>
                 </td>
			</tr>
            
        </table>
	<%
		if (ofi.getNulo().trim().equals("")|| ofi.getNulo().trim().equals("N")) {
	%>
        <%-- formulario --%>
        <div align="center">        
                <table border="0" width="50%">
                            <tr>
                            <td>
                               <form name="oficioDescartaForm" action="fichaOficioAnular.jsp" method="post" >
    				            <input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen
									.toHtml(codOficina)%>">
                				<input type="hidden" name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen
											.toHtml(ano)%>">
        						<input type="hidden" name="oficio" value="<%=es.caib.regweb.webapp.servlet.HtmlGen
									.toHtml(oficio)%>">
                				<input type="hidden" name="motius" value=""/>
                                <!-- Boton de enviar -->          
                                <p align="center">
                                <input type="button" name="descartar" value="Anular" onclick="return confirmaDescarta()">
                                </P>
                                </form>
                            </td>
                        </tr>
                </table>
        </div>
	<%
		}
      }
%>               
</body>
</html>