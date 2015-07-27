<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title>${titulo}</title>
    <c:import url="../modulos/imports.jsp"/>
</head>

<body>

<c:import url="../modulos/menu.jsp"/>

<div class="row-fluid container main">

    <div class="well well-white">

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong>${titulo}</strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->


        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title">
                        	<i class="fa fa-search"></i><strong> ${titulo}</strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <c:if test="${empty registros}">
                            <div class="alert alert-warning alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registro.registro"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty registros}">

                            <div class="alert-grey">
                                <c:if test="${fn:length(registros) == 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${fn:length(registros)}</strong> <spring:message code="registro.registro"/>
                                </c:if>
                                <c:if test="${fn:length(registros) > 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${fn:length(registros)}</strong> <spring:message code="registro.registros"/>
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
                                        <col>
                                        <col>
                                        <col width="60">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="registroEntrada.libro.corto"/></th>
                                            <th><spring:message code="registroEntrada.usuario"/></th>
                                            <th><spring:message code="registroEntrada.oficina"/></th>
                                            <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                            <c:if test="${registroEntrada.estado == RegwebConstantes.ESTADO_PENDIENTE}">
                                                <th><spring:message code="registroEntrada.reserva"/></th>
                                            </c:if>
                                            <c:if test="${registroEntrada.estado != RegwebConstantes.ESTADO_PENDIENTE}">
                                                <th><spring:message code="registroEntrada.extracto"/></th>
                                            </c:if>
                                            <th><spring:message code="registroEntrada.anexos"/></th>

                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        <c:forEach var="registroEntrada" items="${registros}" varStatus="status">
                                            <tr>
                                                <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                <td><label class="no-bold" rel="ayuda" data-content="${registroEntrada.libro.nombre}" data-toggle="popover">${registroEntrada.libro.codigo}</label></td>
                                                <td>${registroEntrada.usuario.usuario.identificador}</td>
                                                <td><label class="no-bold" rel="ayuda" data-content="${registroEntrada.oficina.denominacion}" data-toggle="popover">${registroEntrada.oficina.codigo}</label></td>
                                                <td>${(empty registroEntrada.destino)? registroEntrada.destinoExternoDenominacion : registroEntrada.destino.denominacion}</td>

                                                <c:if test="${registroEntrada.estado == RegwebConstantes.ESTADO_PENDIENTE}">
                                                    <td>${registroEntrada.registroDetalle.reserva}</td>
                                                </c:if>
                                                <c:if test="${registroEntrada.estado != RegwebConstantes.ESTADO_PENDIENTE}">
                                                    <td>${registroEntrada.registroDetalle.extracto}</td>
                                                </c:if>
                                                <c:if test="${registroEntrada.registroDetalle.anexos != null}">
                                                    <td class="center">${fn:length(registroEntrada.registroDetalle.anexos)}</td>
                                                </c:if>
                                                <c:if test="${registroEntrada.registroDetalle.anexos == null}">
                                                    <td class="center">0</td>
                                                </c:if>

                                                <td class="center">
                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
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