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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message
                            code="oficioRemision.pendientesLlegada"/></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>


        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.aceptados"/></strong> </h3>
                    </div>

                        <form:form modelAttribute="oficioRemisionBusqueda" method="post" cssClass="form-horizontal">
                            <form:hidden path="pageNumber"/>

                            <div class="panel-body">

                                <div class="col-xs-12">
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="tipoOficioRemision" rel="ayuda" data-content="<spring:message code="registro.ayuda.tipo.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.tipo"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="tipoOficioRemision" cssClass="chosen-select">
                                                <c:forEach items="${tiposOficioRemision}" var="tipo">
                                                    <form:option value="${tipo}"><spring:message code="oficioRemision.tipo.${tipo}" /></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>

                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="oficioRemision.numeroOficio" rel="ayuda" data-content="<spring:message code="registro.ayuda.numeroOficio.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.numeroOficio"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="oficioRemision.numeroOficio" cssClass="form-control" maxlength="10"/>
                                        </div>
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
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="oficioRemision.aceptados"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">

                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.aceptados"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.aceptados"/>
                                            </c:if>
                                        </div>

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <%--<col width="100">--%>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col width="51">
                                                </colgroup>
                                                <thead>
                                                    <tr>
                                                        <th><spring:message code="oficioRemision.numeroOficio"/></th>
                                                        <th><spring:message code="oficioRemision.fecha.aceptado"/></th>
                                                        <%--<th><spring:message code="oficioRemision.usuario"/></th>--%>
                                                        <th><spring:message code="oficioRemision.oficina"/></th>
                                                        <th><spring:message code="oficioRemision.organismoDestino"/></th>
                                                        <th><spring:message code="oficioRemision.numero.re"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <c:forEach var="oficioRemision" items="${paginacion.listado}">
                                                        <tr>
                                                            <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</td>
                                                            <td><fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                            <%--<td>${oficioRemision.usuarioResponsable.usuario.nombreCompleto}</td>--%>
                                                            <td><label class="no-bold" rel="ayuda"
                                                                       data-content="${oficioRemision.oficina.codigo}"
                                                                       data-toggle="popover">${oficioRemision.oficina.denominacion}</label>
                                                            </td>
                                                            <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                            <td>
                                                                <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                    ${fn:length(oficioRemision.registrosEntrada)}
                                                                </c:if>
                                                                <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                    ${fn:length(oficioRemision.registrosSalida)}
                                                                </c:if>
                                                            </td>

                                                            <td class="center">
                                                                <a class="btn btn-info btn-sm" href="<c:url value="/oficioRemision/${oficioRemision.id}/detalle"/>" title="<spring:message code="oficioRemision.detalle"/>"><span class="fa fa-eye"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="oficioRemision"/>
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

<c:import url="../modulos/pie.jsp"/>

</body>
</html>