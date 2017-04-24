<%@ page contentType="text/html;chasientoRegistralSiret=UTF-8" language="java" %>
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
                    <li><a href="<c:url value="/asientoRegistralSir/list"/>"><i class="fa fa-list"></i> <spring:message
                            code="asientoRegistralSir.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message
                            code="asientoRegistralSir.asientoRegistralSir"/> ${asientoRegistralSir.numeroRegistro}</li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->
        <c:url value="/asientoRegistralSir/aceptar/${asientoRegistralSir.id}" var="urlAceptar" scope="request"/>


        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message
                                    code="asientoRegistralSir.asientoRegistralSir"/> ${asientoRegistralSir.numeroRegistro}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <dl class="detalle_registro">

                            <c:if test="${not empty asientoRegistralSir.fechaRecepcion}">
                                <dt><i class="fa fa-clock-o"></i> <spring:message code="asientoRegistralSir.fechaRecepcion"/>: </dt>
                                <dd><fmt:formatDate value="${asientoRegistralSir.fechaRecepcion}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            </c:if>

                            <c:if test="${not empty asientoRegistralSir.codigoUnidadTramitacionOrigen}">
                                <dt><i class="fa fa-institution"></i> <spring:message code="asientoRegistralSir.unidadOrigen"/>:
                                </dt>
                                <dd> ${asientoRegistralSir.codigoUnidadTramitacionOrigen}
                                    <c:if test="${not empty asientoRegistralSir.decodificacionUnidadTramitacionOrigen}">
                                        - ${asientoRegistralSir.decodificacionUnidadTramitacionOrigen}
                                    </c:if>
                                </dd>
                            </c:if>

                            <dt><i class="fa fa-home"></i> <spring:message code="asientoRegistralSir.oficinaOrigen"/>:
                            </dt>
                            <dd> ${asientoRegistralSir.codigoEntidadRegistralOrigen}
                                <c:if test="${not empty asientoRegistralSir.decodificacionEntidadRegistralOrigen}">
                                    - ${asientoRegistralSir.decodificacionEntidadRegistralOrigen}
                                </c:if>
                            </dd>

                            <c:if test="${not empty asientoRegistralSir.codigoUnidadTramitacionDestino}">
                                <dt><i class="fa fa-institution"></i> <spring:message code="asientoRegistralSir.unidadDestino"/>:
                                </dt>
                                <dd> ${asientoRegistralSir.codigoUnidadTramitacionDestino}
                                    <c:if test="${not empty asientoRegistralSir.decodificacionUnidadTramitacionDestino}">
                                        - ${asientoRegistralSir.decodificacionUnidadTramitacionDestino}
                                    </c:if>
                                </dd>
                            </c:if>

                            <c:if test="${not empty asientoRegistralSir.codigoUnidadTramitacionDestino}">
                                <dt><i class="fa fa-home"></i> <spring:message code="asientoRegistralSir.oficinaDestino"/>:
                                </dt>
                                <dd> ${asientoRegistralSir.codigoEntidadRegistralDestino}
                                    <c:if test="${not empty asientoRegistralSir.decodificacionEntidadRegistralDestino}">
                                        - ${asientoRegistralSir.decodificacionEntidadRegistralDestino}
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.numeroRegistro}">
                                <dt><i class="fa fa-barcode"></i> <spring:message code="asientoRegistralSir.numeroRegistro"/>: </dt>
                                <dd>${asientoRegistralSir.numeroRegistro}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.fechaRegistro}">
                                <dt><i class="fa fa-clock-o"></i> <spring:message code="asientoRegistralSir.fechaRegistro"/>: </dt>
                                <dd><fmt:formatDate value="${asientoRegistralSir.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            </c:if>
                            <dt><i class="fa fa-file-o"></i> <spring:message code="asientoRegistralSir.tipoRegistro"/>:
                            </dt>
                            <c:if test="${asientoRegistralSir.tipoRegistro == 'ENTRADA'}">
                                <dd><span class="label label-info"><spring:message code="asientoRegistralSir.entrada"/></span></dd>
                            </c:if>
                            <c:if test="${asientoRegistralSir.tipoRegistro == 'SALIDA'}">
                                <dd><span class="label label-danger"><spring:message code="asientoRegistralSir.salida"/></span></dd>
                            </c:if>


                            <c:if test="${not empty asientoRegistralSir.nombreUsuario}">
                                <dt><i class="fa fa-user"></i> <spring:message code="usuario.usuario"/>: </dt>
                                <dd> ${asientoRegistralSir.nombreUsuario}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.contactoUsuario}">
                                <dt><i class="fa fa-at"></i> <spring:message code="asientoRegistralSir.contacto"/>: </dt>
                                <dd> ${asientoRegistralSir.contactoUsuario}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.identificadorIntercambio}">
                                <dt><i class="fa fa-qrcode"></i> <spring:message code="asientoRegistralSir.identificadorIntercambio"/>: </dt>
                                <dd> ${asientoRegistralSir.identificadorIntercambio}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.tipoAnotacion}">
                                <dt><i class="fa fa-map-marker"></i> <spring:message code="asientoRegistralSir.tipoAnotacion"/>: </dt>
                                <dd>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == '01'}">
                                        <span class="label label-warning"><spring:message
                                                code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}"/></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == '02'}">
                                        <span class="label label-success"><spring:message
                                                code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}"/></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == '03'}">
                                        <span class="label label-info"><spring:message
                                                code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}"/></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == '04'}">
                                        <span class="label label-danger"><spring:message
                                                code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}"/></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.decodificacionTipoAnotacion}">
                                <dt><i class="fa fa-newspaper-o"></i> <spring:message code="asientoRegistralSir.descripcionTipoAnotacion"/>: </dt>
                                <dd> ${asientoRegistralSir.decodificacionTipoAnotacion}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.indicadorPrueba}">
                                <dt><i class="fa fa-tag"></i> <spring:message code="asientoRegistralSir.indicadorPrueba"/>: </dt>
                                <dd>
                                    <c:if test="${asientoRegistralSir.indicadorPrueba == 'NORMAL'}">
                                        <span class="label label-success"><spring:message
                                                code="asientoRegistralSir.indicadorPrueba.normal"/></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.indicadorPrueba == 'PRUEBA'}">
                                        <span class="label label-danger"><spring:message
                                                code="asientoRegistralSir.indicadorPrueba.prueba"/></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.documentacionFisica}">
                                <dt><i class="fa fa-file"></i> <spring:message code="asientoRegistralSir.tipoDocumentacionFisica"/>: </dt>
                                <dd><spring:message code="tipoDocumentacionFisica.${asientoRegistralSir.documentacionFisica}"/></dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.resumen}">
                                <dt><i class="fa fa-file-text-o"></i> <spring:message code="asientoRegistralSir.extracto"/>: </dt>
                                <dd> ${asientoRegistralSir.resumen}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.codigoAsunto}">
                                <dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt>
                                <dd> ${asientoRegistralSir.codigoAsunto}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.referenciaExterna}">
                                <dt><i class="fa fa-thumb-tack"></i> <spring:message code="asientoRegistralSir.referenciaExterna"/>: </dt>
                                <dd> ${asientoRegistralSir.referenciaExterna}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.numeroExpediente}">
                                <dt><i class="fa fa-newspaper-o"></i> <spring:message
                                        code="asientoRegistralSir.expediente"/>:
                                </dt>
                                <dd> ${asientoRegistralSir.numeroExpediente}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.tipoTransporte}">
                                <dt><i class="fa fa-bus"></i> <spring:message code="asientoRegistralSir.transporte"/>:
                                </dt>
                                <dd><spring:message
                                        code="transporte.${asientoRegistralSir.tipoTransporte}"/> ${asientoRegistralSir.numeroTransporte}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.observacionesApunte}">
                                <dt><i class="fa fa-file-text-o"></i> <spring:message code="asientoRegistralSir.observaciones"/>: </dt>
                                <dd> ${asientoRegistralSir.observacionesApunte}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.expone}">
                                <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>: </dt>
                                <dd> ${asientoRegistralSir.expone}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.solicita}">
                                <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>: </dt>
                                <dd> ${asientoRegistralSir.solicita}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.estado}">
                                <dt><i class="fa fa-bookmark"></i> <spring:message code="asientoRegistralSir.estado"/>: </dt>
                                <dd>
                                    <c:if test="${asientoRegistralSir.estado == 'PENDIENTE_ENVIO' || asientoRegistralSir.estado == 'DEVUELTO' || asientoRegistralSir.estado == 'RECIBIDO' || asientoRegistralSir.estado == 'REINTENTAR_VALIDACION'}">
                                        <span class="label label-warning"><spring:message
                                                code="asientoRegistralSir.estado.${asientoRegistralSir.estado}"/></span>
                                    </c:if>

                                    <c:if test="${asientoRegistralSir.estado == 'ENVIADO' || asientoRegistralSir.estado == 'ENVIADO_Y_ACK' || asientoRegistralSir.estado == 'ACEPTADO' || asientoRegistralSir.estado == 'REENVIADO' || asientoRegistralSir.estado == 'REENVIADO_Y_ACK' || asientoRegistralSir.estado == 'VALIDADO'}">
                                        <span class="label label-success"><spring:message
                                                code="asientoRegistralSir.estado.${asientoRegistralSir.estado}"/></span>
                                    </c:if>

                                    <c:if test="${asientoRegistralSir.estado == 'ENVIADO_Y_ERROR' || asientoRegistralSir.estado == 'REENVIADO_Y_ERROR' || asientoRegistralSir.estado == 'ANULADO' || asientoRegistralSir.estado == 'RECHAZADO' || asientoRegistralSir.estado == 'RECHAZADO_Y_ACK' ||asientoRegistralSir.estado == 'RECHAZADO_Y_ERROR'}">
                                        <span class="label label-danger"><spring:message
                                                code="asientoRegistralSir.estado.${asientoRegistralSir.estado}"/></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.fechaEstado}">
                                <dt><i class="fa fa-clock-o"></i> <spring:message code="asientoRegistralSir.fechaEstado"/>: </dt>
                                <dd><fmt:formatDate value="${asientoRegistralSir.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.numeroReintentos}">
                                <dt><i class="fa fa-retweet"></i> <spring:message code="oficioRemision.reintentos"/>: </dt>
                                <dd> ${asientoRegistralSir.numeroReintentos}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.codigoError}">
                                <dt><i class="fa fa-bug"></i> <spring:message code="asientoRegistralSir.codigoError"/>: </dt>
                                <dd> ${asientoRegistralSir.codigoError}</dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.descripcionError}">
                                <dt><i class="fa fa-comment"></i> <spring:message code="asientoRegistralSir.descripcionError"/>: </dt>
                                <dd> ${asientoRegistralSir.descripcionError}</dd>
                            </c:if>

                        </dl>

                    </div>

                    <form:form modelAttribute="registrarForm" action="${urlAceptar}" method="post" cssClass="form-horizontal">

                    <%-- Se muestra la Botonera si el AsientoRegistralSir está pendiente de procesar--%>
                    <c:if test="${asientoRegistralSir.estado == 'RECIBIDO'}">

                        <%--Formulari per completar dades del registre--%>
                        <div class="panel-footer">

                            <div class="panel-heading">
                                <h3 class="panel-title">
                                    <strong><spring:message code="registro.completar"/></strong>
                                </h3>
                            </div>

                            <%--Si s'ha de triar llibre--%>
                            <c:if test="${fn:length(libros) > 1}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                                        <label for="idLibro"><span class="text-danger">*</span> <spring:message
                                                code="libro.libro"/></label>
                                    </div>
                                    <div class="col-xs-7 no-pad-right" id="libro">
                                        <form:select path="idLibro" cssClass="chosen-select">
                                            <form:options items="${libros}" itemValue="id" itemLabel="nombre"/>
                                        </form:select>
                                        <span class="errors"></span>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${fn:length(libros) == 1}">
                                <form:hidden path="idLibro" value="${libros[0].id}"/>
                            </c:if>

                            <%--Si hay al menos un Libro, podemos aceptar el AsientoRegistalSir--%>
                            <c:if test="${fn:length(libros) >= 1}">

                                <%--Idioma--%>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                                        <label for="idIdioma"><span class="text-danger">*</span> <spring:message
                                                code="registroEntrada.idioma"/></label>
                                    </div>
                                    <div class="col-xs-7 no-pad-right" id="idioma">
                                        <form:select path="idIdioma" cssClass="chosen-select">
                                            <c:forEach items="${idiomas}" var="idioma">
                                                <c:if test="${idioma == RegwebConstantes.IDIOMA_CASTELLANO_ID}">
                                                    <form:option value="${idioma}" selected="selected"><spring:message
                                                            code="idioma.${idioma}"/></form:option>
                                                </c:if>
                                                <c:if test="${idioma != RegwebConstantes.IDIOMA_CASTELLANO_ID}">
                                                    <form:option value="${idioma}"><spring:message
                                                            code="idioma.${idioma}"/></form:option>
                                                </c:if>
                                            </c:forEach>
                                        </form:select>
                                        <span class="errors"></span>
                                    </div>
                                </div>

                                <%--TipoAsunto--%>

                                <div class="form-group col-xs-12">
                                    <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                                        <label for="idTipoAsunto"><span class="text-danger">*</span> <spring:message
                                                code="registroEntrada.tipoAsunto"/></label>
                                    </div>
                                    <div class="col-xs-7 no-pad-right" id="tipoAsunto">
                                        <form:select path="idTipoAsunto" cssClass="chosen-select">
                                            <form:option value="-1">...</form:option>
                                            <form:options items="${tiposAsunto}" itemValue="id" itemLabel="traduccion.nombre"/>
                                        </form:select>
                                        <span class="errors"></span>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-xs-12 list-group-item-heading">
                                        <button type="button" class="btn btn-success btn-sm btn-block"
                                                onclick="aceptarAsientoRegistralSir()"><spring:message
                                                code="asientoRegistralSir.aceptar"/></button>
                                    </div>
                                </div>
                                <c:set var="errorObligatori"><spring:message code="error.valor.requerido"/></c:set>
                                <input id="error" type="hidden" value="${errorObligatori}"/>

                            </c:if>

                            <%--Si no hay Libros no podremos aceptar el AsientoRegistralSir--%>
                            <c:if test="${empty libros}">
                                No existe ningún Libro de <c:if test="${not empty asientoRegistralSir.decodificacionUnidadTramitacionDestino}"> ${asientoRegistralSir.decodificacionUnidadTramitacionDestino}</c:if>.
                                No se podrá aceptar el AsientoRegistralSir.
                            </c:if>

                        </div>

                        <div class="panel-footer">

                            <%--Botón Reenviar--%>
                            <c:if test="${puedeReenviar}">
                                <div class="btn-group">
                                    <a class="btn btn-warning btn-sm" href="<c:url value="/asientoRegistralSir/${asientoRegistralSir.id}/reenviar"/>" role="button"><spring:message code="asientoRegistralSir.estado.reenviar"/></a>
                                </div>
                            </c:if>

                            <%--Rechazar--%>
                            <div class="btn-group">
                                <a data-toggle="modal" role="button" href="#rechazoModal"
                                   onclick="limpiarModalRechazo();"
                                   class="btn btn-danger btn-sm"><spring:message code="asientoRegistralSir.estado.rechazar"/></a>
                            </div>
                        </div>
                    </c:if>
                </div>

            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <%--INTERESADOS--%>
            <c:import url="interesadosSir.jsp"/>

            <!-- ANEXOS -->
            <c:import url="anexosSir.jsp"/>
            </form:form>
                <%--TRAZABILIDAD--%>
            <c:if test="${not empty trazabilidades}">
                <c:import url="../trazabilidad/trazabilidadSir.jsp"/>
            </c:if>


        </div><!-- /div.row-->


        <%--Modal Rechazo--%>
        <div id="rechazoModal" class="modal fade bs-example-modal-lg">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                        <h3>Rechazar Asiento Registral Sir ${asientoRegistralSir.numeroRegistro}</h3>
                    </div>

                    <div class="modal-body">
                        <c:url value="/asientoRegistralSir/rechazar/${asientoRegistralSir.id}" var="urlRechazar" scope="request"/>
                        <form:form modelAttribute="rechazarForm" method="post" action="${urlRechazar}" cssClass="form-horizontal">
                            <div class="panel panel-success">

                                <div class="panel-heading">
                                    <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i>
                                        <strong>Introduzca el motivo del rechazo</strong></h3>
                                </div>

                                <div class="panel-body">

                                    <div class="form-group col-xs-12">
                                        <div class="col-xs-4 pull-left etiqueta_regweb_left control-label">
                                            <form:label for="observacionesRechazo" path="observacionesRechazo"> Observaciones</form:label>
                                        </div>
                                        <div class="col-xs-8" id="observacionesRechazoSir">
                                            <form:textarea path="observacionesRechazo" rows="5" cssClass="form-control"/> <span class="errors"></span>
                                        </div>
                                    </div>

                                </div> <!-- /.panel body -->
                            </div>
                            <!-- /.panel panel-info -->
                            <div class="form-actions">
                                <input type="submit" value="<spring:message code="asientoRegistralSir.estado.rechazar"/>"
                                       class="btn btn-danger btn-sm"
                                       onclick="return rechazarAsientoRegistralSir()">
                            </div>
                        </form:form>

                    </div>
                    <div class="modal-footer">
                        <button class="btn" data-dismiss="modal" aria-hidden="true"><spring:message
                                code="regweb.cerrar"/></button>
                    </div>
                </div>
            </div>
        </div>

        <%--<c:url value="/asientoRegistralSir/reenviar/${asientoRegistralSir.id}" var="urlReenviar" scope="request"/>
      <form:form modelAttribute="reenviarForm" method="post" action="${urlReenviar}" cssClass="form-horizontal">
           <form:hidden path="codigoOficina" value=""/>
           <form:hidden path="denominacionOficina" value=""/>
           <form:hidden path="codigoOrganismoResponsable" value=""/>
           <form:hidden path="denominacionOrganismoResponsable" value=""/>
       </form:form>--%>

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

<!-- Importamos el codigo jsp del modal del formulario para realizar la busqueda de organismos Origen
                 Mediante el archivo "busquedaorganismo.js" se implementa dicha búsqueda -->
<c:import url="../registro/buscadorOrganismosOficinasREPestanas.jsp">
    <c:param name="tipo" value="OficinaSir"/>
</c:import>
<script type="text/javascript" src="<c:url value="/js/busquedaorganismo.js"/>"></script>

<script type="application/javascript">
    // Realiza el Registro de un AsientoRegistralSir
    function aceptarAsientoRegistralSir() {
        var libro = true;
        var idioma = true;
        var tipoAsunto = true;
        var idLibro = $('#idLibro').val();
        var idIdioma = $('#idIdioma').val();
        var idTipoAsunto = $('#idTipoAsunto').val();

        // Valida que haya un libro elegido
        if (!validaCampo(idLibro, 'libro')) {
            libro = false;
        }
        // Valida que haya un idioma elegido
        if (!validaCampo(idIdioma, 'idioma')) {
            idioma = false;
        }
        // Valida que haya un tipoAsunto elegido
        if (!validaSelect(idTipoAsunto, 'tipoAsunto')) {
            tipoAsunto = false;
        }
        // Mira si todos los campos son correctos
        if ((libro) && (idioma) && (tipoAsunto)) {

            //$( "#registrarForm" ).submit();
            doForm('#registrarForm');
            //return true;
        } else {
            return false;
        }
    }

    function rechazarAsientoRegistralSir() {
        var observaciones = $('#observacionesRechazo').val();

        if(observaciones == ""){
            var variable = "#observacionesRechazoSir span.errors";
            var formatoHtml = "<span id='observaciones.errors' class='help-block'>El campo es obligatorio</span>";
            $(variable).html(formatoHtml);
            $(variable).parents(".form-group").addClass("has-error");
            return false;
        }else{
            doForm('#rechazarForm');
        }
    }

    function limpiarModalRechazo(){
        $('#observaciones').val("");
        var variable = "#observacionesRechazoSir span.errors";
        var formatoHtml = "<span id='observaciones.errors' class='help-block'></span>";
        $(variable).html(formatoHtml);
        $(variable).parents(".form-group").removeClass("has-error");
    }

</script>

</body>
</html>