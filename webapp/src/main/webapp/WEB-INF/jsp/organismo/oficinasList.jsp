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
                    <li><a href="<c:url value="/inici"/>" ><i class="fa fa-power-off"></i> <spring:message code="regweb.inicio"/></a></li>
                    <li><a href="<c:url value="/entidad/${entidadActiva.id}/edit"/>" ><i class="fa fa-institution"></i> ${entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> ${organismo.denominacion}</li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <c:import url="../modulos/mensajes.jsp"/>
                <div id="mensajes"></div>

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="organismo.oficinas"/> a: ${organismo.denominacion}</strong></h3>
                    </div>

                    <div class="panel-body">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${oficinas != null && empty oficinas}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="oficina.oficina"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty oficinas}">

                                    <div class="alert-grey">
                                        <c:if test="${fn:length(oficinas) == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${fn:length(oficinas)}</strong> <spring:message code="oficina.oficina"/>
                                        </c:if>
                                        <c:if test="${fn:length(oficinas) > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${fn:length(oficinas)}</strong> <spring:message code="oficina.oficinas"/>
                                        </c:if>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-bordered table-hover table-striped">
                                            <thead>
                                            <tr>
                                                <th><spring:message code="oficina.denominacion"/></th>
                                                <th><spring:message code="oficina.codigo"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="oficina" items="${oficinas}">
                                                <tr>
                                                    <td>${oficina.denominacion}</td>
                                                    <td>${oficina.codigo}</td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>


                                    </div>
                                </c:if>

                            </div>
                        </div>


                    </div>

                </div> <!--/.panel success-->

            </div>
        </div> <!-- /.row-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

</body>
</html>