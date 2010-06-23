<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<% 
ListadoRegistrosEntradaFacade listado=ListadoRegistrosEntradaFacadeUtil.getHome().create();
ParametrosListadoRegistrosEntrada parametros = new ParametrosListadoRegistrosEntrada();

int pagina=(request.getParameter("pagina")==null) ? 1: Integer.parseInt(request.getParameter("pagina"));
// Constantes
int sizePagina=100;

String usuario=request.getRemoteUser();
String oficinaDesde="";
String oficinaHasta="";
String fechaDesde="";
String fechaHasta="";
String extractoBusqueda="";
String tipoBusqueda="";
String remitenteBusqueda="";
String procedenciaBusqueda="";
String destinatarioBusqueda="";
String codidestinatariBusqueda="";
String cadenaEnlace="";
String veureNombreTotalRegistres="";
String codiMun060=(request.getParameter("mun_060")==null)? "000":request.getParameter("mun_060");

//System.out.println("codiMun060="+codiMun060);

if (request.getParameter("any")==null) {
    oficinaDesde=request.getParameter("oficinaDesde");
    oficinaHasta=request.getParameter("oficinaHasta");
    fechaDesde=request.getParameter("fechaDesde");
    fechaHasta=request.getParameter("fechaHasta");
    extractoBusqueda=request.getParameter("extracto");
    tipoBusqueda=request.getParameter("tipo");
    remitenteBusqueda=request.getParameter("remitente");
    procedenciaBusqueda=request.getParameter("procedencia");
    destinatarioBusqueda=request.getParameter("destinatario");
    codidestinatariBusqueda=request.getParameter("codidestinatari");
    veureNombreTotalRegistres=(request.getParameter("veureNombreTotalRegistres")!=null) ? request.getParameter("veureNombreTotalRegistres") : "";
    cadenaEnlace="oficinaDesde="+oficinaDesde+"&oficinaHasta="+oficinaHasta+"&fechaDesde="+fechaDesde+"&fechaHasta="+fechaHasta+
            "&extracto="+extractoBusqueda+"&tipo="+tipoBusqueda+"&remitente="+remitenteBusqueda+"&procedencia="+procedenciaBusqueda+
            "&destinatario="+destinatarioBusqueda+"&codidestinatari="+codidestinatariBusqueda;
} else {
    oficinaDesde=request.getParameter("oficina");
    oficinaHasta=request.getParameter("oficina");
    fechaDesde="01/01/"+request.getParameter("any");
    fechaHasta="31/12/"+request.getParameter("any");
    cadenaEnlace="oficina="+oficinaDesde+"&any="+request.getParameter("any");
    if (request.getParameter("numero")!=null && !request.getParameter("numero").trim().equals("") ) {
            %>
            <jsp:forward page="ficha.jsp" >
                <jsp:param name="oficina" value="<%=request.getParameter("oficina")%>"/>
                <jsp:param name="numeroEntrada" value="<%=request.getParameter("numero")%>"/>
                <jsp:param name="anoEntrada" value="<%=request.getParameter("any")%>"/>
            </jsp:forward>
            <%
    }
}
parametros.setoficinaFisica(request.getParameter("oficinafisica"));
parametros.setCalcularTotalRegistres( (veureNombreTotalRegistres.equals("on") ? true : false) );
parametros.setfechaDesde(fechaDesde);
parametros.setfechaHasta(fechaHasta);
parametros.setoficinaDesde(oficinaDesde);
parametros.setoficinaHasta(oficinaHasta);
parametros.setExtracto(extractoBusqueda);
parametros.setTipo(tipoBusqueda);
parametros.setRemitente(remitenteBusqueda);
parametros.setProcedencia(procedenciaBusqueda);
parametros.setDestinatario(destinatarioBusqueda);
parametros.setCodiDestinatari(codidestinatariBusqueda);
parametros.setCodiMunicipi060(codiMun060);

session.setAttribute("listadoEntrada",parametros);
%>

<% boolean ok=listado.validarBusqueda(parametros);
if (!ok){
%>
  <%  if (request.getParameter("any")==null) { %>
        <jsp:forward page="busquedaEntradasXFechas.jsp" />
    <% } else { %>
        <jsp:forward page="busquedaEntradasXRegistro.jsp" />
    <% }  %>
<% } else { %>

<html>
<head><title><fmt:message key='registre_entrades'/></title>
    
    
    
    <script src="jscripts/TAO.js"></script>
    <script>
        var listaExtractos=new Array();
        var i=0;
        var copiado=false;
     
        function cargarDatos(cadena) {
        listaExtractos[i++]=cadena.toUpperCase();
        }
     
        function buscar() {
        var resultados=false;
        var cadena=prompt("Text a cercar en l'extracte","");
        if (cadena==null || trim(cadena)=="") {
        return;
        }
        
        enfocado=false;
        encontrados=0;
        for (var n=0;n < i; n++) {
        var elemento=listaExtractos[n];
        trId = "fila" + n;
        refId= "ref" + n;
        var fila=document.getElementById(trId);
        if (elemento.lastIndexOf(trim(cadena.toUpperCase()))>-1) {
        fila.style.background="#fff8a7"; 
        if (!enfocado) {
        document.getElementById(refId).focus();
        enfocado=true;
        }
        resultados=true;
        encontrados++;
        } else {
        if (n%2==0) {
        fila.style.background="";
        } else {
        fila.style.background="#DDDDFF";
        }
        }
        }
        if (!resultados) {
        alert("Text "+'"'+cadena+'"'+" no trobat dins l'extracte");
        } else {
        alert("Trobats "+encontrados+" registres amb el text "+'"'+cadena+'"'+" a l'extracte");
        }
        }
    </script>
</head>
<body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="busquedaEntradasIndex.jsp"><fmt:message key='consulta_entrades'/></a></li>
		<li><a href="<%=(request.getParameter("any")==null) ? "busquedaEntradasXFechas.jsp" : "busquedaEntradasXRegistro.jsp"%>"><%=(request.getParameter("any")==null) ? "<fmt:message key='consulta_per_oficines_i_dates'/>" : "Consulta per registre i any d'entrada"%></a></li>
		<li><fmt:message key='llistat_registre_entrades'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
<%
Vector registros=listado.recuperar(parametros, usuario, sizePagina, pagina);

if (registros.size()==0) { 
/* No hi ha cap element al llistat, eliminam el llistat de la sessió.*/
        	session.removeAttribute("listadoEntrada");
        	%>
<p><p>
<center><b><fmt:message key='no_shan_trobat_registres_que_compleixin_els_criteris_seleccionats'/></B></center>
<!-- &nbsp;<br><center>[&nbsp;<a href="<%=(request.getParameter("any")==null) ? "busquedaEntradasXFechas.jsp" : "busquedaEntradasXRegistro.jsp"%>"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center> -->
     <% } else { %>
     
<table border="0" width="95%" align="center">
    <tr>
        <td align="left" width="33%">
            <%
            if (pagina>1) {
            %>
            <a href="listado.jsp?<%=cadenaEnlace%>&pagina=<%=pagina-1%>" title="Retrocedir" style="text-decoration:none;"><< 100 <fmt:message key='anteriors'/></a>
            <%
            }
            %>
        </td>
        <td align="center" width="33%">
            [&nbsp;<a href="<%=(request.getParameter("any")==null) ? "busquedaEntradasXFechas.jsp" : "busquedaEntradasXRegistro.jsp"%>"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]
        </td>
        <td align="right" width="33%">
            <%
            if (registros.size()>sizePagina) {
            %>
            <a href="listado.jsp?<%=cadenaEnlace%>&pagina=<%=pagina+1%>" title="Avançar" style="text-decoration:none;">100 <fmt:message key='seguents'/> >></a>
            <%
            }
            %>
        </td>
    </tr>
   <% if (parametros.isCalcularTotalRegistres() ) { %>
    <tr>
    	<td align="center" colspan="3"><fmt:message key='nombre_total_registres_criteris_consulta'/> <strong><%=parametros.getTotalFiles()%></strong></td>
    </tr>
   <% } %>
</table>


<table width="100%" border=0>
    <tr>
        <td align="left">
            <font color="red"><fmt:message key='nota_registres_vermell_anulats'/></font>
        </td>
        <td align="right">
            <font color="green"><fmt:message key='recerca_paraules'/></font>
        </td>
    </tr>
</table>

<table width="100%" border="1">
    <tr>
    <td></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='data_ent'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='num_reg'/></TD>
    <td align="center" class="cabeceraTabla"><fmt:message key='oficina'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='data_doc'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='t_document'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='remitent'/>/<fmt:message key='procedencia_geografica'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='destinatari'/></td>
    <td width="25%" >
       <table width="100%" border="0">
       <tr>
           <td width="10%" class="cabeceraTabla">&nbsp;&nbsp;<fmt:message key='extracte'/></td>
           <td align="left">
               <a href="javascript: buscar()"> 
                   <img src="imagenes/buscar.gif" border=0  title="<fmt:message key='cercar_extracte'/>">
               </a>
           </td>
       </tr>
       </table>
    </td>
    </tr>
    <%   
    int hasta= (registros.size()>sizePagina) ?  sizePagina : registros.size();
    for (int i=0;i<hasta;i++) {
        RegistroSeleccionado reg=(RegistroSeleccionado)registros.get(i);
        
        String anoEntrada=reg.getAnoEntrada();
        String fechaEntrada=reg.getFechaES();
        String numeroEntrada=reg.getNumeroEntrada();
        String oficina=reg.getOficina();
        String textoOficina=reg.getDescripcionOficina();
        String textoOficinaFisica=reg.getDescripcionOficinaFisica();
        String fechaDocumento=reg.getData();
        String remitente=reg.getDescripcionRemitente();
        String procedencia=reg.getDescripcionGeografico();
        String destinatario=reg.getDescripcionOrganismoDestinatario();
        String tipoDocumento=reg.getDescripcionDocumento();
        String claveRegistro=anoEntrada+"-"+numeroEntrada;
        String extracto=reg.getExtracto();
        boolean anulado=(reg.getRegistroAnulado().equals("") || reg.getRegistroAnulado().equals(" ")) ? false : true;
    %>
    <tr id="<%="fila"+i%>" class="<%=((i%2)==0)? "par":"impar"%>"> 
     
        <td>
        <a id="<%="ref"+i%>" href="ficha.jsp?oficina=<%=oficina%>&numeroEntrada=<%=numeroEntrada%>&anoEntrada=<%=anoEntrada%>">
            <img src="imagenes/open24.gif" border=0  title="Veure document">
        </a>
        </td> 
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=fechaEntrada%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>" align="center"><%=numeroEntrada%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=oficina%>-<%=textoOficina%> (<%=textoOficinaFisica%>)</td>
        <td style="<%= (anulado) ? "color:red;" : "" %>" align="center"><%=fechaDocumento%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=tipoDocumento%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=remitente%> &nbsp;/&nbsp; <%=procedencia%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=destinatario%></td>
        <c:set var="texto" scope="page"><%=extracto%></c:set>
        <td style="<%= (anulado) ? "color:red;" : "" %>" width="25%"><c:out escapeXml="false" value="${texto}"/><script>cargarDatos("<c:out escapeXml="true" value="${texto}"/>");</script></td>
    </tr>
    <% }%>
</table>

<%
	/* Per maquetar quan només apareixen pocs
	   registres al llistat */
	if ( registros.size()<10 && registros.size()>0) {
		for (int i=6-registros.size();i>0;i--) {
            out.println("<p>&nbsp;</p>");
		}
	}
%>
&nbsp;<br>
<table border="0" width="95%" align="center">
    <tr>
        <td align="left" width="33%">
            <%
            if (pagina>1) {
            %>
            <a href="listado.jsp?<%=cadenaEnlace%>&pagina=<%=pagina-1%>" title="Retrocedir" style="text-decoration:none;"><< 100 <fmt:message key='anteriors'/></a>
            <%
}
            %>
        </td>
<!--        <td align="center" width="33%">
            [&nbsp;<a href="<%=(request.getParameter("any")==null) ? "busquedaEntradasXFechas.jsp" : "busquedaEntradasXRegistro.jsp"%>"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]
        </td>-->
        <td align="right" width="33%">
            <%
            if (registros.size()>sizePagina) {
            %>
            <a href="listado.jsp?<%=cadenaEnlace%>&pagina=<%=pagina+1%>" title="Avançar" style="text-decoration:none;">100 <fmt:message key='seguents'/> >></a>
            <%
}
            %>
        </td>
    </tr>
</table>

 <%}%>
<% } %>
 		
                 
   		
	</body>
</html>   