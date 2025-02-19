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

            <!-- Miga de pan -->
            <div class="row">
                <div class="col-xs-12">
                    <ol class="breadcrumb">
                        <c:import url="../modulos/migadepan.jsp"/>
                        <li class="active"><i class="fa fa-search"></i> <spring:message code="idIntercambio.detalle"/> ${oficioRemision.identificadorIntercambio}</li>
                    </ol>
                </div>
            </div>

            <div id="mensajes"></div>

            <c:import url="../modulos/mensajes.jsp"/>

            <div class="row">

                <%-- PANEL LATERAL  --%>
                <div class="col-lg-4">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-file"></i>
                            <strong><spring:message code="registro.enviado.sir"/></strong>
                        </div>

                        <%--Formulario oculto integraciones--%>
                        <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                            <form:hidden path="texto"/>
                        </form:form>

                        <%--DETALLE REGISTRO ENVIADO SIR--%>

                        <%--REGISTRO ENTRADA--%>
                        <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

                            <div class="panel-body">
                                <c:set var="registro" value="${trazabilidades[0].registroEntradaOrigen}" scope="request"/>
                                <dl class="detalle_registro">
                                    <dt><i class="fa fa-file-o"></i> <spring:message code="registroSir.tipoRegistro"/>: </dt>
                                    <dd><span class="label label-info"><spring:message code="registroSir.entrada"/></span></dd>
                                    <dt><i class="fa fa-barcode"></i> <spring:message code="registroEntrada.numeroRegistro"/>: </dt>
                                    <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd>

                                    <%--Detalle registro--%>
                                    <c:import url="../registro/detalleRegistro.jsp">
                                        <c:param name="tipoRegistro" value="intercambio"/>
                                    </c:import>

                                    <%--Reintentos--%>
                                    <c:if test="${oficioRemision.numeroReintentos > 0}">
                                        <hr class="divider-warning">
                                        <dt><i class="fa fa-retweet"></i> <spring:message code="oficioRemision.reintentos"/>:</dt>
                                        <dd> ${oficioRemision.numeroReintentos}</dd>
                                    </c:if>

                                </dl>
                            </div>
                            <%--BOTONERA DETALLE E INTEGRACIONES--%>
                            <div class="panel-footer center">

                               <%--Botón integraciones--%>
                                <div class="btn-group">
                                    <button type="button" onclick="buscarIntegraciones('${oficioRemision.identificadorIntercambio}')" class="btn btn-warning btn-sm btn-block">
                                        <spring:message code="integracion.integraciones"/>
                                    </button>
                                </div>

                                <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/adminEntidad/registroEntrada/${registro.id}/detalle"/>')" class="btn btn-info btn-sm"><spring:message code="registroEntrada.detalle"/></button></div>
                            </div>
                            <%--BOTONERA REINICIAR Y REENVIAR--%>
                            <div class="panel-footer center">
                                <c:if test="${oficioRemision.estado != RegwebConstantes.OFICIO_ACEPTADO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECHAZADO &&
                                    oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO}">
                                    <c:url value="/sir/oficio/reiniciar" var="urlReiniciar"/>
                                    <div class="btn-group"><button type="button" onclick="reiniciarContador('${oficioRemision.id}','${urlReiniciar}')" class="btn btn-warning btn-sm"><spring:message code="registroSir.reiniciar"/></button></div>
                                </c:if>

                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO
                                                                || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}">
                                    <div class="btn-group"><button type="button" onclick='confirm("javascript:reenviarIntercambio(${oficioRemision.id})","<spring:message code="regweb.confirmar.enviarIntercambio" htmlEscape="true"/>")' class="btn btn-warning btn-sm"><spring:message code="intercambio.reenviar"/></button></div>
                                </c:if>
                            </div>
                        </c:if>

                        <%--REGISTRO SALIDA--%>
                        <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                            <div class="panel-body">
                                <c:set var="registro" value="${trazabilidades[0].registroSalida}" scope="request"/>
                                <dl class="detalle_registro">
                                    <dt><i class="fa fa-file-o"></i> <spring:message code="registroSir.tipoRegistro"/>: </dt>
                                    <dd><span class="label label-danger"><spring:message code="registroSir.salida"/></span></dd>
                                    <dt><i class="fa fa-barcode"></i> <spring:message code="registroSalida.numeroRegistro"/>: </dt>
                                    <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd>

                                    <%--Detalle registro--%>
                                    <c:import url="../registro/detalleRegistro.jsp">
                                        <c:param name="tipoRegistro" value="intercambio"/>
                                    </c:import>

                                    <%--Reintentos--%>
                                    <c:if test="${oficioRemision.numeroReintentos > 0}">
                                        <hr class="divider-warning">
                                        <dt><i class="fa fa-retweet"></i> <spring:message code="oficioRemision.reintentos"/>:</dt>
                                        <dd> ${oficioRemision.numeroReintentos}</dd>
                                    </c:if>
                                </dl>
                            </div>
                            <%--BOTONERA DETALLE E INTEGRACIONES--%>
                            <div class="panel-footer center">

                                <%--Botón integraciones--%>
                                <div class="btn-group">
                                    <button type="button" onclick="buscarIntegraciones('${oficioRemision.identificadorIntercambio}')" class="btn btn-warning btn-sm btn-block">
                                        <spring:message code="integracion.integraciones"/>
                                    </button>
                                </div>

                                <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/adminEntidad/registroSalida/${registro.id}/detalle"/>')" class="btn btn-danger btn-sm"><spring:message code="registroSalida.detalle"/></button></div>

                            </div>
                            <%--BOTONERA REINICIAR Y REENVIAR--%>
                            <div class="panel-footer center">
                                <c:if test="${oficioRemision.estado != RegwebConstantes.OFICIO_ACEPTADO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECHAZADO &&
                                                                              oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO && oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO}">
                                    <c:url value="/sir/oficio/reiniciar" var="urlReiniciar"/>
                                    <div class="btn-group"><button type="button" onclick="reiniciarContador('${oficioRemision.id}','${urlReiniciar}')" class="btn btn-warning btn-sm"><spring:message code="registroSir.reiniciar"/></button></div>
                                </c:if>

                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO
                                                                || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}">
                                    <div class="btn-group"><button type="button" onclick='confirm("javascript:reenviarIntercambio(${oficioRemision.id})","<spring:message code="regweb.confirmar.enviarIntercambio" htmlEscape="true"/>")' class="btn btn-warning btn-sm"><spring:message code="intercambio.reenviar"/></button></div>
                                </c:if>
                            </div>
                        </c:if>
                   </div>
                </div>

                <%--DESTINO--%>
                <div class="col-lg-8">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-institution"></i> <strong><spring:message code="oficioRemision.destino"/></strong>
                        </div>
                        <div class="panel-body">
                            <p><i class="fa fa-institution"></i> <strong><spring:message code="organismo.organismo"/>:</strong> ${oficioRemision.destinoExternoDenominacion}</p>
                            <c:if test="${not empty oficioRemision.decodificacionEntidadRegistralDestino}">
                                <p><i class="fa fa-home"></i> <strong><spring:message code="oficina.oficina"/>:</strong> <a href="<c:url value="${loginInfo.dir3Caib.server}/oficina/${oficioRemision.codigoEntidadRegistralDestino}/detall"/>" target="_blank">${oficioRemision.decodificacionEntidadRegistralDestino} (${oficioRemision.codigoEntidadRegistralDestino})</a></p>
                            </c:if>

                            <c:if test="${not empty oficioRemision.fechaEstado && oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}">
                                <p><strong><i class="fa fa-clock-o"></i> <spring:message code="oficioRemision.fecha.aceptado"/>:</strong> <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                <p><strong><i class="fa fa-barcode"></i> <spring:message code="registroSir.numeroRegistro"/>:</strong> ${oficioRemision.numeroRegistroEntradaDestino}</p>
                            </c:if>

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
                            
                            <%--TRAZABILIDAD ENVIADO SIR--%>
                            <c:forEach var="trazabilidad" items="${trazabilidades}">

                                <a href="javascript:void(0);" class="list-group-item">
                                    <span class="label label-warning"><i class="fa fa-check fa-fw"></i> <spring:message code="oficioRemision.estado.3"/></span>
                                    <span class="pull-right text-muted small"><em><fmt:formatDate value="${trazabilidad.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></em></span>
                                </a>

                                <c:if test="${trazabilidad.oficioRemision.estado != RegwebConstantes.OFICIO_SIR_ENVIADO}">
                                    <a href="javascript:void(0);" class="list-group-item">
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_INTERNO_ENVIADO}"><span class="label label-warning"><i class="fa fa-check fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_EXTERNO_ENVIADO}"><span class="label label-warning"><i class="fa fa-check fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}"><span class="label label-success" rel="popupAbajo" data-content="<c:out value="${trazabilidad.oficioRemision.numeroRegistroEntradaDestino}" escapeXml="true"/> - <fmt:formatDate value="${trazabilidad.oficioRemision.fechaEntradaDestino}" pattern="dd/MM/yyyy HH:mm:ss"/>" data-toggle="popover"><i class="fa fa-barcode fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO}"><span class="label label-warning"><i class="fa fa-check fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK}"><span class="label label-success"><i class="fa fa-check fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR}"><span class="label label-danger"><i class="fa fa-warning fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO}"><span class="label label-warning" rel="popupAbajo" data-content="<strong><spring:message code="oficioRemision.destino"/>:</strong> ${trazabilidad.oficioRemision.decodificacionEntidadRegistralDestino} (${trazabilidad.oficioRemision.codigoEntidadRegistralDestino})" data-toggle="popover"><i class="fa fa-check fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}"><span class="label label-success" rel="popupAbajo" data-content="<strong><spring:message code="oficioRemision.destino"/>:</strong> ${trazabilidad.oficioRemision.decodificacionEntidadRegistralDestino} (${trazabilidad.oficioRemision.codigoEntidadRegistralDestino})" data-toggle="popover"><i class="fa fa-check fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR}"><span class="label label-danger" rel="popupAbajo" data-content="<strong>Error:</strong> ${trazabilidad.oficioRemision.codigoError} - ${trazabilidad.oficioRemision.descripcionError}" data-toggle="popover"><i class="fa fa-warning fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}"><span class="label label-danger"><i class="fa fa-warning fa-fw"></i></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"></c:if>
                                        <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}">
                                            <%-- Decodificación T. anotación (Posteriormente se añadió esta info en Oficio Remisión, de ahí está comprobación)--%>
                                            <c:if test="${empty trazabilidad.oficioRemision.decodificacionTipoAnotacion}">
                                                <c:set var="decodificacionTipoAnotacion" value="${trazabilidad.registroEntradaOrigen.registroDetalle.decodificacionTipoAnotacion}" scope="request"/>
                                            </c:if>
                                            <c:if test="${not empty trazabilidad.oficioRemision.decodificacionTipoAnotacion}">
                                                <c:set var="decodificacionTipoAnotacion" value="${trazabilidad.oficioRemision.decodificacionTipoAnotacion}" scope="request"/>
                                            </c:if>
                                            <span class="label label-danger" rel="popupAbajo" data-content="<c:out value="${decodificacionTipoAnotacion}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-warning fa-fw"></i>
                                        </c:if>

                                        <spring:message code="oficioRemision.estado.${trazabilidad.oficioRemision.estado}"/></span>
                                        <span class="pull-right text-muted small"><em><fmt:formatDate value="${trazabilidad.oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></em></span>
                                    </a>
                                </c:if>
                            </c:forEach>

                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR}">
                                <%--BOTONERA--%>
                                <div class="panel-footer center">
                                    <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/sir/${oficioRemision.id}/anular"/>')" class="btn btn-danger btn-sm"><spring:message code="oficioRemision.anular"/></button></div>
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
                                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK || oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO}">
                                                    <div class="btn-group"><button type="button" onclick='confirm("<c:url value="/sir/${oficioRemision.id}/confirmar"/>","<spring:message code="intercambio.confirmar.confirmacion" htmlEscape="true"/>")' class="btn btn-success btn-xs btn-block" title="<spring:message code="regweb.confirmar"/>"><i class="fa fa-check fa-fw"></i></button></div>
                                                </c:if>
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