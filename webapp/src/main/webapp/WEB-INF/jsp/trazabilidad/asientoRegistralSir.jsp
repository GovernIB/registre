<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge warning"><i class="fa fa-file-o"></i></div>
<div class="timeline-panel <c:if test="${param.activo == true}">timeline-panel-activo-re</c:if>">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/asientoRegistralSir/${asientoRegistralSir.id}/detalle"/>">
                <spring:message code="asientoRegistralSir.asientoRegistralSir"/> ${asientoRegistralSir.numeroRegistro}
            </a>
        </h4>
        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${asientoRegistralSir.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
    </div>
    <div class="timeline-body">
        <p><small><i class="fa fa-barcode"></i> <strong><spring:message code="asientoRegistralSir.identificadorIntercambio"/>:</strong> ${asientoRegistralSir.identificadorIntercambio}</small></p>
        <p><small><i class="fa fa-file-o"></i> <strong><spring:message code="asientoRegistralSir.tipoRegistro"/>:</strong>
        <c:if test="${asientoRegistralSir.tipoRegistro == 'ENTRADA'}">
             <span class="label label-info"><spring:message code="asientoRegistralSir.entrada"/></span></small></p>
        </c:if>
        <c:if test="${asientoRegistralSir.tipoRegistro == 'SALIDA'}">
            <span class="label label-danger"><spring:message code="asientoRegistralSir.salida"/></span></small></p>
        </c:if>

        <c:if test="${not empty asientoRegistralSir.decodificacionUnidadTramitacionOrigen}">
            <p><small><i class="fa fa-institution"></i> <strong><spring:message code="organismo.origen"/>:</strong> ${asientoRegistralSir.decodificacionUnidadTramitacionOrigen}</small></p>
        </c:if>
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.origen"/>:</strong> ${asientoRegistralSir.decodificacionEntidadRegistralOrigen}</small></p>
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="oficina.destino"/>:</strong> ${asientoRegistralSir.decodificacionEntidadRegistralDestino}</small></p>
    </div>
</div>