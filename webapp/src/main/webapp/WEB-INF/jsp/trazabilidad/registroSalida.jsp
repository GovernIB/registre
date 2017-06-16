<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge danger"><i class="fa fa-share"></i></div>
<div class="timeline-panel <c:if test="${param.activo == true}">timeline-panel-activo-rs</c:if>">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>">${titulo} </a>
        </h4>
        <p>
            <small class="text-muted"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroSalida.fechaRegistro"/>:</strong> <fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small><br>
            <small class="text-muted"><i class="fa fa-barcode"></i> <strong><spring:message code="registroSalida.numeroRegistro"/>:</strong> ${registroSalida.numeroRegistroFormateado}</small>
        </p>
    </div>
    <div class="timeline-body">
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="registroSalida.oficina"/>:</strong> ${registroSalida.oficina.denominacion}</small></p>
        <p><small><i class="fa fa-bookmark"></i> <strong><spring:message code="registroSalida.estado"/>:</strong>
            <c:import url="../registro/estadosRegistro.jsp">
                <c:param name="estado" value="${registroSalida.estado}"/>
                <c:param name="decodificacionTipoAnotacion" value="${registroSalida.registroDetalle.decodificacionTipoAnotacion}"/>
            </c:import>
        </small></p>
    </div>
</div>