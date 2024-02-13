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
                        <c:import url="../modulos/migadepan.jsp"/>
                        <li><a href="<c:url value="/integracion/list/0"/>"> <i class="fa fa-gears"></i> <spring:message code="integracion.integraciones"/></a></li>
                        <li class="active"><i class="fa fa-gears"></i> <spring:message code="integracion.integraciones"/> de ${busqueda.texto}</li>
                    </ol>
                </div>
            </div><!-- /.row -->

            <c:import url="../modulos/mensajes.jsp"/>

            <div class="row">
                <div class="col-xs-12">

                    <ul class="nav nav-tabs" role="tablist">
                        <li class="active"><a href="javascript:void(0);"> <i class="fa fa-file-o"></i> Integraciones de ${busqueda.texto}</a></li>
                    </ul>

                    <div class="tab-content">

                        <div class="panel-body">

                            <c:if test="${empty integraciones}">
                                <div class="alert alert-grey alert-dismissable">
                                    <spring:message code="regweb.listado.vacio"/> <strong><spring:message code="integracion.integracion"/></strong>
                                </div>
                            </c:if>

                            <c:if test="${not empty integraciones}">

                                <div class="alert-grey">
                                    <c:if test="${fn:length(integraciones) == 1}">
                                        <spring:message code="regweb.resultado"/>
                                        <strong>${fn:length(integraciones)}</strong> <spring:message code="integracion.integracion"/>
                                    </c:if>
                                    <c:if test="${fn:length(integraciones) > 1}">
                                        <spring:message code="regweb.resultados"/>
                                        <strong>${fn:length(integraciones)}</strong> <spring:message code="integracion.integraciones"/>
                                    </c:if>

                                </div>

                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped tablesorter">
                                        <colgroup>
                                            <col width="140">
                                            <col width="80">
                                            <col width="140">
                                            <col width="150">
                                            <col width="80">
                                            <col width="70">
                                            <col width="150">
                                            <col width="60">
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th><spring:message code="integracion.fecha"/></th>
                                                <th class="center"><spring:message code="integracion.tipo"/></th>
                                                <th><spring:message code="registroEntrada.numeroRegistro"/></th>
                                                <th><spring:message code="integracion.descripcion"/></th>
                                                <th><spring:message code="integracion.tiempo"/></th>
                                                <th class="center"><spring:message code="integracion.estado"/></th>
                                                <th><spring:message code="integracion.error"/></th>
                                                <th class="center"><spring:message code="regweb.acciones"/></th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                        <c:forEach var="integracion" items="${integraciones}">
                                            <tr>
                                                <td><fmt:formatDate value="${integracion.fecha}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                                <td class="center"><span class="label label-warning"><spring:message code="integracion.tipo.${integracion.tipo}" /></span> </td>
                                                <td>
                                                    <c:if test="${integracion.tipo == RegwebConstantes.INTEGRACION_SIR}">
                                                        <a href="<c:url value="/sir/${integracion.numRegFormat}/detalle"/>" target="_blank">${integracion.numRegFormat}</a>
                                                    </c:if>
                                                    <c:if test="${integracion.tipo != RegwebConstantes.INTEGRACION_SIR}">
                                                        ${integracion.numRegFormat}
                                                    </c:if>
                                                </td>
                                                <td>${integracion.descripcion}</td>
                                                <td>${integracion.tiempoFormateado}</td>
                                                <td class="center">
                                                    <c:if test="${integracion.estado == 0}"><span class="label label-success"><span class="fa fa-check"></span>  Ok</span></c:if>
                                                    <c:if test="${integracion.estado == 1}"><span class="label label-danger"><span class="fa fa-warning"></span> Error</span></c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${fn:length(integracion.error) <= 50}">
                                                        ${integracion.error}
                                                    </c:if>
                                                    <c:if test="${fn:length(integracion.error) > 50}">
                                                        ${integracion.errorCorto}
                                                    </c:if>
                                                </td>
                                                <td class="center">
                                                    <a class="btn btn-warning btn-sm" data-toggle="modal" role="button" href="#infoIntegracion" onclick="infoIntegracion('${integracion.id}')" title="<spring:message code="regweb.info"/>"><span class="fa fa-info-circle"></span></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        </tbody>
                                    </table>

                                </div>
                            </c:if>
                        </div>
                    </div>

                </div>
            </div>


        </div>
    </div> <!-- /container -->


    <div id="infoIntegracion" class="modal fade">
        <div class="modal-dialog modal-lg">

            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="limpiarIntegracion()">x
                    </button>
                    <h4 class="modal-title"><spring:message code="integracion.informacion"/></h4>
                </div>
                <div class="modal-body">

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.fecha"/>:</label>
                        </div>
                        <div class="col-xs-10" id="fecha"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.tipo"/>:</label>
                        </div>
                        <div class="col-xs-10" id="tipo"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.descripcion"/>:</label>
                        </div>
                        <div class="col-xs-10" id="descripcionIntegracion"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.tiempo"/>:</label>
                        </div>
                        <div class="col-xs-10" id="tiempo"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.estado"/>:</label>
                        </div>
                        <div class="col-xs-10" id="estadoIntegracion"></div>
                    </div>

                    <div class="form-group col-xs-12">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.peticion"/>:</label>
                        </div>
                        <div class="col-xs-10" id="peticion"></div>
                    </div>

                    <div class="form-group col-xs-12" id="errorBox">
                        <div class="col-xs-2 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.error"/>:</label>
                        </div>
                        <div class="col-xs-10" id="error"></div>
                    </div>

                    <div class="form-group col-xs-12" id="excepcionBox">
                        <div class="col-xs-12 pull-left etiqueta_regweb control-label">
                            <label><spring:message code="integracion.excepcion"/>:</label>
                        </div>

                            <pre id="excepcion" class=".pre-scrollable"></pre>

                    </div>



                    <div class="clearfix"></div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-sm" data-dismiss="modal" aria-hidden="true" onclick="limpiarIntegracion()">
                        <spring:message code="regweb.cerrar"/>
                    </button>
                </div>
            </div>
        </div>

    </div>

    <c:import url="../modulos/pie.jsp"/>

    <script type="text/javascript" src="<c:url value="/js/integracion.js"/>"></script>

<script type="text/javascript">

    var urlobtenerIntegracion = '<c:url value="/rest/obtenerIntegracion"/>';

    var tradsIntegracion = [];
    tradsIntegracion['integracion.tipo.0'] = "<spring:message code='integracion.tipo.0' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.1'] = "<spring:message code='integracion.tipo.1' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.2'] = "<spring:message code='integracion.tipo.2' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.3'] = "<spring:message code='integracion.tipo.3' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.4'] = "<spring:message code='integracion.tipo.4' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.5'] = "<spring:message code='integracion.tipo.5' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.6'] = "<spring:message code='integracion.tipo.6' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.7'] = "<spring:message code='integracion.tipo.7' javaScriptEscape='true' />";
    tradsIntegracion['integracion.tipo.8'] = "<spring:message code='integracion.tipo.8' javaScriptEscape='true' />";

</script>

</body>
</html>