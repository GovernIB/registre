<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ page import="es.caib.regweb3.utils.Configuracio" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--Menú REGISTRO ENTRADA--%>
<div class="btn-group">
    <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown">
        <spring:message code="menu.entradas"/> <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <%--<c:import url="/reprosUsuario/1"/>--%>
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/new"/>"><i class="fa fa-file-o"></i> <spring:message code="registroEntrada.nuevo"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/reserva"/>"><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.reserva"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/list"/>"><i class="fa fa-search"></i> <spring:message code="registroEntrada.listado"/></a></li>
        <li class="divider"></li>
        <c:if test="${loginInfo.entidadActiva.sir && loginInfo.oficinaActiva.sirRecepcion }">
            <li class="submenu-complet"><a href="<c:url value="/registroEntrada/pendientesDistribuirSir/list/1"/>"><i class="fa fa-sign-out"></i> <spring:message code="registroEntrada.pendientesDistribuir.sir"/></a></li>
        </c:if>
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/pendientesDistribuir/list/1"/>"><i class="fa fa-sign-out"></i> <spring:message code="registroEntrada.pendientesDistribuir"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/reservas/list/1"/>"><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.reservas"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/registroEntrada/pendientesVisar/list/1"/>"><i class="fa fa-check-square-o"></i> <spring:message code="registroEntrada.pendientesVisar"/></a></li>
        <%if (Configuracio.isCAIB()) {%>
            <li class="divider"></li>
            <li class="submenu-complet"><a href="<%=Configuracio.getUrlPreregistre()%>" target="_blank"><i class="fa fa-external-link"></i> <spring:message code="regweb.preregistro.caib"/></a></li>
        <%}%>

    </ul>
</div>

<%--Menú REGISTRO SALIDA--%>
<div class="btn-group">
    <button type="button" class="btn btn-danger btn-sm dropdown-toggle" data-toggle="dropdown">
        <spring:message code="menu.salidas"/> <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <li class="submenu-complet"><a href="<c:url value="/registroSalida/new"/>"><i class="fa fa-file-o"></i> <spring:message code="registroSalida.nuevo"/></a></li>
        <li class="submenu-complet"><a href="<c:url value="/registroSalida/list"/>"><i class="fa fa-search"></i> <spring:message code="registroSalida.listado"/></a></li>
    </ul>
</div>

<%--Menú OFICIO REMISIÓN--%>
<c:if test="${loginInfo.entidadActiva.oficioRemision}">
    <div class="btn-group">
        <button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown">
            <spring:message code="menu.oficiosRemision"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li><a href="<c:url value="/oficioRemision/list"/>"><i class="fa fa-envelope-o"></i> <spring:message code="oficioRemision.listado"/></a></li>
            <li><a href="<c:url value="/oficioRemision/aceptados/list"/>"><i class="fa fa-envelope-open-o"></i> <spring:message code="oficioRemision.aceptados"/></a></li>
            <li class="divider"></li>
            <li><a href="<c:url value="/oficioRemision/pendientesLlegada/list"/>"><i class="fa fa-mail-reply"></i> <spring:message code="oficioRemision.pendientesLlegada"/></a></li>
            <li class="divider"></li>
            <li><a href="<c:url value="/oficioRemision/entradasPendientesRemision"/>"><i class="fa fa-mail-forward"></i> <spring:message code="registroEntrada.oficiosRemision"/></a></li>
            <li><a href="<c:url value="/oficioRemision/salidasPendientesRemision"/>"><i class="fa fa-mail-forward"></i> <spring:message code="registroSalida.oficiosRemision"/></a></li>

        </ul>
    </div>
</c:if>

<%--Menú SIR--%>
<c:if test="${loginInfo.entidadActiva.sir && (loginInfo.oficinaActiva.sirRecepcion || loginInfo.oficinaActiva.sirEnvio)}">
    <div class="btn-group">
        <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
            <spring:message code="menu.sir"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <c:if test="${loginInfo.oficinaActiva.sirRecepcion}">
                <li><a href="<c:url value="/registroSir/list"/>"><i class="fa fa-mail-reply"></i> <spring:message code="registroSir.recibidos"/></a></li>
            </c:if>
            <c:if test="${loginInfo.oficinaActiva.sirEnvio}">
                <li><a href="<c:url value="/registroSir/enviados"/>"><i class="fa fa-mail-forward"></i> <spring:message code="registroSir.enviados"/></a></li>
            </c:if>
            <li class="divider"></li>
            <c:if test="${loginInfo.oficinaActiva.sirRecepcion}">
                <li><a href="<c:url value="/registroSir/pendientesProcesar/list"/>"><i class="fa fa-refresh fa-spin"></i> <spring:message code="registroSir.pendientesProcesar"/></a></li>
            </c:if>

            <c:if test="${loginInfo.oficinaActiva.sirEnvio}">
                <li><a href="<c:url value="/registroEntrada/pendientesSir/list/1"/>"><i class="fa fa-warning"></i> <spring:message code="registroEntrada.pendientesSir"/></a></li>
                <li><a href="<c:url value="/registroSalida/pendientesSir/list/1"/>"><i class="fa fa-warning"></i> <spring:message code="registroSalida.pendientesSir"/></a></li>
            </c:if>

        </ul>
    </div>
</c:if>

<%--Menú ADMINISTRACIÓN--%>
<div class="btn-group">
    <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
        <spring:message code="menu.administracion"/> <span class="caret"></span>
    </button>
    <ul class="dropdown-menu">
        <c:if test="${loginInfo.entidadActiva != null}">
            <li class="submenu-complet"><a href="<c:url value="/persona/list"/>"><i class="fa fa-address-book-o"></i> <spring:message code="menu.personas"/></a></li>
        </c:if>
        <li class="submenu-complet"><a href="<c:url value="/repro/list"/>"><i class="fa fa-briefcase"></i> <spring:message code="menu.repros"/></a></li>

        <c:if test="${loginInfo.oficinaActiva != null}">
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message
                        code="menu.estadisticas"/></a>
                <ul class="dropdown-menu">
                    <c:if test="${fn:length(loginInfo.librosAdministrados) > 0}">
                        <li><a href="<c:url value="/informe/indicadores"/>"><i class="fa fa-bar-chart"></i> <spring:message code="informe.indicadores"/></a></li>
                        <li><a href="<c:url value="/informe/indicadoresOficina"/>"><i class="fa fa-bar-chart"></i> <spring:message code="informe.indicadoresOficina"/></a></li>
                    </c:if>
                    <li><a href="<c:url value="/informe/libroRegistro"/>"><i class="fa fa-book"></i> <spring:message code="menu.libro"/></a></li>
                    <c:if test="${fn:length(loginInfo.librosAdministrados) > 0}">
                        <li class="divider"></li>
                        <li><a href="<c:url value="/informe/usuarioLopd"/>"><i class="fa fa-eye"></i> <spring:message code="informe.usuarioLopd"/></a></li>
                        <li><a href="<c:url value="/informe/registroLopd"/>"><i class="fa fa-eye"></i> <spring:message code="informe.registroLopd"/></a></li>
                    </c:if>
                </ul>
            </li>
        </c:if>

        <c:if test="${loginInfo.registrosMigrados}">
            <li class="divider"></li>
            <li class="submenu-complet"><a href="<c:url value="/registroMigrado/list"/>"><i class="fa fa-exchange"></i> <spring:message code="registroMigrado.consultaRegistro"/></a></li>
        </c:if>

        <c:if test="${loginInfo.enlaceDir3}">
            <li class="divider"></li>
            <li class="submenu-complet"><a href="<c:url value="<%=PropiedadGlobalUtil.getDir3CaibServer()%>"/>" target="_blank"><i class="fa fa-institution"></i> <spring:message code="menu.dir3caib"/></a></li>
        </c:if>

        <li class="divider"></li>
        <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Operador_de_RegWeb3.pdf"/>" target="_blank"><i class="fa fa-file-pdf-o"></i> <spring:message code="menu.manual.oper"/></a></li>

    </ul>
</div>
<!-- /btn-group -->