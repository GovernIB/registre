<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="modulos/imports.jsp"/>
</head>

    <body>

        <c:import url="modulos/menu.jsp"/>

        <div class="row-fluid container main">

            <div class="well well-white">

            <c:import url="modulos/mensajes.jsp"/>

                <div class="row">

                    <div class="col-xs-12">

                        <ol class="breadcrumb">
                            <c:import url="modulos/migadepan.jsp"/>
                        </ol>

                        <c:if test="${rolAutentidado.nombre == 'RWE_SUPERADMIN' || rolAutentidado.nombre == 'RWE_ADMIN'}">
                            <c:if test="${catalogo == null}">
                                <div class="alert alert-danger">
                                    <strong><spring:message code="regweb.aviso"/>: </strong> <spring:message code="catalogoDir3.catalogo.vacio"/>
                                </div>
                            </c:if>
                        </c:if>

                    </div>


                    <%--ÚLTIMOS REGISTROS DE ENTRADA DE LA OFICINA ACTIVA--%>
                    <c:if test="${not empty registroEntradas}">
                        <div class="col-xs-6">
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong>${fn:length(registroEntradas)} <spring:message code="registroEntrada.ultimos"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col width="80">
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                <th><spring:message code="registroEntrada.fecha"/></th>
                                                <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                <th><spring:message code="registroEntrada.usuario"/></th>
                                                <th><spring:message code="registroEntrada.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroEntrada" items="${registroEntradas}" varStatus="status">
                                                <tr>
                                                    <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                    <td>${registroEntrada.libro.nombre}</td>
                                                    <td>${registroEntrada.usuario.usuario.identificador}</td>
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
                    </c:if>

                    <%--ÚLTIMOS REGISTROS DE SALIDA DE LA OFICINA ACTIVA--%>
                    <c:if test="${not empty registroSalidas}">
                        <div class="col-xs-6">
                            <div class="panel panel-danger">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong>${fn:length(registroSalidas)} <spring:message code="registroSalida.ultimos"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col width="80">
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                                <th><spring:message code="registroSalida.fecha"/></th>
                                                <th><spring:message code="registroSalida.libro.corto"/></th>
                                                <th><spring:message code="registroSalida.usuario"/></th>
                                                <th><spring:message code="registroSalida.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroSalida" items="${registroSalidas}" varStatus="status">
                                                <tr>
                                                    <td>${registroSalida.numeroRegistroFormateado}</td>
                                                    <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                    <td>${registroSalida.libro.nombre}</td>
                                                    <td>${registroSalida.usuario.usuario.identificador}</td>
                                                    <td>${registroSalida.registroDetalle.extracto}</td>
                                                    <td class="center">
                                                        <a class="btn btn-info btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <%--REGISTROS DE ENTRADA PENDIENTES DE VISAR--%>
                    <c:if test="${not empty pendientesVisar}">
                        <div class="col-xs-6">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.pendientesVisar"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col width="80">
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                <th><spring:message code="registroEntrada.fecha"/></th>
                                                <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                <th><spring:message code="registroEntrada.usuario"/></th>
                                                <th><spring:message code="registroEntrada.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroEntrada" items="${pendientesVisar}" varStatus="status">
                                                <tr>
                                                    <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${registroEntrada.libro.nombre}</td>
                                                    <td>${registroEntrada.usuario.usuario.identificador}</td>
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
                        </c:if>

                    <%--OFICIONS PENDIENTES DE LLEGADA--%>
                    <c:if test="${not empty oficiosPendientesLlegada}">
                        <div class="col-xs-6">

                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.pendientesLlegada.ultimos"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
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
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="oficioRemision" items="${oficiosPendientesLlegada}" end="5">
                                                <tr>
                                                    <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</td>
                                                    <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${oficioRemision.oficina.denominacion}</td>
                                                    <td class="center">
                                                        <a class="btn btn-success btn-sm" href="<c:url value="/oficioRemision/${oficioRemision.id}/procesar"/>" title="<spring:message code="oficioRemision.procesar"/>"><span class="fa fa-check"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>

                                            </tbody>
                                        </table>

                                    </div>

                                </div>
                            </div>

                        </div>
                    </c:if>

                    <%--REGISTROS DE ENTRADA PENDIENTES (RESERVA)--%>
                    <c:if test="${not empty pendientes}">
                        <div class="col-xs-6">
                            <div class="panel panel-warning">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.pendientes"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col width="80">
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                <th><spring:message code="registroEntrada.fecha"/></th>
                                                <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                <th><spring:message code="registroEntrada.usuario"/></th>
                                                <th><spring:message code="registroEntrada.reserva"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroEntrada" items="${pendientes}" varStatus="status">
                                                <tr>
                                                    <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${registroEntrada.libro.nombre}</td>
                                                    <td>${registroEntrada.usuario.usuario.identificador}</td>
                                                    <td>${registroEntrada.registroDetalle.reserva}</td>
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
                    </c:if>

                    <%--OFICIONS PENDIENTES DE REMISIÓN INTERNA--%>
                    <c:if test="${not empty organismosOficioRemisionInterna}">
                        <div class="col-xs-6">

                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.organimosInterno.inicio"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="organismo.organismo"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="organismo" items="${organismosOficioRemisionInterna}">
                                                <tr>
                                                    <td>${organismo}</td>
                                                    <td class="center">
                                                        <a class="btn btn-success btn-sm" href="<c:url value="/oficioRemision/oficiosPendientesRemisionInterna"/>" title="<spring:message code="oficioRemision.buscador"/>"><span class="fa fa-search"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>

                                            </tbody>
                                        </table>

                                    </div>

                                </div>
                            </div>

                        </div>
                    </c:if>

                    <%--OFICIONS PENDIENTES DE REMISIÓN EXTERNA--%>
                    <c:if test="${not empty organismosOficioRemisionExterna}">
                        <div class="col-xs-6">

                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.organimosExterna.inicio"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="organismo.organismo"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="organismo" items="${organismosOficioRemisionExterna}">
                                                <tr>
                                                    <td>${organismo}</td>
                                                    <td class="center">
                                                        <a class="btn btn-success btn-sm" href="<c:url value="/oficioRemision/oficiosPendientesRemisionExterna"/>" title="<spring:message code="oficioRemision.buscador"/>"><span class="fa fa-search"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>

                                            </tbody>
                                        </table>

                                    </div>

                                </div>
                            </div>

                        </div>
                    </c:if>

                    <%--PREREGISTROS PENDIENTES DE PROCESAR--%>
                    <c:if test="${not empty preRegistros}">
                        <div class="col-xs-6">

                            <div class="panel panel-warning">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="preRegistro.preRegistro.inicio"/></strong> </h3>
                                </div>

                                <div class="panel-body">

                                    <div class="table-responsive-inici">

                                        <table class="table1 table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="preRegistro.numero"/></th>
                                                <th><spring:message code="preRegistro.fecha"/></th>
                                                <th><spring:message code="preRegistro.oficinaDestino"/></th>
                                                <th><spring:message code="preRegistro.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="preRegistro" items="${preRegistros}">
                                                <tr>
                                                    <td><fmt:formatDate value="${preRegistro.fecha}" pattern="yyyy"/> / ${preRegistro.numeroPreregistro}</td>
                                                    <td><fmt:formatDate value="${preRegistro.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${preRegistro.decodificacionEntidadRegistralDestino}</td>
                                                    <td>${preRegistro.registroDetalle.extracto}</td>
                                                    <td class="center">
                                                        <a class="btn btn-info btn-sm" href="<c:url value="/preRegistro/${preRegistro.id}/detalle"/>" title="<spring:message code="preRegistro.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>

                                            </tbody>
                                        </table>

                                    </div>

                                </div>
                            </div>

                        </div>
                    </c:if>




                </div><!-- /.row -->



            </div><!-- /.well-white -->
        </div> <!-- /container -->

        <c:import url="modulos/pie.jsp"/>

    </body>
</html>