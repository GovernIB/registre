<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${loginInfo.entidadActiva != null}">

    <div class="btn-group">
        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
            <spring:message code="menu.configuracion"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li class="submenu-complet"><a href="<c:url value="/entidad/${loginInfo.entidadActiva.id}/edit"/>"><i class="fa fa-institution"></i> <spring:message code="menu.entidad.editar"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/plugin/list"/>"><i class="fa fa-plug"></i> <spring:message code="plugin.plugins"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/propiedadGlobal/list"/>"><i class="fa fa-list"></i> <spring:message code="propiedadGlobal.propiedadesGlobales"/></a></li>
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="organismo.organismos"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/organismo/list"/>"><i class="fa fa-search"></i> <spring:message code="organismo.listado"/></a></li>
                    <li><a href="<c:url value="/organismo/arbolList"/>"><i class="fa fa-sitemap"></i> <spring:message code="organismo.organigrama"/></a></li>
                    <li class="divider"></li>
                    <li><a href="<c:url value="/libro/list"/>"><i class="fa fa-list-ol"></i> <spring:message code="libro.listado"/></a></li>
                    <li><a href="<c:url value="/entidad/librosCambiar"/>"><i class="fa fa-book"></i> <spring:message code="entidad.cambiarlibros"/></a></li>
                    <li class="divider"></li>
                    <li><a href="<c:url value="/entidad/descargas/list"/>"><i class="fa fa-refresh"></i> <spring:message code="organismo.sincronizaciones"/></a></li>
                </ul>
            </li>
            <li class="divider"></li>

            <li class="submenu-complet"><a href="<c:url value="/entidad/usuarios"/>"><i class="fa fa-users"></i> <spring:message code="menu.usuarios"/></a></li>
            <c:if test="${loginInfo.entidadActiva.configuracionPersona != 1}">
                <li class="submenu-complet"><a href="<c:url value="/persona/list"/>"><i class="fa fa-address-book-o"></i> <spring:message code="menu.personas"/></a></li>
            </c:if>

            <c:if test="${loginInfo.registrosMigrados}">
                <li class="divider"></li>
                <li class="submenu-complet"><a href="<c:url value="/registroMigrado/list"/>"><i class="fa fa-exchange"></i> <spring:message code="informe.migradoLopd"/></a></li>
            </c:if>
            <li class="divider"></li>

            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="entidad.catalogoDatos"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/tipoDocumental/list/"/>"><i class="fa fa-file-text-o"></i> <spring:message code="menu.tipoDocumental"/></a></li>
                    <li><a href="<c:url value="/tipoAsunto/list/"/>"><i class="fa fa-database"></i> <spring:message code="menu.tipoAsunto"/></a></li>
                    <li><a href="<c:url value="/modeloOficioRemision/list/"/>"><i class="fa fa-file-code-o"></i> <spring:message code="menu.modeloOficioRemision"/></a></li>
                </ul>
            </li>

            <li class="divider"></li>
            <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Administrador_Entitat_de_RegWeb3.pdf"/>"
                                           target="_blank"><i class="fa fa-file-pdf-o"></i> <spring:message code="menu.manual.aden"/></a></li>

        </ul>
    </div>

    <%--MENÚ MONITORIZACIÓN--%>
    <div class="btn-group">
        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
            <spring:message code="menu.monitorizacion"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li class="submenu-complet"><a href="<c:url value="/integracion/list/0"/>"><i class="fa fa-gears"></i> <spring:message code="integracion.integraciones"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/cola/list/0"/>"><i class="fa fa-ellipsis-h"></i> <spring:message code="cola.colas"/></a></li>
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="menu.estadisticas"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/informe/indicadores"/>"><i class="fa fa-bar-chart"></i> <spring:message code="informe.indicadores"/></a></li>
                    <li><a href="<c:url value="/informe/indicadoresOficina"/>"><i class="fa fa-bar-chart"></i> <spring:message code="informe.indicadoresOficina"/></a></li>
                    <li><a href="<c:url value="/informe/libroRegistro"/>"><i class="fa fa-book"></i> <spring:message code="menu.libro"/></a></li>
                    <li class="divider"></li>
                    <li><a href="<c:url value="/informe/usuarioLopd"/>"><i class="fa fa-eye"></i> <spring:message code="informe.usuarioLopd"/></a></li>
                    <li><a href="<c:url value="/informe/registroLopd"/>"><i class="fa fa-eye"></i> <spring:message code="informe.registroLopd"/></a></li>
                </ul>
            </li>
            <li class="divider"></li>
            <li class="submenu-complet"><a href="<c:url value="/persona/personasDuplicadas/"/>"><i class="fa fa-eraser"></i> <spring:message code="persona.buscador.duplicadas"/></a></li>
        </ul>


    </div>

    <%--MENÚ SIR--%>
    <c:if test="${loginInfo.entidadActiva.sir}">
        <div class="btn-group">
            <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
                <spring:message code="menu.sir"/> <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li class="submenu-complet"><a href="<c:url value="/integracion/list/4"/>"><i class="fa fa-gears"></i> <spring:message code="integracion.sir"/></a></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/monitorEnviados"/>"><i class="fa fa-refresh"></i> <spring:message code="sir.monitor.envios"/></a></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/monitorRecibidos"/>"><i class="fa fa-refresh"></i> <spring:message code="sir.monitor.recibidos"/></a></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/pendientesDistribuir/list"/>"><i class="fa fa-sign-out"></i> <spring:message code="menu.pendientesDistribuir"/></a></li>
            </ul>

        </div>
    </c:if>

</c:if>