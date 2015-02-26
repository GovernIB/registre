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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong>Oficio procesado</strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong>Oficio de Remisión</strong> </h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <div class="alert alert-grey">
                                    <spring:message code="oficioRemision.procesado.resumen"/> <strong>${fn:length(registrosEntrada)}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                </div>

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="80">
                                            <col>
                                            <col width="100">
                                            <col>
                                            <col>
                                            <col>
                                            <col>
                                            <col width="125">
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                <th><spring:message code="registroEntrada.fecha"/></th>
                                                <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                <th><spring:message code="registroEntrada.usuario"/></th>
                                                <th><spring:message code="registroEntrada.oficina"/></th>
                                                <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                <th><spring:message code="registroEntrada.extracto"/></th>
                                                 <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <c:forEach var="registroEntrada" items="${registrosEntrada}" varStatus="status">
                                                <tr>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="yyyy"/> / ${registroEntrada.numeroRegistro}</td>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${registroEntrada.libro.nombreCompleto}</td>
                                                    <td>${registroEntrada.usuario.usuario.identificador}</td>
                                                    <td>${registroEntrada.oficina.denominacion}</td>
                                                    <td>${(empty registroEntrada.destino)? registroEntrada.destinoExternoDenominacion : registroEntrada.destino.denominacion}</td>
                                                    <td>${registroEntrada.registroDetalle.extracto}</td>

                                                    <td class="center">
                                                        <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>


                                </div>

                            </div>
                        </div>
                    </div>

                </div>
             </div>
            </div>
        </div>

</div><!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>