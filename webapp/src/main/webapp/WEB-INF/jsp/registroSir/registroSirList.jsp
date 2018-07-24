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

        <div class="row">
            <div class="col-xs-12">
                <ol class="breadcrumb">
                    <li><a <c:if test="${loginInfo.oficinaActiva.sirEnvio || loginInfo.oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${loginInfo.oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSir.registrosSir"/></strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-primary">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong> <spring:message code="registroSir.buscador.recibidos"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="registroSirBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroSir.identificadorIntercambio" rel="ayuda" data-content="<spring:message code="registro.ayuda.idSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.identificadorIntercambio"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="registroSir.identificadorIntercambio" cssClass="form-control"/>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="estado" rel="ayuda" data-content="<spring:message code="registro.ayuda.estadoSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.estado"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="estado" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach var="estado" items="${estados}">
                                                <form:option value="${estado.value}"><spring:message code="registroSir.estado.${estado.value}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroSir.resumen" rel="ayuda" data-content="<spring:message code="registro.ayuda.extractoSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.extracto"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="registroSir.resumen" cssClass="form-control"/>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="anyo" rel="ayuda" data-content="<spring:message code="registro.ayuda.anyoSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.anyRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="anyo" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach items="${anys}" var="anyo">
                                                <form:option value="${anyo}">${anyo}</form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>
                    </form:form>

                    <c:if test="${paginacion != null}">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="registroSir.registroSir"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSir.registroSir"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="registroSir.registroSir"/>
                                        </c:if>

                                        <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                    </div>

                                    <div class="table-responsive">

                                        <table class="table table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col>
                                                <col width="51">
                                            </colgroup>
                                            <thead>
                                            <tr>
                                                <th><spring:message code="registroSir.identificadorIntercambio"/></th>
                                                <th><spring:message code="regweb.fecha"/></th>
                                                <th><spring:message code="regweb.tipo"/></th>
                                                <th><spring:message code="registroSir.oficinaOrigen"/></th>
                                                <th><spring:message code="registroSir.oficinaDestino"/></th>
                                                <th><spring:message code="registroSir.estado"/></th>
                                                <th><spring:message code="registroSir.extracto"/></th>
                                                <th>Doc</th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroSir" items="${paginacion.listado}" varStatus="status">
                                                <tr>
                                                    <td> ${registroSir.identificadorIntercambio}</td>
                                                    <td><fmt:formatDate value="${registroSir.fechaRegistro}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                    <td class="center">
                                                        <c:if test="${registroSir.tipoRegistro == 'ENTRADA'}">
                                                            <span class="label label-info"><spring:message code="registroSir.entrada"/></span>
                                                        </c:if>

                                                        <c:if test="${registroSir.tipoRegistro == 'SALIDA'}">
                                                            <span class="label label-danger"><spring:message code="registroSir.salida"/></span>
                                                        </c:if>
                                                    </td>
                                                    <td>${registroSir.decodificacionEntidadRegistralOrigen}</td>
                                                    <td>${registroSir.decodificacionEntidadRegistralDestino}</td>
                                                    <td class="center">
                                                        <c:if test="${registroSir.estado == 'RECIBIDO' || registroSir.estado == 'REENVIADO' || registroSir.estado == 'RECHAZADO'}">
                                                            <span class="label label-warning"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${registroSir.estado == 'ACEPTADO' || registroSir.estado == 'REENVIADO_Y_ACK' || registroSir.estado == 'RECHAZADO_Y_ACK'}">
                                                            <span class="label label-success"><spring:message code="registroSir.estado.${registroSir.estado}" /></span>
                                                        </c:if>

                                                        <c:if test="${registroSir.estado == 'REENVIADO_Y_ERROR' || registroSir.estado == 'RECHAZADO_Y_ERROR'}">
                                                            <p rel="errorSir" data-content="<c:out value="${registroSir.codigoError} - ${registroSir.descripcionError}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="registroSir.estado.${registroSir.estado}" /></span></p>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${fn:length(registroSir.resumen) <= 40}">
                                                            ${registroSir.resumen}
                                                        </c:if>

                                                        <c:if test="${fn:length(registroSir.resumen) > 40}">
                                                            <p rel="resumen" data-content="${registroSir.resumen}" data-toggle="popover">${registroSir.resumenCorto}</p>
                                                        </c:if>
                                                    </td>
                                                    <td class="center">
                                                        <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                                            <i class="fa fa-file-text text-verd"  title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
                                                        </c:if>
                                                        <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA}">
                                                            <i class="fa fa-file-text text-vermell" title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
                                                        </c:if>
                                                        <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA}">
                                                            <i class="fa fa-file-text text-taronja" title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
                                                        </c:if>
                                                    </td>

                                                    <td class="center">
                                                        <a class="btn btn-primary btn-sm" href="<c:url value="/registroSir/${registroSir.id}/detalle"/>" title="<spring:message code="registroSir.detalle"/>"><span class="fa fa-eye"></span></a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="registroSir"/>
                                        </c:import>

                                    </div>

                                </c:if>

                            </div>
                        </div>

                    </c:if>


                </div>
            </div>
        </div>
    </div>

    <!-- FIN BUSCADOR -->




</div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>
<script type="text/javascript">
    $("[rel='errorSir']").popover({ trigger: 'hover',placement: 'top',container:"body", html:true});
</script>

</body>
</html>