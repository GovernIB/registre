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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="asientoRegistralSir.asientosRegistralesSir"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="asientoRegistralSir.buscador"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="asientoRegistralSirBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="asientoRegistralSir.anyRegistro"/></div>
                                <div class="col-xs-8">
                                    <form:select path="anyo" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach items="${anys}" var="anyo">
                                            <form:option value="${anyo}">${anyo}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="asientoRegistralSir.numero"/></div>
                                <div class="col-xs-8">
                                    <form:input path="asientoRegistralSir.numeroRegistro" cssClass="form-control" maxlength="10"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="asientoRegistralSir.extracto"/></div>
                                <div class="col-xs-8">
                                    <form:input path="asientoRegistralSir.resumen" cssClass="form-control" maxlength="10"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="asientoRegistralSir.estado"/></div>
                                <div class="col-xs-8">
                                    <form:select path="estado" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="estado" items="${estados}">
                                            <form:option value="${estado}"><spring:message code="asientoRegistralSir.estado.${estado}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>
                    </form:form>

                    <c:if test="${paginacion != null}">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-warning alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="asientoRegistralSir.asientoRegistralSir"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="asientoRegistralSir.asientoRegistralSir"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="asientoRegistralSir.asientoRegistralSir"/>
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
                                                <th><spring:message code="asientoRegistralSir.numero"/></th>
                                                <th><spring:message code="asientoRegistralSir.fecha"/></th>
                                                <th><spring:message code="asientoRegistralSir.unidadOrigen"/></th>
                                                <th><spring:message code="asientoRegistralSir.oficinaDestino"/></th>
                                                <th><spring:message code="asientoRegistralSir.estado"/></th>
                                                <th><spring:message code="asientoRegistralSir.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="asientoRegistralSir" items="${paginacion.listado}" varStatus="status">
                                                <tr>
                                                    <td> ${asientoRegistralSir.numeroRegistro}</td>
                                                    <td><fmt:formatDate value="${asientoRegistralSir.fechaRegistro}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${asientoRegistralSir.decodificacionUnidadTramitacionOrigen}</td>
                                                    <td>${asientoRegistralSir.decodificacionEntidadRegistralDestino}</td>
                                                    <td>
                                                        <c:if test="${asientoRegistralSir.estado == 'PENDIENTE_ENVIO' || asientoRegistralSir.estado == 'DEVUELTO' || asientoRegistralSir.estado == 'RECIBIDO' || asientoRegistralSir.estado == 'REINTENTAR_VALIDACION'}">
                                                            <span class="label label-warning"><spring:message code="asientoRegistralSir.estado.${asientoRegistralSir.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${asientoRegistralSir.estado == 'ENVIADO' || asientoRegistralSir.estado == 'ENVIADO_Y_ACK' || asientoRegistralSir.estado == 'ACEPTADO' || asientoRegistralSir.estado == 'REENVIADO' || asientoRegistralSir.estado == 'REENVIADO_Y_ACK' || asientoRegistralSir.estado == 'VALIDADO'}">
                                                            <span class="label label-success"><spring:message code="asientoRegistralSir.estado.${asientoRegistralSir.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${asientoRegistralSir.estado == 'ENVIADO_Y_ERROR' || asientoRegistralSir.estado == 'REENVIADO_Y_ERROR' || asientoRegistralSir.estado == 'ANULADO' || asientoRegistralSir.estado == 'RECHAZADO' || asientoRegistralSir.estado == 'RECHAZADO_Y_ACK' ||asientoRegistralSir.estado == 'RECHAZADO_Y_ERROR'}">
                                                            <span class="label label-danger"><spring:message code="asientoRegistralSir.estado.${asientoRegistralSir.estado}" /></span>
                                                        </c:if>
                                                    </td>
                                                    <td>${asientoRegistralSir.resumen}</td>

                                                    <td class="center">
                                                        <a class="btn btn-info btn-sm" href="<c:url value="/asientoRegistralSir/${asientoRegistralSir.id}/detalle"/>" title="<spring:message code="asientoRegistralSir.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="asientoRegistralSir"/>
                                        </c:import>

                                    </div>

                                </c:if>

                            </div>
                        </div>

                    </c:if>


                </div>
            </div>
        </div>
    </div>

    <!-- FIN BUSCADOR -->




</div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>