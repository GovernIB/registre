<%@page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<% 

ListadoRegAccesosEntradaFacade listado = ListadoRegAccesosEntradaFacadeUtil.getHome().create();
ParametrosListadoAcceso parametros = new ParametrosListadoAcceso();

int pagina=(request.getParameter("pagina")==null) ? 1: Integer.parseInt(request.getParameter("pagina"));
// Constantes
int sizePagina=100;

String usuario=request.getRemoteUser();
String oficinaDesde="";
String oficinaHasta="";
String fechaDesde="";
String fechaHasta="";
String cadenaEnlace="";
String numRegistre="";
String anyRegistre="";
String veureNombreTotalRegistres="";

if (request.getParameter("any") == null) {
    oficinaDesde=request.getParameter("oficinaDesde");
    oficinaHasta=request.getParameter("oficinaHasta");
    fechaDesde=request.getParameter("fechaDesde");
    fechaHasta=request.getParameter("fechaHasta");
    numRegistre=request.getParameter("numRegistre");
    anyRegistre=request.getParameter("anyRegistre");
	veureNombreTotalRegistres=(request.getParameter("veureNombreTotalRegistres")!=null) ? request.getParameter("veureNombreTotalRegistres") : "";
    cadenaEnlace="oficinaDesde="+oficinaDesde+"&oficinaHasta="+oficinaHasta+"&fechaDesde="+fechaDesde+"&fechaHasta="+fechaHasta+
				"&numRegistre="+numRegistre+"&anyRegistre="+anyRegistre+"&veureNombreTotalRegistres="+veureNombreTotalRegistres;
} else {
    oficinaDesde=request.getParameter("oficina");
    oficinaHasta=request.getParameter("oficina");
    fechaDesde="01/01/"+request.getParameter("any");
    fechaHasta="31/12/"+request.getParameter("any");
    cadenaEnlace="oficina="+oficinaDesde+"&any="+request.getParameter("any");
}

parametros.setCalcularTotalRegistres( (veureNombreTotalRegistres!=null && veureNombreTotalRegistres.equals("on") ? true : false) );
parametros.setfechaDesde(fechaDesde);
parametros.setfechaHasta(fechaHasta);
parametros.setoficinaDesde(oficinaDesde);
parametros.setoficinaHasta(oficinaHasta);
parametros.setoficinaHasta(oficinaHasta);
parametros.setNumRegistre(numRegistre);
parametros.setAnyRegistre(anyRegistre);
session.setAttribute("listadoRegAcESSortida",parametros);
%>

<%
parametros=listado.validarBusqueda(parametros);
boolean ok=parametros.getValidado();
if (!ok){
%>
    <jsp:forward page="busquedaRegAccSortidesXFechas.jsp" />
<% } else { %>

<html>
<head><title><fmt:message key='registre_sortides'/></title>
    
    
    
    <script src="<c:url value='/jscripts/TAO.js'/>"></script>
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
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key='inici'/></a></li>	
			<li><a href="<c:url value='/lopd/index_lopd.jsp'/>"><fmt:message key='registre_accessos_lopd'/></a></li>
			<li><a href="<c:url value='/lopd/busquedaRegAccSortidesXFechas.jsp'/>"><fmt:message key='consulta_log_sortides'/></a></li>
			<li><fmt:message key='lopd.llistat_log_sortides'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
<%
Vector registros=listado.recuperar(parametros, usuario, sizePagina, pagina);
%>
        <!-- Mostramos Errores si los hubiera -->
        <% Hashtable errores = (parametros==null) ? new Hashtable() : parametros.getErrores();
        if (errores.size() > 0) {%>
        <table class="recuadroErrors" width="591" align="center">
            <tr>
                <td>
                    <p><b><fmt:message key='registro.error.atencion'/></b> <fmt:message key='registro.error.revise_problemas'/>:</p>
                    <ul>
                        <%      for (Enumeration e=errores.elements();e.hasMoreElements();) { %>
                        <li><%= e.nextElement()%></li>
                        <%}%>
                    </ul>
                </td>
            </tr>
        </table>
        <br>
		<% } //De if (errores.size()>0) %>
<%
if (registros.size()==0) { 
/* No hi ha cap element al llistat, eliminam el llistat de la sessió.*/
        	session.removeAttribute("listadoRegAcESSortida");
        	%>
<p></p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<center><b><fmt:message key='no_shan_trobat_registres_que_compleixin_els_criteris_seleccionats'/></b></center>
<br/>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<center>[&nbsp;<a href="busquedaRegAccSortidesXFechas.jsp"><fmt:message key='consulta_log_sortides'/></a>&nbsp;]</center>
     <% } else { %>
     
<table border="0" align="center"><!--width="95%"--> 
    <tr>
        <td align="left" width="33%">
            <%
            if (pagina>1) {
            %>
            <a href="listadoRegAccSortida.jsp?<%=cadenaEnlace%>&pagina=<%=pagina-1%>" title="Retrocedir" style="text-decoration:none;"><< 100 <fmt:message key='anteriors'/></a>
            <%
            }
            %>
        </td>
        <td align="center" width="33%">
            [&nbsp;<a href="<%=(request.getParameter("any")==null) ? "busquedaRegAccSortidesXFechas.jsp" : "busquedaRegAccSortidesXRegistro.jsp"%>"><fmt:message key='tornar_seleccionar'/></a>&nbsp;]
        </td>
        <td align="right" width="33%">
            <%
            if (registros.size()>sizePagina) {
            %>
            <a href="listadoRegAccSortida.jsp?<%=cadenaEnlace%>&pagina=<%=pagina+1%>" title="Avançar" style="text-decoration:none;">100 <fmt:message key='seguents'/> >></a>
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

<table  border="1" align="center">
    <tr>
    <td align="center" class="cabeceraTabla"><fmt:message key='lopd.tipus_acces'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='lopd.data_canvi'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='lopd.hora_canvi'/><br/><fmt:message key='lopd.hhmmssmis'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='lopd.num_reg'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='any_reg'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='oficina'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='usuari'/></td>
    </tr>
    <%   
    int hasta= (registros.size()>sizePagina) ?  sizePagina : registros.size();
    for (int i=0;i<hasta;i++) {
        RegistroESlopdSeleccionado reg=(RegistroESlopdSeleccionado)registros.get(i);
        String fechaSortida=reg.getDataCanvi();
		String horacanvi = reg.getHoraCanvi();
        String numeroSortida=reg.getNombreRegistre();
        String oficina=reg.getOficinaRegistre();
        String anySortida=reg.getAnyRegistre();
        String textoOficina=reg.getOficinaRegistreDesc();
		String TipusAcces = reg.getTipusAcces();
        String claveRegistro=anySortida+"-"+numeroSortida;
		String usucanvi = reg.getUsuCanvi();
    %>
    <tr id="<%="fila"+i%>" class="<%=((i%2)==0) ? "par" : "impar"%>"> 
     
 <!--       <td>
        <a id="<%="ref"+i%>" href="fichaRegAccSortida.jsp?oficina=<%=oficina%>&numeroSortida=<%=numeroSortida%>&anoSortida=<%=anySortida%>">
            <img src="imagenes/open24.gif" border=0  title="Veure document">
        </a>
        </td>  -->
        <td align="center"><%=TipusAcces%></td>
        <td ><%=fechaSortida%></td>
        <td align="center"><%=horacanvi%></td>
        <td align="center"><%=numeroSortida%></td>
        <td align="center"><%=anySortida%></td>
        <td><%=oficina%>-<%=textoOficina%></td>
        <td><%=usucanvi%></td>        
    </tr> 
    <% }%>
</table>
&nbsp;<br>
<table border="0" align="center">
    <tr>
        <td align="left" width="33%">
            <%
            if (pagina>1) {
            %>
            <a href="listadoRegAccSortida.jsp?<%=cadenaEnlace%>&pagina=<%=pagina-1%>" title="Retrocedir" style="text-decoration:none;"><< 100 <fmt:message key='anteriors'/></a>
            <%
}
            %>
        </td>
<!--        <td align="center" width="33%">
            [&nbsp;<a href="<%=(request.getParameter("any")==null) ? "busquedaSortidesXFechas.jsp" : "busquedaSortidesXRegistro.jsp"%>"><fmt:message key='tornar_seleccionar'/></a>&nbsp;]
        </td>-->
        <td align="right" width="33%">
            <%
            if (registros.size()>sizePagina) {
            %>
            <a href="listadoRegAccSortida.jsp?<%=cadenaEnlace%>&pagina=<%=pagina+1%>" title="Avançar" style="text-decoration:none;">100 <fmt:message key='seguents'/> >></a>
            <%
}
            %>
        </td>
    </tr>
</table>

 <%}
	/* Per maquetar quan només apareixen pocs
	   registres al llistat */
	if ( registros.size()<10 && registros.size()>0) {
		for (int i=9-registros.size();i>0;i--) {
            out.println("<p>&nbsp;</p>");
		}
	}
%>
<% } %>
 		<!-- PEU -->
                 
   		<!-- Fi PEU -->
	</body>
</html>   