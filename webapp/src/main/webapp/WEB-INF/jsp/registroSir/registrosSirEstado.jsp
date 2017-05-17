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

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a <c:if test="${oficinaActiva.sirEnvio || oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSir.registrosSir"/> <spring:message code="registroSir.estado.${estado}" /></strong></li>
                </ol>
            </div>
        </div>

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-primary">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i>
                            <strong><spring:message code="registroSir.listado"/> <spring:message code="registroSir.estado.${estado}" /></strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message
                                            code="registroSir.registroSir"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSir.registroSir"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSir.registroSir"/>
                                        </c:if>

                                        <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                    </div>

                                    <div class="table-responsive">

                                        <table class="table table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="registroSir.numeroRegistro"/></th>
                                                <th><spring:message code="registroSir.fechaRegistro"/></th>
                                                <th><spring:message code="registroSir.oficinaOrigen"/></th>
                                                <th><spring:message code="registroSir.oficinaDestino"/></th>
                                                <th><spring:message code="registroSir.estado"/></th>
                                                <th><spring:message code="registroSir.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroSir" items="${paginacion.listado}" varStatus="status">
                                                <tr>
                                                    <td> ${registroSir.numeroRegistro}</td>
                                                    <td><fmt:formatDate value="${registroSir.fechaRegistro}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${registroSir.decodificacionEntidadRegistralOrigen}</td>
                                                    <td>${registroSir.decodificacionEntidadRegistralDestino}</td>
                                                    <td>
                                                        <c:if test="${registroSir.estado == 'RECIBIDO'}">
                                                            <span class="label label-warning"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${registroSir.estado == 'ACEPTADO' || registroSir.estado == 'REENVIADO' || registroSir.estado == 'REENVIADO_Y_ACK'}">
                                                            <span class="label label-success"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${registroSir.estado == 'REENVIADO_Y_ERROR' || registroSir.estado == 'RECHAZADO' || registroSir.estado == 'RECHAZADO_Y_ACK' ||registroSir.estado == 'RECHAZADO_Y_ERROR'}">
                                                            <span class="label label-danger"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
                                                        </c:if>
                                                    </td>
                                                    <td>${registroSir.resumen}</td>

                                                    <td class="center">
                                                        <a class="btn btn-primary btn-sm" href="<c:url value="/registroSir/${registroSir.id}/detalle"/>" title="<spring:message code="registroSir.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacion.jsp">
                                            <c:param name="entidad" value="registroSir/${url}"/>
                                        </c:import>

                                    </div>

                                </c:if>

                            </div>
                        </div>


                    </div>

                </div>
            </div>

        </div>


    </div>
</div>
<!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>