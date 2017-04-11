<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == 'entrada'}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == 'salida'}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-8 pull-right">
    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroDetalle.expone.solicita.titulo"/></strong></h3>
        </div>

        <div class="panel-body">
            <c:if test="${not empty registro.registroDetalle.expone}">
                <p><strong><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>:</strong> ${registro.registroDetalle.expone}</p>
            </c:if>

            <c:if test="${ not empty registro.registroDetalle.solicita}">
                <p><strong><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>:</strong> ${registro.registroDetalle.solicita}</p>
            </c:if>

        </div>
    </div>

</div>


