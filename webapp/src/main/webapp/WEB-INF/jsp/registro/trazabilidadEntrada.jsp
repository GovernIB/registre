<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 pull-right">

    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
        </div>

        <div class="panel-body">
            <ul class="timeline">

                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="status">

                    <c:if test="${registro.id == trazabilidad.registroEntradaOrigen.id}">

                        <c:if test="${status.first}">
                            <li> <%--REGISTRO ENTRADA ORIGEN--%>
                                <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>

                                <div class="timeline-panel timeline-panel-activo-re">
                                    <div class="timeline-heading">
                                        <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</h4>
                                        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                    </div>
                                    <div class="timeline-body">
                                        <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                    </div>
                                </div>
                            </li>
                        </c:if>


                        <li> <%--OFICIO REMISION--%>
                            <div class="timeline-badge success"><i class="fa fa-envelope-o"></i></div>

                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">
                                        <a href="<c:url value="/oficioRemision/${trazabilidad.oficioRemision.id}/detalle"/>"><spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${trazabilidad.oficioRemision.fecha}" pattern="yyyy"/> / ${trazabilidad.oficioRemision.numeroOficio}</a>
                                    </h4>
                                    <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                </div>
                                <div class="timeline-body">

                                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.organismoDestino"/>:</strong>
                                        <c:if test="${trazabilidad.oficioRemision.organismoDestinatario != null}">${trazabilidad.oficioRemision.organismoDestinatario.denominacion}</c:if>
                                        <c:if test="${trazabilidad.oficioRemision.organismoDestinatario == null}">${trazabilidad.oficioRemision.destinoExternoDenominacion}</c:if>
                                    </small></p>

                                    <p>
                                        <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO}"><span class="label label-warning"></c:if>
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO}"><span class="label label-warning"></c:if>
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_ACEPTADO}"><span class="label label-success"></c:if>
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_ANULADO}"><span class="label label-danger"></c:if>

                                                <spring:message code="oficioRemision.estado.${trazabilidad.oficioRemision.estado}"/>
                                                <c:if test="${not empty trazabilidad.oficioRemision.fechaEstado && trazabilidad.oficioRemision.estado != 0}">
                                                    - <fmt:formatDate value="${trazabilidad.oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                </c:if>
                                            </span>
                                        </small>
                                    </p>

                                </div>
                            </div>
                        </li>

                        <li> <%--REGISTRO SALIDA--%>
                            <div class="timeline-badge danger"><i class="fa fa-external-link"></i></div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">
                                        <a href="<c:url value="/registroSalida/${trazabilidad.registroSalida.id}/detalle"/>"><spring:message code="registroSalida.registroSalida"/> ${trazabilidad.registroSalida.numeroRegistroFormateado}</a>
                                    </h4>
                                    <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                </div>
                                <div class="timeline-body">
                                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${trazabilidad.registroSalida.oficina.denominacion}</small></p>
                                    <c:if test="${trazabilidad.registroSalida.estado == RegwebConstantes.REGISTRO_ANULADO}">
                                    <p><small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong><span class="label label-danger"><spring:message code="registro.estado.${trazabilidad.registroSalida.estado}"/></span></small></p>
                                    </c:if>
                                </div>
                            </div>
                        </li>

                        <c:if test="${trazabilidad.registroEntradaDestino != null}">
                            <li class="timeline-inverted"> <%--REGISTRO ENTRADA DESTINO--%>
                                <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                                <div class="timeline-panel">
                                    <div class="timeline-heading">
                                        <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${trazabilidad.registroEntradaDestino.numeroRegistroFormateado}</h4>
                                        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.registroEntradaDestino.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                    </div>
                                    <div class="timeline-body">
                                        <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${trazabilidad.registroEntradaDestino.oficina.denominacion}</small></p>
                                    </div>
                                </div>
                            </li>
                        </c:if>
                    </c:if>

                    <c:if test="${registro.id == trazabilidad.registroEntradaDestino.id}">

                        <li class="timeline-inverted"> <%--REGISTRO ENTRADA ORIGEN--%>
                            <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${trazabilidad.registroEntradaOrigen.numeroRegistroFormateado}</h4>
                                    <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.registroEntradaOrigen.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                </div>
                                <div class="timeline-body">
                                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${trazabilidad.registroEntradaOrigen.oficina.denominacion}</small></p>
                                </div>
                            </div>
                        </li>

                        <li class="timeline-inverted"> <%--OFICIO REMISION--%>
                            <div class="timeline-badge success"><i class="fa fa-envelope-o"></i></div>

                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">
                                        <spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${trazabilidad.oficioRemision.fecha}" pattern="yyyy"/> / ${trazabilidad.oficioRemision.numeroOficio}

                                    </h4>
                                    <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                </div>
                                <div class="timeline-body">

                                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.oficina"/>:</strong> ${trazabilidad.oficioRemision.oficina.denominacion}</small></p>

                                    <p>
                                        <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>
                                       <span class="label ${(trazabilidad.oficioRemision.estado == 2)?'label-success':'label-danger'}">
                                          <spring:message code="oficioRemision.estado.${trazabilidad.oficioRemision.estado}"/>
                                          <c:if test="${not empty trazabilidad.oficioRemision.fechaEstado && trazabilidad.oficioRemision.estado != 0}">
                                              - <fmt:formatDate value="${trazabilidad.oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                          </c:if>
                                        </span>
                                        </small>
                                    </p>

                                </div>
                            </div>
                        </li>

                        <li class="timeline-inverted"> <%--REGISTRO SALIDA--%>
                            <div class="timeline-badge danger"><i class="fa fa-external-link"></i></div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title"><spring:message code="registroSalida.registroSalida"/> ${trazabilidad.registroSalida.numeroRegistroFormateado}</h4>
                                    <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                </div>
                                <div class="timeline-body">
                                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${trazabilidad.registroSalida.oficina.denominacion}</small></p>
                                </div>
                            </div>
                        </li>

                        <li> <%--REGISTRO ENTRADA ORIGEN--%>
                            <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
                            <div class="timeline-panel timeline-panel-activo-re">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title"><spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</h4>
                                    <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                                </div>
                                <div class="timeline-body">
                                    <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                                </div>
                            </div>
                        </li>

                    </c:if>

                </c:forEach>

            </ul>
        </div>

    </div>

</div>
