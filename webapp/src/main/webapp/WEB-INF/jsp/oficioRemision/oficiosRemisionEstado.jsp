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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="oficioRemision.oficiosRemision"/> <spring:message code="oficioRemision.estado.${estado}" /></strong></li>
                </ol>
            </div>
        </div>

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i>
                            <strong><spring:message code="oficioRemision.oficiosRemision"/> <spring:message code="oficioRemision.estado.${estado}" /></strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-grey">
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="oficioRemision.oficioRemision"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.oficioRemision"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.oficioRemision"/>
                                        </c:if>

                                        <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                    </div>

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
                                                <th><spring:message code="oficioRemision.numeroOficio"/></th>
                                                <th><spring:message code="oficioRemision.fecha"/></th>
                                                <th><spring:message code="oficioRemision.oficina"/></th>
                                                <th><spring:message code="oficioRemision.organismoDestino"/></th>
                                                <th><spring:message code="registroSir.motivo"/></th>
                                                <th><spring:message code="oficioRemision.tipo"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="oficioRemision" items="${paginacion.listado}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</td>
                                                <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                <td><label class="no-bold" rel="popupAbajo" data-content="${oficioRemision.oficina.codigo}" data-toggle="popover">${oficioRemision.oficina.denominacion}</label></td>
                                                <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>

                                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}">
                                                    <td>
                                                        <%-- Decodificación T. anotación (Posteriormente se añadió esta info en Oficio Remisión, de ahí está comprobación)--%>
                                                        <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                            <c:if test="${empty oficioRemision.decodificacionTipoAnotacion}">
                                                                ${oficioRemision.registrosEntrada[0].registroDetalle.decodificacionTipoAnotacion}
                                                            </c:if>
                                                            <c:if test="${not empty oficioRemision.decodificacionTipoAnotacion}">
                                                                ${oficioRemision.decodificacionTipoAnotacion}
                                                            </c:if>
                                                        </c:if>

                                                        <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                            <c:if test="${empty oficioRemision.decodificacionTipoAnotacion}">
                                                                ${oficioRemision.registrosSalida[0].registroDetalle.decodificacionTipoAnotacion}
                                                            </c:if>
                                                            <c:if test="${not empty oficioRemision.decodificacionTipoAnotacion}">
                                                                ${oficioRemision.decodificacionTipoAnotacion}
                                                            </c:if>
                                                        </c:if>
                                                    </td>
                                                </c:if>

                                                <td>
                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                        <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                                    </c:if>

                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                        <span class="label label-danger"><spring:message code="oficioRemision.tipo.2"/></span>
                                                    </c:if>
                                                </td>

                                                <td class="center">
                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                        <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${oficioRemision.registrosEntrada[0].id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </c:if>

                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                        <a class="btn btn-danger btn-sm" href="<c:url value="/registroSalida/${oficioRemision.registrosSalida[0].id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                    <!-- Paginacion -->
                                    <c:import url="../modulos/paginacion.jsp">
                                        <c:param name="entidad" value="oficioRemision/${url}"/>
                                    </c:import>
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