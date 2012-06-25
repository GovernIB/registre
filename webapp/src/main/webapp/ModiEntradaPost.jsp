<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*, org.apache.log4j.Logger" %>
<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>

<%! private Logger log = Logger.getLogger(this.getClass()); %>
<% request.setCharacterEncoding("UTF-8"); 



RegistroEntradaFacade regent = RegistroEntradaFacadeUtil.getHome().create();
ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
ValoresFacade valores = ValoresFacadeUtil.getHome().create();
boolean ok= true;

Integer intSerie=(Integer)session.getAttribute("serie");
if (intSerie==null) {
    intSerie=new Integer(0);
    session.setAttribute("serie", intSerie);
}
int serie=intSerie.intValue();
int serieForm = Integer.parseInt(request.getParameter("serie"));

if (serie>serieForm) {
    session.setAttribute("errorAtras","1");
}

serie++;

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
registro.setEmailRemitent(request.getParameter("emailRemitente"));
if (request.getParameter("mun_060")!=null){
   registro.setMunicipi060(request.getParameter("mun_060"));
	   try{
	   		registro.setNumeroDocumentosRegistro060(request.getParameter("numreg_060"));
		 }catch(NumberFormatException ne){
			 log.error("Parámetro del número de dos 012 no recibido.",ne);
		 }
	  }
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


//Comprobamos si es una oficina relacionada con el BOIB 
if (Conf.get("infoBOIB","false").equalsIgnoreCase("true") && registro.getOficina().equals(Conf.get("oficinaBOIB","false"))) {

   	ParametrosRegistroPublicadoEntrada paramRegPubEnt = new ParametrosRegistroPublicadoEntrada();
   	
   	String numeroBOCAIB=(request.getParameter("numeroBOCAIB")==null) ? "0" : (request.getParameter("numeroBOCAIB").trim().equals("")) ? "0" : request.getParameter("numeroBOCAIB");
    String dataPublic=(request.getParameter("dataPublic")==null) ? "0" : (request.getParameter("dataPublic").trim().equals("")) ? "0" : request.getParameter("dataPublic");
    String pagina=(request.getParameter("pagina")==null) ? "0" : (request.getParameter("pagina").trim().equals("")) ? "0" : request.getParameter("pagina");
    String lineas=(request.getParameter("lineas")==null) ? "0" : (request.getParameter("lineas").trim().equals("")) ? "0" : request.getParameter("lineas");
    String contenido = (request.getParameter("textoPublic")==null) ? "" : request.getParameter("textoPublic");
    String observaciones = (request.getParameter("observaciones")==null) ? "" : request.getParameter("observaciones");
 	
    try{
		  paramRegPubEnt.setAnoEntrada(Integer.parseInt(request.getParameter("anoEntrada")));
		  paramRegPubEnt.setNumero(Integer.parseInt(request.getParameter("numeroRegistro")));
		  paramRegPubEnt.setOficina(Integer.parseInt(request.getParameter("oficina")));
		  paramRegPubEnt.setFecha(dataPublic);
		  paramRegPubEnt.setNumeroBOCAIB(Integer.parseInt(numeroBOCAIB));
		  paramRegPubEnt.setPagina(Integer.parseInt(pagina));
		  paramRegPubEnt.setLineas(Integer.parseInt(lineas));
		  paramRegPubEnt.setContenido(contenido);
		  paramRegPubEnt.setObservaciones(observaciones);
		  registro.setParamRegPubEnt(paramRegPubEnt);   
    }catch(Exception ex){
    		log.error("Parámetro de publicación del BOIB erróneo.",ex);
    }
 
}

registro = regent.validar(registro);
ok=registro.getValidado();

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
       
      	<%-- Molla pa --%> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="ModiEntradaClave.jsp"><fmt:message key='modificacio_dentrades'/></a></li>
		<li><fmt:message key='registre_entrada_modificat'/></li>
		</ul>
		<p>&nbsp;</p>
        <table class="recuadroEntradas" width="400" align="center">
            <tr>
                <td style="border:0">
                    &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro.getNumeroEntrada()%>/<%=registro.getAnoEntrada()%> <fmt:message key='modificat_correctament'/></B></center><br/>
                </td>
            </tr>   
            <tr><td style="border:0">&nbsp;</td></tr>
            <tr>
                <td style="border:0">
                    <br/> <center><b><fmt:message key='oficina'/>:&nbsp;<%=registro.getOficina()%>-<%=valores.recuperaDescripcionOficina(registro.getOficina().toString())%></b></center>
                </td>
            </tr>
            <tr><td style="border:0">&nbsp;</td></tr>
            <tr>
                <td style="border:0">
                    <p>
                    	<center>
                    		<a style="text-decoration: none;" type="button" class="botonFormulario" href="ModiEntradaClave.jsp">&nbsp;<fmt:message key='boton.nueva_modificacion'/>&nbsp;</a>
                        </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0">&nbsp;</td></tr>
        </table>
		&nbsp;<br/>
        <c:set var="data" scope="request"><%=registro.getDataEntrada()%></c:set>
        <c:set var="hora" scope="request"><%=registro.getHora()%></c:set>
        <c:set var="oficina" scope="request"><%=valores.recuperaDescripcionOficina(registro.getOficina())%></c:set>
        <c:set var="oficinaid" scope="request"><%=registro.getOficina()%></c:set>
        <c:set var="numero" scope="request"><%=registro.getNumeroEntrada()%></c:set>
        <c:set var="ano" scope="request"><%=registro.getAnoEntrada()%></c:set>
        <c:set var="ES" scope="request">E</c:set>
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