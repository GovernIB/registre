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
String usuario=request.getRemoteUser();

session.setAttribute("oficinaSesion", request.getParameter("oficina"));
session.setAttribute("oficinaFisicaSesion", request.getParameter("oficinafisica"));

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
    <jsp:forward page="pedirdatosSalida.jsp" />
       <% }

serie++;
// intSerie++;
intSerie=Integer.valueOf(String.valueOf(serie));
session.setAttribute("serie", intSerie);
session.removeAttribute("errorAtras");
%>

<%
registro.fijaUsuario(usuario);
registro.setCorreo(request.getParameter("correo"));
registro.setdatasalida(request.getParameter("datasalida"));
registro.sethora(request.getParameter("hora"));
registro.setoficina(request.getParameter("oficina"));
registro.setoficinafisica(request.getParameter("oficinafisica"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setentidad1(request.getParameter("entidad1"));
registro.setentidad2(request.getParameter("entidad2"));
registro.setaltres(request.getParameter("altres"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setentrada1(request.getParameter("entrada1"));
registro.setentrada2(request.getParameter("entrada2"));
registro.setremitent(request.getParameter("remitent"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
registro.setcomentario(request.getParameter("comentario"));
%>

<%

     registro=regsal.validar(registro);
     boolean ok=registro.getValidado();




if (!ok){
    request.setAttribute("registroSalida",registro);

%>
        <jsp:forward page="pedirdatosSalida.jsp" />
<% } else { 


    registro=regsal.grabar(registro);

    boolean grabado=registro.getregistroSalidaGrabado();



    if (!grabado) {
        request.setAttribute("registroSalida",registro);

%>
                <jsp:forward page="pedirdatosSalida.jsp" />

<%            } else {
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
        
        <link rel="shortcut icon" href="favicon.ico"/>
        
        <script src="jscripts/TAO.js"></script>
    </head>
    <body>
        
     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="pedirdatosSalida.jsp"><fmt:message key='registre_de_sortides'/></a></li>
		<li><fmt:message key='registre_de_sortida_creat'/></li>
		</ul>
		<!-- Fi Molla pa-->
        <!-- <p>&nbsp;
        <center><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center>&nbsp;<p>
        -->
		<p>&nbsp;</p>
        <table class="recuadroSalidas" width="400" align="center">
            <tr>
                <td style="border:0" >
                    &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro.getNumeroSalida()%>/<%=registro.getAnoSalida()%> <fmt:message key='desat_correctament'/></B></center></p>
                </td>
            </tr>   
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p><center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center></p>
                    <p><center><b><fmt:message key='oficina_fisica'/>:&nbsp;<%=registro.getOficinafisica()%>-<%=valores.recuperaDescripcionOficinaFisica(registro.getOficina().toString(),registro.getOficinafisica().toString())%></b></center></p>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
            <tr>
                <td style="border:0" >
                    <p><center><a style="text-decoration: none;" type="button" class="botonFormulario" href="pedirdatosSalida.jsp">
                        &nbsp;<fmt:message key='registro.nuevo_registro'/>&nbsp;</a>
                    </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0" >&nbsp;</td></tr>
        </table>
        <!-- Nueva tabla -->
		<br/>
		<br/>

        <%-- substituir per incloure la pàgina "sellos.jsp" --%>
        <%--
        <table align="center">
            <tr>
                <td align="center">
                    <iframe height="87" width="63" name="pdf1" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <iframe height="87" width="63" name="pdf2" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <iframe height="87" width="63" name="pdf3" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <iframe height="87" width="63" name="pdf4" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
            </tr>
             <tr>
                <td align="center"> 
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_1'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_2'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_3'/></a>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_4'/></a>
                </td>
            </tr>
            <tr>
                <td>
                    <iframe height="70" width="87" name="pdf5" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=5&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>
                    <iframe height="70" width="87" name="pdf6" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=6&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>
                    <iframe height="70" width="87" name="pdf7" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=7&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>                       
                    <iframe height="70" width="87" name="pdf8" src="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=8&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S"></iframe>
                </td>
            </tr>
            <tr>
                <td align="center"> 
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=5&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_5'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=6&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_6'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=7&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_7'/></a>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataSalida()%>&tipo=8&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroSalida()%>&ano=<%=registro.getAnoSalida()%>&ES=S&auto_print=si"><fmt:message key='pdf_8'/></a>
                </td>
            </tr>
        </table>
        --%>
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
        
                 
    </body>
</html>