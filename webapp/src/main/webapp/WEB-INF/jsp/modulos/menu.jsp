<%@ page import="es.caib.regweb.utils.Configuracio" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="container-fluid">

      <div class="navbar-header">

        <c:if test="${entidadActiva == null}">
            <div class="govern-logo pull-left"><img src="<c:url value="/img/govern-logo.png"/>" width="159" height="36" alt="Govern de les Illes Balears" /></div>
        </c:if>
        <c:if test="${entidadActiva != null}">
            <div class="govern-logo pull-left">
                <c:if test="${entidadActiva.logoMenu != null}">
                    <img src="<c:url value="/archivo/${entidadActiva.logoMenu.id}"/>" alt="${entidadActiva.nombre}" />
                </c:if>
                <c:if test="${entidadActiva.logoMenu == null}">
                    <img src="<c:url value="/img/govern-logo.png"/>" width="159" height="36" alt="Govern de les Illes Balears" />
                </c:if>
            </div>
        </c:if>
        <div class="aplication-logo pull-left">
          <a href="<c:url value="/"/>">
            <img src="<c:url value="/img/logo-regweb.png"/>" width="180" height="48" alt="Regweb"/>
          </a>
        </div>
        

        <div class="pull-right main-menu">

            <ul class="nav navbar-nav pull-right navbar_right">

                <%--MENÚ USUARIO--%>
                <li class="dropdown">
                    <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown">
                       <i class="fa fa-user"></i>
                       ${usuarioAutenticado.nombreCompleto}
                       <%-- ${loginInfo.usuarioAutenticado.nombreCompleto} --%> 
                       <i class="fa fa-caret-down"></i>
                    </a>
                    <ul class="dropdown-menu">
                        <c:if test="${pageContext.response.locale == 'ca'}">
                            <li><a href="<c:url value="${requestScope.requestURI}?lang=es"/>"><i class="fa fa-bullhorn"></i> <spring:message code="menu.idioma.castellano"/></a></li>
                        </c:if>
                        <c:if test="${pageContext.response.locale == 'es'}">
                            <li><a href="<c:url value="${requestScope.requestURI}?lang=ca"/>"><i class="fa fa-bullhorn"></i> <spring:message code="menu.idioma.catalan"/></a></li>
                        </c:if>
                        <li>
                        <%--    <a href="<c:url value="/usuario/${loginInfo.usuarioAutenticado.id}/edit"/>"> --%>
                            <a href="<c:url value="/usuario/${usuarioAutenticado.id}/edit"/>">
                              <i class="fa fa-gear"></i>
                               <spring:message code="menu.configuracion"/>
                            </a>
                        </li>

                    </ul>
                </li>


                <%--MENÚ ENTIDADES ADMINISTRADOR--%>
                <c:if test="${rolAutenticado.nombre == 'RWE_ADMIN' || rolAutenticado.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasAnyRole('RWE_ADMIN','RWE_USUARI')">
                        <c:if test="${fn:length(entidades) > 1}">

                            <li class="dropdown">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-globe"></i> <spring:message code="entidad.entidades"/> <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <c:forEach var="entidad" items="${entidades}">
                                        <c:if test="${entidad.id != entidadActiva.id}">
                                            <li><a href="<c:url value="/cambioEntidad/${entidad.id}"/>"><i class="fa fa-home"></i> ${entidad.nombre}</a></li>
                                        </c:if>
                                    </c:forEach>

                                </ul>
                            </li>

                        </c:if>
                    </sec:authorize>
                </c:if>

                <%--MENÚ OFICINAS--%>
                <c:if test="${rolAutenticado.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasRole('RWE_USUARI')">
                        <c:if test="${fn:length(oficinas) > 1}">

                            <li class="dropdown">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-globe"></i> <spring:message code="regweb.oficinas"/> <i class="fa fa-caret-down"></i></a>
                                <ul class="dropdown-menu">
                                    <c:forEach var="oficina" items="${oficinas}">
                                        <c:if test="${oficina.id != oficinaActiva.id}">
                                            <li><a href="<c:url value="/cambioOficina/${oficina.id}"/>"><i class="fa fa-home"></i> ${oficina.nombre}</a></li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </li>

                        </c:if>
                    </sec:authorize>
                </c:if>

                <%--MENÚ ROLES DE USUARIO--%>
                <c:if test="${fn:length(rolesAutenticado) == 1}">
                    <li class="dropdown"><a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-lock"></i> ${rolAutenticado.descripcion}</a></li>
                </c:if>

                <c:if test="${fn:length(rolesAutenticado) > 1}">

                    <li class="dropdown">
                        <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-lock"></i> ${rolAutenticado.descripcion} <i class="fa fa-caret-down"></i></a>
                        <ul class="dropdown-menu">

                            <c:forEach var="rol" items="${rolesAutenticado}">
                                <c:if test="${rol.id != rolAutenticado.id}">
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
                <c:if test="${rolAutenticado.nombre == 'RWE_USUARI'}">
                    <sec:authorize access="hasRole('RWE_USUARI')">

                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
                                <spring:message code="menu.entradas"/> <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li class="submenu-complet"><a href="<c:url value="/registroEntrada/new"/>"><spring:message code="registroEntrada.registroEntrada"/></a></li>
                                <li class="submenu-complet"><a href="<c:url value="/registroEntrada/reserva"/>"><spring:message code="registroEntrada.reserva"/></a></li>
                                <li class="submenu-complet"><a href="<c:url value="/registroEntrada/list"/>"><spring:message code="registroEntrada.listado"/></a></li>
                                <c:if test="${entidadActiva.sir && tienePreRegistros}">
                                    <li class="divider"></li>
                                    <li class="submenu-complet"><a href="<c:url value="/preRegistro/list"/>"><spring:message code="preRegistro.listado"/></a></li>
                                </c:if>

                                <%if(Configuracio.isCAIB()){%>
                                    <li class="submenu-complet"><a href="<%=Configuracio.getUrlPreregistre()%>" target="_blank"><spring:message code="regweb.preregistro.caib"/></a></li>
                                <%}%>

                                <li class="divider"></li>
                                <li class="dropdown-submenu-left toggle-left">
                                    <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="oficioRemision.oficiosRemision"/></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="<c:url value="/oficioRemision/oficiosPendientesLlegada"/>"><spring:message code="oficioRemision.pendientesLlegada"/></a></li>
                                        <li class="divider"></li>
                                        <li><a href="<c:url value="/oficioRemision/oficiosPendientesRemisionInterna"/>"><spring:message code="registroEntrada.oficiosRemisionInterna"/></a></li>
                                        <li><a href="<c:url value="/oficioRemision/oficiosPendientesRemisionExterna"/>"><spring:message code="registroEntrada.oficiosRemisionExterna"/></a></li>
                                        <li><a href="<c:url value="/oficioRemision/list"/>"><spring:message code="oficioRemision.listado"/></a></li>
                                    </ul>
                                </li>
                            </ul>
                        </div><!-- /btn-group -->

                        <div class="btn-group">
                            <button type="button" class="btn btn-danger btn-sm dropdown-toggle" data-toggle="dropdown">
                                <spring:message code="menu.salidas"/> <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li class="submenu-complet"><a href="<c:url value="/registroSalida/new"/>"><spring:message code="registroSalida.registroSalida"/></a></li>
                                <li class="submenu-complet"><a href="<c:url value="/registroSalida/list"/>"><spring:message code="registroSalida.listado"/></a></li>
                            </ul>
                        </div>


                        <div class="btn-group">
                            <button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown">
                               <spring:message code="menu.administracion"/> <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <c:if test="${entidadActiva != null}">
                                    <li class="submenu-complet"><a href="<c:url value="/persona/list"/>"><spring:message code="menu.personas"/></a></li>
                                </c:if>
                                <li class="submenu-complet"><a href="<c:url value="/repro/list"/>"><spring:message code="menu.repros"/></a></li>

                                <c:if test="${oficinaActiva != null}">
                                    <li class="divider"></li>
                                    <li class="dropdown-submenu-left toggle-left">
                                        <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="menu.estadisticas"/></a>
                                        <ul class="dropdown-menu">
                                            <li><a href="<c:url value="/informe/libroRegistro"/>"><spring:message code="menu.libro"/></a></li>
                                        </ul>
                                    </li>
                                </c:if>

                                <c:if test="${fn:length(librosAdministrados) > 0}">
                                    <li class="divider"></li>
                                    <li class="submenu-complet"><a href="<c:url value="/informe/usuarioLopd"/>"><spring:message code="informe.usuarioLopd"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/informe/registroLopd"/>"><spring:message code="informe.registroLopd"/></a></li>
                                </c:if>

                                <c:if test="${registrosMigrados}">
                                    <li class="divider"></li>
                                    <li class="submenu-complet"><a href="<c:url value="/registroMigrado/list"/>"><spring:message code="registroMigrado.consultaRegistro"/></a></li>
                                </c:if>
                                 
                                <li class="divider"></li>
                                <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Operador_de_RegWeb.pdf"/>" target="_blank" ><spring:message code="menu.manual.oper"/></a></li>

                            </ul>
                        </div><!-- /btn-group -->


                    </sec:authorize>
                </c:if>

                <%--MENÚ ADMINISTRACIÓN ENTIDADES--%>
                <sec:authorize access="hasRole('RWE_ADMIN')">
                    <c:if test="${rolAutenticado.nombre == 'RWE_ADMIN'}">
                        <c:if test="${entidadActiva != null}">

                            <div class="btn-group">
                                <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
                                        ${entidadActiva.nombre} <span class="caret"></span>
                                </button>
                                <ul class="dropdown-menu">
                                    <li class="submenu-complet"><a href="<c:url value="/entidad/${entidadActiva.id}/edit"/>"><spring:message code="menu.entidad.editar"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/organismo/list"/>"><spring:message code="menu.oficinas"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/entidad/usuarios"/>"><spring:message code="menu.usuarios"/></a></li>
                                    <c:if test="${entidadActiva.configuracionPersona != 1}">
                                        <li class="submenu-complet"><a href="<c:url value="/persona/list"/>"><spring:message code="menu.personas"/></a></li>
                                    </c:if>

                                    <%--<li class="submenu-complet"><a href="<c:url value="/dir3/datosCatalogo"/>" tabindex="-1"><spring:message code="menu.dir3"/></a></li>--%>
                                    <li class="divider"></li>
                                    <li class="dropdown-submenu-left toggle-left">
                                        <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="menu.estadisticas"/></a>
                                        <ul class="dropdown-menu">
                                            <li><a href="<c:url value="/informe/indicadores"/>"><spring:message code="informe.indicadores"/></a></li>
                                            <li><a href="<c:url value="/informe/libroRegistro"/>"><spring:message code="menu.libro"/></a></li>
                                        </ul>
                                    </li>
                                    <li class="divider"></li>

                                    <li class="submenu-complet"><a href="<c:url value="/informe/usuarioLopd"/>"><spring:message code="informe.usuarioLopd"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/informe/registroLopd"/>"><spring:message code="informe.registroLopd"/></a></li>
                                    <li class="submenu-complet"><a href="<c:url value="/registroMigrado/list"/>"><spring:message code="informe.migradoLopd"/></a></li>
                                    <li class="divider"></li>

                                    <li class="dropdown-submenu-left toggle-left">
                                        <a href="javascript:void(0);"><i class="fa fa-chevron-left"></i> <spring:message code="entidad.catalogoDatos"/></a>
                                        <ul class="dropdown-menu">
                                            <li><a href="<c:url value="/tipoDocumental/list/"/>"><spring:message code="menu.tipoDocumental"/></a></li>
                                            <li><a href="<c:url value="/tipoAsunto/list/"/>"><spring:message code="menu.tipoAsunto"/></a></li>
                                            <li><a href="<c:url value="/modeloRecibo/list/"/>" ><spring:message code="menu.modeloRecibo"/></a></li>
                                            <li><a href="<c:url value="/modeloOficioRemision/list/"/>" ><spring:message code="menu.modeloOficioRemision"/></a></li>
                                        </ul>
                                    </li>
                                    
                                    <li class="divider"></li>
                                    <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Administrador_Entitat_de_RegWeb.pdf"/>" target="_blank" ><spring:message code="menu.manual.aden"/></a></li>

                                </ul>
                            </div><!-- /btn-group -->

                        </c:if>
                    </c:if>
                </sec:authorize>

                <%--MENÚ ADMINISTRACIÓN--%>
                <c:if test="${rolAutenticado.nombre == 'RWE_SUPERADMIN'}">
                    <div class="btn-group">
                        <button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown">
                            <spring:message code="menu.configuracion"/> <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu">
                            <li class="submenu-complet"><a href="<c:url value="/entidad/list"/>"><spring:message code="entidad.entidades"/></a></li>
                            <li class="submenu-complet"><a href="<c:url value="/usuario/list"/>"><spring:message code="menu.usuarios"/></a></li>
                            <li class="submenu-complet"><a href="<c:url value="/dir3/datosCatalogo"/>" tabindex="-1"><spring:message code="menu.dir3"/></a></li>
                            <li class="divider"></li>
                            <li class="submenu-complet"><a href="<c:url value="/doc/Manual_de_Usuari_Administrador_de_RegWeb.pdf"/>" target="_blank" ><spring:message code="menu.manual.admin"/></a></li>
                        </ul>
                    </div><!-- /btn-group -->
                </c:if>

            </div>

        </div>

    </div>

</div>