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

<%--CONFIGURACIONES SEGÚN EL TIPO DE REGISTRO--%>
<c:if test="${registroMigrado.tipoRegistro}">
    <c:set var="color" value="info"/>
</c:if>
<c:if test="${!registroMigrado.tipoRegistro}">
    <c:set var="color" value="danger"/>
</c:if>

<div class="row-fluid container main">

    <div class="well well-white">

        <!-- Miga de pan -->
        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <c:import url="../modulos/migadepan.jsp"/>
                    <li><a href="<c:url value="/registroMigrado/list"/>" ><i class="fa fa-list"></i> <spring:message code="registroMigrado.listado"/></a></li>
                    <c:if test="${registroMigrado.tipoRegistro}">
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroMigrado.registroMigrado.entrada"/> ${registroMigrado.numero}/${registroMigrado.ano}</li>
                    </c:if>
                    <c:if test="${!registroMigrado.tipoRegistro}">
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroMigrado.registroMigrado.salida"/> ${registroMigrado.numero}/${registroMigrado.ano}</li>
                    </c:if>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">

            <!-- Panel Lateral -->
            <div class="col-xs-4">

                <div class="panel panel-${color}">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="registroMigrado.registroMigrado"/> ${registroMigrado.numero}/${registroMigrado.ano}</strong>
                        </h3>
                    </div>

                    <div class="panel-body">

                        <%--DETALLE REGISTRO--%>
                        <dl class="detalle_registro">
                            <dt><i class="fa fa-home"></i> <spring:message code="oficina.oficina"/>: </dt> <dd> ${registroMigrado.codigoOficina}-${registroMigrado.denominacionOficina} / ${registroMigrado.codigoOficinaFisica}-${registroMigrado.denominacionOficinaFisica}</dd>
                            <dt><i class="fa fa-calendar"></i> <spring:message code="regweb.fecha"/>: </dt> <dd> <fmt:formatDate value="${registroMigrado.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            <dt><i class="fa fa-book"></i> <spring:message code="registroMigrado.numeroRegistro"/>: </dt> <dd> ${registroMigrado.numero} / ${registroMigrado.ano}</dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="registroEntrada.organismoDestino"/>: </dt> <dd>${registroMigrado.descripcionOrganismoDestinatarioEmisor}</dd>
                            <dt><i class="fa fa-bookmark"></i>
                                <c:if test="${registroMigrado.tipoRegistro}"> <%--Registro de entrada--%>
                                    <spring:message code="registroMigrado.entradaAnulada"/>:
                                </c:if>

                                <c:if test="${!registroMigrado.tipoRegistro}"><%--Registro de salida--%>
                                    <spring:message code="registroMigrado.salidaAnulada"/>:
                                </c:if>
                            </dt>
                            <dd>
                                <c:if test="${!registroMigrado.anulado}">
                                    <span class="label label-success"><spring:message code="regweb.no"/></span>
                                </c:if>
                                <c:if test="${registroMigrado.anulado}">
                                    <span class="label label-danger"><spring:message code="regweb.si"/></span>
                                </c:if>
                            </dd>
                            <c:if test="${not empty registroMigrado.fechaVisado}">
                                <dt><i class="fa fa-clock-o"></i> <spring:message code="registroMigrado.fechaVisado"/>: </dt> <dd><fmt:formatDate value="${registroMigrado.fechaVisado}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            </c:if>
                        </dl>
                    </div>

                    <%--Botonera--%>
                    <div class="panel-footer center">
                        <div class="btn-group">
                            <div class="btn-group"><button type="button" onclick="goToNewPage('<c:url value="/registroMigrado/${registroMigrado.id}/lopd"/>')" class="btn btn-warning btn-sm"><spring:message code="registroMigrado.lopd"/></button></div>
                        </div>
                    </div>
                </div>
            </div>

            <%--Panel central--%>
            <div class="col-xs-8">

                <div class="col-xs-12">
                    <div class="panel panel-${color}">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-user-circle-o"></i> <strong><spring:message code="registroMigrado.datosDocumento"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <div class="form-group col-xs-12">
                                <div class="col-xs-6"><i class="fa fa-file-o"></i> <strong><spring:message code="registroMigrado.tipoDocumento"/>:</strong> ${registroMigrado.descripcionDocumento}</div>
                                <div class="col-xs-6"><i class="fa fa-calendar"></i> <strong><spring:message code="regweb.fecha"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaDocumento}" pattern="dd/MM/yyyy"/></div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-6"><i class="fa fa-language"></i> <strong><spring:message code="registroMigrado.idioma"/>:</strong> ${registroMigrado.descripcionIdiomaDocumento}</div>
                                <div class="col-xs-6"><i class="fa fa-tag"></i>

                                    <c:if test="${registroMigrado.tipoRegistro}"> <%--Registro de entrada--%>
                                        <strong><spring:message code="registroMigrado.numeroSalida"/>:</strong>
                                    </c:if>

                                    <c:if test="${!registroMigrado.tipoRegistro}"><%--Registro de salida--%>
                                        <strong><spring:message code="registroMigrado.numeroEntrada"/>:</strong>
                                    </c:if>

                                    <c:if test="${(not empty registroMigrado.numeroEntradaSalida) && (registroMigrado.numeroEntradaSalida != 0)}">
                                        ${registroMigrado.numeroEntradaSalida} / ${registroMigrado.anoEntradaSalida}
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-6"><i class="fa fa-mail-forward"></i>
                                    <c:if test="${registroMigrado.tipoRegistro}"> <%--Registro de entrada--%>
                                        <strong><spring:message code="registroMigrado.remitente"/>:</strong>
                                    </c:if>

                                    <c:if test="${!registroMigrado.tipoRegistro}"><%--Registro de salida--%>
                                        <strong><spring:message code="registroMigrado.destinatario"/>:</strong>
                                    </c:if>

                                    ${registroMigrado.descripcionRemitenteDestinatario}
                                </div>
                                <div class="col-xs-6"><i class="fa fa-location-arrow"></i>

                                    <c:if test="${registroMigrado.tipoRegistro}"> <%--Registro de entrada--%>
                                        <strong><spring:message code="registroMigrado.procedencia"/>:</strong>
                                    </c:if>

                                    <c:if test="${!registroMigrado.tipoRegistro}"><%--Registro de salida--%>
                                        <strong><spring:message code="registroMigrado.destino"/>:</strong>
                                    </c:if>

                                    ${registroMigrado.procedenciaDestinoGeografico}</div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-12"><i class="fa fa-exchange"></i>
                                    <c:if test="${registroMigrado.tipoRegistro}"> <%--Registro de entrada--%>
                                        <strong><spring:message code="registroMigrado.organismoDestinatario"/>:</strong>
                                    </c:if>

                                    <c:if test="${!registroMigrado.tipoRegistro}"><%--Registro de salida--%>
                                        <strong><spring:message code="registroMigrado.organismoEmisor"/>:</strong>
                                    </c:if>

                                    ${registroMigrado.descripcionOrganismoDestinatarioEmisor}
                                </div>

                            </div>

                        </div>
                    </div>

                    <div class="panel panel-${color}">

                        <div class="panel-heading">
                            <h3 class="panel-title"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroMigrado.datosExtracto"/></strong></h3>
                        </div>

                        <div class="panel-body">
                            <div class="form-group col-xs-12">
                                <div class="col-xs-12"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroMigrado.extracto"/>:</strong> ${registroMigrado.extracto}</div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-4"><i class="fa fa-save"></i> <strong><spring:message code="registroMigrado.numeroDisquet"/>:</strong> ${registroMigrado.numeroDisquet}</div>
                                <div class="col-xs-4"><i class="fa fa-envelope-o"></i> <strong><spring:message code="registroMigrado.numeroCorreo"/>:</strong> ${registroMigrado.numeroCorreo}</div>
                                <div class="col-xs-4"><i class="fa fa-language"></i> <strong><spring:message code="registroMigrado.idioma"/>:</strong> ${registroMigrado.nombreIdiomaExtracto}</div>
                            </div>

                            <c:if test="${not empty registroMigrado.emailRemitente}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-at"></i> <strong><spring:message code="registroMigrado.emailRemitente"/>:</strong> ${registroMigrado.emailRemitente}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty registroMigrado.infoAdicional}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-info-circle"></i> <strong><spring:message code="registroMigrado.infoAdicional"/>:</strong> ${registroMigrado.infoAdicional}</div>
                                </div>
                            </c:if>
                            <c:if test="${not empty registroMigrado.otros}">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-info-circle"></i> <strong><spring:message code="registroMigrado.otros"/>:</strong> ${registroMigrado.otros}</div>
                                </div>
                            </c:if>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

<c:import url="../modulos/pie.jsp"/>

</body>
</html>