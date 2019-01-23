<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI' && loginInfo.oficinaActiva != null}">

    <c:set var="total" value="${pendientesVisarEntrada + pendientesVisarSalida + reservas + oficiosEntradaPendientesRemision + oficiosPendientesLlegada + oficiosSalidaPendientesRemision + entradasRechazadosReenviados + salidasRechazadasReenviadas + validos}"/>

    <c:if test="${total > 0}">

        <ul class="list-inline pull-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                    <i class="fa fa-bell fa-fw"></i>(${total}) <i class="fa fa-caret-down"></i>
                </a>

                <ul class="dropdown-menu pull-right">
                    <c:if test="${validos > 0}">
                        <li>
                            <a href="<c:url value="/registroEntrada/pendientesDistribuir/list/1"/>">
                                <div>
                                    <i class="fa fa-file-o"></i> <spring:message code="registroEntrada.pendientesDistribuir"/> (${validos})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${pendientesVisarEntrada > 0}">
                        <li>
                            <a href="<c:url value="/registroEntrada/pendientesVisar/list/1"/>">
                                <div>
                                    <i class="fa fa-eye"></i> <spring:message code="registroEntrada.pendientesVisar"/> (${pendientesVisarEntrada})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${pendientesVisarSalida > 0}">
                        <li>
                            <a href="<c:url value="/registroSalida/pendientesVisar/list/1"/>">
                                <div>
                                    <i class="fa fa-eye"></i> <spring:message code="registroSalida.pendientesVisar"/> (${pendientesVisarSalida})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${reservas > 0}">
                        <li>
                            <a href="<c:url value="/registroEntrada/reservas/list/1"/>">
                                <div>
                                    <i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.reserva"/> (${reservas})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosEntradaPendientesRemision > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/entradasPendientesRemision"/>">
                                <div>
                                    <i class="fa fa-mail-forward"></i> <spring:message code="registroEntrada.oficiosRemision"/> (${oficiosEntradaPendientesRemision})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosSalidaPendientesRemision > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/salidasPendientesRemision"/>">
                                <div>
                                    <i class="fa fa-mail-forward"></i> <spring:message code="registroSalida.oficiosRemision"/> (${oficiosSalidaPendientesRemision})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosPendientesLlegada > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/pendientesLlegada/list"/>">
                                <div>
                                    <i class="fa fa-mail-reply"></i> <spring:message code="oficioRemision.pendientesLlegada"/> (${oficiosPendientesLlegada})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${entradasRechazadosReenviados > 0}">
                        <li>
                            <a href="<c:url value="/registroEntrada/pendientesSir/list/1"/>">
                                <div>
                                    <i class="fa fa-warning"></i> <spring:message code="registroEntrada.rechazados.inicio"/> (${entradasRechazadosReenviados})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${salidasRechazadasReenviadas > 0}">
                        <li>
                            <a href="<c:url value="/registroSalida/pendientesSir/list/1"/>">
                                <div>
                                    <i class="fa fa-warning"></i> <spring:message code="registroSalida.rechazados.inicio"/> (${salidasRechazadasReenviadas})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <li>
                        <div>
                         <span class="pull-right text-muted small date">
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

</c:if>