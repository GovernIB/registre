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
                    <li class="active"><i class="fa fa-list-ul"></i> <spring:message code="persona.personas"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <div class="row">
            <div class="col-xs-12">
                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="persona.buscador.duplicadas"/></strong> </h3>
                    </div>

                    <div class="panel-body">

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty listado}">
                                        <div class="alert alert-warning alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="persona.persona"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty listado}">
                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="persona.persona"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="persona.personas"/>
                                            </c:if>

                                            <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                        </div>

                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col width="101">
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th><spring:message code="regweb.nombre"/></th>
                                                    <th><spring:message code="persona.documento"/></th>
                                                    <th><spring:message code="persona.tipoPersona"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="persona" items="${listado}">
                                                    <tr>
                                                        <td>
                                                            <c:if test="${persona.tipo == 2}"> ${persona.nombrePersonaFisica} </c:if>
                                                            <c:if test="${persona.tipo == 3}"> ${persona.nombrePersonaJuridica} </c:if>
                                                        </td>
                                                        <td>${persona.documento}</td>
                                                        <td>
                                                            <c:if test="${persona.tipo == 2}">
                                                                <span class="label label-success"><spring:message code="persona.tipo.${persona.tipo}"/></span>
                                                            </c:if>
                                                            <c:if test="${persona.tipo == 3}">
                                                                <span class="label label-warning"><spring:message code="persona.tipo.${persona.tipo}"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/persona/${persona.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                            <a class="btn btn-danger btn-sm" onclick='javascript:confirm("<c:url value="/persona/${persona.id}/delete"/>","<spring:message code="regweb.confirmar.eliminacion" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.eliminar"/>"><span class="fa fa-eraser"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>

                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="persona"/>
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


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


</body>
</html>