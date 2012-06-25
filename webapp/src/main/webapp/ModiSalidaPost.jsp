<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!--
  Registro General CAIB - Registro de Salidas
-->
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%

RegistroSalidaFacade regsal = RegistroSalidaFacadeUtil.getHome().create();
ParametrosRegistroSalida registro = new ParametrosRegistroSalida();

ValoresFacade valores = ValoresFacadeUtil.getHome().create();

Integer intSerie=(Integer)session.getAttribute("serie");
if (intSerie==null) {
    intSerie=new Integer(0);
    session.setAttribute("serie", intSerie);
}
int serie=intSerie.intValue();
int serieForm = Integer.parseInt(request.getParameter("serie"));

if (serie>serieForm) {
    session.setAttribute("errorAtras","1");
%>
    <jsp:forward page="ModiSalida.jsp" />
       <% }

serie++;
// intSerie++;
intSerie=Integer.valueOf(String.valueOf(serie));
session.setAttribute("serie", intSerie);
session.removeAttribute("errorAtras");

String comentarioAnterior=(request.getParameter("comentarioAnterior")==null) ? "" : request.getParameter("comentarioAnterior").trim();
String entidad1Anterior=(request.getParameter("entidad1Anterior")==null) ? "" : request.getParameter("entidad1Anterior");
String entidad2Anterior=(request.getParameter("entidad2Anterior")==null) ? "" : request.getParameter("entidad2Anterior");
String altresAnterior=(request.getParameter("altresAnterior")==null) ? "" : request.getParameter("altresAnterior").trim();
String motivo=(request.getParameter("motivo")==null) ? "" : request.getParameter("motivo").trim();

String usuario=request.getRemoteUser().toUpperCase();
registro.fijaUsuario(usuario);

// Primary Keys
registro.setAnoSalida(request.getParameter("anoSalida"));
registro.setNumeroSalida(request.getParameter("numeroRegistro"));
registro.setoficina(request.getParameter("oficina"));
// Resto de campos
registro.setoficinafisica(request.getParameter("oficinafisica"));
registro.setCorreo(request.getParameter("correo"));
registro.setdatasalida(request.getParameter("datasalida"));
registro.sethora(request.getParameter("hora"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setentrada1(request.getParameter("entrada1"));
registro.setentrada2(request.getParameter("entrada2"));
registro.setremitent(request.getParameter("remitent"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
registro.setRegistroAnulado(request.getParameter("suprimir"));
registro.setEmailRemitent(request.getParameter("emailRemitente"));
registro.setActualizacion(true);

registro.setMotivo(motivo);
if (motivo.equals("")) {
    registro.setentidad1(request.getParameter("entidad1"));
    registro.setentidad2(request.getParameter("entidad2"));
    registro.setcomentario(request.getParameter("comentario"));
    registro.setaltres(request.getParameter("altres").trim());
    
    registro.setEntidad1Nuevo("");
    registro.setEntidad2Nuevo("");
    registro.setAltresNuevo("");
    registro.setComentarioNuevo("");
    
} else {
    registro.setentidad1(entidad1Anterior);
    registro.setentidad2(entidad2Anterior);
    registro.setcomentario(comentarioAnterior);
    registro.setaltres(altresAnterior);
    
    registro.setEntidad1Nuevo(request.getParameter("entidad1"));
    registro.setEntidad2Nuevo(request.getParameter("entidad2"));
    registro.setAltresNuevo(request.getParameter("altres").trim());
    registro.setComentarioNuevo(request.getParameter("comentario"));
}



           registro=regsal.validar(registro);
           boolean ok=registro.getValidado();

if (!ok){
    request.setAttribute("registroSalida",registro);
%>
        <jsp:forward page="ModiSalida.jsp"/>
<% } else { 



    registro=regsal.actualizar(registro);

    boolean actualizado=registro.getregistroActualizado();


    if (!actualizado) {
        request.setAttribute("registroSalida",registro);
%>
                <jsp:forward page="ModiSalida.jsp"/>
<%
    } else {
        String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
        String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
        String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");
        
        if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
            valores.liberarDisquete(bloqueoOficina,bloqueoTipo,bloqueoAno,usuario);
            session.removeAttribute("bloqueoOficina");
            session.removeAttribute("bloqueoTipo");
            session.removeAttribute("bloqueoAno");
            session.removeAttribute("bloqueoUsuario");
            session.removeAttribute("bloqueoDisquete");
        }
%>

<html>
    <head><title><fmt:message key='registre_de_sortides'/></title>
        <script src="jscripts/TAO.js"></script>
    </head>
    <body>       
      	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="ModiSalidaClave.jsp"><fmt:message key='modificacio_de_sortides'/></a></li>
		<li><fmt:message key='registre_de_sortida_modificat'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
        <table class="recuadroSalidas" width="400" align="center">
            <tr>
                <td style="border:0">
                    &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro.getNumeroSalida()%>/<%=registro.getAnoSalida()%> <fmt:message key='modificat_correctament'/></B></center><br/>
                </td>
            </tr>   
            <tr><td style="border:0">&nbsp;</td></tr>
            <tr>
                <td style="border:0">
                    <center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center>
                </td>
            </tr>
            <tr><td style="border:0">&nbsp;</td></tr>
            <tr>
                <td style="border:0">
                    <center><a style="text-decoration: none;" type="button" class="botonFormulario" href="ModiSalidaClave.jsp">
                        &nbsp;<fmt:message key='boton.nueva_modificacion'/>&nbsp;
                    </a>
                    </center>     
                </td>
            </tr>
            <tr><td style="border:0">&nbsp;</td></tr>
        </table>
        <!-- Nueva tabla -->
&nbsp;<br>
        <c:set var="data" scope="request"><%=registro.getDataSalida()%></c:set>
        <c:set var="hora" scope="request"><%=registro.getHora()%></c:set>
        <c:set var="oficina" scope="request"><%=valores.recuperaDescripcionOficina(registro.getOficina())%></c:set>
        <c:set var="oficinaid" scope="request"><%=registro.getOficina()%></c:set>
        <c:set var="numero" scope="request"><%=registro.getNumeroSalida()%></c:set>
        <c:set var="ano" scope="request"><%=registro.getAnoSalida()%></c:set>
        <c:set var="ES" scope="request">S</c:set>
        <jsp:include page="sellos.jsp" flush="true" />

        <!-- Fin de la nueva tabla -->

        <%
            }
}

        %>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
   		
              
    </body>
</html>