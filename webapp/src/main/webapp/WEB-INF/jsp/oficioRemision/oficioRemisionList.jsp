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
                    <li><a <c:if test="${oficinaActiva.sirEnvio || oficinaActiva.sirRecepcion}">class="azul"</c:if> href="<c:url value="/inici"/>"><i class="fa fa-home"></i> ${oficinaActiva.denominacion}</a></li>
                    <li class="active"><i class="fa fa-list-ul"></i> <strong><spring:message code="oficioRemision.oficiosRemision"/></strong></li>
                    <%--Importamos el menú de avisos--%>
                    <c:import url="/avisos"/>
                </ol>
            </div>
        </div><!-- /.row -->

        <c:import url="../modulos/mensajes.jsp"/>

        <!-- BUSCADOR -->

        <div class="row">

            <div class="col-xs-12">

                <div class="panel panel-success">
                
                <c:if test="${(not empty librosConsulta) && (not empty oficioRemisionBusqueda)}">

                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-search"></i> <strong><spring:message code="oficioRemision.buscador"/></strong> </h3>
                    </div>

                    <form:form modelAttribute="oficioRemisionBusqueda" method="post" cssClass="form-horizontal">
                        <form:hidden path="pageNumber"/>

                        <div class="panel-body">

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="tipoOficioRemision" rel="ayuda" data-content="<spring:message code="registro.ayuda.tipo.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.tipo"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="tipoOficioRemision" cssClass="chosen-select">
                                            <form:option value="0">...</form:option>
                                            <c:forEach items="${tiposOficioRemision}" var="tipo">
                                                <form:option value="${tipo}"><spring:message code="oficioRemision.tipo.${tipo}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="destinoOficioRemision" rel="ayuda" data-content="<spring:message code="registro.ayuda.destinoOficio.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.destino"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="destinoOficioRemision" cssClass="chosen-select">
                                            <form:option value="">...</form:option>
                                            <c:forEach items="${destinosOficioRemision}" var="destino">
                                                <form:option value="${destino}"><spring:message code="oficioRemision.destino.${destino}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="oficioRemision.libro.id" rel="ayuda" data-content="<spring:message code="registro.ayuda.libroOficio.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.libro"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="oficioRemision.libro.id" items="${librosConsulta}" itemValue="id" itemLabel="nombreCompleto" cssClass="chosen-select"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="estadoOficioRemision" rel="ayuda" data-content="<spring:message code="registro.ayuda.estadoOficio.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.estado"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="estadoOficioRemision" cssClass="chosen-select">
                                            <form:option value="">...</form:option>
                                            <c:forEach items="${estadosOficioRemision}" var="estado">
                                                <form:option value="${estado}"><spring:message code="oficioRemision.estado.${estado}" /></form:option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="oficioRemision.numeroOficio" rel="ayuda" data-content="<spring:message code="registro.ayuda.numeroOficio.busqueda"/>" data-toggle="popover"><spring:message code="oficioRemision.numeroOficio"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:input path="oficioRemision.numeroOficio" cssClass="form-control" maxlength="10"/>
                                    </div>
                                </div>
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="usuario" rel="ayuda" data-content="<spring:message code="registro.ayuda.usuarioOficio.busqueda"/>" data-toggle="popover"><spring:message code="usuario.usuario"/></label>
                                    </div>
                                    <div class="col-xs-8">
                                        <form:select path="usuario" class="chosen-select">
                                            <form:option value="">...</form:option>
                                            <c:forEach items="${usuariosEntidad}" var="usuarioEntidad">
                                                <option value="${usuarioEntidad.usuario.identificador}" <c:if test="${oficioRemisionBusqueda.usuario == usuarioEntidad.usuario.identificador}">selected="selected"</c:if>>${usuarioEntidad.usuario.identificador}</option>
                                            </c:forEach>
                                        </form:select>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-12">
                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaInicio" rel="ayuda" data-content="<spring:message code="registro.ayuda.inicioOficio.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechainicio"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaInicio">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaInicio" cssClass="form-control" maxlength="10"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-xs-6 espaiLinies">
                                    <div class="col-xs-4 pull-left etiqueta_regweb">
                                        <label for="fechaFin" rel="ayuda" data-content="<spring:message code="registro.ayuda.finOficio.busqueda"/>" data-toggle="popover"><spring:message code="regweb.fechafin"/></label>
                                    </div>
                                    <div class="col-xs-8" id="fechaFin">
                                        <div class="input-group date no-pad-right">
                                            <form:input path="fechaFin" cssClass="form-control" maxlength="10"/>
                                            <span class="input-group-addon"><span class="fa fa-calendar"></span></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group col-xs-12">
                                <button type="submit" class="btn btn-warning btn-sm"><spring:message code="regweb.buscar"/></button>
                            </div>
                    </form:form>

                </c:if>

                    <c:if test="${paginacion != null}">

                        <div class="row">
                            <div class="col-xs-12">

                                <c:if test="${empty paginacion.listado}">
                                    <div class="alert alert-grey alert-dismissable">
                                        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                        <spring:message code="regweb.busqueda.vacio"/> <strong><spring:message code="oficioRemision.oficioRemision"/></strong>
                                    </div>
                                </c:if>

                                <c:if test="${not empty paginacion.listado}">

                                    <div class="alert-grey">
                                        <c:if test="${paginacion.totalResults == 1}">
                                            <spring:message code="regweb.resultado"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.oficioRemision"/>
                                        </c:if>
                                        <c:if test="${paginacion.totalResults > 1}">
                                            <spring:message code="regweb.resultados"/> <strong>${paginacion.totalResults}</strong> <spring:message code="oficioRemision.oficiosRemision"/>
                                        </c:if>

                                        <p class="pull-right"><spring:message code="regweb.pagina"/> <strong>${paginacion.currentIndex}</strong> de ${paginacion.totalPages}</p>
                                    </div>

                                    <div class="table-responsive">

                                        <table class="table table-bordered table-hover table-striped tablesorter">
                                            <colgroup>
                                                <col width="80">
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
                                                    <th><spring:message code="oficioRemision.numeroOficio"/></th>
                                                    <th><spring:message code="oficioRemision.fecha"/></th>
                                                    <th><spring:message code="oficioRemision.oficina"/></th>
                                                    <th><spring:message code="oficioRemision.organismoDestino"/></th>
                                                    <th><spring:message code="oficioRemision.destino"/></th>
                                                    <th><spring:message code="oficioRemision.tipo"/></th>
                                                    <th><spring:message code="oficioRemision.estado"/></th>
                                                    <th class="center"><spring:message code="regweb.acciones"/></th>
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <c:forEach var="oficioRemision" items="${paginacion.listado}" varStatus="status">
                                                    <tr>
                                                        <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="yyyy"/> / ${oficioRemision.numeroOficio}</td>
                                                        <td><fmt:formatDate value="${oficioRemision.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                        <td><label class="no-bold" rel="ayuda" data-content="${oficioRemision.oficina.denominacion}" data-toggle="popover">${oficioRemision.oficina.codigo}</label></td>
                                                        <td>${(empty oficioRemision.organismoDestinatario)? oficioRemision.destinoExternoDenominacion : oficioRemision.organismoDestinatario.denominacion}</td>
                                                        <td>
                                                            <span class="label label-default">
                                                                <c:if test="${not empty oficioRemision.organismoDestinatario}">
                                                                    <spring:message code="oficioRemision.interno"/>
                                                                </c:if>
                                                                <c:if test="${empty oficioRemision.organismoDestinatario}">
                                                                    <spring:message code="oficioRemision.externo"/>
                                                                </c:if>
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA}">
                                                                <span class="label label-info"><spring:message code="oficioRemision.tipo.1"/></span>
                                                            </c:if>

                                                            <c:if test="${oficioRemision.tipoOficioRemision == RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA}">
                                                                <span class="label label-danger"><spring:message code="oficioRemision.tipo.2"/></span>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_INTERNO_ENVIADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_EXTERNO_ENVIADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ACEPTADO}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ACK}"><span class="label label-success"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR}"><p rel="errorSir" data-content="<c:out value="Error: ${oficioRemision.codigoError} - ${oficioRemision.descripcionError}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></p></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ACK}"><span class="label label-warning"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR}"><p rel="errorSir" data-content="<c:out value="Error: ${oficioRemision.codigoError} -  ${oficioRemision.descripcionError}" escapeXml="true"/>" data-toggle="popover"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span>></p></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_RECHAZADO}"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_SIR_DEVUELTO}"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                            <c:if test="${oficioRemision.estado == RegwebConstantes.OFICIO_ANULADO}"><span class="label label-danger"><spring:message code="oficioRemision.estado.${oficioRemision.estado}"/></span></c:if>
                                                        </td>
                                                        <td class="center">
                                                            <a class="btn btn-info btn-sm" href="<c:url value="/oficioRemision/${oficioRemision.id}/detalle"/>" title="Detalle"><span class="fa fa-eye"></span></a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>

                                        <!-- Paginacion -->
                                        <c:import url="../modulos/paginacionBusqueda.jsp">
                                            <c:param name="entidad" value="oficioRemision"/>
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


</div>
</div> <!-- /container -->

<c:import url="../modulos/pie.jsp"/>
<script type="text/javascript">
    $("[rel='errorSir']").popover({ trigger: 'hover',placement: 'top',container:"body", html:true});
</script>

</body>
</html>