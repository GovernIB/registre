<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="col-xs-8 col-xs-offset pull-right">

    <div class="panel panel-danger">
        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-clock-o fa-fw"></i> <strong><spring:message code="registroEntrada.trazabilidad"/></strong></h3>
        </div>

        <div class="panel-body">
            <ul class="timeline">

                <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="loopStatus">

                    <li> <%--REGISTRO ENTRADA ORIGEN--%>
                        <div class="timeline-badge info"><i class="fa fa-file-o"></i></div>

                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">
                                    <a href="<c:url value="/registroEntrada/${trazabilidad.registroEntradaOrigen.id}/detalle"/>">
                                        <spring:message code="registroEntrada.registroEntrada"/> ${trazabilidad.registroEntradaOrigen.numeroRegistroFormateado}
                                    </a>
                                </h4>
                                <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${trazabilidad.registroEntradaOrigen.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                            </div>
                            <div class="timeline-body">
                                <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${trazabilidad.registroEntradaOrigen.oficina.denominacion}</small></p>
                            </div>
                        </div>
                    </li>

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

                                <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.organismoDestino"/>:</strong> ${trazabilidad.oficioRemision.organismoDestinatario}</small></p>

                                <p>
                                    <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>
                                        <span class="label ${(trazabilidad.oficioRemision.estado == 2)?'label-success':'label-danger'}">
                                           <spring:message code="oficioRemision.estado.${trazabilidad.oficioRemision.estado}"/>
                                           <c:if test="${not empty trazabilidad.oficioRemision.fechaEstado}">
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
                        <div class="timeline-panel timeline-panel-activo-rs">
                            <div class="timeline-heading">
                                <h4 class="timeline-title">
                                    <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}
                                </h4>
                                <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                            </div>
                            <div class="timeline-body">
                                <p><small><i class="fa fa-exchange"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${registro.oficina.denominacion}</small></p>
                            </div>
                        </div>
                    </li>

                </c:forEach>

            </ul>
        </div>

    </div>

</div>
