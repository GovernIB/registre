<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<% 

    RegistroModificadoEntradaFacade regmod = RegistroModificadoEntradaFacadeUtil.getHome().create();

    ValoresFacade valores = ValoresFacadeUtil.getHome().create();

    String usuario=request.getRemoteUser().toUpperCase();
    String oficina=request.getParameter("oficina");
 %>

<html>
<head><title><fmt:message key='registre_entrades'/></title>
    
    
    
</head>
<body>

     	<!-- Molla pa --> 
		<ul id="mollaPa">
		<li><a href="index.jsp"><fmt:message key='inici'/></a></li>
		<li><a href="VisaEntradaSel.jsp"><fmt:message key='seleccionar_oficina_a_visar'/></a></li>
		<li><fmt:message key='seleccionar_registre_entrada_a_visar'/></li>
		</ul>
		<!-- Fi Molla pa-->
<%
     List registros=regmod.recuperarRegistros(oficina, usuario);
     if (registros.size()==0) { %>
<p></p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<center><b><fmt:message key='no_shan_trobat_registres_que_compleixin_els_criteris_seleccionats'/></b></center>
&nbsp;<br/><center>[&nbsp;<a href="VisaEntradaSel.jsp"><fmt:message key='tornar_a_seleccionar'/></a>&nbsp;]</center>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
     <% } else { %>
<%--     
<div>
    <font color="red">Nota: Expedients en vermell estan anul·lats.</font><p>
</div>
--%>
<p></p>
<table width="90%" border="1" align="center">
    <tr>
    <td></td>
    <td align="center"><b><fmt:message key='oficina'/></b></td>
    <td align="center"><b>Registre</b></td>
    <td align="center"><b><fmt:message key='r'/></b></td>
    <td align="center"><b><fmt:message key='e'/></b></td>
    <td align="center"><b><fmt:message key='d_modif'/></b></td>
    <td align="center" width="50%"><b><fmt:message key='motiu_del_canvi'/></b></td>
    </tr>
<%    for (int i=0;i<registros.size();i++) {
            RegistroModificadoSeleccionado reg=(RegistroModificadoSeleccionado)registros.get(i);
            String numeroOficina=reg.getNumeroOficina()+" - "+valores.recuperaDescripcionOficina(reg.getNumeroOficina()+"");
            String registro=reg.getNumeroRegistro()+" / "+reg.getAnoRegistro();
            String R=reg.getVisadoR();
            String C=reg.getVisadoC();
            String fechaModif=reg.getFechaModificacion();
            String motivo=reg.getMotivoCambio();
%>
    <tr class="<%=((i%2)==0)? "par":"impar"%>"> 
     
        <td align="center">
        <a href="VisaEntrada.jsp?oficina=<%=reg.getNumeroOficina()%>&registro=<%=reg.getNumeroRegistro()%>&ano=<%=reg.getAnoRegistro()%>&fecha=<%=reg.getFechaModif()%>&hora=<%=reg.getHoraModif()%>">
            <img src="imagenes/open24.gif" border=0 alt="Visar cambi">
        </a>
        </td>
        <td><%=numeroOficina%></td>
        <td align="center"><%=registro%></td>
        <td align="center"><%=R.equals("") ? "&nbsp;" : R %></td>
        <td align="center"><%=C.equals("") ? "&nbsp;" : C %></td>
        <td align="center"><%=fechaModif%></td>
        <td><%=motivo%></td>
    </tr>
<% }%>
</table>
<%
	/* Per maquetar quan només apareixen pocs
	   registres al llistat */
	if ( registros.size()<10 ) {
		for (int i=9-registros.size();i>0;i--) {
            out.println("<p>&nbsp;</p>");
		}
	}

     }

 %>
 		
                 
		
	</body>
</html>