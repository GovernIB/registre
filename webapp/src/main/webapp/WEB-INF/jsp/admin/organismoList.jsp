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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="organismo.organismos"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <c:import url="../modulos/mensajes.jsp"/>
                <div id="mensajes"></div>

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message
                                code="organismo.buscador"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <form:form modelAttribute="organismoBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber" value="1"/>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message
                                        code="entidad.entidad"/></div>
                                <div class="col-xs-8">
                                    <form:select path="entidad" items="${entidades}" itemValue="id" itemLabel="nombre"
                                                 cssClass="chosen-select"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right">
                                    <spring:message code="organismo.estado"/>
                                </div>
                                <div class="col-xs-8">
                                    <form:select path="organismo.estado.id" items="${estados}" itemValue="id"
                                                 itemLabel="descripcionEstadoEntidad" cssClass="chosen-select"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="regweb.nombre"/></div>
                                <div class="col-xs-8">
                                    <form:input path="organismo.denominacion" cssClass="form-control"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message
                                        code="organismo.codigo"/></div>
                                <div class="col-xs-8">
                                    <form:input path="organismo.codigo" cssClass="form-control"/>
                                </div>
                            </div>

                            <%--<div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message
                                        code="organismo.permiteUsuarios"/></div>
                                <div class="col-xs-8">
                                    <form:checkbox path="organismo.permiteUsuarios"/>
                                </div>
                            </div>--%>

                            <div class="form-group col-xs-12">
                                <input type="submit" value="<spring:message code="regweb.buscar"/>"
                                       class="btn btn-warning btn-sm"/>
                                <input type="reset" value="<spring:message code="regweb.restablecer"/>"
                                       class="btn btn-sm"/>
                            </div>
                        </form:form>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${paginacion != null && empty paginacion.listado}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert"
                                                    aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message
                                                code="organismo.organismo"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">

                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message
                                                    code="organismo.organismo"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message
                                                    code="organismo.organismos"/>
                                            </c:if>
                                            <p class="pull-right"><spring:message code="regweb.pagina"/>
                                                <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}
                                            </p>
                                        </div>

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="organismo.organismo"/></th>
                                                    <th><spring:message code="entidad.codigoDir3"/></th>
                                                    <th width="50"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="organismo" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>${organismo.denominacion}</td>
                                                        <td>${organismo.codigo}</td>
                                                        <td class="center">
                                                            <a class="btn btn-warning btn-sm"
                                                               href="<c:url value="/admin/organismo/${organismo.id}/edit"/>"
                                                               title="<spring:message code="regweb.editar"/>"><span
                                                                    class="fa fa-pencil"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="organismo"/>
                                            </c:import>

                                        </div>
                                    </c:if>

                                </div>
                            </div>

                        </c:if>
                    </div>

                </div> <!--/.panel success-->

            </div>
        </div> <!-- /.row-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>