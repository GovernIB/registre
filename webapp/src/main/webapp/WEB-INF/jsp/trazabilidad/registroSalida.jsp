<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge danger"><i class="fa fa-external-link"></i></div>
<div class="timeline-panel <c:if test="${param.activo == true}">timeline-panel-activo-rs</c:if>">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>"><spring:message code="registroSalida.registroSalida"/> ${registroSalida.numeroRegistroFormateado}</a>
        </h4>
        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
    </div>
    <div class="timeline-body">
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${registroSalida.oficina.denominacion}</small></p>
        <c:if test="${registroSalida.estado == RegwebConstantes.REGISTRO_ANULADO}">
            <p><small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong><span class="label label-danger"><spring:message code="registro.estado.${registroSalida.estado}"/></span></small></p>
        </c:if>
    </div>
</div>