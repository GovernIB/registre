<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%--
  Registro General CAIB - Registro de Entradas
--%>
<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*,java.net.URLDecoder" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%
    ParametrosRegistroEntrada registro;
    registro=(ParametrosRegistroEntrada)request.getAttribute("registroEntrada");
    if (registro!=null) registro.setNumeroEntrada(null);
  
    String oficinaSesion = (String) session.getAttribute("oficinaSesion");
    String oficinaFisicaSesion = (String) session.getAttribute("oficinaFisicaSesion");
    if (oficinaSesion==null) oficinaSesion = "";
    if (oficinaFisicaSesion==null) oficinaFisicaSesion = "";

    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
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
    String usuario=request.getRemoteUser();
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
            	codOficina=document.registroForm.oficina.options[document.registroForm.oficina.selectedIndex].value;
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
            	codOficina=document.registroForm.oficina.options[document.registroForm.oficina.selectedIndex].value;
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
	
            function guarda_cookie() {
				var valor = "";
           		valor+=","+"oficina"+"="+document.registroForm.oficina.options[document.registroForm.oficina.selectedIndex].value;
           		valor+=","+"oficinafisica"+"="+document.registroForm.oficinafisica.options[document.registroForm.oficinafisica.selectedIndex].value;
            	valor+=","+"tipo"+"="+document.registroForm.tipo.options[document.registroForm.tipo.selectedIndex].value;
            	valor+=","+"idioma"+"="+document.registroForm.idioma.options[document.registroForm.idioma.selectedIndex].value;
            	valor+=","+"entidad1"+"="+document.registroForm.entidad1.value;
            	valor+=","+"entidad2"+"="+document.registroForm.entidad2.value;
            	valor+=","+"altres"+"="+document.registroForm.altres.value;
            	valor+=","+"balears"+"="+document.registroForm.balears.options[document.registroForm.balears.selectedIndex].value;
            	valor+=","+"fora"+"="+document.registroForm.fora.value;
            	valor+=","+"destinatari"+"="+document.registroForm.destinatari.value;
            	valor+=","+"idioex"+"="+document.registroForm.idioex.options[document.registroForm.idioex.selectedIndex].value;
            	valor+=","+"comentario"+"="+document.registroForm.comentario.value;
				
				var cookie="";
				
				//Lo codificamos dos veces. Una para pasarlo como parámetro y otra para guardarlo en la bbdd
				//El motivo es que el jsp de destino lo descodifica una vez de forma automática.
            	cookie+=URLEncode(URLEncode(valor));
                
            	miVentana=open("popup/grabaRepro.jsp?tipusCookie=RWE&valorCookieAgrabar="+cookie,"GravarRepro","scrollbars,resizable,width=400,height=400");
            	miVentana.focus();
            }
            
            function borra_cookie() {
           		miVentana=open("popup/borraRepro.jsp?tipusCookie=RWE","EsborrarRepro","scrollbars,resizable,width=400,height=400");
            	miVentana.focus();
            }

            function lee_cookie() {
            	miVentana2=open("popup/leeRepro.jsp?tipusCookie=RWE","LlegirRepro","scrollbars,resizable,width=400,height=400");
            	miVentana2.focus();
            }
            
            function importar_cookie() {
            	if (CheckIsIE()) { 
            		miVentana3=open("popup/importarRepro.jsp?tipusCookie=RWE","ImportarRepro","scrollbars,resizable,width=450,height=250");
            	} else {
            		miVentana3=open("popup/importarRepro.jsp?tipusCookie=RWE","ImportarRepro","scrollbars,resizable,width=400,height=200");                
            	}
            	miVentana3.focus();
            }

            function leerCookie(Repo) {
	            nodos = URLDecode(Repo);
	            nodos=nodos.split(",");
           	 	for (j=0; j < nodos.length; j++) {
		   	         valor = new Array();
		   	         if (nodos[j]!="") {
		   	 	        valor=nodos[j].split("=");
          			 }
	       	     	 if (valor.length>0) { // ejecutar
       					 if (valor[0]!="oficina" && valor[0]!="oficinafisica" && valor[0]!="tipo" && valor[0]!="idioma" && valor[0]!="entidad1" 
				                && valor[0]!="entidad2" && valor[0]!="altres" && valor[0]!="balears" && valor[0]!="fora"
				                && valor[0]!="destinatari" && valor[0]!="idioex" && valor[0]!="comentario") {
				                document.registroForm.comentario.value+=","+valor[0];
		   	             } else {
  		       	 				document.registroForm[valor[0]].value=valor[1];
				         }
				         if (valor[0]=="oficina") refrescaFisica();
      	    	     }
        	    }
            	recuperaDescripcionEntidad();
	            recuperaDestinatario();
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
		<li><fmt:message key='registre_entrades'/></li>
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

        <% Hashtable errores = (registro==null)? new Hashtable(): registro.getErrores();
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
        <table border="0" width="599">
            <tr>
                <td>
                    <font class="errorcampo">*</font>&nbsp;<fmt:message key='registro.campos_obligatorios'/>
                </td>
                <td width="5%">
                    <a href="javascript:guarda_cookie();"><img src="imagenes/grabarMacro.gif" border="0" alt="Desar Repro" title="Desar Repro"></a>
                </td>
                <td width="5%" align="center">
                    <a href="javascript:borra_cookie();"><img src="imagenes/borrarepro.gif" border="0" alt="Esborrar Repro" title="Esborrar Repro"></a>
                </td>
                <td  width="5%">
                    <a href="javascript:lee_cookie();"><img src="imagenes/repro.gif"  alt="Llegir Repro" border="0" title="Llegir Repro"></a>
                </td>
                <td  width="5%">
                    <a href="<c:url value='/repro/expImpRepro.jsp?tipusCookie=RWE' />"><img src="imagenes/exportar.gif"  alt="Exportar Repro" border="0" title="Exportar Repro"></a>
                </td>
                <td  width="5%">
                    <a href="javascript:importar_cookie();"><img src="imagenes/importar.gif"  alt="<fmt:message key='importar_repro'/>" border="0" title="<fmt:message key='importar_repro'/>"></a>
                </td>
            </tr>
        </table>
        <form name="registroForm" id="registroForm" action="paso.jsp" method="post" onsubmit="return confirmaProceso()">
        <input type="hidden" name="serie" value="<%=intSerie%>">
        <table class="recuadroEntradas" width="599">
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
                                <select name="oficina" id="oficina" onchange="refrescaFisica()">
                                    <% escribeSelect(out, "S", valores.buscarOficinas(usuario, "AE"), (registro==null)?oficinaSesion:registro.getOficina()); %>
                                </select>
                            </td>
                            <td style="border:0">
                                <!-- Despegable para oficinas autorizadas para el usuario -->
                                &nbsp;<br><font class="<%= errorEn(errores,"oficinafisica")%>"><fmt:message key='oficina_fisica'/>:</font>
                                <select name="oficinafisica" id="oficinafisica">
                                    <%  escribeSelect2(out, "N", valores.buscarOficinasFisicas(usuario, "AE"), (registro==null)?oficinaFisicaSesion:registro.getOficinafisica(), (registro==null)?oficinaSesion:registro.getOficina()); %>
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
                        <select name="tipo" size="1" style="width: 250px">
                            <% escribeSelect(out, "N", valores.buscarDocumentos(), (registro==null)? "":registro.getTipo()); %>
                        </select>
                        <!-- Despegable para Idiomas -->
                        <font class="<%=errorEn(errores,"idioma")%>"><fmt:message key='registro.idioma'/></font>
                        <select name="idioma" size="1">
                            <% escribeSelect(out, "N", valores.buscarIdiomas(), (registro==null)? "":registro.getIdioma()); %>
                        </select>
                    </td>
                </tr>
                <!-- 3ª fila de la tabla -->
                <tr>
                <!-- Remitente -->
                <td style="border:0;" width="55%">
                <br><font class="errorcampo">*</font>
                <fmt:message key='remitent'/>........<font class="<%=errorEn(errores,"entidad1")%>"><fmt:message key='registro.entidad'/></font>
                <!-- Remitente Entidad 1 -->
                <input id="enti" type=text name=entidad1 size="7" value="<%=(registro==null)? "":registro.getEntidad1()%>" onblur="recuperaDescripcionEntidad()">
                <!-- Remitente Entidad 2 -->
                <input type=text name=entidad2 size="3" value="<%=(registro==null)? "":registro.getEntidad2()%>" onblur="recuperaDescripcionEntidad()">
                <a href="javascript:abreRemitentes()"><img border="0" src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>"></a>
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
                            <% escribeSelect(out, "N", valores.buscarBaleares(), (registro==null)? "":registro.getBalears()); %>
                            </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0;">&nbsp;</td>
                            <td style="border:0;" valign="bottom" colspan="2">
                                <fmt:message key='registro.fuera_baleares'/>&nbsp;
                                <input onkeypress="return check(event)" type=text name=fora size="25" maxlength="25" value="<%=(registro==null)? "":registro.getFora().trim()%>">            
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
                    <input onKeyPress="return goodchars(event,'0123456789')" type=text name=salida1 maxlength="6" size="6" value="<%=(registro==null)? "":registro.getSalida1()%>">&nbsp;&nbsp;/&nbsp; 
                    <input onKeyPress="return goodchars(event,'0123456789')" type=text name=salida2 maxlength="4" size="4" value="<%=(registro==null)? "":registro.getSalida2()%>">
                </td>
                </tr> 
                <!-- 8ª fila de la tabla -->
                <tr>
                    <td style="border:0;">
                    <!-- Organismo destinatario -->
                    &nbsp;<br><font class="errorcampo">*</font><font class="<%=errorEn(errores,"destinatari")%>"><fmt:message key='registro.organismo_destinatario'/>..............:</font>
                    <input id="desti" type=text name=destinatari size="4"  maxlength="4" value="<%=(registro==null)? "":registro.getDestinatari()%>" onblur="recuperaDestinatario()">
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
                        <select name =idioex    >
                            <% String anteriorIdioex=(registro==null)? "":registro.getIdioex(); %>
                            <option value="2" <%=anteriorIdioex.equals("2") ? "selected" : "" %> > <fmt:message key='registro.idioma.catala'/>
                            <option value="1" <%=anteriorIdioex.equals("1") ? "selected" : "" %> > <fmt:message key='registro.idioma.castella'/>
                        </select>&nbsp;
                        
                        <c:choose>
                        <c:when test="${initParam['registro.entrada.view.disquete_correo']}">
                        <!--Numero de disquete -->
                        <font class="<%=errorEn(errores,"disquet")%>"><fmt:message key='registro.num_disquete'/> </font>
                        <input onkeypress="return check(event)" type=text name=disquet size="8" value="<%=(registro==null)? "":registro.getDisquet().trim()%>">
                        <a href="javascript:abreDisquete()"><img src="imagenes/buscar.gif" align=middle alt="Darrer disquet" border="0"></a>
                        <!--Numero de disquete -->
                        &nbsp;&nbsp;
                        <font class="<%=errorEn(errores,"correo")%>"><fmt:message key='registro.num_correo'/> </font>
                        <input id="corr" onkeypress="return check(event)" type=text name=correo size="8" value="<%=(registro==null)? "":registro.getCorreo().trim()%>">
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
                        <textarea cols="67" onkeypress="return check(event)" rows="3" name="comentario"><%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml((registro==null) ? "" : registro.getComentario())%></textarea>
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
        </form>
        </center>  
        </div>
        <!-- Fin Cuerpo central -->
<script type="text/javascript">
	 var elFocus = document.getElementById('dataentrada');
	 elFocus.focus();
</script>
<script type="text/javascript">
refrescaFisica();
</script>

        
                 
    </body>
</html> 
