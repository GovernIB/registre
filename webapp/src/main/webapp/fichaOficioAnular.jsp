<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>

<!--
  Registro General CAIB - Registro de Salidas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%String usuario=request.getRemoteUser();
String codOficina=request.getParameter("oficina");
String ano=request.getParameter("ano");
String numOficio=request.getParameter("oficio");
String motivo=request.getParameter("motius");
%>

<%
OficioRemisionFacade oficio = OficioRemisionFacadeUtil.getHome().create();
ParametrosOficioRemision param = new ParametrosOficioRemision();
ParametrosOficioRemision param2 = new ParametrosOficioRemision();

param.setAnoOficio(ano);
param.setOficinaOficio(codOficina);
param.setNumeroOficio(numOficio);
param2 = oficio.leer(param);
param2.setNulo("S");
param2.setMotivosNulo(motivo);
param2.setUsuarioNulo(usuario);
param2.setAnoEntrada(null);
param2.setOficinaEntrada(null);
param2.setNumeroEntrada(null);
param2.setFechaEntrada(null);
param2=oficio.actualizar(param2);
oficio.anular(param2);

%>
<%
String [] parametros=(String [])session.getAttribute("listadoOficios");

String oficinaSel=parametros[0];
String oficinaFisicaSel=parametros[1];
String anyoSel=parametros[2];

%>

            <jsp:forward page="listadoOficios.jsp" >
                <jsp:param name="oficina" value="<%=oficinaSel%>"/>
                <jsp:param name="oficinafisica" value="<%=oficinaFisicaSel%>"/>
                <jsp:param name="any" value="<%=anyoSel%>"/>
            </jsp:forward>
