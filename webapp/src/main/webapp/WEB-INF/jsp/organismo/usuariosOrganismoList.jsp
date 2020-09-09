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

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li><a href="<c:url value="/organismo/list"/>" ><i class="fa fa-list-ul"></i> <spring:message code="organismo.organismos"/> </a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i><strong><spring:message code="usuario.usuarios"/> de ${organismo.denominacion}</strong></li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->


        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                <spring:message code="organismo.usuarios"/> de ${organismo.denominacion}
                            </strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <c:if test="${empty usuarios}">
                            <div class="alert alert-grey">
                                <strong><spring:message code="organismo.usuarios.noExisten"/></strong>
                            </div>
                        </c:if>

                        <c:if test="${not empty usuarios}">
                            <div class="form-group col-xs-12">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                        <tr>
                                            <th style="text-align:center;"><spring:message code="organismo.usuario"/></th>
                                            <c:forEach var="permiso" items="${permisos}">
                                                <th style="text-align:center;"><spring:message code="permiso.nombre.${permiso}"/></th>
                                            </c:forEach>
                                        </tr>
                                        </thead>
                                        <tbody>

                                        <c:forEach var="usuario" items="${usuarios}">
                                            <tr>
                                                <td>${usuario.nombreCompleto}</td>
                                                <c:forEach var="permisoOrganismo" items="${pou}">
                                                    <c:if test="${usuario.id == permisoOrganismo.usuario.id}">
                                                        <td style="text-align:center;">
                                                            <c:if test="${permisoOrganismo.activo == true}"><span class="label label-success">Si</span></c:if>
                                                            <c:if test="${permisoOrganismo.activo == false}"><span class="label label-danger">No</span></c:if>
                                                        </td>
                                                    </c:if>
                                                </c:forEach>
                                                <td><a class="btn btn-warning btn-sm" href="<c:url value="/permisos/${usuario.id}"/>" target="_blank" title="<spring:message code="usuario.modificar.permisos"/>"><span class="fa fa-key"></span></a></td>
                                            </tr>
                                        </c:forEach>

                                        </tbody>

                                    </table>
                                </div>

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