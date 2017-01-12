<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<div class="timeline-badge success"><i class="fa fa-envelope-o"></i></div>

<div class="timeline-panel">
    <div class="timeline-heading">
        <h4 class="timeline-title">
            <a href="<c:url value="/oficioRemision/${oficioRemision.id}/detalle"/>"><spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</a>
        </h4>
        <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></small></p>
    </div>
    <div class="timeline-body">

        <p>
            <small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.oficina"/>:</strong>
                ${oficioRemision.oficina.denominacion}
            </small>
        </p>
        <p>
            <small><i class="fa fa-exchange"></i> <strong><spring:message code="oficioRemision.organismoDestino"/>:</strong>
                <c:if test="${not empty oficioRemision.organismoDestinatario}">${oficioRemision.organismoDestinatario.denominacion}</c:if>
                <c:if test="${empty oficioRemision.organismoDestinatario}">${oficioRemision.destinoExternoDenominacion}</c:if>
            </small>
        </p>
        <p>
            <small><i class="fa fa-sign-in"></i> <strong><spring:message code="oficioRemision.destino"/>:</strong>
                <span class="label label-default">
                    <c:if test="${not empty oficioRemision.organismoDestinatario}"><spring:message code="oficioRemision.interno"/></c:if>
                    <c:if test="${empty oficioRemision.organismoDestinatario}"><spring:message code="oficioRemision.externo"/></c:if>
                </span>
            </small>
        </p>
        <p>
            <small><i class="fa fa-bookmark"></i> <strong><spring:message code="oficioRemision.estado"/>:</strong>
                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO}"><span class="label label-warning"></c:if>
                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO}"><span class="label label-warning"></c:if>
                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_ACEPTADO}"><span class="label label-success"></c:if>
                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_REMISION_ANULADO}"><span class="label label-danger"></c:if>

                    <spring:message code="oficioRemision.estado.${oficioRemision.estado}"/>
                    <c:if test="${not empty oficioRemision.fechaEstado && oficioRemision.estado != 0}">
                        - <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                    </c:if>
                </span>
            </small>
        </p>

    </div>
</div>