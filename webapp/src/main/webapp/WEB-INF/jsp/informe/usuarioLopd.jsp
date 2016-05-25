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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.usuarioLopd"/></li>
                </ol>
            </div>
        </div><!-- /.row -->


        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="informe.usuarioLopd"/></strong> </h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="usuarioLopdBusquedaForm" method="post" cssClass="form-horizontal" name="usuarioLopdBusquedaForm" onsubmit="return validaFormulario(this)">
                            <div class="row">
                                <div class="form-group col-xs-6  pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="fechaInicio"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaInicio" maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="fechaFin"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="libro"><spring:message code="libro.libro"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right">
                                        <form:select path="libro"  cssClass="chosen-select" onchange="actualizarUsuarios(this)">
                                            <form:option path="libro" value="-1" selected="selected">...</form:option>
                                            <form:options path="libro" items="${libros}" itemValue="id" itemLabel="nombre"/>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 pad-left">
                                    <div class="col-xs-3 pull-left etiqueta_regweb control-label">
                                        <form:label path="usuario"><span class="text-danger">*</span> <spring:message code="usuario.usuario"/></form:label>
                                    </div>
                                    <div class="col-xs-9 no-pad-right" id="user">
                                        <div>
                                            <form:select path="usuario" cssClass="chosen-select" multiple="false">
                                                <form:option path="usuario" value="-1" selected="selected">...</form:option>
                                                <form:options path="usuario" items="${usuarios}" itemValue="id" itemLabel="nombreCompleto"/>
                                            </form:select>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning"><spring:message code="regweb.buscar"/></button>
                            </div>

                            <c:set var="errorInicio"><spring:message code="error.fechaInicio.posterior"/></c:set>
                            <input id="error1" type="hidden" value="${errorInicio}"/>
                            <c:set var="errorFin"><spring:message code="error.fechaFin.posterior"/></c:set>
                            <input id="error2" type="hidden" value="${errorFin}"/>
                            <c:set var="errorInicioFin"><spring:message code="error.fechaInicioFin.posterior"/></c:set>
                            <input id="error3" type="hidden" value="${errorInicioFin}"/>

                        </form:form>

                     </div>
                </div>

                <c:if test="${usuarioLopdBusquedaForm.usuario != null}">

                    <div class="row">
                        <div class="col-xs-12">

                        <!-- PARÁMETROS DE BÚSQUEDA -->
                        <div class="panel panel-success">
                            <div class="panel-heading">
                                <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                    <strong>
                                        <spring:message code="informe.usuarioLopd"/>
                                    </strong>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="col-xs-12"><strong><spring:message code="usuario.usuario"/>: ${usuario.identificador}</strong></div>
                                <div class="col-xs-12"><strong><spring:message code="informe.fechaInicio"/>: <fmt:formatDate value="${usuarioLopdBusquedaForm.fechaInicio}" pattern="dd/MM/yyyy"/></strong></div>
                                <div class="col-xs-12"><strong><spring:message code="informe.fechaFin"/>: <fmt:formatDate value="${usuarioLopdBusquedaForm.fechaFin}" pattern="dd/MM/yyyy"/></strong></div>
                                <c:if test="${usuarioLopdBusquedaForm.libro > 0}">
                                    <div class="col-xs-12"><strong><spring:message code="libro.libro"/>: ${libro.nombreCompleto}</strong></div>
                                </c:if>
                            </div>
                        </div>

                            <!-- REGISTROS DE ENTRADA -->
                            <div class="panel panel-info">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                        <strong>
                                            <spring:message code="registroEntrada.registroEntradas"/>
                                        </strong>
                                    </h3>
                                </div>
                                <div class="panel-body">

                                    <!-- REGISTROS DE ENTRADA CREADOS -->
                                    <div class="alert-lopd alert-info alert-dismissable">
                                        <strong><spring:message code="regweb.creados"/></strong>
                                    </div>

                                    <c:if test="${empty entradasCreadas}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty entradasCreadas}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.anyRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                    <th><spring:message code="registroEntrada.oficina"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="registroEntrada" items="${entradasCreadas}" varStatus="status">
                                                    <tr>
                                                        <td>${registroEntrada.numeroRegistro}</td>
                                                        <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="yyyy"/></td>
                                                        <td>${registroEntrada.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                        <td>${registroEntrada.oficina.denominacion}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS DE ENTRADA MODIFICADOS -->
                                    <div class="alert-lopd alert-info alert-dismissable">
                                        <strong><spring:message code="regweb.modificados"/></strong>
                                    </div>

                                    <c:if test="${empty entradasModificadas}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty entradasModificadas}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.anyRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                    <th><spring:message code="regweb.modificacion"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="historicoRegistroEntrada" items="${entradasModificadas}" varStatus="status">
                                                    <tr>
                                                        <td>${historicoRegistroEntrada.registroEntrada.numeroRegistro}</td>
                                                        <td><fmt:formatDate value="${historicoRegistroEntrada.registroEntrada.fecha}" pattern="yyyy"/></td>
                                                        <td>${historicoRegistroEntrada.registroEntrada.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${historicoRegistroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                        <td>${historicoRegistroEntrada.modificacion}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS DE ENTRADA CONSULTADOS -->
                                    <div class="alert-lopd alert-info alert-dismissable">
                                        <strong><spring:message code="regweb.consultados"/></strong>
                                    </div>

                                    <c:if test="${empty consultasEntrada}">
                                        <div class="alert-lopd alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty consultasEntrada}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.anyRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="lopd" items="${consultasEntrada}" varStatus="status">
                                                    <tr>
                                                        <td>${lopd.numeroRegistro}</td>
                                                        <td>${lopd.anyoRegistro}</td>
                                                        <td>${lopd.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS DE ENTRADA LISTADOS -->
                                    <div class="alert-lopd alert-info alert-dismissable">
                                        <strong><spring:message code="regweb.listados"/></strong>
                                    </div>

                                    <c:if test="${empty listadosEntrada}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty listadosEntrada}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.anyRegistro"/></th>
                                                    <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="lopd" items="${listadosEntrada}" varStatus="status">
                                                    <tr>
                                                        <td>${lopd.numeroRegistro}</td>
                                                        <td>${lopd.anyoRegistro}</td>
                                                        <td>${lopd.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                </div>
                            </div>

                            <!-- REGISTROS DE SALIDA -->
                            <div class="panel panel-danger">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                        <strong>
                                            <spring:message code="registroSalida.registroSalidas"/>
                                        </strong>
                                    </h3>
                                </div>
                                <div class="panel-body">

                                    <!-- REGISTROS DE SALIDA CREADOS -->
                                    <div class="alert-lopd alert-danger alert-dismissable">
                                        <strong><spring:message code="regweb.creados"/></strong>
                                    </div>

                                    <c:if test="${empty salidasCreadas}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty salidasCreadas}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                                    <th><spring:message code="registroSalida.anyRegistro"/></th>
                                                    <th><spring:message code="registroSalida.libro.corto"/></th>
                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                    <th><spring:message code="registroSalida.oficina"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="registroSalida" items="${salidasCreadas}" varStatus="status">
                                                    <tr>
                                                        <td>${registroSalida.numeroRegistro}</td>
                                                        <td><fmt:formatDate value="${registroSalida.fecha}" pattern="yyyy"/></td>
                                                        <td>${registroSalida.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                        <td>${registroSalida.oficina.denominacion}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS DE SALIDA MODIFICADOS -->
                                    <div class="alert-lopd alert-danger alert-dismissable">
                                        <strong><spring:message code="regweb.modificados"/></strong>
                                    </div>

                                    <c:if test="${empty salidasModificadas}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty salidasModificadas}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                                    <th><spring:message code="registroSalida.anyRegistro"/></th>
                                                    <th><spring:message code="registroSalida.libro.corto"/></th>
                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                    <th><spring:message code="regweb.modificacion"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="historicoRegistroSalida" items="${salidasModificadas}" varStatus="status">
                                                    <tr>
                                                        <td>${historicoRegistroSalida.registroSalida.numeroRegistro}</td>
                                                        <td><fmt:formatDate value="${historicoRegistroSalida.registroSalida.fecha}" pattern="yyyy"/></td>
                                                        <td>${historicoRegistroSalida.registroSalida.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${historicoRegistroSalida.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                        <td>${historicoRegistroSalida.modificacion}</td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS DE SALIDA CONSULTADOS -->
                                    <div class="alert-lopd alert-danger alert-dismissable">
                                        <strong><spring:message code="regweb.consultados"/></strong>
                                    </div>

                                    <c:if test="${empty consultasSalida}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty consultasSalida}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                                    <th><spring:message code="registroSalida.anyRegistro"/></th>
                                                    <th><spring:message code="registroSalida.libro.corto"/></th>
                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="lopd" items="${consultasSalida}" varStatus="status">
                                                    <tr>
                                                        <td>${lopd.numeroRegistro}</td>
                                                        <td>${lopd.anyoRegistro}</td>
                                                        <td>${lopd.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS DE SALIDA LISTADOS -->
                                    <div class="alert-lopd alert-danger alert-dismissable">
                                        <strong><spring:message code="regweb.listados"/></strong>
                                    </div>

                                    <c:if test="${empty listadosSalida}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty listadosSalida}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                                    <th><spring:message code="registroSalida.anyRegistro"/></th>
                                                    <th><spring:message code="registroSalida.libro.corto"/></th>
                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="lopd" items="${listadosSalida}" varStatus="status">
                                                    <tr>
                                                        <td>${lopd.numeroRegistro}</td>
                                                        <td>${lopd.anyoRegistro}</td>
                                                        <td>${lopd.libro.nombreCompleto}</td>
                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                </div>
                            </div>

                            <!-- REGISTROS MIGRADOS -->
                            <div class="panel panel-success">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                        <strong>
                                            <spring:message code="registroMigrado.registroMigrados"/>
                                        </strong>
                                    </h3>
                                </div>
                                <div class="panel-body">

                                    <!-- REGISTROS MIGRADOS CONSULTADOS -->
                                    <div class="alert-lopd alert-success alert-dismissable">
                                        <strong><spring:message code="regweb.consultados"/></strong>
                                    </div>

                                    <c:if test="${empty consultasMigrado}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty consultasMigrado}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroMigrado.numeroRegistro"/></th>
                                                    <th><spring:message code="registroMigrado.ano"/></th>
                                                    <th><spring:message code="registroMigrado.oficina"/></th>
                                                    <th><spring:message code="registroMigrado.tipoRegistro"/></th>
                                                    <th><spring:message code="registroMigrado.fecha"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="lopdMigrado" items="${consultasMigrado}" varStatus="status">
                                                    <tr>
                                                        <td>${lopdMigrado.registroMigrado.numero}</td>
                                                        <td>${lopdMigrado.registroMigrado.ano}</td>
                                                        <td>${lopdMigrado.registroMigrado.denominacionOficina}</td>
                                                        <c:if test="${lopdMigrado.registroMigrado.tipoRegistro}">
                                                            <td><spring:message code="informe.entrada"/></td>
                                                        </c:if>
                                                        <c:if test="${!lopdMigrado.registroMigrado.tipoRegistro}">
                                                            <td><spring:message code="informe.salida"/></td>
                                                        </c:if>
                                                        <td><fmt:formatDate value="${lopdMigrado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>

                                    </c:if>

                                    <!-- REGISTROS MIGRADOS LISTADOS -->
                                    <div class="alert-lopd alert-success alert-dismissable">
                                        <strong><spring:message code="regweb.listados"/></strong>
                                    </div>

                                    <c:if test="${empty listadosMigrado}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty listadosMigrado}">

                                        <div class="table-responsive">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="registroMigrado.numeroRegistro"/></th>
                                                    <th><spring:message code="registroMigrado.ano"/></th>
                                                    <th><spring:message code="registroMigrado.oficina"/></th>
                                                    <th><spring:message code="registroMigrado.tipoRegistro"/></th>
                                                    <th><spring:message code="registroMigrado.fecha"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="lopdMigrado" items="${listadosMigrado}" varStatus="status">
                                                    <tr>
                                                        <td>${lopdMigrado.registroMigrado.numero}</td>
                                                        <td>${lopdMigrado.registroMigrado.ano}</td>
                                                        <td>${lopdMigrado.registroMigrado.denominacionOficina}</td>
                                                        <c:if test="${lopdMigrado.registroMigrado.tipoRegistro}">
                                                            <td><spring:message code="informe.entrada"/></td>
                                                        </c:if>
                                                        <c:if test="${!lopdMigrado.registroMigrado.tipoRegistro}">
                                                            <td><spring:message code="informe.salida"/></td>
                                                        </c:if>
                                                        <td><fmt:formatDate value="${lopdMigrado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
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

                </c:if>

            </div>
        </div>
        <!-- FIN BUSCADOR-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


<!-- Modifica los Usuarios según el libro elegido -->
<script type="text/javascript">

function actualizarUsuarios(){
    <c:url var="obtenerUsuarios" value="/informe/obtenerUsuarios" />
    actualizarUsuariosTodos('${obtenerUsuarios}','#usuario',$('#libro option:selected').val(),$('#usuario option:selected').val(),true);
}

</script>


<!-- VALIDADOR DE FORMULARI -->
<script type="text/javascript">

//Valida si ha elegido un usuario en el formulario
function validaUsuario(idUsuario, campInicio){
    if(idUsuario.value == -1){
        var variable = "#" + campInicio + " span.errors";
        var formatoHtml = "<span id='"+ campInicio +".errors' class='help-block'>S'ha de triar un <spring:message code="usuario.usuario"/></span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").addClass("has-error");
        return false;
    } else{
        var variable = "#" + campInicio + " span.errors";
        var htmlNormal = "<span id='"+ campInicio +".errors'></span>";
        $(variable).html(htmlNormal);
        $(variable).parents(".form-group").removeClass("has-error");
        return true;
    }
}

// Valida el formuario si las fechas Inicio y Fin son correctas
function validaFormulario(form) {
    var fechaInicio = true;
    var fechaFin = true;
    var fechas = true;
    var usuario = true;
    // Valida el formato de Fecha de Inicio
    if (!validaFecha(form.fechaInicio, 'fechaInicio')) {
        fechaInicio = false;
    }
    // Valida el formato de Fecha de Fin
    if (!validaFecha(form.fechaFin, 'fechaFin')) {
        fechaFin = false;
    }
    // Si las Fechas son correctas, Valida el Fecha Inicio y Fecha Fin menor o igual que fecha actual, Fecha Inicio menor o igual que Fecha Fin
    if((fechaInicio)&&(fechaFin)){
        if (!validaFechasConjuntas(form.fechaInicio, form.fechaFin, 'fechaInicio', 'fechaFin')) {
            fechas = false;
        }
    }
    // Valida que se ha legido un usuario
    if (!validaUsuario(form.usuario, 'user')) {
        usuario = false;
    }
    // Si todos los campos son correctos, hace el submit
    if((fechaInicio)&&(fechaFin)&&(fechas)&&(usuario)){
        return true;
    } else{
        return false;
    }
}

function actualizarUsuariosTodos(url, idSelect, seleccion, valorSelected, todos){
    var html = '';
    if(seleccion != '-1'){
        jQuery.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                if(todos){html = '<option value="-1">...</option>';}
                var len = result.length;
                var selected='';
                for ( var i = 0; i < len; i++) {
                    selected='';
                    if(valorSelected != null && result[i].id == valorSelected){
                        selected = 'selected="selected"';
                    }
                    html += '<option '+selected+' value="' + result[i].id + '">'
                            + result[i].nombreCompleto + '</option>';
                }
                html += '</option>';

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }


            }
        });

    }else{
        jQuery.ajax({
            url: url,
            type: 'GET',
            dataType: 'json',
            data: { id: seleccion },
            contentType: 'application/json',
            success: function(result) {
                html = '<option value="-1">...</option>';
                var len = result.length;
                for ( var i = 0; i < len; i++) {
                    html += '<option value="' + result[i].id + '">' + result[i].nombreCompleto + '</option>';
                }

                if(len != 0){
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",false).trigger("chosen:updated");
                }else if(len==0){
                    var html='';
                    $(idSelect).html(html);
                    $(idSelect).attr("disabled",true).trigger("chosen:updated");
                }
            }
        });
    }

}
</script>

</body>
</html>