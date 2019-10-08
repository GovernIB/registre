<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI' && loginInfo.oficinaActiva != null}">

    <c:set var="total" value="${pendientesVisarEntrada + pendientesVisarSalida + reservas + oficiosEntradaInternosPendientesRemision + oficiosEntradaExternosPendientesRemision + oficiosPendientesLlegada + oficiosSalidaInternosPendientesRemision + oficiosSalidaExternosPendientesRemision + entradasRechazadosReenviados + salidasRechazadasReenviadas}"/>

    <c:if test="${total > 0}">

        <ul class="list-inline pull-right">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0);">
                    <i class="fa fa-bell fa-fw"></i>(${total}) <i class="fa fa-caret-down"></i>
                </a>

                <ul class="dropdown-menu pull-right">

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

                    <c:if test="${oficiosEntradaInternosPendientesRemision > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/entradasPendientesRemision/2"/>">
                                <div>
                                    <i class="fa fa-mail-forward text-info"></i> <spring:message code="registroEntrada.oficiosRemision.internos"/> (${oficiosEntradaInternosPendientesRemision})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosEntradaExternosPendientesRemision > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/entradasPendientesRemision/3"/>">
                                <div>
                                    <i class="fa fa-mail-forward text-info"></i> <spring:message code="registroEntrada.oficiosRemision.externos"/> (${oficiosEntradaExternosPendientesRemision})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosSalidaInternosPendientesRemision > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/salidasPendientesRemision/2"/>">
                                <div>
                                    <i class="fa fa-mail-forward text-danger"></i> <spring:message code="registroSalida.oficiosRemision.internos"/> (${oficiosSalidaInternosPendientesRemision})
                                </div>
                            </a>
                        </li>
                        <li class="divider"></li>
                    </c:if>

                    <c:if test="${oficiosSalidaExternosPendientesRemision > 0}">
                        <li>
                            <a href="<c:url value="/oficioRemision/salidasPendientesRemision/3"/>">
                                <div>
                                    <i class="fa fa-mail-forward text-danger"></i> <spring:message code="registroSalida.oficiosRemision.externos"/> (${oficiosSalidaExternosPendientesRemision})
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