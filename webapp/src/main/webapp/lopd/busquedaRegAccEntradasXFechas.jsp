<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<%String usuario=request.getRemoteUser();

ValoresFacade valores = ValoresFacadeUtil.getHome().create();

ParametrosListadoAcceso listado;
listado=(ParametrosListadoAcceso)session.getAttribute("listadoRegAcESEntrada");
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
%>

<html>
    <head><title><fmt:message key='registre_de_entrades'/></title>
        
        
        <jsp:include page="/jscripts/jscalendar/calendario.jsp" />
        
    </head>
    <body>
    
       	<!-- Molla pa --> 
		<ul id="mollaPa">
			<li><a href="<c:url value='/index.jsp'/>"><fmt:message key='inici'/></a></li>	
            <li><a href="<c:url value='/lopd/index_lopd.jsp'/>"><fmt:message key='registre_accessos_lopd'/></a></li>
			<li><fmt:message key='consulta_log_entrades'/></li>
		</ul>
		<!-- Fi Molla pa-->
    <p>&nbsp;</p>

        <center><font class="titulo"><fmt:message key='consulta_log_entrades'/></font></center>&nbsp;<p>
        <!-- Mostramos Errores si los hubiera -->
        <% Hashtable errores = (listado==null)?new Hashtable():listado.getErrores();
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
        <%  
        	/* Un cop mostrats els errors, eliminam el llistat.*/
        	session.removeAttribute("listadoRegAcESEntrada");
        	} else {
            if (listado==null) {
            } else {
                listado.inizializar();
            }
        }%>

        <form name="busquedaForm" action="listadoRegAccEntrada.jsp" method="post">
        <table align="center" width="70%" class="recuadroEntradas" cols="3">
        <tr>
        <td style="border:0">&nbsp;<br>&nbsp;</td>
        <td style="border:0" align="center">
        <b><fmt:message key='des_de'/></b>
        </td>
        <td style="border:0" align="center">
            <b><fmt:message key='fins'/></b>
        </td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='oficina'/>...........................:
            </td>
            <td style="border:0">
                <select name="oficinaDesde"> 
                    <% escribeSelect(out, "", valores.buscarOficinas(usuario,"AE"), (listado==null)? "":listado.getOficinaDesde());%>
                </select>
            </td>
            <td style="border:0">
                <select name="oficinaHasta">
                    <% escribeSelect(out, "", valores.buscarOficinas(usuario,"AE"), (listado==null)? "":listado.getOficinaHasta());%>
                </select>
            </td>
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></td>
        </tr>
        <tr>
        <td style="border:0">
            &nbsp;<br>&nbsp;<fmt:message key='data_operacio'/>............:
        </td>
        <td style="border:0">
            <%String anteriorFechaDesde=(listado==null)? "":listado.getFechaDesde();%>
            <input type="text" readonly="true" name="fechaDesde" id="fechaDesde" size="10" maxlength="10" value="<%=anteriorFechaDesde.equals("") ? "" : anteriorFechaDesde%>">&nbsp;<a href="#"  ><img src="<c:url value='/imagenes/enl_calendario_hab.gif'/>" border="0" id="lanza_calendario1"></a>
        </td>
        <td style="border:0">
            <%String anteriorFechaHasta=(listado==null)? "":listado.getFechaHasta();%>
            <input type="text" readonly="true" name="fechaHasta" id="fechaHasta" size="10" maxlength="10" value="<%=anteriorFechaHasta.equals("") ? "" : anteriorFechaHasta%>">&nbsp;<a href="#"  ><img src="<c:url value='/imagenes/enl_calendario_hab.gif'/>" border="0" id="lanza_calendario2"></a>
        </td>
        </tr>

        <tr>
            <td style="border:0" colspan="3" height="5px"></TD>
        </tr>
        <tr>
        <td style="border:0">
            &nbsp;<br>&nbsp;<fmt:message key='numero_de_registre'/>.........:
        </td>
        <td style="border:0">
            <%String anteriorNumRegistro=(listado==null)? "":listado.getNumRegistre();%>
            <input type="text" name="numRegistre" id="numRegistre" size="5" maxlength="5" value="<%=anteriorNumRegistro.equals("") ? "" : anteriorNumRegistro%>">
        </td>
		<td style="border:0"></td>
        </tr>

        <tr>
            <td style="border:0" colspan="3" height="5px"></TD>
        </tr>
        <tr>
        <td style="border:0">
            &nbsp;<br>&nbsp;<fmt:message key='any_registre'/>...................:
        </td>
        <td style="border:0">
            <%String anteriorAnyRegistre=(listado==null)? "":listado.getAnyRegistre();%>
            <input type="text" name="anyRegistre" id="anyRegistre" size="5" maxlength="5" value="<%=anteriorAnyRegistre.equals("") ? "" : anteriorAnyRegistre%>">
        </td>
		<td style="border:0"></td>
        </tr>
        <tr>
            <td style="border:0">
                &nbsp;<fmt:message key='mostrar_nombre_total_registres_trobats'/>:<input type="checkbox" name="veureNombreTotalRegistres" id="veureNombreTotalRegistres"/>
            </td>
            <td style="border:0">&nbsp;</td>
			<td style="border:0">&nbsp;</td>            
        </tr>
        <tr>
            <td style="border:0" colspan="3" height="5px"></TD>
        </tr>
        <tr>
        <td style="border:0" colspan="3">
            <p align="center">
            <input type=submit value="<fmt:message key='cercar'/>">&nbsp;&nbsp;&nbsp;
            </p>
        </td>
        </tr>
        </table>
        </form>
        <p>&nbsp;</p>
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
		<!-- PEU -->
        
		<!-- Fi PEU -->
    </body>
</html>
