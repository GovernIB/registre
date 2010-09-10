<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
  Registro General CAIB - Modificacion del Registro de Salidas
--%>

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%
    String usuario=request.getRemoteUser(); 
    String hora="";
    String hhmm="";
    String numeroSalida="";
    String ano="";
    String codOficina="";
    String comentarioAnterior="";
    String entidad1Anterior="";
    String entidad2Anterior="";
    String altresAnterior="";
    ParametrosRegistroSalida registro;
    String motivo="";
    String entidad1="";
    String entidad2="";
    String altres="";
    String comentario="";

    if (request.getAttribute("registroSalida")!=null) {//Viene de error
        registro=(ParametrosRegistroSalida)request.getAttribute("registroSalida");
        comentarioAnterior=registro.getComentario();
        entidad1Anterior=registro.getEntidad1();
        entidad2Anterior=(entidad1Anterior.trim().equals("")) ? "" : registro.getEntidad2();
        altresAnterior=registro.getAltres();
        motivo=registro.getMotivo().trim();
        if (motivo.equals("")) {
            comentario=registro.getComentario();
            entidad1=registro.getEntidad1();
            entidad2=registro.getEntidad2();
            altres=registro.getAltres();
        } else {
            comentario=registro.getComentarioNuevo();
            entidad1=registro.getEntidad1Nuevo();
            entidad2=registro.getEntidad2Nuevo();
            altres=registro.getAltresNuevo();
        }
        ano=registro.getAnoSalida();
        codOficina=registro.getOficina();
        numeroSalida=registro.getNumeroSalida();
        hhmm=registro.getHora();

    } else {
        numeroSalida=(request.getParameter("numero")==null) ? "": request.getParameter("numero");
        codOficina=(request.getParameter("oficina")==null) ? "" :request.getParameter("oficina");
        ano=(request.getParameter("any")==null) ? "" : request.getParameter("any");
        javax.naming.InitialContext contexto = new javax.naming.InitialContext();

        RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
        ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
        registro = new ParametrosRegistroSalida();

        pregsal.fijaUsuario(usuario);
        pregsal.setoficina(codOficina);
        pregsal.setNumeroSalida(numeroSalida);  
        pregsal.setAnoSalida(ano);
        registro=regsal.leer(pregsal);

        if (!registro.getLeido()) { 

%>
        <jsp:forward page="ModiSalidaClave.jsp">
            <jsp:param name="error" value="S"/>
            <jsp:param name="numero" value="<%=numeroSalida%>"/>
            <jsp:param name="ano" value="<%=ano%>"/>
        </jsp:forward>
<%
        }
        comentarioAnterior=registro.getComentario().trim();
        altresAnterior=registro.getAltres().trim();
        entidad1Anterior=registro.getEntidad1();
        entidad2Anterior=(entidad1Anterior.trim().equals("")) ? "" : registro.getEntidad2();
        hora=registro.getHora();
        entidad1=registro.getEntidad1();
        entidad2=registro.getEntidad2();
        altres=registro.getAltres();
        comentario=registro.getComentario();

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
       antics debut a que no tenien hora de entrada/sortida */
        //if (hora.length()<4) {hora="0"+hora;}
        //hhmm=hora.substring(0,2)+":"+hora.substring(2,4);
    }

    javax.naming.InitialContext contexto = new javax.naming.InitialContext();
    
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    

    if (entidad1.equals("") && entidad2.equals("0")) { entidad2="";}

    Integer intSerie=(Integer)session.getAttribute("serie");
    
    if (intSerie==null) {
        intSerie=new Integer(0);
        session.setAttribute("serie", intSerie);
    }

   String fechaSalida=registro.getDataSalida();
   String fechaVisado="";
   if (registro.getDataVisado()!=null && !registro.getDataVisado().trim().equals("0")) {
       fechaVisado=registro.getDataVisado();
   }
   request.setAttribute("registro",registro);
%>

<%! 
    String errorEn(Hashtable errores, String campo) {
        return (errores.containsKey(campo))? "errorcampo" : "";
    }

    void escribeSelect(javax.servlet.jsp.JspWriter out, Vector valores, String referencia) throws java.io.IOException {
        
        for (int i=0;i<valores.size();i=i+2){
                String codigo=valores.get(i).toString();
                String descripcion=valores.get(i+1).toString();
                out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
                out.write(descripcion);
                out.write("</option>\n");
        }
    }
%>
<!-- Exploter no renderitza bé.   -->
<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        
        <script language="javascript" src="livescript.js"></script>
        <script language="javascript" src="jscripts/TAO.js"></script>
        <script>
            
            comentarioAnterior="<%=es.caib.regweb.webapp.servlet.HtmlGen.toJavascript(comentarioAnterior)%>";
            entidad1Anterior="<%=es.caib.regweb.webapp.servlet.HtmlGen.toJavascript(entidad1Anterior)%>";
            entidad2Anterior="<%=es.caib.regweb.webapp.servlet.HtmlGen.toJavascript(entidad2Anterior)%>";
            altresAnterior="<%=es.caib.regweb.webapp.servlet.HtmlGen.toJavascript(altresAnterior)%>";
            <% if (registro.getTipo().equals("DU")) {%>
            pedirMotivo=false;
            <% } else { %>
            pedirMotivo=<%=(registro.getDataSalida().equals(valores.getFecha())) ? "false" : "true"%>
            <% } %>
            confirmandoProceso=false;
            contador=0;
            remitenteTexto="";
            
            function volverAtras() {
                contador=0;
                motivo = document.getElementById("idMotivo");
                motivo.style.display = 'none';                  
                cuerpo = document.getElementById("idCuerpo");
                cuerpo.style.display = 'block';
                confirmandoProceso=false;                
            }
            
            function confirmaProceso() {
            valor=document.registroForm.comentario.value;
            valor1=document.registroForm.altres.value;
            valor2=document.registroForm.fora.value;
            valor3=document.registroForm.correo.value;
            valor4=document.registroForm.disquet.value;
            valor5=document.registroForm.motivo.value;
			if (valor.indexOf('¤',0)>-1 || valor1.indexOf('¤',0)>-1 || valor2.indexOf('¤',0)>-1 || valor3.indexOf('¤',0)>-1 || valor4.indexOf('¤',0)>-1 || valor5.indexOf('¤',0)>-1) {
                alert("El símbol \"¤\" no és permès a l\'aplicació. Emprau \"euro\" o \"euros\" segons pertoqui");
                return false;
            }

                valor=document.registroForm.comentario.value;
                if (valor.indexOf('¤',0)>-1) {
                    alert("El símbol \"¤\" no és permès a l\'aplicació. Emprau \"euro\" o \"euros\" segons pertoqui");
                    return false;
                }
                
                if ( trim(entidad1Anterior)!=trim(document.registroForm.entidad1.value) || trim(entidad2Anterior)!=trim(document.registroForm.entidad2.value) ||
                     trim(altresAnterior)!=trim(document.registroForm.altres.value) || trim(comentarioAnterior)!=trim(document.registroForm.comentario.value) ) {
                        hayCambio=true;
                } else {
                        hayCambio=false;
                }
                
                motivoTexto=trim(document.registroForm.motivo.value);
                
                if ((pedirMotivo && hayCambio && motivoTexto=="") || (pedirMotivo && contador==0 && motivoTexto!="" && hayCambio)) {
                  
                  cambiaTexto(document.registroForm.comentario.value, 'extracteNou');
                  
                  cambiaTexto(document.registroForm.entidad1.value+" "+document.registroForm.entidad2.value+" ", 'entidadActual');
                  
                  if (trim(document.registroForm.entidad1.value)=="" && (trim(document.registroForm.entidad2.value)=="0" || trim(document.registroForm.entidad2.value)=="")) {
                        cambiaTexto('', 'remitenteActual');
                  } else {
                        recuperaDescripcionEntidadX(document.registroForm.entidad1.value,document.registroForm.entidad2.value, 'remitenteActual');
                  }
                  
                  cambiaTexto(document.registroForm.altres.value, 'remitenteAltresActual');
                  
                  motivo = document.getElementById("idMotivo");
                  motivo.style.display = 'block';                  
                  cuerpo = document.getElementById("idCuerpo");
                  cuerpo.style.display = 'none';
                  contador=1;
                  return false;
                }
                else {
                  confirmandoProceso=true;
                  return true;
                }
            }
            
            function cargaDatos() {
            if (!document.registroForm.entidad1.value=="") {
            recuperaDescripcionEntidad();
            }
            if (!document.registroForm.remitent.value=="") {
            recuperaDestinatario();
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

            function setRemitent(cod, descod) {
           document.registroForm.remitent.value=cod;
            cambiaTexto(descod, 'destinatario_desc');
            }

            function setPersona(persona) {
           document.registroForm.altres.value=persona;
            }

            function abreDestinatarios() {
            codOficina=document.registroForm.oficina.value;
            miVentana=open("popup/destinatariosSalida.jsp?oficina="+codOficina,"destinatarios","scrollbars,resizable,width=300,height=200");
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
            codOficina=document.registroForm.oficina.value;
            fentrada=document.registroForm.datasalida.value;
            miDisquete=open("popup/disquete.jsp?oficina="+codOficina+"&tipo=S&fEntrada="+fentrada,"disquete","scrollbars,resizable,width=250,height=150");
            miDisquete.focus();
            }

            function recuperaDestinatario() {
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
            remitenteTexto=value1;
            cambiaTexto(value1, 'remitente_desc');
            };
            context.onerror = function(message) {
            alert("Error: " + message);
            };
            context.invoke(entidad1, entidad2);
            }
            }

            function recuperaDescripcionEntidadX(entidad1, entidad2, id) {
            if (entidad1 != null) {
            var RECUPERAVALOR_PATH = 'RecuperaDescripcionRemitente';
            var context = new InvocationContext(RECUPERAVALOR_PATH);
            context.onresult = function(value) {
            var re = new RegExp ("\\+", 'gi') ;
            value1=value.replace(re, ' ');
            cambiaTexto(value1, id);
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

    <body bgcolor="#FFFFFF" onunload="cerrarVentana()" onload="cargaDatos()">

      
        
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="ModiSalidaClave.jsp"><fmt:message key='modificacio_de_sortides'/></a></li>
		<li><fmt:message key='modificacio_registre_sortida'/></li>
		</ul>
		<!-- Fi Molla pa-->
<!--        <p>
        <center>
        <font class="titulo">
            Usuari : <%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(usuario)%>
        </font>
        </center>
        &nbsp;<p>-->
        <div align="center">  
        <!-- Mostramos Errores si los hubiera -->

<% Hashtable errores = (registro==null)?new Hashtable():registro.getErrores();
    if (errores.size() > 0) {%>
        <table class="recuadroErrors" width="610" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/></p>
                    <ul>
  <%      for (Enumeration e=errores.elements();e.hasMoreElements();) { %>
                        <li><%= es.caib.regweb.webapp.servlet.HtmlGen.toHtml(e.nextElement()+"")%></li>
<%}%>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
   <%  } %>
    <%
       if (Helper.estaPdteVisado("S", registro.getOficina(), registro.getAnoSalida(), registro.getNumeroSalida())) {
           %>
           <script>
                alert("<fmt:message key='avis_modificacio_pendent_visat'/>");
           </script>
      <% } %>

        <!-- Cuerpo central -->

        <center>
        <table border="0" width="610">
            <tr>
                <td>
                    <font class="errorcampo">*</font>&nbsp;<fmt:message key='registro.campos_obligatorios'/>
                </td>
            </tr>
        </table>

        <form name="registroForm" action="ModiSalidaPaso.jsp" method="post" onsubmit="return confirmaProceso()">
        
        <div id="idCuerpo" style="display:block">
            <table class="recuadroSalidas" width="610">
            <input type="hidden" name="serie" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(intSerie+"")%>">
            <input type="hidden" name="anoSalida" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(ano)%>">
            <input type="hidden" name="entidad1Anterior" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad1Anterior)%>">
            <input type="hidden" name="entidad2Anterior" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad2Anterior)%>">
            <input type="hidden" name="altresAnterior" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(altresAnterior)%>">
            <input type="hidden" name="comentarioAnterior" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(comentarioAnterior)%>">
            <input type="hidden" name="numeroRegistro" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(registro.getNumeroSalida())%>">
            <tr>
                <td>
                    <!-- Tabla para datos de cabecera -->
                    <table class="bordeSalida" style="border:0">
                        <tr>
                            <td style="border:0">
                                <!-- Data d'entrada -->
                                <font class="<%=errorEn(errores,"dataentrada")%>"><fmt:message key='registro.fecha_salida'/></font>
                                 <%String anteriorDataSalida=(registro==null)? "":registro.getDataSalida();%>
                                <input type="hidden" name="datasalida" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(anteriorDataSalida)%>">
                                <input readonly type=text name=NNdatasalida value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(fechaSalida)%>" size="10" >
                            </td>
                            <td style="border:0">
                                <font class="<%=errorEn(errores,"hora")%>"><fmt:message key='registro.hora'/></font>
                                <input type=text name=hora value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(hhmm)%>" size="5">
                            </td>
                            <td style="border:0">
                                <font class="<%=errorEn(errores,"suprimir")%>"><fmt:message key='sortida_anulada'/>:</font>
                                <select name=suprimir>
                    <% String suprimir=(registro==null)? "":registro.getRegistroAnulado(); %>
                                    <option value="S" <%=suprimir.equals("S") ? "selected" : "" %> > S
                                    <option value=" " <%=suprimir.equals(" ") || suprimir.equals("") ? "selected" : "" %> > 
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0">
                                <font class="<%= errorEn(errores,"oficina")%>"><fmt:message key='oficina'/>:</font>
                                <c:set var="texto" scope="page"><%=valores.recuperaDescripcionOficina(registro.getOficina())%></c:set>
                                <c:set var="texto2" scope="page"><%=registro.getDescripcionOficinaFisica()%></c:set>
                                <font style="background-color: #DEDEDE; font-size: 14px;">&nbsp;<c:out escapeXml="false" value="${texto}"/>&nbsp;-&nbsp;<c:out escapeXml="false" value="${texto2}"/>&nbsp;</font>
                                <%-- <input type="text" name="desOficina" readonly value="<%=valores.recuperaDescripcionOficina(registro.getOficina())%>"> --%>
                                <input type="hidden" name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(registro.getOficina())%>">
                                <input type="hidden" name="oficinafisica" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(registro.getOficinafisica())%>">
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </td>
                            <td style="border:0">
                               <%
                                    String registroAno=registro.getNumeroSalida()+"/"+registro.getAnoSalida();
                                %>
                                <fmt:message key='num_registre'/> <input type="text" size="<%=registroAno.length()%>" maxlength="<%=registroAno.length()%>" name="numeroRegistro" readonly value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(registro.getNumeroSalida()+"/"+registro.getAnoSalida())%>">
                                &nbsp;&nbsp;&nbsp;
                            </td>
                            <td style="border:0">
                                <fmt:message key='data_registre'/> <input readonly type=text maxlength="10" name=NNXdataentrada value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(fechaVisado)%>" size="10">
                            </td>
                        </tr>
                    </table>
                    <!-- De la tabla principal -->
                </td>
            </tr>
            <tr>
            <td>
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
                &nbsp;<br>
                    <font class="<%= errorEn(errores,"data") %>"><fmt:message key='registro.fecha'/></font>
          <% String anteriorData=(registro==null)? "":registro.getData(); %>
                    <input type=text name=data value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(anteriorData.equals("") ? valores.getFecha() : anteriorData)%>" size="10" > 

                    <!-- Despegable para Tipos de documentos -->
                    &nbsp;<font class="errorcampo">*</font>
          <%-- Tipo de documento. Cuando sea DU no se saca desplegable y no se puede modificar el campo --%>          
                    <font class="<%=errorEn(errores, "tipo")%>"><fmt:message key='registro.tipo'/> </font>
          <% if (registro.getTipo().equals("DU")) {%>
                    <input type="hidden" name="tipo" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(registro.getTipo())%>">
                    <font style="font-size:12px; font: bold; background-color: #cccccc;">
                        <%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(valores.recuperarTipoDocumento(registro.getTipo()))%>
                    </font>
                    &nbsp;&nbsp;&nbsp;
          <% } else { %>
                    <select name="tipo" size="1">
                <% escribeSelect(out, valores.buscarDocumentos(), (registro==null)? "":registro.getTipo()); %>
                    </select>
          <% } %>          
                    
                    <!-- Despegable para Idiomas -->
                    <font class="<%=errorEn(errores,"idioma")%>"><fmt:message key='registro.idioma'/></font>
                    <select name=idioma size=1>
                <% escribeSelect(out, valores.buscarIdiomas(), (registro==null)? "":registro.getIdioma()); %>
                    </select>
                </td>
            </tr>
            <!-- 3ª fila de la tabla -->
            <tr>
            <!-- Remitente -->
            <td style="border:0;" width="55%">
            <br><font class="errorcampo">*</font>
            <fmt:message key='destinatari'/>..<font class="<%=errorEn(errores,"entidad1")%>"><fmt:message key='registro.entidad'/></font>
            <!-- Remitente Entidad 1 -->
            <input type=text name=entidad1 size="7" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad1)%>" onblur="recuperaDescripcionEntidad()">
            <!-- Remitente Entidad 2 -->
            <input type=text name=entidad2 size="3" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad2)%>" onblur="recuperaDescripcionEntidad()">
            <a href="javascript:abreRemitentes()">
                <img border="0" src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>">
            </a>
            </td>
            <!-- Descipcion del Remitente  -->
            <td width="45%" style="border:0;">
                <div id="remitente_desc" style="font-size:12px; font: bold; "></div>
            </td>
            </tr>
            <!-- 4ª fila de la tabla -->
            <tr>
            <td style="border:0;" colspan="2">
            <!-- Remitente Altres entidades -->
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            &nbsp;&nbsp;&nbsp;&nbsp;
            <fmt:message key='altres'/>&nbsp;&nbsp;<input onkeypress="return check(event)" type=text name=altres maxlength="30" size="30" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(altres.trim())%>">
            <%--<a href="javascript:abreBDP()">
            <img border="0" src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>">
            </a>--%>
            </td>
            </tr>
             <tr>
                   <td style="border:0" colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td style="border:0" colspan="2">
                    <table>
                        <tr>
                            <td style="border:0" valign="middle">
                                <!-- Procedencia geografica -->
                                <font class="errorcampo">*</font>
                                <fmt:message key='registro.destino_geografico'/>.........
                            </td>
                            <td style="border:0" valign="middle">
                                <!-- Despegable para la Procedencia Geografica de Baleares -->
                                <span class="<%=errorEn(errores,"balears")%>"> <fmt:message key='registro.baleares'/></span>
                            </td>
                            <td style="border:0">
                                <select name=balears>
                                    <% escribeSelect(out, valores.buscarBaleares(), (registro==null)? "":registro.getBalears()); %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td style="border:0">&nbsp;</td>
                            <td style="border:0" valign="bottom" colspan="2">
                                <fmt:message key='registro.fuera_baleares'/>&nbsp;
                                <input onkeypress="return check(event)" type=text name=fora size="25" maxlength="25" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml((registro==null)? "":registro.getFora().trim())%>">            
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!-- 7ª fila de la tabla -->
            <tr>
                <td style="border:0;" colspan="2">
                    <!-- Numero de salida -->
                    <%
                        String entrada1=(registro==null)? "": (registro.getEntrada1().equals("0")) ? "" : registro.getEntrada1();
                        String entrada2=(registro==null)? "": (registro.getEntrada2().equals("0")) ? "" : registro.getEntrada2();
                    %>
                    &nbsp;<br><font class="<%=errorEn(errores,"entrada1")%>"><fmt:message key='registro.num_entrada'/></font>
                    <input onKeyPress="return goodchars(event,'0123456789')" type=text name=entrada1 maxlength="6" size="6" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entrada1)%>">&nbsp;&nbsp;/&nbsp; 
                    <input onKeyPress="return goodchars(event,'0123456789')" type=text name=entrada2 maxlength="4" size="4" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entrada2)%>">
                </td>
                </tr> 
                <!-- 8ª fila de la tabla -->
                <tr>
                    <td style="border:0;">
                    <!-- Organismo destinatario -->
                    &nbsp;<br><font class="errorcampo">*</font><font class="<%=errorEn(errores,"remitent")%>"><fmt:message key='registro.organismo_emisor'/>..............:</font>
                    <input type=text name=remitent size="4" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml((registro==null)? "":registro.getRemitent())%>" onblur="recuperaDestinatario()">
                    <a href="javascript:abreDestinatarios()">
                        <img src="imagenes/buscar.gif" align=middle alt="<fmt:message key='cercar'/>" border="0">
                    </a>
                    </td>
                    <td style="border:0">
                    	<div id="destinatario_desc" style="font-size:12px; font: bold;"></div>
                    </td>
                </tr>
                    </table>
                </td>
            </tr>
            <tr>
            <td>
            <!-- tabla de datos del Extracto -->
            <table class="bordeSalida" style="border:0">
            <tr>
                <td style="border:0;">
                    &nbsp;<br><b><fmt:message key='dades_de_lextracte'/></b>
                    </td>
                    </tr>
                    <tr>
                        <td style="border:0;">
                            <!-- Idioma del Extracto -->
                            &nbsp;<br><font class="<%=errorEn(errores,"idioex")%>"><fmt:message key='registro.idioma'/></font>
          <c:set var="anteriorIdioex" value="${registro.idioex}" />
          <c:set var="idioText"><fmt:message key='registro.idioma.castella'/></c:set>
          <c:if test="${anteriorIdioex eq '2'}">
            <c:set var="idioText"><fmt:message key='registro.idioma.catala'/></c:set>
          </c:if>
                            <input type="hidden" name="idioex" value="<c:out value='${anteriorIdioex}' />">
                            <input readonly type="text" name="idioexText" value="<c:out value='${idioText}' />" size="8">
                            &nbsp;

                            <c:choose>
                            <c:when test="${initParam['registro.entrada.view.disquete_correo']}">
                            <!--Numero de disquete -->
                            <font class="<%=errorEn(errores,"disquet")%>"><fmt:message key='registro.num_disquete'/> </font>
                            <input onkeypress="return check(event)" type=text name=disquet size="8" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml((registro==null)? "":registro.getDisquet().trim())%>">
                            <a href="javascript:abreDisquete()"><img src="imagenes/buscar.gif" align=middle alt="Darrer disquet" border="0"></a>
                            <!--Numero de disquete -->
                            &nbsp;&nbsp;
                            <font class="<%=errorEn(errores,"correo")%>"><fmt:message key='registro.num_correo'/> </font>
                            <input onkeypress="return check(event)" type=text name=correo size="8" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml((registro==null)? "":(registro.getCorreo()==null) ? "": registro.getCorreo().trim())%>">
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
                            <textarea cols="70" onkeypress="return check(event)" rows="3" name="comentario"><%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(comentario.trim())%></textarea>
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
        </div>
        
        <%-- Div para pedir motivo para cambios de remitentes o comentario --%>
        
        <div id="idMotivo" style="display:none">
            <table border=0 width="599">
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>
                        <b><fmt:message key='valor_inicial'/></b>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <fmt:message key='destinatari'/>:
                                </td>

                        <%
                            if (altresAnterior.trim().equals("")) { 
                        %>  
                                <td>
                                    <font style="font-size:14px; font: bold;">
                                        &nbsp;&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad1Anterior)%>&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad2Anterior)%>&nbsp;
                                    </font>
                                </td>
                                <td>
                                    <script>recuperaDescripcionEntidadX('<%=es.caib.regweb.webapp.servlet.HtmlGen.toJavascript(entidad1Anterior)%>','<%=es.caib.regweb.webapp.servlet.HtmlGen.toJavascript(entidad2Anterior)%>', 'entidadTextoAnterior')</script>
                                    <div id="entidadTextoAnterior" style="font-size:14px; font: bold;background-color: #cccccc;"></div>
                                </td>
                        <% } else {%>
                                <td>
                                    &nbsp;&nbsp;
                                    <font style="font-size:14px; font: bold;background-color: #cccccc;">
                                    <%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(altresAnterior)%>
                                    </font>
                                </td>
                        <% } %>
                        </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <fmt:message key='extracte'/>:
                        <div style="font-size:14px; font: bold;background-color: #cccccc;">
                            <%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(comentarioAnterior)%>
                        </div>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>
                        <b><fmt:message key='valor_final'/></b>
                    </td>
                </tr>
                
                <tr>
                    <td> 
                        <table>
                            <tr>
                                <td>
                                    <fmt:message key='destinatari'/>
                                </td>
                                <td>
                                    <fmt:message key='registro.entidad'/>
                                </td>
                                <td>
                                    <div id="entidadActual" style="font-size:14px; font: bold;"></div>
                                </td>
                                <td>
                                    <div id="remitenteActual" style="font-size:14px; font: bold;background-color: #cccccc;"></div>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    &nbsp;
                                </td>
                                <td>
                                    <fmt:message key='altres'/>
                                </td>
                                <td colspan="2">
                                    <div id="remitenteAltresActual" style="font-size:14px; font: bold;background-color: #cccccc;"></div>
                                </td>
                        </table>
                    </td>
                </tr>
                
                <tr>
                    <td>
                        <fmt:message key='extracte'/>:
                        <div id="extracteNou" style="font-size:14px; font: bold;background-color: #cccccc;"></div>
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr><td>&nbsp;</td></tr>
                <tr>
                    <td>
                        <font class="errorcampo">*</font>
                        <fmt:message key='motiu_del_canvi'/> :
                        <input onkeypress="return check(event)" type=text name=motivo size="100" maxlength="150" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(motivo)%>">
                    </td>
                </tr>
                <tr><td>&nbsp;</td></tr>                
                <tr>
                  <td style="border:0">
                            <!-- Boton de enviar -->          
                        <p align="center">
                            <input type=button value="<fmt:message key='tornar'/>" onclick="volverAtras()">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type=submit value="<fmt:message key='enviar'/>">
                            </P>
                        </td>
                    </tr>
            </table>
        </div>
        </form>
        </center>
		</div>
        <!-- Fin Cuerpo central -->
		
                 
		
    </body>
</html> 
