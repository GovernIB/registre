<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%

    if (request.getParameter("numero")!=null && !request.getParameter("numero").trim().equals("") ) {
            %>
            <jsp:forward page="fichaOficio.jsp" >
                <jsp:param name="oficina" value="<%=request.getParameter("oficina")%>"/>
                <jsp:param name="numero" value="<%=request.getParameter("numero")%>"/>
                <jsp:param name="any" value="<%=request.getParameter("any")%>"/>
            </jsp:forward>
            <%
    }

%>
<%
ListadoOficiosFacade listado=ListadoOficiosFacadeUtil.getHome().create();

String usuario=request.getRemoteUser().toUpperCase();
String oficinaSel=request.getParameter("oficina");
String oficinaFisicaSel=request.getParameter("oficinafisica");
String anyoSel=request.getParameter("any");
String [] parametros = {oficinaSel, oficinaFisicaSel, anyoSel};

session.setAttribute("listadoOficios",parametros);
%>


<html>
<head><title><fmt:message key='registre_sortides'/></title>
    <link type="text/CSS" rel="stylesheet" href="estilos.css">
    <link rel="shortcut icon" href="favicon.ico"/> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		<li><a href="busquedaOficiosIndex.jsp"><fmt:message key='consulta_oficis'/></a></li>
		<li><a href="busquedaOficiosXOficio.jsp"><fmt:message key='consulta_per_numero_dofici_i_any'/></a></li>
		<li><fmt:message key='llistat_oficis'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
<%
Vector registros=listado.recuperarRegistros(usuario, oficinaSel, oficinaFisicaSel, anyoSel);

if (registros.size()==0) { 
/* No hi ha cap element al llistat, eliminam el llistat de la sessió.*/
        	session.removeAttribute("listadoOficios");
        	%>
<p><p>
<center><b><fmt:message key='no_shan_trobat_registres_que_compleixin_els_criteris_seleccionats'/></b></center>
     <% } else { %>
     
<table border="0" width="95%" align="center">
    <tr>
        <td align="left" width="33%">
        </td>
        <td align="center" width="33%">
            [&nbsp;<a href="busquedaOficiosXOficio.jsp"><fmt:message key='tornar_seleccionar'/></a>&nbsp;]
        </td>
        <td align="right" width="33%">
        </td>
    </tr>
</table>


<table width="100%" border=0>
    <tr>
        <td align="left">
            
        </td>
        <td align="right">
            <font color="green"><fmt:message key='recerca_paraules'/></font>
        </td>
    </tr>
</table>

<table width="100%" border="1">
    <tr>
    <td></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='data'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='num_ofici'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='num_reg_sort'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='oficina'/></td>
    <td align="center" class="cabeceraTabla"><fmt:message key='destinatari'/>/<fmt:message key='registro.destino_geografico'/></td>
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
    int hasta=registros.size();
    for (int i=0;i<hasta;i++) {
        RegistroSeleccionado reg=(RegistroSeleccionado)registros.get(i);
        
        String anoSalida=reg.getAnoEntrada();
        String fechaSalida=reg.getFechaES();
        String numeroSalida=reg.getNumeroEntrada();
        String oficina=reg.getOficina();
        String textoOficina=reg.getDescripcionOficina();
        String textoOficinaFisica=reg.getDescripcionOficinaFisica();
        String fechaDocumento=reg.getData();
        String remitente=reg.getDescripcionOrganismoDestinatario();
        String destinatario=reg.getDescripcionRemitente();
        String procedencia=reg.getDescripcionGeografico();
        String tipoDocumento=reg.getDescripcionDocumento();
        String claveRegistro=anoSalida+"-"+numeroSalida;
        String extracto=reg.getExtracto();
        String oficio=reg.getOficio();
        boolean anulado=(reg.getRegistroAnulado().equals("") || reg.getRegistroAnulado().equals(" ") || reg.getRegistroAnulado().equals("N")) ? false : true;
    %>
    <tr id="<%="fila"+i%>" class="<%=((i%2)==0)? "par":"impar"%>"> 
     
        <td>
        <a id="<%="ref"+i%>" href="fichaOficio.jsp?numero=<%=oficio %>&oficina=<%=oficina%>&any=<%=anoSalida%>">
            <img src="imagenes/open24.gif" border=0  title="Veure document">
        </a>
        </td> 
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=fechaSalida%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>" align="center"><%=oficio%>/<%=anoSalida%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>" align="center"><%=numeroSalida%>/<%=anoSalida%></td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=oficina%>-<%=textoOficina%> (<%=textoOficinaFisica%>)</td>
        <td style="<%= (anulado) ? "color:red;" : "" %>"><%=destinatario%> &nbsp;/&nbsp; <%=procedencia%></td>
        <c:set var="texto" scope="page"><%=extracto%></c:set>
        <td style="<%= (anulado) ? "color:red;" : "" %>" width="25%"><c:out value="${texto}"/><script>cargarDatos("<c:out value="${texto}"/>");</script></td>
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
        </td>
        <td align="right" width="33%">
        </td>
    </tr>
</table>

<% } %>
 		<!-- PEU -->
   		<!-- Fi PEU -->
	</body>
</html>   