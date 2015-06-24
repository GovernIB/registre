<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>
<un:useConstants var="RegwebConstantes" className="es.caib.regweb3.utils.RegwebConstantes"/>
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
                    <li><a href="<c:url value="/preRegistro/list"/>" ><i class="fa fa-list"></i> <spring:message code="preRegistro.listado"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="preRegistro.preRegistro"/> <fmt:formatDate value="${preRegistro.fecha}" pattern="yyyy"/> / ${preRegistro.numeroPreregistro}</li>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="preRegistro.preRegistro"/> <fmt:formatDate value="${preRegistro.fecha}" pattern="yyyy"/> / ${preRegistro.numeroPreregistro}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <dl class="detalle_registro">
                            <c:if test="${not empty preRegistro.fecha}"><dt><i class="fa fa-clock-o"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${preRegistro.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd></c:if>
                            <c:if test="${not empty preRegistro.usuario}"><dt><i class="fa fa-user"></i> <spring:message code="usuario.usuario"/>: </dt> <dd> ${preRegistro.usuario}</dd></c:if>
                            <c:if test="${not empty preRegistro.contactoUsuario}"><dt><i class="fa fa-at"></i> <spring:message code="preRegistro.contacto"/>: </dt> <dd> ${preRegistro.contactoUsuario}</dd></c:if>
                            <c:if test="${not empty preRegistro.estado}"><dt><i class="fa fa-bookmark"></i> <spring:message code="preRegistro.estado"/>: </dt>
                            <dd>
                                <c:if test="${preRegistro.estado == 1}">
                                    <span class="label label-warning"><spring:message code="preRegistro.estado.${preRegistro.estado}" /></span>
                                </c:if>
                                <c:if test="${preRegistro.estado == 2}">
                                    <span class="label label-success"><spring:message code="preRegistro.estado.${preRegistro.estado}" /></span>
                                </c:if>
                                <c:if test="${preRegistro.estado == 3}">
                                    <span class="label label-danger"><spring:message code="preRegistro.estado.${preRegistro.estado}" /></span>
                                </c:if>
                            </dd>
                            </c:if>
                            <c:if test="${not empty preRegistro.idIntercambio}"><dt><i class="fa fa-qrcode"></i> <spring:message code="preRegistro.identificadorIntercambio"/>: </dt> <dd> ${preRegistro.idIntercambio}</dd></c:if>
                            <c:if test="${not empty preRegistro.tipoAnotacion}"><dt><i class="fa fa-map-marker"></i> <spring:message code="preRegistro.tipoAnotacion"/>: </dt>
                                <dd>
                                    <c:if test="${preRegistro.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_PENDIENTE}">
                                        <span class="label label-warning"><spring:message code="preRegistro.tipoAnotacion.${preRegistro.tipoAnotacion}" /></span>
                                    </c:if>
                                    <c:if test="${preRegistro.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_ENVIO}">
                                        <span class="label label-success"><spring:message code="preRegistro.tipoAnotacion.${preRegistro.tipoAnotacion}" /></span>
                                    </c:if>
                                    <c:if test="${preRegistro.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_REENVIO}">
                                        <span class="label label-info"><spring:message code="preRegistro.tipoAnotacion.${preRegistro.tipoAnotacion}" /></span>
                                    </c:if>
                                    <c:if test="${preRegistro.tipoAnotacion == RegwebConstantes.TIPO_ANOTACION_RECHAZO}">
                                        <span class="label label-danger"><spring:message code="preRegistro.tipoAnotacion.${preRegistro.tipoAnotacion}" /></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty preRegistro.descripcionTipoAnotacion}"><dt><i class="fa fa-newspaper-o"></i> <spring:message code="preRegistro.descripcionTipoAnotacion"/>: </dt> <dd> ${preRegistro.descripcionTipoAnotacion}</dd></c:if>
                            <c:if test="${not empty preRegistro.indicadorPrueba}"><dt><i class="fa fa-tag"></i> <spring:message code="preRegistro.indicadorPrueba"/>: </dt>
                                <dd>
                                    <c:if test="${preRegistro.indicadorPrueba == '0'}">
                                        <span class="label label-success"><spring:message code="preRegistro.indicadorPrueba.normal"/></span>
                                    </c:if>
                                    <c:if test="${preRegistro.indicadorPrueba == '1'}">
                                        <span class="label label-danger"><spring:message code="preRegistro.indicadorPrueba.prueba"/></span>
                                    </c:if>
                                </dd>
                            </c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.tipoDocumentacionFisica}"><dt><i class="fa fa-file"></i> <spring:message code="preRegistro.tipoDocumentacionFisica"/>: </dt> <dd> <spring:message code="tipoDocumentacionFisica.${preRegistro.registroDetalle.tipoDocumentacionFisica}"/></dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.extracto}"><dt><i class="fa fa-file-text-o"></i> <spring:message code="preRegistro.extracto"/>: </dt> <dd> ${preRegistro.registroDetalle.extracto}</dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.tipoAsunto}"><dt><i class="fa fa-thumb-tack"></i> <spring:message code="tipoAsunto.tipoAsunto"/>: </dt> <dd> <i:trad value="${preRegistro.registroDetalle.tipoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.idioma}"><dt><i class="fa fa-bullhorn"></i> <spring:message code="preRegistro.idioma"/>: </dt> <dd> <spring:message code="idioma.${preRegistro.registroDetalle.idioma}"/></dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.codigoAsunto}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="codigoAsunto.codigoAsunto"/>: </dt> <dd> <i:trad value="${preRegistro.registroDetalle.codigoAsunto}" property="nombre"/></dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.referenciaExterna}"> <dt><i class="fa fa-thumb-tack"></i> <spring:message code="preRegistro.referenciaExterna"/>: </dt> <dd> ${preRegistro.registroDetalle.referenciaExterna}</dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.expediente}"> <dt><i class="fa fa-newspaper-o"></i> <spring:message code="preRegistro.expediente"/>: </dt> <dd> ${preRegistro.registroDetalle.expediente}</dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.transporte}"> <dt><i class="fa fa-bus"></i> <spring:message code="preRegistro.transporte"/>: </dt> <dd> <spring:message code="transporte.${preRegistro.registroDetalle.transporte}" /> ${registro.registroDetalle.numeroTransporte}</dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.observaciones}"> <dt><i class="fa fa-file-text-o"></i> <spring:message code="preRegistro.observaciones"/>: </dt> <dd> ${preRegistro.registroDetalle.observaciones}</dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.expone}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.expone"/>: </dt> <dd> ${preRegistro.registroDetalle.expone}</dd></c:if>
                            <c:if test="${not empty preRegistro.registroDetalle.solicita}"> <dt><i class="fa fa-hand-o-right"></i> <spring:message code="registroDetalle.solicita"/>: </dt> <dd> ${preRegistro.registroDetalle.solicita}</dd></c:if>
                        </dl>

                    </div>

                    <%-- Se muestra la Botonera si el PreRegistro está pendiente de procesar--%>
                    <c:if test="${preRegistro.estado==1}">
                        <div class="panel-footer">  <%--Botonera--%>
                            <c:if test="${fn:length(libros) > 1}">
                                <form:form modelAttribute="libro" method="post" cssClass="form-horizontal">
                                    <div class="col-xs-12 btn-block">
                                        <div class="col-xs-6 no-pad-lateral list-group-item-heading">
                                            <form:select path="id" cssClass="chosen-select">
                                                <form:options items="${libros}" itemValue="id" itemLabel="nombre"/>
                                            </form:select>
                                        </div>
                                        <div class="col-xs-6 no-pad-right list-group-item-heading">
                                            <button type="button" class="btn btn-success btn-sm btn-block" onclick="registrarPreRegistro('<c:url value="/preRegistro/${preRegistro.id}/registrar/"/>')"><spring:message code="preRegistro.estado.registrar"/></button>
                                        </div>
                                    </div>
                                </form:form>
                            </c:if>
                            <c:if test="${fn:length(libros) == 1}">
                                <button type="button" class="btn btn-success btn-sm btn-block" onclick="goTo('<c:url value="/preRegistro/${preRegistro.id}/registrar/${libros[0].id}"/>')"><spring:message code="preRegistro.estado.registrar"/></button>
                            </c:if>

                            <button type="button" onclick="goTo('/preRegistro/${preRegistro.id}/rechazar')" class="btn btn-danger btn-sm btn-block"><spring:message code="preRegistro.estado.rechazar"/></button>
                            <button type="button" onclick="goTo('/preRegistro/${preRegistro.id}/reenviar')" class="btn btn-info btn-sm btn-block"><spring:message code="preRegistro.estado.reenviar"/></button>
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

                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="preRegistro.preRegistroInicio"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <div class="col-xs-12">
                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <col>
                                        <col>
                                        <col>
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="preRegistro.numeroOrigen"/></th>
                                        <th><spring:message code="preRegistro.fechaOrigen"/></th>
                                        <th><spring:message code="preRegistro.unidadInicio"/></th>
                                        <th><spring:message code="preRegistro.oficinaDestino"/></th>
                                        <th><spring:message code="preRegistro.tipoRegistro"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                        <tr>
                                            <td>${preRegistro.registroDetalle.numeroRegistroOrigen}</td>
                                            <td><fmt:formatDate value="${preRegistro.registroDetalle.fechaOrigen}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                            <td>
                                                <c:if test="${not empty preRegistro.registroDetalle.oficinaOrigen}">${preRegistro.registroDetalle.oficinaOrigen.denominacion}</c:if>
                                                <c:if test="${not empty preRegistro.registroDetalle.oficinaOrigenExternoCodigo}">${preRegistro.registroDetalle.oficinaOrigenExternoDenominacion}</c:if>
                                                <c:if test="${(empty preRegistro.registroDetalle.oficinaOrigenExternoCodigo) && (empty preRegistro.registroDetalle.oficinaOrigen)}">${preRegistro.decodificacionEntidadRegistralInicio}</c:if>
                                            </td>
                                            <td>${preRegistro.decodificacionEntidadRegistralDestino}</td>
                                            <td>
                                                <c:if test="${preRegistro.tipoRegistro == '0'}">
                                                    <span class="label label-info"><spring:message code="preRegistro.entrada"/></span>
                                                </c:if>
                                                <c:if test="${preRegistro.tipoRegistro == '1'}">
                                                    <span class="label label-danger"><spring:message code="preRegistro.salida"/></span>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <%--PRE REGISTRE--%>
            <div class="col-xs-8 col-xs-offset">

                <div class="panel panel-warning">

                    <div class="panel-heading">

                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="preRegistro.preRegistro"/></strong></h3>
                    </div>

                    <div class="panel-body">
                        <div class="col-xs-12">
                            <div class="table-responsive">

                                <table class="table table-bordered table-hover table-striped tablesorter">
                                    <colgroup>
                                        <col>
                                        <col>
                                        <col>
                                        <col>
                                        <col>
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th><spring:message code="preRegistro.numero"/></th>
                                        <th><spring:message code="preRegistro.unidadOrigen"/></th>
                                        <th><spring:message code="preRegistro.oficinaOrigen"/></th>
                                        <th><spring:message code="preRegistro.unidadDestino"/></th>
                                        <th><spring:message code="preRegistro.oficinaDestino"/></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <tr>
                                        <td>${preRegistro.numeroPreregistro}</td>
                                        <td>${preRegistro.decodificacionUnidadTramitacionOrigen}</td>
                                        <td>${preRegistro.decodificacionEntidadRegistralOrigen}</td>
                                        <td>${preRegistro.decodificacionUnidadTramitacionDestino}</td>
                                        <td>${preRegistro.decodificacionEntidadRegistralDestino}</td>
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
                        <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message code="anexo.anexos"/></strong></h3>
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
                                <c:if test="${empty preRegistro.registroDetalle.interesados}">
                                    <div class="alert alert-warning ">
                                        <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="registroEntrada.interesado"/></strong>
                                    </div>
                                </c:if>
                                <c:if test="${not empty preRegistro.registroDetalle.interesados}">
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
                                        <c:forEach var="interesado" items="${preRegistro.registroDetalle.interesados}">
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