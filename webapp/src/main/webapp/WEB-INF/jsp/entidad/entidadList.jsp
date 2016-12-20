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
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="entidad.entidades"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <a class="btn btn-success btn-xs pull-right" href="<c:url value="/entidad/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="entidad.nuevo"/></a>
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="entidad.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty listado}">
                            <div class="alert alert-warning alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="entidad.entidad"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty listado}">

                            <div class="alert-grey">
                                <c:if test="${paginacion.totalResults == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="entidad.entidad"/>
                                </c:if>
                                <c:if test="${paginacion.totalResults > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="entidad.entidades"/>
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
                                        <col width="200">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th><spring:message code="regweb.nombre"/></th>
                                            <th><spring:message code="entidad.propietario"/></th>
                                            <th><spring:message code="entidad.codigoDir3"/></th>
                                            <th><spring:message code="regweb.activo"/></th>
                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var="entidad" items="${listado}">
                                            <tr>
                                                <td>${entidad.nombre}</td>
                                                <td>${entidad.propietario.nombreCompleto}</td>
                                                <td>${entidad.codigoDir3}</td>
                                                <td>
                                                    <c:if test="${entidad.activo}"><span class="label label-success">Si</span></c:if>
                                                    <c:if test="${not entidad.activo}"><span class="label label-danger">No</span></c:if>
                                                </td>
                                                <td class="center">
                                                    <c:if test="${entidad.activo}">
                                                        <a class="btn btn-warning btn-sm" href="<c:url value="/entidad/${entidad.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                        <a class="btn btn-info btn-sm" onclick='confirm("<c:url value="/entidad/${entidad.id}/reiniciarContadores"/>","<spring:message code="entidad.confirmar.reiniciarContadores" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="entidad.reiniciarContadores"/>"><span class="fa fa-refresh"></span></a>
                                                        <a class="btn btn-danger btn-sm" onclick='confirm("<c:url value="/entidad/${entidad.id}/anular"/>","<spring:message code="entidad.confirmar.anular" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="entidad.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                        <a class="btn btn-danger btn-sm" onclick='confirm("<c:url value="/entidad/${entidad.id}/eliminarRegistros"/>","<spring:message code="entidad.confirmar.eliminarRegistros" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="entidad.eliminarRegistros"/>"><span class="fa fa-envelope-o"></span></a>
                                                        <a class="btn btn-danger btn-sm" onclick='confirm("<c:url value="/entidad/${entidad.id}/eliminar"/>","<spring:message code="entidad.confirmar.eliminar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="entidad.eliminar"/>"><span class="fa fa fa-eraser"></span></a>
                                                    </c:if>
                                                    <c:if test="${not entidad.activo}">
                                                        <a class="btn btn-primary btn-sm" onclick='confirm("<c:url value="/entidad/${entidad.id}/activar"/>","<spring:message code="entidad.confirmar.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="entidad.activar"/>"><span class="fa fa-thumbs-o-up"></span></a>
                                                    </c:if>

                                                </td>
                                            </tr>
                                        </c:forEach>


                                    </tbody>
                                </table>

                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacion.jsp">
                                    <c:param name="entidad" value="entidad"/>
                                </c:import>


                            </div>

                        </c:if>

                    </div> <%--Fin panel-body--%>

                </div>
            </div>
        </div>


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>