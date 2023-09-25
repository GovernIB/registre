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
                     <li><a href="<c:url value="/oficina/list"/>"><i class="fa fa-list-ul"></i>  <spring:message code="oficina.oficinas"/></a></li>
                    <li class="active"><i class="fa fa-home"></i> <spring:message code="oficina.oficina"/> ${oficina.denominacion}</li>
                 </ol>
             </div>
        </div>
        <!-- Fin miga de pan -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">

            <!-- Panel Lateral -->
            <div class="col-xs-4">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-home"></i>
                            <strong> ${oficina.denominacion}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <%--DETALLE OFICINA--%>
                        <dl class="detalle_registro">

                            <dt><i class="fa fa-home"></i> <spring:message code="oficina.codigo"/>: </dt> <dd> ${oficina.codigo}</dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="oficina.organismoResponsable"/>: </dt> <dd> ${oficina.organismoResponsable.denominacion}</dd>
                            <c:if test="${not empty oficina.oficinaResponsable}">
                                <dt><i class="fa fa-home"></i> <spring:message code="oficina.oficinaResponsable"/>: </dt> <dd> ${oficina.oficinaResponsable.denominacion}</dd>
                            </c:if>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="regweb.estado"/>: </dt>
                            <dd>
                                <c:choose>
                                    <c:when test="${oficina.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_ANULADO}">
                                        <span class="label label-danger"><spring:message code="unidad.estado.${oficina.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                    <c:when test="${oficina.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO}">
                                        <span class="label label-warning"><spring:message code="unidad.estado.${oficina.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                    <c:when test="${oficina.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO}">
                                        <span class="label label-default"><spring:message code="unidad.estado.${oficina.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                    <c:when test="${oficina.estado.codigoEstadoEntidad == RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}">
                                        <span class="label label-success"><spring:message code="unidad.estado.${oficina.estado.codigoEstadoEntidad}" /></span>
                                    </c:when>
                                </c:choose>
                            </dd>
                            <dt><i class="fa fa-list-ul"></i> <spring:message code="oficina.servicios"/>: </dt>
                            <dd> <br>
                                <ul>
                                    <c:forEach var="servicio" items="${oficina.servicios}">
                                        <li>${servicio.descServicio}</li>
                                    </c:forEach>
                                </ul>
                            </dd>
                            <dt><i class="fa fa-bookmark"></i> LIBSIR: </dt>
                            <dd>
                                <c:choose>
                                    <c:when test="${oficina.activaLibSir}">
                                        <span class="label label-success"><spring:message code="cola.activa" /></span>
                                    </c:when>
                                    <c:when test="${not oficina.activaLibSir}">
                                        <span class="label label-danger">Inactiva</span>
                                    </c:when>

                                </c:choose>
                            </dd>

                        </dl>

                    </div>

                    <div class="panel-footer center">

                        <div class="btn-group">
                            <button type="button" onclick="confirm('<c:url value="/oficina/${oficina.id}/gestionarOficinaLibSir"/>', '<spring:message code="regweb.continuar" htmlEscape="true"/>')" class="btn btn-primary btn-sm">
                                <c:if test="${oficina.activaLibSir}">
                                    <spring:message code="oficina.desactivarLibSir"/>
                                </c:if>
                                <c:if test="${not oficina.activaLibSir}">
                                    <spring:message code="oficina.activarLibSir"/>
                                </c:if>
                            </button>
                        </div>

                    </div>
                </div>
            </div>
            <!-- Fin Panel Lateral -->

                <div class="col-xs-8">

                    <div class="panel panel-warning">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="oficina.organismos"/></strong></h3>
                        </div>

                        <div class="panel-body">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${organismos != null && empty organismos}">
                                        <div class="alert alert-grey">
                                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="organismo.organismo"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty organismos}">

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                    <tr>
                                                        <th><spring:message code="organismo.denominacion"/></th>
                                                        <th>EDP</th>
                                                        <th><spring:message code="organismo.buscador.codigo"/></th>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="organismo" items="${organismos}">
                                                    <tr>
                                                        <td>${organismo.denominacion}</td>
                                                        <td>
                                                            <c:if test="${organismo.edp == true}">
                                                                <span class="label label-success">Si</span>
                                                            </c:if>
                                                            <c:if test="${organismo.edp == false}">
                                                                <span class="label label-danger">No</span>
                                                            </c:if>
                                                        </td>
                                                        <td>${organismo.codigo}</td>
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