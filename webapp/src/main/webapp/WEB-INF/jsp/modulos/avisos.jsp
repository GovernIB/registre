<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${rolAutenticado.nombre == 'RWE_USUARI' && oficinaActiva != null}">

    <c:set var="total" value="${pendientesVisarEntrada + pendientesVisarSalida + pendientes + oficiosRemisionInterna + oficiosRemisionExterna + oficiosPendientesLlegada}"/>

    <c:if test="${total > 0}">

        <ul class="list-inline pull-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                    <i class="fa fa-bell fa-fw"></i>(${total}) <i class="fa fa-caret-down"></i>
                </a>

                <ul class="dropdown-menu pull-right">
                    <c:if test="${pendientesVisarEntrada > 0}">
                        <li>
                            <a href="<c:url value="/avisos/pendientesVisar/Entrada"/>">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> <spring:message code="registroEntrada.pendientesVisar"/> (${pendientesVisarEntrada})
                                    <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${pendientesVisarSalida > 0}">
                        <li>
                            <a href="<c:url value="/avisos/pendientesVisar/Salida"/>">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> <spring:message code="registroSalida.pendientesVisar"/> (${pendientesVisarSalida})
                                        <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${pendientes > 0}">
                        <li>
                            <a href="<c:url value="/avisos/pendientes"/>">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> <spring:message code="registroEntrada.reserva"/> (${pendientes})
                                    <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosRemisionInterna > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/oficiosPendientesRemisionInterna"/>">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> <spring:message code="oficioRemision.pendientesRemisionInterna"/> (${oficiosRemisionInterna})
                                    <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosRemisionExterna > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/oficiosPendientesRemisionExterna"/>">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> <spring:message code="oficioRemision.pendientesRemisionExterna"/> (${oficiosRemisionExterna})
                                    <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosPendientesLlegada > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/oficiosPendientesLlegada"/>">
                                <div>
                                    <i class="fa fa-comment fa-fw"></i> <spring:message code="oficioRemision.pendientesLlegada"/> (${oficiosPendientesLlegada})
                                    <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <li>
                        <div>
                         <span class="pull-right text-muted small">
                             <c:set var="ahora" value="<%=new java.util.Date()%>" />
                             <spring:message code="aviso.generado"/> <fmt:formatDate type="time" value="${ahora}" />
                         </span>
                        </div>
                    </li>

                </ul>
                <!-- /.dropdown-alerts -->
            </li>
        </ul>
    </c:if>
        <%--<li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                <i class="fa fa-envelope fa-fw"></i>  <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu pull-right">
                <li>
                    <a href="javascript:void(0);">
                        <div>
                            <strong>John Smith</strong>
                                <span class="pull-right text-muted">
                                    <em>Yesterday</em>
                                </span>
                        </div>
                        <div>Lorem ipsum dolor sit amet, consectetur adipiscing elit1.</div>
                    </a>
                </li>
                <li class="divider"></li>
                <li>
                    <a href="javascript:void(0);">
                        <div>
                            <strong>John Smith</strong>
                                <span class="pull-right text-muted">
                                    <em>Yesterday</em>
                                </span>
                        </div>
                        <div>Lorem ipsum dolor sit amet, consectetur adipiscing elit2.</div>
                    </a>
                </li>

                <li class="divider"></li>
                <li>
                    <a class="text-center" href="javascript:void(0);">
                        <strong>Read All Messages</strong>
                        <i class="fa fa-angle-right"></i>
                    </a>
                </li>
            </ul>
            <!-- /.dropdown-messages -->
        </li>--%>

        <!-- /.dropdown -->
        <%--<li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                <i class="fa fa-user fa-fw"></i>  <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu pull-right">
                <li><a href="javascript:void(0);"><i class="fa fa-user fa-fw"></i> User Profile</a>
                </li>
                <li><a href="javascript:void(0);"><i class="fa fa-gear fa-fw"></i> Settings</a>
                </li>
                <li class="divider"></li>
                <li><a href="login.html"><i class="fa fa-sign-out fa-fw"></i> Logout</a>
                </li>
            </ul>
            <!-- /.dropdown-user -->
        </li>--%>
        <!-- /.dropdown -->

</c:if>