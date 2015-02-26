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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="preRegistro.preRegistros"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="preRegistro.buscador"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="preRegistroBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="preRegistro.anyRegistro"/></div>
                                <div class="col-xs-8">
                                    <form:select path="anyo" cssClass="chosen-select">
                                        <form:option value="" label="..."/>

                                        <c:set var="now" value="<%=new java.util.Date()%>" />
                                        <fmt:formatDate value="${now}" pattern="yyyy" var="anyActual" />

                                        <c:forEach begin="2014" end="${anyActual}" step="1" var="year" varStatus="status">
                                            <option value="${year}" <c:if test="${status.last}">selected="selected"</c:if>>${year}</option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="preRegistro.numero"/></div>
                                <div class="col-xs-8">
                                    <form:input path="preRegistro.numeroPreregistro" cssClass="form-control" maxlength="10"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="preRegistro.extracto"/></div>
                                <div class="col-xs-8">
                                    <form:input path="preRegistro.registroDetalle.extracto" cssClass="form-control" maxlength="10"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 pull-left align-right"><spring:message code="preRegistro.estado"/></div>
                                <div class="col-xs-8">
                                    <form:select path="estado" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="estado" items="${estados}">
                                            <form:option value="${estado}"><spring:message code="preRegistro.estado.${estado}"/></form:option>
                                        </c:forEach>
                                    </form:select>
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
                                    <div class="alert alert-warning alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="preRegistro.preRegistro"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="preRegistro.preRegistro"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="preRegistro.preRegistro"/>
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
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="preRegistro.numero"/></th>
                                                <th><spring:message code="preRegistro.fecha"/></th>
                                                <th><spring:message code="preRegistro.unidadOrigen"/></th>
                                                <th><spring:message code="preRegistro.oficinaDestino"/></th>
                                                <th><spring:message code="preRegistro.estado"/></th>
                                                <th><spring:message code="preRegistro.extracto"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="preRegistro" items="${paginacion.listado}" varStatus="status">
                                                <tr>
                                                    <td><fmt:formatDate value="${preRegistro.fecha}" pattern="yyyy"/> / ${preRegistro.numeroPreregistro}</td>
                                                    <td><fmt:formatDate value="${preRegistro.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <td>${preRegistro.decodificacionUnidadTramitacionOrigen}</td>
                                                    <td>${preRegistro.decodificacionEntidadRegistralDestino}</td>
                                                    <td>
                                                        <c:if test="${preRegistro.estado == 1}">
                                                            <span class="label label-warning"><spring:message code="preRegistro.estado.${preRegistro.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${preRegistro.estado == 2}">
                                                            <span class="label label-success"><spring:message code="preRegistro.estado.${preRegistro.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${preRegistro.estado == 3}">
                                                            <span class="label label-danger"><spring:message code="preRegistro.estado.${preRegistro.estado}" /></span>
                                                        </c:if>
                                                    </td>
                                                    <td>${preRegistro.registroDetalle.extracto}</td>

                                                    <td class="center">
                                                        <a class="btn btn-info btn-sm" href="<c:url value="/preRegistro/${preRegistro.id}/detalle"/>" title="<spring:message code="preRegistro.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="preRegistro"/>
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