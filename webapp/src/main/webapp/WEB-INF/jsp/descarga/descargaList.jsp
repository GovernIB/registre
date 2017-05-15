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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="descarga.descargas"/></li>
                </ol>
            </div>
        </div>
        <!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message
                                code="descarga.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${listado != null && empty listado}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message
                                            code="descarga.descarga"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty listado}">
                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/>
                                            <strong>${paginacion.totalResults}</strong> <spring:message
                                                code="descarga.descarga"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/>
                                            <strong>${paginacion.totalResults}</strong> <spring:message
                                                code="descarga.descargas"/>
                                        </c:if>

                                        <p class="pull-right"><spring:message code="regweb.pagina"/>
                                            <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}
                                        </p>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="50">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th>Id</th>
                                                <th><spring:message code="descarga.fechaImportacion"/></th>
                                                <th><spring:message code="descarga.tipo"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="descarga" items="${listado}">
                                                <tr>
                                                    <td>${descarga.id}</td>
                                                    <td><fmt:formatDate value="${descarga.fechaImportacion}"
                                                                        pattern="dd/MM/yyyy"/></td>
                                                    <td>${descarga.tipo}</td>

                                                    <td class="center">
                                                        <a class="btn btn-danger btn-sm"
                                                           onclick='javascript:confirm("<c:url value="/descarga/${descarga.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);"
                                                           title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>


                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacion.jsp">
                                            <c:param name="entidad" value="descarga"/>
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