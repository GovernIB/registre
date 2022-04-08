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
        <!-- Miga de pan-->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-institution"></i> ${loginInfo.entidadActiva.nombre}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="organismos.resultado.actualizacion"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

       <c:import url="../modulos/mensajes.jsp"/>

        <div class="row"> <!--row principal-->
            <div class="col-xs-12">
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i> <strong>spring:message code="organismos.resultado.actualizacion"/></strong></h3>
                    </div>
                    <div class="panel-body">

                        <!-- PANEL RESUMEN EXTINGUIDOS AUTOMATICOS -->
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="panel panel-success">
                                    <div class="panel-heading">
                                        <h3 class="panel-title"><i class="fa fa-file-o"></i> <strong><spring:message code="organismos.extinguidos.automatico"/></strong></h3>
                                    </div>
                                    <div  class="panel-body">
                                        <c:if test="${not empty extinguidosAutomaticos}">

                                            <c:forEach var="extinguidoAutomatico" items="${extinguidosAutomaticos}">

                                                <c:set var="organismoSustituye" value="${extinguidoAutomatico.value}"/>

                                                <table class="table table-bordered table-hover table-striped">
                                                    <colgroup>
                                                        <col>
                                                        <col>
                                                    </colgroup>
                                                    <thead>
                                                    <tr>
                                                        <th><spring:message code="organismo.extinguido"/></th>
                                                        <th>&nbsp;&nbsp;&nbsp;</th>
                                                        <th><spring:message code="organismo.asignado"/></th>
                                                    </tr>
                                                    </thead>

                                                    <tbody>
                                                    <tr>
                                                        <td>${extinguidoAutomatico.key}</td>
                                                        <td><span class="fa fa-arrow-right" aria-hidden="true"></span></td>
                                                        <td>${organismoSustituye.denominacion}</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </c:forEach>
                                        </c:if>

                                        <c:if test="${empty extinguidosAutomaticos}">
                                            No se ha requerido ninguna actualización de permisos automática
                                        </c:if>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<c:import url="../modulos/pie.jsp"/>

</body>
</html>