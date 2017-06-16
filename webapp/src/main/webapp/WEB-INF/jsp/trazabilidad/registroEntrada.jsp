<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge info"><i class="fa fa-file-o"></i></div>
<div class="timeline-panel <c:if test="${param.activo == true}">timeline-panel-activo-re</c:if>">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>">${titulo}</a>
        </h4>
        <p>
            <small class="text-muted"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroEntrada.fechaRegistro"/>:</strong> <fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small><br>
            <small class="text-muted"><i class="fa fa-barcode"></i> <strong><spring:message code="registroEntrada.numeroRegistro"/>:</strong> ${registroEntrada.numeroRegistroFormateado}</small>
        </p>
    </div>
    <div class="timeline-body">
        <p><small><i class="fa fa-home"></i> <strong><spring:message code="registroEntrada.oficina"/>:</strong> ${registroEntrada.oficina.denominacion}</small></p>
        <p><small><i class="fa fa-bookmark"></i> <strong><spring:message code="registroEntrada.estado"/>:</strong>
            <c:import url="../registro/estadosRegistro.jsp">
                <c:param name="estado" value="${registroEntrada.estado}"/>
                <c:param name="decodificacionTipoAnotacion" value="${registroEntrada.registroDetalle.decodificacionTipoAnotacion}"/>
            </c:import>
        </small></p>
    </div>
</div>