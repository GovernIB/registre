<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:choose>
    <c:when test="${param.estado == 'RECIBIDO'}">
        <span class="label label-warning"><spring:message code="registroSir.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == 'REENVIADO'}">
        <p rel="reenviado" data-content="<c:out value="${param.decodificacionTipoAnotacion}" escapeXml="true"/>" data-toggle="popover"><span class="label label-warning"><spring:message code="registroSir.estado.${param.estado}" /></span></p>
    </c:when>
    <c:when test="${param.estado == 'RECHAZADO'}">
        <p rel="rechazado" data-content="<c:out value="${param.decodificacionTipoAnotacion}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="registroSir.estado.${param.estado}" /></span></p>
    </c:when>
    <c:when test="${param.estado == 'ACEPTADO' || param.estado == 'REENVIADO_Y_ACK' || param.estado == 'RECHAZADO_Y_ACK'}">
        <span class="label label-success"><spring:message code="registroSir.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == 'REENVIADO_Y_ERROR' || param.estado == 'RECHAZADO_Y_ERROR'}">
        <p rel="errorSir" data-content="<c:out value="${param.codigoError} - ${param.descripcionError}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="registroSir.estado.${param.estado}" /></span></p>
    </c:when>
</c:choose>