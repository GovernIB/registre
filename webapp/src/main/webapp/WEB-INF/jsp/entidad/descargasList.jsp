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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="entidad.descargas"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div class="row">
            <div class="col-xs-12">

                <c:import url="../modulos/mensajes.jsp"/>
                <div id="mensajes"></div>

                <div class="panel panel-success">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-list"></i> <strong><spring:message code="entidad.descargas.lista"/> ${entidad.nombre}</strong></h3>
                    </div>

                    <div class="panel-body">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty listado}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="descarga.descarga"/></strong>
                                        </div>

                                    </c:if>


                                   <c:if test="${not empty listado}">


                                       <div class="alert-grey">
                                           <c:if test="${paginacion.totalResults == 1}">
                                               <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="descarga.descarga"/>
                                           </c:if>
                                           <c:if test="${paginacion.totalResults > 1}">
                                               <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="descarga.descargas"/>
                                           </c:if>

                                           <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                       </div>

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>Id</th>
                                                        <th><spring:message code="regweb.fechasincronizacion"/></th>
                                                        <th><spring:message code="regweb.tipo"/></th>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <c:forEach var="descarga" items="${listado}">
                                                        <tr>
                                                            <td>${descarga.id}</td>
                                                            <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${descarga.fechaImportacion}" /></td>
                                                            <td><c:if test="${descarga.tipo=='unidad'}"><spring:message code="descarga.tipo.unidad"/></c:if><c:if test="${descarga.tipo=='oficina'}"><spring:message code="descarga.tipo.oficina"/></c:if></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacion.jsp">
                                                <c:param name="entidad" value="entidad/descargas"/>
                                            </c:import>


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