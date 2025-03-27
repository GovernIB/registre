<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/> - <spring:message code="registroMigrado.buscador"/></title>
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
            <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroMigrado.registroMigrados"/></strong></li>
        </ol>
    </div>
</div><!-- /.row -->

<c:import url="../modulos/mensajes.jsp"/>

<!-- BUSCADOR -->

<div class="row">

<div class="col-xs-12">

    <div class="panel panel-warning">

        <div class="panel-heading">
            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroMigrado.buscador"/></strong> </h3>
        </div>

        <form:form modelAttribute="registroMigradoBusqueda" method="post" cssClass="form-horizontal">
            <form:hidden path="pageNumber"/>

            <div class="panel-body">
                <div class="col-xs-12">
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="registroMigrado.tipoRegistro"><spring:message code="registroMigrado.tipoRegistro"/></form:label></div>
                        <div class="col-xs-8">
                            <form:select path="registroMigrado.tipoRegistro" cssClass="chosen-select" multiple="false">
                                <form:option value="true"><spring:message code="informe.entrada"/></form:option>
                                <form:option value="false"><spring:message code="informe.salida"/></form:option>
                            </form:select>
                        </div>
                    </div>
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="registroMigrado.codigoOficina"><spring:message code="registroMigrado.oficina"/></form:label></div>
                        <div class="col-xs-8">
                            <form:select path="registroMigrado.codigoOficina" cssClass="chosen-select">
                                <form:option value="">...</form:option>
                                <c:forEach var="oficinaMigrado" items="${oficinasMigrado}" varStatus="status">
                                    <form:option value="${oficinaMigrado[0]}">${oficinaMigrado[1]}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="anoRegistro"><spring:message code="registroMigrado.ano"/></form:label></div>
                        <div class="col-xs-8">
                            <form:input path="anoRegistro" type="text" cssClass="form-control" maxlength="4"/><form:errors path="anoRegistro" cssClass="help-block" element="span"/>
                        </div>
                    </div>
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="numeroRegistro"><spring:message code="registroMigrado.numero"/></form:label></div>
                        <div class="col-xs-8">
                            <form:input path="numeroRegistro" type="text" cssClass="form-control" maxlength="5"/><form:errors path="numeroRegistro" cssClass="help-block" element="span"/>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="fechaInicio"><spring:message code="informe.fechaInicio"/></form:label></div>
                        <div class="col-xs-8" id="fechaInicio">
                            <div class="input-group date no-pad-right">
                                <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                            </div>
                            <form:errors path="fechaInicio" cssClass="help-block" element="span"/>
                        </div>
                    </div>
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="fechaFin"><spring:message code="informe.fechaFin"/></form:label></div>
                        <div class="col-xs-8" id="fechaFin">
                            <div class="input-group date no-pad-right">
                                <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                            </div>
                            <form:errors path="fechaFin" cssClass="help-block" element="span"/>

                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="registroMigrado.extracto"><spring:message code="registroMigrado.extracto"/></form:label></div>
                        <div class="col-xs-8">
                            <form:input path="registroMigrado.extracto" cssClass="form-control" maxlength="200"/> <form:errors path="registroMigrado.extracto" cssClass="help-block" element="span"/>
                        </div>
                    </div>
                    <div class="col-xs-6 espaiLinies">
                        <div class="col-xs-4 etiqueta_regweb"><form:label path="registroMigrado.descripcionRemitenteDestinatario"><spring:message code="registroMigrado.remitentDestinatari"/></form:label></div>
                        <div class="col-xs-8">
                            <form:input path="registroMigrado.descripcionRemitenteDestinatario" type="text" cssClass="form-control"/><form:errors path="registroMigrado.descripcionRemitenteDestinatario" cssClass="help-block" element="span"/>
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
                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroMigrado.registroMigrado"/></strong>
                        </div>
                    </c:if>

                    <c:if test="${not empty paginacion.listado}">

                        <div class="alert-grey">
                            <c:if test="${paginacion.totalResults == 1}">
                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroMigrado.registroMigrado"/>
                            </c:if>
                            <c:if test="${paginacion.totalResults > 1}">
                                <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroMigrado.registroMigrados"/>
                            </c:if>

                            <%--Select de "Ir a página"--%>
                            <%--<c:import url="../modulos/paginas.jsp"/>--%>
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
                                    <col width="100">
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th><spring:message code="registroMigrado.fecha"/></th>
                                        <th><spring:message code="registroMigrado.numero"/></th>
                                        <th><spring:message code="registroMigrado.oficina"/></th>
                                        <%--<th><spring:message code="registroMigrado.fechaDocumento"/></th>--%>
                                        <%--<th><spring:message code="registroMigrado.tipoDocumento"/></th>--%>
                                        <th><spring:message code="registroMigrado.tipoRegistro"/></th>
                                        <c:if test="${registroMigradoBusqueda.registroMigrado.tipoRegistro}">
                                            <th><spring:message code="registroMigrado.remitente"/></th>
                                        </c:if>
                                        <c:if test="${!registroMigradoBusqueda.registroMigrado.tipoRegistro}">
                                            <th><spring:message code="registroMigrado.destinatario"/></th>
                                        </c:if>
                                        <c:if test="${registroMigradoBusqueda.registroMigrado.tipoRegistro}">
                                            <th><spring:message code="registroMigrado.destinatario"/></th>
                                        </c:if>
                                        <c:if test="${!registroMigradoBusqueda.registroMigrado.tipoRegistro}">
                                            <th><spring:message code="registroMigrado.emisor"/></th>
                                        </c:if>
                                        <th><spring:message code="registroMigrado.extracto"/></th>
                                        <th class="center"><spring:message code="regweb.acciones"/></th>
                                    </tr>
                                </thead>

                                <tbody>
                                    <c:forEach var="registroMigrado" items="${paginacion.listado}" varStatus="status">
                                        <tr>
                                            <td><fmt:formatDate value="${registroMigrado.fechaRegistro}" pattern="dd/MM/yyyy"/></td>
                                            <td>${registroMigrado.numero}</td>
                                            <td>${registroMigrado.denominacionOficina}</td>
                                           <%-- <td>${registroMigrado.fechaDocumento}</td>--%>
                                            <%--<td>${registroMigrado.tipoDocumento}</td>--%>
                                            <td class="center">
                                                <c:if test="${registroMigrado.tipoRegistro}">
                                                    <span class="label label-info"><spring:message code="registroSir.entrada"/></span>
                                                </c:if>

                                                <c:if test="${!registroMigrado.tipoRegistro}">
                                                    <span class="label label-danger"><spring:message code="registroSir.salida"/></span>
                                                </c:if>
                                            </td>
                                            <td>${registroMigrado.descripcionRemitenteDestinatario}</td>
                                            <td>${registroMigrado.descripcionOrganismoDestinatarioEmisor}</td>
                                            <td>${registroMigrado.extracto}</td>

                                            <td class="center">
                                                <a class="btn btn-info btn-sm" target="_blank" href="<c:url value="/registroMigrado/${registroMigrado.id}/detalle"/>" title="<spring:message code="registroMigrado.detalle"/>"><span class="fa fa-eye"></span></a>
                                                <a class="btn btn-warning btn-sm" target="_blank" href="<c:url value="/registroMigrado/${registroMigrado.id}/lopd"/>" title="<spring:message code="registroMigrado.lopd"/>"><span class="fa fa-lock"></span></a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>


                            <!-- Paginacion -->
                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                <c:param name="entidad" value="registroMigrado"/>
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