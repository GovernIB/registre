<%@page import="java.util.*, java.text.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" contentType="text/html"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<%
    ValoresFacade valores = ValoresFacadeUtil.getHome().create();
    
    String codOficina=request.getParameter("oficina");
    String tipo=request.getParameter("tipo");
    String fEntrada=request.getParameter("fEntrada");
    String usuario=request.getRemoteUser().toUpperCase();
    
    String disquete="";
    
    String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
    String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
    String bloqueoAno=(session.getAttribute("bloqueoAnyo")==null) ? "" : (String)session.getAttribute("bloqueoAnyo");
    String bloqueoDisquete=(session.getAttribute("bloqueoDisquete")==null) ? "" : (String)session.getAttribute("bloqueoDisquete");

    DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaTest=dateF.parse(fEntrada);
    Calendar cal=Calendar.getInstance();
    cal.setTime(fechaTest);
    String ano=String.valueOf(cal.get(Calendar.YEAR));
    
    if (bloqueoOficina.equals(codOficina) && bloqueoTipo.equals(tipo) && bloqueoAno.equals(ano)) {
        disquete=bloqueoDisquete;
    } else {
        if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
            valores.liberarDisquete(bloqueoOficina, bloqueoTipo, bloqueoAno, usuario);
        }
        
        disquete=valores.buscarDisquete(codOficina, tipo, fEntrada, usuario, session);
    }

%>

<html>
    <head><title>Darrer disquet</title>
        <script>
            function selecciona() {
            close();
            }
        </script>
        
        
        
    </head>
    <body>
        <fmt:message key='oficina'/>:<b><%=codOficina%></b>
        <p>Darrer disquet:<b><%=disquete%></b><p>

        <center><a href="javascript:selecciona()"><fmt:message key='tancar'/></a></center>
    </body>
</html>
