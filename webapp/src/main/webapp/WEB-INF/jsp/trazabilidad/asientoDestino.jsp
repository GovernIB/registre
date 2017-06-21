<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge warning"><i class="fa fa-file-o"></i></div>
<div class="timeline-panel">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}">
                <spring:message code="registroSir.registroSir"/> <spring:message code="registroSir.aceptado"/>
            </c:if>
            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}">
                <spring:message code="registroSir.registroSir"/> <spring:message code="registro.estado.${registroEnviado.estado}" />
            </c:if>
            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}">
                <spring:message code="registroSir.registroSir"/> <spring:message code="registro.estado.${registroEnviado.estado}" />
            </c:if>
        </h4>
    </div>
    <div class="timeline-body">
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.oficina"/>:</strong> ${oficioRemision.decodificacionEntidadRegistralDestino} - ${oficioRemision.codigoEntidadRegistralDestino}</small></p>
        <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}">
            <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="oficioRemision.fecha.aceptado"/>:</strong> <fmt:formatDate value="${oficioRemision.fechaEntradaDestino}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
            <p><small><i class="fa fa-barcode"></i> <strong><spring:message code="registroSir.numeroRegistro"/>:</strong> ${oficioRemision.numeroRegistroEntradaDestino}</small></p>
        </c:if>
        <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}">
            <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="oficioRemision.fecha"/>:</strong> <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
            <p><small><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroSir.motivo"/>:</strong> ${registroEnviado.registroDetalle.decodificacionTipoAnotacion}</small></p>
        </c:if>

    </div>
</div>