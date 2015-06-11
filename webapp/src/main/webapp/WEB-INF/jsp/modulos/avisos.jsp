<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${rolAutenticado.nombre == 'RWE_USUARI'}">
    <ul class="list-inline pull-right">
<c:set var="total" value="${pendientesVisar + pendientes + organismosOficioRemisionInterna + organismosOficioRemisionExterna + oficiosPendientesLlegada}"/>
        <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                <i class="fa fa-bell fa-fw"></i><c:if test="${total > 0}"> (${total})</c:if>  <i class="fa fa-caret-down"></i>
            </a>
            <c:set var="ahora" value="<%=new java.util.Date()%>" />
            <ul class="dropdown-menu pull-right">
                <c:if test="${pendientesVisar > 0}">
                    <li>
                        <a href="<c:url value="/registroEntrada/busqueda"/>">
                            <div>
                                <i class="fa fa-comment fa-fw"></i> Pendientes visar (${pendientesVisar})
                                <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                </c:if>

                <c:if test="${pendientes > 0}">
                    <li>
                        <a href="<c:url value="/registroEntrada/busqueda"/">
                            <div>
                                <i class="fa fa-comment fa-fw"></i> Pendientes (${pendientes})
                                <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                </c:if>

                <c:if test="${organismosOficioRemisionInterna > 0}">
                    <li>
                        <a href="<c:url value="/registroEntrada/busqueda"/">
                            <div>
                                <i class="fa fa-comment fa-fw"></i> <spring:message code="oficioRemision.pendientesRemisionInterna"/> (${organismosOficioRemisionInterna})
                                <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                </c:if>

                <c:if test="${organismosOficioRemisionExterna > 0}">
                    <li>
                        <a href="<c:url value="/registroEntrada/busqueda"/">
                            <div>
                                <i class="fa fa-comment fa-fw"></i> <spring:message code="oficioRemision.pendientesRemisionExterna"/> (${organismosOficioRemisionExterna})
                                <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                </c:if>

                <c:if test="${oficiosPendientesLlegada > 0}">
                    <li>
                        <a href="<c:url value="/registroEntrada/busqueda"/">
                            <div>
                                <i class="fa fa-comment fa-fw"></i> Oficis pendents d'arribada (${oficiosPendientesLlegada})
                                <%--<span class="pull-right text-muted small"><fmt:formatDate type="time" value="${ahora}" /></span>--%>
                            </div>
                        </a>
                    </li>
                    <li class="divider"></li>
                </c:if>


            </ul>
            <!-- /.dropdown-alerts -->
        </li>

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
    </ul>
</c:if>