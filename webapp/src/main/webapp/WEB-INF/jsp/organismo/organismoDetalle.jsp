<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
             <div class="col-xs-12">
                 <ol class="breadcrumb">
                     <c:import url="../modulos/migadepan.jsp"/>
                     <li><a href="<c:url value="/organismo/list"/>"><i class="fa fa-list-ul"></i>  <spring:message code="organismo.organismos"/></a></li>
                    <li class="active"><i class="fa fa-home"></i> ${organismo.denominacion}</li>
                 </ol>
             </div>
        </div>
        <!-- Fin miga de pan -->

        <div class="row">

            <!-- Panel Lateral -->
            <div class="col-xs-4">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-home"></i>
                            <strong> ${organismo.denominacion}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <%--DETALLE organismo--%>
                        <dl class="detalle_registro">

                            <dt><i class="fa fa-home"></i> <spring:message code="entidad.codigoDir3"/>: </dt> <dd> ${organismo.codigo}</dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="organismo.superior"/>: </dt> <dd> ${organismo.organismoSuperior.denominacion}</dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="organismo.edp"/>: </dt>
                            <dd>
                                <c:if test="${organismo.edp == true}">
                                    <span class="label label-success">Si</span>
                                </c:if>
                                <c:if test="${organismo.edp == false}">
                                    <span class="label label-danger">No</span>
                                </c:if>
                            </dd>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="regweb.estado"/>: </dt>
                            <dd>
                                <c:choose>
                                    <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_ANULADO}">
                                        <span class="label label-danger"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                    <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO}">
                                        <span class="label label-warning"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                    <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO}">
                                        <span class="label label-default"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                    <c:when test="${organismo.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}">
                                        <span class="label label-success"><spring:message code="unidad.estado.${organismo.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                </c:choose>
                            </dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="organismo.externo"/>: </dt>
                            <dd>
                                <c:if test="${organismo.externo == true}">
                                    <span class="label label-success">Si</span>
                                </c:if>
                                <c:if test="${organismo.externo == false}">
                                    <span class="label label-danger">No</span>
                                </c:if>
                            </dd>
                            <c:if test="${not empty organismo.codAmbComunidad}">
                                <dt><i class="fa fa-map"></i> <spring:message code="organismo.buscador.comunidadAutonoma"/>: </dt> <dd> ${organismo.codAmbComunidad.descripcionComunidad}</dd>
                            </c:if>
                            <c:if test="${not empty organismo.codAmbProvincia}">
                                <dt><i class="fa fa-home"></i> <spring:message code="oficina.provincia"/>: </dt> <dd> ${organismo.codAmbProvincia.descripcionProvincia}</dd>
                            </c:if>
                            <c:if test="${not empty organismo.isla}">
                                <dt><i class="fa fa-map-marker"></i> <spring:message code="oficina.isla"/>: </dt> <dd> ${organismo.isla.descripcionIsla}</dd>
                            </c:if>

                        </dl>

                    </div>

                </div>

            </div>
            <!-- Fin Panel Lateral -->

            <div class="col-xs-8">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="organismo.oficinas"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${oficinas != null && empty oficinas}">
                                    <div class="alert alert-grey">
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="oficina.oficina"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty oficinas}">

                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                            <tr>
                                                <th><spring:message code="oficina.denominacion"/></th>
                                                <th><spring:message code="oficina.codigo"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="oficina" items="${oficinas}">
                                                <tr>
                                                    <td>${oficina.denominacion}</td>
                                                    <td>${oficina.codigo}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /div.row-->
</div>

<c:import url="../modulos/pie.jsp"/>

</body>
</html>