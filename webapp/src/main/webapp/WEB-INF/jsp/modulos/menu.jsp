<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="container-fluid">

    <div class="navbar-header">
        <%--Si el usuario es SuperAdministrador--%>
        <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
            <c:if test="${loginInfo.configuracion.logoMenu != null}">
                <div class="govern-logo pull-left">
                    <img src="<c:url value="/archivo/${loginInfo.configuracion.logoMenu.id}"/>" alt="${loginInfo.configuracion.logoMenu.nombre}"/>
                </div>
            </c:if>
        </c:if>
        <%--Si el usuario no es SuperAdministrador--%>
        <c:if test="${loginInfo.rolActivo.nombre != 'RWE_SUPERADMIN'}">
            <c:if test="${loginInfo.entidadActiva == null}">
                <div class="govern-logo pull-left"><img src="<c:url value="/img/govern-logo.png"/>" width="70"
                                                        height="70" alt="Govern de les Illes Balears"/></div>
            </c:if>
            <c:if test="${loginInfo.entidadActiva != null}">
                <div class="govern-logo pull-left">
                    <c:if test="${loginInfo.entidadActiva.logoMenu != null}">
                        <img src="<c:url value="/archivo/${loginInfo.entidadActiva.logoMenu.id}"/>" alt="${loginInfo.entidadActiva.nombre}"/>
                    </c:if>
                    <c:if test="${loginInfo.entidadActiva.logoMenu == null}">
                        <img src="<c:url value="/img/govern-logo.png"/>" width="70" height="70" alt="Govern de les Illes Balears"/>
                    </c:if>
                </div>
            </c:if>
        </c:if>
        <div class="aplication-logo pull-left">
            <a href="<c:url value="/"/>">
                <img src="<c:url value="/img/logo-regweb3.png"/>" width="180" height="48" alt="Regweb3"/>
            </a>
        </div>


        <div class="pull-right main-menu">

            <ul class="nav navbar-nav pull-right navbar_right">

                <%--MENÚ USUARIO--%>
                <li class="dropdown">
                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-user"></i>
                        ${loginInfo.usuarioAutenticado.nombreCompleto}
                        <c:if test="${loginInfo.usuarioAutenticado.dib_user_rw}">
                            <i class="fa fa-print"></i>
                        </c:if>
                        <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <%--Idioma catalán--%>
                        <c:if test="${pageContext.response.locale == 'ca'}">
                            <li><a href="<c:url value="${requestScope.requestURI}?lang=es"/>"><i class="fa fa-bullhorn"></i> <spring:message code="menu.idioma.castellano"/></a></li>
                        </c:if>

                        <%--Idioma castellano--%>
                        <c:if test="${pageContext.response.locale == 'es'}">
                            <li><a href="<c:url value="${requestScope.requestURI}?lang=ca"/>"><i class="fa fa-bullhorn"></i> <spring:message code="menu.idioma.catalan"/></a></li>
                        </c:if>

                        <%--Notificaciones--%>
                        <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN' || loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                            <li>
                                <a href="<c:url value="/notificacion/list"/>">
                                    <i class="fa fa-envelope"></i> <spring:message code="notificacion.notificaciones"/>
                                </a>
                            </li>
                        </c:if>

                        <%--Datos usuario--%>
                        <li>
                            <a href="<c:url value="/usuario/${loginInfo.usuarioAutenticado.id}/edit"/>">
                                <i class="fa fa-gear"></i> <spring:message code="usuario.editar"/>
                            </a>
                        </li>

                    </ul>
                </li>


                <%--MENÚ ENTIDADES ADMINISTRADAS--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN' || loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasAnyAuthority('RWE_ADMIN','RWE_USUARI')">
                        <c:if test="${fn:length(loginInfo.entidades) > 1}">

                            <li class="dropdown">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-institution"></i> <spring:message code="entidad.entidades"/> <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <c:forEach var="entidad" items="${loginInfo.entidades}">
                                        <c:if test="${entidad.id != loginInfo.entidadActiva.id}">
                                            <li><a href="<c:url value="/cambioEntidad/${entidad.id}"/>"><i class="fa fa-institution"></i> ${entidad.nombre}</a></li>
                                        </c:if>
                                    </c:forEach>

                                </ul>
                            </li>

                        </c:if>
                    </sec:authorize>
                </c:if>

                <%--MENÚ OFICINAS--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasAuthority('RWE_USUARI')">
                        <c:if test="${fn:length(loginInfo.oficinasAcceso) > 1}">

                            <li class="dropdown">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-home"></i> <spring:message code="regweb.oficinas"/> <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu scrollable-menu">
                                    <c:forEach var="oficina" items="${loginInfo.oficinasAcceso}">
                                        <c:if test="${oficina.id != loginInfo.oficinaActiva.id}">
                                            <li><a href="<c:url value="/cambioOficina/${oficina.id}"/>"><i class="fa fa-home"></i> ${oficina.denominacion}
                                                <c:if test="${oficina.sirEnvio || oficina.sirRecepcion}">
                                                    <span class="label"><img src="<c:url value="/img/logo-SIR-azul.png"/>" width="19" alt="Oficina SIR" title="Oficina SIR" style="vertical-align: inherit"/></span>
                                                </c:if>
                                                </a>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </li>

                        </c:if>
                    </sec:authorize>
                </c:if>

                <%--MENÚ ROLES DE USUARIO--%>
                <c:if test="${fn:length(loginInfo.rolesAutenticado) == 1}">
                    <li class="dropdown"><a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-lock"></i> ${loginInfo.rolActivo.descripcion}</a></li>
                </c:if>

                <c:if test="${fn:length(loginInfo.rolesAutenticado) > 1}">

                    <li class="dropdown">
                        <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i
                                class="fa fa-lock"></i> ${loginInfo.rolActivo.descripcion} <i class="fa fa-caret-down"></i></a>
                        <ul class="dropdown-menu">

                            <c:forEach var="rol" items="${loginInfo.rolesAutenticado}">
                                <c:if test="${rol.id != loginInfo.rolActivo.id}">
                                    <li><a href="<c:url value="/rol/${rol.id}"/>"><i class="fa fa-briefcase"></i> ${rol.descripcion}</a></li>
                                </c:if>
                            </c:forEach>

                        </ul>
                    </li>

                </c:if>

            </ul>
            <%--Fin menu superior--%>
            <div class="clearfix"></div>

            <%--Inicio menú inferior--%>
            <div class="user-nav pull-right navbar-right">

                <%--MENÚ USUARIO REGISTRO--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasAuthority('RWE_USUARI')">
                        <c:import url="/WEB-INF/jsp/modulos/menuUsuario.jsp"/>
                    </sec:authorize>
                </c:if>

                <%--MENÚ ADMINISTRADOR ENTIDAD--%>
                <sec:authorize access="hasAuthority('RWE_ADMIN')">
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                        <c:import url="/WEB-INF/jsp/modulos/menuAdminEntidad.jsp"/>
                    </c:if>
                </sec:authorize>

                <%--MENÚ ADMINISTRADOR--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
                    <c:import url="/WEB-INF/jsp/modulos/menuAdministrador.jsp"/>
                </c:if>

            </div>

        </div>

    </div>

</div>