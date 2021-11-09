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
                    <li class="active"><i class="fa fa-search"></i> <strong><spring:message code="registroSir.buscador.enviados"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <div id="mensajes"></div>

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-warning">

                    <c:if test="${(not empty organismos) && (not empty oficioRemisionBusqueda)}">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message
                                    code="registroSir.buscador.enviados"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <form:form modelAttribute="oficioRemisionBusqueda" method="post" cssClass="form-horizontal">
                                <form:hidden path="pageNumber"/>

                                <div class="col-xs-12">
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="oficioRemision.identificadorIntercambio" rel="popupAbajo"
                                                   data-content="<spring:message code="registro.ayuda.idEnviadoSir.busqueda"/>"
                                                   data-toggle="popover"><spring:message code="registroSir.identificadorIntercambio"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:input path="oficioRemision.identificadorIntercambio" cssClass="form-control"/>
                                        </div>
                                    </div>

                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="idOrganismo" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.organismo.busqueda"/>" data-toggle="popover"><spring:message code="organismo.organismo"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="idOrganismo" cssClass="chosen-select">
                                                <form:option value="">...</form:option>
                                                <c:forEach items="${organismos}" var="organismo">
                                                    <form:option value="${organismo.id}">${organismo.denominacion}</form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="estadoOficioRemision" rel="popupAbajo"
                                                   data-content="<spring:message code="registro.ayuda.estadoEnviadoSir.busqueda"/>"
                                                   data-toggle="popover"><spring:message code="oficioRemision.estado"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="estadoOficioRemision" cssClass="chosen-select">
                                                <form:option value="">...</form:option>
                                                <c:forEach items="${estadosOficioRemision}" var="estado">
                                                    <form:option value="${estado}"><spring:message code="oficioRemision.estado.${estado}"/></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="tipoOficioRemision" rel="popupAbajo"
                                                   data-content="<spring:message code="registro.ayuda.tipoEnviadoSir.busqueda"/>"
                                                   data-toggle="popover"><spring:message code="oficioRemision.tipo"/></label>
                                        </div>
                                        <div class="col-xs-8">
                                            <form:select path="tipoOficioRemision" cssClass="chosen-select">
                                                <form:option value="0">...</form:option>
                                                <c:forEach items="${tiposOficioRemision}" var="tipo">
                                                    <form:option value="${tipo}"><spring:message code="oficioRemision.tipo.${tipo}"/></form:option>
                                                </c:forEach>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-12">
                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="fechaInicio" rel="popupAbajo"
                                                   data-content="<spring:message code="registro.ayuda.inicioEnviadoSir.busqueda"/>"
                                                   data-toggle="popover"><spring:message code="regweb.fechainicio"/></label>
                                        </div>
                                        <div class="col-xs-8" id="fechaInicio">
                                            <div class="input-group date no-pad-right">
                                                <form:input path="fechaInicio" type="text" cssClass="form-control"
                                                            maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-xs-6 espaiLinies">
                                        <div class="col-xs-4 pull-left etiqueta_regweb">
                                            <label for="fechaFin" rel="popupAbajo"
                                                   data-content="<spring:message code="registro.ayuda.finEnviadoSir.busqueda"/>"
                                                   data-toggle="popover"><spring:message code="regweb.fechafin"/></label>
                                        </div>
                                        <div class="col-xs-8" id="fechaFin">
                                            <div class="input-group date no-pad-right">
                                                <form:input path="fechaFin" type="text" cssClass="form-control"
                                                            maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                                <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="form-group col-xs-12">
                                    <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                                </div>
                            </form:form>

                        </c:if>

                        <c:if test="${paginacion != null}">

                            <div class="row">
                                <div class="col-xs-12">

                                    <c:if test="${empty paginacion.listado}">
                                        <div class="alert alert-grey alert-dismissable">
                                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                            <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="oficioRemision.oficioRemision"/></strong>
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty paginacion.listado}">

                                        <div class="alert-grey">
                                            <c:if test="${paginacion.totalResults == 1}">
                                                <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.oficioRemision"/>
                                            </c:if>
                                            <c:if test="${paginacion.totalResults > 1}">
                                                <spring:message code="regweb.resultados"/>
                                                <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.oficiosRemision"/>
                                            </c:if>

                                            <%--Select de "Ir a página"--%>
                                            <c:import url="../modulos/paginas.jsp"/>
                                        </div>

                                        <div class="table-responsive overVisible">

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
                                                    <th><spring:message code="registroSir.identificadorIntercambio"/></th>
                                                    <th><spring:message code="oficioRemision.fecha"/></th>
                                                    <th class="center"><spring:message code="oficioRemision.tipo"/></th>
                                                    <th class="center"><spring:message code="oficioRemision.oficina"/></th>
                                                    <th class="center"><spring:message code="oficioRemision.organismoDestino"/></th>
                                                    <th class="center"><spring:message code="oficioRemision.estado"/></th>
                                                    <!--th><spring:message code="oficioRemision.reintentos"/></th-->
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach var="oficioRemision" items="${paginacion.listado}">
                                                    <tr>
                                                    	<td>
                                                        <c:choose>
                                                            <c:when test="${not empty oficioRemision.identificadorIntercambio}">${oficioRemision.identificadorIntercambio}</c:when>
                                                            <c:otherwise><span class="label label-danger"><spring:message code="regweb.registre.id.pendent"/></span></c:otherwise>
                                                        </c:choose>
                                                        </td>
                                                        <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                        <td class="center">
                                                            <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                <span class="label label-info"><spring:message code="registroSir.entrada"/></span>
                                                            </c:if>

                                                            <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                <span class="label label-danger"><spring:message code="registroSir.salida"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td><label class="no-bold" rel="popupAbajo"
                                                                   data-content="${oficioRemision.oficina.codigo}"
                                                                   data-toggle="popover">${oficioRemision.oficina.denominacion}</label>
                                                        </td>
                                                        <td><label class="no-bold" rel="popupAbajo"
                                                                   data-content="<spring:message code="oficina.oficina"/>: ${oficioRemision.decodificacionEntidadRegistralDestino} (${oficioRemision.codigoEntidadRegistralDestino})"
                                                                   data-toggle="popover">${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</label>
                                                        </td>
                                                        <td class="center">
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_INTERNO_ENVIADO}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_EXTERNO_ENVIADO}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                                    
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_SIN_DATOS}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_PENDIENTE_ENVIO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_PENDIENTE_CONFIRMACION}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></p></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_PENDIENTE_CONFIRMACION_MANUAL}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_CONFIRMADO}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_RECHAZADO}"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span>></p></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_EN_TRAMITE}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ASIGNADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_FINALIZADO}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECTIFICADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIO_PROCESO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ELIMINADO}"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
        
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                        </td>
                                                        <!-- td class="center">${oficioRemision.numeroReintentos}</td-->
                                                        <td class="center">
                                                            <div class="btn-group pull-right text12">
                                                                <button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
                                                                    <spring:message code="regweb.acciones"/> <span class="caret"></span>
                                                                </button>
                                                                <ul class="dropdown-menu dropdown">
                                                                	<c:choose>
			                                                            <c:when test="${not empty oficioRemision.identificadorIntercambio}"><li><a href="<c:url value="/sir/${oficioRemision.identificadorIntercambio}/detalle"/>" target="_blank"><spring:message code="idIntercambio.detalle"/></a></li></c:when>
			                                                            <c:otherwise><li class="disabled_events"><a href="<c:url value="/sir/${oficioRemision.identificadorIntercambio}/detalle"/>" target="_blank"><spring:message code="idIntercambio.detalle"/></a></li></c:otherwise>
			                                                        </c:choose>
                                                                    <!--li><a href="<c:url value="/sir/${oficioRemision.id}/ficheroIntercambio"/>"><spring:message code="registroSir.ficheroIntercambio"/></a></li -->
                                                                    <c:if test="${oficioRemision.estado != RegwebConstantes.OFICIO_ACEPTADO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_ENVIADO_CONFIRMADO &&
                                                                                  oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECIBIDO_CONFIRMADO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_ENVIADO_RECHAZADO &&
                                                                                  oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECIBIDO_RECHAZADO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_FINALIZADO &&
                                                                                  oficioRemision.estado != RegwebConstantes.OFICIO_SIR_REENVIADO_RECHAZADO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECIBIDO_RECHAZADO_CIUDADANO &&
                                                                                  oficioRemision.estado != RegwebConstantes.OFICIO_SIR_ELIMINADO}">
                                                                        <c:url value="/sir/oficio/actualizarEstado" var="urlActualizar"/>
                                                                    	<li><a href="javascript:void(0);" onclick="actualizarRegistroSir('${oficioRemision.id}','${urlActualizar}')"><spring:message code="registroSir.actualizar"/></a></li>
                                                                        <c:url value="/sir/oficio/reiniciar" var="urlReiniciar"/>
                                                                        <!--li><a href="javascript:void(0);" onclick="reiniciarContador('${oficioRemision.id}','${urlReiniciar}')"><spring:message code="registroSir.reiniciar"/></a></li-->
                                                                    </c:if>
                                                                    <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_SIN_DATOS || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_PENDIENTE_ENVIO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_PENDIENTE_CONFIRMACION
                                                                    || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_PENDIENTE_CONFIRMACION_MANUAL || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO
                                                                    || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_EN_TRAMITE || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ASIGNADO
                                                                    || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECTIFICADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIO_PROCESO}">
                                                                        <!--li class="divider"></li-->
                                                                        <!--li><a href="javascript:void(0);" onclick='confirm("javascript:reenviarIntercambio(${oficioRemision.id})","<spring:message code="regweb.confirmar.enviarIntercambio" htmlEscape="true"/>")'><spring:message code="intercambio.reenviar"/></a></li-->
                                                                    </c:if>

                                                                </ul>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                            <!-- Paginacion -->
                                            <c:import url="../modulos/paginacionBusqueda.jsp">
                                                <c:param name="entidad" value="oficioRemision"/>
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

<script type="text/javascript">
    var urlReenviarIntercambio = '<c:url value="/sir/reenviarIntercambio"/>';
    var tradsSir = [];
    tradsSir['registroSir.reiniciar.ok'] = "<spring:message code='registroSir.reiniciar.ok' javaScriptEscape='true' />";
    tradsSir['registroSir.reiniciar.error'] = "<spring:message code='registroSir.reiniciar.error' javaScriptEscape='true' />";
    tradsSir['intercambio.reenviado.ok'] = "<spring:message code='intercambio.reenviado.ok' javaScriptEscape='true' />";
    tradsSir['intercambio.reenviado.error'] = "<spring:message code='intercambio.reenviado.error' javaScriptEscape='true' />";
</script>

<script type="text/javascript" src="<c:url value="/js/sir.js"/>"></script>

</body>
</html>