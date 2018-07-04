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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="modeloOficioRemision.listado"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <a class="btn btn-warning btn-xs pull-right  margin-left10" href="<c:url value="/modeloOficioRemision/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="modeloOficioRemision.nuevo"/></a>
                        <a data-toggle="modal" href="#myModalModeloOficioRemision" class="btn btn-warning btn-xs pull-right"><spring:message code="regweb.ayuda"/></a>
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="modeloOficioRemision.listado"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty listado}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="modeloOficioRemision.modeloOficioRemision"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty listado}">

                            <div class="alert-grey">
                                <c:if test="${paginacion.totalResults == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="modeloOficioRemision.modeloOficioRemision"/>
                                </c:if>
                                <c:if test="${paginacion.totalResults > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="modeloOficioRemision.modeloOficiosRemision"/>
                                </c:if>

                                <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                            </div>

                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="regweb.nombre"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="modeloOficioRemision" items="${listado}">
                                        <tr>
                                            <td>${modeloOficioRemision.nombre}</td>
                                            <td class="center">
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/modeloOficioRemision/${modeloOficioRemision.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/modeloOficioRemision/${modeloOficioRemision.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacion.jsp">
                                    <c:param name="entidad" value="modeloOficioRemision"/>
                                </c:import>

                            </div>

                        </c:if>

                    </div>

                </div>
            </div>
        </div>


    </div>
</div> <!-- /container -->

<!-- ************* <spring:message code="regweb.inicio"/> Modal Ayuda Modelo Oficio Remisión ************************** -->
<div class="modal fade" id="myModalModeloOficioRemision">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title"><spring:message code="modeloOficioRemision.ayuda.modeloOficioRemision"/></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-12">
                        <div class="panel panel-success">
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12 pull-left"><spring:message code="modeloOficioRemision.ayuda.contenido.modeloOficioRemision"/></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <a href="javascript:void(0);" data-dismiss="modal" class="btn btn-warning  btn-sm"><spring:message code="regweb.cerrar"/></a>
            </div>
        </div>
    </div>
</div>
<!-- *************Fi Modal Ayuda Modelo Recibo ************************** -->


<c:import url="../modulos/pie.jsp"/>


</body>
</html>