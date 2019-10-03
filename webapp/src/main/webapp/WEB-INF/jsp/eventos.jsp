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

                        <c:import url="modulos/mensajes.jsp"/>

                    </div>
                </div>

                <div class="row">

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
                                    <dt class="text-danger">Registros de entradas sin evento asignado:</dt> <dd> ${entradasPendientes}</dd>
                                    <dt class="text-danger">Registros de salidas sin evento asignado:</dt> <dd> ${salidasPendientes}</dd>
                                    <hr class="divider-warning">
                                    <dt class="text-success">Registros de entradas con evento asignado:</dt> <dd> ${entradasEventoAsignado}</dd>
                                    <dt class="text-success">Registros de salidas con evento asignado:</dt> <dd> ${salidasEventoAsignado}</dd>
                                    <hr class="divider-warning">
                                    <dt class="text-warning">Registros de entrada sin evento:</dt> <dd> ${entradasSinEvento}</dd>
                                    <dt class="text-warning">Registros de salida sin evento:</dt> <dd> ${salidasSinEvento}</dd>
                                    <%--<hr class="divider-warning">
                                    <dt>Entradas con evento 'Procesado':</dt> <dd> ${entradasEventoProcesado}</dd>
                                    <dt>Salidas con evento 'Procesado':</dt> <dd> ${salidasEventoProcesado}</dd>--%>
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
                                            <td>Marcar entradas 'Oficio interno' de la Entidad</td>
                                            <td class="center">
                                                <a class="btn btn-success btn-sm" href="<c:url value="/eventoOficioInternoEntradas"/>" title="Marcar entradas como 'Oficio interno'"><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                        <%--<c:if test="${!loginInfo.entidadActiva.sir}">--%>
                                            <tr>
                                                <td>Marcar entradas 'Oficio externo' de la Entidad</td>
                                                <td class="center">
                                                    <a class="btn btn-info btn-sm" href="<c:url value="/eventoOficioExternoEntradas"/>" title="Marcar entradas como 'Oficio externo'"><span class="fa fa-eye"></span></a>
                                                </td>
                                            </tr>
                                        <%--</c:if>--%>
                                        <tr>
                                            <td>Marcar entradas 'Distribuir' de la Entidad</td>
                                            <td class="center">
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/eventoDistribuirEntradas"/>" title="Marcar entradas como 'Distribuir'"><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Marcar salidas (a una Administración) 'Distribuir' de la Entidad</td>
                                            <td class="center">
                                                <a class="btn btn-danger btn-sm" href="<c:url value="/eventoDistribuirSalidas"/>" title="Marcar salidas a una administración como 'Distribuir'"><span class="fa fa-eye"></span></a>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Marcar salidas (a un ciudadano) 'Distribuir' de la Entidad</td>
                                            <td class="center">
                                                <a class="btn btn-danger btn-sm" href="<c:url value="/eventoDistribuirSalidasPersonas"/>" title="Marcar salidas a un ciudadano como 'Distribuir'"><span class="fa fa-user"></span></a>
                                            </td>
                                        </tr>
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
                                                <a class="btn btn-success btn-sm"
                                                   href="<c:url value="/eventoOficioInternoEntradas/${oficina.id}"/>"
                                                   title="Marcar entradas 'Oficio interno'"><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-info btn-sm"
                                                   href="<c:url value="/eventoOficioExternoEntradas/${oficina.id}"/>"
                                                   title="Marcar entradas 'Oficio externo'"><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-warning btn-sm"
                                                   href="<c:url value="/eventoDistribuirEntradas/${oficina.id}"/>"
                                                   title="Marcar entradas 'Distribuir'"><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-danger btn-sm"
                                                   href="<c:url value="/eventoDistribuirSalidas/${oficina.id}"/>"
                                                   title="Marcar salidas a una administración 'Distribuir'"><span class="fa fa-eye"></span></a>

                                                <a class="btn btn-danger btn-sm"
                                                   href="<c:url value="/eventoDistribuirSalidasPersonas/${oficina.id}"/>"
                                                   title="Marcar salidas a un ciudadano 'Distribuir'"><span class="fa fa-user"></span></a>
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


