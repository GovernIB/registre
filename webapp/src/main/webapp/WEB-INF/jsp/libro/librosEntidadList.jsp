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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ol"></i> <spring:message code="libro.listado"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list-ol"></i> <strong><spring:message code="organismo.libros"/> de ${entidad.nombre}</strong></h3>
                    </div>
                    <div class="panel-body">

                        <c:import url="../modulos/mensajes.jsp"/>

                        <c:if test="${empty librosList}">
                            <div class="alert alert-grey alert-dismissable">
                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="libro.libro"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty librosList}">
                            <div class="alert-grey">
                                <c:if test="${fn:length(librosList) == 1}">
                                    <spring:message code="regweb.resultados"/> <strong>${fn:length(librosList)}</strong> <spring:message code="libro.libro"/>
                                </c:if>
                                <c:if test="${fn:length(librosList) > 1}">
                                    <spring:message code="regweb.resultado"/> <strong>${fn:length(librosList)}</strong> <spring:message code="libro.libros"/>
                                </c:if>
                            </div>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <thead>
                                    <tr>
                                        <th><spring:message code="regweb.nombre"/></th>
                                        <th><spring:message code="libro.codigo"/></th>
                                        <th><spring:message code="organismo.organismo"/></th>
                                        <th><spring:message code="libro.contador.entrada"/></th>
                                        <th><spring:message code="libro.contador.salida"/></th>
                                        <th><spring:message code="libro.contador.oficio"/></th>
                                        <th><spring:message code="libro.contador.sir"/></th>
                                        <th><spring:message code="regweb.activo"/></th>
                                        <th width="130" class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="libro" items="${librosList}">
                                        <tr>
                                            <td>${libro.nombre}</td>
                                            <td>${libro.codigo}</td>
                                            <td>${libro.organismo.nombreCompleto}</td>
                                            <td>${libro.contadorEntrada.numero}</td>
                                            <td>${libro.contadorSalida.numero}</td>
                                            <td>${libro.contadorOficioRemision.numero}</td>
                                            <td>${libro.contadorSir.numero}</td>
                                            <td>
                                                <c:if test="${libro.activo}"><span class="label label-success">Si</span></c:if>
                                                <c:if test="${not libro.activo}"><span class="label label-danger">No</span></c:if>
                                            </td>
                                            <td>
                                                <%--<a class="btn btn-warning btn-sm" href="<c:url value="/libro/${libro.id}/usuarios"/>" title="<spring:message code="organismo.usuarios"/>"><span class="fa fa-users"></span></a>--%>
                                                <a class="btn btn-warning btn-sm" onclick='javascript:confirm("<c:url value="/libro/${libro.id}/inicializar"/>","<spring:message code="regweb.confirmar.inicializacion" htmlEscape="true"/>")' title="<spring:message code="libro.inicializar"/>"><span class="fa fa-clock-o"></span></a>
                                                <a class="btn btn-warning btn-sm" href="<c:url value="/libro/${libro.organismo.id}/${libro.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                    <c:if test="${libro.activo}">
                                                        <a class="btn btn-success btn-sm" onclick='javascript:confirm("<c:url value="/permisos/migrarPermisos/${libro.id}"/>","Desea migrar los permisos")' title="Migrar permisos"><span class="fa fa-users"></span></a>
                                                    </c:if>
                                                <%--<a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/libro/${libro.id}/${libro.organismo.id}/delete"/>", "<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' title="Eliminar" href="javascript:void(0);"><span class="fa fa-eraser"></span></a>--%>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
                </div> <!-- ./panel success -->

                <%--Botonera--%>
                <button type="button" onclick="goTo('<c:url value="/organismo/list"/>')" class="btn btn-warning btn-sm" title="<spring:message code="regweb.volver"/>">
                    <spring:message code="regweb.volver"/>
                </button>
            </div>
        </div>
    </div>
</div>

<c:import url="../modulos/pie.jsp"/>


</body>
</html>