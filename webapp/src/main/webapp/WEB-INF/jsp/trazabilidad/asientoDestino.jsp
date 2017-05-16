<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge warning"><i class="fa fa-file-o"></i></div>
<div class="timeline-panel <c:if test="${param.activo == true}">timeline-panel-activo-re</c:if>">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <spring:message code="registroSir.registroSir"/> <spring:message code="registroSir.aceptado"/>
        </h4>
    </div>
    <div class="timeline-body">
        <p><small><i class="fa fa-clock-o"></i> <strong><spring:message code="oficioRemision.fecha.aceptado"/>:</strong> <fmt:formatDate value="${oficioRemision.fechaEntradaDestino}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
        <p><small><i class="fa fa-barcode"></i> <strong><spring:message code="registroSir.numeroRegistro"/>:</strong> ${oficioRemision.numeroRegistroEntradaDestino}</small></p>
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.origen"/>:</strong> ${registroEntradaEnviado.oficina.denominacion}</small></p>
        <p><small><i class="fa fa-institution"></i> <strong><spring:message code="organismo.destino"/>:</strong> ${registroEntradaEnviado.destinoExternoDenominacion}</small></p>
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.destino"/>:</strong> ${registroEntradaEnviado.registroDetalle.decodificacionEntidadRegistralDestino}</small></p>
    </div>
</div>