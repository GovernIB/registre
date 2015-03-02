<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="registroSalida.buscador"/></title>
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
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSalida.buscador"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

            <div class="panel panel-danger">

            <div class="panel-heading">
                <a class="btn btn-danger btn-xs pull-right" href="<c:url value="/registroSalida/new"/>" role="button"><span class="fa fa-plus"></span> <spring:message code="registroSalida.nuevo"/></a>
                <h3 class="panel-title"><i class="fa fa-search"></i><strong><spring:message code="registroSalida.buscador"/></strong> </h3>
            </div>

            <form:form modelAttribute="registroSalidaBusqueda" method="post" cssClass="form-horizontal">
            <form:hidden path="pageNumber"/>

            <div class="panel-body">
                <div class="form-group col-xs-6">
                    <div class="col-xs-4"><spring:message code="registroSalida.libro"/></div>
                    <div class="col-xs-8">
                        <form:select path="registroSalida.libro.id" items="${librosConsulta}" itemLabel="nombreCompleto" itemValue="id" cssClass="chosen-select"/>
                    </div>
                </div>
                <div class="form-group col-xs-6">
                    <div class="col-xs-4 "><spring:message code="registroSalida.estado"/></div>
                    <div class="col-xs-8">
                        <form:select path="registroSalida.estado" cssClass="chosen-select">
                            <form:option value="" label="..."/>
                            <c:forEach var="estado" items="${estados}">
                                <form:option value="${estado}"><spring:message code="registro.estado.${estado}"/></form:option>
                            </c:forEach>
                        </form:select>
                        
                         <form:errors path="registroSalida.estado" cssClass="help-block" element="span"/>
                    </div>
                </div>

                <div class="form-group col-xs-6">
                    <div class="col-xs-4 "><spring:message code="registroSalida.numeroRegistro"/></div>
                    <div class="col-xs-8">
                        <form:input path="registroSalida.numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="registroSalida.numeroRegistroFormateado" cssClass="help-block" element="span"/>
                    </div>
                </div>
                <div class="form-group col-xs-6">
                    <div class="col-xs-4 "><spring:message code="registroSalida.extracto"/></div>
                    <div class="col-xs-8">
                        <form:input path="registroSalida.registroDetalle.extracto" cssClass="form-control" maxlength="200"/> <form:errors path="registroSalida.registroDetalle.extracto" cssClass="help-block" element="span"/>
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
                                    <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroSalida.registroSalida"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty paginacion.listado}">

                                <div class="alert-grey">
                                    <c:if test="${paginacion.totalResults == 1}">
                                        <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSalida.registroSalida"/>
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
                                            <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                            <th><spring:message code="registroSalida.fecha"/></th>
                                            <th><spring:message code="registroSalida.libro.corto"/></th>
                                            <th><spring:message code="registroSalida.usuario"/></th>
                                            <th><spring:message code="registroSalida.oficina"/></th>
                                            <th><spring:message code="registroSalida.origen"/></th>
                                            <c:if test="${registroSalidaBusqueda.registroSalida.estado == 2}">
                                                <th><spring:message code="registroEntrada.reserva"/></th>
                                            </c:if>
                                            <c:if test="${registroSalidaBusqueda.registroSalida.estado != 2}">
                                                <th><spring:message code="registroSalida.extracto"/></th>
                                            </c:if>
                                            <th><spring:message code="registroEntrada.anexos"/></th>

                                            <th class="center"><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="registroSalida" items="${paginacion.listado}" varStatus="status">
                                            <tr>
                                                <td>${registroSalida.numeroRegistroFormateado}</td>
                                                <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy"/></td>
                                                <td>${registroSalida.libro.nombre}</td>
                                                <td>${registroSalida.usuario.usuario.identificador}</td>
                                                <td>${registroSalida.oficina.denominacion}</td>
                                                <c:if test="${registroSalida.origen != null}">
                                                    <td>${registroSalida.origen.denominacion}</td>
                                                </c:if>
                                                <c:if test="${registroSalida.origen == null}">
                                                    <td>${registroSalida.origenExternoDenominacion}</td>
                                                </c:if>
                                                <c:if test="${registroSalida.estado == 2}">
                                                    <td>${registroSalida.registroDetalle.reserva}</td>
                                                </c:if>
                                                <c:if test="${registroSalida.estado != 2}">
                                                    <td>${registroSalida.registroDetalle.extracto}</td>
                                                </c:if>
                                                <c:if test="${registroSalida.registroDetalle.anexos != null}">
                                                    <td>${fn:length(registroEntrada.registroDetalle.anexos)}</td>
                                                </c:if>
                                                <c:if test="${registroSalida.registroDetalle.anexos == null}">
                                                    <td>0</td>
                                                </c:if>

                                                <td class="center">
                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        <%--Acciones según el estado--%>
                                                    <c:choose>
                                                        <c:when test="${registroSalida.estado == 1 && puedeEditar}">  <%--Válido--%>
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                            <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                        </c:when>
                                                        <c:when test="${registroSalida.estado == 2 && puedeEditar}">  <%--Pendiente--%>
                                                            <a class="btn btn-warning btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/edit"/>" title="<spring:message code="regweb.editar"/>"><span class="fa fa-pencil"></span></a>
                                                            <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                        </c:when>
                                                        <c:when test="${registroSalida.estado == 3 && isAdministradorLibro}">  <%--Pendiente de Visar--%>
                                                            <a class="btn btn-danger btn-sm" href="javascript:void(0);" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' title="<spring:message code="regweb.anular"/>"><span class="fa fa-thumbs-o-down"></span></a>
                                                        </c:when>
                                                        <c:when test="${registroSalida.estado == 4 || registroSalida.estado == 5}">  <%--Oficio externo e interno--%>

                                                        </c:when>
                                                        <c:when test="${registroSalida.estado == 6}"> <%--Enviado--%>

                                                        </c:when>
                                                        <c:when test="${registroSalida.estado == 7}"> <%--Tramitado--%>

                                                        </c:when>
                                                        <c:when test="${registroSalida.estado == 8 && puedeEditar}">  <%--Anulado--%>
                                                            <a class="btn btn-primary btn-sm" onclick='javascript:confirm("<c:url value="/registroSalida/${registroSalida.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' href="javascript:void(0);" title="<spring:message code="regweb.activar"/>"><span class="fa fa-thumbs-o-up"></span></a>
                                                        </c:when>

                                                    </c:choose>

                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>


                                    <!-- Paginacion -->
                                    <c:import url="../modulos/paginacionBusqueda.jsp">
                                        <c:param name="entidad" value="registroSalida"/>
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