<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%--
  Registro General CAIB - Visado de modificaciones
--%>

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%
    String usuario=request.getRemoteUser();
    String oficinaParm=(request.getParameter("oficina")==null) ? "0": request.getParameter("oficina");
    String registroParm=(request.getParameter("registro")==null) ? "0": request.getParameter("registro");
    String anoParm=(request.getParameter("ano")==null) ? "0": request.getParameter("ano");
    String fechaParm=(request.getParameter("fecha")==null) ? "": request.getParameter("fecha");
    String horaParm=(request.getParameter("hora")==null) ? "": request.getParameter("hora");

    RegistroModificadoSalidaFacade regmod = RegistroModificadoSalidaFacadeUtil.getHome().create();
    ParametrosRegistroModificado registroModificado = new ParametrosRegistroModificado();

    registroModificado.setOficina(Integer.parseInt(oficinaParm));
    registroModificado.setNumeroRegistro(Integer.parseInt(registroParm));
    registroModificado.setAnoSalida(Integer.parseInt(anoParm));
    registroModificado.setFechaModificacion(fechaParm);
    registroModificado.setHoraModificacion(horaParm);
    registroModificado=regmod.leer(registroModificado);
    
    if (!registroModificado.getLeido()) { 

%>
        <jsp:forward page="VisaSalidaSel.jsp">
            <jsp:param name="error" value="Modificacion no encontrada"/>
        </jsp:forward>
<%
    }
    
    String numeroRegistro=registroModificado.getNumeroRegistro()+"";
    String anoSalida=registroModificado.getAnoSalida()+"";
    String oficina=registroModificado.getOficina()+"";
    String motivo=registroModificado.getMotivo();
    String entidad1=registroModificado.getEntidad1Catalan();
    String entidad2=registroModificado.getEntidad2()+"";
    String entidadTexto="";
    String extracto=registroModificado.getExtracto();
    String remitente=registroModificado.getRemitente();

    
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    
    RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
    ParametrosRegistroSalida pregsal = new ParametrosRegistroSalida();
    ParametrosRegistroSalida registro = new ParametrosRegistroSalida();
    
    pregsal.fijaUsuario(usuario);
    pregsal.setoficina(oficina);
    pregsal.setNumeroSalida(numeroRegistro);  
    pregsal.setAnoSalida(anoSalida);
    registro=regsal.leer(pregsal);
    
    if (!registro.getLeido()) {
%>
        <jsp:forward page="VisaSalidaSel.jsp">
            <jsp:param name="error" value="Registro no encontrada"/>
        </jsp:forward>
<%
    }
   
    if (!entidad1.trim().equals("")) {
        entidadTexto=valores.recuperaRemitente(entidad1, entidad2);
    }

    String descripcionOficina=valores.recuperaDescripcionOficina(oficina);
    
    String originalRemitente=(registro.getEntidad1().trim().equals("")) ? registro.getDescripcionDestinatario() : 
            registro.getEntidad1()+" - "+registro.getEntidad2()+"  "+registro.getDescripcionDestinatario();
    String originalExtracto=registro.getComentario();

    Integer intSerie=(Integer)session.getAttribute("serie");
    
    if (intSerie==null) {
        intSerie=new Integer(0);
        session.setAttribute("serie", intSerie);
    }
%>

<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        
        <script language="javascript" src="livescript.js"></script>
        <script language="javascript" src="jscripts/TAO.js"></script>
        <script>
            function valida() {
                valor=document.registroForm.motivo.value;
                if (valor.indexOf('¤',0)>-1) {
                    alert("El símbol \"¤\" no és permès a l\'aplicació. Emprau \"euro\" o \"euros\" segons pertoqui");
                    return false;
                }

                validado=true;
                if (
                <% if (!entidad1.trim().equals("") || !remitente.trim().equals("")) {%>
                    !document.registroForm.remitente.checked
                <% } %>    
                <% if (!extracto.trim().equals("")) {%>
                            <% if (!entidad1.trim().equals("") || !remitente.trim().equals("")) {%> && <% } %>
                    !document.registroForm.extracto.checked
                <% } %>    ) {
                    //alert("S'ha de seleccionar un check");
                    var continuar=confirm("No s'ha admès cap canvi. Confirmar");
                }
                return continuar;
            }
        </script>
        <style>
            #destinatario_desc {background-color: #cccccc;}
            #remitente_desc {background-color: #cccccc;}
        </style>

    </head>

    <body bgcolor="#FFFFFF">
    <%-- Cabecera --%>
    
   	<!-- Molla pa --> 
	<ul id="mollaPa">
	<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
	<li><a href="VisaSalidaSel.jsp"><fmt:message key='seleccionar_oficina_a_visar'/></a></li>
	<li><fmt:message key='motiu_del_canvi'/></li>
	</ul>
	<!-- Fi Molla pa-->
    <%-- Cuerpo y formulario --%>
    <form name="registroForm" action="VisaSalidaPaso.jsp" method="post" onsubmit="return valida()">

    <div align="center">
        <table>
                <tr>
                    <td>
                        <b><fmt:message key='sortida_a_visar'/></b>
                        <font style="font-size:14px; font: bold;background-color: #cccccc;">
                            &nbsp;&nbsp;&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(numeroRegistro)%> &nbsp;&nbsp;/&nbsp;&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(anoSalida)%></b>
                        </font>
                    </td>
                </tr>
                <tr>
                    <td>
                        <b><fmt:message key='oficina'/>........:</b>
                        <font style="font-size:14px; font: bold;background-color: #cccccc;">
                            &nbsp;&nbsp;&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(oficina)%> &nbsp;-&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(descripcionOficina)%>
                        </font>
                    </td>
                </tr>
        </table>
    </div>
    <p>&nbsp;</p>
    <%-- Valores originales --%>
    <div>
        <table width="70%" align="center">
            <tr>
                <td colspan="2"> 
                    <b><fmt:message key='valors_originals'/></b>
                </td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <b><fmt:message key='destinatari'/> :</b>
                    &nbsp;&nbsp;
                    <c:set var="texto" scope="page"><%=originalRemitente%></c:set>  
                    <c:out escapeXml="false" value="${texto}"/>
                </td>
            </tr>
            <tr><td colspan="2">&nbsp;</td></tr>
            <tr>
                <td>&nbsp;</td>
                <td>
                    <b><fmt:message key='extracte'/> :</b>
                    &nbsp;&nbsp;
                    <c:set var="texto" scope="page"><%=originalExtracto%></c:set>  
                    <c:out escapeXml="false" value="${texto}"/>
                </td>
           </tr>
           <tr><td colspan="2">&nbsp;</td></tr>
           <tr><td colspan="2">&nbsp;</td></tr>
           <tr>
                <td colspan="2">
                    <b><fmt:message key='motiu_del_canvi'/> :</b>
                    &nbsp;&nbsp;
                    <c:set var="texto" scope="page"><%=motivo.trim()%></c:set>
                    <input onkeypress="return check(event)" type=text size="100" maxlength="150" name="motivo" value="<c:out escapeXml="true" value="${texto}"/>">
                </td>
           </tr>

        </table>
    </div>
    <p>&nbsp;</p>
    <%-- Texto informativo --%>
    <table width="70%" align="center">
        <tr>
            <td>
                <fmt:message key='seleccionar_check'/>
            </td>
        </tr>
    </table>
    <%-- formulario --%>
    <div align="center">
            <input type="hidden" name="serie" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(intSerie+"")%>">
            <input type=hidden name="oficina" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(oficinaParm)%>">
            <input type=hidden name="registro" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(registroParm)%>">
            <input type=hidden name="ano" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(anoParm)%>">
            <input type=hidden name="fecha" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(fechaParm)%>">
            <input type=hidden name="hora" value="<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(horaParm)%>">
            <table border=0 width="70%" class="bordeSalida">
                <% if (!entidad1.trim().equals("")) {%>
                    <tr>
                        <td width="5%">
                            <input type="checkbox" name="remitente" value="S">
                        </td>
                        <td align="left">
                            <b><fmt:message key='destinatari'/>:</b>
                            &nbsp;&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad1)%>&nbsp;-&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidad2)%>&nbsp;&nbsp;&nbsp;<%=es.caib.regweb.webapp.servlet.HtmlGen.toHtml(entidadTexto)%>
                        </td>
                    </tr>
                <% } %>
                <% if (!remitente.trim().equals("")) {%>
                    <tr>
                        <td width="5%">
                            <input type="checkbox" name="remitente" value="S">
                        </td>
                        <td align="left">
                            <b><fmt:message key='destinatari'/>:</b>
                           <c:set var="texto" scope="page"><%=remitente%></c:set>  
                           &nbsp;&nbsp;<c:out escapeXml="false" value="${texto}"/>
                        </td>
                    </tr>
                <% } %>
                <% if (!extracto.trim().equals("")) {%>
                    <tr>
                        <td width="5%">
                            <input type="checkbox" name="extracto" value="S">
                        </td>
                        <td align="left">
                            <b><fmt:message key='extracte'/>:</b>
                            <c:set var="texto" scope="page"><%=extracto%></c:set>  
                            &nbsp;&nbsp;<c:out escapeXml="false" value="${texto}"/>
                        </td>
                    </tr>
                <% } %>
                    <tr><td colspan="2">&nbsp;</td></tr>                
                    <tr>
                        <td colspan="2">
                            <!-- Boton de enviar -->          
                            <p align="center">
                                <input type=submit value="<fmt:message key='visar'/>">
                            </P>
                        </td>
                    </tr>
            </table>
    </div>
    </form>

		<p>&nbsp;</p>
		<p>&nbsp;</p>
                 
		
    </body>
</html>
<%


%>
