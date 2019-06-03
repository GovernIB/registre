<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/modulos/includes.jsp" %>

<!DOCTYPE html>
<html lang="ca">
<head>
    <title><spring:message code="regweb.titulo"/></title>
    <c:import url="modulos/imports.jsp"/>
</head>

    <body>

        <c:import url="modulos/menu.jsp"/>

        <div class="row-fluid container main">

            <div class="well well-white">

                <div id="mensajes"></div>

                <div class="row">

                    <div class="col-xs-12">

                        <ol class="breadcrumb">
                            <c:import url="modulos/migadepan.jsp"/>
                        </ol>

                        <c:import url="modulos/mensajes.jsp"/>

                        <c:if test="${notificacionesPendientes > 0}">
                            <div class="alert alert-danger">
                                <div class="row vertical-align">
                                    <div class="col-xs-1 text-center">
                                        <i class="fa fa-envelope fa-2x"></i>
                                    </div>
                                    <div class="col-xs-11">
                                        <spring:message code="notificacion.tiene"/> <a class="alert-link" href="<c:url value="/notificacion/list"/>"><strong>${notificacionesPendientes} <spring:message code="notificacion.notificaciones"/></strong></a> <spring:message code="notificacion.pendientes"/>
                                    </div>
                                </div>

                            </div>
                        </c:if>

                        <c:if test="${loginInfo.rolActivo.nombre == 'RWE_SUPERADMIN' || loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">
                            <c:if test="${catalogo == null}">
                                <div class="alert alert-danger">
                                    <div class="row vertical-align">
                                        <div class="col-xs-1 text-center">
                                            <i class="fa fa-times-circle fa-2x"></i>
                                        </div>
                                        <div class="col-xs-11">
                                            <strong><spring:message code="regweb.aviso"/>: </strong> <spring:message code="catalogoDir3.catalogo.vacio"/>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:if>

                        <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN' && loginInfo.entidadActiva == null}">

                            <div class="alert alert-danger">
                                <div class="row vertical-align">
                                    <div class="col-xs-1 text-center">
                                        <i class="fa fa-times-circle fa-2x"></i>
                                    </div>
                                    <div class="col-xs-11">
                                        <strong><spring:message code="regweb.aviso"/>: </strong> <spring:message code="aviso.entidadActiva"/>
                                    </div>
                                </div>
                            </div>

                        </c:if>

                    </div><!-- /.col-xs-12 -->

                    <!-- Afegim els panels i després els estils els agrupen en 2 columnes -->
                    <div class="col-xs-12">

                        <div class="columnesInici">

                            <%--DASHBOARD ADMINISTRADOR ENTIDAD--%>

                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_ADMIN'}">

                                <%--INCIDENCIAS INTEGRACIONES--%>
                                <c:if test="${not empty incidencias}">
                                    <div class="col-xs-6 filas">

                                        <div class="panel panel-warning">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-book"></i> <strong><spring:message code="integracion.incidencias"/></strong> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="integracion.fecha"/></th>
                                                            <th><spring:message code="integracion.tipo"/></th>
                                                            <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                            <th><spring:message code="integracion.descripcion"/></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach var="incidencia" items="${incidencias}">
                                                            <tr>
                                                                <td><fmt:formatDate value="${incidencia.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                                <td><span class="label label-danger"><spring:message code="integracion.tipo.${incidencia.tipo}"/></span></td>
                                                                <td><a href="javascript:void(0);" onclick="buscarIntegraciones('${incidencia.numRegFormat}')">${incidencia.numRegFormat}</a> </td>
                                                                <td>${incidencia.descripcion}</td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>

                                                    <form:form modelAttribute="integracion" action="${pageContext.request.contextPath}/integracion/busqueda" method="post" cssClass="form-horizontal" target="_blank">
                                                        <form:hidden path="texto"/>
                                                    </form:form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <%--Sincronización Directorio--%>
                                <c:if test="${not empty descargaUnidad}">
                                    <div class="col-xs-6 filas">

                                        <div class="panel panel-warning">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-home"></i> <strong><spring:message code="organismo.organigrama.ultimaSincro"/></strong> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <thead>
                                                            <tr>
                                                                <th><spring:message code="organismo.organismos"/></th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <c:if test="${not empty descargaUnidad}">
                                                                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${descargaUnidad.fechaImportacion}" />
                                                                    </c:if>
                                                                    <c:if test="${empty descargaUnidad}"><spring:message code="catalogoDir3.sincronizar.vacio"/></c:if>
                                                                </td>
                                                            </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>


                            </c:if>

                            <%--DASHBOARD USUARIO--%>

                            <c:if test="${loginInfo.rolActivo.nombre == 'RWE_USUARI'}">

                                <%--REGISTROS SIR PENDIENTES DE PROCESAR--%>
                                <c:if test="${not empty pendientesProcesarSir}">
                                    <div class="col-xs-6 filas">

                                        <div id="pendientesProc" class="panel panel-primary">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-refresh"></i> <a href="<c:url value="/registroSir/pendientesProcesar/list"/>" class="primary"><strong><spring:message code="registroSir.pendientesProcesar.inicio"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="oficioRemision.origen"/></th>
                                                            <th><spring:message code="regweb.recibido"/></th>
                                                            <th><spring:message code="registroSir.extracto"/></th>
                                                            <th>Doc</th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroSir" items="${pendientesProcesarSir}">
                                                            <tr>
                                                                <td>${registroSir.decodificacionEntidadRegistralOrigen}</td>
                                                                <td><fmt:formatDate value="${registroSir.fechaRecepcion}" pattern="dd/MM/yyyy"/></td>
                                                                <td>
                                                                    <c:if test="${fn:length(registroSir.resumen) <= 40}">
                                                                        ${registroSir.resumen}
                                                                    </c:if>

                                                                    <c:if test="${fn:length(registroSir.resumen) > 40}">
                                                                        <p rel="popupArriba" data-content="${registroSir.resumen}" data-toggle="popover">${registroSir.resumenCorto}</p>
                                                                    </c:if>

                                                                </td>
                                                                <td class="center">
                                                                    <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                                                        <i class="fa fa-print text-verd" title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
                                                                    </c:if>
                                                                    <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                                                        <i class="fa fa-file-text text-vermell" title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
                                                                    </c:if>
                                                                    <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                                                        <i class="fa fa-clipboard text-taronja" title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
                                                                    </c:if>
                                                                </td>
                                                                <td class="center">
                                                                    <a class="btn btn-primary btn-sm" href="<c:url value="/registroSir/${registroSir.id}/detalle"/>" title="<spring:message code="registroSir.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                        </tbody>
                                                    </table>

                                                </div>

                                            </div>
                                        </div>

                                    </div>
                                </c:if>

                                <%--REGISTROS DE ENTRADA PENDIENTES DE DISTRIBUIR--%>
                                <c:if test="${not empty pendientesDistribuir}">
                                    <div class="col-xs-6 filas">
                                        <div id="pendientesDistribuir" class="panel panel-info">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-sign-out"></i> <a href="<c:url value="/registroEntrada/pendientesDistribuirSir/list/1"/>" class="primary"><strong><spring:message code="registroEntrada.pendientesDistribuir.sir"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col width="80">
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="regweb.numero"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroEntrada" items="${pendientesDistribuir}" varStatus="status">
                                                            <tr>
                                                                <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${registroEntrada.registroDetalle.extracto}</td>
                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <%--Registros de Entrada Rechazados o Reenviados--%>
                                <c:if test="${not empty entradasRechazadosReenviados}">
                                    <div class="col-xs-6 filas">

                                        <div id="entradasRechazadosReenviados" class="panel panel-primary">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-warning"></i> <a href="<c:url value="/registroEntrada/pendientesSir/list/1"/>" class="primary"><strong><spring:message code="registroEntrada.rechazados.inicio"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="registroEntrada.fechaRegistro"/></th>
                                                            <th><spring:message code="registroSir.oficinaDestino"/></th>
                                                            <th><spring:message code="registroEntrada.estado"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroEntrada" items="${entradasRechazadosReenviados}" varStatus="status">
                                                            <tr>
                                                                <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${registroEntrada.registroDetalle.decodificacionEntidadRegistralDestino}</td>
                                                                <td>
                                                                    <c:import url="registro/estadosRegistro.jsp">
                                                                        <c:param name="estado" value="${registroEntrada.estado}"/>
                                                                        <c:param name="decodificacionTipoAnotacion" value="${registroEntrada.registroDetalle.decodificacionTipoAnotacion}"/>
                                                                    </c:import>
                                                                </td>

                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <%--Registros de Salida Rechazados o Reenviados--%>
                                <c:if test="${not empty salidasRechazadasReenviadas}">
                                    <div class="col-xs-6 filas">

                                        <div id="salidasRechazadosReenviados" class="panel panel-primary">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-warning"></i> <a href="<c:url value="/registroSalida/pendientesSir/list/1"/>" class="primary"><strong><spring:message code="registroSalida.rechazados.inicio"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="registroSalida.fechaRegistro"/></th>
                                                            <th><spring:message code="registroSir.oficinaDestino"/></th>
                                                            <th><spring:message code="registroSalida.estado"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroSalida" items="${salidasRechazadasReenviadas}" varStatus="status">
                                                            <tr>
                                                                <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${registroSalida.registroDetalle.decodificacionEntidadRegistralDestino}</td>
                                                                <td>
                                                                    <c:import url="registro/estadosRegistro.jsp">
                                                                        <c:param name="estado" value="${registroSalida.estado}"/>
                                                                        <c:param name="decodificacionTipoAnotacion" value="${registroSalida.registroDetalle.decodificacionTipoAnotacion}"/>
                                                                    </c:import>
                                                                </td>

                                                                <td class="center">
                                                                    <a class="btn btn-danger btn-sm" href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>


                                <%--REGISTROS DE ENTRADA PENDIENTES DE VISAR--%>
                                <%--<c:set var="avisos" value="${avisos+1}"/>
                                    <div class="col-xs-6 centrat">
                                        <div class="panel panel-primary">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.pendientesVisar"/></strong> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col width="80">
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="regweb.numero"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                            <th><spring:message code="registroEntrada.usuario"/></th>
                                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                                            <th class="center"><spring:message code="regweb3.acciones"/></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroEntrada" items="${pendientesVisar}" varStatus="status">
                                                            <tr>
                                                                <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${registroEntrada.libro}</td>
                                                                <td>${registroEntrada.usuario}</td>
                                                                <td>${registroEntrada.extracto}</td>
                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                --%>

                                <%--OFICIOS PENDIENTES DE LLEGADA--%>
                                <c:if test="${not empty oficiosPendientesLlegada}">
                                    <div class="col-xs-6 filas">

                                        <div id="pendientesLleg" class="panel panel-success">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-mail-reply"></i> <a href="<c:url value="/oficioRemision/pendientesLlegada/list"/>" class="success"><strong><spring:message code="oficioRemision.pendientesLlegada.ultimos"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="regweb.fecha"/></th>
                                                            <th><spring:message code="oficioRemision.origen"/></th>
                                                            <th><spring:message code="oficioRemision.destino"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="oficioRemision" items="${oficiosPendientesLlegada}" end="5">
                                                            <tr>
                                                                <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${oficioRemision.oficina.denominacion}</td>
                                                                <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                                <td class="center">
                                                                    <a class="btn btn-success btn-sm" href="<c:url value="/oficioRemision/${oficioRemision.id}/aceptar"/>" title="<spring:message code="oficioRemision.aceptar"/>"><span class="fa fa-check"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                        </tbody>
                                                    </table>

                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                <%--OFICIOS DE ENTRADA PENDIENTES DE REMISIÓN--%>
                                <c:if test="${not empty organismosOficioRemisionEntrada}">
                                    <div class="col-xs-6 filas">

                                        <div id="pendientesRemEnt" class="panel panel-success">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-mail-forward"></i> <a href="<c:url value="/oficioRemision/entradasPendientesRemision"/>" class="success"><strong><spring:message code="registroEntrada.oficiosRemision"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="organismo.destino"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="organismo" items="${organismosOficioRemisionEntrada}">
                                                            <tr>
                                                                <td>${organismo.denominacion}</td>
                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm" href="<c:url value="/oficioRemision/entradasPendientesRemision"/>" title="<spring:message code="oficioRemision.buscador"/>"><span class="fa fa-search"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                        </tbody>
                                                    </table>

                                                </div>

                                            </div>
                                        </div>

                                    </div>
                                </c:if>

                                <%--OFICIOS DE SALIDA PENDIENTES DE REMISIÓN--%>
                                <c:if test="${not empty organismosOficioRemisionSalida}">
                                    <div class="col-xs-6 filas">

                                        <div id="pendientesRemSal" class="panel panel-success">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-mail-forward"></i> <a href="<c:url value="/oficioRemision/salidasPendientesRemision"/>" class="success"><strong><spring:message code="registroSalida.oficiosRemision"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="organismo.destino"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="organismo" items="${organismosOficioRemisionSalida}">
                                                            <tr>
                                                                <td>${organismo.denominacion}</td>
                                                                <td class="center">
                                                                    <a class="btn btn-danger btn-sm" href="<c:url value="/oficioRemision/salidasPendientesRemision"/>" title="<spring:message code="oficioRemision.buscador"/>"><span class="fa fa-search"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>

                                                        </tbody>
                                                    </table>

                                                </div>

                                            </div>
                                        </div>

                                    </div>
                                </c:if>

                                <%--REGISTROS DE ENTRADA PENDIENTES (RESERVA)--%>
                                <c:if test="${not empty reservas}">
                                    <div class="col-xs-6 filas">
                                        <div id="reserves" class="panel panel-info">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-file-text-o"></i> <a href="<c:url value="/registroEntrada/reservas/list/1"/>" class="info"><strong><spring:message code="registroEntrada.reservas"/></strong></a> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col width="80">
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="regweb.numero"/></th>
                                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                                            <th><spring:message code="registroEntrada.libro.corto"/></th>
                                                            <th><spring:message code="registroEntrada.reserva"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                        <tbody>
                                                        <c:forEach var="registroEntrada" items="${reservas}" varStatus="status">
                                                            <tr>
                                                                <td>${registroEntrada.numeroRegistroFormateado}</td>
                                                                <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td>${registroEntrada.libro}</td>
                                                                <td>${registroEntrada.extracto}</td>
                                                                <td class="center">
                                                                    <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>


                                <%--REGISTROS RECHAZADOS
                                <c:if test="${not empty oficiosRechazados}">
                                    <div class="col-xs-6 centrat">

                                        <div id="rechazados" class="panel panel-primary">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-warning"></i> <strong><spring:message code="registroEntrada.rechazados.inicio"/></strong> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                            <tr>
                                                                <th><spring:message code="oficioRemision.fecha"/></th>
                                                                <th><spring:message code="oficioRemision.oficina"/></th>
                                                                <th><spring:message code="oficioRemision.organismoDestino"/></th>
                                                                <th><spring:message code="oficioRemision.tipo"/></th>
                                                                <th class="center"></th>
                                                            </tr>
                                                        </thead>

                                                    <tbody>
                                                    <c:forEach var="oficioRemision" items="${oficiosRechazados}" varStatus="status">
                                                        <tr>
                                                            <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td><label class="no-bold" rel="popupAbajo" data-content="${oficioRemision.oficina.codigo}" data-toggle="popover">${oficioRemision.oficina.denominacion}</label></td>
                                                            <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                            <td>
                                                                <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                    <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                                                </c:if>
                                                        <tbody>
                                                        <c:forEach var="oficioRemision" items="${oficiosRechazados}" varStatus="status">
                                                            <tr>
                                                                <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td><label class="no-bold" rel="ayuda" data-content="${oficioRemision.oficina.codigo}" data-toggle="popover">${oficioRemision.oficina.denominacion}</label></td>
                                                                <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                                <td>
                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                        <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                                                    </c:if>

                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                        <span class="label label-danger"><spring:message code="oficioRemision.tipo.2"/></span>
                                                                    </c:if>
                                                                </td>
                                                                <td class="center">
                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                        <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${oficioRemision.registrosEntrada[0].id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                    </c:if>

                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                        <a class="btn btn-danger btn-sm" href="<c:url value="/registroSalida/${oficioRemision.registrosSalida[0].id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>

                                &lt;%&ndash;REGISTROS REENVIADOS&ndash;%&gt;
                                <c:if test="${not empty oficiosReenviados}">
                                    <div class="col-xs-6 centrat">

                                        <div id="reenviados" class="panel panel-primary">
                                            <div class="panel-heading">
                                                <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="registroEntrada.reenviados.inicio"/></strong> </h3>
                                            </div>

                                            <div class="panel-body">

                                                <div class="table-responsive-inici">

                                                    <table class="table table-hover table-striped marg-bot0">
                                                        <colgroup>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col>
                                                            <col width="51">
                                                        </colgroup>
                                                        <thead>
                                                        <tr>
                                                            <th><spring:message code="oficioRemision.fecha"/></th>
                                                            <th><spring:message code="oficioRemision.oficina"/></th>
                                                            <th><spring:message code="oficioRemision.organismoDestino"/></th>
                                                            <th><spring:message code="oficioRemision.tipo"/></th>
                                                            <th class="center"></th>
                                                        </tr>
                                                        </thead>

                                                    <tbody>
                                                    <c:forEach var="oficioRemision" items="${oficiosReenviados}" varStatus="status">
                                                        <tr>
                                                            <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                            <td><label class="no-bold" rel="popupAbajo" data-content="${oficioRemision.oficina.codigo}" data-toggle="popover">${oficioRemision.oficina.denominacion}</label></td>
                                                            <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                            <td>
                                                                <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                    <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                                                </c:if>
                                                        <tbody>
                                                        <c:forEach var="oficioRemision" items="${oficiosReenviados}" varStatus="status">
                                                            <tr>
                                                                <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy"/></td>
                                                                <td><label class="no-bold" rel="ayuda" data-content="${oficioRemision.oficina.codigo}" data-toggle="popover">${oficioRemision.oficina.denominacion}</label></td>
                                                                <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                                <td>
                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                        <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                                                    </c:if>

                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                        <span class="label label-danger"><spring:message code="oficioRemision.tipo.2"/></span>
                                                                    </c:if>
                                                                </td>
                                                                <td class="center">
                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                        <a class="btn btn-info btn-sm" href="<c:url value="/registroEntrada/${oficioRemision.registrosEntrada[0].id}/detalle"/>" title="<spring:message code="registroEntrada.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                    </c:if>

                                                                    <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                        <a class="btn btn-danger btn-sm" href="<c:url value="/registroSalida/${oficioRemision.registrosSalida[0].id}/detalle"/>" title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </c:if>--%>

                            </c:if>



                        </div><!-- /.columnesInici -->
                    </div><!-- /.col-xs-12 -->

                </div><!-- /.row -->


            </div><!-- /.well-white -->
        </div> <!-- /container -->

        <c:import url="modulos/pie.jsp"/>

        <script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

        <!-- Lleva la classe filas al darrer panell dibuixat. Així funciona si només hi ha 2 panells -->
        <script type="text/javascript">
            var numItems = $('.filas').length;
            if (numItems == '2') {
                $(".filas").last().removeClass("filas");
            }
        </script>


    </body>
</html>