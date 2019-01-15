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
                    <li class="active"><i class="fa fa-search"></i> <spring:message code="registroEntrada.distribuidos"/></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div id="mensajes"></div>

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message
                                code="registroEntrada.distribuidos.buscador"/></strong></h3>
                    </div>

                    <div class="panel-body">

                        <form:form modelAttribute="registroEntradaBusqueda"  method="post" cssClass="form-horizontal">

                            <form:hidden path="pageNumber"/>
                            <form:hidden path="registroEntrada.estado"/>

                            <div class="panel-body">

                                <div class="col-xs-12">

                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="registroEntrada.libro.id" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.libro.busqueda"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="registroEntrada.libro"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="registroEntrada.libro.id" cssClass="chosen-select">
                                                <form:option value="0">...</form:option>
                                                <form:options items="${librosConsulta}" itemLabel="nombreCompleto" itemValue="id"/>
                                            </form:select>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="registroEntrada.numeroRegistroFormateado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numero.busqueda"/>" data-toggle="popover"><spring:message code="registroEntrada.numeroRegistro"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="registroEntrada.numeroRegistroFormateado" cssClass="form-control"/> <form:errors path="registroEntrada.numeroRegistroFormateado" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>

                                </div>

                                <div class="col-xs-12">

                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicio.busqueda"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaInicio"/></label>
                                        </div>
                                        <div class="col-xs-8" id="fechaInicio">
                                            <div class="input-group date no-pad-right">
                                                <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                            </div>
                                            <form:errors path="fechaInicio" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.fin.busqueda"/>" data-toggle="popover"><span class="text-danger">*</span> <spring:message code="informe.fechaFin"/></label>
                                        </div>
                                        <div class="col-xs-8" id="fechaFin">
                                            <div class="input-group date no-pad-right">
                                                <form:input type="text" cssClass="form-control" path="fechaFin" maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                            </div>
                                            <form:errors path="fechaFin" cssClass="help-block" element="span"/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">

                                <div class="form-group col-xs-12">
                                    <div class="col-xs-1 boto-panel center">
                                        <button type="submit" class="btn btn-warning btn-sm" style="margin-left: 15px;">
                                            <spring:message code="regweb.buscar"/>
                                        </button>
                                    </div>
                                </div>

                            </div>

                        </form:form>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty paginacion.listado}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroEntrada.distribuido"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">

                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.distribuido"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message code="registroEntrada.distribuidos"/>
                                            </c:if>

                                            <p class="pull-right"><spring:message code="regweb.pagina"/>
                                                <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}
                                            </p>
                                        </div>

                                        <div class="table-responsive overVisible">

                                            <table class="table table-bordered table-hover table-striped tablesorter">
                                                <colgroup>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col>
                                                    <col width="51">
                                                </colgroup>
                                                <thead>
                                                <tr>
                                                    <th class="center"><spring:message code="regweb.numero"/></th>
                                                    <th class="center"><spring:message code="registroEntrada.fecha"/></th>
                                                    <th class="center"><spring:message code="registroEntrada.oficina"/></th>
                                                    <th class="center"><spring:message code="organismo.destino"/></th>
                                                    <th class="center"><spring:message code="registroEntrada.estado"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="registro" items="${paginacion.listado}" varStatus="status">
                                                    <tr>
                                                        <td class="center">${registro.numeroRegistroFormateado}</td>
                                                        <td class="center"><fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy"/></td>
                                                        <td class="center"><label class="no-bold" rel="popupAbajo" data-content="${registro.oficina.codigo}" data-toggle="popover">${registro.oficina.denominacion}</label></td>
                                                        <td class="center">${(empty registro.destino)? registro.destinoExternoDenominacion : registro.destino.denominacion}</td>
                                                        <td class="center">
                                                            <c:import url="../registro/estadosRegistro.jsp">
                                                                <c:param name="estado" value="${registro.estado}"/>
                                                                <c:param name="decodificacionTipoAnotacion" value="${registro.registroDetalle.decodificacionTipoAnotacion}"/>
                                                            </c:import>
                                                        </td>
                                                        <td class="center">
                                                            <a class="btn btn-primary btn-sm" href="javascript:void(0);" onclick="redistribuir('<c:url value="/distribucion/${registro.id}/redistribuir"/>')" title="<spring:message code="registroEntrada.redistribuir"/>"><span class="fa fa-refresh"></span></a>
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
    </div>

    <%-- MODAL Distribuir--%>
    <c:import url="../registro/registroTramitar.jsp">
        <c:param name="tipoRegistro" value="registroEntrada"/>
    </c:import>

</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript" src="<c:url value="/js/distribuir.js"/>"></script>

<script type="text/javascript">
    var tradsSir = [];
    tradsSir['registroSir.reiniciar.ok'] = "<spring:message code='registroSir.reiniciar.ok' javaScriptEscape='true' />";
    tradsSir['registroSir.reiniciar.error'] = "<spring:message code='registroSir.reiniciar.error' javaScriptEscape='true' />";

    <%-- Traducciones para distribuir.js --%>
    var traddistribuir = [];
    traddistribuir['campo.obligatorio'] = "<spring:message code='registro.distribuir.propuesto.obligatorio' javaScriptEscape='true' />";
    traddistribuir['distribuir.nodestinatarios'] = "<spring:message code='registro.distribuir.nodestinatarios' javaScriptEscape='true' />";
    traddistribuir['distribuir.noenviado'] = "<spring:message code='registroEntrada.distribuir.error.noEnviado' javaScriptEscape='true' />";
    traddistribuir['distribuir.error.plugin'] = "<spring:message code='registroEntrada.distribuir.error.plugin' javaScriptEscape='true' />";
    traddistribuir['distribuir.distribuyendo'] ="<spring:message code="registroEntrada.distribuyendo" javaScriptEscape="true"/>";

</script>


</body>
</html>