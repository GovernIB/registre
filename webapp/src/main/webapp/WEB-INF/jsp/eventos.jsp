<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="modulos/imports.jsp"/>
</head>

    <body>

        <c:import url="modulos/menu.jsp"/>

        <div class="row-fluid container main">

            <div class="well well-white">

                <div class="row">

                    <div class="col-xs-12">
                        <ol class="breadcrumb">
                            <c:import url="modulos/migadepan.jsp"/>
                        </ol>

                    </div>
                </div>

                <div class="row">

                    <c:import url="modulos/mensajes.jsp"/>

                    <!-- Panel Lateral -->
                    <div class="col-xs-5">

                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Eventos</strong></h3>
                            </div>

                            <div class="panel-body">

                                <dl class="detalle_registro">
                                    <dt>Registros de entrada totales:</dt> <dd> ${totalEntradas}</dd>
                                    <dt>Registros de salida totales:</dt> <dd> ${totalSalidas}</dd>
                                    <hr class="divider-warning">
                                    <dt class="text-danger">Registros de entradas sin evento:</dt> <dd> ${entradasPendientes}</dd>
                                    <dt class="text-danger">Registros de salidas sin evento:</dt> <dd> ${salidasPendientes}</dd>
                                    <hr class="divider-warning">
                                    <dt class="text-success">Entradas con evento asignado:</dt> <dd> ${entradasEvento}</dd>
                                    <dt class="text-success">Salidas con evento asignado:</dt> <dd> ${salidasEvento}</dd>
                                    <hr class="divider-warning">
                                    <dt>Entradas con evento 'Procesado':</dt> <dd> ${entradasProcesadas}</dd>
                                    <dt>Salidas con evento 'Procesado':</dt> <dd> ${salidasProcesadas}</dd>
                                </dl>

                            </div>
                        </div>

                    </div>

                    <!-- Panel central Entidad -->
                    <div class="col-xs-7">

                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Entidad</strong></h3>
                            </div>

                            <div class="panel-body">
                                <table class="table table-bordered table-hover table-striped">
                                    <colgroup>
                                        <col>
                                        <col <%--width="100"--%>>
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>Evento</th>
                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td>Distribuir entradas</td>
                                            <td class="center">
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/eventoDistribuirEntradas"/>" title="eventoDistribuirEntradas"><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Distribuir salidas</td>
                                            <td class="center">
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/eventoDistribuirSalidas"/>" title="eventoDistribuirSalidas"><span class="fa fa-eye"></span></a>
                                                <a class="btn btn-info btn-sm" href="<c:url value="/eventoDistribuirSalidasPersonas"/>" title="eventoDistribuirSalidasPersonas"><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Oficio interno entradas</td>
                                            <td class="center">
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/eventoOficioInternoEntradas"/>" title="eventoOficioInternoEntradas"><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                        <c:if test="${!loginInfo.entidadActiva.sir}">
                                            <tr>
                                                <td>Oficio externo entradas</td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" href="<c:url value="/eventoOficioExternoEntradas"/>"><span class="fa fa-eye"></span></a>
                                                </td>
                                            </tr>
                                        </c:if>

                                        <c:if test="${loginInfo.entidadActiva.sir}">
                                            <tr>
                                                <td>Oficio sir entradas</td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" href="<c:url value="/"/>"><span class="fa fa-eye"></span></a>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>Oficio sir salidas</td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" href="<c:url value="/"/>"><span class="fa fa-eye"></span></a>
                                                </td>
                                            </tr>
                                        </c:if>

                                    </tbody>
                                </table>
                            </div>

                        </div>

                    </div>

                    <!-- Panel central Oficinas -->
                    <div class="col-xs-7">

                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Oficinas</strong></h3>
                            </div>

                            <div class="panel-body">
                                <table class="table table-bordered table-hover table-striped">
                                    <colgroup>
                                        <col>
                                        <col <%--width="100"--%>>
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="oficina.oficina"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="oficina" items="${oficinas}">
                                        <tr>
                                            <td>${oficina.denominacion}</td>
                                            <td class="center">
                                                <a class="btn btn-warning btn-sm"
                                                   href="<c:url value="//${oficina.id}/edit"/>"
                                                   title=""><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-info btn-sm"
                                                   href="<c:url value="//${oficina.id}/edit"/>"
                                                   title=""><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-danger btn-sm"
                                                   href="<c:url value="//${oficina.id}/edit"/>"
                                                   title=""><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-success btn-sm"
                                                   href="<c:url value="//${oficina.id}/edit"/>"
                                                   title=""><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>

                </div>
            </div>
        </div>

        <c:import url="modulos/pie.jsp"/>

    </body>

</html>


