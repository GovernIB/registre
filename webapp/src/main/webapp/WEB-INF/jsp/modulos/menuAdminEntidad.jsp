<%@ page import="es.caib.regweb3.persistence.utils.PropiedadGlobalUtil" %>
<%@ page import="es.caib.regweb3.utils.Configuracio" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${loginInfo.entidadActiva != null}">

    <%--MENÚ CONFIGURACIÓN--%>
    <div class="btn-group">
        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-dashboard"></i> <spring:message code="menu.configuracion"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li class="submenu-complet"><a href="<c:url value="/entidad/${loginInfo.entidadActiva.id}/edit"/>"><i class="fa fa-institution"></i> <spring:message code="menu.entidad.editar"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/plugin/list"/>"><i class="fa fa-plug"></i> <spring:message code="plugin.plugins"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/propiedadGlobal/list"/>"><i class="fa fa-list"></i> <spring:message code="propiedadGlobal.propiedadesGlobales"/></a></li>
            <li class="divider"></li>

            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="entidad.catalogoDatos"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/tipoDocumental/list/"/>"><i class="fa fa-file-text-o"></i> <spring:message code="menu.tipoDocumental"/></a></li>
                    <li><a href="<c:url value="/codigoAsunto/list/"/>"><i class="fa fa-database"></i> <spring:message code="menu.codigoAsunto"/></a></li>
                    <li><a href="<c:url value="/modeloOficioRemision/list/"/>"><i class="fa fa-file-code-o"></i> <spring:message code="menu.modeloOficioRemision"/></a></li>
                </ul>
            </li>

            <c:if test="${loginInfo.enlaceDir3}">
                <li class="divider"></li>
                <li class="submenu-complet"><a href="<c:url value="${loginInfo.dir3Caib.server}"/>" target="_blank"><i class="fa fa-institution"></i> <spring:message code="menu.dir3caib"/></a></li>
            </c:if>

            <c:if test="${loginInfo.entidadActiva.configuracionPersona != 1}">
                <li class="divider"></li>
                <li class="submenu-complet"><a href="<c:url value="/persona/list"/>"><i class="fa fa-address-book-o"></i> <spring:message code="menu.personas"/></a></li>
            </c:if>

            <c:if test="${loginInfo.registrosMigrados}">
                <li class="divider"></li>
                <li class="submenu-complet"><a href="<c:url value="/registroMigrado/list"/>"><i class="fa fa-exchange"></i> <spring:message code="informe.migradoLopd"/></a></li>
            </c:if>

            <li class="divider"></li>
            <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Administrador_Entitat_de_RegWeb3.pdf"/>"
                                           target="_blank"><i class="fa fa-file-pdf-o"></i> <spring:message code="menu.manual.aden"/></a></li>

        </ul>
    </div>

    <%--MENÚ ORGANIGRAMA--%>
    <div class="btn-group">
        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-institution"></i> <spring:message code="menu.organigrama"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li class="submenu-complet"><a href="<c:url value="/organismo/list"/>"><i class="fa fa-institution"></i> <spring:message code="organismo.listado"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/oficina/list"/>"><i class="fa fa-home"></i> <spring:message code="oficina.listado"/></a></li>
            <li class="submenu-complet"><a href="<c:url value="/organismo/arbol"/>"><i class="fa fa-sitemap"></i> <spring:message code="regweb.organigrama"/></a></li>
            <li class="divider"></li>
            <li class="submenu-complet"><a href="<c:url value="/usuarioEntidad/list"/>"><i class="fa fa-users"></i> <spring:message code="menu.usuarios"/></a></li>
            <li class="divider"></li>
            <li><a href="<c:url value="/entidad/descargas/list"/>"><i class="fa fa-refresh"></i> <spring:message code="organismo.sincronizaciones"/></a></li>
            <li><a href="<c:url value="/pendiente/list"/>"><i class="fa fa-refresh"></i> Pendientes de procesar</a></li>
        </ul>
    </div>

    <%--MENÚ MONITORIZACIÓN--%>
    <div class="btn-group">
        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-cogs"></i> <spring:message code="menu.monitorizacion"/> <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li class="dropdown-submenu-left toggle-left">
                <a href="<c:url value="/integracion/list/0"/>"><i class="fa fa-chevron-left"></i> <spring:message code="integracion.integraciones"/></a>
                <ul class="dropdown-menu">
                    <c:forEach items="${tiposIntegracion}" var="tipoIntegracion">
                        <li><a href="<c:url value="/integracion/list/${tipoIntegracion}"/>"><i class="fa fa-gears"></i> <spring:message code="integracion.tipo.${tipoIntegracion}" /></a></li>
                    </c:forEach>

                </ul>
            </li>
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="menu.colas"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/cola/list/0"/>"><i class="fa fa-sign-out"></i> <spring:message code="cola.tipo.0" /></a></li>
                    <spring:eval expression="@environment.getProperty('es.caib.regweb3.iscaib')" var="isCaib"/>
                    <c:if test="${isCaib}">
                        <li><a href="<c:url value="/cola/list/1"/>"><i class="fa fa-file-archive-o"></i> <spring:message code="cola.tipo.1" /></a></li>
                    </c:if>
                </ul>
            </li>
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="registro.registros"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/adminEntidad/registroEntrada/list"/>"><i class="fa fa-search"></i> <spring:message code="registroEntrada.buscador"/></a></li>
                    <li><a href="<c:url value="/adminEntidad/registroSalida/list"/>"><i class="fa fa-search"></i> <spring:message code="registroSalida.buscador"/></a></li>
                </ul>
            </li>
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="menu.estadisticas"/></a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/informe/indicadores"/>"><i class="fa fa-bar-chart"></i> <spring:message code="informe.indicadores"/></a></li>
                    <li><a href="<c:url value="/informe/indicadoresOficina"/>"><i class="fa fa-bar-chart"></i> <spring:message code="informe.indicadoresOficina"/></a></li>
                    <li><a href="<c:url value="/informe/registrosOrganismo"/>"><i class="fa fa-book"></i> <spring:message code="informe.organismos"/></a></li>
                    <li class="divider"></li>
                    <li><a href="<c:url value="/informe/usuarioLopd"/>"><i class="fa fa-eye"></i> <spring:message code="informe.usuarioLopd"/></a></li>
                    <li><a href="<c:url value="/informe/registroLopd"/>"><i class="fa fa-eye"></i> <spring:message code="informe.registroLopd"/></a></li>
                </ul>
            </li>
            <li class="divider"></li>
            <li class="dropdown-submenu-left toggle-left">
                <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> Purgado</a>
                <ul class="dropdown-menu">
                    <li><a href="<c:url value="/adminEntidad/purgarAnexosDistribuidos"/>"><i class="fa fa-eraser"></i> <spring:message code="menu.purgar.anexos.distribuidos"/></a></li>
                    <li><a href="<c:url value="/adminEntidad/purgarAnexosSir"/>"><i class="fa fa-eraser"></i> <spring:message code="menu.purgar.anexos.sir"/></a></li>
                    <li><a href="<c:url value="/adminEntidad/purgarAnexosAceptados"/>"><i class="fa fa-eraser"></i> <spring:message code="menu.purgar.anexos.aceptados"/></a></li>
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
                <i class="fa fa-exchange"></i> <spring:message code="menu.sir"/> <span class="caret"></span>
            </button>
            <ul class="dropdown-menu">
                <li class="submenu-complet"><a href="<c:url value="/integracion/list/4"/>"><i class="fa fa-gears"></i> <spring:message code="integracion.sir"/></a></li>
                <li class="divider"></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/monitorEnviados"/>"><i class="fa fa-mail-forward"></i> <spring:message code="registroSir.enviados.buscador"/></a></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/monitorRecibidos"/>"><i class="fa fa-mail-reply"></i> <spring:message code="registroSir.recibidos.buscador"/></a></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/mensajeControl/list"/>"><i class="fa fa-envelope-o"></i> <spring:message code="mensajeControl.mensajesControl"/></a></li>
                <li class="divider"></li>
                <li class="submenu-complet"><a href="<c:url value="/sir/pendientesDistribuir/list"/>"><i class="fa fa-sign-out"></i> <spring:message code="menu.pendientesDistribuir"/></a></li>
            </ul>

        </div>
    </c:if>

</c:if>