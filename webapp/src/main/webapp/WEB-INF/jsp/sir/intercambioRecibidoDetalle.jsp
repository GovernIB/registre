<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
    <head>
        <title><spring:message code="regweb.titulo"/> - <spring:message code="idIntercambio.detalle"/> ${registroSir.identificadorIntercambio}</title>
        <c:import url="../modulos/imports.jsp"/>
    </head>

<body>

    <c:import url="../modulos/menu.jsp"/>

    <div class="row-fluid container main">

        <div class="well well-white">

            <!-- Miga de pan -->
            <div class="row">
                <div class="col-xs-12">
                    <ol class="breadcrumb">
                        <c:import url="../modulos/migadepan.jsp"/>
                        <li class="active"><i class="fa fa-search"></i> <spring:message code="idIntercambio.detalle"/> ${registroSir.identificadorIntercambio}</li>
                    </ol>
                </div>
            </div><!-- Fin miga de pan -->

            <div id="mensajes"></div>

            <c:import url="../modulos/mensajes.jsp"/>

            <div class="row">

                <%-- PANEL LATERAL  --%>
                <div class="col-lg-4">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-file"></i>
                            <strong><spring:message code="registro.recibido.sir"/></strong>
                        </div>

                        <%--Formulario oculto integraciones--%>
                        <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                            <form:hidden path="texto"/>
                        </form:form>

                        <%--DETALLE REGISTRO RECIBIDO SIR--%>
                        <div class="panel-body">
                            <c:import url="../registroSir/detalleRegistroSir.jsp">
                                <c:param name="tipo" value="intercambio"/>
                            </c:import>
                        </div>
                        <%--BOTONERA--%>
                        <div class="panel-footer center">
                            <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/registroSir/${registroSir.id}/detalle"/>')" class="btn btn-primary btn-sm"><spring:message code="registroSir.detalle"/></button></div>
                        </div>
                        <div class="panel-footer center">
                            <div class="btn-group"><button type="button" onclick='confirm("javascript:enviarACK(${registroSir.id})","<spring:message code="regweb.confirmar.enviarMensaje" htmlEscape="true"/>")' class="btn btn-info btn-sm"><spring:message code="mensajeControl.enviar.ACK"/></button></div>
                            <%--Botón integraciones--%>
                            <div class="btn-group">
                                <button type="button" onclick="buscarIntegraciones('${registroSir.identificadorIntercambio}')" class="btn btn-warning btn-sm btn-block">
                                    <spring:message code="integracion.integraciones"/>
                                </button>
                            </div>
                            <c:if test="${registroSir.estado == 'ACEPTADO'}">
                                <div class="btn-group"><button type="button" onclick='confirm("javascript:enviarConfirmacion(${registroSir.id})","<spring:message code="regweb.confirmar.enviarMensaje" htmlEscape="true"/>")' class="btn btn-success btn-sm"><spring:message code="mensajeControl.enviar.confirmacion"/></button></div>
                            </c:if>
                        </div>

                   </div>
                </div>

                <%--ORIGEN--%>
                <div class="col-lg-8">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-institution"></i> <strong><spring:message code="regweb.origen"/></strong>
                        </div>
                        <div class="panel-body">
                            <p><i class="fa fa-institution"></i> <strong><spring:message code="organismo.organismo"/>:</strong>
                                <c:if test="${not empty registroSir.decodificacionUnidadTramitacionOrigen}">
                                    ${registroSir.decodificacionUnidadTramitacionOrigen} (${registroSir.codigoUnidadTramitacionOrigen})
                                </c:if>
                                <c:if test="${empty registroSir.decodificacionUnidadTramitacionOrigen}">
                                    ${registroSir.codigoUnidadTramitacionOrigen}
                                </c:if>
                            </p>

                            <p><i class="fa fa-home"></i> <strong><spring:message code="oficina.oficina"/>:</strong>
                                <c:if test="${not empty registroSir.decodificacionEntidadRegistralOrigen}">
                                    <a href="<c:url value="${loginInfo.dir3Caib.server}/oficina/${registroSir.codigoEntidadRegistralOrigen}/detall"/>" target="_blank">${registroSir.decodificacionEntidadRegistralOrigen} (${registroSir.codigoEntidadRegistralOrigen})</a>
                                </c:if>
                                <c:if test="${empty registroSir.decodificacionEntidadRegistralOrigen}">
                                    <a href="<c:url value="${loginInfo.dir3Caib.server}/oficina/${registroSir.codigoEntidadRegistralOrigen}/detall"/>" target="_blank">${registroSir.codigoEntidadRegistralOrigen} (${registroSir.codigoEntidadRegistralOrigen})</a>
                                </c:if>
                            </p>

                        </div>
                    </div>
                </div>

                <%-- ESTADOS --%>
                <div class="col-lg-4">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-clock-o"></i> <strong><spring:message code="idIntercambio.estados"/></strong>
                        </div>
                        <div class="panel-body">

                            <%--TRAZABILIDAD RECIBIDO SIR--%>
                            <c:forEach var="trazabilidadSir" items="${trazabilidadesSir}">

                                <a href="javascript:void(0);" class="list-group-item">
                                    <c:if test="${trazabilidadSir.tipo == RegwebConstantes.TRAZABILIDAD_SIR_RECEPCION}">
                                        <span class="label label-warning" rel="popupAbajo" data-content="<strong><spring:message code="oficina.origen"/>:</strong> <c:out value="${trazabilidadSir.decodificacionEntidadRegistralOrigen}"/> (<c:out value="${trazabilidadSir.codigoEntidadRegistralOrigen}"/>)" data-toggle="popover"><i class="fa fa-check fa-fw"></i> <spring:message code="trazabilidadSir.tipo.1"/></span>
                                    </c:if>

                                    <c:if test="${trazabilidadSir.tipo == RegwebConstantes.TRAZABILIDAD_SIR_REENVIO}">
                                        <span class="label label-warning" rel="popupAbajo" data-content="<strong><spring:message code="oficina.destino"/>:</strong> <c:out value="${trazabilidadSir.decodificacionEntidadRegistralDestino}"/> (<c:out value="${trazabilidadSir.codigoEntidadRegistralDestino}"/>)<br><strong><spring:message code="registroSir.motivo"/>:</strong> <c:out value="${trazabilidadSir.observaciones}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-check fa-fw"></i> <spring:message code="trazabilidadSir.tipo.2"/></span>
                                    </c:if>

                                    <c:if test="${trazabilidadSir.tipo == RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO}">
                                        <span class="label label-danger" rel="popupAbajo" data-content="<strong><spring:message code="registroSir.motivo"/>:</strong> <c:out value="${trazabilidadSir.observaciones}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-check fa-fw"></i> <spring:message code="trazabilidadSir.tipo.3"/></span>
                                    </c:if>

                                    <c:if test="${trazabilidadSir.tipo == RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO}">
                                        <span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="regweb.numero"/>:</strong> <c:out value="${trazabilidadSir.registroEntrada.numeroRegistroFormateado}"/>" data-toggle="popover"><i class="fa fa-check fa-fw"></i> <spring:message code="trazabilidadSir.tipo.4"/></span>
                                        <c:set var="idRegistroEntrada" value="${trazabilidadSir.registroEntrada.id}"/>
                                    </c:if>

                                    <c:if test="${trazabilidadSir.tipo == RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO_ORIGEN}">
                                        <span class="label label-danger" rel="popupAbajo" data-content="<strong><spring:message code="registroSir.motivo"/>:</strong> <c:out value="${trazabilidadSir.observaciones}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-check fa-fw"></i> <spring:message code="trazabilidadSir.tipo.5"/></span>
                                    </c:if>

                                    <c:if test="${trazabilidadSir.tipo == RegwebConstantes.TRAZABILIDAD_SIR_ELIMINAR}">
                                        <span class="label label-danger" rel="popupAbajo" data-content="<strong><spring:message code="registroSir.motivo"/>:</strong> <c:out value="${trazabilidadSir.observaciones}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-close fa-fw"></i> <spring:message code="trazabilidadSir.tipo.6"/></span>
                                    </c:if>

                                    <span class="pull-right text-muted small"><em><fmt:formatDate value="${trazabilidadSir.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></em></span>
                                </a>

                            </c:forEach>

                            <c:if test="${registroSir.estado == 'ACEPTADO'}">
                                <%--BOTONERA--%>
                                <div class="panel-footer center">
                                    <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/adminEntidad/registroEntrada/${idRegistroEntrada}/detalle"/>')" class="btn btn-info btn-sm"><spring:message code="registroEntrada.detalle"/></button></div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

                <%-- MENSAJES DE CONTROL --%>
                <div class="col-lg-4">

                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-envelope"></i> <strong><spring:message code="mensajeControl.mensajesControl"/></strong>
                        </div>

                        <div class="panel-body">

                            <c:if test="${empty mensajes}">
                                <div class="alert alert-grey">
                                    <spring:message code="mensajeControl.vacio"/>
                                </div>

                            </c:if>

                            <c:if test="${not empty mensajes}">
                                <div class="list-group">
                                    <c:forEach var="mensajeControl" items="${mensajes}">
                                        <a href="javascript:void(0);" class="list-group-item">
                                            <%--Tipo Mensaje--%>
                                            <c:if test="${mensajeControl.tipoMensaje == RegwebConstantes.MENSAJE_CONTROL_ACK}">
                                                <span class="label label-info"><i class="fa fa-check fa-fw"></i> <spring:message code="mensajeControl.tipo.01"/></span>
                                            </c:if>

                                            <c:if test="${mensajeControl.tipoMensaje == RegwebConstantes.MENSAJE_CONTROL_ERROR}">
                                                <span class="label label-danger" rel="popupAbajo" data-content="<c:out value="${mensajeControl.codigoError} - ${mensajeControl.descripcionMensaje}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-warning fa-fw"></i> <spring:message code="mensajeControl.tipo.02"/></span>
                                            </c:if>

                                            <c:if test="${mensajeControl.tipoMensaje == RegwebConstantes.MENSAJE_CONTROL_CONFIRMACION}">
                                                <span class="label label-success" rel="popupAbajo" data-content="<fmt:formatDate value="${mensajeControl.fechaEntradaDestino}" pattern="dd/MM/yyyy HH:mm:ss"/> - <c:out value="${mensajeControl.numeroRegistroEntradaDestino}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-barcode fa-fw"></i> <spring:message code="mensajeControl.tipo.03"/></span>
                                            </c:if>

                                            <%--Tipo Comunicación--%>
                                            <c:if test="${mensajeControl.tipoComunicacion == RegwebConstantes.TIPO_COMUNICACION_ENVIADO}">
                                                <span class="label label-warning" title="<spring:message code="mensajeControl.tipoComunicacion.enviado"/>"><i class="fa fa-mail-forward"></i></span>
                                            </c:if>
                                            <c:if test="${mensajeControl.tipoComunicacion == RegwebConstantes.TIPO_COMUNICACION_RECIBIDO}">
                                                <span class="label label-default" title="<spring:message code="mensajeControl.tipoComunicacion.recibido"/>"><i class="fa fa-mail-reply"></i></span>
                                            </c:if>

                                            <span class="pull-right text-muted small"><em><fmt:formatDate value="${mensajeControl.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></em></span>

                                        </a>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> <!-- /container -->

    <c:import url="../modulos/pie.jsp"/>

    <script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

    <script type="text/javascript">

        var urlEnviarACK = '<c:url value="/sir/enviarACK"/>';
        var urlEnviarConfirmacion = '<c:url value="/sir/enviarConfirmacion"/>';
        var urlReenviarIntercambio = '<c:url value="/sir/reenviarIntercambio"/>';
        var tradsMensajeControl = [];
        tradsMensajeControl['mensajeControl.ACK.enviado.ok'] = "<spring:message code='mensajeControl.ACK.enviado.ok' javaScriptEscape='true' />";
        tradsMensajeControl['mensajeControl.ACK.enviado.error'] = "<spring:message code='mensajeControl.ACK.enviado.error' javaScriptEscape='true' />";
        tradsMensajeControl['mensajeControl.confirmacion.enviado.ok'] = "<spring:message code='mensajeControl.confirmacion.enviado.ok' javaScriptEscape='true' />";
        tradsMensajeControl['mensajeControl.confirmacion.enviado.error'] = "<spring:message code='mensajeControl.confirmacion.enviado.error' javaScriptEscape='true' />";
        var tradsSir = [];
        tradsSir['registroSir.reiniciar.ok'] = "<spring:message code='registroSir.reiniciar.ok' javaScriptEscape='true' />";
        tradsSir['registroSir.reiniciar.error'] = "<spring:message code='registroSir.reiniciar.error' javaScriptEscape='true' />";
        tradsSir['intercambio.reenviado.ok'] = "<spring:message code='intercambio.reenviado.ok' javaScriptEscape='true' />";
        tradsSir['intercambio.reenviado.error'] = "<spring:message code='intercambio.reenviado.error' javaScriptEscape='true' />";
    </script>

    <script type="text/javascript" src="<c:url value="/js/sir.js"/>"></script>

</body>
</html>