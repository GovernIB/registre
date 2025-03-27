<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/> - <spring:message code="entidad.descargas.lista"/></title>
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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="entidad.descargas"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <c:import url="../modulos/mensajes.jsp"/>
                <div id="mensajes"></div>

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="entidad.descargas.lista"/> ${entidad.nombre}</strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty listado}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="sincronizacion.sincronizacion"/></strong>
                                    </div>

                                </c:if>


                               <c:if test="${not empty listado}">

                                   <div class="alert-grey">
                                       <c:if test="${paginacion.totalResults == 1}">
                                           <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="sincronizacion.sincronizacion"/>
                                       </c:if>
                                       <c:if test="${paginacion.totalResults > 1}">
                                           <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="sincronizacion.sincronizaciones"/>
                                       </c:if>

                                       <%--Select de "Ir a página"--%>
                                       <c:import url="../modulos/paginas.jsp"/>
                                   </div>


                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                                <tr>
                                                    <th>Id</th>
                                                    <th><spring:message code="sincronizacion.fecha"/></th>
                                                    <th><spring:message code="regweb.tipo"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <c:forEach var="descarga" items="${listado}">
                                                    <tr>
                                                        <td>${descarga.id}</td>
                                                        <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${descarga.fechaImportacion}" /></td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${descarga.tipo == RegwebConstantes.DESCARGA_UNIDAD}">
                                                                    <span class="label label-success"><spring:message code="sincronizacion.tipo.${descarga.tipo}" /></span>
                                                                </c:when>
                                                                <c:when test="${descarga.tipo == RegwebConstantes.DESCARGA_OFICINA}">
                                                                    <span class="label label-warning"><spring:message code="sincronizacion.tipo.${descarga.tipo}" /></span>
                                                                </c:when>
                                                                <c:when test="${descarga.tipo == RegwebConstantes.DESCARGA_CATALOGO}">
                                                                    <span class="label label-default"><spring:message code="sincronizacion.tipo.${descarga.tipo}" /></span>
                                                                </c:when>
                                                            </c:choose>
                                                        </td>
                                                        <td class="center">
                                                            <%--<a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/sincronizacion/${descarga.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>--%>
                                                            <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoSincronizacion" onclick="infoSincronizacion('${descarga.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacion.jsp">
                                            <c:param name="entidad" value="sincronizacion"/>
                                        </c:import>

                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div> <!--/.panel success-->

            </div>
        </div> <!-- /.row-->
    </div>
</div> <!-- /container -->

<div id="infoSincronizacion" class="modal fade">
    <div class="modal-dialog modal-lg">

        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarSincronizacion()">x
                </button>
                <h4 class="modal-title"><spring:message code="sincronizacion.informacion"/></h4>
            </div>
            <div class="modal-body">

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="sincronizacion.fecha"/>:</label>
                    </div>
                    <div class="col-xs-10" id="fecha"></div>
                </div>

                <div class="form-group col-xs-12">
                    <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="sincronizacion.tipo"/>:</label>
                    </div>
                    <div class="col-xs-10" id="tipo"></div>
                </div>

                <div class="form-group col-xs-12" id="elementosBox">
                    <div class="col-xs-12 pull-left etiqueta_regweb control-label">
                        <label><spring:message code="sincronizacion.elementos"/>:</label>
                    </div>

                    <pre id="elementos" class=".pre-scrollable"></pre>

                </div>

                <div class="clearfix"></div>

            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarSincronizacion()">
                    <spring:message code="regweb.cerrar"/>
                </button>
            </div>
        </div>
    </div>

</div>

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/sincronizacion.js"/>"></script>

<script type="text/javascript">

    var urlObtenerSincronizacion = '<c:url value="/rest/obtenerSincronizacion"/>';

    var tradsSincronizacion = [];
    tradsSincronizacion['sincronizacion.tipo.1'] = "<spring:message code='sincronizacion.tipo.1' javaScriptEscape='true' />";
    tradsSincronizacion['sincronizacion.tipo.2'] = "<spring:message code='sincronizacion.tipo.2' javaScriptEscape='true' />";
    tradsSincronizacion['sincronizacion.tipo.3'] = "<spring:message code='sincronizacion.tipo.3' javaScriptEscape='true' />";

</script>

</body>
</html>