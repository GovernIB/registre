<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%--
  Registro General CAIB - Registro de Entradas
--%>

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*,java.net.URLDecoder" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
String usuario=request.getRemoteUser();

String [] listaregistros=(String[])request.getAttribute("listaregistros");
String codOficina=(String)request.getAttribute("oficina");
String numeroEntrada=(String)request.getAttribute("numeroEntrada");
String ano=(String)request.getAttribute("ano");
String oficinaFisica=(String)request.getAttribute("oficinaFisica");

if (listaregistros==null) listaregistros=request.getParameterValues("registre");
if (codOficina==null) codOficina=request.getParameter("oficina");
if (numeroEntrada==null) numeroEntrada=request.getParameter("numeroEntrada");
if (ano==null) ano=request.getParameter("ano");
if (oficinaFisica==null) oficinaFisica=request.getParameter("oficinaFisica");

if (listaregistros!=null && listaregistros.length>0) {
	String reg = listaregistros[0];
	String [] campos = reg.split("-");
	codOficina = campos[0];
	numeroEntrada = campos[1];
	ano = campos[2];
	oficinaFisica = campos[3];
}
%>

<%
    ParametrosRegistroSalida registro;
    registro=(ParametrosRegistroSalida)request.getAttribute("registroSalida");
 
    RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
    ParametrosRegistroEntrada param = new ParametrosRegistroEntrada();
    ParametrosRegistroEntrada reg = new ParametrosRegistroEntrada();

    
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
	String codiCookies = "RWS";

	  param.fijaUsuario(usuario);
    param.setoficina(codOficina);
    param.setNumeroEntrada(numeroEntrada);
    param.setAnoEntrada(ano);
    reg = regent.leer(param);

    // para que no rellene el numero de entrada y año de entrada en el registro de salida
    reg.setNumeroEntrada("");
    reg.setAnoEntrada("");
    
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

%>

<%
    Integer intSerie=(Integer)session.getAttribute("serie");
    if (intSerie==null) {
        intSerie=new Integer(0);
        session.setAttribute("serie", intSerie);
    }
    
    Vector oficinasfisicas = valores.buscarOficinasFisicas(usuario, "AS");

%>
<!-- Exploter no renderitza bé.  -->
<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        
        <script language="javascript" src="livescript.js"></script>
        <script language="javascript" src="jscripts/TAO.js"></script>
        <script>

        var oficinasfisicasarray = new Array();
        <% for (int ii=0; oficinasfisicas!=null && ii<oficinasfisicas.size(); ii=ii+3) { %>
        	oficinasfisicasarray.push([<%= oficinasfisicas.get(ii).toString() %>, <%= oficinasfisicas.get(ii+1).toString() %>,"<%= oficinasfisicas.get(ii+2).toString() %>"]);
        <% } %>


        function refrescaFisica(){
           	fis=document.getElementById('oficinafisica').value;
           	oficina=document.getElementById('oficina').value;
           	options=document.getElementById('oficinafisica').options;
           	ofifis=document.getElementById('oficinafisica');

           	while(ofifis.hasChildNodes()){
           		ofifis.removeChild(ofifis.childNodes[0]);
           	}
           		   	
        	var seleccionado = document.getElementById('oficinafisica').selectedIndex;

            for(i=0; i<oficinasfisicasarray.length; i++) {
           		of = oficinasfisicasarray[i];
           		if(of[0] == oficina){
           			opcion= new Option(of[2],of[1]);
           			opcion.value=of[1];
           			opcion.selected=(fis==of[1]);
           			if (fis==of[1]) seleccionado = options.length;
           			options[options.length]=opcion;	
           		}
           	}
           	document.getElementById('oficinafisica').selectedIndex=seleccionado;
           }


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

            // variables globales para entidad1, entidad2, emisor
            e1="";
            e2="";
            de="";

            function setPersona(persona) {
            document.registroForm.altres.value=persona;
            }

            function abreBDP() {
            	myBDP=open("popup/BDP.jsp","BDP","scrollbars,resizable,width=360,height=280");
            	myBDP.focus();
            }

            function setRemitent(cod, descod) {
            document.registroForm.remitent.value=cod;
            	cambiaTexto(descod, 'remitente_desc');
            }

            function abreRemitentes() {
            	codOficina=document.registroForm.oficina.options[document.registroForm.oficina.selectedIndex].value;
            	myDestinatarios=open("popup/destinatariosSalida.jsp?oficina="+codOficina,"destinatariosSalida","scrollbars,resizable,width=300,height=200");
            	myDestinatarios.focus();
            }

            function setEntidad(codEntidad1, codEntidad2, descod) {
            document.registroForm.entidad1.value=codEntidad1;
            document.registroForm.entidad2.value=codEntidad2;
            	cambiaTexto(descod, 'destinatario_desc');
            }

            function abreDestinatarios() {
            	myRemitentes=open("popup.do?accion=remitentes","remitentes","scrollbars,resizable,width=400,height=400");
            	myRemitentes.focus();
            }
            function abreDisquete() {
            	codOficina=document.registroForm.oficina.options[document.registroForm.oficina.selectedIndex].value;
            	fentrada=document.registroForm.datasalida.value;
            	myDisquete=open("popup/disquete.jsp?oficina="+codOficina+"&tipo=S&fEntrada="+fentrada,"disquete","scrollbars,resizable,width=250,height=150");
            	myDisquete.focus();
            }

            function recuperaRemitente() {
            	texto=document.registroForm.remitent.value;
            	if (de==texto) {
            		return;
            }
            de=texto;
            var RECUPERAVALOR_PATH = 'RecuperaDescripcionDestinatario';
            var context = new InvocationContext(RECUPERAVALOR_PATH);
            context.onresult = function(value) {
            	var re = new RegExp ("\\+", 'gi') ;
            	value1=value.replace(re, ' ');
            	cambiaTexto(value1, "remitente_desc");
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
            		cambiaTexto(value1, 'destinatario_desc');
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
		<li><a href="RemiEntradaSel.jsp"><fmt:message key='consulta_registres_pendents_remisio'/></a></li>
		<li><fmt:message key='registre_ofici_remisio'/></li>
		</ul>
		<!-- Fi Molla pa-->
<!--         <div align="center"> --> 
<!--        <center>
            <font class="titulo">
                Usuari : <%=usuario%>
            </font>
        </center> -->
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

        <center>
        <form name="registroForm" action="RemiEntradaPasoSalida.jsp" method="post" onsubmit="return confirmaProceso()">
        
        <table border="0" width="599">
            <tr>
                <td>
                    <font class="errorcampo">*</font>&nbsp;<fmt:message key='registro.campos_obligatorios'/>
                </td>
            </tr>
        </table>
        <table width="599" class="recuadroSalidas">
            <input type="hidden" name="serie" value="<%=intSerie%>">
            <tr>
            <td class="cellaSortides">
                <!-- Tabla para datos de cabecera -->
                <table class="bordeSalida" style="border:0">
                    <tr>
                    <td style="border:0" width="60%">
                        <!-- Data d'entrada -->
                        <font class="<%=errorEn(errores,"datasalida")%>"><fmt:message key='registro.fecha_salida'/></font>
              <%String anteriorDataSalida=(registro==null)? "":registro.getDataSalida();%>
                        <input id="datasalida" type=text name="datasalida" value="<%=anteriorDataSalida.equals("") ? valores.getFecha() : anteriorDataSalida %>" size="10" >
                    </td>
                    <td style="border:0">
                        <!-- Hora d'entrada -->
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <font class="<%=errorEn(errores,"hora")%>"><fmt:message key='registro.hora'/></font>
              <% String anteriorHora=(registro==null)? "":registro.getHora();%>
                        <input type=text name=hora value="<%=anteriorHora.equals("") ? valores.getHorasMinutos() : anteriorHora %>" size="5">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    </tr>
                    <tr>
                    <td style="border:0">
                        <!-- Despegable para oficinas autorizadas para el usuario -->
                        &nbsp;<br><font class="<%= errorEn(errores,"oficina")%>"><fmt:message key='oficina'/>:</font>
                        <select name="oficinasel" disabled>
                   <% escribeSelect(out, "S", valores.buscarOficinas(usuario, "AS"), codOficina); %>
                        </select>
                        <input type="hidden" name="oficina" id="oficina" value="<%=codOficina%>"/>
                    </td>
                    <td style="border:0">
                        <!-- Despegable para oficinas autorizadas para el usuario -->
                        &nbsp;<br><font class="<%= errorEn(errores,"oficinafisica")%>"><fmt:message key='oficina_fisica'/>:</font>
                        <select name="oficinafisicasel"  disabled>
                        <%  escribeSelect2(out, "N", valores.buscarOficinasFisicas(usuario, "AS"), oficinaFisica, codOficina); %>
                        </select>
                        <input type="hidden" name="oficinafisica" id="oficinafisica" value="<%=oficinaFisica%>"/>
                    </td>
                    
                    </tr>
                </table>
                <!-- De la tabla principal -->
            </td>
            </tr>
            <tr>
            <td class="cellaSortides">
            <!-- Tabla para los datos del documentos -->
            <table class="bordeSalida" style="border:0">
                <!-- 1ª fila de la tabla -->
                <tr>
                <td style="border:0;" colspan="2">
                &nbsp;<br><b><fmt:message key='dades_del_document'/></b><p>
                </td>
                </tr>
                <!-- 2ª fila de la tabla -->  
                <tr>
                <!-- Fecha del documento -->
                <td style="border:0;" colspan="2">
                    <font class="<%= errorEn(errores,"data") %>"><fmt:message key='registro.fecha'/></font>
          <% String anteriorData=(registro==null)? "":registro.getData(); %>
                    <input type=text name=data value="<%=anteriorData.equals("") ? valores.getFecha() : anteriorData %>" maxlength="10" size="10" >
                    <!-- Despegable para Tipos de documentos -->
                    &nbsp;<font class="errorcampo">*</font>
                    <font class="<%=errorEn(errores, "tipo")%>"><fmt:message key='registro.tipo'/></font>
                    <select name="tiposel" size="1" style="width: 250px" disabled>
                <% escribeSelect(out, "N", valores.buscarDocumentos(), application.getInitParameter("oficios.config.tipo_documento")); %>
                    </select>
                    <input type="hidden" name="tipo" value="${initParam['oficios.config.tipo_documento']}"/>
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
                <fmt:message key='destinatari'/>..<font class="<%=errorEn(errores,"entidad1")%>"><fmt:message key='registro.entidad'/></font>
                <!-- Remitente Entidad 1 -->
                <input type=text name="entidad1" readonly size="7" value="${initParam['oficios.config.entidad']}" onblur="recuperaDescripcionEntidad()"> 
                <!-- Remitente Entidad 2 -->
                
               <%
               // <input type=text name="entidad2" readonly size="3" value="<-%=reg.getDestinatari().substring(0,2)% >" onblur="javascript:recuperaDescripcionEntidad()">
               Double num_ent2 = new Double(Math.ceil(Integer.parseInt(reg.getDestinatari())/100));
               String str_ent2 = Integer.toString(num_ent2.intValue());
               %>
                <input type=text name="entidad2" readonly size="3" value="<%= str_ent2%>" onblur="recuperaDescripcionEntidad()">
                </td>
                <!-- Descipcion del Destinatario  -->
                <td width="45%" style="border:0;">
                    <div id="destinatario_desc" style="font-size:12px; font: bold; "></div>
                </td>
                </tr>
                <!-- 4ª fila de la tabla -->
                <tr>
                <td style="border:0;" colspan="2">
                <!-- Remitente Altres entidades -->
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;
                <fmt:message key='altres'/>&nbsp;&nbsp;<input onkeypress="return check(event)" readonly type=text name="altres" size="30" maxlength="30" value="">
                <!--<a href="javascript:abreBDP()">
                <img border="0" src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>">
                </a>-->
                </td>
                </tr>
                <tr>
                    <td style="border:0;" colspan="2">&nbsp;</td>
                </tr>
               <tr>
                <td style="border:0;" colspan="2">
                    <table>
                        <tr>
                            <td style="border:0;" valign="middle">
                                <!-- Procedencia geografica -->
                                <font class="errorcampo">*</font>
                                <fmt:message key='registro.destino_geografico'/>.........
                            </td>
                            <td style="border:0;" valign="middle">
                                <!-- Despegable para la Procedencia Geografica de Baleares -->
                                <span class="<%=errorEn(errores,"balears")%>"> <fmt:message key='registro.baleares'/></span>
                            </td>
                            <td style="border:0;">
                                <select name="balears">
                                    <% escribeSelect(out, "N", valores.buscarBaleares(), ""); %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0;">&nbsp;</td>
                            <td style="border:0;" valign="bottom" colspan="2">
                                <fmt:message key='registro.fuera_baleares'/>&nbsp;
                                <input onkeypress="return check(event)" type=text name="fora" size="25" maxlength="25" value="">            
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
                
                <!-- 7ª fila de la tabla -->
                <tr>
                <td style="border:0;" colspan="2">
                    <!-- Número d'entrada -->
                    &nbsp;<br><font class="<%=errorEn(errores,"entrada1")%>"><fmt:message key='registro.num_entrada'/></font>
                    <input readonly onKeyPress="return goodchars(event,'0123456789')" type=text name="entrada1" maxlength="6" size="6" value="<%=reg.getNumeroEntrada()%>">&nbsp;&nbsp;/&nbsp; 
                    <input readonly onKeyPress="return goodchars(event,'0123456789')" type=text name="entrada2" maxlength="4" size="4" value="<%=reg.getAnoEntrada()%>">
                </td>
                </TR> 
                <!-- 8ª fila de la tabla -->
                <tr>
                <td style="border:0;">
                <!-- Organismo destinatario -->
                &nbsp;<br><font class="errorcampo">*</font><font class="<%=errorEn(errores,"remitent")%>"><fmt:message key='registro.organismo_emisor'/>..............:</font>
                <input type=text name="remitent" size="4" maxlength="4" value="<%=reg.getOficina()+"00"%>" readonly onblur="recuperaRemitente()">
                </td>
                <td style="border:0" >
                <div id="remitente_desc" style="font-size:12px; font: bold;"></div>
                </td>
                </tr>
            </table>
            </td>
            </tr>
            <tr>
            <td class="cellaSortides">
            <!-- tabla de datos del Extracto -->
            <table class="bordeSalida" style="border:0">
                <tr>
                <td style="border:0;">
          <!--       &nbsp;<br><b><fmt:message key='dades_de_lextracte'/></b>-->
                </TD>
                </TR>
                <tr>
                <td style="border:0;">
                
          	<% String anteriorIdioex="2";//reg.getIdioex(); %>
              <!-- &nbsp;<br><font class="<%=errorEn(errores,"idioex")%>"><fmt:message key='registro.idioma'/></font>
                     <select name="idioexsel" disabled>
                    <option value="2" <%=anteriorIdioex.equals("2") ? "selected" : "" %> > <fmt:message key='registro.idioma.catala'/>
                    <option value="1" <%=anteriorIdioex.equals("1") ? "selected" : "" %> > <fmt:message key='registro.idioma.castella'/>
                    </select>&nbsp; -->
                    <input type="hidden" name="idioex" value="<%=anteriorIdioex%>"/>

                    <c:choose>
                    <c:when test="${initParam['registro.entrada.view.disquete_correo']}">
                    <!-- Numero de disquete -->
                    <!--  <font class="<%=errorEn(errores,"disquet")%>"><fmt:message key='registro.num_disquete'/> </font> -->
                    <input readonly type=hidden onkeypress="return check(event)" name=disquet size="8" value="<%=reg.getDisquet()!=null?reg.getDisquet().trim():""%>">
					<!--<a href="javascript:abreDisquete()"><img src="imagenes/buscar.gif" align=middle alt="Darrer disquet" border="0"></a>-->
                    <!--Numero de disquete -->
                    &nbsp;&nbsp;
                    <!-- <font class="<%=errorEn(errores,"correo")%>"><fmt:message key='registro.num_correo'/> </font>-->
                    <input readonly onkeypress="return check(event)" type=hidden name=correo size="8" value="<%=reg.getCorreo()!=null?reg.getCorreo().trim():""%>">
                    </c:when>
                    <c:otherwise>
                    <input type="hidden" name="disquet" value=""/>
                    <input type="hidden" name="correo" value=""/>
                    </c:otherwise>
                    </c:choose>
                    
                </td>
                </tr>

                <!--  tr>
                <td style="border:0;">
                    &nbsp;<br>
                    <font class="errorcampo">*</font>
                    <font class="<%=errorEn(errores,"comentario")%>"><fmt:message key='extracte_del_document'/>:
                    <textarea cols="67" readonly onkeypress="return check(event)" rows="3" name="comentario"><%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml("Ofici de sortida de documentació amb núm. de registre " + reg.getNumeroEntrada() + "/" + reg.getAnoEntrada())%></textarea>
                </td>
                </tr -->
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
        
        <input type="hidden" name="numeroEntrada" value="<%=numeroEntrada %>"/>
        <input type="hidden" name="ano" value="<%=ano %>"/>
        <%
        String listareg = "";
        for(int i=0; listaregistros!=null && i<listaregistros.length; i++) {
        	if (i>0) listareg += "|";
        	listareg += listaregistros[i];
        } 
        %>
        <input type="hidden" name="registre" value="<%=listareg%>" />
        </form>
        </center>
        <!-- Fin Cuerpo central -->
<script type="text/javascript">
	recuperaDescripcionEntidad();
	recuperaRemitente();

	 var elFocus = document.getElementById('datasalida');
	 elFocus.focus();
</script>
<script type="text/javascript">
refrescaFisica();
</script>
        
                 
    </body>
</html> 
