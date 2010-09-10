<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!--
  Registro General CAIB - Registro de Entradas
-->
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%
RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada param = new ParametrosRegistroEntrada();
ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();

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
       <% }

serie++;
//    intSerie++;
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
registro.setAnoEntrada(request.getParameter("anoEntrada"));
registro.setNumeroEntrada(request.getParameter("numeroRegistro"));
registro.setoficina(request.getParameter("oficina"));
// Resto de campos
registro.setoficinafisica(request.getParameter("oficinafisica"));
registro.setCorreo(request.getParameter("correo"));
registro.setdataentrada(request.getParameter("dataentrada"));
registro.sethora(request.getParameter("hora"));
registro.setdata(request.getParameter("data"));
registro.settipo(request.getParameter("tipo"));
registro.setidioma(request.getParameter("idioma"));
registro.setbalears(request.getParameter("balears"));
registro.setfora(request.getParameter("fora"));
registro.setsalida1(request.getParameter("salida1"));
registro.setsalida2(request.getParameter("salida2"));
registro.setdestinatari(request.getParameter("destinatari"));
registro.setidioex(request.getParameter("idioex"));
registro.setdisquet(request.getParameter("disquet"));
registro.setRegistroAnulado(request.getParameter("suprimir"));
if (request.getParameter("mun_060")!=null)
   registro.setMunicipi060(request.getParameter("mun_060"));
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


registro = regent.validar(registro);
boolean ok=registro.getValidado();

if (!ok){
    request.setAttribute("registroEntrada",registro);
%>
        <jsp:forward page="ModiEntrada.jsp"/>
<% } else { 
    


    registro=regent.actualizar(registro);

    boolean actualizado=registro.getregistroActualizado();

    if (!actualizado) {
        request.setAttribute("registroEntrada",registro);
%>
                <jsp:forward page="ModiEntrada.jsp"/>
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
    <head><title><fmt:message key='registre_entrades'/></title>
        
        
        
        <script src="jscripts/TAO.js"></script>
    </head>
    <body>
       
      	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="ModiEntradaClave.jsp"><fmt:message key='modificacio_dentrades'/></a></li>
		<li><fmt:message key='registre_entrada_modificat'/></li>
		</ul>
		<!-- Fi Molla pa-->
<!--        <p>&nbsp;
        <center><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center>&nbsp;<p>
-->
		<p>&nbsp;</p>
        <table class="recuadroEntradas" width="400" align="center">
            <tr>
                <td style="border:0">
                    &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%> <fmt:message key='modificat_correctament'/></B></center></p>
                </td>
            </tr>   
            <tr><td style="border:0">&nbsp;</td></tr>
            <tr>
                <td style="border:0">
                    <p><center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center>
                </td>
            </tr>
            <tr><td style="border:0">&nbsp;</td></tr>
            <tr>
                <td style="border:0">
                    <p>
                    	<center>
                    		<a style="text-decoration: none;" type="button" class="botonFormulario" href="ModiEntradaClave.jsp">
                        &nbsp;<fmt:message key='boton.nueva_modificacion'/>&nbsp;</a>
                        </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0">&nbsp;</td></tr>
        </table>
       
        <!-- Nueva tabla -->
&nbsp;<br>
        <table align="center">
            <tr>
                <td align="center">
                    <iframe height="87" width="63" name="pdf1" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <iframe height="87" width="63" name="pdf2" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <iframe height="87" width="63" name="pdf3" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <iframe height="87" width="63" name="pdf4" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=1&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_1'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=2&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_2'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=3&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_3'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">                       
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=4&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_4'/></a> 
                </td>
            </tr>
            <tr>
                <td>
                    <iframe height="70" width="87" name="pdf5" src="./imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=5&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>
                    <iframe height="70" width="87" name="pdf6" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=6&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>
                    <iframe height="70" width="87" name="pdf7" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=7&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
                <td>&nbsp;&nbsp;</td>
                <td>                       
                    <iframe height="70" width="87" name="pdf8" src="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=8&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E"></iframe>
                </td>
            </tr>
            <tr>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=5&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_5'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=6&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_6'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=7&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_7'/></a> 
                </td>
                <td>&nbsp;&nbsp;</td>
                <td align="center">
                    <a target="_blank" href="imprimeSello?data=<%=registro.getDataEntrada()%>&tipo=8&oficina=<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%>&oficinaid=<%=registro.getOficina().toString()%>&numero=<%=registro.getNumeroEntrada()%>&ano=<%=registro.getAnoEntrada()%>&ES=E&auto_print=si"><fmt:message key='pdf_8'/></a> 
                </td>
            </tr>

        </table>

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