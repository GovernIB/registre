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
                    <c:import url="../modulos/migadepan.jsp">
                        <c:param name="avisos" value="false"/> <%--Importamos el menú de avisos--%>
                    </c:import>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="registroSir.registrosSir"/></strong></li>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-primary">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i><strong> <spring:message code="registroSir.buscador.recibidos"/> ${loginInfo.oficinaActiva.denominacion}</strong> </h3>
                    </div>

                    <form:form modelAttribute="registroSirBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroSir.identificadorIntercambio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.idSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.identificadorIntercambio"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="registroSir.identificadorIntercambio" cssClass="form-control"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="registroSir.numeroRegistro" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.numeroRegistro.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.numeroRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="registroSir.numeroRegistro" cssClass="form-control"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="estado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.estadoSir.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.estado"/></label>
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

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="estado" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.tipoRegistro.busqueda"/>" data-toggle="popover"><spring:message code="registroSir.tipoRegistro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="registroSir.tipoRegistro" cssClass="chosen-select">
                                            <form:option value="" label="..."/>
                                            <c:forEach var="tipo" items="${tipos}">
                                                <form:option value="${tipo}"><spring:message code="registroSir.tipoRegistro.${tipo.value}"/></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>

                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaInicio" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.inicioEnviadoSir.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechainicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaInicio" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaInicio"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaFin" rel="popupAbajo" data-content="<spring:message code="registro.ayuda.finEnviadoSir.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechafin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaFin" type="text" cssClass="form-control"  maxlength="10" placeholder="dd/mm/yyyy" name="fechaFin"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
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

                                        <%--Select de "Ir a página"--%>
                                        <c:import url="../modulos/paginas.jsp"/>
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
                                                <th><spring:message code="registroSir.oficinaOrigen"/></th>
                                                <th><spring:message code="regweb.recibido"/></th>
                                                <th><spring:message code="registroSir.identificadorIntercambio"/></th>
                                                <th><spring:message code="registroSir.numeroRegistro"/></th>
                                                <th class="center"><spring:message code="regweb.tipo"/></th>
                                                <th><spring:message code="registroSir.extracto"/></th>
                                                <th class="center"><spring:message code="registroSir.estado"/></th>
                                                <th>Doc</th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                            </thead>

                                            <tbody>
                                            <c:forEach var="registroSir" items="${paginacion.listado}" varStatus="status">
                                                <c:set var="registroSir" value="${registroSir}" scope="request"/>
                                                <tr>
                                                    <td>${registroSir.decodificacionEntidadRegistralOrigen}</td>
                                                    <td><fmt:formatDate value="${registroSir.fechaRecepcion}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                    <td>
                                                    <c:choose>
                                                    	<c:when test="${not empty registroSir.identificadorIntercambio}">${registroSir.identificadorIntercambio}</c:when>
                                                        <c:otherwise><span class="label label-danger"><spring:message code="regweb.registre.id.pendent"/></span></c:otherwise>
													</c:choose>
                                                    </td>
                                                    <td> ${registroSir.numeroRegistro}</td>
                                                    <td class="center">
                                                        <c:if test="${registroSir.tipoRegistro == 'ENTRADA'}">
                                                            <span class="label label-info"><spring:message code="registroSir.entrada"/></span>
                                                        </c:if>

                                                        <c:if test="${registroSir.tipoRegistro == 'SALIDA'}">
                                                            <span class="label label-danger"><spring:message code="registroSir.salida"/></span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${fn:length(registroSir.resumen) <= 40}">
                                                            ${registroSir.resumen}
                                                        </c:if>

                                                        <c:if test="${fn:length(registroSir.resumen) > 40}">
                                                            <p rel="popupArriba" data-content="${registroSir.resumen}" data-toggle="popover">${registroSir.resumenCorto}</p>
                                                        </c:if>
                                                    </td>
                                                    <td class="center">
                                                        <c:import url="estadosRegistroSir.jsp" />
                                                    </td>
                                                    <td class="center">
                                                        <c:if test="${registroSir.documentacionFisica == RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC}">
                                                            <i class="fa fa-print text-verd"  title="<spring:message code="tipoDocumentacionFisica.${registroSir.documentacionFisica}"/>"></i>
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

</body>
</html>