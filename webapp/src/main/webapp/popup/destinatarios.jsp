<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
%>

<html>
<head><title>Seleccioni destinatari</title>
	
	<script>
	function selecciona(cod, descod) {
 	   top.opener.setDestinatari(cod, descod);
 	   close();
	}
	</script>
	
	
</head>
<body>
<table width="100%" border="0">
<%
    String codOficina=request.getParameter("oficina");
    Vector destinos=valores.buscarDestinatarios(codOficina);
    int j=0;
    for (int i=0;i<destinos.size();i=i+3) {
            String codigoDestino=destinos.get(i).toString();
            String textoDestino=destinos.get(i+1).toString();
            String textoLlargDestino=destinos.get(i+2).toString();%>
     <tr class="<%=(((j/2)%2)==0)? "par":"impar"%>">
     <td><a href="javascript:selecciona(<%=codigoDestino%>,'<%=textoLlargDestino.replaceAll("'", "\\\\'")%>')"><%=codigoDestino%></a></td>
     <td><%=textoDestino%></td>
     <% j++;j++; %>
     </tr>
<%}%>
</table>
</body>
</html>
