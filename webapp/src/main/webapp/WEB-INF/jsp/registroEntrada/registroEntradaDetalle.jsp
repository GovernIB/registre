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
                    <li><a <c:if test="${oficinaActiva.sirEnvio || oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                 </ol>
             </div>
        </div><!-- Fin miga de pan -->

        <div id="mensajes"></div>

        <div class="row">

            <!-- Panel Lateral -->
            <div class="col-xs-4">

                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroEntrada.registroEntrada"/> ${registro.numeroRegistroFormateado}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <%--DETALLE REGISTRO--%>
                        <dl class="detalle_registro">
                            <c:import url="../registro/detalleRegistro.jsp">
                                <c:param name="tipoRegistro" value="entrada"/>
                            </c:import>
                        </dl>

                    </div>

                    <%--BOTONERA--%>

                    <%--Si no nos encontramos en la misma Oficia en la que se creó el Registro o en su Oficina Responsable, no podemos hacer nada con el--%>
                    <c:if test="${oficinaRegistral}">

                        <%--Botones imprimir Recibo, Justificante y Sello--%>
                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR && registro.estado != RegwebConstantes.REGISTRO_ANULADO}">
                            <div class="panel-footer center">

                                <%--Si la entidad no es SIR o es una Reserva de Número, muestra el boton Modelo Recibo--%>
                                <c:if test="${!entidadActiva.sir || registro.estado == RegwebConstantes.REGISTRO_RESERVA}">
                                    <%--Si hay varios Modelos Recibo, muestra select--%>
                                    <c:if test="${fn:length(modelosRecibo) > 1}">
                                        <form:form modelAttribute="modeloRecibo" method="post" cssClass="form-horizontal">
                                            <div class="col-xs-12 btn-block">
                                                <div class="col-xs-6 no-pad-lateral list-group-item-heading">
                                                    <form:select path="idModelo" cssClass="chosen-select">
                                                        <form:options items="${modelosRecibo}" itemValue="id" itemLabel="nombre"/>
                                                    </form:select>
                                                </div>
                                                <div class="col-xs-6 no-pad-right list-group-item-heading">
                                                    <button type="button" class="btn btn-warning btn-sm btn-block" onclick="imprimirRecibo('<c:url value="/modeloRecibo/${registro.id}/RE/imprimir/"/>')"><spring:message code="modeloRecibo.imprimir"/></button>
                                                </div>
                                            </div>
                                        </form:form>
                                    </c:if>
                                    <%--Si hay 1 Modelo Recibo, muestra sólo el botón --%>
                                    <c:if test="${fn:length(modelosRecibo) == 1}">
                                        <div class="btn-group"><button type="button" class="btn btn-warning btn-sm" onclick="goTo('<c:url value="/modeloRecibo/${registro.id}/RE/imprimir/${modelosRecibo[0].id}"/>')"><spring:message code="modeloRecibo.imprimir"/></button></div>
                                    </c:if>
                                </c:if>

                                <%--Si la entidad es SIR, no és una Reserva de Número y no tiene ya justificante, muestra el boton Justificante --%>
                                <c:if test="${entidadActiva.sir && registro.estado != RegwebConstantes.REGISTRO_RESERVA}">

                                    <c:if test="${idJustificante == null && registro.estado == RegwebConstantes.REGISTRO_VALIDO}">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown">
                                                <spring:message code="justificante.boton"/> <span class="caret"></span>
                                            </button>
                                            <ul class="dropdown-menu">
                                                <li class="submenu-complet"><a href="<c:url value="/registroEntrada/${registro.id}/justificante/ca"/>"><spring:message code="regweb.catalan"/></a></li>
                                                <li class="submenu-complet"><a href="<c:url value="/registroEntrada/${registro.id}/justificante/es"/>"><spring:message code="regweb.castellano"/></a></li>
                                            </ul>
                                        </div>
                                    </c:if>

                                    <c:if test="${idJustificante != null}">
                                        <div class="btn-group"><button type="button" class="btn btn-success btn-sm" onclick="goTo('<c:url value="/anexo/descargarFirma/${idJustificante}"/>')"><span class="fa fa-download"></span> <spring:message code="justificante.boton"/></button></div>
                                    </c:if>

                                </c:if>

                                <%-- Botón de sello --%>
                                <div class="btn-group"><button type="button" data-toggle="modal" data-target="#selloModal" class="btn btn-warning btn-sm"><spring:message code="sello.imprimir"/></button></div>

                            </div>
                        </c:if>

                        <%--Botón Distribuir y Oficio Remision--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO}">
                            <c:if test="${(isDistribuir && puedeDistribuir) || oficio.oficioRemision}">
                                <div class="panel-footer center">
                                    <c:if test="${isDistribuir && puedeDistribuir}">

                                        <button type="button" onclick='confirmDistribuir("<spring:message code="regweb.confirmar.distribuir" htmlEscape="true"/>")'
                                                class="btn btn-success btn-sm btn-block"><spring:message
                                                code="regweb.distribuir"/></button>

                                    </c:if>

                                    <c:if test="${oficio.oficioRemision}">

                                        <c:if test="${oficio.interno}">
                                            <button type="button" onclick="goTo('<c:url value="/oficioRemision/entradasPendientesRemision"/>')" class="btn btn-success btn-sm btn-block">
                                                <spring:message code="oficioRemision.boton.crear.interno"/>
                                            </button>
                                        </c:if>
                                        <c:if test="${oficio.externo}">
                                            <button type="button" onclick="goTo('<c:url value="/oficioRemision/entradasPendientesRemision"/>')" class="btn btn-success btn-sm btn-block">
                                                <spring:message code="oficioRemision.boton.crear.externo"/>
                                            </button>
                                        </c:if>
                                        <c:if test="${oficio.sir && oficinaActiva.sirEnvio}">
                                            <c:if test="${empty erroresAnexosSir}">
                                                <button type="button" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/enviarSir"/>')" class="btn btn-success btn-sm btn-block">
                                                    <spring:message code="registroEntrada.enviar.sir"/>
                                                </button>
                                            </c:if>

                                            <c:if test="${not empty erroresAnexosSir}">
                                                <button type="button" class="btn btn-success btn-sm btn-block disabled">
                                                    <spring:message code="registroEntrada.enviar.sir"/>
                                                </button>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${oficio.sir && !oficinaActiva.sirEnvio}">
                                            <p class="text-danger">El <strong>${(empty registro.destino)? registro.destinoExternoDenominacion : registro.destino.denominacion}</strong> dispone de una Oficina integrada en SIR, pero la ${oficinaActiva.denominacion} no está integrada en SIR y no se podrá realizar el intercambio.</p>
                                            <button type="button" onclick="goTo('<c:url value="/oficioRemision/entradasPendientesRemision"/>')" class="btn btn-success btn-sm btn-block">
                                                <spring:message code="oficioRemision.boton.crear.externo"/>
                                            </button>
                                        </c:if>

                                    </c:if>

                                </div>
                            </c:if>
                        </c:if>
                    </c:if>

                    <div class="panel-footer center">

                        <%--Botón nuevo registro--%>
                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroEntrada/new"/>')" class="btn btn-info btn-sm"><spring:message code="registro.boton.nuevo"/></button></div>
                        </c:if>

                        <%--Botón nueva reserva--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_RESERVA}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroEntrada/reserva"/>')" class="btn btn-info btn-sm"><spring:message code="registro.boton.nuevo.reserva"/></button></div>
                        </c:if>

                        <%--Si estamos en la Oficina donde se hizo el registro, podemos operar con el--%>
                        <c:if test="${oficinaRegistral}">

                            <%--Botón Editar Registro--%>
                            <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA) && puedeEditar && !tieneJustificante}">
                                <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroEntrada/${registro.id}/edit"/>')" class="btn btn-warning btn-sm"><spring:message code="registro.boton.editar"/></button></div>
                            </c:if>

                            <%--Botón Activar--%>
                            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO && puedeEditar}">
                                <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' class="btn btn-primary btn-sm"><spring:message code="regweb.activar"/></button></div>
                            </c:if>

                            <%--Botón Visar--%>
                            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR && isAdministradorLibro}">
                                <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/visar"/>","<spring:message code="regweb.confirmar.visar" htmlEscape="true"/>")' class="btn btn-success btn-sm"><spring:message code="regweb.visar"/></button></div>
                            </c:if>

                            <%--Botón Anular--%>
                            <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_RESERVA || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && puedeEditar}">
                                <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' class="btn btn-danger btn-sm"><spring:message code="regweb.anular"/></button></div>
                            </c:if>

                            <%--Botón reenviar--%>
                            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_RECHAZADO}">
                                <div class="btn-group"><button type="button" onclick='javascript:goTo("<c:url value="/registroEntrada/${registro.id}/reenviar"/>")' class="btn btn-success btn-sm"><spring:message code="registro.boton.reenviar"/></button></div>
                            </c:if>

                            <%--Botón rectificar--%>
                            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO || registro.estado == RegwebConstantes.REGISTRO_RECHAZADO || registro.estado == RegwebConstantes.REGISTRO_TRAMITADO}">
                                <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroEntrada/${registro.id}/rectificar"/>","<spring:message code="regweb.confirmar.rectificar" htmlEscape="true"/>")' class="btn btn-danger btn-sm"><spring:message code="registro.boton.rectificar"/></button></div>
                            </c:if>

                        </c:if>

                    </div>

                </div>

            </div>
            <!-- Fin Panel Lateral -->

            <div class="col-xs-8 pull-right" id="mensajesdetalle">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <!-- ANEXOS COMPLETO-->
            <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && oficinaRegistral && puedeEditar && !tieneJustificante}">
                <c:import url="../registro/anexos.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

            <%--ANEXOS SOLO LECTURA--%>
            <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO && registro.estado != RegwebConstantes.REGISTRO_RESERVA && registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR) || !oficinaRegistral || !puedeEditar || tieneJustificante}">
                <c:import url="../registro/anexosLectura.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

            <%--INTERESADOS--%>
            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO && oficinaRegistral && puedeEditar && !tieneJustificante}">
                <c:import url="../registro/interesados.jsp">
                    <c:param name="tipo" value="detalle"/>
                    <c:param name="tipoRegistro" value="entrada"/>
                    <c:param name="comunidad" value="${comunidad.codigoComunidad}"/>
                    <c:param name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
                </c:import>
            </c:if>

            <%--INTERESADOS SOLO LECTURA--%>
            <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO && registro.estado != RegwebConstantes.REGISTRO_RESERVA) || !oficinaRegistral || !puedeEditar || tieneJustificante}">
                <c:import url="../registro/interesadosLectura.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

            <%--EXPONE - SOLICITA--%>
            <c:if test="${not empty registro.registroDetalle.expone || not empty registro.registroDetalle.solicita}">
                <c:import url="../registro/exponeSolicita.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

            <%--TRAZABILIDAD--%>
            <c:if test="${not empty trazabilidades}">
                <c:import url="../trazabilidad/trazabilidadEntrada.jsp"/>
            </c:if>

            <!-- MODIFICACIONES REGISTRO -->
            <c:if test="${not empty historicos && registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                <c:import url="../registro/modificaciones.jsp">
                    <c:param name="tipoRegistro" value="entrada"/>
                </c:import>
            </c:if>

        </div>
        </div><!-- /div.row-->

    <%--SELLO --%>
    <c:import url="../registro/sello.jsp">
        <c:param name="tipoRegistro" value="registroEntrada"/>
    </c:import>

    <%-- MODAL Distribuir--%>
    <c:import url="../registro/registroTramitar.jsp">
        <c:param name="tipoRegistro" value="registroEntrada"/>
    </c:import>

</div>


<c:import url="../modulos/pie.jsp"/>

<script type="text/javascript">
    var urlDistribuir = '<c:url value="/registroEntrada/${registro.id}/distribuir"/>';
    var urlEnviarDestinatarios = '<c:url value="/registroEntrada/${registro.id}/enviarDestinatarios"/>';
    var urlDetalle = '<c:url value="/registroEntrada/${registro.id}/detalle" />';

    <%-- Traducciones para distribuir.js --%>
    var traddistribuir = new Array();
    traddistribuir['campo.obligatorio'] = "<spring:message code='registro.distribuir.propuesto.obligatorio' javaScriptEscape='true' />";
    traddistribuir['distribuir.nodestinatarios'] = "<spring:message code='registro.distribuir.nodestinatarios' javaScriptEscape='true' />";
    traddistribuir['distribuir.noenviado'] = "<spring:message code='registroEntrada.distribuir.noenviado' javaScriptEscape='true' />";
    traddistribuir['distribuir.error.plugin'] = "<spring:message code='registroEntrada.distribuir.error.plugin' javaScriptEscape='true' />";
</script>


<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/sello.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/repro.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/distribuir.js"/>"></script>

<script type="text/javascript">

    // Descarga el justificante si se ha generado manualmente
    window.onload = function descargaJustificante(){
        <c:if test="${justificante}">
            goTo('<c:url value="/anexo/descargarFirma/${idJustificante}"/>');
        </c:if>
    }


    // Muestra los datos del hitórico seleccionado y oculta el resto
    function comparaRegistros(idHistorico){

        <c:forEach var="historico" items="${historicos}">
            $('#'+${historico.id}).hide();
        </c:forEach>
        var elemento = '#'+idHistorico;
        $(elemento).show();
    }

    function actualizarLocalidad(){
        <c:url var="obtenerLocalidades" value="/rest/obtenerLocalidades" />
        actualizarSelect('${obtenerLocalidades}','#localidad\\.id',$('#provincia\\.id option:selected').val(),$('#localidad\\.id option:selected').val(),false,false);
    }


</script>


</body>
</html>