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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="oficioRemision.pendientesLlegada"/></strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>


        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.pendientesLlegada"/></strong> </h3>
                    </div>

                    <div class="panel-body">

                        <c:if test="${oficiosPendientesLlegada != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty oficiosPendientesLlegada}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="oficioRemision.oficioRemision"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty oficiosPendientesLlegada}">

                                        <div class="alert-grey">
                                            <c:if test="${fn:length(oficiosPendientesLlegada) == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${fn:length(oficiosPendientesLlegada)}</strong> <spring:message code="oficioRemision.pendientesLlegada"/>
                                            </c:if>
                                            <c:if test="${fn:length(oficiosPendientesLlegada) > 1}">
                                                <spring:message code="regweb.resultados"/> <strong>${fn:length(oficiosPendientesLlegada)}</strong> <spring:message code="oficioRemision.pendientesLlegada"/>
                                            </c:if>
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
                                                    <col width="51">
                                                </colgroup>
                                                <thead>
                                                    <tr>
                                                        <th><spring:message code="oficioRemision.numeroOficio"/></th>
                                                        <th><spring:message code="oficioRemision.fecha"/></th>
                                                        <th><spring:message code="oficioRemision.libro.corto"/></th>
                                                        <th><spring:message code="oficioRemision.usuario"/></th>
                                                        <th><spring:message code="oficioRemision.oficina"/></th>
                                                        <th><spring:message code="oficioRemision.organismoDestino"/></th>
                                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <c:forEach var="oficioRemision" items="${oficiosPendientesLlegada}">
                                                        <tr>
                                                            <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</td>
                                                            <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td><label class="no-bold" rel="ayuda" data-content="${oficioRemision.libro.nombre}" data-toggle="popover">${oficioRemision.libro.codigo}</label></td>
                                                            <td>${oficioRemision.usuarioResponsable.usuario.nombreCompleto}</td>
                                                            <td><label class="no-bold" rel="ayuda" data-content="${oficioRemision.oficina.denominacion}" data-toggle="popover">${oficioRemision.oficina.codigo}</label></td>
                                                            <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>

                                                            <td class="center">
                                                                <a class="btn btn-success btn-sm" href="<c:url value="/oficioRemision/${oficioRemision.id}/procesar"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-check"></span></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>

                                                </tbody>
                                            </table>

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
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

</body>
</html>