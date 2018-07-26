<%@ page import="es.caib.regweb3.utils.Configuracio" %>
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
                        <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <c:if test="${pageContext.response.locale == 'ca'}">
                            <li><a href="<c:url value="${requestScope.requestURI}?lang=es"/>"><i class="fa fa-bullhorn"></i> <spring:message code="menu.idioma.castellano"/></a></li>
                        </c:if>
                        <c:if test="${pageContext.response.locale == 'es'}">
                            <li><a href="<c:url value="${requestScope.requestURI}?lang=ca"/>"><i class="fa fa-bullhorn"></i> <spring:message code="menu.idioma.catalan"/></a></li>
                        </c:if>
                        <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN' || loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                            <li>
                                <a href="<c:url value="/notificacion/list"/>">
                                    <i class="fa fa-envelope"></i> <spring:message code="notificacion.notificaciones"/> (${notificacionesPendientes})
                                </a>
                            </li>
                        </c:if>

                        <li>
                            <a href="<c:url value="/usuario/${loginInfo.usuarioAutenticado.id}/edit"/>">
                                <i class="fa fa-gear"></i> <spring:message code="menu.configuracion"/>
                            </a>
                        </li>

                    </ul>
                </li>


                <%--MENÚ ENTIDADES ADMINISTRADOR--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN' || loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasAnyRole('RWE_ADMIN','RWE_USUARI')">
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
                    <sec:authorize access="hasRole('RWE_USUARI')">
                        <c:if test="${fn:length(loginInfo.oficinasRegistro) > 1}">

                            <li class="dropdown">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-home"></i> <spring:message code="regweb.oficinas"/> <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu scrollable-menu">
                                    <c:forEach var="oficina" items="${loginInfo.oficinasRegistro}">
                                        <c:if test="${oficina.id != loginInfo.oficinaActiva.id}">
                                            <li><a href="<c:url value="/cambioOficina/${oficina.id}"/>"><i class="fa fa-home"></i> ${oficina.denominacion}</a></li>
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

                <%--MENÚ REGISTRO ENTRADA Y SALIDA--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasRole('RWE_USUARI')">

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
                                <%if (Configuracio.isCAIB()) {%>
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
                        <c:if test="${loginInfo.entidadActiva.sir && loginInfo.oficinaActiva.sirRecepcion}">
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
                                    <spring:message code="menu.sir"/> <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a href="<c:url value="/registroSir/list"/>"><i class="fa fa-mail-reply"></i> <spring:message code="registroSir.recibidos"/></a></li>
                                    <li><a href="<c:url value="/registroSir/enviados"/>"><i class="fa fa-mail-forward"></i> <spring:message code="registroSir.enviados"/></a></li>
                                    <li class="divider"></li>
                                    <li><a href="<c:url value="/registroSir/pendientesProcesar/list"/>"><i class="fa fa-refresh fa-spin"></i> <spring:message code="registroSir.pendientesProcesar"/></a></li>
                                    <li><a href="<c:url value="/registroEntrada/pendientesSir/list/1"/>"><i class="fa fa-warning"></i> <spring:message code="registroEntrada.pendientesSir"/></a></li>
                                    <li><a href="<c:url value="/registroSalida/pendientesSir/list/1"/>"><i class="fa fa-warning"></i> <spring:message code="registroSalida.pendientesSir"/></a></li>
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

                                <li class="divider"></li>
                                <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Operador_de_RegWeb3.pdf"/>" target="_blank"><i class="fa fa-file-pdf-o"></i> <spring:message code="menu.manual.oper"/></a></li>

                            </ul>
                        </div>
                        <!-- /btn-group -->


                    </sec:authorize>
                </c:if>

                <%--MENÚ ADMINISTRACIÓN ENTIDADES--%>
                <sec:authorize access="hasRole('RWE_ADMIN')">
                    <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                        <c:if test="${loginInfo.entidadActiva != null}">

                            <div class="btn-group">
                                <button type="button" class="btn btn-warning btn-sm dropdown-toggle"
                                        data-toggle="dropdown">
                                        ${loginInfo.entidadActiva.nombre} <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li class="submenu-complet"><a href="<c:url value="/entidad/${loginInfo.entidadActiva.id}/edit"/>"><i class="fa fa-institution"></i> <spring:message code="menu.entidad.editar"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/plugin/list"/>"><i class="fa fa-plug"></i> <spring:message code="plugin.plugins"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/propiedadGlobal/list"/>"><i class="fa fa-list"></i> <spring:message code="propiedadGlobal.propiedadesGlobales"/></a></li>
                                    <li class="divider"></li>
                                    <li class="submenu-complet"><a href="<c:url value="/integracion/list/0"/>"><i class="fa fa-gears"></i> <spring:message code="integracion.integraciones"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/cola/list/0"/>"><i class="fa fa-ellipsis-h"></i> <spring:message code="cola.colas"/></a></li>
                                    <li class="divider"></li>
                                    <c:if test="${loginInfo.entidadActiva.sir}">
                                        <li class="dropdown-submenu-left toggle-left">
                                            <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="menu.sir"/></a>
                                            <ul class="dropdown-menu">
                                                <li><a href="<c:url value="/integracion/list/4"/>"><i class="fa fa-gears"></i> <spring:message code="integracion.sir"/></a></li>
                                                <li><a href="<c:url value="/sir/monitorEnviados"/>"><i class="fa fa-refresh"></i> <spring:message code="sir.monitor.envios"/></a></li>
                                            </ul>
                                        </li>
                                        <li class="divider"></li>
                                    </c:if>
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
                                        <li class="submenu-complet"><a href="<c:url value="/persona/personasDuplicadas/"/>"><i class="fa fa-eraser"></i> <spring:message code="persona.buscador.duplicadas"/></a></li>
                                    </c:if>

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

                                    <c:if test="${loginInfo.registrosMigrados}">
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
                            <!-- /btn-group -->

                        </c:if>
                    </c:if>
                </sec:authorize>

                <%--MENÚ ADMINISTRACIÓN--%>
                <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN'}">
                    <div class="btn-group">
                        <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
                            <spring:message code="menu.configuracion"/> <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li class="submenu-complet"><a href="<c:url value="/entidad/list"/>"><i class="fa fa-institution"></i> <spring:message code="entidad.entidades"/></a>
                                    <%--<ul class="dropdown-menu">
                                        <li><a href="<c:url value="/admin/organismo/list"/>"><spring:message
                                                code="organismo.listado"/></a></li>
                                        <li><a href="<c:url value="/admin/oficina/list"/>"><spring:message
                                                code="oficina.listado"/></a></li>
                                    </ul>--%>
                            </li>

                            <li class="submenu-complet"><a href="<c:url value="/usuario/list"/>"><i class="fa fa-users"></i> <spring:message code="menu.usuarios"/></a></li>
                            <li class="submenu-complet"><a href="<c:url value="/plugin/list"/>"><i class="fa fa-plug"></i> <spring:message code="plugin.plugins"/></a></li>
                            <li class="submenu-complet"><a href="<c:url value="/propiedadGlobal/list"/>"><i class="fa fa-list"></i> <spring:message code="propiedadGlobal.propiedadesGlobales"/></a></li>
                            <li class="submenu-complet"><a href="<c:url value="/dir3/datosCatalogo"/>" tabindex="-1"><i class="fa fa-th"></i> <spring:message code="menu.dir3"/></a></li>
                            <li class="submenu-complet"><a href="<c:url value="/configuracion/editar"/>"><i class="fa fa-gear"></i> <spring:message code="regweb.apariencia"/></a></li>
                            <li class="divider"></li>
                            <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Administrador_de_RegWeb3.pdf"/>" target="_blank"><i class="fa fa-file-pdf-o"></i> <spring:message code="menu.manual.admin"/></a></li>
                        </ul>
                    </div>
                    <!-- /btn-group -->
                </c:if>

            </div>

        </div>

    </div>

</div>