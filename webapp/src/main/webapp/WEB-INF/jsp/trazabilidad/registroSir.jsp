<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge primary"><i class="fa fa-file-o"></i></div>
<div class="timeline-panel <c:if test="${param.activo == true}">timeline-panel-activo-re</c:if>">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/registroSir/${registroSir.id}/detalle"/>">
                <spring:message code="registroSir.registroSir.recibido"/>
            </a>
        </h4>
        <p>
            <small class="text-muted"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroSir.fechaRegistro"/>: </strong><fmt:formatDate value="${registroSir.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></small><br>
            <small class="text-muted"><i class="fa fa-barcode"></i> <strong><spring:message code="registroSir.numeroRegistro"/>:</strong> ${registroSir.numeroRegistro}</small>
        </p>
    </div>
    <div class="timeline-body">
        <c:if test="${not empty registroSir.decodificacionUnidadTramitacionOrigen}">
            <p><small><i class="fa fa-institution"></i> <strong><spring:message code="organismo.origen"/>:</strong> ${registroSir.decodificacionUnidadTramitacionOrigen}</small></p>
        </c:if>
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.origen"/>:</strong> ${registroSir.decodificacionEntidadRegistralOrigen}</small></p>
        <p><small><i class="fa fa-file-o"></i> <strong><spring:message code="registroSir.tipoRegistro"/>:</strong>
        <c:if test="${registroSir.tipoRegistro == 'ENTRADA'}">
             <span class="label label-info"><spring:message code="registroSir.entrada"/></span></small></p>
        </c:if>
        <c:if test="${registroSir.tipoRegistro == 'SALIDA'}">
            <span class="label label-danger"><spring:message code="registroSir.salida"/></span></small></p>
        </c:if>
        <p>
            <small class="text-muted"><i class="fa fa-barcode"></i> <strong><spring:message code="registroSir.identificadorIntercambio"/>:</strong>
                <a target="_blank" href="<c:url value="/sir/${registroSir.identificadorIntercambio}/detalle"/>">${registroSir.identificadorIntercambio}</a>
            </small>
        </p>
        <p><small class="text-muted"><i class="fa fa-gears"></i> <strong><spring:message code="registroSir.aplicacion"/>:</strong> ${registroSir.aplicacion}</small></p>
    </div>
</div>