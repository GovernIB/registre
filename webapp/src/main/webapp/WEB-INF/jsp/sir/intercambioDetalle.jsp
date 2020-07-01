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
                        <li class="active"><i class="fa fa-search"></i> <spring:message code="idIntercambio.detalle"/> ${idIntercambio}</li>
                    </ol>
                </div>
            </div><!-- Fin miga de pan -->

            <c:import url="../modulos/mensajes.jsp"/>

            <div class="row">

                <%-- PANEL LATERAL  --%>
                <div class="col-lg-4">
                    <div class="panel panel-warning">
                        <div class="panel-heading">
                            <i class="fa fa-file"></i>
                            <c:if test="${not empty trazabilidadesSir}">
                                <strong><spring:message code="registro.recibido.sir"/></strong>
                            </c:if>
                            <c:if test="${not empty trazabilidades}">
                                <strong><spring:message code="registro.enviado.sir"/></strong>
                            </c:if>
                        </div>

                            <%--DETALLE REGISTRO RECIBIDO SIR--%>
                            <c:if test="${not empty trazabilidadesSir}">

                                <div class="panel-body">
                                    <c:set var="registroSir" value="${trazabilidadesSir[0].registroSir}" scope="request"/>
                                    <c:import url="../registroSir/detalleRegistroSir.jsp"/>
                                </div>
                                <%--BOTONERA--%>
                                <div class="panel-footer center">
                                    <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroSir/${registroSir.id}/detalle"/>')" class="btn btn-primary btn-sm"><spring:message code="registroSir.detalle"/></button></div>
                                </div>

                            </c:if>

                            <%--DETALLE REGISTRO ENVIADO SIR--%>
                            <c:if test="${not empty trazabilidades}">
                                <%--REGISTRO ENTRADA--%>
                                <c:if test="${trazabilidades[0].oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

                                    <div class="panel-body">
                                        <c:set var="registro" value="${trazabilidades[0].registroEntradaOrigen}" scope="request"/>
                                        <dl class="detalle_registro">
                                            <dt><i class="fa fa-file-o"></i> <spring:message code="registroSir.tipoRegistro"/>: </dt>
                                            <dd><span class="label label-info"><spring:message code="registroSir.entrada"/></span></dd>
                                            <dt><i class="fa fa-barcode"></i> <spring:message code="registroEntrada.numeroRegistro"/>: </dt>
                                            <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd>
                                            <c:import url="../registro/detalleRegistro.jsp">
                                                <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                            </c:import>
                                        </dl>
                                    </div>
                                    <%--BOTONERA--%>
                                    <div class="panel-footer center">
                                        <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/adminEntidad/registroEntrada/${registro.id}/detalle"/>')" class="btn btn-info btn-sm"><spring:message code="registroEntrada.detalle"/></button></div>
                                    </div>

                                </c:if>

                                <%--REGISTRO SALIDA--%>
                                <c:if test="${trazabilidades[0].oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                    <div class="panel-body">
                                        <c:set var="registro" value="${trazabilidades[0].registroSalida}" scope="request"/>
                                        <dl class="detalle_registro">
                                            <dt><i class="fa fa-file-o"></i> <spring:message code="registroSir.tipoRegistro"/>: </dt>
                                            <dd><span class="label label-danger"><spring:message code="registroSir.salida"/></span></dd>
                                            <dt><i class="fa fa-barcode"></i> <spring:message code="registroSalida.numeroRegistro"/>: </dt>
                                            <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd>

                                            <c:import url="../registro/detalleRegistro.jsp">
                                                <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_SALIDA}"/>
                                            </c:import>
                                        </dl>
                                    </div>
                                    <%--BOTONERA--%>
                                    <div class="panel-footer center">
                                        <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/adminEntidad/registroSalida/${registro.id}/detalle"/>')" class="btn btn-danger btn-sm"><spring:message code="registroSalida.detalle"/></button></div>
                                    </div>
                                </c:if>
                            </c:if>
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
                            <c:if test="${not empty trazabilidadesSir}">
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
                            </c:if>

                            <%--TRAZABILIDAD ENVIADO SIR--%>
                            <c:if test="${not empty trazabilidades}">
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
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}"><span class="label label-danger" rel="popupAbajo" data-content="<c:out value="${trazabilidad.registroEntradaOrigen.registroDetalle.decodificacionTipoAnotacion}" escapeXml="true"/>" data-toggle="popover"><i class="fa fa-warning fa-fw"></i></c:if>
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}"><span class="label label-danger"><i class="fa fa-warning fa-fw"></i></c:if>
                                            <c:if test="${trazabilidad.oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"></c:if>
                                            <spring:message code="oficioRemision.estado.${trazabilidad.oficioRemision.estado}"/></span>
                                            <span class="pull-right text-muted small"><em><fmt:formatDate value="${trazabilidad.oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></em></span>
                                        </a>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO}">
                                    <%--BOTONERA--%>
                                    <div class="panel-footer center">
                                        <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/sir/${trazabilidades[0].oficioRemision.id}/anular"/>')" class="btn btn-danger btn-sm"><spring:message code="oficioRemision.anular"/></button></div>
                                    </div>
                                </c:if>
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

</body>
</html>