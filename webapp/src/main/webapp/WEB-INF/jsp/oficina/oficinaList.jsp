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

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="oficina.oficinas"/></li>
                </ol>
            </div>
        </div>
        <!-- /.row -->
		<div id="mensajes"></div>
        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message
                                code="oficina.buscador.entidad"/> ${entidad.nombre} (${entidad.codigoDir3})</strong></h3>
                    </div>

                    <div class="panel-body">
                        <form:form modelAttribute="oficinaBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber"/>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="regweb.nombre"/></div>
                                <div class="col-xs-8">
                                    <form:input path="oficina.denominacion" cssClass="form-control"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="oficina.codigo"/></div>
                                <div class="col-xs-8">
                                    <form:input path="oficina.codigo" cssClass="form-control"/>
                                </div>
                            </div>

                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="organismo.estado"/></div>
                                <div class="col-xs-8">
                                    <form:select path="oficina.estado.id" items="${estados}" itemValue="id"
                                                 itemLabel="descripcionEstadoEntidad" cssClass="chosen-select"/>
                                </div>
                            </div>

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
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message
                                                code="oficina.oficina"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">
                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message
                                                    code="oficina.oficina"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message
                                                    code="oficina.oficinas"/>
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
                                                    <col>
                                                    <col width="51">
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="regweb.nombre"/></th>
                                                    <th><spring:message code="oficina.codigo"/></th>
                                                    <th><spring:message code="oficina.organismoResponsable"/></th>
                                                    <th><spring:message code="oficina.sir"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="oficina" items="${paginacion.listado}">
                                                    <tr>
                                                        <td>${oficina.denominacion}</td>
                                                        <td>${oficina.codigo}</td>
                                                        <td>${oficina.organismoResponsable.denominacion}</td>
                                                        <td>
                                                            <c:if test="${oficina.oficinaSir}">
                                                            	<c:url value="/oficina/${oficina.id}/modificar" var="urlModificar"/>
                                                            	<input class="form-check-input checkOficina" type="checkbox" onchange="habilitarSir('false','${urlModificar}')" checked>
                                                                <%-- 
                                                                <span class="label label-success"><spring:message code="regweb.si"/></span>
                                                                --%>
                                                            </c:if>

                                                            <c:if test="${not oficina.oficinaSir}">
                                                            	<c:url value="/oficina/${oficina.id}/modificar" var="urlModificar"/>
                                                            	<input class="form-check-input checkOficina" type="checkbox" onchange="habilitarSir('true','${urlModificar}')">
                                                            	<%-- 
                                                                <span class="label label-danger"><spring:message code="regweb.no"/></span>
                                                                --%>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">
                                                            <a class="btn btn-warning btn-sm"
                                                               href="<c:url value="/oficina/${oficina.id}/detalle"/>"
                                                               title="<spring:message code="oficina.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>


                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="oficina"/>
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


    </div>
</div>
<!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">

var tradsModSir = [];
tradsModSir['registroSir.modificar.ok'] = "<spring:message code='registroSir.modificar.ok' javaScriptEscape='true' />";
tradsModSir['registroSir.modificar.ko'] = "<spring:message code='registroSir.modificar.ko' javaScriptEscape='true' />";
</script>
</body>
</html>