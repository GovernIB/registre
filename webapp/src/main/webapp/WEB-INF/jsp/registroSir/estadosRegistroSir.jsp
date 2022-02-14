<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<c:choose>
    <c:when test="${registroSir.estado == 'RECIBIDO_CONFIRMADO'}">
        <span class="label label-success"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
    </c:when>
    <c:when test="${registroSir.estado == 'ENVIADO_CONFIRMADO'}">
        <span class="label label-success"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
    </c:when>
    <c:when test="${registroSir.estado == 'FINALIZADO'}">
        <span class="label label-success"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
    </c:when>
</c:choose>