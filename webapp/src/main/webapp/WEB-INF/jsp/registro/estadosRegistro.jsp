<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:choose>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_VALIDO}">
        <span class="label label-success"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_RESERVA}">
        <span class="label label-warning"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR}">
        <span class="label label-info"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_OFICIO_EXTERNO || param.estado == RegwebConstantes.REGISTRO_OFICIO_INTERNO}">
        <span class="label label-default"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_OFICIO_ACEPTADO}">
        <span class="label label-success"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_TRAMITADO}">
        <span class="label label-primary"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_ANULADO}">
        <span class="label label-danger"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_RECTIFICADO}">
        <span class="label label-danger"><spring:message code="registro.estado.${param.estado}" /></span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_RECHAZADO}">
        <span class="label label-danger"><spring:message code="registro.estado.${param.estado}" />: ${param.decodificacionTipoAnotacion}</span>
    </c:when>
    <c:when test="${param.estado == RegwebConstantes.REGISTRO_REENVIADO}">
        <span class="label label-danger"><spring:message code="registro.estado.${param.estado}" />: ${param.decodificacionTipoAnotacion}</span>
    </c:when>
</c:choose>