<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<!--
  Registro General CAIB - Visado de Entradas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%
    RegistroModificadoEntradaFacade regmod = RegistroModificadoEntradaFacadeUtil.getHome().create();
    ParametrosRegistroModificado registroModificado = new ParametrosRegistroModificado();
    
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
    <jsp:forward page="VisaEntrada.jsp" />
<% }
   
    serie++;
   // intSerie++;
    intSerie=Integer.valueOf(String.valueOf(serie));
    session.setAttribute("serie", intSerie);
    session.removeAttribute("errorAtras");
    
    String oficina=(request.getParameter("oficina")==null) ? "0" : request.getParameter("oficina");
    String registro=(request.getParameter("registro")==null) ? "0" : request.getParameter("registro");
    String ano=(request.getParameter("ano")==null) ? "0" : request.getParameter("ano");
    String fecha=(request.getParameter("fecha")==null) ? "" : request.getParameter("fecha");
    String hora=(request.getParameter("hora")==null) ? "" : request.getParameter("hora");
    String remitenteCheck=(request.getParameter("remitente")==null) ? "" : request.getParameter("remitente");
    String extractoCheck=(request.getParameter("extracto")==null) ? "" : request.getParameter("extracto");
    String motivo=(request.getParameter("motivo")==null) ? "" : request.getParameter("motivo");

    String usuario=request.getRemoteUser().toUpperCase();
//  Visado del registro
    registroModificado.setOficina(Integer.parseInt(oficina));
    registroModificado.setNumeroRegistro(Integer.parseInt(registro));
    registroModificado.setAnoEntrada(Integer.parseInt(ano));
    registroModificado.setFechaModificacion(fecha);
    registroModificado.setHoraModificacion(hora);
    registroModificado=regmod.leer(registroModificado);

    if (!registroModificado.getLeido()) { 

%>
        <jsp:forward page="VisaEntradaSel.jsp">
            <jsp:param name="error" value="Visado no efectuado no se ha encontrado la modificacion"/>
        </jsp:forward>
<%
       }
    registroModificado.setMotivo(motivo);
    registroModificado.setUsuarioVisado(usuario);
    registroModificado.setVisarExtracto((extractoCheck.equals("S")) ? true : false);
    registroModificado.setVisarRemitente((remitenteCheck.equals("S")) ? true : false);
    registroModificado.setFechaModificacion(fecha);
    registroModificado.setHoraModificacion(hora);
%>

<html>
    <head><title><fmt:message key='registre_entrades'/></title>
        
        
        
    </head>
    <body>
        
     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="VisaEntradaSel.jsp"><fmt:message key='seleccionar_oficina_a_visar'/></a></li>		<li><fmt:message key='registre_entrada_visat'/></li>
		</ul>
		<!-- Fi Molla pa-->
		<p>&nbsp;</p>
<%    if (regmod.visar(registroModificado)) { %>

        <table class="recuadroEntradas" width="400" align="center">
            <tr>
            <td style="border:0;">
                &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro%>/<%=ano%>, Visat Correctament</b></center></p>
            </td>
            </tr>   
            <tr><td style="border:0;">&nbsp;</td></tr>
            <tr>
            <td style="border:0;">
                <p><center><b><fmt:message key='oficina'/>:&nbsp;<%=oficina%>-<%=valores.recuperaDescripcionOficina(oficina)%>
            </td>
            </tr>
            <tr><td style="border:0;">&nbsp;</td></tr>
            <tr>
                <td style="border:0;">
                    <p><center><a style="text-decoration: none;" type="button" class="botonFormulario" href="VisaEntradaSel.jsp">
                                    &nbsp;Nou visat&nbsp;
                                </a>
                        </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0;">&nbsp;</td></tr>
        </table>
<% } else { %>
        <table class="recuadro" width="400" align="center">
            <tr>
            <td style="border:0;">
                &nbsp;<br><center><b><fmt:message key='registre'/> <%=registro%>/<%=ano%>, <font color="red"><fmt:message key='no_sha_visat_correctament'/></font></b></center></p>
            </td>
            </tr>   
            <tr><td style="border:0;">&nbsp;</td></tr>
            <tr><td style="border:0;">&nbsp;</td></tr>
            <tr>
                <td style="border:0;">
                    <p><center><a style="text-decoration: none;" type="button" class="botonFormulario" href="VisaEntradaSel.jsp">
                                    &nbsp;Nou visat&nbsp;
                                </a>
                        </center>
                    </p>
                </td>
            </tr>
            <tr><td style="border:0;">&nbsp;</td></tr>
        </table>

<%    }


%>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
	<p>&nbsp;</p>
    
      
    
    </body>
</html>