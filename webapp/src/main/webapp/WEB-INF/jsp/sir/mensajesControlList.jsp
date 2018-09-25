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
                    <li class="active"><i class="fa fa-search"></i> <spring:message code="mensajeControl.buscador"/></li>
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
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="mensajeControl.buscador"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="mensajeControlBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="mensajeControl.identificadorIntercambio" rel="ayuda" data-content="<spring:message code="registro.ayuda.idEnviadoSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.identificadorIntercambio"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="mensajeControl.identificadorIntercambio" cssClass="form-control"/>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="mensajeControl.tipoMensaje" rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoEnviadoSir.busqueda"/>" data-toggle="popover"><spring:message code="mensajeControl.tipo"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="mensajeControl.tipoMensaje" cssClass="chosen-select">
                                            <form:option value="">...</form:option>
                                            <c:forEach items="${tiposMensajeControl}" var="tipo">
                                                <form:option value="${tipo}"><spring:message code="mensajeControl.tipo.${tipo}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaInicio" rel="ayuda" data-content="<spring:message code="registro.ayuda.fechaInicio.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechainicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaFin" rel="ayuda" data-content="<spring:message code="registro.ayuda.fechaFin.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechafin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaFin" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="mensajeControl.tipoComunicacion" rel="ayuda" data-content="<spring:message code="registro.ayuda.tipoComunicacion.busqueda"/>" data-toggle="popover"><spring:message code="mensajeControl.comunicacion"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="mensajeControl.tipoComunicacion" cssClass="chosen-select">
                                            <form:option value="">...</form:option>
                                            <c:forEach items="${tiposComunicacion}" var="tipo">
                                                <form:option value="${tipo}"><spring:message code="mensajeControl.tipoComunicacion.${tipo}" /></form:option>
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
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="mensajeControl.mensajeControl"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="mensajeControl.mensajeControl"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="mensajeControl.mensajesControl"/>
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
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                                <tr>
                                                    <th><spring:message code="registroSir.identificadorIntercambio"/></th>
                                                    <th><spring:message code="mensajeControl.fecha"/></th>
                                                    <th class="center"><spring:message code="mensajeControl.comunicacion"/></th>
                                                    <th class="center"><spring:message code="mensajeControl.origen"/></th>
                                                    <th class="center"><spring:message code="mensajeControl.destino"/></th>
                                                    <th class="center"><spring:message code="mensajeControl.tipo"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <c:forEach var="mensajeControl" items="${paginacion.listado}" varStatus="status">
                                                    <tr>
                                                        <td>${mensajeControl.identificadorIntercambio}</td>
                                                        <td><fmt:formatDate value="${mensajeControl.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                        <td class="center">
                                                            <c:if test="${mensajeControl.tipoComunicacion == RegwebConstantes.TIPO_COMUNICACION_ENVIADO}">
                                                                <span class="label label-warning"><spring:message code="mensajeControl.tipoComunicacion.1"/></span>
                                                            </c:if>

                                                            <c:if test="${mensajeControl.tipoComunicacion == RegwebConstantes.TIPO_COMUNICACION_RECIBIDO}">
                                                                <span class="label label-default"><spring:message code="mensajeControl.tipoComunicacion.2"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">${mensajeControl.codigoEntidadRegistralOrigen}</td>
                                                        <td class="center">${mensajeControl.codigoEntidadRegistralDestino}</td>
                                                        <td class="center">
                                                            <c:if test="${mensajeControl.tipoMensaje == RegwebConstantes.MENSAJE_CONTROL_ACK}">
                                                                <span class="label label-info"><spring:message code="mensajeControl.tipo.01"/></span>
                                                            </c:if>

                                                            <c:if test="${mensajeControl.tipoMensaje == RegwebConstantes.MENSAJE_CONTROL_ERROR}">
                                                                <p rel="info" data-content="<c:out value="${mensajeControl.codigoError} - ${mensajeControl.descripcionMensaje}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="mensajeControl.tipo.02"/></span></p>
                                                            </c:if>

                                                            <c:if test="${mensajeControl.tipoMensaje == RegwebConstantes.MENSAJE_CONTROL_CONFIRMACION}">
                                                                <p rel="info" data-content="<fmt:formatDate value="${mensajeControl.fechaEntradaDestino}" pattern="dd/MM/yyyy HH:mm:ss"/> - <c:out value="${mensajeControl.numeroRegistroEntradaDestino}" escapeXml="true"/>" data-toggle="popover"><span class="label label-success"><spring:message code="mensajeControl.tipo.03"/></span></p>
                                                            </c:if>
                                                        </td>
                                                        <td class="center">
                                                            <c:if test="${mensajeControl.tipoComunicacion == RegwebConstantes.TIPO_COMUNICACION_ENVIADO}">
                                                                <a class="btn btn-success btn-sm" href="javascript:void(0);" onclick='confirm("javascript:reenviarMensaje(${mensajeControl.id})","<spring:message code="regweb.confirmar.enviarMensaje" htmlEscape="true"/>")' title="<spring:message code="mensajeControl.reenviar"/>"><span class="fa fa-mail-forward"></span></a>
                                                            </c:if>
                                                            <c:if test="${mensajeControl.tipoComunicacion == RegwebConstantes.TIPO_COMUNICACION_RECIBIDO}">
                                                                <a class="btn btn-success btn-sm disabled" href="javascript:void(0);" title="<spring:message code="mensajeControl.reenviar"/>"><span class="fa fa-mail-forward"></span></a>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="mensajeControl"/>
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
<script type="text/javascript" src="<c:url value="/js/sir.js"/>"></script>
<script type="text/javascript">
    $("[rel='info']").popover({ trigger: 'hover',placement: 'bottom',container:"body", html:true});
    var urlReenviarMensaje = '<c:url value="/sir/mensajeControl/reenviar"/>';
    var tradsMensajesControl = [];
    tradsMensajesControl['mensajeControl.reenviado.ok'] = "<spring:message code='mensajeControl.reenviado.ok' javaScriptEscape='true' />";
    tradsMensajesControl['mensajeControl.reenviado.error'] = "<spring:message code='mensajeControl.reenviado.error' javaScriptEscape='true' />";
</script>


</body>
</html>