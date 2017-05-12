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
            <li><a href="<c:url value="/registroMigrado/list"/>" ><i class="fa fa-list"></i> <spring:message code="registroMigrado.listado"/></a></li>
            <c:if test="${registroMigrado.tipoRegistro}">
                <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroMigrado.registroMigrado"/> ${registroMigrado.numero}-${registroMigrado.ano}-${registroMigrado.denominacionOficina}-<spring:message code="informe.entrada"/></li>
            </c:if>
            <c:if test="${!registroMigrado.tipoRegistro}">
                <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroMigrado.registroMigrado"/> ${registroMigrado.numero}-${registroMigrado.ano}-${registroMigrado.denominacionOficina}-<spring:message code="informe.salida"/></li>
            </c:if>
        </ol>
    </div>
</div><!-- /.row -->


    <div class="row">
        <div class="col-xs-12">

            <!-- REGISTRO MIGRADO -->
            <div class="panel panel-warning">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="fa fa-file-o"></i>
                        <strong>
                            <spring:message code="registroMigrado.consultaRegistroLopd"/> (${registroMigrado.numero}-${registroMigrado.ano}-${registroMigrado.denominacionOficina})
                        </strong>
                    </h3>
                </div>
                <div class="panel-body">

                    <!-- CREACION REGISTRO MIGRADO -->
                    <c:if test="${registroMigrado.tipoRegistro}">
                      <div class="alert-lopd alert-info alert-dismissable">
                    </c:if>
                    <c:if test="${!registroMigrado.tipoRegistro}">
                      <div class="alert-lopd alert-danger alert-dismissable">
                    </c:if>
                        <strong><spring:message code="registroMigrado.registroMigrado"/></strong>
                    </div>

                    <c:if test="${empty registroMigrado}">
                        <div class="alert alert-grey alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <spring:message code="regweb.busqueda.vacio"/>
                        </div>
                    </c:if>

                    <c:if test="${not empty registroMigrado}">

                        <div class="table-responsive">

                            <table class="table table-bordered table-hover table-striped tablesorter">
                                <colgroup>
                                    <col>
                                    <col>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th><spring:message code="registroMigrado.fecha"/></th>
                                    <th><spring:message code="usuario.usuario"/></th>
                                </tr>
                                </thead>

                                <tbody>
                                <tr>
                                    <td><fmt:formatDate value="${registroCreado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td>${registroCreado.usuario}</td>
                                </tr>
                                </tbody>
                            </table>

                        </div>

                    </c:if>

                    <!-- MODIFICACIONES SOBRE EL REGISTRO MIGRADO -->
                    <c:if test="${registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-info alert-dismissable">
                    </c:if>
                    <c:if test="${!registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-danger alert-dismissable">
                    </c:if>
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
                                    <col>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th><spring:message code="registroMigrado.fecha"/></th>
                                    <th><spring:message code="usuario.usuario"/></th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach var="registroLopdMigrado" items="${modificaciones}" varStatus="status">
                                    <tr>
                                        <td><fmt:formatDate value="${registroLopdMigrado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td>${registroLopdMigrado.usuario}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>

                    </c:if>

                    <!-- CONSULTAS DEL REGISTRO MIGRADO -->
                    <c:if test="${registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-info alert-dismissable">
                    </c:if>
                    <c:if test="${!registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-danger alert-dismissable">
                    </c:if>
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
                                    <col>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                    <th><spring:message code="usuario.usuario"/></th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach var="registroLopdMigrado" items="${consultas}" varStatus="status">
                                    <tr>
                                        <td><fmt:formatDate value="${registroLopdMigrado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td>${registroLopdMigrado.usuario}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>

                    </c:if>

                    <!-- LISTADOS SOBRE EL REGISTRO MIGRADO -->
                    <c:if test="${registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-info alert-dismissable">
                    </c:if>
                    <c:if test="${!registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-danger alert-dismissable">
                    </c:if>
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
                                    <col>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                    <th><spring:message code="usuario.usuario"/></th>
                                </tr>
                                </thead>

                                <tbody>
                                <c:forEach var="registroLopdMigrado" items="${listados}" varStatus="status">
                                    <tr>
                                        <td><fmt:formatDate value="${registroLopdMigrado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td>${registroLopdMigrado.usuario}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>

                        </div>

                    </c:if>

                    <!-- VISADOS SOBRE EL REGISTRO MIGRADO -->
                    <c:if test="${registroMigrado.tipoRegistro}">
                    <div class="alert-lopd alert-info alert-dismissable">
                        </c:if>
                        <c:if test="${!registroMigrado.tipoRegistro}">
                        <div class="alert-lopd alert-danger alert-dismissable">
                            </c:if>
                            <strong><spring:message code="regweb.visado"/></strong>
                        </div>

                        <c:if test="${empty visados}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.busqueda.vacio"/>
                            </div>
                        </c:if>

                        <c:if test="${not empty visados}">

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
                                        <th><spring:message code="registroEntrada.fecha"/></th>
                                        <th><spring:message code="visado.fecha.modificacion"/></th>
                                        <th><spring:message code="visado.acceso"/></th>
                                        <th><spring:message code="usuario.usuario"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="modificacionLopdMigrado" items="${visados}" varStatus="status">
                                        <tr>
                                            <td><fmt:formatDate value="${modificacionLopdMigrado.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td><fmt:formatDate value="${modificacionLopdMigrado.fechaModificacion}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td>${modificacionLopdMigrado.tipoAcceso}</td>
                                            <td>${modificacionLopdMigrado.usuario}</td>
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
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>