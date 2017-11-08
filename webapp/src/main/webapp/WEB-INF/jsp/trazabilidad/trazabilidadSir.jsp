<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>


<ul class="timeline">

    <c:forEach var="trazabilidad" items="${trazabilidades}" varStatus="status">

        <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION}">

            <li class="timeline-inverted">
                <div class="timeline-badge warning"><i class="fa fa-reply"></i></div>
                <div class="timeline-panel">
                    <div class="timeline-heading">
                        <h4 class="timeline-title"><spring:message code="registroSir.recibido"/>:</h4>
                    </div>
                    <div class="timeline-body">
                        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.origen"/>:</strong> ${trazabilidad.decodificacionEntidadRegistralOrigen}</small></p>
                        <c:if test="${not empty trazabilidad.registroSir.decodificacionUnidadTramitacionOrigen}">
                            <p><small><i class="fa fa-institution"></i> <strong><spring:message code="organismo.origen"/>:</strong> ${trazabilidad.registroSir.decodificacionUnidadTramitacionOrigen}</small></p>
                        </c:if>
                        <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="registroSir.fechaRecepcion"/>:</strong> <fmt:formatDate value="${trazabilidad.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                    </div>
                </div>
            </li>

        </c:if>

        <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_SIR_REENVIO}">

            <li>
                <div class="timeline-badge success"><i class="fa fa-share"></i></div>
                <div class="timeline-panel">
                    <div class="timeline-heading">
                        <h4 class="timeline-title"><spring:message code="registroSir.reenviado"/>:</h4>
                    </div>
                    <div class="timeline-body">
                        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.destino"/>:</strong> ${trazabilidad.decodificacionEntidadRegistralDestino} - ${trazabilidad.codigoEntidadRegistralDestino}</small></p>
                        <p><small><i class="fa fa-file-o"></i> <strong><spring:message code="registroSir.motivo"/>:</strong> ${trazabilidad.observaciones}</small></p>
                        <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="registroSir.fechaReenvio"/>:</strong> <fmt:formatDate value="${trazabilidad.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                    </div>
                </div>
            </li>

        </c:if>

        <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO}">

            <li>
                <div class="timeline-badge danger"><i class="fa fa-warning"></i></div>
                <div class="timeline-panel">
                    <div class="timeline-heading">
                        <h4 class="timeline-title"><spring:message code="registroSir.rechazado"/>:</h4>
                    </div>
                    <div class="timeline-body">
                        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.destino"/>:</strong> ${trazabilidad.decodificacionEntidadRegistralDestino} - ${trazabilidad.codigoEntidadRegistralDestino}</small></p>
                        <p><small><i class="fa fa-file-o"></i> <strong><spring:message code="registroSir.motivo"/>:</strong> ${trazabilidad.observaciones}</small></p>
                        <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="registroSir.fechaRechazo"/>:</strong> <fmt:formatDate value="${trazabilidad.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                    </div>
                </div>
            </li>

        </c:if>

        <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO}">

            <li>
                <c:set var="registroEntrada" value="${trazabilidad.registroEntrada}" scope="request"/>
                <fmt:message key="registroEntrada.aceptado" var="titulo" scope="request"/>
                <c:import url="../trazabilidad/registroEntrada.jsp">
                    <c:param name="activo" value="true"/>
                </c:import>
            </li>

        </c:if>

        <c:if test="${trazabilidad.tipo == RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO_ORIGEN}">

            <li class="timeline-inverted">
                <div class="timeline-badge danger"><i class="fa fa-warning"></i></div>
                <div class="timeline-panel">
                    <div class="timeline-heading">
                        <h4 class="timeline-title"><spring:message code="registroSir.rechazado.origen"/>:</h4>
                    </div>
                    <div class="timeline-body">
                        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.origen"/>:</strong> ${trazabilidad.decodificacionEntidadRegistralOrigen} - ${trazabilidad.codigoEntidadRegistralOrigen}</small></p>
                        <p><small><i class="fa fa-file-o"></i> <strong><spring:message code="registroSir.motivo"/>:</strong> ${trazabilidad.observaciones}</small></p>
                        <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="registroSir.fechaRechazo"/>:</strong> <fmt:formatDate value="${trazabilidad.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
                    </div>
                </div>
            </li>

        </c:if>

    </c:forEach>

</ul>
