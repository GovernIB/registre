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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong>${titulo}</strong></li>
                </ol>
            </div>
        </div>

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-danger">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i>
                           <strong>${titulo}</strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-grey">
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="registroSalida.registroSalida"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                        </c:if>

                                        <%--Select de "Ir a página"--%>
                                        <c:import url="../modulos/paginas.jsp"/>
                                    </div>

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="80">
                                            <col>
                                            <col width="80">
                                            <col>
                                            <%--<col>--%>
                                            <col>
                                            <col>
                                            <col width="50">
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th class="center"><spring:message code="registroSalida.numeroRegistro"/></th>
                                                <th class="center"><spring:message code="registroSalida.fecha"/></th>
                                                <th class="center"><spring:message code="registroSalida.usuario"/></th>
                                                <th class="center"><spring:message code="registroSalida.oficina"/></th>
                                                <%--<th class="center"><spring:message code="organismo.destino"/></th>--%>
                                                <th class="center"><spring:message code="registroSalida.extracto"/></th>
                                                <th class="center"><spring:message code="registroSalida.estado"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="registro" items="${paginacion.listado}" varStatus="status">
                                            <tr>
                                                <td>${registro.numeroRegistroFormateado}</td>
                                                <td class="center"><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy"/></td>
                                                <td class="center">${registro.usuario.usuario.identificador}</td>
                                                <td class="center"><label class="no-bold" rel="popupAbajo" data-content="${registro.oficina.codigo}" data-toggle="popover">${registro.oficina.denominacion}</label></td>
                                                <%--<td>${registro.interesadoDestinoDenominacion}</td>--%>
                                                <td>${registro.registroDetalle.extracto}</td>
                                                <td class="center">
                                                    <c:import url="../registro/estadosRegistro.jsp">
                                                        <c:param name="estado" value="${registro.estado}"/>
                                                        <c:param name="decodificacionTipoAnotacion" value="${registro.registroDetalle.decodificacionTipoAnotacion}"/>
                                                    </c:import>
                                                </td>
                                                <td class="center">
                                                    <a class="btn btn-danger btn-sm" href="<c:url value="/registroSalida/${registro.id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                    <!-- Paginacion -->
                                    <c:import url="../modulos/paginacion.jsp">
                                        <c:param name="entidad" value="registroSalida/${url}"/>
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