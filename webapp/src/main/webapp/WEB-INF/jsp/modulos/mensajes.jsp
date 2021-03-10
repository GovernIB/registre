<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<%--Entidad en modo Mantenimiento--%>
<c:if test="${loginInfo.entidadActiva.mantenimiento}">
    <div class="alert alert-danger">
        <div class="row vertical-align">
            <div class="col-xs-1 text-center">
                <i class="fa fa-exclamation-triangle fa-2x"></i>
            </div>
            <div class="col-xs-11">
                <strong><spring:message code="entidad.mantenimiento.activo"/></strong>
            </div>
        </div>
    </div>
</c:if>

<c:if test="${not empty infos}">
    <div class="alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <div class="row vertical-align">
            <div class="col-xs-1 text-center">
                <i class="fa fa-info-circle fa-2x"></i>
            </div>
            <div class="col-xs-11">
                <c:forEach var="info" items="${infos}">
                    <strong>${info}</strong><br>
                </c:forEach>
                <c:remove var="infos" scope="session"/>
            </div>
        </div>
    </div>
</c:if>

<c:if test="${not empty error}">

    <div id="mensajeError" class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <div class="row vertical-align">
            <div class="col-xs-1 text-center">
                <i class="fa fa-times-circle fa-2x"></i>
            </div>
            <div class="col-xs-11">
                <strong>${error}</strong>
                <c:remove var="error" scope="session"/>
            </div>
        </div>
    </div>

</c:if>

<c:if test="${not empty aviso}">

    <div class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <div class="row vertical-align">
            <div class="col-xs-1 text-center">
                <i class="fa fa-exclamation-triangle fa-2x"></i>
            </div>
            <div class="col-xs-11">
                <strong>${aviso}</strong>
                <c:remove var="aviso" scope="session"/>
            </div>
        </div>
    </div>

</c:if>
