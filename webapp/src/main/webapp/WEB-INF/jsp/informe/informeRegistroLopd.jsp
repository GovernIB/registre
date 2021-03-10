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
                    <li><a href="<c:url value="/informe/registroLopd"/>" class="texto_naranja"><i class="fa fa-list-ul"></i> <spring:message code="informe.registroLopd"/></a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="informe.informe.registroLopd"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">

                <!-- PARÁMETROS DE BÚSQUEDA -->
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong>
                                <spring:message code="informe.registroLopd"/>
                            </strong>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <div class="col-xs-12"><strong><spring:message code="registroEntrada.numeroRegistro"/>: ${numRegistro}</strong></div>
                        <div class="col-xs-12"><strong><spring:message code="registroEntrada.anyRegistro"/>: ${anyoRegistro}</strong></div>
                        <div class="col-xs-12"><strong><spring:message code="libro.libro"/>: ${libro.nombre}</strong></div>
                        <c:if test="${idTipoRegistro == 1}">
                            <div class="col-xs-12"><strong><spring:message code="regweb.tipoRegistro"/>: <spring:message code="registroEntrada.registroEntrada"/></strong></div>
                        </c:if>
                        <c:if test="${idTipoRegistro == 2}">
                            <div class="col-xs-12"><strong><spring:message code="regweb.tipoRegistro"/>: <spring:message code="registroSalida.registroSalida"/></strong></div>
                        </c:if>
                    </div>
                </div>


                <c:if test="${idTipoRegistro == RegwebConstantes.REGISTRO_ENTRADA}">

                    <!-- REGISTROS DE ENTRADA -->
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                <strong>
                                    <spring:message code="registroEntrada.registroEntrada"/>
                                </strong>
                            </h3>
                        </div>
                        <div class="panel-body">

                            <!-- CREACION REGISTRO ENTRADA -->
                            <div class="alert-lopd alert-info alert-dismissable">
                                <strong><spring:message code="regweb.creado"/></strong>
                            </div>

                            <c:if test="${empty registro}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty registro}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                            <tr>
                                                <td><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${registro.usuario.usuario.identificador}</td>
                                            </tr>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- MODIFICACIONES SOBRE EL REGISTRO DE ENTRADA -->
                            <div class="alert-lopd alert-info alert-dismissable">
                                <strong><spring:message code="regweb.modificado"/></strong>
                            </div>

                            <c:if test="${empty modificaciones}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty modificaciones}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="historicoRegistroEntrada" items="${modificaciones}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${historicoRegistroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${historicoRegistroEntrada.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- CONSULTAS DEL REGISTRO DE ENTRADA -->
                            <div class="alert-lopd alert-info alert-dismissable">
                                <strong><spring:message code="regweb.consultado"/></strong>
                            </div>

                            <c:if test="${empty consultas}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty consultas}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="lopd" items="${consultas}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${lopd.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- LISTADOS SOBRE EL REGISTRO DE ENTRADA -->
                            <div class="alert-lopd alert-info alert-dismissable">
                                <strong><spring:message code="regweb.listado"/></strong>
                            </div>

                            <c:if test="${empty listados}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty listados}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="lopd" items="${listados}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${lopd.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- DESCARGAS DE JUSTIFICANTE SOBRE EL REGISTRO DE ENTRADA -->
                            <div class="alert-lopd alert-info alert-dismissable">
                                <strong><spring:message code="informe.lopd.justificante"/></strong>
                            </div>

                            <c:if test="${empty consultasJustificante}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty consultasJustificante}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="lopd" items="${consultasJustificante}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${lopd.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                        </div>
                    </div>

                </c:if>

                <c:if test="${idTipoRegistro == RegwebConstantes.REGISTRO_SALIDA}">

                    <!-- REGISTROS DE SALIDA -->
                    <div class="panel panel-danger">
                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-file-o"></i>
                                <strong>
                                    <spring:message code="registroSalida.registroSalida"/>
                                </strong>
                            </h3>
                        </div>
                        <div class="panel-body">

                            <!-- CREACION REGISTRO SALIDA -->
                            <div class="alert-lopd alert-danger alert-dismissable">
                                <strong><spring:message code="regweb.creado"/></strong>
                            </div>

                            <c:if test="${empty registro}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty registro}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroSalida.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                            <tr>
                                                <td><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${registro.usuario.usuario.identificador}</td>
                                            </tr>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- MODIFICACIONES SOBRE EL REGISTRO DE SALIDA -->
                            <div class="alert-lopd alert-danger alert-dismissable">
                                <strong><spring:message code="regweb.modificado"/></strong>
                            </div>

                            <c:if test="${empty modificaciones}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty modificaciones}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroSalida.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="historicoRegistroSalida" items="${modificaciones}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${historicoRegistroSalida.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${historicoRegistroSalida.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- CONSULTAS DEL REGISTRO DE SALIDA -->
                            <div class="alert-lopd alert-danger alert-dismissable">
                                <strong><spring:message code="regweb.consultado"/></strong>
                            </div>

                            <c:if test="${empty consultas}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty consultas}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroSalida.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="lopd" items="${consultas}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${lopd.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- LISTADOS SOBRE EL REGISTRO DE SALIDA -->
                            <div class="alert-lopd alert-danger alert-dismissable">
                                <strong><spring:message code="regweb.listado"/></strong>
                            </div>

                            <c:if test="${empty listados}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty listados}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroSalida.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="lopd" items="${listados}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${lopd.usuario.usuario.identificador}</td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>

                            </c:if>

                            <!-- DESCARGAS DE JUSTIFICANTE SOBRE EL REGISTRO DE ENTRADA -->
                            <div class="alert-lopd alert-danger alert-dismissable">
                                <strong><spring:message code="informe.lopd.justificante"/></strong>
                            </div>

                            <c:if test="${empty consultasJustificante}">
                                <div class="alert alert-grey alert-dismissable">
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                    <spring:message code="regweb.busqueda.vacio"/>
                                </div>
                            </c:if>

                            <c:if test="${not empty consultasJustificante}">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col>
                                            <col width="400">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <th><spring:message code="usuario.usuario"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="lopd" items="${consultasJustificante}" varStatus="status">
                                            <tr>
                                                <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>${lopd.usuario.usuario.identificador}</td>
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
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>