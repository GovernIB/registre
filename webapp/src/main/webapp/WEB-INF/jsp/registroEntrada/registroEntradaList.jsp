<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroEntrada.buscador"/></title>
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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroEntrada.buscador"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-info">

                    <div class="panel-heading">
                        <a class="btn btn-info btn-xs pull-right" href="<c:url value="/registroEntrada/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="registroEntrada.nuevo"/></a>
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="registroEntrada.buscador"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="registroEntradaBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4"><spring:message code="registroEntrada.libro"/></div>
                                <div class="col-xs-8">
                                    <form:select path="registroEntrada.libro.id" items="${librosConsulta}" itemLabel="nombreCompleto" itemValue="id" cssClass="chosen-select"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registroEntrada.estado"/></div>
                                <div class="col-xs-8">
                                <%--
                                    <form:select path="registroEntrada.estado" items="${estados}" itemValue="id" itemLabel="traduccion.nombre" cssClass="chosen-select"/>
                                     --%>
                                    <form:select path="registroEntrada.estado" cssClass="chosen-select">
                                        <form:option value="" label="..."/>
                                        <c:forEach var="estado" items="${estados}">
                                            <form:option value="${estado}"><spring:message code="registro.estado.${estado}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                    
                                    <form:errors path="registroEntrada.estado" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registroEntrada.numeroRegistro"/></div>
                                <div class="col-xs-8">
                                    <form:input path="registroEntrada.numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="registroEntrada.numeroRegistroFormateado" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4 "><spring:message code="registroEntrada.extracto"/></div>
                                <div class="col-xs-8">
                                    <form:input path="registroEntrada.registroDetalle.extracto" cssClass="form-control" maxlength="200"/> <form:errors path="registroEntrada.registroDetalle.extracto" cssClass="help-block" element="span"/>
                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></div>
                                <div class="col-xs-8" id="fechaInicio">
                                    <div class="input-group date no-pad-right">
                                        <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                    </div>
                                    <form:errors path="fechaInicio" cssClass="help-block" element="span"/>

                                </div>
                            </div>
                            <div class="form-group col-xs-6">
                                <div class="col-xs-4"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></div>
                                <div class="col-xs-8" id="fechaFin">
                                    <div class="input-group date no-pad-right">
                                        <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                        <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                    </div>
                                    <form:errors path="fechaFin" cssClass="help-block" element="span"/>

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
                                                <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroEntrada.registroEntrada"/></strong>
                                            </div>
                                        </c:if>

                                        <c:if test="${not empty paginacion.listado}">

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
                                                            <col width="80">
                                                            <col>
                                                            <col width="80">
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="125">
                                                        </colgroup>
                                                        <thead>
                                                            <tr>
                                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                                <th><spring:message code="registroEntrada.fecha"/></th>
                                                                <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                                <th><spring:message code="registroEntrada.usuario"/></th>
                                                                <th><spring:message code="registroEntrada.oficina"/></th>
                                                                <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                                                <c:if test="${registroEntradaBusqueda.registroEntrada.estado == 2}">
                                                                    <th><spring:message code="registroEntrada.reserva"/></th>
                                                                </c:if>
                                                                <c:if test="${registroEntradaBusqueda.registroEntrada.estado != 2}">
                                                                    <th><spring:message code="registroEntrada.extracto"/></th>
                                                                </c:if>
                                                                <th><spring:message code="registroEntrada.anexos"/></th>

                                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                                            </tr>
                                                        </thead>

                                                        <tbody>
                                                            <c:forEach var="registroEntrada" items="${paginacion.listado}" varStatus="status">
                                                                <tr>
                                                                    <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                    <td>${registroEntrada.libro.nombre}</td>
                                                                    <td>${registroEntrada.usuario.usuario.identificador}</td>
                                                                    <td>${registroEntrada.oficina.denominacion}</td>
                                                                    <td>${(empty registroEntrada.destino)? registroEntrada.destinoExternoDenominacion : registroEntrada.destino.denominacion}</td>
                                                                    <c:if test="${registroEntrada.estado == 2}">
                                                                        <td>${registroEntrada.registroDetalle.reserva}</td>
                                                                    </c:if>
                                                                    <c:if test="${registroEntrada.estado != 2}">
                                                                        <td>${registroEntrada.registroDetalle.extracto}</td>
                                                                    </c:if>
                                                                    <c:if test="${registroEntrada.registroDetalle.anexos != null}">
                                                                        <td>${fn:length(registroEntrada.registroDetalle.anexos)}</td>
                                                                    </c:if>
                                                                    <c:if test="${registroEntrada.registroDetalle.anexos == null}">
                                                                        <td>0</td>
                                                                    </c:if>

                                                                    <td class="center">
                                                                        <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                        <%--Acciones según el estado--%>
                                                                        <c:choose>
                                                                            <c:when test="${registroEntrada.estado == 1 && puedeEditar}">  <%--Válido--%>
                                                                                <a class="btn btn-warning btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                                                <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroEntrada/${registroEntrada.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                                            </c:when>
                                                                            <c:when test="${registroEntrada.estado == 2 && puedeEditar}">  <%--Pendiente--%>
                                                                                <a class="btn btn-warning btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                                                <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroEntrada/${registroEntrada.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                                            </c:when>
                                                                            <c:when test="${registroEntrada.estado == 3 && isAdministradorLibro}">  <%--Pendiente de Visar--%>
                                                                                <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroEntrada/${registroEntrada.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                                            </c:when>
                                                                            <c:when test="${registroEntrada.estado == 4 || registroEntrada.estado == 5}">  <%--Oficio externo e interno--%>

                                                                            </c:when>
                                                                            <c:when test="${registroEntrada.estado == 6}"> <%--Enviado--%>

                                                                            </c:when>
                                                                            <c:when test="${registroEntrada.estado == 7}"> <%--Tramitado--%>

                                                                            </c:when>
                                                                            <c:when test="${registroEntrada.estado == 8 && puedeEditar}">  <%--Anulado--%>
                                                                                <a class="btn btn-primary btn-sm" onclick='javascript:confirm("<c:url value="/registroEntrada/${registroEntrada.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.activar"/>"><span class="fa fa-thumbs-o-up"></span></a>
                                                                            </c:when>

                                                                        </c:choose>

                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>


                                                <!-- Paginacion -->
                                                <c:import url="../modulos/paginacionBusqueda.jsp">
                                                    <c:param name="entidad" value="registroEntrada"/>
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