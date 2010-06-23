<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ page pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<!--
  Registro General CAIB - Registro de Salidas
-->

<%@ page import = "java.util.*, es.caib.regweb.logic.interfaces.*, es.caib.regweb.logic.util.*, es.caib.regweb.logic.helper.*" %>
<%String usuario=request.getRemoteUser();
String motivo=request.getParameter("motius");
%>

<%
LineaOficioRemisionFacade lin = LineaOficioRemisionFacadeUtil.getHome().create();
ParametrosLineaOficioRemision param = new ParametrosLineaOficioRemision();

String listareg = request.getParameter("registre");
String [] registros = listareg.split("\\|");
for (int i=0; registros!=null && i<registros.length;i++) {
		String [] campos = registros[i].split("-");
		
		param.setOficinaEntrada(campos[0]);
		param.setNumeroEntrada(campos[1]);
		param.setAnoEntrada(campos[2]);
		param.setDescartadoEntrada("S");
		param.setMotivosDescarteEntrada(motivo);
		param.setUsuarioEntrada(usuario);
		param.setAnoOficio(null);
		param.setOficinaOficio(null);
		param.setNumeroOficio(null);
		lin.grabar(param);
}

%>
            <jsp:forward page="RemiEntradaLis.jsp" />
