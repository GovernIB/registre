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

            <div id="mensajes"></div>

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

                        <%--Formulario oculto integraciones--%>
                        <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                            <form:hidden path="texto"/>
                        </form:form>

                        <%--DETALLE REGISTRO RECIBIDO SIR--%>
                        <c:if test="${not empty trazabilidadesSir}">

                            <div class="panel-body">
                                <c:set var="registroSir" value="${trazabilidadesSir[0].registroSir}" scope="request"/>
                                <c:import url="../registroSir/detalleRegistroSir.jsp"/>
                            </div>
                            <%--BOTONERA--%>
                            <div class="panel-footer center">
                                <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/registroSir/${registroSir.id}/detalle"/>')" class="btn btn-primary btn-sm"><spring:message code="registroSir.detalle"/></button></div>
                            </div>
                            <div class="panel-footer center">
                                <div class="btn-group"><button type="button" onclick='confirm("javascript:enviarACK(${registroSir.id})","<spring:message code="regweb.confirmar.enviarMensaje" htmlEscape="true"/>")' class="btn btn-info btn-sm"><spring:message code="mensajeControl.enviar.ACK"/></button></div>
                                <%--Botón integraciones--%>
                                <div class="btn-group">
                                    <button type="button" onclick="buscarIntegraciones('${idIntercambio}')" class="btn btn-warning btn-sm btn-block">
                                        <spring:message code="integracion.integraciones"/>
                                    </button>
                                </div>
                                <c:if test="${registroSir.estado == 'ACEPTADO'}">
                                    <div class="btn-group"><button type="button" onclick='confirm("javascript:enviarConfirmacion(${registroSir.id})","<spring:message code="regweb.confirmar.enviarMensaje" htmlEscape="true"/>")' class="btn btn-success btn-sm"><spring:message code="mensajeControl.enviar.confirmacion"/></button></div>
                                </c:if>
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

                                        <%--Detalle registro--%>
                                        <c:import url="../registro/detalleRegistro.jsp">
                                            <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_ENTRADA}"/>
                                        </c:import>

                                        <%--Reintentos--%>
                                        <c:if test="${trazabilidades[0].oficioRemision.numeroReintentos > 0}">
                                            <hr class="divider-warning">
                                            <dt><i class="fa fa-retweet"></i> <spring:message code="oficioRemision.reintentos"/>:</dt>
                                            <dd> ${trazabilidades[0].oficioRemision.numeroReintentos}</dd>
                                        </c:if>

                                    </dl>
                                </div>
                                <%--BOTONERA DETALLE E INTEGRACIONES--%>
                                <div class="panel-footer center">

                                   <%--Botón integraciones--%>
                                    <div class="btn-group">
                                        <button type="button" onclick="buscarIntegraciones('${idIntercambio}')" class="btn btn-warning btn-sm btn-block">
                                            <spring:message code="integracion.integraciones"/>
                                        </button>
                                    </div>

                                    <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/adminEntidad/registroEntrada/${registro.id}/detalle"/>')" class="btn btn-info btn-sm"><spring:message code="registroEntrada.detalle"/></button></div>
                                </div>
                                <%--BOTONERA REINICIAR Y REENVIAR--%>
                                <div class="panel-footer center">
                                    <c:if test="${trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_ACEPTADO && trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECHAZADO &&
                                                                                  trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO && trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO}">
                                        <c:url value="/sir/oficio/reiniciar" var="urlReiniciar"/>
                                        <div class="btn-group"><button type="button" onclick="reiniciarContador('${trazabilidades[0].oficioRemision.id}','${urlReiniciar}')" class="btn btn-warning btn-sm"><spring:message code="registroSir.reiniciar"/></button></div>
                                    </c:if>

                                    <c:if test="${trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO
                                                                    || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}">
                                        <%--<div class="btn-group"><button type="button" onclick='confirm("javascript:reenviarIntercambio(${trazabilidades[0].oficioRemision.id})","<spring:message code="regweb.confirmar.enviarIntercambio" htmlEscape="true"/>")' class="btn btn-warning btn-sm"><spring:message code="intercambio.reenviar"/></button></div>--%>
                                        <div class="btn-group"><button type="button" onclick="reencolarIntercambioModal('${trazabilidades[0].oficioRemision.oficina.codigo}','${trazabilidades[0].oficioRemision.identificadorIntercambio}', confirmModal)" class="btn btn-warning btn-sm"><spring:message code="intercambio.reenviar"/></button></div>
                                    </c:if>
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

                                        <%--Detalle registro--%>
                                        <c:import url="../registro/detalleRegistro.jsp">
                                            <c:param name="tipoRegistro" value="${RegwebConstantes.REGISTRO_SALIDA}"/>
                                        </c:import>

                                        <%--Reintentos--%>
                                        <c:if test="${trazabilidades[0].oficioRemision.numeroReintentos > 0}">
                                            <hr class="divider-warning">
                                            <dt><i class="fa fa-retweet"></i> <spring:message code="oficioRemision.reintentos"/>:</dt>
                                            <dd> ${trazabilidades[0].oficioRemision.numeroReintentos}</dd>
                                        </c:if>
                                    </dl>
                                </div>
                                <%--BOTONERA DETALLE E INTEGRACIONES--%>
                                <div class="panel-footer center">
                                    <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                                        <form:hidden path="texto"/>
                                    </form:form>

                                        <%--Botón integraciones--%>
                                    <div class="btn-group">
                                        <button type="button" onclick="buscarIntegraciones('${idIntercambio}')" class="btn btn-warning btn-sm btn-block">
                                            <spring:message code="integracion.integraciones"/>
                                        </button>
                                    </div>

                                    <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/adminEntidad/registroSalida/${registro.id}/detalle"/>')" class="btn btn-danger btn-sm"><spring:message code="registroSalida.detalle"/></button></div>

                                </div>
                                <%--BOTONERA REINICIAR Y REENVIAR--%>
                                <div class="panel-footer center">
                                    <c:if test="${trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_ACEPTADO && trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_SIR_RECHAZADO &&
                                                                                  trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO && trazabilidades[0].oficioRemision.estado != RegwebConstantes.OFICIO_SIR_DEVUELTO}">
                                        <c:url value="/sir/oficio/reiniciar" var="urlReiniciar"/>
                                        <div class="btn-group"><button type="button" onclick="reiniciarContador('${trazabilidades[0].oficioRemision.id}','${urlReiniciar}')" class="btn btn-warning btn-sm"><spring:message code="registroSir.reiniciar"/></button></div>
                                    </c:if>

                                    <c:if test="${trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO  || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO
                                                                    || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}">
                                        <%--<div class="btn-group"><button type="button" onclick='confirm("javascript:reencolarIntercambio(${trazabilidades[0].oficioRemision.oficina.codigo},${trazabilidades[0].oficioRemision.idIntercambio})","<spring:message code="regweb.confirmar.enviarIntercambio" htmlEscape="true"/>")' class="btn btn-warning btn-sm"><spring:message code="intercambio.reenviar"/></button></div>--%>
                                        <div class="btn-group"><button type="button" onclick="reencolarIntercambioModal('${trazabilidades[0].oficioRemision.oficina.codigo}','${trazabilidades[0].oficioRemision.identificadorIntercambio}', confirmModal)" class="btn btn-warning btn-sm"><spring:message code="intercambio.reenviar"/></button></div>
                                    </c:if>
                                </div>
                            </c:if>
                        </c:if>
                   </div>
                </div>

                <%--DESTINO--%>
                <c:if test="${not empty trazabilidades}">
                    <div class="col-lg-8">
                        <div class="panel panel-warning">
                            <div class="panel-heading">
                                <i class="fa fa-institution"></i> <strong><spring:message code="oficioRemision.destino"/></strong>
                            </div>
                            <div class="panel-body">
                                <p><i class="fa fa-institution"></i> <strong><spring:message code="organismo.organismo"/>:</strong> ${trazabilidades[0].oficioRemision.destinoExternoDenominacion}</p>
                                <c:if test="${not empty trazabilidades[0].oficioRemision.decodificacionEntidadRegistralDestino}">

                                    <p><i class="fa fa-home"></i> <strong><spring:message code="oficina.oficina"/>:</strong>${trazabilidades[0].oficioRemision.decodificacionEntidadRegistralDestino} (${trazabilidades[0].oficioRemision.codigoEntidadRegistralDestino})</p>
                                    <c:if test="${not empty trazabilidades[0].oficioRemision.contactosEntidadRegistralDestino}">
                                        <div class="alert alert-grey"><small>${trazabilidades[0].oficioRemision.contactosEntidadRegistralDestino}</small></div>
                                    </c:if>

                                </c:if>

                                <c:if test="${not empty trazabilidades[0].oficioRemision.fechaEstado && trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}">
                                    <p><strong><i class="fa fa-clock-o"></i> <spring:message code="oficioRemision.fecha.aceptado"/>:</strong> <fmt:formatDate value="${trazabilidades[0].oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                    <p><strong><i class="fa fa-barcode"></i> <spring:message code="registroSir.numeroRegistro"/>:</strong> ${trazabilidades[0].oficioRemision.numeroRegistroEntradaDestino}</p>
                                </c:if>

                            </div>
                        </div>
                    </div>
                </c:if>

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

                                <c:if test="${trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO || trazabilidades[0].oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR}">
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

    <script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

    <script type="text/javascript">

        var urlEnviarACK = '<c:url value="/sir/enviarACK"/>';
        var urlEnviarConfirmacion = '<c:url value="/sir/enviarConfirmacion"/>';
        var urlReencolarIntercambio = '<c:url value="/sir/reencolarIntercambio"/>';
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
        tradsSir['intercambio.continuar'] = "<spring:message code="regweb.continuar" javaScriptEscape="true"/>";

        var confirmModal =
            $("<div class=\"modal fade\">" +
                "<div class=\"modal-dialog\">" +
                "<div class=\"modal-content\">" +
                "<div class=\"modal-header\">" +
                "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>" +
                "<h4 class=\"modal-title\">Confirmar</h4>" +
                "</div>" +

                "<div class=\"modal-body\">" +
                "<p>"+tradsSir['intercambio.continuar']+"</p>" +
                "</div>" +

                "<div class=\"modal-footer\">" +
                "<button type=\"button\" id=\"cancelButton\" class=\"btn btn-default\" data-dismiss=\"modal\">No</button>"+
                "<button type=\"button\" id=\"okButton\" class=\"btn btn-danger\">Ok</button>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</div>");

    </script>

    <script type="text/javascript" src="<c:url value="/js/sir.js"/>"></script>

</body>
</html>