<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%
    ParametrosListadoRegistrosSalida parametros = new ParametrosListadoRegistrosSalida();
    ListadoRegistrosSalidaFacade listado = ListadoRegistrosSalidaFacadeUtil.getHome().create();
    
    int oficina=(request.getParameter("oficina")==null) ? 0: Integer.parseInt(request.getParameter("oficina"));
    String any=request.getParameter("any");
    String usuario=request.getRemoteUser(); 

    String accion=(request.getParameter("accion")==null) ? "A": request.getParameter("accion");
    int numero=(request.getParameter("numeroUltimo")==null) ? 0: Integer.parseInt(request.getParameter("numeroUltimo"));
    int pagina=(request.getParameter("pagina")==null) ? 1: Integer.parseInt(request.getParameter("pagina"));
    int maxRegistros=21;

    Vector registros=listado.recuperarRegistrosOficina(usuario, maxRegistros, oficina, any, accion, numero);
  
    
    
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    String descripcionOficina=valores.recuperaDescripcionOficina(oficina+"");

%>

<html>
<head>
	<title>Seleccionar Registres de Sortides - <%=oficina%>&nbsp;<%=descripcionOficina%></title>
	<script>
		function selecciona(numeroSalida, anoSalida) {
		    top.opener.setRegistro(numeroSalida, anoSalida);
		    close();
		}
	</script>
	
	
	<link rel="shortcut icon" href="favicon.ico"/>
</head>
<body>
<%
    if (registros.size()>0) {
%>
    <table width="100%" border="0">
<%
    // Solamente mostramos 20 registros en pantalla
    int hasta=0;
    int desde=0;
    if (accion.equals("A")) {
        hasta=(registros.size()==maxRegistros) ? maxRegistros-1 : registros.size();
        desde=0;
    } else {
        desde=1;
        hasta=registros.size();
    }

    String anoSalida="";
    String numeroSalida="";
    String anoSalidaPrimero="";
    String numeroSalidaPrimero="";

        for (int i=desde;i<hasta;i++) {
            RegistroSeleccionado reg=(RegistroSeleccionado)registros.get(i);
            
            if (i==0) {
                numeroSalidaPrimero=reg.getNumeroEntrada();
                anoSalidaPrimero=reg.getAnoEntrada();
            }

            anoSalida=reg.getAnoEntrada();
            numeroSalida=reg.getNumeroEntrada();
            String remitente=reg.getDescripcionRemitente();
            String destinatario=reg.getDescripcionOrganismoDestinatario();
            String tipoDocumento=reg.getDescripcionDocumento();
            String claveRegistro=anoSalida+"-"+numeroSalida;
            boolean anulado=(reg.getRegistroAnulado().equals("") || reg.getRegistroAnulado().equals(" ")) ? false : true;
            
%>
     <tr class="<%=((i%2)==0)? "par": "impar"%>">
     <td><a href="javascript:selecciona('<%=numeroSalida%>', '<%=anoSalida%>')"><%=numeroSalida%>/<%=anoSalida%></a></td>
     <td style="<%= (anulado) ? "color:red;" : "" %>"><%=tipoDocumento%></td>
     <td style="<%= (anulado) ? "color:red;" : "" %>"><%=remitente%></td>
     <td style="<%= (anulado) ? "color:red;" : "" %>"><%=destinatario%></td>
     </tr>
 
<%
        }
%>
     <tr>
        <td colspan="3">&nbsp;</td>
     </tr>
     <tr>
        <td>
            <%
                    if (pagina>1) {
            %>
                <a href="listadoRegistrosSalidaAux.jsp?oficina=<%=oficina%>&any=<%=any%>&numeroUltimo=<%=numeroSalidaPrimero%>&anoUltimo=<%=anoSalidaPrimero%>&accion=R&pagina=<%=pagina-1%>" title="Retrocedir" style="text-decoration:none;"><<</a>
            <%
                    } else {
            %>
                    &nbsp;
            <%
                    }
            %>
        </td>
        <td colspan="2" align="center">
           <a style="text-decoration: none;" type="button" class="botonFormulario" onclick="window.close()">
                               &nbsp;<fmt:message key='tancar_sense_seleccionar'/>&nbsp;
           </a>
        </td>
        <td align="right">
            <% 
                if (registros.size()==maxRegistros) {
            %>
            <a href="listadoRegistrosSalidaAux.jsp?oficina=<%=oficina%>&any=<%=any%>&numeroUltimo=<%=numeroSalida%>&anoUltimo=<%=anoSalida%>&accion=A&pagina=<%=pagina+1%>" title="Avançar" style="text-decoration:none;">>></a>
            <%
                } else {
            %>
            &nbsp;
            <%
                }
            %>
        </td>
        </tr>


    </table>
<%
    } else {
%>
    &nbsp;<p>&nbsp;<p>&nbsp;<p>&nbsp;<p>&nbsp;<p>
    <div align="center" class="errorSinRegistros">
            <fmt:message key='no_shan_trobat_registres_que_compleixin_els_criteris_seleccionats'/>
    </div>
    &nbsp;<p>&nbsp;<p>
    <p><center>
            <a style="text-decoration: none;" type="button" class="botonFormulario" onclick="window.close()">
                               &nbsp;<fmt:message key='tancar'/>&nbsp;
             </a>
        </center>
    </p>

<%
    }
%>

</body>
</html>
