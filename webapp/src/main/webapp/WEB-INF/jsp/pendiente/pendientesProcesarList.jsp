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
                    <li class="active"><i class="fa fa-list-ul"></i> Pendientes de procesar</li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong>Listado de Pendientes de procesar</strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-6">
                                <div class="form-group col-xs-12">
                                    <form:form modelAttribute="pendienteBusqueda" method="post" cssClass="form-horizontal">
                                        <form:hidden path="pageNumber"/>
                                        <div class="col-xs-4 pull-left etiqueta_regweb control-label">
                                            <form:label path="procesado">Procesado</form:label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="procesado" cssClass="chosen-select" onchange="doFormPaginacion('#pendienteBusqueda')">
                                                <form:option value="">...</form:option>
                                                <form:option value="true"><spring:message code="regweb.si"/></form:option>
                                                <form:option value="false"><spring:message code="regweb.no"/></form:option>
                                            </form:select>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        </div>

                        <c:if test="${empty listado}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.listado.vacio"/> <strong>Pendientes de procesar</strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty listado}">

                            <div class="alert-grey">
                                <c:if test="${paginacion.totalResults == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> Pendientes de procesar
                                </c:if>
                                <c:if test="${paginacion.totalResults > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> Pendientes de procesar
                                </c:if>

                                <p class="pull-right"><spring:message code="regweb.pagina"/><strong> ${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                            </div>

                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <col>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="organismo.organismo"/></th>
                                        <th>Procesado</th>
                                        <th><spring:message code="organismo.estado"/></th>
                                        <th><spring:message code="regweb.fecha"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="pendiente" items="${listado}">
                                        <tr>
                                            <td>${pendiente.idOrganismo}</td>
                                            <td>
                                                <c:if test="${pendiente.procesado}">
                                                    <span class="label label-success"><spring:message code="regweb.si"/></span>
                                                 </c:if>
                                                <c:if test="${not pendiente.procesado}">
                                                    <span class="label label-danger"><spring:message code="regweb.no"/></span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${pendiente.estado == RegwebConstantes.ESTADO_ENTIDAD_ANULADO}">
                                                        <span class="label label-danger"><spring:message code="unidad.estado.${pendiente.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${pendiente.estado == RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO}">
                                                        <span class="label label-warning"><spring:message code="unidad.estado.${pendiente.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${pendiente.estado == RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO}">
                                                        <span class="label label-default"><spring:message code="unidad.estado.${pendiente.estado}" /></span>
                                                    </c:when>
                                                    <c:when test="${pendiente.estado == RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}">
                                                        <span class="label label-success"><spring:message code="unidad.estado.${pendiente.estado}" /></span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${pendiente.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                            </td>

                                            <td class="center">
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/organismo/${pendiente.idOrganismo}/detalle"/>" target="_blank" title="<spring:message code="organismo.detalle"/>"><span class="fa fa-institution"></span></a>
                                                <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/pendiente/${pendiente.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

                                <!-- Paginacion -->
                                <c:import url="../modulos/paginacionBusqueda.jsp">
                                    <c:param name="entidad" value="pendiente"/>
                                </c:import>

                            </div>

                        </c:if>

                    </div>
                    <%--Fin panel-body--%>

                </div>
            </div>
        </div>


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>