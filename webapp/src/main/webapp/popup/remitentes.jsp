<%@page import="java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
ValoresFacade valores = ValoresFacadeUtil.getHome().create();
%>

<html>
    <head>
        <title>Seleccionar Remitents</title>
        <script>
            function selecciona(codEntidad1, codEntidad2, desc) {
            top.opener.setEntidad(codEntidad1, codEntidad2, desc);
            close();
            }
        </script>
        
        
        <link rel="shortcut icon" href="favicon.ico"/>
    </head>
    <body>
        <form>
        <%
            String subcadenaCodigo=(request.getParameter("subcadenaCodigo")==null) ? "" :request.getParameter("subcadenaCodigo").trim();
            String subcadenaTexto=(request.getParameter("subcadenaTexto")==null) ? "" : request.getParameter("subcadenaTexto").trim();
        %>
        <table width="100%" border="0">
            <%
            Vector remitentes=valores.buscarRemitentes(subcadenaCodigo, subcadenaTexto);
            for (int i=0;i<remitentes.size();i=i+3) {
                String entidad1=remitentes.get(i).toString();
                String entidad2=remitentes.get(i+1).toString();
                String texto=remitentes.get(i+2).toString();
                String entidades=entidad1+"-"+entidad2; 
                %>
            <tr class="<%=(((i/3)%2)==0)? "par":"impar"%>">
                <td><a href="javascript:selecciona('<%=entidad1%>', '<%=entidad2%>', '<%=texto.replaceAll("\'", "\\\\\'")%>')"><%=entidades%></a></td>
                <td><%=texto%></td>
            </tr>
            <%}%>
        </table>
    </body>
</html>

