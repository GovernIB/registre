<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>

<%
String usuario=request.getRemoteUser();
ValoresFacade valores = ValoresFacadeUtil.getHome().create();
ParametrosListadoOficioRemision parametros =(ParametrosListadoOficioRemision)session.getAttribute("listadoOficio");
%>
<%! 
void escribeSelect(javax.servlet.jsp.JspWriter out, String tipo, Vector valores, String referencia) throws java.io.IOException {
    
    for (int i=0;i<valores.size();i=i+2){
        String codigo=valores.get(i).toString();
        String descripcion=valores.get(i+1).toString();
        if (!codigo.trim().equals("")) {
            out.write("<option value=\""+codigo+"\" "+ (codigo.equals(referencia) ? "selected" : "")+">");
            if (tipo.equals("N")) {
                out.write(descripcion);
            } else {
                out.write(codigo+" - "+descripcion);
            }
            out.write("</option>\n");
        }
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

%>

<html>
    <head>
    	<title><fmt:message key='registre_de_sortides'/></title> 		        
        <jsp:include page="/jscripts/jscalendar/calendario.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">    
        
        <script type="text/javascript">
        <!-- 
        function confirmaProceso() {
        	if ((document.busquedaForm.fechaDesde.value.length == 0) || (document.busquedaForm.fechaHasta.value.length == 0)){
        		alert("<fmt:message key='error_data_consulta_oficis'/>");
        		return false
        	}
        	return true
        }
        -->
        </script>       
    </head>
    <body bgcolor="#FFFFFF">
       	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="busquedaSalidasIndex.jsp"><fmt:message key='consulta_oficis'/></a></li>
		<li><fmt:message key='consulta_oficis_per_oficines_i_dates'/></li>
		</ul>
		<!-- Fi Molla pa-->
   		<p>&nbsp;</p>
        <center><br/><font class="titulo"><fmt:message key='consulta_oficis'/></font></center></p>

        <table align="center" width="70%" class="recuadroSalidas" cols="3">
        <form name="busquedaForm" action="listadoOficios.jsp" method="post" onsubmit="return confirmaProceso()">
        <input type="hidden" name="pag_origen" value="busqOficiosFechas">
        <tr>
        	<td style="border:0">
        		&nbsp;<br>&nbsp;
        	</td>
	        <td style="border:0" align="center">
	        	<b><fmt:message key='des_de'/></b>
	       	</td>
 	       	<td style="border:0" align="center">
        	    <b><fmt:message key='fins'/></b>
    	    </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='oficina'/>..............................:
            </td>
            <td style="border:0">
                <select name="oficinaDesde" id="oficinaDesde"  onchange="refrescaFisica()">
                    <% escribeSelect(out, "", valores.buscarOficinas(usuario,"CS",false), (parametros==null)? "": parametros.getOficinaDesde());%>
                </select>
            </td>
            <td style="border:0">
                <select name="oficinaHasta" id="oficinaHasta"  onchange="refrescaFisica()">
                    <% escribeSelect(out, "", valores.buscarOficinas(usuario,"CS",false), (parametros==null)? "": parametros.getOficinaHasta());%>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
	        <td style="border:0">
    	        &nbsp;<br>&nbsp;<fmt:message key='data_sortida'/>..................:
        	</td>
        	<td style="border:0">
            	<%String anteriorFechaDesde=(parametros==null)? "": parametros.getFechaDesde();%>
            	<input type="text" readonly="true" name="fechaDesde" id="fechaDesde" size="10" maxlength="10" value="<%=anteriorFechaDesde.equals("") ? "" : anteriorFechaDesde%>">&nbsp;<a href="#" ><img src="imagenes/enl_calendario_hab.gif" border="0" id="lanza_calendario1"></a>
        	</td>
        	<td style="border:0">
            	<%String anteriorFechaHasta=(parametros==null)? "": parametros.getFechaHasta();%>
            	<input type="text" readonly="true" name="fechaHasta" id="fechaHasta" size="10" maxlength="10" value="<%=anteriorFechaHasta.equals("") ? "" : anteriorFechaHasta%>">&nbsp;<a href="#" ><img src="imagenes/enl_calendario_hab.gif" border="0" id="lanza_calendario2"></a>
        	</td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <!--  
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='mostrar_nombre_total_registres_trobats'/>:<input type="checkbox" name="veureNombreTotalRegistres" id="veureNombreTotalRegistres"/>
            </td>
            <td style="border:0">&nbsp;</td>
			<td style="border:0">&nbsp;</td>            
        </tr>
        -->
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
        <td style="border:0" colspan="3">
            <p align="center">
            <input type="submit" value="<fmt:message key='cercar'/>">&nbsp;&nbsp;&nbsp;
            </p>
        </td>
        </tr>
        </form>
        </table>
        &nbsp;<br/>
        <div align="center">
            [&nbsp;<a href="busquedaOficiosIndex.jsp"><fmt:message key='tornar'/></a>&nbsp;]
        </div>
        <script>
            Calendar.setup(
            {
            inputField  : "fechaDesde",      // ID of the input field
            ifFormat    : "%d/%m/%Y",    // the date format
            button      : "lanza_calendario1"    // ID of the button
            }
            );
            Calendar.setup(
            {
            inputField  : "fechaHasta",      // ID of the input field
            ifFormat    : "%d/%m/%Y",    // the date format
            button      : "lanza_calendario2"    // ID of the button
            }
            );
        </script>
        <script language="JavaScript">
			document.busquedaForm.oficinaDesde.focus();
		</script>
		<script type="text/javascript">
		refrescaFisica();
		</script>
    </body>
</html>
