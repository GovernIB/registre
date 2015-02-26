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
                    <li><a href="<c:url value="/registroMigrado/list"/>" ><i class="fa fa-list"></i> <spring:message code="registroMigrado.listado"/></a></li>
                    <c:if test="${registroMigrado.tipoRegistro}">
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroMigrado.registroMigrado"/> ${registroMigrado.numero}-${registroMigrado.ano}-${registroMigrado.denominacionOficina}-<spring:message code="informe.entrada"/></li>
                    </c:if>
                    <c:if test="${!registroMigrado.tipoRegistro}">
                        <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="registroMigrado.registroMigrado"/> ${registroMigrado.numero}-${registroMigrado.ano}-${registroMigrado.denominacionOficina}-<spring:message code="informe.salida"/></li>
                    </c:if>
                </ol>
            </div>
        </div><!-- Fin miga de pan -->

        <div class="row">

            <div class="col-xs-12">

                <%--Registro Migrado de ENTRADA --%>
                <c:if test="${registroMigrado.tipoRegistro}">

                    <div class="panel panel-info">
                        <div class="panel-heading">
                        </div>
                        <div class="panel-body">
                            <div class="form-group col-xs-12">
                                <div class="col-xs-12"><i class="fa fa-globe"></i> <strong><spring:message code="entidad.entidad"/>:</strong> ${registroMigrado.entidad.nombre}</div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-6"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroMigrado.fechaEntrada"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaRegistro}" pattern="dd/MM/yyyy"/></div>
                                <div class="col-xs-3"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroMigrado.hora"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaRegistro}" pattern="HH:mm:ss"/></div>
                                <div class="col-xs-3"><i class="fa fa-bookmark"></i> <strong><spring:message code="registroMigrado.entradaAnulada"/>:</strong>
                                    <c:if test="${!registroMigrado.anulado}">
                                        <span class="label label-success"><spring:message code="regweb.no"/></span>
                                    </c:if>
                                    <c:if test="${registroMigrado.anulado}">
                                        <span class="label label-danger"><spring:message code="regweb.si"/></span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-6"><i class="fa fa-briefcase"></i> <strong><spring:message code="registroMigrado.oficina"/>:</strong> ${registroMigrado.codigoOficina}-${registroMigrado.denominacionOficina} / ${registroMigrado.codigoOficinaFisica}-${registroMigrado.denominacionOficinaFisica}</div>
                                <div class="col-xs-3"><i class="fa fa-asterisk"></i> <strong><spring:message code="registroMigrado.numeroRegistro"/>:</strong> ${registroMigrado.numero}/${registroMigrado.ano}</div>
                                <c:if test="${not empty registroMigrado.fechaVisado}">
                                    <div class="col-xs-3"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroMigrado.fechaVisado"/>:</strong> ${registroMigrado.fechaVisado}</div>
                                </c:if>
                            </div>
                            <div class="form-group col-xs-12">
                                <div class="col-xs-12"><i class="fa fa-gears"></i> <strong><spring:message code="registroEntrada.aplicacion"/>:</strong> <spring:message code="registroMigrado.aplicacion"/></div>
                            </div>
                        </div>

                            <div class="panel-heading-migrado">
                                <h3 class="panel-title">
                                    <strong> <spring:message code="registroMigrado.datosDocumento"/></strong>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4"><i class="fa fa-calendar"></i> <strong><spring:message code="regweb.fecha"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaDocumento}" pattern="dd/MM/yyyy"/></div>
                                    <div class="col-xs-4"><i class="fa fa-file-o"></i> <strong><spring:message code="registroMigrado.tipoDocumento"/>:</strong> ${registroMigrado.descripcionDocumento}</div>
                                    <div class="col-xs-4"><i class="fa fa-language"></i> <strong><spring:message code="registroMigrado.idioma"/>:</strong> ${registroMigrado.descripcionIdiomaDocumento}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-mail-forward"></i> <strong><spring:message code="registroMigrado.remitente"/>:</strong> ${registroMigrado.descripcionRemitenteDestinatario}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-location-arrow"></i> <strong><spring:message code="registroMigrado.procedencia"/>:</strong> ${registroMigrado.procedenciaDestinoGeografico}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4"><i class="fa fa-tag"></i> <strong><spring:message code="registroMigrado.numeroSalida"/>:</strong>
                                        <c:if test="${(not empty registroMigrado.numeroEntradaSalida) && (registroMigrado.numeroEntradaSalida != 0)}">
                                            ${registroMigrado.numeroEntradaSalida} / ${registroMigrado.anoEntradaSalida}
                                        </c:if>
                                    </div>
                                    <div class="col-xs-4"><i class="fa fa-exchange"></i> <strong><spring:message code="registroMigrado.organismoDestinatario"/>:</strong> ${registroMigrado.descripcionOrganismoDestinatarioEmisor}</div>
                                </div>
                            </div>

                            <div class="panel-heading-migrado">
                                <h3 class="panel-title">
                                    <strong> <spring:message code="registroMigrado.datosExtracto"/></strong>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4"><i class="fa fa-language"></i> <strong><spring:message code="registroMigrado.idioma"/>:</strong> ${registroMigrado.nombreIdiomaExtracto}</div>
                                    <div class="col-xs-4"><i class="fa fa-save"></i> <strong><spring:message code="registroMigrado.numeroDisquet"/>:</strong> ${registroMigrado.numeroDisquet}</div>
                                    <div class="col-xs-4"><i class="fa fa-envelope-o"></i> <strong><spring:message code="registroMigrado.numeroCorreo"/>:</strong> ${registroMigrado.numeroCorreo}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroMigrado.extracto"/>:</strong> ${registroMigrado.extracto}</div>
                                </div>
                                <c:if test="${not empty registroMigrado.emailRemitente}">
                                    <div class="form-group col-xs-12">
                                        <div class="col-xs-12"><i class="fa fa-at"></i> <strong><spring:message code="registroMigrado.emailRemitente"/>:</strong> ${registroMigrado.emailRemitente}</div>
                                    </div>
                                </c:if>
                            </div>

                    </div>

                </c:if>

                <%--Registro Migrado de SALIDA --%>
                    <c:if test="${!registroMigrado.tipoRegistro}">

                        <div class="panel panel-danger">
                            <div class="panel-heading">
                            </div>
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-globe"></i> <strong><spring:message code="entidad.entidad"/>:</strong> ${registroMigrado.entidad.nombre}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-6"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroMigrado.fechaSalida"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaRegistro}" pattern="dd/MM/yyyy"/></div>
                                    <div class="col-xs-3"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroMigrado.hora"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaRegistro}" pattern="HH:mm:ss"/></div>
                                    <div class="col-xs-3"><i class="fa fa-bookmark"></i> <strong><spring:message code="registroMigrado.salidaAnulada"/>:</strong>
                                        <c:if test="${!registroMigrado.anulado}">
                                            <span class="label label-success"><spring:message code="regweb.no"/></span>
                                        </c:if>
                                        <c:if test="${registroMigrado.anulado}">
                                            <span class="label label-danger"><spring:message code="regweb.si"/></span>
                                        </c:if>
                                    </div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-6"><i class="fa fa-briefcase"></i> <strong><spring:message code="registroMigrado.oficina"/>:</strong> ${registroMigrado.codigoOficina}-${registroMigrado.denominacionOficina} / ${registroMigrado.codigoOficinaFisica}-${registroMigrado.denominacionOficinaFisica}</div>
                                    <div class="col-xs-3"><i class="fa fa-asterisk"></i> <strong><spring:message code="registroMigrado.numeroRegistro"/>:</strong> ${registroMigrado.numero}/${registroMigrado.ano}</div>
                                    <c:if test="${not empty registroMigrado.fechaVisado}">
                                        <div class="col-xs-3"><i class="fa fa-clock-o"></i> <strong><spring:message code="registroMigrado.fechaVisado"/>:</strong> ${registroMigrado.fechaVisado}</div>
                                    </c:if>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-gears"></i> <strong><spring:message code="registroEntrada.aplicacion"/>:</strong> <spring:message code="registroMigrado.aplicacion"/></div>
                                </div>
                            </div>

                            <div class="panel-heading-migrado">
                                <h3 class="panel-title">
                                    <strong> <spring:message code="registroMigrado.datosDocumento"/></strong>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4"><i class="fa fa-calendar"></i> <strong><spring:message code="regweb.fecha"/>:</strong> <fmt:formatDate value="${registroMigrado.fechaDocumento}" pattern="dd/MM/yyyy"/></div>
                                    <div class="col-xs-4"><i class="fa fa-file-o"></i> <strong><spring:message code="registroMigrado.tipoDocumento"/>:</strong> ${registroMigrado.descripcionDocumento}</div>
                                    <div class="col-xs-4"><i class="fa fa-language"></i> <strong><spring:message code="registroMigrado.idioma"/>:</strong> ${registroMigrado.descripcionIdiomaDocumento}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-mail-forward"></i> <strong><spring:message code="registroMigrado.destinatario"/>:</strong> ${registroMigrado.descripcionRemitenteDestinatario}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-location-arrow"></i> <strong><spring:message code="registroMigrado.destino"/>:</strong> ${registroMigrado.procedenciaDestinoGeografico}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4"><i class="fa fa-tag"></i> <strong><spring:message code="registroMigrado.numeroEntrada"/>:</strong>
                                        <c:if test="${(not empty registroMigrado.numeroEntradaSalida) && (registroMigrado.numeroEntradaSalida != 0)}">
                                            ${registroMigrado.numeroEntradaSalida} / ${registroMigrado.anoEntradaSalida}
                                        </c:if>
                                    </div>
                                    <div class="col-xs-4"><i class="fa fa-exchange"></i> <strong><spring:message code="registroMigrado.organismoEmisor"/>:</strong> ${registroMigrado.descripcionOrganismoDestinatarioEmisor}</div>
                                </div>
                            </div>

                            <div class="panel-heading-migrado">
                                <h3 class="panel-title">
                                    <strong> <spring:message code="registroMigrado.datosExtracto"/></strong>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-4"><i class="fa fa-language"></i> <strong><spring:message code="registroMigrado.idioma"/>:</strong> ${registroMigrado.nombreIdiomaExtracto}</div>
                                    <div class="col-xs-4"><i class="fa fa-save"></i> <strong><spring:message code="registroMigrado.numeroDisquet"/>:</strong> ${registroMigrado.numeroDisquet}</div>
                                    <div class="col-xs-4"><i class="fa fa-envelope-o"></i> <strong><spring:message code="registroMigrado.numeroCorreo"/>:</strong> ${registroMigrado.numeroCorreo}</div>
                                </div>
                                <div class="form-group col-xs-12">
                                    <div class="col-xs-12"><i class="fa fa-file-text-o"></i> <strong><spring:message code="registroMigrado.extracto"/>:</strong> ${registroMigrado.extracto}</div>
                                </div>
                                <c:if test="${not empty registroMigrado.emailRemitente}">
                                    <div class="form-group col-xs-12">
                                        <div class="col-xs-12"><i class="fa fa-at"></i> <strong><spring:message code="registroMigrado.emailRemitente"/>:</strong> ${registroMigrado.emailRemitente}</div>
                                    </div>
                                </c:if>
                            </div>

                        </div>

                    </c:if>

            </div>

            <div class="col-xs-8 pull-right">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

        </div>

    </div>

</div>

<c:import url="../modulos/pie.jsp"/>

</body>
</html>