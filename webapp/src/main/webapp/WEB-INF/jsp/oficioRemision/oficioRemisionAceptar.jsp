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
                    <c:import url="../modulos/migadepan.jsp">
                        <c:param name="avisos" value="${loginInfo.mostrarAvisos}"/>
                    </c:import>
                    <li><a href="<c:url value="/oficioRemision/pendientesLlegada/list"/>" ><i class="fa fa-list"></i> <spring:message code="oficioRemision.pendientesLlegada"/></a></li>
                    <li class="active"><i class="fa fa-pencil-square-o"></i> <spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</li>
                </ol>
            </div>
       </div><!-- Fin miga de pan -->

        <div class="row">

            <div class="col-xs-4">

                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-file-o"></i>
                            <strong> <spring:message code="oficioRemision.oficioRemision"/> <fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</strong>
                        </h3>
                    </div>
                    <div class="panel-body">

                        <dl class="detalle_registro">
                            <dt><i class="fa fa-home"></i> <spring:message code="registroEntrada.oficina"/>: </dt> <dd> ${oficioRemision.oficina.denominacion}</dd>
                            <dt><i class="fa fa-clock-o"></i> <spring:message code="registroEntrada.fecha"/>: </dt> <dd> <fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></dd>
                            <dt><i class="fa fa-book"></i> <spring:message code="libro.libro"/>: </dt> <dd> ${oficioRemision.libro.nombre}</dd>
                            <dt><i class="fa fa-user"></i> <spring:message code="usuario.usuario"/>: </dt> <dd> ${oficioRemision.usuarioResponsable.usuario.nombreCompleto}</dd>
                            <dt><i class="fa fa-institution"></i> <spring:message code="oficioRemision.organismoDestino"/>: </dt>
                            <dd>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</dd>
                            <dt><i class="fa fa-file-text-o"></i> <spring:message code="oficioRemision.tipo"/>:</dt>
                            <dd>
                                <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                    <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                </c:if>

                                <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                    <span class="label label-danger"><spring:message code="oficioRemision.tipo.2"/></span>
                                </c:if>
                            </dd>
                            <dt><i class="fa fa-bookmark"></i> <spring:message code="oficioRemision.estado"/>: </dt>
                            <dd>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_INTERNO_ENVIADO}"><span class="label label-warning"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_EXTERNO_ENVIADO}"><span class="label label-warning"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}"><span class="label label-success"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO}"><span class="label label-warning"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK}"><span class="label label-success"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR}"><span class="label label-danger"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO}"><span class="label label-warning"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}"><span class="label label-success"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR}"><span class="label label-danger"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}"><span class="label label-danger"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}"><span class="label label-danger"></c:if>
                                <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"></c:if>
                                <spring:message code="oficioRemision.estado.${oficioRemision.estado}"/>
                                <c:if test="${not empty oficioRemision.fechaEstado && oficioRemision.estado != 0}">
                                    - <fmt:formatDate value="${oficioRemision.fechaEstado}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </c:if>
                                <c:if test="${oficioRemision.estado == 0}">
                                    - <fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                </c:if>
                                </span>
                            </dd>

                        </dl>

                    </div>
                    <div class="panel-footer">
                    <c:if test="${fn:length(modelosOficioRemision) > 0}">
                        <form:form modelAttribute="modeloOficioRemision" method="post" cssClass="form-horizontal row pad-lateral-10">
                            <div class="col-xs-12 btn-block">
                                <div class="col-xs-6 no-pad-lateral list-group-item-heading">
                                    <form:select path="idModelo" cssClass="chosen-select">
                                        <form:options items="${modelosOficioRemision}" itemValue="id" itemLabel="nombre"/>
                                    </form:select>
                                </div>
                                <div class="col-xs-6 no-pad-right list-group-item-heading">
                                    <button type="button" class="btn btn-warning btn-sm btn-block" onclick="imprimirRecibo('<c:url value="/oficioRemision/${oficioRemision.id}/imprimir/"/>')"><spring:message code="oficioRemision.descargar"/></button>
                                </div>
                            </div>
                        </form:form>
                    </c:if>

                        <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_INTERNO_ENVIADO}">
                        <button type="button" onclick="doForm('#oficioPendienteLlegadaForm')" class="btn btn-success btn-sm btn-block"><spring:message code="oficioRemision.aceptar"/></button>
                    </c:if>

                    </div>
                </div>

            </div>

            <div class="col-xs-8 col-xs-offset">
                <c:import url="../modulos/mensajes.jsp"/>
            </div>

            <%--Oficio de Remisión de Entrada--%>
            <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">

                <c:if test="${not empty oficioRemision.registrosEntrada}">
                    <div class="col-xs-8 col-xs-offset">

                        <div class="panel panel-success">

                            <div class="panel-heading">

                                <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                                        code="oficioRemision.registrosEntrada"/></strong>
                                </h3>
                            </div>

                            <div class="panel-body">

                                <div class="table-responsive sin-scroll">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="80">
                                            <col>
                                            <c:if test="${fn:length(libros) > 1}">
                                                <col>
                                            </c:if>
                                            <col width="100">
                                            <col>
                                            <col width="50">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="regweb.numero"/></th>
                                            <th><spring:message code="registroEntrada.fecha"/></th>
                                            <c:if test="${fn:length(libros) > 1}">
                                                <th><spring:message code="registroEntrada.libro.corto"/></th>
                                            </c:if>
                                            <th><spring:message code="registroEntrada.organismoDestino"/></th>
                                            <th><spring:message code="registroEntrada.extracto"/></th>
                                            <th><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                        <form:form modelAttribute="oficioPendienteLlegadaForm" method="post" cssClass="form-horizontal">

                                            <c:forEach var="registroEntrada" items="${oficioRemision.registrosEntrada}"
                                                       varStatus="status">

                                                <input type="hidden" id="oficios[${status.index}].idRegistro"
                                                       name="oficios[${status.index}].idRegistro"
                                                       value="${registroEntrada.id}"/>
                                                <tr>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="yyyy"/> / ${registroEntrada.numeroRegistro}</td>
                                                    <td><fmt:formatDate value="${registroEntrada.fecha}" pattern="dd/MM/yyyy"/></td>
                                                    <c:if test="${fn:length(libros) > 1}">
                                                        <td><form:select path="oficios[${status.index}].idLibro"
                                                                         class="chosen-select" items="${libros}"
                                                                         itemLabel="nombre" itemValue="id"/></td>
                                                    </c:if>
                                                    <c:if test="${fn:length(libros) == 1}">
                                                        <input type="hidden" id="oficios[${status.index}].idLibro"
                                                               name="oficios[${status.index}].idLibro"
                                                               value="${libros[0].id}"/>
                                                    </c:if>

                                                    <td>
                                                        <c:if test="${fn:length(organismosOficinaActiva) > 1}">
                                                            <div class="row select-destinatari">
                                                                <select id="oficios[${status.index}].idOrganismoDestinatario"
                                                                        name="oficios[${status.index}].idOrganismoDestinatario"
                                                                        class="chosen-select">
                                                                    <c:forEach items="${organismosOficinaActiva}"
                                                                               var="organismo">
                                                                        <option value="${organismo.id}"
                                                                                <c:if test="${registroEntrada.destino.id == organismo.id}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <%--<form:select path="oficios[${status.index}].idOrganismoDestinatario" class="chosen-select" items="${organismosOficinaActiva}" itemLabel="denominacion" itemValue="id"/>--%>
                                                        </c:if>

                                                        <c:if test="${fn:length(organismosOficinaActiva) == 1}">
                                                            ${registroEntrada.destino.denominacion}
                                                            <input type="hidden"
                                                                   id="oficios[${status.index}].idOrganismoDestinatario"
                                                                   name="oficios[${status.index}].idOrganismoDestinatario"
                                                                   value="${registroEntrada.destino.id}"/>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${fn:length(registroEntrada.registroDetalle.extracto) <= 40}">
                                                            <c:out value="${registroEntrada.registroDetalle.extracto}" escapeXml="true"/>
                                                        </c:if>
                                                        <c:if test="${fn:length(registroEntrada.registroDetalle.extracto) > 40}">
                                                            <p rel="popupArriba" data-content="<c:out value="${registroEntrada.registroDetalle.extracto}" escapeXml="true"/>" data-toggle="popover"><c:out value="${registroEntrada.registroDetalle.extractoCorto}" escapeXml="true"/></p>
                                                        </c:if>
                                                    </td>
                                                    <td class="center">
                                                        <a class="btn btn-info btn-sm"
                                                           href="<c:url value="/registroEntrada/${registroEntrada.id}/detalle"/>"
                                                           target="_blank"
                                                           title="<spring:message code="registroEntrada.detalle"/>"><span
                                                                class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </form:form>
                                        </tbody>
                                    </table>

                                </div>

                            </div>
                        </div>

                    </div>

                </c:if>
            </c:if>

            <%--Oficio de Remisión de Salida--%>
            <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">

                <c:if test="${not empty oficioRemision.registrosSalida}">
                    <div class="col-xs-8 col-xs-offset">

                        <div class="panel panel-success">

                            <div class="panel-heading">

                                <h3 class="panel-title"><i class="fa fa-pencil-square-o"></i> <strong><spring:message
                                        code="oficioRemision.registrosSalida"/></strong>
                                </h3>
                            </div>

                            <div class="panel-body">

                                <div class="table-responsive">

                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="80">
                                            <col>
                                            <c:if test="${fn:length(libros) > 1}">
                                                <col>
                                            </c:if>
                                            <col width="100">
                                            <col>
                                            <col>
                                            <col width="50">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th><spring:message code="registroSalida.numeroRegistro"/></th>
                                            <th><spring:message code="registroSalida.fecha"/></th>
                                            <c:if test="${fn:length(libros) > 1}">
                                                <th><spring:message code="registroSalida.libro.corto"/></th>
                                            </c:if>
                                            <th><spring:message code="registroSalida.origen"/></th>
                                            <th><spring:message code="registroSalida.extracto"/></th>
                                            <th><spring:message code="registroEntrada.interesados"/></th>
                                            <th><spring:message code="regweb.acciones"/></th>
                                        </tr>
                                        </thead>

                                        <tbody>
                                            <form:form modelAttribute="oficioPendienteLlegadaForm" method="post" cssClass="form-horizontal">

                                                <c:forEach var="registroSalida" items="${oficioRemision.registrosSalida}" varStatus="status">

                                                    <input type="hidden" id="oficios[${status.index}].idRegistro"
                                                           name="oficios[${status.index}].idRegistro"
                                                           value="${registroSalida.id}"/>
                                                    <tr>
                                                        <td><fmt:formatDate value="${registroSalida.fecha}" pattern="yyyy"/> / ${registroSalida.numeroRegistro}</td>
                                                        <td><fmt:formatDate value="${registroSalida.fecha}" pattern="dd/MM/yyyy"/></td>
                                                        <c:if test="${fn:length(libros) > 1}">
                                                            <td><form:select path="oficios[${status.index}].idLibro"
                                                                             class="chosen-select" items="${libros}"
                                                                             itemLabel="nombre" itemValue="id"/></td>
                                                        </c:if>
                                                        <c:if test="${fn:length(libros) == 1}">
                                                            <input type="hidden" id="oficios[${status.index}].idLibro"
                                                                   name="oficios[${status.index}].idLibro"
                                                                   value="${libros[0].id}"/>
                                                        </c:if>

                                                        <td>

                                                            <div class="row select-destinatari">
                                                                <select id="oficios[${status.index}].idOrganismoDestinatario"
                                                                        name="oficios[${status.index}].idOrganismoDestinatario"
                                                                        class="chosen-select">
                                                                    <c:forEach items="${organismosOficinaActiva}" var="organismo">
                                                                        <option value="${organismo.id}"
                                                                                <c:if test="${oficioRemision.organismoDestinatario.id == organismo.id}">selected="selected"</c:if>>${organismo.denominacion}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <%--<form:select path="oficios[${status.index}].idOrganismoDestinatario" class="chosen-select" items="${organismosOficinaActiva}" itemLabel="denominacion" itemValue="id"/>--%>
                                                        </td>

                                                        <td>
                                                            <c:if test="${fn:length(registroSalida.registroDetalle.extracto) <= 40}">
                                                                <c:out value="${registroSalida.registroDetalle.extracto}" escapeXml="true"/>
                                                            </c:if>
                                                            <c:if test="${fn:length(registroSalida.registroDetalle.extracto) > 40}">
                                                                <p rel="popupArriba" data-content="<c:out value="${registroSalida.registroDetalle.extracto}" escapeXml="true"/>" data-toggle="popover"><c:out value="${registroSalida.registroDetalle.extractoCorto}" escapeXml="true"/></p>
                                                            </c:if>
                                                        </td>
                                                        <td class="center"><label class="no-bold representante" rel="popupAbajo"
                                                                                  data-content="<c:out value="${registroSalida.registroDetalle.nombreInteresadosHtml}" escapeXml="true"/>"
                                                                                  data-toggle="popover">${registroSalida.registroDetalle.totalInteresados}</label>
                                                        </td>
                                                        <td class="center">
                                                            <a class="btn btn-danger btn-sm"
                                                               href="<c:url value="/registroSalida/${registroSalida.id}/detalle"/>" target="_blank"
                                                               title="<spring:message code="registroSalida.detalle"/>"><span class="fa fa-eye"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </form:form>
                                        </tbody>
                                    </table>

                                </div>

                            </div>
                        </div>

                    </div>

                </c:if>

            </c:if>

        </div><!-- /div.row-->


    </div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>


<script type="text/javascript">
    function imprimirOficio(url) {

        var idModelo = $('#id').val();

        document.location.href=url.concat(idModelo);
    }
</script>


</body>
</html>