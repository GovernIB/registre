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
                    <li><a href="<c:url value="/inici"/>"><i class="fa fa-globe"></i> ${oficinaActiva.denominacion}</a></li>
                    <li><a href="<c:url value="/asientoRegistralSir/list"/>" ><i class="fa fa-list"></i> <spring:message code="asientoRegistralSir.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="asientoRegistralSir.asientoRegistralSir"/> <fmt:formatDate value="${asientoRegistralSir.fechaRegistro}" pattern="dd/MM/yyyy"/> / ${asientoRegistralSir.numeroRegistro}</li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="asientoRegistralSir.asientosRegistralesSir"/> <fmt:formatDate value="${asientoRegistralSir.fechaRegistro}" pattern="dd/MM/yyyy"/> / ${asientoRegistralSir.numeroRegistro}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <dl class="detalle_registro">

                                <dt><i class="fa fa-briefcase"></i> <spring:message code="asientoRegistralSir.oficinaDestino"/>:
                                </dt>
                                <dd> ${asientoRegistralSir.codigoEntidadRegistralDestino}
                                    <c:if test="${not empty asientoRegistralSir.decodificacionEntidadRegistralDestino}">
                                        ${asientoRegistralSir.decodificacionEntidadRegistralDestino}
                                    </c:if>
                                </dd>

                                <dt><i class="fa fa-file-o"></i> <spring:message code="asientoRegistralSir.tipoRegistro"/>:</dt>
                                <c:if test="${asientoRegistralSir.tipoRegistro == '0'}">
                                    <dd><span class="label label-info"><spring:message code="asientoRegistralSir.entrada"/></span></dd>
                                </c:if>
                                <c:if test="${asientoRegistralSir.tipoRegistro == '1'}">
                                    <dd><span class="label label-danger"><spring:message code="asientoRegistralSir.salida"/></span></dd>
                                </c:if>

                            <c:if test="${not empty asientoRegistralSir.fechaRegistro}"><dt><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${asientoRegistralSir.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.nombreUsuario}"><dt><i class="fa fa-user"></i> <spring:message code="usuario.usuario"/>: </dt> <dd> ${asientoRegistralSir.nombreUsuario}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.contactoUsuario}"><dt><i class="fa fa-at"></i> <spring:message code="asientoRegistralSir.contacto"/>: </dt> <dd> ${asientoRegistralSir.contactoUsuario}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.estado}"><dt><i class="fa fa-bookmark"></i> <spring:message code="asientoRegistralSir.estado"/>: </dt>
                            <dd>
                                <c:if test="${asientoRegistralSir.estado == 1}">
                                    <span class="label label-warning"><spring:message code="asientoRegistralSir.estado.${asientoRegistralSir.estado}" /></span>
                                </c:if>
                                <c:if test="${asientoRegistralSir.estado == 2}">
                                    <span class="label label-success"><spring:message code="asientoRegistralSir.estado.${asientoRegistralSir.estado}" /></span>
                                </c:if>
                                <c:if test="${asientoRegistralSir.estado == 3}">
                                    <span class="label label-danger"><spring:message code="asientoRegistralSir.estado.${asientoRegistralSir.estado}" /></span>
                                </c:if>
                            </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.identificadorIntercambio}"><dt><i class="fa fa-qrcode"></i> <spring:message code="asientoRegistralSir.identificadorIntercambio"/>: </dt> <dd> ${asientoRegistralSir.identificadorIntercambio}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.tipoAnotacion}"><dt><i class="fa fa-map-marker"></i> <spring:message code="asientoRegistralSir.tipoAnotacion"/>: </dt>
                                <dd>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_PENDIENTE}">
                                        <span class="label label-warning"><spring:message code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}" /></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_ENVIO}">
                                        <span class="label label-success"><spring:message code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}" /></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_REENVIO}">
                                        <span class="label label-info"><spring:message code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}" /></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_RECHAZO}">
                                        <span class="label label-danger"><spring:message code="asientoRegistralSir.tipoAnotacion.${asientoRegistralSir.tipoAnotacion}" /></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.decodificacionTipoAnotacion}"><dt><i class="fa fa-newspaper-o"></i> <spring:message code="asientoRegistralSir.descripcionTipoAnotacion"/>: </dt> <dd> ${asientoRegistralSir.decodificacionTipoAnotacion}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.indicadorPrueba}"><dt><i class="fa fa-tag"></i> <spring:message code="asientoRegistralSir.indicadorPrueba"/>: </dt>
                                <dd>
                                    <c:if test="${asientoRegistralSir.indicadorPrueba == '0'}">
                                        <span class="label label-success"><spring:message code="asientoRegistralSir.indicadorPrueba.normal"/></span>
                                    </c:if>
                                    <c:if test="${asientoRegistralSir.indicadorPrueba == '1'}">
                                        <span class="label label-danger"><spring:message code="asientoRegistralSir.indicadorPrueba.prueba"/></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty asientoRegistralSir.documentacionFisica}"><dt><i class="fa fa-file"></i> <spring:message code="asientoRegistralSir.tipoDocumentacionFisica"/>: </dt> <dd> <spring:message code="tipoDocumentacionFisica.${asientoRegistralSir.tipoDocumentacionFisica}"/></dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.resumen}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="asientoRegistralSir.extracto"/>: </dt> <dd> ${asientoRegistralSir.resumen}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.codigoAsunto}"><dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt> <dd> ${asientoRegistralSir.codigoAsunto}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.referenciaExterna}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="asientoRegistralSir.referenciaExterna"/>: </dt> <dd> ${asientoRegistralSir.referenciaExterna}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.numeroExpediente}"> <dt><i class="fa fa-newspaper-o"></i> <spring:message code="asientoRegistralSir.expediente"/>: </dt> <dd> ${asientoRegistralSir.numeroExpediente}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.tipoTransporte}"> <dt><i class="fa fa-bus"></i> <spring:message code="asientoRegistralSir.transporte"/>: </dt> <dd> <spring:message code="transporte.${asientoRegistralSir.tipoTransporte}" /> ${registro.numeroTransporte}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.observacionesApunte}"> <dt><i class="fa fa-file-text-o"></i> <spring:message code="asientoRegistralSir.observaciones"/>: </dt> <dd> ${asientoRegistralSir.observacionesApunte}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.expone}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>: </dt> <dd> ${asientoRegistralSir.expone}</dd></c:if>
                            <c:if test="${not empty asientoRegistralSir.solicita}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>: </dt> <dd> ${asientoRegistralSir.solicita}</dd></c:if>
                        </dl>

                    </div>

                    <%-- Se muestra la Botonera si el AsientoRegistralSir está pendiente de procesar--%>
                    <c:if test="${asientoRegistralSir.estado==1}">

                        <div class="panel-footer">  <%--Formulari per completar dades del registre--%>
                            <c:if test="${(fn:length(libros) > 1)||(empty asientoRegistralSir.idioma)||(empty asientoRegistralSir.idioma)}">
                                <div class="panel-heading">
                                    <h3 class="panel-title">
                                        <strong><spring:message code="registro.completar"/></strong>
                                    </h3>
                                </div>
                            </c:if>
                            <form:form modelAttribute="registrarForm" method="post" cssClass="form-horizontal">
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
                                <c:if test ="${fn:length(libros) == 1}">
                                    <input id="idLibro" type="hidden" value="${libros[0].id}"/>
                                </c:if>

                                <%--Si s'ha de posar valor per l'idioma--%>
                                <c:if test="${empty asientoRegistralSir.idioma}">
                                    <div class="form-group col-xs-12">
                                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                                            <label for="idIdioma"><span class="text-danger">*</span> <spring:message
                                                    code="registroEntrada.idioma"/></label>
                                        </div>
                                        <div class="col-xs-7 no-pad-right" id="idioma">
                                            <form:select path="idIdioma" cssClass="chosen-select">
                                                <c:forEach items="${idiomas}" var="idioma">
                                                    <c:if test="${idioma == RegwebConstantes.IDIOMA_CASTELLANO_ID}">
                                                        <form:option value="${idioma}" selected="selected"><spring:message code="idioma.${idioma}"/></form:option>
                                                    </c:if>
                                                    <c:if test="${idioma != RegwebConstantes.IDIOMA_CASTELLANO_ID}">
                                                        <form:option value="${idioma}"><spring:message code="idioma.${idioma}"/></form:option>
                                                    </c:if>
                                                </c:forEach>
                                            </form:select>
                                            <span class="errors"></span>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test ="${not empty asientoRegistralSir.idioma}">
                                    <input id="idIdioma" type="hidden" value="${asientoRegistralSir.idioma}"/>
                                </c:if>

                                <%--Si s'ha de posar valor per tipoAsunto--%>
                                <c:if test="${empty asientoRegistralSir.tipoAsunto}">
                                    <div class="form-group col-xs-12">
                                        <div class="col-xs-5 pull-left etiqueta_regweb control-label">
                                            <label for="idTipoAsunto"><span class="text-danger">*</span> <spring:message
                                                    code="registroEntrada.tipoAsunto"/></label>
                                        </div>
                                        <div class="col-xs-7 no-pad-right" id="tipoAsunto">
                                            <form:select path="idTipoAsunto"  cssClass="chosen-select">
                                                <form:option value="-1">...</form:option>
                                                <form:options items="${tiposAsunto}" itemValue="id" itemLabel="traduccion.nombre"/>
                                            </form:select>
                                            <span class="errors"></span>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test ="${not empty asientoRegistralSir.tipoAsunto}">
                                    <input id="idTipoAsunto" type="hidden" value="${asientoRegistralSir.tipoAsunto.id}"/>
                                </c:if>

                                <div class="row">
                                    <div class="col-xs-12 list-group-item-heading">
                                        <button type="button" class="btn btn-success btn-sm btn-block" onclick="registrarAsientoRegistralSir('<c:url value="/asientoRegistralSir/${asientoRegistralSir.id}/registrar/"/>')"><spring:message code="asientoRegistralSir.estado.registrar"/></button>
                                    </div>
                                </div>
                                <c:set var="errorObligatori"><spring:message code="error.valor.requerido"/></c:set>
                                <input id="error" type="hidden" value="${errorObligatori}"/>
                            </form:form>
                        </div>

                        <div class="panel-footer">  <%--Botonera--%>
                            <button type="button" onclick="goTo('/asientoRegistralSir/${asientoRegistralSir.id}/rechazar')" class="btn btn-danger btn-sm btn-block"><spring:message code="asientoRegistralSir.estado.rechazar"/></button>
                            <button type="button" onclick="goTo('/asientoRegistralSir/${asientoRegistralSir.id}/reenviar')" class="btn btn-info btn-sm btn-block"><spring:message code="asientoRegistralSir.estado.reenviar"/></button>
                        </div>
                    </c:if>

                </div>

            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <%--PRE REGISTRE INICI--%>
            <div class="col-xs-8 col-xs-offset">

                <div class="panel panel-warning">

                    <div class="panel-heading">

                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                                code="asientoRegistralSir.asientoRegistralSirOrigen"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <div class="col-xs-12">
                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_REENVIO}">
                                            <col>
                                        </c:if>
                                        <col>
                                        <col>
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="asientoRegistralSir.numeroOrigen"/></th>
                                        <th><spring:message code="asientoRegistralSir.fechaOrigen"/></th>
                                        <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_REENVIO}">
                                            <th><spring:message code="asientoRegistralSir.oficinaInicio"/></th>
                                        </c:if>
                                        <th><spring:message code="asientoRegistralSir.unidadOrigen"/></th>
                                        <th><spring:message code="asientoRegistralSir.oficinaOrigen"/></th>

                                    </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td>${asientoRegistralSir.numeroRegistroOrigen}</td>
                                            <td><fmt:formatDate value="${asientoRegistralSir.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                            <c:if test="${asientoRegistralSir.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_REENVIO}">
                                                <td>${asientoRegistralSir.codigoEntidadRegistralInicio}</td>
                                            </c:if>
                                            <td>${asientoRegistralSir.codigoUnidadTramitacionOrigen}
                                                - ${asientoRegistralSir.decodificacionUnidadTramitacionOrigen}</td>
                                            <td>
                                                <c:if test="${not empty asientoRegistralSir.oficinaOrigen}">${asientoRegistralSir.oficinaOrigen.nombreCompleto}</c:if>
                                                <c:if test="${not empty asientoRegistralSir.oficinaOrigenExternoCodigo}">${asientoRegistralSir.oficinaOrigenExternoCodigo} - ${asientoRegistralSir.oficinaOrigenExternoDenominacion}</c:if>
                                            </td>

                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ANEXOS -->
            <div class="col-xs-8 pull-right">

                <div class="panel panel-warning">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                                code="anexo.anexos"/></strong>: <spring:message
                                code="tipoDocumentacionFisica.${asientoRegistralSir.tipoDocumentacionFisica}"/>
                        </h3>
                    </div>

                    <div class="panel-body">
                        <div class="col-xs-12">
                            <div id="anexosdiv" class="table-responsive">

                                <c:if test="${empty anexos}">
                                    <div class="alert alert-warning alert-dismissable">
                                        <spring:message code="regweb.listado.vacio"/> <spring:message code="anexo.anexo"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty anexos}">
                                    <table id="anexos" class="table table-bordered table-hover table-striped">
                                        <colgroup>
                                            <col>
                                            <col>
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="anexo.titulo"/></th>
                                            <th><spring:message code="anexo.tipoDocumento"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="anexo" items="${anexos}">
                                            <tr id="anexo${anexo.id}">
                                                <td>${anexo.titulo}</td>
                                                <td><spring:message code="tipoDocumento.${anexo.tipoDocumento}"/></td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>

            </div>


            <%--INTERESADOS--%>
            <div class="col-xs-8 pull-right">
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="interesado.interesados"/></strong></h3>
                    </div>
                    <div class="panel-body">
                        <div class="col-xs-12">
                            <div class="table-responsive">
                                <c:if test="${empty asientoRegistralSir.interesados}">
                                    <div class="alert alert-warning ">
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="registroEntrada.interesado"/></strong>
                                    </div>
                                </c:if>
                                <c:if test="${not empty asientoRegistralSir.interesados}">
                                    <table id="interesados" class="table table-bordered table-hover table-striped">
                                        <colgroup>
                                            <col>
                                            <col>
                                            <col>
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroEntrada.interesado"/></th>
                                            <th><spring:message code="interesado.tipoInteresado"/></th>
                                            <th><spring:message code="representante.representante"/></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="interesado" items="${asientoRegistralSir.interesados}">
                                            <tr>
                                                <td>
                                                    <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}">${interesado.nombreOrganismo} </c:if>
                                                    <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}">${interesado.nombrePersonaFisica} </c:if>
                                                    <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}">${interesado.nombrePersonaJuridica} </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION}"><spring:message code="organismo.organismo"/></c:if>
                                                    <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA}"><spring:message code="persona.fisica"/></c:if>
                                                    <c:if test="${interesado.tipo == RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA}"><spring:message code="persona.juridica"/></c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${interesado.isRepresentante}">
                                                        <span class="label label-success">Si, Representado: ${interesado.representado.nombreCompleto}</span>
                                                    </c:if>

                                                    <c:if test="${!interesado.isRepresentante}">
                                                        <span class="label label-danger">No</span>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div><!-- /div.row-->

    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>

</body>
</html>