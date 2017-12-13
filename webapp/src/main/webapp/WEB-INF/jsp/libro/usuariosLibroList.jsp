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
                    <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
                    <li><a href="<c:url value="/organismo/list"/>" ><i class="fa fa-institution"></i> ${libro.organismo.entidad.nombre}</a></li>
                    <li><a href="<c:url value="/libro/${libro.organismo.id}/libros"/>">${libro.organismo.denominacion}</a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i><strong>Usuarios del Libro ${libro.nombre}</strong></li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->


        <div class="row">
            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                            <strong>
                                Usuarios del Libro: ${libro.nombre}
                            </strong>
                        </h3>
                    </div>

                    <div class="panel-body">

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
                                                <c:forEach var="permisoLibro" items="${plu}">
                                                    <c:if test="${usuario.id == permisoLibro.usuario.id}">
                                                        <td style="text-align:center;">
                                                            <c:if test="${permisoLibro.activo == true}"><span class="label label-success">Si</span></c:if>
                                                            <c:if test="${permisoLibro.activo == false}"><span class="label label-danger">No</span></c:if>
                                                        </td>
                                                    </c:if>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>

                                    </tbody>

                                </table>
                            </div>

                        </div>

                    </div>

                </div>
                    <!-- Botonera -->
                    <button type="button" onclick="goTo('<c:url value="/libro/${libro.organismo.id}/libros"/>')" class="btn btn-warning btn-sm" title="<spring:message code="regweb.volver"/>">
                        <spring:message code="regweb.volver"/>
                    </button>

                    <!-- Fin Botonera -->

            </div>
        </div>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>