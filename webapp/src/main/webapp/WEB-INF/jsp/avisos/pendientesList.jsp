<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroEntrada.reservas"/></title>
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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroEntrada.reservas"/></strong></li>
                </ol>
            </div>
        </div>
        <!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <i class="fa fa-search"></i><strong> <spring:message
                                code="registroEntrada.reservas"/></strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <c:if test="${empty registros}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert"
                                        aria-hidden="true">&times;</button>
                                <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message
                                    code="registro.registro"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty registros}">

                            <div class="alert-grey">
                                <c:if test="${fn:length(registros) == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${fn:length(registros)}</strong>
                                    <spring:message code="registro.registro"/>
                                </c:if>
                                <c:if test="${fn:length(registros) > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${fn:length(registros)}</strong>
                                    <spring:message code="registro.registros"/>
                                </c:if>
                            </div>

                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col width="80">
                                        <col>
                                        <col width="80">
                                        <col>
                                        <col>
                                        <col>
                                        <col width="100">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                        <th><spring:message code="registroEntrada.libro.corto"/></th>
                                        <th><spring:message code="registroEntrada.usuario"/></th>
                                        <th><spring:message code="registroEntrada.oficina"/></th>
                                        <th><spring:message code="registroEntrada.reserva"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="registro" items="${registros}" varStatus="status">
                                        <tr>
                                            <td>${registro.numeroRegistroFormateado}</td>
                                            <td><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy"/></td>
                                            <td><label class="no-bold" rel="popupAbajo"
                                                       data-content="${registro.libro.nombre}"
                                                       data-toggle="popover">${registro.libro.codigo}</label></td>
                                            <td>${registro.usuario.usuario.identificador}</td>
                                            <td><label class="no-bold" rel="popupAbajo"
                                                       data-content="${registro.oficina.codigo}"
                                                       data-toggle="popover">${registro.oficina.denominacion}</label>
                                            </td>
                                            <td>${registro.registroDetalle.reserva}</td>
                                            <td class="center">

                                                <a class="btn btn-info btn-sm"
                                                   href="<c:url value="/registroEntrada/${registro.id}/detalle"/>"
                                                   title="<spring:message code="registroEntrada.detalle"/>"><span
                                                        class="fa fa-eye"></span></a>
                                                <a class="btn btn-warning btn-sm"
                                                   href="<c:url value="/registroEntrada/${registro.id}/edit"/>"
                                                   title="<spring:message code="registroEntrada.editar"/>"><span
                                                        class="fa fa-pencil"></span></a>

                                                <%--<c:if test="${registro.class.simpleName == 'RegistroSalida'}">
                                                    <a class="btn btn-info btn-sm"
                                                       href="<c:url value="/registroSalida/${registro.id}/detalle"/>"
                                                       title="<spring:message code="registroEntrada.detalle"/>"><span
                                                            class="fa fa-eye"></span></a>
                                                </c:if>--%>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>

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