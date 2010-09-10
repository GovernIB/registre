<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%String usuario=request.getRemoteUser(); %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<html>
<head><title><fmt:message key='registre_entrades'/></title>
<LINK TYPE="text/CSS" rel="stylesheet" HREF="estilos.css">


</head>

<body bgcolor="#FFFFFF">



<p>&nbsp;

<center><font class="titulo"><fmt:message key='usuari'/> : <%=usuario%></font></center><p><p>
<%
    ListadoRegistrosEntradaFacade listado=ListadoRegistrosEntradaFacadeUtil.getHome().create();
    ParametrosListadoRegistrosEntrada parametros = new ParametrosListadoRegistrosEntrada();
    Vector registros=listado.recuperar(parametros, usuario, 1000, 1);
    if (registros.size()==0) { 
%>
<p><p>
<center><b><fmt:message key='no_hi_ha_registres_per_a_aquest_usuari'/></B></center>
<% } else { %>

<table width="100%" border="1">
<tr>
<td> </TD>
<td><b><fmt:message key='any'/></b></td>
<td><b><fmt:message key='n_doc'/></B></TD>
<td><b><fmt:message key='oficina'/></B></td>
<td><B><fmt:message key='data_doc'/></B></td>
<td><B><fmt:message key='t_document'/></B></td>
<td><B><fmt:message key='remitent'/></B></td>
<td><B><fmt:message key='destinatari'/></B></td>
</TR>
<% 
   
   for (int i=0;i<registros.size();i++) {         
            ParametrosRegistroEntrada reg=(ParametrosRegistroEntrada)registros.get(i);
            String anoEntrada=reg.getAnoEntrada();
            String numeroEntrada=reg.getNumeroEntrada();
            String oficina=reg.getOficina();
            String textoOficina=reg.getDescripcionOficina();
            String fechaDocumento=reg.getData();
            String remitente=reg.getDescripcionRemitente();
            String destinatario=reg.getDescripcionOrganismoDestinatario();
            String tipoDocumento=reg.getDescripcionDocumento();
            String claveRegistro=anoEntrada+"-"+numeroEntrada;
%>
     <tr class="<%=((i%2)==0)? "par":"impar"%>"> 
     
     <td>
     <a href="ficha.jsp?oficina=<%=oficina%>&numeroEntrada=<%=numeroEntrada%>&anoEntrada=<%=anoEntrada%>">
     <img src="imagenes/open24.gif" border=0>
     </a>
     </TD> 
     <td><%=anoEntrada%></td>
     <td><%=numeroEntrada%></td>
     <td><%=oficina%>-<%=textoOficina%></td>
     <td><%=fechaDocumento%></td>
     <td><%=tipoDocumento%></td>
     <td><%=remitente%></td>
     <td><%=destinatario%></td>
     </tr>
<% }
 }%>
</table>

</body>
</html>
