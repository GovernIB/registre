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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroEntrada.pendientesDistribuir.sir"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong> <spring:message code="registroEntrada.pendientesDistribuir.sir"/> </strong> </h3>
                    </div>

                    <form:form modelAttribute="pendientesDistribuirBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroEntrada.oficina.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.oficina.busqueda"/>" data-toggle="popover"><spring:message code="registro.oficinaRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="registroEntrada.oficina.id" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach var="oficina" items="${oficinasSir}">
                                                <form:option value="${oficina.id}">${oficina.denominacion}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>
                    </form:form>

                    <c:if test="${paginacion != null}">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registro.registro"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registro.registro"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registro.registros"/>
                                        </c:if>

                                        <%--Select de "Ir a página"--%>
                                        <c:import url="../modulos/paginas.jsp"/>
                                    </div>

                                    <div class="table-responsive">

                                        <table class="table table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="regweb.numero"/></th>
                                                <th><spring:message code="registroEntrada.fecha"/></th>
                                                <th><spring:message code="registroEntrada.oficina"/></th>
                                                <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                <th><spring:message code="registroEntrada.extracto"/></th>
                                                <th><spring:message code="registroEntrada.estado"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registro" items="${paginacion.listado}" varStatus="status">
                                                <tr>
                                                    <td>${registro.numeroRegistroFormateado}</td>
                                                    <td><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td><label class="no-bold" rel="popupAbajo" data-content="${registro.oficina.codigo}" data-toggle="popover">${registro.oficina.denominacion}</label></td>
                                                    <td>${(empty registro.destino)? registro.destinoExternoDenominacion : registro.destino.denominacion}</td>
                                                    <td>${registro.registroDetalle.extracto}</td>
                                                    <td class="center">
                                                        <c:import url="../registro/estadosRegistro.jsp">
                                                            <c:param name="estado" value="${registro.estado}"/>
                                                            <c:param name="decodificacionTipoAnotacion" value="${registro.registroDetalle.decodificacionTipoAnotacion}"/>
                                                        </c:import>
                                                    </td>

                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="pendientesDistribuir"/>
                                        </c:import>

                                    </div>

                                </c:if>

                            </div>
                        </div>

                    </c:if>


                </div>
            </div>
        </div>
    </div>

    <!-- FIN BUSCADOR -->




</div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

</body>
</html>