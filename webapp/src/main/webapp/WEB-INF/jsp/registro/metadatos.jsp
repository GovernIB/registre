<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${param.tipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="col-xs-12">
    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registro.metadatos.generales.titulo"/></strong></h3>
        </div>

        <div class="panel-body">

            <c:if test="${not empty metadatosGenerales}">
                <c:forEach var="metadatoGeneral" items="${metadatosGenerales}">
                    <p><strong><i class="fa fa-hand-o-right"></i> ${metadatoGeneral.campo}:</strong> ${metadatoGeneral.valor}</p>
                </c:forEach>
            </c:if>

        </div>
    </div>


    <div class="panel panel-${color}">

        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registro.metadatos.particulares.titulo"/></strong></h3>
        </div>

        <div class="panel-body">

            <c:if test="${not empty metadatosParticulares}">
                <c:forEach var="metadatoParticular" items="${metadatosParticulares}">
                    <p><strong><i class="fa fa-hand-o-right"></i> ${metadatoParticular.campo}:</strong> ${metadatoParticular.valor}</p>
                </c:forEach>
            </c:if>

        </div>
    </div>

</div>