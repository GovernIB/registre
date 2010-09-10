<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--
  Registro General CAIB - Registro de Entradas
--%>
<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*,java.net.URLDecoder" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
    <%String usuario=request.getRemoteUser();
    
    
    String codOficina=(String)request.getAttribute("oficina");
    String numeroSalida=(String)request.getAttribute("numeroSalida");
    String ano=(String)request.getAttribute("ano");
    String oficio=(String)request.getAttribute("oficio");

    if (codOficina==null) codOficina=request.getParameter("oficina");
    if (numeroSalida==null) numeroSalida=request.getParameter("numeroSalida");
    if (ano==null) ano=request.getParameter("ano");
    if (oficio==null) oficio=request.getParameter("oficio");
    %>

<%
    ParametrosRegistroEntrada registro;
    registro=(ParametrosRegistroEntrada)request.getAttribute("registroEntrada");

    
    RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
    ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
    ParametrosRegistroSalida reg = new ParametrosRegistroSalida();

    
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    
    OficioRemisionFacade ofirem = OficioRemisionFacadeUtil.getHome().create();
    ParametrosOficioRemision param = new ParametrosOficioRemision();
    ParametrosOficioRemision ofi = new ParametrosOficioRemision();

    pregsal.fijaUsuario(usuario);
    pregsal.setoficina(codOficina);
    pregsal.setNumeroSalida(numeroSalida);
    pregsal.setAnoSalida(ano);
    reg=regsal.leer(pregsal);
	
	param.setAnoOficio(ano);
	param.setOficinaOficio(codOficina);
	param.setNumeroOficio(oficio);
	ofi = ofirem.leer(param);

%>

<%!
    String errorEn(Hashtable errores, String campo) {
        return (errores.containsKey(campo))? "errorcampo" : "";
    }

    void escribeSelect(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia) throws java.io.IOException {

        for (int i=0;i<valores.size();i=i+2){
            String codigo=valores.get(i).toString();
            String descripcion=valores.get(i+1).toString();
            out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
            if (tipo.equals("N")) {
                out.write(descripcion);
            } else {
                out.write(codigo+" - "+descripcion);
            }
            out.write("</option>\n");
        }
    }
    
    void escribeSelect2(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia, String referencia2) throws java.io.IOException {

        for (int i=0;i<valores.size();i=i+3){
        	String ofi=valores.get(i).toString();
        	String codigo=valores.get(i+1).toString();
            String descripcion=valores.get(i+2).toString();
            out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) && ofi.equals(referencia2) ? "selected" : "")+">");
            if (tipo.equals("N")) {
                out.write(descripcion);
            } else {
                out.write(codigo+" - "+descripcion);
            }
            out.write("</option>\n");
        }
    }

    String retornarChecked(ParametrosRegistroEntrada registro){
		String retornar = "";
		
		try{
		if(registro!=null)
			if(!(registro.getMunicipi060().equals("000") || registro.getMunicipi060().equals("")))
				retornar = " checked ";
		}catch(Exception e){
			e.printStackTrace();
		}
		return retornar;
	}
    String retornarDisabled(ParametrosRegistroEntrada registro){
		String retornar = " disabled=\"disabled\" ";
		
		try{
		if(registro!=null)
			if(!(registro.getMunicipi060().equals("000") || registro.getMunicipi060().equals("")))
				retornar = " ";
		}catch(Exception e){
			e.printStackTrace();
		}
		return retornar;
	}
%>

<%
    Integer intSerie=(Integer)session.getAttribute("serie");
    if (intSerie==null) {
        intSerie=new Integer(0);
        session.setAttribute("serie", intSerie);
    }
    
    Vector oficinasfisicas = valores.buscarOficinasFisicas(usuario, "AE");

%>
<!-- Exploter no renderitza bé.  -->
<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/CSS" rel="stylesheet" href="estilos.css"/>
        
        <script language="javascript" src="livescript.js"></script>
        <script language="javascript" src="jscripts/TAO.js"></script>
<script language="javascript">
     function activar_060(){
       <c:if test="${initParam['registro.entrada.view.registre012']}">
			valor=document.registroForm.Reg060.checked;
            
            if (valor){
           
			    document.registroForm.mun_060.disabled = false;
            }else{
				
				document.registroForm.mun_060.disabled = true;
			}
			</c:if>
}
</script>
        <script>
            confirmandoProceso=false;
 
            function confirmaProceso() {

            	valor=document.registroForm.comentario.value;
            	valor1=document.registroForm.altres.value;
            	valor2=document.registroForm.fora.value;
            	valor3=document.registroForm.correo.value;
            	valor4=document.registroForm.disquet.value;
            	if (valor.indexOf('¤',0)>-1 || valor1.indexOf('¤',0)>-1 || valor2.indexOf('¤',0)>-1 || valor3.indexOf('¤',0)>-1 || valor4.indexOf('¤',0)>-1) {
            		alert("El símbol \"¤\" no és permès a l\'aplicació. Emprau \"euro\" o \"euros\" segons pertoqui");
            		return false;
            	} else { 
                <c:if test="${initParam['registro.entrada.view.registre012']}">
                    if (document.registroForm.Reg060.checked && document.registroForm.mun_060.value == "000"){
                      alert("S'ha de seleccionar el municipi del 060.");
                      return false;
                    }
                </c:if>
                confirmandoProceso=true;
                return true;
            	} 
            }
            
            function cerrarVentana() {
            	if (!confirmandoProceso) {
            		desbloqueaSession();
            	}
            }
            
            function desbloqueaSession() {
            	var RECUPERAVALOR_PATH = 'DesbloqueaSession';
            	var context = new InvocationContext(RECUPERAVALOR_PATH);
            	context.onresult = function(value) {
            		//cambiaTexto(value, "destinatario_desc");
            	};
            	context.onerror = function(message) {
            	alert("Error: " + message);
            };
            	context.invoke("hola");
            }
            
            // variables globales para entidad1, entidad2, destinatario
            e1="";
            e2="";
            de="";

            function setDestinatari(cod, descod) {
            document.registroForm.destinatari.value=cod;
            	cambiaTexto(descod, 'destinatario_desc');
            }

            function setPersona(persona) {
            document.registroForm.altres.value=persona;
            }

            function abreDestinatarios() {
            	codOficina=document.registroForm.oficinasel.options[document.registroForm.oficinasel.selectedIndex].value;
            	miVentana=open("popup/destinatarios.jsp?oficina="+codOficina,"destinatarios","scrollbars,resizable,width=300,height=200");
            	miVentana.focus();
            }

            function abreBDP() {
            	miBDP=open("popup/BDP.jsp","BDP","scrollbars,resizable,width=360,height=280");
            	miBDP.focus();
            }

            function setEntidad(codEntidad1, codEntidad2, descod) {
            document.registroForm.entidad1.value=codEntidad1;
            document.registroForm.entidad2.value=codEntidad2;
            	cambiaTexto(descod, 'remitente_desc');
            }

            function abreRemitentes() {
            	miRemitentes=open("popup.do?accion=remitentes","remitentes","scrollbars,resizable,width=400,height=400");
            	miRemitentes.focus();
            }
            function abreDisquete() {
            	codOficina=document.registroForm.oficinasel.options[document.registroForm.oficinasel.selectedIndex].value;
            	fentrada=document.registroForm.dataentrada.value;
            	miDisquete=open("popup/disquete.jsp?oficina="+codOficina+"&tipo=E&fEntrada="+fentrada,"disquete","scrollbars,resizable,width=250,height=150");
            	miDisquete.focus();
            }

            function recuperaDestinatario() {
            	texto=document.registroForm.destinatari.value;
            	if (de==texto) {
            		//return;
            	}
           	 	de=texto;
            	var RECUPERAVALOR_PATH = 'RecuperaDescripcionDestinatario';
            	var context = new InvocationContext(RECUPERAVALOR_PATH);
            	context.onresult = function(value) {
            	var re = new RegExp ("\\+", 'gi') ;
            	value1=value.replace(re, ' ');
            	cambiaTexto(value1, "destinatario_desc");
            };

            context.onerror = function(message) {
	            alert("Error: " + message);
            };
            context.invoke(texto);
            }

            function recuperaDescripcionEntidad() {
            	entidad1=document.registroForm.entidad1.value;
            	entidad2=document.registroForm.entidad2.value;
            	if (e1==entidad1 && e2==entidad2) {
            		return;
            }
            e1=entidad1;
            e2=entidad2;
            
			if (entidad1 != null) {
            	var RECUPERAVALOR_PATH = 'RecuperaDescripcionRemitente';
	            var context = new InvocationContext(RECUPERAVALOR_PATH);
	            context.onresult = function(value) {
	            var re = new RegExp ("\\+", 'gi') ;
	            value1=value.replace(re, ' ');
	            cambiaTexto(value1, 'remitente_desc');
            };
            context.onerror = function(message) {
    	        alert("Error: " + message);
            };
            context.invoke(entidad1, entidad2);
            }
            }
	
            
        </script>

        <style>
            #destinatario_desc {background-color: #cccccc;}
            #remitente_desc {background-color: #cccccc;}
        </style>

    </head>

    <body bgcolor="#FFFFFF" onunload="cerrarVentana()">

        
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="RemiSalidaSel.jsp"><fmt:message key='consulta_oficis_remisio_pendents_arribar'/></a></li>
		<li><fmt:message key='registre_ofici_remisio'/></li>
		</ul>
		<!-- Fi Molla pa-->
        <div align="center">
<!--        <p>
         <center>
        <font class="titulo">
            Usuari : <%=usuario%>
        </font>
        </center> 
        &nbsp;<p>-->
        
        <!-- Mostramos Errores si los hubiera -->

        <% Hashtable errores = (registro==null)?new Hashtable():registro.getErrores();
            if (errores.size() > 0) {%>
        <table class="recuadroErrors" width="599" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
                        <%      for (Enumeration e=errores.elements();e.hasMoreElements();) { %>
                        <li><%= e.nextElement()%></li>
                        <%}%>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
        <%  } %>
        <!-- Cuerpo central -->
        <form name="registroForm" action="RemiSalidaPasoEntrada.jsp" method="post" onsubmit="return confirmaProceso()">

        <center>
        <table border="0" width="599">
            <tr>
                <td>
                    <font class="errorcampo">*</font>&nbsp;<fmt:message key='registro.campos_obligatorios'/>
                </td>
            </tr>
        </table>
        <table class="recuadroEntradas" width="599">
            <input type="hidden" name="serie" value="<%=intSerie%>">
            <tr>
                <td class="cellaEntrades">
                    <!-- Tabla para datos de cabecera -->
                    <table class="bordeEntrada" style="border:0">
                        <tr>
                            <td style="border:0">
                                <!-- Data d'entrada -->
                                <font class="<%=errorEn(errores,"dataentrada")%>"> <fmt:message key='registro.fecha_entrada'/></font>
                                <%String anteriorDataEntrada=(registro==null)? "":registro.getDataEntrada();%>
                                <input id="dataentrada" type=text name="dataentrada" value="<%=anteriorDataEntrada.equals("") ? valores.getFecha() : anteriorDataEntrada %>" size="10" >
                            </td>
                            <td style="border:0;">
                                <!-- Hora d'entrada -->
                                &nbsp;&nbsp;&nbsp;&nbsp;
                                <font class="<%=errorEn(errores,"hora")%>"><fmt:message key='registro.hora'/></font>
                                <% String anteriorHora=(registro==null)? "":registro.getHora();%>
                                <input type=text name="hora" value="<%=anteriorHora.equals("") ? valores.getHorasMinutos() : anteriorHora %>" size="5">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0">
                                <!-- Despegable para oficinas autorizadas para el usuario -->
                                &nbsp;<br><font class="<%= errorEn(errores,"oficina")%>"><fmt:message key='oficina'/>:</font>
                                <select name="oficinasel" disabled>
                                    <% escribeSelect(out, "S", valores.buscarOficinas(usuario, "AE"), reg.getEntidad2()); %>
                                </select>
                                <input type="hidden" name="oficina" value="<%=reg.getEntidad2()%>"/>
                            </td>
                    <td style="border:0">
                        <!-- Despegable para oficinas autorizadas para el usuario -->
                        &nbsp;<br><font class="<%= errorEn(errores,"oficinafisica")%>"><fmt:message key='oficina_fisica'/>:</font>
                        <select name="oficinafisica" id="oficinafisica">
                                    <%  escribeSelect2(out, "N", valores.buscarOficinasFisicas(usuario, "AE"), (reg==null)? "":reg.getOficinafisica(), (reg==null)? "":reg.getOficina()); %>
                        </select>
                    </td>
                            
                        </tr>
                    </table>
                    <!-- De la tabla principal -->
                </td>
            </tr>
            <tr>
            <td class="cellaEntrades">
            <!-- Tabla para los datos del documentos -->
            <table class="bordeEntrada" style="border:0;">
                <!-- 1ª fila de la tabla -->
                <tr>
                <td style="border:0;" colspan="2">
                &nbsp;<br><b><fmt:message key='dades_del_document'/></b><p>
                </TD>
                </TR>
                <!-- 2ª fila de la tabla -->  
                <tr>
                    <!-- Fecha del documento -->
                    <td style="border:0;" colspan="2">
                        <font class="<%= errorEn(errores,"data") %>"><fmt:message key='registro.fecha'/></font>
                        <% String anteriorData=(registro==null)? "":registro.getData(); %>
                        <input type=text name=data value="<%=anteriorData.equals("") ? valores.getFecha() : anteriorData %>" size="10" > 
                        <!-- Despegable para Tipos de documentos -->
                        &nbsp;<font class="errorcampo">*</font>
                        <font class="<%=errorEn(errores, "tipo")%>"><fmt:message key='registro.tipo'/> </font>
                        <select name="tiposel" size="1" style="width: 250px" disabled>
                            <% escribeSelect(out, "N", valores.buscarDocumentos(), application.getInitParameter("oficios.config.tipo_documento")); %>
                        </select>
                        <input type="hidden" name="tipo" value="${initParam['oficios.config.tipo_documento']}}"/>
                        <!-- Despegable para Idiomas -->
                        <font class="<%=errorEn(errores,"idioma")%>"><fmt:message key='registro.idioma'/></font>
                        <select name="idiomasel" size="1" disabled>
                            <% escribeSelect(out, "N", valores.buscarIdiomas(), application.getInitParameter("oficios.config.idioma")); %>
                        </select>
                        <input type="hidden" name="idioma" value="${initParam['oficios.config.idioma']}"/>
                    </td>
                </tr>
                <!-- 3ª fila de la tabla -->
                <tr>
                <!-- Remitente -->
                <td style="border:0;" width="55%">
                <br><font class="errorcampo">*</font>
                <fmt:message key='remitent'/>........<font class="<%=errorEn(errores,"entidad1")%>"><fmt:message key='registro.entidad'/></font>
                <!-- Remitente Entidad 1 -->
                <input id="enti" type=text name=entidad1 readonly size="7" value="${initParam['oficios.config.entidad']}" onblur="recuperaDescripcionEntidad()"> 
                <!-- Remitente Entidad 2 -->
               <%
               // Calculamos el destinatario
               Double num_ent2 = new Double(Math.ceil(Integer.parseInt(reg.getRemitent())/100));
               String str_ent2 = Integer.toString(num_ent2.intValue());
               %>
                <input type=text name=entidad2 size="3" readonly value="<%=str_ent2%>" onblur="recuperaDescripcionEntidad()">
                </td>
                <!-- Descipcion del Remitente  -->
                <td style="border:0;" width="45%"> 
                    <div id="remitente_desc" style="font-size:12px; font: bold; "></div>
                </td>
                </tr>
                <!-- 4ª fila de la tabla -->
                <tr>
                <td style="border:0;" colspan="2">
                <!-- Remitente Altres entidades -->
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;
                <fmt:message key='altres'/>&nbsp;&nbsp;<input onkeypress="return check(event)" type=text name=altres size="30" maxlength="30" value="<%=(registro==null)? "":registro.getAltres().trim()%>">
                <%--<a href="javascript:abreBDP()">
                <img border="0" src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>">
                </a>
                --%>
                </td>
                </tr>
                <tr>
                    <td style="border:0;" colspan="2">&nbsp;</td>
                </tr> 
                <!-- 5ª fila de la tabla -->
                <tr>
                    <td style="border:0;" colspan="2">
                    <table>
                        <tr>
                            <td style="border:0;" valign="middle">
                                <!-- Procedencia geografica -->
                                <font class="errorcampo">*</font>
                                <fmt:message key='procedencia_geografica'/>.........
                            </td>
                            <td  style="border:0;" valign="middle">
                                <!-- Despegable para la Procedencia Geografica de Baleares -->
                                <span class="<%=errorEn(errores,"balears")%>"> <fmt:message key='registro.baleares'/></span>
                            </td>
                            <td style="border:0;">
                            <select name=balears>
                            <% escribeSelect(out, "N", valores.buscarBaleares(), ""); %>
                            </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0;">&nbsp;</td>
                            <td style="border:0;" valign="bottom" colspan="2">
                                <fmt:message key='registro.fuera_baleares'/>&nbsp;
                                <input onkeypress="return check(event)" type=text name=fora size="25" maxlength="25" value="">            
                            </td>
                        </tr>
                    </table>
                    </td>
                </tr>
                <!-- 7ª fila de la tabla -->
                <tr>
                <td style="border:0;" colspan="2">
                    <!-- Numero de salida -->
                    &nbsp;<br><font class="<%=errorEn(errores,"salida1")%>"><fmt:message key='registro.num_sortida'/></font>
                    <input readonly onKeyPress="return goodchars(event,'0123456789')" type=text name=salida1 maxlength="6" size="6" value="<%=reg.getNumeroSalida()%>">&nbsp;&nbsp;/&nbsp; 
                    <input readonly onKeyPress="return goodchars(event,'0123456789')" type=text name=salida2 maxlength="4" size="4" value="<%=reg.getAnoSalida()%>">
                </td>
                </tr> 
                <!-- 8ª fila de la tabla -->
                <tr>
                    <td style="border:0;">
                    <!-- Organismo destinatario -->
                    &nbsp;<br><font class="errorcampo">*</font><font class="<%=errorEn(errores,"destinatari")%>"><fmt:message key='registro.organismo_destinatario'/>..............:</font>
                    <input id="desti" type=text name=destinatari size="4"  maxlength="4" value="<%=reg.getEntidad2()+"00"%>" onblur="recuperaDestinatario()">
                    <a href="javascript:abreDestinatarios()">
                        <img src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>" border="0">
                    </a>
                    </td>
                    <td style="border:0" >
                    <div id="destinatario_desc" style="font-size:12px; font: bold;"></div>
                    </td>
                </tr>
                <!-- 9ª fila de la tabla -->
                <c:if test="${initParam['registro.entrada.view.registre012']}">
                <tr>
					<td style="border:0;" colspan="2">
                    <table>
                        <tr>
                            <td style="border:0;" valign="bottom">
                              <input TYPE="checkbox" NAME="Reg060" VALUE="Si" Onclick="activar_060()" <%=(registro==null)? "":retornarChecked(registro)%> ><fmt:message key='registre_012'/>........................</input>
                            </td> 	
                            <td style="border:0;">
                              <!-- Despegable para AYUNTAMIENTOS DEL 060 -->
                                &nbsp;<br><font class="<%= errorEn(errores,"mun_060")%>"><fmt:message key='registro.entidad_local'/> </font>
                                <select name="mun_060" <%=(registro==null)? "disabled":retornarDisabled(registro)%>
                                    <% 
										String munSeleccionat = (registro==null)? "000":registro.getMunicipi060();
										munSeleccionat = (munSeleccionat.equals("")? "000":munSeleccionat);
										escribeSelect(out, "N", valores.buscar_060(), munSeleccionat); %> 
                                </select>
                            </td>
                        </tr>
                    </table>
					</td>
                </tr>
                </c:if>
				<tr>
            		<td style="border:0" colspan="2" height="5px"></td>
        		</tr>
            </table>
                </td>
            </tr>
            <tr>
            <td class="cellaEntrades">
            <!-- tabla de datos del Extracto -->
            <table class="bordeEntrada" style="border:0;" >
                <tr>
                <td style="border:0;">
                &nbsp;<br><b><fmt:message key='dades_de_lextracte'/></b>
                </td>
                </tr>
                <tr>
                    <td style="border:0;">
                        <!-- Idioma del Extracto -->
                        &nbsp;<br><font class="<%=errorEn(errores,"idioex")%>"><fmt:message key='registro.idioma'/></font>
                        <select name="idioexsel" disabled>
                            <% String anteriorIdioex="2";//reg.getIdioex(); %>
                            <option value="2" <%=anteriorIdioex.equals("2") ? "selected" : "" %> > <fmt:message key='registro.idioma.catala'/>
                            <option value="1" <%=anteriorIdioex.equals("1") ? "selected" : "" %> > <fmt:message key='registro.idioma.castella'/>
                        </select>&nbsp;
                    <input type="hidden" name="idioex" value="<%=anteriorIdioex%>"/>

                        <c:choose>
                        <c:when test="${initParam['registro.entrada.view.disquete_correo']}">
                        <!--Numero de disquete -->
                        <font class="<%=errorEn(errores,"disquet")%>"><fmt:message key='registro.num_disquete'/> </font>
                        <input readonly onkeypress="return check(event)" type=text name=disquet size="8" value="<%=reg.getDisquet()!=null?reg.getDisquet().trim():""%>">
                        <!-- <a href="javascript:abreDisquete()"><img src="imagenes/buscar.gif" align=middle alt="Darrer disquet" border="0"></a> -->
                        <!--Numero de disquete -->
                        &nbsp;&nbsp;
                        <font class="<%=errorEn(errores,"correo")%>"><fmt:message key='registro.num_correo'/> </font>
                        <input readonly id="corr" onkeypress="return check(event)" type=text name=correo size="8" value="<%=reg.getCorreo()!=null?reg.getCorreo().trim():""%>">
                        </c:when>
                        <c:otherwise>
                        <input type="hidden" name="disquet" value=""/>
                        <input type="hidden" name="correo" value=""/>
                        </c:otherwise>
                        </c:choose>
                        
                        
                    </td>
                </tr>
                <tr>
                    <td style="border:0;">
                        &nbsp;<br>
                        <!-- Extracto del documento -->
                        <font class="errorcampo">*</font>
                        <font class="<%=errorEn(errores,"comentario")%>"><fmt:message key='extracte_del_document'/>:
                        <textarea cols="67" readonly onkeypress="return check(event)" rows="3" name="comentario"><%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml("Ofici d'entrada de documentació amb núm. " + ofi.getNumeroOficio() + "/" +ofi.getAnoOficio() + " - " +reg.getDescripcionOrganismoRemitente().trim())%></textarea>
                    </td>
                </tr>
                <tr>
                    <td style="border:0">
                        <!-- Boton de enviar -->          
                        <p align="center">
                        <input type=submit value="<fmt:message key='enviar'/>">
                        </P>
                    </td>
                </tr>
            </table>
                </td>
            </tr>
    
        </table>
        
        <input type="hidden" name="numeroSalida" value="<%=numeroSalida %>"/>
        <input type="hidden" name="oficinaSalida" value="<%=codOficina %>"/>
        <input type="hidden" name="ano" value="<%=ano %>"/>
        <input type="hidden" name="oficio" value="<%=oficio %>"/>
        
        
        </center>
        </form>
        </div>
        <!-- Fin Cuerpo central -->
<script type="text/javascript">
    recuperaDescripcionEntidad();
    recuperaDestinatario();

	 var elFocus = document.getElementById('dataentrada');
	 elFocus.focus();
</script>
        
                 
    </body>
</html> 
