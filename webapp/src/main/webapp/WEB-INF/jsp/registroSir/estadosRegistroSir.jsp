<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:choose>
    <c:when test="${registroSir.estado == 'RECIBIDO'}">
        <span class="label label-warning"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
    </c:when>
    <c:when test="${registroSir.estado == 'REENVIADO'}">
        <p rel="reenviado" data-content="<c:out value="${registroSir.decodificacionTipoAnotacion}" escapeXml="true"/>" data-toggle="popover"><span class="label label-warning"><spring:message code="registroSir.estado.${registroSir.estado}" /></span></p>
    </c:when>
    <c:when test="${registroSir.estado == 'RECHAZADO'}">
        <p rel="rechazado" data-content="<c:out value="${registroSir.decodificacionTipoAnotacion}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="registroSir.estado.${registroSir.estado}" /></span></p>
    </c:when>
    <c:when test="${registroSir.estado == 'ACEPTADO' || registroSir.estado == 'REENVIADO_Y_ACK' || registroSir.estado == 'RECHAZADO_Y_ACK'}">
        <span class="label label-success"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
    </c:when>
    <c:when test="${registroSir.estado == 'REENVIADO_Y_ERROR' || registroSir.estado == 'RECHAZADO_Y_ERROR'}">
        <p rel="errorSir" data-content="<c:out value="${registroSir.codigoError} - ${registroSir.descripcionError}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="registroSir.estado.${registroSir.estado}" /></span></p>
    </c:when>
</c:choose>