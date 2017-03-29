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
                    <li><a <c:if test="${oficinaActiva.sir}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                    <%--<li><a href="<c:url value="/registroSalida/list"/>" ><i class="fa fa-list"></i> <spring:message code="registroSalida.listado"/></a></li>--%>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}</li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div id="mensajes"></div>
    
        <div class="row">
        
            <div class="col-xs-4">
            
                <div class="panel panel-danger">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroSalida.registroSalida"/> ${registro.numeroRegistroFormateado}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">
            
                        <dl class="detalle_registro">
                            <dt><i class="fa fa-home"></i> <spring:message code="registroSalida.oficina"/>: </dt> <dd> ${registro.oficina.denominacion}</dd>
                            <dt><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${registro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            <dt><i class="fa fa-book"></i> <spring:message code="libro.libro"/>: </dt> <dd> ${registro.libro.nombre}</dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="registroSalida.origen"/>: </dt> <dd>${registro.origen.denominacion} <c:if test="${registro.origen.estado.codigoEstadoEntidad != RegwebConstantes.ESTADO_ENTIDAD_VIGENTE}"><span class="label label-danger"><spring:message code="unidad.estado.${registro.origen.estado.codigoEstadoEntidad}" /></span></c:if></dd>
                            <c:if test="${not empty registro.registroDetalle.extracto}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.extracto"/>: </dt> <dd> ${registro.registroDetalle.extracto}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.tipoDocumentacionFisica}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.tipoDocumentacionFisica"/>: </dt> <dd> <spring:message code="tipoDocumentacionFisica.${registro.registroDetalle.tipoDocumentacionFisica}" /></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.tipoAsunto}"><dt><i class="fa fa-thumb-tack"></i> <spring:message code="tipoAsunto.tipoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.tipoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.idioma}">
                                 <dt><i class="fa fa-bullhorn"></i>
                                  <spring:message code="registroEntrada.idioma"/>:
                                  </dt>
                                  <dd> <spring:message code="idioma.${registro.registroDetalle.idioma}" /></dd>
                            </c:if>
                            <c:if test="${not empty registro.registroDetalle.codigoAsunto}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt> <dd> <i:trad value="${registro.registroDetalle.codigoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.referenciaExterna}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="registroEntrada.referenciaExterna"/>: </dt> <dd> ${registro.registroDetalle.referenciaExterna}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.expediente}"> <dt><i class="fa fa-newspaper-o"></i> <spring:message code="registroEntrada.expediente"/>: </dt> <dd> ${registro.registroDetalle.expediente}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.transporte || not empty registro.registroDetalle.numeroTransporte}"> <dt><i class="fa fa-bus"></i> <spring:message code="registroSalida.transporte"/>: </dt></c:if>  <c:if test="${not empty registro.registroDetalle.transporte}"><dd> <spring:message code="transporte.0${registro.registroDetalle.transporte}"/></c:if>  <c:if test="${not empty registro.registroDetalle.numeroTransporte}">${registro.registroDetalle.numeroTransporte}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.oficinaOrigen}"> <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficinaOrigen"/>: </dt> <dd> ${registro.registroDetalle.oficinaOrigen.denominacion} </dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.oficinaOrigenExternoCodigo}"> <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficinaOrigen"/>: </dt> <dd> ${registro.registroDetalle.oficinaOrigenExternoDenominacion}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.numeroRegistroOrigen}"> <dt><i class="fa fa-barcode"></i> <spring:message code="registroEntrada.numeroRegistroOrigen"/>: </dt> <dd> ${registro.registroDetalle.numeroRegistroOrigen}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.fechaOrigen}"> <dt><i class="fa fa-clock-o"></i> <spring:message code="registroEntrada.fechaOrigen"/>: </dt> <dd> <fmt:formatDate value="${registro.registroDetalle.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/></dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.observaciones}"> <dt><i class="fa fa-file-text-o"></i> <spring:message code="registroEntrada.observaciones"/>: </dt> <dd> ${registro.registroDetalle.observaciones}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.expone}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>: </dt> <dd> ${registro.registroDetalle.expone}</dd></c:if>
                            <c:if test="${not empty registro.registroDetalle.solicita}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>: </dt> <dd> ${registro.registroDetalle.solicita}</dd></c:if>
                            <dt><i class="fa fa-gears"></i> <spring:message code="registroEntrada.aplicacion"/>: </dt> <dd> ${registro.registroDetalle.aplicacion} ${registro.registroDetalle.version}</dd>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="registroSalida.estado"/>: </dt>
                            <dd>
                                <c:choose>
                                    <c:when test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO}">
                                        <span class="label label-success"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR}">
                                        <span class="label label-info"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.REGISTRO_OFICIO_EXTERNO || registro.estado == RegwebConstantes.REGISTRO_OFICIO_INTERNO}">
                                        <span class="label label-default"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.REGISTRO_ENVIADO}">
                                        <span class="label label-primary"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.REGISTRO_TRAMITADO}">
                                        <span class="label label-primary"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>
                                    <c:when test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO}">
                                        <span class="label label-danger"><spring:message code="registro.estado.${registro.estado}" /></span>
                                    </c:when>

                                </c:choose>
                            </dd>
                        </dl>
            
                    </div>

                    <%--Si no nos encontramos en la misma Oficia en la que se creó el Registro o en su Oficina Responsable, no podemos hacer nada con el--%>
                    <c:if test="${oficinaRegistral}">

                        <%--Botones imprimir Recibo, Justificante y Sello--%>
                        <c:if test="${registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR && registro.estado != RegwebConstantes.REGISTRO_ANULADO}">
                            <div class="panel-footer center">

                                <%--Si la entidad no es SIR o es una Reserva de Número, muestra el boton Modelo Recibo--%>
                                <c:if test="${!entidadActiva.sir || registro.estado == RegwebConstantes.REGISTRO_RESERVA}">
                                    <%--Si hay varios Modelso Recibo, muestra select--%>
                                    <c:if test="${fn:length(modelosRecibo) > 1}">
                                        <form:form modelAttribute="modeloRecibo" method="post" cssClass="form-horizontal">
                                            <div class="col-xs-12 btn-block">
                                                <div class="col-xs-6 no-pad-lateral list-group-item-heading">
                                                    <form:select path="idModelo" cssClass="chosen-select">
                                                        <form:options items="${modelosRecibo}" itemValue="id" itemLabel="nombre"/>
                                                    </form:select>
                                                </div>
                                                <div class="col-xs-6 no-pad-right list-group-item-heading">
                                                    <button type="button" class="btn btn-warning btn-sm btn-block" onclick="imprimirRecibo('<c:url value="/modeloRecibo/${registro.id}/RS/imprimir/"/>')"><spring:message code="modeloRecibo.imprimir"/></button>
                                                </div>
                                            </div>
                                        </form:form>

                                        <button type="button" data-toggle="modal" data-target="#selloModal" class="btn btn-warning btn-sm btn-block"><spring:message code="sello.imprimir"/></button>
                                    </c:if>

                                    <c:if test="${fn:length(modelosRecibo) == 1}">
                                        <div class="btn-group"><button type="button" class="btn btn-warning btn-sm" onclick="goTo('<c:url value="/modeloRecibo/${registro.id}/RS/imprimir/${modelosRecibo[0].id}"/>')"><spring:message code="modeloRecibo.imprimir"/></button></div>
                                    </c:if>

                                </c:if>

                                <%--Si la entidad es SIR, no és una Reserva de Número y no tiene ya justificante, muestra el boton Justificante --%>
                                <c:if test="${entidadActiva.sir && registro.estado != RegwebConstantes.REGISTRO_RESERVA && idJustificante == null}">
                                    <div class="btn-group"><button type="button" class="btn btn-warning btn-sm" onclick="goTo('<c:url value="/registroSalida/${registro.id}/justificante"/>')"><spring:message code="justificante.boton"/></button></div>
                                </c:if>

                                <%--Si la entidad es SIR, no es una Reserva de Número y tiene justificante, muestra el boton Descargar Justificante --%>
                                <c:if test="${entidadActiva.sir && registro.estado != RegwebConstantes.REGISTRO_RESERVA && idJustificante != null}">
                                    <div class="btn-group"><button type="button" class="btn btn-warning btn-sm" onclick="goTo('<c:url value="/anexo/descargarDocumento/${idJustificante}"/>')"><span class="fa fa-download"></span> <spring:message code="justificante.boton"/></button></div>
                                </c:if>

                                <%-- Botón de sello --%>
                                <div class="btn-group"><button type="button" data-toggle="modal" data-target="#selloModal" class="btn btn-warning btn-sm"><spring:message code="sello.imprimir"/></button></div>


                            </div>
                        </c:if>

                        <%--Botón Oficio Remision--%>
                        <c:if test="${oficio.oficioRemision}">
                            <div class="panel-footer center">
                                <button type="button" onclick="goTo('/regweb3/oficioRemision/salidasPendientesRemision')"
                                        class="btn btn-success btn-sm btn-block">
                                    <c:if test="${oficio.interno}">
                                        <spring:message code="oficioRemision.boton.crear.interno"/>
                                    </c:if>
                                    <c:if test="${oficio.externo}">
                                        <spring:message code="oficioRemision.boton.crear.externo"/>
                                    </c:if>
                                    <c:if test="${oficio.sir}">
                                        <spring:message code="oficioRemision.boton.crear.sir"/>
                                    </c:if>

                                </button>
                            </div>
                        </c:if>

                    </c:if>

                    <div class="panel-footer center">

                        <%--Botón Nueva Salida--%>
                        <div class="btn-group"><button type="button" onclick="goTo('/regweb3/registroSalida/new')" class="btn btn-danger btn-sm"><spring:message code="registro.boton.nuevo"/></button></div>

                        <%--Botón Editar Registro--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO && puedeEditar}">
                            <div class="btn-group"><button type="button" onclick="goTo('<c:url value="/registroSalida/${registro.id}/edit"/>')" class="btn btn-warning btn-sm"><spring:message code="registro.boton.editar"/></button></div>
                        </c:if>

                        <%--Botón Activar--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_ANULADO && puedeEditar}">
                            <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/activar"/>","<spring:message code="regweb.confirmar.activar" htmlEscape="true"/>")' class="btn btn-primary btn-sm"><spring:message code="regweb.activar"/></button></div>
                        </c:if>

                        <%--Botón Visar--%>
                        <c:if test="${registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR && isAdministradorLibro}">
                            <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/visar"/>","<spring:message code="regweb.confirmar.visar" htmlEscape="true"/>")' class="btn btn-success btn-sm"><spring:message code="regweb.visar"/></button></div>
                        </c:if>

                        <%--Botón Anular--%>
                        <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR) && puedeEditar}">
                            <div class="btn-group"><button type="button" onclick='javascript:confirm("<c:url value="/registroSalida/${registro.id}/anular"/>","<spring:message code="regweb.confirmar.anular" htmlEscape="true"/>")' class="btn btn-danger btn-sm"><spring:message code="regweb.anular"/></button></div>
                        </c:if>

                    </div>
            
                </div>
            
            </div>
            
            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <!-- ANEXOS -->
            <c:if test="${showannexes}">
                <c:if test="${(registro.estado == RegwebConstantes.REGISTRO_VALIDO || registro.estado == RegwebConstantes.REGISTRO_PENDIENTE_VISAR)&& oficinaRegistral && puedeEditar}">
                    <c:import url="../registro/anexos.jsp">
                        <c:param name="tipoRegistro" value="salida"/>
                    </c:import>
                </c:if>

                <%--ANEXOS SOLO LECTURA--%>
            <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO && registro.estado != RegwebConstantes.REGISTRO_RESERVA && registro.estado != RegwebConstantes.REGISTRO_PENDIENTE_VISAR) || !oficinaRegistral || !puedeEditar}">
                    <c:import url="../registro/anexosLectura.jsp">
                        <c:param name="tipoRegistro" value="salida"/>
                    </c:import>
                </c:if>
            </c:if>
            <%--INTERESADOS--%>
            <c:if test="${registro.estado == RegwebConstantes.REGISTRO_VALIDO && oficinaRegistral && puedeEditar}">
                <c:import url="../registro/interesados.jsp">
                    <c:param name="tipo" value="detalle"/>
                    <c:param name="tipoRegistro" value="salida"/>
                    <c:param name="comunidad" value="${comunidad.codigoComunidad}"/>
                    <c:param name="idRegistroDetalle" value="${registro.registroDetalle.id}"/>
                </c:import>
            </c:if>

            <%--INTERESADOS SOLO LECTURA--%>
            <c:if test="${(registro.estado != RegwebConstantes.REGISTRO_VALIDO  && registro.estado != RegwebConstantes.REGISTRO_RESERVA) || !oficinaRegistral || !puedeEditar}">
                <c:import url="../registro/interesadosLectura.jsp">
                    <c:param name="tipoRegistro" value="salida"/>
                </c:import>
            </c:if>

            <%--TRAZABILIDAD--%>
            <c:if test="${not empty trazabilidades}">
                <c:import url="../trazabilidad/trazabilidadSalida.jsp"/>
            </c:if>

            <!-- MODIFICACIONES REGISTRO -->
            <c:if test="${not empty historicos && registro.estado != RegwebConstantes.REGISTRO_RESERVA}">
                <c:import url="../registro/modificaciones.jsp">
                    <c:param name="tipoRegistro" value="salida"/>
                </c:import>
            </c:if>

        </div>
    
    </div>

    <%--SELLO --%>
    <c:import url="../registro/sello.jsp">
        <c:param name="tipoRegistro" value="registroSalida"/>
    </c:import>

</div>

<c:import url="../modulos/pie.jsp"/>

<%--<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>--%>
<script type="text/javascript" src="<c:url value="/js/sello.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/repro.js"/>"></script>

<script type="text/javascript">

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