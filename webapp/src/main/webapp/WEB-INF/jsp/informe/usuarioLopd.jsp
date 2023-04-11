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

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->
        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="informe.usuarioLopd"/></strong> </h3>
                    </div>
                    <div class="panel-body">
                        <form:form modelAttribute="usuarioLopdBusqueda" method="post" cssClass="form-horizontal" name="usuarioLopdBusqueda" onsubmit="return validaFormulario(this)">

                            <form:hidden path="pageNumber"/>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicio.usuarioLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaInicio" maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fin.usuarioLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="libro" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.libro.usuarioLopd"/>" data-toggle="popover"><spring:message code="libro.libro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="libro" items="${libros}" itemValue="id" itemLabel="libroOrganismo" cssClass="chosen-select"/>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="usuario" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.usuario.usuarioLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="usuario.usuario"/></label>
                                    </div>
                                    <div class="col-xs-8" id="user">
                                        <div>
                                            <form:select path="usuario" cssClass="chosen-select" items="${usuarios}" itemValue="id" itemLabel="nombreCompleto"/>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="tipo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoLibro.usuarioLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.tipoLibro"/></label>
                                    </div>
                                    <div class="col-xs-8" id="tip">
                                        <form:select path="tipo" cssClass="chosen-select" multiple="false">
                                            <form:option value="1" default="default"><spring:message code="informe.entrada"/></form:option>
                                            <form:option value="2"><spring:message code="informe.salida"/></form:option>
                                            <form:option value="3"><spring:message code="registroMigrado.migrado"/></form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="form-group col-xs-6 espaiLinies senseMargeLat">
                                    <div class="col-xs-4 pull-left etiqueta_regweb control-label textEsq">
                                        <label for="accion" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.accion.usuarioLopd"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="regweb.accion"/></label>
                                    </div>
                                    <div class="col-xs-8" id="acci">
                                        <div>
                                            <form:select path="accion" cssClass="chosen-select" multiple="false">
                                                <form:option value="3" default="default"><spring:message code="informe.lopd.creacion"/></form:option>
                                                <form:option value="4"><spring:message code="informe.lopd.modificacion"/></form:option>
                                                <form:option value="1"><spring:message code="informe.lopd.listado"/></form:option>
                                                <form:option value="2"><spring:message code="informe.lopd.consultado"/></form:option>
                                                <form:option value="5"><spring:message code="informe.lopd.justificante"/></form:option>
                                            </form:select>
                                        </div>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning  btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>

                        </form:form>

                    </div>
                </div>

                <c:if test="${usuarioLopdBusqueda.usuario != null}">

                    <div class="row">
                        <div class="col-xs-12">

                            <c:if test="${tipo == RegwebConstantes.REGISTRO_ENTRADA}">
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
                                        <c:if test="${accion == RegwebConstantes.LOPD_CREACION}">
                                            <!-- REGISTROS DE ENTRADA CREADOS -->
                                            <div class="alert-lopd alert-info alert-dismissable">
                                                <strong><spring:message code="regweb.creados"/></strong>
                                            </div>

                                            <div class="row">

                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="col-xs-12">
                                                        <div class="alert alert-grey alert-dismissable">
                                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroEntrada.registroEntrada"/></strong>
                                                        </div>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                                    <th><spring:message code="registroEntrada.oficina"/></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                <c:forEach var="registroEntrada" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${registroEntrada.numeroRegistro}</td>
                                                                        <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="yyyy"/></td>
                                                                        <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                        <td>${registroEntrada.oficina.denominacion}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_MODIFICACION}">
                                            <!-- REGISTROS DE ENTRADA MODIFICADOS -->
                                            <div class="alert-lopd alert-info alert-dismissable">
                                                <strong><spring:message code="regweb.modificados"/></strong>
                                            </div>

                                            <div class="row">

                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                                    <th><spring:message code="regweb.modificacion"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="historicoRegistroEntrada" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${historicoRegistroEntrada.registroEntrada.numeroRegistro}</td>
                                                                        <td><fmt:formatDate value="${historicoRegistroEntrada.registroEntrada.fecha}" pattern="yyyy"/></td>
                                                                        <td><fmt:formatDate value="${historicoRegistroEntrada.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                        <td>${historicoRegistroEntrada.modificacion}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_CONSULTA}">
                                            <!-- REGISTROS DE ENTRADA CONSULTADOS -->
                                            <div class="alert-lopd alert-info alert-dismissable">
                                                <strong><spring:message code="regweb.consultados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert-lopd alert-warning alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="lopd" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${lopd.numeroRegistro}</td>
                                                                        <td>${lopd.anyoRegistro}</td>
                                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_LISTADO}">
                                            <!-- REGISTROS DE ENTRADA LISTADOS -->
                                            <div class="alert-lopd alert-info alert-dismissable">
                                                <strong><spring:message code="regweb.listados"/></strong>
                                            </div>

                                            <div class="row">

                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="lopd" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${lopd.numeroRegistro}</td>
                                                                        <td>${lopd.anyoRegistro}</td>
                                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_JUSTIFICANTE}">
                                            <!-- REGISTROS DE ENTRADA CONSULTA JUSTIFICANTE -->
                                            <div class="alert-lopd alert-info alert-dismissable">
                                                <strong><spring:message code="informe.lopd.justificante"/></strong>
                                            </div>

                                            <div class="row">

                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="lopd" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${lopd.numeroRegistro}</td>
                                                                        <td>${lopd.anyoRegistro}</td>
                                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${tipo == RegwebConstantes.REGISTRO_SALIDA}">
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
                                        <c:if test="${accion == RegwebConstantes.LOPD_CREACION}">
                                            <!-- REGISTROS DE SALIDA CREADOS -->
                                            <div class="alert-lopd alert-danger alert-dismissable">
                                                <strong><spring:message code="regweb.creados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalidas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                                    <th><spring:message code="registroSalida.oficina"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="registroSalida" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${registroSalida.numeroRegistro}</td>
                                                                        <td><fmt:formatDate value="${registroSalida.fecha}" pattern="yyyy"/></td>
                                                                        <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                        <td>${registroSalida.oficina.denominacion}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_MODIFICACION}">
                                            <!-- REGISTROS DE SALIDA MODIFICADOS -->
                                            <div class="alert-lopd alert-danger alert-dismissable">
                                                <strong><spring:message code="regweb.modificados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalidas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                                    <th><spring:message code="regweb.modificacion"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="historicoRegistroSalida" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${historicoRegistroSalida.registroSalida.numeroRegistro}</td>
                                                                        <td><fmt:formatDate value="${historicoRegistroSalida.registroSalida.fecha}" pattern="yyyy"/></td>
                                                                        <td><fmt:formatDate value="${historicoRegistroSalida.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                        <td>${historicoRegistroSalida.modificacion}</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_CONSULTA}">
                                            <!-- REGISTROS DE SALIDA CONSULTADOS -->
                                            <div class="alert-lopd alert-danger alert-dismissable">
                                                <strong><spring:message code="regweb.consultados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalidas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="lopd" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${lopd.numeroRegistro}</td>
                                                                        <td>${lopd.anyoRegistro}</td>
                                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_LISTADO}">
                                            <!-- REGISTROS DE SALIDA LISTADOS -->
                                            <div class="alert-lopd alert-danger alert-dismissable">
                                                <strong><spring:message code="regweb.listados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalidas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroSalida.fecha"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="lopd" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${lopd.numeroRegistro}</td>
                                                                        <td>${lopd.anyoRegistro}</td>
                                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_JUSTIFICANTE}">
                                            <!-- REGISTROS DE ENTRADA CONSULTA JUSTIFICANTE -->
                                            <div class="alert-lopd alert-danger alert-dismissable">
                                                <strong><spring:message code="informe.lopd.justificante"/></strong>
                                            </div>

                                            <div class="row">

                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntrada"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.registroEntradas"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                    <th><spring:message code="registroEntrada.fecha"/></th>
                                                                </tr>
                                                                </thead>

                                                                <tbody>
                                                                <c:forEach var="lopd" items="${paginacion.listado}" varStatus="status">
                                                                    <tr>
                                                                        <td>${lopd.numeroRegistro}</td>
                                                                        <td>${lopd.anyoRegistro}</td>
                                                                        <td><fmt:formatDate value="${lopd.fecha}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                                    </tr>
                                                                </c:forEach>
                                                                </tbody>
                                                            </table>

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${tipo == RegwebConstantes.LOPD_TIPO_MIGRADO}">
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
                                        <c:if test="${accion == RegwebConstantes.LOPD_CONSULTA}">
                                            <!-- REGISTROS MIGRADOS CONSULTADOS -->
                                            <div class="alert-lopd alert-success alert-dismissable">
                                                <strong><spring:message code="regweb.consultados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroMigrado.registroMigrado"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroMigrado.registroMigrados"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>

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
                                                                <c:forEach var="lopdMigrado" items="${paginacion.listado}" varStatus="status">
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

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${accion == RegwebConstantes.LOPD_LISTADO}">
                                            <!-- REGISTROS MIGRADOS LISTADOS -->
                                            <div class="alert-lopd alert-success alert-dismissable">
                                                <strong><spring:message code="regweb.listados"/></strong>
                                            </div>

                                            <div class="row">
                                                <c:if test="${empty paginacion.listado}">
                                                    <div class="alert alert-grey alert-dismissable">
                                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                                        <spring:message code="regweb.busqueda.vacio"/>
                                                    </div>
                                                </c:if>

                                                <c:if test="${not empty paginacion.listado}">

                                                    <div class="col-xs-12">
                                                        <div class="alert-grey">
                                                            <c:if test="${paginacion.totalResults == 1}">
                                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroMigrado.registroMigrado"/>
                                                            </c:if>
                                                            <c:if test="${paginacion.totalResults > 1}">
                                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroMigrado.registroMigrados"/>
                                                            </c:if>

                                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                                        </div>


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
                                                                <c:forEach var="lopdMigrado" items="${paginacion.listado}" varStatus="status">
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

                                                            <!-- Paginacion -->
                                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                                <c:param name="entidad" value="usuarioLopd"/>
                                                            </c:import>

                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:if>

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
        return (fechaInicio) && (fechaFin) && (fechas) && (usuario);
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

<!-- Gestió de les opcions disponibles segons els camps que es trien al formulari-->
<script type="text/javascript">

    $(document).ready(function() {

        gestioTipusregistres();
        gestioAccions();

        // Gestión de Tipo Registro
        $('#libro').change(
            function() {gestioTipusregistres();});

        // Gestión de Acciones
        $('#tipo').change(
            function() {gestioAccions();});

    });
</script>
<!-- Tipus de Registre disponibles quan se tria el Llibre -->
<script type="text/javascript">
    function gestioTipusregistres() {
        var libro = $('#libro option:selected').val();
        if (libro == -1) { //Ningún libro seleccionado
            $('#tipo option[value="3"]').removeAttr("disabled");
        }else{
            $('#tipo option[value="3"]').attr("disabled", "disabled");
            $('#tipo option[value="3"]').removeAttr("selected");
            $('#tipo option[value="1"]').attr("selected");
            $('#accion option[value="3"]').removeAttr("disabled", "disabled");
            $('#accion option[value="4"]').removeAttr("disabled", "disabled");
        }
        $('#tipo').trigger("chosen:updated");
        $('#accion').trigger("chosen:updated");
    }
</script>
<!-- Accions disponibles quan se tria el Tipus de Registre Migrat-->
<script type="text/javascript">
    function gestioAccions() {
        var tipo = $('#tipo option:selected').val();
        if (tipo == 3) { //Registre Migrat
            $('#accion option[value="3"]').attr("disabled", "disabled");
            $('#accion option[value="4"]').attr("disabled", "disabled");
            $('#accion option[value="5"]').attr("disabled", "disabled");
            $('#accion option[value="2"]').removeAttr("selected");
            $('#accion option[value="3"]').removeAttr("selected");
            $('#accion option[value="4"]').removeAttr("selected");
            $('#accion option[value="5"]').removeAttr("selected");
            $('#accion option[value="1"]').attr("selected");
        }else{
            $('#accion option[value="3"]').removeAttr("disabled", "disabled");
            $('#accion option[value="4"]').removeAttr("disabled", "disabled");
            $('#accion option[value="5"]').removeAttr("disabled", "disabled");
        }
        $('#accion').trigger("chosen:updated");
    }
</script>

</body>
</html>